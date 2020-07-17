
/* Tabela 045 */
---------------
ALTER TABLE "mtrsm001"."mtrtb045_atributo_extracao" ADD COLUMN "no_atributo_sicli" VARCHAR(100);
ALTER TABLE "mtrsm001"."mtrtb045_atributo_extracao" ADD COLUMN "no_objeto_sicli" VARCHAR(100);
ALTER TABLE "mtrsm001"."mtrtb045_atributo_extracao" ADD COLUMN "ic_tipo_sicli" VARCHAR(50);
COMMENT ON COLUMN "mtrsm001"."mtrtb045_atributo_extracao"."no_atributo_sicli" IS 'Atributo utilizado para definir o nome do atributo utilizado na atualização de dados do SICLI que tem relação com o atributo do documento.
Esse atributo deve ser utilizado em conjunto com o atributo "no_objeto_sicli" e "ic_tipo_sicli" que determina o agrupamento do atributo a ser enviado na mensagem de atualização.
Ex1:
no_objeto_sicli = renda/fontePagadora
no_atributo_sicli = nomeFantasia
ic_tipo_sicli = STRING

objeto enviado = {
    "renda" : {
        "fontePagadora" : {
            "nomeFantasia" : "XXXXXXXXXXXXXXX"
            ......................
        }
        ........................
    }
}


Ex2:
no_objeto_sicli = endereco
no_atributo_sicli = cep
ic_tipo_sicli = STRING

objeto enviado = {
    "endereco" : {
        ....................
        "cep" : "XXXXXXXXXXXXXXX"
    }
}';
COMMENT ON COLUMN "mtrsm001"."mtrtb045_atributo_extracao"."no_objeto_sicli" IS 'Atributo utilizado para definir o objeto que agrupa o atributo utilizado na atualização de dados do SICLI que tem relação com o atributo do documento.
Esse atributo deve ser utilizado em conjunto com o atributo "no_atributo_sicli" e "ic_tipo_sicli" que determina o agrupamento do atributo a ser enviado na mensagem de atualização.
Ex1:
no_objeto_sicli = renda/fontePagadora
no_atributo_sicli = nomeFantasia
ic_tipo_sicli = STRING

objeto enviado = {
    "renda" : {
        "fontePagadora" : {
            "nomeFantasia" : "XXXXXXXXXXXXXXX"
            ........................
        }
        ......................
    }
}


Ex2:
no_objeto_sicli = endereco
no_atributo_sicli = cep
ic_tipo_sicli = STRING

objeto enviado = {
    "endereco" : {
        ...........
        "cep" : "XXXXXXXXXXXXXXX"
    }
}';
COMMENT ON COLUMN "mtrsm001"."mtrtb045_atributo_extracao"."ic_tipo_sicli" IS 'Atributo utilizado para indicar qual o tipo de atributo definido junto ao objeto a ser enviado na atualização de dados do SICLI. Pode assumir os seguintes valores:
- BOOLEAN
- STRING
- LONG
- DATE
- DECIMAL';
