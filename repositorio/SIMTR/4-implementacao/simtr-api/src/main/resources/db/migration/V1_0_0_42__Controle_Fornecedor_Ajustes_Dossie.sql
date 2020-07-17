
/* Renomeacao de Tabela */
-------------------------

ALTER TABLE mtr.mtrtb050_auditoria_dossie RENAME CONSTRAINT pk_mtrtb050 TO pk_mtrtb104;
ALTER TABLE mtr.mtrtb050_auditoria_dossie RENAME TO mtrtb104_auditoria_atendimento;
ALTER TABLE mtr.mtrtb104_auditoria_atendimento SET TABLESPACE mtrtsdt000;

ALTER SEQUENCE mtr.mtrsq050_auditoria_dossie RENAME TO mtrsq104_auditoria_atendimento;

/* Exclusao de Tabela */
-------------------------

DROP TABLE IF EXISTS mtr.endereco;

/* Tabela 006 */
---------------
ALTER TABLE mtr.mtrtb006_canal ADD COLUMN ic_atualizacao_documento BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE mtr.mtrtb006_canal ALTER COLUMN ic_atualizacao_documento DROP DEFAULT;

COMMENT ON COLUMN mtr.mtrtb006_canal.ic_atualizacao_documento IS 'Atrbuto utilizado para indicar que o canal pode atualizar as informações de atributos, classificação e avaliação de autenticidade.';

/* Tabela 009 */
---------------

ALTER TABLE mtr.mtrtb009_tipo_documento ADD COLUMN ic_extracao_externa BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE mtr.mtrtb009_tipo_documento ALTER COLUMN ic_extracao_externa DROP DEFAULT;

ALTER TABLE mtr.mtrtb009_tipo_documento ADD COLUMN ic_extracao_m0 BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE mtr.mtrtb009_tipo_documento ALTER COLUMN ic_extracao_m0 DROP DEFAULT;

COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.ic_extracao_externa IS 'Indica se o documento deve ser encaminhado para extração de dados junto a empresa de prestação de serviço externa';
COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.ic_extracao_m0 IS 'Indica se o documento pode ser encaminhado para extração em janela on-line (M0)';

/* Tabela 010 */
---------------

ALTER TABLE mtr.mtrtb010_funcao_documental ADD COLUMN ic_processo_administrativo BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE mtr.mtrtb010_funcao_documental ALTER COLUMN ic_processo_administrativo DROP DEFAULT;

ALTER TABLE mtr.mtrtb010_funcao_documental ADD COLUMN ic_dossie_digital BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE mtr.mtrtb010_funcao_documental ALTER COLUMN ic_dossie_digital DROP DEFAULT;

ALTER TABLE mtr.mtrtb010_funcao_documental ADD COLUMN ic_apoio_negocio BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE mtr.mtrtb010_funcao_documental ALTER COLUMN ic_apoio_negocio DROP DEFAULT;

COMMENT ON COLUMN mtr.mtrtb010_funcao_documental.ic_processo_administrativo IS 'Atributo utilizado para identificar se a função faz utilização perante o Processo Administrativo Eletronico (PAE).';
COMMENT ON COLUMN mtr.mtrtb010_funcao_documental.ic_dossie_digital IS 'Atributo utilizado para identificar se a função documental faz utilização perante o Dossiê Digital.';
COMMENT ON COLUMN mtr.mtrtb010_funcao_documental.ic_apoio_negocio IS 'Atributo utilizado para identificar se a função documental faz utilização perante o Apoio ao Negocio.';

/* Tabela 104 */
---------------
ALTER TABLE mtr.mtrtb104_auditoria_atendimento RENAME nu_identificador  TO nu_auditoria_atendimento;
ALTER TABLE mtr.mtrtb104_auditoria_atendimento RENAME nu_cpf_dossie  TO nu_cpf_cnpj;
ALTER TABLE mtr.mtrtb104_auditoria_atendimento ADD COLUMN ic_tipo_pessoa VARCHAR(1) NOT NULL DEFAULT 'F';
ALTER TABLE mtr.mtrtb104_auditoria_atendimento ALTER COLUMN ic_tipo_pessoa DROP DEFAULT;
ALTER TABLE mtr.mtrtb104_auditoria_atendimento ALTER COLUMN de_tela_acao TYPE VARCHAR(255);
ALTER TABLE mtr.mtrtb104_auditoria_atendimento ALTER COLUMN nu_versao SET NOT NULL;
ALTER TABLE mtr.mtrtb104_auditoria_atendimento RENAME de_matricula_operador TO co_matricula_operador;
ALTER TABLE mtr.mtrtb104_auditoria_atendimento ALTER COLUMN co_matricula_operador TYPE VARCHAR(7);
ALTER TABLE mtr.mtrtb104_auditoria_atendimento RENAME de_unidade_operador TO nu_unidade_operador;
ALTER TABLE mtr.mtrtb104_auditoria_atendimento ADD COLUMN nu_sr_operador integer;
ALTER TABLE mtr.mtrtb104_auditoria_atendimento ADD COLUMN nu_dire_operador integer;

COMMENT ON TABLE mtr.mtrtb104_auditoria_atendimento IS
'Tabela utilizada para realizar o controle de ações relacionadas a operação de atendimento do dossiê digital quando realizadas pela interface propria da solução';

COMMENT ON COLUMN mtr.mtrtb104_auditoria_atendimento.nu_auditoria_atendimento IS
'Atributo que representa a chave primaria da entidade.';

COMMENT ON COLUMN mtr.mtrtb104_auditoria_atendimento.nu_versao IS
'Campo de controle das versões do registro para viabilizar a concorrencia otimista';

COMMENT ON COLUMN mtr.mtrtb104_auditoria_atendimento.nu_cpf_cnpj IS
'Atributo que representa o CPF/CNPJ do cliente vinculado a ação registrada relacionada ao fluxo do dossiê digital.';

COMMENT ON COLUMN mtr.mtrtb104_auditoria_atendimento.ic_tipo_pessoa IS
'Atributo que determina qual tipo de pessoa relacionada ao registro do dossiê.
Pode assumir os seguintes valores:
F - Fisica
J - Juridica';

COMMENT ON COLUMN mtr.mtrtb104_auditoria_atendimento.de_acao IS
'Atributo utilizado para identificar a ação realizada relacionada ao registro de auditoria.
Pode assumir os valores
- CANCELAR
- CRIAR';

COMMENT ON COLUMN mtr.mtrtb104_auditoria_atendimento.de_tela_acao IS
'Atributo utilizado para identificar a tela ativa no atendimento quando houve o registro da ação';

COMMENT ON COLUMN mtr.mtrtb104_auditoria_atendimento.ts_data_acao IS
'Atributo utilizado para armazenar a data/hora de execução da ação.';

COMMENT ON COLUMN mtr.mtrtb104_auditoria_atendimento.co_matricula_operador IS
'Atributo utilizado para armazenar a matricula do operador que estava realizando o atendimento no ato do registro da ação';

COMMENT ON COLUMN mtr.mtrtb104_auditoria_atendimento.nu_unidade_operador IS
'Atributo utilizado para armazenar o CGC da unidade de vinculação do operador que estava realizando o atendimento no ato do registro da ação';

COMMENT ON COLUMN mtr.mtrtb104_auditoria_atendimento.nu_sr_operador IS
'Atributo utilizado para armazenar o CGC da SR de vinculação do operador que estava realizando o atendimento no ato do registro da ação';

COMMENT ON COLUMN mtr.mtrtb104_auditoria_atendimento.nu_dire_operador IS
'Atributo utilizado para armazenar o CGC da diretoria regional de vinculação do operador que estava realizando o atendimento no ato do registro da ação';


/* Tabela 050 */
---------------

CREATE SEQUENCE mtr.MTRSQ050_CONTROLE_DOCUMENTO;

CREATE TABLE mtr.mtrtb050_controle_documento (
   nu_controle_documento            BIGINT               NOT NULL,
   nu_versao                        INTEGER              NOT NULL,
   nu_documento                     BIGINT               NOT NULL,
   ic_classificacao                 BOOLEAN              NOT NULL,
   ic_extracao                      BOOLEAN              NOT NULL,
   ic_avaliacao_cadastral           BOOLEAN              NOT NULL,
   ic_avaliacao_autenticidade       BOOLEAN              NOT NULL,
   ic_janela_extracao               VARCHAR(5)           NULL,
   co_fornecedor                    VARCHAR(100)         NULL,
   ts_envio                         TIMESTAMP            NOT NULL,
   ts_retorno_classificacao         TIMESTAMP            NULL,
   ts_retorno_extracao              TIMESTAMP            NULL,
   ts_retorno_avaliacao_cadastral   TIMESTAMP            NULL,
   ts_retorno_autenticidade         TIMESTAMP            NULL,
   ts_retorno_rejeicao              TIMESTAMP            NULL,
   de_retorno_classificacao         VARCHAR(100)         NULL,
   de_retorno_extracao              TEXT                 NULL,
   de_retorno_avaliacao_cadastral   VARCHAR(255)         NULL,
   ix_retorno_autenticidade         DECIMAL(5,2)         NULL,
   co_rejeicao                      VARCHAR(10)          NULL,
   de_rejeicao                      VARCHAR(255)         NULL,
   CONSTRAINT pk_mtrtb050 PRIMARY KEY (nu_controle_documento)
)
TABLESPACE mtrtsdt000;


COMMENT ON TABLE mtr.mtrtb050_controle_documento IS
'Tabela utilizada para controlar os momentos de envio e retorno do documento aos serviços externos como Extração de Dados e Avaliação de Autenticidade Documental';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.nu_versao IS
'Campo de controle das versões do registro para viabilizar a concorrencia otimista';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.nu_documento IS
'Atributo utilizado para vincular o registro ao documento cujo serviço foi solicitado';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.ic_classificacao IS
'Atributo utilizado para indicar se o serviço de classificação de documento foi solicitado ao fornecedor:';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.ic_extracao IS
'Atributo utilizado para indicar se o serviço de extração de dados do documento foi solicitado ao fornecedor:';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.ic_avaliacao_cadastral IS
'Atributo utilizado para indicar se o serviço de avaliação cadastral baseada nos dados do documento foi solicitado ao fornecedor:';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.ic_avaliacao_autenticidade IS
'Atributo utilizado para indicar se o serviço de avaliação de autenticidade do documento foi solicitado ao fornecedor.';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.ic_janela_extracao IS
'Atributo utilizado para indicar a janela temporal definida para o fornecedor realizar o serviço de extração de forma a validar o SLA da execução.

Pode assumir os seguintes valores:

M0 - Indica solcitação de extração na janela temporal de SLA com até 1 minuto
M30 - Indica solicitação de extração na janela temporal de SLA com 30 minutos
M60 - Indica solicitação de extração na janela temporal de SLA com 60 minutos';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.co_fornecedor IS
'Atributo utilizado para armazenar o codigo de identificação do documento junto fornecedor';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.ts_envio IS
'Atributo utilizado para armazenar a data e hora de envio do documento para execução do serviço pelo fornecedor.';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.ts_retorno_classificacao IS
'Atributo utilizado para armazenar a data e hora de retorno do resultado da execução do serviço de classificação pelo fornecedor.';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.ts_retorno_extracao IS
'Atributo utilizado para armazenar a data e hora de retorno do resultado da execução do serviço de extração de dados pelo fornecedor.';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.ts_retorno_avaliacao_cadastral IS
'Atributo utilizado para armazenar a data e hora de retorno do resultado da execução do serviço de avaliação cadastral pelo fornecedor.';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.ts_retorno_autenticidade IS
'Atributo utilizado para armazenar a data e hora de retorno do resultado da execução do serviço de avaliação de autenticidade documental pelo fornecedor.';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.ts_retorno_rejeicao IS
'Atributo utilizado para armazenar a data e hora de retorno do resultado contemplando a rejeição da execução do serviço pelo fornecedor.';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.de_retorno_classificacao IS
'Atributo utilizado para armazenar o valor retornado pelo fornecedor para o serviço de classificação documental.';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.de_retorno_extracao IS
'Atributo utilizado para armazenar o valor retornado pelo fornecedor para o serviço de extração de dados.';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.de_retorno_avaliacao_cadastral IS
'Atributo utilizado para armazenar o valor retornado pelo fornecedor para o serviço de avaliação cadastral.';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.ix_retorno_autenticidade IS
'Atributo utilizado para armazenar o valor retornado pelo fornecedor para o serviço de avaliação de autenticidade documental.';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.co_rejeicao IS
'Atributo utilizado para armazenar o codigo de rejeição para os casos de impossibilidade de execução do serviço solicitado por problemas identificados no documento encaminhado.';

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.de_rejeicao IS
'Atributo utilizado para armazenar a descrição da causa da rejeição para os casos de impossibilidade de execução do serviço solicitado por problemas identificados no documento encaminhado.';

ALTER TABLE mtr.mtrtb050_controle_documento
ADD CONSTRAINT fk_mtrtb050_mtrtb003 FOREIGN KEY (nu_documento)
REFERENCES mtr.mtrtb003_documento (nu_documento)
ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE mtr.mtrtb050_controle_documento ALTER COLUMN nu_controle_documento SET DEFAULT nextval('mtr.mtrsq050_controle_documento'::regclass);

/* Cria os registros na tabela de controle para permitir a remoção dos atributos da tabela 003 */
------------------------------------------------------------------------------------------------
INSERT INTO mtr.mtrtb050_controle_documento(
    nu_versao, 
    nu_documento, 
    ic_classificacao, 
    ic_extracao, 
    ic_avaliacao_cadastral, 
    ic_avaliacao_autenticidade, 
    ic_janela_extracao, 
    co_fornecedor, 
    ts_envio, 
    ts_retorno_extracao
)
SELECT 
    1 as nu_versao, 
    nu_documento, 
    false as ic_classificacao,
    true as ic_extracao,
    false as ic_avaliacao_cadastral,
    false as ic_avaliacao_autenticidade,
    'M0' as ic_janela_extracao,
    'ECM' || nu_documento as co_fornecedor,
    ts_envio_extracao,
    ts_retorno_extracao
FROM mtr.mtrtb003_documento
WHERE ts_envio_extracao IS NOT NULL OR ts_envio_autenticidade IS NOT NULL;

UPDATE mtr.mtrtb050_controle_documento SET co_fornecedor = 'ECM' || "nu_controle_documento";

/* Tabela 003 */
---------------
ALTER TABLE mtr.mtrtb003_documento DROP COLUMN co_extracao;
ALTER TABLE mtr.mtrtb003_documento DROP COLUMN ts_envio_extracao;
ALTER TABLE mtr.mtrtb003_documento DROP COLUMN ts_retorno_extracao;
ALTER TABLE mtr.mtrtb003_documento DROP COLUMN co_autenticidade;
ALTER TABLE mtr.mtrtb003_documento DROP COLUMN ts_envio_autenticidade;
ALTER TABLE mtr.mtrtb003_documento DROP COLUMN ts_retorno_autenticidade;
ALTER TABLE mtr.mtrtb003_documento DROP COLUMN ic_janela_extracao;

ALTER TABLE mtr.mtrtb003_documento ALTER COLUMN ic_temporario DROP DEFAULT;
ALTER TABLE mtr.mtrtb003_documento ALTER COLUMN ic_origem_documento DROP DEFAULT;

/* Alteração de Indices */
-------------------------
DROP INDEX IF EXISTS mtr.ix_mtrtb004_01;

CREATE UNIQUE INDEX ix_mtrtb004_01
 ON mtr.mtrtb004_dossie_cliente_produto USING BTREE
 ( nu_dossie_produto, nu_dossie_cliente, nu_dossie_cliente_relacionado, ic_tipo_relacionamento )
TABLESPACE mtrtsix000;

DROP INDEX IF EXISTS mtr.ix_mtrtb004_02;

CREATE UNIQUE INDEX IX_MTRTB004_02 
 ON mtr.mtrtb004_dossie_cliente_produto USING BTREE
 ( nu_dossie_produto, nu_sequencia_titularidade, ic_tipo_relacionamento )
TABLESPACE mtrtsix000;

DROP INDEX IF EXISTS mtr.ix_mtrtb009_01;

CREATE UNIQUE INDEX ix_mtrtb009_01
  ON mtr.mtrtb009_tipo_documento
  USING btree
  (no_tipo_documento COLLATE pg_catalog."default", ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio)
TABLESPACE mtrtsix000;

DROP INDEX IF EXISTS mtr.ix_mtrtb010_01;

CREATE UNIQUE INDEX ix_mtrtb010_01
  ON mtr.mtrtb010_funcao_documental
  USING btree
  (no_funcao COLLATE pg_catalog."default", ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio)
TABLESPACE mtrtsix000;

/* Remoção de tabelas */
-----------------------
DROP TABLE IF EXISTS mtr.mtrtb040_cadeia_tipo_sto_dossie;
DROP TABLE IF EXISTS mtr.mtrtb041_cadeia_stco_documento;

/*Correcao de Dados do flyway*/
------------------------------
--UPDATE mtr.schema_version SET script='V1_0_0_39__Delete_Atributo_Extracao.sql' WHERE version = '1.0.0.39';
--UPDATE mtr.schema_version SET script='V1_0_0_40__Adicionando_Validacao_cadastral.sql' WHERE version = '1.0.0.40';