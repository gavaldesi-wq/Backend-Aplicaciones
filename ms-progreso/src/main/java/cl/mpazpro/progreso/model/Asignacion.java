package cl.mpazpro.progreso.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "asignacion")
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "curso_id", nullable = false)
    private Long cursoId;

    /** Nulo cuando la asignacion es para el curso completo. */
    @Column(name = "estudiante_id")
    private Long estudianteId;

    @Column(name = "objetivo_aprendizaje_id", nullable = false)
    private Long objetivoAprendizajeId;

    @Column(name = "docente_id", nullable = false)
    private Long docenteId;

    @Column(name = "disponible_desde", nullable = false)
    private Instant disponibleDesde;

    @Column(name = "disponible_hasta", nullable = false)
    private Instant disponibleHasta;

    @Column(name = "limite_intentos_prueba", nullable = false)
    private Integer limiteIntentosPrueba;

    @Column(nullable = false)
    private boolean activa = true;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private Instant actualizadoEn;
}
