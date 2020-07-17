
/* Tabela 009 */
---------------
COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.ic_tipo_pessoa IS 'Atributo que determina qual tipo de pessoa pode ter o documento atribuido. Quando atributo possui o valor nulo indica que trata-se de um documento de produto/serviço, não relacionado a uma pessoa.
Pode assumir os seguintes valores:
F - Fisica
J - Juridica
A - Ambos (Fisica ou Juridica)';

/* Tabela 045 */
---------------
ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN ic_identificador_pessoa BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE mtr.mtrtb045_atributo_extracao ALTER COLUMN ic_identificador_pessoa DROP DEFAULT;

COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.ic_identificador_pessoa IS 'Atributo utilizado para indicar se o atributo extraido do documento deve ser utilizado como identificador de CPF/CNPJ para associação do documento a um determinado dossiê de cliente.';

UPDATE mtr.mtrtb045_atributo_extracao SET ic_calculo_data = false WHERE ic_calculo_data IS NULL;
ALTER TABLE mtr.mtrtb045_atributo_extracao ALTER COLUMN ic_calculo_data SET NOT NULL;

/* Tabela 050 */
---------------
ALTER TABLE mtr.mtrtb050_controle_documento ADD COLUMN ic_execucao_caixa BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE mtr.mtrtb050_controle_documento ALTER COLUMN ic_execucao_caixa DROP DEFAULT;

ALTER TABLE mtr.mtrtb050_controle_documento ADD COLUMN ts_retorno_conteudo TIMESTAMP;
ALTER TABLE mtr.mtrtb050_controle_documento DROP COLUMN IF EXISTS de_rejeicao;

COMMENT ON COLUMN mtr.mtrtb050_controle_documento.ic_execucao_caixa IS 'Atributo utilizado para identificar se o registro de controle foi criado para encaminhado a execução das atividades por equipe interna da CAIXA quando definido como "true" ou encaminhado para execução em fornecedor externo quando definido como "false"';
COMMENT ON COLUMN mtr.mtrtb050_controle_documento.ts_retorno_conteudo IS 'Atributo utilizado para armazenar a data e hora de retorno do conteudo tratado encaminhado no resultado da atividade executada pelo fornecedor.';


/* Alteração de Indices */
-------------------------
CREATE UNIQUE INDEX ix_mtrtb050_01
 ON mtr.mtrtb050_controle_documento USING BTREE
 ( co_fornecedor )
TABLESPACE mtrtsix000;
