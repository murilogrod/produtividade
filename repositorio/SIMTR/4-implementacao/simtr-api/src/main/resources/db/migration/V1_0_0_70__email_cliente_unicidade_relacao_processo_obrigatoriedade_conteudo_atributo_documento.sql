DO $$
BEGIN

/* Tabela 001 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb001_dossie_cliente' AND COLUMN_NAME = 'de_email'
) THEN 
    ALTER TABLE mtr.mtrtb001_dossie_cliente ADD COLUMN de_email VARCHAR(255);
    COMMENT ON COLUMN mtr.mtrtb001_dossie_cliente.de_email IS 'Atributo utilizado para identificar a localização de um documento armazenado no sistema de Gestão Eletronica de Documentos (SIECM:GED) sob o OS_DOSSIEIDIGITAL arquivando o documento recebido e transacionado pelo SIMTR.';
END IF;

/* Tabela 007 */
---------------

ALTER TABLE mtr.mtrtb007_atributo_documento ALTER COLUMN de_conteudo DROP NOT NULL;

/* Tabela 026 */
---------------

DROP INDEX IF EXISTS mtr.ix_mtrtb026_02;
CREATE UNIQUE INDEX ix_mtrtb026_02 ON mtr.mtrtb026_relacao_processo (
    nu_processo_pai,
    nu_processo_filho
)
TABLESPACE mtrtsix000;

END $$;