package cl.mpazpro.progreso.dto;

import lombok.Data;

@Data
public class ObjetivoDisponibleDTO {

    private Long id;
    private Long unidadId;
    private String codigo;
    private String descripcion;
    private String estado;
}