----------------------------------------------------nome tabela--------------------------------------------------
ALTER TABLE mtrsm001.mtrtb016_motivo_situacao_dcmnto RENAME TO mtrtb016_motivo_stco_documento;
alter sequence mtrsm001.mtrsq016_motivo_situacao_dcmnto RENAME TO mtrsq016_motivo_stco_documento;

ALTER TABLE mtrsm001.mtrtb017_situacao_instnca_dcmnto RENAME TO mtrtb017_stco_instnca_documento;
ALTER sequence mtrsm001.mtrsq017_situacao_instnca_dcmnto RENAME TO mtrsq017_stco_instnca_documento;

ALTER TABLE mtrsm001.mtrtb033_garantia RENAME TO mtrtb033_tipo_garantia;
ALTER sequence mtrsm001.mtrsq033_garantia RENAME TO mtrsq033_tipo_garantia;

ALTER TABLE mtrsm001.mtrtb041_cadeia_situacao_dcto RENAME TO mtrtb041_cadeia_stco_documento;

ALTER TABLE mtrsm001.mtrtb040_cadeia_situacao_dossie RENAME TO mtrtb040_cadeia_tipo_stco_dossie;

ALTER TABLE mtrsm001.mtrtb101_documento RENAME TO mtrtb101_autorizacao_documento;
ALTER sequence mtrsm001.mtrsq101_documento RENAME TO mtrsq101_autorizacao_documento;

drop sequence mtrsm001.mtrtb029_campo_apresentacao_nu_campo_formulario_seq cascade;

drop sequence mtrsm001.mtrtb030_resposta_dossie_nu_campo_formulario_seq cascade;


--------------------------------------------------fim nome tabela--------------------------------------------------

----------------------------------------------nome atributo--------------------------------------------------------
alter table mtrsm001.mtrtb033_tipo_garantia rename column nu_garantia to nu_tipo_garantia;
alter table mtrsm001.mtrtb033_tipo_garantia rename column nu_operacao to nu_garantia_bacen;
alter table mtrsm001.mtrtb002_dossie_produto rename column nu_cgc_criacao to nu_unidade_criacao;
alter table mtrsm001.mtrtb002_dossie_produto rename column nu_cgc_priorizado to nu_unidade_priorizado;
alter table mtrsm001.mtrtb013_situacao_dossie rename column nu_cgc_unidade to nu_unidade;
alter table mtrsm001.mtrtb016_motivo_stco_documento rename column nu_motivo_situacao_dcto  to nu_motivo_stco_documento;
alter table mtrsm001.mtrtb016_motivo_stco_documento rename column no_motivo_situacao_dcto  to no_motivo_stco_documento;
alter table mtrsm001.mtrtb017_stco_instnca_documento rename column nu_cgc_unidade to nu_unidade;
alter table mtrsm001.mtrtb017_stco_instnca_documento rename column nu_situacao_instancia_dcto to nu_stco_instnca_documento;
alter table mtrsm001.mtrtb017_stco_instnca_documento rename column nu_motivo_situacao_dcto to nu_motivo_stco_documento;
alter table mtrsm001.mtrtb018_unidade_tratamento rename column nu_cgc_unidade to nu_unidade;
alter table mtrsm001.mtrtb021_unidade_autorizada rename column nu_cgc to nu_unidade;
alter table mtrsm001.mtrtb032_elemento_conteudo rename column nu_qtde_obrigatorio to qt_elemento_obrigatorio;
alter table mtrsm001.mtrtb009_tipo_documento rename column ic_tipo_pessoa to ic_tipo_documento;


---------------------------------------------inclusão de colunas --------------------------------------------------------

alter table mtrsm001.mtrtb001_pessoa_juridica add column VR_FATURAMENTO_ANUAL DECIMAL(15,2)        null;
alter table mtrsm001.mtrtb001_pessoa_fisica add column VR_RENDA_MENSAL      DECIMAL(15,2)        null;
alter table mtrsm001.mtrtb033_tipo_garantia add column IC_FIDEJUSSORIA      BOOL                 null;
alter table mtrsm001.mtrtb035_garantia_informada add column DE_GARANTIA          TEXT                 null;
alter table mtrsm001.mtrtb035_garantia_informada add column NU_DOSSIE_CLIENTE    INT8                 null;
alter table mtrsm001.mtrtb002_dossie_produto add column  ts_criacao timestamp without time zone ;
update mtrsm001.mtrtb002_dossie_produto  set ts_criacao = '2017-12-26 00:00:00';
alter table mtrsm001.mtrtb002_dossie_produto alter column ts_criacao set not null;
alter table mtrsm001.mtrtb002_dossie_produto add column co_matricula_criacao character(7) null;
update mtrsm001.mtrtb002_dossie_produto set co_matricula_criacao = 'S000000' ;
alter table mtrsm001.mtrtb002_dossie_produto alter column co_matricula_criacao set default not null;


-----------------------------------------criação da tabelas---------------------------------------------------
create table MTRSM001.MTRTB043_DOCUMENTO_TP_GARANTIA (
   NU_DOCUMENTO_TIPO_GARANTIA SERIAL               not null,
   NU_TIPO_GARANTIA     INT4                 null,
   NU_TIPO_DOCUMENTO    INT4                 null,
   NU_PROCESSO          INT4                 null,
   NU_VERSAO            INT4                 null,
   constraint PK_MTRTB043_DOCUMENTO_TIPO_GAR primary key (NU_DOCUMENTO_TIPO_GARANTIA)
);

alter sequence mtrsm001.mtrtb043_documento_tp_garantia_nu_documento_tipo_garantia_seq rename to mtrsq043_documento_tp_garantia;

comment on table MTRSM001.MTRTB043_DOCUMENTO_TP_GARANTIA is
'Tabela utilizada para identificar os documentos (por tipo ou função) que são necessários para o tipo de garantia. 
';

comment on column MTRSM001.MTRTB043_DOCUMENTO_TP_GARANTIA.NU_DOCUMENTO_TIPO_GARANTIA is
'Atributo que representa a chave primária da entidade.';

comment on column MTRSM001.MTRTB043_DOCUMENTO_TP_GARANTIA.NU_TIPO_GARANTIA is
'Atributo utilizado para identificar o tipo de garantia de vinculação para agrupar os documentos necessários.';

comment on column MTRSM001.MTRTB043_DOCUMENTO_TP_GARANTIA.NU_TIPO_DOCUMENTO is
'Atributo utilizado para referenciar um tipo de documento específico, necessário ao tipo de garantia. ';

comment on column MTRSM001.MTRTB043_DOCUMENTO_TP_GARANTIA.NU_PROCESSO is
'Atributo utilizado para armazenar o nome de identificação negocial do processo.';

comment on column MTRSM001.MTRTB043_DOCUMENTO_TP_GARANTIA.NU_VERSAO is
'Campo de controle das versões do registro para viabilizar a concorrência otimista.';

alter table MTRSM001.MTRTB043_DOCUMENTO_TP_GARANTIA
   add constraint FK_MTRTB043_REFERENCE_MTRTB033 foreign key (NU_TIPO_GARANTIA)
      references MTRSM001.MTRTB033_TIPO_GARANTIA (NU_TIPO_GARANTIA)
      on delete restrict on update restrict;

alter table MTRSM001.MTRTB043_DOCUMENTO_TP_GARANTIA
   add constraint FK_MTRTB043_REFERENCE_MTRTB009 foreign key (NU_TIPO_DOCUMENTO)
      references MTRSM001.MTRTB009_TIPO_DOCUMENTO (NU_TIPO_DOCUMENTO)
      on delete restrict on update restrict;

alter table MTRSM001.MTRTB043_DOCUMENTO_TP_GARANTIA
   add constraint FK_MTRTB043_REFERENCE_MTRTB020 foreign key (NU_PROCESSO)
      references MTRSM001.MTRTB020_PROCESSO (NU_PROCESSO)
      on delete restrict on update restrict;
	  
	  
	  
create table MTRSM001.MTRTB044_PRODUTO_DOCUMENTO (
   NU_PRODUTO_DOCUMENTO SERIAL               not null,
   NU_PRODUTO           INT4                 null,
   NU_TIPO_DOCUMENTO    INT4                 null,
   NU_PROCESSO          INT4                 null,
   NU_VERSAO            INT4                 not null,
   constraint PK_MTRTB044_PRODUTO_DOCUMENTO primary key (NU_PRODUTO_DOCUMENTO)
);

alter sequence mtrsm001.mtrtb044_produto_documento_nu_produto_documento_seq rename to mtrsq044_produto_documento;

comment on table MTRSM001.MTRTB044_PRODUTO_DOCUMENTO is
'Tabela utilizada para identificar os documentos (por tipo ou função) que são necessários para o documento de um processo específico.';

comment on column MTRSM001.MTRTB044_PRODUTO_DOCUMENTO.NU_PRODUTO_DOCUMENTO is
'Atributo que representa a chave primária da entidade.';

comment on column MTRSM001.MTRTB044_PRODUTO_DOCUMENTO.NU_PRODUTO is
'Atributo utilizado para identificar o produto de vinculação para agrupar os documentos necessários.';

comment on column MTRSM001.MTRTB044_PRODUTO_DOCUMENTO.NU_TIPO_DOCUMENTO is
'Atributo utilizado para referenciar um tipo de documento específico, necessário ao produto. ';

comment on column MTRSM001.MTRTB044_PRODUTO_DOCUMENTO.NU_PROCESSO is
'Atributo utilizado para armazenar o nome de identificação negocial do processo.';

comment on column MTRSM001.MTRTB044_PRODUTO_DOCUMENTO.NU_VERSAO is
'Campo de controle das versões do registro para viabilizar a concorrência otimista.';

alter table MTRSM001.MTRTB044_PRODUTO_DOCUMENTO
   add constraint FK_MTRTB044_REFERENCE_MTRTB022 foreign key (NU_PRODUTO)
      references MTRSM001.MTRTB022_PRODUTO (NU_PRODUTO)
      on delete restrict on update restrict;

alter table MTRSM001.MTRTB044_PRODUTO_DOCUMENTO
   add constraint FK_MTRTB044_REFERENCE_MTRTB009 foreign key (NU_TIPO_DOCUMENTO)
      references MTRSM001.MTRTB009_TIPO_DOCUMENTO (NU_TIPO_DOCUMENTO)
      on delete restrict on update restrict;

alter table MTRSM001.MTRTB044_PRODUTO_DOCUMENTO
   add constraint FK_MTRTB044_REFERENCE_MTRTB020 foreign key (NU_PROCESSO)
      references MTRSM001.MTRTB020_PROCESSO (NU_PROCESSO)
      on delete restrict on update restrict;
  
	  
COMMENT ON COLUMN mtrsm001.mtrtb009_tipo_documento.ic_tipo_documento IS 'Atributo que determina qual tipo de pessoa pode ter o documento atribuído.
Pode assumir os seguintes valores:
F - Física
J - Jurídica
S - Serviço
A - Física ou Jurídica
G - Garantia
P - Produto
T - Todos';


COMMENT ON COLUMN mtrsm001.mtrtb004_dossie_cliente_produto.ic_tipo_relacionamento IS 'Atributo que indica o tipo de relacionamento do cliente com o produto, podendo assumir os valores:
- AVALISTA
- CONJUGE
- CONJUGE_SOCIO
- FIADOR
- SOCIO
- SEGUNDO_TITULAR
- TOMADOR_PRIMEIRO_TITULAR
- CONGLOMERADO
- EMPRESA CONGLOMERADO
etc';

comment on column mtrsm001.MTRTB002_DOSSIE_PRODUTO.TS_CRIACAO is
'Atributo que armazena a data e hora que foi realizada a inclusão do dossiê.';

comment on column mtrsm001.MTRTB002_DOSSIE_PRODUTO.CO_MATRICULA_CRIACAO is
'Atributo que armazena a matrícula do usuário que realizou a inclusão do dossiê.';