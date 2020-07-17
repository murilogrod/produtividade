
/* Tabela 045 */
---------------
ALTER TABLE "mtrsm001"."mtrtb045_atributo_extracao" ADD COLUMN "ic_campo_sicpf" VARCHAR(50);
ALTER TABLE "mtrsm001"."mtrtb045_atributo_extracao" ADD COLUMN "ic_modo_sicpf" VARCHAR(1);
COMMENT ON COLUMN "mtrsm001"."mtrtb045_atributo_extracao"."ic_campo_sicpf" IS 'Atributo utilizado para indicar qual campo de retorno o SICPF deve ser utilizado para uma possivel comparação podendo assumi o seguinte dominio:

- NASCIMENTO
- NOME
- MAE
- ELEITOR';
COMMENT ON COLUMN "mtrsm001"."mtrtb045_atributo_extracao"."ic_modo_sicpf" IS 'Atributo utilizado para indicar a forma de comparação com o campo de retorno o SICPF deve ser utilizado para uma possivel comparação podendo assumi o seguinte dominio:

- E - Exato
- P - Parcial';


-------------------------------------------------------------------------

