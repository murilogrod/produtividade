
/* Tabela 049 */
---------------
ALTER TABLE "mtrsm001"."mtrtb049_documento_adm" ADD COLUMN "de_descricao" text;
COMMENT ON COLUMN "mtrsm001"."mtrtb049_documento_adm"."de_descricao" IS 'Atributo utilizado para armazenar uma descrição para identificação e pesquisa.';
