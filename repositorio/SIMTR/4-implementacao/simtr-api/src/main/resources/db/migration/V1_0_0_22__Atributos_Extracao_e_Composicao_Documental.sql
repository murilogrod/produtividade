
/* Tabela 001 */
---------------
ALTER TABLE "mtrsm001"."mtrtb001_pessoa_juridica" ADD COLUMN "ic_conglomerado" BOOLEAN;
COMMENT ON COLUMN "mtrsm001"."mtrtb001_pessoa_juridica"."ic_conglomerado" IS 'Atributo utilizado para indicar se a empresa integra um conglomerado';

/* Tabela 009 */
---------------
ALTER TABLE "mtrsm001"."mtrtb009_tipo_documento" ADD COLUMN "ic_dossie_digital" BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE "mtrsm001"."mtrtb009_tipo_documento" ALTER COLUMN "ic_dossie_digital" DROP DEFAULT;
ALTER TABLE "mtrsm001"."mtrtb009_tipo_documento" ADD COLUMN "ic_apoio_negocio" BOOLEAN NOT NULL DEFAULT true;
ALTER TABLE "mtrsm001"."mtrtb009_tipo_documento" ALTER COLUMN "ic_apoio_negocio" DROP DEFAULT;
COMMENT ON COLUMN "mtrsm001"."mtrtb009_tipo_documento"."ic_dossie_digital" IS 'Atributo utilizado para identificar se o tipo de documento faz utilização perante o Dossiê Digital.';
COMMENT ON COLUMN "mtrsm001"."mtrtb009_tipo_documento"."ic_apoio_negocio" IS 'Atributo utilizado para identificar se o tipo de documento faz utilização perante o Apoio ao Negocio.';

/* Tabela 010 */
---------------
ALTER TABLE "mtrsm001"."mtrtb010_funcao_documental" DROP COLUMN "ic_ativo";

/* Tabela 020 */
---------------
ALTER TABLE "mtrsm001"."mtrtb020_processo" DROP COLUMN "ic_modelo_carga_documentos";

/* Tabela 036 */
---------------
ALTER TABLE "mtrsm001"."mtrtb036_composicao_documental" ADD COLUMN "ic_conclusao_operacao" BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE "mtrsm001"."mtrtb036_composicao_documental" ALTER COLUMN "ic_conclusao_operacao" DROP DEFAULT;
COMMENT ON COLUMN "mtrsm001"."mtrtb036_composicao_documental"."ic_conclusao_operacao" IS 'Atributo utilizado para indicar se a composição deve ser analisada no ato de conclusão da operação indicando quais são os documentos necessarios a serem entregue para permitir a finalização da operação.';


/* Tabela 045 */
---------------
ALTER TABLE "mtrsm001"."mtrtb045_atributo_extracao" ADD COLUMN "ic_tipo_campo" VARCHAR(20) NOT NULL DEFAULT 'INPUT_TEXT';
ALTER TABLE "mtrsm001"."mtrtb045_atributo_extracao" ALTER COLUMN "ic_tipo_campo" DROP DEFAULT;
ALTER TABLE "mtrsm001"."mtrtb045_atributo_extracao" ADD COLUMN "ic_obrigatorio" BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE "mtrsm001"."mtrtb045_atributo_extracao" ALTER COLUMN "ic_obrigatorio" DROP DEFAULT;
COMMENT ON COLUMN "mtrsm001"."mtrtb045_atributo_extracao"."ic_tipo_campo" IS 'Atributo utilizado para indicar o tipo de campo que deverá ser utilizado pela interface para os casos de captura da informalção quando inserida de forma manual.
Exemplos validos para este atributo são:
- INPUT_TEXT
- INPUT_PASSWORD
- INPUT_DATE
- INPUT_DATE_TIME
- INPUT_TIME
- INPUT_EMAIL
- INPUT_NUMBER
- INPUT_CPF
- INPUT_CNPJ';
COMMENT ON COLUMN "mtrsm001"."mtrtb045_atributo_extracao"."ic_obrigatorio" IS 'Atributo utilizado para indicar se esta informação é de captura obrigatorio para o tipo de documento associado.';
