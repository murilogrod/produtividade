DELETE FROM mtrsm001.mtrtb029_campo_apresentacao;
DELETE FROM mtrtb030_resposta_dossie;

ALTER SEQUENCE mtrsm001.mtrsq005_dossie_cliente_produto RESTART WITH 20;

--CAMPO ENTRADA--
ALTER TABLE MTRSM001.MTRTB027_CAMPO_ENTRADA
DROP COLUMN NU_FORMULARIO,
DROP COLUMN NU_ORDEM,
DROP COLUMN nu_largura,
DROP COLUMN ic_obrigatorio,
DROP COLUMN de_expressao,
DROP COLUMN ic_ativo;
----------------------------------------------------------------------------------------------
--CAMPO FORMULARIO
/*==============================================================*/
/* Table: MTRTB042_CAMPO_FORMULARIO                     */
/*==============================================================*/
create table MTRSM001.MTRTB042_CAMPO_FORMULARIO 
(
  NU_CAMPO_FORMULARIO  BIGSERIAL            not null,
   NU_FORMULARIO        INT4                 null,
   NU_CAMPO_ENTRADA     INT8                 not null,
   NU_LARGURA           INT4                 not null,
   IC_OBRIGATORIO       BOOL                 not null,
   DE_EXPRESSAO         TEXT                 null,
   IC_ATIVO             BOOL                 not null,
   NU_ORDEM             INT4                 not null,
   constraint PK_MTRTB042_CAMPO_FORMULARIO primary key (NU_CAMPO_FORMULARIO)
);

comment on table MTRSM001.MTRTB042_CAMPO_FORMULARIO is
'Tabela criada para armazenar os campos de entrada que foram vinculados a um formulario e serão alimentados na inclusão de um novo dossiê para o processo vinculado.
Esta estrutura permitirá realizar a construção dinâmica do formulário.
Registros desta tabela só devem ser excluídos fisicamente caso não exista nenhuma resposta de formulário atrelada a este registro. Caso essa situação ocorra o registro deve ser excluído logicamente definindo seu atributo ativo como false.';

comment on column MTRSM001.MTRTB042_CAMPO_FORMULARIO.NU_FORMULARIO is
'Atributo utilizado para referenciar o formulário ao qual o campo esteja vinculado.';

comment on column MTRSM001.MTRTB042_CAMPO_FORMULARIO.NU_CAMPO_ENTRADA is
'Atributo utilizado para identificar a campo de entrada relacionado no formulario. 
O campo de entrada determina o tipo de entrada (INPUT/SELECT/RADIO/etc) e suas caracteristicas de apresentação e validação';

comment on column MTRSM001.MTRTB042_CAMPO_FORMULARIO.NU_LARGURA is
'Atributo que armazena o número de colunas do bootstrap ocupadas pelo campo do formulário na estrutura de tela. Este valor pode variar de 1 a 12.';

comment on column MTRSM001.MTRTB042_CAMPO_FORMULARIO.IC_OBRIGATORIO is
'Atributo que armazena o indicativo do obrigatoriedade do campo no formulário.';

comment on column MTRSM001.MTRTB042_CAMPO_FORMULARIO.DE_EXPRESSAO is
'Atributo que armazena a expressão a ser aplicada pelo Java script para determinar a exposição ou não do campo no formulário.';

comment on column MTRSM001.MTRTB042_CAMPO_FORMULARIO.IC_ATIVO is
'Atributo que indica se o campo de entrada está apto ou não para ser inserido no formulário.';

comment on column MTRSM001.MTRTB042_CAMPO_FORMULARIO.NU_ORDEM is
'Atributo utilizado para definir a ordem de exibição dos campos do formulário.';

/*==============================================================*/
/* Index: IX_MTRTB027_01                                        */
/*==============================================================*/
create unique index IX_MTRTB042_01 on MTRSM001.MTRTB042_CAMPO_FORMULARIO (
NU_FORMULARIO ASC,
NU_CAMPO_ENTRADA ASC
);

alter table MTRSM001.MTRTB042_CAMPO_FORMULARIO
   add constraint FK_MTRTB042_MTRTB027 foreign key (NU_CAMPO_ENTRADA)
      references MTRSM001.MTRTB027_CAMPO_ENTRADA (NU_CAMPO_ENTRADA)
      on update restrict
      on delete restrict;

alter table MTRSM001.MTRTB042_CAMPO_FORMULARIO
   add constraint FK_MTRTB042_REFERENCE_MTRTB026 foreign key (NU_FORMULARIO)
      references MTRSM001.MTRTB026_FORMULARIO (NU_FORMULARIO)
      on update restrict
      on delete restrict;
	 

----------------------------------------------------------------------------------------------
--RESPOSTA DOSSIE--
ALTER TABLE MTRSM001.mtrtb030_resposta_dossie
DROP CONSTRAINT fk_mtrtb030_mtrtb027,
DROP COLUMN nu_campo_entrada;

ALTER TABLE MTRSM001.mtrtb030_resposta_dossie
ADD COLUMN NU_CAMPO_FORMULARIO BIGSERIAL                      not null,
ADD constraint FK_MTRTB030_MTRTB042 foreign key (NU_CAMPO_FORMULARIO)
      references MTRSM001.MTRTB042_CAMPO_FORMULARIO (NU_CAMPO_FORMULARIO)
      on update restrict
      on delete restrict;
	  
----------------------------------------------------------------------------------------------
--CAMPO APRESENTACAO--
ALTER TABLE MTRSM001.mtrtb029_campo_apresentacao
DROP CONSTRAINT fk_mtrtb029_mtrtb027,
DROP COLUMN nu_campo_entrada;

ALTER TABLE MTRSM001.mtrtb029_campo_apresentacao
ADD COLUMN NU_CAMPO_FORMULARIO BIGSERIAL                      not null,
ADD constraint FK_MTRTB029_MTRTB042 foreign key (NU_CAMPO_FORMULARIO)
      references MTRSM001.MTRTB042_CAMPO_FORMULARIO (NU_CAMPO_FORMULARIO)
      on update restrict
      on delete restrict;	  
	    
	  
------------------------------------------------------
---PROCESSO---
ALTER TABLE mtrsm001.mtrtb020_processo
ADD COLUMN   IC_TIPO_PROCESSO     VARCHAR        null;