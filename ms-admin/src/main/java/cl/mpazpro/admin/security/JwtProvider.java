package cl.mpazpro.admin.security;

import cl.mpazpro.admin.model.Usuario;
import cl.mpazpro.admin.repository.UsuarioRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final UsuarioRepository usuarioRepository;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private Long expiration;

    public String generarToken(Authentication authentication) {

        Usuario usuario = usuarioRepository
                .findByCodigoUsuarioIgnoreCase(authentication.getName())
                .orElseThrow();

        Date ahora = new Date();
        Date vencimiento = new Date(
                ahora.getTime() + expiration
        );

        return Jwts.builder()
                .subject(usuario.getCodigoUsuario())
                .claim("uid", usuario.getId())
                .claim("rol", usuario.getRol().name())
                .issuedAt(ahora)
                .expiration(vencimiento)
                .signWith(claveDeFirma())
                .compact();
    }

    public Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(claveDeFirma())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey claveDeFirma() {
    return Keys.hmacShaKeyFor(
        secret.getBytes(StandardCharsets.UTF_8)
    );
}
}
