DO $$
BEGIN

/* Tabeela 024*/
---------------
--Remove campo em desuso
IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'vr_contrato'
) THEN 
    ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN vr_contrato;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'pc_juros_operacao'
) THEN 
    ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN pc_juros_operacao;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'ic_periodo_juros'
) THEN 
    ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN ic_periodo_juros;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'pz_operacao'
) THEN 
    ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN pz_operacao;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'pz_carencia'
) THEN 
    ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN pz_carencia;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'ic_liquidacao'
) THEN 
    ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN ic_liquidacao;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'co_contrato_renovado'
) THEN 
    ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN co_contrato_renovado;
END IF;

END $$;
