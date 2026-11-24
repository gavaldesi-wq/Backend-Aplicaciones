package cl.mpazpro.actividades.dto;

import java.util.List;

import lombok.Data;

@Data
public class SolucionPruebaDTO {

    private Long pruebaId;
    private List<SolucionPreguntaDTO> preguntas;
}