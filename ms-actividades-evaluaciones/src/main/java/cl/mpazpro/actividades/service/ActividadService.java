package cl.mpazpro.actividades.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import cl.mpazpro.actividades.dto.ActividadDTO;
import cl.mpazpro.actividades.model.ActividadDesafio;
import cl.mpazpro.actividades.model.EstadoContenido;
import cl.mpazpro.actividades.model.TipoRecursoPublicable;
import cl.mpazpro.actividades.model.TransicionPublicacion;
import cl.mpazpro.actividades.repository.ActividadDesafioRepository;
import cl.mpazpro.actividades.repository.TransicionPublicacionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActividadService {

    private final ActividadDesafioRepository actividadRepository;
    private final TransicionPublicacionRepository transicionRepository;

    @Transactional(readOnly = true)
    public List<ActividadDTO> getAll(Long objetivoAprendizajeId) {

        List<ActividadDesafio> actividades;

        if (objetivoAprendizajeId != null) {
            actividades = actividadRepository
                    .findByObjetivoAprendizajeIdOrderByOrdenAsc(objetivoAprendizajeId);
        } else {
            actividades = actividadRepository.findAll();
        }

        return actividades.stream()
                .map(ActividadDTO::fromModel)
                .toList();
    }

    @Transactional
    public ActividadDTO save(ActividadDTO dto) {
        ActividadDesafio actividad = dto.toModel();

        actividad.setObjetivoAprendizajeId(dto.getObjetivoAprendizajeId());
        actividad.setEstado(EstadoContenido.BORRADOR);
        actividad.setCreadoPorUsuarioId(1L);
        actividad.setCreadoEn(Instant.now());
        actividad.setActualizadoEn(Instant.now());

        return ActividadDTO.fromModel(
                actividadRepository.save(actividad)
        );
    }

    @Transactional(readOnly = true)
    public ActividadDTO getById(Long id) {
        ActividadDesafio actividad = buscarActividad(id);
        return ActividadDTO.fromModel(actividad);
    }

    @Transactional
    public ActividadDTO update(Long id, ActividadDTO dto) {
        ActividadDesafio actividad = buscarActividad(id);

        if (actividad.getEstado() != EstadoContenido.BORRADOR) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Solo se pueden editar actividades en BORRADOR"
            );
        }

        actividad.setObjetivoAprendizajeId(dto.getObjetivoAprendizajeId());
        actividad.setTipo(dto.getTipo());
        actividad.setTitulo(dto.getTitulo());
        actividad.setOrden(dto.getOrden());
        actividad.setActualizadoEn(Instant.now());

        return ActividadDTO.fromModel(
                actividadRepository.save(actividad)
        );
    }

    @Transactional
    public ActividadDTO cambiarEstado(Long id, EstadoContenido estadoNuevo) {
        ActividadDesafio actividad = buscarActividad(id);
        EstadoContenido estadoActual = actividad.getEstado();

        boolean permitido = switch (estadoActual) {
            case BORRADOR -> estadoNuevo == EstadoContenido.EN_REVISION;
            case EN_REVISION -> estadoNuevo == EstadoContenido.AUTORIZADO;
            case AUTORIZADO -> estadoNuevo == EstadoContenido.PUBLICADO;
            case PUBLICADO -> estadoNuevo == EstadoContenido.DESHABILITADO;
            case DESHABILITADO -> false;
        };

        if (!permitido) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Transicion de estado no permitida"
            );
        }

        actividad.setEstado(estadoNuevo);
        actividad.setActualizadoEn(Instant.now());
        actividadRepository.save(actividad);

        TransicionPublicacion transicion = new TransicionPublicacion();
        transicion.setEventoId(UUID.randomUUID().toString());
        transicion.setRecursoTipo(TipoRecursoPublicable.ACTIVIDAD);
        transicion.setRecursoId(id);
        transicion.setEstadoAnterior(estadoActual);
        transicion.setEstadoNuevo(estadoNuevo);
        transicion.setUsuarioId(1L);
        transicion.setOcurridoEn(Instant.now());
        transicionRepository.save(transicion);

        return ActividadDTO.fromModel(actividad);
    }

    private ActividadDesafio buscarActividad(Long id) {
        return actividadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Actividad no encontrada"
                ));
    }
}