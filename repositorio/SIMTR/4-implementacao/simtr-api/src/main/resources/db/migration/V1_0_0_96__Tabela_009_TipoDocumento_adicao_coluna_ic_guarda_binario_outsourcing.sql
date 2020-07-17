DO $$
BEGIN

/* Tabela 009 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb009_tipo_documento' AND COLUMN_NAME = 'ic_guarda_binario_outsourcing'
) THEN 
    ALTER TABLE mtr.mtrtb009_tipo_documento ADD COLUMN ic_guarda_binario_outsourcing BOOLEAN NOT NULL DEFAULT FALSE;
    COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.ic_guarda_binario_outsourcing IS 'Atributo determina se o binário do documento encaminhado para atendimento ao serviço outsourcing documental deve ser armazenado no SIECM.';
    ALTER TABLE mtr.mtrtb009_tipo_documento ALTER COLUMN ic_guarda_binario_outsourcing DROP DEFAULT;
END IF;
END $$;