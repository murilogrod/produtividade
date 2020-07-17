
/* Tabela 002 */
---------------
ALTER TABLE "mtrsm001"."mtrtb002_dossie_produto" ADD COLUMN "nu_instancia_processo_bpm" BIGINT;
COMMENT ON COLUMN "mtrsm001"."mtrtb002_dossie_produto"."nu_instancia_processo_bpm" IS 'Atributo que armazena o identificador da instancia de processo em execução perante a solução de BPM vinculada ao dossiê de produto.';

/* Tabela 020 */
---------------
ALTER TABLE "mtrsm001"."mtrtb020_processo" ADD COLUMN "no_processo_bpm" VARCHAR(255);
ALTER TABLE "mtrsm001"."mtrtb020_processo" ADD COLUMN "no_container_bpm" VARCHAR(255);
COMMENT ON COLUMN "mtrsm001"."mtrtb020_processo"."no_processo_bpm" IS 'Atributo que armazena o valor de identificação do processo orignador junto a solução de BPM.
Ex: bpm-simtr.ProcessoConformidade';
COMMENT ON COLUMN "mtrsm001"."mtrtb020_processo"."no_container_bpm" IS 'Atributo que armazena o valor de identificação do container utilizado no agrupamento dos processos junto a solução de BPM que possui o processo originador vinculado.
Ex: bpm-simtr_1.0.0';


