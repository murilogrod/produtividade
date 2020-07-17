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