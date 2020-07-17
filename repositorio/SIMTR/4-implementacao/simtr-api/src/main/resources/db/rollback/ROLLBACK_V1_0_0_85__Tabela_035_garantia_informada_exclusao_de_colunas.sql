DO $$
BEGIN	
	
/* Tabela 035 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb035_garantia_informada' AND COLUMN_NAME = 'pc_garantia_informada'
) THEN
	ALTER TABLE mtr.mtrtb035_garantia_informada ADD COLUMN pc_garantia_informada NUMERIC(8,2);
	COMMENT ON COLUMN mtr.mtrtb035_garantia_informada.pc_garantia_informada IS 'Percentual da garantia informada em relação ao valor pretendido, podendo ser o valor da PMT, da operação, do saldo devedor, etc.';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb035_garantia_informada' AND COLUMN_NAME = 'ic_forma_garantia'
) THEN
	ALTER TABLE mtr.mtrtb035_garantia_informada ADD COLUMN ic_forma_garantia VARCHAR(3);
	COMMENT ON COLUMN mtr.mtrtb035_garantia_informada.ic_forma_garantia IS 'Definição da forma de utilização da garantia, para o campo pc_garantia_informada....';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb035_garantia_informada' AND COLUMN_NAME = 'de_garantia'
) THEN
	ALTER TABLE mtr.mtrtb035_garantia_informada ADD COLUMN de_garantia TEXT;
	COMMENT ON COLUMN mtr.mtrtb035_garantia_informada.de_garantia IS 'Atributo utilizado para armazenar uma descrição livre da garantia pelo usuario submissor. Essa informação será utilizada pelo usuáro avaliador da operação/documentos.';
END IF;

END $$;