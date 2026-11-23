package cl.mpazpro.contenidos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "unidad")
@Getter
@Setter
@NoArgsConstructor
public class Unidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asignatura_id", nullable = false)
    private Asignatura asignatura;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false)
    private Integer orden;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoContenido estado = EstadoContenido.BORRADOR;
}
