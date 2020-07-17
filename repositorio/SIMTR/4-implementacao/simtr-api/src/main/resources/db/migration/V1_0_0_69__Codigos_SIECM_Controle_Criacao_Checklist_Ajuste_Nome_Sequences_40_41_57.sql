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