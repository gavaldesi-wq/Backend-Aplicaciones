package cl.mpazpro.actividades.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import cl.mpazpro.actividades.dto.PruebaFinalDTO;
import cl.mpazpro.actividades.model.EstadoContenido;
import cl.mpazpro.actividades.model.PruebaFinal;
import cl.mpazpro.actividades.model.TipoRecursoPublicable;
import cl.mpazpro.actividades.model.TransicionPublicacion;
import cl.mpazpro.actividades.repository.PruebaFinalRepository;
import cl.mpazpro.actividades.repository.TransicionPublicacionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PruebaFinalService {

    private final PruebaFinalRepository pruebaRepository;
    private final TransicionPublicacionRepository transicionRepository;

    @Transactional(readOnly = true)
    public List<PruebaFinalDTO> getAll(Long objetivoAprendizajeId) {
        List<PruebaFinal> pruebas;

        if (objetivoAprendizajeId != null) {
            pruebas = pruebaRepository
                    .findByObjetivoAprendizajeId(objetivoAprendizajeId);
        } else {
            pruebas = pruebaRepository.findAll();
        }

        return pruebas.stream()
                .map(PruebaFinalDTO::fromModel)
                .toList();
    }

    @Transactional
    public PruebaFinalDTO save(PruebaFinalDTO dto) {
        PruebaFinal prueba = dto.toModel();

        if (prueba.getDisponibleDesde() != null
                && prueba.getDisponibleHasta() != null
                && !prueba.getDisponibleDesde().isBefore(prueba.getDisponibleHasta())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "disponibleDesde debe ser anterior a disponibleHasta"
            );
        }

        prueba.setObjetivoAprendizajeId(dto.getObjetivoAprendizajeId());
        prueba.setEstado(EstadoContenido.BORRADOR);
        prueba.setCreadoPorUsuarioId(1L);
        prueba.setCreadoEn(Instant.now());
        prueba.setActualizadoEn(Instant.now());

        return PruebaFinalDTO.fromModel(
                pruebaRepository.save(prueba)
        );
    }

    @Transactional(readOnly = true)
    public PruebaFinalDTO getById(Long id) {
        PruebaFinal prueba = buscarPrueba(id);
        return PruebaFinalDTO.fromModel(prueba);
    }

    @Transactional
    public PruebaFinalDTO update(Long id, PruebaFinalDTO dto) {
        PruebaFinal prueba = buscarPrueba(id);

        if (prueba.getEstado() != EstadoContenido.BORRADOR) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Solo se pueden editar pruebas en BORRADOR"
            );
        }

        if (dto.getDisponibleDesde() != null
                && dto.getDisponibleHasta() != null
                && !dto.getDisponibleDesde().isBefore(dto.getDisponibleHasta())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "disponibleDesde debe ser anterior a disponibleHasta"
            );
        }

        prueba.setObjetivoAprendizajeId(dto.getObjetivoAprendizajeId());
        prueba.setTitulo(dto.getTitulo());
        prueba.setDisponibleDesde(dto.getDisponibleDesde());
        prueba.setDisponibleHasta(dto.getDisponibleHasta());
        prueba.setLimiteIntentos(dto.getLimiteIntentos());
        prueba.setActualizadoEn(Instant.now());

        return PruebaFinalDTO.fromModel(
                pruebaRepository.save(prueba)
        );
    }
    @Transactional
    public PruebaFinalDTO cambiarEstado(Long id, EstadoContenido estadoNuevo) {
        PruebaFinal prueba = buscarPrueba(id);
        EstadoContenido estadoActual = prueba.getEstado();
    
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
    
        prueba.setEstado(estadoNuevo);
        prueba.setActualizadoEn(Instant.now());
        pruebaRepository.save(prueba);
    
        TransicionPublicacion transicion = new TransicionPublicacion();
        transicion.setEventoId(UUID.randomUUID().toString());
        transicion.setRecursoTipo(TipoRecursoPublicable.PRUEBA_FINAL);
        transicion.setRecursoId(id);
        transicion.setEstadoAnterior(estadoActual);
        transicion.setEstadoNuevo(estadoNuevo);
        transicion.setUsuarioId(1L);
        transicion.setOcurridoEn(Instant.now());
        transicionRepository.save(transicion);
    
        return PruebaFinalDTO.fromModel(prueba);
    }
    
    private PruebaFinal buscarPrueba(Long id) {
        return pruebaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Prueba no encontrada"
                ));
    }
}