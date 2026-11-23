package cl.mpazpro.admin.dto;

import cl.mpazpro.admin.model.HistorialAprobacion;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneOffset;

@Data
public class HistorialAprobacionDTO {

    private Long id;
    private String eventoId;
    private String tipoRecurso;
    private Long recursoId;
    private String estadoAnterior;
    private String estadoNuevo;
    private Long usuarioId;
    private Instant creadoEn;

    public static HistorialAprobacionDTO fromModel(
            HistorialAprobacion historial
    ) {
        HistorialAprobacionDTO dto = new HistorialAprobacionDTO();

        dto.setId(historial.getId());
        dto.setEventoId(historial.getEventoId());
        dto.setTipoRecurso(historial.getTipoRecurso());
        dto.setRecursoId(historial.getRecursoId());
        dto.setEstadoAnterior(historial.getEstadoAnterior());
        dto.setEstadoNuevo(historial.getEstadoNuevo());
        dto.setUsuarioId(historial.getUsuarioId());
        dto.setCreadoEn(
                historial.getCreadoEn().toInstant(ZoneOffset.UTC)
        );

        return dto;
    }

    public HistorialAprobacion toModel() {
        HistorialAprobacion historial = new HistorialAprobacion();

        historial.setEventoId(eventoId);
        historial.setTipoRecurso(tipoRecurso);
        historial.setRecursoId(recursoId);
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(estadoNuevo);

        return historial;
    }
}
