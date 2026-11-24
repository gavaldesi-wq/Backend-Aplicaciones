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
@Table(name = "progreso")
public class Progreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estudiante_id", nullable = false)
    private Long estudianteId;

    @Column(name = "objetivo_aprendizaje_id", nullable = false)
    private Long objetivoAprendizajeId;

    @Column(name = "porcentaje_avance", nullable = false)
    private Integer porcentajeAvance;

    @Column(name = "actualizado_en", nullable = false)
    private Instant actualizadoEn;
}
