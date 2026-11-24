-- Asignacion de OA 31 al curso 1, con la ventana y los 2 intentos del contrato.
-- docente_id = 2 es el id provisional de DOC01. EST01 es el id 3 en el contrato.
-- estudiante_id NULL significa el curso completo.

INSERT INTO asignacion (
    id, curso_id, estudiante_id, objetivo_aprendizaje_id, docente_id,
    disponible_desde, disponible_hasta, limite_intentos_prueba, activa,
    creado_en, actualizado_en
) VALUES (
    1, 1, NULL, 31, 2,
    '2026-11-21 12:00:00.000000', '2026-12-15 23:59:59.000000', 2, TRUE,
    '2026-11-21 12:00:00.000000', '2026-11-21 12:00:00.000000'
);

ALTER TABLE asignacion AUTO_INCREMENT = 2;
