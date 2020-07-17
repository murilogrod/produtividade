/* Tabela 049 */
---------------

ALTER TABLE mtrsm001.mtrtb049_documento_adm ADD COLUMN ts_exclusao TIMESTAMP NULL;
COMMENT ON COLUMN mtrsm001.mtrtb049_documento_adm.ts_exclusao IS 'Atributo utilizado para registrar a data e hora de execução da ação de exclusão logica do documento administrativo';

ALTER TABLE mtrsm001.mtrtb049_documento_adm ADD COLUMN co_matricula_exclusao VARCHAR(7) NULL;
COMMENT ON COLUMN mtrsm001.mtrtb049_documento_adm.co_matricula_exclusao IS 'Atributo utilizado para registrar a matricula do usuário que executou a ação de exclusão logica do documento administrativo

Este campo deverá estar preenchido sempre que o atributo ts_exclusao estiver definido.';

ALTER TABLE mtrsm001.mtrtb049_documento_adm ADD COLUMN de_justificativa_exclusao TEXT NULL;
COMMENT ON COLUMN mtrsm001.mtrtb049_documento_adm.de_justificativa_exclusao IS 'Atributo utilizado para armazenar a justificativa utilizada quando da exclusão logica de um documento adminstrativo.

Este campo deverá estar preenchido sempre que o atributo ts_exclusao estiver definido.';