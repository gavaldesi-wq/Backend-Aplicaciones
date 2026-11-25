package cl.mpazpro.progreso.dto;

import java.util.List;

import cl.mpazpro.progreso.model.TipoActividad;
import lombok.Data;

@Data
public class PreguntaEstudianteDTO {

    private Long id;
    private String enunciado;
    private TipoActividad tipo;
    private List<OpcionDTO> opciones;
}