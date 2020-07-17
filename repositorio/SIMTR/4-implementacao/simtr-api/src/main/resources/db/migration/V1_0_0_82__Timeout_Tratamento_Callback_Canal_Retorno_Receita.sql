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