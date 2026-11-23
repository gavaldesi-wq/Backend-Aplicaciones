package cl.mpazpro.contenidos.controller;

import cl.mpazpro.contenidos.dto.AsignaturaDTO;
import cl.mpazpro.contenidos.service.AsignaturaService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/v1/asignaturas")
@RequiredArgsConstructor
public class AsignaturaController {

    private final AsignaturaService asignaturaService;

    @GetMapping
    public ResponseEntity<List<AsignaturaDTO>> getAll() {
        return ResponseEntity.ok(
                asignaturaService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsignaturaDTO> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                asignaturaService.getById(id)
        );
    }
}
