ALTER TABLE MTRSM001.MTRTB006_CANAL_CAPTURA 		RENAME TO MTRTB006_CANAL;
ALTER TABLE MTRSM001.MTRTB006_CANAL  RENAME COLUMN NU_CANAL_CAPTURA TO NU_CANAL; 
ALTER TABLE MTRSM001.MTRTB006_CANAL  RENAME COLUMN SG_CANAL_CAPTURA TO SG_CANAL;
ALTER TABLE MTRSM001.MTRTB006_CANAL  RENAME COLUMN DE_CANAL_CAPTURA TO DE_CANAL;
ALTER TABLE MTRSM001.MTRTB006_CANAL  ADD COLUMN CO_INTEGRACAO INT8 NOT NULL;

comment on column MTRSM001.MTRTB006_CANAL.CO_INTEGRACAO is
'Atributo que representa o código único para o sistema de origem.';

ALTER TABLE MTRSM001.MTRTB103_SICLI_ERRO 			RENAME TO MTRTB200_SICLI_ERRO;
ALTER SEQUENCE MTRSM001.MTRSQ103_SICLI_ERRO 		RENAME TO MTRSQ200_SICLI_ERRO;

ALTER TABLE MTRSM001.MTRTB101_DOCUMENTO ADD COLUMN DE_FINALIDADE VARCHAR(100) NOT NULL;
comment on column MTRSM001.MTRTB101_DOCUMENTO.DE_FINALIDADE is
'Atributo que representa para qual finalidade o documento foi usado';

ALTER TABLE MTRSM001.MTRTB007_ATRIBUTO_DOCUMENTO ADD COLUMN IX_ASSERTIVIDADE DECIMAL(5,2);
comment on column MTRSM001.MTRTB007_ATRIBUTO_DOCUMENTO.IX_ASSERTIVIDADE is
'Atributo que representa a porcentagem de assertividade que o OCR obteve';

ALTER TABLE MTRSM001.MTRTB007_ATRIBUTO_DOCUMENTO ADD COLUMN IC_ACERTO_MANUAL BOOL NOT NULL;
comment on column MTRSM001.MTRTB007_ATRIBUTO_DOCUMENTO.IC_ACERTO_MANUAL is
'Atributo que representa se houve ajuste manual no atributo extraído';


/*==============================================================*/
/* Table: MTRTB045_ATRIBUTO_EXTRACAO                            */
/*==============================================================*/

CREATE SEQUENCE MTRSM001.MTRSQ045_ATRIBUTO_EXTRACAO
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
create table MTRSM001.MTRTB045_ATRIBUTO_EXTRACAO (
   NU_ATRIBUTO_EXTRACAO BIGINT DEFAULT nextval('MTRSM001.MTRSQ045_ATRIBUTO_EXTRACAO'::regclass)               not null,
   NU_VERSAO            INT4                 not null,
   NU_TIPO_DOCUMENTO    INT4                 not null,
   NO_ATRIBUTO_NEGOCIAL VARCHAR(100)         not null,
   NO_ATRIBUTO_RETORNO  VARCHAR(100)         not null,
   IC_ATIVO             BOOL                 not null,
   IC_GED               BOOL                 not null,
   DE_ATRIBUTO_GED      VARCHAR(255)         not null,
   constraint PK_MTRTB045_ATRIBUTO_EXTRACAO primary key (NU_ATRIBUTO_EXTRACAO)
);

comment on table MTRSM001.MTRTB045_ATRIBUTO_EXTRACAO is
'Tabela utilizada para identificar os atributos que devem ser extraidos pelas rotinas automaticas de OCR/ICR ou de complementação utilizadas para alimentar a tabela 007 de atributos do documento';

comment on column MTRSM001.MTRTB045_ATRIBUTO_EXTRACAO.NU_ATRIBUTO_EXTRACAO is
'Atributo que representa a chave primaria da entidade.';

comment on column MTRSM001.MTRTB045_ATRIBUTO_EXTRACAO.NU_VERSAO is
'Campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column MTRSM001.MTRTB045_ATRIBUTO_EXTRACAO.NU_TIPO_DOCUMENTO is
'Atributo que representa o tipo de documento vinculado cujo atributo deve ser extraido.';

comment on column MTRSM001.MTRTB045_ATRIBUTO_EXTRACAO.NO_ATRIBUTO_NEGOCIAL is
'Atributo utilizado para armazenar o nome do atributo identificado pelo negocio. Esse valor armazena o nome utilizado no dia a dia e que pode ser apresentado como algum label no sistema quando necessario.';

comment on column MTRSM001.MTRTB045_ATRIBUTO_EXTRACAO.NO_ATRIBUTO_RETORNO is
'Atributo utilizado para armazenar o nome do atributo retornado pela rotina/serviço de extração de dados do documento. Esse valor armazena o nome utilizado no campo de retorno da informação.';

comment on column MTRSM001.MTRTB045_ATRIBUTO_EXTRACAO.IC_ATIVO is
'Atributo utilizado para definir se o atributo deve ser procurado para captura no objeto de retorno dos dados extaidos do documento.';

comment on column MTRSM001.MTRTB045_ATRIBUTO_EXTRACAO.IC_GED is
'Atributo utilizado para indicar que o atributo deve ser utilizado para alimentar a informação do GED';

comment on column MTRSM001. MTRTB045_ATRIBUTO_EXTRACAO.DE_ATRIBUTO_GED is
'Atributo utilizado para definir o nome do atributo do GED que tem relação com o atributo do documento.';

alter table MTRSM001.MTRTB045_ATRIBUTO_EXTRACAO
   add constraint FK_MTRTB045_FK_MTRTB0_MTRTB009 foreign key (NU_TIPO_DOCUMENTO)
      references MTRSM001.MTRTB009_TIPO_DOCUMENTO (NU_TIPO_DOCUMENTO)
      on delete restrict on update restrict;

      
----------------------------- TABELA MTRTB003-----------------------------
ALTER TABLE mtrsm001.mtrtb003_documento RENAME COLUMN dt_validade TO ts_validade;
ALTER TABLE mtrsm001.mtrtb003_documento ADD COLUMN co_extracao VARCHAR(100);
ALTER TABLE mtrsm001.mtrtb003_documento ADD COLUMN ts_envio_extracao timestamp without time zone;
ALTER TABLE mtrsm001.mtrtb003_documento ADD COLUMN ts_retorno_extracao timestamp without time zone;
ALTER TABLE mtrsm001.mtrtb003_documento ADD COLUMN co_autenticidade VARCHAR(100);
ALTER TABLE mtrsm001.mtrtb003_documento ADD COLUMN ts_envio_autenticidade timestamp without time zone;
ALTER TABLE mtrsm001.mtrtb003_documento ADD COLUMN ts_retorno_autenticidade timestamp without time zone;
ALTER TABLE mtrsm001.mtrtb003_documento ADD COLUMN ix_antifraude DECIMAL(5,2);
----------------------------- TABELA MTRTB003-----------------------------      

----------------------------- TABELA MTRTB024-----------------------------
ALTER TABLE MTRSM001.MTRTB024_PRODUTO_DOSSIE ALTER COLUMN VR_CONTRATO DROP NOT NULL;
ALTER TABLE MTRSM001.MTRTB024_PRODUTO_DOSSIE ALTER COLUMN PC_JUROS_OPERACAO DROP NOT NULL;
ALTER TABLE MTRSM001.MTRTB024_PRODUTO_DOSSIE ALTER COLUMN IC_PERIODO_JUROS DROP NOT NULL;
ALTER TABLE MTRSM001.MTRTB024_PRODUTO_DOSSIE ALTER COLUMN PZ_OPERACAO DROP NOT NULL;
ALTER TABLE MTRSM001.MTRTB024_PRODUTO_DOSSIE ALTER COLUMN PZ_CARENCIA DROP NOT NULL;
ALTER TABLE MTRSM001.MTRTB024_PRODUTO_DOSSIE ALTER COLUMN IC_LIQUIDACAO DROP NOT NULL;
ALTER TABLE MTRSM001.MTRTB024_PRODUTO_DOSSIE ALTER COLUMN CO_CONTRATO_RENOVADO DROP NOT NULL;
----------------------------- TABELA MTRTB024-----------------------------