package cl.mpazpro.progreso.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.progreso.dto.EntregaIntentoDTO;
import cl.mpazpro.progreso.dto.PreguntaEstudianteDTO;
import cl.mpazpro.progreso.dto.ResultadoIntentoDTO;
import cl.mpazpro.progreso.service.PruebaEstudianteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/estudiante/intentos")
@RequiredArgsConstructor
public class IntentoPreguntaController {

    private final PruebaEstudianteService pruebaEstudianteService;

    @GetMapping("/{id}/preguntas")
    public ResponseEntity<List<PreguntaEstudianteDTO>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(
                pruebaEstudianteService.preguntas(id)
        );
    }

    @PostMapping("/{id}/entrega")
    public ResponseEntity<ResultadoIntentoDTO> entregar(
            @PathVariable Long id,
            @Valid @RequestBody EntregaIntentoDTO dto) {
    
        return ResponseEntity.ok(
                pruebaEstudianteService.entregar(id, dto)
        );
    }
}