-- Execução dos flyway #59, #60, #61 e #62

-- INICIO DA TRANSACAO
BEGIN;

-- INICIO FLYWAY #59
DO $$
BEGIN

/* Tabela 001 */
---------------

ALTER TABLE mtr.mtrtb001_dossie_cliente DROP COLUMN IF EXISTS dt_criacao;
ALTER TABLE mtr.mtrtb001_dossie_cliente DROP COLUMN IF EXISTS nu_dire_criacao;
ALTER TABLE mtr.mtrtb001_dossie_cliente DROP COLUMN IF EXISTS nu_sr_criacao;
ALTER TABLE mtr.mtrtb001_dossie_cliente DROP COLUMN IF EXISTS nu_unidade_criacao;

/* Tabela 009 */
---------------
IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE table_schema = 'mtr' and table_name='mtrtb009_tipo_documento' and column_name='no_classe_ged'
) THEN
    ALTER TABLE mtr.mtrtb009_tipo_documento RENAME COLUMN no_classe_ged TO no_classe_siecm;
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE table_schema = 'mtr' and table_name='mtrtb009_tipo_documento' and column_name='ic_validacao_sicod'
) THEN 
    ALTER TABLE mtr.mtrtb009_tipo_documento ADD COLUMN ic_validacao_sicod BOOLEAN NOT NULL DEFAULT false;
END IF;
ALTER TABLE mtr.mtrtb009_tipo_documento ALTER COLUMN ic_validacao_sicod DROP DEFAULT;
COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.ic_validacao_sicod IS 'Indica se o documento pode ser enviado a avaliação de validade documental com o SICOD.';

COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.ic_validacao_documental IS 'Indica se o documento pode ser enviado a avaliação de validade documental externa.';

/* Tabela 045 */
---------------
IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE table_schema = 'mtr' and table_name='mtrtb045_atributo_extracao' and column_name='no_atributo_ged'
) THEN
    ALTER TABLE mtr.mtrtb045_atributo_extracao RENAME COLUMN no_atributo_ged TO no_atributo_siecm;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE table_schema = 'mtr' and table_name='mtrtb045_atributo_extracao' and column_name='ic_tipo_ged'
) THEN
    ALTER TABLE mtr.mtrtb045_atributo_extracao RENAME COLUMN ic_tipo_ged TO ic_tipo_siecm;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE table_schema = 'mtr' and table_name='mtrtb045_atributo_extracao' and column_name='ic_obrigatorio_ged'
) THEN
    ALTER TABLE mtr.mtrtb045_atributo_extracao RENAME COLUMN ic_obrigatorio_ged TO ic_obrigatorio_siecm;
END IF;

ALTER TABLE mtr.mtrtb045_atributo_extracao DROP COLUMN IF EXISTS ic_ged;

ALTER TABLE mtr.mtrtb045_atributo_extracao ALTER COLUMN ic_tipo_geral TYPE VARCHAR(20);
ALTER TABLE mtr.mtrtb045_atributo_extracao ALTER COLUMN ic_tipo_sicli TYPE VARCHAR(20);

COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.no_atributo_siecm IS 'Atributo utilizado para definir o nome do atributo do SIECM que tem relação com o atributo do documento.';
COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.ic_obrigatorio_siecm IS 'Atributo utilizado para indicar se esta informação junto ao SIECM tem cunho obrigatorio.';
COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.ic_tipo_siecm IS 'Atributo utilizado para indicar qual o tipo de atributo definido junto a classe documental no SIECM. Pode assumir os seguintes valores:
- BOOLEAN
- STRING
- LONG
- DATE';

COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.ic_tipo_campo IS 'Atributo utilizado para indicar o tipo de campo que deverá ser utilizado pela interface para os casos de captura da informação quando inserida de forma manual.
Exemplos validos para este atributo são:
- TEXT
- PASSWORD
- DATE
- DATE_TIME
- TIME
- EMAIL
- NUMBER
- CPF
- CNPJ';

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE table_schema = 'mtr' and table_name='mtrtb045_atributo_extracao' and column_name='no_atributo_sicod'
) THEN 
    ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN no_atributo_sicod VARCHAR(100);
END IF;
COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.no_atributo_sicod IS 'Atributo utilizado para definir o nome do atributo do SICOD que tem relação com o atributo do documento.';

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE table_schema = 'mtr' and table_name='mtrtb045_atributo_extracao' and column_name='ic_tipo_sicod'
) THEN 
    ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN ic_tipo_sicod VARCHAR(20);
END IF;
COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.ic_tipo_sicod IS 'Atributo utilizado para indicar qual o tipo de atributo definido junto ao objeto a ser enviado na atualização de dados do SICOD. Pode assumir os seguintes valores:
- BOOLEAN
- STRING
- LONG
- DATE
- DECIMAL';

END $$;
-- FIM FLYWAY #59


-- INICIO FLYWAY #60
DO $$
BEGIN

/* Tabela 020 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE table_schema = 'mtr' and table_name='mtrtb020_processo' and column_name='de_orientacao'
) THEN 
    ALTER TABLE mtr.mtrtb020_processo ADD COLUMN de_orientacao TEXT;
END IF;

COMMENT ON COLUMN mtr.mtrtb020_processo.de_orientacao IS
'Atributo utilizado para representar a orientação a ser exibida ao usuário indicando o que é esperado a ser feito na fase da operação';

END $$;
-- FIM FLYWAY #60


-- INICIO FLYWAY #61
DO $$
BEGIN

/* Tabela 001 */
---------------

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_juridica' and COLUMN_NAME = 'porte'
) THEN 
    ALTER TABLE mtr.mtrtb001_pessoa_juridica DROP COLUMN porte;
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_pessoa_juridica' and COLUMN_NAME = 'ic_porte'
) THEN 
    ALTER TABLE mtr.mtrtb001_pessoa_juridica ADD COLUMN ic_porte VARCHAR(10) NOT NULL DEFAULT 'DEMAIS';
    ALTER TABLE mtr.mtrtb001_pessoa_juridica ALTER COLUMN ic_porte DROP DEFAULT;
    COMMENT ON COLUMN mtr.mtrtb001_pessoa_juridica.ic_porte IS 'Atributo utilizado para identificar o porte da empresa, perante a receita federal. Exemplos
    - ME
    - EPP
    - MEI
    - DMS';
END IF;

END $$;
-- FIM FLYWAY #61


-- INICIO FLYWAY #62
DO $$
BEGIN

/* Tabela 006 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb006_canal' and COLUMN_NAME = 'ic_outorga_receita'
) THEN 
    ALTER TABLE mtr.mtrtb006_canal ADD COLUMN ic_outorga_receita BOOLEAN DEFAULT FALSE;
    COMMENT ON COLUMN mtr.mtrtb006_canal.ic_outorga_receita IS 'Atributo utilizado para indicar se o canal possui permisão ou não de consumo dos serviços relacionados a receita federal expostos diretamente na API do SIMTR.';
    ALTER TABLE mtr.mtrtb006_canal ALTER COLUMN ic_outorga_receita DROP DEFAULT;
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb006_canal' and COLUMN_NAME = 'ic_outorga_cadastro_caixa'
) THEN 
    ALTER TABLE mtr.mtrtb006_canal ADD COLUMN ic_outorga_cadastro_caixa BOOLEAN DEFAULT FALSE;
    COMMENT ON COLUMN mtr.mtrtb006_canal.ic_outorga_cadastro_caixa IS 'Atributo utilizado para indicar se o canal possui permisão ou não de consumo dos serviços relacionados ao cadastro CAIXA expostos diretamente na API do SIMTR.';
    ALTER TABLE mtr.mtrtb006_canal ALTER COLUMN ic_outorga_cadastro_caixa DROP DEFAULT;
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb006_canal' and COLUMN_NAME = 'no_cliente_sso'
) THEN 
    ALTER TABLE mtr.mtrtb006_canal ADD COLUMN no_cliente_sso VARCHAR(20) DEFAULT 'cli-sso-' || round(random() * 1000);
    COMMENT ON COLUMN mtr.mtrtb006_canal.no_cliente_sso IS 'Atributo utilizado para armazenar o nome do cliente SSO utilizado pelo canal. Este atributo será utilizado para identificar o solicitante através da informação oriunda do token do SSO.';
    ALTER TABLE mtr.mtrtb006_canal ALTER COLUMN no_cliente_sso DROP DEFAULT;
END IF;

CREATE UNIQUE INDEX ix_mtrtb006_02
ON mtr.mtrtb006_canal
USING btree
(no_cliente_sso)
TABLESPACE mtrtsix000;

/* Tabela 007 */
---------------

ALTER TABLE mtr.mtrtb007_atributo_documento DROP COLUMN IF EXISTS de_opcoes_selecionadas;

/* Tabela 040 */
---------------
DROP TABLE IF EXISTS mtr.mtrtb040_opcao_selecionada;

CREATE TABLE mtr.mtrtb040_opcao_selecionada (
    nu_opcao_selecionada    BIGSERIAL       NOT NULL,
    nu_versao               INT4            NOT NULL,
    nu_atributo_documento   INT8            NOT NULL,
    no_value                 VARCHAR(50)    NOT NULL,
    no_opcao                 VARCHAR(255)   NOT NULL,
    CONSTRAINT pk_mtrtb040 PRIMARY KEY (nu_opcao_selecionada)
)
TABLESPACE mtrtsdt000;

COMMENT ON TABLE mtr.mtrtb040_opcao_selecionada IS
'Tabela responsavel por armazenar as opções selecionadas para a situação em que o valor do atributo capturado do documento seja referente a um valor pré definido utilizado principalmente em documentos gerados internamente ou documento de dados declarados.';

COMMENT ON COLUMN mtr.mtrtb040_opcao_selecionada.nu_opcao_selecionada IS
'Atributo que representa a chave primaria da entidade.';

COMMENT ON COLUMN mtr.mtrtb040_opcao_selecionada.nu_versao IS
'Campo de controle das versões do registro para viabilizar a concorrencia otimista';

COMMENT ON COLUMN mtr.mtrtb040_opcao_selecionada.nu_atributo_documento IS
'Atributo utilizado para vincular a opção selecionada ao atributo do documento cuja informação foi informada.';

COMMENT ON COLUMN mtr.mtrtb040_opcao_selecionada.no_value IS
'Atributo utilizado para armazenar o valor selecionado do atributo objetivo que estava definido no documento no ato da captura.';

COMMENT ON COLUMN mtr.mtrtb040_opcao_selecionada.no_opcao IS
'Atributo utilizado para armazenar a descrição da opção relacionada com a opção selecionada do atributo objetivo que estava definido no documento no ato da captura.';

ALTER TABLE mtr.mtrtb040_opcao_selecionada
ADD CONSTRAINT fk_mtrtb040_mtrtb007 FOREIGN KEY (nu_atributo_documento)
REFERENCES mtr.mtrtb007_atributo_documento (nu_atributo_documento)
ON DELETE RESTRICT ON UPDATE RESTRICT;


CREATE UNIQUE INDEX ix_mtrtb040_01
ON mtr.mtrtb040_opcao_selecionada
USING btree
(nu_atributo_documento, no_value)
TABLESPACE mtrtsix000;

END $$;
-- FIM FLYWAY #62

COMMIT; 
--FIM DA TRANSACAO