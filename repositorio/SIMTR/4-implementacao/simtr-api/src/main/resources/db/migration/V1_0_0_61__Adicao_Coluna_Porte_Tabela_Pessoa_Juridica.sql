DO $$
BEGIN

/* Tabela 001 */
---------------

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_juridica' and COLUMN_NAME = 'porte'
) THEN 
    ALTER TABLE mtr.mtrtb001_pessoa_juridica DROP COLUMN porte;
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_juridica' and COLUMN_NAME = 'ic_porte'
) THEN 
    ALTER TABLE mtr.mtrtb001_pessoa_juridica ADD COLUMN ic_porte VARCHAR(10) NOT NULL DEFAULT 'DEMAIS';
    ALTER TABLE mtr.mtrtb001_pessoa_juridica ALTER COLUMN ic_porte DROP DEFAULT;
    COMMENT ON COLUMN mtr.mtrtb001_pessoa_juridica.ic_porte IS 'Atributo utilizado para identificar o porte da empresa, perante a receita federal. Exemplos
    - ME
    - EPP
    - MEI
    - DMS';
END IF;

END $$;