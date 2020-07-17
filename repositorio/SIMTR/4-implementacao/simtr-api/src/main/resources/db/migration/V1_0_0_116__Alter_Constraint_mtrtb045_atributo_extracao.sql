/*Adcionando mais um atributo de extração*/
DROP INDEX mtr.ix_mtrtb045_02;

CREATE UNIQUE INDEX ix_mtrtb045_02
  ON mtr.mtrtb045_atributo_extracao
  USING btree
  (nu_tipo_documento, no_atributo_documento, no_array_documento COLLATE pg_catalog."default")
TABLESPACE mtrtsix000;    