ALTER TABLE mtr.mtrtb001_dossie_cliente ADD COLUMN dt_criacao DATE ;
comment on column mtr.mtrtb001_dossie_cliente.dt_criacao is
'Atributo representa a Data de Criação do Dossie do Cliente';

ALTER TABLE mtr.mtrtb001_dossie_cliente ADD COLUMN nu_dire_criacao INTEGER ;
comment on column mtr.mtrtb001_dossie_cliente.nu_dire_criacao is
'Atributo representa o Número da Dire do usuário que criou o Dossiê';

ALTER TABLE mtr.mtrtb001_dossie_cliente ADD COLUMN nu_sr_criacao INTEGER ;
comment on column mtr.mtrtb001_dossie_cliente.nu_sr_criacao is
'Atributo representa o Número SR do usuário que criou o Dossiê';

ALTER TABLE mtr.mtrtb001_dossie_cliente ADD COLUMN nu_unidade_criacao INTEGER ;
comment on column mtr.mtrtb001_dossie_cliente.nu_unidade_criacao is
'Atributo representa o Número da Unidade do usuário que criou o Dossiê';

ALTER TABLE mtr.mtrtb050_auditoria_dossie ADD COLUMN de_dire_operador integer;

ALTER TABLE mtr.mtrtb050_auditoria_dossie ADD COLUMN de_sr_operador integer;

ALTER TABLE mtr.mtrtb050_auditoria_dossie ALTER COLUMN de_unidade_operador TYPE integer USING de_unidade_operador::integer;