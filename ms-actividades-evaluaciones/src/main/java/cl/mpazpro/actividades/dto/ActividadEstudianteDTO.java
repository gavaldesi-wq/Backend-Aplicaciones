package cl.mpazpro.actividades.dto;

import java.util.List;

import cl.mpazpro.actividades.model.TipoActividad;
import lombok.Data;

@Data
public class ActividadEstudianteDTO {

    private Long id;
    private Long objetivoAprendizajeId;
    private TipoActividad tipo;
    private String titulo;
    private List<PreguntaEstudianteDTO> preguntas;
}