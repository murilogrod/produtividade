DO $$
BEGIN

/* Tabela 020 */
---------------
IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb020_processo' AND COLUMN_NAME = 'nu_identificador_bpm'
) THEN 
    ALTER TABLE mtr.mtrtb020_processo ADD COLUMN nu_identificador_bpm INT4;
    COMMENT ON COLUMN mtr.mtrtb020_processo.nu_identificador_bpm IS 'Atributo utilizado para que a solução de BPM possa capturar dados do processo independente do seu identificador serial que pode diferir de acordo com cada ambiente. Por exemplo, localizar qual o identificador do processo fase';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb020_processo' AND COLUMN_NAME = 'ic_interface'
) THEN 
    ALTER TABLE mtr.mtrtb020_processo ADD COLUMN ic_interface BOOLEAN NOT NULL DEFAULT FALSE;
    COMMENT ON COLUMN mtr.mtrtb020_processo.ic_interface IS 'Atributo que indica se o processo deve ser apresentado na interface web para criação da operação.';
    ALTER TABLE mtr.mtrtb020_processo ALTER COLUMN ic_interface DROP DEFAULT;
END IF;

/* Tabela 032 */
---------------
IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb032_elemento_conteudo' AND COLUMN_NAME = 'nu_identificador_bpm'
) THEN 
    ALTER TABLE mtr.mtrtb032_elemento_conteudo ADD COLUMN nu_identificador_bpm INT4;
    COMMENT ON COLUMN mtr.mtrtb032_elemento_conteudo.nu_identificador_bpm IS 'Atributo utilizado para que a solução de BPM possa capturar dados do processo independente do seu identificador serial que pode diferir de acordo com cada ambiente. Por exemplo, localizar qual o identificador do processo fase';
END IF;

DROP INDEX IF EXISTS mtr.ix_mtrtb020_02;
CREATE INDEX ix_mtrtb020_02 ON mtr.mtrtb020_processo (
    nu_identificador_bpm
)
TABLESPACE mtrtsix000;

DROP INDEX IF EXISTS mtr.ix_mtrtb032_05;
CREATE INDEX ix_mtrtb032_05 ON mtr.mtrtb032_elemento_conteudo (
    nu_identificador_bpm
)
TABLESPACE mtrtsix000;


END $$;