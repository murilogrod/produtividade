ALTER TABLE mtr.mtrtb104_auditoria_atendimento ALTER COLUMN co_matricula_operador TYPE character varying(7);
ALTER TABLE mtr.mtrtb104_auditoria_atendimento ALTER COLUMN co_matricula_operador SET NOT NULL;
ALTER TABLE mtr.mtrtb104_auditoria_atendimento ALTER COLUMN nu_unidade_operador TYPE integer USING nu_unidade_operador::integer;
ALTER TABLE mtr.mtrtb104_auditoria_atendimento ALTER COLUMN nu_unidade_operador SET NOT NULL;