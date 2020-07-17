-- Execução dos flyway #84, #85, #86, #87, #88, #89, #90, #91, #92, #93, #94 e #95

-- INICIO DA TRANSACAO
BEGIN;

-- INICIO FLYWAY #84
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
-- FIM FLYWAY #84


-- INICIO FLYWAY #85
DO $$
BEGIN
	
	
/* Tabela 035 */
---------------

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb035_garantia_informada' AND COLUMN_NAME = 'pc_garantia_informada'
) THEN
	ALTER TABLE mtr.mtrtb035_garantia_informada DROP COLUMN pc_garantia_informada;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb035_garantia_informada' AND COLUMN_NAME = 'ic_forma_garantia'
) THEN
	ALTER TABLE mtr.mtrtb035_garantia_informada DROP COLUMN ic_forma_garantia;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb035_garantia_informada' AND COLUMN_NAME = 'de_garantia'
) THEN
	ALTER TABLE mtr.mtrtb035_garantia_informada DROP COLUMN de_garantia;
END IF;

END $$;
-- FIM FLYWAY #85


-- INICIO FLYWAY #86
DO $$
BEGIN

/* Tabeela 024*/
---------------
--Remove campo em desuso
IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'vr_contrato'
) THEN 
    ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN vr_contrato;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'pc_juros_operacao'
) THEN 
    ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN pc_juros_operacao;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'ic_periodo_juros'
) THEN 
    ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN ic_periodo_juros;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'pz_operacao'
) THEN 
    ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN pz_operacao;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'pz_carencia'
) THEN 
    ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN pz_carencia;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'ic_liquidacao'
) THEN 
    ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN ic_liquidacao;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'co_contrato_renovado'
) THEN 
    ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN co_contrato_renovado;
END IF;

END $$;
-- FIM FLYWAY #86


-- INICIO FLYWAY #87
DO $$
BEGIN
	
	
/* Tabela mtrtb001_dossie_cliente */
---------------

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_dossie_cliente' AND COLUMN_NAME = 'de_telefone'
) THEN
	ALTER TABLE mtr.mtrtb001_dossie_cliente DROP COLUMN de_telefone;
END IF;

/* Tabela mtrtb001_pessoa_fisica */
---------------

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_fisica' AND COLUMN_NAME = 'ic_estado_civil'
) THEN
	ALTER TABLE mtr.mtrtb001_pessoa_fisica DROP COLUMN ic_estado_civil;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_fisica' AND COLUMN_NAME = 'nu_nis'
) THEN
	ALTER TABLE mtr.mtrtb001_pessoa_fisica DROP COLUMN nu_nis;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_fisica' AND COLUMN_NAME = 'nu_identidade'
) THEN
	ALTER TABLE mtr.mtrtb001_pessoa_fisica DROP COLUMN nu_identidade;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_fisica' AND COLUMN_NAME = 'no_orgao_emissor'
) THEN
	ALTER TABLE mtr.mtrtb001_pessoa_fisica DROP COLUMN no_orgao_emissor;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_fisica' AND COLUMN_NAME = 'no_pai'
) THEN
	ALTER TABLE mtr.mtrtb001_pessoa_fisica DROP COLUMN no_pai;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_fisica' AND COLUMN_NAME = 'vr_renda_mensal'
) THEN
	ALTER TABLE mtr.mtrtb001_pessoa_fisica DROP COLUMN vr_renda_mensal;
END IF;

/* Tabela mtrtb001_pessoa_juridica */
---------------

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_juridica' AND COLUMN_NAME = 'ic_segmento'
) THEN
	ALTER TABLE mtr.mtrtb001_pessoa_juridica DROP COLUMN ic_segmento;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_juridica' AND COLUMN_NAME = 'vr_faturamento_anual'
) THEN
	ALTER TABLE mtr.mtrtb001_pessoa_juridica DROP COLUMN vr_faturamento_anual;
END IF;

END $$;
-- FIM FLYWAY #87


-- INICIO FLYWAY #88
DO $$
BEGIN
	
	
/* Tabela mtrtb036_composicao_documental */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb036_composicao_documental' AND COLUMN_NAME = 'ts_ultima_alteracao'
) THEN
	ALTER TABLE mtr.mtrtb036_composicao_documental ADD COLUMN ic_tipo_pessoa VARCHAR(1) NOT NULL DEFAULT 'F';
	COMMENT ON COLUMN mtr.mtrtb036_composicao_documental.ic_tipo_pessoa IS 'Atributo utilizado para indicar o tipo de pessoa que a composição documental possui relação. Pode assumir os valores:
F - Pessoa Física
J - Pessoa Jurídica';
	ALTER TABLE mtr.mtrtb036_composicao_documental ALTER COLUMN ic_tipo_pessoa DROP DEFAULT;
	
	ALTER TABLE mtr.mtrtb036_composicao_documental ADD COLUMN ic_cadastro_caixa BOOLEAN NOT NULL DEFAULT false;
	COMMENT ON COLUMN mtr.mtrtb036_composicao_documental.ic_cadastro_caixa IS 'Atributo utilizado para indicar se a composição documental em referência indica documentos a serem utilizados na atualização do cadastro CAIXA';
	ALTER TABLE mtr.mtrtb036_composicao_documental ALTER COLUMN ic_cadastro_caixa DROP DEFAULT;
	
	ALTER TABLE mtr.mtrtb036_composicao_documental ADD COLUMN ts_ultima_alteracao TIMESTAMP NOT NULL DEFAULT NOW();
	COMMENT ON COLUMN mtr.mtrtb036_composicao_documental.ts_ultima_alteracao IS 'Atributo utilizado para armazenar a data e hora da ultima alteração realizada no registro. Esse valor será utilizado pela API para identificar se houve alteração na estrutura de composições documentais identificando o registro que possui o maior valor e comparando com a data do ultimo carregamento realizado. Toda vez que o registro for alterado ou alguma de suas vinculações que exija pela regra de negócio realizar um recarregamento da estrutura, o registro deve ser atualizado com a data e hora da realização da ação.';
	ALTER TABLE mtr.mtrtb036_composicao_documental ALTER COLUMN ts_ultima_alteracao DROP DEFAULT;
	
END IF;

ALTER TABLE mtr.mtrtb036_composicao_documental DROP CONSTRAINT IF EXISTS cc_mtrtb036_1;
ALTER TABLE mtr.mtrtb036_composicao_documental ADD CONSTRAINT cc_mtrtb036_1 CHECK (ic_tipo_pessoa IN ('F','J') AND ic_tipo_pessoa = UPPER(ic_tipo_pessoa));


END $$;
-- FIM FLYWAY #88


-- INICIO FLYWAY #89
DO $$
BEGIN

/* Tabela 006 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb051_checklist' AND COLUMN_NAME = 'ts_ultima_alteracao'
) THEN 

    ALTER TABLE mtr.mtrtb051_checklist ADD COLUMN ts_inativacao TIMESTAMP;
    COMMENT ON COLUMN mtr.mtrtb051_checklist .ts_inativacao IS 'Atributo utilizado para armazenar a data e hora de inativação de um checklist. Uma vez inativado um checklist não deverá mais ser vinculado, mas poderá ser utilizado como base para criação de outro.';

    ALTER TABLE mtr.mtrtb051_checklist ADD COLUMN ts_ultima_alteracao TIMESTAMP NOT NULL DEFAULT now();
    COMMENT ON COLUMN mtr.mtrtb051_checklist .ts_ultima_alteracao  IS 'Atributo utilizado para armazenar a data e hora da última alteração realizada no registro. Esse valor será utilizado pela API para identificar se houve alteração na estrutura do checklist identificando o registro que possui o maior valor e comparando com a data do último carregamento realizado. Toda vez que o registro for alterado ou alguma de suas vinculações que exija pela regra de negócio realizar um recarregamento da estrutura, o registro deve ser atualizado com a data e hora da realização da ação.';
    ALTER TABLE mtr.mtrtb051_checklist ALTER COLUMN ts_ultima_alteracao DROP DEFAULT;

END IF;

END $$;
-- FIM FLYWAY #89


-- INICIO FLYWAY #90
/* Tabela mtrtb037_regra_documental */
---------------
DO $$
BEGIN
    ALTER TABLE mtr.mtrtb037_regra_documental DROP COLUMN IF EXISTS nu_canal_captura;
    ALTER TABLE mtr.mtrtb037_regra_documental DROP COLUMN IF EXISTS ix_antifraude;
END $$; 

-- FIM FLYWAY #90


-- INICIO FLYWAY #91
DO $$
BEGIN
	
	
/* Tabela mtrtb036_composicao_documental */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb050_controle_documento' AND COLUMN_NAME = 'no_processo'
) THEN
	ALTER TABLE mtr.mtrtb050_controle_documento ADD COLUMN no_processo VARCHAR(100);
	COMMENT ON COLUMN mtr.mtrtb050_controle_documento.no_processo IS 'Atributo utilizando para armazenar um valor a ser usado para sinalizar o fornecedor de alguma forma de atuação especifica definnida sob o acordo de nível operacional Essa informação será utilizada no apoio ao calculo de faturamento e só deverá estar presente nos casosde envio ao fornecedor externo. Para o controle gerado sob a execução da equipe interna da CAIXA (ic_execucao_caixa = true), essa informação não terá validade';
END IF;

END $$;
-- FIM FLYWAY #91


-- INICIO FLYWAY #92
DO $$
BEGIN
	
	
/* TB002 - Produto Dossiê */
---------------------------

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'vr_contrato'
) THEN
	ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN vr_contrato;
	ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN pc_juros_operacao;
	ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN ic_periodo_juros;
	ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN pz_operacao;
	ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN pz_carencia;
	ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN ic_liquidacao;
	ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN co_contrato_renovado;
END IF;

/* TB045 - Indices de Unicidade */
---------------------------------
-- Impedir a criação de dois atributos com o valor true para o cálculo de data vinculados ao mesmo tipo de documento.
DROP INDEX IF EXISTS mtr.ix_mtrtb045_03;
DROP INDEX IF EXISTS mtr.ix_mtrtb045_04;

CREATE UNIQUE INDEX ix_mtrtb045_03 ON mtr.mtrtb045_atributo_extracao (
    nu_tipo_documento,
	ic_calculo_data
)
TABLESPACE mtrtsix000
WHERE ic_calculo_data = true;


CREATE UNIQUE INDEX ix_mtrtb045_04 ON mtr.mtrtb045_atributo_extracao (
	nu_tipo_documento,
	ic_identificador_pessoa
)
TABLESPACE mtrtsix000
WHERE ic_identificador_pessoa = true;

END $$;
-- FIM FLYWAY #92


-- INICIO FLYWAY #93
DO $$
BEGIN

/* Tabela 006 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb006_canal' AND COLUMN_NAME = 'de_token'
) THEN 
    ALTER TABLE mtr.mtrtb006_canal ADD COLUMN de_token TEXT;
    COMMENT ON COLUMN mtr.mtrtb006_canal.ic_outorga_siric IS 'Atributo utilizado para armazenar um token de longa duração criado para validar o canal com base em alguns serviços especificos que necessitarão ser públicos do ponto de vista do Keycloak (SSO) sendo utilizado este token a ser enviado para captura das informações e controle de permissão';
END IF;

END $$;
-- FIM FLYWAY #93


-- INICIO FLYWAY #94
DO $$
BEGIN

/* TB022 - Produto */
---------------------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb022_produto' AND COLUMN_NAME = 'nu_portal_empreendedor'
) THEN
	ALTER TABLE mtr.mtrtb022_produto ADD COLUMN nu_portal_empreendedor INT4;
	COMMENT ON COLUMN mtr.mtrtb022_produto.nu_portal_empreendedor IS 'Atributo utilizado para identificar que o produto deve ser utilizado quando referenciado por este código de  necessidade apresentado pelo portal do empreendedor';
END IF;

END $$;
-- FIM FLYWAY #94


-- INICIO FLYWAY #95
DO $$
BEGIN
   IF to_regclass('mtr.ix_mtrtb022_02') IS NULL THEN
      CREATE UNIQUE INDEX ix_mtrtb022_02 ON mtr.mtrtb022_produto USING btree (nu_portal_empreendedor);
   END IF;
END $$;
-- FIM FLYWAY #95

COMMIT;
-- FIM DA TRANSACAO