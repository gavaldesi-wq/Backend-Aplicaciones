
CREATE TABLE curso (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    anio_escolar INT NOT NULL,
    docente_id BIGINT NOT NULL,
    activo BIT(1) NOT NULL DEFAULT b'1',

    PRIMARY KEY (id),
    UNIQUE KEY uq_curso_nombre_anio (nombre, anio_escolar),
    CONSTRAINT fk_curso_docente
        FOREIGN KEY (docente_id) REFERENCES usuario(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE matricula (
    id BIGINT NOT NULL AUTO_INCREMENT,
    curso_id BIGINT NOT NULL,
    estudiante_id BIGINT NOT NULL,
    activo BIT(1) NOT NULL DEFAULT b'1',

    PRIMARY KEY (id),
    UNIQUE KEY uq_matricula_curso_estudiante
        (curso_id, estudiante_id),
    CONSTRAINT fk_matricula_curso
        FOREIGN KEY (curso_id) REFERENCES curso(id),
    CONSTRAINT fk_matricula_estudiante
        FOREIGN KEY (estudiante_id) REFERENCES usuario(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
