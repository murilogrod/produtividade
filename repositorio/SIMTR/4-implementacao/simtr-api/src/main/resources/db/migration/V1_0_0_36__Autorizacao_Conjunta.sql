/* Tabela 100 */
---------------
ALTER TABLE mtr.mtrtb100_autorizacao ADD COLUMN co_autorizacao_conjunta BIGINT NULL;
COMMENT ON COLUMN mtr.mtrtb100_autorizacao.co_autorizacao_conjunta IS 'Atributo utilizado para armazenar o codigo de autorização gerado para vincular autorizações individuais e entrega ao sistema de negocio como forma de identificar uma autorização concidida a uma operação conjunta. Os registros de autorizações vinculados deverão possuir o mesmo valor deste campo.';