
package cl.mpazpro.admin.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath()
                .equals("/api/v1/auth/demo-login");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String token = obtenerJwtDeLaPeticion(request);

        try {
            if (StringUtils.hasText(token)) {

                Claims claims = jwtProvider.obtenerClaims(token);

                String codigoUsuario = claims.getSubject();
                String rolToken = claims.get("rol", String.class);

                if (StringUtils.hasText(codigoUsuario)
                        && StringUtils.hasText(rolToken)
                        && SecurityContextHolder.getContext()
                                .getAuthentication() == null) {

                    UserDetails usuario = userDetailsService
                            .loadUserByUsername(codigoUsuario);

                    boolean rolCoincide = usuario.getAuthorities()
                            .stream()
                            .anyMatch(permiso ->
                                    permiso.getAuthority()
                                            .equals("ROLE_" + rolToken));

                    if (usuario.isEnabled() && rolCoincide) {

                        UsernamePasswordAuthenticationToken autenticacion =
                                new UsernamePasswordAuthenticationToken(
                                        usuario,
                                        null,
                                        usuario.getAuthorities()
                                );

                        autenticacion.setDetails(
                                new WebAuthenticationDetailsSource()
                                        .buildDetails(request)
                        );

                        SecurityContextHolder.getContext()
                                .setAuthentication(autenticacion);
                    }
                }
            }

        } catch (JwtException |
                 IllegalArgumentException |
                 UsernameNotFoundException e) {

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String obtenerJwtDeLaPeticion(
            HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken)
                && bearerToken.startsWith("Bearer ")) {

            return bearerToken.substring(7);
        }

        return null;
    }
}
