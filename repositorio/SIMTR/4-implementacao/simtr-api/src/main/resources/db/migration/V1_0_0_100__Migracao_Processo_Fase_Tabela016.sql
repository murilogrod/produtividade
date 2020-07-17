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