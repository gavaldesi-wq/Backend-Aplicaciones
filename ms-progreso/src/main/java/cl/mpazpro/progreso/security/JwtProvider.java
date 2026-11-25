package cl.mpazpro.progreso.security;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    public Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(claveDeFirma())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey claveDeFirma() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}