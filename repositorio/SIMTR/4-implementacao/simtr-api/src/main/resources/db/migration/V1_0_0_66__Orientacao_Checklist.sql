DO $$
BEGIN

/* Tabela 051 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb051_checklist' AND COLUMN_NAME = 'de_orientacao_operador'
) THEN 
    ALTER TABLE mtr.mtrtb051_checklist ADD COLUMN de_orientacao_operador TEXT;
    COMMENT ON COLUMN mtr.mtrtb051_checklist.de_orientacao_operador IS 'Atributo utilizado para armazenar a orientação a ser apresentado ao operador para indicando uma orientação geral sobre o preenchimento do checklist,';
END IF;

END $$;