package cl.mpazpro.admin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "curso")
@Getter
@Setter
@NoArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "anio_escolar", nullable = false)
    private Integer anioEscolar;

    @Column(name = "docente_id", nullable = false)
    private Long docenteId;

    @Column(nullable = false)
    private Boolean activo = true;
}
