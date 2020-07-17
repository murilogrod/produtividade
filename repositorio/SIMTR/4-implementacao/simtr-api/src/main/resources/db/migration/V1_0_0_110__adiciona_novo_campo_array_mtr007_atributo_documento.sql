DO $$
BEGIN
/* Tabela 007 */
IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb007_atributo_documento' AND COLUMN_NAME = 'de_array'
) THEN 
	ALTER TABLE mtr.mtrtb007_atributo_documento ADD COLUMN de_array varchar(100);
	COMMENT ON COLUMN mtr.mtrtb007_atributo_documento.de_array IS 'Atributo que indica lista de array';
END IF;
END $$;
