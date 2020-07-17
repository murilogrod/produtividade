-- Execução dos flyway #101, #102, #103, #104, #1.0.1.00, #1.0.1.01, #1.0.1.02, #1.0.1.03, #105, #106, #107 e #108.

-- INICIO DA TRANSACAO
BEGIN;


-- INICIO FLYWAY #101
DO $$
BEGIN

/* tb003 */
----------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb003_documento' AND COLUMN_NAME = 'ix_pad'
) THEN
	ALTER TABLE mtr.mtrtb003_documento ADD COLUMN ix_pad DECIMAL(5,2);
    COMMENT ON COLUMN mtr.mtrtb003_documento.ix_pad IS 'Atributo utilizado para armazenar o ultimo indice de assertividade do documento apurado na atividade de extração de dados do outsourcing documental.';
END IF;

/* tb050 */
----------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb050_controle_documento' AND COLUMN_NAME = 'ix_pad'
) THEN
	ALTER TABLE mtr.mtrtb050_controle_documento ADD COLUMN ix_pad DECIMAL(5,2);
    COMMENT ON COLUMN mtr.mtrtb050_controle_documento.ix_pad IS 'Atributo utilizado para armazenar o indice de assertividade do documento apurado na atividade de extração de dados do outsourcing documental.';
END IF;

END $$;
-- FIM FLYWAY #101


-- INICIO FLYWAY #102
DO $$
BEGIN

/* tb002 */
----------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb002_dossie_produto' AND COLUMN_NAME = 'ts_falha_bpm'
) THEN
    ALTER TABLE mtr.mtrtb002_dossie_produto ADD COLUMN ts_falha_bpm TIMESTAMP;
    COMMENT ON COLUMN mtr.mtrtb002_dossie_produto.ts_falha_bpm IS 'Atributo utilizado para armazenar a data/hora em que houve falha de comunicação com o jBPM referente a esse dossiê de produto. Sempre que a comunicação ocorrer de forma bem sucedida esse atributo deve ser anulado novamente viabilizando realizar um acompanhamento das operações que estão com algum tipo de pendência nesta comunicação.';
END IF;

/* tb045 */
----------

DROP INDEX IF EXISTS mtr.ix_mtrtb045_01;
CREATE UNIQUE INDEX ix_mtrtb045_01
  ON mtr.mtrtb045_atributo_extracao
  USING btree
  (nu_tipo_documento, no_atributo_negocial)
TABLESPACE mtrtsix000;

DROP INDEX IF EXISTS mtr.ix_mtrtb045_02;
CREATE UNIQUE INDEX ix_mtrtb045_02
  ON mtr.mtrtb045_atributo_extracao
  USING btree
  (nu_tipo_documento, no_atributo_documento)
TABLESPACE mtrtsix000;

DROP INDEX IF EXISTS mtr.ix_mtrtb045_05;
CREATE UNIQUE INDEX ix_mtrtb045_05
  ON mtr.mtrtb045_atributo_extracao
  USING btree
  (nu_tipo_documento, no_atributo_documento)
TABLESPACE mtrtsix000;

END $$;
-- FIM FLYWAY #102


-- INICIO FLYWAY #103
DO $$
BEGIN

/* tb016 */
----------

ALTER TABLE mtr.mtrtb016_processo_fase_dossie ALTER COLUMN nu_processo_fase SET NOT NULL;

END $$;
-- FIM FLYWAY #103


-- INICIO FLYWAY #104
DO $$
BEGIN
 
DROP INDEX IF EXISTS mtr.ix_mtrtb045_05;
CREATE UNIQUE INDEX ix_mtrtb045_05
  ON mtr.mtrtb045_atributo_extracao
  USING btree
  (nu_tipo_documento, no_atributo_retorno)
TABLESPACE mtrtsix000;

END $$;
-- FIM FLYWAY #104


-- INICIO FLYWAY #1.0.1.00
DO $$
BEGIN
/* Tabela 009 */
IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb009_tipo_documento' AND COLUMN_NAME = 'ic_ativo'
) THEN 
    ALTER TABLE mtr.mtrtb009_tipo_documento
    ADD COLUMN ic_ativo boolean;
    COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.ic_ativo IS 'Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema';
END IF;

END $$;
-- FIM FLYWAY #1.0.1.00


-- INICIO FLYWAY #1.0.1.01
DO $$
BEGIN
/* Tabela 009 */
IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb009_tipo_documento' AND COLUMN_NAME = 'ic_ativo'
) THEN 
    UPDATE mtr.mtrtb009_tipo_documento SET ic_ativo=true;
	ALTER TABLE mtr.mtrtb009_tipo_documento
	   ALTER COLUMN ic_ativo SET DEFAULT true;
	ALTER TABLE mtr.mtrtb009_tipo_documento
	   ALTER COLUMN ic_ativo SET NOT NULL;
	COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.ic_ativo IS 'Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema';
END IF;

END $$;
-- FIM FLYWAY #1.0.1.01


-- INICIO FLYWAY #1.0.1.02
DO $$
BEGIN
/* Tabela 009 */
IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb009_tipo_documento' AND COLUMN_NAME = 'ic_ativo'
) THEN 
	ALTER TABLE mtr.mtrtb009_tipo_documento
	   ALTER COLUMN ic_ativo DROP NOT NULL;
	COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.ic_ativo IS 'Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema';
END IF;

END $$;
-- FIM FLYWAY #1.0.1.02


-- INICIO FLYWAY #1.0.1.03
DO $$
BEGIN
/* Tabela 009 */
IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb009_tipo_documento' AND COLUMN_NAME = 'ic_ativo'
) THEN 
ALTER TABLE mtr.mtrtb009_tipo_documento
   ALTER COLUMN ic_ativo SET NOT NULL;
COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.ic_ativo IS 'Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema';
END IF;

END $$;
-- FIM FLYWAY #1.0.1.03


-- INICIO FLYWAY #105
DO $$ 
BEGIN 
	/* Table: mtrtb104_dominio  */ 
	--------------- 
	IF NOT EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb104_dominio' 
	) THEN 

		CREATE TABLE mtr.mtrtb104_dominio (
		   nu_dominio    		INT4            	 NOT NULL,
		   nu_versao            INT4                 NOT NULL,
		   no_dominio           VARCHAR(100)         NOT NULL,
		   ic_tipo              VARCHAR(20)          NOT NULL,
		   ic_multiplos         bool		         NOT NULL,
		   ts_ultima_alteracao  timestamp            NOT NULL,
		   CONSTRAINT pk_mtrtb104 PRIMARY KEY (nu_dominio)
		)
		tablespace mtrtsdt000;
		
		COMMENT ON TABLE mtr.mtrtb104_dominio IS
		'Tabela utilizada para armazenar dados de dominios de dados utilizados em campos de formulário e de extração de dados de documentos';
		
		COMMENT ON COLUMN mtr.mtrtb104_dominio.nu_dominio IS
		'Atributo que representa a chave primaria da entidade.';
		
		COMMENT ON COLUMN mtr.mtrtb104_dominio.nu_versao IS
		'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
		
		COMMENT ON COLUMN mtr.mtrtb104_dominio.no_dominio IS
		'Atributo utilizado para representar o nome do dominio utilizado para agrupar os valores das opções';
		
		COMMENT ON COLUMN mtr.mtrtb104_dominio.ic_tipo IS
		'Atributo utilizado para armazenar o tipo de campo de formulario que será gerado. Exemplos validos para este atributo são:
		- CHECKBOX
		- DATE
		- EMAIL,
		- NUMBER
		- RADIO
		- SELECT
		- TEXT
		- TIME
		- URL
		//*********
		- CPF_CNPJ
		- SELECT_SN';
		
		COMMENT ON COLUMN mtr.mtrtb104_dominio.ic_multiplos IS
		'Atributo utilizado para indicar se o dominio permite seleção multipla caso a estrutura do campo comporte';
		
		COMMENT ON COLUMN mtr.mtrtb104_dominio.ts_ultima_alteracao IS
		'Atributo utilizado para armazenar de data e hora da ultima alterações realizada no registro de forma a viabilizar a realização de cache da informação';

 	END IF;
	
END $$;
-- FIM FLYWAY #105


-- INICIO FLYWAY #106
DO $$ 
BEGIN 
	/* Table: mtrtb105_opcoes_dominio  */ 
	--------------- 
	IF NOT EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb105_opcoes_dominio' 
	) THEN 

		CREATE TABLE mtr.mtrtb105_opcoes_dominio (
		   nu_opcao_dominio     INT4            	 NOT NULL,
		   nu_dominio           INT4                 NULL,
		   nu_versao            INT4                 NOT NULL,
		   no_value             VARCHAR(50)          NOT NULL,
		   no_opcao             VARCHAR(255)         NOT NULL,
		   de_sicli             VARCHAR(255)         NULL,
		   de_siric             VARCHAR(255)         NULL,
		   CONSTRAINT pk_mtrtb105 PRIMARY KEY (nu_opcao_dominio),
		   CONSTRAINT fk_mtrtb105_fk_mtrtb1_mtrtb104 FOREIGN KEY (nu_dominio)
      	   REFERENCES mtr.mtrtb104_dominio (nu_dominio) ON DELETE RESTRICT ON UPDATE RESTRICT
		)
		tablespace mtrtsdt000;
		
		COMMENT ON TABLE mtr.mtrtb105_opcoes_dominio IS
		'Tabela utilizada para armazenar dados de opções de dominios utilizados em campos de formulário e de extração de dados de documentos';
		
		COMMENT ON COLUMN mtr.mtrtb105_opcoes_dominio.nu_opcao_dominio IS
		'Atributo que representa a chave primaria da entidade.';
		
		COMMENT ON COLUMN mtr.mtrtb105_opcoes_dominio.nu_dominio IS
		'Atributo que representa a chave primaria da entidade.';
		
		COMMENT ON COLUMN mtr.mtrtb105_opcoes_dominio.nu_versao IS
		'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
		
		COMMENT ON COLUMN mtr.mtrtb105_opcoes_dominio.no_value IS
		'Atributo utilizado para armazenar o valor que será definido como value da opção na interface grafica.';
		
		COMMENT ON COLUMN mtr.mtrtb105_opcoes_dominio.no_opcao IS
		'Atributo que armazena o valor da opção que será exibida para o usuario no campo do formulario utilizado na interface grafica.';
		
		COMMENT ON COLUMN mtr.mtrtb105_opcoes_dominio.de_sicli IS
		'Atributo utilizado para armazenar o valor que deverá ser utilizado para envio ao SICLI quando a estrutura contiver o valor que representa este registro na seleção';
		
		COMMENT ON COLUMN mtr.mtrtb105_opcoes_dominio.de_siric IS
		'Atributo utilizado para armazenar o valor que deverá ser utilizado para envio ao SIRIC quando a estrutura contiver o valor que representa este registro na seleção';

 	END IF;
	
END $$;
-- FIM FLYWAY #106


-- INICIO FLYWAY #107
DO $$ 
BEGIN
	
	/* CRIAR SEQUENCIA da tabela 104_dominio */ 
	--------------- 
	IF EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb104_dominio' 
	) THEN 

		CREATE SEQUENCE mtr.mtrsq104_dominio;
		ALTER TABLE mtr.mtrtb104_dominio ALTER COLUMN nu_dominio SET DEFAULT nextval('mtr.mtrsq104_dominio'::regclass);
		
 	END IF;
 	
 	
 	/* CRIAR SEQUENCIA da tabela 105_opcoes_dominio  */ 
	--------------- 
	IF EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb105_opcoes_dominio' 
	) THEN 

		CREATE SEQUENCE mtr.mtrsq105_opcoes_dominio;
		ALTER TABLE mtr.mtrtb105_opcoes_dominio ALTER COLUMN nu_opcao_dominio SET DEFAULT nextval('mtr.mtrsq105_opcoes_dominio'::regclass);
		
 	END IF;
	
END $$;
-- FIM FLYWAY #107


-- INICIO FLYWAY #108
DO $$
BEGIN

IF EXISTS(
    SELECT * FROM information_schema.table_constraints
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND CONSTRAINT_NAME = 'ix_mtrtb019_01'
) THEN
	ALTER TABLE mtr.mtrtb019_campo_formulario DROP CONSTRAINT ix_mtrtb019_01;
END IF;

DROP INDEX IF EXISTS mtr.ix_mtrtb019_01;

END $$;
-- FIM FLYWAY #108


COMMIT;
-- FIM DA TRANSACAO