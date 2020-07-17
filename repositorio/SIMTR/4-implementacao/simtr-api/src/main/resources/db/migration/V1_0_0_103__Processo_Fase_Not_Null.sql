DO $$
BEGIN

/* tb016 */
----------

ALTER TABLE mtr.mtrtb016_processo_fase_dossie ALTER COLUMN nu_processo_fase SET NOT NULL;

END $$;