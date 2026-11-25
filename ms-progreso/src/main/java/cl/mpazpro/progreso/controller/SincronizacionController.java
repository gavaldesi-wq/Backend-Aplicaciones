package cl.mpazpro.progreso.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.progreso.dto.LoteEventosDTO;
import cl.mpazpro.progreso.dto.ResultadoLoteDTO;
import cl.mpazpro.progreso.service.EventoProgresoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/sincronizacion/eventos")
@RequiredArgsConstructor
public class SincronizacionController {

    private final EventoProgresoService eventoProgresoService;

    @PostMapping
    public ResponseEntity<ResultadoLoteDTO> registrar(
            @Valid @RequestBody LoteEventosDTO dto) {

        return ResponseEntity.ok(
                eventoProgresoService.registrarLote(dto)
        );
    }
}