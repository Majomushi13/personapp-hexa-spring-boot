-- Limpiar privilegios y remover usuario y schema si existen
FLUSH PRIVILEGES;
DROP USER IF EXISTS 'persona_db'@'%';
DROP SCHEMA IF EXISTS `persona_db`;

-- Crear usuario y schema
CREATE USER IF NOT EXISTS 'persona_db'@'%' IDENTIFIED BY 'persona_db';
CREATE SCHEMA IF NOT EXISTS `persona_db`; 

-- Otorgar privilegios al usuario sobre el schema
GRANT EXECUTE, TRIGGER, INSERT, UPDATE, DELETE, SELECT ON `persona_db`.* TO 'persona_db'@'%'; 
FLUSH PRIVILEGES;

-- Usar el schema creado
USE `persona_db`;

-- Crear tablas necesarias con sus respectivas claves primarias y foráneas
CREATE TABLE IF NOT EXISTS `persona` (
 `cc` INT(15) NOT NULL,
 `nombre` VARCHAR(45) NOT NULL,
 `apellido` VARCHAR(45) NOT NULL,
 `genero` ENUM('M', 'F') NOT NULL,
 `edad` INT NULL, 
 CONSTRAINT `persona_pk` PRIMARY KEY (`cc`)
);

CREATE TABLE IF NOT EXISTS `profesion` (
 `id` INT(6) NOT NULL,
 `nom` VARCHAR(90) NOT NULL,
 `des` TEXT NULL, 
 CONSTRAINT `profesion_pk` PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `telefono` (
 `num` VARCHAR(15) NOT NULL,
 `oper` VARCHAR(45) NOT NULL,
 `duenio` INT(15) NOT NULL, 
 CONSTRAINT `telefono_pk` PRIMARY KEY (`num`), 
 CONSTRAINT `telefono_persona_fk` FOREIGN KEY (`duenio`) REFERENCES `persona` (`cc`)
);

CREATE TABLE IF NOT EXISTS `estudios` (
 `id_prof` INT(6) NOT NULL,
 `cc_per` INT(15) NOT NULL,
 `fecha` DATE NULL,
 `univer` VARCHAR(50) NULL, 
 CONSTRAINT `estudios_pk` PRIMARY KEY (`id_prof`, `cc_per`),
 CONSTRAINT `estudio_persona_fk` FOREIGN KEY (`cc_per`) REFERENCES `persona` (`cc`), 
 CONSTRAINT `estudio_profesion_fk` FOREIGN KEY (`id_prof`) REFERENCES `profesion` (`id`)
);

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
