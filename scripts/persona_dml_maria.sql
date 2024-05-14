-- Insertar datos iniciales en las tablas
INSERT INTO `persona`(`cc`,`nombre`,`apellido`,`genero`,`edad`) VALUES
    (123456789, 'Pepe', 'Perez', 'M', 30),
    (987654321, 'Pepito', 'Perez', 'M', NULL),
    (321654987, 'Pepa', 'Juarez', 'F', 30),
    (147258369, 'Pepita', 'Juarez', 'F', 10),
    (963852741, 'Fede', 'Perez', 'M', 18),
    (112233445, 'Ana', 'Ortegon', 'F', 21);

INSERT INTO `telefono` (`num`, `oper`, `duenio`) VALUES
    ('3001234567', 'Claro', 123456789),
    ('3104567890', 'Movistar', 987654321),
    ('3209876543', 'Tigo', 321654987),
    ('3151234567', 'Claro', 147258369),
    ('3058765432', 'Movistar', 963852741);

INSERT INTO `profesion` (`id`, `nom`, `des`) VALUES
    (1, 'Ingeniería de Sistemas', 'Profesional encargado del desarrollo y mantenimiento de sistemas informáticos.'),
    (2, 'Medicina', 'Profesional de la salud encargado de diagnosticar y tratar enfermedades.'),
    (3, 'Derecho', 'Profesional que ejerce la abogacía, defensa y asesoría legal.'),
    (4, 'Arquitectura', 'Profesional que se encarga del diseño de edificaciones y espacios urbanos.'),
    (5, 'Periodismo', 'Profesional dedicado a la recolección, redacción y distribución de noticias.');

INSERT INTO `estudios` (`id_prof`, `cc_per`, `fecha`, `univer`) VALUES
    (1, 123456789, '2021-06-15', 'Universidad Javeriana'),
    (2, 987654321, '2020-05-20', 'Universidad Nacional'),
    (3, 321654987, '2019-11-30', 'Universidad de los Andes'),
    (4, 147258369, '2018-10-05', 'Universidad del Rosario'),
    (5, 963852741, '2022-01-25', 'Universidad Externado de Colombia'),
    (1, 321654987, '2023-03-12', 'Universidad Javeriana'),
    (2, 147258369, '2021-04-18', 'Universidad Nacional'),
    (3, 963852741, '2020-09-10', 'Universidad de los Andes'),
    (4, 123456789, '2022-02-14', 'Universidad del Rosario'),
    (5, 987654321, '2023-05-07', 'Universidad Externado de Colombia');

COMMIT;
