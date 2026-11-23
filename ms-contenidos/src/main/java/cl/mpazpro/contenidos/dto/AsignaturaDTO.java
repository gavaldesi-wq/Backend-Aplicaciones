package cl.mpazpro.contenidos.dto;

import cl.mpazpro.contenidos.model.Asignatura;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AsignaturaDTO {

    private Long id;

    @NotBlank
    @Size(max = 150)
    private String nombre;

    @NotBlank
    @Size(max = 50)
    private String nivel;


    public static AsignaturaDTO fromModel(Asignatura asignatura) {
        AsignaturaDTO dto = new AsignaturaDTO();

        dto.setId(asignatura.getId());
        dto.setNombre(asignatura.getNombre());
        dto.setNivel(asignatura.getNivel());

        return dto;
    }

    public Asignatura toModel() {
        Asignatura asignatura = new Asignatura();

        asignatura.setNombre(nombre);
        asignatura.setNivel(nivel);

        return asignatura;
    }
}
