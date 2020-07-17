DO $$
BEGIN

/* TB022 - Produto */
---------------------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb022_produto' AND COLUMN_NAME = 'nu_portal_empreendedor'
) THEN
	ALTER TABLE mtr.mtrtb022_produto ADD COLUMN nu_portal_empreendedor INT4;
	COMMENT ON COLUMN mtr.mtrtb022_produto.nu_portal_empreendedor IS 'Atributo utilizado para identificar que o produto deve ser utilizado quando referenciado por este código de  necessidade apresentado pelo portal do empreendedor';
END IF;

END $$;