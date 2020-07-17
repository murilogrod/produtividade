-- Execução do flyway #53.


-- INICIO FLYWAY #53
ALTER TABLE mtr.mtrtb053_vinculacao_checklist DROP COLUMN IF EXISTS ic_verificacao_previa;
ALTER TABLE mtr.mtrtb054_checklist_associado DROP COLUMN IF EXISTS ic_verificacao_previa;

/* Tabela 051 */
---------------
ALTER TABLE mtr.mtrtb051_checklist ADD COLUMN ic_verificacao_previa BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE mtr.mtrtb051_checklist ALTER COLUMN ic_verificacao_previa DROP DEFAULT;
COMMENT ON COLUMN mtr.mtrtb051_checklist.ic_verificacao_previa IS 'Atributo utilizado para indicar que o checklist em questão trata-se de um checklist previo.
No ato do tratamento, esse checklist será apresentado primeiramente, e os demais só serão apresentados caso este checklist seja aprovado na totalidade.
Caso o checklist previo possua algum item não aprovado na verificação, a API poderá receber a execução do tratamento registrando verificação apenas para o checklist previo e desconsiderando a obrigatoriedade das demais verificações.';


/* AJUSTE DE COMENTARIOS ENVIADOS INCORRETAMENTE A COLUNA */
COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.co_rgb_box IS 'Atributo utilizado para armazenar a cor que deverá ser atribuida a box apresentada na interface grafica para montagem e apresentação das filas de extração de dados dos documentos associados a esta tipologia.
A cor deve ser definida em hexadecimal representando o padrão RGB.';


/* INDICES DE UNICIDADE*/
------------------------
--Ajustando indices de unicidade com campos presença de campos null
--Executa DROP dos indices existentes sem consideração de clausula WHERE
DROP INDEX IF EXISTS mtr.ix_mtrtb054_01;

--Cria o indice de unicidade para impedir que o mesmo checklist seja associado mais de uma vez ao dossiê de produto na mesma fase
CREATE UNIQUE INDEX ix_mtrtb054_01 ON mtr.mtrtb054_checklist_associado (
    nu_checklist,
    nu_dossie_produto,
    nu_processo_fase
)
TABLESPACE mtrtsix000
WHERE nu_instancia_documento IS NULL;

--Cria o indice de unicidade para impedir que mais de um checklist seja associado a mesma instância de documento no mesmo dossiê de produto na mesma fase
CREATE UNIQUE INDEX ix_mtrtb054_02 ON mtr.mtrtb054_checklist_associado (
    nu_dossie_produto,
    nu_processo_fase,
    nu_instancia_documento
)
TABLESPACE mtrtsix000
WHERE nu_instancia_documento IS NOT NULL;
-- FIM FLYWAY #53