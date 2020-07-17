DO $$
BEGIN

/* Tabela 019 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND COLUMN_NAME = 'no_campo'
) THEN 
    ALTER TABLE mtr.mtrtb019_campo_formulario ADD COLUMN no_campo character varying(255) DEFAULT NULL;
    COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.no_campo IS 'Atributo utilizado para armazenar o nome do atributo retornado pela rotina/serviço de extração de dados do documento. Esse valor armazena o nome utilizado no campo de retorno da informação';
END IF;

END $$;