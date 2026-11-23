package cl.mpazpro.contenidos.dto;

import cl.mpazpro.contenidos.model.Unidad;
import cl.mpazpro.contenidos.model.EstadoContenido;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UnidadDTO {

    private Long id;

    @NotNull
    @Positive
    private Long asignaturaId;

    @NotBlank
    @Size(max = 200)
    private String titulo;

    @NotNull
    @Min(0)
    private Integer orden;

    private EstadoContenido estado;


    public static UnidadDTO fromModel(Unidad unidad) {
        UnidadDTO dto = new UnidadDTO();

        dto.setId(unidad.getId());
        dto.setAsignaturaId(unidad.getAsignatura().getId());
        dto.setTitulo(unidad.getTitulo());
        dto.setOrden(unidad.getOrden());
        dto.setEstado(unidad.getEstado());

        return dto;
    }

    public Unidad toModel() {
        Unidad unidad = new Unidad();

        unidad.setTitulo(titulo);
        unidad.setOrden(orden);

        return unidad;
    }
}
