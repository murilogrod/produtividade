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