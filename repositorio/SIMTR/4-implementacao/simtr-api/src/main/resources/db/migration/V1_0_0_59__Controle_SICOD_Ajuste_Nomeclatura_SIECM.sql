DO $$
BEGIN

/* Tabela 001 */
---------------

ALTER TABLE mtr.mtrtb001_dossie_cliente DROP COLUMN IF EXISTS dt_criacao;
ALTER TABLE mtr.mtrtb001_dossie_cliente DROP COLUMN IF EXISTS nu_dire_criacao;
ALTER TABLE mtr.mtrtb001_dossie_cliente DROP COLUMN IF EXISTS nu_sr_criacao;
ALTER TABLE mtr.mtrtb001_dossie_cliente DROP COLUMN IF EXISTS nu_unidade_criacao;

/* Tabela 009 */
---------------
IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE table_schema = 'mtr' and table_name='mtrtb009_tipo_documento' and column_name='no_classe_ged'
) THEN
    ALTER TABLE mtr.mtrtb009_tipo_documento RENAME COLUMN no_classe_ged TO no_classe_siecm;
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE table_schema = 'mtr' and table_name='mtrtb009_tipo_documento' and column_name='ic_validacao_sicod'
) THEN 
    ALTER TABLE mtr.mtrtb009_tipo_documento ADD COLUMN ic_validacao_sicod BOOLEAN NOT NULL DEFAULT false;
END IF;
ALTER TABLE mtr.mtrtb009_tipo_documento ALTER COLUMN ic_validacao_sicod DROP DEFAULT;
COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.ic_validacao_sicod IS 'Indica se o documento pode ser enviado a avaliação de validade documental com o SICOD.';

COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.ic_validacao_documental IS 'Indica se o documento pode ser enviado a avaliação de validade documental externa.';

/* Tabela 045 */
---------------
IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE table_schema = 'mtr' and table_name='mtrtb045_atributo_extracao' and column_name='no_atributo_ged'
) THEN
    ALTER TABLE mtr.mtrtb045_atributo_extracao RENAME COLUMN no_atributo_ged TO no_atributo_siecm;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE table_schema = 'mtr' and table_name='mtrtb045_atributo_extracao' and column_name='ic_tipo_ged'
) THEN
    ALTER TABLE mtr.mtrtb045_atributo_extracao RENAME COLUMN ic_tipo_ged TO ic_tipo_siecm;
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE table_schema = 'mtr' and table_name='mtrtb045_atributo_extracao' and column_name='ic_obrigatorio_ged'
) THEN
    ALTER TABLE mtr.mtrtb045_atributo_extracao RENAME COLUMN ic_obrigatorio_ged TO ic_obrigatorio_siecm;
END IF;

ALTER TABLE mtr.mtrtb045_atributo_extracao DROP COLUMN IF EXISTS ic_ged;

ALTER TABLE mtr.mtrtb045_atributo_extracao ALTER COLUMN ic_tipo_geral TYPE VARCHAR(20);
ALTER TABLE mtr.mtrtb045_atributo_extracao ALTER COLUMN ic_tipo_sicli TYPE VARCHAR(20);

COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.no_atributo_siecm IS 'Atributo utilizado para definir o nome do atributo do SIECM que tem relação com o atributo do documento.';
COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.ic_obrigatorio_siecm IS 'Atributo utilizado para indicar se esta informação junto ao SIECM tem cunho obrigatorio.';
COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.ic_tipo_siecm IS 'Atributo utilizado para indicar qual o tipo de atributo definido junto a classe documental no SIECM. Pode assumir os seguintes valores:
- BOOLEAN
- STRING
- LONG
- DATE';

COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.ic_tipo_campo IS 'Atributo utilizado para indicar o tipo de campo que deverá ser utilizado pela interface para os casos de captura da informação quando inserida de forma manual.
Exemplos validos para este atributo são:
- TEXT
- PASSWORD
- DATE
- DATE_TIME
- TIME
- EMAIL
- NUMBER
- CPF
- CNPJ';

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE table_schema = 'mtr' and table_name='mtrtb045_atributo_extracao' and column_name='no_atributo_sicod'
) THEN 
    ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN no_atributo_sicod VARCHAR(100);
END IF;
COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.no_atributo_sicod IS 'Atributo utilizado para definir o nome do atributo do SICOD que tem relação com o atributo do documento.';

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE table_schema = 'mtr' and table_name='mtrtb045_atributo_extracao' and column_name='ic_tipo_sicod'
) THEN 
    ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN ic_tipo_sicod VARCHAR(20);
END IF;
COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.ic_tipo_sicod IS 'Atributo utilizado para indicar qual o tipo de atributo definido junto ao objeto a ser enviado na atualização de dados do SICOD. Pode assumir os seguintes valores:
- BOOLEAN
- STRING
- LONG
- DATE
- DECIMAL';

END $$;