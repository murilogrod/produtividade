ALTER TABLE MTRSM001.MTRTB101_AUTORIZACAO_DOCUMENTO RENAME TO MTRTB101_DOCUMENTO;
ALTER SEQUENCE MTRSM001.MTRSQ101_AUTORIZACAO_DOCUMENTO RENAME TO MTRSQ101_DOCUMENTO;

ALTER TABLE MTRSM001.MTRTB102_SICLI_ERRO RENAME TO MTRTB103_SICLI_ERRO;
ALTER SEQUENCE MTRSM001.MTRSQ102_SICLI_ERRO RENAME TO MTRSQ103_SICLI_ERRO;

ALTER TABLE MTRSM001.MTRTB100_AUTORIZACAO RENAME COLUMN NU_AUTORIZACAO_DOSSIE TO CO_AUTORIZACAO;


----------------------------------------------------------------------------------------------------
ALTER TABLE MTRSM001.MTRTB022_PRODUTO ADD COLUMN IC_TIPO_PESSOA CHARACTER(1) NOT NULL;
ALTER TABLE MTRSM001.MTRTB022_PRODUTO ADD COLUMN IC_DOSSIE_DIGITAL BOOLEAN NOT NULL;
ALTER TABLE MTRSM001.MTRTB022_PRODUTO DROP COLUMN IC_PESQUISAS;
ALTER TABLE MTRSM001.MTRTB022_PRODUTO RENAME COLUMN IC_BLOQUEIO_CADIN TO IC_PESQUISA_CADIN;
ALTER TABLE MTRSM001.MTRTB022_PRODUTO RENAME COLUMN IC_BLOQUEIO_SCPC TO IC_PESQUISA_SCPC;
ALTER TABLE MTRSM001.MTRTB022_PRODUTO RENAME COLUMN IC_BLOQUEIO_SERASA TO IC_PESQUISA_SERASA;
ALTER TABLE MTRSM001.MTRTB022_PRODUTO RENAME COLUMN IC_BLOQUEIO_CCF TO IC_PESQUISA_CCF;
ALTER TABLE MTRSM001.MTRTB022_PRODUTO RENAME COLUMN IC_BLOQUEIO_SICOW TO IC_PESQUISA_SICOW;
ALTER TABLE MTRSM001.MTRTB022_PRODUTO RENAME COLUMN IC_BLOQUEIO_RECEITA TO IC_PESQUISA_RECEITA;


comment on column MTRSM001.MTRTB022_PRODUTO.IC_TIPO_PESSOA is
'Atributo que determina qual tipo de pessoa pode contratar o produto.
Pode assumir os seguintes valores:
F - Física
J - Jurídica
A - Física ou Jurídica
';

comment on column MTRSM001.MTRTB022_PRODUTO.IC_DOSSIE_DIGITAL is
'Atributo utilizado para indicar se o produto já esta mapeado para executar ações vinculadas ao modelo do dossiê digital';

----------------------------------------------------------------------------------------------------

ALTER TABLE MTRSM001.MTRTB003_DOCUMENTO ADD COLUMN IC_ORIGEM_DOCUMENTO CHARACTER(1) NOT NULL DEFAULT 'S';
ALTER TABLE MTRSM001.MTRTB003_DOCUMENTO ADD COLUMN IC_TEMPORARIO INT2 NOT NULL DEFAULT 0;

comment on column MTRSM001.MTRTB003_DOCUMENTO.IC_ORIGEM_DOCUMENTO is
'Atributo utilizado para armazenar a origem do documento digitalizado submetido baseado no seguinte dominio:

O = Documento Original
S = Cópia Simples
C = Cópia Autenticada em Cartório
A = Cópia Autenticada Administrativamente

Sempre que um documento for submetido via upload ou via app mobile este atributo deverá ser preenchido como copia simples (S)';

comment on column MTRSM001.MTRTB003_DOCUMENTO.IC_TEMPORARIO is
'Atributo utilizado para indicar se o documento ainda esta fase de analise sob a otica do dossiê digital.

Quando um documento for submetido pelo fluxo do dossiê digital, antes de utiliza-lo uma serie de verificações deve ser realizada dentro de um determinado espaço de tempo, senão esse documento será expurgado da base.

Esse atributo pode assumir o dominio abaixo:

0 - Definitivo
1 - Temporario - Extração de dados (OCR)
2 - Temporario - Antifraude';
------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE MTRSM001.MTRSQ102_AUTORIZACAO_NEGADA
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


/*==============================================================*/
/* Table: MTRTB102_AUTORIZACAO_NEGADA                           */
/*==============================================================*/
create table MTRSM001.MTRTB102_AUTORIZACAO_NEGADA (
   NU_AUTORIZACAO       INT8      NOT NULL DEFAULT nextval('MTRSM001.MTRSQ102_AUTORIZACAO_NEGADA'::regclass) ,
   NU_VERSAO            INT4                 not null,
   TS_REGISTRO          TIMESTAMP WITHOUT TIME ZONE            not null,
   NU_OPERACAO          INT4                 not null,
   NU_MODALIDADE        INT4                 not null,
   NO_PRODUTO           VARCHAR(255)         not null,
   NU_CPF_CNPJ          INT8                 not null,
   IC_TIPO_PESSOA       VARCHAR(1)           not null,
   CO_SISTEMA_SOLICITANTE VARCHAR(100)         not null,
   DE_MOTIVO            TEXT                 not null,
   constraint PK_MTRTB102_AUTORIZACAO_NEGADA primary key (NU_AUTORIZACAO)
);

comment on table MTRSM001.MTRTB102_AUTORIZACAO_NEGADA is
'Tabela utilizada para armazenar as solicitações de autorizações relacionadas ao nivel documental que foram negadas.';

comment on column MTRSM001.MTRTB102_AUTORIZACAO_NEGADA.NU_AUTORIZACAO is
'Atributo que representa a chave primaria da entidade.';

comment on column MTRSM001.MTRTB102_AUTORIZACAO_NEGADA.NU_VERSAO is
'Campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column MTRSM001.MTRTB102_AUTORIZACAO_NEGADA.TS_REGISTRO is
'Atributo utilizado para armazenar a datae hora de recebimento da solicitação de autorização ';

comment on column MTRSM001.MTRTB102_AUTORIZACAO_NEGADA.NU_OPERACAO is
'Atributo utilizado para armazenar o codigo de operação do produto solicitado na autorização.';

comment on column MTRSM001.MTRTB102_AUTORIZACAO_NEGADA.NU_MODALIDADE is
'Atributo utilizado para armazenar o codigo da modalidade do produto solicitado na autorização.';

comment on column MTRSM001.MTRTB102_AUTORIZACAO_NEGADA.NO_PRODUTO is
'Atributo utilizado para armazenar o nome do produto solicitado na autorização.';

comment on column MTRSM001.MTRTB102_AUTORIZACAO_NEGADA.NU_CPF_CNPJ is
'Atributo utilizado para armazenar o numero do CPF/CNPJ do cliente vinculado a autorização fornecida.';

comment on column MTRSM001.MTRTB102_AUTORIZACAO_NEGADA.IC_TIPO_PESSOA is
'Atributo utilizado para indicar o tipo de pessoa, se fisica ou juridica podendo assumir os seguintes valores:
F - Fisica
J - Juridica';

comment on column MTRSM001.MTRTB102_AUTORIZACAO_NEGADA.CO_SISTEMA_SOLICITANTE is
'Codigo identificador do sistema solicitante da autorização encaminhado no token do SSO';

comment on column MTRSM001.MTRTB102_AUTORIZACAO_NEGADA.DE_MOTIVO is
'Atributo utilizado para armazenar a descrição do motivo para a negativa.';


CREATE SEQUENCE MTRSM001.MTRSQ044_COMPORTAMENTO_PESQUISA
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
    /*==============================================================*/
/* Table: MTRTB044_COMPORTAMENTO_PESQUISA                       */
/*==============================================================*/
create table MTRSM001.MTRTB044_COMPORTAMENTO_PESQUISA (
   NU_COMPORTAMENTO_PESQUISA INT8          NOT NULL DEFAULT nextval('MTRSM001.MTRSQ044_COMPORTAMENTO_PESQUISA'::regclass),
   NU_VERSAO            INT4                 not null,
   NU_PRODUTO           INT4                 not null,
   IC_SISTEMA_RETORNO   VARCHAR(10)          not null,
   VR_CODIGO_RETORNO    VARCHAR(10)          not null,
   IC_BLOQUEIO          BOOL                 not null,
   DE_ORIENTACAO        TEXT                 null,
   constraint PK_MTRTB044_COMPORTAMENTO_PESQ primary key (NU_COMPORTAMENTO_PESQUISA)
);

comment on table MTRSM001.MTRTB044_COMPORTAMENTO_PESQUISA is
'Tabela utilizada para armazenar o comportamento esperado conforme os codigos de retorno da pesquisa do SIPES com relação ao impedimento ou não de gerar uma autorização para o cliente solicitado e definição das mensagens de orientação que deverão ser encaminhadas para os usuarios.';

comment on column MTRSM001.MTRTB044_COMPORTAMENTO_PESQUISA.NU_COMPORTAMENTO_PESQUISA is
'Atributo que representa a chave primaria da entidade.';

comment on column MTRSM001.MTRTB044_COMPORTAMENTO_PESQUISA.NU_VERSAO is
'Campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column MTRSM001.MTRTB044_COMPORTAMENTO_PESQUISA.NU_PRODUTO is
'Atributo utilizado para identificar o produto relacionado ao comportamento do retorno da pesquisa';

comment on column MTRSM001.MTRTB044_COMPORTAMENTO_PESQUISA.IC_SISTEMA_RETORNO is
'Atributo utilizado para indicar o sistema relacionado com o retorno da pesquisa cadastral baseado no dominio abaixo:

SICPF
SERASA
CADIN
SINAD
CCF
SPC
SICOW';

comment on column MTRSM001.MTRTB044_COMPORTAMENTO_PESQUISA.VR_CODIGO_RETORNO is
'Atributo utilizado para definir o valor do codigo de retorno que deve ser analisado para envio da mensagem e definição do comportamento de emissão ou bloqueio de autorização documental.';

comment on column MTRSM001.MTRTB044_COMPORTAMENTO_PESQUISA.IC_BLOQUEIO is
'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao sistema especificado no atributo "ic_sistema" retorne um resultado com o codigo definido no atributo "vr_codigo_retorno".';

comment on column MTRSM001.MTRTB044_COMPORTAMENTO_PESQUISA.DE_ORIENTACAO is
'Atributo utilizado para armazenar a mensagem de orientação que deve ser encaminhada para o usuario do sistema. ';

alter table MTRSM001.MTRTB044_COMPORTAMENTO_PESQUISA
   add constraint FK_MTRTB044_MTRTB022 foreign key (NU_PRODUTO)
      references MTRSM001.MTRTB022_PRODUTO (NU_PRODUTO)
      on delete restrict on update restrict;




