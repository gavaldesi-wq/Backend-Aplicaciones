package cl.mpazpro.progreso.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class PruebaPublicadaDTO {

    private Long id;
    private Long objetivoAprendizajeId;
    private String estado;
    private Instant disponibleDesde;
    private Instant disponibleHasta;
    private Integer limiteIntentos;
}