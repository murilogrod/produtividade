-- V1_0_0_36__Autorizacao_Conjunta.sql

/* Tabela 100 */
---------------
ALTER TABLE mtr.mtrtb100_autorizacao ADD COLUMN co_autorizacao_conjunta BIGINT NULL;
COMMENT ON COLUMN mtr.mtrtb100_autorizacao.co_autorizacao_conjunta IS 'Atributo utilizado para armazenar o codigo de autorização gerado para vincular autorizações individuais e entrega ao sistema de negocio como forma de identificar uma autorização concidida a uma operação conjunta. Os registros de autorizações vinculados deverão possuir o mesmo valor deste campo.';

-- V1_0_0_37__Partilha_Valor_Atributo_Documento.sql

/* Tabela 045 */
---------------
ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN ic_modo_partilha VARCHAR(1) NULL;
COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.ic_modo_partilha IS 'Atributo utilizado para indicar a forma de partilha da informação de um atributo quando comparado com o campo do SICLI que representa o nome da mãe indicando qual parte da informação deve ser enviada para outro atributo, podendo assumir o seguinte dominio:

- S - Sobra
- L - Localizado';

ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN nu_atributo_partilha INT4 NULL;
COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.ic_modo_partilha IS 'Representa a relação com o atributo que deve ser utilizado na partilha da informação quando comparado ao nome da mãe do SICPF na forma indicada no atributo ic_modo_partilha';

/* CHAVES ESTRANGEIRAS */
------------------------
ALTER TABLE mtr.mtrtb045_atributo_extracao ADD CONSTRAINT fk_mtrtb045_mtrtb045_01
FOREIGN KEY (nu_atributo_partilha)
REFERENCES mtr.mtrtb045_atributo_extracao (nu_atributo_extracao)
ON DELETE RESTRICT
ON UPDATE RESTRICT
NOT DEFERRABLE;

/* CORRECAO DE NOMES DE CHAVES ESTRANGEIRAS*/
--------------------------------------------
ALTER TABLE mtr.mtrtb045_atributo_extracao RENAME CONSTRAINT fk_mtrtb045_fk_mtrtb0_mtrtb009 TO fk_mtrtb045_mtrtb009_01;

-- V1_0_0_38__Auditoria_Dossie.sql

CREATE SEQUENCE "mtr"."mtrsq050_auditoria_dossie";

CREATE TABLE "mtr"."mtrtb050_auditoria_dossie" (
	"nu_identificador" BIGINT NOT NULL,
	"de_acao" varchar(100) NOT NULL,
	"nu_cpf_dossie" BIGINT NOT NULL, 
	"de_tela_acao" varchar(250) NOT NULL,
	"ts_data_acao" timestamp NOT NULL,
	"de_matricula_operador" varchar(25) NOT NULL,
	"de_unidade_operador" varchar(250) NOT NULL,
	"nu_versao" integer,
	CONSTRAINT "pk_mtrtb050" PRIMARY KEY ("nu_identificador" )
	
);

ALTER TABLE mtr.mtrtb050_auditoria_dossie ALTER COLUMN nu_identificador SET DEFAULT nextval('mtr.mtrsq050_auditoria_dossie'::regclass);

-- V1_0_0_91__Delete_Atributo_Extracao.sql

DELETE FROM mtr.mtrtb045_atributo_extracao WHERE nu_atributo_extracao = 458;
DELETE FROM mtr.mtrtb045_atributo_extracao WHERE nu_atributo_extracao = 459;
DELETE FROM mtr.mtrtb045_atributo_extracao WHERE nu_atributo_extracao = 455;
DELETE FROM mtr.mtrtb045_atributo_extracao WHERE nu_atributo_extracao = 456;
UPDATE mtr.mtrtb045_atributo_extracao SET no_atributo_ged = 'MUNICIPIO' WHERE no_atributo_negocial = 'Município';

-- V1_0_0_92__Adicionando_Validacao_cadastral.sql

UPDATE mtr.mtrtb009_tipo_documento set ic_validacao_cadastral = true where nu_tipo_documento = 7;
UPDATE mtr.mtrtb009_tipo_documento set ic_validacao_cadastral = true where nu_tipo_documento = 9;
UPDATE mtr.mtrtb009_tipo_documento set ic_validacao_cadastral = true where nu_tipo_documento = 28;
UPDATE mtr.mtrtb009_tipo_documento set ic_validacao_cadastral = true where nu_tipo_documento = 29;
UPDATE mtr.mtrtb009_tipo_documento set ic_validacao_cadastral = true where nu_tipo_documento = 30;
UPDATE mtr.mtrtb009_tipo_documento set ic_validacao_cadastral = true where nu_tipo_documento = 32;
UPDATE mtr.mtrtb009_tipo_documento set ic_validacao_cadastral = true where nu_tipo_documento = 44;
