DO $$
BEGIN
	
	
/* Tabela mtrtb036_composicao_documental */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb036_composicao_documental' AND COLUMN_NAME = 'ts_ultima_alteracao'
) THEN
	ALTER TABLE mtr.mtrtb036_composicao_documental ADD COLUMN ic_tipo_pessoa VARCHAR(1) NOT NULL DEFAULT 'F';
	COMMENT ON COLUMN mtr.mtrtb036_composicao_documental.ic_tipo_pessoa IS 'Atributo utilizado para indicar o tipo de pessoa que a composição documental possui relação. Pode assumir os valores:
F - Pessoa Física
J - Pessoa Jurídica';
	ALTER TABLE mtr.mtrtb036_composicao_documental ALTER COLUMN ic_tipo_pessoa DROP DEFAULT;
	
	ALTER TABLE mtr.mtrtb036_composicao_documental ADD COLUMN ic_cadastro_caixa BOOLEAN NOT NULL DEFAULT false;
	COMMENT ON COLUMN mtr.mtrtb036_composicao_documental.ic_cadastro_caixa IS 'Atributo utilizado para indicar se a composição documental em referência indica documentos a serem utilizados na atualização do cadastro CAIXA';
	ALTER TABLE mtr.mtrtb036_composicao_documental ALTER COLUMN ic_cadastro_caixa DROP DEFAULT;
	
	ALTER TABLE mtr.mtrtb036_composicao_documental ADD COLUMN ts_ultima_alteracao TIMESTAMP NOT NULL DEFAULT NOW();
	COMMENT ON COLUMN mtr.mtrtb036_composicao_documental.ts_ultima_alteracao IS 'Atributo utilizado para armazenar a data e hora da ultima alteração realizada no registro. Esse valor será utilizado pela API para identificar se houve alteração na estrutura de composições documentais identificando o registro que possui o maior valor e comparando com a data do ultimo carregamento realizado. Toda vez que o registro for alterado ou alguma de suas vinculações que exija pela regra de negócio realizar um recarregamento da estrutura, o registro deve ser atualizado com a data e hora da realização da ação.';
	ALTER TABLE mtr.mtrtb036_composicao_documental ALTER COLUMN ts_ultima_alteracao DROP DEFAULT;
	
END IF;

ALTER TABLE mtr.mtrtb036_composicao_documental DROP CONSTRAINT IF EXISTS cc_mtrtb036_1;
ALTER TABLE mtr.mtrtb036_composicao_documental ADD CONSTRAINT cc_mtrtb036_1 CHECK (ic_tipo_pessoa IN ('F','J') AND ic_tipo_pessoa = UPPER(ic_tipo_pessoa));


END $$;