
package cl.mpazpro.contenidos.service;

import cl.mpazpro.contenidos.dto.ObjetivoAprendizajeDTO;
import cl.mpazpro.contenidos.dto.ObjetivoElegibleDTO;
import cl.mpazpro.contenidos.model.ObjetivoAprendizaje;
import cl.mpazpro.contenidos.model.Unidad;
import cl.mpazpro.contenidos.model.EstadoContenido;

import cl.mpazpro.contenidos.repository.ObjetivoAprendizajeRepository;
import cl.mpazpro.contenidos.repository.UnidadRepository;
import cl.mpazpro.contenidos.client.HistorialAprobacionClient;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjetivoAprendizajeService {

    private final ObjetivoAprendizajeRepository objetivoRepository;
    private final UnidadRepository unidadRepository;
    private final HistorialAprobacionClient historialClient;

    @Transactional(readOnly = true)
    public List<ObjetivoAprendizajeDTO> getAll(Long unidadId) {

        List<ObjetivoAprendizaje> objetivos;

        if (unidadId != null) {
            objetivos = objetivoRepository
                    .findByUnidad_IdOrderByIdAsc(unidadId);
        } else {
            objetivos = objetivoRepository.findAll();
        }

        return objetivos.stream()
                .map(ObjetivoAprendizajeDTO::fromModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public ObjetivoAprendizajeDTO getById(Long id) {
        return ObjetivoAprendizajeDTO.fromModel(
                buscarObjetivo(id)
        );
    }

    @Transactional
    public ObjetivoAprendizajeDTO save(ObjetivoAprendizajeDTO dto) {

        Unidad unidad = buscarUnidad(dto.getUnidadId());

        ObjetivoAprendizaje objetivo = dto.toModel();

        objetivo.setUnidad(unidad);
        objetivo.setEstado(EstadoContenido.BORRADOR);

        return ObjetivoAprendizajeDTO.fromModel(
                objetivoRepository.save(objetivo)
        );
    }

    @Transactional
    public ObjetivoAprendizajeDTO update(
            Long id,
            ObjetivoAprendizajeDTO dto) {

        ObjetivoAprendizaje objetivo = buscarObjetivo(id);

        if (objetivo.getEstado() != EstadoContenido.BORRADOR) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Solo se pueden editar objetivos en BORRADOR"
            );
        }

        Unidad unidad = buscarUnidad(dto.getUnidadId());

        objetivo.setUnidad(unidad);
        objetivo.setCodigo(dto.getCodigo());
        objetivo.setDescripcion(dto.getDescripcion());
        objetivo.setEjeCurricular(dto.getEjeCurricular());
        objetivo.setTextoReferencia(dto.getTextoReferencia());
        objetivo.setOrden(dto.getOrden());

        return ObjetivoAprendizajeDTO.fromModel(
                objetivoRepository.save(objetivo)
        );
    }

    private ObjetivoAprendizaje buscarObjetivo(Long id) {
        return objetivoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Objetivo de aprendizaje no encontrado"
                ));
    }

    private Unidad buscarUnidad(Long id) {
        return unidadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unidad no encontrada"
                ));
    }



        @Transactional
        public ObjetivoAprendizajeDTO cambiarEstado(
                Long id,
                EstadoContenido estadoNuevo,
                String authorization
        ) {
        ObjetivoAprendizaje objetivo = objetivoRepository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Objetivo de aprendizaje no encontrado"
        ));

        EstadoContenido estadoActual = objetivo.getEstado();

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

        if (estadoNuevo == EstadoContenido.PUBLICADO
                && objetivo.getUnidad().getEstado()
                        != EstadoContenido.PUBLICADO) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "La unidad debe estar PUBLICADA"
                );
        }

        objetivo.setEstado(estadoNuevo);
        objetivoRepository.saveAndFlush(objetivo);

        historialClient.registrarObjetivo(
                id,
                estadoActual,
                estadoNuevo,
                authorization
        );

        return ObjetivoAprendizajeDTO.fromModel(objetivo);
        }

        @Transactional(readOnly = true)
        public List<ObjetivoElegibleDTO> listarElegibles() {
            return objetivoRepository.findAll().stream()
                    .filter(objetivo -> objetivo.getEstado() == EstadoContenido.AUTORIZADO
                            || objetivo.getEstado() == EstadoContenido.PUBLICADO)
                    .map(objetivo -> {
                        ObjetivoElegibleDTO dto = new ObjetivoElegibleDTO();
                        dto.setId(objetivo.getId());
                        dto.setUnidadId(objetivo.getUnidad().getId());
                        dto.setCodigo(objetivo.getCodigo());
                        dto.setDescripcion(objetivo.getDescripcion());
                        dto.setEstado(objetivo.getEstado().name());
                        return dto;
                    })
                    .toList();
        }

}
