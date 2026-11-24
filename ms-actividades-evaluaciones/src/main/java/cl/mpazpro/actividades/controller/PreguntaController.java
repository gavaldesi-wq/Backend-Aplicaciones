package cl.mpazpro.actividades.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.actividades.dto.PreguntaDTO;
import cl.mpazpro.actividades.service.PreguntaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/utp/preguntas")
@RequiredArgsConstructor
public class PreguntaController {

    private final PreguntaService preguntaService;

    @PostMapping
    public ResponseEntity<PreguntaDTO> save(@Valid @RequestBody PreguntaDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(preguntaService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreguntaDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody PreguntaDTO dto) {

        return ResponseEntity.ok(
                preguntaService.update(id, dto)
        );
    }
}