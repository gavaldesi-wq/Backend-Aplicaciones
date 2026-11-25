package cl.mpazpro.progreso.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cl.mpazpro.progreso.client.ActividadesCliente;
import cl.mpazpro.progreso.dto.EnvioActividadDTO;
import cl.mpazpro.progreso.dto.FeedbackDTO;
import cl.mpazpro.progreso.dto.RespuestaEnviadaDTO;
import cl.mpazpro.progreso.dto.ResultadoEnvioDTO;
import cl.mpazpro.progreso.dto.SolucionActividadDTO;
import cl.mpazpro.progreso.dto.SolucionPreguntaDTO;
import cl.mpazpro.progreso.model.EnvioActividad;
import cl.mpazpro.progreso.model.Progreso;
import cl.mpazpro.progreso.model.RespuestaEstudiante;
import cl.mpazpro.progreso.model.TipoActividad;
import cl.mpazpro.progreso.repository.EnvioActividadRepository;
import cl.mpazpro.progreso.repository.ProgresoRepository;
import cl.mpazpro.progreso.repository.RespuestaEstudianteRepository;
import lombok.RequiredArgsConstructor;
import cl.mpazpro.progreso.security.SesionActual;
@Service
@RequiredArgsConstructor
public class EnvioActividadService {

    private final ActividadesCliente actividadesCliente;
    private final EnvioActividadRepository envioRepository;
    private final RespuestaEstudianteRepository respuestaRepository;
    private final ProgresoRepository progresoRepository;
    private final ObjectMapper objectMapper;
    private final SesionActual sesionActual;

    @Transactional
    public ResultadoEnvioDTO registrar(Long actividadId, EnvioActividadDTO dto) {
        long estudianteId = sesionActual.estudianteId();
        return envioRepository.findByClientEventId(dto.getClientEventId())
                .map(existente -> reutilizar(actividadId, estudianteId, existente))
                .orElseGet(() -> corregirYGuardar(actividadId, estudianteId, dto));
    }
    
    private ResultadoEnvioDTO reutilizar(Long actividadId, long estudianteId, EnvioActividad existente) {
        if (!existente.getActividadId().equals(actividadId)
                || !Long.valueOf(estudianteId).equals(existente.getEstudianteId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El evento ya fue usado en otra actividad"
            );
        }
        return armarDesdeGuardado(existente);
    }

    private ResultadoEnvioDTO corregirYGuardar(Long actividadId,long estudianteId,EnvioActividadDTO dto) {
        SolucionActividadDTO solucion = actividadesCliente.obtenerSolucionario(actividadId);

        if (solucion.getPreguntas() == null || solucion.getPreguntas().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La actividad no tiene preguntas"
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
        int avance = actualizarProgreso(estudianteId, solucion.getObjetivoAprendizajeId(), puntaje);
        Instant ahora = Instant.now();

        EnvioActividad envio = new EnvioActividad();
        envio.setActividadId(actividadId);
        envio.setEstudianteId(estudianteId);
        envio.setObjetivoAprendizajeId(solucion.getObjetivoAprendizajeId());
        envio.setClientEventId(dto.getClientEventId());
        envio.setPuntaje(puntaje);
        envio.setPorcentajeAvance(avance);
        envio.setRespondidaEn(dto.getRespondidaEn());
        envio.setRegistradoEn(ahora);
        envio = envioRepository.save(envio);

        for (RespuestaEnviadaDTO enviada : dto.getRespuestas()) {
            SolucionPreguntaDTO pregunta = porPregunta.get(enviada.getPreguntaId());
            boolean correcta = enviada.getRespuesta().getAlternativaId()
                    .equals(pregunta.getAlternativaCorrectaId());

            RespuestaEstudiante respuesta = new RespuestaEstudiante();
            respuesta.setEnvioActividad(envio);
            respuesta.setPreguntaId(enviada.getPreguntaId());
            respuesta.setTipo(TipoActividad.SELECCION);
            respuesta.setRespuestaJson(aJson(enviada));
            respuesta.setCorrecta(correcta);
            respuesta.setExplicacion(pregunta.getExplicacion());
            respuestaRepository.save(respuesta);
        }

        return armar(actividadId, puntaje, avance, feedback, ahora);
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
                        "La pregunta no pertenece a la actividad"
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

    private int actualizarProgreso(long estudianteId, Long objetivoAprendizajeId, int puntaje) {
        Progreso progreso = progresoRepository
                .findByEstudianteIdAndObjetivoAprendizajeId(estudianteId, objetivoAprendizajeId)
                .orElseGet(() -> {
                    Progreso nuevo = new Progreso();
                    nuevo.setEstudianteId(estudianteId);
                    nuevo.setObjetivoAprendizajeId(objetivoAprendizajeId);
                    nuevo.setPorcentajeAvance(0);
                    return nuevo;
                });
    
        int avance = Math.max(progreso.getPorcentajeAvance(), puntaje);
        progreso.setPorcentajeAvance(avance);
        progreso.setActualizadoEn(Instant.now());
        progresoRepository.save(progreso);
        return avance;
    }

    private ResultadoEnvioDTO armarDesdeGuardado(EnvioActividad envio) {
        List<FeedbackDTO> feedback = respuestaRepository
                .findByEnvioActividad_Id(envio.getId())
                .stream()
                .map(respuesta -> {
                    FeedbackDTO item = new FeedbackDTO();
                    item.setPreguntaId(respuesta.getPreguntaId());
                    item.setCorrecta(respuesta.getCorrecta());
                    item.setExplicacion(respuesta.getExplicacion());
                    return item;
                })
                .toList();

        return armar(
                envio.getActividadId(),
                envio.getPuntaje(),
                envio.getPorcentajeAvance(),
                feedback,
                envio.getRegistradoEn()
        );
    }

    private ResultadoEnvioDTO armar(
            Long actividadId,
            int puntaje,
            int avance,
            List<FeedbackDTO> feedback,
            Instant registradoEn) {

        ResultadoEnvioDTO dto = new ResultadoEnvioDTO();
        dto.setActividadId(actividadId);
        dto.setPuntaje(puntaje);
        dto.setPorcentajeAvance(avance);
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