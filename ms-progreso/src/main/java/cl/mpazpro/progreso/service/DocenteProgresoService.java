package cl.mpazpro.progreso.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.mpazpro.progreso.dto.ProgresoEstudianteDTO;
import cl.mpazpro.progreso.dto.SeguimientoDTO;
import cl.mpazpro.progreso.model.Asignacion;
import cl.mpazpro.progreso.repository.AsignacionRepository;
import cl.mpazpro.progreso.repository.ProgresoRepository;
import cl.mpazpro.progreso.security.SesionActual;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocenteProgresoService {

    private static final long ESTUDIANTE_CONOCIDO = 3L;

    private final AsignacionRepository asignacionRepository;
    private final ProgresoRepository progresoRepository;
    private final SesionActual sesionActual;

    @Transactional(readOnly = true)
    public SeguimientoDTO obtener(Long cursoId, Long objetivoAprendizajeId) {
        long docenteId = sesionActual.docenteId();
        List<Asignacion> asignaciones = asignacionRepository
                .findByCursoIdAndObjetivoAprendizajeId(cursoId, objetivoAprendizajeId);

        Set<Long> estudiantes = new LinkedHashSet<>();
        for (Asignacion asignacion : asignaciones) {
            if (!asignacion.isActiva()
                    || !Long.valueOf(docenteId).equals(asignacion.getDocenteId())) {
                continue;
            }
            if (asignacion.getEstudianteId() == null) {
                estudiantes.add(ESTUDIANTE_CONOCIDO);
            } else {
                estudiantes.add(asignacion.getEstudianteId());
            }
        }

        List<ProgresoEstudianteDTO> filas = new ArrayList<>();
        for (Long estudianteId : estudiantes) {
            ProgresoEstudianteDTO fila = new ProgresoEstudianteDTO();
            fila.setEstudianteId(estudianteId);
            fila.setPorcentajeAvance(0);
            progresoRepository
                    .findByEstudianteIdAndObjetivoAprendizajeId(
                            estudianteId, objetivoAprendizajeId)
                    .ifPresent(progreso -> {
                        fila.setPorcentajeAvance(progreso.getPorcentajeAvance());
                        fila.setActualizadoEn(progreso.getActualizadoEn());
                    });
            filas.add(fila);
        }

        SeguimientoDTO dto = new SeguimientoDTO();
        dto.setCursoId(cursoId);
        dto.setObjetivoAprendizajeId(objetivoAprendizajeId);
        dto.setEstudiantes(filas);
        return dto;
    }
}