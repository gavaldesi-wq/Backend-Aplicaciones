package cl.mpazpro.admin.repository;

import cl.mpazpro.admin.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCodigoUsuarioIgnoreCase(
            String codigoUsuario
    );

    boolean existsByCodigoUsuarioIgnoreCase(
            String codigoUsuario
    );
}
