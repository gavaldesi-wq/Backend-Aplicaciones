
-- TABLA ASIGNATURA
CREATE TABLE asignatura (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    nivel VARCHAR(50) NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uq_asignatura_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- TABLA UNIDAD
CREATE TABLE unidad (
    id BIGINT NOT NULL AUTO_INCREMENT,
    asignatura_id BIGINT NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    orden INT NOT NULL,
    estado ENUM(
        'BORRADOR',
        'EN_REVISION',
        'AUTORIZADO',
        'PUBLICADO',
        'DESHABILITADO'
    ) NOT NULL DEFAULT 'BORRADOR',

    PRIMARY KEY (id),
    INDEX idx_unidad_asignatura (asignatura_id),

    CONSTRAINT fk_unidad_asignatura
        FOREIGN KEY (asignatura_id)
        REFERENCES asignatura(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- TABLA OBJETIVO DE APRENDIZAJE
CREATE TABLE objetivo_aprendizaje (
    id BIGINT NOT NULL AUTO_INCREMENT,
    unidad_id BIGINT NOT NULL,
    codigo VARCHAR(30) NOT NULL,
    descripcion TEXT NOT NULL,
    eje_curricular VARCHAR(150),
    texto_referencia TEXT,
    orden INT NOT NULL,
    estado ENUM(
        'BORRADOR',
        'EN_REVISION',
        'AUTORIZADO',
        'PUBLICADO',
        'DESHABILITADO'
    ) NOT NULL DEFAULT 'BORRADOR',

    PRIMARY KEY (id),
    INDEX idx_objetivo_unidad (unidad_id),

    CONSTRAINT fk_objetivo_unidad
        FOREIGN KEY (unidad_id)
        REFERENCES unidad(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- TABLA CONTENIDO DIDACTICO
CREATE TABLE contenido_didactico (
    id BIGINT NOT NULL AUTO_INCREMENT,
    objetivo_aprendizaje_id BIGINT NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    explicacion_escrita TEXT,
    archivo_audio VARCHAR(500),
    ejemplos_guiados TEXT,
    instrucciones TEXT,
    orden INT NOT NULL,
    responsable_origen VARCHAR(200),
    licencia VARCHAR(200),
    estado ENUM(
        'BORRADOR',
        'EN_REVISION',
        'AUTORIZADO',
        'PUBLICADO',
        'DESHABILITADO'
    ) NOT NULL DEFAULT 'BORRADOR',

    PRIMARY KEY (id),
    INDEX idx_contenido_objetivo (objetivo_aprendizaje_id),

    CONSTRAINT fk_contenido_objetivo
        FOREIGN KEY (objetivo_aprendizaje_id)
        REFERENCES objetivo_aprendizaje(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- TABLA IMAGENES DEL CONTENIDO
CREATE TABLE contenido_imagen (
    contenido_id BIGINT NOT NULL,
    orden INT NOT NULL,
    imagen_url VARCHAR(500) NOT NULL,

    PRIMARY KEY (contenido_id, orden),

    CONSTRAINT fk_imagen_contenido
        FOREIGN KEY (contenido_id)
        REFERENCES contenido_didactico(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
