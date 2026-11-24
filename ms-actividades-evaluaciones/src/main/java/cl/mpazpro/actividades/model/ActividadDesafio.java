package cl.mpazpro.actividades.model;

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
@Table(name = "actividad_desafio")
public class ActividadDesafio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "objetivo_aprendizaje_id", nullable = false)
    private Long objetivoAprendizajeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoActividad tipo;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoContenido estado;

    @Column(name = "orden", nullable = false)
    private Integer orden;

    @Column(name = "creado_por_usuario_id", nullable = false)
    private Long creadoPorUsuarioId;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private Instant actualizadoEn;
}
