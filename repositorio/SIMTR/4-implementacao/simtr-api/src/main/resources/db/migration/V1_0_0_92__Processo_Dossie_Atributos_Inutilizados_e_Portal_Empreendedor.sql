DO $$
BEGIN
	
	
/* TB002 - Produto Dossiê */
---------------------------

IF EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb024_produto_dossie' AND COLUMN_NAME = 'vr_contrato'
) THEN
	ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN vr_contrato;
	ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN pc_juros_operacao;
	ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN ic_periodo_juros;
	ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN pz_operacao;
	ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN pz_carencia;
	ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN ic_liquidacao;
	ALTER TABLE mtr.mtrtb024_produto_dossie DROP COLUMN co_contrato_renovado;
END IF;

/* TB045 - Indices de Unicidade */
---------------------------------
-- Impedir a criação de dois atributos com o valor true para o cálculo de data vinculados ao mesmo tipo de documento.
DROP INDEX IF EXISTS mtr.ix_mtrtb045_03;
DROP INDEX IF EXISTS mtr.ix_mtrtb045_04;

CREATE UNIQUE INDEX ix_mtrtb045_03 ON mtr.mtrtb045_atributo_extracao (
    nu_tipo_documento,
	ic_calculo_data
)
TABLESPACE mtrtsix000
WHERE ic_calculo_data = true;


CREATE UNIQUE INDEX ix_mtrtb045_04 ON mtr.mtrtb045_atributo_extracao (
	nu_tipo_documento,
	ic_identificador_pessoa
)
TABLESPACE mtrtsix000
WHERE ic_identificador_pessoa = true;

END $$;