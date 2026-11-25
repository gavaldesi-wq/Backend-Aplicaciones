package cl.mpazpro.progreso.dto;

import cl.mpazpro.progreso.model.TipoActividad;
import lombok.Data;

@Data
public class SolucionPreguntaDTO {

    private Long preguntaId;
    private TipoActividad tipo;
    private Long alternativaCorrectaId;
    private String explicacion;
}