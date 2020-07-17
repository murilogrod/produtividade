-- Execução dos flyway #63, #65, #66, #68

-- INICIO DA TRANSACAO
BEGIN;

-- INICIO FLYWAY #63
DO $$
BEGIN

/* Tabela 045 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb045_atributo_extracao' AND COLUMN_NAME = 'nu_grupo_atributo'
) THEN 
    ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN nu_grupo_atributo INT4;
    COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.nu_grupo_atributo IS 'Atributo utilizado para indicar o agrupamento que o atributo faz parte. Em determinadas situações é necessário que ao inforar um determinando atributo, obriga-se que outros campos tenham a informação definida, mesmo que estes sejam de carater opicional tendo em vista que este grupo compõe um bloco de informações, Ex: Ao informar uma renda declarada, deve ser informado o tipo de renda, valor e ocupação';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb045_atributo_extracao' AND COLUMN_NAME = 'nu_ordem'
) THEN 
    ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN nu_ordem INT4 NOT NULL DEFAULT 1;
    ALTER TABLE mtr.mtrtb045_atributo_extracao ALTER COLUMN nu_ordem DROP DEFAULT;
    COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.nu_ordem IS 'Atributo utilizado para definir a ordem de exibição dos campos do formulário montado para executar a extação de dados do formulário.';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb045_atributo_extracao' AND COLUMN_NAME = 'de_expressao'
) THEN 
    ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN de_expressao TEXT;
    COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.de_expressao IS 'Atributo que armazena a expressão a ser aplicada pelo Java script para determinar a exposição ou não do campo no formulário montado para extração de dados.';
END IF;

END $$;
-- FIM FLYWAY #63

-- INICIO FLYWAY #65
DO $$
BEGIN

/*====================*/
/* CRIACAO DE TABELAS */
/*====================*/

/* Tabela 041 */
---------------
CREATE TABLE IF NOT EXISTS mtr.mtrtb041_tipo_relacionamento (
   nu_tipo_relacionamento   SERIAL               NOT NULL,
   nu_versao                INT4                 NOT NULL,
   no_tipo_relacionamento   VARCHAR(50)          NOT NULL,
   ic_principal             BOOL                 NOT NULL,
   ic_relacionado           BOOL                 NOT NULL,
   ic_sequencia             BOOL                 NOT NULL,
   ic_tipo_pessoa           VARCHAR(1)           NOT NULL,
   ts_ultima_alteracao		TIMESTAMP			 NOT NULL 	DEFAULT now()
   CONSTRAINT ckc_ic_tipo_pessoa_mtrtb041 CHECK (ic_tipo_pessoa in ('F','J','A') AND ic_tipo_pessoa = UPPER(ic_tipo_pessoa)),
   CONSTRAINT pk_mtrtb041_tipo_relacionament PRIMARY KEY (nu_tipo_relacionamento)
)
TABLESPACE mtrtsdt000;

COMMENT ON TABLE mtr.mtrtb041_tipo_relacionamento IS
'Tabela utilizada para controlar as definições de tipos de relacionamento possiveis na associação entre um dosiê de cliente e um dossiê de produto.';

COMMENT ON COLUMN mtr.mtrtb041_tipo_relacionamento.nu_tipo_relacionamento IS
'Atributo que representa a chave primaria da entidade.';

COMMENT ON COLUMN mtr.mtrtb041_tipo_relacionamento.nu_versao IS
'Campo de controle das versões do registro para viabilizar a concorrencia otimista';

COMMENT ON COLUMN mtr.mtrtb041_tipo_relacionamento.no_tipo_relacionamento IS
'Atributo utilizado para armazenar o nome que descreve o tipo de relacionamento (Ex: Tomador, Titular, Socio PF, etc)';

COMMENT ON COLUMN mtr.mtrtb041_tipo_relacionamento.ic_principal IS
'Atributo que determina se o tipo de relacionamento trata-se de um tipo principal ou não perante um processo. Um tipo de relacionamento principal deverá ser único no dossiê de produto e será utilizado para identificar o dossiê de cliente foco da operação.';

COMMENT ON COLUMN mtr.mtrtb041_tipo_relacionamento.ic_relacionado IS
'Atributo que determina se o tipo de relacionamento trata-se de um tipo que deve ser relacionado a outro dossiê de cliente perante um dossiê de produto.
Um tipo de relacionamento definido como relacionado deverá identificar um outro dossiê de cliente associado a operação criando uma identificação entre as pessoas 
(Ex: CPF1 -> "SOCIO PF" -> CNPJ)';

COMMENT ON COLUMN mtr.mtrtb041_tipo_relacionamento.ic_sequencia IS
'Atributo que determina se o tipo de relacionamento trata-se de um tipo que deve ser indicado numero de sequencia na relação perante um dossiê e produto.
Um tipo de relacionamento com sequencia deverá identificar qual a ordem das associações de dossiês do mesmo tipo de relacionamento entre os dossiês de cliente associados a operação criando uma identificação sequencial entre as pessoas e não deverá ter ausências na lista de sequênciais.
(Ex Valido: 
CPF1 -> "TITULAR" -> 1)
CPF2 -> "TITULAR" -> 2)

(Ex Invalido: 
CPF1 -> "TITULAR" -> 1
CPF2 -> "TITULAR" -> 5
CPF3 -> "TITULAR" -> 6

Aqui faltam os sequenciais 2, 3 e 4)
';

COMMENT ON COLUMN mtr.mtrtb041_tipo_relacionamento.ic_tipo_pessoa IS
'Atributo que determina qual tipo de pessoa pode ter o tipo de relacionamento associado.
Pode assumir os seguintes valores:
F - Física
J - Jurídica
A - Física ou Jurídica
';

COMMENT ON COLUMN mtr.mtrtb041_tipo_relacionamento.ts_ultima_alteracao IS
'Atributo que determina a data da ultima alteração realizada no registro';

CREATE INDEX ix_mtrtb041_01 ON mtr.mtrtb041_tipo_relacionamento (
    no_tipo_relacionamento
)
TABLESPACE mtrtsix000;

/*======================*/
/* ALTERACAO DE TABELAS */
/*======================*/

/* Tabela 004 */
---------------
IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb004_dossie_cliente_produto' and COLUMN_NAME = 'nu_tipo_relacionamento'
) THEN 
    ALTER TABLE mtr.mtrtb004_dossie_cliente_produto ADD COLUMN nu_tipo_relacionamento INT4;
    COMMENT ON COLUMN mtr.mtrtb004_dossie_cliente_produto.nu_tipo_relacionamento IS 'Atributo que representa o tipo de relacionamento do dossiê cliente com o dossiê na operação representada.';
        
    ALTER TABLE mtr.mtrtb004_dossie_cliente_produto
    ADD CONSTRAINT fk_mtrtb004_mtrtb041 FOREIGN KEY (nu_tipo_relacionamento)
    REFERENCES mtr.mtrtb041_tipo_relacionamento (nu_tipo_relacionamento)
    ON DELETE RESTRICT ON UPDATE RESTRICT;
END IF;

--Ajuste dos indices
DROP INDEX IF EXISTS mtr.ix_mtrtb004_01;

CREATE UNIQUE INDEX ix_mtrtb004_01
ON mtr.mtrtb004_dossie_cliente_produto
USING btree (nu_dossie_produto, nu_dossie_cliente, nu_dossie_cliente_relacionado, nu_tipo_relacionamento)
TABLESPACE mtrtsix000
WHERE nu_dossie_cliente_relacionado IS NOT NULL AND nu_sequencia_titularidade IS NULL;

DROP INDEX IF EXISTS mtr.ix_mtrtb004_02;

CREATE UNIQUE INDEX ix_mtrtb004_02
ON mtr.mtrtb004_dossie_cliente_produto
USING btree (nu_dossie_produto, nu_sequencia_titularidade, nu_tipo_relacionamento)
TABLESPACE mtrtsix000
WHERE nu_dossie_cliente_relacionado IS NULL AND nu_sequencia_titularidade IS NOT NULL;

/* Tabela 006 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb006_canal' and COLUMN_NAME = 'ic_outorga_dossie_digital'
) THEN 
    ALTER TABLE mtr.mtrtb006_canal ADD COLUMN ic_outorga_dossie_digital BOOLEAN DEFAULT TRUE;
    COMMENT ON COLUMN mtr.mtrtb006_canal.ic_outorga_dossie_digital IS 'Atributo utilizado para indicar se o canal possui permisão ou não de consumo dos serviços relacionados ao fluxo do dossiê digital expostos na API do SIMTR.';
    ALTER TABLE mtr.mtrtb006_canal ALTER COLUMN ic_outorga_dossie_digital DROP DEFAULT;
END IF;


/* Tabela 020 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb020_processo' and COLUMN_NAME = 'ic_validar_documento'
) THEN 
    ALTER TABLE mtr.mtrtb020_processo ADD COLUMN ic_validar_documento BOOLEAN DEFAULT TRUE;
    COMMENT ON COLUMN mtr.mtrtb020_processo.ic_validar_documento IS 'Atributo que indica se o documento deve ser validado quando vinculado no dossiê.
Caso falso, a instancia do documento deve ser criada e asociada as situações "Criado" e "Conforme" de acordo com a regra de negocio realizada pelo sistema, desde que já exista outra instancia do mesmo documento com situação "Conforme" previamente.
Caso verdadeiro, a instancia do documento deve ser criada com a situação "Criado".';
    ALTER TABLE mtr.mtrtb020_processo ALTER COLUMN ic_validar_documento DROP DEFAULT;
END IF;

/* Tabela 025 */
---------------
IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb025_processo_documento' and COLUMN_NAME = 'nu_tipo_relacionamento'
) THEN 
    ALTER TABLE mtr.mtrtb025_processo_documento ADD COLUMN nu_tipo_relacionamento INT4;
    COMMENT ON COLUMN mtr.mtrtb025_processo_documento.nu_tipo_relacionamento IS 'Atributo que representa o tipo de relacionamento do dossiê cliente com o dossiê produto no processo relacionado para definir a documentação necessária.';
    
    ALTER TABLE mtr.mtrtb025_processo_documento
    ADD CONSTRAINT fk_mtrtb025_mtrtb041 FOREIGN KEY (nu_tipo_relacionamento)
    REFERENCES mtr.mtrtb041_tipo_relacionamento (nu_tipo_relacionamento)
    ON DELETE RESTRICT ON UPDATE RESTRICT;
END IF;

ALTER TABLE mtr.mtrtb025_processo_documento DROP COLUMN IF EXISTS ic_validar;


/* Tabela 032 */
---------------
ALTER TABLE mtr.mtrtb032_elemento_conteudo DROP COLUMN IF EXISTS ic_validar;


/*===============================*/
/* EXECUÇÃO DAS FUNCTIONS DE DML */ 
/*===============================*/
PERFORM mtr.atualiza_tipo_relacionamento();

/*=======================*/
/* ADEQUACAO DAS COLUNAS */ 
/*=======================*/

/* Tabela 004 */
---------------
ALTER TABLE mtr.mtrtb004_dossie_cliente_produto ALTER COLUMN nu_tipo_relacionamento SET NOT NULL;
ALTER TABLE mtr.mtrtb004_dossie_cliente_produto DROP COLUMN IF EXISTS ic_tipo_relacionamento;

ALTER TABLE mtr.mtrtb025_processo_documento ALTER COLUMN nu_tipo_relacionamento SET NOT NULL;
ALTER TABLE mtr.mtrtb025_processo_documento DROP COLUMN IF EXISTS ic_tipo_relacionamento;

ALTER TABLE mtr.mtrtb041_tipo_relacionamento ALTER COLUMN ts_ultima_alteracao DROP DEFAULT;

/*===============================*/
/* EXCLUSAO DAS FUNCTIONS DE DML */ 
/*===============================*/

DROP FUNCTION mtr.atualiza_tipo_relacionamento();

END $$;
-- FIM FLYWAY #65

-- INICIO FLYWAY #66
DO $$
BEGIN

/* Tabela 051 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb051_checklist' AND COLUMN_NAME = 'de_orientacao_operador'
) THEN 
    ALTER TABLE mtr.mtrtb051_checklist ADD COLUMN de_orientacao_operador TEXT;
    COMMENT ON COLUMN mtr.mtrtb051_checklist.de_orientacao_operador IS 'Atributo utilizado para armazenar a orientação a ser apresentado ao operador para indicando uma orientação geral sobre o preenchimento do checklist,';
END IF;

END $$;
-- FIM FLYWAY #66

-- INICIO FLYWAY #68
DO $$
BEGIN

/* Tabela 019 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' and COLUMN_NAME = 'nu_processo_fase'
) THEN 

    --Renomeia o campo atual para representar o processo fase
    ALTER TABLE mtr.mtrtb019_campo_formulario RENAME COLUMN nu_processo TO nu_processo_fase;

    --Cria o campo para representar o processo gerador de dossiê
    ALTER TABLE mtr.mtrtb019_campo_formulario ADD COLUMN nu_processo INT4;
  
    --Aplica os comentarios das colunas referentes aos campos de ligação com a tabela de processo
    COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.nu_processo IS 'Atributo que representa o processo em que deverá ser apresentado o campo do formulário para preenchimento. Este campo tem relação com o processo que define a estrutura do dossiê de produto. A esta informação deverá ser somada ainda uma das opções a seguir:
    - Processo Fase
    - Tipo de Vinculo de Pessoa
    - Produto
    - Garantia

    Só devem ser associado a este atributo, registros de processos que representam um processo que define um dossiê de produto (ic_dossie = true).

    Com base na observação dessas dois atributos, deverá ser possivel montar formulários dinamicos para cada uma das sitações relacionadas';

    COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.nu_processo_fase IS 'Atributo utilizado para identificar a fase do processo que deve ter o campo de formulario associado. Nesta situação, deve ser montado um formulário dinâmico para cada fase definida com o identificador representado por este campo nos dossiês de produto criado com o processo gerador de dossiê relacionado ao registro.

    Só devem ser associado a este atributo, registros de processos que representam um processo que define uma etapa (fase).';

    --Remove as chaves estrangeiras por ventura existentes com a tabela de processos
    ALTER TABLE mtr.mtrtb019_campo_formulario DROP CONSTRAINT IF EXISTS fk_mtrtb019_mtrtb020;
    ALTER TABLE mtr.mtrtb019_campo_formulario DROP CONSTRAINT IF EXISTS fk_mtrtb019_fk_mtrtb0_mtrtb020;

    --Inclui as novas chaves estrangeiras retivas a tabela de processos
    ALTER TABLE mtr.mtrtb019_campo_formulario
    ADD CONSTRAINT fk_mtrtb019_mtrtb020_01 FOREIGN KEY (nu_processo)
    REFERENCES mtr.mtrtb020_processo (nu_processo)
    ON DELETE RESTRICT ON UPDATE RESTRICT;

    ALTER TABLE mtr.mtrtb019_campo_formulario
    ADD CONSTRAINT fk_mtrtb019_mtrtb020_02 FOREIGN KEY (nu_processo_fase)
    REFERENCES mtr.mtrtb020_processo (nu_processo)
    ON DELETE RESTRICT ON UPDATE RESTRICT;

    --Executa ajustes nos dados das tabelas para setar os valores do atributo que representa o processo gerador de dossiê
    PERFORM mtr.ajusta_processo_tabela19();

    --Altera caracteristica do atributo que representa o processo gerador de dossiê para não permitir valores nulos
    ALTER TABLE mtr.mtrtb019_campo_formulario ALTER COLUMN nu_processo SET NOT NULL;
    ALTER TABLE mtr.mtrtb019_campo_formulario ALTER COLUMN nu_processo_fase DROP NOT NULL;
END IF;


IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND COLUMN_NAME = 'nu_tipo_relacionamento'
) THEN 
    --Inclui novo campo utilizado para criar o vinculo com o tipo de relacionamento
    ALTER TABLE mtr.mtrtb019_campo_formulario ADD COLUMN nu_tipo_relacionamento INT4;
    COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.nu_tipo_relacionamento IS 'Atributo utilizado para identificar o tipo de relacionamento vinculado ao processo que deve ter o campo de formulario associado. Nesta situação, deve ser montado um formulário dinâmico para cada vinculo de pessoa definido com o tipo representado por este campo nos dossiês de produto criado com o processo gerador de dossiê relacionado ao registro.';

    --Inclui a chave estrangeira reativa a tabela de tipo de relacionamento
    ALTER TABLE mtr.mtrtb019_campo_formulario
    ADD CONSTRAINT fk_mtrtb019_mtrtb041 FOREIGN KEY (nu_tipo_relacionamento)
    REFERENCES mtr.mtrtb041_tipo_relacionamento (nu_tipo_relacionamento)
    ON DELETE RESTRICT ON UPDATE RESTRICT;
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND COLUMN_NAME = 'nu_produto'
) THEN 
    --Inclui novo campo utilizado para criar o vinculo com o produto
    ALTER TABLE mtr.mtrtb019_campo_formulario ADD COLUMN nu_produto INT4;
    COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.nu_produto IS 'Atributo utilizado para identificar o produto que deve ter o campo de formulario associado. Nesta situação, deve ser montado um formulário dinâmico para cada produto definido com o identificador representado por este campo nos dossiês de produto criado com o processo gerador de dossiê relacionado ao registro.';

    --Inclui a chave estrangeira reativa a tabela de tipo de garantia
    ALTER TABLE mtr.mtrtb019_campo_formulario
    ADD CONSTRAINT fk_mtrtb019_mtrtb022 FOREIGN KEY (nu_produto)
    REFERENCES mtr.mtrtb022_produto (nu_produto)
    ON DELETE RESTRICT ON UPDATE RESTRICT;
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND COLUMN_NAME = 'nu_garantia'
) THEN 
    --Inclui novo campo utilizado para criar o vinculo com a garantia
    ALTER TABLE mtr.mtrtb019_campo_formulario ADD COLUMN nu_garantia INT4;
    COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.nu_garantia IS 'Atributo utilizado para identificar a garantia que deve ter o campo de formulario associado. Nesta situação, deve ser montado um formulário dinâmico para cada garantia definido com o identificador representado por este campo nos dossiês de produto criado com o processo gerador de dossiê relacionado ao registro.';
    
    --Inclui a chave estrangeira reativa a tabela de tipo de garantia
    ALTER TABLE mtr.mtrtb019_campo_formulario
    ADD CONSTRAINT fk_mtrtb019_mtrtb033 FOREIGN KEY (nu_garantia)
    REFERENCES mtr.mtrtb033_garantia (nu_garantia)
    ON DELETE RESTRICT ON UPDATE RESTRICT;
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND COLUMN_NAME = 'nu_identificador_bpm'
) THEN 
    --Inclui novo campo utilizado para criar o vinculo com a garantia
    ALTER TABLE mtr.mtrtb019_campo_formulario ADD COLUMN nu_identificador_bpm INT4;
    COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.nu_identificador_bpm IS 'Atributo utilizado para que a solução de BPM possa capturar o campo de formulário independente do seu identificador serial que pode diferir de acordo com cada ambiente';

    --Criação do indice de unicidade para a identificação do campo junto ao processo de BPM
    DROP INDEX IF EXISTS mtr.ix_mtrtb019_02;
    CREATE INDEX ix_mtrtb019_02 ON mtr.mtrtb019_campo_formulario (
	nu_identificador_bpm
    )
    TABLESPACE mtrtsix000;   
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND COLUMN_NAME = 'no_campo'
) THEN 
    --Remove campo em desuso
    ALTER TABLE mtr.mtrtb019_campo_formulario DROP COLUMN no_campo;
END IF;

/*===============================*/
/* EXCLUSAO DAS FUNCTIONS DE DML */ 
/*===============================*/
DROP FUNCTION IF EXISTS mtr.ajusta_processo_tabela19();

/*========================================*/
/* INCLUSAO DE INDICES DE UNICIDADE TB007 */ 
/*========================================*/
DROP INDEX IF EXISTS mtr.ix_mtrtb007_01;
CREATE INDEX ix_mtrtb007_01 ON mtr.mtrtb007_atributo_documento (
    nu_documento,
    de_atributo
)
TABLESPACE mtrtsix000;

END $$;
--FIM FLYWAY #68

COMMIT;
-- FIM DA TRANSACAO