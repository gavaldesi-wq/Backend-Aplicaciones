package cl.mpazpro.progreso.dto;

import java.time.Instant;

import cl.mpazpro.progreso.model.Asignacion;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AsignacionDTO {

    private Long id;

    @NotNull
    @Positive
    private Long cursoId;

    @Positive
    private Long estudianteId;

    @NotNull
    @Positive
    private Long objetivoAprendizajeId;

    private Long docenteId;

    @NotNull
    private Instant disponibleDesde;

    @NotNull
    private Instant disponibleHasta;

    @NotNull
    @Min(1)
    private Integer limiteIntentosPrueba;

    private Boolean activa;

    public static AsignacionDTO fromModel(Asignacion asignacion) {
        AsignacionDTO dto = new AsignacionDTO();
        dto.setId(asignacion.getId());
        dto.setCursoId(asignacion.getCursoId());
        dto.setEstudianteId(asignacion.getEstudianteId());
        dto.setObjetivoAprendizajeId(asignacion.getObjetivoAprendizajeId());
        dto.setDocenteId(asignacion.getDocenteId());
        dto.setDisponibleDesde(asignacion.getDisponibleDesde());
        dto.setDisponibleHasta(asignacion.getDisponibleHasta());
        dto.setLimiteIntentosPrueba(asignacion.getLimiteIntentosPrueba());
        dto.setActiva(asignacion.isActiva());
        return dto;
    }
}