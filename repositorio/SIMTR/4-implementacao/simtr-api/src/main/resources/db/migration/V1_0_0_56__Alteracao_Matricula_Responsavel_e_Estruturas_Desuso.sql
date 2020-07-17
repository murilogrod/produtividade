/* Tabela 003 */
---------------
ALTER TABLE mtr.mtrtb003_documento RENAME COLUMN co_matricula TO co_responsavel;
ALTER TABLE mtr.mtrtb003_documento ALTER COLUMN co_responsavel TYPE VARCHAR(20);
COMMENT ON COLUMN mtr.mtrtb003_documento.co_responsavel IS 'Atributo utilizado para armazenar a identificação do responsavel (empregado, cliente ou serviço) pela captura do documento';


/* Tabela 013 */
---------------
ALTER TABLE mtr.mtrtb013_situacao_dossie RENAME COLUMN co_matricula TO co_responsavel;
ALTER TABLE mtr.mtrtb013_situacao_dossie ALTER COLUMN co_responsavel TYPE VARCHAR(20);
COMMENT ON COLUMN mtr.mtrtb013_situacao_dossie.co_responsavel IS 'Atributo utilizado para armazenar a matricula do responsavel (empregado, cliente ou serviço) pela atribuição da situação ao dossiê';


/* Tabela 016 */
---------------
DROP TABLE IF EXISTS mtr.mtrtb016_motivo_stco_documento CASCADE;


/* Tabela 017 */
---------------
ALTER TABLE mtr.mtrtb017_stco_instnca_documento DROP COLUMN IF EXISTS nu_motivo_stco_documento;
ALTER TABLE mtr.mtrtb017_stco_instnca_documento RENAME COLUMN co_matricula TO co_responsavel;
ALTER TABLE mtr.mtrtb017_stco_instnca_documento ALTER COLUMN co_responsavel TYPE VARCHAR(20);
COMMENT ON COLUMN mtr.mtrtb017_stco_instnca_documento.co_responsavel IS 'Atributo utilizado para armazenar a matricula do responsavel (empregado, cliente ou serviço) pela atribuição da situação a instância do documento';


/* Tabela 018 */
---------------
COMMENT ON COLUMN mtr.mtrtb018_unidade_tratamento.nu_dossie_produto IS 'Atributo utilizado para vincular o dossiê do produto com a unidade de tratamento';
COMMENT ON COLUMN mtr.mtrtb018_unidade_tratamento.nu_unidade IS 'Atributo que indica o código da unidade que tem permissão de manipular o dossiê de produto naquele momento';


/* Tabela 021 */
---------------
ALTER TABLE mtr.mtrtb021_unidade_autorizada DROP CONSTRAINT IF EXISTS pk_mtrtb021;
ALTER TABLE mtr.mtrtb021_unidade_autorizada DROP COLUMN IF EXISTS ic_tipo_tratamento;
ALTER TABLE mtr.mtrtb021_unidade_autorizada DROP COLUMN IF EXISTS nu_apenso_adm;
ALTER TABLE mtr.mtrtb021_unidade_autorizada DROP COLUMN IF EXISTS nu_contrato_adm;
ALTER TABLE mtr.mtrtb021_unidade_autorizada DROP COLUMN IF EXISTS nu_processo_adm;
ALTER TABLE mtr.mtrtb021_unidade_autorizada DROP COLUMN IF EXISTS nu_versao;
ALTER TABLE mtr.mtrtb021_unidade_autorizada DROP COLUMN IF EXISTS nu_unidade_autorizada;
ALTER TABLE mtr.mtrtb021_unidade_autorizada ADD CONSTRAINT pk_mtrtb021 PRIMARY KEY (nu_processo, nu_unidade) USING INDEX TABLESPACE mtrtsix000;
DROP SEQUENCE IF EXISTS mtr.mtrsq021_unidade_autorizada;
COMMENT ON COLUMN mtr.mtrtb021_unidade_autorizada.nu_processo IS 'Atributo que representa o processo definido a ser utilizado por uma determinada unidade.';
COMMENT ON COLUMN mtr.mtrtb021_unidade_autorizada.nu_unidade IS 'Atributo que representa o codigo da unidade na definição de autorização para utilizar o processo em referência';


/* Tabela 200 */
---------------
DROP TABLE IF EXISTS mtr.mtrtb200_sicli_erro;
