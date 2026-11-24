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
@Table(name = "envio_actividad")
public class EnvioActividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "actividad_id", nullable = false)
    private Long actividadId;

    @Column(name = "estudiante_id", nullable = false)
    private Long estudianteId;

    @Column(name = "objetivo_aprendizaje_id", nullable = false)
    private Long objetivoAprendizajeId;

    @Column(name = "client_event_id", nullable = false, length = 36, unique = true)
    private String clientEventId;

    @Column(nullable = false)
    private Integer puntaje;

    @Column(name = "porcentaje_avance", nullable = false)
    private Integer porcentajeAvance;

    @Column(name = "respondida_en", nullable = false)
    private Instant respondidaEn;

    @Column(name = "registrado_en", nullable = false)
    private Instant registradoEn;
}
