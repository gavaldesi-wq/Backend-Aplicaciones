CREATE TABLE usuario (
    id BIGINT NOT NULL AUTO_INCREMENT,

    codigo_usuario VARCHAR(30) NOT NULL,
    nombre_completo VARCHAR(150) NOT NULL,
    clave_hash VARCHAR(255) NOT NULL,

    rol ENUM(
        'UTP',
        'DOCENTE',
        'ESTUDIANTE'
    ) NOT NULL,

    activo BIT(1) NOT NULL DEFAULT b'1',

    PRIMARY KEY (id),
    UNIQUE KEY uq_usuario_codigo (codigo_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
