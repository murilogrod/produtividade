/* Aplicação de Sequences - Parte 01 de 02 */
--------------------------------------------
CREATE SEQUENCE mtr.mtrsq051_checklist;
CREATE SEQUENCE mtr.mtrsq052_apontamento;
CREATE SEQUENCE mtr.mtrsq053_vinculacao_checklist;

/* Tabela 051 */
---------------

CREATE TABLE mtr.mtrtb051_checklist (
   nu_checklist             INT4               NOT NULL,
   nu_versao                INT4                 NOT NULL,
   no_checklist             VARCHAR(150)         NOT NULL,
   nu_unidade_responsavel   INT4                 NOT NULL,
   CONSTRAINT pk_mtrtb051 PRIMARY KEY (nu_checklist)
);

COMMENT ON TABLE mtr.mtrtb051_checklist IS
'Tabela utilizada para agrupar os itens de verificação compondo uma lista de elementos a serem verificados. Esta junção permitirá ao operador identificar todos os itens de verificação que serão necessarios avaliação ao elemento no ato do tratamento do dossiê de produto.';

COMMENT ON COLUMN mtr.mtrtb051_checklist.nu_checklist IS
'Atributo que representa a chave primaria da entidade';

COMMENT ON COLUMN mtr.mtrtb051_checklist.nu_versao IS
'Campo de controle das versões do registro para viabilizar a concorrencia otimista';

COMMENT ON COLUMN mtr.mtrtb051_checklist.no_checklist IS
'Nome utilizado para identificação do checklist';

COMMENT ON COLUMN mtr.mtrtb051_checklist.nu_unidade_responsavel IS
'Codigo de identificação da unidade CAIXA responsavel pela manutenção dos apontamentos do referido checklist.';

/* Tabela 052 */
---------------
CREATE TABLE mtr.mtrtb052_apontamento (
   nu_apontamento       INT8               NOT NULL,
   nu_versao            INT4                 NOT NULL,
   nu_checklist         INT4                 NOT NULL,
   no_apontamento       VARCHAR(500)         NOT NULL,
   de_apontamento       TEXT                 NULL,
   CONSTRAINT pk_mtrtb052 PRIMARY KEY (nu_apontamento)
);

COMMENT ON TABLE mtr.mtrtb052_apontamento IS
'Tabela utilizada para armazenar os registros que representam os apontamentos na estrutura de checklists a ser definida para utilização no fluxo de verificação utilizado na plataforma.A junção destes itens deverá formar o conceito de checklist a ser aplicado junto ao elemento desejado.';

COMMENT ON COLUMN mtr.mtrtb052_apontamento.nu_apontamento IS
'Atributo que representa a chave primaria da entidade';

COMMENT ON COLUMN mtr.mtrtb052_apontamento.nu_versao IS
'Campo de controle das versões do registro para viabilizar a concorrencia otimista';

COMMENT ON COLUMN mtr.mtrtb052_apontamento.nu_checklist IS
'Atributo utilizado para identificar o checklist de associação do item de verificação.';

COMMENT ON COLUMN mtr.mtrtb052_apontamento.no_apontamento IS
'Nome do item de verificação. Deve ser utilizado como um titulo a ser apresentado indicando a checagem que deverá ser realizada para atendimento ao item. Este valor deverá ser apresentado na composição do checklist.';

COMMENT ON COLUMN mtr.mtrtb052_apontamento.de_apontamento IS
'Descrição do que deve ser verificado. Neste item podem ser armazenadas instruções indicando a forma de como o item deve ser verificado propondo inclusive um pequeno fluxo descritivo no modelo de "passo a passo"';

/* Tabela 053 */
---------------
CREATE TABLE mtr.mtrtb053_vinculacao_checklist (
   nu_vinculacao_checklist  INT8    NOT NULL,
   nu_versao                INT4    NOT NULL,
   nu_processo_dossie       INT4    NOT NULL,
   nu_processo_fase         INT4    NOT NULL,
   nu_tipo_documento        INT4    NULL,
   nu_funcao_documental     INT4    NULL,
   nu_checklist             INT4    NOT NULL,
   dt_revogacao             DATE    NOT NULL,
   CONSTRAINT pk_mtrtb053 PRIMARY KEY (nu_vinculacao_checklist)
);

COMMENT ON TABLE mtr.mtrtb053_vinculacao_checklist IS
'Tabela responsavel pelo armazenamento das informações referentes a asociação de checklists aplicaveis no fluxo de tratamento do SIMTR.

Essa situação permitirá aplicar um checklist distinto para cada tipo de documento e também diversos checklists a serem aplicados ao mesmo documento com apontamentos distintos a depender da fase de analise do documento.

Para cada documento analisado serão aplicados checklists identificados sempre pela seu tipo definido ou pela sua função documental exercida, mas nunca pelo dois.

Caso ocorra uma situação de concorrência por consequencia de atendimento a dois registros devido a uma parametrização que atenda ao tipo e a função ao qual este tipo esteja associada, deverá ser prevalecido a utilização do checklist de menor granularidade, ou seja, o do tipo de documento.

Ex:

nu_processo | nu_tipo_documento | nu_funcao_documental | nu_checklist_vigente
         5          |            null                |                  1                    |         123                          (Aplicado a documentos de identificação)
         5          |              4                 |                 null                  |         456                          (Aplicado a CNH)

Caso esta situação se apresente, qualquer documento de identificação apresentado no processo 5 deverá analisado com o checklist 123, mas caso uma CNH seja apresentada deverá ser analisada com o checklist 456.

A coluna de processo fase foi ocultada do exemplo para fins de ilustração, mas a relação envolve a mesma.

OBS: Sempre que for definido um tipo documental, a função documental deverá ser nula e vice versa, porém os dois campos não deverão nunca ser mutuamente nulos. Sempre um deles deverá estar preenchido.';

COMMENT ON COLUMN mtr.mtrtb053_vinculacao_checklist.nu_vinculacao_checklist IS
'Atributo que representa a chave primaria da entidade.';

COMMENT ON COLUMN mtr.mtrtb053_vinculacao_checklist.nu_versao IS
'Campo de controle das versões do registro para viabilizar a concorrencia otimista';

COMMENT ON COLUMN mtr.mtrtb053_vinculacao_checklist.nu_processo_dossie IS
'Atributo que representa o processo de geração do dossiiê relacionado com a configuração do checklist.
No ato da identificação da captura do checklist para tratamento pelo operador, este campo deverá ser comparado com o atributo nu_processo_dossie do dossiê de produto em analise.';

COMMENT ON COLUMN mtr.mtrtb053_vinculacao_checklist.nu_processo_fase IS
'Atributo que representa a fase do processo no ciclo de vida do dossIê do produto relacionado com a configuração do checklist.
No ato da identificação da captura do checklist para tratamento pelo operador, este campo deverá ser comparado com o atributo nu_processo_fase do dossiê de produto em analise.';

COMMENT ON COLUMN mtr.mtrtb053_vinculacao_checklist.nu_tipo_documento IS
'Atributo utilizado para identificar o checklist aplicado a um tipo de documento definido para o documento analisado. Quando o documento apresentado para o processo x fase representado no registro que atenda a este tipo de documento deverá ser analisado pelo checklist definido em "nu_checklist_vigente"';

COMMENT ON COLUMN mtr.mtrtb053_vinculacao_checklist.nu_funcao_documental IS
'Atributo utilizado para identificar o checklist aplicado a uma função documental de um documento analisado. Qualquer documento apresentado para o processo x fase representado no registro que atenda a esta funação documental deverá ser analisado pelo checklist definido em "nu_checklist_vigente"';

COMMENT ON COLUMN mtr.mtrtb053_vinculacao_checklist.nu_checklist IS
'Atributo utilizado para identificar o checklist a ser aplicado no elemento em verificação.';

COMMENT ON COLUMN mtr.mtrtb053_vinculacao_checklist.dt_revogacao IS
'Atributo utilizado para permitir o planejamento previo de mudanças de aplicação de chaklists para o documento.

Este atributo deverá sempre estar preenchido e armazena a data limite para aplicação do checklist associado ao registro.

Caso não haja data de revogação definida, deverá ser cadastrada uma data futura bem distante, por exemplo 31/12/2100';


/* INDICES DE UNICIDADE*/
------------------------
CREATE UNIQUE INDEX ix_mtrtb051_01 ON mtr.mtrtb051_checklist (
no_checklist,
nu_unidade_responsavel
);

CREATE UNIQUE INDEX ix_mtrtb053_01 ON mtr.mtrtb053_vinculacao_checklist (
nu_processo_dossie,
nu_processo_fase,
nu_tipo_documento,
dt_revogacao
);

CREATE UNIQUE INDEX ix_mtrtb053_02 ON mtr.mtrtb053_vinculacao_checklist (
nu_processo_dossie,
nu_processo_fase,
nu_funcao_documental,
dt_revogacao
);

CREATE UNIQUE INDEX ix_mtrtb053_03 ON mtr.mtrtb053_vinculacao_checklist (
nu_processo_dossie,
nu_processo_fase,
nu_checklist,
dt_revogacao
);

/* CHAVES ESTRANGEIRAS */
------------------------
ALTER TABLE mtr.mtrtb052_apontamento
   ADD CONSTRAINT fk_mtrtb052_mtrtb051 FOREIGN KEY (nu_checklist)
   REFERENCES mtr.mtrtb051_checklist(nu_checklist)
      ON DELETE RESTRICT 
      ON UPDATE RESTRICT
      NOT DEFERRABLE;

ALTER TABLE mtr.mtrtb053_vinculacao_checklist
   ADD CONSTRAINT fk_mtrtb053_mtrtb009 FOREIGN KEY (nu_tipo_documento)
      REFERENCES mtr.mtrtb009_tipo_documento (nu_tipo_documento)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE mtr.mtrtb053_vinculacao_checklist
   ADD CONSTRAINT fk_mtrtb053_mtrtb010 FOREIGN KEY (nu_funcao_documental)
      REFERENCES mtr.mtrtb010_funcao_documental (nu_funcao_documental)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE mtr.mtrtb053_vinculacao_checklist
   ADD CONSTRAINT fk_mtrtb053_mtrtb020_01 FOREIGN KEY (nu_processo_dossie)
      REFERENCES mtr.mtrtb020_processo (nu_processo)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE mtr.mtrtb053_vinculacao_checklist
   ADD CONSTRAINT fk_mtrtb053_mtrtb020_02 FOREIGN KEY (nu_processo_fase)
      REFERENCES mtr.mtrtb020_processo (nu_processo)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE mtr.mtrtb053_vinculacao_checklist
   ADD CONSTRAINT fk_mtrtb053_mtrtb052 FOREIGN KEY (nu_checklist)
      REFERENCES mtr.mtrtb051_checklist (nu_checklist)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

/* Aplicação de Sequences - Parte 02 de 02 */
--------------------------------------------

ALTER TABLE mtr.mtrtb051_checklist 		ALTER COLUMN nu_checklist 	 	SET DEFAULT nextval('mtr.mtrsq051_checklist'::regclass);
ALTER TABLE mtr.mtrtb052_apontamento 		ALTER COLUMN nu_apontamento 		SET DEFAULT nextval('mtr.mtrsq052_apontamento'::regclass);
ALTER TABLE mtr.mtrtb053_vinculacao_checklist   ALTER COLUMN nu_vinculacao_checklist 	SET DEFAULT nextval('mtr.mtrsq053_vinculacao_checklist'::regclass);
