DO $$
BEGIN
	
	IF EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb059_objeto_integracao' 
	) THEN 
	
	  ALTER TABLE mtr.mtrtb059_objeto_integracao ADD COLUMN no_objeto_consulta VARCHAR(100);

	END IF;
	
	IF EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb060_atributo_integracao' 
	) THEN 
	
	  ALTER TABLE mtr.mtrtb060_atributo_integracao ADD COLUMN no_atributo_consulta VARCHAR(100);

	END IF;
	
END $$ 