DO $$
BEGIN

DROP INDEX mtr.ix_mtrtb019_02;
CREATE UNIQUE INDEX ix_mtrtb019_02
  ON mtr.mtrtb019_campo_formulario
  USING btree
  (nu_identificador_bpm)
TABLESPACE mtrtsix000;

DROP INDEX IF EXISTS mtr.ix_mtrtb020_02;
CREATE UNIQUE INDEX ix_mtrtb020_02 ON mtr.mtrtb020_processo (
    nu_identificador_bpm
)
TABLESPACE mtrtsix000;

DROP INDEX IF EXISTS mtr.ix_mtrtb020_02;
CREATE UNIQUE INDEX ix_mtrtb020_02 ON mtr.mtrtb020_processo (
    nu_identificador_bpm
)
TABLESPACE mtrtsix000;

DROP INDEX IF EXISTS mtr.ix_mtrtb032_05;
CREATE UNIQUE INDEX ix_mtrtb032_05 ON mtr.mtrtb032_elemento_conteudo (
    nu_identificador_bpm
)
TABLESPACE mtrtsix000;


END $$;