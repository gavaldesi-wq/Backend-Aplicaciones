package cl.mpazpro.progreso.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "informe_pedagogico")
public class InformePedagogico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estudiante_id", nullable = false)
    private Long estudianteId;

    @Column(name = "objetivo_aprendizaje_id", nullable = false)
    private Long objetivoAprendizajeId;

    @Column(name = "docente_id", nullable = false)
    private Long docenteId;

    @Column(name = "curso_id", nullable = false)
    private Long cursoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoInforme estado;

    @Column(nullable = false, length = 4000)
    private String resumen;

    @Column(name = "areas_refuerzo", length = 2000)
    private String areasRefuerzo;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;
}
