package cl.mpazpro.progreso.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EventoProgresoDTO {

    @NotBlank
    @Size(min = 36, max = 36)
    private String clientEventId;

    @NotBlank
    @Size(max = 40)
    private String tipo;

    private Long objetivoAprendizajeId;

    private Long contenidoId;

    @NotNull
    private Instant ocurridoEn;
}