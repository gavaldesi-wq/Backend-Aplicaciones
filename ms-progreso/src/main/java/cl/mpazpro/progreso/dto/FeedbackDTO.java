package cl.mpazpro.progreso.dto;

import lombok.Data;

@Data
public class FeedbackDTO {

    private Long preguntaId;
    private Boolean correcta;
    private String explicacion;
}