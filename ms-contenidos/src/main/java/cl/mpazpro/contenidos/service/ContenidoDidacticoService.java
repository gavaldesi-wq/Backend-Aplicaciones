
package cl.mpazpro.contenidos.service;

import cl.mpazpro.contenidos.dto.ContenidoDidacticoDTO;
import cl.mpazpro.contenidos.model.ContenidoDidactico;
import cl.mpazpro.contenidos.model.ObjetivoAprendizaje;
import cl.mpazpro.contenidos.model.EstadoContenido;

import cl.mpazpro.contenidos.repository.ContenidoDidacticoRepository;
import cl.mpazpro.contenidos.repository.ObjetivoAprendizajeRepository;
import cl.mpazpro.contenidos.client.HistorialAprobacionClient;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContenidoDidacticoService {

    private final ContenidoDidacticoRepository contenidoRepository;
    private final ObjetivoAprendizajeRepository objetivoRepository;
    private final HistorialAprobacionClient historialClient;

    @Transactional(readOnly = true)
    public List<ContenidoDidacticoDTO> getAll(
            Long objetivoAprendizajeId) {

        List<ContenidoDidactico> contenidos;

        if (objetivoAprendizajeId != null) {
            contenidos = contenidoRepository
                    .findByObjetivoAprendizaje_IdOrderByOrdenAsc(
                            objetivoAprendizajeId);
        } else {
            contenidos = contenidoRepository.findAll();
        }

        return contenidos.stream()
                .map(ContenidoDidacticoDTO::fromModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public ContenidoDidacticoDTO getById(Long id) {
        return ContenidoDidacticoDTO.fromModel(
                buscarContenido(id)
        );
    }

    @Transactional
    public ContenidoDidacticoDTO save(ContenidoDidacticoDTO dto) {

        ObjetivoAprendizaje objetivo =
                buscarObjetivo(dto.getObjetivoAprendizajeId());

        ContenidoDidactico contenido = dto.toModel();

        contenido.setObjetivoAprendizaje(objetivo);
        contenido.setEstado(EstadoContenido.BORRADOR);

        return ContenidoDidacticoDTO.fromModel(
                contenidoRepository.save(contenido)
        );
    }

    @Transactional
    public ContenidoDidacticoDTO update(
            Long id,
            ContenidoDidacticoDTO dto) {

        ContenidoDidactico contenido = buscarContenido(id);

        if (contenido.getEstado() != EstadoContenido.BORRADOR) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Solo se pueden editar contenidos en BORRADOR"
            );
        }

        ObjetivoAprendizaje objetivo =
                buscarObjetivo(dto.getObjetivoAprendizajeId());

        contenido.setObjetivoAprendizaje(objetivo);
        contenido.setTitulo(dto.getTitulo());
        contenido.setExplicacionEscrita(dto.getExplicacionEscrita());
        contenido.setArchivoAudio(dto.getArchivoAudio());
        contenido.setEjemplosGuiados(dto.getEjemplosGuiados());
        contenido.setInstrucciones(dto.getInstrucciones());
        contenido.setOrden(dto.getOrden());
        contenido.setResponsableOrigen(dto.getResponsableOrigen());
        contenido.setLicencia(dto.getLicencia());

        contenido.getImagenes().clear();

        if (dto.getImagenes() != null) {
            contenido.getImagenes().addAll(dto.getImagenes());
        }

        return ContenidoDidacticoDTO.fromModel(
                contenidoRepository.save(contenido)
        );
    }


        @Transactional
        public ContenidoDidacticoDTO cambiarEstado(
                Long id,
                EstadoContenido estadoNuevo,
                String authorization
        ) {
        ContenidoDidactico contenido = buscarContenido(id);

        EstadoContenido estadoActual = contenido.getEstado();

        boolean permitido = switch (estadoActual) {
                case BORRADOR ->
                        estadoNuevo == EstadoContenido.EN_REVISION;

                case EN_REVISION ->
                        estadoNuevo == EstadoContenido.AUTORIZADO;

                case AUTORIZADO ->
                        estadoNuevo == EstadoContenido.PUBLICADO;

                case PUBLICADO ->
                        estadoNuevo == EstadoContenido.DESHABILITADO;

                case DESHABILITADO -> false;
        };

        if (!permitido) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Transicion de estado no permitida"
                );
        }

        if (estadoNuevo == EstadoContenido.PUBLICADO) {
                ObjetivoAprendizaje objetivo =
                        contenido.getObjetivoAprendizaje();

                if (objetivo.getEstado() != EstadoContenido.PUBLICADO) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "El objetivo debe estar PUBLICADO"
                );
                }

                if (objetivo.getUnidad().getEstado()
                        != EstadoContenido.PUBLICADO) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "La unidad debe estar PUBLICADA"
                );
                }
        }

        contenido.setEstado(estadoNuevo);
        contenidoRepository.saveAndFlush(contenido);

        historialClient.registrarContenido(
                id,
                estadoActual,
                estadoNuevo,
                authorization
        );

        return ContenidoDidacticoDTO.fromModel(contenido);
        }


    private ContenidoDidactico buscarContenido(Long id) {
        return contenidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Contenido didactico no encontrado"
                ));
    }

    private ObjetivoAprendizaje buscarObjetivo(Long id) {
        return objetivoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Objetivo de aprendizaje no encontrado"
                ));
    }
}
