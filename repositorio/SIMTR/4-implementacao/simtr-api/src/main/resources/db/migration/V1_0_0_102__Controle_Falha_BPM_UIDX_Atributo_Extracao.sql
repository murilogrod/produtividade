DO $$
BEGIN

/* tb002 */
----------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb002_dossie_produto' AND COLUMN_NAME = 'ts_falha_bpm'
) THEN
    ALTER TABLE mtr.mtrtb002_dossie_produto ADD COLUMN ts_falha_bpm TIMESTAMP;
    COMMENT ON COLUMN mtr.mtrtb002_dossie_produto.ts_falha_bpm IS 'Atributo utilizado para armazenar a data/hora em que houve falha de comunicação com o jBPM referente a esse dossiê de produto. Sempre que a comunicação ocorrer de forma bem sucedida esse atributo deve ser anulado novamente viabilizando realizar um acompanhamento das operações que estão com algum tipo de pendência nesta comunicação.';
END IF;

/* tb045 */
----------

DROP INDEX IF EXISTS mtr.ix_mtrtb045_01;
CREATE UNIQUE INDEX ix_mtrtb045_01
  ON mtr.mtrtb045_atributo_extracao
  USING btree
  (nu_tipo_documento, no_atributo_negocial)
TABLESPACE mtrtsix000;

DROP INDEX IF EXISTS mtr.ix_mtrtb045_02;
CREATE UNIQUE INDEX ix_mtrtb045_02
  ON mtr.mtrtb045_atributo_extracao
  USING btree
  (nu_tipo_documento, no_atributo_documento)
TABLESPACE mtrtsix000;

DROP INDEX IF EXISTS mtr.ix_mtrtb045_05;
CREATE UNIQUE INDEX ix_mtrtb045_05
  ON mtr.mtrtb045_atributo_extracao
  USING btree
  (nu_tipo_documento, no_atributo_documento)
TABLESPACE mtrtsix000;

END $$;