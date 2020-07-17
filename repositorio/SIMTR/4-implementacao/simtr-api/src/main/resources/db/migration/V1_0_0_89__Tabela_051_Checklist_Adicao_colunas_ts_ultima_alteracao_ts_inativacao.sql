DO $$
BEGIN

/* Tabela 006 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb051_checklist' AND COLUMN_NAME = 'ts_ultima_alteracao'
) THEN 

    ALTER TABLE mtr.mtrtb051_checklist ADD COLUMN ts_inativacao TIMESTAMP;
    COMMENT ON COLUMN mtr.mtrtb051_checklist .ts_inativacao IS 'Atributo utilizado para armazenar a data e hora de inativação de um checklist. Uma vez inativado um checklist não deverá mais ser vinculado, mas poderá ser utilizado como base para criação de outro.';

    ALTER TABLE mtr.mtrtb051_checklist ADD COLUMN ts_ultima_alteracao TIMESTAMP NOT NULL DEFAULT now();
    COMMENT ON COLUMN mtr.mtrtb051_checklist .ts_ultima_alteracao  IS 'Atributo utilizado para armazenar a data e hora da última alteração realizada no registro. Esse valor será utilizado pela API para identificar se houve alteração na estrutura do checklist identificando o registro que possui o maior valor e comparando com a data do último carregamento realizado. Toda vez que o registro for alterado ou alguma de suas vinculações que exija pela regra de negócio realizar um recarregamento da estrutura, o registro deve ser atualizado com a data e hora da realização da ação.';
    ALTER TABLE mtr.mtrtb051_checklist ALTER COLUMN ts_ultima_alteracao DROP DEFAULT;

END IF;

END $$;