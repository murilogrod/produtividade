DO $$
BEGIN
	
	
/* Tabela 006 */
---------------

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb006_canal' AND COLUMN_NAME = 'co_integracao'
) THEN
	ALTER TABLE mtr.mtrtb006_canal DROP COLUMN co_integracao;
END IF;

END $$;