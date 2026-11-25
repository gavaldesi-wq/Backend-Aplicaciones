package cl.mpazpro.progreso.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import cl.mpazpro.progreso.dto.CrearInformeDTO;
import cl.mpazpro.progreso.dto.InformeDTO;
import cl.mpazpro.progreso.dto.ValidacionInformeDTO;
import cl.mpazpro.progreso.model.Asignacion;
import cl.mpazpro.progreso.model.EnvioActividad;
import cl.mpazpro.progreso.model.EstadoInforme;
import cl.mpazpro.progreso.model.InformePedagogico;
import cl.mpazpro.progreso.model.IntentoPrueba;
import cl.mpazpro.progreso.model.ObservacionDocente;
import cl.mpazpro.progreso.model.Validacion;
import cl.mpazpro.progreso.repository.AsignacionRepository;
import cl.mpazpro.progreso.repository.EnvioActividadRepository;
import cl.mpazpro.progreso.repository.InformePedagogicoRepository;
import cl.mpazpro.progreso.repository.IntentoPruebaRepository;
import cl.mpazpro.progreso.repository.ObservacionDocenteRepository;
import cl.mpazpro.progreso.repository.ProgresoRepository;
import cl.mpazpro.progreso.repository.RespuestaEstudianteRepository;
import cl.mpazpro.progreso.repository.ValidacionRepository;
import cl.mpazpro.progreso.security.SesionActual;
import lombok.RequiredArgsConstructor;
import java.io.ByteArrayOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
@Service
@RequiredArgsConstructor
public class InformeService {


    private final InformePedagogicoRepository informeRepository;
    private final AsignacionRepository asignacionRepository;
    private final ProgresoRepository progresoRepository;
    private final EnvioActividadRepository envioRepository;
    private final IntentoPruebaRepository intentoRepository;
    private final RespuestaEstudianteRepository respuestaRepository;
    private final ValidacionRepository validacionRepository;
    private final ObservacionDocenteRepository observacionRepository;
    private final SesionActual sesionActual;

    @Transactional
    public InformeDTO generar(CrearInformeDTO dto) {
        long docenteId = sesionActual.docenteId();
        if (!estaAsignado(dto, docenteId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Asignacion no encontrada"
            );
        }

        int avance = progresoRepository
                .findByEstudianteIdAndObjetivoAprendizajeId(
                        dto.getEstudianteId(),
                        dto.getObjetivoAprendizajeId()
                )
                .map(progreso -> progreso.getPorcentajeAvance())
                .orElse(0);

        List<EnvioActividad> envios = envioRepository
                .findByEstudianteIdOrderByRegistradoEnAsc(dto.getEstudianteId())
                .stream()
                .filter(envio -> dto.getObjetivoAprendizajeId()
                        .equals(envio.getObjetivoAprendizajeId()))
                .toList();

        List<IntentoPrueba> intentos = intentoRepository
                .findByEstudianteIdOrderByIniciadoEnAsc(dto.getEstudianteId());

        List<Long> porReforzar = new ArrayList<>();
        for (EnvioActividad envio : envios) {
            juntarIncorrectas(
                    porReforzar,
                    respuestaRepository.findByEnvioActividad_Id(envio.getId())
            );
        }
        for (IntentoPrueba intento : intentos) {
            juntarIncorrectas(
                    porReforzar,
                    respuestaRepository.findByIntento_Id(intento.getId())
            );
        }

        InformePedagogico informe = new InformePedagogico();
        informe.setCursoId(dto.getCursoId());
        informe.setEstudianteId(dto.getEstudianteId());
        informe.setObjetivoAprendizajeId(dto.getObjetivoAprendizajeId());
        informe.setDocenteId(docenteId);
        informe.setEstado(EstadoInforme.PENDIENTE_VALIDACION);
        informe.setResumen(resumen(dto, avance, envios.size(), intentos.size()));
        informe.setAreasRefuerzo(areas(porReforzar));
        informe.setCreadoEn(Instant.now());

        return InformeDTO.fromModel(informeRepository.save(informe));
    }

    @Transactional(readOnly = true)
    public InformeDTO obtener(Long id) {
        return InformeDTO.fromModel(buscarPropio(id));
    }
    
    private InformePedagogico buscarPropio(Long id) {
        long docenteId = sesionActual.docenteId();
        InformePedagogico informe = informeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Informe no encontrado"
                ));
        if (!Long.valueOf(docenteId).equals(informe.getDocenteId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Informe no encontrado"
            );
        }
        return informe;
    }

    public byte[] descargar(Long id) {
        InformeDTO informe = obtener(id);
    
        try {
            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            Document documento = new Document();
            PdfWriter.getInstance(documento, salida);
            documento.open();
            documento.add(new Paragraph("Informe pedagogico " + informe.getId()));
            documento.add(new Paragraph("Estudiante: " + informe.getEstudianteId()));
            documento.add(new Paragraph("Curso: " + informe.getCursoId()));
            documento.add(new Paragraph("Objetivo: " + informe.getObjetivoAprendizajeId()));
            documento.add(new Paragraph("Estado: " + informe.getEstado()));
            documento.add(new Paragraph("Resumen: " + informe.getResumen()));
            documento.add(new Paragraph(
                    "Areas de refuerzo: " + (informe.getAreasRefuerzo() == null
                            ? "Sin areas de refuerzo."
                            : informe.getAreasRefuerzo())
            ));
            documento.close();
            return salida.toByteArray();
        } catch (DocumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo generar el PDF"
            );
        }
    }
    
    @Transactional
    public InformeDTO validar(Long id, ValidacionInformeDTO dto) {
        InformePedagogico informe = buscarPropio(id);
    
        if (dto.getEstado() != EstadoInforme.VALIDADO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Solo se puede pasar el informe a VALIDADO"
            );
        }
    
        if (informe.getEstado() == EstadoInforme.VALIDADO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El informe ya fue validado"
            );
        }
    
        Instant ahora = Instant.now();
        informe.setEstado(EstadoInforme.VALIDADO);
        informeRepository.save(informe);
    
        Validacion validacion = new Validacion();
        validacion.setInforme(informe);
        validacion.setDocenteId(informe.getDocenteId());
        validacion.setEstado(EstadoInforme.VALIDADO);
        validacion.setValidadoEn(ahora);
        validacionRepository.save(validacion);
    
        if (dto.getObservacion() != null && !dto.getObservacion().isBlank()) {
            ObservacionDocente observacion = new ObservacionDocente();
            observacion.setInforme(informe);
            observacion.setDocenteId(informe.getDocenteId());
            observacion.setTexto(dto.getObservacion().trim());
            observacion.setCreadoEn(ahora);
            observacionRepository.save(observacion);
        }
    
        return InformeDTO.fromModel(informe);
    }

    private boolean estaAsignado(CrearInformeDTO dto, long docenteId) {
        return asignacionRepository
                .findByCursoIdAndObjetivoAprendizajeId(
                        dto.getCursoId(),
                        dto.getObjetivoAprendizajeId()
                )
                .stream()
                .filter(Asignacion::isActiva)
                .filter(asignacion -> Long.valueOf(docenteId).equals(asignacion.getDocenteId()))
                .anyMatch(asignacion -> asignacion.getEstudianteId() == null
                        || dto.getEstudianteId().equals(asignacion.getEstudianteId()));
    }

    private void juntarIncorrectas(
            List<Long> destino,
            List<cl.mpazpro.progreso.model.RespuestaEstudiante> respuestas) {

        for (var respuesta : respuestas) {
            if (Boolean.FALSE.equals(respuesta.getCorrecta())
                    && !destino.contains(respuesta.getPreguntaId())) {
                destino.add(respuesta.getPreguntaId());
            }
        }
    }

    private String resumen(CrearInformeDTO dto, int avance, int envios, int intentos) {
        return "El estudiante " + dto.getEstudianteId()
                + " tiene un avance de " + avance
                + "% en el objetivo " + dto.getObjetivoAprendizajeId()
                + ". Practicas registradas: " + envios
                + ". Intentos de prueba: " + intentos + ".";
    }

    private String areas(List<Long> preguntas) {
        if (preguntas.isEmpty()) {
            return null;
        }
        return "Reforzar las preguntas: " + preguntas + ".";
    }
}