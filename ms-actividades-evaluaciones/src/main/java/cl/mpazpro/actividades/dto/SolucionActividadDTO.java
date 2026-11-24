package cl.mpazpro.actividades.dto;

import java.util.List;

import lombok.Data;

@Data
public class SolucionActividadDTO {

    private Long actividadId;
    private List<SolucionPreguntaDTO> preguntas;
}