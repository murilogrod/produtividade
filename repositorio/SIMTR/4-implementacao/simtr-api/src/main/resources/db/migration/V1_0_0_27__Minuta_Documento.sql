
/* Tabela 009 */
---------------
ALTER TABLE "mtrsm001"."mtrtb009_tipo_documento" ADD COLUMN "no_arquivo_minuta" VARCHAR(100);
COMMENT ON COLUMN "mtrsm001"."mtrtb009_tipo_documento"."no_arquivo_minuta" IS 'Atributo utilizado para indicar o nome do arquivo utilizado pelo gerador de relatorio na emissão da minuta do documento';

ALTER TABLE "mtrsm001"."mtrtb045_atributo_extracao" ALTER COLUMN "ic_tipo_campo" DROP NOT NULL;
ALTER TABLE "mtrsm001"."mtrtb045_atributo_extracao" ADD COLUMN "ic_tipo_geral" VARCHAR(50);
COMMENT ON COLUMN "mtrsm001"."mtrtb045_atributo_extracao"."ic_tipo_geral" IS 'Atributo utilizado para indicar qual o tipo de atributo definido para ações internas do SIMTR como geração de minutas, validação da informação, etc. Pode assumir os seguintes valores:
- BOOLEAN
- STRING
- LONG
- DATE
- DECIMAL';
