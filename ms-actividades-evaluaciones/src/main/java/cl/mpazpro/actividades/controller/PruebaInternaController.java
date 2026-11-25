package cl.mpazpro.actividades.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.actividades.dto.PruebaPublicadaDTO;
import cl.mpazpro.actividades.service.PruebaFinalService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/interno/objetivos")
@RequiredArgsConstructor
public class PruebaInternaController {

    private final PruebaFinalService pruebaFinalService;

    @GetMapping("/{id}/prueba-final")
    public ResponseEntity<PruebaPublicadaDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(
                pruebaFinalService.obtenerPublicada(id)
        );
    }
}