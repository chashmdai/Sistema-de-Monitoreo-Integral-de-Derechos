-- ============================================================================
-- SMID 6.7 — Catálogo de Derechos · V2: semilla inicial
-- ----------------------------------------------------------------------------
-- Taxonomía base inspirada en la Convención sobre los Derechos del Niño (CRC).
-- Se insertan con id y alt_key EXPLÍCITOS (deterministas) para que las referencias
-- padre-hijo y las pruebas sean estables. Al final se reposiciona el AUTO_INCREMENT
-- en 1000, de modo que los registros creados por la aplicación no colisionen con
-- los ids de la semilla y queden claramente separados.
--
-- Las raíces se insertan antes que sus hijos para satisfacer la FK fk_derecho_padre.
-- ============================================================================

-- ----------------------------- Raíces (nivel 0) -----------------------------
INSERT INTO derecho (id, alt_key, id_padre, codigo, nombre, descripcion, nivel, orden, vigente, vigente_desde) VALUES
 (1,  '11111111-1111-4111-8111-000000000001', NULL, 'VIDA',              'Derecho a la vida y la supervivencia',                 'Vida, supervivencia y desarrollo del niño, niña o adolescente.',                 0, 1, 1, '2024-01-01'),
 (2,  '11111111-1111-4111-8111-000000000002', NULL, 'SALUD',             'Derecho a la salud',                                   'Acceso a salud física y mental y a servicios de atención.',                      0, 2, 1, '2024-01-01'),
 (3,  '11111111-1111-4111-8111-000000000003', NULL, 'EDU',               'Derecho a la educación',                               'Acceso, permanencia y calidad en la educación.',                                 0, 3, 1, '2024-01-01'),
 (4,  '11111111-1111-4111-8111-000000000004', NULL, 'FAMILIA',           'Derecho a vivir en familia',                           'Vida familiar, cuidados y no separación arbitraria.',                            0, 4, 1, '2024-01-01'),
 (5,  '11111111-1111-4111-8111-000000000005', NULL, 'PROTECCION',        'Derecho a la protección contra toda forma de violencia','Protección frente a maltrato, abuso, explotación y negligencia.',                0, 5, 1, '2024-01-01'),
 (6,  '11111111-1111-4111-8111-000000000006', NULL, 'IDENTIDAD',         'Derecho a la identidad',                               'Nombre, nacionalidad y relaciones familiares.',                                  0, 6, 1, '2024-01-01'),
 (7,  '11111111-1111-4111-8111-000000000007', NULL, 'PARTICIPACION',     'Derecho a la participación y a ser oído',              'Expresar su opinión y que esta sea tomada en cuenta.',                            0, 7, 1, '2024-01-01'),
 (8,  '11111111-1111-4111-8111-000000000008', NULL, 'JUSTICIA',          'Derecho a una justicia especializada',                 'Garantías y trato especializado ante el sistema de justicia.',                   0, 8, 1, '2024-01-01'),
 (9,  '11111111-1111-4111-8111-000000000009', NULL, 'RECREACION',        'Derecho al juego, la recreación y la cultura',         'Juego, descanso, actividades recreativas y vida cultural.',                      0, 9, 1, '2024-01-01'),
 (10, '11111111-1111-4111-8111-000000000010', NULL, 'NIVEL_VIDA',        'Derecho a un nivel de vida adecuado',                  'Condiciones materiales adecuadas para el desarrollo integral.',                  0, 10, 1, '2024-01-01'),
 (11, '11111111-1111-4111-8111-000000000011', NULL, 'NO_DISCRIMINACION', 'Derecho a la no discriminación',                       'Igualdad de trato sin distinción de ningún tipo.',                               0, 11, 1, '2024-01-01');

-- ----------------------------- Hijos (nivel 1) -----------------------------
INSERT INTO derecho (id, alt_key, id_padre, codigo, nombre, descripcion, nivel, orden, vigente, vigente_desde) VALUES
 (12, '11111111-1111-4111-8111-000000000012', 3, 'EDU.ACCESO',            'Acceso y permanencia',          'Acceso oportuno y permanencia en el sistema educativo.',     1, 1, 1, '2024-01-01'),
 (13, '11111111-1111-4111-8111-000000000013', 3, 'EDU.CALIDAD',           'Calidad y pertinencia',         'Calidad de los aprendizajes y pertinencia de la enseñanza.', 1, 2, 1, '2024-01-01'),
 (14, '11111111-1111-4111-8111-000000000014', 5, 'PROTECCION.MALTRATO',   'Maltrato físico o psicológico', 'Protección frente a maltrato físico o psicológico.',         1, 1, 1, '2024-01-01'),
 (15, '11111111-1111-4111-8111-000000000015', 5, 'PROTECCION.ABUSO_SEXUAL','Abuso y explotación sexual',   'Protección frente a abuso y explotación sexual.',            1, 2, 1, '2024-01-01'),
 (16, '11111111-1111-4111-8111-000000000016', 5, 'PROTECCION.NEGLIGENCIA','Negligencia y abandono',        'Protección frente a negligencia y abandono.',                1, 3, 1, '2024-01-01');

-- ----------------------------- Causas de ejemplo -----------------------------
INSERT INTO causa (id, alt_key, id_derecho, codigo, nombre, vigente) VALUES
 (1, '22222222-2222-4222-8222-000000000001', 12, 'DESERCION',     'Deserción escolar',                       1),
 (2, '22222222-2222-4222-8222-000000000002', 12, 'NO_MATRICULA',  'Falta de matrícula',                      1),
 (3, '22222222-2222-4222-8222-000000000003', 14, 'FISICO',        'Maltrato físico',                         1),
 (4, '22222222-2222-4222-8222-000000000004', 14, 'PSICOLOGICO',   'Maltrato psicológico',                    1),
 (5, '22222222-2222-4222-8222-000000000005', 15, 'INTRAFAMILIAR', 'Abuso en el entorno familiar',            1),
 (6, '22222222-2222-4222-8222-000000000006', 2,  'ACCESO_APS',    'Falta de acceso a atención primaria',     1);

-- ------------- Reposicionar autoincrementales por encima de la semilla -------------
ALTER TABLE derecho AUTO_INCREMENT = 1000;
ALTER TABLE causa   AUTO_INCREMENT = 1000;
