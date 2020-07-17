DO $$
BEGIN
-- INICIO ROLLBACK FLYWAY #69
ALTER TABLE mtr.mtrtb003_documento DROP COLUMN co_siecm_tratado;

ALTER TABLE mtr.mtrtb003_documento DROP COLUMN co_siecm_caixa;

ALTER TABLE mtr.mtrtb003_documento DROP COLUMN co_siecm_reuso;

ALTER TABLE mtr.mtrtb051_checklist DROP COLUMN ts_criacao;

ALTER SEQUENCE IF EXISTS mtr.mtrsq040_opcao_selecionada RENAME TO mtrtb040_opcao_selecionada_nu_opcao_selecionada_seq;
ALTER SEQUENCE IF EXISTS mtr.mtrsq041_tipo_relacionamento RENAME TO mtrtb041_tipo_relacionamento_nu_tipo_relacionamento_seq;
ALTER SEQUENCE IF EXISTS mtr.mtrsq057_opcao_atributo RENAME TO mtrtb057_opcao_atributo_nu_opcao_atributo_seq;



-- INICIO ROLLBACK FLYWAY #70



/* Tabela 001 */
---------------

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_dossie_cliente' AND COLUMN_NAME = 'de_email'
) THEN 
    ALTER TABLE mtr.mtrtb001_dossie_cliente DROP COLUMN de_email;
END IF;

/* Tabela 007 */
---------------

ALTER TABLE mtr.mtrtb007_atributo_documento ALTER COLUMN de_conteudo SET DEFAULT NULL;


-- FIM ROLLBACK FLYWAY #70

-- INICIO ROLLBACK FLYWAY #71
ALTER TABLE mtr.mtrtb003_documento DROP COLUMN no_formato_tratado;


-- INICIO ROLLBACK FLYWAY #73
ALTER TABLE mtr.mtrtb051_checklist ALTER COLUMN ic_verificacao_previa DROP DEFAULT;
COMMENT ON COLUMN mtr.mtrtb051_checklist.ic_verificacao_previa IS 'Atributo utilizado para indicar que o checklist em questão trata-se de um checklist previo.
No ato do tratamento, esse checklist será apresentado primeiramente, e os demais só serão apresentados caso este checklist seja aprovado na totalidade.
Caso o checklist previo possua algum item não aprovado na verificação, a API poderá receber a execução do tratamento registrando verificação apenas para o checklist previo e desconsiderando a obrigatoriedade das demais verificações.';

-- INICIO ROLLBACK FLYWAY #75 e #76
ALTER TABLE mtr.mtrtb003_documento DROP COLUMN co_ged;


-- INICIO ROLLBACK FLYWAY #77
ALTER TABLE mtr.mtrtb006_canal DROP COLUMN ic_outorga_apimanager;

--INICIO DO ROLLBACK FLYWAY #78
ALTER TABLE mtr.mtrtb019_campo_formulario DROP COLUMN no_campo;

-- INICIO DO ROLLBACK FLYWAY #79
ALTER TABLE mtr.mtrtb020_processo DROP COLUMN ic_tratamento_seletivo;

-- INICIO ROLLBACK FLYWAY #80
ALTER TABLE mtr.mtrtb006_canal DROP COLUMN ic_outorga_siric;


-- INICIO ROLLBACK FLYWAY #81
DROP TABLE IF EXISTS mtr.mtrtb058_unidade_padrao;

	
/* Tabela 002 */
---------------

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb002_dossie_produto' AND COLUMN_NAME = 'nu_canal'
) THEN
    ALTER TABLE mtr.mtrtb002_dossie_produto DROP CONSTRAINT IF EXISTS fk_mtrtb002_mtrtb006;  
    ALTER TABLE mtr.mtrtb002_dossie_produto DROP COLUMN nu_canal;
END IF;


IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb002_dossie_produto' AND COLUMN_NAME = 'ic_canal_caixa'
) THEN 
    ALTER TABLE mtr.mtrtb002_dossie_produto ADD COLUMN ic_canal_caixa VARCHAR(3) NOT NULL DEFAULT 'AGE';
    ALTER TABLE mtr.mtrtb002_dossie_produto ALTER COLUMN ic_canal_caixa DROP DEFAULT;
    ALTER TABLE mtr.mtrtb002_dossie_produto ADD CONSTRAINT cc_mtrtb002_1 CHECK (ic_canal_caixa IN ('AGE','AGD','CCA') AND ic_canal_caixa = UPPER(ic_canal_caixa));
    COMMENT ON COLUMN mtr.mtrtb002_dossie_produto.ic_canal_caixa IS 'Atributo utilizado para indicar o canal CAIXA utilizado para submeter o dossiê conforme o seguinte dominio:';
END IF;
	
/* Tabela 006 */
---------------

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb006_canal' AND COLUMN_NAME = 'de_url_callback_documento'
) THEN 
    ALTER TABLE mtr.mtrtb006_canal DROP COLUMN de_url_callback_documento;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb006_canal' AND COLUMN_NAME = 'de_url_callback_dossie'
) THEN 
    ALTER TABLE mtr.mtrtb006_canal DROP COLUMN de_url_callback_dossie;	
END IF;
	
IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb006_canal' AND COLUMN_NAME = 'nu_unidade_callback_dossie'
) THEN 
    ALTER TABLE mtr.mtrtb006_canal DROP COLUMN nu_unidade_callback_dossie;	
END IF;
	
/* Tabela 020 */
---------------

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb020_processo' AND COLUMN_NAME = 'qt_tempo_tratamento'
) THEN 
    ALTER TABLE mtr.mtrtb020_processo DROP COLUMN qt_tempo_tratamento;
END IF;

/* Tabela 041 */
---------------

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb041_tipo_relacionamento' AND COLUMN_NAME = 'ic_receita_pf'
) THEN 
    ALTER TABLE mtr.mtrtb041_tipo_relacionamento DROP COLUMN ic_receita_pf;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb041_tipo_relacionamento' AND COLUMN_NAME = 'ic_receita_pj'
) THEN 
    ALTER TABLE mtr.mtrtb041_tipo_relacionamento DROP COLUMN ic_receita_pj;
END IF;

/* AJUSTE DE SEQUENCES */
------------------------
IF EXISTS(
    SELECT * FROM information_schema.sequences
    WHERE SEQUENCE_SCHEMA = 'mtr' AND SEQUENCE_NAME = 'mtrsq058_unidade_padrao'
) THEN 
    ALTER SEQUENCE mtr.mtrsq058_unidade_padrao RENAME TO mtrtb058_unidade_padrao_nu_unidade_padrao_seq;
    ALTER TABLE mtr.mtrtb058_unidade_padrao ALTER COLUMN nu_unidade_padrao SET DEFAULT nextval('mtr.mtrsq058_unidade_padrao'::regclass);
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.sequences
    WHERE SEQUENCE_SCHEMA = 'mtr' AND SEQUENCE_NAME = 'mtrsq200_sicli_erro'
)THEN
   CREATE SEQUENCE mtr.mtrsq200_sicli_erro;
END IF;

-- INICIO ROLLBACK FLYWAY #84
ALTER TABLE mtr.mtrtb006_canal ALTER COLUMN co_integracao SET NOT NULL;
COMMENT ON COLUMN mtr.mtrtb006_canal.co_integracao IS 'Atributo que representa o código único para o sistema de origem.';

END $$;


