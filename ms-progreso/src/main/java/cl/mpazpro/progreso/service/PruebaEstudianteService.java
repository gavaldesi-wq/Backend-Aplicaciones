package cl.mpazpro.progreso.service;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import cl.mpazpro.progreso.client.ActividadesCliente;
import cl.mpazpro.progreso.dto.InicioIntentoDTO;
import cl.mpazpro.progreso.dto.IntentoDTO;
import cl.mpazpro.progreso.dto.PreguntaEstudianteDTO;
import cl.mpazpro.progreso.dto.PruebaEstudianteDTO;
import cl.mpazpro.progreso.dto.PruebaPublicadaDTO;
import cl.mpazpro.progreso.model.Asignacion;
import cl.mpazpro.progreso.model.EstadoIntento;
import cl.mpazpro.progreso.model.IntentoPrueba;
import cl.mpazpro.progreso.repository.AsignacionRepository;
import cl.mpazpro.progreso.repository.IntentoPruebaRepository;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cl.mpazpro.progreso.dto.EntregaIntentoDTO;
import cl.mpazpro.progreso.dto.FeedbackDTO;
import cl.mpazpro.progreso.dto.RespuestaEnviadaDTO;
import cl.mpazpro.progreso.dto.ResultadoIntentoDTO;
import cl.mpazpro.progreso.dto.SolucionPreguntaDTO;
import cl.mpazpro.progreso.dto.SolucionPruebaDTO;
import cl.mpazpro.progreso.model.Progreso;
import cl.mpazpro.progreso.model.RespuestaEstudiante;
import cl.mpazpro.progreso.model.TipoActividad;
import cl.mpazpro.progreso.repository.ProgresoRepository;
import cl.mpazpro.progreso.repository.RespuestaEstudianteRepository;
import cl.mpazpro.progreso.security.SesionActual;
@Service
@RequiredArgsConstructor
public class PruebaEstudianteService {

    private final ActividadesCliente actividadesCliente;
    private final AsignacionRepository asignacionRepository;
    private final IntentoPruebaRepository intentoRepository;
    private final RespuestaEstudianteRepository respuestaRepository;
    private final ProgresoRepository progresoRepository;
    private final ObjectMapper objectMapper;
    private final SesionActual sesionActual;

    @Transactional(readOnly = true)
        public PruebaEstudianteDTO obtener(Long objetivoAprendizajeId) {
        long estudianteId = sesionActual.estudianteId();
        PruebaPublicadaDTO prueba = actividadesCliente
                .obtenerPruebaPublicada(objetivoAprendizajeId);
        Asignacion asignacion = buscarAsignacion(objetivoAprendizajeId, estudianteId);

        Instant desde = masTarde(
                prueba.getDisponibleDesde(),
                asignacion.getDisponibleDesde()
        );
        Instant hasta = masTemprano(
                prueba.getDisponibleHasta(),
                asignacion.getDisponibleHasta()
        );

        int limite = limiteEfectivo(
                prueba.getLimiteIntentos(),
                asignacion.getLimiteIntentosPrueba()
        );
        int usados = intentoRepository.countByPruebaIdAndEstudianteId(
                prueba.getId(),
                estudianteId
        );
        int restantes = Math.max(0, limite - usados);

        PruebaEstudianteDTO dto = new PruebaEstudianteDTO();
        dto.setPruebaId(prueba.getId());
        dto.setObjetivoAprendizajeId(objetivoAprendizajeId);
        dto.setEstado(prueba.getEstado());
        dto.setDisponibleDesde(desde);
        dto.setDisponibleHasta(hasta);
        dto.setLimiteIntentos(limite);
        dto.setIntentosUtilizados(usados);
        dto.setIntentosRestantes(restantes);
        dto.setPuedeRendir(puedeRendir(desde, hasta, restantes));
        return dto;
        }

        @Transactional
        public IntentoDTO iniciar(Long pruebaId, InicioIntentoDTO dto) {
            long estudianteId = sesionActual.estudianteId();
            return intentoRepository.findByClientEventId(dto.getClientEventId())
                    .map(existente -> reutilizar(pruebaId, estudianteId, existente))
                    .orElseGet(() -> crear(pruebaId, estudianteId, dto));
        }
        
        private IntentoDTO reutilizar(Long pruebaId, long estudianteId, IntentoPrueba existente) {
            if (!existente.getPruebaId().equals(pruebaId)
                    || !Long.valueOf(estudianteId).equals(existente.getEstudianteId())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "El evento ya fue usado en otra prueba"
                );
            }
            return aIntento(existente);
        }
        
        private IntentoDTO crear(Long pruebaId, long estudianteId, InicioIntentoDTO dto) {
            PruebaPublicadaDTO prueba = actividadesCliente.obtenerPruebaPorId(pruebaId);
            Asignacion asignacion = buscarAsignacion(prueba.getObjetivoAprendizajeId(), estudianteId);
        
            Instant desde = masTarde(
                    prueba.getDisponibleDesde(),
                    asignacion.getDisponibleDesde()
            );
            Instant hasta = masTemprano(
                    prueba.getDisponibleHasta(),
                    asignacion.getDisponibleHasta()
            );
            int limite = limiteEfectivo(
                    prueba.getLimiteIntentos(),
                    asignacion.getLimiteIntentosPrueba()
            );
            int usados = intentoRepository.countByPruebaIdAndEstudianteId(
                    pruebaId,
                    estudianteId
            );
        
            if (!puedeRendir(desde, hasta, limite - usados)) {
                if (usados >= limite) {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "No quedan intentos disponibles para esta prueba."
                    );
                }
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "La prueba esta fuera de plazo"
                );
            }
        
            IntentoPrueba intento = new IntentoPrueba();
            intento.setPruebaId(pruebaId);
            intento.setEstudianteId(estudianteId);
            intento.setAsignacion(asignacion);
            intento.setClientEventId(dto.getClientEventId());
            intento.setNumeroIntento(usados + 1);
            intento.setEstado(EstadoIntento.INICIADO);
            intento.setIniciadoEn(Instant.now());
            return aIntento(intentoRepository.save(intento));
        }
    
    private IntentoDTO aIntento(IntentoPrueba intento) {
        IntentoDTO dto = new IntentoDTO();
        dto.setId(intento.getId());
        dto.setPruebaId(intento.getPruebaId());
        dto.setNumeroIntento(intento.getNumeroIntento());
        dto.setEstado(intento.getEstado());
        dto.setIniciadoEn(intento.getIniciadoEn());
        return dto;
    }
    
    private Asignacion buscarAsignacion(Long objetivoAprendizajeId, long estudianteId) {
        List<Asignacion> asignaciones = asignacionRepository
                .findByObjetivoAprendizajeIdAndActivaTrue(objetivoAprendizajeId);
    
        return asignaciones.stream()
                .filter(asignacion -> Long.valueOf(estudianteId)
                        .equals(asignacion.getEstudianteId()))
                .findFirst()
                .or(() -> asignaciones.stream()
                        .filter(asignacion -> asignacion.getEstudianteId() == null)
                        .findFirst())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Prueba no encontrada"
                ));
    }

    private int limiteEfectivo(Integer limitePrueba, int limiteAsignacion) {
        if (limitePrueba == null) {
            return limiteAsignacion;
        }
        return Math.min(limitePrueba, limiteAsignacion);
    }

    private Instant masTarde(Instant primera, Instant segunda) {
        if (primera == null) {
            return segunda;
        }
        if (segunda == null) {
            return primera;
        }
        return primera.isAfter(segunda) ? primera : segunda;
    }

    private Instant masTemprano(Instant primera, Instant segunda) {
        if (primera == null) {
            return segunda;
        }
        if (segunda == null) {
            return primera;
        }
        return primera.isBefore(segunda) ? primera : segunda;
    }

    private boolean puedeRendir(Instant desde, Instant hasta, int restantes) {
        Instant ahora = Instant.now();
        boolean dentro = (desde == null || !ahora.isBefore(desde))
                && (hasta == null || ahora.isBefore(hasta));
        return dentro && restantes > 0;
    }

    @Transactional(readOnly = true)
    public List<PreguntaEstudianteDTO> preguntas(Long intentoId) {
        long estudianteId = sesionActual.estudianteId();
        IntentoPrueba intento = intentoRepository.findById(intentoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Intento no encontrado"
                ));
    
        if (!Long.valueOf(estudianteId).equals(intento.getEstudianteId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Intento no encontrado"
            );
        }
    
        if (intento.getEstado() != EstadoIntento.INICIADO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El intento ya fue entregado"
            );
        }
    
        return actividadesCliente.obtenerPreguntasDePrueba(intento.getPruebaId());
    }

    @Transactional
    public ResultadoIntentoDTO entregar(Long intentoId, EntregaIntentoDTO dto) {
        long estudianteId = sesionActual.estudianteId();
        IntentoPrueba intento = intentoRepository.findById(intentoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Intento no encontrado"
                ));
        if (!Long.valueOf(estudianteId).equals(intento.getEstudianteId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Intento no encontrado"
            );
        }
        if (intento.getEstado() == EstadoIntento.CALIFICADO) {
            return armarDesdeGuardado(intento);
        }
        if (intento.getEstado() != EstadoIntento.INICIADO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El intento ya fue entregado"
            );
        }
        PruebaPublicadaDTO prueba = actividadesCliente.obtenerPruebaPorId(intento.getPruebaId());
        Asignacion asignacion = buscarAsignacion(prueba.getObjetivoAprendizajeId(), estudianteId);
        Instant desde = masTarde(prueba.getDisponibleDesde(), asignacion.getDisponibleDesde());
        Instant hasta = masTemprano(prueba.getDisponibleHasta(), asignacion.getDisponibleHasta());
        if (!puedeRendir(desde, hasta, 1)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La prueba esta fuera de plazo"
            );
        }
        SolucionPruebaDTO solucion = actividadesCliente
                .obtenerSolucionarioPrueba(intento.getPruebaId());
        if (solucion.getPreguntas() == null || solucion.getPreguntas().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La prueba no tiene preguntas"
            );
        }
        Map<Long, SolucionPreguntaDTO> porPregunta = solucion.getPreguntas().stream()
                .collect(Collectors.toMap(
                        SolucionPreguntaDTO::getPreguntaId,
                        Function.identity()
                ));
        validarRespuestas(dto.getRespuestas(), porPregunta);
        List<FeedbackDTO> feedback = new ArrayList<>();
        int correctas = 0;
        for (SolucionPreguntaDTO pregunta : solucion.getPreguntas()) {
            RespuestaEnviadaDTO enviada = dto.getRespuestas().stream()
                    .filter(r -> r.getPreguntaId().equals(pregunta.getPreguntaId()))
                    .findFirst()
                    .orElse(null);
            boolean correcta = enviada != null
                    && enviada.getRespuesta().getAlternativaId()
                            .equals(pregunta.getAlternativaCorrectaId());
            if (correcta) {
                correctas++;
            }
            FeedbackDTO item = new FeedbackDTO();
            item.setPreguntaId(pregunta.getPreguntaId());
            item.setCorrecta(correcta);
            item.setExplicacion(pregunta.getExplicacion());
            feedback.add(item);
        }
        int puntaje = correctas * 100 / solucion.getPreguntas().size();
        Instant ahora = Instant.now();
        intento.setEstado(EstadoIntento.CALIFICADO);
        intento.setPuntaje(puntaje);
        intento.setEntregadoEn(ahora);
        intentoRepository.save(intento);
        actualizarProgreso(estudianteId, prueba.getObjetivoAprendizajeId(), puntaje);
        for (RespuestaEnviadaDTO enviada : dto.getRespuestas()) {
            SolucionPreguntaDTO pregunta = porPregunta.get(enviada.getPreguntaId());
            RespuestaEstudiante respuesta = new RespuestaEstudiante();
            respuesta.setIntento(intento);
            respuesta.setPreguntaId(enviada.getPreguntaId());
            respuesta.setTipo(TipoActividad.SELECCION);
            respuesta.setRespuestaJson(aJson(enviada));
            respuesta.setCorrecta(enviada.getRespuesta().getAlternativaId()
                    .equals(pregunta.getAlternativaCorrectaId()));
            respuesta.setExplicacion(pregunta.getExplicacion());
            respuestaRepository.save(respuesta);
        }
        return armarResultado(intento.getId(), intento.getPruebaId(), puntaje, feedback, ahora);
    }
    
    private void validarRespuestas(
            List<RespuestaEnviadaDTO> respuestas,
            Map<Long, SolucionPreguntaDTO> porPregunta) {
    
        Set<Long> vistas = new HashSet<>();
        for (RespuestaEnviadaDTO enviada : respuestas) {
            if (!vistas.add(enviada.getPreguntaId())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La pregunta esta repetida"
                );
            }
            if (!porPregunta.containsKey(enviada.getPreguntaId())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La pregunta no pertenece a la prueba"
                );
            }
            if (enviada.getRespuesta().getTipo() != TipoActividad.SELECCION) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Por ahora solo se corrigen preguntas de seleccion"
                );
            }
        }
    }
    
    private void actualizarProgreso(long estudianteId, Long objetivoAprendizajeId, int puntaje) {
        Progreso progreso = progresoRepository
                .findByEstudianteIdAndObjetivoAprendizajeId(estudianteId, objetivoAprendizajeId)
                .orElseGet(() -> {
                    Progreso nuevo = new Progreso();
                    nuevo.setEstudianteId(estudianteId);
                    nuevo.setObjetivoAprendizajeId(objetivoAprendizajeId);
                    nuevo.setPorcentajeAvance(0);
                    return nuevo;
                });
        progreso.setPorcentajeAvance(Math.max(progreso.getPorcentajeAvance(), puntaje));
        progreso.setActualizadoEn(Instant.now());
        progresoRepository.save(progreso);
    }
    
    private ResultadoIntentoDTO armarDesdeGuardado(IntentoPrueba intento) {
        List<FeedbackDTO> feedback = respuestaRepository
                .findByIntento_Id(intento.getId())
                .stream()
                .map(respuesta -> {
                    FeedbackDTO item = new FeedbackDTO();
                    item.setPreguntaId(respuesta.getPreguntaId());
                    item.setCorrecta(respuesta.getCorrecta());
                    item.setExplicacion(respuesta.getExplicacion());
                    return item;
                })
                .toList();
        return armarResultado(
                intento.getId(),
                intento.getPruebaId(),
                intento.getPuntaje(),
                feedback,
                intento.getEntregadoEn()
        );
    }
    
    private ResultadoIntentoDTO armarResultado(
            Long intentoId,
            Long pruebaId,
            int puntaje,
            List<FeedbackDTO> feedback,
            Instant registradoEn) {
    
        ResultadoIntentoDTO dto = new ResultadoIntentoDTO();
        dto.setIntentoId(intentoId);
        dto.setPruebaId(pruebaId);
        dto.setPuntaje(puntaje);
        dto.setFeedback(feedback);
        dto.setRegistradoEn(registradoEn);
        return dto;
    }
    
    private String aJson(RespuestaEnviadaDTO enviada) {
        try {
            return objectMapper.writeValueAsString(enviada.getRespuesta());
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo guardar la respuesta"
            );
        }
    }
}