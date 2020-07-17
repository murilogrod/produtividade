DO $$
BEGIN
	
	/* ATUALIZA UNIQUE da data tabela  mtrtb059_objeto_integracao */ 
	--------------- 
	DROP INDEX IF EXISTS mtr.ix_mtrtb059_01;
	CREATE UNIQUE INDEX ix_mtrtb059_01
	  ON mtr.mtrtb059_objeto_integracao
	  USING btree
	  (ic_sistema_integracao, no_objeto_integracao);
	
END $$;        