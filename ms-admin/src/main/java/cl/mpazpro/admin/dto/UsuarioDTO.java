
package cl.mpazpro.admin.dto;

import cl.mpazpro.admin.model.Rol;
import cl.mpazpro.admin.model.Usuario;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {

    private Long id;

    @NotBlank
    @Size(max = 30)
    private String codigoUsuario;

    @NotBlank
    @Size(max = 150)
    private String nombreCompleto;

    @NotNull
    private Rol rol;

    private Boolean activo;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 8, max = 72)
    private String clave;

    public static UsuarioDTO fromModel(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setCodigoUsuario(usuario.getCodigoUsuario());
        dto.setNombreCompleto(usuario.getNombreCompleto());
        dto.setRol(usuario.getRol());
        dto.setActivo(usuario.getActivo());
        return dto;
    }

    public Usuario toModel() {
        Usuario usuario = new Usuario();
        usuario.setCodigoUsuario(codigoUsuario);
        usuario.setNombreCompleto(nombreCompleto);
        usuario.setRol(rol);
        usuario.setActivo(activo != null ? activo : true);
        return usuario;
    }
}
