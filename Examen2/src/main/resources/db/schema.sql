CREATE DATABASE IF NOT EXISTS examenes
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE examenes;

CREATE TABLE IF NOT EXISTS examen (
    codigo VARCHAR(40) NOT NULL,
    nombre_paciente VARCHAR(160) NOT NULL,
    costo DECIMAL(12, 2) NOT NULL,
    tipo_sangre ENUM('A', 'B', 'AB', 'O') NOT NULL,
    factor_rh ENUM('POSITIVO', 'NEGATIVO') NOT NULL,
    PRIMARY KEY (codigo),
    CONSTRAINT chk_examen_costo_no_negativo CHECK (costo >= 0)
) ENGINE=InnoDB;
