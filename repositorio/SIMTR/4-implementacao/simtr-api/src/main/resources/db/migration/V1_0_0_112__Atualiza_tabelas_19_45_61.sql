DO $$ 
BEGIN 
	
	/* adiciona unique na tabela mtrtb061_priorizacao_objeto_integracao  */ 
	--------------- 
	IF EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb061_priorizacao_objeto_integracao' 
	) THEN 
	
		CREATE UNIQUE INDEX ix_mtrtb061_01
	  	ON mtr.mtrtb061_priorizacao_objeto_integracao
	  	USING btree
	  	(nu_priorizacao_atributo_integracao, nu_processo_dossie, nu_ordem_prioridade);
	
	END IF;
	
 	/* atualiza tabela mtrtb045_atributo_extracao  */ 
	--------------- 
	IF EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb060_atributo_integracao' 
	) THEN 
	
	  ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN nu_atributo_integracao INT4;
	  ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN no_array_documento varchar(100);
 
	  ALTER TABLE mtr.mtrtb045_atributo_extracao ADD CONSTRAINT fk_mtrtb45_fk_mtrtb1_mtrtb60 FOREIGN KEY (nu_atributo_integracao)
      REFERENCES mtr.mtrtb060_atributo_integracao (nu_atributo_integracao) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT;
	
	END IF;
	
	/* atualiza tabela mtrtb019_campo_formulario  */ 
	--------------- 
	IF EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb060_atributo_integracao' 
	) THEN 
	
	  ALTER TABLE mtr.mtrtb019_campo_formulario ADD COLUMN nu_atributo_integracao INT4;

	  ALTER TABLE mtr.mtrtb019_campo_formulario ADD CONSTRAINT fk_mtrtb019_mtrtb060 FOREIGN KEY (nu_atributo_integracao)
      REFERENCES mtr.mtrtb060_atributo_integracao (nu_atributo_integracao) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT;
	
	END IF;
	
END $$;       