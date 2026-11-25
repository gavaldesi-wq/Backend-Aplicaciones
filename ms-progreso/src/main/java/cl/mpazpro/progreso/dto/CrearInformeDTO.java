package cl.mpazpro.progreso.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CrearInformeDTO {

    @NotNull
    @Positive
    private Long cursoId;

    @NotNull
    @Positive
    private Long estudianteId;

    @NotNull
    @Positive
    private Long objetivoAprendizajeId;
}