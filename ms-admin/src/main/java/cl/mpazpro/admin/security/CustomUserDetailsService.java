package cl.mpazpro.admin.security;

import cl.mpazpro.admin.model.Usuario;
import cl.mpazpro.admin.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String codigoUsuario)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
                .findByCodigoUsuarioIgnoreCase(codigoUsuario)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuario no encontrado"
                        )
                );

        return User.builder()
                .username(usuario.getCodigoUsuario())
                .password(usuario.getClaveHash())
                .authorities("ROLE_" + usuario.getRol().name())
                .disabled(!Boolean.TRUE.equals(usuario.getActivo()))
                .build();
    }
}
