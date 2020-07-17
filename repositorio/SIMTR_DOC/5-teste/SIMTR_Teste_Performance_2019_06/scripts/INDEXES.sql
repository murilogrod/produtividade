CREATE INDEX ix_mtrtb001_02
  ON mtr.mtrtb001_dossie_cliente
  USING btree
  (ic_tipo_pessoa);
  
---------------------------------  
CREATE INDEX ix_mtrtb001_01_pf
  ON mtr.mtrtb001_pessoa_fisica
  USING btree
  (nu_dossie_cliente);
  
CREATE INDEX ix_mtrtb001_01_pj
  ON mtr.mtrtb001_pessoa_juridica
  USING btree
  (nu_dossie_cliente);

---------------------------------
  
CREATE INDEX ix_mtrtb002_01
  ON mtr.mtrtb002_dossie_produto
  USING btree
  (nu_processo_fase);  
  
CREATE INDEX ix_mtrtb002_02
  ON mtr.mtrtb002_dossie_produto
  USING btree
  (nu_processo_dossie); 
  
  
--------------------------------
  
CREATE INDEX ix_mtrtb003_01
  ON mtr.mtrtb003_documento
  USING btree
  (nu_canal_captura);

CREATE INDEX ix_mtrtb003_02
  ON mtr.mtrtb003_documento
  USING btree
  (nu_tipo_documento);  
 
--------------------------------

CREATE INDEX ix_mtrtb004_03
  ON mtr.mtrtb004_dossie_cliente_produto
  USING btree
  (nu_dossie_cliente);
 
CREATE INDEX ix_mtrtb004_04
  ON mtr.mtrtb004_dossie_cliente_produto
  USING btree
  (nu_dossie_cliente_relacionado); 

CREATE INDEX ix_mtrtb004_05
  ON mtr.mtrtb004_dossie_cliente_produto
  USING btree
  (nu_dossie_produto);

--------------------------------
CREATE INDEX ix_mtrtb005_01
  ON mtr.mtrtb005_documento_cliente
  USING btree
  (nu_documento);

CREATE INDEX ix_mtrtb005_02
  ON mtr.mtrtb005_documento_cliente
  USING btree
  (nu_dossie_cliente);
--------------------------------
 
CREATE INDEX ix_mtrtb007_01
  ON mtr.mtrtb007_atributo_documento
  USING btree
  (nu_documento);
  
---------------------------------
CREATE INDEX ix_mtrtb011_01
  ON mtr.mtrtb011_funcao_documento
  USING btree
  (nu_tipo_documento);
  
CREATE INDEX ix_mtrtb011_02
  ON mtr.mtrtb011_funcao_documento
  USING btree
  (nu_funcao_documental);

  
---------------------------------
CREATE INDEX ix_mtrtb013_01
  ON mtr.mtrtb013_situacao_dossie
  USING btree
  (nu_tipo_situacao_dossie);
  
CREATE INDEX ix_mtrtb013_02
  ON mtr.mtrtb013_situacao_dossie
  USING btree
  (nu_dossie_produto);
  
CREATE INDEX ix_mtrtb013_03
  ON mtr.mtrtb013_situacao_dossie
  USING btree
  (ts_inclusao);
  
CREATE INDEX ix_mtrtb013_04
  ON mtr.mtrtb013_situacao_dossie
  USING btree
  (ts_saida);
  
--------------------------------

CREATE INDEX ix_mtrtb014_02
  ON mtr.mtrtb014_instancia_documento
  USING btree
  (nu_elemento_conteudo);

CREATE INDEX ix_mtrtb014_03
  ON mtr.mtrtb014_instancia_documento
  USING btree
  (nu_documento);
  
CREATE INDEX ix_mtrtb014_04
  ON mtr.mtrtb014_instancia_documento
  USING btree
  (nu_dossie_produto);
  
CREATE INDEX ix_mtrtb014_05
  ON mtr.mtrtb014_instancia_documento
  USING btree
  (nu_dossie_cliente_produto);
  
CREATE INDEX ix_mtrtb014_06
  ON mtr.mtrtb014_instancia_documento
  USING btree
  (nu_garantia_informada);
  
--------------------------------
CREATE INDEX ix_mtrtb017_01
  ON mtr.mtrtb017_stco_instnca_documento
  USING btree
  (nu_situacao_documento);
  
CREATE INDEX ix_mtrtb017_02
  ON mtr.mtrtb017_stco_instnca_documento
  USING btree
  (nu_instancia_documento);
  
CREATE INDEX ix_mtrtb017_03
  ON mtr.mtrtb017_stco_instnca_documento
  USING btree
  (nu_motivo_stco_documento);

-------------------------------
CREATE INDEX ix_mtrtb024_01
  ON mtr.mtrtb024_produto_dossie
  USING btree
  (nu_dossie_produto);
  
CREATE INDEX ix_mtrtb024_02
  ON mtr.mtrtb024_produto_dossie
  USING btree
  (nu_produto);
  
-------------------------------
CREATE INDEX ix_mtrtb025_01
  ON mtr.mtrtb025_processo_documento
  USING btree
  (nu_processo);
  
CREATE INDEX ix_mtrtb025_02
  ON mtr.mtrtb025_processo_documento
  USING btree
  (nu_tipo_documento);
  
CREATE INDEX ix_mtrtb025_03
  ON mtr.mtrtb025_processo_documento
  USING btree
  (nu_funcao_documental);
  
-------------------------------
CREATE INDEX ix_mtrtb038_01
  ON mtr.mtrtb038_nivel_documental
  USING btree
  (nu_dossie_cliente);

CREATE INDEX ix_mtrtb038_02
  ON mtr.mtrtb038_nivel_documental
  USING btree
  (nu_composicao_documental);

-------------------------------
CREATE INDEX ix_mtrtb039_01
  ON mtr.mtrtb039_produto_composicao
  USING btree
  (nu_composicao_documental);
  
CREATE INDEX ix_mtrtb039_02
  ON mtr.mtrtb039_produto_composicao
  USING btree
  (nu_produto);

