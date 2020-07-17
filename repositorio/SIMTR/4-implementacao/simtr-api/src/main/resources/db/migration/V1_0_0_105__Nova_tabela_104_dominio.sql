DO $$ 
BEGIN 
	/* Table: mtrtb104_dominio  */ 
	--------------- 
	IF NOT EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb104_dominio' 
	) THEN 

		CREATE TABLE mtr.mtrtb104_dominio (
		   nu_dominio    		INT4            	 NOT NULL,
		   nu_versao            INT4                 NOT NULL,
		   no_dominio           VARCHAR(100)         NOT NULL,
		   ic_tipo              VARCHAR(20)          NOT NULL,
		   ic_multiplos         bool		         NOT NULL,
		   ts_ultima_alteracao  timestamp            NOT NULL,
		   CONSTRAINT pk_mtrtb104 PRIMARY KEY (nu_dominio)
		)
		tablespace mtrtsdt000;
		
		COMMENT ON TABLE mtr.mtrtb104_dominio IS
		'Tabela utilizada para armazenar dados de dominios de dados utilizados em campos de formulário e de extração de dados de documentos';
		
		COMMENT ON COLUMN mtr.mtrtb104_dominio.nu_dominio IS
		'Atributo que representa a chave primaria da entidade.';
		
		COMMENT ON COLUMN mtr.mtrtb104_dominio.nu_versao IS
		'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
		
		COMMENT ON COLUMN mtr.mtrtb104_dominio.no_dominio IS
		'Atributo utilizado para representar o nome do dominio utilizado para agrupar os valores das opções';
		
		COMMENT ON COLUMN mtr.mtrtb104_dominio.ic_tipo IS
		'Atributo utilizado para armazenar o tipo de campo de formulario que será gerado. Exemplos validos para este atributo são:
		- CHECKBOX
		- DATE
		- EMAIL,
		- NUMBER
		- RADIO
		- SELECT
		- TEXT
		- TIME
		- URL
		//*********
		- CPF_CNPJ
		- SELECT_SN';
		
		COMMENT ON COLUMN mtr.mtrtb104_dominio.ic_multiplos IS
		'Atributo utilizado para indicar se o dominio permite seleção multipla caso a estrutura do campo comporte';
		
		COMMENT ON COLUMN mtr.mtrtb104_dominio.ts_ultima_alteracao IS
		'Atributo utilizado para armazenar de data e hora da ultima alterações realizada no registro de forma a viabilizar a realização de cache da informação';

 	END IF;
	
END $$;