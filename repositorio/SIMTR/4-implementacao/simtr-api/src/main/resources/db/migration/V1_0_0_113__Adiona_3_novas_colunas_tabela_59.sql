DO $$
BEGIN
	IF EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb059_objeto_integracao' 
	) THEN 
	
	  ALTER TABLE mtr.mtrtb059_objeto_integracao ADD COLUMN ic_comparacao_completa bool;
	  ALTER TABLE mtr.mtrtb059_objeto_integracao ADD COLUMN ic_operador_comparacao varchar(10);
	  ALTER TABLE mtr.mtrtb059_objeto_integracao ADD COLUMN ic_acao_comparacao varchar(1);

	END IF;
END $$ 