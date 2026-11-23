package cl.mpazpro.admin.controller;

import cl.mpazpro.admin.service.HistorialAprobacionService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import cl.mpazpro.admin.dto.HistorialAprobacionDTO;
import cl.mpazpro.admin.model.Usuario;
import cl.mpazpro.admin.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import java.util.UUID;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasRole('UTP')")
public class HistorialAprobacionController {

    private final HistorialAprobacionService historialService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/api/v1/utp/unidades/{id}/historial")
    public ResponseEntity<Map<String, Object>> listarHistorialUnidad(
            @PathVariable @Positive Long id,
            @RequestParam(defaultValue = "0") @Min(0) int pagina,
            @RequestParam(defaultValue = "20")
            @Min(1) @Max(100) int tamano
    ) {
        return ResponseEntity.ok(
                historialService.listarPorRecurso(
                        "UNIDAD",
                        id,
                        pagina,
                        tamano
                )
        );
    }



        @PostMapping("/api/v1/utp/historial-aprobaciones")
    public ResponseEntity<HistorialAprobacionDTO> registrarEvento(
            @RequestBody HistorialAprobacionDTO dto,
            Authentication authentication
    ) {
        if (dto.getEventoId() == null
                || dto.getTipoRecurso() == null
                || dto.getRecursoId() == null
                || dto.getEstadoNuevo() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Faltan datos del evento"
            );
        }

        UUID eventoId;

        try {
            eventoId = UUID.fromString(dto.getEventoId());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "eventoId invalido"
            );
        }

        Usuario usuario = usuarioRepository
                .findByCodigoUsuarioIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuario no encontrado"
                ));

        HistorialAprobacionDTO resultado =
                historialService.registrarEvento(
                        eventoId,
                        dto.getTipoRecurso(),
                        dto.getRecursoId(),
                        dto.getEstadoAnterior(),
                        dto.getEstadoNuevo(),
                        usuario.getId()
                );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resultado);
    }

}
