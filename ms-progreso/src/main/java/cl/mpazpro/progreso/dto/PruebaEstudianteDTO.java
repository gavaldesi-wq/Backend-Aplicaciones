package cl.mpazpro.progreso.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class PruebaEstudianteDTO {

    private Long pruebaId;
    private Long objetivoAprendizajeId;
    private String estado;
    private Instant disponibleDesde;
    private Instant disponibleHasta;
    private Integer limiteIntentos;
    private Integer intentosUtilizados;
    private Integer intentosRestantes;
    private Boolean puedeRendir;
}