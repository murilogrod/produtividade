DO $$
BEGIN
	
	/* ADICIONA UNIQUE para coluna ic_sistema_integracao na tabela mtrtb059_objeto_integracao  */ 
	--------------- 
	IF EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb059_objeto_integracao' AND COLUMN_NAME = 'ic_sistema_integracao'
	) THEN 
		CREATE UNIQUE INDEX ix_mtrtb059_01 ON mtr.mtrtb059_objeto_integracao USING btree (ic_sistema_integracao);
	END IF;
	
	
	/* RENOMEAR coluna no_atributo na tabela mtrtb060_atributo_integracao  */ 
	--------------- 
	IF EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb060_atributo_integracao' AND COLUMN_NAME = 'no_atributo'
	) THEN 
		ALTER TABLE mtr.mtrtb060_atributo_integracao RENAME COLUMN no_atributo TO no_atributo_integracao;
	END IF;

	/* RENOMEAR coluna tipo_atributo e alterar pra varchar(20) na tabela mtrtb060_atributo_integracao  */ 
	--------------- 
	IF EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb060_atributo_integracao' AND COLUMN_NAME = 'tipo_atributo'
	) THEN 
	
		ALTER TABLE mtr.mtrtb060_atributo_integracao RENAME COLUMN tipo_atributo TO tipo_atributo_integracao;
		
		ALTER TABLE mtr.mtrtb060_atributo_integracao ALTER COLUMN tipo_atributo_integracao TYPE VARCHAR(20);
		
	END IF;
	
	/* ADICIONA FOREING KEY da tabela mtrtb061_priorizacao_objeto_integracao para tabela mtrtb020_processo  */ 
	--------------- 
	IF EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb061_priorizacao_objeto_integracao' AND COLUMN_NAME = 'nu_processo_dossie'
	) THEN
	
		ALTER TABLE mtr.mtrtb061_priorizacao_objeto_integracao ADD CONSTRAINT fk_mtrtb061_mtrtb020 FOREIGN KEY (nu_processo_dossie)
      	REFERENCES mtr.mtrtb020_processo (nu_processo) MATCH SIMPLE
      	ON UPDATE RESTRICT ON DELETE RESTRICT;
      	
	END IF;
	
	/* REMOVER UNIQUE da data tabela  mtrtb061_priorizacao_objeto_integracao */ 
	--------------- 
	DROP INDEX IF EXISTS mtr.ix_mtrtb061_01;
	CREATE UNIQUE INDEX ix_mtrtb061_01
	  ON mtr.mtrtb061_priorizacao_objeto_integracao
	  USING btree
	  (nu_processo_dossie, nu_objeto_integracao, nu_ordem_prioridade);
	
END $$;        