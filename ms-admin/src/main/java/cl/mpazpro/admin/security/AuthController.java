
package cl.mpazpro.admin.security;

import cl.mpazpro.admin.dto.LoginRequestDTO;
import cl.mpazpro.admin.dto.UsuarioDTO;
import cl.mpazpro.admin.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UsuarioService usuarioService;

    @Value("${jwt.expiration-ms}")
    private Long expirationMs;

    @PostMapping("/demo-login")
    public ResponseEntity<Map<String, Object>> login(
            @Valid @RequestBody LoginRequestDTO credenciales) {

        String codigoUsuario = credenciales
                .getCodigoUsuario()
                .trim()
                .toUpperCase(Locale.ROOT);

        Authentication autenticacion =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                codigoUsuario,
                                credenciales.getClave()
                        )
                );

        String token = jwtProvider.generarToken(autenticacion);

        UsuarioDTO usuario = usuarioService.obtenerPorCodigo(
                autenticacion.getName()
        );

        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("token", token);
        respuesta.put(
                "expiraEn",
                Instant.now().plusMillis(expirationMs).toString()
        );
        respuesta.put("usuario", usuario);

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> me(
            Authentication autenticacion) {

        return ResponseEntity.ok(
                usuarioService.obtenerPorCodigo(
                        autenticacion.getName()
                )
        );
    }
}
