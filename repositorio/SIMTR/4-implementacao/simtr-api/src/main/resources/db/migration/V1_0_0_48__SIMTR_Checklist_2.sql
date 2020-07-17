
/* ALTERAÇÕES DE NOMES DE CHAVE ESTRANGEIRA */
---------------------------------------------
--ALTER TABLE MTR.mtrtb053_vinculacao_checklist RENAME CONSTRAINT fk_mtrtb053_mtrtb052 TO fk_mtrtb053_mtrtb051;

/* REMOVE TABELAS E SEQUENCES PRE EXISTENTES */
----------------------------------
DROP TABLE IF EXISTS mtr.mtrtb056_parecer;
DROP TABLE IF EXISTS mtr.mtrtb055_verificacao;
DROP TABLE IF EXISTS mtr.mtrtb054_checklist_associado;
DROP TABLE IF EXISTS mtr.mtrtb053_vinculacao_checklist;
DROP TABLE IF EXISTS mtr.mtrtb052_apontamento;
DROP TABLE IF EXISTS mtr.mtrtb051_checklist;

DROP SEQUENCE IF EXISTS mtr.mtrsq051_checklist;
DROP SEQUENCE IF EXISTS mtr.mtrsq052_apontamento;
DROP SEQUENCE IF EXISTS mtr.mtrsq053_vinculacao_checklist;
DROP SEQUENCE IF EXISTS mtr.mtrsq054_checklist_associado;
DROP SEQUENCE IF EXISTS mtr.mtrsq055_verificacao;
DROP SEQUENCE IF EXISTS mtr.mtrsq056_parecer;


/* Criacao de Sequences */
-------------------------
CREATE SEQUENCE mtr.mtrsq051_checklist;
CREATE SEQUENCE mtr.mtrsq052_apontamento;
CREATE SEQUENCE mtr.mtrsq053_vinculacao_checklist;
CREATE SEQUENCE mtr.mtrsq054_checklist_associado;
CREATE SEQUENCE mtr.mtrsq055_verificacao;
CREATE SEQUENCE mtr.mtrsq056_parecer;

/* Tabela 051 */
---------------
CREATE TABLE mtr.mtrtb051_checklist (
   nu_checklist             INT4            NOT NULL DEFAULT nextval('mtr.mtrsq051_checklist'::regclass),
   nu_versao                INT4            NOT NULL,
   no_checklist             VARCHAR(150)    NOT NULL,
   nu_unidade_responsavel   INT4            NOT NULL,
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
    nu_apontamento      INT8            NOT NULL DEFAULT nextval('mtr.mtrsq052_apontamento'::regclass),
    nu_versao           INT4            NOT NULL,
    nu_checklist        INT4            NOT NULL,
    no_apontamento      VARCHAR(500)    NOT NULL,
    de_apontamento      TEXT            NULL,
    de_orientacao       TEXT            NOT NULL,
    ic_informacao       BOOLEAN         NOT NULL,
    ic_rejeicao         BOOLEAN         NOT NULL,
    ic_seguranca        BOOLEAN         NOT NULL,
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

COMMENT ON COLUMN mtr.mtrtb052_apontamento.de_orientacao IS
'Atributo utilizado para armazenar a orientação a ser encaminhada para a unidade no caso de negativa emitida pelo operador para o apontamento em questão,';

COMMENT ON COLUMN mtr.mtrtb052_apontamento.ic_informacao IS
'Atributo utilizado para indicar que no caso do apontamento em questão seja sinalizado negativamente pelo operador, indica que o elemento avaliado será questionado e portanto o dossiê de produto deverá ser encaminhado para situação "Pendente Informação".';

COMMENT ON COLUMN mtr.mtrtb052_apontamento.ic_rejeicao IS
'Atributo utilizado para indicar que no caso do apontamento em questão seja sinalizado negativamente pelo operador, indica que o elemento avaliado será rejeitado e portanto o dossiê de produto deverá ser encaminhado para situação "Pendente Informação" e o elemento substituido.';

COMMENT ON COLUMN mtr.mtrtb052_apontamento.ic_seguranca IS
'Atributo utilizado para indicar que no caso do apontamento em questão seja sinalizado negativamente pelo operador, indica que o elemento possui uma suspeita de fraude e portanto o dossiê de produto deverá ser encaminhado para situação "Pendente Segurança".';

/* Tabela 053 */
---------------
CREATE TABLE mtr.mtrtb053_vinculacao_checklist (
   nu_vinculacao_checklist  INT8    NOT NULL DEFAULT nextval('mtr.mtrsq053_vinculacao_checklist'::regclass),
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


/* Tabela 054 */
---------------
CREATE TABLE mtr.mtrtb054_checklist_associado (
   nu_checklist_associado   BIGINT  NOT NULL DEFAULT nextval('mtr.mtrsq054_checklist_associado'::regclass),
   nu_versao                INT     NOT NULL,
   nu_checklist             INT     NOT NULL,
   nu_instancia_documento   BIGINT  NULL,
   nu_dossie_produto        BIGINT  NOT NULL,
   nu_processo_fase         INT     NOT NULL,
   CONSTRAINT pk_mtrtb054 PRIMARY KEY (nu_checklist_associado)
   USING INDEX TABLESPACE mtrtsix000
)
TABLESPACE mtrtsdt000;

COMMENT ON TABLE mtr.mtrtb054_checklist_associado IS
'Tabela utilizada para registrar o checklist qe foi associado a uma instancia de documento ou ao dossiê de prpoduto para verificação de uma determinada fase a ser executado por uma operação de tratamento.';

COMMENT ON COLUMN mtr.mtrtb054_checklist_associado.nu_checklist_associado IS
'Atributo que representa a chave primaria da entidade';

COMMENT ON COLUMN mtr.mtrtb054_checklist_associado.nu_versao IS
'Campo de controle das versões do registro para viabilizar a concorrencia otimista';

COMMENT ON COLUMN mtr.mtrtb054_checklist_associado.nu_checklist IS
'Atributo que o identificador do checklist vinculado a associação';

COMMENT ON COLUMN mtr.mtrtb054_checklist_associado.nu_instancia_documento IS
'Atributo que o identificador da instancia de documento vinculado a associação para os casos de aplicação do checklist a um determinado documento. Caso este atributo seja nulo, significa que o checklist foi utilizado como um checklist não documental aplicado na fase especificada para o dosiê de produto relacionado.';

COMMENT ON COLUMN mtr.mtrtb054_checklist_associado.nu_dossie_produto IS
'Atributo que representa o identificador do dossiê de produto vinculado a associação realizada para a verificação do checklist.';

COMMENT ON COLUMN mtr.mtrtb054_checklist_associado.nu_processo_fase IS
'Atributo que representa o identificador do processo fase vinculado a associação realizada para a verificação do checklist.';


/* Tabela 055 */
---------------
CREATE TABLE mtr.mtrtb055_verificacao (
   nu_verificacao           BIGINT      NOT NULL DEFAULT nextval('mtr.mtrsq055_verificacao'::regclass),
   nu_versao                INT         NOT NULL,
   nu_checklist_associado   BIGINT      NULL,
   ts_verificacao           TIMESTAMP   NOT NULL,
   co_matricula             VARCHAR(7)  NOT NULL,
   nu_unidade               INT         NOT NULL,
   ic_verificacao_aprovada  BOOL        NOT NULL,
   ts_contestacao           TIMESTAMP   NULL,
   co_matricula_contestacao VARCHAR(7)  NULL,
   ts_revisao               TIMESTAMP   NULL,
   co_matricula_revisao     VARCHAR(7)  NULL,
   nu_unidade_revisao       INT         NULL,
   ic_revisao_aprovada      BOOL        NULL,
   CONSTRAINT pk_mtrtb055_verificacao PRIMARY KEY (nu_verificacao)
   USING INDEX TABLESPACE mtrtsix000
)
TABLESPACE mtrtsdt000;

COMMENT ON TABLE mtr.mtrtb055_verificacao IS
'Tabela responsável por armazenar as verificações realizadas no checklist associado.';

COMMENT ON COLUMN mtr.mtrtb055_verificacao.nu_verificacao IS
'Atributo que representa a chave primaria da entidade';

COMMENT ON COLUMN mtr.mtrtb055_verificacao.nu_versao IS
'Campo de controle das versões do registro para viabilizar a concorrencia otimista';

COMMENT ON COLUMN mtr.mtrtb055_verificacao.ts_verificacao IS
'Data e Hora de realizaçãpo da verificação';

COMMENT ON COLUMN mtr.mtrtb055_verificacao.co_matricula IS
'Matricula do operador responsavel pela verificação realizada.';

COMMENT ON COLUMN mtr.mtrtb055_verificacao.nu_unidade IS
'Atributo utilizado para armazenar a unidade do usuário responsável pela realização da verificação.';

COMMENT ON COLUMN mtr.mtrtb055_verificacao.ic_verificacao_aprovada IS
'Atributo utilizado para armazenar o resultado final da verificação indicando se esta foi aprovada (true) ou reprovada (false)';

COMMENT ON COLUMN mtr.mtrtb055_verificacao.ts_contestacao IS
'Atributo utilizado para armazenar a data e hora da realização da contestação';

COMMENT ON COLUMN mtr.mtrtb055_verificacao.co_matricula_contestacao IS
'Atributo utilizado para armazenar a matricula do usuário que realizou a contestação da verificação.';

COMMENT ON COLUMN mtr.mtrtb055_verificacao.ts_revisao IS
'Data e Hora de realização da revisão após a contestação';

COMMENT ON COLUMN mtr.mtrtb055_verificacao.co_matricula_revisao IS
'Matricula do operador responsavel pela revisão da verificação.';

COMMENT ON COLUMN mtr.mtrtb055_verificacao.nu_unidade_revisao IS
'Atributo utilizado para armazenar a unidade do usuário responsável pela revisão da verificação.';

COMMENT ON COLUMN mtr.mtrtb055_verificacao.ic_revisao_aprovada IS
'Atributo utilizado para identificar se a verificação foi aprovada após a revisão provocada pela contestação da verificação.';

/* Tabela 056 */
---------------
CREATE TABLE mtr.mtrtb056_parecer (
   nu_parecer                   BIGINT     NOT NULL DEFAULT nextval('mtr.mtrsq056_parecer'::regclass),
   nu_versao                    INT     NOT NULL,
   nu_apontamento               BIGINT  NOT NULL,
   nu_verificacao               BIGINT  NOT NULL,
   ic_aprovado                  BOOL    NOT NULL,
   de_comentario_tratamento     TEXT    NULL,
   de_comentario_contestacao    TEXT    NULL,
   ic_contestacao_acatada       BOOL    NULL,
   CONSTRAINT pk_mtrtb056 PRIMARY KEY (nu_parecer)
   USING INDEX TABLESPACE mtrtsix000
)
TABLESPACE mtrtsdt000;

COMMENT ON TABLE mtr.mtrtb056_parecer IS
'Tabela responsavel por armazenar o resultado (parecer) relacionado com a verificação realizada ao checklist avaliado.';

COMMENT ON COLUMN mtr.mtrtb056_parecer.nu_parecer IS
'Atributo que representa a chave primaria da entidade';

COMMENT ON COLUMN mtr.mtrtb056_parecer.nu_versao IS
'Campo de controle das versões do registro para viabilizar a concorrencia otimista';

COMMENT ON COLUMN mtr.mtrtb056_parecer.nu_apontamento IS
'Atributo que representa o identificador do apontamentoavaliado na composição do parecer.';

COMMENT ON COLUMN mtr.mtrtb056_parecer.nu_verificacao IS
'Atributo que representa o identificador do registro de verficação do checklist avaliado pelo operador';

COMMENT ON COLUMN mtr.mtrtb056_parecer.ic_aprovado IS
'Atributo utilizado para armazenar o resultado da avaliação do apontamento indicando se a avaliação representa uma aprovação (true) ou uma repreovação (false) para o apontamento proposto';

COMMENT ON COLUMN mtr.mtrtb056_parecer.de_comentario_tratamento IS
'Atributo utilizado para armazenar o possivel comentario definido pelo operador nos casos de reprovação do apontamento.';

COMMENT ON COLUMN mtr.mtrtb056_parecer.de_comentario_contestacao IS
'Atributo utilizado para armazenar o possivel comentario de replica encaminhado pela unidade responsavel pela alimentação do dossiê nos casos de contestação do parecer encaminhado.';

COMMENT ON COLUMN mtr.mtrtb056_parecer.ic_contestacao_acatada IS
'Atributo utilizado para armazenar o resultado da segunda avaliação pós contestação. Caso a avaliação permaneça reprovada, a unidade responsável pela alimentação, não poderá mais contestar a verificação e deverá aternder a orientação para ser apresciada em um novo registro de verificação ao checklist associado.';


/* INDICES DE UNICIDADE*/
------------------------
CREATE UNIQUE INDEX ix_mtrtb051_01 ON mtr.mtrtb051_checklist (
no_checklist,
nu_unidade_responsavel
)
TABLESPACE mtrtsix000;

CREATE UNIQUE INDEX ix_mtrtb052_01 ON mtr.mtrtb052_apontamento (
    nu_checklist,
    no_apontamento
)
TABLESPACE mtrtsix000;


CREATE UNIQUE INDEX ix_mtrtb053_01 ON mtr.mtrtb053_vinculacao_checklist (
    nu_processo_dossie,
    nu_processo_fase,
    nu_tipo_documento,
    dt_revogacao
)
TABLESPACE mtrtsix000;

CREATE UNIQUE INDEX ix_mtrtb053_02 ON mtr.mtrtb053_vinculacao_checklist (
    nu_processo_dossie,
    nu_processo_fase,
    nu_funcao_documental,
    dt_revogacao
)
TABLESPACE mtrtsix000;

CREATE UNIQUE INDEX ix_mtrtb053_03 ON mtr.mtrtb053_vinculacao_checklist (
    nu_processo_dossie,
    nu_processo_fase,
    nu_checklist,
    dt_revogacao
)
TABLESPACE mtrtsix000;

/* CHAVES ESTRANGEIRAS */
------------------------
ALTER TABLE mtr.mtrtb052_apontamento
   ADD CONSTRAINT fk_mtrtb052_mtrtb051 FOREIGN KEY (nu_checklist)
   REFERENCES mtr.mtrtb051_checklist(nu_checklist)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

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
   ADD CONSTRAINT fk_mtrtb053_mtrtb051 FOREIGN KEY (nu_checklist)
      REFERENCES mtr.mtrtb051_checklist (nu_checklist)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE mtr.mtrtb054_checklist_associado
   ADD CONSTRAINT fk_mtrtb054_mtrtb002 FOREIGN KEY (nu_dossie_produto)
      REFERENCES mtr.mtrtb002_dossie_produto (nu_dossie_produto)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE mtr.mtrtb054_checklist_associado
   ADD CONSTRAINT fk_mtrtb054_mtrtb014 FOREIGN KEY (nu_instancia_documento)
      REFERENCES mtr.mtrtb014_instancia_documento (nu_instancia_documento)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE mtr.mtrtb054_checklist_associado
   ADD CONSTRAINT fk_mtrtb054_mtrtb020 FOREIGN KEY (nu_processo_fase)
      REFERENCES mtr.mtrtb020_processo (nu_processo)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE mtr.mtrtb054_checklist_associado
   ADD CONSTRAINT fk_mtrtb054_mtrtb051 FOREIGN KEY (nu_checklist)
      REFERENCES mtr.mtrtb051_checklist (nu_checklist)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE mtr.mtrtb055_verificacao
   ADD CONSTRAINT fk_mtrtb055_mtrtb054 FOREIGN KEY (nu_checklist_associado)
      REFERENCES mtr.mtrtb054_checklist_associado (nu_checklist_associado)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE mtr.mtrtb056_parecer
   ADD CONSTRAINT fk_mtrtb056_mtrtb052 FOREIGN KEY (nu_apontamento)
      REFERENCES mtr.mtrtb052_apontamento (nu_apontamento)
      ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE mtr.mtrtb056_parecer
   ADD CONSTRAINT fk_mtrtb056_mtrtb055 FOREIGN KEY (nu_verificacao)
      REFERENCES mtr.mtrtb055_verificacao (nu_verificacao)
      ON DELETE RESTRICT ON UPDATE RESTRICT;
