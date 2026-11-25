package cl.mpazpro.progreso.service;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import cl.mpazpro.progreso.dto.AsignacionDTO;
import cl.mpazpro.progreso.model.Asignacion;
import cl.mpazpro.progreso.repository.AsignacionRepository;
import cl.mpazpro.progreso.security.SesionActual;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AsignacionService {

    private static final long ESTUDIANTE_CONOCIDO = 3L;

    private final AsignacionRepository asignacionRepository;
    private final SesionActual sesionActual;

    @Transactional
    public AsignacionDTO save(AsignacionDTO dto) {
        if (!dto.getDisponibleDesde().isBefore(dto.getDisponibleHasta())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "disponibleDesde debe ser anterior a disponibleHasta"
            );
        }

        if (dto.getEstudianteId() != null
                && dto.getEstudianteId() != ESTUDIANTE_CONOCIDO) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El estudiante no pertenece al curso"
            );
        }
        Instant ahora = Instant.now();
        Asignacion asignacion = new Asignacion();
        asignacion.setCursoId(dto.getCursoId());
        asignacion.setEstudianteId(dto.getEstudianteId());
        asignacion.setObjetivoAprendizajeId(dto.getObjetivoAprendizajeId());
        asignacion.setDocenteId(sesionActual.docenteId());
        asignacion.setDisponibleDesde(dto.getDisponibleDesde());
        asignacion.setDisponibleHasta(dto.getDisponibleHasta());
        asignacion.setLimiteIntentosPrueba(dto.getLimiteIntentosPrueba());
        asignacion.setActiva(true);
        asignacion.setCreadoEn(ahora);
        asignacion.setActualizadoEn(ahora);

        return AsignacionDTO.fromModel(asignacionRepository.save(asignacion));
    }
    
    @Transactional(readOnly = true)
    public List<AsignacionDTO> getAll(Long cursoId) {
        long docenteId = sesionActual.docenteId();
        return asignacionRepository.findByCursoId(cursoId).stream()
                .filter(asignacion -> Long.valueOf(docenteId).equals(asignacion.getDocenteId()))
                .map(AsignacionDTO::fromModel)
                .toList();
    }

    @Transactional
    public AsignacionDTO update(Long id, AsignacionDTO dto) {
        Asignacion asignacion = asignacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Asignacion no encontrada"
                ));

        long docenteId = sesionActual.docenteId();
        if (!Long.valueOf(docenteId).equals(asignacion.getDocenteId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Asignacion no encontrada"
            );
        }

        if (!dto.getDisponibleDesde().isBefore(dto.getDisponibleHasta())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "disponibleDesde debe ser anterior a disponibleHasta"
            );
        }
    
        asignacion.setDisponibleDesde(dto.getDisponibleDesde());
        asignacion.setDisponibleHasta(dto.getDisponibleHasta());
        asignacion.setLimiteIntentosPrueba(dto.getLimiteIntentosPrueba());
        asignacion.setActualizadoEn(Instant.now());
    
        return AsignacionDTO.fromModel(asignacionRepository.save(asignacion));
    }
}