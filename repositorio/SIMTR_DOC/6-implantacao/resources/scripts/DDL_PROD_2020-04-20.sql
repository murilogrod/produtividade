-- Execução dos flyway #96, #97, #98, #99 e #100

-- INICIO DA TRANSACAO
BEGIN;

-- INICIO FLYWAY #96
DO $$
BEGIN

/* Tabela 009 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb009_tipo_documento' AND COLUMN_NAME = 'ic_guarda_binario_outsourcing'
) THEN 
    ALTER TABLE mtr.mtrtb009_tipo_documento ADD COLUMN ic_guarda_binario_outsourcing BOOLEAN NOT NULL DEFAULT FALSE;
    COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.ic_guarda_binario_outsourcing IS 'Atributo determina se o binário do documento encaminhado para atendimento ao serviço outsourcing documental deve ser armazenado no SIECM.';
    ALTER TABLE mtr.mtrtb009_tipo_documento ALTER COLUMN ic_guarda_binario_outsourcing DROP DEFAULT;
END IF;
END $$;
-- FIM FLYWAY #96


-- INICIO FLYWAY #97
DO $$
BEGIN
	
/* Tabela mtrtb057_atributo_opcao */
----------------------------------------

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb057_opcao_atributo' AND COLUMN_NAME = 'ic_ativo'
) THEN
	ALTER TABLE mtr.mtrtb057_opcao_atributo DROP COLUMN ic_ativo;
END IF;

-- Impedir que não exista duas opções vinculadas ao mesmo atributo com o mesmo valor de retorno
DROP INDEX IF EXISTS mtr.ix_mtrtb057_01;
CREATE UNIQUE INDEX ix_mtrtb057_01 ON mtr.mtrtb057_opcao_atributo (
    nu_atributo_extracao,
    no_value
)
TABLESPACE mtrtsix000;
END $$;
-- FIM FLYWAY #97


-- INICIO FLYWAY #98
DO $$
BEGIN

/* Tabela 020 */
---------------
IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb020_processo' AND COLUMN_NAME = 'nu_identificador_bpm'
) THEN 
    ALTER TABLE mtr.mtrtb020_processo ADD COLUMN nu_identificador_bpm INT4;
    COMMENT ON COLUMN mtr.mtrtb020_processo.nu_identificador_bpm IS 'Atributo utilizado para que a solução de BPM possa capturar dados do processo independente do seu identificador serial que pode diferir de acordo com cada ambiente. Por exemplo, localizar qual o identificador do processo fase';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb020_processo' AND COLUMN_NAME = 'ic_interface'
) THEN 
    ALTER TABLE mtr.mtrtb020_processo ADD COLUMN ic_interface BOOLEAN NOT NULL DEFAULT FALSE;
    COMMENT ON COLUMN mtr.mtrtb020_processo.ic_interface IS 'Atributo que indica se o processo deve ser apresentado na interface web para criação da operação.';
    ALTER TABLE mtr.mtrtb020_processo ALTER COLUMN ic_interface DROP DEFAULT;
END IF;

/* Tabela 032 */
---------------
IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb032_elemento_conteudo' AND COLUMN_NAME = 'nu_identificador_bpm'
) THEN 
    ALTER TABLE mtr.mtrtb032_elemento_conteudo ADD COLUMN nu_identificador_bpm INT4;
    COMMENT ON COLUMN mtr.mtrtb032_elemento_conteudo.nu_identificador_bpm IS 'Atributo utilizado para que a solução de BPM possa capturar dados do processo independente do seu identificador serial que pode diferir de acordo com cada ambiente. Por exemplo, localizar qual o identificador do processo fase';
END IF;

DROP INDEX IF EXISTS mtr.ix_mtrtb020_02;
CREATE INDEX ix_mtrtb020_02 ON mtr.mtrtb020_processo (
    nu_identificador_bpm
)
TABLESPACE mtrtsix000;

DROP INDEX IF EXISTS mtr.ix_mtrtb032_05;
CREATE INDEX ix_mtrtb032_05 ON mtr.mtrtb032_elemento_conteudo (
    nu_identificador_bpm
)
TABLESPACE mtrtsix000;


END $$;
-- FIM FLYWAY #98


-- INICIO FLYWAY #99
DO $$
BEGIN

DROP INDEX mtr.ix_mtrtb019_02;
CREATE UNIQUE INDEX ix_mtrtb019_02
  ON mtr.mtrtb019_campo_formulario
  USING btree
  (nu_identificador_bpm)
TABLESPACE mtrtsix000;

DROP INDEX IF EXISTS mtr.ix_mtrtb020_02;
CREATE UNIQUE INDEX ix_mtrtb020_02 ON mtr.mtrtb020_processo (
    nu_identificador_bpm
)
TABLESPACE mtrtsix000;

DROP INDEX IF EXISTS mtr.ix_mtrtb020_02;
CREATE UNIQUE INDEX ix_mtrtb020_02 ON mtr.mtrtb020_processo (
    nu_identificador_bpm
)
TABLESPACE mtrtsix000;

DROP INDEX IF EXISTS mtr.ix_mtrtb032_05;
CREATE UNIQUE INDEX ix_mtrtb032_05 ON mtr.mtrtb032_elemento_conteudo (
    nu_identificador_bpm
)
TABLESPACE mtrtsix000;


END $$;
-- FIM FLYWAY #99


-- INICIO FLYWAY #100
DO $$
BEGIN

/* tb002 */
----------

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb002_dossie_produto' AND COLUMN_NAME = 'nu_processo_fase'
) THEN
	ALTER TABLE mtr.mtrtb002_dossie_produto DROP COLUMN nu_processo_fase;
END IF;

/* tb016 */
----------

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb016_subprocesso_bpm'
) THEN
	DROP TABLE mtr.mtrtb016_subprocesso_bpm;
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb016_processo_fase_dossie'
) THEN
CREATE TABLE mtr.mtrtb016_processo_fase_dossie (
   nu_processo_fase_dossie	bigserial       NOT NULL,
   nu_versao            	INT4            NOT NULL,
   nu_dossie_produto    	INT8            NOT NULL,
   nu_processo_fase     	INT4            NULL,
   ts_inclusao          	TIMESTAMP       NOT NULL,
   ts_saida             	TIMESTAMP       NULL,
   co_responsavel       	VARCHAR(20)     NOT NULL,
   nu_unidade           	INT4            NOT NULL,
   CONSTRAINT pk_mtrtb016 PRIMARY KEY (nu_processo_fase_dossie)
);

COMMENT ON TABLE mtr.mtrtb016_processo_fase_dossie IS
'Tabela responsavel por armazenar o historico de processos fases atribuidos ao dossiê do produto. Cada vez que houver uma mudança na etapa do processo, um novo registro deve ser inserido gerando assim um historico das fases vivenciadas durante o seu ciclo de vida.';

COMMENT ON COLUMN mtr.mtrtb016_processo_fase_dossie.nu_processo_fase_dossie IS
'Atributo que representa a chave primaria da entidade.';

COMMENT ON COLUMN mtr.mtrtb016_processo_fase_dossie.nu_versao IS
'Campo de controle das versões do registro para viabilizar a concorrencia otimista';

COMMENT ON COLUMN mtr.mtrtb016_processo_fase_dossie.nu_dossie_produto IS
'Atributo utilizado para armazenar a referência do dossiê de produto vinculado a fase da operação.';

COMMENT ON COLUMN mtr.mtrtb016_processo_fase_dossie.nu_processo_fase IS
'Atributo utilizado para referenciar a etapa de andamento do dossiê.
A este registro deverá estar vinculado apenas processos do tipo "etapa"';

COMMENT ON COLUMN mtr.mtrtb016_processo_fase_dossie.ts_saida IS
'Atributo utilizado para armazenar a data/hora de saída da situação ao dossiê';

COMMENT ON COLUMN mtr.mtrtb016_processo_fase_dossie.co_responsavel IS
'Atributo utilizado para armazenar a matricula do responsavel (empregado, cliente ou serviço) pela inclusão da fase ao dossiê';

COMMENT ON COLUMN mtr.mtrtb016_processo_fase_dossie.nu_unidade IS
'Atributo que indica o código da unidade do empregado que registrou a fase  do dossiê';

ALTER TABLE mtr.mtrtb016_processo_fase_dossie
ADD CONSTRAINT fk_mtrtb016_mtrtb002 FOREIGN KEY (nu_dossie_produto)
REFERENCES mtr.mtrtb002_dossie_produto (nu_dossie_produto)
ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE mtr.mtrtb016_processo_fase_dossie
ADD CONSTRAINT fk_mtrtb016_mtrtb020 FOREIGN KEY (nu_processo_fase)
REFERENCES mtr.mtrtb020_processo (nu_processo)
ON DELETE RESTRICT ON UPDATE RESTRICT;

END IF;


END $$;
-- FIM FLYWAY #100

COMMIT;
-- FIM DA TRANSACAO