package cl.mpazpro.progreso.dto;

import java.time.Instant;
import java.util.List;

import lombok.Data;

@Data
public class EnvioResultadoDTO {

    private Long id;
    private Long actividadId;
    private Long objetivoAprendizajeId;
    private Integer puntaje;
    private Instant registradoEn;
    private List<FeedbackDTO> feedback;
}