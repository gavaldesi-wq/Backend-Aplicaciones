package cl.mpazpro.progreso.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.progreso.dto.SeguimientoDTO;
import cl.mpazpro.progreso.service.DocenteProgresoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/docente/progreso")
@RequiredArgsConstructor
public class DocenteProgresoController {

    private final DocenteProgresoService docenteProgresoService;

    @GetMapping
    public ResponseEntity<SeguimientoDTO> obtener(
            @RequestParam Long cursoId,
            @RequestParam Long objetivoAprendizajeId) {

        return ResponseEntity.ok(
                docenteProgresoService.obtener(cursoId, objetivoAprendizajeId)
        );
    }
}