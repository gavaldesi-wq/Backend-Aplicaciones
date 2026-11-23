
package cl.mpazpro.contenidos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "objetivo_aprendizaje")
@Getter
@Setter
@NoArgsConstructor
public class ObjetivoAprendizaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unidad_id", nullable = false)
    private Unidad unidad;

    @Column(nullable = false, length = 30)
    private String codigo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "eje_curricular", length = 150)
    private String ejeCurricular;

    @Column(name = "texto_referencia", columnDefinition = "TEXT")
    private String textoReferencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoContenido estado = EstadoContenido.BORRADOR;

    @Column(nullable = false)
    private Integer orden;
}
