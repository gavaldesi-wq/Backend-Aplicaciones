package cl.mpazpro.progreso.dto;

import java.time.Instant;

import cl.mpazpro.progreso.model.EstadoInforme;
import cl.mpazpro.progreso.model.InformePedagogico;
import lombok.Data;

@Data
public class InformeDTO {

    private Long id;
    private Long cursoId;
    private Long estudianteId;
    private Long objetivoAprendizajeId;
    private Long docenteId;
    private EstadoInforme estado;
    private String resumen;
    private String areasRefuerzo;
    private Instant creadoEn;

    public static InformeDTO fromModel(InformePedagogico informe) {
        InformeDTO dto = new InformeDTO();
        dto.setId(informe.getId());
        dto.setCursoId(informe.getCursoId());
        dto.setEstudianteId(informe.getEstudianteId());
        dto.setObjetivoAprendizajeId(informe.getObjetivoAprendizajeId());
        dto.setDocenteId(informe.getDocenteId());
        dto.setEstado(informe.getEstado());
        dto.setResumen(informe.getResumen());
        dto.setAreasRefuerzo(informe.getAreasRefuerzo());
        dto.setCreadoEn(informe.getCreadoEn());
        return dto;
    }
}