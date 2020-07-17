DO $$ 
BEGIN 
	
	/* Table: mtrtb059_objeto_integracao  */ 
	--------------- 
	IF NOT EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb059_objeto_integracao' 
	) THEN 
	
		CREATE SEQUENCE mtr.mtrsq059_objeto_integracao;
	
		CREATE TABLE mtr.mtrtb059_objeto_integracao
		(
		  nu_objeto_integracao 		INT4 NOT NULL DEFAULT nextval('mtr.mtrsq059_objeto_integracao'::regclass), 
		  ic_sistema_integracao 	VARCHAR(100),
		  no_objeto_integracao		VARCHAR(100),
		  ic_acao					VARCHAR(1),
		  nu_versao            		INT4 NOT NULL,
		  
		  CONSTRAINT pk_mtrtb059 PRIMARY KEY (nu_objeto_integracao)
		)
		tablespace mtrtsdt000;
	
 	END IF;
 	
 	/* Table: mtrtb060_atributo_integracao  */ 
	--------------- 
	IF NOT EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb060_atributo_integracao' 
	) THEN 
	
		CREATE SEQUENCE mtr.mtrsq060_atributo_integracao;
	  
		CREATE TABLE mtr.mtrtb060_atributo_integracao
		(
		  nu_atributo_integracao 		INT4 NOT NULL DEFAULT nextval('mtr.mtrsq060_atributo_integracao'::regclass), 
		  nu_objeto_integracao 			INT4,
		  no_atributo					VARCHAR(100),
		  tipo_atributo					VARCHAR(1),
		  ic_obrigatorio				bool NOT NULL,
		  ic_acao						VARCHAR(1),
		  ic_ativo						bool NOT NULL,
		  ic_chave_comparacao			bool NOT NULL,
		  ic_operador_comparacao		VARCHAR(10),
		  ic_acao_comparacao			VARCHAR(1),
		  nu_versao            			INT4 NOT NULL,
		  		
		  CONSTRAINT pk_mtrtb060 PRIMARY KEY (nu_atributo_integracao),
		  
		  CONSTRAINT fk_mtrtb060_mtrtb059 FOREIGN KEY (nu_objeto_integracao)
		      REFERENCES mtr.mtrtb059_objeto_integracao (nu_objeto_integracao) MATCH SIMPLE
		      ON UPDATE RESTRICT ON DELETE RESTRICT
		)
		tablespace mtrtsdt000;
	
	END IF;
	
	/* Table: mtrtb061_priorizacao_objeto_integracao  */ 
	--------------- 
	IF NOT EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb061_priorizacao_objeto_integracao' 
	) THEN 
	
		CREATE SEQUENCE mtr.mtrsq061_priorizacao_atributo_integracao;
	
		CREATE TABLE mtr.mtrtb061_priorizacao_objeto_integracao
		(
		  nu_priorizacao_atributo_integracao 	INT4 NOT NULL DEFAULT nextval('mtr.mtrsq061_priorizacao_atributo_integracao'::regclass), 
		  nu_processo_dossie 			INT4 NOT NULL,
		  nu_objeto_integracao			INT4,
		  ic_fonte						VARCHAR(1),
		  nu_tipo_documento				INT4,
		  nu_ordem_prioridade			INT4 NOT NULL,
		  nu_versao            			INT4 NOT NULL,
		  
		  CONSTRAINT pk_mtrtb061 PRIMARY KEY (nu_priorizacao_atributo_integracao),
		  
		  CONSTRAINT fk_mtrtb061_mtrtb059 FOREIGN KEY (nu_objeto_integracao)
		      REFERENCES mtr.mtrtb059_objeto_integracao (nu_objeto_integracao) MATCH SIMPLE
		      ON UPDATE RESTRICT ON DELETE RESTRICT
		)
		tablespace mtrtsdt000;
	
	END IF;
	
END $$;
       