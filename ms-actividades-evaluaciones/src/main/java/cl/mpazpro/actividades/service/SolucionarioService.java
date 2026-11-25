package cl.mpazpro.actividades.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import cl.mpazpro.actividades.dto.SolucionActividadDTO;
import cl.mpazpro.actividades.dto.SolucionPreguntaDTO;
import cl.mpazpro.actividades.dto.SolucionPruebaDTO;
import cl.mpazpro.actividades.model.ActividadDesafio;
import cl.mpazpro.actividades.model.Alternativa;
import cl.mpazpro.actividades.model.EstadoContenido;
import cl.mpazpro.actividades.model.Pregunta;
import cl.mpazpro.actividades.model.PruebaFinal;
import cl.mpazpro.actividades.repository.ActividadDesafioRepository;
import cl.mpazpro.actividades.repository.AlternativaRepository;
import cl.mpazpro.actividades.repository.PreguntaRepository;
import cl.mpazpro.actividades.repository.PruebaFinalRepository;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class SolucionarioService {

    private final ActividadDesafioRepository actividadRepository;
    private final PreguntaRepository preguntaRepository;
    private final AlternativaRepository alternativaRepository;
    private final PruebaFinalRepository pruebaFinalRepository;

    @Transactional(readOnly = true)
    public SolucionActividadDTO obtener(Long actividadId) {
        ActividadDesafio actividad = actividadRepository.findById(actividadId)
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

        List<SolucionPreguntaDTO> preguntas = preguntaRepository
                .findByActividad_IdOrderByOrdenAsc(actividadId)
                .stream()
                .map(this::aSolucion)
                .toList();

        SolucionActividadDTO dto = new SolucionActividadDTO();
        dto.setActividadId(actividadId);
        dto.setObjetivoAprendizajeId(actividad.getObjetivoAprendizajeId());
        dto.setPreguntas(preguntas);
        return dto;
    }

    @Transactional(readOnly = true)
    public SolucionPruebaDTO obtenerPrueba(Long pruebaId) {
        PruebaFinal prueba = pruebaFinalRepository.findById(pruebaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Prueba no encontrada"
                ));
    
        if (prueba.getEstado() != EstadoContenido.PUBLICADO) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Prueba no encontrada"
            );
        }
    
        List<SolucionPreguntaDTO> preguntas = preguntaRepository
                .findByPruebaFinal_IdOrderByOrdenAsc(pruebaId)
                .stream()
                .map(this::aSolucion)
                .toList();
    
        SolucionPruebaDTO dto = new SolucionPruebaDTO();
        dto.setPruebaId(pruebaId);
        dto.setPreguntas(preguntas);
        return dto;
    }

    private SolucionPreguntaDTO aSolucion(Pregunta pregunta) {
        Alternativa correcta = alternativaRepository
                .findByPregunta_IdOrderByOrdenAsc(pregunta.getId())
                .stream()
                .filter(Alternativa::isEsCorrecta)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "La pregunta no tiene solucionario"
                ));

        SolucionPreguntaDTO dto = new SolucionPreguntaDTO();
        dto.setPreguntaId(pregunta.getId());
        dto.setTipo(pregunta.getTipo());
        dto.setAlternativaCorrectaId(correcta.getId());
        dto.setExplicacion(pregunta.getExplicacion());
        return dto;
    }
}