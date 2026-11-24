package cl.mpazpro.actividades.dto;

import cl.mpazpro.actividades.model.ActividadDesafio;
import cl.mpazpro.actividades.model.EstadoContenido;
import cl.mpazpro.actividades.model.TipoActividad;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ActividadDTO {

    private Long id;

    @NotNull
    @Positive
    private Long objetivoAprendizajeId;

    @NotNull
    private TipoActividad tipo;

    @NotBlank
    @Size(max = 200)
    private String titulo;

    @NotNull
    @Min(0)
    private Integer orden;

    private EstadoContenido estado;

    public static ActividadDTO fromModel(ActividadDesafio actividad) {
        ActividadDTO dto = new ActividadDTO();

        dto.setId(actividad.getId());
        dto.setObjetivoAprendizajeId(actividad.getObjetivoAprendizajeId());
        dto.setTipo(actividad.getTipo());
        dto.setTitulo(actividad.getTitulo());
        dto.setOrden(actividad.getOrden());
        dto.setEstado(actividad.getEstado());

        return dto;
    }

    public ActividadDesafio toModel() {
        ActividadDesafio actividad = new ActividadDesafio();

        actividad.setTipo(tipo);
        actividad.setTitulo(titulo);
        actividad.setOrden(orden);

        return actividad;
    }
}