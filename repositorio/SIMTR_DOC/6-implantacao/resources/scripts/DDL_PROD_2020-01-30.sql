-- Execução dos flyway #69, #70, #71, #72, #73, #74, #77, #78, #79, #80, #81, #82 e #83

-- INICIO DA TRANSACAO
BEGIN;

-- INICIO FLYWAY #69
DO $$
BEGIN

/* Tabela 003 */
---------------

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb003_documento' AND COLUMN_NAME = 'co_ged'
) THEN 
    ALTER TABLE mtr.mtrtb003_documento RENAME COLUMN co_ged TO co_siecm_proprio;
    COMMENT ON COLUMN mtr.mtrtb003_documento.co_siecm_proprio IS 'Atributo utilizado para identificar a localização de um documento armazenado no sistema de Gestão Eletronica de Documentos (SIECM:GED) sob o OS_DOSSIEIDIGITAL arquivando o documento recebido e transacionado pelo SIMTR.';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb003_documento' AND COLUMN_NAME = 'co_siecm_tratado'
) THEN 
    --Inclui novo campo utilizado para criar o vinculo com o tipo de relacionamento
    ALTER TABLE mtr.mtrtb003_documento ADD COLUMN co_siecm_tratado VARCHAR(255);
    COMMENT ON COLUMN mtr.mtrtb003_documento.co_siecm_tratado IS 'Atributo utilizado para identificar a localização de um documento armazenado no sistema de Gestão Eletronica de Documentos (SIECM:GED) sob o OS_DOSSIEIDIGITAL arquivando o documento recebido em retorno do fornecedor do serviço de extração de dados com os tratamentos aplicados na imagem.';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb003_documento' AND COLUMN_NAME = 'co_siecm_caixa'
) THEN 
    --Inclui novo campo utilizado para criar o vinculo com o tipo de relacionamento
    ALTER TABLE mtr.mtrtb003_documento ADD COLUMN co_siecm_caixa VARCHAR(255);
    COMMENT ON COLUMN mtr.mtrtb003_documento.co_siecm_caixa IS 'Atributo utilizado para identificar a localização de um documento armazenado no sistema de Gestão Eletronica de Documentos (SIECM:GED) sob o OS_CAIXA de forma a receber um documento de outros sistemas pela referencia de identificação do SIECM para o documento arquivado pelo sistema solicitante.';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb003_documento' AND COLUMN_NAME = 'co_siecm_reuso'
) THEN 
    --Inclui novo campo utilizado para criar o vinculo com o tipo de relacionamento
    ALTER TABLE mtr.mtrtb003_documento ADD COLUMN co_siecm_reuso VARCHAR(255);
    COMMENT ON COLUMN mtr.mtrtb003_documento.co_siecm_reuso IS 'Atributo utilizado para identificar a localização de um documento armazenado no sistema de Gestão Eletronica de Documentos (SIECM:GED) sob o ObjectStore definido pela area de gestão arquivistica de forma a armazenar o realizar a consulta do documento definitivo permitindo que outros possam reutilizar com a indicação de que o documento foi alvo de conformidade/avaliação pela SUBAN através do SIMTR.';
END IF;

/* Tabela 051 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb051_checklist' AND COLUMN_NAME = 'ts_criacao'
) THEN 
    ALTER TABLE mtr.mtrtb051_checklist ADD COLUMN ts_criacao TIMESTAMP NOT NULL DEFAULT NOW();
    COMMENT ON COLUMN mtr.mtrtb051_checklist.ts_criacao IS 'Atributo utilizado para armazenar a data e hora de criação do checklist.';
END IF;

/* Adequação de Sequences */
---------------------------
ALTER SEQUENCE IF EXISTS mtr.mtrtb040_opcao_selecionada_nu_opcao_selecionada_seq RENAME TO mtrsq040_opcao_selecionada;
ALTER SEQUENCE IF EXISTS mtr.mtrtb041_tipo_relacionamento_nu_tipo_relacionamento_seq RENAME TO mtrsq041_tipo_relacionamento;
ALTER SEQUENCE IF EXISTS mtr.mtrtb057_opcao_atributo_nu_opcao_atributo_seq RENAME TO mtrsq057_opcao_atributo;

END $$;
-- FIM FLYWAY #69


-- INICIO FLYWAY #70
DO $$
BEGIN

/* Tabela 001 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_dossie_cliente' AND COLUMN_NAME = 'de_email'
) THEN 
    ALTER TABLE mtr.mtrtb001_dossie_cliente ADD COLUMN de_email VARCHAR(255);
    COMMENT ON COLUMN mtr.mtrtb001_dossie_cliente.de_email IS 'Atributo utilizado para identificar a localização de um documento armazenado no sistema de Gestão Eletronica de Documentos (SIECM:GED) sob o OS_DOSSIEIDIGITAL arquivando o documento recebido e transacionado pelo SIMTR.';
END IF;

/* Tabela 007 */
---------------

ALTER TABLE mtr.mtrtb007_atributo_documento ALTER COLUMN de_conteudo DROP NOT NULL;

/* Tabela 026 */
---------------

DROP INDEX IF EXISTS mtr.ix_mtrtb026_02;
CREATE UNIQUE INDEX ix_mtrtb026_02 ON mtr.mtrtb026_relacao_processo (
    nu_processo_pai,
    nu_processo_filho
)
TABLESPACE mtrtsix000;

END $$;
-- FIM FLYWAY #70


-- INICIO FLYWAY #71
DO $$
BEGIN

/* Tabela 003 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb003_documento' AND COLUMN_NAME = 'no_formato_tratado'
) THEN 
    ALTER TABLE mtr.mtrtb003_documento ADD COLUMN no_formato_tratado VARCHAR(10);
    COMMENT ON COLUMN mtr.mtrtb003_documento.no_formato_tratado IS 'Atributo utilizado para armazenar o formato do documento que foi retornado pelo fornecedor com os tratamentos aplicados. Ex:
- PDF
- JPG
- TIFF';
END IF;

END $$;
-- FIM FLYWAY #71


-- INICIO FLYWAY #72
DO $$
BEGIN

/* Tabela 006 */
---------------
ALTER TABLE mtr.mtrtb006_canal DROP CONSTRAINT IF EXISTS cc_mtrtb006_1;
ALTER TABLE mtr.mtrtb006_canal ADD CONSTRAINT cc_mtrtb006_1 CHECK ((ic_canal_caixa::text = ANY (ARRAY['AGE'::character varying::text, 'AGD'::character varying::text, 'AUT'::character varying::text, 'CCA'::character varying::text])) AND ic_canal_caixa::text = upper(ic_canal_caixa::text));
	
/* Tabela 030 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb030_resposta_dossie' AND COLUMN_NAME = 'nu_processo_fase'
) THEN 
    ALTER TABLE mtr.mtrtb030_resposta_dossie ADD COLUMN nu_processo_fase INT;
    COMMENT ON COLUMN mtr.mtrtb030_resposta_dossie.nu_processo_fase IS 'Atributo utilizado na indicação do processo a fase ao qual a resposta foi concedida';
    
	ALTER TABLE mtr.mtrtb030_resposta_dossie
	ADD CONSTRAINT fk_mtrtb030_mtrtb020 FOREIGN KEY (nu_processo_fase)
	REFERENCES mtr.mtrtb020_processo (nu_processo)
	ON DELETE RESTRICT ON UPDATE RESTRICT;
	
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb030_resposta_dossie' AND COLUMN_NAME = 'nu_dossie_cliente_produto'
) THEN 
    ALTER TABLE mtr.mtrtb030_resposta_dossie ADD COLUMN nu_dossie_cliente_produto BIGINT;
    COMMENT ON COLUMN mtr.mtrtb030_resposta_dossie.nu_dossie_cliente_produto IS 'Atributo utilizado na indicação do vinculo do dossiê de cliente ao qual a resposta foi concedida na operação relacionada.';
    
    ALTER TABLE mtr.mtrtb030_resposta_dossie
	ADD CONSTRAINT fk_mtrtb030_mtrtb004 FOREIGN KEY (nu_dossie_cliente_produto)
	REFERENCES mtr.mtrtb004_dossie_cliente_produto (nu_dossie_cliente_produto)
	ON DELETE RESTRICT ON UPDATE RESTRICT;
	
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb030_resposta_dossie' AND COLUMN_NAME = 'nu_produto_dossie'
) THEN 
    ALTER TABLE mtr.mtrtb030_resposta_dossie ADD COLUMN nu_produto_dossie BIGINT;
    COMMENT ON COLUMN mtr.mtrtb030_resposta_dossie.nu_produto_dossie IS 'Atributo utilizado na indicação do vinculo do produto ao qual a resposta foi concedida na operação relacionada.';
    
    ALTER TABLE mtr.mtrtb030_resposta_dossie
	ADD CONSTRAINT fk_mtrtb030_mtrtb024 FOREIGN KEY (nu_produto_dossie)
	REFERENCES mtr.mtrtb024_produto_dossie (nu_produto_dossie)
	ON DELETE RESTRICT ON UPDATE RESTRICT;
	
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb030_resposta_dossie' AND COLUMN_NAME = 'nu_garantia_informada'
) THEN 
    ALTER TABLE mtr.mtrtb030_resposta_dossie ADD COLUMN nu_garantia_informada BIGINT;
    COMMENT ON COLUMN mtr.mtrtb030_resposta_dossie.nu_garantia_informada IS 'Atributo utilizado na indicação do vinculo da garantia ao qual a resposta foi concedida na operação relacionada.';
    
    ALTER TABLE mtr.mtrtb030_resposta_dossie
	ADD CONSTRAINT fk_mtrtb030_mtrtb035 FOREIGN KEY (nu_garantia_informada)
	REFERENCES mtr.mtrtb035_garantia_informada (nu_garantia_informada)
	ON DELETE RESTRICT ON UPDATE RESTRICT;
	
END IF;

END $$;
-- FIM FLYWAY #72


-- INICIO FLYWAY #73
/* Remover coluna ic_verificacao_previa se existir na tabela mtr.mtrtb009_tipo_documento */

ALTER TABLE mtr.mtrtb009_tipo_documento DROP COLUMN IF EXISTS ic_verificacao_previa
-- FIM FLYWAY #73


-- INICIO FLYWAY #74
ALTER TABLE mtr.mtrtb035_garantia_informada ALTER COLUMN pc_garantia_informada DROP NOT  NULL;
ALTER TABLE mtr.mtrtb035_garantia_informada ALTER COLUMN ic_forma_garantia DROP NOT  NULL;  
-- FIM FLYWAY #74


-- INICIO FLYWAY #75
ALTER TABLE mtr.mtrtb003_documento ADD COLUMN cod_ged character varying(255);
-- FIM FLYWAY #75


-- INICIO FLYWAY #76
ALTER TABLE mtr.mtrtb003_documento RENAME COLUMN cod_ged TO co_ged;
-- FIM FLYWAY #76


-- INICIO FLYWAY #77
DO $$
BEGIN

/* Tabela 006 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb006_canal' AND COLUMN_NAME = 'ic_outorga_apimanager'
) THEN 
    ALTER TABLE mtr.mtrtb006_canal ADD COLUMN ic_outorga_apimanager BOOLEAN NOT NULL DEFAULT FALSE;
    COMMENT ON COLUMN mtr.mtrtb006_canal.ic_outorga_receita IS 'Atributo utilizado para indicar se o canal possui permisão ou não de consumo dos serviços do API Manager expostos diretamente na API do SIMTR em um endpoint de ponte.';
    ALTER TABLE mtr.mtrtb006_canal ALTER COLUMN ic_outorga_apimanager DROP DEFAULT;
END IF;

END $$;
-- FIM FLYWAY #77


-- INICIO FLYWAY #78
DO $$
BEGIN

/* Tabela 019 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND COLUMN_NAME = 'no_campo'
) THEN 
    ALTER TABLE mtr.mtrtb019_campo_formulario ADD COLUMN no_campo character varying(255) DEFAULT NULL;
    COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.no_campo IS 'Atributo utilizado para armazenar o nome do atributo retornado pela rotina/serviço de extração de dados do documento. Esse valor armazena o nome utilizado no campo de retorno da informação';
END IF;

END $$;
-- FIM FLYWAY #78


-- INICIO FLYWAY #79
DO $$ 
BEGIN 
/* Tabela 020 */ 
--------------- 
IF NOT EXISTS( 
SELECT * FROM information_schema.columns 
WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb020_processo' AND COLUMN_NAME = 'ic_tratamento_seletivo' 
) THEN 
ALTER TABLE mtr.mtrtb020_processo ADD COLUMN ic_tratamento_seletivo BOOLEAN NOT NULL DEFAULT FALSE; 
COMMENT ON COLUMN mtr.mtrtb020_processo.ic_tratamento_seletivo IS 'Atributo que indica se o dossiê de produto pode ser capturado para tratamento de forma específica, desde que o mesmo esteja na situação "AGUARDANDO TRATAMENTO" e o solicitante possua o perfil específico para realização de tal ação.'; 
ALTER TABLE mtr.mtrtb020_processo ALTER COLUMN ic_tratamento_seletivo DROP DEFAULT; 
END IF; 
END $$; 
-- FIM FLYWAY #79


-- INICIO FLYWAY #80
DO $$
BEGIN

/* Tabela 006 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb006_canal' AND COLUMN_NAME = 'ic_outorga_siric'
) THEN 
    ALTER TABLE mtr.mtrtb006_canal ADD COLUMN ic_outorga_siric BOOLEAN NOT NULL DEFAULT FALSE;
    COMMENT ON COLUMN mtr.mtrtb006_canal.ic_outorga_receita IS 'Atributo utilizado para indicar se o canal possui permisão ou não de consumo dos serviços do SIRIC expostos diretamente na API do SIMTR.';
    ALTER TABLE mtr.mtrtb006_canal ALTER COLUMN ic_outorga_siric DROP DEFAULT;
END IF;

END $$;
-- FIM FLYWAY #80


-- INICIO FLYWAY #81
DO $$ 
BEGIN 
	
	/* Tabela 006 */ 
	---------------
	COMMENT ON COLUMN mtr.mtrtb006_canal.ic_outorga_apimanager IS 'Atributo utilizado para indicar se o canal possui permisão ou não de consumo dos serviços do API Manager expostos diretamente na API do SIMTR em um endpoint de ponte.';
	COMMENT ON COLUMN mtr.mtrtb006_canal.ic_outorga_receita IS 'Atributo utilizado para indicar se o canal possui permisão ou não de consumo dos serviços relacionados a receita federal expostos diretamente na API do SIMTR.';
	COMMENT ON COLUMN mtr.mtrtb006_canal.ic_outorga_siric IS 'Atributo utilizado para indicar se o canal possui permissão de consumo dos serviços do SIRIC junto ao API Manager expostos diretamente na API do SIMTR.';
	
	/* Tabela 019 */ 
	---------------
	ALTER TABLE mtr.mtrtb019_campo_formulario ALTER COLUMN no_campo TYPE character varying(100);
	ALTER TABLE mtr.mtrtb019_campo_formulario ALTER COLUMN no_campo DROP DEFAULT;
	
	/* Tabela 058 */ 
	--------------- 
	IF NOT EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb058_unidade_padrao' 
	) THEN 

		CREATE TABLE mtr.mtrtb058_unidade_padrao (
		   nu_unidade_padrao    SERIAL            	 NOT NULL,
		   nu_versao            INT4                 NOT NULL,
		   nu_processo          INT4                 NOT NULL,
		   nu_etapa_bpm         INT4                 NOT NULL,
		   nu_unidade           INT4                 NOT NULL,
		   CONSTRAINT pk_mtrtb058_unidade_padrao PRIMARY KEY (nu_unidade_padrao)
		);
		
		COMMENT ON TABLE mtr.mtrtb058_unidade_padrao IS
		'Tabela utilizada para indicar as unidades de tratamento padrão que devem ser atribuidas para o dossê em determinado momento pelo BPM
		Sempre o o BPM tiver que definir as unidades de tratamento, em uma determinada etapa do processo essa tabela será consultada para identificar essa informação.';
		
		COMMENT ON COLUMN mtr.mtrtb058_unidade_padrao.nu_unidade_padrao IS
		'Atributo que representa a chave primaria da entidade.';
		
		COMMENT ON COLUMN mtr.mtrtb058_unidade_padrao.nu_versao IS
		'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
		
		COMMENT ON COLUMN mtr.mtrtb058_unidade_padrao.nu_processo IS
		'Atributo utilizado para referenciar o processo que determinará as unidades padrão a serem atribuidas ao dossiê de produto esteja vinculado.
		A este registro deverá estar vinculado apenas processos do tipo "dossiê"';
		
		COMMENT ON COLUMN mtr.mtrtb058_unidade_padrao.nu_etapa_bpm IS
		'Atributo utilizado para que BPM faça rereferência a etapa no desenho do processo e possa selecionar as unidades que devem ser atribuidas naquele determinado momento.';
		
		COMMENT ON COLUMN mtr.mtrtb058_unidade_padrao.nu_unidade IS
		'Atributo que indica o código da unidade que deverá ser definida como unidade de tratamento junto ao dossiê de produto naquele momento';
		
		/*==============================================================*/
		/* Index: ix_mtrtb058_001                                       */
		/*==============================================================*/
		CREATE UNIQUE INDEX ix_mtrtb058_01 on mtr.mtrtb058_unidade_padrao (
			nu_processo,
			nu_etapa_bpm,
			nu_unidade
		);
		
		ALTER TABLE mtr.mtrtb058_unidade_padrao
		ADD CONSTRAINT fk_mtrtb058_mtrtb020_01 foreign key (nu_processo)
		REFERENCES mtr.mtrtb020_processo (nu_processo)
		ON DELETE RESTRICT ON UPDATE RESTRICT;
 	END IF;
END $$;
-- FIM FLYWAY #81


-- INICIO FLYWAY #82
DO $$
BEGIN

	
/* Tabela 002 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb002_dossie_produto' AND COLUMN_NAME = 'nu_canal'
) THEN 
    ALTER TABLE mtr.mtrtb002_dossie_produto ADD COLUMN nu_canal INT4 NOT NULL DEFAULT 1;
    COMMENT ON COLUMN mtr.mtrtb002_dossie_produto.nu_canal IS 'Atributo utilizado para identificar o canal de de comunicação reesponsável pela criação do dossiê de produto.';
    ALTER TABLE mtr.mtrtb002_dossie_produto ALTER COLUMN nu_canal DROP DEFAULT;
	
    ALTER TABLE mtr.mtrtb002_dossie_produto
    ADD CONSTRAINT fk_mtrtb002_mtrtb006 FOREIGN KEY (nu_canal)
    REFERENCES mtr.mtrtb006_canal (nu_canal)
    ON DELETE RESTRICT ON UPDATE RESTRICT;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb002_dossie_produto' AND COLUMN_NAME = 'ic_canal_caixa'
) THEN 
    ALTER TABLE mtr.mtrtb002_dossie_produto DROP COLUMN ic_canal_caixa;
END IF;
	
/* Tabela 006 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb006_canal' AND COLUMN_NAME = 'de_url_callback_documento'
) THEN 
    ALTER TABLE mtr.mtrtb006_canal ADD COLUMN de_url_callback_documento VARCHAR(255);
    COMMENT ON COLUMN mtr.mtrtb006_canal.de_url_callback_documento IS 'Atributo utilizado para armazenar o endereço de callback utilizado pela API de serviços para notificar o sistema que enviou o documento para o serviço de extração de dados quando ocorrer qualquer retorno por parte do serviço de outsourcing de dados ou da equipe interna CAIXA que tenha realizado a atividade elo fluxo de extração manual.';	
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb006_canal' AND COLUMN_NAME = 'de_url_callback_dossie'
) THEN 
    ALTER TABLE mtr.mtrtb006_canal ADD COLUMN de_url_callback_dossie VARCHAR(255);
    COMMENT ON COLUMN mtr.mtrtb006_canal.de_url_callback_dossie IS 'Atributo utilizado para armazenar o endereço de callback utilizado para notificar o sistema que enviou o dossiê de produto quando este entrar numa situação que dependa de sua atuação.
A identificação da atuação deverá ser identificada pela atribuição da unidade definida no atributo "nu_unidade_callback_dossie" na lista de unidades tratamento (tb018)
Essa ação de inclusão e notificação devem ser realizadas pelo BPM.';	
END IF;
	
IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb006_canal' AND COLUMN_NAME = 'nu_unidade_callback_dossie'
) THEN 
    ALTER TABLE mtr.mtrtb006_canal ADD COLUMN nu_unidade_callback_dossie INT4;
    COMMENT ON COLUMN mtr.mtrtb006_canal.nu_unidade_callback_dossie IS 'Atributo utilizado para armazenar a unidade indicada para realização de callback para notificar o sistema que enviou o dossiê de produto quando este entrar numa situação que dependa de sua atuação.
A identificação da atuação deverá ser identificada pela atribuição do valor aqui definido na lista de unidades tratamento (tb018)
Essa ação de inclusão e notificação devem ser realizadas pelo BPM.';	
END IF;
	
/* Tabela 020 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb020_processo' AND COLUMN_NAME = 'qt_tempo_tratamento'
) THEN 
    ALTER TABLE mtr.mtrtb020_processo ADD COLUMN qt_tempo_tratamento INT4;
    COMMENT ON COLUMN mtr.mtrtb020_processo.qt_tempo_tratamento IS 'Atributo que indica o tempo em minutos que operador terá para realizar o tratamento de um dossiê de produto.
Esse atributo deverá ser informado no processo gerador de dossiê e valerá para qualquer tratamento vinculado a essa operação.
Caso este atributo não esteja definido, o tempo padrão utilizado no serviço será de 10 minutos.

OBS: O atributo não definido para ser utilizado junto ao processo fase, viabilizando a execução de tempos diferentes de acordo com a etapa do processo, definido a funcionalidade de retorno para a fila após a tempo definido a partir da inclusão da situação "EM_TRATAMENTO".
Essa ação será executada sempre que for realizado o consumo de serviço para listagem de processos para tratamento e com uma quantidade de dossiês muito grande essa ação se tornará muito custosa causando problemas de performance no serviço.';
	
END IF;

/* Tabela 041 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb041_tipo_relacionamento' AND COLUMN_NAME = 'ic_receita_pf'
) THEN 
    ALTER TABLE mtr.mtrtb041_tipo_relacionamento ADD COLUMN ic_receita_pf BOOLEAN NOT NULL DEFAULT FALSE;
    COMMENT ON COLUMN mtr.mtrtb041_tipo_relacionamento.ic_receita_pf IS 'Atributo utilizado para indicar que processos vinculados a este tipo de relacionamento, devem ter um registro incluido no dossiê de produto para cada indicação de sócio PF dentre a lista de retornados perante a consulta de integração da Receita Federal.';
    ALTER TABLE mtr.mtrtb041_tipo_relacionamento ALTER COLUMN ic_receita_pf DROP DEFAULT;
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb041_tipo_relacionamento' AND COLUMN_NAME = 'ic_receita_pj'
) THEN 
    ALTER TABLE mtr.mtrtb041_tipo_relacionamento ADD COLUMN ic_receita_pj BOOLEAN NOT NULL DEFAULT FALSE;
    COMMENT ON COLUMN mtr.mtrtb041_tipo_relacionamento.ic_receita_pj IS 'Atributo utilizado para indicar que processos vinculados a este tipo de relacionamento, devem ter um registro incluido no dossiê de produto para cada indicação de sócio PJ dentre a lista de retornados perante a consulta de integração da Receita Federal.';
    ALTER TABLE mtr.mtrtb041_tipo_relacionamento ALTER COLUMN ic_receita_pj DROP DEFAULT;
END IF;

/* AJUSTE DE SEQUENCES */
------------------------
IF EXISTS(
    SELECT * FROM information_schema.sequences
    WHERE SEQUENCE_SCHEMA = 'mtr' AND SEQUENCE_NAME = 'mtrtb058_unidade_padrao_nu_unidade_padrao_seq'
) THEN 

    ALTER SEQUENCE mtr.mtrtb058_unidade_padrao_nu_unidade_padrao_seq RENAME TO mtrsq058_unidade_padrao;
    ALTER TABLE mtr.mtrtb058_unidade_padrao ALTER COLUMN nu_unidade_padrao SET DEFAULT nextval('mtr.mtrsq058_unidade_padrao'::regclass);
END IF;

DROP SEQUENCE IF EXISTS mtr.mtrsq200_sicli_erro;


END $$;
-- FIM FLYWAY #82


-- INICIO FLYWAY #83
DO $$
BEGIN

	
/* Tabela 104 */
---------------

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb104_auditoria_atendimento'
) THEN 
    DROP TABLE mtr.mtrtb104_auditoria_atendimento;
END IF;

END $$;
-- FIM FLYWAY #83

COMMIT;
-- FIM DA TRANSACAO