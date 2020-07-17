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