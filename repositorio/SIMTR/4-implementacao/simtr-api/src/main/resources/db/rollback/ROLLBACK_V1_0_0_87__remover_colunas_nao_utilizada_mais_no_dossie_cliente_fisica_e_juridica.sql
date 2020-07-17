DO $$
BEGIN
	
	
/* Tabela mtrtb001_dossie_cliente */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_dossie_cliente' AND COLUMN_NAME = 'de_telefone'
) THEN
	ALTER TABLE mtr.mtrtb001_dossie_cliente ADD COLUMN de_telefone character varying(15);
	COMMENT ON COLUMN mtr.mtrtb001_dossie_cliente.de_telefone IS 'Atributo que representa o telefone informado para o cliente.';
END IF;

/* Tabela mtrtb001_pessoa_fisica */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_fisica' AND COLUMN_NAME = 'ic_estado_civil'
) THEN
	ALTER TABLE mtr.mtrtb001_pessoa_fisica ADD COLUMN ic_estado_civil integer;
	COMMENT ON COLUMN mtr.mtrtb001_pessoa_fisica.ic_estado_civil IS 'Atributo utilizado para armazenar o estado civil de pessoas físicas. Pose assumir:
			/* Casado */
			C,
			/* Solteiro */
			S,
			/* Divorciado */
			D,
			/* Desquitado */
			Q,
			/* Viúvo */
			V,
			/* Outros */
			O';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_fisica' AND COLUMN_NAME = 'nu_nis'
) THEN
	ALTER TABLE mtr.mtrtb001_pessoa_fisica ADD COLUMN nu_nis character varying(20);
	COMMENT ON COLUMN mtr.mtrtb001_pessoa_fisica.nu_nis IS 'Atributo utilizado para armazenar o número do NIS de pessoas físicas.';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_fisica' AND COLUMN_NAME = 'nu_identidade'
) THEN
	ALTER TABLE mtr.mtrtb001_pessoa_fisica ADD COLUMN nu_identidade character varying(15);
	COMMENT ON COLUMN mtr.mtrtb001_pessoa_fisica.nu_identidade IS 'Atributo utilizado para armazenar o número de identidade de pessoas físicas.';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_fisica' AND COLUMN_NAME = 'no_orgao_emissor'
) THEN
	ALTER TABLE mtr.mtrtb001_pessoa_fisica ADD COLUMN no_orgao_emissor character varying(15);
	COMMENT ON COLUMN mtr.mtrtb001_pessoa_fisica.no_orgao_emissor IS 'Atributo utilizado para armazenar o órgão emissor da identidade de pessoas físicas.';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_fisica' AND COLUMN_NAME = 'no_pai'
) THEN
	ALTER TABLE mtr.mtrtb001_pessoa_fisica ADD COLUMN no_pai character varying(255);
	COMMENT ON COLUMN mtr.mtrtb001_pessoa_fisica.no_pai IS 'Atributo utilizado para armazenar o nome do pai de pessoas físicas.';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_fisica' AND COLUMN_NAME = 'vr_renda_mensal'
) THEN
	ALTER TABLE mtr.mtrtb001_pessoa_fisica ADD COLUMN vr_renda_mensal numeric(15,2);
	COMMENT ON COLUMN mtr.mtrtb001_pessoa_fisica.vr_renda_mensal IS 'Atributo utilizado para armazenar o valor da renda mensal do cliente. Esta informação pode influenciar no fluxo de tratamento de alguns processos.';
END IF;

/* Tabela mtrtb001_pessoa_juridica */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_juridica' AND COLUMN_NAME = 'ic_segmento'
) THEN
	ALTER TABLE mtr.mtrtb001_pessoa_juridica ADD COLUMN ic_segmento character varying(10);
	COMMENT ON COLUMN mtr.mtrtb001_pessoa_juridica.ic_segmento IS 'Atributo para identificar o segmento da empresa, podendo assumir os valores oriundos da view do SIICO:
		- MEI
		- MPE
		- MGE
		- CORP';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_juridica' AND COLUMN_NAME = 'vr_faturamento_anual'
) THEN
	ALTER TABLE mtr.mtrtb001_pessoa_juridica ADD COLUMN vr_faturamento_anual numeric(15,2);
	COMMENT ON COLUMN mtr.mtrtb001_pessoa_juridica.vr_faturamento_anual IS 'Atributo utilizado para armazenar o valor do fatutramento anual da empresa. Esta informação pode influenciar no fluxo de tratamento de alguns processos.';
END IF;

END $$;