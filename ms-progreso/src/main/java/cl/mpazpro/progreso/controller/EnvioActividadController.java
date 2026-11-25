package cl.mpazpro.progreso.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.mpazpro.progreso.dto.EnvioActividadDTO;
import cl.mpazpro.progreso.dto.ResultadoEnvioDTO;
import cl.mpazpro.progreso.service.EnvioActividadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/estudiante/actividades")
@RequiredArgsConstructor
public class EnvioActividadController {

    private final EnvioActividadService envioActividadService;

    @PostMapping("/{id}/respuestas")
    public ResponseEntity<ResultadoEnvioDTO> registrar(
            @PathVariable Long id,
            @Valid @RequestBody EnvioActividadDTO dto) {

        return ResponseEntity.ok(
                envioActividadService.registrar(id, dto)
        );
    }
}