DO $$
BEGIN
	
IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND COLUMN_NAME = 'no_atributo_sicli' AND COLUMN_NAME = 'no_objeto_sicli' AND COLUMN_NAME = 'ic_tipo_sicli'
) THEN
	ALTER TABLE mtr.mtrtb019_campo_formulario ADD COLUMN no_atributo_sicli character varying(100);
	COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.no_atributo_sicli IS 'Atributo utilizado para definir o nome do atributo utilizado na atualização de dados do SICLI.';
	ALTER TABLE mtr.mtrtb019_campo_formulario ADD COLUMN no_objeto_sicli character varying(100);
	COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.no_objeto_sicli IS 'Atributo utilizado para definir o objeto que agrupa o atributo utilizado na atualização de dados do SICLI.';
	ALTER TABLE mtr.mtrtb019_campo_formulario ADD COLUMN ic_tipo_sicli character varying(20);
	COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.ic_tipo_sicli IS 'Atributo utilizado para indicar qual o tipo de atributo definido junto ao objeto a ser enviado na atualização de dados do SICLI.';
END IF;

END $$;