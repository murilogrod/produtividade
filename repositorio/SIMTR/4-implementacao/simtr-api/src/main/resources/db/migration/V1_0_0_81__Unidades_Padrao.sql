DO $$ 
BEGIN 
	
	/* Tabela 006 */ 
	---------------
	COMMENT ON COLUMN mtr.mtrtb006_canal.ic_outorga_apimanager IS 'Atributo utilizado para indicar se o canal possui permisão ou não de consumo dos serviços do API Manager expostos diretamente na API do SIMTR em um endpoint de ponte.';
	COMMENT ON COLUMN mtr.mtrtb006_canal.ic_outorga_receita IS 'Atributo utilizado para indicar se o canal possui permisão ou não de consumo dos serviços relacionados a receita federal expostos diretamente na API do SIMTR.';
	COMMENT ON COLUMN mtr.mtrtb006_canal.ic_outorga_siric IS 'Atributo utilizado para indicar se o canal possui permissão de consumo dos serviços do SIRIC junto ao API Manager expostos diretamente na API do SIMTR.';
	
	/* Tabela 019 */ 
	---------------
	ALTER TABLE mtr.mtrtb019_campo_formulario ALTER COLUMN no_campo TYPE character varying(100);
	ALTER TABLE mtr.mtrtb019_campo_formulario ALTER COLUMN no_campo DROP DEFAULT;
	
	/* Tabela 058 */ 
	--------------- 
	IF NOT EXISTS( 
		SELECT * FROM information_schema.columns 
		WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb058_unidade_padrao' 
	) THEN 

		CREATE TABLE mtr.mtrtb058_unidade_padrao (
		   nu_unidade_padrao    SERIAL            	 NOT NULL,
		   nu_versao            INT4                 NOT NULL,
		   nu_processo          INT4                 NOT NULL,
		   nu_etapa_bpm         INT4                 NOT NULL,
		   nu_unidade           INT4                 NOT NULL,
		   CONSTRAINT pk_mtrtb058_unidade_padrao PRIMARY KEY (nu_unidade_padrao)
		);
		
		COMMENT ON TABLE mtr.mtrtb058_unidade_padrao IS
		'Tabela utilizada para indicar as unidades de tratamento padrão que devem ser atribuidas para o dossê em determinado momento pelo BPM
		Sempre o o BPM tiver que definir as unidades de tratamento, em uma determinada etapa do processo essa tabela será consultada para identificar essa informação.';
		
		COMMENT ON COLUMN mtr.mtrtb058_unidade_padrao.nu_unidade_padrao IS
		'Atributo que representa a chave primaria da entidade.';
		
		COMMENT ON COLUMN mtr.mtrtb058_unidade_padrao.nu_versao IS
		'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
		
		COMMENT ON COLUMN mtr.mtrtb058_unidade_padrao.nu_processo IS
		'Atributo utilizado para referenciar o processo que determinará as unidades padrão a serem atribuidas ao dossiê de produto esteja vinculado.
		A este registro deverá estar vinculado apenas processos do tipo "dossiê"';
		
		COMMENT ON COLUMN mtr.mtrtb058_unidade_padrao.nu_etapa_bpm IS
		'Atributo utilizado para que BPM faça rereferência a etapa no desenho do processo e possa selecionar as unidades que devem ser atribuidas naquele determinado momento.';
		
		COMMENT ON COLUMN mtr.mtrtb058_unidade_padrao.nu_unidade IS
		'Atributo que indica o código da unidade que deverá ser definida como unidade de tratamento junto ao dossiê de produto naquele momento';
		
		/*==============================================================*/
		/* Index: ix_mtrtb058_001                                       */
		/*==============================================================*/
		CREATE UNIQUE INDEX ix_mtrtb058_01 on mtr.mtrtb058_unidade_padrao (
			nu_processo,
			nu_etapa_bpm,
			nu_unidade
		);
		
		ALTER TABLE mtr.mtrtb058_unidade_padrao
		ADD CONSTRAINT fk_mtrtb058_mtrtb020_01 foreign key (nu_processo)
		REFERENCES mtr.mtrtb020_processo (nu_processo)
		ON DELETE RESTRICT ON UPDATE RESTRICT;
 	END IF;
END $$;