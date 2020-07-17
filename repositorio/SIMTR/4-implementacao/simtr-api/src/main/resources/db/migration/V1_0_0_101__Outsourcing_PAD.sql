DO $$
BEGIN

/* tb003 */
----------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb003_documento' AND COLUMN_NAME = 'ix_pad'
) THEN
	ALTER TABLE mtr.mtrtb003_documento ADD COLUMN ix_pad DECIMAL(5,2);
    COMMENT ON COLUMN mtr.mtrtb003_documento.ix_pad IS 'Atributo utilizado para armazenar o ultimo indice de assertividade do documento apurado na atividade de extração de dados do outsourcing documental.';
END IF;

/* tb050 */
----------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb050_controle_documento' AND COLUMN_NAME = 'ix_pad'
) THEN
	ALTER TABLE mtr.mtrtb050_controle_documento ADD COLUMN ix_pad DECIMAL(5,2);
    COMMENT ON COLUMN mtr.mtrtb050_controle_documento.ix_pad IS 'Atributo utilizado para armazenar o indice de assertividade do documento apurado na atividade de extração de dados do outsourcing documental.';
END IF;

END $$;