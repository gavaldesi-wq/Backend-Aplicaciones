package cl.mpazpro.progreso.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.progreso.dto.InicioDTO;
import cl.mpazpro.progreso.service.InicioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/estudiante/inicio")
@RequiredArgsConstructor
public class InicioController {

    private final InicioService inicioService;

    @GetMapping
    public ResponseEntity<InicioDTO> obtener() {
        return ResponseEntity.ok(inicioService.obtener());
    }
}