
package cl.mpazpro.admin.controller;

import cl.mpazpro.admin.dto.UsuarioDTO;
import cl.mpazpro.admin.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('UTP')")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerPorId(
            @PathVariable @Positive Long id
    ) {
        return ResponseEntity.ok(
                usuarioService.obtenerUsuarioPorId(id)
        );
    }

    @GetMapping("/codigo/{codigoUsuario}")
    public ResponseEntity<UsuarioDTO> obtenerPorCodigo(
            @PathVariable String codigoUsuario
    ) {
        return ResponseEntity.ok(
                usuarioService.obtenerPorCodigo(codigoUsuario)
        );
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> crearUsuario(
            @Valid @RequestBody UsuarioDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.crearUsuario(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(
            @PathVariable @Positive Long id,
            @Valid @RequestBody UsuarioDTO dto,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                usuarioService.actualizarUsuario(
                        id,
                        dto,
                        authentication.getName()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarUsuario(
            @PathVariable @Positive Long id,
            Authentication authentication
    ) {
        usuarioService.desactivarUsuario(
                id,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}
