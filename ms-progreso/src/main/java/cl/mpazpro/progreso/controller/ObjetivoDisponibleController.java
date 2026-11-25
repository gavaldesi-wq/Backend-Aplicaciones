package cl.mpazpro.progreso.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.progreso.dto.ObjetivoDisponibleDTO;
import cl.mpazpro.progreso.service.ObjetivoDisponibleService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/docente/objetivos-disponibles")
@RequiredArgsConstructor
public class ObjetivoDisponibleController {

    private final ObjetivoDisponibleService objetivoDisponibleService;

    @GetMapping
    public ResponseEntity<List<ObjetivoDisponibleDTO>> listar(
            @RequestParam Long cursoId) {
        return ResponseEntity.ok(objetivoDisponibleService.listar(cursoId));
    }
}