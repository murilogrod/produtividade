DO $$ 
BEGIN
	
	/* CRIAR SEQUENCIA da tabela 104_dominio */ 
	--------------- 
	IF EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb104_dominio' 
	) THEN 

		CREATE SEQUENCE mtr.mtrsq104_dominio;
		ALTER TABLE mtr.mtrtb104_dominio ALTER COLUMN nu_dominio SET DEFAULT nextval('mtr.mtrsq104_dominio'::regclass);
		
 	END IF;
 	
 	
 	/* CRIAR SEQUENCIA da tabela 105_opcoes_dominio  */ 
	--------------- 
	IF EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb105_opcoes_dominio' 
	) THEN 

		CREATE SEQUENCE mtr.mtrsq105_opcoes_dominio;
		ALTER TABLE mtr.mtrtb105_opcoes_dominio ALTER COLUMN nu_opcao_dominio SET DEFAULT nextval('mtr.mtrsq105_opcoes_dominio'::regclass);
		
 	END IF;
	
END $$;