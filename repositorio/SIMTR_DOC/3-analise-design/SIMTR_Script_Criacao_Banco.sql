
/*==============================================================*/
/* user: mtrsm001                                               */
/*==============================================================*/
create schema mtrsm001;

--*==============================================================*/
--/* table: mtrsm001.mtrtb001_dossie_cliente                     */
--/*==============================================================*/
create table mtrsm001.mtrtb001_dossie_cliente (
   nu_dossie_cliente    bigserial            not null,
   nu_versao            int4                 not null,
   nu_cpf_cnpj          int8                 not null,
   no_cliente           varchar(255)         not null,
   de_endereco          varchar(255)         not null,
   de_telefone          varchar(15)          null,
   constraint pk_mtrtb001_dossie_cliente primary key (nu_dossie_cliente)
);

comment on table mtrsm001.mtrtb001_dossie_cliente is
'tabela responsavel pelo armazenamento do dossie do cliente com seus respectivos dados.
nesta tabela serão efetivamente armazenados os dados do cliente.';

comment on column mtrsm001.mtrtb001_dossie_cliente.nu_dossie_cliente is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb001_dossie_cliente.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb001_dossie_cliente.nu_cpf_cnpj is
'atroibuto que representa o numero do cpf/cnpj do cliente';

comment on column mtrsm001.mtrtb001_dossie_cliente.no_cliente is
'atributo que representa o nome do cliente';

comment on column mtrsm001.mtrtb001_dossie_cliente.de_endereco is
'atributo que representa o endereço informado para o cliente';

comment on column mtrsm001.mtrtb001_dossie_cliente.de_telefone is
'atributo que representa o telefone informado para o cliente';

--/*==============================================================*/
--/* index: ix_mtrtb001_01                                        */
--/*==============================================================*/
create unique index ix_mtrtb001_01 on mtrsm001.mtrtb001_dossie_cliente (
nu_cpf_cnpj
);

/*==============================================================*/
/* table: mtrsm001.mtrtb001_pessoa_fisica                                */
/*==============================================================*/
create table mtrsm001.mtrtb001_pessoa_fisica (
   nu_dossie_cliente    int8                 not null,
   dt_nascimento        date                 not null,
   ic_estado_civil      char                 not null,
   nu_nis               varchar(20)          null,
   nu_identidade        varchar(15)          not null,
   no_orgao_emissor     varchar(15)          not null,
   no_mae               varchar(255)         not null,
   no_pai               varchar(255)         null,
   constraint pk_mtrtb001_pessoa_fisica primary key (nu_dossie_cliente)
);

comment on table mtrsm001.mtrtb001_pessoa_fisica is
'tabela de especialização do dossiê de cliente para armazenar os atributos especificos de uma pessoa fisica';

comment on column mtrsm001.mtrtb001_pessoa_fisica.dt_nascimento is
'campo utilizado para armazenar a data de nascimento de pessoas fisicas';

/*==============================================================*/
/* table: mtrsm001.mtrtb001_pessoa_juridica                              */
/*==============================================================*/
create table mtrsm001.mtrtb001_pessoa_juridica (
   nu_dossie_cliente    int8                 not null,
   no_razao_social      varchar(255)         null,
   dt_fundacao          date                 null,
   ic_segmento          varchar(10)          null,
   constraint pk_mtrtb001_pessoa_juridica primary key (nu_dossie_cliente)
);

comment on table mtrsm001.mtrtb001_pessoa_juridica is
'tabela de especialização do dossiê de cliente para armazenar os atributos especificos de uma pessoajuridica';

comment on column mtrsm001.mtrtb001_pessoa_juridica.ic_segmento is
'atributo para identificar o segmento da empresa, podendo assumir os valores oriundos da view do siico icotbxxx:
- mei
- mpe
- mge
- corp';

/*==============================================================*/
/* table: mtrsm001.mtrtb002_dossie_produto                               */
/*==============================================================*/
create table mtrsm001.mtrtb002_dossie_produto (
   nu_dossie_produto    bigserial            not null,
   nu_versao            int4                 not null,
   nu_processo          int4                 not null,
   co_cgc_criacao       int4                 not null,
   co_cgc_priorizado    int4                 null,
   co_matricula_priorizado varchar(7)           null,
   nu_peso_prioridade   int4                 null,
   nu_fase_utilizacao   int4                 not null,
   constraint pk_mtrtb002_dossie_produto primary key (nu_dossie_produto)
);

comment on table mtrsm001.mtrtb002_dossie_produto is
'tabela responsavel pelo armazenamento do dossie do produto com seus respectivos dados.
nesta tabela serão efetivamente armazenados os dados do produto.
para cada produto contratato ou submetido a analise deve ser gerado um novo registro representando o vinculo com o cliente.';

comment on column mtrsm001.mtrtb002_dossie_produto.nu_dossie_produto is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb002_dossie_produto.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb002_dossie_produto.nu_processo is
'atributo utilizado para referenciar o processo ao qual o dossiê de produto esteja vinculado.';

comment on column mtrsm001.mtrtb002_dossie_produto.co_cgc_criacao is
'atributo utrilizado para armazenar o cgc da undade de criação do dossiê';

comment on column mtrsm001.mtrtb002_dossie_produto.co_cgc_priorizado is
'atributo que indica o cgc da unidade que deverá tratar o dissiê na proxima chamada da fila por qualquer empregado vinculado ao mesmo.';

comment on column mtrsm001.mtrtb002_dossie_produto.co_matricula_priorizado is
'atributo que indica o empregado especifico da unidade que deverá tratar o dissiê na próxima chamada da fila.';

comment on column mtrsm001.mtrtb002_dossie_produto.nu_peso_prioridade is
'valor que indica dentre os dossiês priorizados, qual a ordem de captura na chamada da fila, sendo aplicado do maior para o menor. 
o valor é um numero livre atribuido pelo usuario que realizar a priorização do dossiê e a fila será organizada pelos dossiês priorizados com valor de peso do maior para o menor, em seguida pela ordem de cadastro definido pelo atributo de data de criação do mais antigo para o mais novo. ';

comment on column mtrsm001.mtrtb002_dossie_produto.nu_fase_utilizacao is
'atributo utilizado para identificar a fase de associação do mapa junto ao dossiê.
conforme a fase definida no dossiê do produto, o conjunto de imagens e campos do formulario ficam disponiveis para preenchimento, caso selecionado uma fase do processo diferente da atual do dossiê, o formulario e relação de documentos referente a outra fase ficam disponiveis apenas para consulta.';

/*==============================================================*/
/* table: mtrsm001.mtrtb003_documento                                    */
/*==============================================================*/
create table mtrsm001.mtrtb003_documento (
   nu_documento         bigserial            not null,
   nu_versao            int4                 not null,
   nu_tipo_documento    int4                 not null,
   ts_captura           timestamp            not null,
   co_matricula         varchar(7)           not null,
   dt_validade          date                 not null,
   ic_aceito            bool                 null,
   ic_dossie_digital    bool                 null,
   constraint pk_mtrtb003_documento primary key (nu_documento)
);

comment on table mtrsm001.mtrtb003_documento is
'tabela responsavel pelo armazenamento da referência dos documentos de um determinado cliente.
esses documentos podem estar associados a um ou mais dossiês de produtos possibilitando o reaproveitamento dos mesmos em diversos produtos.
nesta tabela serão efetivamente armazenados os dados dos documentos que pode representar o agrupamento de uma ou mais imagens na sua formação.
também deverão ser armazenadas as propriedades do mesmo e as marcas conforme seu ciclo de vida';

comment on column mtrsm001.mtrtb003_documento.nu_documento is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb003_documento.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb003_documento.nu_tipo_documento is
'atributo utilizado para armazenar a vinculação do tipo de documento referenciado pelo documento registrado';

comment on column mtrsm001.mtrtb003_documento.ts_captura is
'atributo que armazena a data e hora que foi realizada a captura do documento';

comment on column mtrsm001.mtrtb003_documento.co_matricula is
'atributo que armazena a matricula do usuario que realizou a captura do documento';

comment on column mtrsm001.mtrtb003_documento.dt_validade is
'atributo que armazena a data de validade do documento conforme definições corporativas calculado pelo prazo definido no tipo documento';

comment on column mtrsm001.mtrtb003_documento.ic_aceito is
'atributo que determina que o documento já foi aprovado por algum dossiê previo e que sua instancia foi aprovada pelo analista.
este atributo deve ser marcado exclusivamente pelo sistema ao aprovar o dossiê que faz a utilização do mesmo.
documentos marcados neste atributo como verdade serão indicados como previamente capturados em novos dossiês que façam utilização da sua tipologia dentro do prazo de validade definido. (instancia de documento criada automaticamente e atribuido ao novo dossiê de produto)
caso este atributo esteja nulo, significa que nenhuma instancia do mesmo foi tratada até o momento e por isso não existe parecer definido.';

comment on column mtrsm001.mtrtb003_documento.ic_dossie_digital is
'atributo utilizado para determinar que o documento passou por todas as etapas definidas pelo modelo do dossiê digital e que esta apto para reaproveitmanto em qualquer processo que utilize este modelo de conformidade.';

/*==============================================================*/
/* table: mtrsm001.mtrtb004_imagem                                       */
/*==============================================================*/
create table mtrsm001.mtrtb004_imagem (
   nu_imagem            bigserial            not null,
   nu_versao            int4                 not null,
   nu_documento         int8                 not null,
   de_uri               varchar(255)         null,
   co_ged               varchar(100)         null,
   constraint pk_mtrtb004_imagem primary key (nu_imagem)
);

comment on table mtrsm001.mtrtb004_imagem is
'tabela responsavel pelo armazenamento das referencia de imagens que compoem os documento.
nesta tabela serão efetivamente armazenados os dados que caracterizam as imagens e dados para localização do arquivo propriamente dito.';

comment on column mtrsm001.mtrtb004_imagem.nu_imagem is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb004_imagem.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

/*==============================================================*/
/* table: mtrsm001.mtrtb005_tipo_documento                               */
/*==============================================================*/
create table mtrsm001.mtrtb005_tipo_documento (
   nu_tipo_documento    serial               not null,
   nu_versao            int4                 not null,
   no_tipo_documento    varchar(100)         not null,
   ic_tipo_pessoa       char                 not null,
   pz_validade_dias     int4                 null,
   ic_validade_auto_contida bool                 null,
   constraint pk_mtrtb005_tipo_documento primary key (nu_tipo_documento)
);

comment on table mtrsm001.mtrtb005_tipo_documento is
'tabela responsavel pelo armazenamento dos possiveis tipos de documento que podem ser submetidos ao vinculo com os dossiês';

comment on column mtrsm001.mtrtb005_tipo_documento.nu_tipo_documento is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb005_tipo_documento.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb005_tipo_documento.no_tipo_documento is
'atributo que identifica o tipo de documento vinculado. como exemplo podemos ter:
- rg
- cnh
- certidão negativa de debito
- passaporte
- etc';

comment on column mtrsm001.mtrtb005_tipo_documento.ic_tipo_pessoa is
'atributo que determina qual tipo de pessoa pode ter o documento atribuido.
pode assumir os seguintes valores:
f - fisica
j - juridica
s - serviço
a - fisica ou juridica
t - todos';

comment on column mtrsm001.mtrtb005_tipo_documento.pz_validade_dias is
'atributo que indica a quantidade de dias para atribuição da validade do documento a partir da sua emissão.
caso o valor deste atributo não esteja definido, significa que o documento possui um prazo de validade indeterminado.';

comment on column mtrsm001.mtrtb005_tipo_documento.ic_validade_auto_contida is
'atributo determina se a validade do documento esta definida no proprio documento ou não como por exemplo no caso de certidões que possuem a validade determinada em seu corpo.
caso o valor deste atributo seja falso, o prazo de validade deve ser calculado conforme definido no atributo de prazo de validade.';

/*==============================================================*/
/* index: ix_mtrtb005_01                                        */
/*==============================================================*/
create unique index ix_mtrtb005_01 on mtrsm001.mtrtb005_tipo_documento (
no_tipo_documento
);

/*==============================================================*/
/* table: mtrsm001.mtrtb006_funcao_documental                            */
/*==============================================================*/
create table mtrsm001.mtrtb006_funcao_documental (
   nu_funcao_documental serial               not null,
   nu_versao            int4                 not null,
   no_funcao            varchar(100)         not null,
   ic_ativo             bool                 not null,
   constraint pk_mtrtb006_funcao_documental primary key (nu_funcao_documental)
);

comment on table mtrsm001.mtrtb006_funcao_documental is
'tabela responsavel por armazenar as possiveis funções documentais.
essa informação permite agrupar documentos que possuem a mesma finalidade e um documento pode possui mais de uma função.
exemplos dessa atribuição funcional, são:
- identificação;
- renda;
- comprovação de residencia
- etc';

comment on column mtrsm001.mtrtb006_funcao_documental.nu_funcao_documental is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb006_funcao_documental.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb006_funcao_documental.no_funcao is
'atributo definido para armazenar o nome da função documental, como por exemplo:
- identificação
- comprovação de renda
- comprovação de residencia
- regularidade fiscal
- etc
';

comment on column mtrsm001.mtrtb006_funcao_documental.ic_ativo is
'atributo que indica se a função documental esta ativa ou não para utilização no sistema.
uma função só pode ser excluida fisicamente caso ela não possua relação com nenhum tipo de documento previamente.';

/*==============================================================*/
/* index: ix_mtrtb006_01                                        */
/*==============================================================*/
create unique index ix_mtrtb006_01 on mtrsm001.mtrtb006_funcao_documental (
no_funcao
);

/*==============================================================*/
/* table: mtrsm001.mtrtb007_funcao_documento                             */
/*==============================================================*/
create table mtrsm001.mtrtb007_funcao_documento (
   nu_tipo_documento    int4                 not null,
   nu_funcao_documental int4                 not null,
   constraint pk_mtrtb007_funcao_documento primary key (nu_tipo_documento, nu_funcao_documental)
);

comment on table mtrsm001.mtrtb007_funcao_documento is
'tabela associativa que vincula um tipo de documento a sua função.
ex: 
- rg x identificação
- dirpf x renda
- dirpf x identificação
';

comment on column mtrsm001.mtrtb007_funcao_documento.nu_tipo_documento is
'atributo que representa a o tipo de documento vinculado na relação com a função documental.';

comment on column mtrsm001.mtrtb007_funcao_documento.nu_funcao_documental is
'atributo que representa a o função documental vinculado na relação com o tipo de documento.';

/*==============================================================*/
/* table: mtrsm001.mtrtb008_nivel_documental                             */
/*==============================================================*/
create table mtrsm001.mtrtb008_nivel_documental (
   nu_nivel_documental  serial               not null,
   nu_versao            int4                 not null,
   co_nivel_documental  varchar(20)          not null,
   de_nivel_documental  varchar(255)         null,
   constraint pk_mtrtb008_nivel_documental primary key (nu_nivel_documental)
);

comment on table mtrsm001.mtrtb008_nivel_documental is
'tabela responsavel por armazenar as referências de niveis dicumentais possiveis para associação a clientes e produtos.
o nivel documental é uma informação pertencente ao cliente, porém o mesmo deve estar associado a um conjunto de tipos de documentos e informações que torna a informação dinamica para o cliente, ou seja, se um cliente submete um determinado documento que aumenta sua gama de informações validas, ele pode ganhar um determinado nivel documental, porém se um documento passa a ter sua validade ultrapassada o cliente perde aquele determinado nivel.';

comment on column mtrsm001.mtrtb008_nivel_documental.nu_nivel_documental is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb008_nivel_documental.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb008_nivel_documental.co_nivel_documental is
'atributo que representa o codigo do nivel documental utilizado pelos aplicativos de negocio na avaliação de conformidade documental do cliente. este valor pode ser um numero, uma letra, ou uma combinação de caracteres que representará o nivel documental concebido.';

comment on column mtrsm001.mtrtb008_nivel_documental.de_nivel_documental is
'atributo que representa uma descrição do nivel. valor livre para descrever seu significado, documentos envolvidos ou qualquer outra informação julgada pertinente.';

/*==============================================================*/
/* index: ix_mtrtb008_01                                        */
/*==============================================================*/
create unique index ix_mtrtb008_01 on mtrsm001.mtrtb008_nivel_documental (
co_nivel_documental
);

/*==============================================================*/
/* table: mtrsm001.mtrtb009_composicao_tipo_dcto                         */
/*==============================================================*/
create table mtrsm001.mtrtb009_composicao_tipo_dcto (
   nu_composicao_tipo_dcto serial               not null,
   nu_versao            int4                 not null,
   no_composicao_tipo_dcto varchar(100)         not null,
   constraint pk_mtrtb009_composicao_tipo_dc primary key (nu_composicao_tipo_dcto)
);

comment on table mtrsm001.mtrtb009_composicao_tipo_dcto is
'tabela responsavel por agrupar tipos de documentos visando criar estruturas que representam conjuntos de tipos de documentos a serem analisados conjuntamente.
essa conjunção será utilizada na analise do nivel documento e por ser formada como os exemplos a seguir:
- rg
- conta concercionaria
- contra cheque
-----------------------------------------------------------------------------------
- cnh
- conta concercionaria
- dirpf';

comment on column mtrsm001.mtrtb009_composicao_tipo_dcto.nu_composicao_tipo_dcto is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb009_composicao_tipo_dcto.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb009_composicao_tipo_dcto.no_composicao_tipo_dcto is
'atributo utilizado para armazenar o nome negovial da composição de documentos';

/*==============================================================*/
/* table: mtrsm001.mtrtb010_conjunto_tipo_dcto                           */
/*==============================================================*/
create table mtrsm001.mtrtb010_conjunto_tipo_dcto (
   nu_composicao_tipo_dcto int4                 not null,
   nu_tipo_documento    int4                 not null,
   constraint pk_mtrtb010_conjunto_tipo_dcto primary key (nu_tipo_documento, nu_composicao_tipo_dcto)
);

comment on table mtrsm001.mtrtb010_conjunto_tipo_dcto is
'tabela de relacionamento entre a construção do conjunto de documentos e os tipos de documentos';

comment on column mtrsm001.mtrtb010_conjunto_tipo_dcto.nu_composicao_tipo_dcto is
'atributo que representa composição de tipos de documentos associada aos possiveis tipos de documentos.';

comment on column mtrsm001.mtrtb010_conjunto_tipo_dcto.nu_tipo_documento is
'atributo que representa o tipo de documento definido vinculado na relação com a composição de tipos de documentos.';

/*==============================================================*/
/* table: mtrsm001.mtrtb011_nivel_composicao_dcto                        */
/*==============================================================*/
create table mtrsm001.mtrtb011_nivel_composicao_dcto (
   nu_composicao_tipo_dcto int4                 not null,
   nu_nivel_documental  int4                 not null,
   constraint pk_mtrtb011_nivel_composicao_d primary key (nu_composicao_tipo_dcto, nu_nivel_documental)
);

comment on table mtrsm001.mtrtb011_nivel_composicao_dcto is
'tabela associativa entre o nivel documental e as possiveis composições de tipo de documentos autorizados.';

comment on column mtrsm001.mtrtb011_nivel_composicao_dcto.nu_composicao_tipo_dcto is
'atributo que representa composição de tipos de documentos associada aos possiveis niveis documentais.';

comment on column mtrsm001.mtrtb011_nivel_composicao_dcto.nu_nivel_documental is
'atributo que representa o nivel documental definido vinculado na relação com a composição de tipos de documentos.';

/*==============================================================*/
/* table: mtrsm001.mtrtb012_situacao_documento                           */
/*==============================================================*/
create table mtrsm001.mtrtb012_situacao_documento (
   nu_situacao_documento serial               not null,
   nu_versao            int4                 not null,
   no_situacao          varchar(100)         not null,
   constraint pk_mtrtb012_situacao_documento primary key (nu_situacao_documento)
);

comment on table mtrsm001.mtrtb012_situacao_documento is
'tabela responsavel pelo armazenamento das possiveis situações vinculadas a um documento.
essas situações também deverão agrupar motivos para atribuição desta situação.
como exemplo podemos ter as possiveis situações e entre parenteses os motivos de agrupamento:
- aprovado
- rejeitado (ilegivel / rasurado / segurança)
- pendente (recaptura)';

comment on column mtrsm001.mtrtb012_situacao_documento.nu_situacao_documento is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb012_situacao_documento.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb012_situacao_documento.no_situacao is
'atributo que armazena o nome da situação do documento';

/*==============================================================*/
/* index: ix_mtrtb012_01                                        */
/*==============================================================*/
create unique index ix_mtrtb012_01 on mtrsm001.mtrtb012_situacao_documento (
no_situacao
);

/*==============================================================*/
/* table: mtrsm001.mtrtb013_motivo_situacao_dcto                         */
/*==============================================================*/
create table mtrsm001.mtrtb013_motivo_situacao_dcto (
   nu_motivo_situacao_dcto serial               not null,
   nu_versao            int4                 not null,
   nu_situacao_documento int4                 not null,
   no_motivo_situacao_dcto varchar(100)         not null,
   constraint pk_mtrtb013_motivo_situacao_dc primary key (nu_motivo_situacao_dcto)
);

comment on table mtrsm001.mtrtb013_motivo_situacao_dcto is
'tabela de motivos especificos para indicar a causa de uma determinada situação vinculada a um dado documento.
ex:  
- ilegivel -> rejeitado
- rasurado -> rejeitado
- segurança -> rejeitado
- recaptura -> pendente';

comment on column mtrsm001.mtrtb013_motivo_situacao_dcto.nu_motivo_situacao_dcto is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb013_motivo_situacao_dcto.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb013_motivo_situacao_dcto.nu_situacao_documento is
'atributo utilizado para vincular o motivo com uma situação especifica.';

comment on column mtrsm001.mtrtb013_motivo_situacao_dcto.no_motivo_situacao_dcto is
'atributo que armazena o nome do motivo da situação do documento';

/*==============================================================*/
/* index: ix_mtrtb013_01                                        */
/*==============================================================*/
create unique index ix_mtrtb013_01 on mtrsm001.mtrtb013_motivo_situacao_dcto (
nu_situacao_documento,
no_motivo_situacao_dcto
);

/*==============================================================*/
/* table: mtrsm001.mtrtb014_documento_cliente                            */
/*==============================================================*/
create table mtrsm001.mtrtb014_documento_cliente (
   nu_documento         int8                 not null,
   nu_dossie_cliente    int8                 not null,
   constraint pk_mtrtb014_documento_cliente primary key (nu_documento, nu_dossie_cliente)
);

comment on table mtrsm001.mtrtb014_documento_cliente is
'tabela de relacionamento que vincula um documento ao dossiê de um cliente.';

comment on column mtrsm001.mtrtb014_documento_cliente.nu_documento is
'atributo que representa o documento vinculado ao dossiê do cliente referenciado no registro.';

comment on column mtrsm001.mtrtb014_documento_cliente.nu_dossie_cliente is
'atributo que representa a o dossiê do cliente vinculado na relação de documentos.';

/*==============================================================*/
/* table: mtrsm001.mtrtb015_processo                                     */
/*==============================================================*/
create table mtrsm001.mtrtb015_processo (
   nu_processo          serial               not null,
   nu_versao            int4                 not null,
   no_processo          varchar(255)         not null,
   ts_inclusao          timestamp            not null,
   ic_ativo             bool                 not null,
   constraint pk_mtrtb015_processo primary key (nu_processo)
);

comment on table mtrsm001.mtrtb015_processo is
'tabela responsavel pelo armazenamento dos processos que podem ser atrelados aos dossiês de forma a identificar qual o processo bancario relacionado.
processos que possuam vinculação com dossiês de produto não devem ser excluidos fisicamente, e sim atribuidos como inativo.
exemplos de processos na linguagem negocial são:
- concessão de cretido habitacional
- conta corrente
- financiamento de veiculos
- pagamento de loterias
- etc';

comment on column mtrsm001.mtrtb015_processo.nu_processo is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb015_processo.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb015_processo.no_processo is
'atributo utilizado para armazenar o nome de identificação negocial do processo';

comment on column mtrsm001.mtrtb015_processo.ts_inclusao is
'atributo utilizado para armazenar o registro de data/hora da inclusão do processo no sistema';

comment on column mtrsm001.mtrtb015_processo.ic_ativo is
'atributo que indica que se o processo esta ativo ou não para utilização pelo sistema.';

/*==============================================================*/
/* index: ix_mtrtb015_01                                        */
/*==============================================================*/
create unique index ix_mtrtb015_01 on mtrsm001.mtrtb015_processo (
no_processo
);

/*==============================================================*/
/* table: mtrsm001.mtrtb016_campo_entrada                                */
/*==============================================================*/
create table mtrsm001.mtrtb016_campo_entrada (
   nu_campo_entrada     bigserial            not null,
   nu_mapa_processo     int4                 null,
   nu_versao            int4                 not null,
   no_campo             varchar(50)          not null,
   ic_tipo              varchar(20)          not null,
   ic_chave             bool                 not null,
   no_label             varchar(50)          not null,
   nu_largura           int4                 not null,
   ic_obrigatorio       bool                 not null,
   de_mascara           varchar(100)         null,
   de_placeholder       varchar(100)         null,
   nu_tamanho_minimo    int4                 null,
   nu_tamanho_maximo    int4                 null,
   de_expressao         varchar(255)         null,
   ic_ativo             bool                 not null,
   nu_ordem             int4                 not null,
   constraint pk_mtrtb016_campo_entrada primary key (nu_campo_entrada)
);

comment on table mtrsm001.mtrtb016_campo_entrada is
'tabela responsavel por armazenar a estrutura de entradas de dados que serão alimentados na inclusão de um novo dossiê para o processo vinculado.
esta estrutura permitirá realizar a construção dinamica do formulario
registros desta tabela só devem ser excluidos fisicamente caso não exista nenhuma resposta de formulario atrelada a este registro. caso essa situação ocorra o registro deve ser exluido logicamente definindo seu atributo ativo como false.';

comment on column mtrsm001.mtrtb016_campo_entrada.nu_campo_entrada is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb016_campo_entrada.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb016_campo_entrada.no_campo is
'atributo que indica o nome do campo. este nome pode ser utilizado pela estrutura de programação para referenciar a campo no formulario independente do label exposto para o usuário.';

comment on column mtrsm001.mtrtb016_campo_entrada.ic_tipo is
'atributo utilizado para armazenar o tipo de campo de formulario que será gerado. exemplos validos para este atributo são:
- text
- select
- radio';

comment on column mtrsm001.mtrtb016_campo_entrada.ic_chave is
'atributo que indica se o campo do formulario pode ser utilizado como chave de pesquisa posterior.';

comment on column mtrsm001.mtrtb016_campo_entrada.no_label is
'atributo que armazena o valor a ser exibido no label do campo do formulario para o usuario final.';

comment on column mtrsm001.mtrtb016_campo_entrada.nu_largura is
'atributo que armazena o numero de colunas do bootstrap ocupadas pelo campo do formulario na estrutura de tela. este valor pode variar de 1 a 12';

comment on column mtrtb016_campo_entrada.ic_obrigatorio is
'atributo que armazena o indicativo do obrigatoriedade do campo no formulario.';

comment on column mtrsm001.mtrtb016_campo_entrada.de_mascara is
'atributo que armazena o valor da mascara de formatação do campo de for o caso.';

comment on column mtrsm001.mtrtb016_campo_entrada.de_placeholder is
'atributo que armazena o valor do placeholder para exibição no campo de for o caso.';

comment on column mtrsm001.mtrtb016_campo_entrada.nu_tamanho_minimo is
'atributo que armazena o numero de caracteres minimo utilizados em campos de texto livre.';

comment on column mtrsm001.mtrtb016_campo_entrada.nu_tamanho_maximo is
'atributo que armazena o numero de caracteres maximo utilizados em campos de texto livre.';

comment on column mtrsm001.mtrtb016_campo_entrada.de_expressao is
'atributo que armazena a expressão a ser aplicada pelo javascript para determinar a exposição ou não do campo no formulario.';

comment on column mtrtb016_campo_entrada.ic_ativo is
'atributo que indica se o campo de entrada esta apto ou não para ser inserido no formulario.';

comment on column mtrsm001.mtrtb016_campo_entrada.nu_ordem is
'atributo utilizado para definir a ordem de exibição dos campos do formulario.';

/*==============================================================*/
/* index: ix_mtrtb016_01                                        */
/*==============================================================*/
create unique index ix_mtrtb016_01 on mtrsm001.mtrtb016_campo_entrada (
nu_ordem
);

/*==============================================================*/
/* table: mtrsm001.mtrtb017_opcao_campo                                  */
/*==============================================================*/
create table mtrsm001.mtrtb017_opcao_campo (
   nu_opcao_campo       bigserial            not null,
   nu_versao            int4                 not null,
   nu_campo_entrada     int8                 not null,
   no_opcao             varchar(255)         not null,
   ic_ativo             bool                 not null,
   constraint pk_mtrtb017_opcao_campo primary key (nu_opcao_campo)
);

comment on table mtrsm001.mtrtb017_opcao_campo is
'tabela responsavel pelo armazenamento de opções pré-definidas para alguns tipos de atributos a exemplo:
- lista;
- radio;
- check;

registros desta tabela só devem ser excluidos fisicamente caso não exista nenhuma resposta de formulario atrelada a este registro. caso essa situação ocorra o registro deve ser exluido logicamente definindo seu atributo ativo como false.';

comment on column mtrsm001.mtrtb017_opcao_campo.nu_opcao_campo is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb017_opcao_campo.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb017_opcao_campo.nu_campo_entrada is
'atributo que identifica o campo de entrada do formulario ao qual a opção esta associada.';

comment on column mtrsm001.mtrtb017_opcao_campo.no_opcao is
'atributo que armazena o valor da opção que será exibida para o usuario no campo do formulario.';

comment on column mtrsm001.mtrtb017_opcao_campo.ic_ativo is
'atributo que indica se a opção do campo de entrada esta apta ou não para ser inserido no campo de entrada do formulario.';

/*==============================================================*/
/* table: mtrsm001.mtrtb018_atributo_documento                           */
/*==============================================================*/
create table mtrsm001.mtrtb018_atributo_documento (
   nu_atributo_documento bigserial            not null,
   nu_versao            int4                 not null,
   nu_documento         int8                 not null,
   de_atributo          varchar(100)         not null,
   de_conteudo          text                 not null,
   constraint pk_mtrtb018_atributo_documento primary key (nu_atributo_documento)
);

comment on table mtrsm001.mtrtb018_atributo_documento is
'tabela responsavel por armazenar os atributos capturados do documento utilizando a estrutura de chave x valor onde o nome do atributo determina o campo do documento que a informação foi extraida e o conteudo trata-se do dado propriamente extraido.';

comment on column mtrsm001.mtrtb018_atributo_documento.nu_atributo_documento is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb018_atributo_documento.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb018_atributo_documento.nu_documento is
'atributo utilizado para vincular o atributo definido ao documento cuja informação foi extraida';

comment on column mtrsm001.mtrtb018_atributo_documento.de_atributo is
'atributo utilizado para armazenar a descrição da chave que identifica o atributo.
como uxemplo, um registro que armazena o data de nascimento de um rg 

data_nascimento = 01/09/1980

neste caso o conteudo deste campo seria "data_nascimento" e o atributo conteudo armazenaria "01/09/1980" tal qual extraido do documento';

comment on column mtrsm001.mtrtb018_atributo_documento.de_conteudo is
'atributo utilizado para armazenar a dado extraido de um campo do documento.
como uxemplo, um registro que armazena o data de nascimento de um rg 

data_nascimento = 01/09/1980

neste caso o conteudo deste campo seria "01/09/1980" tal qual extraido do documento e o atributo de descricao armazenaria "data_nascimento"';

/*==============================================================*/
/* table: mtrsm001.mtrtb019_unidade_autorizada                           */
/*==============================================================*/
create table mtrsm001.mtrtb019_unidade_autorizada (
   nu_unidade_autorizada serial               not null,
   nu_versao            int4                 not null,
   nu_processo          int4                 not null,
   nu_cgc               int4                 not null,
   ic_tipo_tratamento   int4                 not null,
   constraint pk_mtrtb019_unidade_autorizada primary key (nu_unidade_autorizada)
);

comment on table mtrsm001.mtrtb019_unidade_autorizada is
'tabela responsavel pelo armazenamento das unidades autorizadas a utilização do processo
';

comment on column mtrsm001.mtrtb019_unidade_autorizada.nu_unidade_autorizada is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb019_unidade_autorizada.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb019_unidade_autorizada.ic_tipo_tratamento is
'atributo que indica as ações possiveis a serem realizadas no processo para a determinada unidade. a soma dos valores das ações determinam quais as permissões da unidade sobre os dossiês do processo. os valores possiveis são:
1 - consulta_dossie
2 - tratar_dossie
4 - criar_dossie
8 - priorizar_dossie

considerando o fato, como exemplo uma unidade que possua o valor 7 atribuido pode consultar, tratar e criardossiês, mas não pode priorizar.';

/*==============================================================*/
/* table: mtrsm001.mtrtb020_instancia_documento                          */
/*==============================================================*/
create table mtrsm001.mtrtb020_instancia_documento (
   nu_instancia_documento bigserial            not null,
   nu_versao            int4                 not null,
   nu_documento         int8                 not null,
   nu_dossie_produto    int8                 not null,
   constraint pk_mtrtb020_instancia_document primary key (nu_instancia_documento)
);

comment on table mtrsm001.mtrtb020_instancia_documento is
'tabela responsavel pelo armazenamento de instancias de documentos que estarão vinculados aos dossiês dos produtos';

comment on column mtrsm001.mtrtb020_instancia_documento.nu_instancia_documento is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb020_instancia_documento.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb020_instancia_documento.nu_documento is
'atributo que vincula o registro da instancia de documento ao documento propriamente dito permitndo assim o reaproveitamento de documento previamente existentes.';

comment on column mtrsm001.mtrtb020_instancia_documento.nu_dossie_produto is
'atributo que armazena a referência do dossiê de produto vinculado a instancia do documento.';

/*==============================================================*/
/* index: ix_mtrtb020_01                                        */
/*==============================================================*/
create unique index ix_mtrtb020_01 on mtrsm001.mtrtb020_instancia_documento (
nu_documento,
nu_dossie_produto
);

/*==============================================================*/
/* table: mtrsm001.mtrtb021_nivel_doctal_cliente                         */
/*==============================================================*/
create table mtrsm001.mtrtb021_nivel_doctal_cliente (
   nu_dossie_cliente    int8                 not null,
   nu_nivel_documental  int4                 not null,
   constraint pk_mtrtb021_nivel_doctal_clien primary key (nu_dossie_cliente, nu_nivel_documental)
);

comment on table mtrsm001.mtrtb021_nivel_doctal_cliente is
'tabela de relacionamento responsavel por armazenar os niveis documentais associados ao dossiê do cliente
os niveis podem ser adquiridos quando novos documentos são aportados ao dossiê do cliente em contra partida são removidos quando documentos são vencidos ou invalidados.';

comment on column mtrsm001.mtrtb021_nivel_doctal_cliente.nu_dossie_cliente is
'atributo que representa o dossiê de cliente vinculado ao nivel documental estabelecido.';

comment on column mtrsm001.mtrtb021_nivel_doctal_cliente.nu_nivel_documental is
'atributo que representa o nivel documental vinculado ao dossiê do cliente.';

/*==============================================================*/
/* table: mtrsm001.mtrtb022_resposta_dossie                              */
/*==============================================================*/
create table mtrsm001.mtrtb022_resposta_dossie (
   nu_resposta_dossie   bigserial            not null,
   nu_versao            int4                 not null,
   nu_dossie_produto    int8                 not null,
   nu_campo_entrada     int8                 not null,
   de_resposta          text                 null,
   constraint pk_mtrtb022_resposta_dossie primary key (nu_resposta_dossie)
);

comment on table mtrsm001.mtrtb022_resposta_dossie is
'tabela responsavel pelo armazenamento das respostas aos itens montados dos formularios de inclusão de processos para um dosiê especifico.';

comment on column mtrsm001.mtrtb022_resposta_dossie.nu_resposta_dossie is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb022_resposta_dossie.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb022_resposta_dossie.nu_dossie_produto is
'atributo utilizado para identificar o dossiê de produto ao qual a resposta esta vinculada.';

comment on column mtrsm001.mtrtb022_resposta_dossie.nu_campo_entrada is
'atributo utilizado para identificar o campo do formulario dinamico ao qual a resposta esta vinculada.';

comment on column mtrsm001.mtrtb022_resposta_dossie.de_resposta is
'atributo utilizado para armazenar a resposta informada no formulario nos casos de atributos em texto aberto.';

/*==============================================================*/
/* table: mtrsm001.mtrtb023_dossie_cliente_produto                       */
/*==============================================================*/
create table mtrsm001.mtrtb023_dossie_cliente_produto (
   nu_dossie_cliente_produto bigserial            not null,
   nu_versao            int4                 not null,
   nu_dossie_produto    int8                 not null,
   nu_dossie_cliente    int8                 not null,
   nu_sequencia_titularidade int4                 null,
   ic_tipo_relacionamento varchar(50)          not null,
   constraint pk_mtrtb023_dossie_cliente_pro primary key (nu_dossie_cliente_produto)
);

comment on table mtrsm001.mtrtb023_dossie_cliente_produto is
'tabela de relacionamento para permitir vincular um dossiê de produto a mais de um cliente devido a necessidades de produtos com mais de um titular';

comment on column mtrsm001.mtrtb023_dossie_cliente_produto.nu_dossie_cliente_produto is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb023_dossie_cliente_produto.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb023_dossie_cliente_produto.nu_dossie_produto is
'atributo que armazena a referencia para o dossiê do produto vinculado na relação';

comment on column mtrsm001.mtrtb023_dossie_cliente_produto.nu_dossie_cliente is
'atributo que armazena a referencia para o dossiê do cliente vinculado na relação';

comment on column mtrsm001.mtrtb023_dossie_cliente_produto.nu_sequencia_titularidade is
'atributo que indica a sequencia de titularidade dos clientes para aquele processo.
ao cadastraf um processo o operador pode incluir titulares conforme a necessidade do produto e este atributo indicara a ordinalidade dos titulares.';

comment on column mtrsm001.mtrtb023_dossie_cliente_produto.ic_tipo_relacionamento is
'atributo que indica o tipo de relacionamento do cliente com o produto, podendo assumir os valores:
- titular
- avalista
- conjuge
- socio
- ';

/*==============================================================*/
/* table: mtrsm001.mtrtb024_resposta_opcao                               */
/*==============================================================*/
create table mtrsm001.mtrtb024_resposta_opcao (
   nu_opcao_campo       int8                 not null,
   nu_resposta_dossie   int8                 not null,
   constraint pk_mtrtb024_resposta_opcao primary key (nu_resposta_dossie, nu_opcao_campo)
);

comment on table mtrsm001.mtrtb024_resposta_opcao is
'tabela de relacionamento com finalidade de armazenar todas as respostas objetivas informadas pelo cliente a mesma pergunta no formulario de identificação do dossiê.';

comment on column mtrsm001.mtrtb024_resposta_opcao.nu_opcao_campo is
'atributo que representa a opção selecionada vinculado na relação com a resposta do formulario.';

comment on column mtrsm001.mtrtb024_resposta_opcao.nu_resposta_dossie is
'atributo que representa a resposta vinculada na relação com a opção selecionada do campo.';

/*==============================================================*/
/* table: mtrsm001.mtrtb025_produto                                      */
/*==============================================================*/
create table mtrsm001.mtrtb025_produto (
   nu_produto           serial               not null,
   nu_versao            int4                 not null,
   nu_operacao          int4                 not null,
   nu_modalidade        int4                 not null,
   no_produto           varchar(255)         not null,
   constraint pk_mtrtb025_produto primary key (nu_produto)
);

comment on table mtrsm001.mtrtb025_produto is
'tabela responsavel pelo armazenamento dos produtos da caixa que serão vinculados aos processos definidos';

comment on column mtrsm001.mtrtb025_produto.nu_produto is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb025_produto.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb025_produto.nu_operacao is
'atributo que armazena o numero de opeção corporativa do produto';

comment on column mtrsm001.mtrtb025_produto.nu_modalidade is
'atributo que armazena o numero da modalidade corporativa do produto';

comment on column mtrsm001.mtrtb025_produto.no_produto is
'atributo que armazena o nome corporativo do produto';

/*==============================================================*/
/* index: ix_mtrtb025_01                                        */
/*==============================================================*/
create unique index ix_mtrtb025_01 on mtrsm001.mtrtb025_produto (
nu_operacao,
nu_modalidade
);

/*==============================================================*/
/* table: mtrsm001.mtrtb026_processo_produto                             */
/*==============================================================*/
create table mtrsm001.mtrtb026_processo_produto (
   nu_processo          int4                 not null,
   nu_produto           int4                 not null,
   constraint pk_mtrtb026_processo_produto primary key (nu_processo, nu_produto)
);

comment on table mtrsm001.mtrtb026_processo_produto is
'tabela de relacionamento para vinculação do produto com o processo. 
existe a possibilidade que um produto seja vinculado a diversos processos pois pode diferenciar a forma de realizar as ações conforme o canal de contratação, campanha, ou outro fator, como por exemplo uma conta que seja contratada pela agencia fisica, agencia virtual, cca ou aplicativo de abertura de contas';

comment on column mtrsm001.mtrtb026_processo_produto.nu_processo is
'atributo que representa o processo vinculado na relação com o produto.';

comment on column mtrsm001.mtrtb026_processo_produto.nu_produto is
'atributo que representa o produto vinculado na relação com o processo.';

/*==============================================================*/
/* table: mtrsm001.mtrtb027_mapa_documentos                              */
/*==============================================================*/
create table mtrsm001.mtrtb027_mapa_documentos (
   nu_mapa_documentos   serial               not null,
   nu_mapa_processo     int4                 null,
   nu_versao            int4                 not null,
   constraint pk_mtrtb027_mapa_documentos primary key (nu_mapa_documentos)
);

comment on table mtrsm001.mtrtb027_mapa_documentos is
'tabela responsavel por identificar um arranjo de documentos que podem ser utilizados para um terminado processo.
vinculado ao mapa estará uma definição de hierarquia respresentado "pastas" e "documentos" que devem ser submetidos para o dossiê';

comment on column mtrsm001.mtrtb027_mapa_documentos.nu_mapa_documentos is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb027_mapa_documentos.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

/*==============================================================*/
/* table: mtrsm001.mtrtb028_elemento_mapa                                */
/*==============================================================*/
create table mtrsm001.mtrtb028_elemento_mapa (
   nu_elemento_mapa     bigserial            not null,
   nu_versao            int4                 not null,
   nu_elemento_vinculador int8                 null,
   nu_mapa_documento    int4                 not null,
   nu_tipo_elemento     int4                 not null,
   nu_documento         int8                 null,
   ic_obrigatorio       bool                 not null,
   nu_qtde_obrigatorio  int4                 null,
   ic_propriedade       varchar(15)          null,
   ic_validar           bool                 not null,
   no_label             varchar(100)         not null,
   constraint pk_mtrtb028_elemento_mapa primary key (nu_elemento_mapa)
);

comment on table mtrsm001.mtrtb028_elemento_mapa is
'tabela responsavel pelo armazenamento dos elementos que compoem o mapa de documentos para vinculacao ao processo.
esse elementos estão associados aos tipos de documentos para identicação dos mesmo na atoa da captura.';

comment on column mtrsm001.mtrtb028_elemento_mapa.nu_elemento_mapa is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb028_elemento_mapa.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb028_elemento_mapa.nu_elemento_vinculador is
'atributo utilizado para armazenar uma outra instancia de elemento ao qual o elemento se vincula.
esta estrutura permite criar uma estrutura hierarquizada de elementos, porem elementos só devem ser vinculados a outros elementos que não sejam finais, ou seja, não sejam associados a nenhum tipo de elemento que seja associado a um tipo de documento.';

comment on column mtrsm001.mtrtb028_elemento_mapa.nu_mapa_documento is
'atributo utilizado para identificar o mapa de documento de processo ao qual o elemento esta vinculado.';

comment on column mtrsm001.mtrtb028_elemento_mapa.nu_tipo_elemento is
'atributo utilizado para identificar o tipo de elemento de forma a identificar se trata-se de um elemento final ou não.';

comment on column mtrsm001.mtrtb028_elemento_mapa.nu_documento is
'atributo utilizado para identificar o o documento que foi submetido no mapa.
ao recuperar um dossiê para visualização, é necessario trazer a instancia do documento vinculado ao dossiê do produto referente ao registro especifico do documento submetido/associado ao mapa. ';

comment on column mtrsm001.mtrtb028_elemento_mapa.ic_obrigatorio is
'atributo para indicar se o elemento é de submissão obrigatoria ou não de forma individual.';

comment on column mtrsm001.mtrtb028_elemento_mapa.nu_qtde_obrigatorio is
'este atributo indica a quantidade de elementos que são de tipo de elemento final obrigatorios dentro da sua arvore e só deve ser preenchido se o tipo do elemento permitir agrupamento: exemplo:
- identificação (este elemento deve ter 2 filhos obrigatorios)
   |-- rg
   |-- cnh
   |-- pasaporte
   |-- ctps';

comment on column mtrsm001.mtrtb028_elemento_mapa.ic_propriedade is
'atributo para indicar qual o ente que detem a propriedade do documento dentro da estrutura do processo.
como exemplo podemos ter a seguinte situação:
- cliente (rg / cnh / etc)
- processo (bilhete de loteria / laudo de vistoria / mo / etc)';

comment on column mtrsm001.mtrtb028_elemento_mapa.ic_validar is
'atributo que indica se o documento deve ser validado quando apresentado no processo.
caso verdadeiro, a instancia do documento deve ser criada com a situação vazia.
caso false, a instancia do documento deve ser criada com a situação de aprovada conforme regra de negocio realizada pelo sistema.';

comment on column mtrsm001.mtrtb028_elemento_mapa.no_label is
'atributo utilizado para armazenar o label que será exposto na interface para identificação visual na estrutura da hierarquia de documento (pastas x documentos esperados)';

/*==============================================================*/
/* table: mtrsm001.mtrtb029_tipo_elemento                                */
/*==============================================================*/
create table mtrsm001.mtrtb029_tipo_elemento (
   nu_tipo_elemento     serial               not null,
   nu_versao            int4                 not null,
   nu_tipo_documento    int4                 null,
   no_tipo_elemento     varchar(100)         not null,
   constraint pk_mtrtb029_tipo_elemento primary key (nu_tipo_elemento)
);

comment on table mtrsm001.mtrtb029_tipo_elemento is
'tabela responsavel pelo armazenamento dos tipos possiveis de elementos para permitir os agrupadores.
exemplos de tipos podem ser:
- documento
- identificação
- residencia
- tomador
- vendedor

estes tipos de elementos determinarão se os elementos podem ser agrupados (dependentes) de outros ou não.';

comment on column mtrsm001.mtrtb029_tipo_elemento.nu_tipo_elemento is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb029_tipo_elemento.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb029_tipo_elemento.nu_tipo_documento is
'vinculação com o tipo de documento.
caso a vinculação existe, significa que trata-se de um elemento final da hierarquia. 
para os elemento vinculados a tipos que não são finais, é permitido realizar a vinculação de outros elementos viabilizando a definição de uma hierarquia analoga a pastas e documento de um explorador de arquivos.';

comment on column mtrsm001.mtrtb029_tipo_elemento.no_tipo_elemento is
'atributo utilizado para armazenar o nome de apresentação do tipo de elemento que compõe o mapa de documentos do processo.';

/*==============================================================*/
/* table: mtrsm001.mtrtb030_situacao_dossie                              */
/*==============================================================*/
create table mtrsm001.mtrtb030_situacao_dossie (
   nu_situacao_dossie   bigserial            not null,
   nu_versao            int4                 not null,
   nu_dossie_produto    int8                 not null,
   nu_tipo_situacao_dossie int4                 null,
   ts_inclusao          timestamp            not null,
   co_matricula         varchar(7)           not null,
   de_justificativa     text                 null,
   constraint pk_mtrtb030_situacao_dossie primary key (nu_situacao_dossie)
);

comment on table mtrsm001.mtrtb030_situacao_dossie is
'tabela responsavel por armazenar o historico de situações relativas ao dossiê do produto. cada vez que houver uma mudança na situação apresentada pelo processo, um novo registro deve ser inserido gerando assim um historico das situações vivenciadas durante o seu ciclo de vida.';

comment on column mtrsm001.mtrtb030_situacao_dossie.nu_situacao_dossie is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb030_situacao_dossie.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb030_situacao_dossie.nu_dossie_produto is
'atributo utilizado pata armazenar a referencia do dossiê do produto vinculado a situação';

comment on column mtrsm001.mtrtb030_situacao_dossie.nu_tipo_situacao_dossie is
'atributo utilizado para armazenar o tipo situação do dossiê que será atribuido manualmente pelo operador ou pela automacao do workflow quando estruturado.';

comment on column mtrsm001.mtrtb030_situacao_dossie.ts_inclusao is
'atributo utilizado para armazenar a data/hora de atribuição da situação ao dossiê';

comment on column mtrsm001.mtrtb030_situacao_dossie.co_matricula is
'atributo utilizado para armazenar a matricula do empregado ou serviço que atribuiu a situação ao dossiê';

comment on column mtrsm001.mtrtb030_situacao_dossie.de_justificativa is
'informação do usuario indicando o motivo de atribuição da situação definida.';

/*==============================================================*/
/* table: mtrsm001.mtrtb031_dossie_produto_produto                       */
/*==============================================================*/
create table mtrsm001.mtrtb031_dossie_produto_produto (
   nu_dossie_produto    int8                 not null,
   nu_produto           int4                 not null,
   constraint pk_mtrtb031_dossie_produto_pro primary key (nu_dossie_produto, nu_produto)
);

comment on table mtrsm001.mtrtb031_dossie_produto_produto is
'tabela de relacionamento para vinculação dos produtos selecionados para tratamento no doissiê. 
existe a possibilidade que mais de um produto seja vinculado a um dossiê para tratamento unico como é o caso do contrato de relacionamento que encolve cartão de credito / crot / cdc / conta corrente';

comment on column mtrsm001.mtrtb031_dossie_produto_produto.nu_dossie_produto is
'atributo que representa o dossiê de vinculação da coleção de produtos em analise.';

comment on column mtrsm001.mtrtb031_dossie_produto_produto.nu_produto is
'atributo que representa o produto vinculado na relação com o dossiê.';

/*==============================================================*/
/* table: mtrsm001.mtrtb032_situacao_instancia_dct                       */
/*==============================================================*/
create table mtrsm001.mtrtb032_situacao_instancia_dct (
   nu_situacao_instancia_dcto bigserial            not null,
   nu_versao            int4                 not null,
   nu_instancia_documento bigserial            not null,
   nu_situacao_documento int4                 not null,
   nu_motivo_situacao_dcto int4                 not null,
   ts_inclusao          timestamp            not null,
   co_matricula         varchar(7)           null,
   constraint pk_mtrtb032_situacao_instancia primary key (nu_situacao_instancia_dcto)
);

comment on table mtrsm001.mtrtb032_situacao_instancia_dct is
'tabela responsavel por armazenar o historico de situações relativas a instancia do documento em avaliação. cada vez que houver uma mudança na situação apresentada pelo processo, um novo registro deve ser inserido gerando assim um historico das situações vivenciadas durante o seu ciclo de vida.';

comment on column mtrsm001.mtrtb032_situacao_instancia_dct.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb032_situacao_instancia_dct.nu_instancia_documento is
'atributo utilizado pata armazenar a referencia da instancia do documento em avaliação vinculado a situação';

comment on column mtrsm001.mtrtb032_situacao_instancia_dct.nu_situacao_documento is
'atributo utilizado pata armazenar a referencia a situação do documento escolhida vinculada a instancia do documento em avaliação';

comment on column mtrsm001.mtrtb032_situacao_instancia_dct.nu_motivo_situacao_dcto is
'atributo utilizado pata armazenar a referencia o motivo especifico para a situação escolhida vinculada a instancia do documento em avaliação';

comment on column mtrsm001.mtrtb032_situacao_instancia_dct.ts_inclusao is
'atributo utilizado para armazenar a data/hora de atribuição da situação ao dossiê';

comment on column mtrsm001.mtrtb032_situacao_instancia_dct.co_matricula is
'atributo utilizado para armazenar a matricula do empregado ou serviço que atribuiu a situação a instancia do documento em avaliação';

/*==============================================================*/
/* table: mtrsm001.mtrtb033_tipo_situacao_dossie                         */
/*==============================================================*/
create table mtrsm001.mtrtb033_tipo_situacao_dossie (
   nu_tipo_situacao_dossie serial               not null,
   nu_versao            int4                 not null,
   no_tipo_situacao     varchar(100)         not null,
   constraint pk_mtrtb033_tipo_situacao_doss primary key (nu_tipo_situacao_dossie)
);

comment on table mtrsm001.mtrtb033_tipo_situacao_dossie is
'tabela responsavel pelo armazenamento das possiveis situações vinculadas a um dossiê de produto.

como exemplo podemos ter as possiveis situações:
- criado
- atualizado
- disponivel
- em analise
- etc';

comment on column mtrsm001.mtrtb033_tipo_situacao_dossie.nu_tipo_situacao_dossie is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb033_tipo_situacao_dossie.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb033_tipo_situacao_dossie.no_tipo_situacao is
'atributo que armazena o nome do tipo de situação do dossiê';

/*==============================================================*/
/* index: ix_mtrtb012_02                                        */
/*==============================================================*/
create unique index ix_mtrtb012_02 on mtrsm001.mtrtb033_tipo_situacao_dossie (
no_tipo_situacao
);

/*==============================================================*/
/* table: mtrsm001.mtrtb034_mapa_processo                                */
/*==============================================================*/
create table mtrsm001.mtrtb034_mapa_processo (
   nu_mapa_processo     serial               not null,
   nu_processo          int4                 null,
   nu_versao            int4                 not null,
   nu_fase_utilizacao   int4                 not null,
   constraint pk_mtrtb034_mapa_processo primary key (nu_mapa_processo)
);

comment on table mtrsm001.mtrtb034_mapa_processo is
'tabela responsavel por agrupar um mapa de documentos com um conjunto de campos de entrada do formulario dinamico que podem ser utilizados para um determinado processo.';

comment on column mtrsm001.mtrtb034_mapa_processo.nu_processo is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb034_mapa_processo.nu_versao is
'campo de controle das versões do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb034_mapa_processo.nu_fase_utilizacao is
'atributo utilizado para identificar a fase de associação do mapa junto ao dossiê.
conforme a fase definida no dossiê do produto, o conjunto de imagens e campos do formulario ficam disponiveis para preenchimento, caso selecionado uma fase do processo diferente da atual do dossiê, o formulario e relação de documentos referente a outra fase ficam disponiveis apenas para consulta.';


/*==============================================================*/
/* table: mtrsm001.mtrtb035_stco_dossie_cliente                          */
/*==============================================================*/
create table mtrsm001.mtrtb035_stco_dossie_cliente (
   nu_stco_dossie_cliente bigserial            not null,
   nu_dossie_cliente    bigserial            not null,
   nu_tipo_stco_dossie_cliente int4          not null,
   nu_versao            int4                 not null,
   ts_inclusao          timestamp            not null,
   co_matricula         varchar(7)           not null,
   de_justificativa     text                 null,
   constraint pk_mtrtb035_stco_dossie_client primary key (nu_stco_dossie_cliente)
);

comment on table mtrsm001.mtrtb035_stco_dossie_cliente is
'tabela responsavel por armazenar o historico de situa��es relativas ao dossi� do cliente. cada vez que houver uma mudan�a na situa��o apresentada por um  processo, um novo registro deve ser inserido gerando assim um historico das situa��es vivenciadas durante o seu ciclo de vida.';

comment on column mtrsm001.mtrtb035_stco_dossie_cliente.nu_dossie_cliente is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb035_stco_dossie_cliente.nu_tipo_stco_dossie_cliente is
'atributo que representa a chave primaria da entidade.';


/*==============================================================*/
/* table: mtrsm001.mtrtb036_tipo_stco_dossie_cliente            */
/*==============================================================*/
create table mtrsm001.mtrtb036_tipo_stco_dossie_cliente (
   nu_tipo_stco_dossie_cliente serial               not null,
   nu_versao            int4                        not null,
   no_tipo_stco_dossie_cliente varchar(100)         not null,
   constraint pk_mtrtb036_tipo_stco_dossie_c primary key (nu_tipo_stco_dossie_cliente)
);

comment on table mtrsm001.mtrtb036_tipo_stco_dossie_cliente is
'tabela responsavel pelo armazenamento das possiveis situa��es vinculadas a um dossi� de cliente.

como exemplo podemos ter as possiveis situa��es:
- criado
- atualizado
- disponivel
- em analise
- etc';

comment on column mtrsm001.mtrtb036_tipo_stco_dossie_cliente.nu_tipo_stco_dossie_cliente is
'atributo que representa a chave primaria da entidade.';

comment on column mtrsm001.mtrtb036_tipo_stco_dossie_cliente.nu_versao is
'campo de controle das vers�es do registro para viabilizar a concorrencia otimista';

comment on column mtrsm001.mtrtb036_tipo_stco_dossie_cliente.no_tipo_stco_dossie_cliente is
'atributo que armazena o nome do tipo de situa��o do dossi�';



alter table mtrsm001.mtrtb001_pessoa_fisica
   add constraint fk_mtrtb001_mtrtb001 foreign key (nu_dossie_cliente)
      references mtrsm001.mtrtb001_dossie_cliente (nu_dossie_cliente)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb001_pessoa_juridica
   add constraint fk_mtrtb001_mtrtb001 foreign key (nu_dossie_cliente)
      references mtrsm001.mtrtb001_dossie_cliente (nu_dossie_cliente)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb002_dossie_produto
   add constraint fk_mtrtb002_mtrtb015 foreign key (nu_processo)
      references mtrsm001.mtrtb015_processo (nu_processo)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb003_documento
   add constraint fk_mtrtb003_mtrtb005 foreign key (nu_tipo_documento)
      references mtrsm001.mtrtb005_tipo_documento (nu_tipo_documento)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb004_imagem
   add constraint fk_mtrtb004_mtrtb003 foreign key (nu_documento)
      references mtrsm001.mtrtb003_documento (nu_documento)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb007_funcao_documento
   add constraint fk_mtrtb007_mtrtb005 foreign key (nu_tipo_documento)
      references mtrsm001.mtrtb005_tipo_documento (nu_tipo_documento)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb007_funcao_documento
   add constraint fk_mtrtb007_mtrtb006 foreign key (nu_funcao_documental)
      references mtrsm001.mtrtb006_funcao_documental (nu_funcao_documental)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb010_conjunto_tipo_dcto
   add constraint fk_mtrtb010_mtrtb009 foreign key (nu_composicao_tipo_dcto)
      references mtrsm001.mtrtb009_composicao_tipo_dcto (nu_composicao_tipo_dcto)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb010_conjunto_tipo_dcto
   add constraint fk_mtrtb010_reference_mtrtb005 foreign key (nu_tipo_documento)
      references mtrtb005_tipo_documento (nu_tipo_documento)
      on delete restrict on update restrict;

alter table mtrtb011_nivel_composicao_dcto
   add constraint fk_mtrtb011_mtrtb009 foreign key (nu_composicao_tipo_dcto)
      references mtrsm001.mtrtb009_composicao_tipo_dcto (nu_composicao_tipo_dcto)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb011_nivel_composicao_dcto
   add constraint fk_mtrtb011_mtrtb008 foreign key (nu_nivel_documental)
      references mtrsm001.mtrtb008_nivel_documental (nu_nivel_documental)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb013_motivo_situacao_dcto
   add constraint fk_mtrtb013_mtrtb012 foreign key (nu_situacao_documento)
      references mtrsm001.mtrtb012_situacao_documento (nu_situacao_documento)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb014_documento_cliente
   add constraint fk_mtrtb014_mtrtb003 foreign key (nu_documento)
      references mtrsm001.mtrtb003_documento (nu_documento)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb014_documento_cliente
   add constraint fk_mtrtb014_mtrtb001 foreign key (nu_dossie_cliente)
      references mtrsm001.mtrtb001_dossie_cliente (nu_dossie_cliente)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb016_campo_entrada
   add constraint fk_mtrtb016_mtrtb034 foreign key (nu_mapa_processo)
      references mtrsm001.mtrtb034_mapa_processo (nu_mapa_processo)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb017_opcao_campo
   add constraint fk_mtrtb017_mtrtb016 foreign key (nu_campo_entrada)
      references mtrsm001.mtrtb016_campo_entrada (nu_campo_entrada)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb018_atributo_documento
   add constraint fk_mtrtb018_mtrtb003 foreign key (nu_documento)
      references mtrsm001.mtrtb003_documento (nu_documento)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb019_unidade_autorizada
   add constraint fk_mtrtb019_mtrtb015 foreign key (nu_processo)
      references mtrsm001.mtrtb015_processo (nu_processo)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb020_instancia_documento
   add constraint fk_mtrtb020_mtrtb002 foreign key (nu_dossie_produto)
      references mtrsm001.mtrtb002_dossie_produto (nu_dossie_produto)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb020_instancia_documento
   add constraint fk_mtrtb020_mtrtb003 foreign key (nu_documento)
      references mtrsm001.mtrtb003_documento (nu_documento)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb021_nivel_doctal_cliente
   add constraint fk_mtrtb021_mtrtb001 foreign key (nu_dossie_cliente)
      references mtrsm001.mtrtb001_dossie_cliente (nu_dossie_cliente)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb021_nivel_doctal_cliente
   add constraint fk_mtrtb021_mtrtb008 foreign key (nu_nivel_documental)
      references mtrsm001.mtrtb008_nivel_documental (nu_nivel_documental)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb022_resposta_dossie
   add constraint fk_mtrtb022_mtrtb016 foreign key (nu_campo_entrada)
      references mtrsm001.mtrtb016_campo_entrada (nu_campo_entrada)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb022_resposta_dossie
   add constraint fk_mtrtb022_mtrtb002 foreign key (nu_dossie_produto)
      references mtrsm001.mtrtb002_dossie_produto (nu_dossie_produto)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb023_dossie_cliente_produto
   add constraint fk_mtrtb023_mtrtb002 foreign key (nu_dossie_produto)
      references mtrsm001.mtrtb002_dossie_produto (nu_dossie_produto)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb023_dossie_cliente_produto
   add constraint fk_mtrtb023_mtrtb001 foreign key (nu_dossie_cliente)
      references mtrsm001.mtrtb001_dossie_cliente (nu_dossie_cliente)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb024_resposta_opcao
   add constraint fk_mtrtb024_mtrtb022 foreign key (nu_resposta_dossie)
      references mtrsm001.mtrtb022_resposta_dossie (nu_resposta_dossie)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb024_resposta_opcao
   add constraint fk_mtrtb024_mtrtb017 foreign key (nu_opcao_campo)
      references mtrsm001.mtrtb017_opcao_campo (nu_opcao_campo)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb026_processo_produto
   add constraint fk_mtrtb026_mtrtb015 foreign key (nu_processo)
      references mtrsm001.mtrtb015_processo (nu_processo)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb026_processo_produto
   add constraint fk_mtrtb026_mtrtb025 foreign key (nu_produto)
      references mtrsm001.mtrtb025_produto (nu_produto)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb027_mapa_documentos
   add constraint fk_mtrtb027_mtrtb034 foreign key (nu_mapa_processo)
      references mtrsm001.mtrtb034_mapa_processo (nu_mapa_processo)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb028_elemento_mapa
   add constraint fk_mtrtb028_mtrtb028 foreign key (nu_elemento_vinculador)
      references mtrsm001.mtrtb028_elemento_mapa (nu_elemento_mapa)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb028_elemento_mapa
   add constraint fk_mtrtb028_mtrtb027 foreign key (nu_mapa_documento)
      references mtrsm001.mtrtb027_mapa_documentos (nu_mapa_documentos)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb028_elemento_mapa
   add constraint fk_mtrtb028_mtrtb029 foreign key (nu_tipo_elemento)
      references mtrsm001.mtrtb029_tipo_elemento (nu_tipo_elemento)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb028_elemento_mapa
   add constraint fk_mtrtb028_mtrtb003 foreign key (nu_documento)
      references mtrsm001.mtrtb003_documento (nu_documento)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb029_tipo_elemento
   add constraint fk_mtrtb029_mtrtb005 foreign key (nu_tipo_documento)
      references mtrsm001.mtrtb005_tipo_documento (nu_tipo_documento)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb030_situacao_dossie
   add constraint fk_mtrtb030_mtrtb002 foreign key (nu_dossie_produto)
      references mtrsm001.mtrtb002_dossie_produto (nu_dossie_produto)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb030_situacao_dossie
   add constraint fk_mtrtb030_mtrtb033 foreign key (nu_tipo_situacao_dossie)
      references mtrsm001.mtrtb033_tipo_situacao_dossie (nu_tipo_situacao_dossie)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb031_dossie_produto_produto
   add constraint fk_mtrtb031_mtrtb025 foreign key (nu_produto)
      references mtrsm001.mtrtb025_produto (nu_produto)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb031_dossie_produto_produto
   add constraint fk_mtrtb031_mtrtb002 foreign key (nu_dossie_produto)
      references mtrsm001.mtrtb002_dossie_produto (nu_dossie_produto)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb032_situacao_instancia_dct
   add constraint fk_mtrtb032_mtrtb020 foreign key (nu_instancia_documento)
      references mtrsm001.mtrtb020_instancia_documento (nu_instancia_documento)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb032_situacao_instancia_dct
   add constraint fk_mtrtb032_mtrtb012 foreign key (nu_situacao_documento)
      references mtrsm001.mtrtb012_situacao_documento (nu_situacao_documento)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb032_situacao_instancia_dct
   add constraint fk_mtrtb032_mtrtb013 foreign key (nu_motivo_situacao_dcto)
      references mtrsm001.mtrtb013_motivo_situacao_dcto (nu_motivo_situacao_dcto)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb034_mapa_processo
   add constraint fk_mtrtb034_mtrtb015 foreign key (nu_processo)
      references mtrsm001.mtrtb015_processo (nu_processo)
      on delete restrict on update restrict;
	  
alter table mtrsm001.mtrtb035_stco_dossie_cliente
   add constraint fk_mtrtb035_mtrtb001 foreign key (nu_dossie_cliente)
      references mtrsm001.mtrtb001_dossie_cliente (nu_dossie_cliente)
      on delete restrict on update restrict;

alter table mtrsm001.mtrtb035_stco_dossie_cliente
   add constraint fk_mtrtb035_mtrtb036 foreign key (nu_tipo_stco_dossie_cliente)
      references mtrsm001.mtrtb036_tipo_stco_dossie_cliente (nu_tipo_stco_dossie_cliente)
      on delete restrict on update restrict;

