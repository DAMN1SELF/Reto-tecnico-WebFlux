CREATE TABLE IF NOT EXISTS tmAlumno (
    id VARCHAR(250) DEFAULT RANDOM_UUID(),
    nombre VARCHAR(120) NOT NULL,
    apellido VARCHAR(120) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    edad INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_tm_alumno_nombre_apellido UNIQUE (nombre, apellido)
    );