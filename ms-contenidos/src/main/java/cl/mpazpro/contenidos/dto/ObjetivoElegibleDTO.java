package cl.mpazpro.contenidos.dto;

import lombok.Data;

@Data
public class ObjetivoElegibleDTO {

    private Long id;
    private Long unidadId;
    private String codigo;
    private String descripcion;
    private String estado;
}