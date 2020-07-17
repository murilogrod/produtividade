/* Tabela 009 */
---------------
ALTER TABLE mtrsm001.mtrtb009_tipo_documento ADD COLUMN ic_validacao_cadastral BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE mtrsm001.mtrtb009_tipo_documento ALTER COLUMN ic_validacao_cadastral DROP DEFAULT;
COMMENT ON COLUMN mtrsm001.mtrtb009_tipo_documento.ic_validacao_cadastral IS 'Indica se o documento pode ser enviado a avaliação de validade cadastral.

Atualmente essa avaliação é realizada pelo SIFRC';

ALTER TABLE mtrsm001.mtrtb009_tipo_documento ADD COLUMN ic_validacao_documental BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE mtrsm001.mtrtb009_tipo_documento ALTER COLUMN ic_validacao_documental DROP DEFAULT;
COMMENT ON COLUMN mtrsm001.mtrtb009_tipo_documento.ic_validacao_documental IS 'Indica se o documento pode ser enviado a avaliação de validade documental.

Atualmente essa avaliação é realizada pelo SICOD';