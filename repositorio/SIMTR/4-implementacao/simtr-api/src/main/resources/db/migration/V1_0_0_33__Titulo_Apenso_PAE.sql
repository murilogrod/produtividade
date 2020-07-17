/* Tabela 048 */
---------------

ALTER TABLE mtrsm001.mtrtb048_apenso_adm ADD COLUMN no_titulo VARCHAR(100) NULL;
COMMENT ON COLUMN mtrsm001.mtrtb048_apenso_adm.no_titulo IS 'Atributo utilizado para permitir a identificação nomeada de um apenso.

Este atributo deverá armazenar um valor unico para apensos vinculados a um mesmo contratoc ou processo, ou seja, uma chave de unicidade existente entre o numero do processo, numero do contrato e titulo do apenso, pois sempre que houver vinculo de um apenso com o processo, não haverá com o contrato e vice versa.';

CREATE UNIQUE INDEX ix_mtrtb048_02 ON mtrsm001.mtrtb048_apenso_adm USING btree (nu_processo_adm, no_titulo COLLATE pg_catalog."default");

CREATE UNIQUE INDEX ix_mtrtb048_03 ON mtrsm001.mtrtb048_apenso_adm USING btree (nu_contrato_adm, no_titulo COLLATE pg_catalog."default");