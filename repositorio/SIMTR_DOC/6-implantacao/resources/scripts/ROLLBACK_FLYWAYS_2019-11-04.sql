-- INICIO ROLLBACK FLYWAY #68
DO $$
BEGIN
	DROP INDEX IF EXISTS mtr.ix_mtrtb007_01;

	IF NOT EXISTS(
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND COLUMN_NAME = 'no_campo'
	) THEN 
		ALTER TABLE mtr.mtrtb019_campo_formulario ADD COLUMN no_campo character varying(50);
		ALTER TABLE mtr.mtrtb019_campo_formulario ALTER COLUMN no_campo SET NOT NULL;
		COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.no_campo IS 'Atributo que indica o nome do campo. Este nome pode ser utilizado pela estrutura de programação para referenciar a campo no formulario independente do label exposto para o usuário.';
	END IF;

	IF EXISTS(
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND COLUMN_NAME = 'nu_identificador_bpm'
	) THEN 
		DROP INDEX IF EXISTS mtr.ix_mtrtb019_02;
		ALTER TABLE mtr.mtrtb019_campo_formulario DROP COLUMN nu_identificador_bpm;
	END IF;

	IF EXISTS(
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND COLUMN_NAME = 'nu_garantia'
	) THEN 
		ALTER TABLE mtr.mtrtb019_campo_formulario
		DROP CONSTRAINT fk_mtrtb019_mtrtb033;
		ALTER TABLE mtr.mtrtb019_campo_formulario DROP COLUMN nu_garantia;
	END IF;

	IF EXISTS(
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND COLUMN_NAME = 'nu_produto'
	) THEN 
		ALTER TABLE mtr.mtrtb019_campo_formulario
		DROP CONSTRAINT fk_mtrtb019_mtrtb022;
		ALTER TABLE mtr.mtrtb019_campo_formulario DROP COLUMN nu_produto;
	END IF;

	IF EXISTS(
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND COLUMN_NAME = 'nu_tipo_relacionamento'
	) THEN 
		ALTER TABLE mtr.mtrtb019_campo_formulario
		DROP CONSTRAINT fk_mtrtb019_mtrtb041;
		ALTER TABLE mtr.mtrtb019_campo_formulario DROP COLUMN nu_tipo_relacionamento;
	END IF;

	IF EXISTS(
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' and COLUMN_NAME = 'nu_processo_fase'
	) THEN 
		ALTER TABLE mtr.mtrtb019_campo_formulario ALTER COLUMN nu_processo_fase SET NOT NULL;

		ALTER TABLE mtr.mtrtb019_campo_formulario
		DROP CONSTRAINT fk_mtrtb019_mtrtb020_02;

		ALTER TABLE mtr.mtrtb019_campo_formulario
		DROP CONSTRAINT fk_mtrtb019_mtrtb020_01;

		ALTER TABLE mtr.mtrtb019_campo_formulario DROP COLUMN nu_processo;

		ALTER TABLE mtr.mtrtb019_campo_formulario RENAME COLUMN nu_processo_fase TO nu_processo;
		
		ALTER TABLE mtr.mtrtb019_campo_formulario
		  ADD CONSTRAINT fk_mtrtb019_fk_mtrtb0_mtrtb020 FOREIGN KEY (nu_processo)
			  REFERENCES mtr.mtrtb020_processo (nu_processo) MATCH SIMPLE
			  ON UPDATE RESTRICT ON DELETE RESTRICT;
			
	END IF;
END $$;

-- INICIO ROLLBACK FLYWAY #66
DO $$
BEGIN
	IF EXISTS(
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb051_checklist' AND COLUMN_NAME = 'de_orientacao_operador'
	) THEN 
		ALTER TABLE mtr.mtrtb051_checklist DROP COLUMN de_orientacao_operador;
	END IF;
END $$;
-- FIM FLYWAY #66

-- INICIO ROLLBACK FLYWAY #65
DO $$
BEGIN
	ALTER TABLE mtr.mtrtb025_processo_documento ADD COLUMN ic_tipo_relacionamento character varying(50);
	ALTER TABLE mtr.mtrtb025_processo_documento ALTER COLUMN ic_tipo_relacionamento SET NOT NULL;
	COMMENT ON COLUMN mtr.mtrtb025_processo_documento.ic_tipo_relacionamento IS 'Atributo que indica o tipo de relacionamento do cliente com o produto, podendo assumir os valores:
	- TITULAR
	- AVALISTA
	- CONJUGE
	- SOCIO
	etc.';

	ALTER TABLE mtr.mtrtb004_dossie_cliente_produto ADD COLUMN ic_tipo_relacionamento character varying(50);
	ALTER TABLE mtr.mtrtb004_dossie_cliente_produto ALTER COLUMN ic_tipo_relacionamento SET NOT NULL;
	COMMENT ON COLUMN mtr.mtrtb004_dossie_cliente_produto.ic_tipo_relacionamento IS 'Atributo que indica o tipo de relacionamento do cliente com o produto, podendo assumir os valores:
	- TITULAR
	- AVALISTA
	- CONJUGE
	- SOCIO
	etc.';

	ALTER TABLE mtr.mtrtb032_elemento_conteudo ADD COLUMN ic_validar boolean;
	ALTER TABLE mtr.mtrtb032_elemento_conteudo ALTER COLUMN ic_validar SET NOT NULL;
	COMMENT ON COLUMN mtr.mtrtb032_elemento_conteudo.ic_validar IS 'Atributo que indica se o documento deve ser validado quando apresentado no processo.
	Caso verdadeiro, a instância do documento deve ser criada com a situação vazia
	Caso false, a instância do documento deve ser criada com a situação de aprovada conforme regra de negócio realizada pelo sistema, desde que já¡ exista outra instância do mesmo documento com situação aprovada previamente.';

	ALTER TABLE mtr.mtrtb025_processo_documento ADD COLUMN ic_validar boolean;
	ALTER TABLE mtr.mtrtb025_processo_documento ALTER COLUMN ic_validar SET NOT NULL;
	COMMENT ON COLUMN mtr.mtrtb025_processo_documento.ic_validar IS 'Atributo que indica se o documento deve ser validado quando vinculado no dossiê.
	________________________________________________________
	Caso verdadeiro, a instancia do documento deve ser criada com a situação "Criado" -> "Aguardando Avaliação.
	Caso false, a instancia do documento deve ser criada com a situação de aprovada conforme regra de negocio realizada pelo sistema, desde que já¡ exista outra instancia do mesmo documento com situação aprovada previamente.';

	IF EXISTS(
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb025_processo_documento' and COLUMN_NAME = 'nu_tipo_relacionamento'
	) THEN 
		ALTER TABLE mtr.mtrtb025_processo_documento
		DROP CONSTRAINT fk_mtrtb025_mtrtb041;
		ALTER TABLE mtr.mtrtb025_processo_documento DROP COLUMN nu_tipo_relacionamento;
	END IF;

	IF EXISTS(
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb020_processo' and COLUMN_NAME = 'ic_validar_documento'
	) THEN 
		ALTER TABLE mtr.mtrtb020_processo DROP COLUMN ic_validar_documento;
	END IF;

	IF EXISTS(
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb006_canal' and COLUMN_NAME = 'ic_outorga_dossie_digital'
	) THEN 
		ALTER TABLE mtr.mtrtb006_canal DROP COLUMN ic_outorga_dossie_digital;
	END IF;

	DROP INDEX mtr.ix_mtrtb004_02;

	CREATE UNIQUE INDEX ix_mtrtb004_02
	  ON mtr.mtrtb004_dossie_cliente_produto
	  USING btree
	  (nu_dossie_produto, nu_sequencia_titularidade, ic_tipo_relacionamento COLLATE pg_catalog."default")
	TABLESPACE mtrtsix000
	  WHERE nu_dossie_cliente_relacionado IS NULL AND nu_sequencia_titularidade IS NOT NULL;

	DROP INDEX mtr.ix_mtrtb004_01;

	CREATE UNIQUE INDEX ix_mtrtb004_01
	  ON mtr.mtrtb004_dossie_cliente_produto
	  USING btree
	  (nu_dossie_produto, nu_dossie_cliente, nu_dossie_cliente_relacionado, ic_tipo_relacionamento COLLATE pg_catalog."default")
	TABLESPACE mtrtsix000
	  WHERE nu_dossie_cliente_relacionado IS NOT NULL AND nu_sequencia_titularidade IS NULL;

	IF EXISTS(
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb004_dossie_cliente_produto' and COLUMN_NAME = 'nu_tipo_relacionamento'
	) THEN 
		ALTER TABLE mtr.mtrtb004_dossie_cliente_produto
		DROP CONSTRAINT fk_mtrtb004_mtrtb041;
		ALTER TABLE mtr.mtrtb004_dossie_cliente_produto DROP COLUMN nu_tipo_relacionamento;
	END IF;

	DROP TABLE IF EXISTS mtr.mtrtb041_tipo_relacionamento CASCADE;
END $$;
-- FIM FLYWAY #65

-- INICIO ROLLBACK FLYWAY #63
DO $$
BEGIN
	IF EXISTS(
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb045_atributo_extracao' AND COLUMN_NAME = 'de_expressao'
	) THEN 
		ALTER TABLE mtr.mtrtb045_atributo_extracao DROP COLUMN de_expressao;
	END IF;

	IF EXISTS(
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb045_atributo_extracao' AND COLUMN_NAME = 'nu_ordem'
	) THEN 
		ALTER TABLE mtr.mtrtb045_atributo_extracao DROP COLUMN nu_ordem;
	END IF;

	IF EXISTS(
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb045_atributo_extracao' AND COLUMN_NAME = 'nu_grupo_atributo'
	) THEN 
		ALTER TABLE mtr.mtrtb045_atributo_extracao DROP COLUMN nu_grupo_atributo;
	END IF;
END $$;
-- FIM FLYWAY #63
