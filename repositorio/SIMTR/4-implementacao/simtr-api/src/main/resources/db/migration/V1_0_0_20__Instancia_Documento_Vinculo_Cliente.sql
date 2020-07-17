/* Tabela 014 */
---------------
ALTER TABLE "mtrsm001"."mtrtb014_instancia_documento" ADD COLUMN "nu_dossie_cliente_produto" BIGINT;
COMMENT ON COLUMN "mtrsm001"."mtrtb014_instancia_documento"."nu_dossie_cliente_produto" IS 'Atributo que representa e relação existente entre o dossiê de cliente e o dossiê de produto ao qual foi a instancia foi vinculada. Utilizado apenas para os casos de documentos associados pela vinculção definida pelo processo originador do dossiê de produto. Para os casos de documento definidos pelo elemento de conteudo do processo/produto ou da garantia informada no dossiê do produto este atributo não estara definido.';

-------------------------------------------------------------------------
/* CHAVES ESTRANGEIRAS */
------------------------
ALTER TABLE "mtrsm001"."mtrtb014_instancia_documento" ADD CONSTRAINT "fk_mtrtb014_mtrtb004"
FOREIGN KEY ("nu_dossie_cliente_produto")
REFERENCES "mtrsm001"."mtrtb004_dossie_cliente_produto" ("nu_dossie_cliente_produto")
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;