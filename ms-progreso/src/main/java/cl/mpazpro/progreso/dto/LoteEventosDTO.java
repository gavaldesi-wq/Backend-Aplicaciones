package cl.mpazpro.progreso.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoteEventosDTO {

    @NotEmpty
    @Valid
    private List<EventoProgresoDTO> eventos;
}