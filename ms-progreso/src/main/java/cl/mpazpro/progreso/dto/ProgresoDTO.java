package cl.mpazpro.progreso.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class ProgresoDTO {

    private Long objetivoAprendizajeId;
    private Integer porcentajeAvance;
    private Instant actualizadoEn;
}