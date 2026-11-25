package cl.mpazpro.progreso.dto;

import java.time.Instant;
import java.util.List;

import cl.mpazpro.progreso.model.EstadoIntento;
import lombok.Data;

@Data
public class IntentoResultadoDTO {

    private Long id;
    private Long pruebaId;
    private Integer numeroIntento;
    private EstadoIntento estado;
    private Integer puntaje;
    private Instant iniciadoEn;
    private Instant entregadoEn;
    private List<FeedbackDTO> feedback;
}