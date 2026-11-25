package cl.mpazpro.progreso.dto;

import cl.mpazpro.progreso.model.EstadoEvento;
import lombok.Data;

@Data
public class ResultadoEventoDTO {

    private String clientEventId;
    private EstadoEvento estado;
}