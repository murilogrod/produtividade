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