package cl.mpazpro.actividades.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.actividades.dto.ActividadEstudianteDTO;
import cl.mpazpro.actividades.service.EstudianteActividadService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/estudiante/objetivos")
@RequiredArgsConstructor
public class EstudianteObjetivoController {

    private final EstudianteActividadService estudianteActividadService;

    @GetMapping("/{id}/actividades")
    public ResponseEntity<List<ActividadEstudianteDTO>> listar(@PathVariable Long id) {
        return ResponseEntity.ok(
                estudianteActividadService.listarPublicadas(id)
        );
    }
}