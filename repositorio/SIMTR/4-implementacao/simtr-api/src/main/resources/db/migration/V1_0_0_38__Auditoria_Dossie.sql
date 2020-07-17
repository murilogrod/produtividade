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