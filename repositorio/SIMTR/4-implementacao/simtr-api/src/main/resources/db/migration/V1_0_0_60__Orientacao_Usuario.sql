DO $$
BEGIN

/* Tabela 020 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE table_schema = 'mtr' and table_name='mtrtb020_processo' and column_name='de_orientacao'
) THEN 
    ALTER TABLE mtr.mtrtb020_processo ADD COLUMN de_orientacao TEXT;
END IF;

COMMENT ON COLUMN mtr.mtrtb020_processo.de_orientacao IS
'Atributo utilizado para representar a orientação a ser exibida ao usuário indicando o que é esperado a ser feito na fase da operação';

END $$;