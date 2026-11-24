package cl.mpazpro.actividades.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.actividades.dto.SolucionActividadDTO;
import cl.mpazpro.actividades.service.SolucionarioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/interno/actividades")
@RequiredArgsConstructor
public class SolucionarioController {

    private final SolucionarioService solucionarioService;

    @GetMapping("/{id}/solucionario")
    public ResponseEntity<SolucionActividadDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(
                solucionarioService.obtener(id)
        );
    }
}