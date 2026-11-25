package cl.mpazpro.progreso.dto;

import java.util.List;

import lombok.Data;

@Data
public class SolucionPruebaDTO {

    private Long pruebaId;
    private List<SolucionPreguntaDTO> preguntas;
}