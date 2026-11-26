package cl.mpazpro.actividades.service;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import cl.mpazpro.actividades.dto.AlternativaDTO;
import cl.mpazpro.actividades.dto.PreguntaDTO;
import cl.mpazpro.actividades.model.ActividadDesafio;
import cl.mpazpro.actividades.model.Alternativa;
import cl.mpazpro.actividades.model.EstadoContenido;
import cl.mpazpro.actividades.model.Pregunta;
import cl.mpazpro.actividades.model.PruebaFinal;
import cl.mpazpro.actividades.model.TipoActividad;
import cl.mpazpro.actividades.repository.ActividadDesafioRepository;
import cl.mpazpro.actividades.repository.AlternativaRepository;
import cl.mpazpro.actividades.repository.PreguntaRepository;
import cl.mpazpro.actividades.repository.PruebaFinalRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PreguntaService {

    private final PreguntaRepository preguntaRepository;
    private final AlternativaRepository alternativaRepository;
    private final ActividadDesafioRepository actividadRepository;
    private final PruebaFinalRepository pruebaFinalRepository;

    @Transactional
    public PreguntaDTO save(PreguntaDTO dto) {
        if (dto.getTipo() != TipoActividad.SELECCION) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Por ahora solo se crean preguntas de seleccion"
            );
        }

        long correctas = dto.getOpciones().stream()
                .filter(opcion -> Boolean.TRUE.equals(opcion.getEsCorrecta()))
                .count();

        if (correctas != 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La pregunta de seleccion debe tener una alternativa correcta"
            );
        }

        boolean tieneActividad = dto.getActividadId() != null;
        boolean tienePrueba = dto.getPruebaFinalId() != null;
        if (tieneActividad == tienePrueba) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La pregunta debe pertenecer a una actividad o a una prueba"
            );
        }

        Instant ahora = Instant.now();
        Pregunta pregunta = new Pregunta();
        pregunta.setEnunciado(dto.getEnunciado());
        pregunta.setTipo(dto.getTipo());
        pregunta.setOrden(dto.getOrden());
        pregunta.setExplicacion(dto.getExplicacion());
        pregunta.setCreadoEn(ahora);
        pregunta.setActualizadoEn(ahora);

        if (tieneActividad) {
            ActividadDesafio actividad = actividadRepository
                    .findById(dto.getActividadId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Actividad no encontrada"
                    ));
            if (actividad.getEstado() != EstadoContenido.BORRADOR) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Solo se pueden crear preguntas de una actividad en BORRADOR"
                );
            }
            pregunta.setActividad(actividad);
        } else {
            PruebaFinal prueba = pruebaFinalRepository
                    .findById(dto.getPruebaFinalId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Prueba no encontrada"
                    ));
            if (prueba.getEstado() != EstadoContenido.BORRADOR) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Solo se pueden crear preguntas de una prueba en BORRADOR"
                );
            }
            pregunta.setPruebaFinal(prueba);
        }

        pregunta = preguntaRepository.save(pregunta);

        for (AlternativaDTO opcion : dto.getOpciones()) {
            Alternativa alternativa = new Alternativa();
            alternativa.setPregunta(pregunta);
            alternativa.setTexto(opcion.getTexto());
            alternativa.setEsCorrecta(opcion.getEsCorrecta());
            alternativa.setOrden(opcion.getOrden());
            alternativaRepository.save(alternativa);
        }

        return armarRespuesta(pregunta.getId());
    }

    @Transactional
    public PreguntaDTO update(Long id, PreguntaDTO dto) {
        Pregunta pregunta = preguntaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pregunta no encontrada"
                ));

        if (pregunta.getActividad() != null) {
            if (pregunta.getActividad().getEstado() != EstadoContenido.BORRADOR) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Solo se pueden editar preguntas de una actividad en BORRADOR"
                );
            }
        } else if (pregunta.getPruebaFinal() != null) {
            if (pregunta.getPruebaFinal().getEstado() != EstadoContenido.BORRADOR) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Solo se pueden editar preguntas de una prueba en BORRADOR"
                );
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La pregunta no pertenece a una actividad ni a una prueba"
            );
        }

        if (dto.getTipo() != TipoActividad.SELECCION) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Por ahora solo se editan preguntas de seleccion"
            );
        }

        long correctas = dto.getOpciones().stream()
                .filter(opcion -> Boolean.TRUE.equals(opcion.getEsCorrecta()))
                .count();

        if (correctas != 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La pregunta de seleccion debe tener una alternativa correcta"
            );
        }

        pregunta.setEnunciado(dto.getEnunciado());
        pregunta.setOrden(dto.getOrden());
        pregunta.setExplicacion(dto.getExplicacion());
        pregunta.setActualizadoEn(Instant.now());
        preguntaRepository.save(pregunta);

        List<Alternativa> anteriores = alternativaRepository
                .findByPregunta_IdOrderByOrdenAsc(id);
        alternativaRepository.deleteAll(anteriores);

        for (AlternativaDTO opcion : dto.getOpciones()) {
            Alternativa alternativa = new Alternativa();
            alternativa.setPregunta(pregunta);
            alternativa.setTexto(opcion.getTexto());
            alternativa.setEsCorrecta(opcion.getEsCorrecta());
            alternativa.setOrden(opcion.getOrden());
            alternativaRepository.save(alternativa);
        }

        return armarRespuesta(id);
    }

    @Transactional(readOnly = true)
    public List<PreguntaDTO> listarPorActividad(Long actividadId) {
        if (!actividadRepository.existsById(actividadId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Actividad no encontrada"
            );
        }
        return preguntaRepository.findByActividad_IdOrderByOrdenAsc(actividadId)
                .stream()
                .map(pregunta -> armarRespuesta(pregunta.getId()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PreguntaDTO> listarPorPrueba(Long pruebaFinalId) {
        if (!pruebaFinalRepository.existsById(pruebaFinalId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Prueba no encontrada"
            );
        }
        return preguntaRepository.findByPruebaFinal_IdOrderByOrdenAsc(pruebaFinalId)
                .stream()
                .map(pregunta -> armarRespuesta(pregunta.getId()))
                .toList();
    }

    private PreguntaDTO armarRespuesta(Long preguntaId) {
        Pregunta pregunta = preguntaRepository.findById(preguntaId).orElseThrow();
        List<AlternativaDTO> opciones = alternativaRepository
                .findByPregunta_IdOrderByOrdenAsc(preguntaId)
                .stream()
                .map(this::aAlternativa)
                .toList();

        PreguntaDTO dto = new PreguntaDTO();
        dto.setId(pregunta.getId());
        if (pregunta.getActividad() != null) {
            dto.setActividadId(pregunta.getActividad().getId());
        }
        if (pregunta.getPruebaFinal() != null) {
            dto.setPruebaFinalId(pregunta.getPruebaFinal().getId());
        }
        dto.setEnunciado(pregunta.getEnunciado());
        dto.setTipo(pregunta.getTipo());
        dto.setOrden(pregunta.getOrden());
        dto.setExplicacion(pregunta.getExplicacion());
        dto.setOpciones(opciones);
        return dto;
    }

    private AlternativaDTO aAlternativa(Alternativa alternativa) {
        AlternativaDTO dto = new AlternativaDTO();
        dto.setId(alternativa.getId());
        dto.setTexto(alternativa.getTexto());
        dto.setEsCorrecta(alternativa.isEsCorrecta());
        dto.setOrden(alternativa.getOrden());
        return dto;
    }
}