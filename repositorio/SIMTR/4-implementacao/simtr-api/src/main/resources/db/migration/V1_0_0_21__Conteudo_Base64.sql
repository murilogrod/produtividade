/*CRIACAO DO NOVO SCHEMA*/
-------------------------

CREATE SCHEMA "mtrsm002" AUTHORIZATION "postgres";

/* Adequação de Sequences - Parte 01 de 02 */
--------------------------------------------
CREATE SEQUENCE "mtrsm002"."mtrsq008_conteudo";


/* Tabela 003 */
---------------
ALTER TABLE "mtrsm001"."mtrtb003_documento" ADD COLUMN "no_formato" VARCHAR(10);
COMMENT ON COLUMN "mtrsm001"."mtrtb003_documento"."no_formato" IS 'Atributo utilizado para armazenar o formato do documento, podendo ser nulo para atender situações onde armazena-se um documento sem conteudos. Ex:
- pdf
- jpg
- tiff';

/* Tabela 008 */
---------------
DROP TABLE mtrsm001.mtrtb008_conteudo;

CREATE TABLE "mtrsm002"."mtrtb008_conteudo" (
                "nu_conteudo" BIGINT NOT NULL,
                "nu_versao" INTEGER NOT NULL,
                "nu_documento" BIGINT NOT NULL,
                "nu_ordem" INT NOT NULL,
                "de_conteudo" TEXT NOT NULL,
                CONSTRAINT "pk_mtrtb008" PRIMARY KEY ("nu_conteudo")
);
COMMENT ON TABLE "mtrsm002"."mtrtb008_conteudo" IS 'Tabela responsavel pelo armazenamento das referencia de conteudo que compoem o documento.
Nesta tabela serão efetivamente armazenados os dados que caracterizam o conteudo do documento (ou o binario) para manipulação da aplicação até o momento que o documento será efetivamente arquivado no repositorio em carater definitivo.';
COMMENT ON COLUMN "mtrsm002"."mtrtb008_conteudo"."nu_conteudo" IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN "mtrsm002"."mtrtb008_conteudo"."nu_versao" IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
COMMENT ON COLUMN "mtrsm002"."mtrtb008_conteudo"."nu_documento" IS 'Atributo utilizado para identificar o documento que o conteudo esta vinculado. Neste formato é possivel associar varios conteudos a um mesmo documento.';
COMMENT ON COLUMN "mtrsm002"."mtrtb008_conteudo"."nu_ordem" IS 'Atributo utilizado para identificar a ordem de exibição na composição do documento. Documentos que possuem apenas um elemento, como um arquivo pdf por exemplo terão apenas um registro de conteudo com o atributo de ordem como 1';
COMMENT ON COLUMN "mtrsm002"."mtrtb008_conteudo"."de_conteudo" IS 'Atributo utilizado para armazenar o conteudo efetivo do documento em formato base64. Documentos que possuem apenas um elemento, como um arquivo pdf por exemplo terão apenas um registro de conteudo contendo o documento na integra.';


/* CHAVES ESTRANGEIRAS */
------------------------
ALTER TABLE "mtrsm002"."mtrtb008_conteudo" ADD CONSTRAINT "fk_mtrtb008_mtrtb003"
FOREIGN KEY ("nu_documento")
REFERENCES "mtrsm001"."mtrtb003_documento" ("nu_documento")
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;


/* INDICES DE UNICIDADE*/
------------------------
CREATE UNIQUE INDEX ix_mtrtb008_01 ON "mtrsm002"."mtrtb008_conteudo" USING BTREE ( "nu_documento", "nu_ordem" );

/* Adequação de Sequences - Parte 02 de 02 */
--------------------------------------------
ALTER TABLE mtrsm002.mtrtb008_conteudo ALTER COLUMN nu_conteudo SET DEFAULT nextval('mtrsm002.mtrsq008_conteudo'::regclass);