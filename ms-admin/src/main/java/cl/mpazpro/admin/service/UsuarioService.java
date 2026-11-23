
package cl.mpazpro.admin.service;

import cl.mpazpro.admin.dto.UsuarioDTO;
import cl.mpazpro.admin.model.Usuario;
import cl.mpazpro.admin.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioDTO::fromModel)
                .toList();
    }

    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        return UsuarioDTO.fromModel(buscarUsuario(id));
    }

    public UsuarioDTO obtenerPorCodigo(String codigoUsuario) {
        Usuario usuario = usuarioRepository
                .findByCodigoUsuarioIgnoreCase(codigoUsuario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"
                ));

        return UsuarioDTO.fromModel(usuario);
    }

    @Transactional
    public UsuarioDTO crearUsuario(UsuarioDTO dto) {
        String codigo = normalizarCodigo(dto.getCodigoUsuario());

        if (usuarioRepository.existsByCodigoUsuarioIgnoreCase(codigo)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El codigo de usuario ya existe"
            );
        }

        if (dto.getClave() == null || dto.getClave().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La clave es obligatoria"
            );
        }

        Usuario usuario = dto.toModel();
        usuario.setCodigoUsuario(codigo);
        usuario.setNombreCompleto(dto.getNombreCompleto().trim());
        usuario.setClaveHash(passwordEncoder.encode(dto.getClave()));

        return UsuarioDTO.fromModel(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioDTO actualizarUsuario(
            Long id,
            UsuarioDTO dto,
            String codigoSolicitante
    ) {
        Usuario usuario = buscarUsuario(id);

        String codigo = normalizarCodigo(dto.getCodigoUsuario());

        if (!usuario.getCodigoUsuario().equalsIgnoreCase(codigo)
                && usuarioRepository.existsByCodigoUsuarioIgnoreCase(codigo)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El codigo de usuario ya existe"
            );
        }

        boolean esUsuarioActual = usuario.getCodigoUsuario()
                .equalsIgnoreCase(codigoSolicitante);

        if (esUsuarioActual &&
                (dto.getRol() != usuario.getRol()
                        || Boolean.FALSE.equals(dto.getActivo()))) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No puedes cambiar tu propio rol ni desactivarte"
            );
        }

        usuario.setCodigoUsuario(codigo);
        usuario.setNombreCompleto(dto.getNombreCompleto().trim());
        usuario.setRol(dto.getRol());

        if (dto.getActivo() != null) {
            usuario.setActivo(dto.getActivo());
        }

        if (dto.getClave() != null && !dto.getClave().isBlank()) {
            usuario.setClaveHash(passwordEncoder.encode(dto.getClave()));
        }

        return UsuarioDTO.fromModel(usuarioRepository.save(usuario));
    }

    @Transactional
    public void desactivarUsuario(Long id, String codigoSolicitante) {
        Usuario usuario = buscarUsuario(id);

        if (usuario.getCodigoUsuario()
                .equalsIgnoreCase(codigoSolicitante)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No puedes desactivar tu propia cuenta"
            );
        }

        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"
                ));
    }

    private String normalizarCodigo(String codigo) {
        return codigo.trim().toUpperCase(Locale.ROOT);
    }
}
