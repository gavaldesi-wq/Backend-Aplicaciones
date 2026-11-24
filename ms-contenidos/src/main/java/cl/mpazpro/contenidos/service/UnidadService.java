
package cl.mpazpro.contenidos.service;

import cl.mpazpro.contenidos.dto.UnidadDTO;
import cl.mpazpro.contenidos.model.Asignatura;
import cl.mpazpro.contenidos.model.Unidad;
import cl.mpazpro.contenidos.model.EstadoContenido;

import cl.mpazpro.contenidos.repository.AsignaturaRepository;
import cl.mpazpro.contenidos.repository.UnidadRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import cl.mpazpro.contenidos.client.HistorialAprobacionClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnidadService {

    private final UnidadRepository unidadRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final HistorialAprobacionClient historialClient;

    @Transactional(readOnly = true)
    public List<UnidadDTO> getAll(Long asignaturaId) {

        List<Unidad> unidades;

        if (asignaturaId != null) {
            unidades = unidadRepository
                    .findByAsignatura_IdOrderByOrdenAsc(asignaturaId);
        } else {
            unidades = unidadRepository.findAll();
        }

        return unidades.stream()
                .map(UnidadDTO::fromModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public UnidadDTO getById(Long id) {
        Unidad unidad = buscarUnidad(id);
        return UnidadDTO.fromModel(unidad);
    }

    @Transactional
    public UnidadDTO save(UnidadDTO dto) {

        Asignatura asignatura = asignaturaRepository
                .findById(dto.getAsignaturaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Asignatura no encontrada"
                ));

        Unidad unidad = dto.toModel();

        unidad.setAsignatura(asignatura);
        unidad.setEstado(EstadoContenido.BORRADOR);

        return UnidadDTO.fromModel(
                unidadRepository.save(unidad)
        );
    }


                @Transactional
        public UnidadDTO cambiarEstado(
                Long id,
                EstadoContenido estadoNuevo,
                String authorization
        ) {
        Unidad unidad = buscarUnidad(id);

        EstadoContenido estadoActual = unidad.getEstado();

        boolean permitido = switch (estadoActual) {
                case BORRADOR ->
                        estadoNuevo == EstadoContenido.EN_REVISION;

                case EN_REVISION ->
                        estadoNuevo == EstadoContenido.AUTORIZADO;

                case AUTORIZADO ->
                        estadoNuevo == EstadoContenido.PUBLICADO;

                case PUBLICADO ->
                        estadoNuevo == EstadoContenido.DESHABILITADO;

                case DESHABILITADO -> estadoNuevo == EstadoContenido.PUBLICADO;
        };

        if (!permitido) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Transicion de estado no permitida"
                );
        }

        unidad.setEstado(estadoNuevo);
        unidadRepository.saveAndFlush(unidad);

        historialClient.registrarUnidad(
                id,
                estadoActual,
                estadoNuevo,
                authorization
        );

        return UnidadDTO.fromModel(unidad);
        }


    @Transactional
    public UnidadDTO update(Long id, UnidadDTO dto) {

        Unidad unidad = buscarUnidad(id);

        if (unidad.getEstado() != EstadoContenido.BORRADOR) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Solo se pueden editar unidades en BORRADOR"
            );
        }

        Asignatura asignatura = asignaturaRepository
                .findById(dto.getAsignaturaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Asignatura no encontrada"
                ));

        unidad.setAsignatura(asignatura);
        unidad.setTitulo(dto.getTitulo());
        unidad.setOrden(dto.getOrden());

        return UnidadDTO.fromModel(
                unidadRepository.save(unidad)
        );
    }

    private Unidad buscarUnidad(Long id) {
        return unidadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unidad no encontrada"
                ));
    }
}
