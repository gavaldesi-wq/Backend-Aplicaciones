package cl.mpazpro.progreso.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RespuestaEnviadaDTO {

    @NotNull
    @Positive
    private Long preguntaId;

    @NotNull
    @Valid
    private RespuestaSeleccionDTO respuesta;
}