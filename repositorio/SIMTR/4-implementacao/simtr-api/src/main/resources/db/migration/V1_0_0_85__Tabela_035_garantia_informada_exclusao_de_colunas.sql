DO $$
BEGIN
	
	
/* Tabela 035 */
---------------

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb035_garantia_informada' AND COLUMN_NAME = 'pc_garantia_informada'
) THEN
	ALTER TABLE mtr.mtrtb035_garantia_informada DROP COLUMN pc_garantia_informada;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb035_garantia_informada' AND COLUMN_NAME = 'ic_forma_garantia'
) THEN
	ALTER TABLE mtr.mtrtb035_garantia_informada DROP COLUMN ic_forma_garantia;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb035_garantia_informada' AND COLUMN_NAME = 'de_garantia'
) THEN
	ALTER TABLE mtr.mtrtb035_garantia_informada DROP COLUMN de_garantia;
END IF;

END $$;