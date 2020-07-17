DO $$
BEGIN
   IF to_regclass('mtr.ix_mtrtb022_02') IS NULL THEN
      CREATE UNIQUE INDEX ix_mtrtb022_02 ON mtr.mtrtb022_produto USING btree (nu_portal_empreendedor);
   END IF;
END $$;