package cl.mpazpro.actividades.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.actividades.dto.ActividadEstudianteDTO;
import cl.mpazpro.actividades.service.EstudianteActividadService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/estudiante/actividades")
@RequiredArgsConstructor
public class EstudianteActividadController {

    private final EstudianteActividadService estudianteActividadService;

    @GetMapping("/{id}")
    public ResponseEntity<ActividadEstudianteDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                estudianteActividadService.obtener(id)
        );
    }
}