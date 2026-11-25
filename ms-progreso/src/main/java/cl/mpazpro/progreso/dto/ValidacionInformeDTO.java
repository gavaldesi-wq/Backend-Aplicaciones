package cl.mpazpro.progreso.dto;

import cl.mpazpro.progreso.model.EstadoInforme;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ValidacionInformeDTO {

    @NotNull
    private EstadoInforme estado;

    @Size(max = 2000)
    private String observacion;
}