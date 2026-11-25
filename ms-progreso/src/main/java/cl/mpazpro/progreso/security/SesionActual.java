package cl.mpazpro.progreso.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class SesionActual {

    public long estudianteId() {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion == null || !(autenticacion.getDetails() instanceof Long id)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sesion no valida");
        }
        return id;
    }

    public long docenteId() {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion == null || !(autenticacion.getDetails() instanceof Long id)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sesion no valida");
        }
        boolean docente = autenticacion.getAuthorities().stream()
                .anyMatch(autoridad -> "ROLE_DOCENTE".equals(autoridad.getAuthority()));
        if (!docente) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo un docente puede hacer esta accion");
        }
        return id;
    }
}