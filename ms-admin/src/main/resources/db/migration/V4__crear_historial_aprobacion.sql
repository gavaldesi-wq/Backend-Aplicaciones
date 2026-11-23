
CREATE TABLE historial_aprobacion (
    id BIGINT NOT NULL AUTO_INCREMENT,
    evento_id VARCHAR(36) NOT NULL,
    tipo_recurso VARCHAR(40) NOT NULL,
    recurso_id BIGINT NOT NULL,
    estado_anterior VARCHAR(20),
    estado_nuevo VARCHAR(20) NOT NULL,
    usuario_id BIGINT NOT NULL,
    creado_en DATETIME(6) NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uq_historial_evento (evento_id),
    KEY idx_historial_recurso (tipo_recurso, recurso_id),

    CONSTRAINT fk_historial_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
