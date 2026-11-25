package cl.mpazpro.actividades.dto;

import cl.mpazpro.actividades.model.TipoActividad;
import lombok.Data;

@Data
public class SolucionPreguntaDTO {

    private Long preguntaId;
    private TipoActividad tipo;
    private Long alternativaCorrectaId;
    private String explicacion;
}