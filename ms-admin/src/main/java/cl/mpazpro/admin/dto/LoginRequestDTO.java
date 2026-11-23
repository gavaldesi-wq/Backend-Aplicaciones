package cl.mpazpro.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank
    private String codigoUsuario;

    @NotBlank
    private String clave;
}
