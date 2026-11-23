package cl.mpazpro.admin.dto;

import cl.mpazpro.admin.model.Matricula;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MatriculaDTO {

    private Long id;

    @NotNull
    @Positive
    private Long cursoId;

    @NotNull
    @Positive
    private Long estudianteId;

    private Boolean activo;

    public static MatriculaDTO fromModel(Matricula matricula) {
        MatriculaDTO dto = new MatriculaDTO();
        dto.setId(matricula.getId());
        dto.setCursoId(matricula.getCursoId());
        dto.setEstudianteId(matricula.getEstudianteId());
        dto.setActivo(matricula.getActivo());
        return dto;
    }

    public Matricula toModel() {
        Matricula matricula = new Matricula();
        matricula.setCursoId(cursoId);
        matricula.setEstudianteId(estudianteId);
        matricula.setActivo(activo != null ? activo : true);
        return matricula;
    }
}
