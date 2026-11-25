package cl.mpazpro.progreso.dto;

import java.time.Instant;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EnvioActividadDTO {

    @NotBlank
    @Size(min = 36, max = 36)
    private String clientEventId;

    @NotEmpty
    @Valid
    private List<RespuestaEnviadaDTO> respuestas;

    @NotNull
    private Instant respondidaEn;
}