DO $$ 
BEGIN 
	/* Table: mtrtb105_opcoes_dominio  */ 
	--------------- 
	IF NOT EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb105_opcoes_dominio' 
	) THEN 

		CREATE TABLE mtr.mtrtb105_opcoes_dominio (
		   nu_opcao_dominio     INT4            	 NOT NULL,
		   nu_dominio           INT4                 NULL,
		   nu_versao            INT4                 NOT NULL,
		   no_value             VARCHAR(50)          NOT NULL,
		   no_opcao             VARCHAR(255)         NOT NULL,
		   de_sicli             VARCHAR(255)         NULL,
		   de_siric             VARCHAR(255)         NULL,
		   CONSTRAINT pk_mtrtb105 PRIMARY KEY (nu_opcao_dominio),
		   CONSTRAINT fk_mtrtb105_fk_mtrtb1_mtrtb104 FOREIGN KEY (nu_dominio)
      	   REFERENCES mtr.mtrtb104_dominio (nu_dominio) ON DELETE RESTRICT ON UPDATE RESTRICT
		)
		tablespace mtrtsdt000;
		
		COMMENT ON TABLE mtr.mtrtb105_opcoes_dominio IS
		'Tabela utilizada para armazenar dados de opções de dominios utilizados em campos de formulário e de extração de dados de documentos';
		
		COMMENT ON COLUMN mtr.mtrtb105_opcoes_dominio.nu_opcao_dominio IS
		'Atributo que representa a chave primaria da entidade.';
		
		COMMENT ON COLUMN mtr.mtrtb105_opcoes_dominio.nu_dominio IS
		'Atributo que representa a chave primaria da entidade.';
		
		COMMENT ON COLUMN mtr.mtrtb105_opcoes_dominio.nu_versao IS
		'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
		
		COMMENT ON COLUMN mtr.mtrtb105_opcoes_dominio.no_value IS
		'Atributo utilizado para armazenar o valor que será definido como value da opção na interface grafica.';
		
		COMMENT ON COLUMN mtr.mtrtb105_opcoes_dominio.no_opcao IS
		'Atributo que armazena o valor da opção que será exibida para o usuario no campo do formulario utilizado na interface grafica.';
		
		COMMENT ON COLUMN mtr.mtrtb105_opcoes_dominio.de_sicli IS
		'Atributo utilizado para armazenar o valor que deverá ser utilizado para envio ao SICLI quando a estrutura contiver o valor que representa este registro na seleção';
		
		COMMENT ON COLUMN mtr.mtrtb105_opcoes_dominio.de_siric IS
		'Atributo utilizado para armazenar o valor que deverá ser utilizado para envio ao SIRIC quando a estrutura contiver o valor que representa este registro na seleção';

 	END IF;
	
END $$;