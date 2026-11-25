package cl.mpazpro.progreso.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.mpazpro.progreso.dto.EnvioResultadoDTO;
import cl.mpazpro.progreso.dto.FeedbackDTO;
import cl.mpazpro.progreso.dto.IntentoResultadoDTO;
import cl.mpazpro.progreso.dto.ResultadosEstudianteDTO;
import cl.mpazpro.progreso.model.EnvioActividad;
import cl.mpazpro.progreso.model.IntentoPrueba;
import cl.mpazpro.progreso.model.RespuestaEstudiante;
import cl.mpazpro.progreso.repository.EnvioActividadRepository;
import cl.mpazpro.progreso.repository.IntentoPruebaRepository;
import cl.mpazpro.progreso.repository.RespuestaEstudianteRepository;
import lombok.RequiredArgsConstructor;
import cl.mpazpro.progreso.repository.AsignacionRepository;
import cl.mpazpro.progreso.security.SesionActual;
@Service
@RequiredArgsConstructor
public class DocenteResultadoService {

    private final EnvioActividadRepository envioRepository;
    private final IntentoPruebaRepository intentoRepository;
    private final RespuestaEstudianteRepository respuestaRepository;
    private static final long ESTUDIANTE_CONOCIDO = 3L;
    private final AsignacionRepository asignacionRepository;
    private final SesionActual sesionActual;

    @Transactional(readOnly = true)
    public ResultadosEstudianteDTO obtener(Long estudianteId) {
        ResultadosEstudianteDTO dto = new ResultadosEstudianteDTO();
        dto.setEstudianteId(estudianteId);
    
        if (!cubre(estudianteId)) {
            dto.setEnvios(List.of());
            dto.setIntentos(List.of());
            return dto;
        }
    
        List<EnvioResultadoDTO> envios = envioRepository
                .findByEstudianteIdOrderByRegistradoEnAsc(estudianteId)
                .stream()
                .map(this::aEnvio)
                .toList();
    
        List<IntentoResultadoDTO> intentos = intentoRepository
                .findByEstudianteIdOrderByIniciadoEnAsc(estudianteId)
                .stream()
                .map(this::aIntento)
                .toList();
    
        dto.setEnvios(envios);
        dto.setIntentos(intentos);
        return dto;
    }
    
    private boolean cubre(Long estudianteId) {
        long docenteId = sesionActual.docenteId();
        return asignacionRepository.findByActivaTrue().stream().anyMatch(asignacion ->
                Long.valueOf(docenteId).equals(asignacion.getDocenteId())
                        && (Long.valueOf(estudianteId).equals(asignacion.getEstudianteId())
                                || (asignacion.getEstudianteId() == null
                                        && Long.valueOf(ESTUDIANTE_CONOCIDO).equals(estudianteId)))
        );
    }

    private EnvioResultadoDTO aEnvio(EnvioActividad envio) {
        EnvioResultadoDTO dto = new EnvioResultadoDTO();
        dto.setId(envio.getId());
        dto.setActividadId(envio.getActividadId());
        dto.setObjetivoAprendizajeId(envio.getObjetivoAprendizajeId());
        dto.setPuntaje(envio.getPuntaje());
        dto.setRegistradoEn(envio.getRegistradoEn());
        dto.setFeedback(respuestaRepository
                .findByEnvioActividad_Id(envio.getId())
                .stream()
                .map(this::aFeedback)
                .toList());
        return dto;
    }

    private IntentoResultadoDTO aIntento(IntentoPrueba intento) {
        IntentoResultadoDTO dto = new IntentoResultadoDTO();
        dto.setId(intento.getId());
        dto.setPruebaId(intento.getPruebaId());
        dto.setNumeroIntento(intento.getNumeroIntento());
        dto.setEstado(intento.getEstado());
        dto.setPuntaje(intento.getPuntaje());
        dto.setIniciadoEn(intento.getIniciadoEn());
        dto.setEntregadoEn(intento.getEntregadoEn());
        dto.setFeedback(respuestaRepository
                .findByIntento_Id(intento.getId())
                .stream()
                .map(this::aFeedback)
                .toList());
        return dto;
    }

    private FeedbackDTO aFeedback(RespuestaEstudiante respuesta) {
        FeedbackDTO dto = new FeedbackDTO();
        dto.setPreguntaId(respuesta.getPreguntaId());
        dto.setCorrecta(respuesta.getCorrecta());
        dto.setExplicacion(respuesta.getExplicacion());
        return dto;
    }
}