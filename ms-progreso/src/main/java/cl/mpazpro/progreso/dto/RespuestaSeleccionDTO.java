package cl.mpazpro.progreso.dto;

import cl.mpazpro.progreso.model.TipoActividad;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RespuestaSeleccionDTO {

    @NotNull
    private TipoActividad tipo;

    @NotNull
    @Positive
    private Long alternativaId;
}