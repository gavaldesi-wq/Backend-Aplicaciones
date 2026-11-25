package cl.mpazpro.progreso.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class ProgresoEstudianteDTO {

    private Long estudianteId;
    private Integer porcentajeAvance;
    private Instant actualizadoEn;
}