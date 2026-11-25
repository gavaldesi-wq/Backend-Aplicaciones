package cl.mpazpro.progreso.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cl.mpazpro.progreso.dto.EventoProgresoDTO;
import cl.mpazpro.progreso.dto.LoteEventosDTO;
import cl.mpazpro.progreso.dto.ResultadoEventoDTO;
import cl.mpazpro.progreso.dto.ResultadoLoteDTO;
import cl.mpazpro.progreso.model.Asignacion;
import cl.mpazpro.progreso.model.EstadoEvento;
import cl.mpazpro.progreso.model.EventoSincronizado;
import cl.mpazpro.progreso.repository.AsignacionRepository;
import cl.mpazpro.progreso.repository.EventoSincronizadoRepository;
import cl.mpazpro.progreso.security.SesionActual;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventoProgresoService {

    private static final String CONTENIDO_VISTO = "CONTENIDO_VISTO";

    private final EventoSincronizadoRepository eventoRepository;
    private final AsignacionRepository asignacionRepository;
    private final ObjectMapper objectMapper;
    private final SesionActual sesionActual;

    @Transactional
    public ResultadoEventoDTO registrar(EventoProgresoDTO dto) {
        long estudianteId = sesionActual.estudianteId();
        return eventoRepository.findById(dto.getClientEventId())
                .map(this::aResultado)
                .orElseGet(() -> guardar(estudianteId, dto));
    }

    @Transactional
    public ResultadoLoteDTO registrarLote(LoteEventosDTO dto) {
        List<ResultadoEventoDTO> resultados = dto.getEventos().stream()
                .map(this::registrar)
                .toList();

        ResultadoLoteDTO lote = new ResultadoLoteDTO();
        lote.setResultados(resultados);
        return lote;
    }

    private ResultadoEventoDTO guardar(long estudianteId, EventoProgresoDTO dto) {
        EventoSincronizado evento = new EventoSincronizado();
        evento.setClientEventId(dto.getClientEventId());
        evento.setTipo(dto.getTipo());
        evento.setObjetivoAprendizajeId(dto.getObjetivoAprendizajeId());
        evento.setContenidoId(dto.getContenidoId());
        evento.setOcurridoEn(dto.getOcurridoEn());
        evento.setPayloadJson(aJson(dto));
        evento.setRegistradoEn(Instant.now());

        if (!CONTENIDO_VISTO.equals(dto.getTipo())) {
            evento.setEstado(EstadoEvento.RECHAZADO);
            evento.setUltimoError("Tipo de evento no soportado");
        } else if (dto.getObjetivoAprendizajeId() == null || dto.getContenidoId() == null) {
            evento.setEstado(EstadoEvento.RECHAZADO);
            evento.setUltimoError("Faltan datos del evento");
        } else if (!tieneAsignacion(estudianteId, dto.getObjetivoAprendizajeId())) {
            evento.setEstado(EstadoEvento.RECHAZADO);
            evento.setUltimoError("Sin asignacion");
        } else {
            evento.setEstado(EstadoEvento.SINCRONIZADO);
        }

        return aResultado(eventoRepository.save(evento));
    }

    private boolean tieneAsignacion(long estudianteId, Long objetivoAprendizajeId) {
        List<Asignacion> asignaciones = asignacionRepository
                .findByObjetivoAprendizajeIdAndActivaTrue(objetivoAprendizajeId);

        return asignaciones.stream().anyMatch(asignacion ->
                Long.valueOf(estudianteId).equals(asignacion.getEstudianteId())
                        || asignacion.getEstudianteId() == null
        );
    }

    private ResultadoEventoDTO aResultado(EventoSincronizado evento) {
        ResultadoEventoDTO dto = new ResultadoEventoDTO();
        dto.setClientEventId(evento.getClientEventId());
        dto.setEstado(evento.getEstado());
        return dto;
    }

    private String aJson(EventoProgresoDTO dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException ex) {
            return "{}";
        }
    }
}