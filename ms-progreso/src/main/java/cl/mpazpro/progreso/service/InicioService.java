package cl.mpazpro.progreso.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.mpazpro.progreso.dto.InicioDTO;
import cl.mpazpro.progreso.dto.ProgresoDTO;
import cl.mpazpro.progreso.model.Asignacion;
import cl.mpazpro.progreso.repository.AsignacionRepository;
import cl.mpazpro.progreso.repository.ProgresoRepository;
import cl.mpazpro.progreso.security.SesionActual;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InicioService {

    private final AsignacionRepository asignacionRepository;
    private final ProgresoRepository progresoRepository;
    private final SesionActual sesionActual;

    @Transactional(readOnly = true)
    public InicioDTO obtener() {
        long estudianteId = sesionActual.estudianteId();

        Set<Long> objetivos = new LinkedHashSet<>();
        for (Asignacion asignacion : asignacionRepository.findByActivaTrue()) {
            if (asignacion.getEstudianteId() == null
                    || Long.valueOf(estudianteId).equals(asignacion.getEstudianteId())) {
                objetivos.add(asignacion.getObjetivoAprendizajeId());
            }
        }

        List<ProgresoDTO> filas = new ArrayList<>();
        for (Long objetivoId : objetivos) {
            ProgresoDTO fila = new ProgresoDTO();
            fila.setObjetivoAprendizajeId(objetivoId);
            fila.setPorcentajeAvance(0);
            progresoRepository
                    .findByEstudianteIdAndObjetivoAprendizajeId(estudianteId, objetivoId)
                    .ifPresent(progreso -> {
                        fila.setPorcentajeAvance(progreso.getPorcentajeAvance());
                        fila.setActualizadoEn(progreso.getActualizadoEn());
                    });
            filas.add(fila);
        }

        InicioDTO dto = new InicioDTO();
        dto.setEstudianteId(estudianteId);
        dto.setObjetivos(filas);
        return dto;
    }
}