-- Tabela mtrtb009_tipo_documento
ALTER TABLE mtrsm001.mtrtb009_tipo_documento ADD COLUMN de_tags TEXT;
COMMENT ON COLUMN mtrsm001.mtrtb009_tipo_documento.de_tags IS 'Atributo utilizado para realizar a vinculação de TAGS pre definidaas ao tipo documental. Essas Tasgs devem ser sepradas por ";" (ponto e virgula) serão utilizadas para localizar documentos que estejam associados ao tipo que tenha relação com TAG pesquisada.

Ex: 
Tipo de Documento            |  TAGS
-------------------------------------------------------------------------------------
Comunicado Interno           | CI;Comunicados
Certidão Negativa Receita | Certidões;Certidão;Certidao
';


-- Tabela mtrtb046_processo_adm
ALTER TABLE mtrsm001.mtrtb046_processo_adm RENAME COLUMN nu_unidade_pregao TO nu_unidade_contratacao;
ALTER TABLE mtrsm001.mtrtb046_processo_adm ADD COLUMN co_protocolo_siclg CHARACTER VARYING(255);
ALTER TABLE mtrsm001.mtrtb046_processo_adm ADD COLUMN nu_unidade_demandante INTEGER NOT NULL DEFAULT 0;
ALTER TABLE mtrsm001.mtrtb046_processo_adm ALTER COLUMN nu_unidade_demandante DROP DEFAULT;

COMMENT ON COLUMN mtrsm001.mtrtb046_processo_adm.co_protocolo_siclg IS 'Atributo utilizado para armanezar o numero de protocolo atribuido pelo SICLG no momento da criação do fluxo do processo.

Esta informação servirá como identificação negocial do registro do processo apenas de carater informativo.';
COMMENT ON COLUMN mtrsm001.mtrtb046_processo_adm.nu_unidade_demandante IS 'Atributo utilizado para armazenar a unidade CAIXA demaqndante da solicitação que originou o processo relativo ao registro.';

-- Tabela mtrtb047_contrato_adm
ALTER TABLE mtrsm001.mtrtb047_contrato_adm ADD COLUMN nu_unidade_operacional INTEGER NOT NULL DEFAULT 0;
ALTER TABLE mtrsm001.mtrtb047_contrato_adm ALTER COLUMN nu_unidade_operacional DROP DEFAULT;

COMMENT ON COLUMN mtrsm001.mtrtb047_contrato_adm.nu_unidade_operacional IS 'Atributo utilizado para armazenar a unidade CAIXA responsável pela operacionalização do contrato.';

-- Tabela mtrtb048_apenso_adm
ALTER TABLE mtrsm001.mtrtb048_apenso_adm ALTER COLUMN co_protocolo_siclg DROP NOT NULL;

-- Tabela mtrtb049_documento_adm
ALTER TABLE mtrsm001.mtrtb049_documento_adm RENAME COLUMN de_descricao TO de_documento_adm;
ALTER TABLE mtrsm001.mtrtb049_documento_adm ADD COLUMN de_justificativa_substituicao TEXT;

COMMENT ON COLUMN mtrsm001.mtrtb049_documento_adm.de_justificativa_substituicao IS 'Atributo utilizado para armazenar a justificativa utilizada quando da substituição de um documento adminstrativo.

Este campo deverá estar preenchi sempre que o atributo nu_documento_substituto estiver definido.';