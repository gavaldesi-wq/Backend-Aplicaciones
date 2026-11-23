
package cl.mpazpro.contenidos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contenido_didactico")
@Getter
@Setter
@NoArgsConstructor
public class ContenidoDidactico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "objetivo_aprendizaje_id", nullable = false)
    private ObjetivoAprendizaje objetivoAprendizaje;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(name = "explicacion_escrita", columnDefinition = "TEXT")
    private String explicacionEscrita;

    @Column(name = "archivo_audio", length = 500)
    private String archivoAudio;

    @ElementCollection
    @CollectionTable(
        name = "contenido_imagen",
        joinColumns = @JoinColumn(name = "contenido_id")
    )
    @OrderColumn(name = "orden")
    @Column(name = "imagen_url", nullable = false, length = 500)
    private List<String> imagenes = new ArrayList<>();

    @Column(name = "ejemplos_guiados", columnDefinition = "TEXT")
    private String ejemplosGuiados;

    @Column(columnDefinition = "TEXT")
    private String instrucciones;

    @Column(nullable = false)
    private Integer orden;

    @Column(name = "responsable_origen", length = 200)
    private String responsableOrigen;

    @Column(length = 200)
    private String licencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoContenido estado = EstadoContenido.BORRADOR;
}
