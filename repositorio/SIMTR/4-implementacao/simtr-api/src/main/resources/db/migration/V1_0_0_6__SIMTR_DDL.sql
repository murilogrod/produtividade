
CREATE SEQUENCE MTRSM001.MTRSQ102_SICLI_ERRO
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--SICLIE ERRO
/*==============================================================*/
/* Table: MTRTB102_SICLI_ERRO                     */
/*==============================================================*/
create table MTRSM001.MTRTB102_SICLI_ERRO 
(
   NU_SICLI_ERRO  		INT8	NOT NULL DEFAULT nextval('MTRSM001.MTRSQ102_SICLI_ERRO'::regclass),
   CO_MATRICULA			CHAR(7) NOT NULL,
   IP_USUARIO			VARCHAR(15) NOT NULL,
   DNS_USUARIO			VARCHAR(70) NOT NULL,
   TS_ERRO        		TIMESTAMP WITHOUT TIME ZONE  NOT NULL,
   CO_IDENTIFICACAO		INT8 	NOT NULL,
   IC_TIPO_PESSOA		CHAR(1) NOT NULL,
   DE_ERRO				TEXT 	NOT NULL,
   constraint PK_MTRTB102_SICLI_ERRO primary key (NU_SICLI_ERRO)
);

COMMENT on table MTRSM001.MTRTB102_SICLI_ERRO IS
'Tabela utilizada para armazenar os erros de comunicação e resposta do SECLI';

COMMENT on column MTRSM001.MTRTB102_SICLI_ERRO.NU_SICLI_ERRO IS
'Atributo que representa a chave primária da entidade.';

COMMENT on column MTRSM001.MTRTB102_SICLI_ERRO.CO_MATRICULA IS
'Atributo que representa a matricula do usuário que realizou a pesquisa';

COMMENT on column MTRSM001.MTRTB102_SICLI_ERRO.IP_USUARIO IS
'Atributo que representa o IP da máquina do usuário que solicitou a pesquisa';

COMMENT on column MTRSM001.MTRTB102_SICLI_ERRO.DNS_USUARIO IS
'Atributo que representa o DNS da máquina do usuário que solicitou a pesquisa';

COMMENT on column MTRSM001.MTRTB102_SICLI_ERRO.TS_ERRO IS
'Atributo utilizado para registar a data hora(com segundos) da pesquisa realizada';

COMMENT on column MTRSM001.MTRTB102_SICLI_ERRO.CO_IDENTIFICACAO IS
'Atributo utilizado para referenciar o número de identificação do cliente pesquisado no SICLI';

COMMENT ON COLUMN MTRSM001.MTRTB102_SICLI_ERRO.IC_TIPO_PESSOA IS 'Atributo que determina qual tipo de pessoa possa ter.
Pode assumir os seguintes valores:
F - Física
J - Jurídica
C - Cocli';

COMMENT ON COLUMN MTRSM001.MTRTB102_SICLI_ERRO.DE_ERRO IS 
'Atributo para descrever os detalhes do erro ocorrido';

-------------------------------mtrtb015_situacao_documento-------------------------------
INSERT INTO mtrsm001.mtrtb015_situacao_documento (nu_situacao_documento, nu_versao, no_situacao, ic_situacao_inicial, ic_situacao_final) VALUES (2, 1, 'Validado', false, false);
INSERT INTO mtrsm001.mtrtb015_situacao_documento (nu_situacao_documento, nu_versao, no_situacao, ic_situacao_inicial, ic_situacao_final) VALUES (1, 1, 'Não Verificado', true, false);
INSERT INTO mtrsm001.mtrtb015_situacao_documento (nu_situacao_documento, nu_versao, no_situacao, ic_situacao_inicial, ic_situacao_final) VALUES (3, 1, 'Pendente', false, false);
INSERT INTO mtrsm001.mtrtb015_situacao_documento (nu_situacao_documento, nu_versao, no_situacao, ic_situacao_inicial, ic_situacao_final) VALUES (4, 1, 'Rejeitado', false, false);
-------------------------------fim mtrtb015_situacao_documento-------------------------------

-------------------------------mtrtb016_motivo_stco_documento-------------------------------
INSERT INTO mtrsm001.mtrtb016_motivo_stco_documento (nu_motivo_stco_documento, nu_versao, nu_situacao_documento, no_motivo_stco_documento) VALUES (1, 1, 3, 'Recaptura');
INSERT INTO mtrsm001.mtrtb016_motivo_stco_documento (nu_motivo_stco_documento, nu_versao, nu_situacao_documento, no_motivo_stco_documento) VALUES (2, 1, 4, 'Ilegível');
INSERT INTO mtrsm001.mtrtb016_motivo_stco_documento (nu_motivo_stco_documento, nu_versao, nu_situacao_documento, no_motivo_stco_documento) VALUES (3, 1, 4, 'Rasurado');
INSERT INTO mtrsm001.mtrtb016_motivo_stco_documento (nu_motivo_stco_documento, nu_versao, nu_situacao_documento, no_motivo_stco_documento) VALUES (4, 1, 4, 'Segurança');
-------------------------------fim mtrtb016_motivo_stco_documento-------------------------------

