ALTER TABLE mtr.mtrtb009_tipo_documento DROP COLUMN IF EXISTS no_avatar;
ALTER TABLE mtr.mtrtb009_tipo_documento DROP COLUMN IF EXISTS co_rgb_box;
ALTER TABLE mtr.mtrtb053_vinculacao_checklist DROP COLUMN IF EXISTS ic_verificacao_previa;
ALTER TABLE mtr.mtrtb054_checklist_associado DROP COLUMN IF EXISTS ic_verificacao_previa;

/* Tabela 009 */
---------------
ALTER TABLE mtr.mtrtb009_tipo_documento ADD COLUMN no_avatar VARCHAR(255) NOT NULL DEFAULT 'glyphicon glyphicon-picture';
ALTER TABLE mtr.mtrtb009_tipo_documento ALTER COLUMN no_avatar DROP DEFAULT;
COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.no_avatar IS 'Atributo utilizado para armazenar o nome do avatar que será disponibilizado no pacote da ineterface grafica para montagem e apresentação das filas de extração de dados dos documentos associados a esta tipologia.';

ALTER TABLE mtr.mtrtb009_tipo_documento ADD COLUMN co_rgb_box VARCHAR(7) NOT NULL DEFAULT '#FFCC00';
ALTER TABLE mtr.mtrtb009_tipo_documento ALTER COLUMN co_rgb_box DROP DEFAULT;
COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.co_rgb_box IS 'Atributo utilizado para armazenar a cor que deverá ser atribuida a box apresentada na interface grafica para montagem e apresentação das filas de extração de dados dos documentos associados a esta tipologia.
A cor deve ser definida em hexadecimal representando o padrão RGB.';

/* Tabela 012 */
---------------
ALTER TABLE mtr.mtrtb012_tipo_situacao_dossie DROP COLUMN IF EXISTS ic_resumo;
ALTER TABLE mtr.mtrtb012_tipo_situacao_dossie DROP COLUMN IF EXISTS ic_fila_tratamento;
ALTER TABLE mtr.mtrtb012_tipo_situacao_dossie DROP COLUMN IF EXISTS ic_produtividade;

/* Tabela 053 */
---------------
ALTER TABLE mtr.mtrtb053_vinculacao_checklist ADD COLUMN ic_verificacao_previa BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE mtr.mtrtb053_vinculacao_checklist ALTER COLUMN ic_verificacao_previa DROP DEFAULT;
COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.co_rgb_box IS 'Atributo utilizado para indicar que o checklist em questão trata-se de um checklist previo.
Apenas checklists não documentais devem ser apontados como previos.
No ato do tratamento, esse checklist será apresentado primeiramente, e os demais só serão apresentados, caso este checklist seja aprovado na totalidade.
Caso o checklist previo possua algum item não aprovado na verificação, a API poderá receber a execução do tratamento registrando verificação apenas para o checklist previo e desconsiderando a obrigatoriedade das demais verificações.';

/* Tabela 054 */
---------------
ALTER TABLE mtr.mtrtb054_checklist_associado ADD COLUMN ic_verificacao_previa BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE mtr.mtrtb054_checklist_associado ALTER COLUMN ic_verificacao_previa DROP DEFAULT;
COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.co_rgb_box IS 'Atributo utilizado para indicar se o checklist foi associado com oum checklist previo ou não. 
Apenas checklists não documentais devem ser associados como previos.';



/* INDICES DE UNICIDADE*/
------------------------
--Ajustando indices de unicidade com campos presença de campos null
--Executa DROP dos indices existentes sem consideração de clausula WHERE
DROP INDEX IF EXISTS mtr.ix_mtrtb053_01, mtr.ix_mtrtb053_02, mtr.ix_mtrtb053_03;

--Cria o indice de unicidade para o tipo de documento na determinada fase do processo
CREATE UNIQUE INDEX ix_mtrtb053_01 ON mtr.mtrtb053_vinculacao_checklist USING BTREE (
    nu_processo_dossie,
    nu_processo_fase,
    nu_tipo_documento,
    dt_revogacao
)
TABLESPACE mtrtsix000
WHERE nu_tipo_documento IS NOT NULL AND nu_funcao_documental IS NULL;

--Cria o indice de unicidade para função documental na determinada fase do processo
CREATE UNIQUE INDEX ix_mtrtb053_02 ON mtr.mtrtb053_vinculacao_checklist USING BTREE (
    nu_processo_dossie,
    nu_processo_fase,
    nu_funcao_documental,
    dt_revogacao
)
TABLESPACE mtrtsix000
WHERE nu_funcao_documental IS NOT NULL AND nu_tipo_documento IS NULL;

--Cria o indice de unicidade para o checklist não documental na determinada fase do processo
CREATE UNIQUE INDEX ix_mtrtb053_03 ON mtr.mtrtb053_vinculacao_checklist USING BTREE (
    nu_processo_dossie,
    nu_processo_fase,
    nu_checklist,
    dt_revogacao
)
TABLESPACE mtrtsix000
WHERE nu_funcao_documental IS NULL AND nu_tipo_documento IS NULL;

--Cria o indice de unicidade para impedir que mais de um checklist seja associado como previo ao dossiê de produto na mesma fase
CREATE UNIQUE INDEX ix_mtrtb054_01 ON mtr.mtrtb054_checklist_associado (
    nu_dossie_produto,
    nu_processo_fase,
    ic_verificacao_previa
)
TABLESPACE mtrtsix000
WHERE ic_verificacao_previa = true;