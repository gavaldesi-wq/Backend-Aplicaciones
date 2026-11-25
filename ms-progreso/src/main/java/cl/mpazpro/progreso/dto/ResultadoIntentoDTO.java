package cl.mpazpro.progreso.dto;

import java.time.Instant;
import java.util.List;

import lombok.Data;

@Data
public class ResultadoIntentoDTO {

    private Long intentoId;
    private Long pruebaId;
    private Integer puntaje;
    private List<FeedbackDTO> feedback;
    private Instant registradoEn;
}