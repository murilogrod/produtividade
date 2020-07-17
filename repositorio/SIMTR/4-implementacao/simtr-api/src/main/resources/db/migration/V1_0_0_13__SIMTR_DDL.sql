/* Tabela mtrtb009_tipo_documento */  
ALTER TABLE mtrsm001.mtrtb009_tipo_documento ADD COLUMN no_classe_ged varchar(100);
/* Tabela mtrtb001_pessoa_fisica */
ALTER TABLE mtrsm001.mtrtb001_pessoa_fisica ALTER COLUMN ic_estado_civil TYPE integer USING (ic_estado_civil::integer);

-- Comentário do ic_estado_civil na tabela mtrtb001_pessoa_fisica.
COMMENT ON COLUMN mtrsm001.mtrtb001_pessoa_fisica.ic_estado_civil IS 'Atributo utilizado para armazenar o estado civil de pessoas físicas. Pode assumir:
0 - NAO_INFORMADO;
1 - SOLTEIRO;
2 - CASADO;
3 - DIVORCIADO;
4 - SEPARADO_JUDICIALMENTE;
5 - VIUVO;
6 - UNIAO_ESTAVEL;
7 - CASADO_COMUNHAO_TOTAL_BENS;
8 - CASADO_SEM_COMUNHAO_TOTAL_BENS;
9 - CASADO_COMUNHAO_PARCIAL_BENS;';