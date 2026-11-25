-- Curso demo del DOC01 (id 2) y matrícula del EST01 (id 3).
INSERT INTO curso (id, nombre, anio_escolar, docente_id, activo)
VALUES (1, '2° Básico A', 2026, 2, b'1')
ON DUPLICATE KEY UPDATE
    nombre = VALUES(nombre),
    anio_escolar = VALUES(anio_escolar),
    docente_id = VALUES(docente_id),
    activo = VALUES(activo);

INSERT INTO matricula (curso_id, estudiante_id, activo)
SELECT 1, 3, b'1'
WHERE NOT EXISTS (
    SELECT 1 FROM matricula WHERE curso_id = 1 AND estudiante_id = 3
);

ALTER TABLE curso AUTO_INCREMENT = 2;
