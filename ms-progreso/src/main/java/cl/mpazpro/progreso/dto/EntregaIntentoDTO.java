package cl.mpazpro.progreso.dto;

import java.time.Instant;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EntregaIntentoDTO {

    @NotEmpty
    @Valid
    private List<RespuestaEnviadaDTO> respuestas;

    @NotNull
    private Instant respondidaEn;
}