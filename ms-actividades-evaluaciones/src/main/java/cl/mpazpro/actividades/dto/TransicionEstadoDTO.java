package cl.mpazpro.actividades.dto;

import cl.mpazpro.actividades.model.EstadoContenido;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransicionEstadoDTO {

    @NotNull
    private EstadoContenido estadoNuevo;
}