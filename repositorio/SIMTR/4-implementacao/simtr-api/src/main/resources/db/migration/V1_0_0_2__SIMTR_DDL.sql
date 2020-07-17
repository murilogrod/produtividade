--------------------------------------------------sequence--------------------------------------------------
ALTER SEQUENCE mtrsm001.mtrtb001_dossie_cliente_nu_dossie_cliente_seq RENAME TO MTRSQ001_DOSSIE_CLIENTE;
ALTER SEQUENCE mtrsm001.mtrtb002_dossie_produto_nu_dossie_produto_seq RENAME TO MTRSQ002_DOSSIE_PRODUTO;
ALTER SEQUENCE mtrsm001.mtrtb003_documento_nu_documento_seq RENAME TO MTRSQ003_DOCUMENTO;
ALTER SEQUENCE mtrsm001.mtrtb004_dossie_cliente_produto_nu_dossie_cliente_produto_seq RENAME TO MTRSQ005_DOSSIE_CLIENTE_PRODUTO;
ALTER SEQUENCE mtrsm001.mtrtb006_canal_captura_nu_canal_captura_seq RENAME TO MTRSQ006_CANAL_CAPTURA;
ALTER SEQUENCE mtrsm001.mtrtb007_atributo_documento_nu_atributo_documento_seq RENAME TO MTRSQ007_ATRIBUTO_DOCUMENTO;
ALTER SEQUENCE mtrsm001.mtrtb008_conteudo_nu_conteudo_seq RENAME TO MTRSQ008_CONTEUDO;
ALTER SEQUENCE mtrsm001.mtrtb009_tipo_documento_nu_tipo_documento_seq RENAME TO MTRSQ009_TIPO_DOCUMENTO;
ALTER SEQUENCE mtrsm001.mtrtb010_funcao_documental_nu_funcao_documental_seq RENAME TO MTRSQ010_FUNCAO_DOCUMENTAL;
ALTER SEQUENCE mtrsm001.mtrtb012_tipo_situacao_dossie_nu_tipo_situacao_dossie_seq RENAME TO MTRSQ012_TIPO_SITUACAO_DOSSIE;
ALTER SEQUENCE mtrsm001.mtrtb013_situacao_dossie_nu_situacao_dossie_seq RENAME TO MTRSQ013_SITUACAO_DOSSIE;
ALTER SEQUENCE mtrsm001.mtrtb014_instancia_documento_nu_instancia_documento_seq RENAME TO MTRSQ014_INSTANCIA_DOCUMENTO;
ALTER SEQUENCE mtrsm001.mtrtb015_situacao_documento_nu_situacao_documento_seq RENAME TO MTRSQ015_SITUACAO_DOCUMENTO;
ALTER SEQUENCE mtrsm001.mtrtb016_motivo_situacao_dcto_nu_motivo_situacao_dcto_seq RENAME TO MTRSQ016_MOTIVO_SITUACAO_DCMNTO;
ALTER SEQUENCE mtrsm001.mtrtb017_situacao_instancia_dct_nu_situacao_instancia_dcto_seq RENAME TO MTRSQ017_SITUACAO_INSTNCA_DCMNTO;
ALTER SEQUENCE mtrsm001.mtrtb019_macroprocesso_nu_macroprocesso_seq RENAME TO MTRSQ019_MACROPROCESSO;
ALTER SEQUENCE mtrsm001.mtrtb020_processo_nu_processo_seq RENAME TO MTRSQ020_PROCESSO;
ALTER SEQUENCE mtrsm001.mtrtb021_unidade_autorizada_nu_unidade_autorizada_seq RENAME TO MTRSQ021_UNIDADE_AUTORIZADA;
ALTER SEQUENCE mtrsm001.mtrtb022_produto_nu_produto_seq RENAME TO MTRSQ022_PRODUTO;
ALTER SEQUENCE mtrsm001.mtrtb024_produto_dossie_nu_produto_dossie_seq RENAME TO MTRSQ024_PRODUTO_DOSSIE;
ALTER SEQUENCE mtrsm001.mtrtb025_processo_documento_nu_processo_documento_seq RENAME TO MTRSQ025_PROCESSO_DOCUMENTO;
ALTER SEQUENCE mtrsm001.mtrtb026_formulario_nu_formulario_seq RENAME TO MTRSQ026_FORMULARIO;
ALTER SEQUENCE mtrsm001.mtrtb027_campo_entrada_nu_campo_entrada_seq RENAME TO MTRSQ027_CAMPO_ENTRADA;
ALTER SEQUENCE mtrsm001.mtrtb028_opcao_campo_nu_opcao_campo_seq RENAME TO MTRSQ028_OPCAO_CAMPO;
ALTER SEQUENCE mtrsm001.mtrtb029_campo_apresentacao_nu_campo_apresentacao_seq RENAME TO MTRSQ029_CAMPO_APRESENTACAO;
ALTER SEQUENCE mtrsm001.mtrtb030_resposta_dossie_nu_resposta_dossie_seq RENAME TO MTRSQ030_RESPOSTA_DOSSIE;
ALTER SEQUENCE mtrsm001.mtrtb032_elemento_conteudo_nu_elemento_conteudo_seq RENAME TO MTRSQ032_ELEMENTO_DOSSIE;
ALTER SEQUENCE mtrsm001.mtrtb033_garantia_nu_garantia_seq RENAME TO MTRSQ033_GARANTIA;
ALTER SEQUENCE mtrsm001.mtrtb035_garantia_informada_nu_garantia_informada_seq RENAME TO MTRSQ035_GARANTIA_INFORMADA;
ALTER SEQUENCE mtrsm001.mtrtb036_composicao_documental_nu_composicao_documental_seq RENAME TO MTRSQ036_COMPOSICAO_DOCUMENTAL;
ALTER SEQUENCE mtrsm001.mtrtb037_regra_documental_nu_regra_documental_seq RENAME TO MTRSQ037_REGRA_DOCUMENTAL;
ALTER SEQUENCE mtrsm001.mtrtb100_autorizacao_nu_autorizacao_seq RENAME TO MTRSQ100_AUTORIZACAO;
ALTER SEQUENCE mtrsm001.mtrtb101_documento_nu_documento_seq RENAME TO MTRSQ101_DOCUMENTO;
--------------------------------------------------fim sequence--------------------------------------------------

--------------------------------------------------nome tabela--------------------------------------------------
ALTER TABLE mtrsm001.mtrtb016_motivo_situacao_dcto RENAME TO mtrtb016_motivo_situacao_dcmnto;
ALTER TABLE mtrsm001.mtrtb017_situacao_instancia_dct RENAME TO mtrtb017_situacao_instnca_dcmnto;
--------------------------------------------------fim nome tabela--------------------------------------------------

--------------------------------------------------nome atributo--------------------------------------------------
alter table mtrsm001.mtrtb100_autorizacao rename column co_autorizacao to nu_autorizacao_dossie;
--------------------------------------------------fim nome atributo--------------------------------------------------

--------------------------------------------------nome chave primaria--------------------------------------------------
ALTER TABLE mtrsm001.mtrtb001_dossie_cliente RENAME CONSTRAINT pk_mtrtb001_dossie_cliente TO PK_MTRTB001;
ALTER TABLE mtrsm001.mtrtb002_dossie_produto RENAME CONSTRAINT pk_mtrtb002_dossie_produto TO PK_MTRTB002;
ALTER TABLE mtrsm001.mtrtb003_documento RENAME CONSTRAINT pk_mtrtb003_documento TO PK_MTRTB003;
ALTER TABLE mtrsm001.mtrtb004_dossie_cliente_produto RENAME CONSTRAINT pk_mtrtb004_dossie_cliente_pro TO PK_MTRTB004;
ALTER TABLE mtrsm001.mtrtb005_documento_cliente RENAME CONSTRAINT pk_mtrtb005_documento_cliente TO PK_MTRTB005;
ALTER TABLE mtrsm001.mtrtb006_canal_captura RENAME CONSTRAINT pk_mtrtb006_canal_captura TO PK_MTRTB006;
ALTER TABLE mtrsm001.mtrtb007_atributo_documento RENAME CONSTRAINT pk_mtrtb007_atributo_documento TO PK_MTRTB007;
ALTER TABLE mtrsm001.mtrtb008_conteudo RENAME CONSTRAINT pk_mtrtb008_conteudo TO PK_MTRTB008;
ALTER TABLE mtrsm001.mtrtb009_tipo_documento RENAME CONSTRAINT pk_mtrtb009_tipo_documento TO PK_MTRTB009;
ALTER TABLE mtrsm001.mtrtb010_funcao_documental RENAME CONSTRAINT pk_mtrtb010_funcao_documental TO PK_MTRTB010;
ALTER TABLE mtrsm001.mtrtb011_funcao_documento RENAME CONSTRAINT pk_mtrtb011_funcao_documento TO PK_MTRTB011;
ALTER TABLE mtrsm001.mtrtb012_tipo_situacao_dossie RENAME CONSTRAINT pk_mtrtb012_tipo_situacao_doss TO PK_MTRTB012;
ALTER TABLE mtrsm001.mtrtb013_situacao_dossie RENAME CONSTRAINT pk_mtrtb013_situacao_dossie TO PK_MTRTB013;
ALTER TABLE mtrsm001.mtrtb014_instancia_documento RENAME CONSTRAINT pk_mtrtb014_instancia_document TO PK_MTRTB014;
ALTER TABLE mtrsm001.mtrtb015_situacao_documento RENAME CONSTRAINT pk_mtrtb015_situacao_documento TO PK_MTRTB015;
ALTER TABLE mtrsm001.mtrtb016_motivo_situacao_dcmnto RENAME CONSTRAINT pk_mtrtb016_motivo_situacao_dc TO PK_MTRTB016;
ALTER TABLE mtrsm001.mtrtb017_situacao_instnca_dcmnto RENAME CONSTRAINT pk_mtrtb017_situacao_instancia TO PK_MTRTB017;
ALTER TABLE mtrsm001.mtrtb018_unidade_tratamento RENAME CONSTRAINT pk_mtrtb018_unidade_tratamento TO PK_MTRTB018;
ALTER TABLE mtrsm001.mtrtb019_macroprocesso RENAME CONSTRAINT pk_mtrtb019_macroprocesso TO PK_MTRTB019;
ALTER TABLE mtrsm001.mtrtb020_processo RENAME CONSTRAINT pk_mtrtb020_processo TO PK_MTRTB020;
ALTER TABLE mtrsm001.mtrtb021_unidade_autorizada RENAME CONSTRAINT pk_mtrtb021_unidade_autorizada TO PK_MTRTB021;
ALTER TABLE mtrsm001.mtrtb022_produto RENAME CONSTRAINT pk_mtrtb022_produto TO PK_MTRTB022;
ALTER TABLE mtrsm001.mtrtb023_produto_processo RENAME CONSTRAINT pk_mtrtb023_produto_processo TO PK_MTRTB023;
ALTER TABLE mtrsm001.mtrtb024_produto_dossie RENAME CONSTRAINT pk_mtrtb024_produto_dossie TO PK_MTRTB024;
ALTER TABLE mtrsm001.mtrtb025_processo_documento RENAME CONSTRAINT pk_mtrtb025_processo_documento TO PK_MTRTB025;
ALTER TABLE mtrsm001.mtrtb026_formulario RENAME CONSTRAINT pk_mtrtb026_formulario TO PK_MTRTB026;
ALTER TABLE mtrsm001.mtrtb027_campo_entrada RENAME CONSTRAINT pk_mtrtb027_campo_entrada TO PK_MTRTB027;
ALTER TABLE mtrsm001.mtrtb028_opcao_campo RENAME CONSTRAINT pk_mtrtb028_opcao_campo TO PK_MTRTB028;
ALTER TABLE mtrsm001.mtrtb029_campo_apresentacao RENAME CONSTRAINT pk_mtrtb029_campo_apresentacao TO PK_MTRTB029;
ALTER TABLE mtrsm001.mtrtb030_resposta_dossie RENAME CONSTRAINT pk_mtrtb030_resposta_dossie TO PK_MTRTB030;
ALTER TABLE mtrsm001.mtrtb031_resposta_opcao RENAME CONSTRAINT pk_mtrtb031_resposta_opcao TO PK_MTRTB031;
ALTER TABLE mtrsm001.mtrtb032_elemento_conteudo RENAME CONSTRAINT pk_mtrtb032_elemento_conteudo TO PK_MTRTB0032;
ALTER TABLE mtrsm001.mtrtb033_garantia RENAME CONSTRAINT pk_mtrtb033_garantia TO PK_MTRTB033;
ALTER TABLE mtrsm001.mtrtb034_garantia_produto RENAME CONSTRAINT pk_mtrtb034_garantia_produto TO PK_MTRTB034;
ALTER TABLE mtrsm001.mtrtb035_garantia_informada RENAME CONSTRAINT pk_mtrtb035_garantia_informada TO PK_MTRTB035;
ALTER TABLE mtrsm001.mtrtb036_composicao_documental RENAME CONSTRAINT pk_mtrtb036_composicao_documen TO PK_MTRTB036;
ALTER TABLE mtrsm001.mtrtb037_regra_documental RENAME CONSTRAINT pk_mtrtb037_regra_documental TO PK_MTRTB037;
ALTER TABLE mtrsm001.mtrtb038_nivel_documental RENAME CONSTRAINT pk_mtrtb038_nivel_documental TO PK_MTRTB038;
ALTER TABLE mtrsm001.mtrtb039_produto_composicao RENAME CONSTRAINT pk_mtrtb039_produto_composicao TO PK_MTRTB039;
ALTER TABLE mtrsm001.mtrtb040_cadeia_situacao_dossie RENAME CONSTRAINT pk_mtrtb040_cadeia_situacao_do TO PK_MTRTB040;
ALTER TABLE mtrsm001.mtrtb041_cadeia_situacao_dcto RENAME CONSTRAINT pk_mtrtb041_cadeia_situacao_dc TO PK_MTRTB041;
ALTER TABLE mtrsm001.mtrtb100_autorizacao RENAME CONSTRAINT pk_mtrtb100_autorizacao TO PK_MTRTB100;
ALTER TABLE mtrsm001.mtrtb101_documento RENAME CONSTRAINT pk_mtrtb101_documento TO PK_MTRTB101;
--------------------------------------------------fim nome chave primaria--------------------------------------------------

--------------------------------------------------Nome chave estrangeira --------------------------------------------------
ALTER TABLE mtrsm001.mtrtb003_documento RENAME CONSTRAINT fk_mtrtb003_reference_mtrtb009 TO FK_MTRTB003_MTRTB009;
ALTER TABLE mtrsm001.mtrtb011_funcao_documento RENAME CONSTRAINT fk_mtrtb011_reference_mtrtb009 TO FK_MTRTB011_MTRTB009;
ALTER TABLE mtrsm001.mtrtb011_funcao_documento RENAME CONSTRAINT fk_mtrtb011_reference_mtrtb010 TO FK_MTRTB011_MTRTB010;
ALTER TABLE mtrsm001.mtrtb037_regra_documental RENAME CONSTRAINT fk_mtrtb037_reference_mtrtb009 TO FK_MTRTB037_MTRTB009;
ALTER TABLE mtrsm001.mtrtb037_regra_documental RENAME CONSTRAINT fk_mtrtb037_reference_mtrtb036 TO FK_MTRTB037_MTRTB036;
ALTER TABLE mtrsm001.mtrtb016_motivo_situacao_dcmnto RENAME CONSTRAINT fk_mtrtb016_reference_mtrtb015 TO FK_MTRTB016_MTRTB015;
ALTER TABLE mtrsm001.mtrtb005_documento_cliente RENAME CONSTRAINT fk_mtrtb005_reference_mtrtb003 TO FK_MTRTB005_MTRTB003;
ALTER TABLE mtrsm001.mtrtb005_documento_cliente RENAME CONSTRAINT fk_mtrtb005_reference_mtrtb001 TO FK_MTRTB005_MTRTB001;
ALTER TABLE mtrsm001.mtrtb002_dossie_produto RENAME CONSTRAINT fk_mtrtb002_reference_mtrtb020 TO FK_MTRTB002_MTRTB020;
ALTER TABLE mtrsm001.mtrtb008_conteudo RENAME CONSTRAINT fk_mtrtb008_reference_mtrtb003 TO FK_MTRTB008_MTRTB003;
ALTER TABLE mtrsm001.mtrtb014_instancia_documento RENAME CONSTRAINT fk_mtrtb014_reference_mtrtb002 TO FK_MTRTB014_MTRTB002;
ALTER TABLE mtrsm001.mtrtb014_instancia_documento RENAME CONSTRAINT fk_mtrtb014_reference_mtrtb003 TO FK_MTRTB014_MTRTB003;
ALTER TABLE mtrsm001.mtrtb028_opcao_campo RENAME CONSTRAINT fk_mtrtb028_reference_mtrtb027 TO FK_MTRTB028_MTRTB027;
ALTER TABLE mtrsm001.mtrtb030_resposta_dossie RENAME CONSTRAINT fk_mtrtb030_reference_mtrtb027 TO FK_MTRTB030_MTRTB027;
ALTER TABLE mtrsm001.mtrtb031_resposta_opcao RENAME CONSTRAINT fk_mtrtb031_reference_mtrtb030 TO FK_MTRTB031_MTRTB030;
ALTER TABLE mtrsm001.mtrtb030_resposta_dossie RENAME CONSTRAINT fk_mtrtb030_reference_mtrtb002 TO FK_MTRTB030_MTRTB002;
ALTER TABLE mtrsm001.mtrtb004_dossie_cliente_produto RENAME CONSTRAINT fk_mtrtb004_reference_mtrtb002 TO FK_MTRTB004_MTRTB002;
ALTER TABLE mtrsm001.mtrtb004_dossie_cliente_produto RENAME CONSTRAINT fk_mtrtb004_reference_mtrtb001 TO FK_MTRTB004_MTRTB001;
ALTER TABLE mtrsm001.mtrtb031_resposta_opcao RENAME CONSTRAINT fk_mtrtb031_reference_mtrtb028 TO FK_MTRTB031_MTRTB028;
ALTER TABLE mtrsm001.mtrtb032_elemento_conteudo RENAME CONSTRAINT fk_mtrtb032_reference_mtrtb032 TO FK_MTRTB032_MTRTB032;
ALTER TABLE mtrsm001.mtrtb013_situacao_dossie RENAME CONSTRAINT fk_mtrtb013_reference_mtrtb002 TO FK_MTRTB013_MTRTB002;
ALTER TABLE mtrsm001.mtrtb021_unidade_autorizada RENAME CONSTRAINT fk_mtrtb021_reference_mtrtb020 TO FK_MTRTB021_MTRTB020;
ALTER TABLE mtrsm001.mtrtb007_atributo_documento RENAME CONSTRAINT fk_mtrtb007_reference_mtrtb003 TO FK_MTRTB007_MTRTB003;
ALTER TABLE mtrsm001.mtrtb024_produto_dossie RENAME CONSTRAINT fk_mtrtb024_reference_mtrtb022 TO FK_MTRTB024_MTRTB022;
ALTER TABLE mtrsm001.mtrtb024_produto_dossie RENAME CONSTRAINT fk_mtrtb024_reference_mtrtb002 TO FK_MTRTB024_MTRTB002;
ALTER TABLE mtrsm001.mtrtb017_situacao_instnca_dcmnto RENAME CONSTRAINT fk_mtrtb017_reference_mtrtb014 TO FK_MTRTB017_MTRTB014;
ALTER TABLE mtrsm001.mtrtb017_situacao_instnca_dcmnto RENAME CONSTRAINT fk_mtrtb017_reference_mtrtb015 TO FK_MTRTB003_MTRTB009;
ALTER TABLE mtrsm001.mtrtb017_situacao_instnca_dcmnto RENAME CONSTRAINT fk_mtrtb017_reference_mtrtb016 TO FK_MTRTB017_MTRTB016;
ALTER TABLE mtrsm001.mtrtb013_situacao_dossie RENAME CONSTRAINT fk_mtrtb013_reference_mtrtb012 TO FK_MTRTB003_MTRTB009;
ALTER TABLE mtrsm001.mtrtb029_campo_apresentacao RENAME CONSTRAINT fk_mtrtb029_reference_mtrtb027 TO FK_MTRTB029_MTRTB027;
ALTER TABLE mtrsm001.mtrtb026_formulario RENAME CONSTRAINT fk_mtrtb026_reference_mtrtb020 TO FK_MTRTB026_MTRTB020;
ALTER TABLE mtrsm001.mtrtb018_unidade_tratamento RENAME CONSTRAINT fk_mtrtb018_reference_mtrtb002 TO FK_MTRTB018_MTRTB002;
ALTER TABLE mtrsm001.mtrtb101_documento RENAME CONSTRAINT fk_mtrtb101_reference_mtrtb100 TO FK_MTRTB101_MTRTB100;
ALTER TABLE mtrsm001.mtrtb014_instancia_documento RENAME CONSTRAINT fk_mtrtb014_reference_mtrtb032 TO FK_MTRTB014_MTRTB032;
ALTER TABLE mtrsm001.mtrtb025_processo_documento RENAME CONSTRAINT fk_mtrtb025_reference_mtrtb020 TO FK_MTRTB025_MTRTB020;
ALTER TABLE mtrsm001.mtrtb025_processo_documento RENAME CONSTRAINT fk_mtrtb025_reference_mtrtb010 TO FK_MTRTB025_MTRTB010;
ALTER TABLE mtrsm001.mtrtb038_nivel_documental RENAME CONSTRAINT fk_mtrtb038_reference_mtrtb036 TO FK_MTRTB038_MTRTB036;
ALTER TABLE mtrsm001.mtrtb038_nivel_documental RENAME CONSTRAINT fk_mtrtb038_reference_mtrtb001 TO FK_MTRTB038_MTRTB001;
ALTER TABLE mtrsm001.mtrtb037_regra_documental RENAME CONSTRAINT fk_mtrtb037_reference_mtrtb006 TO FK_MTRTB037_MTRTB006;
ALTER TABLE mtrsm001.mtrtb003_documento RENAME CONSTRAINT fk_mtrtb003_reference_mtrtb006 TO FK_MTRTB003_MTRTB006;
ALTER TABLE mtrsm001.mtrtb039_produto_composicao RENAME CONSTRAINT fk_mtrtb039_reference_mtrtb036 TO FK_MTRTB039_MTRTB036;
ALTER TABLE mtrsm001.mtrtb039_produto_composicao RENAME CONSTRAINT fk_mtrtb039_reference_mtrtb022 TO FK_MTRTB039_MTRTB022;
ALTER TABLE mtrsm001.mtrtb037_regra_documental RENAME CONSTRAINT fk_mtrtb037_reference_mtrtb010 TO FK_MTRTB037_MTRTB010;
ALTER TABLE mtrsm001.mtrtb025_processo_documento RENAME CONSTRAINT fk_mtrtb025_reference_mtrtb009 TO FK_MTRTB025_MTRTB009;
ALTER TABLE mtrsm001.mtrtb020_processo RENAME CONSTRAINT fk_mtrtb020_reference_mtrtb019 TO FK_MTRTB020_MTRTB019;
ALTER TABLE mtrsm001.mtrtb035_garantia_informada RENAME CONSTRAINT fk_mtrtb035_reference_mtrtb033 TO FK_MTRTB035_MTRTB033;
ALTER TABLE mtrsm001.mtrtb035_garantia_informada RENAME CONSTRAINT fk_mtrtb035_reference_mtrtb002 TO FK_MTRTB035_MTRTB002;
ALTER TABLE mtrsm001.mtrtb014_instancia_documento RENAME CONSTRAINT fk_mtrtb014_reference_mtrtb035 TO FK_MTRTB014_MTRTB035;
ALTER TABLE mtrsm001.mtrtb035_garantia_informada RENAME CONSTRAINT fk_mtrtb035_reference_mtrtb022 TO FK_MTRTB035_MTRTB022;
ALTER TABLE mtrsm001.mtrtb034_garantia_produto RENAME CONSTRAINT fk_mtrtb034_reference_mtrtb022 TO FK_MTRTB034_MTRTB022;
ALTER TABLE mtrsm001.mtrtb034_garantia_produto RENAME CONSTRAINT fk_mtrtb034_reference_mtrtb033 TO FK_MTRTB034_MTRTB033;
ALTER TABLE mtrsm001.mtrtb032_elemento_conteudo RENAME CONSTRAINT fk_mtrtb032_reference_mtrtb022 TO FK_MTRTB032_MTRTB022;
ALTER TABLE mtrsm001.mtrtb023_produto_processo RENAME CONSTRAINT fk_mtrtb023_reference_mtrtb020 TO FK_MTRTB023_MTRTB020;
ALTER TABLE mtrsm001.mtrtb023_produto_processo RENAME CONSTRAINT fk_mtrtb023_reference_mtrtb022 TO FK_MTRTB023_MTRTB022;
ALTER TABLE mtrsm001.mtrtb032_elemento_conteudo RENAME CONSTRAINT fk_mtrtb032_reference_mtrtb020 TO FK_MTRTB032_MTRTB020;
ALTER TABLE mtrsm001.mtrtb027_campo_entrada RENAME CONSTRAINT fk_mtrtb027_reference_mtrtb026 TO FK_MTRTB027_MTRTB026;
ALTER TABLE mtrsm001.mtrtb032_elemento_conteudo RENAME CONSTRAINT fk_mtrtb032_reference_mtrtb009 TO FK_MTRTB032_MTRTB009;
ALTER TABLE mtrsm001.mtrtb040_cadeia_situacao_dossie RENAME CONSTRAINT fk_mtrtb040_reference_mtrtb012 TO FK_MTRTB040_MTRTB012_01;
ALTER TABLE mtrsm001.mtrtb040_cadeia_situacao_dossie RENAME CONSTRAINT fk_mtrtb040_reference2_mtrtb012 TO FK_MTRTB040_MTRTB012_02;
ALTER TABLE mtrsm001.mtrtb041_cadeia_situacao_dcto RENAME CONSTRAINT fk_mtrtb041_reference_mtrtb015 TO FK_MTRTB041_MTRTB015_01;
ALTER TABLE mtrsm001.mtrtb041_cadeia_situacao_dcto RENAME CONSTRAINT fk_mtrtb041_reference2_mtrtb015 TO FK_MTRTB041_MTRTB015_02;
ALTER TABLE mtrsm001.mtrtb001_pessoa_fisica RENAME CONSTRAINT fk_mtrtb001_reference_mtrtb001 TO FK_MTRTB001_MTRTB001_01;
ALTER TABLE mtrsm001.mtrtb001_pessoa_juridica RENAME CONSTRAINT fk_mtrtb001_reference_mtrtb001 TO FK_MTRTB001_MTRTB001_02;
--------------------------------------------------Fim Nome chave estrangeira --------------------------------------------------

--------------------------------------------------Nome index --------------------------------------------------
ALTER INDEX mtrsm001.ix_mtrtb011_01 RENAME TO ix_mtrtb010_01;
ALTER INDEX mtrsm001.ix_mtrtb012_02 RENAME TO ix_mtrtb012_01;
ALTER INDEX mtrsm001.ix_mtrtb034_01 RENAME TO ix_mtrtb033_01;
ALTER INDEX mtrsm001.ix_dostb100_01 RENAME TO ix_mtrtb100_01;
--------------------------------------------------Fim Nome index --------------------------------------------------



--------------------------------------------------Comentários tabela --------------------------------------------------
COMMENT ON TABLE mtrsm001.mtrtb001_dossie_cliente IS 'Tabela responsável pelo armazenamento do dossiê do cliente com seus respectivos dados.
Nesta tabela serão efetivamente armazenados os dados do cliente.';
COMMENT ON TABLE mtrsm001.mtrtb001_pessoa_fisica IS 'Tabela de especialização do dossiê de cliente para armazenar os atributos específicos de uma pessoa física.';
COMMENT ON TABLE mtrsm001.mtrtb001_pessoa_juridica IS 'Tabela de especialização do dossiê de cliente para armazenar os atributos específicos de uma pessoa jurídica.';
COMMENT ON TABLE mtrsm001.mtrtb002_dossie_produto IS 'Tabela responsável pelo armazenamento do dossiê do produto com seus respectivos dados.
Nesta tabela serão efetivamente armazenados os dados do produto.
Para cada produto contratado ou submetido a análise deve ser gerado um novo registro representando o vínculo com o cliente.';
COMMENT ON TABLE mtrsm001.mtrtb003_documento IS 'Tabela responsável pelo armazenamento da referência dos documentos de um determinado cliente.
Esses documentos podem estar associados a um ou mais dossiês de produtos possibilitando o reaproveitamento dos mesmos em diversos produtos.
Nesta tabela serão efetivamente armazenados os dados dos documentos que pode representar o agrupamento de uma ou mais imagens na sua formação.
Também deverão ser armazenadas as propriedades do mesmo e as marcas conforme seu ciclo de vida.';
COMMENT ON TABLE mtrsm001.mtrtb004_dossie_cliente_produto IS 'Tabela de relacionamento para permitir vincular um dossiê de produto a mais de um dossiê de cliente devido a necessidades de produtos com mais de um titular.';
COMMENT ON TABLE mtrsm001.mtrtb005_documento_cliente IS 'Tabela de relacionamento que vincula um documento ao dossiê de um cliente.';
COMMENT ON TABLE mtrsm001.mtrtb006_canal_captura IS 'Tabela responsável pelo armazenamento dos possíveis canais de captura de um documento para identificação de sua origem.';
COMMENT ON TABLE mtrsm001.mtrtb007_atributo_documento IS 'Tabela responsável por armazenar os atributos capturados do documento utilizando a estrutura de chave x valor onde o nome do atributo determina o campo do documento que a informação foi extraída e o conteúdo trata-se do dado propriamente extraído.';
COMMENT ON TABLE mtrsm001.mtrtb008_conteudo IS 'Tabela responsável pelo armazenamento das referências de conteúdo que compõem o documento.
Nesta tabela serão efetivamente armazenados os dados que caracterizam a imagem (ou o binário) e dados para localização do arquivo propriamente dito no repositório.';
COMMENT ON TABLE mtrsm001.mtrtb009_tipo_documento IS 'Tabela responsável pelo armazenamento dos possíveis tipos de documento que podem ser submetidos ao vínculo com os dossiês.';
COMMENT ON TABLE mtrsm001.mtrtb010_funcao_documental IS 'Tabela responsável por armazenar as possíveis funções documentais.
Essa informação permite agrupar documentos que possuem a mesma finalidade e um documento pode possui mais de uma função.
Exemplos dessa atribuição funcional, são:
- Identificação;
- Renda;
- Comprovação de Residência
- etc';
COMMENT ON TABLE mtrsm001.mtrtb011_funcao_documento IS 'Tabela associativa que vincula um tipo de documento a sua função.
Ex: 
- RG x Identificação
- DIRPF x Renda
- DIRPF x Identificação';
COMMENT ON TABLE mtrsm001.mtrtb012_tipo_situacao_dossie IS 'Tabela responsável pelo armazenamento das possíveis situações vinculadas a um dossiê de produto.

Como exemplo podemos ter as possíveis situações:
- Criado
- Atualizado
- Disponível
- Em Análise
- etc';
COMMENT ON TABLE mtrsm001.mtrtb013_situacao_dossie IS 'Tabela responsável por armazenar o histórico de situações relativas ao dossiê do produto. Cada vez que houver uma mudança na situação apresentada pelo processo, um novo registro deve ser inserido gerando assim um histórico das situações vivenciadas durante o seu ciclo de vida.';
COMMENT ON TABLE mtrsm001.mtrtb014_instancia_documento IS 'Tabela responsável pelo armazenamento de instâncias de documentos que estarão vinculados aos dossiês dos produtos.';
COMMENT ON TABLE mtrsm001.mtrtb015_situacao_documento IS 'Tabela responsável pelo armazenamento das possíveis situações vinculadas a um documento.
Essas situações também deverão agrupar motivos para atribuição desta situação.
Como exemplo podemos ter as possíveis situações e entre parênteses os motivos de agrupamento:
- Aprovado
- Rejeitado (Ilegível / Rasurado / Segurança)
- Pendente (Recaptura)';
COMMENT ON TABLE mtrsm001.mtrtb016_motivo_situacao_dcmnto IS 'Tabela de motivos específicos para indicar a causa de uma determinada situação vinculada a um dado documento.
Exemplo:  
- Ilegível -> Rejeitado
- Rasurado -> rejeitado
- Segurança -> Rejeitado
- Recaptura -> pendente';
COMMENT ON TABLE mtrsm001.mtrtb017_situacao_instnca_dcmnto IS 'Tabela responsável por armazenar o histórico de situações relativas a instância do documento em avaliação. Cada vez que houver uma mudança na situação apresentada pelo processo, um novo registro deve ser inserido gerando assim um histórico das situações vivenciadas durante o seu ciclo de vida.';
COMMENT ON TABLE mtrsm001.mtrtb018_unidade_tratamento IS 'Tabela utilizada para identificar as unidades de tratamento atribuídas para o dossiê naquele dado momento.
Sempre que a situação do dossiê for modificada, os registros referentes ao dossiê especificamente serão excluídos e reinseridos novos com base na nova situação.';
COMMENT ON TABLE mtrsm001.mtrtb019_macroprocesso IS 'Tabela utilizada para identificar o macroprocesso que agrupa os processos.';
COMMENT ON TABLE mtrsm001.mtrtb020_processo IS 'Tabela responsável pelo armazenamento dos processos que podem ser atrelados aos dossiês de forma a identificar qual o processo bancário relacionado.
Processos que possuam vinculação com dossiês de produto não devem ser excluídos fisicamente, e sim atribuídos como inativo.
Exemplos de processos na linguagem negocial são:
- Concessão de Crédito Habitacional
- Conta Corrente
- Financiamento de Veículos
- Pagamento de Loterias
- Etc';
COMMENT ON TABLE mtrsm001.mtrtb021_unidade_autorizada IS 'Tabela responsável pelo armazenamento das unidades autorizadas a utilização do processo.';
COMMENT ON TABLE mtrsm001.mtrtb022_produto IS 'Tabela responsável pelo armazenamento dos produtos da CAIXA que serão vinculados aos processos definidos.';
COMMENT ON TABLE mtrsm001.mtrtb023_produto_processo IS 'Tabela de relacionamento para vinculação do produto com o processo. 
Existe a possibilidade que um produto seja vinculado a diversos processos pois pode diferenciar a forma de realizar as ações conforme o canal de contratação, campanha, ou outro fator, como por exemplo uma conta que seja contratada pela agência física, agência virtual, CCA ou Aplicativo de abertura de contas.';
COMMENT ON TABLE mtrsm001.mtrtb024_produto_dossie IS 'Tabela de relacionamento para vinculação dos produtos selecionados para tratamento no dossiê. 
Existe a possibilidade que mais de um produto seja vinculado a um dossiê para tratamento único como é o caso do contrato de relacionamento que envolve Cartão de Credito / CROT / CDC / Conta Corrente.';
COMMENT ON TABLE mtrsm001.mtrtb025_processo_documento IS 'Tabela utilizada para identificar os documentos (por tipo ou função) que são necessários para os titulares do dossiê de um processo específico. Fazendo um paralelo, seria como o os elementos do conteúdo, porém voltados aos documentos do cliente, pois um dossiê pode ter a quantidade de clientes definida dinamicamente e por isso não cabe na estrutura do elemento do conteúdo. Esta estrutura ficará a cargo dos elementos específicos do produto.
Quando um dossiê é criado, todos os CNPFs/CNPJs envolvidos na operação deverão apresentar os tipos de documentos ou algum documento da função documental definidas nesta relação com o processo específico definido para o dossiê de produto.';
COMMENT ON TABLE mtrsm001.mtrtb026_formulario IS 'Tabela responsável por agrupar um conjunto de campos de entrada representando um formulário dinâmico que podem ser utilizados para alimentar dados necessários ao dossiê do produto de um determinado processo em uma determinada fase de utilização.
Dessa forma é possível representar formulários distintos para cada fase do dossiê do produto.';
COMMENT ON TABLE mtrsm001.mtrtb027_campo_entrada IS 'Tabela responsável por armazenar a estrutura de entradas de dados que serão alimentados na inclusão de um novo dossiê para o processo vinculado.
Esta estrutura permitirá realizar a construção dinâmica do formulário.
Registros desta tabela só devem ser excluídos fisicamente caso não exista nenhuma resposta de formulário atrelada a este registro. Caso essa situação ocorra o registro deve ser excluído logicamente definindo seu atributo ativo como false.';
COMMENT ON TABLE mtrsm001.mtrtb028_opcao_campo IS 'Tabela responsável pelo armazenamento de opções pré-definidas para alguns tipos de atributos a exemplo:
- Lista;
- Radio;
- Check;

Registros desta tabela só devem ser excluídos fisicamente caso não exista nenhuma resposta de formulário atrelada a este registro. Caso essa situação ocorra o registro deve ser excluído logicamente definindo seu atributo ativo como false.';
COMMENT ON TABLE mtrsm001.mtrtb029_campo_apresentacao IS 'Tabela utilizada para armazenar informações acerca da apresentação do campo na interface gráfica conforme o dispositivo.';
COMMENT ON TABLE mtrsm001.mtrtb030_resposta_dossie IS 'Tabela responsável pelo armazenamento das respostas aos itens montados dos formulários de inclusão de processos para um dossiê específico.';
COMMENT ON TABLE mtrsm001.mtrtb031_resposta_opcao IS 'Tabela de relacionamento com finalidade de armazenar todas as respostas objetivas informadas pelo cliente a mesma pergunta no formulário de identificação do dossiê.';
COMMENT ON TABLE mtrsm001.mtrtb032_elemento_conteudo IS 'Tabela responsável pelo armazenamento dos elementos que compõem o mapa de documentos para vinculação ao processo.
Esses elementos estão associados aos tipos de documentos para identificação dos mesmo no ato da captura.';
COMMENT ON TABLE mtrsm001.mtrtb033_garantia IS 'Tabela responsável pelo armazenamento das garantias da CAIXA que serão vinculados aos dossiês criados.';
COMMENT ON TABLE mtrsm001.mtrtb034_garantia_produto IS 'Tabela de relacionamento responsável por vincular as garantias possíveis de exibição quando selecionado um dado produto.';
COMMENT ON TABLE mtrsm001.mtrtb035_garantia_informada IS 'Tabela responsável por manter a lista de garantias informadas durante o ciclo de vida do dossiê do produto.
Os documentos submetidos são arquivados normalmente na tabela de documentos e vinculados ao dossiê do produto através de instâncias.';
COMMENT ON TABLE mtrsm001.mtrtb036_composicao_documental IS 'Tabela responsável por agrupar tipos de documentos visando criar estruturas que representam conjuntos de tipos de documentos a serem analisados conjuntamente.
Essa conjunção será utilizada na análise do nível documento e por ser formada como os exemplos a seguir:
- RG
- Contracheque
-----------------------------------------------------------------------------------
- CNH
- Conta Concessionária
- DIRPF';
COMMENT ON TABLE mtrsm001.mtrtb037_regra_documental IS 'Tabela utilizada para armazenar as regras de atendimento da composição. Para que uma composição documental esteja satisfeita, todas as regras a ela associadas devem ser atendidas, ou seja, a regra para cada documento definido deve ser verdadeira. Além da presença do documento vinculado valida no dossiê situações como índice mínimo do antifraude e canal devem ser respeitadas. Caso não seja atendida ao menos uma das regras, a composição não terá suas condições satisfatórias atendidas e consequentemente o nível documental não poderá ser atribuído ao dossiê do cliente.';
COMMENT ON TABLE mtrsm001.mtrtb038_nivel_documental IS 'Tabela responsável por armazenar as referências de níveis documentais possíveis para associação a clientes e produtos.
O nível documental é uma informação pertencente ao cliente, porém o mesmo deve estar associado a um conjunto de tipos de documentos e informações que torna a informação dinâmica para o cliente, ou seja, se um cliente submete um determinado documento que aumenta sua gama de informações válidas, ele pode ganhar um determinado nível documental, porém se um documento passa a ter sua validade ultrapassada o cliente perde aquele determinado nível.';
COMMENT ON TABLE mtrsm001.mtrtb039_produto_composicao IS 'Tabela de relacionamento que vincula uma composição de documentos a um ou mais produtos.
Essa associação visa identificar as necessidade documentais para um determinado produto no ato de sua contratação, permitindo ao sistema autorizar ou não a operação do ponto de vista documental. ';
COMMENT ON TABLE mtrsm001.mtrtb040_cadeia_situacao_dossie IS 'Tabela utilizada para armazenar as possibilidades de tipos de situações possíveis de aplicação em um dossiê de produto a partir um determinado tipo de situação.';
COMMENT ON TABLE mtrsm001.mtrtb041_cadeia_situacao_dcto IS 'Tabela utilizada para armazenar as possibilidades de tipos de situações possíveis de aplicação em uma instância de documento a partir um determinado tipo de situação.';
COMMENT ON TABLE mtrsm001.mtrtb100_autorizacao IS 'Tabela utilizada para armazenar as autorizações relacionadas ao nível documental geradas e entregues para os clientes.';
COMMENT ON TABLE mtrsm001.mtrtb101_documento IS 'Tabela utilizada para armazenar a informação dos documentos identificados e utilizados para a emissão da autorização.';

--------------------------------------------------Fim Comentários tabela --------------------------------------------------

--------------------------------------------------Comentários atributo --------------------------------------------------
COMMENT ON COLUMN mtrsm001.mtrtb001_dossie_cliente.nu_dossie_cliente IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb001_dossie_cliente.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb001_dossie_cliente.nu_cpf_cnpj IS 'Atributo que representa o número do CPF/CNPJ do cliente.';
COMMENT ON COLUMN mtrsm001.mtrtb001_dossie_cliente.ic_tipo_pessoa IS 'Atributo que determina qual tipo de pessoa pode ter o documento atribuído.
		Pode assumir os seguintes valores:
		F - Física
		J - Jurídica
		S - Serviço
		A - Física ou Jurídica
		T - Todos';
COMMENT ON COLUMN mtrsm001.mtrtb001_dossie_cliente.no_cliente IS 'Atributo que representa o nome do cliente.';
COMMENT ON COLUMN mtrsm001.mtrtb001_dossie_cliente. de_telefone IS 'Atributo que representa o telefone informado para o cliente.';
COMMENT ON COLUMN mtrsm001.mtrtb001_pessoa_fisica.nu_dossie_cliente IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb001_pessoa_fisica.dt_nascimento IS 'Atributo utilizado para armazenar a data de nascimento de pessoas físicas.';
COMMENT ON COLUMN mtrsm001.mtrtb001_pessoa_fisica.ic_estado_civil IS 'Atributo utilizado para armazenar o estado civil de pessoas físicas. Pose assumir:
			/* Casado */
			C,
			/* Solteiro */
			S,
			/* Divorciado */
			D,
			/* Desquitado */
			Q,
			/* Viúvo */
			V,
			/* Outros */
			O';
COMMENT ON COLUMN mtrsm001.mtrtb001_pessoa_fisica.nu_nis IS 'Atributo utilizado para armazenar o número do NIS de pessoas físicas.';
COMMENT ON COLUMN mtrsm001.mtrtb001_pessoa_fisica.nu_identidade IS 'Atributo utilizado para armazenar o número de identidade de pessoas físicas.';
COMMENT ON COLUMN mtrsm001.mtrtb001_pessoa_fisica.no_orgao_emissor IS 'Atributo utilizado para armazenar o órgão emissor da identidade de pessoas físicas.';
COMMENT ON COLUMN mtrsm001.mtrtb001_pessoa_fisica.no_mae IS 'Atributo utilizado para armazenar o nome da mãe de pessoas físicas.';
COMMENT ON COLUMN mtrsm001.mtrtb001_pessoa_fisica.no_pai IS 'Atributo utilizado para armazenar o nome do pai de pessoas físicas.';
COMMENT ON COLUMN mtrsm001.mtrtb001_pessoa_juridica.nu_dossie_cliente IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb001_pessoa_juridica.no_razao_social IS 'Atributo utilizado para armazenar a razão social de pessoas jurídicas.';
COMMENT ON COLUMN mtrsm001.mtrtb001_pessoa_juridica.dt_fundacao IS 'Atributo utilizado para armazenar a data de fundação de pessoas jurídicas.';
COMMENT ON COLUMN mtrsm001.mtrtb001_pessoa_juridica.ic_segmento IS 'Atributo para identificar o segmento da empresa, podendo assumir os valores oriundos da view do SIICO:
		- MEI
		- MPE
		- MGE
		- CORP';
COMMENT ON COLUMN mtrsm001.mtrtb002_dossie_produto.nu_dossie_produto IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb002_dossie_produto.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb002_dossie_produto.nu_processo IS 'Atributo utilizado para referenciar o processo ao qual o dossiê de produto esteja vinculado.';
COMMENT ON COLUMN mtrsm001.mtrtb002_dossie_produto.nu_cgc_criacao IS 'Atributo utilizado para armazenar o CGC da unidade de criação do dossiê.';
COMMENT ON COLUMN mtrsm001.mtrtb002_dossie_produto.nu_cgc_priorizado IS 'Atributo que indica o CGC da unidade que deverá tratar o dossiê na próxima chamada da fila por qualquer empregado vinculado ao mesmo.';
COMMENT ON COLUMN mtrsm001.mtrtb002_dossie_produto.co_matricula_priorizado IS 'Atributo que indica o empregado específico da unidade que deverá tratar o dossiê na próxima chamada da fila.';
COMMENT ON COLUMN mtrsm001.mtrtb002_dossie_produto.nu_peso_prioridade IS 'Valor que indica dentre os dossiês priorizados, qual a ordem de captura na chamada da fila, sendo aplicado do maior para o menor. 
O valor é um número livre atribuído pelo usuário que realizar a priorização do dossiê e a fila será organizada pelos dossiês priorizados com valor de peso do maior para o menor, em seguida pela ordem de cadastro definido pelo atributo de data de criação do mais antigo para o mais novo.';
COMMENT ON COLUMN mtrsm001.mtrtb002_dossie_produto.nu_fase_utilizacao IS 'Atributo utilizado para identificar a fase de associação do mapa junto ao dossiê.
Conforme a fase definida no dossiê do produto, o conjunto de imagens e campos do formulário ficam disponíveis para preenchimento, caso selecionado uma fase do processo diferente da atual do dossiê, o formulário e relação de documentos referente a outra fase ficam disponíveis apenas para consulta.';
COMMENT ON COLUMN mtrsm001.mtrtb004_dossie_cliente_produto.nu_dossie_cliente_produto IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb004_dossie_cliente_produto.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb004_dossie_cliente_produto.nu_dossie_produto IS 'Atributo que armazena a referência para o dossiê do produto vinculado na relação.';
COMMENT ON COLUMN mtrsm001.mtrtb004_dossie_cliente_produto.nu_dossie_cliente IS 'Atributo que armazena a referência para o dossiê do cliente vinculado na relação.';
COMMENT ON COLUMN mtrsm001.mtrtb004_dossie_cliente_produto.nu_sequencia_titularidade IS 'Atributo que indica a sequência de titularidade dos clientes para aquele processo. Ao cadastrar um processo o operador pode incluir titulares conforme a necessidade do produto e este atributo indicara a ordinalidade dos titulares.';
COMMENT ON COLUMN mtrsm001.mtrtb004_dossie_cliente_produto.ic_tipo_relacionamento IS 'Atributo que indica o tipo de relacionamento do cliente com o produto, podendo assumir os valores:
- TITULAR
- AVALISTA
- CONJUGE
- SOCIO
etc.';
COMMENT ON COLUMN mtrsm001.mtrtb005_documento_cliente.nu_documento IS 'Atributo que representa o documento vinculado ao dossiê do cliente referenciado no registro.';
COMMENT ON COLUMN mtrsm001.mtrtb005_documento_cliente.nu_dossie_cliente IS 'Atributo que representa a o dossiê do cliente vinculado na relação de documentos.';
COMMENT ON COLUMN mtrsm001.mtrtb006_canal_captura.nu_canal_captura IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb006_canal_captura.nu_versao  IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb006_canal_captura.sg_canal_captura IS 'Atributo utilizado para identificar a sigla do canal de captura.
Pode assumir valor como por exemplo:
- SIMTR (Sistema Interno SIMTR)
- APMOB (Aplicativo Mobile)
- STECX (Site Corporativo da CAIXA)';
COMMENT ON COLUMN mtrsm001.mtrtb006_canal_captura.de_canal_captura IS 'Atributo utilizado para descrever o canal de captura.
Exemplo:
Sistema Interno SIMTR
Sistemas Corporativos
Aplicativo Mobile
Site Corporativo';
COMMENT ON COLUMN mtrsm001.mtrtb007_atributo_documento.nu_atributo_documento IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb007_atributo_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb007_atributo_documento.nu_documento IS 'Atributo utilizado para vincular o atributo definido ao documento cuja informação foi extraída.';
COMMENT ON COLUMN mtrsm001.mtrtb007_atributo_documento.de_atributo IS 'Atributo utilizado para armazenar a descrição da chave que identifica o atributo.
Como exemplo, um registro que armazena a data de nascimento de um RG: 
data_nascimento = 01/09/1980
Neste caso o conteúdo deste campo seria "data nascimento" e o atributo conteúdo armazenaria "01/09/1980" tal qual extraído do documento.';
COMMENT ON COLUMN mtrsm001.mtrtb007_atributo_documento.de_conteudo IS 'Atributo utilizado para armazenar a dado extraído de um campo do documento.
Como exemplo, um registro que armazena a data de nascimento de um RG: 
data nascimento = 01/09/1980
Neste caso o conteúdo deste campo seria "01/09/1980" tal qual extraído do documento e o atributo de descrição armazenaria "data nascimento".';
COMMENT ON COLUMN mtrsm001.mtrtb008_conteudo.nu_conteudo IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb008_conteudo.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb008_conteudo.nu_documento IS 'Atributo que representa o documento vinculado ao conteúdo.';
COMMENT ON COLUMN mtrsm001.mtrtb008_conteudo.de_uri IS 'Atributo que representa a localização da imagem do documento no repositório.';
COMMENT ON COLUMN mtrsm001.mtrtb008_conteudo.co_ged IS 'Atributo que representa o código do conteúdo do documento no GED.';
COMMENT ON COLUMN mtrsm001.mtrtb008_conteudo.nu_ordem IS 'Atributo utilizado para identificar a ordem de exibição na composição do documento. Documentos que possuem apenas um elemento, como um arquivo pdf por exemplo terá apenas um registro de conteúdo com o atributo de ordem como 1.';
COMMENT ON COLUMN mtrsm001.mtrtb008_conteudo.no_formato IS 'Atributo utilizado para armazenar o formato do documento. Ex:
- pdf
- jpg
- tiff';
COMMENT ON COLUMN mtrsm001.mtrtb009_tipo_documento.nu_tipo_documento IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb009_tipo_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb009_tipo_documento.no_tipo_documento IS 'Atributo que identifica o tipo de documento vinculado. Como exemplo podemos ter:
- RG
- CNH
- Certidão Negativa de Debito
- Passaporte
- Etc	';
COMMENT ON COLUMN mtrsm001.mtrtb009_tipo_documento.ic_tipo_pessoa IS 'Atributo que determina qual tipo de pessoa pode ter o documento atribuído.
Pode assumir os seguintes valores:
F - Física
J - Jurídica
S - Serviço
A - Física ou Jurídica
T - Todos';
COMMENT ON COLUMN mtrsm001.mtrtb009_tipo_documento.pz_validade_dias IS 'Atributo que indica a quantidade de dias para atribuição da validade do documento a partir da sua emissão.
Caso o valor deste atributo não esteja definido, significa que o documento possui um prazo de validade indeterminado.';
COMMENT ON COLUMN mtrsm001.mtrtb009_tipo_documento.ic_validade_auto_contida IS 'Atributo determina se a validade do documento está definida no próprio documento ou não como por exemplo no caso de certidões que possuem a validade determinada em seu corpo.
Caso o valor deste atributo seja falso, o prazo de validade deve ser calculado conforme definido no atributo de prazo de validade.';
COMMENT ON COLUMN mtrsm001.mtrtb009_tipo_documento.co_tipologia IS 'Atributo utilizado para armazenar o código da tipologia documental corporativa.';
COMMENT ON COLUMN mtrsm001.mtrtb010_funcao_documental.nu_funcao_documental IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb010_funcao_documental.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb010_funcao_documental.no_funcao IS 'Atributo definido para armazenar o nome da função documental, como por exemplo:
- Identificação
- Comprovação de Renda
- Comprovação de Residência
- Regularidade Fiscal
- etc';
COMMENT ON COLUMN mtrsm001.mtrtb010_funcao_documental.ic_ativo IS 'Atributo que indica se a função documental está ativa ou não para utilização no sistema.
Uma função só pode ser excluída fisicamente caso ela não possua relação com nenhum tipo de documento previamente.';
COMMENT ON COLUMN mtrsm001.mtrtb012_tipo_situacao_dossie.nu_tipo_situacao_dossie IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb012_tipo_situacao_dossie.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.
COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.no_tipo_situacao IS Atributo que armazena o nome do tipo de situação do dossiê.';
COMMENT ON COLUMN mtrsm001.mtrtb012_tipo_situacao_dossie.ic_resumo IS 'Atributo utilizado para indicar se o tipo de situação gera agrupamento para exibição de resumo de dossiês.';
COMMENT ON COLUMN mtrsm001.mtrtb012_tipo_situacao_dossie.ic_produtividade IS 'Atributo utilizado para indicar se o tipo de situação considera o dossiê na contagem da produtividade diária.';
COMMENT ON COLUMN mtrsm001.mtrtb012_tipo_situacao_dossie.ic_tipo_inicial IS 'Atributo utilizado para identificar o tipo de situação que deve ser aplicado como primeiro tipo de situação de um dossiê de produto.';
COMMENT ON COLUMN mtrsm001.mtrtb013_situacao_dossie.nu_situacao_dossie IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb013_situacao_dossie.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb013_situacao_dossie.nu_dossie_produto IS 'Atributo utilizado pata armazenar a referência do dossiê do produto vinculado a situação.';
COMMENT ON COLUMN mtrsm001.mtrtb013_situacao_dossie.nu_tipo_situacao_dossie IS 'Atributo utilizado para armazenar o tipo situação do dossiê que será atribuído manualmente pelo operador ou pela automação do workflow quando estruturado.';
COMMENT ON COLUMN mtrsm001.mtrtb013_situacao_dossie.ts_inclusao IS 'Atributo utilizado para armazenar a data/hora de atribuição da situação ao dossiê.';
COMMENT ON COLUMN mtrsm001.mtrtb013_situacao_dossie.co_matricula IS 'Atributo utilizado para armazenar a matrícula do empregado ou serviço que atribuiu a situação ao dossiê.';
COMMENT ON COLUMN mtrsm001.mtrtb013_situacao_dossie.nu_cgc_unidade IS 'Atributo que indica a unidade do empregado que registrou a situação do dossiê.';
COMMENT ON COLUMN mtrsm001.mtrtb013_situacao_dossie.de_observacao IS 'Informação do usuário indicando uma observação com relação ao motivo de atribuição da situação definida.';
COMMENT ON COLUMN mtrsm001.mtrtb014_instancia_documento.nu_instancia_documento IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb014_instancia_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb014_instancia_documento.nu_documento IS 'Atributo que vincula o registro da instância de documento ao documento propriamente dito permitindo assim o reaproveitamento de documento previamente existentes.';
COMMENT ON COLUMN mtrsm001.mtrtb014_instancia_documento.nu_dossie_produto IS 'Atributo que armazena a referência do dossiê de produto vinculado a instância do documento.';
COMMENT ON COLUMN mtrsm001.mtrtb014_instancia_documento.nu_elemento_conteudo IS 'Atributo que representa o elemento de conteúdo do processo ao qual foi a instância foi vinculada. Utilizado apenas para os casos de documentos submetidos pelo mapa de processo. Para os casos de documento do cliente associados/utilizados no dossiê do produto este atributo não estará definido.';
COMMENT ON COLUMN mtrsm001.mtrtb014_instancia_documento.nu_garantia_informada IS 'Atributo utilizado pata armazenar a referência da garantia informada vinculada a instância do documento.';
COMMENT ON COLUMN mtrsm001.mtrtb015_situacao_documento.nu_situacao_documento IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb015_situacao_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb015_situacao_documento.no_situacao IS 'Atributo que armazena o nome da situação do documento.';
COMMENT ON COLUMN mtrsm001.mtrtb015_situacao_documento.ic_situacao_inicial IS 'Atributo utilizado para identificar o tipo de situação que deve ser aplicado como primeiro tipo de situação de uma instância de documento.';
COMMENT ON COLUMN mtrsm001.mtrtb015_situacao_documento.ic_situacao_final IS 'Atributo utilizado para identificar o tipo de situação que é final. Após ser aplicado uma situação deste tipo, o sistema não deverá permitir que sejam criados novos registros de situação para a instância dos documentos.';
COMMENT ON COLUMN mtrsm001.mtrtb016_motivo_situacao_dcmnto.nu_motivo_situacao_dcto IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb016_motivo_situacao_dcmnto.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb016_motivo_situacao_dcmnto.no_motivo_situacao_dcto IS 'Atributo que armazena o nome do motivo da situação do documento.';
COMMENT ON COLUMN mtrsm001.mtrtb017_situacao_instnca_dcmnto.nu_situacao_instancia_dcto IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb017_situacao_instnca_dcmnto.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb017_situacao_instnca_dcmnto.nu_instancia_documento IS 'Atributo utilizado pata armazenar a referência da instancia do documento em avaliação vinculado a situação.';
COMMENT ON COLUMN mtrsm001.mtrtb017_situacao_instnca_dcmnto.nu_situacao_documento IS 'Atributo utilizado pata armazenar a referência a situação do documento escolhida vinculada a instância do documento em avaliação.';
COMMENT ON COLUMN mtrsm001.mtrtb017_situacao_instnca_dcmnto.nu_motivo_situacao_dcto IS 'Atributo utilizado pata armazenar a referência o motivo específico para a situação escolhida vinculada a instância do documento em avaliação.';
COMMENT ON COLUMN mtrsm001.mtrtb017_situacao_instnca_dcmnto.ts_inclusao IS 'Atributo utilizado para armazenar a data/hora de atribuição da situação ao dossiê.';
COMMENT ON COLUMN mtrsm001.mtrtb017_situacao_instnca_dcmnto.co_matricula IS 'Atributo utilizado para armazenar a matrícula do empregado ou serviço que atribuiu a situação a instância do documento em avaliação.';
COMMENT ON COLUMN mtrsm001.mtrtb017_situacao_instnca_dcmnto.nu_cgc_unidade IS 'Atributo que indica o CGC da unidade do empregado que registrou a situação da instancia do documento analisado.';
COMMENT ON COLUMN mtrsm001.mtrtb018_unidade_tratamento.nu_dossie_produto IS 'Atributo utilizado para vincular o dossiê do produto com a unidade de tratamento.';
COMMENT ON COLUMN mtrsm001.mtrtb018_unidade_tratamento.nu_cgc_unidade  IS 'Atributo que indica o número do CGC da unidade responsável pelo tratamento do dossiê.';
COMMENT ON COLUMN mtrsm001.mtrtb019_macroprocesso.nu_macroprocesso IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb019_macroprocesso.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb019_macroprocesso.no_macroprocesso IS 'Atributo utilizado para armazenar o nome do macroprocesso.';
COMMENT ON COLUMN mtrsm001.mtrtb019_macroprocesso.de_avatar IS 'Atributo utilizado para armazenar o nome do avatar que será disponibilizado no pacote da interface gráfica para montagem e apresentação das filas de captura com as informações do processo.';
COMMENT ON COLUMN mtrsm001.mtrtb020_processo.nu_processo IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb020_processo.nu_versao  IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb020_processo.no_processo IS 'Atributo utilizado para armazenar o nome de identificação negocial do processo.';
COMMENT ON COLUMN mtrsm001.mtrtb021_unidade_autorizada.nu_unidade_autorizada IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb021_unidade_autorizada.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb021_unidade_autorizada.nu_cgc IS 'Atributo que representa o CGC da unidade autorizada.';
COMMENT ON COLUMN mtrsm001.mtrtb021_unidade_autorizada.ic_tipo_tratamento IS 'Atributo que indica as ações possíveis a serem realizadas no processo para a determinada unidade. As somas dos valores das ações determinam quais as permissões da unidade sobre os dossiês do processo. Os valores possíveis são:
1 - CONSULTA_DOSSIE
2 - TRATAR_DOSSIE
4 - CRIAR_DOSSIE
8 - PRIORIZAR_DOSSIE
Considerando o fato, como exemplo uma unidade que possua o valor 7 atribuído pode consultar, tratar e criar dossiês, mas não pode priorizar.';
COMMENT ON COLUMN mtrsm001.mtrtb022_produto.nu_produto IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb022_produto.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb022_produto.nu_operacao IS 'Atributo que armazena o número de operação corporativa do produto.';
COMMENT ON COLUMN mtrsm001.mtrtb022_produto.nu_modalidade IS 'Atributo que armazena o número da modalidade corporativa do produto.';
COMMENT ON COLUMN mtrsm001.mtrtb022_produto.no_produto IS 'Atributo que armazena o nome corporativo do produto.';
COMMENT ON COLUMN mtrsm001.mtrtb024_produto_dossie.nu_produto_dossie IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb024_produto_dossie.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb024_produto_dossie.nu_dossie_produto IS 'Atributo que representa o dossiê de vinculação da coleção de produtos em análise.';
COMMENT ON COLUMN mtrsm001.mtrtb024_produto_dossie.vr_contrato IS 'Atributo que representa o valor do produto.';
COMMENT ON COLUMN mtrsm001.mtrtb024_produto_dossie.pc_juros_operacao IS 'Percentual de juros utilizado na contratação do produto.';
COMMENT ON COLUMN mtrsm001.mtrtb024_produto_dossie.pz_operacao IS 'Prazo utilizado na contratação do produto.';
COMMENT ON COLUMN mtrsm001.mtrtb024_produto_dossie.pz_carencia IS 'Prazo utilizado como carência na contratação do produto.';
COMMENT ON COLUMN mtrsm001.mtrtb024_produto_dossie.co_contrato_renovado IS 'Atributo utilizado para armazenar o contrato liquidado/renovado.';
COMMENT ON COLUMN mtrsm001.mtrtb025_processo_documento.nu_processo_documento IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb025_processo_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb025_processo_documento.nu_processo IS 'Atributo utilizado para identificar o processo de vinculação para agrupar os documentos necessários.';
COMMENT ON COLUMN mtrsm001.mtrtb025_processo_documento.nu_funcao_documental IS 'Atributo utilizado para referenciar uma função documental necessária ao processo. Quando definido, qualquer documento valido que o cliente tenha que seja desta função documental, deve ser considerado que o documento existente já atende à necessidade. Caso este atributo esteja nulo, o atributo que representa o tipo de documento deverá estar preenchido.';
COMMENT ON COLUMN mtrsm001.mtrtb025_processo_documento.nu_tipo_documento IS 'Atributo utilizado para referenciar um tipo de documento específico, necessário ao processo. Quando definido, apenas a presença do documento específica em estado válido, presente e associado ao dossiê do cliente deve ser considerado existente e já atende à necessidade. Caso este atributo esteja nulo, o atributo que representa a função documental deverá estar preenchido.';
COMMENT ON COLUMN mtrsm001.mtrtb025_processo_documento.ic_tipo_relacionamento IS 'Atributo que indica o tipo de relacionamento do cliente com o produto, podendo assumir os valores:
- TITULAR
- AVALISTA
- CONJUGE
- SOCIO
etc.';
COMMENT ON COLUMN mtrsm001.mtrtb026_formulario.nu_formulario IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb026_formulario.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb026_formulario.nu_processo IS  'Atributo utilizado para vincular o processo ao formulário.';
COMMENT ON COLUMN mtrsm001.mtrtb026_formulario.nu_fase_utilizacao IS 'Atributo utilizado para identificar a fase de associação do mapa junto ao dossiê.
Conforme a fase definida no dossiê do produto, o conjunto de imagens e campos do formulário ficam disponíveis para preenchimento, caso selecionado uma fase do processo diferente da atual do dossiê, o formulário e relação de documentos referente a outra fase ficam disponíveis apenas para consulta.
Caso o valor esteja preenchido com 0 significa que o formulário não deverá ser utilizado.';
COMMENT ON COLUMN mtrsm001.mtrtb027_campo_entrada.nu_campo_entrada IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb027_campo_entrada.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb027_campo_entrada.no_campo IS 'Atributo que indica o nome do campo. Este nome pode ser utilizado pela estrutura de programação para referenciar a campo no formulário independente do label exposto para o usuário.';
COMMENT ON COLUMN mtrsm001.mtrtb027_campo_entrada.ic_tipo IS 'Atributo utilizado para armazenar o tipo de campo de formulário que será gerado. Exemplos válidos para este atributo são:
- TEXT
- SELECT
- RADIO';

COMMENT ON COLUMN mtrsm001.mtrtb027_campo_entrada.ic_chave IS 'Atributo que indica se o campo do formulário pode ser utilizado como chave de pesquisa posterior.';
COMMENT ON COLUMN mtrsm001.mtrtb027_campo_entrada.no_label IS 'Atributo que armazena o valor a ser exibido no label do campo do formulário para o usuário final.';
COMMENT ON COLUMN mtrsm001.mtrtb027_campo_entrada.ic_obrigatorio IS 'Atributo que armazena o indicativo da obrigatoriedade do campo no formulário.';
COMMENT ON COLUMN mtrsm001.mtrtb027_campo_entrada.de_mascara IS 'Atributo que armazena o valor da máscara de formatação do campo de for o caso.';
COMMENT ON COLUMN mtrsm001.mtrtb027_campo_entrada.nu_tamanho_minimo IS 'Atributo que armazena o número de caracteres mínimo utilizados em campos de texto livre.';
COMMENT ON COLUMN mtrsm001.mtrtb027_campo_entrada.nu_tamanho_maximo IS 'Atributo que armazena o número de caracteres máximo utilizados em campos de texto livre.';
COMMENT ON COLUMN mtrsm001.mtrtb027_campo_entrada.de_expressao IS 'Atributo que armazena a expressão a ser aplicada pelo Java script para determinar a exposição ou não do campo no formulário.';
COMMENT ON COLUMN mtrsm001.mtrtb027_campo_entrada.ic_ativo IS 'Atributo que indica se o campo de entrada está apto ou não para ser inserido no formulário.';
COMMENT ON COLUMN mtrsm001.mtrtb027_campo_entrada.nu_ordem IS 'Atributo utilizado para definir a ordem de exibição dos campos do formulário.';
COMMENT ON COLUMN mtrsm001.mtrtb028_opcao_campo.nu_opcao_campo IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb028_opcao_campo.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb028_opcao_campo.nu_campo_entrada IS 'Atributo que identifica o campo de entrada do formulário ao qual a opção está associada.';
COMMENT ON COLUMN mtrsm001.mtrtb028_opcao_campo.no_value IS 'Atributo utilizado para armazenar o valor que será definido como value da opção na interface gráfica.';
COMMENT ON COLUMN mtrsm001.mtrtb028_opcao_campo.no_opcao IS 'Atributo que armazena o valor da opção que será exibida para o usuário no campo do formulário.';
COMMENT ON COLUMN mtrsm001.mtrtb028_opcao_campo.ic_ativo IS 'Atributo que indica se a opção do campo de entrada está apta ou não para ser inserido no campo de entrada do formulário.';
COMMENT ON COLUMN mtrsm001.mtrtb029_campo_apresentacao.nu_campo_apresentacao IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb029_campo_apresentacao.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb029_campo_apresentacao.nu_campo_entrada IS 'Atributo que representa a o campo de entrada ao qual a forma de exibição referência.';
COMMENT ON COLUMN mtrsm001.mtrtb029_campo_apresentacao.nu_largura IS 'Atributo que armazena o número de colunas do bootstrap ocupadas pelo campo do formulário na estrutura de tela. Este valor pode variar de 1 a 12.';
COMMENT ON COLUMN mtrsm001.mtrtb030_resposta_dossie.nu_resposta_dossie IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb030_resposta_dossie.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb030_resposta_dossie.nu_campo_entrada IS 'Atributo utilizado para identificar o campo do formulário dinâmico ao qual a resposta está vinculada.';
COMMENT ON COLUMN mtrsm001.mtrtb030_resposta_dossie.de_resposta IS 'Atributo utilizado para armazenar a resposta informada no formulário nos casos de atributos em texto aberto.';
COMMENT ON COLUMN mtrsm001.mtrtb031_resposta_opcao.nu_opcao_campo IS 'Atributo que representa a opção selecionada vinculado na relação com a resposta do formulário.';
COMMENT ON COLUMN mtrsm001.mtrtb032_elemento_conteudo.nu_elemento_conteudo IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb032_elemento_conteudo.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb032_elemento_conteudo.nu_produto IS 'Atributo utilizado para vincular o produto ao elemento conteúdo.';
COMMENT ON COLUMN mtrsm001.mtrtb032_elemento_conteudo.nu_processo IS 'Atributo utilizado para vincular o produto ao elemento conteúdo.';
COMMENT ON COLUMN mtrsm001.mtrtb032_elemento_conteudo.nu_qtde_obrigatorio IS 'Este atributo indica a quantidade de elementos que são de tipo de elemento final obrigatórios dentro da sua árvore e só deve ser preenchido se o tipo do elemento permitir agrupamento: Exemplo:
- Identificação (Este elemento deve ter 2 filhos obrigatórios)
   |-- RG
   |-- CNH
   |-- Passaporte
   |-- CTPS';
COMMENT ON COLUMN mtrsm001.mtrtb032_elemento_conteudo.ic_validar IS 'Atributo que indica se o documento deve ser validado quando apresentado no processo.
Caso verdadeiro, a instância do documento deve ser criada com a situação vazia
Caso false, a instância do documento deve ser criada com a situação de aprovada conforme regra de negócio realizada pelo sistema, desde que já exista outra instância do mesmo documento com situação aprovada previamente.';
COMMENT ON COLUMN mtrsm001.mtrtb032_elemento_conteudo.nu_fase_utilizacao IS 'Atributo utilizado para identificar a fase de associação do mapa junto ao dossiê.
Conforme a fase definida no dossiê do produto, o conjunto de imagens e campos do formulário ficam disponíveis para preenchimento, caso selecionado uma fase do processo diferente da atual do dossiê, o formulário e relação de documentos referente a outra fase ficam disponíveis apenas para consulta.
Caso o valor esteja preenchido com 0 significa que o mapa não deverá ser utilizado.';
COMMENT ON COLUMN mtrsm001.mtrtb032_elemento_conteudo.no_elemento IS 'Atributo utilizado para armazenar o nome de apresentação do tipo de elemento de conteúdo.
Este atributo deve estar preenchido quando a vinculação com o tipo de documento for nula, pois nesta situação o valor desta tabela será apresentado na interface gráfica. O registro que possuir vinculação com o tipo de documento, será o nome deste que deverá ser exposto.';
COMMENT ON COLUMN mtrsm001.mtrtb033_garantia.nu_garantia IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb033_garantia.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb033_garantia.nu_operacao IS 'Atributo que armazena o número de operação corporativa da garantia.';
COMMENT ON COLUMN mtrsm001.mtrtb035_garantia_informada.nu_garantia_informada IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb035_garantia_informada.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb035_garantia_informada.nu_dossie_produto IS 'Atributo utilizado para vincular o dossiê produto a garantia.';
COMMENT ON COLUMN mtrsm001.mtrtb035_garantia_informada.nu_garantia IS 'Atributo que representa o código da garantia vinculada.';
COMMENT ON COLUMN mtrsm001.mtrtb035_garantia_informada.nu_produto IS 'Atributo utilizado para vincular o produto a garantia.';
COMMENT ON COLUMN mtrsm001.mtrtb035_garantia_informada.vr_garantia_informada IS 'Valor informado da garantia oferecida no dia da simulação.';
COMMENT ON COLUMN mtrsm001.mtrtb036_composicao_documental.nu_composicao_documental IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb036_composicao_documental.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb036_composicao_documental.no_composicao_documental IS 'Atributo utilizado para armazenar o nome negocial da composição de documentos.';
COMMENT ON COLUMN mtrsm001.mtrtb036_composicao_documental.ts_inclusao IS 'Atributo que armazena a data/hora de cadastro do registro da composição documental.';
COMMENT ON COLUMN mtrsm001.mtrtb036_composicao_documental.ts_revogacao IS 'Atributo que armazena a data/hora de revogação do registro da composição documental.';
COMMENT ON COLUMN mtrsm001.mtrtb036_composicao_documental.co_matricula_inclusao IS 'Atributo que armazena a matrícula do usuário/serviço que realizou o cadastro do registro da composição documental.';
COMMENT ON COLUMN mtrsm001.mtrtb036_composicao_documental.co_matricula_revogacao IS 'Atributo que armazena a matrícula do usuário/serviço que realizou a revogação do registro da composição documental.';
COMMENT ON COLUMN mtrsm001.mtrtb037_regra_documental.nu_regra_documental IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb037_regra_documental.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb037_regra_documental.nu_composicao_documental IS 'Atributo que representa composição de tipos de documentos associada aos possíveis tipos de documentos.';
COMMENT ON COLUMN mtrsm001.mtrtb037_regra_documental.nu_canal_captura IS 'Atributo utilizado para identificar o canal de captura específico para categorizar o conjunto.
Caso este atributo seja nulo, ele permite ao conjunto valer-se de qualquer canal não especificado em outro conjunto para o mesmo documento e composição documental, porém tendo o canal especificado.';
COMMENT ON COLUMN mtrsm001.mtrtb037_regra_documental.in_antifraude IS 'Atributo utilizado para armazenar o valor mínimo aceitável do índice atribuído ao documento pelo sistema de antifraude para considerar o documento válido na composição documental permitindo atribuir o nível documental ao dossiê do cliente.';
COMMENT ON COLUMN mtrsm001.mtrtb038_nivel_documental.nu_composicao_documental IS 'Atributo utilizado para identificar a composição documental que foi atingida ao atribuir o nível documental para o cliente.';
COMMENT ON COLUMN mtrsm001.mtrtb038_nivel_documental.nu_dossie_cliente IS 'Atributo que representa o dossiê do cliente vinculado na atribuição do nível documental.';
COMMENT ON COLUMN mtrsm001.mtrtb040_cadeia_situacao_dossie.nu_tipo_situacao_atual IS 'Atributo que representa o tipo de situação atual na relação.';
COMMENT ON COLUMN mtrsm001.mtrtb040_cadeia_situacao_dossie.nu_tipo_situacao_seguinte IS 'Atributo que representa o tipo de situação que pode ser aplicado como próximo tipo de situação de um dossiê de produto.';
COMMENT ON COLUMN mtrsm001.mtrtb041_cadeia_situacao_dcto.nu_situacao_documento_atual IS 'Atributo que representa o tipo de situação atual na relação.';
COMMENT ON COLUMN mtrsm001.mtrtb041_cadeia_situacao_dcto.nu_situacao_documento_seguinte IS 'Atributo que representa o tipo de situação que pode ser aplicado como próximo tipo de situação de uma instância de documento.';
COMMENT ON COLUMN mtrsm001.mtrtb100_autorizacao.nu_autorizacao IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb100_autorizacao.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb100_autorizacao.nu_autorizacao_dossie IS 'Atributo utilizado para armazenar o código de autorização gerado para entrega ao sistema de negócio e armazenamento junto ao dossiê do cliente.';
COMMENT ON COLUMN mtrsm001.mtrtb100_autorizacao.ts_registro IS 'Atributo utilizado para armazenar a data e hora de recebimento da solicitação de autorização.'; 
COMMENT ON COLUMN mtrsm001.mtrtb100_autorizacao.ts_informe_negocio IS 'Atributo utilizado para armazenar a data e hora de entrega do código de autorização para o sistema de negócio solicitante.';
COMMENT ON COLUMN mtrsm001.mtrtb100_autorizacao.co_protocolo_negocio IS 'Atributo utilizado para armazenar o protocolo de confirmação de recebimento do código de autorização pelo sistema de negócio solicitante.';
COMMENT ON COLUMN mtrsm001.mtrtb100_autorizacao.ts_informe_ecm IS 'Atributo utilizado para armazenar a data e hora de entrega do código de autorização para o sistema de ECM para armazenamento junto ao dossiê do produto.';
COMMENT ON COLUMN mtrsm001.mtrtb100_autorizacao.co_protocolo_ecm IS 'Atributo utilizado para armazenar a data e hora de entrega do código de autorização para o sistema de ECM para armazenamento junto ao dossiê do produto.';
COMMENT ON COLUMN mtrsm001.mtrtb100_autorizacao.nu_operacao IS 'Atributo utilizado para armazenar o código de operação do produto solicitado na autorização.';
COMMENT ON COLUMN mtrsm001.mtrtb100_autorizacao.nu_cpf_cnpj IS 'Atributo utilizado para armazenar o número do CPF ou CNPJ do cliente relacionado com a autorização.';
COMMENT ON COLUMN mtrsm001.mtrtb100_autorizacao.ic_tipo_pessoa IS 'Atributo utilizado para indicar o tipo de pessoa, se física ou jurídica podendo assumir os seguintes valores:
F - Física
J - Jurídica';
COMMENT ON COLUMN mtrsm001.mtrtb101_documento.nu_documento IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrsm001.mtrtb101_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrsm001.mtrtb101_documento.nu_autorizacao IS 'Atributo utilizado para identificar a autorização que está relacionada ao documento.';
COMMENT ON COLUMN mtrsm001.mtrtb101_documento.nu_documento_cliente IS 'Atributo utilizado para armazenar a referência do registro do documento utilizado na emissão da autorização.';

--------------------------------------------------Fim Comentários atributos --------------------------------------------------