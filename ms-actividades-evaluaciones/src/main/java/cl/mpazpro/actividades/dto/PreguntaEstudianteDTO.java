package cl.mpazpro.actividades.dto;

import java.util.List;

import cl.mpazpro.actividades.model.TipoActividad;
import lombok.Data;

@Data
public class PreguntaEstudianteDTO {

    private Long id;
    private String enunciado;
    private TipoActividad tipo;
    private List<OpcionDTO> opciones;
}