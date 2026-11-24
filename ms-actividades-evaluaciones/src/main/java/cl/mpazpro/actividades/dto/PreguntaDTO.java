package cl.mpazpro.actividades.dto;

import java.util.List;

import cl.mpazpro.actividades.model.TipoActividad;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PreguntaDTO {

    private Long id;

    private Long actividadId;

    private Long pruebaFinalId;
    @NotBlank
    @Size(max = 1000)
    private String enunciado;

    @NotNull
    private TipoActividad tipo;

    @NotNull
    private Integer orden;

    @Size(max = 1000)
    private String explicacion;

    @NotEmpty
    @Valid
    private List<AlternativaDTO> opciones;
}