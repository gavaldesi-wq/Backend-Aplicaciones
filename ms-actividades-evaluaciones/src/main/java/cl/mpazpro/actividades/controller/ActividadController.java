package cl.mpazpro.actividades.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.actividades.dto.ActividadDTO;
import cl.mpazpro.actividades.dto.PaginacionDTO;
import cl.mpazpro.actividades.dto.PreguntaDTO;
import cl.mpazpro.actividades.dto.TransicionEstadoDTO;
import cl.mpazpro.actividades.model.EstadoContenido;
import cl.mpazpro.actividades.service.ActividadService;
import cl.mpazpro.actividades.service.PreguntaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/utp/actividades")
@RequiredArgsConstructor
public class ActividadController {

    private final ActividadService actividadService;
    private final PreguntaService preguntaService;

    @GetMapping
    public ResponseEntity<PaginacionDTO<ActividadDTO>> getAll(
            @RequestParam(required = false) Long objetivoAprendizajeId,
            @RequestParam(required = false) EstadoContenido estado,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamano) {

        List<ActividadDTO> actividades =
                actividadService.getAll(objetivoAprendizajeId);

        if (estado != null) {
            actividades = actividades.stream()
                    .filter(actividad -> actividad.getEstado() == estado)
                    .toList();
        }

        return ResponseEntity.ok(
                PaginacionDTO.fromList(actividades, pagina, tamano)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                actividadService.getById(id)
        );
    }

    /** Solo UTP: incluye la alternativa correcta para poder editarla. */
    @GetMapping("/{id}/preguntas")
    public ResponseEntity<List<PreguntaDTO>> preguntas(@PathVariable Long id) {
        return ResponseEntity.ok(
                preguntaService.listarPorActividad(id)
        );
    }

    @PostMapping
    public ResponseEntity<ActividadDTO> save(
            @Valid @RequestBody ActividadDTO dto) {

        ActividadDTO creada = actividadService.save(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creada);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ActividadDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ActividadDTO dto) {

        return ResponseEntity.ok(
                actividadService.update(id, dto)
        );
    }

    @PostMapping("/{id}/transiciones")
    public ResponseEntity<ActividadDTO> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody TransicionEstadoDTO dto) {

        return ResponseEntity.ok(
                actividadService.cambiarEstado(id, dto.getEstadoNuevo())
        );
    }
}