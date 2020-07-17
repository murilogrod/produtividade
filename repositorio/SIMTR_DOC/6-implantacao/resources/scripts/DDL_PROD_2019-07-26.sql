-- Execução dos flyway #55, #56 e #57.


-- INICIO FLYWAY #55
/* INDICES DE UNICIDADE */
-------------------------
--Executa DROP dos indices possivelmente existentes para permitir que o script possa ser reexecutado

/* TB001 - Dossie Cliente */
---------------------------
-- Impedir que sejam incluido dois clientes com o mesmo valor de CPF/CNPJ para o mesmo tipo de pessoa
DROP INDEX IF EXISTS mtr.ix_mtrtb001_01;
CREATE UNIQUE INDEX ix_mtrtb001_01 ON mtr.mtrtb001_dossie_cliente (
    nu_cpf_cnpj,
    ic_tipo_pessoa
)
TABLESPACE mtrtsix000;

/* TB004 - Dossie Cliente Produto*/
----------------------------------
-- Impedir que seja incluido mais de um registro vinculando o dossiê de produto com o mesmo cliente e tipo de relacionamento, associado a um mesmo dossiê cliente relacionado
DROP INDEX IF EXISTS mtr.ix_mtrtb004_01;
CREATE UNIQUE INDEX ix_mtrtb004_01 ON mtr.mtrtb004_dossie_cliente_produto (
    nu_dossie_produto,
    nu_dossie_cliente,
    nu_dossie_cliente_relacionado,
    ic_tipo_relacionamento
)
TABLESPACE mtrtsix000
WHERE nu_dossie_cliente_relacionado IS NOT NULL AND nu_sequencia_titularidade IS NULL;

-- Impedir que seja incluido mais de um registro vinculando o dossiê de produto com o mesmo tipo de relacionamento e mesma sequência de titularidade
DROP INDEX IF EXISTS mtr.ix_mtrtb004_02;
CREATE UNIQUE INDEX ix_mtrtb004_02 ON mtr.mtrtb004_dossie_cliente_produto (
    nu_dossie_produto,
    nu_sequencia_titularidade,
    ic_tipo_relacionamento
)
TABLESPACE mtrtsix000
WHERE nu_dossie_cliente_relacionado IS NULL AND nu_sequencia_titularidade IS NOT NULL;

/* TB009 - Tipo Documento */
---------------------------
-- Inibir a criação de mais de um tipo de documento com mesma tipologia
DROP INDEX IF EXISTS mtr.ix_mtrtb009_01;
CREATE UNIQUE INDEX ix_mtrtb009_01 ON mtr.mtrtb009_tipo_documento (
    co_tipologia
)
TABLESPACE mtrtsix000;

-- Impedir a criação de mais de um tipo de documento com mesmo nome para uso no conceito do apoio ao negocio
DROP INDEX IF EXISTS mtr.ix_mtrtb009_02;
CREATE UNIQUE INDEX ix_mtrtb009_02 ON mtr.mtrtb009_tipo_documento (
    no_tipo_documento,
    ic_apoio_negocio
)
TABLESPACE mtrtsix000
WHERE ic_apoio_negocio = true;

-- Impedir a criação de mais de um tipo de documento com mesmo nome para uso no conceito do dossiê digital
DROP INDEX IF EXISTS mtr.ix_mtrtb009_03;
CREATE UNIQUE INDEX ix_mtrtb009_03 ON mtr.mtrtb009_tipo_documento (
    no_tipo_documento,
    ic_dossie_digital
)
TABLESPACE mtrtsix000
WHERE ic_dossie_digital = true;

-- Impedir a criação de mais de um tipo de documento com mesmo nome para uso no conceito do processo administrativo eletrônico
DROP INDEX IF EXISTS mtr.ix_mtrtb009_04;
CREATE UNIQUE INDEX ix_mtrtb009_04 ON mtr.mtrtb009_tipo_documento (
    no_tipo_documento,
    ic_processo_administrativo
)
TABLESPACE mtrtsix000
WHERE ic_processo_administrativo = true;

/* TB010 - Função Documental */
------------------------------
-- Impedir a criação de mais de uma função documental com mesmo nome para uso no conceito do apoio ao negocio
DROP INDEX IF EXISTS mtr.ix_mtrtb010_01;
CREATE UNIQUE INDEX ix_mtrtb010_01 ON mtr.mtrtb010_funcao_documental (
    no_funcao,
    ic_apoio_negocio
)
TABLESPACE mtrtsix000
WHERE ic_apoio_negocio = true;

-- Impedir a criação de mais de uma função documental com mesmo nome para uso no conceito do dossiê digital
DROP INDEX IF EXISTS mtr.ix_mtrtb010_02;
CREATE UNIQUE INDEX ix_mtrtb010_02 ON mtr.mtrtb010_funcao_documental (
    no_funcao,
    ic_dossie_digital
)
TABLESPACE mtrtsix000
WHERE ic_dossie_digital = true;

-- Impedir a criação de mais de uma função documental com mesmo nome para uso no conceito do processo administrativo eletrônico
DROP INDEX IF EXISTS mtr.ix_mtrtb010_03;
CREATE UNIQUE INDEX ix_mtrtb010_03 ON mtr.mtrtb010_funcao_documental (
    no_funcao,
    ic_processo_administrativo
)
TABLESPACE mtrtsix000
WHERE ic_processo_administrativo = true;

/* TB010 - Função Documental */
------------------------------
-- Impedir que seja definida mais de uma instância do mesmo documento para o mesmo dossiê de produto vinculado ao mesmo dossiê de cliente associado a operação.
DROP INDEX IF EXISTS mtr.ix_mtrtb014_01;
CREATE UNIQUE INDEX ix_mtrtb014_01 ON mtr.mtrtb014_instancia_documento (
    nu_documento,
    nu_dossie_produto,
    nu_dossie_cliente_produto
)
TABLESPACE mtrtsix000
WHERE nu_dossie_cliente_produto IS NOT NULL AND nu_elemento_conteudo IS NULL AND nu_garantia_informada IS NULL;

-- Impedir que seja definida mais de uma instância do mesmo documento para o mesmo dossiê de produto vinculado ao mesmo elemento de conteúdo.
DROP INDEX IF EXISTS mtr.ix_mtrtb014_02;
CREATE UNIQUE INDEX ix_mtrtb014_02 ON mtr.mtrtb014_instancia_documento (
    nu_documento,
    nu_dossie_produto,
    nu_elemento_conteudo
)
TABLESPACE mtrtsix000
WHERE nu_dossie_cliente_produto IS NULL AND nu_elemento_conteudo IS NOT NULL AND nu_garantia_informada IS NULL;

-- Impedir que seja definida mais de uma instância do mesmo documento para o mesmo dossiê de produto vinculado a mesma garantia informada.
DROP INDEX IF EXISTS mtr.ix_mtrtb014_03;
CREATE UNIQUE INDEX ix_mtrtb014_03 ON mtr.mtrtb014_instancia_documento (
    nu_documento,
    nu_dossie_produto,
    nu_garantia_informada
)
TABLESPACE mtrtsix000
WHERE nu_dossie_cliente_produto IS NULL AND nu_elemento_conteudo IS NULL AND nu_garantia_informada IS NOT NULL;


/* TB025 - Processo Documento */
-------------------------------
-- Impedir que seja definida mais de uma vez o mesmo tipo de documento para o mesmo tipo de relacionamento vinculado ao mesmo processo.
DROP INDEX IF EXISTS mtr.ix_mtrtb025_01;
CREATE UNIQUE INDEX ix_mtrtb025_01 ON mtr.mtrtb025_processo_documento(
    nu_processo,
    nu_tipo_documento,
    ic_tipo_relacionamento
)
TABLESPACE mtrtsix000
WHERE nu_tipo_documento IS NOT NULL AND nu_funcao_documental IS NULL;

-- Impedir que seja definida mais de uma vez a mesma função documental para o mesmo tipo de relacionamento vinculado ao mesmo processo.
DROP INDEX IF EXISTS mtr.ix_mtrtb025_02;
CREATE UNIQUE INDEX ix_mtrtb025_02 ON mtr.mtrtb025_processo_documento(
    nu_processo,
    nu_funcao_documental,
    ic_tipo_relacionamento
)
TABLESPACE mtrtsix000
WHERE nu_tipo_documento IS NULL AND nu_funcao_documental IS NOT NULL;

/* TB028 - Opção Campo */
------------------------
-- Impedir a definição de mais de uma opção de resposta ativa com o mesmo valor a ser atribuido no elemento renderizado na página.
DROP INDEX IF EXISTS mtr.ix_mtrtb028_01;
CREATE UNIQUE INDEX ix_mtrtb028_01 ON mtr.mtrtb028_opcao_campo(
    nu_campo_entrada,
    no_value,
    ic_ativo
)
TABLESPACE mtrtsix000
WHERE ic_ativo = true;

/* TB029 - Campo Apresentação */
-------------------------------
-- Impedir a inclusão de mais de um registro para o mesmo campo de formulário associado ao mesmo tipo de dispositivo.
DROP INDEX IF EXISTS mtr.ix_mtrtb029_01;
CREATE UNIQUE INDEX ix_mtrtb029_01 ON mtr.mtrtb029_campo_apresentacao(
    nu_campo_formulario,
    ic_dispositivo
)
TABLESPACE mtrtsix000;

/* TB030 - Resposta Dossiê */
----------------------------
-- Impedir a definição de mais de uma opção de resposta ativa com o mesmo valor a ser atribuido no elemento renderizado na página.
DROP INDEX IF EXISTS mtr.ix_mtrtb030_01;
CREATE UNIQUE INDEX ix_mtrtb030_01 ON mtr.mtrtb030_resposta_dossie(
    nu_dossie_produto,
    nu_campo_formulario,
    de_resposta
)
TABLESPACE mtrtsix000
WHERE de_resposta IS NOT NULL;


/* TB032 - Elemento de Conteudo */
---------------------------------
-- Impedir que seja definido mais de um registro com o mesmo nome de campo associado a um mesmo processo.
DROP INDEX IF EXISTS mtr.ix_mtrtb032_01;
CREATE UNIQUE INDEX ix_mtrtb032_01 ON mtr.mtrtb032_elemento_conteudo(
    nu_processo,
    no_campo
)
TABLESPACE mtrtsix000
WHERE nu_processo IS NOT NULL AND nu_produto IS NULL;

-- Impedir que seja definido mais de um registro com o mesmo nome de campo associado a um mesmo produto.
DROP INDEX IF EXISTS mtr.ix_mtrtb032_02;
CREATE UNIQUE INDEX ix_mtrtb032_02 ON mtr.mtrtb032_elemento_conteudo(
    nu_produto,
    no_campo
)
TABLESPACE mtrtsix000
WHERE nu_processo IS NULL AND nu_produto IS NOT NULL;

-- Impedir que seja definido mais de um registro com o mesmo tipo de documento associado a um mesmo processo.
DROP INDEX IF EXISTS mtr.ix_mtrtb032_03;
CREATE UNIQUE INDEX ix_mtrtb032_03 ON mtr.mtrtb032_elemento_conteudo(
    nu_processo,
    nu_tipo_documento
)
TABLESPACE mtrtsix000
WHERE nu_processo IS NOT NULL AND nu_produto IS NULL;

-- Impedir que seja definido mais de um registro com o mesmo tipo de documento associado a um mesmo produto.
DROP INDEX IF EXISTS mtr.ix_mtrtb032_04;
CREATE UNIQUE INDEX ix_mtrtb032_04 ON mtr.mtrtb032_elemento_conteudo(
    nu_produto,
    nu_tipo_documento
)
TABLESPACE mtrtsix000
WHERE nu_processo IS NULL AND nu_produto IS NOT NULL;


/* TB043 - Documento Garantia */
---------------------------------
-- Impedir que seja definido mais de um registro associando um mesmo tipo de documento com uma garantia para um mesmo processo.
DROP INDEX IF EXISTS mtr.ix_mtrtb043_01;
CREATE UNIQUE INDEX ix_mtrtb043_01 ON mtr.mtrtb043_documento_garantia(
    nu_garantia,
    nu_processo,
    nu_tipo_documento
)
TABLESPACE mtrtsix000
WHERE nu_tipo_documento IS NOT NULL AND nu_funcao_documental IS NULL;

-- Impedir que seja definido mais de um registro associando uma mesma função documental com uma garantia para um mesmo processo.
DROP INDEX IF EXISTS mtr.ix_mtrtb043_02;
CREATE UNIQUE INDEX ix_mtrtb043_02 ON mtr.mtrtb043_documento_garantia(
    nu_garantia,
    nu_processo,
    nu_funcao_documental
)
TABLESPACE mtrtsix000
WHERE nu_tipo_documento IS NOT NULL AND nu_funcao_documental IS NULL;


/* TB048 - Apenso Adminisrativo */
---------------------------------
-- Impedir que seja definido mais de um registro associando um mesmo tipo de documento com uma garantia para um mesmo processo.
DROP INDEX IF EXISTS mtr.ix_mtrtb048_01;
CREATE UNIQUE INDEX ix_mtrtb048_01 ON mtr.mtrtb048_apenso_adm(
    co_protocolo_siclg
)
TABLESPACE mtrtsix000;

DROP INDEX IF EXISTS mtr.ix_mtrtb048_02;
CREATE UNIQUE INDEX ix_mtrtb048_02 ON mtr.mtrtb048_apenso_adm(
    nu_processo_adm,
    no_titulo
)
TABLESPACE mtrtsix000
WHERE nu_processo_adm IS NOT NULL AND nu_contrato_adm IS NULL;

DROP INDEX IF EXISTS mtr.ix_mtrtb048_03;
CREATE UNIQUE INDEX ix_mtrtb048_03 ON mtr.mtrtb048_apenso_adm(
    nu_contrato_adm,
    no_titulo
)
TABLESPACE mtrtsix000
WHERE nu_processo_adm IS NULL AND nu_contrato_adm IS NOT NULL;



/* VALIDAÇÃO DE COLUNAS */
-------------------------
-- TB001
ALTER TABLE mtr.mtrtb001_dossie_cliente DROP CONSTRAINT IF EXISTS cc_mtrtb001_1;
ALTER TABLE mtr.mtrtb001_dossie_cliente ADD CONSTRAINT cc_mtrtb001_1 CHECK (ic_tipo_pessoa IN ('F','J') AND ic_tipo_pessoa = UPPER(ic_tipo_pessoa));

-- TB002
ALTER TABLE mtr.mtrtb002_dossie_produto DROP CONSTRAINT IF EXISTS cc_mtrtb002_1;
ALTER TABLE mtr.mtrtb002_dossie_produto ADD CONSTRAINT cc_mtrtb002_1 CHECK (ic_canal_caixa IN ('AGE','AGD','CCA') AND ic_canal_caixa = UPPER(ic_canal_caixa));

-- TB003
ALTER TABLE mtr.mtrtb003_documento DROP CONSTRAINT IF EXISTS cc_mtrtb003_1;
ALTER TABLE mtr.mtrtb003_documento ADD CONSTRAINT cc_mtrtb003_1 CHECK (ic_origem_documento IN ('O','S','C','A') AND ic_origem_documento = UPPER(ic_origem_documento));

-- TB006
ALTER TABLE mtr.mtrtb006_canal DROP CONSTRAINT IF EXISTS cc_mtrtb006_1;
ALTER TABLE mtr.mtrtb006_canal ADD CONSTRAINT cc_mtrtb006_1 CHECK (ic_canal_caixa IN ('AGE','AGD','CCA') AND ic_canal_caixa = UPPER(ic_canal_caixa));

-- TB009
ALTER TABLE mtr.mtrtb009_tipo_documento DROP CONSTRAINT IF EXISTS cc_mtrtb009_1;
ALTER TABLE mtr.mtrtb009_tipo_documento ADD CONSTRAINT cc_mtrtb009_1 CHECK (ic_tipo_pessoa IN ('F','J','A') AND ic_tipo_pessoa = UPPER(ic_tipo_pessoa));

-- TB020
ALTER TABLE mtr.mtrtb020_processo DROP CONSTRAINT IF EXISTS cc_mtrtb020_1;
ALTER TABLE mtr.mtrtb020_processo ADD CONSTRAINT cc_mtrtb020_1 CHECK (ic_tipo_pessoa IN ('F','J','A') AND ic_tipo_pessoa = UPPER(ic_tipo_pessoa));

-- TB022
ALTER TABLE mtr.mtrtb022_produto DROP CONSTRAINT IF EXISTS cc_mtrtb022_1;
ALTER TABLE mtr.mtrtb022_produto ADD CONSTRAINT cc_mtrtb022_1 CHECK (ic_tipo_pessoa IN ('F','J','A') AND ic_tipo_pessoa = UPPER(ic_tipo_pessoa));

-- TB024
ALTER TABLE mtr.mtrtb024_produto_dossie DROP CONSTRAINT IF EXISTS cc_mtrtb024_1;
ALTER TABLE mtr.mtrtb024_produto_dossie DROP CONSTRAINT IF EXISTS ckc_ic_periodo_juros_mtrtb024;
ALTER TABLE mtr.mtrtb024_produto_dossie ADD CONSTRAINT cc_mtrtb024_1 CHECK (ic_periodo_juros IN ('D','M','A') AND ic_periodo_juros = UPPER(ic_periodo_juros));

-- TB029
ALTER TABLE mtr.mtrtb029_campo_apresentacao DROP CONSTRAINT IF EXISTS cc_mtrtb029_1;
ALTER TABLE mtr.mtrtb029_campo_apresentacao ADD CONSTRAINT cc_mtrtb029_1 CHECK (ic_dispositivo in ('W','L','M','H','X') and ic_dispositivo = upper(ic_dispositivo));

/* AJUSTES DE COMENTARIOS */
---------------------------
COMMENT ON COLUMN mtr.mtrtb001_dossie_cliente.ic_tipo_pessoa IS
'Atributo que determina qual tipo de pessoa relacionada ao registro do dossiê.
Pode assumir os seguintes valores:
F - Fisica
J - Juridica';

COMMENT ON COLUMN mtr.mtrtb004_dossie_cliente_produto.ic_tipo_relacionamento IS
'Atributo que indica o tipo de relacionamento do cliente com o produto, podendo assumir os valores:
- TITULAR
- AVALISTA
- CONJUGE
- SOCIO
etc.';

COMMENT ON COLUMN mtr.mtrtb027_campo_entrada.ic_tipo IS
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
-- FIM FLYWAY #55


-- INICIO FLYWAY #56
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
-- FIM FLYWAY #56


-- INICIO FLYWAY #57
/* Tabela 002 */
---------------
ALTER TABLE mtr.mtrtb002_dossie_produto DROP COLUMN IF EXISTS no_container_bpm;
ALTER TABLE mtr.mtrtb002_dossie_produto ADD COLUMN no_container_bpm VARCHAR(255);
COMMENT ON COLUMN mtr.mtrtb002_dossie_produto.no_container_bpm IS 'Atributo que armazena o valor de identificação do container utilizado no agrupamento dos processos junto a solução de BPM que possui o processo originador vinculado.
Ex: bpm-simtr_1.0.0';

ALTER TABLE mtr.mtrtb002_dossie_produto DROP COLUMN IF EXISTS no_processo_bpm;
ALTER TABLE mtr.mtrtb002_dossie_produto ADD COLUMN no_processo_bpm VARCHAR(255);
COMMENT ON COLUMN mtr.mtrtb002_dossie_produto.no_processo_bpm IS 'Atributo que armazena o valor de identificação do processo orignador junto a solução de BPM.
Ex: bpm-simtr.ProcessoConformidade';

/* Tabela 016 */
---------------
DROP TABLE IF EXISTS mtr.mtrtb016_subprocesso_bpm;

CREATE TABLE mtr.mtrtb016_subprocesso_bpm (
   nu_dossie_produto            int8 NOT NULL,
   nu_instancia_subprocesso_bpm int8 NOT NULL,
   CONSTRAINT pk_mtrtb016_subprocessos_bpm PRIMARY KEY (nu_dossie_produto, nu_instancia_subprocesso_bpm)
);

COMMENT ON TABLE mtr.mtrtb016_subprocesso_bpm IS
'Tabela responsavel por armazenar o identificador dos subprocessos do BPM que possuem relação com o dossiê de produto, mas foram originados a partir da instancia de processo principal ou de algum de seus subprocessos.';

COMMENT ON COLUMN mtr.mtrtb016_subprocesso_bpm.nu_dossie_produto IS
'Atributo utilizado para vincular o dossiê do produto com o identificados do subprocesso existente no BPM utilizado para encaminhar os sinal a todos os vinculados.';

COMMENT ON COLUMN mtr.mtrtb016_subprocesso_bpm.nu_instancia_subprocesso_bpm IS
'Atributo que indica o código da instancia de subprocesso no BPM vinculada a instancia principal relacionada ao dossiê de produto.';

ALTER TABLE mtr.mtrtb016_subprocesso_bpm
ADD CONSTRAINT fk_mtrtb016_mtrtb002 FOREIGN KEY (nu_dossie_produto)
REFERENCES mtr.mtrtb002_dossie_produto (nu_dossie_produto)
ON DELETE RESTRICT ON UPDATE RESTRICT;
-- FIM FLYWAY #57