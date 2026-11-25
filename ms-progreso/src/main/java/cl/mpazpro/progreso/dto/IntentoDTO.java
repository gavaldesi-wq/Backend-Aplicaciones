package cl.mpazpro.progreso.dto;

import java.time.Instant;

import cl.mpazpro.progreso.model.EstadoIntento;
import lombok.Data;

@Data
public class IntentoDTO {

    private Long id;
    private Long pruebaId;
    private Integer numeroIntento;
    private EstadoIntento estado;
    private Instant iniciadoEn;
}