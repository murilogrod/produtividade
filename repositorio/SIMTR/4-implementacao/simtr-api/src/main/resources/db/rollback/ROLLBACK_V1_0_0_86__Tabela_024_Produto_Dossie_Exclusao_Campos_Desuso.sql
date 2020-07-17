DO $$
BEGIN

/* Tabela 024*/
---------------
IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'vr_contrato'
) THEN 
	ALTER TABLE mtr.mtrtb024_produto_dossie
	  ADD COLUMN vr_contrato numeric(15, 2);
	COMMENT ON COLUMN mtr.mtrtb024_produto_dossie.vr_contrato IS 'Atributo que representa o valor do produto';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'pc_juros_operacao'
) THEN 
	ALTER TABLE mtr.mtrtb024_produto_dossie
	  ADD COLUMN pc_juros_operacao numeric(5, 2);
	COMMENT ON COLUMN mtr.mtrtb024_produto_dossie.pc_juros_operacao IS 'Percentual de juros utilizado na contratação do produto';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'ic_periodo_juros'
) THEN 
	ALTER TABLE mtr.mtrtb024_produto_dossie
	  ADD COLUMN ic_periodo_juros character varying(1);
	COMMENT ON COLUMN mtr.mtrtb024_produto_dossie.ic_periodo_juros IS 'Armazena o periodo de juros ao qual se refere a taxa.';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'pz_operacao'
) THEN 
	ALTER TABLE mtr.mtrtb024_produto_dossie
	  ADD COLUMN pz_operacao integer;
	COMMENT ON COLUMN mtr.mtrtb024_produto_dossie.pz_operacao IS 'Prazo utilizado na contratação do produto';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'pz_carencia'
) THEN 
	ALTER TABLE mtr.mtrtb024_produto_dossie
	  ADD COLUMN pz_carencia integer;
	COMMENT ON COLUMN mtr.mtrtb024_produto_dossie.pz_carencia IS 'Prazo utilizado como carência na contratação do produto';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'ic_liquidacao'
) THEN 
	ALTER TABLE mtr.mtrtb024_produto_dossie
	  ADD COLUMN ic_liquidacao boolean;
	COMMENT ON COLUMN mtr.mtrtb024_produto_dossie.ic_liquidacao IS 'Atributo utilizado para indicar se a contratação do produto prevê a liquidação/renovação de um contrato.';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'co_contrato_renovado'
) THEN 
	ALTER TABLE mtr.mtrtb024_produto_dossie
	  ADD COLUMN co_contrato_renovado character varying(100);
	COMMENT ON COLUMN mtr.mtrtb024_produto_dossie.co_contrato_renovado IS 'Atributo utilizado para armazenar o contrato liquidado/renovado.';
END IF;

END $$;
