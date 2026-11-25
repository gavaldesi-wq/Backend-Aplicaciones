package cl.mpazpro.progreso.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InicioIntentoDTO {

    @NotBlank
    @Size(min = 36, max = 36)
    private String clientEventId;
}