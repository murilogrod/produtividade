DO $$
BEGIN
/* Tabela 009 */
IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb009_tipo_documento' AND COLUMN_NAME = 'ic_ativo'
) THEN 
	ALTER TABLE mtr.mtrtb009_tipo_documento
	   ALTER COLUMN ic_ativo DROP NOT NULL;
	COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.ic_ativo IS 'Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema';
END IF;

END $$;