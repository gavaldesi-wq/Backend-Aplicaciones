package cl.mpazpro.actividades.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.actividades.dto.PreguntaEstudianteDTO;
import cl.mpazpro.actividades.service.EstudianteActividadService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/interno/pruebas-finales")
@RequiredArgsConstructor
public class PruebaPreguntasController {

    private final EstudianteActividadService estudianteActividadService;

    @GetMapping("/{id}/preguntas")
    public ResponseEntity<List<PreguntaEstudianteDTO>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(
                estudianteActividadService.preguntasDePrueba(id)
        );
    }
}