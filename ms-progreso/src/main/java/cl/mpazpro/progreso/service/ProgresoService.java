package cl.mpazpro.progreso.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.mpazpro.progreso.dto.ProgresoDTO;
import cl.mpazpro.progreso.model.Progreso;
import cl.mpazpro.progreso.repository.ProgresoRepository;
import cl.mpazpro.progreso.security.SesionActual;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProgresoService {

    private final ProgresoRepository progresoRepository;
    private final SesionActual sesionActual;

    @Transactional(readOnly = true)
    public ProgresoDTO obtener(Long objetivoAprendizajeId) {
        long estudianteId = sesionActual.estudianteId();

        ProgresoDTO dto = new ProgresoDTO();
        dto.setObjetivoAprendizajeId(objetivoAprendizajeId);
        dto.setPorcentajeAvance(0);

        progresoRepository
                .findByEstudianteIdAndObjetivoAprendizajeId(
                        estudianteId,
                        objetivoAprendizajeId
                )
                .ifPresent(progreso -> copiar(dto, progreso));

        return dto;
    }

    private void copiar(ProgresoDTO dto, Progreso progreso) {
        dto.setPorcentajeAvance(progreso.getPorcentajeAvance());
        dto.setActualizadoEn(progreso.getActualizadoEn());
    }
}