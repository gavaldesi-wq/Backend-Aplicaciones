package cl.mpazpro.contenidos.dto;

import cl.mpazpro.contenidos.model.EstadoContenido;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransicionEstadoDTO {

    @NotNull
    private EstadoContenido estadoNuevo;
}
