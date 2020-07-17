-----------------------------------------------------ALTER TABLE mtrsm001.mtrtb008_conteudo-----------------------------------------------------
ALTER TABLE mtrsm001.mtrtb008_conteudo
DROP CONSTRAINT fk_mtrtb008_mtrtb003;

ALTER TABLE mtrsm001.mtrtb008_conteudo
  ADD CONSTRAINT fk_mtrtb008_mtrtb003 FOREIGN KEY (nu_documento)
      REFERENCES mtrsm001.mtrtb003_documento (nu_documento) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE CASCADE;
-----------------------------------------------------FIM ALTER TABLE mtrsm001.mtrtb008_conteudo-----------------------------------------------------

-----------------------------------------------------ALTER TABLE mtrsm001.mtrtb014_instancia_documento-----------------------------------------------------      
ALTER TABLE mtrsm001.mtrtb014_instancia_documento
DROP CONSTRAINT fk_mtrtb014_mtrtb003;
      
ALTER TABLE mtrsm001.mtrtb014_instancia_documento
  ADD CONSTRAINT fk_mtrtb014_mtrtb003 FOREIGN KEY (nu_documento)
      REFERENCES mtrsm001.mtrtb003_documento (nu_documento) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE CASCADE;
-----------------------------------------------------FIM ALTER TABLE mtrsm001.mtrtb014_instancia_documento-----------------------------------------------------

-----------------------------------------------------ALTER TABLE MTRSM001.MTRTB004_DOSSIE_CLIENTE_PRODUTO-----------------------------------------------------      
ALTER TABLE MTRSM001.MTRTB004_DOSSIE_CLIENTE_PRODUTO
	ADD COLUMN TS_VINCULO_PESSOA    TIMESTAMP WITHOUT TIME ZONE  NOT NULL DEFAULT NOW();

comment on column MTRSM001.MTRTB004_DOSSIE_CLIENTE_PRODUTO.TS_VINCULO_PESSOA is 
'Atributo utilizado para armazenar a data/hora do vínculo do relacionamento de pessoa ao dossiê produto. ';
-----------------------------------------------------FIM ALTER TABLE MTRSM001.MTRTB004_DOSSIE_CLIENTE_PRODUTO-----------------------------------------------------