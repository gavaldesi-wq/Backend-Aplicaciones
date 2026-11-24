package cl.mpazpro.progreso.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "intento_prueba")
public class IntentoPrueba {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prueba_id", nullable = false)
    private Long pruebaId;

    @Column(name = "estudiante_id", nullable = false)
    private Long estudianteId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asignacion_id", nullable = false)
    private Asignacion asignacion;

    @Column(name = "client_event_id", nullable = false, length = 36, unique = true)
    private String clientEventId;

    @Column(name = "numero_intento", nullable = false)
    private Integer numeroIntento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoIntento estado;

    private Integer puntaje;

    @Column(name = "iniciado_en", nullable = false)
    private Instant iniciadoEn;

    @Column(name = "entregado_en")
    private Instant entregadoEn;
}
