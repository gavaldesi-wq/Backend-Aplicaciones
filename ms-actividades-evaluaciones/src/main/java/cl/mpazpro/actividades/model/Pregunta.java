package cl.mpazpro.actividades.model;

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
@Table(name = "pregunta")
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actividad_id")
    private ActividadDesafio actividad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prueba_final_id")
    private PruebaFinal pruebaFinal;

    @Column(nullable = false, length = 1000)
    private String enunciado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoActividad tipo;

    @Column(name = "orden", nullable = false)
    private Integer orden;

    @Column(length = 1000)
    private String explicacion;

    /** Solucionario de COMPLETACION. No se envia al estudiante antes de responder. */
    @Column(name = "texto_correcto", length = 500)
    private String textoCorrecto;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private Instant actualizadoEn;
}
