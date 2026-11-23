package cl.mpazpro.contenidos.service;

import cl.mpazpro.contenidos.dto.ContenidoDidacticoDTO;
import cl.mpazpro.contenidos.dto.ObjetivoAprendizajeDTO;
import cl.mpazpro.contenidos.dto.UnidadDTO;
import cl.mpazpro.contenidos.model.ContenidoDidactico;
import cl.mpazpro.contenidos.model.EstadoContenido;
import cl.mpazpro.contenidos.model.ObjetivoAprendizaje;
import cl.mpazpro.contenidos.model.Unidad;
import cl.mpazpro.contenidos.repository.ContenidoDidacticoRepository;
import cl.mpazpro.contenidos.repository.ObjetivoAprendizajeRepository;
import cl.mpazpro.contenidos.repository.UnidadRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstudianteService {

    private final UnidadRepository unidadRepository;
    private final ObjetivoAprendizajeRepository objetivoRepository;
    private final ContenidoDidacticoRepository contenidoRepository;

    @Transactional(readOnly = true)
    public List<UnidadDTO> listarUnidades(Long asignaturaId) {
        return unidadRepository.findAll().stream()
                .filter(unidad ->
                        unidad.getAsignatura().getId()
                                .equals(asignaturaId))
                .filter(unidad ->
                        unidad.getEstado()
                                == EstadoContenido.PUBLICADO)
                .sorted(Comparator.comparing(Unidad::getOrden))
                .map(UnidadDTO::fromModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ObjetivoAprendizajeDTO> listarObjetivos(
            Long unidadId
    ) {
        Unidad unidad = obtenerUnidadPublicada(unidadId);

        return objetivoRepository.findAll().stream()
                .filter(objetivo ->
                        objetivo.getUnidad().getId()
                                .equals(unidad.getId()))
                .filter(objetivo ->
                        objetivo.getEstado()
                                == EstadoContenido.PUBLICADO)
                .sorted(Comparator.comparing(
                        ObjetivoAprendizaje::getOrden))
                .map(ObjetivoAprendizajeDTO::fromModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ContenidoDidacticoDTO> listarContenidos(
            Long objetivoId
    ) {
        ObjetivoAprendizaje objetivo = objetivoRepository
                .findById(objetivoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Objetivo no disponible"
                ));

        if (objetivo.getEstado() != EstadoContenido.PUBLICADO
                || objetivo.getUnidad().getEstado()
                        != EstadoContenido.PUBLICADO) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Objetivo no disponible"
            );
        }

        return contenidoRepository.findAll().stream()
                .filter(contenido ->
                        contenido.getObjetivoAprendizaje().getId()
                                .equals(objetivoId))
                .filter(contenido ->
                        contenido.getEstado()
                                == EstadoContenido.PUBLICADO)
                .sorted(Comparator.comparing(
                        ContenidoDidactico::getOrden))
                .map(ContenidoDidacticoDTO::fromModel)
                .toList();
    }

    private Unidad obtenerUnidadPublicada(Long id) {
        Unidad unidad = unidadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unidad no disponible"
                ));

        if (unidad.getEstado() != EstadoContenido.PUBLICADO) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Unidad no disponible"
            );
        }

        return unidad;
    }
}
