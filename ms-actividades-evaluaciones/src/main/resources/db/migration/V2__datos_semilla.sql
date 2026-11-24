-- Fixtures del contrato: actividad 40, pregunta 301, alternativas 501-503, prueba 70.
-- creado_por_usuario_id = 1 es el id provisional de UTP01 hasta alinearlo con ms-admin.
-- La pregunta 302 y las alternativas 511-513 solo existen para que la prueba no quede vacia.

INSERT INTO actividad_desafio (
    id, objetivo_aprendizaje_id, tipo, titulo, estado, orden,
    creado_por_usuario_id, creado_en, actualizado_en
) VALUES (
    40, 31, 'SELECCION', 'Práctica de idea principal', 'PUBLICADO', 1,
    1, '2026-11-20 12:00:00.000000', '2026-11-21 12:00:00.000000'
);

INSERT INTO prueba_final (
    id, objetivo_aprendizaje_id, titulo, estado,
    disponible_desde, disponible_hasta, limite_intentos,
    creado_por_usuario_id, creado_en, actualizado_en
) VALUES (
    70, 31, 'Prueba final OA1', 'PUBLICADO',
    '2026-11-21 12:00:00.000000', '2026-12-15 23:59:59.000000', 2,
    1, '2026-11-20 12:00:00.000000', '2026-11-21 12:00:00.000000'
);

INSERT INTO pregunta (
    id, actividad_id, prueba_final_id, enunciado, tipo, orden,
    explicacion, texto_correcto, creado_en, actualizado_en
) VALUES (
    301, 40, NULL, '¿Que es la idea principal?', 'SELECCION', 1,
    'La idea principal expresa la informacion fundamental del texto.', NULL,
    '2026-11-20 12:00:00.000000', '2026-11-21 12:00:00.000000'
);

INSERT INTO alternativa (id, pregunta_id, texto, es_correcta, orden) VALUES
    (501, 301, 'Una informacion secundaria', FALSE, 1),
    (502, 301, 'La informacion mas importante', TRUE, 2),
    (503, 301, 'Una palabra aislada', FALSE, 3);

INSERT INTO pregunta (
    id, actividad_id, prueba_final_id, enunciado, tipo, orden,
    explicacion, texto_correcto, creado_en, actualizado_en
) VALUES (
    302, NULL, 70, '¿Que es la idea principal?', 'SELECCION', 1,
    'La idea principal expresa la informacion fundamental del texto.', NULL,
    '2026-11-20 12:00:00.000000', '2026-11-21 12:00:00.000000'
);

INSERT INTO alternativa (id, pregunta_id, texto, es_correcta, orden) VALUES
    (511, 302, 'Una informacion secundaria', FALSE, 1),
    (512, 302, 'La informacion mas importante', TRUE, 2),
    (513, 302, 'Una palabra aislada', FALSE, 3);

ALTER TABLE actividad_desafio AUTO_INCREMENT = 41;
ALTER TABLE prueba_final AUTO_INCREMENT = 71;
ALTER TABLE pregunta AUTO_INCREMENT = 303;
ALTER TABLE alternativa AUTO_INCREMENT = 514;
