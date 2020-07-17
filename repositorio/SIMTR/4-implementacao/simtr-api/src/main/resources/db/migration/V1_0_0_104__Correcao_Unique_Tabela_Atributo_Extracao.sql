DO $$
BEGIN
 
DROP INDEX IF EXISTS mtr.ix_mtrtb045_05;
CREATE UNIQUE INDEX ix_mtrtb045_05
  ON mtr.mtrtb045_atributo_extracao
  USING btree
  (nu_tipo_documento, no_atributo_retorno)
TABLESPACE mtrtsix000;

END $$;