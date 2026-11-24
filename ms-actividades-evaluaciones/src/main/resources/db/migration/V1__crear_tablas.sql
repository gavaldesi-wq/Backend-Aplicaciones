-- Esquema propio de ms-actividades-evaluaciones.
-- objetivo_aprendizaje_id y creado_por_usuario_id son IDs logicos:
-- no hay clave foranea hacia ms-contenidos ni ms-admin.

CREATE TABLE actividad_desafio (
    id BIGINT NOT NULL AUTO_INCREMENT,
    objetivo_aprendizaje_id BIGINT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    orden INT NOT NULL,
    creado_por_usuario_id BIGINT NOT NULL,
    creado_en DATETIME(6) NOT NULL,
    actualizado_en DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_actividad_tipo CHECK (tipo IN ('ASOCIACION', 'SECUENCIA', 'COMPLETACION', 'SELECCION')),
    CONSTRAINT chk_actividad_estado CHECK (estado IN ('BORRADOR', 'EN_REVISION', 'AUTORIZADO', 'PUBLICADO', 'DESHABILITADO'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE prueba_final (
    id BIGINT NOT NULL AUTO_INCREMENT,
    objetivo_aprendizaje_id BIGINT NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    disponible_desde DATETIME(6) NULL,
    disponible_hasta DATETIME(6) NULL,
    limite_intentos INT NULL,
    creado_por_usuario_id BIGINT NOT NULL,
    creado_en DATETIME(6) NOT NULL,
    actualizado_en DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_prueba_estado CHECK (estado IN ('BORRADOR', 'EN_REVISION', 'AUTORIZADO', 'PUBLICADO', 'DESHABILITADO')),
    CONSTRAINT chk_prueba_fechas CHECK (
        disponible_desde IS NULL
        OR disponible_hasta IS NULL
        OR disponible_desde < disponible_hasta
    ),
    CONSTRAINT chk_prueba_limite CHECK (limite_intentos IS NULL OR limite_intentos > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE pregunta (
    id BIGINT NOT NULL AUTO_INCREMENT,
    actividad_id BIGINT NULL,
    prueba_final_id BIGINT NULL,
    enunciado VARCHAR(1000) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    orden INT NOT NULL,
    explicacion VARCHAR(1000) NULL,
    texto_correcto VARCHAR(500) NULL,
    creado_en DATETIME(6) NOT NULL,
    actualizado_en DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_pregunta_actividad FOREIGN KEY (actividad_id) REFERENCES actividad_desafio (id),
    CONSTRAINT fk_pregunta_prueba FOREIGN KEY (prueba_final_id) REFERENCES prueba_final (id),
    CONSTRAINT chk_pregunta_un_padre CHECK (
        (actividad_id IS NOT NULL AND prueba_final_id IS NULL)
        OR (actividad_id IS NULL AND prueba_final_id IS NOT NULL)
    ),
    CONSTRAINT chk_pregunta_tipo CHECK (tipo IN ('ASOCIACION', 'SECUENCIA', 'COMPLETACION', 'SELECCION')),
    CONSTRAINT chk_pregunta_texto_correcto CHECK (
        (tipo = 'COMPLETACION' AND texto_correcto IS NOT NULL)
        OR (tipo <> 'COMPLETACION' AND texto_correcto IS NULL)
    )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Solucionario de SELECCION: es_correcta. No se expone en el GET del estudiante.
CREATE TABLE alternativa (
    id BIGINT NOT NULL AUTO_INCREMENT,
    pregunta_id BIGINT NOT NULL,
    texto VARCHAR(500) NOT NULL,
    es_correcta BOOLEAN NOT NULL,
    orden INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_alternativa_pregunta FOREIGN KEY (pregunta_id) REFERENCES pregunta (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Lados de ASOCIACION. La clave es el texto que envia el cliente (por ejemplo "A" o "2").
CREATE TABLE elemento_asociacion (
    id BIGINT NOT NULL AUTO_INCREMENT,
    pregunta_id BIGINT NOT NULL,
    lado VARCHAR(10) NOT NULL,
    clave VARCHAR(32) NOT NULL,
    texto VARCHAR(500) NOT NULL,
    orden INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_elemento_clave UNIQUE (pregunta_id, lado, clave),
    CONSTRAINT fk_elemento_pregunta FOREIGN KEY (pregunta_id) REFERENCES pregunta (id),
    CONSTRAINT chk_elemento_lado CHECK (lado IN ('IZQUIERDA', 'DERECHA'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Solucionario de ASOCIACION.
CREATE TABLE par_correcto (
    id BIGINT NOT NULL AUTO_INCREMENT,
    pregunta_id BIGINT NOT NULL,
    izquierda_id BIGINT NOT NULL,
    derecha_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_par_pregunta FOREIGN KEY (pregunta_id) REFERENCES pregunta (id),
    CONSTRAINT fk_par_izquierda FOREIGN KEY (izquierda_id) REFERENCES elemento_asociacion (id),
    CONSTRAINT fk_par_derecha FOREIGN KEY (derecha_id) REFERENCES elemento_asociacion (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Solucionario de SECUENCIA: orden_correcto. La clave es el itemId que envia el cliente.
CREATE TABLE item_secuencia (
    id BIGINT NOT NULL AUTO_INCREMENT,
    pregunta_id BIGINT NOT NULL,
    clave VARCHAR(32) NOT NULL,
    texto VARCHAR(500) NOT NULL,
    orden_correcto INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_item_clave UNIQUE (pregunta_id, clave),
    CONSTRAINT fk_item_pregunta FOREIGN KEY (pregunta_id) REFERENCES pregunta (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Copia local del evento de publicacion. El historial central vive en ms-admin.
CREATE TABLE transicion_publicacion (
    id BIGINT NOT NULL AUTO_INCREMENT,
    evento_id VARCHAR(36) NOT NULL,
    recurso_tipo VARCHAR(20) NOT NULL,
    recurso_id BIGINT NOT NULL,
    estado_anterior VARCHAR(20) NOT NULL,
    estado_nuevo VARCHAR(20) NOT NULL,
    usuario_id BIGINT NOT NULL,
    ocurrido_en DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_transicion_evento UNIQUE (evento_id),
    CONSTRAINT chk_transicion_recurso CHECK (recurso_tipo IN ('ACTIVIDAD', 'PRUEBA_FINAL')),
    CONSTRAINT chk_transicion_estado_anterior CHECK (estado_anterior IN ('BORRADOR', 'EN_REVISION', 'AUTORIZADO', 'PUBLICADO', 'DESHABILITADO')),
    CONSTRAINT chk_transicion_estado_nuevo CHECK (estado_nuevo IN ('BORRADOR', 'EN_REVISION', 'AUTORIZADO', 'PUBLICADO', 'DESHABILITADO'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_actividad_objetivo ON actividad_desafio (objetivo_aprendizaje_id, estado);
CREATE INDEX idx_prueba_objetivo ON prueba_final (objetivo_aprendizaje_id, estado);
CREATE INDEX idx_pregunta_actividad ON pregunta (actividad_id);
CREATE INDEX idx_pregunta_prueba ON pregunta (prueba_final_id);
CREATE INDEX idx_alternativa_pregunta ON alternativa (pregunta_id);
CREATE INDEX idx_par_pregunta ON par_correcto (pregunta_id);
CREATE INDEX idx_transicion_recurso ON transicion_publicacion (recurso_tipo, recurso_id);
