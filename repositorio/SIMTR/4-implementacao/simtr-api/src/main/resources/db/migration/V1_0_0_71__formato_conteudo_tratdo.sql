DO $$
BEGIN

/* Tabela 003 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb003_documento' AND COLUMN_NAME = 'no_formato_tratado'
) THEN 
    ALTER TABLE mtr.mtrtb003_documento ADD COLUMN no_formato_tratado VARCHAR(10);
    COMMENT ON COLUMN mtr.mtrtb003_documento.no_formato_tratado IS 'Atributo utilizado para armazenar o formato do documento que foi retornado pelo fornecedor com os tratamentos aplicados. Ex:
- PDF
- JPG
- TIFF';
END IF;

END $$;