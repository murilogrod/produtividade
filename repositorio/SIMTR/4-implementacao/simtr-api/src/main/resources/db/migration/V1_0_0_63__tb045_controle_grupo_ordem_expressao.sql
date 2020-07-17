DO $$
BEGIN

/* Tabela 045 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb045_atributo_extracao' AND COLUMN_NAME = 'nu_grupo_atributo'
) THEN 
    ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN nu_grupo_atributo INT4;
    COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.nu_grupo_atributo IS 'Atributo utilizado para indicar o agrupamento que o atributo faz parte. Em determinadas situações é necessário que ao inforar um determinando atributo, obriga-se que outros campos tenham a informação definida, mesmo que estes sejam de carater opicional tendo em vista que este grupo compõe um bloco de informações, Ex: Ao informar uma renda declarada, deve ser informado o tipo de renda, valor e ocupação';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb045_atributo_extracao' AND COLUMN_NAME = 'nu_ordem'
) THEN 
    ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN nu_ordem INT4 NOT NULL DEFAULT 1;
    ALTER TABLE mtr.mtrtb045_atributo_extracao ALTER COLUMN nu_ordem DROP DEFAULT;
    COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.nu_ordem IS 'Atributo utilizado para definir a ordem de exibição dos campos do formulário montado para executar a extação de dados do formulário.';
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb045_atributo_extracao' AND COLUMN_NAME = 'de_expressao'
) THEN 
    ALTER TABLE mtr.mtrtb045_atributo_extracao ADD COLUMN de_expressao TEXT;
    COMMENT ON COLUMN mtr.mtrtb045_atributo_extracao.de_expressao IS 'Atributo que armazena a expressão a ser aplicada pelo Java script para determinar a exposição ou não do campo no formulário montado para extração de dados.';
END IF;

END $$;