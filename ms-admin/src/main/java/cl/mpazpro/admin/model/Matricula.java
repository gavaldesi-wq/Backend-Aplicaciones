package cl.mpazpro.admin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "matricula",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"curso_id", "estudiante_id"}
    )
)
@Getter
@Setter
@NoArgsConstructor
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "curso_id", nullable = false)
    private Long cursoId;

    @Column(name = "estudiante_id", nullable = false)
    private Long estudianteId;

    @Column(nullable = false)
    private Boolean activo = true;
}
