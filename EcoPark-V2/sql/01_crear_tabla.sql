-- ECOPARK / MYSQL / PASO 1
-- Selecciona previamente tu base de datos local en Workbench.
-- No creamos otra base: usamos la que ya tienes, aunque esté vacía.
-- Ejemplo opcional: USE mi_base_ecopark;

-- Una fila puede representar Auto o Moto. La patente identifica un registro.
CREATE TABLE IF NOT EXISTS estadias (
    patente VARCHAR(12) NOT NULL,
    tipo VARCHAR(10) NOT NULL,
    horas INT NOT NULL,
    convenio BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (patente),
    CONSTRAINT chk_ecopark_tipo CHECK (tipo IN ('Auto', 'Moto')),
    CONSTRAINT chk_ecopark_horas CHECK (horas BETWEEN 1 AND 24)
);

-- Comprueba que la tabla quedó creada. Al principio no habrá filas.
SELECT patente, tipo, horas, convenio FROM estadias;

-- EJEMPLOS DE SQL QUE EJECUTA LA APLICACIÓN (NO LOS EJECUTES PARA INICIALIZAR):
-- INSERT INTO estadias (patente, tipo, horas, convenio) VALUES ('ABCD12','Auto',2,FALSE);
-- DELETE FROM estadias WHERE patente = 'ABCD12';
