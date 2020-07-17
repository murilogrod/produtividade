/* Tabela mtrtb002_dossie_produto */  
ALTER TABLE mtrsm001.mtrtb002_dossie_produto ALTER COLUMN nu_processo_dossie TYPE bigint;
ALTER TABLE mtrsm001.mtrtb002_dossie_produto ALTER COLUMN nu_processo_fase TYPE bigint;

/* Tabela mtrtb003_documento */ 
ALTER TABLE mtrsm001.mtrtb003_documento ALTER COLUMN ic_origem_documento TYPE character varying(1);
ALTER TABLE mtrsm001.mtrtb003_documento ALTER COLUMN ic_temporario TYPE integer;
ALTER TABLE mtrsm001.mtrtb003_documento ALTER COLUMN ix_antifraude TYPE double precision;
ALTER TABLE mtrsm001.mtrtb003_documento ALTER COLUMN nu_tipo_documento TYPE bigint;

/* mtrtb007_atributo_documento */
ALTER TABLE mtrsm001.mtrtb007_atributo_documento ALTER COLUMN IX_ASSERTIVIDADE TYPE double precision;

/* mtrtb009_tipo_documento */
ALTER TABLE mtrsm001.mtrtb009_tipo_documento ALTER COLUMN nu_tipo_documento TYPE bigint;

/* mtrtb010_funcao_documental */
ALTER TABLE mtrsm001.mtrtb010_funcao_documental ALTER COLUMN nu_funcao_documental TYPE bigint;

/* mtrtb011_funcao_documento */
ALTER TABLE mtrsm001.mtrtb011_funcao_documento ALTER COLUMN nu_tipo_documento TYPE bigint;
ALTER TABLE mtrsm001.mtrtb011_funcao_documento ALTER COLUMN nu_funcao_documental TYPE bigint;

/* mtrtb012_tipo_situacao_dossie */
ALTER TABLE mtrsm001.mtrtb012_tipo_situacao_dossie ALTER COLUMN nu_tipo_situacao_dossie TYPE bigint;

/* mtrtb013_situacao_dossie */
ALTER TABLE mtrsm001.mtrtb013_situacao_dossie ALTER COLUMN nu_tipo_situacao_dossie TYPE bigint;

/* mtrtb015_situacao_documento */
ALTER TABLE mtrsm001.mtrtb015_situacao_documento ALTER COLUMN nu_situacao_documento TYPE bigint;

/* mtrtb016_motivo_stco_documento */
ALTER TABLE mtrsm001.mtrtb016_motivo_stco_documento ALTER COLUMN nu_motivo_stco_documento TYPE bigint;
ALTER TABLE mtrsm001.mtrtb016_motivo_stco_documento ALTER COLUMN nu_situacao_documento TYPE bigint;

/* mtrtb017_stco_instnca_documento */
ALTER TABLE mtrsm001.mtrtb017_stco_instnca_documento ALTER COLUMN nu_motivo_stco_documento TYPE bigint;
ALTER TABLE mtrsm001.mtrtb017_stco_instnca_documento ALTER COLUMN nu_situacao_documento TYPE bigint;

/* mtrtb019_campo_formulario */
ALTER TABLE mtrsm001.mtrtb019_campo_formulario ALTER COLUMN nu_processo TYPE bigint;

/* mtrtb020_processo */
ALTER TABLE mtrsm001.mtrtb020_processo ALTER COLUMN nu_processo TYPE bigint;

/* mtrtb021_unidade_autorizada */
ALTER TABLE mtrsm001.mtrtb021_unidade_autorizada ALTER COLUMN nu_processo TYPE bigint;

/* mtrtb022_produto */
ALTER TABLE mtrsm001.mtrtb022_produto ALTER COLUMN ic_tipo_pessoa TYPE character varying(1);

/* mtrtb023_produto_processo */
ALTER TABLE mtrsm001.mtrtb023_produto_processo ALTER COLUMN nu_processo TYPE bigint;

/* mtrtb025_processo_documento */
ALTER TABLE mtrsm001.mtrtb025_processo_documento ALTER COLUMN nu_funcao_documental TYPE bigint;
ALTER TABLE mtrsm001.mtrtb025_processo_documento ALTER COLUMN nu_processo TYPE bigint;
ALTER TABLE mtrsm001.mtrtb025_processo_documento ALTER COLUMN nu_tipo_documento TYPE bigint;

/* mtrtb026_relacao_processo */
ALTER TABLE mtrsm001.mtrtb026_relacao_processo ALTER COLUMN nu_processo_filho TYPE bigint;
ALTER TABLE mtrsm001.mtrtb026_relacao_processo ALTER COLUMN nu_processo_pai TYPE bigint;

/* mtrtb032_elemento_conteudo */
ALTER TABLE mtrsm001.mtrtb032_elemento_conteudo ALTER COLUMN nu_processo TYPE bigint;
ALTER TABLE mtrsm001.mtrtb032_elemento_conteudo ALTER COLUMN nu_tipo_documento TYPE bigint;

/* mtrtb033_garantia */
ALTER TABLE mtrsm001.mtrtb033_garantia ALTER COLUMN nu_garantia TYPE bigint;

/* mtrtb034_garantia_produto */
ALTER TABLE mtrsm001.mtrtb034_garantia_produto ALTER COLUMN nu_garantia TYPE bigint;

/* mtrtb035_garantia_informada */
ALTER TABLE mtrsm001.mtrtb035_garantia_informada ALTER COLUMN nu_garantia TYPE bigint;

/* mtrtb037_regra_documental */
ALTER TABLE mtrsm001.mtrtb037_regra_documental ALTER COLUMN nu_funcao_documental TYPE bigint;
ALTER TABLE mtrsm001.mtrtb037_regra_documental ALTER COLUMN nu_tipo_documento TYPE bigint;

/* mtrtb040_cadeia_tipo_stco_dossie */
ALTER TABLE mtrsm001.mtrtb040_cadeia_tipo_stco_dossie ALTER COLUMN nu_tipo_situacao_atual TYPE bigint;
ALTER TABLE mtrsm001.mtrtb040_cadeia_tipo_stco_dossie ALTER COLUMN nu_tipo_situacao_seguinte TYPE bigint;

/* mtrtb043_documento_garantia */
ALTER TABLE mtrsm001.mtrtb043_documento_garantia ALTER COLUMN nu_garantia TYPE bigint;
ALTER TABLE mtrsm001.mtrtb043_documento_garantia ALTER COLUMN nu_processo TYPE bigint;
ALTER TABLE mtrsm001.mtrtb043_documento_garantia ALTER COLUMN nu_tipo_documento TYPE bigint;

/* MTRTB045_ATRIBUTO_EXTRACAO */
ALTER TABLE mtrsm001.MTRTB045_ATRIBUTO_EXTRACAO ALTER COLUMN NU_TIPO_DOCUMENTO TYPE bigint;

/* MTRTB200_SICLI_ERRO */
ALTER TABLE mtrsm001.MTRTB200_SICLI_ERRO ALTER COLUMN CO_MATRICULA TYPE character varying(7);
ALTER TABLE mtrsm001.MTRTB200_SICLI_ERRO ALTER COLUMN IC_TIPO_PESSOA TYPE character varying(1);