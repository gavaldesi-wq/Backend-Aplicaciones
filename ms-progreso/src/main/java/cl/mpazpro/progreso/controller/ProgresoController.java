package cl.mpazpro.progreso.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.progreso.dto.ProgresoDTO;
import cl.mpazpro.progreso.dto.PruebaEstudianteDTO;
import cl.mpazpro.progreso.service.ProgresoService;
import cl.mpazpro.progreso.service.PruebaEstudianteService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/estudiante/objetivos")
@RequiredArgsConstructor
public class ProgresoController {

    private final ProgresoService progresoService;
    private final PruebaEstudianteService pruebaEstudianteService;

    @GetMapping("/{id}/progreso")
    public ResponseEntity<ProgresoDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(
                progresoService.obtener(id)
        );
    }

    @GetMapping("/{id}/prueba-final")
    public ResponseEntity<PruebaEstudianteDTO> obtenerPrueba(@PathVariable Long id) {
        return ResponseEntity.ok(
                pruebaEstudianteService.obtener(id)
        );
    }    
}