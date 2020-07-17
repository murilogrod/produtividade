
/* Tabela 006 */
---------------
ALTER TABLE mtr.mtrtb006_canal ADD COLUMN ts_ultima_alteracao TIMESTAMP NOT NULL DEFAULT now();
COMMENT ON COLUMN mtr.mtrtb006_canal.ts_ultima_alteracao IS 'Atributo utilizado para armazenar a data e hora da ultima alteração realizada no registro. Esse valor será utilizado pela API para identificar se houve alteração na estrutura de canais identificando o registro que possui o maior valor e comparando com a data do ultimo carregamento realizado. Toda vez que o registro for alterado ou alguma de suas vinculações que exija pela regra de negócio realizar um recarregamento da estrutura, o registro deve ser atualizado com a data e hora da realização da ação.';
ALTER TABLE mtr.mtrtb006_canal ALTER COLUMN ts_ultima_alteracao DROP DEFAULT;

/* Tabela 009 */
---------------
ALTER TABLE mtr.mtrtb009_tipo_documento ADD COLUMN ts_ultima_alteracao TIMESTAMP NOT NULL DEFAULT now();
COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.ts_ultima_alteracao IS 'Atributo utilizado para armazenar a data e hora da última alteração realizada no registro. Esse valor será utilizado pela API para identificar se houve alteração na estrutura de tipos documentais identificando o registro que possui o maior valor e comparando com a data do último carregamento realizado. Toda vez que o registro for alterado ou alguma de suas vinculações que exija pela regra de negócio realizar um recarregamento da estrutura, o registro deve ser atualizado com a data e hora da realização da ação.';
ALTER TABLE mtr.mtrtb009_tipo_documento ALTER COLUMN ts_ultima_alteracao DROP DEFAULT;

/* Tabela 010 */
---------------
ALTER TABLE mtr.mtrtb010_funcao_documental ADD COLUMN ts_ultima_alteracao TIMESTAMP NOT NULL DEFAULT now();
COMMENT ON COLUMN mtr.mtrtb010_funcao_documental.ts_ultima_alteracao IS 'Atributo utilizado para armazenar a data e hora da última alteração realizada no registro. Esse valor será utilizado pela API para identificar se houve alteração na estrutura de funções documentais identificando o registro que possui o maior valor e comparando com a data do último carregamento realizado. Toda vez que o registro for alterado ou alguma de suas vinculações que exija pela regra de negócio realizar um recarregamento da estrutura, o registro deve ser atualizado com a data e hora da realização da ação.';
ALTER TABLE mtr.mtrtb010_funcao_documental ALTER COLUMN ts_ultima_alteracao DROP DEFAULT;

/* Tabela 012 */
---------------
ALTER TABLE mtr.mtrtb012_tipo_situacao_dossie ADD COLUMN ts_ultima_alteracao TIMESTAMP NOT NULL DEFAULT now();
COMMENT ON COLUMN mtr.mtrtb012_tipo_situacao_dossie.ts_ultima_alteracao IS 'Atributo utilizado para armazenar a data e hota da última alteração realizada no registro. Esse valor será utilizado pela API para identificar se houve alteração na estrutura de tipos de situação do dossiê identificando o registro que possui o maior valor e comparando com a data do último carregamento realizado. Toda vez que o registro for alterado ou alguma de suas vinculações que exija pela regra de negócio realizar um recarregamento da estrutura, o registro deve ser atualizado com a data e hora da realização da ação.';
ALTER TABLE mtr.mtrtb012_tipo_situacao_dossie ALTER COLUMN ts_ultima_alteracao DROP DEFAULT;

/* Tabela 015 */
---------------
ALTER TABLE mtr.mtrtb015_situacao_documento ADD COLUMN ts_ultima_alteracao TIMESTAMP NOT NULL DEFAULT now();
COMMENT ON COLUMN mtr.mtrtb015_situacao_documento.ts_ultima_alteracao IS 'Atributo utilizado para armazenar a data e hota da última alteração realizada no registro. Esse valor será utilizado pela API para identificar se houve alteração na estrutura de tipos de situação do documento aplicados as instancias, identificando o registro que possui o maior valor e comparando com a data do último carregamento realizado. Toda vez que o registro for alterado ou alguma de suas vinculações que exija pela regra de negócio realizar um recarregamento da estrutura, o registro deve ser atualizado com a data e hora da realização da ação.';
ALTER TABLE mtr.mtrtb015_situacao_documento ALTER COLUMN ts_ultima_alteracao DROP DEFAULT;

/* Tabela 020 */
---------------
ALTER TABLE mtr.mtrtb020_processo ADD COLUMN ts_ultima_alteracao TIMESTAMP NOT NULL DEFAULT now();
COMMENT ON COLUMN mtr.mtrtb020_processo.ts_ultima_alteracao IS 'Atributo utilizado para armazenar a data e hora da última alteração realizada no registro. Esse valor será utilizado pela API para identificar se houve alteração na estrutura de processos identificando o registro que possui o maior valor e comparando com a data do último carregamento realizado. Toda vez que o registro for alterado ou alguma de suas vinculações que exija pela regra de negócio realizar um recarregamento da estrutura, o registro deve ser atualizado com a data e hora da realização da ação.';
ALTER TABLE mtr.mtrtb020_processo ALTER COLUMN ts_ultima_alteracao DROP DEFAULT;
