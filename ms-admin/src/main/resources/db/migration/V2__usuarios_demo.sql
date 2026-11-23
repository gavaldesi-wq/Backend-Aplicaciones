
INSERT INTO usuario
(codigo_usuario, nombre_completo, clave_hash, rol, activo)
VALUES

(
    'UTP01',
    'Administrador UTP Demo',
    '$2y$10$5jzunCgsmMrJwhRDJjBjy.EMOImRxLtL1oIYfmF.slC3QzlxHp/BC',
    'UTP',
    b'1'
),

(
    'DOC01',
    'Docente Demo',
    '$2y$10$P.ci4meVRrPYdfXQv25T7ON62ItcjadVHfl.HEtI.A3EGNzhIFtU6',
    'DOCENTE',
    b'1'
),

(
    'EST01',
    'Estudiante Demo',
    '$2y$10$NfyW1ExRl3itDNZeIFmnPOCQAIL7eegsnB1gewqaswgFGCXkf59uS',
    'ESTUDIANTE',
    b'1'
);
