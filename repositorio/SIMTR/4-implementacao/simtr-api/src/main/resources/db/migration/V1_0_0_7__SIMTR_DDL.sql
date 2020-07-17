-------------------TASK 48578-------------------
ALTER TABLE MTRSM001.MTRTB020_PROCESSO
  DROP CONSTRAINT fk_mtrtb020_mtrtb019,
  DROP COLUMN NU_MACROPROCESSO;

DROP TABLE MTRSM001.MTRTB019_MACROPROCESSO;
-----------------------------------------------------
ALTER TABLE MTRSM001.MTRTB042_CAMPO_FORMULARIO
  DROP CONSTRAINT fk_mtrtb042_reference_mtrtb026,
  DROP COLUMN nu_formulario;

DROP TABLE MTRSM001.MTRTB026_FORMULARIO;

/*==============================================================*/
/* Table: MTRTB026_RELACAO_PROCESSO                             */
/*==============================================================*/
create table MTRSM001.MTRTB026_RELACAO_PROCESSO 
(
   NU_PROCESSO_PAI      INT4                           not null,
   NU_PROCESSO_FILHO    INT4                           not null,
   NU_VERSAO            INT4                           not null,
   NU_PRIORIDADE        INT4                           null,
   NU_ORDEM             INT4                           null,
   constraint PK_MTRTB026 primary key (NU_PROCESSO_PAI, NU_PROCESSO_FILHO)
);

comment on table MTRSM001.MTRTB026_RELACAO_PROCESSO is 
'Tabela de auto relacionamento da tabela de processos utilizada para identificar a relação entre os mesmos.';

comment on column MTRSM001.MTRTB026_RELACAO_PROCESSO.NU_PROCESSO_PAI is 
'Atributo que representa o processo pai da relação entre os processos. Os processos "pai" são os processos que estão em nivel superior em uma visão de arvore de processos.
Os processos que não possuem registro com outro processo pai são conhecidos como processos patriarcas e estes são os processos inicialmente exibidos nas telas de tratamento e/ou visão de arvores.';

comment on column MTRSM001.MTRTB026_RELACAO_PROCESSO.NU_PROCESSO_FILHO is 
'Atributo que representa o processo filho da relação entre os processos. Os processos "filho" são os processos que estão em nivel inferior em uma visão de organograma dos processos.';

comment on column MTRSM001.MTRTB026_RELACAO_PROCESSO.NU_VERSAO is 
'Campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column MTRSM001.MTRTB026_RELACAO_PROCESSO.NU_PRIORIDADE is 
'Atributo que determina a ordem de prioridade de atendimento do processo na fila de tratamento sob a otica do processo pai. 

Apenas processos do tipo "dossiê" deverão ter a possibilidade de ser priorizados.

Uma vez definida a prioridade para um processo, todos os registros que possuem o mesmo processo pai necessitarão ser definidos.

Quando processos são priorizados, a visão para tratamento deve ser restrita e apresentar apenas a visão do processo patriarca com o botão de captura para tratamento fazendo com que o operador não saiba o dossiê de qual processo irá tratar.';

comment on column MTRSM001.MTRTB026_RELACAO_PROCESSO.NU_ORDEM is 
'Atributo utilizado para definir a ordem de execução dos processos filho sob a otica do processo pai determinando a sequencia de execução das etapas do processo.

Este atributo não deverá ser preenchido para os processos patriarcas e/ou processos de definição de dossiê de produto.';

alter table MTRSM001.MTRTB026_RELACAO_PROCESSO
   add constraint FK_MTRTB026_MTRTB020_01 foreign key (NU_PROCESSO_PAI)
      references MTRSM001.MTRTB020_PROCESSO (NU_PROCESSO)
      on update restrict
      on delete restrict;

alter table MTRSM001.MTRTB026_RELACAO_PROCESSO
   add constraint FK_MTRTB026_MTRTB020_02 foreign key (NU_PROCESSO_FILHO)
      references MTRSM001.MTRTB020_PROCESSO (NU_PROCESSO)
      on update restrict
      on delete restrict;
----------------------------------------

ALTER TABLE mtrsm001.mtrtb002_dossie_produto
 RENAME COLUMN ts_criacao TO ts_finalizado;
 
ALTER TABLE mtrsm001.mtrtb002_dossie_produto 
 ALTER COLUMN ts_finalizado DROP NOT NULL; 
 
 ALTER TABLE mtrsm001.mtrtb002_dossie_produto
 DROP COLUMN co_matricula_criacao; 
 
 ALTER TABLE mtrsm001.mtrtb002_dossie_produto
 ADD COLUMN IC_CANAL_CAIXA       VARCHAR(3)      null; 

 UPDATE mtrsm001.mtrtb002_dossie_produto
 set ic_canal_caixa = 'AGE'; 

 ALTER TABLE mtrsm001.mtrtb002_dossie_produto
 ALTER COLUMN IC_CANAL_CAIXA  SET NOT NULL; 
 
 ALTER TABLE mtrsm001.mtrtb002_dossie_produto
 RENAME COLUMN nu_processo TO nu_processo_dossie;
 ALTER TABLE mtrsm001.mtrtb002_dossie_produto 
 ADD COLUMN NU_PROCESSO_FASE     INT4                           not null;
 
 ALTER TABLE mtrsm001.mtrtb002_dossie_produto
DROP CONSTRAINT fk_mtrtb002_mtrtb020;

ALTER TABLE mtrsm001.MTRTB002_DOSSIE_PRODUTO
   ADD CONSTRAINT FK_MTRTB002_MTRTB020_01 foreign key (NU_PROCESSO_DOSSIE)
      references mtrsm001.MTRTB020_PROCESSO (NU_PROCESSO)
      on update restrict
      on delete restrict;

alter table mtrsm001.MTRTB002_DOSSIE_PRODUTO
   add constraint FK_MTRTB002_MTRTB020_02 foreign key (NU_PROCESSO_FASE)
      references mtrsm001.MTRTB020_PROCESSO (NU_PROCESSO)
      on update restrict
      on delete restrict;
      
ALTER TABLE mtrsm001.mtrtb002_dossie_produto
DROP COLUMN nu_fase_utilizacao;
      
 
----------------------------------------

 ALTER TABLE mtrsm001.mtrtb033_tipo_garantia
 RENAME TO mtrtb033_garantia;
 
 ALTER SEQUENCE mtrsm001.mtrsq033_tipo_garantia 
RENAME TO mtrsq033_garantia;

ALTER TABLE mtrsm001.mtrtb033_garantia 
RENAME COLUMN nu_tipo_garantia TO nu_garantia;
 
 ----------------------------------------

 ALTER TABLE mtrsm001.mtrtb020_processo
  DROP COLUMN nu_prioridade_macroprocesso,
  DROP COLUMN nu_quantidade_tratamento,
  DROP COLUMN ic_tipo_processo;
  
 ALTER TABLE mtrsm001.mtrtb020_processo
  ADD COLUMN IC_DOSSIE            BOOL                           not null default false,
  ADD COLUMN IC_CONTROLA_VALIDADE_DOCUMENTO BOOL                           null,
  ADD COLUMN IC_TIPO_PESSOA       VARCHAR(1)                     null,
  ADD COLUMN IC_MODELO_CARGA_DOCUMENTOS VARCHAR(2)                     null;
  
  UPDATE mtrsm001.mtrtb020_processo
 SET IC_CONTROLA_VALIDADE_DOCUMENTO = false;
 
 ALTER TABLE mtrsm001.mtrtb020_processo
 ALTER COLUMN IC_CONTROLA_VALIDADE_DOCUMENTO  SET NOT NULL; 

 UPDATE mtrsm001.mtrtb020_processo
 SET IC_TIPO_PESSOA = 'J';

  ALTER TABLE mtrsm001.mtrtb020_processo
 ALTER COLUMN IC_TIPO_PESSOA  SET NOT NULL; 

 UPDATE mtrsm001.mtrtb020_processo
 SET IC_MODELO_CARGA_DOCUMENTOS = 'AD ';

  ALTER TABLE mtrsm001.mtrtb020_processo
 ALTER COLUMN IC_MODELO_CARGA_DOCUMENTOS  SET NOT NULL; 
 
 -------------------------------------
 
 ALTER TABLE mtrsm001.mtrtb042_campo_formulario
 RENAME TO mtrtb019_campo_formulario;
 
 ALTER TABLE mtrsm001.mtrtb019_campo_formulario
ADD COLUMN NO_CAMPO             VARCHAR(50)                    not null;

comment on column mtrsm001.MTRTB019_CAMPO_FORMULARIO.NO_CAMPO is 
'Atributo que indica o nome do campo. Este nome pode ser utilizado pela estrutura de programação para referenciar a campo no formulario independente do label exposto para o usuário.';
 
 ALTER TABLE mtrsm001.mtrtb019_campo_formulario	
 ADD COLUMN NU_PROCESSO          INT4 ;

UPDATE mtrsm001.mtrtb019_campo_formulario
 SET NU_PROCESSO = 1; 

ALTER TABLE mtrsm001.mtrtb019_campo_formulario	
 ALTER COLUMN NU_PROCESSO  SET NOT NULL;
 

alter table mtrsm001.MTRTB019_CAMPO_FORMULARIO
   add constraint FK_MTRTB019_FK_MTRTB0_MTRTB020 foreign key (NU_PROCESSO)
      references mtrsm001.MTRTB020_PROCESSO (NU_PROCESSO)
      on update restrict
      on delete restrict;
	  
ALTER TABLE mtrsm001.mtrtb019_campo_formulario RENAME COLUMN nu_largura to nu_versao;

ALTER TABLE mtrsm001.mtrtb019_campo_formulario ALTER COLUMN nu_versao SET NOT NULL;

COMMENT ON COLUMN mtrsm001.mtrtb019_campo_formulario.nu_versao IS 'Atributo que armazena a versão';

-------------------------------------------------------------------

DROP TABLE mtrsm001.mtrtb043_documento_tp_garantia;

/*==============================================================*/
/* Table: MTRTB043_DOCUMENTO_GARANTIA                           */
/*==============================================================*/
create table mtrsm001.MTRTB043_DOCUMENTO_GARANTIA 
(
   NU_GARANTIA          INT4                           null,
   NU_TIPO_DOCUMENTO    INT4                           null,
   NU_PROCESSO          INT4                           null,
   constraint PK_MTRTB043_DOCUMENTO_GARANTIA primary key (NU_GARANTIA, NU_TIPO_DOCUMENTO, NU_PROCESSO)
);

comment on table mtrsm001.MTRTB043_DOCUMENTO_GARANTIA is 
'Tabela utilizada para identificar os documentos (por tipo ou função) que são necessários para o tipo de garantia';

comment on column mtrsm001.MTRTB043_DOCUMENTO_GARANTIA.NU_GARANTIA is 
'Atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.MTRTB043_DOCUMENTO_GARANTIA.NU_TIPO_DOCUMENTO is 
'Atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.MTRTB043_DOCUMENTO_GARANTIA.NU_PROCESSO is 
'Atributo que representa a chave primaria da entidade.';

alter table mtrsm001.MTRTB043_DOCUMENTO_GARANTIA
   add constraint FK_MTRTB043_MTRTB009 foreign key (NU_TIPO_DOCUMENTO)
      references mtrsm001.MTRTB009_TIPO_DOCUMENTO (NU_TIPO_DOCUMENTO)
      on update restrict
      on delete restrict;

alter table mtrsm001.MTRTB043_DOCUMENTO_GARANTIA
   add constraint FK_MTRTB043_MTRTB020 foreign key (NU_PROCESSO)
      references mtrsm001.MTRTB020_PROCESSO (NU_PROCESSO)
      on update restrict
      on delete restrict;

alter table mtrsm001.MTRTB043_DOCUMENTO_GARANTIA
   add constraint FK_MTRTB043_MTRTB033 foreign key (NU_GARANTIA)
      references mtrsm001.MTRTB033_GARANTIA (NU_GARANTIA)
      on update restrict
      on delete restrict; 
      
-------------------------------------------------------------------------------------------

/*==============================================================*/
/* Table: MTRTB042_CLIENTE_GARANTIA                             */
/*==============================================================*/
create table mtrsm001.MTRTB042_CLIENTE_GARANTIA 
(
   NU_GARANTIA_INFORMADA INT8                      NOT null,
   NU_DOSSIE_CLIENTE    INT8                      NOT null,
   constraint PK_MTRTB043 primary key (NU_GARANTIA_INFORMADA, NU_DOSSIE_CLIENTE)
);

comment on table mtrsm001.MTRTB042_CLIENTE_GARANTIA is 
'Tabela de relacionamento entre o dossiê cliente (tb001) e a garantia informada (tb035). Representa as garantias que têm pessoas relacionadas, garantias do tipo fidejussórias (aval, fiador, etc).';

comment on column mtrsm001.MTRTB042_CLIENTE_GARANTIA.NU_GARANTIA_INFORMADA is 
'Atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.MTRTB042_CLIENTE_GARANTIA.NU_DOSSIE_CLIENTE is 
'Atributo que representa a chave primaria da entidade.';

alter table mtrsm001.MTRTB042_CLIENTE_GARANTIA
   add constraint FK_MTRTB042_MTRTB001 foreign key (NU_DOSSIE_CLIENTE)
      references mtrsm001.MTRTB001_DOSSIE_CLIENTE (NU_DOSSIE_CLIENTE)
      on update restrict
      on delete restrict;

alter table mtrsm001.MTRTB042_CLIENTE_GARANTIA
   add constraint FK_MTRTB042_MTRTB035 foreign key (NU_GARANTIA_INFORMADA)
      references mtrsm001.MTRTB035_GARANTIA_INFORMADA (NU_GARANTIA_INFORMADA)
      on update restrict
      on delete restrict;
---------------------------------------------------------------------------------
DROP TABLE mtrsm001.mtrtb044_produto_documento;
--------------------------------------------------------------------------------
ALTER TABLE mtrsm001.mtrtb027_campo_entrada
DROP COLUMN no_campo;
-------------------fim TASK 48578-------------------