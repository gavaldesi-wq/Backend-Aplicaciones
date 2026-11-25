package cl.mpazpro.progreso.dto;

import java.util.List;

import lombok.Data;

@Data
public class SeguimientoDTO {

    private Long cursoId;
    private Long objetivoAprendizajeId;
    private List<ProgresoEstudianteDTO> estudiantes;
}