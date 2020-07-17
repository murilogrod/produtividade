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