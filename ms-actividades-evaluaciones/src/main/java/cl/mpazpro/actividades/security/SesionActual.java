package cl.mpazpro.actividades.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class SesionActual {

    public long utpId() {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion == null || !(autenticacion.getDetails() instanceof Long id)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sesion no valida");
        }
        boolean utp = autenticacion.getAuthorities().stream()
                .anyMatch(autoridad -> "ROLE_UTP".equals(autoridad.getAuthority()));
        if (!utp) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo UTP puede hacer esta accion");
        }
        return id;
    }
}