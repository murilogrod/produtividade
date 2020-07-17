-- INICIO ROLLBACK FLYWAY #90
ALTER TABLE mtr.mtrtb037_regra_documental ADD COLUMN ix_antifraude BOOLEAN;
ALTER TABLE mtr.mtrtb037_regra_documental ADD COLUMN nu_canal_captura INT4 NOT NULL DEFAULT 1;