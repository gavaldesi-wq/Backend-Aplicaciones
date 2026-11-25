package cl.mpazpro.progreso.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.progreso.dto.EventoProgresoDTO;
import cl.mpazpro.progreso.dto.ResultadoEventoDTO;
import cl.mpazpro.progreso.service.EventoProgresoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/estudiante/progreso/eventos")
@RequiredArgsConstructor
public class EventoProgresoController {

    private final EventoProgresoService eventoProgresoService;

    @PostMapping
    public ResponseEntity<ResultadoEventoDTO> registrar(
            @Valid @RequestBody EventoProgresoDTO dto) {

        return ResponseEntity.ok(
                eventoProgresoService.registrar(dto)
        );
    }
}