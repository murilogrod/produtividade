/* Remover coluna ic_verificacao_previa se existir na tabela mtr.mtrtb009_tipo_documento */

ALTER TABLE mtr.mtrtb009_tipo_documento DROP COLUMN IF EXISTS ic_verificacao_previa