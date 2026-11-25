package cl.mpazpro.progreso.dto;

import lombok.Data;

import java.util.List;

@Data
public class InicioDTO {

    private Long estudianteId;
    private List<ProgresoDTO> objetivos;
}