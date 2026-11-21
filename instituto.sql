

SET NAMES utf8mb4;
DROP DATABASE IF EXISTS instituto;
CREATE DATABASE instituto CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE instituto;


CREATE TABLE curso (
    id_curso INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    idioma VARCHAR(50) NOT NULL,
    nivel VARCHAR(30) NOT NULL,
    turno VARCHAR(20) NOT NULL,
    cupo INT NOT NULL,
    vacantes INT NOT NULL,
    cuota_mensual DECIMAL(10, 2) NOT NULL,
    CONSTRAINT chk_curso_cupo CHECK (cupo > 0),
    CONSTRAINT chk_curso_vacantes CHECK (vacantes >= 0 AND vacantes <= cupo),
    CONSTRAINT chk_curso_cuota CHECK (cuota_mensual >= 0)
) ENGINE=InnoDB;


CREATE TABLE alumno (
    id_alumno INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    dni VARCHAR(15) NOT NULL UNIQUE,
    correo VARCHAR(100) NULL
) ENGINE=InnoDB;


CREATE TABLE inscripcion (
    id_inscripcion INT AUTO_INCREMENT PRIMARY KEY,
    id_alumno INT NOT NULL,
    id_curso INT NOT NULL,
    fecha DATE NOT NULL,
    cuota_paga BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_alumno_curso UNIQUE (id_alumno, id_curso),
    CONSTRAINT fk_inscripcion_alumno FOREIGN KEY (id_alumno) 
        REFERENCES alumno(id_alumno) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_inscripcion_curso FOREIGN KEY (id_curso) 
        REFERENCES curso(id_curso) 
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

INSERT INTO curso (codigo, idioma, nivel, turno, cupo, vacantes, cuota_mensual) VALUES
('ING-A1-M', 'Inglés', 'Inicial A1', 'Mañana', 4, 0, 15000.00),
('ING-B2-T', 'Inglés', 'Avanzado B2', 'Tarde', 6, 2, 18000.00),
('FRA-A1-N', 'Francés', 'Inicial A1', 'Noche', 5, 2, 16000.00),
('ITA-A2-T', 'Italiano', 'Elemental A2', 'Tarde', 5, 4, 14000.00);

INSERT INTO alumno (nombre, apellido, dni, correo) VALUES
('Juan', 'Pérez', '35123456', 'juan.perez@gmail.com'),
('María', 'González', '38456789', 'maria.gonzalez@hotmail.com'),
('Carlos', 'Rodríguez', '40789123', 'carlos.rodriguez@yahoo.com'),
('Ana', 'López', '42111222', NULL),
('Lucas', 'Díaz', '39333444', 'lucas.diaz@gmail.com'),
('Sofía', 'Martínez', '41555666', 'sofia.martinez@outlook.com'),
('Diego', 'Fernández', '37777888', 'diego.fernandez@gmail.com'),
('Valentina', 'Gómez', '43999000', 'valentina.gomez@gmail.com');


INSERT INTO inscripcion (id_alumno, id_curso, fecha, cuota_paga) VALUES
(1, 1, '2026-03-01', TRUE),
(2, 1, '2026-03-02', FALSE),
(3, 1, '2026-03-03', TRUE),
(4, 1, '2026-03-04', FALSE);


INSERT INTO inscripcion (id_alumno, id_curso, fecha, cuota_paga) VALUES
(1, 2, '2026-03-05', TRUE),
(5, 2, '2026-03-05', FALSE),
(6, 2, '2026-03-06', TRUE),
(7, 2, '2026-03-07', FALSE);


INSERT INTO inscripcion (id_alumno, id_curso, fecha, cuota_paga) VALUES
(2, 3, '2026-03-08', TRUE),
(3, 3, '2026-03-08', FALSE),
(8, 3, '2026-03-09', TRUE);

INSERT INTO inscripcion (id_alumno, id_curso, fecha, cuota_paga) VALUES
(5, 4, '2026-03-10', FALSE);
