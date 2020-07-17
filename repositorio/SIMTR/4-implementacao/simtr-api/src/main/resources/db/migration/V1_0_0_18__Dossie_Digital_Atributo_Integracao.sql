
/* Tabela 045 */
---------------
ALTER TABLE "mtrsm001"."mtrtb045_atributo_extracao" RENAME "de_atributo_ged" TO "no_atributo_ged";
ALTER TABLE "mtrsm001"."mtrtb045_atributo_extracao" ALTER COLUMN "no_atributo_ged" TYPE VARCHAR(100);
ALTER TABLE "mtrsm001"."mtrtb045_atributo_extracao" ADD COLUMN "no_atributo_documento" VARCHAR(100) NOT NULL DEFAULT '';
ALTER TABLE "mtrsm001"."mtrtb045_atributo_extracao" ALTER COLUMN "no_atributo_documento" DROP DEFAULT;
ALTER TABLE "mtrsm001"."mtrtb045_atributo_extracao" ADD COLUMN "ic_tipo_ged" VARCHAR(20) NULL;
ALTER TABLE "mtrsm001"."mtrtb045_atributo_extracao" ADD COLUMN "ic_obrigatorio_ged" BOOLEAN NULL;
COMMENT ON COLUMN "mtrsm001"."mtrtb045_atributo_extracao"."no_atributo_documento" IS 'Atributo utilizado para armazenar o nome do atributo identificado pela integração com outros sistemas. Esse valor armazena o nome utilizado nas representações JSON/XML encaminhadas nos serviços de integração e na identificação do atributo utilizado na tb007.';
COMMENT ON COLUMN "mtrsm001"."mtrtb045_atributo_extracao"."ic_tipo_ged" IS 'Atributo utilizado para indicar qual o tipo de atributo definido junto a classe GED. Pode assumir os seguintes valores:
- BOOLEAN
- STRING
- LONG
- DATE
';
COMMENT ON COLUMN "mtrsm001"."mtrtb045_atributo_extracao"."ic_obrigatorio_ged" IS 'Atributo utilizado para indicar se esta informação junto ao GED tem cunho obrigatorio.';
-------------------------------------------------------------------------
