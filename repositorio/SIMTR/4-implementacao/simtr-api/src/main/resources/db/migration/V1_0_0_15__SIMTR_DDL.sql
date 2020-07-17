ALTER TABLE mtrsm001.mtrtb017_stco_instnca_documento
  DROP CONSTRAINT fk_mtrtb017_mtrtb014;

ALTER TABLE mtrsm001.mtrtb017_stco_instnca_documento
  ADD CONSTRAINT fk_mtrtb017_mtrtb014 FOREIGN KEY (nu_instancia_documento)
      REFERENCES mtrsm001.mtrtb014_instancia_documento (nu_instancia_documento) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;