package cl.mpazpro.progreso.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class ResultadoEnvioDTO {

    private Long actividadId;
    private Integer puntaje;
    private Integer porcentajeAvance;
    private List<FeedbackDTO> feedback;
    private Instant registradoEn;
}