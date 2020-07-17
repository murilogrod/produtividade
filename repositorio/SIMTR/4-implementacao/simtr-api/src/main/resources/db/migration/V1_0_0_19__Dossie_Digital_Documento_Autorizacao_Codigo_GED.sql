
/* Tabela 101 */
---------------
ALTER TABLE "mtrsm001"."mtrtb101_documento" ADD COLUMN "co_documento_ged" VARCHAR(100) NOT NULL DEFAULT '';
ALTER TABLE "mtrsm001"."mtrtb101_documento" ALTER COLUMN "co_documento_ged" DROP DEFAULT;
ALTER TABLE "mtrsm001"."mtrtb101_documento" DROP COLUMN "nu_documento_cliente";
COMMENT ON COLUMN "mtrsm001"."mtrtb101_documento"."co_documento_ged" IS 'Atributo utilizado para armazenar a referencia do registro do documento junto ao GED utilizado na emissão da autorização';

-------------------------------------------------------------------------
