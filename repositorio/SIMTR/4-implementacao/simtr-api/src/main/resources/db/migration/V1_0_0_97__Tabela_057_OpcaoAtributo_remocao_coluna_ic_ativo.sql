DO $$
BEGIN
	
/* Tabela mtrtb057_atributo_opcao */
----------------------------------------

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb057_opcao_atributo' AND COLUMN_NAME = 'ic_ativo'
) THEN
	ALTER TABLE mtr.mtrtb057_opcao_atributo DROP COLUMN ic_ativo;
END IF;

-- Impedir que não exista duas opções vinculadas ao mesmo atributo com o mesmo valor de retorno
DROP INDEX IF EXISTS mtr.ix_mtrtb057_01;
CREATE UNIQUE INDEX ix_mtrtb057_01 ON mtr.mtrtb057_opcao_atributo (
    nu_atributo_extracao,
    no_value
)
TABLESPACE mtrtsix000;
END $$;