DO $$
BEGIN
	
	
/* Tabela mtrtb036_composicao_documental */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb050_controle_documento' AND COLUMN_NAME = 'no_processo'
) THEN
	ALTER TABLE mtr.mtrtb050_controle_documento ADD COLUMN no_processo VARCHAR(100);
	COMMENT ON COLUMN mtr.mtrtb050_controle_documento.no_processo IS 'Atributo utilizando para armazenar um valor a ser usado para sinalizar o fornecedor de alguma forma de atuação especifica definnida sob o acordo de nível operacional Essa informação será utilizada no apoio ao calculo de faturamento e só deverá estar presente nos casosde envio ao fornecedor externo. Para o controle gerado sob a execução da equipe interna da CAIXA (ic_execucao_caixa = true), essa informação não terá validade';
END IF;

END $$;