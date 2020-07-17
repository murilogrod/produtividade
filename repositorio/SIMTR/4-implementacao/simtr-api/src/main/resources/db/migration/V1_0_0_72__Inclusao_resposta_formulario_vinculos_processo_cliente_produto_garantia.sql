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