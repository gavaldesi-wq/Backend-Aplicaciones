-- Cadena demo alineada al contrato: OA 31 + unidad publicada para estudiante/docente.
INSERT INTO unidad (id, asignatura_id, titulo, orden, estado)
VALUES (10, 1, 'Idea principal (demo)', 1, 'PUBLICADO')
ON DUPLICATE KEY UPDATE
    titulo = VALUES(titulo),
    orden = VALUES(orden),
    estado = VALUES(estado);

INSERT INTO objetivo_aprendizaje (
    id, unidad_id, codigo, descripcion, eje_curricular, texto_referencia, orden, estado
) VALUES (
    31,
    10,
    'OA-LEN-31',
    'Identificar la idea principal de un texto breve',
    'Lectura',
    'Texto corto de demostración para la defensa',
    1,
    'PUBLICADO'
)
ON DUPLICATE KEY UPDATE
    unidad_id = VALUES(unidad_id),
    codigo = VALUES(codigo),
    descripcion = VALUES(descripcion),
    eje_curricular = VALUES(eje_curricular),
    texto_referencia = VALUES(texto_referencia),
    orden = VALUES(orden),
    estado = VALUES(estado);

INSERT INTO contenido_didactico (
    id,
    objetivo_aprendizaje_id,
    titulo,
    explicacion_escrita,
    archivo_audio,
    ejemplos_guiados,
    instrucciones,
    orden,
    responsable_origen,
    licencia,
    estado
) VALUES (
    31,
    31,
    'Material demo idea principal',
    'La idea principal es lo más importante que el texto quiere decir.',
    NULL,
    'Ejemplo: En un texto sobre el sol, la idea principal puede ser que el sol da luz y calor.',
    'Lee el texto y marca la oración que resume la idea principal.',
    1,
    'MPAZ Demo',
    'CC BY 4.0',
    'PUBLICADO'
)
ON DUPLICATE KEY UPDATE
    objetivo_aprendizaje_id = VALUES(objetivo_aprendizaje_id),
    titulo = VALUES(titulo),
    explicacion_escrita = VALUES(explicacion_escrita),
    ejemplos_guiados = VALUES(ejemplos_guiados),
    instrucciones = VALUES(instrucciones),
    orden = VALUES(orden),
    responsable_origen = VALUES(responsable_origen),
    licencia = VALUES(licencia),
    estado = VALUES(estado);

ALTER TABLE unidad AUTO_INCREMENT = 11;
ALTER TABLE objetivo_aprendizaje AUTO_INCREMENT = 32;
ALTER TABLE contenido_didactico AUTO_INCREMENT = 32;
