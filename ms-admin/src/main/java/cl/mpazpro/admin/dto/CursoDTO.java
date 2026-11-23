package cl.mpazpro.admin.dto;

import cl.mpazpro.admin.model.Curso;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CursoDTO {

    private Long id;

    @NotBlank
    @Size(max = 100)
    private String nombre;

    @NotNull
    @Min(2020)
    private Integer anioEscolar;

    @NotNull
    @Positive
    private Long docenteId;

    private Boolean activo;

    public static CursoDTO fromModel(Curso curso) {
        CursoDTO dto = new CursoDTO();
        dto.setId(curso.getId());
        dto.setNombre(curso.getNombre());
        dto.setAnioEscolar(curso.getAnioEscolar());
        dto.setDocenteId(curso.getDocenteId());
        dto.setActivo(curso.getActivo());
        return dto;
    }

    public Curso toModel() {
        Curso curso = new Curso();
        curso.setNombre(nombre);
        curso.setAnioEscolar(anioEscolar);
        curso.setDocenteId(docenteId);
        curso.setActivo(activo != null ? activo : true);
        return curso;
    }
}
