package cl.mpazpro.progreso.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "evento_sincronizado")
public class EventoSincronizado {

    @Id
    @Column(name = "client_event_id", nullable = false, length = 36)
    private String clientEventId;

    @Column(nullable = false, length = 40)
    private String tipo;

    @Column(name = "objetivo_aprendizaje_id")
    private Long objetivoAprendizajeId;

    @Column(name = "contenido_id")
    private Long contenidoId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload_json", nullable = false)
    private String payloadJson;

    @Column(name = "ocurrido_en", nullable = false)
    private Instant ocurridoEn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoEvento estado;

    @Column(name = "ultimo_error", length = 500)
    private String ultimoError;

    @Column(name = "registrado_en", nullable = false)
    private Instant registradoEn;
}
