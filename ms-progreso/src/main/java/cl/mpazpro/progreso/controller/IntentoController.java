package cl.mpazpro.progreso.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.progreso.dto.InicioIntentoDTO;
import cl.mpazpro.progreso.dto.IntentoDTO;
import cl.mpazpro.progreso.service.PruebaEstudianteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/estudiante/pruebas")
@RequiredArgsConstructor
public class IntentoController {

    private final PruebaEstudianteService pruebaEstudianteService;

    @PostMapping("/{id}/intentos")
    public ResponseEntity<IntentoDTO> iniciar(
            @PathVariable Long id,
            @Valid @RequestBody InicioIntentoDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pruebaEstudianteService.iniciar(id, dto));
    }
}