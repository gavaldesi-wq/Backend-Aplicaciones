package cl.mpazpro.actividades.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AlternativaDTO {

    private Long id;

    @NotBlank
    @Size(max = 500)
    private String texto;

    @NotNull
    private Boolean esCorrecta;

    @NotNull
    private Integer orden;
}