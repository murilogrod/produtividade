
-- Execução dos flyway #48, #49, #50 e #46, nesta ordem. O #45 está contemplado no #48.

-- INICIO FLYWAY #48 (contempla o conteúdo do #45 também)
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
-- FIM FLYWAY #48 (contempla o conteúdo do #45 também)


-- INICIO FLYWAY #49
/* Tabela 055 */
---------------
ALTER TABLE mtr.mtrtb055_verificacao ADD COLUMN ic_verificacao_realizada BOOL        NOT NULL;

COMMENT ON COLUMN mtr.mtrtb055_verificacao.ic_verificacao_realizada IS
'Atributo utilizado para armazenar o indicativo do operador de ter realizado a verficiação ou não para aquele elemento. 
Essa situações pode ocorrer devido a existência de documentos opcionais (não obrigatórios) e que neste caso permite ao operador não analisar o mesmo.
Apesar da ausência de analise, ficará registrado que o operador optou por não realizar aquela verificação no momento da execução do tratamento do dossiê.';
-- FIM FLYWAY #49


-- INICIO FLYWAY #50
/* Tabela 051 */
---------------
ALTER TABLE mtr.mtrtb044_comportamento_pesquisa ALTER COLUMN ic_codigo_retorno TYPE character varying(20); 

/* 
    Executa a criação da VIEW baseado no esquema "ico", caso exista, para atender a necessidade da CEPTI
    Executa a criação da VIEW baseado no esquema "icosm001", caso exista, para atender a necessidade do PEDES
*/
DO $$
BEGIN
        DROP VIEW IF EXISTS ico.icovw001_unidade_vinculada;
        DROP VIEW IF EXISTS icosm001.icovw001_unidade_vinculada;

	IF EXISTS(
		SELECT schema_name
		FROM information_schema.schemata
		WHERE schema_name = 'ico'
	)
	THEN
		EXECUTE '
			CREATE OR REPLACE VIEW mtr.icovw001_unidade_vinculada AS 
			SELECT unidade_vncda_suat.nu_unidade_dire,
			    unidade_vncda_suat.nu_natural_dire,
			    unidade_vncda_suat.tipo_dire as sg_tipo_dire,
			    unidade_vncda_suat.no_dire,
			    unidade_vncda_suat.co_email_dire,
			    unidade_vncda_suat.tipo_sr as sg_tipo_sr,
			    unidade_vncda_suat.nu_unidade_sr,
			    unidade_vncda_suat.nu_natural_sr,
			    unidade_vncda_suat.no_su_regional,
			    unidade_vncda_suat.co_email_sr,
			    unidade_vncda_suat.tipo_unidade as sg_tipo_unidade,
			    unidade_vncda_suat.nu_unidade,
			    unidade_vncda_suat.nu_natural,
			    unidade_vncda_suat.no_unidade,
			    unidade_vncda_suat.uf as sg_uf,
			    unidade_vncda_suat.co_email_ag
			   FROM ( WITH emails AS (
					 SELECT DISTINCT u24.nu_unidade,
					    u24.nu_natural,
					    m02.co_comunicacao AS intranet
					   FROM ico.icotbu24_unidade u24
					     LEFT JOIN ico.icotbm03_meiocomun m03 ON u24.nu_unidade = m03.nu_unidade_v03 AND u24.nu_natural = m03.nu_natural_v03
					     LEFT JOIN ico.icotbm02_meio_comu m02 ON m03.nu_sqncl_cmnco_m02 = m02.nu_sqnl_cmnco AND m02.nu_tipo_cmnco_m04 = 5
					  WHERE m02.nu_sqnl_cmnco = (( SELECT max(a.nu_sqncl_cmnco_m02) AS max
						   FROM ico.icotbm03_meiocomun a,
						    ico.icotbm02_meio_comu b
						  WHERE a.nu_unidade_v03 = m03.nu_unidade_v03 AND a.nu_natural_v03 = m03.nu_natural_v03 AND a.nu_sqncl_cmnco_m02 = b.nu_sqnl_cmnco AND b.nu_tipo_cmnco_m04 = 5))
					  ORDER BY u24.nu_unidade
					)
				( SELECT tp_vinc_sr.nu_unde_vnclra_u24::integer AS nu_unidade_dire,
				    tp_vinc_sr.nu_ntrl_vnclra_u24 AS nu_natural_dire,
				    tipo_sn.sg_tipo_unidade::character varying AS tipo_dire,
				    suat.sg_unidade::character varying AS no_dire,
				    email_suat.intranet AS co_email_dire,
				    tipo_sr.sg_tipo_unidade::character varying AS tipo_sr,
				    tp_vinc_ag.nu_unde_vnclra_u24::integer AS nu_unidade_sr,
				    tp_vinc_ag.nu_ntrl_vnclra_u24 AS nu_natural_sr,
				    sr.no_unidade AS no_su_regional,
				    email_sr.intranet AS co_email_sr,
				    tipo_unidade.sg_tipo_unidade::character varying AS tipo_unidade,
				    pab.nu_unidade::integer AS nu_unidade,
				    pab.nu_natural,
				    pab.no_unidade,
				    pab.sg_uf_l22::character varying AS uf,
				    email_ag.intranet AS co_email_ag
				   FROM ico.icotbu24_unidade pab
				     LEFT JOIN ico.icotbu25_vincunid tp_vinc_pab ON pab.nu_unidade = tp_vinc_pab.nu_unde_vncla_u24 AND pab.nu_natural = tp_vinc_pab.nu_ntrl_vncla_u24
				     LEFT JOIN ico.icotbu24_unidade agencia ON tp_vinc_pab.nu_unde_vnclra_u24 = agencia.nu_unidade AND tp_vinc_pab.nu_ntrl_vnclra_u24 = agencia.nu_natural
				     LEFT JOIN ico.icotbu21_tpunidade tipo_unidade ON tipo_unidade.nu_tipo_unidade = pab.nu_tp_unidade_u21
				     LEFT JOIN ico.icotbu25_vincunid tp_vinc_ag ON agencia.nu_unidade = tp_vinc_ag.nu_unde_vncla_u24 AND agencia.nu_natural = tp_vinc_ag.nu_ntrl_vncla_u24
				     LEFT JOIN ico.icotbu24_unidade sr ON tp_vinc_ag.nu_unde_vnclra_u24 = sr.nu_unidade AND tp_vinc_ag.nu_ntrl_vnclra_u24 = sr.nu_natural
				     LEFT JOIN ico.icotbu21_tpunidade tipo_sr ON tipo_sr.nu_tipo_unidade = sr.nu_tp_unidade_u21
				     LEFT JOIN ico.icotbu25_vincunid tp_vinc_sr ON sr.nu_unidade = tp_vinc_sr.nu_unde_vncla_u24 AND sr.nu_natural = tp_vinc_sr.nu_ntrl_vncla_u24
				     LEFT JOIN ico.icotbu24_unidade suat ON tp_vinc_sr.nu_unde_vnclra_u24 = suat.nu_unidade AND tp_vinc_sr.nu_ntrl_vnclra_u24 = suat.nu_natural
				     LEFT JOIN ico.icotbu21_tpunidade tipo_sn ON tipo_sn.nu_tipo_unidade = suat.nu_tp_unidade_u21
				     LEFT JOIN emails email_suat ON email_suat.nu_unidade = tp_vinc_sr.nu_unde_vnclra_u24
				     LEFT JOIN emails email_sr ON email_sr.nu_unidade = tp_vinc_ag.nu_unde_vnclra_u24 AND email_sr.nu_natural = tp_vinc_ag.nu_ntrl_vnclra_u24
				     LEFT JOIN emails email_ag ON email_ag.nu_unidade = pab.nu_unidade AND email_ag.nu_natural = pab.nu_natural
				  WHERE tp_vinc_ag.nu_tp_vinculo_u22 = 1 AND tp_vinc_sr.nu_tp_vinculo_u22 = 1 AND agencia.nu_tp_unidade_u21 = 8 AND tp_vinc_pab.nu_tp_vinculo_u22 = 1 AND tp_vinc_ag.nu_tp_vinculo_u22 = 1 AND (pab.nu_tp_unidade_u21 = ANY (ARRAY[9, 56])) AND tp_vinc_pab.dt_fim IS NULL AND tp_vinc_ag.dt_fim IS NULL AND tp_vinc_sr.dt_fim IS NULL AND (tp_vinc_sr.nu_unde_vnclra_u24 = ANY (ARRAY[5174, 5175, 5176, 5177, 5178, 5179, 5181, 5182])) AND (tp_vinc_sr.nu_ntrl_vnclra_u24 = ANY (ARRAY[8835, 8836, 8837, 8838, 8839, 8840, 8841, 8842])) AND email_suat.nu_unidade = tp_vinc_sr.nu_unde_vnclra_u24 AND email_sr.nu_unidade = tp_vinc_ag.nu_unde_vnclra_u24 AND email_sr.nu_natural = tp_vinc_ag.nu_ntrl_vnclra_u24 AND email_ag.nu_unidade = pab.nu_unidade AND email_ag.nu_natural = pab.nu_natural
				  ORDER BY pab.nu_unidade)
				UNION
				( SELECT tp_vinc_ag.nu_unde_vnclra_u24::integer AS nu_unidade_dire,
				    tp_vinc_ag.nu_ntrl_vnclra_u24 AS nu_natural_dire,
				    tipo_sn.sg_tipo_unidade::character varying AS tipo_dire,
				    sr.sg_unidade::character varying AS no_dire,
				    email_suat.intranet AS co_email_dire,
				    tipo_sr.sg_tipo_unidade::character varying AS tipo_sr,
				    tp_vinc_pab.nu_unde_vnclra_u24::integer AS nu_unidade_sr,
				    tp_vinc_pab.nu_ntrl_vnclra_u24 AS nu_natural_sr,
				    agencia.no_unidade AS no_su_regional,
				    email_sr.intranet AS co_email_sr,
				    tipo_pab.sg_tipo_unidade::character varying AS tipo_unidade,
				    pab.nu_unidade::integer AS nu_unidade,
				    pab.nu_natural,
				    pab.no_unidade,
				    pab.sg_uf_l22::character varying AS uf,
				    email_ag.intranet AS co_email_ag
				   FROM ico.icotbu24_unidade pab
				     LEFT JOIN ico.icotbu25_vincunid tp_vinc_pab ON pab.nu_unidade = tp_vinc_pab.nu_unde_vncla_u24 AND pab.nu_natural = tp_vinc_pab.nu_ntrl_vncla_u24
				     LEFT JOIN ico.icotbu21_tpunidade tipo_pab ON tipo_pab.nu_tipo_unidade = pab.nu_tp_unidade_u21
				     LEFT JOIN ico.icotbu24_unidade agencia ON tp_vinc_pab.nu_unde_vnclra_u24 = agencia.nu_unidade AND tp_vinc_pab.nu_ntrl_vnclra_u24 = agencia.nu_natural
				     LEFT JOIN ico.icotbu25_vincunid tp_vinc_ag ON agencia.nu_unidade = tp_vinc_ag.nu_unde_vncla_u24 AND agencia.nu_natural = tp_vinc_ag.nu_ntrl_vncla_u24
				     LEFT JOIN ico.icotbu24_unidade sr ON tp_vinc_ag.nu_unde_vnclra_u24 = sr.nu_unidade AND tp_vinc_ag.nu_ntrl_vnclra_u24 = sr.nu_natural
				     LEFT JOIN ico.icotbu21_tpunidade tipo_sr ON tipo_sr.nu_tipo_unidade = agencia.nu_tp_unidade_u21
				     LEFT JOIN ico.icotbu25_vincunid tp_vinc_sr ON sr.nu_unidade = tp_vinc_sr.nu_unde_vncla_u24 AND sr.nu_natural = tp_vinc_sr.nu_ntrl_vncla_u24
				     LEFT JOIN ico.icotbu24_unidade suat ON tp_vinc_sr.nu_unde_vnclra_u24 = suat.nu_unidade AND tp_vinc_sr.nu_ntrl_vnclra_u24 = suat.nu_natural
				     LEFT JOIN ico.icotbu21_tpunidade tipo_sn ON tipo_sn.nu_tipo_unidade = sr.nu_tp_unidade_u21
				     LEFT JOIN emails email_suat ON email_suat.nu_unidade = tp_vinc_sr.nu_unde_vncla_u24
				     LEFT JOIN emails email_sr ON email_sr.nu_unidade = tp_vinc_ag.nu_unde_vncla_u24 AND email_sr.nu_natural = tp_vinc_ag.nu_ntrl_vncla_u24
				     LEFT JOIN emails email_ag ON email_ag.nu_unidade = pab.nu_unidade AND email_ag.nu_natural = pab.nu_natural
				  WHERE tp_vinc_ag.nu_tp_vinculo_u22 = 1 AND tp_vinc_sr.nu_tp_vinculo_u22 = 1 AND agencia.nu_tp_unidade_u21 = 42 AND tp_vinc_pab.nu_tp_vinculo_u22 = 1 AND tp_vinc_ag.nu_tp_vinculo_u22 = 1 AND tp_vinc_pab.dt_fim IS NULL AND tp_vinc_ag.dt_fim IS NULL AND tp_vinc_sr.dt_fim IS NULL AND (pab.nu_tp_unidade_u21 = ANY (ARRAY[8, 9, 56])) AND (tp_vinc_ag.nu_unde_vnclra_u24 = ANY (ARRAY[5174, 5175, 5176, 5177, 5178, 5179, 5181, 5182])) AND (tp_vinc_ag.nu_ntrl_vnclra_u24 = ANY (ARRAY[8835, 8836, 8837, 8838, 8839, 8840, 8841, 8842]))
				  ORDER BY pab.nu_unidade)) unidade_vncda_suat;
		';
	ELSE
		EXECUTE '
			CREATE OR REPLACE VIEW mtr.icovw001_unidade_vinculada AS 
			SELECT unidade_vncda_suat.nu_unidade_dire,
			    unidade_vncda_suat.nu_natural_dire,
			    unidade_vncda_suat.tipo_dire as sg_tipo_dire,
			    unidade_vncda_suat.no_dire,
			    unidade_vncda_suat.co_email_dire,
			    unidade_vncda_suat.tipo_sr as sg_tipo_sr,
			    unidade_vncda_suat.nu_unidade_sr,
			    unidade_vncda_suat.nu_natural_sr,
			    unidade_vncda_suat.no_su_regional,
			    unidade_vncda_suat.co_email_sr,
			    unidade_vncda_suat.tipo_unidade as sg_tipo_unidade,
			    unidade_vncda_suat.nu_unidade,
			    unidade_vncda_suat.nu_natural,
			    unidade_vncda_suat.no_unidade,
			    unidade_vncda_suat.uf as sg_uf,
			    unidade_vncda_suat.co_email_ag
			   FROM ( WITH emails AS (
					 SELECT DISTINCT u24.nu_unidade,
					    u24.nu_natural,
					    m02.co_comunicacao AS intranet
					   FROM icosm001.icotbu24_unidade u24
					     LEFT JOIN icosm001.icotbm03_meiocomun m03 ON u24.nu_unidade = m03.nu_unidade_v03 AND u24.nu_natural = m03.nu_natural_v03
					     LEFT JOIN icosm001.icotbm02_meio_comu m02 ON m03.nu_sqncl_cmnco_m02 = m02.nu_sqnl_cmnco AND m02.nu_tipo_cmnco_m04 = 5
					  WHERE m02.nu_sqnl_cmnco = (( SELECT max(a.nu_sqncl_cmnco_m02) AS max
						   FROM icosm001.icotbm03_meiocomun a,
						    icosm001.icotbm02_meio_comu b
						  WHERE a.nu_unidade_v03 = m03.nu_unidade_v03 AND a.nu_natural_v03 = m03.nu_natural_v03 AND a.nu_sqncl_cmnco_m02 = b.nu_sqnl_cmnco AND b.nu_tipo_cmnco_m04 = 5))
					  ORDER BY u24.nu_unidade
					)
				( SELECT tp_vinc_sr.nu_unde_vnclra_u24::integer AS nu_unidade_dire,
				    tp_vinc_sr.nu_ntrl_vnclra_u24 AS nu_natural_dire,
				    tipo_sn.sg_tipo_unidade::character varying AS tipo_dire,
				    suat.sg_unidade::character varying AS no_dire,
				    email_suat.intranet AS co_email_dire,
				    tipo_sr.sg_tipo_unidade::character varying AS tipo_sr,
				    tp_vinc_ag.nu_unde_vnclra_u24::integer AS nu_unidade_sr,
				    tp_vinc_ag.nu_ntrl_vnclra_u24 AS nu_natural_sr,
				    sr.no_unidade AS no_su_regional,
				    email_sr.intranet AS co_email_sr,
				    tipo_unidade.sg_tipo_unidade::character varying AS tipo_unidade,
				    pab.nu_unidade::integer AS nu_unidade,
				    pab.nu_natural,
				    pab.no_unidade,
				    pab.sg_uf_l22::character varying AS uf,
				    email_ag.intranet AS co_email_ag
				   FROM icosm001.icotbu24_unidade pab
				     LEFT JOIN icosm001.icotbu25_vincunid tp_vinc_pab ON pab.nu_unidade = tp_vinc_pab.nu_unde_vncla_u24 AND pab.nu_natural = tp_vinc_pab.nu_ntrl_vncla_u24
				     LEFT JOIN icosm001.icotbu24_unidade agencia ON tp_vinc_pab.nu_unde_vnclra_u24 = agencia.nu_unidade AND tp_vinc_pab.nu_ntrl_vnclra_u24 = agencia.nu_natural
				     LEFT JOIN icosm001.icotbu21_tpunidade tipo_unidade ON tipo_unidade.nu_tipo_unidade = pab.nu_tp_unidade_u21
				     LEFT JOIN icosm001.icotbu25_vincunid tp_vinc_ag ON agencia.nu_unidade = tp_vinc_ag.nu_unde_vncla_u24 AND agencia.nu_natural = tp_vinc_ag.nu_ntrl_vncla_u24
				     LEFT JOIN icosm001.icotbu24_unidade sr ON tp_vinc_ag.nu_unde_vnclra_u24 = sr.nu_unidade AND tp_vinc_ag.nu_ntrl_vnclra_u24 = sr.nu_natural
				     LEFT JOIN icosm001.icotbu21_tpunidade tipo_sr ON tipo_sr.nu_tipo_unidade = sr.nu_tp_unidade_u21
				     LEFT JOIN icosm001.icotbu25_vincunid tp_vinc_sr ON sr.nu_unidade = tp_vinc_sr.nu_unde_vncla_u24 AND sr.nu_natural = tp_vinc_sr.nu_ntrl_vncla_u24
				     LEFT JOIN icosm001.icotbu24_unidade suat ON tp_vinc_sr.nu_unde_vnclra_u24 = suat.nu_unidade AND tp_vinc_sr.nu_ntrl_vnclra_u24 = suat.nu_natural
				     LEFT JOIN icosm001.icotbu21_tpunidade tipo_sn ON tipo_sn.nu_tipo_unidade = suat.nu_tp_unidade_u21
				     LEFT JOIN emails email_suat ON email_suat.nu_unidade = tp_vinc_sr.nu_unde_vnclra_u24
				     LEFT JOIN emails email_sr ON email_sr.nu_unidade = tp_vinc_ag.nu_unde_vnclra_u24 AND email_sr.nu_natural = tp_vinc_ag.nu_ntrl_vnclra_u24
				     LEFT JOIN emails email_ag ON email_ag.nu_unidade = pab.nu_unidade AND email_ag.nu_natural = pab.nu_natural
				  WHERE tp_vinc_ag.nu_tp_vinculo_u22 = 1 AND tp_vinc_sr.nu_tp_vinculo_u22 = 1 AND agencia.nu_tp_unidade_u21 = 8 AND tp_vinc_pab.nu_tp_vinculo_u22 = 1 AND tp_vinc_ag.nu_tp_vinculo_u22 = 1 AND (pab.nu_tp_unidade_u21 = ANY (ARRAY[9, 56])) AND tp_vinc_pab.dt_fim IS NULL AND tp_vinc_ag.dt_fim IS NULL AND tp_vinc_sr.dt_fim IS NULL AND (tp_vinc_sr.nu_unde_vnclra_u24 = ANY (ARRAY[5174, 5175, 5176, 5177, 5178, 5179, 5181, 5182])) AND (tp_vinc_sr.nu_ntrl_vnclra_u24 = ANY (ARRAY[8835, 8836, 8837, 8838, 8839, 8840, 8841, 8842])) AND email_suat.nu_unidade = tp_vinc_sr.nu_unde_vnclra_u24 AND email_sr.nu_unidade = tp_vinc_ag.nu_unde_vnclra_u24 AND email_sr.nu_natural = tp_vinc_ag.nu_ntrl_vnclra_u24 AND email_ag.nu_unidade = pab.nu_unidade AND email_ag.nu_natural = pab.nu_natural
				  ORDER BY pab.nu_unidade)
				UNION
				( SELECT tp_vinc_ag.nu_unde_vnclra_u24::integer AS nu_unidade_dire,
				    tp_vinc_ag.nu_ntrl_vnclra_u24 AS nu_natural_dire,
				    tipo_sn.sg_tipo_unidade::character varying AS tipo_dire,
				    sr.sg_unidade::character varying AS no_dire,
				    email_suat.intranet AS co_email_dire,
				    tipo_sr.sg_tipo_unidade::character varying AS tipo_sr,
				    tp_vinc_pab.nu_unde_vnclra_u24::integer AS nu_unidade_sr,
				    tp_vinc_pab.nu_ntrl_vnclra_u24 AS nu_natural_sr,
				    agencia.no_unidade AS no_su_regional,
				    email_sr.intranet AS co_email_sr,
				    tipo_pab.sg_tipo_unidade::character varying AS tipo_unidade,
				    pab.nu_unidade::integer AS nu_unidade,
				    pab.nu_natural,
				    pab.no_unidade,
				    pab.sg_uf_l22::character varying AS uf,
				    email_ag.intranet AS co_email_ag
				   FROM icosm001.icotbu24_unidade pab
				     LEFT JOIN icosm001.icotbu25_vincunid tp_vinc_pab ON pab.nu_unidade = tp_vinc_pab.nu_unde_vncla_u24 AND pab.nu_natural = tp_vinc_pab.nu_ntrl_vncla_u24
				     LEFT JOIN icosm001.icotbu21_tpunidade tipo_pab ON tipo_pab.nu_tipo_unidade = pab.nu_tp_unidade_u21
				     LEFT JOIN icosm001.icotbu24_unidade agencia ON tp_vinc_pab.nu_unde_vnclra_u24 = agencia.nu_unidade AND tp_vinc_pab.nu_ntrl_vnclra_u24 = agencia.nu_natural
				     LEFT JOIN icosm001.icotbu25_vincunid tp_vinc_ag ON agencia.nu_unidade = tp_vinc_ag.nu_unde_vncla_u24 AND agencia.nu_natural = tp_vinc_ag.nu_ntrl_vncla_u24
				     LEFT JOIN icosm001.icotbu24_unidade sr ON tp_vinc_ag.nu_unde_vnclra_u24 = sr.nu_unidade AND tp_vinc_ag.nu_ntrl_vnclra_u24 = sr.nu_natural
				     LEFT JOIN icosm001.icotbu21_tpunidade tipo_sr ON tipo_sr.nu_tipo_unidade = agencia.nu_tp_unidade_u21
				     LEFT JOIN icosm001.icotbu25_vincunid tp_vinc_sr ON sr.nu_unidade = tp_vinc_sr.nu_unde_vncla_u24 AND sr.nu_natural = tp_vinc_sr.nu_ntrl_vncla_u24
				     LEFT JOIN icosm001.icotbu24_unidade suat ON tp_vinc_sr.nu_unde_vnclra_u24 = suat.nu_unidade AND tp_vinc_sr.nu_ntrl_vnclra_u24 = suat.nu_natural
				     LEFT JOIN icosm001.icotbu21_tpunidade tipo_sn ON tipo_sn.nu_tipo_unidade = sr.nu_tp_unidade_u21
				     LEFT JOIN emails email_suat ON email_suat.nu_unidade = tp_vinc_sr.nu_unde_vncla_u24
				     LEFT JOIN emails email_sr ON email_sr.nu_unidade = tp_vinc_ag.nu_unde_vncla_u24 AND email_sr.nu_natural = tp_vinc_ag.nu_ntrl_vncla_u24
				     LEFT JOIN emails email_ag ON email_ag.nu_unidade = pab.nu_unidade AND email_ag.nu_natural = pab.nu_natural
				  WHERE tp_vinc_ag.nu_tp_vinculo_u22 = 1 AND tp_vinc_sr.nu_tp_vinculo_u22 = 1 AND agencia.nu_tp_unidade_u21 = 42 AND tp_vinc_pab.nu_tp_vinculo_u22 = 1 AND tp_vinc_ag.nu_tp_vinculo_u22 = 1 AND tp_vinc_pab.dt_fim IS NULL AND tp_vinc_ag.dt_fim IS NULL AND tp_vinc_sr.dt_fim IS NULL AND (pab.nu_tp_unidade_u21 = ANY (ARRAY[8, 9, 56])) AND (tp_vinc_ag.nu_unde_vnclra_u24 = ANY (ARRAY[5174, 5175, 5176, 5177, 5178, 5179, 5181, 5182])) AND (tp_vinc_ag.nu_ntrl_vnclra_u24 = ANY (ARRAY[8835, 8836, 8837, 8838, 8839, 8840, 8841, 8842]))
				  ORDER BY pab.nu_unidade)) unidade_vncda_suat;
		';
	END IF;

END
$$;
-- FIM FLYWAY #50


-- INICIO FLYWAY #46
/* ACERTO DE DICIONARIO */
-------------------------
COMMENT ON COLUMN mtr.mtrtb001_pessoa_fisica.vr_renda_mensal IS
'Atributo utilizado para armazenar o valor da renda mensal do cliente.
Esta informação pode influenciar no fluxo de tratamento de alguns processos.';
COMMENT ON COLUMN mtr.mtrtb001_pessoa_juridica.vr_faturamento_anual IS
'Atributo utilizado para armazenar o valor do fatutramento anual da empresa.
Esta informação pode influenciar no fluxo de tratamento de alguns processos.';

COMMENT ON COLUMN mtr.mtrtb002_dossie_produto.ic_canal_caixa IS 'Atributo utilizado para indicar o canal CAIXA utilizado para submeter o dossiê conforme o seguinte dominio:

AGE - Agência Fisica
AGD - Agência Digital
CCA - Correspondente Caixa Aqui';
COMMENT ON COLUMN mtr.mtrtb002_dossie_produto.nu_processo_fase IS 'Atributo utilizado para referenciar a etapa de andamento do dossiê.
A este registro deverá estar vinculado apenas processos do tipo "etapa"';
COMMENT ON COLUMN mtr.mtrtb002_dossie_produto.ts_finalizado IS 'Atributo que armazena a data e hora que foi realizada a finalização do dossiê.';

COMMENT ON COLUMN mtr.mtrtb004_dossie_cliente_produto.nu_dossie_cliente_relacionado IS 'Atributo que armazena a referencia para o dossiê do cliente em que o registro de cliente atual possui relação.

Por exemplo, nos casos de identificar uma relação entre um CPF e um CNPJ junto ao dossiê do produto, o atributo "nu_dossie_cliente" manteria o registro do CPF e o atributo "nu_dossie_cliente_relacionado" manteria o registro do CNPJ';


COMMENT ON COLUMN mtr.mtrtb006_canal.ic_canal_caixa IS 'Atributo utilizado para indicar o canal CAIXA utilizado para submeter o dossiê conforme o seguinte dominio:

AGE - Agência Fisica
AGD - Agência Digital
CCA - Correspondente Caixa Aqui';

COMMENT ON COLUMN mtr.mtrtb009_tipo_documento.no_classe_ged IS 'Atributo utilizado para armazenar o nome da classe no GED que deve ser utilizada para armazenar o documento junto ao SIECM.';

COMMENT ON COLUMN mtr.mtrtb012_tipo_situacao_dossie.ic_tipo_final IS 'Atributo utilizado para identificar o tipo de situação que é final. Após ser aplicado uma situação deste tipo, o sistema não deverá permitir que sejam criados novos registros de situação para o dossiê do produto';

COMMENT ON COLUMN mtr.mtrtb013_situacao_dossie.ts_saida IS 'Atributo utilizado para armazenar a data/hora de saída da situação ao dossiê';

COMMENT ON COLUMN mtr.mtrtb014_instancia_documento.nu_dossie_cliente_produto IS 'Atributo que representa e relação existente entre o dossiê de cliente e o dossiê de produto ao qual foi a instancia foi vinculada. Utilizado apenas para os casos de documentos associados pela vinculção definida pelo processo originador do dossiê de produto. Para os casos de documento definidos pelo elemento de conteudo do processo/produto ou da garantia informada no dossiê do produto este atributo não estara definido.';

COMMENT ON COLUMN mtr.mtrtb019_campo_formulario.nu_processo IS 'Atributo que representa o processo em que deverá ser apresentado o campo do formulário para preenchimento.

Só devem ser associado a este atributo, registros de processos que representam um processo que define uma etapa (fase).';

COMMENT ON COLUMN mtr.mtrtb020_processo.ic_dossie IS 'Atributo utilizado para identificar os processos que podem ter vinculação de dossiês de produto. Ao navegar na árvore de processo, ao chegar a um processo que possa ter vinculação de dossiê, o sistema apresenta as opções de inclusão de dossiê para o processo, considerando a parametrização do processo.

true - Pode ter vinculação de dossiê de produto
false - Não pode ter vinculação direta de dossiê de produto.';
COMMENT ON COLUMN mtr.mtrtb020_processo.ic_controla_validade_documento IS 'Atributo utilizado para identificar se os dossiês vinculados a este tipo de processo deverão ter os registros de documentos com a validade controlada. 
Caso positivo, quando um documento submetida (B) possui a mesma tipologia documental de um pré existente (A) faz com que o registro do documento mais antigo no repositorio (A) tenha o valor de ts_validade alterado para o momento da carga do novo documento(B).
Caso negativo, o atributo ts_validade deve ser sempre nulo e a situação da instancia do documento representada na tabela 017 deve ser registrada apenas como "DOCUMENTO CRIADO"

Apenas para processos que possuam este atributo como "true" a legenda de cores de documentos do dossiê de produto deve ser exibida.';
COMMENT ON COLUMN mtr.mtrtb020_processo.ic_tipo_pessoa IS 'Atributo que determina qual tipo de pessoa pode ter o processo atribuído.
Pode assumir os seguintes valores:
F - Física
J - Jurídica
A - Física ou Jurídica';

COMMENT ON COLUMN mtr.mtrtb021_unidade_autorizada.nu_apenso_adm IS 'Atributo utilizado para identificar o apenso administrativo, a qual a autorização esta vinculada.';
COMMENT ON COLUMN mtr.mtrtb021_unidade_autorizada.nu_contrato_adm IS 'Atributo utilizado para identificar o contrato administrativo, a qual a autorização esta vinculada.';
COMMENT ON COLUMN mtr.mtrtb021_unidade_autorizada.nu_processo_adm IS 'Atributo utilizado para identificar o processo administrativo, a qual a autorização esta vinculada.';

COMMENT ON COLUMN mtr.mtrtb025_processo_documento.ic_obrigatorio IS
'Atributo utilizado para identificar se um determinado documento é obrigatorio para o determinado tipo de relacionamento especificado no processo.

Caso este atributo seja true, significa que o documento que atende a indicação do tipo especifico ou função documental indicado deve estar presente na tabela de documentos e vinculado ao dossiê do cliente associado a operação conforme tipo de relacionamento indicado.

Caso este atributo seja false, trata-se de um documento opcional que poderá auxiloiar na analise da operação, mas que não é essencial e pode não existir na relação de documentos vinculados ao dossiê do cliente especificado.';
COMMENT ON COLUMN mtr.mtrtb025_processo_documento.ic_validar IS
'Atributo que indica se o documento deve ser validado quando vinculado no dossiê.
Caso verdadeiro, a instancia do documento deve ser criada com a situação "Criado" -> "Aguardando Avaliação.
Caso false, a instancia do documento deve ser criada com a situação de aprovada conforme regra de negocio realizada pelo sistema, desde que já exista outra instancia do mesmo documento com situação aprovada previamente.';

COMMENT ON COLUMN mtr.mtrtb029_campo_apresentacao.nu_campo_formulario IS 'Atributo que representa o campo do formulario ao qual a forma de exibição referencia.';

COMMENT ON COLUMN mtr.mtrtb030_resposta_dossie.nu_campo_formulario IS 'Atributo utilizado para identificar o campo do formulario dinamico ao qual a resposta esta vinculada.';

COMMENT ON COLUMN mtr.mtrtb032_elemento_conteudo.nu_processo IS 'Atributo que representa o processo ao qual o elemento de conteudo esta vinculado.

Caso o processo associado seja um processo dossiê, o elemento de conteudo deverá ser visivel durante todas as fases da operação.

Caso o processo associado seja um processo fase, o elemento de conteudo deverá ser visivel apenas quando o dossiê de produto estiver sendo representado na mesma fase de vinculação.';

COMMENT ON COLUMN mtr.mtrtb035_garantia_informada.de_garantia IS 'Atributo utilizado para armazenar uma descrição livre da garantia pelo usuario submissor.
Essa informação será utilizada pelo usuáro avaliador da operação/documentos.';

COMMENT ON COLUMN mtr.mtrtb043_documento_garantia.nu_documento_garantia IS
'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtr.mtrtb043_documento_garantia.nu_versao IS
'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
COMMENT ON COLUMN mtr.mtrtb043_documento_garantia.nu_garantia IS
'Atributo que representa o identificador da garantia associada ao registro de vinculação perante o dossiê de produto.';
COMMENT ON COLUMN mtr.mtrtb043_documento_garantia.nu_processo IS
'Atributo que representa o processo definido na relação para identificar o tipo de documento necessario a apresentação visando a comprovação da garantia a ser ofertada.

Só devem ser associado a este atributo, registros de processos que representam um processo que é utilizado para gerar dossiê.';
COMMENT ON COLUMN mtr.mtrtb043_documento_garantia.nu_tipo_documento IS
'Atributo que representa o tipo de documento especifico definido na relação com a a garantia de acordo com o processo composição de tipos de documentos. Caso este atributo esteja nulo, o atributo que representa a função documental deverá estar prenchido.';
COMMENT ON COLUMN mtr.mtrtb043_documento_garantia.nu_funcao_documental IS
'Atributo que representa a função documental definida na relação com a a garantia de acordo com o processo composição de tipos de documentos. Caso este atributo esteja nulo, o atributo que representa o tipo documental deverá estar prenchido.';



/* Exclusão de valores Default */
--------------------------------
ALTER TABLE mtr.mtrtb035_garantia_informada ALTER COLUMN pc_garantia_informada DROP DEFAULT;
ALTER TABLE mtr.mtrtb035_garantia_informada ALTER COLUMN ic_forma_garantia DROP DEFAULT;
-- FIM FLYWAY #46