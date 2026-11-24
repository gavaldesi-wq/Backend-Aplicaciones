package cl.mpazpro.actividades.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import cl.mpazpro.actividades.dto.ActividadEstudianteDTO;
import cl.mpazpro.actividades.dto.OpcionDTO;
import cl.mpazpro.actividades.dto.PreguntaEstudianteDTO;
import cl.mpazpro.actividades.model.ActividadDesafio;
import cl.mpazpro.actividades.model.Alternativa;
import cl.mpazpro.actividades.model.EstadoContenido;
import cl.mpazpro.actividades.model.Pregunta;
import cl.mpazpro.actividades.repository.ActividadDesafioRepository;
import cl.mpazpro.actividades.repository.AlternativaRepository;
import cl.mpazpro.actividades.repository.PreguntaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstudianteActividadService {

    private final ActividadDesafioRepository actividadRepository;
    private final PreguntaRepository preguntaRepository;
    private final AlternativaRepository alternativaRepository;

    @Transactional(readOnly = true)
    public ActividadEstudianteDTO obtener(Long id) {
        ActividadDesafio actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Actividad no encontrada"
                ));

        if (actividad.getEstado() != EstadoContenido.PUBLICADO) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Actividad no encontrada"
            );
        }

        ActividadEstudianteDTO dto = new ActividadEstudianteDTO();
        dto.setId(actividad.getId());
        dto.setObjetivoAprendizajeId(actividad.getObjetivoAprendizajeId());
        dto.setTipo(actividad.getTipo());
        dto.setTitulo(actividad.getTitulo());
        dto.setPreguntas(preguntasDe(id));
        return dto;
    }

    private List<PreguntaEstudianteDTO> preguntasDe(Long actividadId) {
        return preguntaRepository
                .findByActividad_IdOrderByOrdenAsc(actividadId)
                .stream()
                .map(this::aPregunta)
                .toList();
    }

    private PreguntaEstudianteDTO aPregunta(Pregunta pregunta) {
        List<OpcionDTO> opciones = alternativaRepository
                .findByPregunta_IdOrderByOrdenAsc(pregunta.getId())
                .stream()
                .map(this::aOpcion)
                .toList();

        PreguntaEstudianteDTO dto = new PreguntaEstudianteDTO();
        dto.setId(pregunta.getId());
        dto.setEnunciado(pregunta.getEnunciado());
        dto.setTipo(pregunta.getTipo());
        dto.setOpciones(opciones);
        return dto;
    }

    private OpcionDTO aOpcion(Alternativa alternativa) {
        OpcionDTO dto = new OpcionDTO();
        dto.setId(alternativa.getId());
        dto.setTexto(alternativa.getTexto());
        return dto;
    }
}