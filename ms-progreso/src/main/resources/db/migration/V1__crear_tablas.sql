-- Esquema propio de ms-progreso.
-- curso_id, estudiante_id, docente_id, objetivo_aprendizaje_id, actividad_id,
-- prueba_id y pregunta_id son IDs logicos. No cruzan claves foraneas de otro esquema.

CREATE TABLE asignacion (
    id BIGINT NOT NULL AUTO_INCREMENT,
    curso_id BIGINT NOT NULL,
    estudiante_id BIGINT NULL,
    objetivo_aprendizaje_id BIGINT NOT NULL,
    docente_id BIGINT NOT NULL,
    disponible_desde DATETIME(6) NOT NULL,
    disponible_hasta DATETIME(6) NOT NULL,
    limite_intentos_prueba INT NOT NULL,
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en DATETIME(6) NOT NULL,
    actualizado_en DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_asignacion_fechas CHECK (disponible_desde < disponible_hasta),
    CONSTRAINT chk_asignacion_intentos CHECK (limite_intentos_prueba > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE progreso (
    id BIGINT NOT NULL AUTO_INCREMENT,
    estudiante_id BIGINT NOT NULL,
    objetivo_aprendizaje_id BIGINT NOT NULL,
    porcentaje_avance INT NOT NULL,
    actualizado_en DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_progreso_estudiante_objetivo UNIQUE (estudiante_id, objetivo_aprendizaje_id),
    CONSTRAINT chk_progreso_porcentaje CHECK (porcentaje_avance BETWEEN 0 AND 100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Una practica completa. client_event_id hace idempotente el reenvio.
CREATE TABLE envio_actividad (
    id BIGINT NOT NULL AUTO_INCREMENT,
    actividad_id BIGINT NOT NULL,
    estudiante_id BIGINT NOT NULL,
    objetivo_aprendizaje_id BIGINT NOT NULL,
    client_event_id VARCHAR(36) NOT NULL,
    puntaje INT NOT NULL,
    porcentaje_avance INT NOT NULL,
    respondida_en DATETIME(6) NOT NULL,
    registrado_en DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_envio_evento UNIQUE (client_event_id),
    CONSTRAINT chk_envio_puntaje CHECK (puntaje BETWEEN 0 AND 100),
    CONSTRAINT chk_envio_avance CHECK (porcentaje_avance BETWEEN 0 AND 100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE intento_prueba (
    id BIGINT NOT NULL AUTO_INCREMENT,
    prueba_id BIGINT NOT NULL,
    estudiante_id BIGINT NOT NULL,
    asignacion_id BIGINT NOT NULL,
    client_event_id VARCHAR(36) NOT NULL,
    numero_intento INT NOT NULL,
    estado VARCHAR(20) NOT NULL,
    puntaje INT NULL,
    iniciado_en DATETIME(6) NOT NULL,
    entregado_en DATETIME(6) NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_intento_evento UNIQUE (client_event_id),
    CONSTRAINT uq_intento_numero UNIQUE (prueba_id, estudiante_id, numero_intento),
    CONSTRAINT fk_intento_asignacion FOREIGN KEY (asignacion_id) REFERENCES asignacion (id),
    CONSTRAINT chk_intento_numero CHECK (numero_intento > 0),
    CONSTRAINT chk_intento_estado CHECK (estado IN ('INICIADO', 'ENTREGADO', 'CALIFICADO')),
    CONSTRAINT chk_intento_puntaje CHECK (puntaje IS NULL OR puntaje BETWEEN 0 AND 100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE respuesta_estudiante (
    id BIGINT NOT NULL AUTO_INCREMENT,
    envio_actividad_id BIGINT NULL,
    intento_id BIGINT NULL,
    pregunta_id BIGINT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    respuesta_json JSON NOT NULL,
    correcta BOOLEAN NULL,
    explicacion VARCHAR(1000) NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_respuesta_envio FOREIGN KEY (envio_actividad_id) REFERENCES envio_actividad (id),
    CONSTRAINT fk_respuesta_intento FOREIGN KEY (intento_id) REFERENCES intento_prueba (id),
    CONSTRAINT chk_respuesta_un_padre CHECK (
        (envio_actividad_id IS NOT NULL AND intento_id IS NULL)
        OR (envio_actividad_id IS NULL AND intento_id IS NOT NULL)
    ),
    CONSTRAINT chk_respuesta_tipo CHECK (tipo IN ('ASOCIACION', 'SECUENCIA', 'COMPLETACION', 'SELECCION'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE informe_pedagogico (
    id BIGINT NOT NULL AUTO_INCREMENT,
    estudiante_id BIGINT NOT NULL,
    objetivo_aprendizaje_id BIGINT NOT NULL,
    docente_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,
    estado VARCHAR(30) NOT NULL,
    resumen VARCHAR(4000) NOT NULL,
    areas_refuerzo VARCHAR(2000) NULL,
    creado_en DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_informe_estado CHECK (estado IN ('PENDIENTE_VALIDACION', 'VALIDADO'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE validacion (
    id BIGINT NOT NULL AUTO_INCREMENT,
    informe_id BIGINT NOT NULL,
    docente_id BIGINT NOT NULL,
    estado VARCHAR(30) NOT NULL,
    validado_en DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_validacion_informe UNIQUE (informe_id),
    CONSTRAINT fk_validacion_informe FOREIGN KEY (informe_id) REFERENCES informe_pedagogico (id),
    CONSTRAINT chk_validacion_estado CHECK (estado IN ('PENDIENTE_VALIDACION', 'VALIDADO'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE observacion_docente (
    id BIGINT NOT NULL AUTO_INCREMENT,
    informe_id BIGINT NOT NULL,
    docente_id BIGINT NOT NULL,
    texto VARCHAR(2000) NOT NULL,
    creado_en DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_observacion_informe FOREIGN KEY (informe_id) REFERENCES informe_pedagogico (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE evento_sincronizado (
    client_event_id VARCHAR(36) NOT NULL,
    tipo VARCHAR(40) NOT NULL,
    objetivo_aprendizaje_id BIGINT NULL,
    contenido_id BIGINT NULL,
    payload_json JSON NOT NULL,
    ocurrido_en DATETIME(6) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    ultimo_error VARCHAR(500) NULL,
    registrado_en DATETIME(6) NOT NULL,
    PRIMARY KEY (client_event_id),
    CONSTRAINT chk_evento_estado CHECK (estado IN ('PENDIENTE', 'SINCRONIZADO', 'RECHAZADO'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_asignacion_curso ON asignacion (curso_id);
CREATE INDEX idx_asignacion_objetivo ON asignacion (objetivo_aprendizaje_id);
CREATE INDEX idx_intento_estudiante ON intento_prueba (prueba_id, estudiante_id);
CREATE INDEX idx_respuesta_pregunta ON respuesta_estudiante (pregunta_id);
CREATE INDEX idx_informe_estudiante ON informe_pedagogico (estudiante_id, objetivo_aprendizaje_id);
CREATE INDEX idx_observacion_informe ON observacion_docente (informe_id);
