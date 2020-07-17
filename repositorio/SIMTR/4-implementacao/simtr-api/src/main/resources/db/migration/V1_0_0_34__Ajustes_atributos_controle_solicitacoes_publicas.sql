/* Tabela 003 */
---------------
ALTER TABLE mtrsm001.mtrtb003_documento ALTER COLUMN nu_tipo_documento DROP NOT NULL;
ALTER TABLE mtrsm001.mtrtb003_documento ADD COLUMN ic_janela_extracao VARCHAR(5) NULL;
COMMENT ON COLUMN mtrsm001.mtrtb003_documento.ic_janela_extracao IS 'Atributo utilizado para armazenar a janela temporal solicitada para execução da atividade junto ao fornecedor do serviço podendo assumir os seguintes valores:

M0 - Indica solcitação de extração on-line no minuto 0
M30 - Indica solicitação de extração na janela temporal de SLA com 30 minutos
M60 - Indica solicitação de extração na janela temporal de SLA com 60 minutos';

/* Tabela 003 */
---------------
ALTER TABLE mtrsm001.mtrtb006_canal ADD COLUMN ic_janela_extracao_m0 BOOLEAN NOT NULL DEFAULT true;
ALTER TABLE mtrsm001.mtrtb006_canal ALTER COLUMN ic_janela_extracao_m0 DROP DEFAULT;

ALTER TABLE mtrsm001.mtrtb006_canal ADD COLUMN ic_janela_extracao_m30 BOOLEAN NOT NULL DEFAULT true;
ALTER TABLE mtrsm001.mtrtb006_canal ALTER COLUMN ic_janela_extracao_m30 DROP DEFAULT;

ALTER TABLE mtrsm001.mtrtb006_canal ADD COLUMN ic_janela_extracao_m60 BOOLEAN NOT NULL DEFAULT true;
ALTER TABLE mtrsm001.mtrtb006_canal ALTER COLUMN ic_janela_extracao_m60 DROP DEFAULT;

ALTER TABLE mtrsm001.mtrtb006_canal ADD COLUMN ic_avaliacao_autenticidade BOOLEAN NOT NULL DEFAULT true;
ALTER TABLE mtrsm001.mtrtb006_canal ALTER COLUMN ic_avaliacao_autenticidade DROP DEFAULT;

COMMENT ON COLUMN mtrsm001.mtrtb006_canal.ic_janela_extracao_m0 IS 'Atributo utilizado para indicar se o canal possui permisão ou não de consumo do serviço de extração na janela M+0';
COMMENT ON COLUMN mtrsm001.mtrtb006_canal.ic_janela_extracao_m30 IS 'Atributo utilizado para indicar se o canal possui permisão ou não de consumo do serviço de extração na janela M+30';
COMMENT ON COLUMN mtrsm001.mtrtb006_canal.ic_janela_extracao_m60 IS 'Atributo utilizado para indicar se o canal possui permisão ou não de consumo do serviço de extração na janela M+60';
COMMENT ON COLUMN mtrsm001.mtrtb006_canal.ic_avaliacao_autenticidade IS 'Atributo utilizado para indicar se o canal possui permisão ou não de consumo do serviço de avaliação de autenticidade documental';