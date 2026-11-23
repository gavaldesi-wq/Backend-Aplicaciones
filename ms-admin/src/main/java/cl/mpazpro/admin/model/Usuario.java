
package cl.mpazpro.admin.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "codigo_usuario",
        nullable = false,
        unique = true,
        length = 30
    )
    private String codigoUsuario;

    @Column(
        name = "nombre_completo",
        nullable = false,
        length = 150
    )
    private String nombreCompleto;

    @Column(
        name = "clave_hash",
        nullable = false,
        length = 255
    )
    private String claveHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

    @Column(nullable = false)
    private Boolean activo = true;
}
