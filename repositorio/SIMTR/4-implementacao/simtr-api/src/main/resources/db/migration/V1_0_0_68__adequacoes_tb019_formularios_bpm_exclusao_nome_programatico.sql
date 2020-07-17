DO $$
BEGIN

/* Tabela 019 */
---------------

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' and COLUMN_NAME = 'nu_processo_fase'
) THEN 

    --Renomeia o campo atual para representar o processo fase
    ALTER TABLE mtr.mtrtb019_campo_formulario RENAME COLUMN nu_processo TO nu_processo_fase;

    --Cria o campo para representar o processo gerador de dossiê
    ALTER TABLE mtr.mtrtb019_campo_formulario ADD COLUMN nu_processo INT4;
  
    --Aplica os comentarios das colunas referentes aos campos de ligação com a tabela de processo
    COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.nu_processo IS 'Atributo que representa o processo em que deverá ser apresentado o campo do formulário para preenchimento. Este campo tem relação com o processo que define a estrutura do dossiê de produto. A esta informação deverá ser somada ainda uma das opções a seguir:
    - Processo Fase
    - Tipo de Vinculo de Pessoa
    - Produto
    - Garantia

    Só devem ser associado a este atributo, registros de processos que representam um processo que define um dossiê de produto (ic_dossie = true).

    Com base na observação dessas dois atributos, deverá ser possivel montar formulários dinamicos para cada uma das sitações relacionadas';

    COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.nu_processo_fase IS 'Atributo utilizado para identificar a fase do processo que deve ter o campo de formulario associado. Nesta situação, deve ser montado um formulário dinâmico para cada fase definida com o identificador representado por este campo nos dossiês de produto criado com o processo gerador de dossiê relacionado ao registro.

    Só devem ser associado a este atributo, registros de processos que representam um processo que define uma etapa (fase).';

    --Remove as chaves estrangeiras por ventura existentes com a tabela de processos
    ALTER TABLE mtr.mtrtb019_campo_formulario DROP CONSTRAINT IF EXISTS fk_mtrtb019_mtrtb020;
    ALTER TABLE mtr.mtrtb019_campo_formulario DROP CONSTRAINT IF EXISTS fk_mtrtb019_fk_mtrtb0_mtrtb020;

    --Inclui as novas chaves estrangeiras retivas a tabela de processos
    ALTER TABLE mtr.mtrtb019_campo_formulario
    ADD CONSTRAINT fk_mtrtb019_mtrtb020_01 FOREIGN KEY (nu_processo)
    REFERENCES mtr.mtrtb020_processo (nu_processo)
    ON DELETE RESTRICT ON UPDATE RESTRICT;

    ALTER TABLE mtr.mtrtb019_campo_formulario
    ADD CONSTRAINT fk_mtrtb019_mtrtb020_02 FOREIGN KEY (nu_processo_fase)
    REFERENCES mtr.mtrtb020_processo (nu_processo)
    ON DELETE RESTRICT ON UPDATE RESTRICT;

    --Executa ajustes nos dados das tabelas para setar os valores do atributo que representa o processo gerador de dossiê
    PERFORM mtr.ajusta_processo_tabela19();

    --Altera caracteristica do atributo que representa o processo gerador de dossiê para não permitir valores nulos
    ALTER TABLE mtr.mtrtb019_campo_formulario ALTER COLUMN nu_processo SET NOT NULL;
    ALTER TABLE mtr.mtrtb019_campo_formulario ALTER COLUMN nu_processo_fase DROP NOT NULL;
END IF;


IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND COLUMN_NAME = 'nu_tipo_relacionamento'
) THEN 
    --Inclui novo campo utilizado para criar o vinculo com o tipo de relacionamento
    ALTER TABLE mtr.mtrtb019_campo_formulario ADD COLUMN nu_tipo_relacionamento INT4;
    COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.nu_tipo_relacionamento IS 'Atributo utilizado para identificar o tipo de relacionamento vinculado ao processo que deve ter o campo de formulario associado. Nesta situação, deve ser montado um formulário dinâmico para cada vinculo de pessoa definido com o tipo representado por este campo nos dossiês de produto criado com o processo gerador de dossiê relacionado ao registro.';

    --Inclui a chave estrangeira reativa a tabela de tipo de relacionamento
    ALTER TABLE mtr.mtrtb019_campo_formulario
    ADD CONSTRAINT fk_mtrtb019_mtrtb041 FOREIGN KEY (nu_tipo_relacionamento)
    REFERENCES mtr.mtrtb041_tipo_relacionamento (nu_tipo_relacionamento)
    ON DELETE RESTRICT ON UPDATE RESTRICT;
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND COLUMN_NAME = 'nu_produto'
) THEN 
    --Inclui novo campo utilizado para criar o vinculo com o produto
    ALTER TABLE mtr.mtrtb019_campo_formulario ADD COLUMN nu_produto INT4;
    COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.nu_produto IS 'Atributo utilizado para identificar o produto que deve ter o campo de formulario associado. Nesta situação, deve ser montado um formulário dinâmico para cada produto definido com o identificador representado por este campo nos dossiês de produto criado com o processo gerador de dossiê relacionado ao registro.';

    --Inclui a chave estrangeira reativa a tabela de tipo de garantia
    ALTER TABLE mtr.mtrtb019_campo_formulario
    ADD CONSTRAINT fk_mtrtb019_mtrtb022 FOREIGN KEY (nu_produto)
    REFERENCES mtr.mtrtb022_produto (nu_produto)
    ON DELETE RESTRICT ON UPDATE RESTRICT;
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND COLUMN_NAME = 'nu_garantia'
) THEN 
    --Inclui novo campo utilizado para criar o vinculo com a garantia
    ALTER TABLE mtr.mtrtb019_campo_formulario ADD COLUMN nu_garantia INT4;
    COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.nu_garantia IS 'Atributo utilizado para identificar a garantia que deve ter o campo de formulario associado. Nesta situação, deve ser montado um formulário dinâmico para cada garantia definido com o identificador representado por este campo nos dossiês de produto criado com o processo gerador de dossiê relacionado ao registro.';
    
    --Inclui a chave estrangeira reativa a tabela de tipo de garantia
    ALTER TABLE mtr.mtrtb019_campo_formulario
    ADD CONSTRAINT fk_mtrtb019_mtrtb033 FOREIGN KEY (nu_garantia)
    REFERENCES mtr.mtrtb033_garantia (nu_garantia)
    ON DELETE RESTRICT ON UPDATE RESTRICT;
END IF;

IF NOT EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND COLUMN_NAME = 'nu_identificador_bpm'
) THEN 
    --Inclui novo campo utilizado para criar o vinculo com a garantia
    ALTER TABLE mtr.mtrtb019_campo_formulario ADD COLUMN nu_identificador_bpm INT4;
    COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.nu_identificador_bpm IS 'Atributo utilizado para que a solução de BPM possa capturar o campo de formulário independente do seu identificador serial que pode diferir de acordo com cada ambiente';

    --Criação do indice de unicidade para a identificação do campo junto ao processo de BPM
    DROP INDEX IF EXISTS mtr.ix_mtrtb019_02;
    CREATE INDEX ix_mtrtb019_02 ON mtr.mtrtb019_campo_formulario (
	nu_identificador_bpm
    )
    TABLESPACE mtrtsix000;   
END IF;

IF EXISTS(
    SELECT * FROM information_schema.columns 
    WHERE TABLE_SCHEMA = 'mtr' AND TABLE_NAME = 'mtrtb019_campo_formulario' AND COLUMN_NAME = 'no_campo'
) THEN 
    --Remove campo em desuso
    ALTER TABLE mtr.mtrtb019_campo_formulario DROP COLUMN no_campo;
END IF;

/*===============================*/
/* EXCLUSAO DAS FUNCTIONS DE DML */ 
/*===============================*/
DROP FUNCTION IF EXISTS mtr.ajusta_processo_tabela19();

/*========================================*/
/* INCLUSAO DE INDICES DE UNICIDADE TB007 */ 
/*========================================*/
DROP INDEX IF EXISTS mtr.ix_mtrtb007_01;
CREATE INDEX ix_mtrtb007_01 ON mtr.mtrtb007_atributo_documento (
    nu_documento,
    de_atributo
)
TABLESPACE mtrtsix000;

END $$;
