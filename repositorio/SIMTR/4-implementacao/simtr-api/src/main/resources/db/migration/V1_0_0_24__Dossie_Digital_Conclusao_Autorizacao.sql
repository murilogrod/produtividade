
/* Tabela 100 */
---------------
ALTER TABLE "mtrsm001"."mtrtb100_autorizacao" ADD COLUMN "ts_conclusao" TIMESTAMP;
COMMENT ON COLUMN "mtrsm001"."mtrtb100_autorizacao"."ts_conclusao" IS 'Atributo utilizado para armazenar a data e hora da conclusão da operação vinculada a autorização concedida.';
