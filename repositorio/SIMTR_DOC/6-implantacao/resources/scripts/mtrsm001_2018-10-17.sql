--
-- PostgreSQL database dump
-- Aplicado até a versão abaixo do Flyway:
-- 1.0.0.33, do arquivo 'V1_0_0_33__Titulo_Apenso_PAE.sql'
--

-- Dumped from database version 9.4.5
-- Dumped by pg_dump version 9.5.5

-- Started on 2018-10-17 11:43:09

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 19 (class 2615 OID 2025537)
-- Name: mtrsm001; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA mtrsm001;

SET search_path = mtrsm001, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 1823 (class 1259 OID 2025540)
-- Name: mtrtb001_dossie_cliente; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb001_dossie_cliente (
    nu_dossie_cliente bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_cpf_cnpj bigint NOT NULL,
    ic_tipo_pessoa character varying(1) NOT NULL,
    no_cliente character varying(255) NOT NULL,
    de_telefone character varying(15)
);

COMMENT ON TABLE mtrtb001_dossie_cliente IS 'Tabela responsável pelo armazenamento do dossiê do cliente com seus respectivos dados.
Nesta tabela serão efetivamente armazenados os dados do cliente.';
COMMENT ON COLUMN mtrtb001_dossie_cliente.nu_dossie_cliente IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb001_dossie_cliente.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb001_dossie_cliente.nu_cpf_cnpj IS 'Atributo que representa o número do CPF/CNPJ do cliente.';
COMMENT ON COLUMN mtrtb001_dossie_cliente.ic_tipo_pessoa IS 'Atributo que determina qual tipo de pessoa pode ter o documento atribuído.
		Pode assumir os seguintes valores:
		F - Física
		J - Jurídica
		S - Serviço
		A - Física ou Jurídica
		T - Todos';
COMMENT ON COLUMN mtrtb001_dossie_cliente.no_cliente IS 'Atributo que representa o nome do cliente.';
COMMENT ON COLUMN mtrtb001_dossie_cliente.de_telefone IS 'Atributo que representa o telefone informado para o cliente.';

--
-- TOC entry 1824 (class 1259 OID 2025543)
-- Name: mtrsq001_dossie_cliente; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq001_dossie_cliente
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq001_dossie_cliente OWNED BY mtrtb001_dossie_cliente.nu_dossie_cliente;


--
-- TOC entry 1825 (class 1259 OID 2025545)
-- Name: mtrtb002_dossie_produto; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb002_dossie_produto (
    nu_dossie_produto bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_processo_dossie integer NOT NULL,
    nu_unidade_criacao integer NOT NULL,
    nu_unidade_priorizado integer,
    co_matricula_priorizado character varying(7),
    nu_peso_prioridade integer,
    ts_finalizado timestamp without time zone,
    ic_canal_caixa character varying(3) NOT NULL,
    nu_processo_fase integer NOT NULL,
    nu_instancia_processo_bpm bigint
);

COMMENT ON TABLE mtrtb002_dossie_produto IS 'Tabela responsável pelo armazenamento do dossiê do produto com seus respectivos dados.
Nesta tabela serão efetivamente armazenados os dados do produto.
Para cada produto contratado ou submetido a análise deve ser gerado um novo registro representando o vínculo com o cliente.';
COMMENT ON COLUMN mtrtb002_dossie_produto.nu_dossie_produto IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb002_dossie_produto.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb002_dossie_produto.nu_processo_dossie IS 'Atributo utilizado para referenciar o processo ao qual o dossiê de produto esteja vinculado.';
COMMENT ON COLUMN mtrtb002_dossie_produto.nu_unidade_criacao IS 'Atributo utilizado para armazenar o CGC da unidade de criação do dossiê.';
COMMENT ON COLUMN mtrtb002_dossie_produto.nu_unidade_priorizado IS 'Atributo que indica o CGC da unidade que deverá tratar o dossiê na próxima chamada da fila por qualquer empregado vinculado ao mesmo.';
COMMENT ON COLUMN mtrtb002_dossie_produto.co_matricula_priorizado IS 'Atributo que indica o empregado específico da unidade que deverá tratar o dossiê na próxima chamada da fila.';
COMMENT ON COLUMN mtrtb002_dossie_produto.nu_peso_prioridade IS 'Valor que indica dentre os dossiês priorizados, qual a ordem de captura na chamada da fila, sendo aplicado do maior para o menor. 
O valor é um número livre atribuído pelo usuário que realizar a priorização do dossiê e a fila será organizada pelos dossiês priorizados com valor de peso do maior para o menor, em seguida pela ordem de cadastro definido pelo atributo de data de criação do mais antigo para o mais novo.';
COMMENT ON COLUMN mtrtb002_dossie_produto.ts_finalizado IS 'Atributo que armazena a data e hora que foi realizada a inclusão do dossiê.';
COMMENT ON COLUMN mtrtb002_dossie_produto.nu_instancia_processo_bpm IS 'Atributo que armazena o identificador da instancia de processo em execução perante a solução de BPM vinculada ao dossiê de produto.';

--
-- TOC entry 1826 (class 1259 OID 2025548)
-- Name: mtrsq002_dossie_produto; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq002_dossie_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq002_dossie_produto OWNED BY mtrtb002_dossie_produto.nu_dossie_produto;


--
-- TOC entry 1827 (class 1259 OID 2025550)
-- Name: mtrtb003_documento; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb003_documento (
    nu_documento bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_tipo_documento integer NOT NULL,
    nu_canal_captura integer NOT NULL,
    ts_captura timestamp without time zone NOT NULL,
    co_matricula character varying(7) NOT NULL,
    ts_validade timestamp without time zone,
    ic_dossie_digital boolean NOT NULL,
    ic_origem_documento character varying(1) DEFAULT 'S'::bpchar NOT NULL,
    ic_temporario integer DEFAULT 0 NOT NULL,
    co_extracao character varying(100),
    ts_envio_extracao timestamp without time zone,
    ts_retorno_extracao timestamp without time zone,
    co_autenticidade character varying(100),
    ts_envio_autenticidade timestamp without time zone,
    ts_retorno_autenticidade timestamp without time zone,
    ix_antifraude numeric(5,2),
    co_ged character varying(255),
    no_formato character varying(10)
);

COMMENT ON TABLE mtrtb003_documento IS 'Tabela responsável pelo armazenamento da referência dos documentos de um determinado cliente.
Esses documentos podem estar associados a um ou mais dossiês de produtos possibilitando o reaproveitamento dos mesmos em diversos produtos.
Nesta tabela serão efetivamente armazenados os dados dos documentos que pode representar o agrupamento de uma ou mais imagens na sua formação.
Também deverão ser armazenadas as propriedades do mesmo e as marcas conforme seu ciclo de vida.';
COMMENT ON COLUMN mtrtb003_documento.nu_documento IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb003_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
COMMENT ON COLUMN mtrtb003_documento.nu_tipo_documento IS 'Atributo utilizado para armazenar a vinculação do tipo de documento referenciado pelo documento registrado';
COMMENT ON COLUMN mtrtb003_documento.nu_canal_captura IS 'Atributo utilizado para identificar o canal de captura utilizado para recepcionar o documento.';
COMMENT ON COLUMN mtrtb003_documento.ts_captura IS 'Atributo que armazena a data e hora que foi realizada a captura do documento';
COMMENT ON COLUMN mtrtb003_documento.co_matricula IS 'Atributo que armazena a matricula do usuario que realizou a captura do documento';
COMMENT ON COLUMN mtrtb003_documento.ts_validade IS 'Atributo que armazena a data de validade do documento conforme definições corporativas calculado pelo prazo definido no tipo documento.

Sempre que definida uma data futura para o documento a hora deve ser definida com o valor de 23:59:59';
COMMENT ON COLUMN mtrtb003_documento.ic_dossie_digital IS 'Atributo utilizado para determinar que o documento foi obtido do repositorio do dossiê digital e encontra-se disponivel no OBJECT STORE dessa solução.';
COMMENT ON COLUMN mtrtb003_documento.ic_origem_documento IS 'Atributo utilizado para armazenar a origem do documento digitalizado submetido baseado no seguinte dominio:

O = Documento Original
S = Cópia Simples
C = Cópia Autenticada em Cartório
A = Cópia Autenticada Administrativamente

Sempre que um documento for submetido via upload ou via app mobile este atributo deverá ser preenchido como copia simples (S)';
COMMENT ON COLUMN mtrtb003_documento.ic_temporario IS 'Atributo utilizado para indicar se o documento ainda esta fase de analise sob a otica do dossiê digital.

Quando um documento for submetido pelo fluxo do dossiê digital, antes de utiliza-lo uma serie de verificações deve ser realizada dentro de um determinado espaço de tempo, senão esse documento será expurgado da base.

Esse atributo pode assumir o dominio abaixo:

0 - Definitivo
1 - Temporario - Extração de dados (OCR)
2 - Temporario - Antifraude';
COMMENT ON COLUMN mtrtb003_documento.co_extracao IS 'Atributo utilizado para armazenar o codigo de identificação junto ao serviço de extração de dados';
COMMENT ON COLUMN mtrtb003_documento.ts_envio_extracao IS 'Atributo utilizado para armazenar a data e hora de envio do documento para o serviço de extração de dados.';
COMMENT ON COLUMN mtrtb003_documento.ts_retorno_extracao IS 'Atributo utilizado para armazenar a data e hora de retorno dos atributos extraidos do documento junto ao serviço de extração de dados.';
COMMENT ON COLUMN mtrtb003_documento.co_autenticidade IS 'Atributo utilizado para armazenar o codigo de identificação do documento junto ao serviço de avaliação de autenticidade documental';
COMMENT ON COLUMN mtrtb003_documento.ts_envio_autenticidade IS 'Atributo utilizado para armazenar a data e hora de envio do documento para o serviço de avaliação de autenticidade documental.';
COMMENT ON COLUMN mtrtb003_documento.ts_retorno_autenticidade IS 'Atributo utilizado para armazenar a data e hora de retorno do score junto ao serviço de avaliação de autenticidade documental.';
COMMENT ON COLUMN mtrtb003_documento.ix_antifraude IS 'Atributo utilizado para armazenar o indice retornado pelo serviço de antifraude para o documento.
O indice devolve o percentual para o indicio de fraude no documento.
Para documentos validos este valor deve tender a zero.';
COMMENT ON COLUMN mtrtb003_documento.co_ged IS 'Atributo utilizado para identificar a localização de um documento armazenado em um sistema de Gestão Eletronica de Documentos (GED).
A utilização deste atrinbuto dependerá da estrategia de armazenamento, pois se a identificação for feita através do registro do documento, será aqui que deverá estar armazenado o identificador único deste conteudo perante o GED, porém se a identificação for individual por conteudo (pagina, imagem, etc) este atributo ficará inutilizado.';
COMMENT ON COLUMN mtrtb003_documento.no_formato IS 'Atributo utilizado para armazenar o formato do documento, podendo ser nulo para atender situações onde armazena-se um documento sem conteudos. Ex:
- PDF
- JPG
- TIFF';

--
-- TOC entry 1828 (class 1259 OID 2025558)
-- Name: mtrsq003_documento; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq003_documento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq003_documento OWNED BY mtrtb003_documento.nu_documento;

--
-- TOC entry 1829 (class 1259 OID 2025560)
-- Name: mtrsq004_dossie_cliente_produto; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq004_dossie_cliente_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 1830 (class 1259 OID 2025562)
-- Name: mtrtb006_canal; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb006_canal (
    nu_canal integer NOT NULL,
    nu_versao integer NOT NULL,
    sg_canal character varying(10) NOT NULL,
    de_canal character varying(255) NOT NULL,
    co_integracao bigint NOT NULL,
    ic_canal_caixa character varying(3) NOT NULL
);

COMMENT ON TABLE mtrtb006_canal IS 'Tabela responsável pelo armazenamento dos possíveis canais de captura de um documento para identificação de sua origem.';
COMMENT ON COLUMN mtrtb006_canal.nu_canal IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb006_canal.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb006_canal.sg_canal IS 'Atributo utilizado para identificar a sigla do canal de captura.
Pode assumir valor como por exemplo:
- SIMTR (Sistema Interno SIMTR)
- APMOB (Aplicativo Mobile)
- STECX (Site Corporativo da CAIXA)';
COMMENT ON COLUMN mtrtb006_canal.de_canal IS 'Atributo utilizado para descrever o canal de captura.
Exemplo:
Sistema Interno SIMTR
Sistemas Corporativos
Aplicativo Mobile
Site Corporativo';
COMMENT ON COLUMN mtrtb006_canal.co_integracao IS 'Atributo que representa o código único para o sistema de origem.';

--
-- TOC entry 1831 (class 1259 OID 2025565)
-- Name: mtrsq006_canal; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq006_canal
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq006_canal OWNED BY mtrtb006_canal.nu_canal;

--
-- TOC entry 1832 (class 1259 OID 2025567)
-- Name: mtrtb007_atributo_documento; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb007_atributo_documento (
    nu_atributo_documento bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_documento bigint NOT NULL,
    de_atributo character varying(100) NOT NULL,
    de_conteudo text NOT NULL,
    ix_assertividade numeric(5,2),
    ic_acerto_manual boolean NOT NULL
);

COMMENT ON TABLE mtrtb007_atributo_documento IS 'Tabela responsável por armazenar os atributos capturados do documento utilizando a estrutura de chave x valor onde o nome do atributo determina o campo do documento que a informação foi extraída e o conteúdo trata-se do dado propriamente extraído.';
COMMENT ON COLUMN mtrtb007_atributo_documento.nu_atributo_documento IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb007_atributo_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb007_atributo_documento.nu_documento IS 'Atributo utilizado para vincular o atributo definido ao documento cuja informação foi extraída.';
COMMENT ON COLUMN mtrtb007_atributo_documento.de_atributo IS 'Atributo utilizado para armazenar a descrição da chave que identifica o atributo.
Como exemplo, um registro que armazena a data de nascimento de um RG: 
data_nascimento = 01/09/1980
Neste caso o conteúdo deste campo seria "data nascimento" e o atributo conteúdo armazenaria "01/09/1980" tal qual extraído do documento.';
COMMENT ON COLUMN mtrtb007_atributo_documento.de_conteudo IS 'Atributo utilizado para armazenar a dado extraído de um campo do documento.
Como exemplo, um registro que armazena a data de nascimento de um RG: 
data nascimento = 01/09/1980
Neste caso o conteúdo deste campo seria "01/09/1980" tal qual extraído do documento e o atributo de descrição armazenaria "data nascimento".';
COMMENT ON COLUMN mtrtb007_atributo_documento.ix_assertividade IS 'Atributo utilizado para armazenar o indice de assertividade retornado pelo serviço de extração de dados. ';
COMMENT ON COLUMN mtrtb007_atributo_documento.ic_acerto_manual IS 'Atributo que representa se houver ajuste manual no atributo extraído';

--
-- TOC entry 1833 (class 1259 OID 2025573)
-- Name: mtrsq007_atributo_documento; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq007_atributo_documento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq007_atributo_documento OWNED BY mtrtb007_atributo_documento.nu_atributo_documento;

--
-- TOC entry 1834 (class 1259 OID 2025575)
-- Name: mtrtb009_tipo_documento; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb009_tipo_documento (
    nu_tipo_documento integer NOT NULL,
    nu_versao integer NOT NULL,
    no_tipo_documento character varying(100) NOT NULL,
    ic_tipo_pessoa character(1),
    pz_validade_dias integer,
    ic_validade_auto_contida boolean NOT NULL,
    co_tipologia character varying(100),
    no_classe_ged character varying(100),
    ic_reuso boolean NOT NULL,
    ic_processo_administrativo boolean NOT NULL,
    ic_dossie_digital boolean NOT NULL,
    ic_apoio_negocio boolean NOT NULL,
    no_arquivo_minuta character varying(100),
    de_tags text,
    ic_validacao_cadastral boolean NOT NULL,
    ic_validacao_documental boolean NOT NULL
);

COMMENT ON TABLE mtrtb009_tipo_documento IS 'Tabela responsável pelo armazenamento dos possíveis tipos de documento que podem ser submetidos ao vínculo com os dossiês.';
COMMENT ON COLUMN mtrtb009_tipo_documento.nu_tipo_documento IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb009_tipo_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb009_tipo_documento.no_tipo_documento IS 'Atributo que identifica o tipo de documento vinculado. Como exemplo podemos ter:
- RG
- CNH
- Certidão Negativa de Debito
- Passaporte
- Etc	';
COMMENT ON COLUMN mtrtb009_tipo_documento.ic_tipo_pessoa IS 'Atributo que determina qual tipo de pessoa pode ter o documento atribuído.
Pode assumir os seguintes valores:
F - Física
J - Jurídica
S - Serviço
A - Física ou Jurídica
G - Garantia
P - Produto
T - Todos';
COMMENT ON COLUMN mtrtb009_tipo_documento.pz_validade_dias IS 'Atributo que indica a quantidade de dias para atribuição da validade do documento a partir da sua emissão.
Caso o valor deste atributo não esteja definido, significa que o documento possui um prazo de validade indeterminado.';
COMMENT ON COLUMN mtrtb009_tipo_documento.ic_validade_auto_contida IS 'Atributo determina se a validade do documento está definida no próprio documento ou não como por exemplo no caso de certidões que possuem a validade determinada em seu corpo.
Caso o valor deste atributo seja falso, o prazo de validade deve ser calculado conforme definido no atributo de prazo de validade.';
COMMENT ON COLUMN mtrtb009_tipo_documento.co_tipologia IS 'Atributo utilizado para armazenar o código da tipologia documental corporativa.';
COMMENT ON COLUMN mtrtb009_tipo_documento.ic_reuso IS 'Atributo utilizado para identificar se deve ser aplicado reuso ou não na carga do documento.

Existem situações, em que é necessario carregar um novo documento do mesmo tipo de forma atualizada, pois espera-se que o documento contenha novas informações devido ao andamento da contratação.

Nestes casos, não deverá ser realizada a inclusão de uma instancia do documento de forma automatica para esta determinada tipologia';
COMMENT ON COLUMN mtrtb009_tipo_documento.ic_processo_administrativo IS 'Atributo utilizado para identificar se o tipo de documento faz utilização perante o Processo Administrativo Eletronico (PAE) considerando que o mesmo possui restrições de apresentação quanto as opções de seleção para o usuário.';
COMMENT ON COLUMN mtrtb009_tipo_documento.ic_dossie_digital IS 'Atributo utilizado para identificar se o tipo de documento faz utilização perante o Dossiê Digital.';
COMMENT ON COLUMN mtrtb009_tipo_documento.ic_apoio_negocio IS 'Atributo utilizado para identificar se o tipo de documento faz utilização perante o Apoio ao Negocio.';
COMMENT ON COLUMN mtrtb009_tipo_documento.no_arquivo_minuta IS 'Atributo utilizado para indicar o nome do arquivo utilizado pelo gerador de relatorio na emissão da minuta do documento';
COMMENT ON COLUMN mtrtb009_tipo_documento.de_tags IS 'Atributo utilizado para realizar a vinculação de TAGS pre definidaas ao tipo documental. Essas Tasgs devem ser sepradas por ";" (ponto e virgula) serão utilizadas para localizar documentos que estejam associados ao tipo que tenha relação com TAG pesquisada.

Ex: 
Tipo de Documento            |  TAGS
-------------------------------------------------------------------------------------
Comunicado Interno           | CI;Comunicados
Certidão Negativa Receita | Certidões;Certidão;Certidao
';
COMMENT ON COLUMN mtrtb009_tipo_documento.ic_validacao_cadastral IS 'Indica se o documento pode ser enviado a avaliação de validade cadastral.

Atualmente essa avaliação é realizada pelo SIFRC';
COMMENT ON COLUMN mtrtb009_tipo_documento.ic_validacao_documental IS 'Indica se o documento pode ser enviado a avaliação de validade documental.

Atualmente essa avaliação é realizada pelo SICOD';

--
-- TOC entry 1835 (class 1259 OID 2025581)
-- Name: mtrsq009_tipo_documento; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq009_tipo_documento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq009_tipo_documento OWNED BY mtrtb009_tipo_documento.nu_tipo_documento;

--
-- TOC entry 1836 (class 1259 OID 2025583)
-- Name: mtrtb010_funcao_documental; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb010_funcao_documental (
    nu_funcao_documental integer NOT NULL,
    nu_versao integer NOT NULL,
    no_funcao character varying(100) NOT NULL
);

COMMENT ON TABLE mtrtb010_funcao_documental IS 'Tabela responsável por armazenar as possíveis funções documentais.
Essa informação permite agrupar documentos que possuem a mesma finalidade e um documento pode possui mais de uma função.
Exemplos dessa atribuição funcional, são:
- Identificação;
- Renda;
- Comprovação de Residência
- etc';
COMMENT ON COLUMN mtrtb010_funcao_documental.nu_funcao_documental IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb010_funcao_documental.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb010_funcao_documental.no_funcao IS 'Atributo definido para armazenar o nome da função documental, como por exemplo:
- Identificação
- Comprovação de Renda
- Comprovação de Residência
- Regularidade Fiscal
- etc';

--
-- TOC entry 1837 (class 1259 OID 2025586)
-- Name: mtrsq010_funcao_documental; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq010_funcao_documental
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq010_funcao_documental OWNED BY mtrtb010_funcao_documental.nu_funcao_documental;

--
-- TOC entry 1838 (class 1259 OID 2025588)
-- Name: mtrtb012_tipo_situacao_dossie; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb012_tipo_situacao_dossie (
    nu_tipo_situacao_dossie integer NOT NULL,
    nu_versao integer NOT NULL,
    no_tipo_situacao character varying(100) NOT NULL,
    ic_resumo boolean NOT NULL,
    ic_fila_tratamento boolean NOT NULL,
    ic_produtividade boolean NOT NULL,
    ic_tipo_inicial boolean DEFAULT false NOT NULL,
    ic_tipo_final boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE mtrtb012_tipo_situacao_dossie IS 'Tabela responsável pelo armazenamento das possíveis situações vinculadas a um dossiê de produto.

Como exemplo podemos ter as possíveis situações:
- Criado
- Atualizado
- Disponível
- Em Análise
- etc';
COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.nu_tipo_situacao_dossie IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.
COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.no_tipo_situacao IS Atributo que armazena o nome do tipo de situação do dossiê.';
COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.no_tipo_situacao IS 'Atributo que armazena o nome do tipo de situação do dossiê';
COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.ic_resumo IS 'Atributo utilizado para indicar se o tipo de situação gera agrupamento para exibição de resumo de dossiês.';
COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.ic_fila_tratamento IS 'Atrinuto utilizado para indicar se o tipo de situação inclui o dossiês na fila para tratamento';
COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.ic_produtividade IS 'Atributo utilizado para indicar se o tipo de situação considera o dossiê na contagem da produtividade diária.';
COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.ic_tipo_inicial IS 'Atributo utilizado para identificar o tipo de situação que deve ser aplicado como primeiro tipo de situação de um dossiê de produto.';

--
-- TOC entry 1839 (class 1259 OID 2025593)
-- Name: mtrsq012_tipo_situacao_dossie; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq012_tipo_situacao_dossie
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq012_tipo_situacao_dossie OWNED BY mtrtb012_tipo_situacao_dossie.nu_tipo_situacao_dossie;

--
-- TOC entry 1840 (class 1259 OID 2025595)
-- Name: mtrtb013_situacao_dossie; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb013_situacao_dossie (
    nu_situacao_dossie bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_dossie_produto bigint NOT NULL,
    nu_tipo_situacao_dossie integer NOT NULL,
    ts_inclusao timestamp without time zone NOT NULL,
    co_matricula character varying(7) NOT NULL,
    nu_unidade integer NOT NULL,
    de_observacao text,
    ts_saida timestamp without time zone
);

COMMENT ON TABLE mtrtb013_situacao_dossie IS 'Tabela responsável por armazenar o histórico de situações relativas ao dossiê do produto. Cada vez que houver uma mudança na situação apresentada pelo processo, um novo registro deve ser inserido gerando assim um histórico das situações vivenciadas durante o seu ciclo de vida.';
COMMENT ON COLUMN mtrtb013_situacao_dossie.nu_situacao_dossie IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb013_situacao_dossie.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb013_situacao_dossie.nu_dossie_produto IS 'Atributo utilizado pata armazenar a referência do dossiê do produto vinculado a situação.';
COMMENT ON COLUMN mtrtb013_situacao_dossie.nu_tipo_situacao_dossie IS 'Atributo utilizado para armazenar o tipo situação do dossiê que será atribuido manualmente pelo operador ou pela automacao do workflow quando estruturado.';
COMMENT ON COLUMN mtrtb013_situacao_dossie.ts_inclusao IS 'Atributo utilizado para armazenar a data/hora de atribuição da situação ao dossiê.';
COMMENT ON COLUMN mtrtb013_situacao_dossie.co_matricula IS 'Atributo utilizado para armazenar a matrícula do empregado ou serviço que atribuiu a situação ao dossiê.';
COMMENT ON COLUMN mtrtb013_situacao_dossie.nu_unidade IS 'Atributo que indica a unidade do empregado que registrou a situação do dossiê.';
COMMENT ON COLUMN mtrtb013_situacao_dossie.de_observacao IS 'Informação do usuário indicando uma observação com relação ao motivo de atribuição da situação definida.';

--
-- TOC entry 1841 (class 1259 OID 2025601)
-- Name: mtrsq013_situacao_dossie; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq013_situacao_dossie
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq013_situacao_dossie OWNED BY mtrtb013_situacao_dossie.nu_situacao_dossie;

--
-- TOC entry 1842 (class 1259 OID 2025603)
-- Name: mtrtb014_instancia_documento; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb014_instancia_documento (
    nu_instancia_documento bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_documento bigint NOT NULL,
    nu_dossie_produto bigint NOT NULL,
    nu_elemento_conteudo bigint,
    nu_garantia_informada bigint,
    nu_dossie_cliente_produto bigint
);

COMMENT ON TABLE mtrtb014_instancia_documento IS 'Tabela responsável pelo armazenamento de instâncias de documentos que estarão vinculados aos dossiês dos produtos.';
COMMENT ON COLUMN mtrtb014_instancia_documento.nu_instancia_documento IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb014_instancia_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb014_instancia_documento.nu_documento IS 'Atributo que vincula o registro da instância de documento ao documento propriamente dito permitindo assim o reaproveitamento de documento previamente existentes.';
COMMENT ON COLUMN mtrtb014_instancia_documento.nu_dossie_produto IS 'Atributo que armazena a referência do dossiê de produto vinculado a instância do documento.';
COMMENT ON COLUMN mtrtb014_instancia_documento.nu_elemento_conteudo IS 'Atributo que representa o elemento de conteúdo do processo ao qual foi a instância foi vinculada. Utilizado apenas para os casos de documentos submetidos pelo mapa de processo. Para os casos de documento do cliente associados/utilizados no dossiê do produto este atributo não estará definido.';
COMMENT ON COLUMN mtrtb014_instancia_documento.nu_garantia_informada IS 'Atributo utilizado pata armazenar a referência da garantia informada vinculada a instância do documento.';

--
-- TOC entry 1843 (class 1259 OID 2025606)
-- Name: mtrsq014_instancia_documento; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq014_instancia_documento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq014_instancia_documento OWNED BY mtrtb014_instancia_documento.nu_instancia_documento;

--
-- TOC entry 1844 (class 1259 OID 2025608)
-- Name: mtrtb015_situacao_documento; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb015_situacao_documento (
    nu_situacao_documento integer NOT NULL,
    nu_versao integer NOT NULL,
    no_situacao character varying(100) NOT NULL,
    ic_situacao_inicial boolean DEFAULT false NOT NULL,
    ic_situacao_final boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE mtrtb015_situacao_documento IS 'Tabela responsável pelo armazenamento das possíveis situações vinculadas a um documento.
Essas situações também deverão agrupar motivos para atribuição desta situação.
Como exemplo podemos ter as possíveis situações e entre parênteses os motivos de agrupamento:
- Aprovado
- Rejeitado (Ilegível / Rasurado / Segurança)
- Pendente (Recaptura)';
COMMENT ON COLUMN mtrtb015_situacao_documento.nu_situacao_documento IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb015_situacao_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb015_situacao_documento.no_situacao IS 'Atributo que armazena o nome da situação do documento.';
COMMENT ON COLUMN mtrtb015_situacao_documento.ic_situacao_inicial IS 'Atributo utilizado para identificar o tipo de situação que deve ser aplicado como primeiro tipo de situação de uma instância de documento.';
COMMENT ON COLUMN mtrtb015_situacao_documento.ic_situacao_final IS 'Atributo utilizado para identificar o tipo de situação que é final. Após ser aplicado uma situação deste tipo, o sistema não deverá permitir que sejam criados novos registros de situação para a instância dos documentos.';

--
-- TOC entry 1845 (class 1259 OID 2025613)
-- Name: mtrsq015_situacao_documento; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq015_situacao_documento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq015_situacao_documento OWNED BY mtrtb015_situacao_documento.nu_situacao_documento;

--
-- TOC entry 1846 (class 1259 OID 2025615)
-- Name: mtrtb016_motivo_stco_documento; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb016_motivo_stco_documento (
    nu_motivo_stco_documento integer NOT NULL,
    nu_versao integer NOT NULL,
    nu_situacao_documento integer NOT NULL,
    no_motivo_stco_documento character varying(100) NOT NULL
);

COMMENT ON TABLE mtrtb016_motivo_stco_documento IS 'Tabela de motivos específicos para indicar a causa de uma determinada situação vinculada a um dado documento.
Exemplo:  
- Ilegível -> Rejeitado
- Rasurado -> rejeitado
- Segurança -> Rejeitado
- Recaptura -> pendente';
COMMENT ON COLUMN mtrtb016_motivo_stco_documento.nu_motivo_stco_documento IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb016_motivo_stco_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb016_motivo_stco_documento.nu_situacao_documento IS 'Atributo utilizado para vincular o motivo com uma situação especifica.';
COMMENT ON COLUMN mtrtb016_motivo_stco_documento.no_motivo_stco_documento IS 'Atributo que armazena o nome do motivo da situação do documento.';

--
-- TOC entry 1847 (class 1259 OID 2025618)
-- Name: mtrsq016_motivo_stco_documento; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq016_motivo_stco_documento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq016_motivo_stco_documento OWNED BY mtrtb016_motivo_stco_documento.nu_motivo_stco_documento;

--
-- TOC entry 1848 (class 1259 OID 2025620)
-- Name: mtrsq017_situacao_instnca_dcmnto; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq017_situacao_instnca_dcmnto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 1849 (class 1259 OID 2025622)
-- Name: mtrsq019_campo_formulario; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq019_campo_formulario
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 1850 (class 1259 OID 2025624)
-- Name: mtrtb020_processo; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb020_processo (
    nu_processo integer NOT NULL,
    nu_versao integer NOT NULL,
    no_processo character varying(255) NOT NULL,
    ic_ativo boolean NOT NULL,
    de_avatar character varying(255),
    ic_dossie boolean DEFAULT false NOT NULL,
    ic_controla_validade_documento boolean NOT NULL,
    ic_tipo_pessoa character varying(1) NOT NULL,
    no_processo_bpm character varying(255),
    no_container_bpm character varying(255)
);

COMMENT ON TABLE mtrtb020_processo IS 'Tabela responsável pelo armazenamento dos processos que podem ser atrelados aos dossiês de forma a identificar qual o processo bancário relacionado.
Processos que possuam vinculação com dossiês de produto não devem ser excluídos fisicamente, e sim atribuídos como inativo.
Exemplos de processos na linguagem negocial são:
- Concessão de Crédito Habitacional
- Conta Corrente
- Financiamento de Veículos
- Pagamento de Loterias
- Etc';
COMMENT ON COLUMN mtrtb020_processo.nu_processo IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb020_processo.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb020_processo.no_processo IS 'Atributo utilizado para armazenar o nome de identificação negocial do processo.';
COMMENT ON COLUMN mtrtb020_processo.ic_ativo IS 'Atributo que indica que se o processo esta ativo ou não para utilização pelo sistema.';
COMMENT ON COLUMN mtrtb020_processo.de_avatar IS 'Atributo utilizado para armazenar o nome do avatar que será disponibilizado no pacote da ineterface grafica para montagem e apresentação das filas de captura o uinformações do processo.';
COMMENT ON COLUMN mtrtb020_processo.no_processo_bpm IS 'Atributo que armazena o valor de identificação do processo orignador junto a solução de BPM.
Ex: bpm-simtr.ProcessoConformidade';
COMMENT ON COLUMN mtrtb020_processo.no_container_bpm IS 'Atributo que armazena o valor de identificação do container utilizado no agrupamento dos processos junto a solução de BPM que possui o processo originador vinculado.
Ex: bpm-simtr_1.0.0';

--
-- TOC entry 1851 (class 1259 OID 2025631)
-- Name: mtrsq020_processo; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq020_processo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq020_processo OWNED BY mtrtb020_processo.nu_processo;

--
-- TOC entry 1852 (class 1259 OID 2025633)
-- Name: mtrtb021_unidade_autorizada; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb021_unidade_autorizada (
    nu_unidade_autorizada integer NOT NULL,
    nu_versao integer NOT NULL,
    nu_processo integer,
    nu_unidade integer NOT NULL,
    ic_tipo_tratamento character varying(10) NOT NULL,
    nu_apenso_adm bigint,
    nu_contrato_adm bigint,
    nu_processo_adm bigint
);

COMMENT ON TABLE mtrtb021_unidade_autorizada IS 'Tabela responsável pelo armazenamento das unidades autorizadas a utilização do processo.';
COMMENT ON COLUMN mtrtb021_unidade_autorizada.nu_unidade_autorizada IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb021_unidade_autorizada.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb021_unidade_autorizada.nu_processo IS 'Atributo que representa o processo na definição de autorização a realização de algum tipo de tratamento por uma determinada unidade.';
COMMENT ON COLUMN mtrtb021_unidade_autorizada.nu_unidade IS 'Atributo que representa o CGC da unidade autorizada.';
COMMENT ON COLUMN mtrtb021_unidade_autorizada.ic_tipo_tratamento IS 'Atributo que indica as ações possiveis a serem realizadas no sistema para a determinada unidade de acordo com o registro de vinculação da autorização. A representação dos valores das ações são definidas em numeração binaria e determinam quais as permissões da unidade sobre os itens aos quais estão vinculados. Os valores possiveis são:

1 - CONSULTA_DOSSIE
2 - TRATAR_DOSSIE
4 - CRIAR_DOSSIE
8 - PRIORIZAR_DOSSIE
16 - ALIMENTAR_DOSSIE

Considerando o fato, como exemplo uma unidade que possa consultar, tratar e criar dossiês, mas não pode priorizar, nem alimentar o dossiê e deve estar representado o valor 7 no seguinte formato:

0000000111';

--
-- TOC entry 1853 (class 1259 OID 2025636)
-- Name: mtrsq021_unidade_autorizada; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrsq021_unidade_autorizada
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq021_unidade_autorizada OWNED BY mtrtb021_unidade_autorizada.nu_unidade_autorizada;

--
-- TOC entry 1854 (class 1259 OID 2025638)
-- Name: mtrtb022_produto; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb022_produto (
    nu_produto integer NOT NULL,
    nu_versao integer NOT NULL,
    nu_operacao integer NOT NULL,
    nu_modalidade integer NOT NULL,
    no_produto character varying(255) NOT NULL,
    ic_contratacao_conjunta boolean NOT NULL,
    ic_pesquisa_cadin boolean DEFAULT false NOT NULL,
    ic_pesquisa_scpc boolean DEFAULT false NOT NULL,
    ic_pesquisa_serasa boolean DEFAULT false NOT NULL,
    ic_pesquisa_ccf boolean DEFAULT false NOT NULL,
    ic_pesquisa_sicow boolean DEFAULT false NOT NULL,
    ic_pesquisa_receita boolean DEFAULT false NOT NULL,
    ic_tipo_pessoa character varying(1) NOT NULL,
    ic_dossie_digital boolean NOT NULL,
    ic_pesquisa_sinad boolean NOT NULL
);

COMMENT ON TABLE mtrtb022_produto IS 'Tabela responsável pelo armazenamento dos produtos da CAIXA que serão vinculados aos processos definidos.';
COMMENT ON COLUMN mtrtb022_produto.nu_produto IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb022_produto.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb022_produto.nu_operacao IS 'Atributo que armazena o número de operação corporativa do produto.';
COMMENT ON COLUMN mtrtb022_produto.nu_modalidade IS 'Atributo que armazena o número da modalidade corporativa do produto.';
COMMENT ON COLUMN mtrtb022_produto.no_produto IS 'Atributo que armazena o nome corporativo do produto.';
COMMENT ON COLUMN mtrtb022_produto.ic_contratacao_conjunta IS 'Atributo utilizado para identificar se o produto permite realizar contatação conjunta com outros produtos, caso o atributo esteja setado como false, será criado um dossiê individual para o cada produto selecionado nessa consição.';
COMMENT ON COLUMN mtrtb022_produto.ic_pesquisa_cadin IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao CADIN retorne resultado alguma restrição.';
COMMENT ON COLUMN mtrtb022_produto.ic_pesquisa_scpc IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao SCPC retorne resultado alguma restrição.';
COMMENT ON COLUMN mtrtb022_produto.ic_pesquisa_serasa IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao SPC/SERASA retorne resultado alguma restrição.';
COMMENT ON COLUMN mtrtb022_produto.ic_pesquisa_ccf IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao SICCF retorne resultado alguma restrição.';
COMMENT ON COLUMN mtrtb022_produto.ic_pesquisa_sicow IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao SICOW retorne resultado alguma restrição.';
COMMENT ON COLUMN mtrtb022_produto.ic_pesquisa_receita IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto a Receita retorne resultado alguma restrição.';
COMMENT ON COLUMN mtrtb022_produto.ic_tipo_pessoa IS 'Atributo que determina qual tipo de pessoa pode contratar o produto.
Pode assumir os seguintes valores:
F - Física
J - Jurídica
A - Física ou Jurídica
';
COMMENT ON COLUMN mtrtb022_produto.ic_dossie_digital IS 'Atributo utilizado para indicar se o produto já esta mapeado para executar ações vinculadas ao modelo do dossiê digital';
COMMENT ON COLUMN mtrtb022_produto.ic_pesquisa_sinad IS 'Atributo utilizado para identificar se a pesquisa junto a Receita deve ser realizada para o produto especificado.';

--
-- TOC entry 1855 (class 1259 OID 2025647)
-- Name: mtrsq022_produto; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq022_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq022_produto OWNED BY mtrtb022_produto.nu_produto;

--
-- TOC entry 1856 (class 1259 OID 2025649)
-- Name: mtrtb024_produto_dossie; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb024_produto_dossie (
    nu_produto_dossie bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_dossie_produto bigint NOT NULL,
    nu_produto integer NOT NULL,
    vr_contrato numeric(15,2),
    pc_juros_operacao numeric(5,2),
    ic_periodo_juros character varying(1),
    pz_operacao integer,
    pz_carencia integer,
    ic_liquidacao boolean,
    co_contrato_renovado character varying(100),
    CONSTRAINT ckc_ic_periodo_juros_mtrtb024 CHECK (((ic_periodo_juros)::bpchar = ANY (ARRAY['D'::bpchar, 'M'::bpchar, 'A'::bpchar])))
);

COMMENT ON TABLE mtrtb024_produto_dossie IS 'Tabela de relacionamento para vinculação dos produtos selecionados para tratamento no dossiê. 
Existe a possibilidade que mais de um produto seja vinculado a um dossiê para tratamento único como é o caso do contrato de relacionamento que envolve Cartão de Credito / CROT / CDC / Conta Corrente.';
COMMENT ON COLUMN mtrtb024_produto_dossie.nu_produto_dossie IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb024_produto_dossie.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb024_produto_dossie.nu_dossie_produto IS 'Atributo que representa o dossiê de vinculação da coleção de produtos em análise.';
COMMENT ON COLUMN mtrtb024_produto_dossie.nu_produto IS 'Atributo que representa o produto vinculado na relação com o dossiê.';
COMMENT ON COLUMN mtrtb024_produto_dossie.vr_contrato IS 'Atributo que representa o valor do produto.';
COMMENT ON COLUMN mtrtb024_produto_dossie.pc_juros_operacao IS 'Percentual de juros utilizado na contratação do produto.';
COMMENT ON COLUMN mtrtb024_produto_dossie.ic_periodo_juros IS 'Armazena o periodo de juros ao qual se refere a taxa. 
D - Diário
M - Mensal
A - Anual';
COMMENT ON COLUMN mtrtb024_produto_dossie.pz_operacao IS 'Prazo utilizado na contratação do produto.';
COMMENT ON COLUMN mtrtb024_produto_dossie.pz_carencia IS 'Prazo utilizado como carência na contratação do produto.';
COMMENT ON COLUMN mtrtb024_produto_dossie.ic_liquidacao IS 'Atributo utilizado para indicar se a contratação do produto prevê a liquidação/renovação de um contrato.';
COMMENT ON COLUMN mtrtb024_produto_dossie.co_contrato_renovado IS 'Atributo utilizado para armazenar o contrato liquidado/renovado.';

--
-- TOC entry 1857 (class 1259 OID 2025653)
-- Name: mtrsq024_produto_dossie; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq024_produto_dossie
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq024_produto_dossie OWNED BY mtrtb024_produto_dossie.nu_produto_dossie;

--
-- TOC entry 1858 (class 1259 OID 2025655)
-- Name: mtrtb025_processo_documento; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb025_processo_documento (
    nu_processo_documento bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_processo integer NOT NULL,
    nu_funcao_documental integer,
    nu_tipo_documento integer,
    ic_tipo_relacionamento character varying(50) NOT NULL,
    ic_obrigatorio boolean NOT NULL,
    ic_validar boolean NOT NULL
);

COMMENT ON TABLE mtrtb025_processo_documento IS 'Tabela utilizada para identificar os documentos (por tipo ou função) que são necessários para os titulares do dossiê de um processo específico. Fazendo um paralelo, seria como o os elementos do conteúdo, porém voltados aos documentos do cliente, pois um dossiê pode ter a quantidade de clientes definida dinamicamente e por isso não cabe na estrutura do elemento do conteúdo. Esta estrutura ficará a cargo dos elementos específicos do produto.
Quando um dossiê é criado, todos os CNPFs/CNPJs envolvidos na operação deverão apresentar os tipos de documentos ou algum documento da função documental definidas nesta relação com o processo específico definido para o dossiê de produto.';
COMMENT ON COLUMN mtrtb025_processo_documento.nu_processo_documento IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb025_processo_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb025_processo_documento.nu_processo IS 'Atributo utilizado para identificar o processo de vinculação para agrupar os documentos necessarios.';
COMMENT ON COLUMN mtrtb025_processo_documento.nu_funcao_documental IS 'Atrinuto utilizado para referenciar uma função documental necessaria ao processo. Quando definido, qualquer documento valido que o cliente tenha que seja desta função documental, deve ser considerado que o documento existente já atende a necessidade. Caso este atributo esteja nulo, o atributo que representa o tipo de documento deverá estar prenchido.';
COMMENT ON COLUMN mtrtb025_processo_documento.nu_tipo_documento IS 'Atrinuto utilizado para referenciar um tipo de documento especifico, necessari ao processo. Quando definido, apenas a presença do documento especifica em estado valido, presente e associado ao dossiê do cliente deve ser considerado existente e já atende a necessidade. Caso este atributo esteja nulo, o atributo que representa a função documental deverá estar prenchido.';
COMMENT ON COLUMN mtrtb025_processo_documento.ic_tipo_relacionamento IS 'Atributo que indica o tipo de relacionamento do cliente com o produto, podendo assumir os valores:
- TITULAR
- AVALISTA
- CONJUGE
- SOCIO
etc.';

--
-- TOC entry 1859 (class 1259 OID 2025658)
-- Name: mtrsq025_processo_documento; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq025_processo_documento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq025_processo_documento OWNED BY mtrtb025_processo_documento.nu_processo_documento;

--
-- TOC entry 1860 (class 1259 OID 2025660)
-- Name: mtrtb027_campo_entrada; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb027_campo_entrada (
    nu_campo_entrada bigint NOT NULL,
    nu_versao integer NOT NULL,
    ic_tipo character varying(20) NOT NULL,
    ic_chave boolean NOT NULL,
    no_label character varying(50) NOT NULL,
    de_mascara character varying(100),
    de_placeholder character varying(100),
    nu_tamanho_minimo integer,
    nu_tamanho_maximo integer
);

COMMENT ON TABLE mtrtb027_campo_entrada IS 'Tabela responsável por armazenar a estrutura de entradas de dados que serão alimentados na inclusão de um novo dossiê para o processo vinculado.
Esta estrutura permitirá realizar a construção dinâmica do formulário.
Registros desta tabela só devem ser excluídos fisicamente caso não exista nenhuma resposta de formulário atrelada a este registro. Caso essa situação ocorra o registro deve ser excluído logicamente definindo seu atributo ativo como false.';
COMMENT ON COLUMN mtrtb027_campo_entrada.nu_campo_entrada IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb027_campo_entrada.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb027_campo_entrada.ic_tipo IS 'Atributo utilizado para armazenar o tipo de campo de formulário que será gerado. Exemplos válidos para este atributo são:
- TEXT
- SELECT
- RADIO';
COMMENT ON COLUMN mtrtb027_campo_entrada.ic_chave IS 'Atributo que indica se o campo do formulário pode ser utilizado como chave de pesquisa posterior.';
COMMENT ON COLUMN mtrtb027_campo_entrada.no_label IS 'Atributo que armazena o valor a ser exibido no label do campo do formulário para o usuário final.';
COMMENT ON COLUMN mtrtb027_campo_entrada.de_mascara IS 'Atributo que armazena o valor da máscara de formatação do campo de for o caso.';
COMMENT ON COLUMN mtrtb027_campo_entrada.de_placeholder IS 'Atributo que armazena o valor do placeholder para exibição no campo de for o caso.';
COMMENT ON COLUMN mtrtb027_campo_entrada.nu_tamanho_minimo IS 'Atributo que armazena o número de caracteres mínimo utilizados em campos de texto livre.';
COMMENT ON COLUMN mtrtb027_campo_entrada.nu_tamanho_maximo IS 'Atributo que armazena o número de caracteres máximo utilizados em campos de texto livre.';

--
-- TOC entry 1861 (class 1259 OID 2025663)
-- Name: mtrsq027_campo_entrada; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq027_campo_entrada
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq027_campo_entrada OWNED BY mtrtb027_campo_entrada.nu_campo_entrada;

--
-- TOC entry 1862 (class 1259 OID 2025665)
-- Name: mtrtb028_opcao_campo; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb028_opcao_campo (
    nu_opcao_campo bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_campo_entrada bigint NOT NULL,
    no_value character varying(50) NOT NULL,
    no_opcao character varying(255) NOT NULL,
    ic_ativo boolean NOT NULL
);

COMMENT ON TABLE mtrtb028_opcao_campo IS 'Tabela responsável pelo armazenamento de opções pré-definidas para alguns tipos de atributos a exemplo:
- Lista;
- Radio;
- Check;

Registros desta tabela só devem ser excluídos fisicamente caso não exista nenhuma resposta de formulário atrelada a este registro. Caso essa situação ocorra o registro deve ser excluído logicamente definindo seu atributo ativo como false.';
COMMENT ON COLUMN mtrtb028_opcao_campo.nu_opcao_campo IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb028_opcao_campo.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb028_opcao_campo.nu_campo_entrada IS 'Atributo que identifica o campo de entrada do formulário ao qual a opção está associada.';
COMMENT ON COLUMN mtrtb028_opcao_campo.no_value IS 'Atributo utilizado para armazenar o valor que será definido como value da opção na interface gráfica.';
COMMENT ON COLUMN mtrtb028_opcao_campo.no_opcao IS 'Atributo que armazena o valor da opção que será exibida para o usuário no campo do formulário.';
COMMENT ON COLUMN mtrtb028_opcao_campo.ic_ativo IS 'Atributo que indica se a opção do campo de entrada está apta ou não para ser inserido no campo de entrada do formulário.';

--
-- TOC entry 1863 (class 1259 OID 2025668)
-- Name: mtrsq028_opcao_campo; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq028_opcao_campo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq028_opcao_campo OWNED BY mtrtb028_opcao_campo.nu_opcao_campo;

--
-- TOC entry 1864 (class 1259 OID 2025670)
-- Name: mtrtb029_campo_apresentacao; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb029_campo_apresentacao (
    nu_campo_apresentacao bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_largura integer NOT NULL,
    ic_dispositivo character varying(1) NOT NULL,
    nu_campo_formulario bigint NOT NULL
);

COMMENT ON TABLE mtrtb029_campo_apresentacao IS 'Tabela utilizada para armazenar informações acerca da apresentação do campo na interface gráfica conforme o dispositivo.';
COMMENT ON COLUMN mtrtb029_campo_apresentacao.nu_campo_apresentacao IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb029_campo_apresentacao.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb029_campo_apresentacao.nu_largura IS 'Atributo que armazena o número de colunas do bootstrap ocupadas pelo campo do formulário na estrutura de tela. Este valor pode variar de 1 a 12.';
COMMENT ON COLUMN mtrtb029_campo_apresentacao.ic_dispositivo IS 'Atributo utilizado para indicar o dispositivo de renderizaçãodo componente em tela.
Pode assumir as seguintes opções:
- W (Web)
- L (Low DPI)
- M (Medium DPI)
- H (High DPI)
- X (eXtra DPI)';

--
-- TOC entry 1865 (class 1259 OID 2025673)
-- Name: mtrsq029_campo_apresentacao; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq029_campo_apresentacao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq029_campo_apresentacao OWNED BY mtrtb029_campo_apresentacao.nu_campo_apresentacao;

--
-- TOC entry 1866 (class 1259 OID 2025675)
-- Name: mtrtb030_resposta_dossie; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb030_resposta_dossie (
    nu_resposta_dossie bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_dossie_produto bigint NOT NULL,
    de_resposta text,
    nu_campo_formulario bigint NOT NULL
);

COMMENT ON TABLE mtrtb030_resposta_dossie IS 'Tabela responsável pelo armazenamento das respostas aos itens montados dos formulários de inclusão de processos para um dossiê específico.';
COMMENT ON COLUMN mtrtb030_resposta_dossie.nu_resposta_dossie IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb030_resposta_dossie.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb030_resposta_dossie.nu_dossie_produto IS 'Atributo utilizado para identificar o dossiê de produto ao qual a resposta esta vinculada.';
COMMENT ON COLUMN mtrtb030_resposta_dossie.de_resposta IS 'Atributo utilizado para armazenar a resposta informada no formulário nos casos de atributos em texto aberto.';

--
-- TOC entry 1867 (class 1259 OID 2025681)
-- Name: mtrsq030_resposta_dossie; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq030_resposta_dossie
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq030_resposta_dossie OWNED BY mtrtb030_resposta_dossie.nu_resposta_dossie;

--
-- TOC entry 1868 (class 1259 OID 2025683)
-- Name: mtrsq032_elemento_conteudo; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq032_elemento_conteudo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 1869 (class 1259 OID 2025685)
-- Name: mtrtb033_garantia; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb033_garantia (
    nu_garantia integer NOT NULL,
    nu_versao integer NOT NULL,
    nu_garantia_bacen integer NOT NULL,
    no_garantia character varying(255) NOT NULL,
    ic_fidejussoria boolean NOT NULL
);

COMMENT ON TABLE mtrtb033_garantia IS 'Tabela responsável pelo armazenamento das garantias da CAIXA que serão vinculados aos dossiês criados.';
COMMENT ON COLUMN mtrtb033_garantia.nu_garantia IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb033_garantia.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb033_garantia.nu_garantia_bacen IS 'Atributo que armazena o número de operação corporativa da garantia.';
COMMENT ON COLUMN mtrtb033_garantia.no_garantia IS 'Atributo que armazena o nome corporativo da garantia';
COMMENT ON COLUMN mtrtb033_garantia.ic_fidejussoria IS 'Atributo utilizado para identificar se trata-se de uma garantia fidejussoria a exemplo do avalista';

--
-- TOC entry 1870 (class 1259 OID 2025688)
-- Name: mtrsq033_garantia; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq033_garantia
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq033_garantia OWNED BY mtrtb033_garantia.nu_garantia;

--
-- TOC entry 1871 (class 1259 OID 2025690)
-- Name: mtrtb035_garantia_informada; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb035_garantia_informada (
    nu_garantia_informada bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_dossie_produto bigint NOT NULL,
    nu_garantia integer NOT NULL,
    nu_produto integer,
    vr_garantia_informada numeric(15,2) NOT NULL,
    pc_garantia_informada numeric(8,2) DEFAULT 0 NOT NULL,
    ic_forma_garantia character varying(3) DEFAULT '0'::character varying NOT NULL,
    de_garantia text
);

COMMENT ON TABLE mtrtb035_garantia_informada IS 'Tabela responsável por manter a lista de garantias informadas durante o ciclo de vida do dossiê do produto.
Os documentos submetidos são arquivados normalmente na tabela de documentos e vinculados ao dossiê do produto através de instâncias.';
COMMENT ON COLUMN mtrtb035_garantia_informada.nu_garantia_informada IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb035_garantia_informada.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb035_garantia_informada.nu_dossie_produto IS 'Atributo utilizado para vincular o dossiê produto a garantia.';
COMMENT ON COLUMN mtrtb035_garantia_informada.nu_garantia IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb035_garantia_informada.nu_produto IS 'Atributo utilizado para vincular o produto a garantia.';
COMMENT ON COLUMN mtrtb035_garantia_informada.vr_garantia_informada IS 'Valor informado da garantia oferecida no dia da simulação.';
COMMENT ON COLUMN mtrtb035_garantia_informada.pc_garantia_informada IS 'Percentual da garantia informada em relação ao valor pretendido, podendo ser o valor da PMT, da operação, do saldo devedor, etc.';
COMMENT ON COLUMN mtrtb035_garantia_informada.ic_forma_garantia IS 'Definição da forma de utilização da garantia, para o campo pc_garantia_informada.
Valores possíveis:
SDD - Saldo devedor; 
VRC - Valor contratado; 
PMT - PMT;
LMD - Limite Disponibilizado; 
LMC - Limite Contratado;
';

--
-- TOC entry 1872 (class 1259 OID 2025698)
-- Name: mtrsq035_garantia_informada; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq035_garantia_informada
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq035_garantia_informada OWNED BY mtrtb035_garantia_informada.nu_garantia_informada;

--
-- TOC entry 1873 (class 1259 OID 2025700)
-- Name: mtrtb036_composicao_documental; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb036_composicao_documental (
    nu_composicao_documental bigint NOT NULL,
    nu_versao integer NOT NULL,
    no_composicao_documental character varying(100) NOT NULL,
    ts_inclusao timestamp without time zone NOT NULL,
    ts_revogacao timestamp without time zone,
    co_matricula_inclusao character varying(7) NOT NULL,
    co_matricula_revogacao character varying(7),
    ic_conclusao_operacao boolean NOT NULL
);

COMMENT ON TABLE mtrtb036_composicao_documental IS 'Tabela responsável por agrupar tipos de documentos visando criar estruturas que representam conjuntos de tipos de documentos a serem analisados conjuntamente.
Essa conjunção será utilizada na análise do nível documento e por ser formada como os exemplos a seguir:
- RG
- Contracheque
-----------------------------------------------------------------------------------
- CNH
- Conta Concessionária
- DIRPF';
COMMENT ON COLUMN mtrtb036_composicao_documental.nu_composicao_documental IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb036_composicao_documental.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb036_composicao_documental.no_composicao_documental IS 'Atributo utilizado para armazenar o nome negocial da composição de documentos.';
COMMENT ON COLUMN mtrtb036_composicao_documental.ts_inclusao IS 'Atributo que armazena a data/hora de cadastro do registro da composição documental.';
COMMENT ON COLUMN mtrtb036_composicao_documental.ts_revogacao IS 'Atributo que armazena a data/hora de revogação do registro da composição documental.';
COMMENT ON COLUMN mtrtb036_composicao_documental.co_matricula_inclusao IS 'Atributo que armazena a matrícula do usuário/serviço que realizou o cadastro do registro da composição documental.';
COMMENT ON COLUMN mtrtb036_composicao_documental.co_matricula_revogacao IS 'Atributo que armazena a matrícula do usuário/serviço que realizou a revogação do registro da composição documental.';
COMMENT ON COLUMN mtrtb036_composicao_documental.ic_conclusao_operacao IS 'Atributo utilizado para indicar se a composição deve ser analisada no ato de conclusão da operação indicando quais são os documentos necessarios a serem entregue para permitir a finalização da operação.';

--
-- TOC entry 1874 (class 1259 OID 2025703)
-- Name: mtrsq036_composicao_documental; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq036_composicao_documental
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq036_composicao_documental OWNED BY mtrtb036_composicao_documental.nu_composicao_documental;

--
-- TOC entry 1875 (class 1259 OID 2025705)
-- Name: mtrtb037_regra_documental; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb037_regra_documental (
    nu_regra_documental bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_composicao_documental bigint NOT NULL,
    nu_tipo_documento integer,
    nu_funcao_documental integer,
    nu_canal_captura integer,
    ix_antifraude numeric(10,5)
);

COMMENT ON TABLE mtrtb037_regra_documental IS 'Tabela utilizada para armazenar as regras de atendimento da composição. Para que uma composição documental esteja satisfeita, todas as regras a ela associadas devem ser atendidas, ou seja, a regra para cada documento definido deve ser verdadeira. Além da presença do documento vinculado valida no dossiê situações como índice mínimo do antifraude e canal devem ser respeitadas. Caso não seja atendida ao menos uma das regras, a composição não terá suas condições satisfatórias atendidas e consequentemente o nível documental não poderá ser atribuído ao dossiê do cliente.';
COMMENT ON COLUMN mtrtb037_regra_documental.nu_regra_documental IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb037_regra_documental.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb037_regra_documental.nu_composicao_documental IS 'Atributo que representa composição de tipos de documentos associada aos possíveis tipos de documentos.';
COMMENT ON COLUMN mtrtb037_regra_documental.nu_tipo_documento IS 'Atributo que representa o tipo de documento definido vinculado na relação com a composição de tipos de documentos. Caso este atributo esteja nulo, o atributo que representa a função documental deverá estar prenchido.';
COMMENT ON COLUMN mtrtb037_regra_documental.nu_funcao_documental IS 'Atributo que representa a função documental definida vinculada na relação com a composição de tipos de documentos. Caso este atributo esteja nulo, o atributo que representa o tipo de documento deverá estar prenchido.';
COMMENT ON COLUMN mtrtb037_regra_documental.nu_canal_captura IS 'Atributo utilizado para identificar o canal de captura específico para categorizar o conjunto.
Caso este atributo seja nulo, ele permite ao conjunto valer-se de qualquer canal não especificado em outro conjunto para o mesmo documento e composição documental, porém tendo o canal especificado.';
COMMENT ON COLUMN mtrtb037_regra_documental.ix_antifraude IS 'Atributo utilizado para armazenar o valor mínimo aceitável do índice atribuído ao documento pelo sistema de antifraude para considerar o documento válido na composição documental permitindo atribuir o nível documental ao dossiê do cliente.';

--
-- TOC entry 1876 (class 1259 OID 2025708)
-- Name: mtrsq037_regra_documental; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq037_regra_documental
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq037_regra_documental OWNED BY mtrtb037_regra_documental.nu_regra_documental;

--
-- TOC entry 1877 (class 1259 OID 2025710)
-- Name: mtrsq043_documento_garantia; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq043_documento_garantia
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 1878 (class 1259 OID 2025712)
-- Name: mtrsq044_comportamento_pesquisa; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq044_comportamento_pesquisa
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 1879 (class 1259 OID 2025714)
-- Name: mtrsq045_atributo_extracao; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq045_atributo_extracao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 1880 (class 1259 OID 2025716)
-- Name: mtrsq046_processo_adm; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq046_processo_adm
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 1881 (class 1259 OID 2025718)
-- Name: mtrsq047_contrato_adm; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq047_contrato_adm
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 1882 (class 1259 OID 2025720)
-- Name: mtrsq048_apenso_adm; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq048_apenso_adm
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 1883 (class 1259 OID 2025722)
-- Name: mtrsq049_documento_adm; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq049_documento_adm
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 1884 (class 1259 OID 2025724)
-- Name: mtrtb100_autorizacao; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb100_autorizacao (
    nu_autorizacao bigint NOT NULL,
    nu_versao integer NOT NULL,
    co_autorizacao bigint NOT NULL,
    ts_registro timestamp without time zone NOT NULL,
    ts_informe_negocio timestamp without time zone,
    co_protocolo_negocio character varying(100),
    ts_informe_ecm timestamp without time zone,
    co_protocolo_ecm character varying(100),
    nu_operacao integer NOT NULL,
    nu_modalidade integer NOT NULL,
    no_produto character varying(255) NOT NULL,
    nu_cpf_cnpj bigint NOT NULL,
    ic_tipo_pessoa character varying(1) NOT NULL,
    sg_canal_solicitacao character varying(10) NOT NULL,
    ts_conclusao timestamp without time zone
);

COMMENT ON TABLE mtrtb100_autorizacao IS 'Tabela utilizada para armazenar as autorizações relacionadas ao nível documental geradas e entregues para os clientes.';
COMMENT ON COLUMN mtrtb100_autorizacao.nu_autorizacao IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb100_autorizacao.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb100_autorizacao.co_autorizacao IS 'Atributo utilizado para armazenar o código de autorização gerado para entrega ao sistema de negócio e armazenamento junto ao dossiê do cliente.';
COMMENT ON COLUMN mtrtb100_autorizacao.ts_registro IS 'Atributo utilizado para armazenar a data e hora de recebimento da solicitação de autorização.';
COMMENT ON COLUMN mtrtb100_autorizacao.ts_informe_negocio IS 'Atributo utilizado para armazenar a data e hora de entrega do código de autorização para o sistema de negócio solicitante.';
COMMENT ON COLUMN mtrtb100_autorizacao.co_protocolo_negocio IS 'Atributo utilizado para armazenar o protocolo de confirmação de recebimento do código de autorização pelo sistema de negócio solicitante.';
COMMENT ON COLUMN mtrtb100_autorizacao.ts_informe_ecm IS 'Atributo utilizado para armazenar a data e hora de entrega do código de autorização para o sistema de ECM para armazenamento junto ao dossiê do produto.';
COMMENT ON COLUMN mtrtb100_autorizacao.co_protocolo_ecm IS 'Atributo utilizado para armazenar a data e hora de entrega do código de autorização para o sistema de ECM para armazenamento junto ao dossiê do produto.';
COMMENT ON COLUMN mtrtb100_autorizacao.nu_operacao IS 'Atributo utilizado para armazenar o código de operação do produto solicitado na autorização.';
COMMENT ON COLUMN mtrtb100_autorizacao.nu_modalidade IS 'Atributo utilizado para armazenar o codigo da modalidade do produto solicitado na autorização.';
COMMENT ON COLUMN mtrtb100_autorizacao.no_produto IS 'Atributo utilizado para armazenar o nome do produto solicitado na autorização.';
COMMENT ON COLUMN mtrtb100_autorizacao.nu_cpf_cnpj IS 'Atributo utilizado para armazenar o número do CPF ou CNPJ do cliente relacionado com a autorização.';
COMMENT ON COLUMN mtrtb100_autorizacao.ic_tipo_pessoa IS 'Atributo utilizado para indicar o tipo de pessoa, se física ou jurídica podendo assumir os seguintes valores:
F - Física
J - Jurídica';
COMMENT ON COLUMN mtrtb100_autorizacao.sg_canal_solicitacao IS 'Sigla de identificação do canal/sistema solicitante da autorização.
Informação é obtida pela comparação do codigo de integração enviado com o registro da tabela 006';
COMMENT ON COLUMN mtrtb100_autorizacao.ts_conclusao IS 'Atributo utilizado para armazenar a data e hora da conclusão da operação vinculada a autorização concedida.';

--
-- TOC entry 1885 (class 1259 OID 2025727)
-- Name: mtrsq100_autorizacao; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq100_autorizacao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq100_autorizacao OWNED BY mtrtb100_autorizacao.nu_autorizacao;

--
-- TOC entry 1886 (class 1259 OID 2025729)
-- Name: mtrtb101_documento; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb101_documento (
    nu_documento bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_autorizacao bigint NOT NULL,
    de_finalidade character varying(100) NOT NULL,
    co_documento_ged character varying(100) NOT NULL
);

COMMENT ON TABLE mtrtb101_documento IS 'Tabela utilizada para armazenar a informação dos documentos identificados e utilizados para a emissão da autorização.';
COMMENT ON COLUMN mtrtb101_documento.nu_documento IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb101_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb101_documento.nu_autorizacao IS 'Atributo utilizado para identificar a autorização que está relacionada ao documento.';
COMMENT ON COLUMN mtrtb101_documento.de_finalidade IS 'Atributo que representa para qual finalidade o documento foi usado';
COMMENT ON COLUMN mtrtb101_documento.co_documento_ged IS 'Atributo utilizado para armazenar a referencia do registro do documento junto ao GED utilizado na emissão da autorização';

--
-- TOC entry 1887 (class 1259 OID 2025732)
-- Name: mtrsq101_documento; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq101_documento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE mtrsq101_documento OWNED BY mtrtb101_documento.nu_documento;

--
-- TOC entry 1888 (class 1259 OID 2025734)
-- Name: mtrsq102_autorizacao_negada; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq102_autorizacao_negada
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 1889 (class 1259 OID 2025736)
-- Name: mtrsq103_autorizacao_orientacao; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq103_autorizacao_orientacao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 1890 (class 1259 OID 2025738)
-- Name: mtrsq200_sicli_erro; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--
CREATE SEQUENCE mtrsq200_sicli_erro
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 1891 (class 1259 OID 2025740)
-- Name: mtrtb001_pessoa_fisica; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb001_pessoa_fisica (
    nu_dossie_cliente bigint NOT NULL,
    dt_nascimento date,
    ic_estado_civil integer,
    nu_nis character varying(20),
    nu_identidade character varying(15),
    no_orgao_emissor character varying(15),
    no_mae character varying(255),
    no_pai character varying(255),
    vr_renda_mensal numeric(15,2)
);

COMMENT ON TABLE mtrtb001_pessoa_fisica IS 'Tabela de especialização do dossiê de cliente para armazenar os atributos específicos de uma pessoa física.';
COMMENT ON COLUMN mtrtb001_pessoa_fisica.nu_dossie_cliente IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb001_pessoa_fisica.dt_nascimento IS 'Atributo utilizado para armazenar a data de nascimento de pessoas físicas.';
COMMENT ON COLUMN mtrtb001_pessoa_fisica.ic_estado_civil IS 'Atributo utilizado para armazenar o estado civil de pessoas físicas. Pose assumir:
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
COMMENT ON COLUMN mtrtb001_pessoa_fisica.nu_nis IS 'Atributo utilizado para armazenar o número do NIS de pessoas físicas.';
COMMENT ON COLUMN mtrtb001_pessoa_fisica.nu_identidade IS 'Atributo utilizado para armazenar o número de identidade de pessoas físicas.';
COMMENT ON COLUMN mtrtb001_pessoa_fisica.no_orgao_emissor IS 'Atributo utilizado para armazenar o órgão emissor da identidade de pessoas físicas.';
COMMENT ON COLUMN mtrtb001_pessoa_fisica.no_mae IS 'Atributo utilizado para armazenar o nome da mãe de pessoas físicas.';
COMMENT ON COLUMN mtrtb001_pessoa_fisica.no_pai IS 'Atributo utilizado para armazenar o nome do pai de pessoas físicas.';

--
-- TOC entry 1892 (class 1259 OID 2025746)
-- Name: mtrtb001_pessoa_juridica; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb001_pessoa_juridica (
    nu_dossie_cliente bigint NOT NULL,
    no_razao_social character varying(255) NOT NULL,
    dt_fundacao date,
    ic_segmento character varying(10),
    vr_faturamento_anual numeric(15,2),
    ic_conglomerado boolean
);

COMMENT ON TABLE mtrtb001_pessoa_juridica IS 'Tabela de especialização do dossiê de cliente para armazenar os atributos específicos de uma pessoa jurídica.';
COMMENT ON COLUMN mtrtb001_pessoa_juridica.nu_dossie_cliente IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb001_pessoa_juridica.no_razao_social IS 'Atributo utilizado para armazenar a razão social de pessoas jurídicas.';
COMMENT ON COLUMN mtrtb001_pessoa_juridica.dt_fundacao IS 'Atributo utilizado para armazenar a data de fundação de pessoas jurídicas.';
COMMENT ON COLUMN mtrtb001_pessoa_juridica.ic_segmento IS 'Atributo para identificar o segmento da empresa, podendo assumir os valores oriundos da view do SIICO:
		- MEI
		- MPE
		- MGE
		- CORP';
COMMENT ON COLUMN mtrtb001_pessoa_juridica.ic_conglomerado IS 'Atributo utilizado para indicar se a empresa integra um conglomerado';

--
-- TOC entry 1893 (class 1259 OID 2025749)
-- Name: mtrtb004_dossie_cliente_produto; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb004_dossie_cliente_produto (
    nu_dossie_cliente_produto bigint DEFAULT nextval('mtrsq004_dossie_cliente_produto'::regclass) NOT NULL,
    nu_versao integer NOT NULL,
    nu_dossie_produto bigint NOT NULL,
    nu_dossie_cliente bigint NOT NULL,
    nu_sequencia_titularidade integer,
    ic_tipo_relacionamento character varying(50) NOT NULL,
    nu_dossie_cliente_relacionado bigint
);

COMMENT ON TABLE mtrtb004_dossie_cliente_produto IS 'Tabela de relacionamento para permitir vincular um dossiê de produto a mais de um dossiê de cliente devido a necessidades de produtos com mais de um titular.';
COMMENT ON COLUMN mtrtb004_dossie_cliente_produto.nu_dossie_cliente_produto IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb004_dossie_cliente_produto.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb004_dossie_cliente_produto.nu_dossie_produto IS 'Atributo que armazena a referência para o dossiê do produto vinculado na relação.';
COMMENT ON COLUMN mtrtb004_dossie_cliente_produto.nu_dossie_cliente IS 'Atributo que armazena a referência para o dossiê do cliente vinculado na relação.';
COMMENT ON COLUMN mtrtb004_dossie_cliente_produto.nu_sequencia_titularidade IS 'Atributo que indica a sequência de titularidade dos clientes para aquele processo. Ao cadastrar um processo o operador pode incluir titulares conforme a necessidade do produto e este atributo indicara a ordinalidade dos titulares.';
COMMENT ON COLUMN mtrtb004_dossie_cliente_produto.ic_tipo_relacionamento IS 'Atributo que indica o tipo de relacionamento do cliente com o produto, podendo assumir os valores:
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

--
-- TOC entry 1894 (class 1259 OID 2025753)
-- Name: mtrtb005_documento_cliente; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb005_documento_cliente (
    nu_documento bigint NOT NULL,
    nu_dossie_cliente bigint NOT NULL
);

COMMENT ON TABLE mtrtb005_documento_cliente IS 'Tabela de relacionamento que vincula um documento ao dossiê de um cliente.';
COMMENT ON COLUMN mtrtb005_documento_cliente.nu_documento IS 'Atributo que representa o documento vinculado ao dossiê do cliente referenciado no registro.';
COMMENT ON COLUMN mtrtb005_documento_cliente.nu_dossie_cliente IS 'Atributo que representa a o dossiê do cliente vinculado na relação de documentos.';

--
-- TOC entry 1895 (class 1259 OID 2025756)
-- Name: mtrtb011_funcao_documento; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb011_funcao_documento (
    nu_tipo_documento integer NOT NULL,
    nu_funcao_documental integer NOT NULL
);

COMMENT ON TABLE mtrtb011_funcao_documento IS 'Tabela associativa que vincula um tipo de documento a sua função.
Ex: 
- RG x Identificação
- DIRPF x Renda
- DIRPF x Identificação';
COMMENT ON COLUMN mtrtb011_funcao_documento.nu_tipo_documento IS 'Atributo que representa a o tipo de documento vinculado na relação com a função documental.';
COMMENT ON COLUMN mtrtb011_funcao_documento.nu_funcao_documental IS 'Atributo que representa a o função documental vinculado na relação com o tipo de documento.';

--
-- TOC entry 1896 (class 1259 OID 2025759)
-- Name: mtrtb017_stco_instnca_documento; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb017_stco_instnca_documento (
    nu_stco_instnca_documento bigint DEFAULT nextval('mtrsq017_situacao_instnca_dcmnto'::regclass) NOT NULL,
    nu_versao integer NOT NULL,
    nu_instancia_documento bigint NOT NULL,
    nu_situacao_documento integer NOT NULL,
    nu_motivo_stco_documento integer,
    ts_inclusao timestamp without time zone NOT NULL,
    co_matricula character varying(7) NOT NULL,
    nu_unidade integer NOT NULL
);

COMMENT ON TABLE mtrtb017_stco_instnca_documento IS 'Tabela responsável por armazenar o histórico de situações relativas a instância do documento em avaliação. Cada vez que houver uma mudança na situação apresentada pelo processo, um novo registro deve ser inserido gerando assim um histórico das situações vivenciadas durante o seu ciclo de vida.';
COMMENT ON COLUMN mtrtb017_stco_instnca_documento.nu_stco_instnca_documento IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb017_stco_instnca_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb017_stco_instnca_documento.nu_instancia_documento IS 'Atributo utilizado pata armazenar a referência da instancia do documento em avaliação vinculado a situação.';
COMMENT ON COLUMN mtrtb017_stco_instnca_documento.nu_situacao_documento IS 'Atributo utilizado pata armazenar a referencia a situação do documento escolhida vinculada a instancia do documento em avaliação';
COMMENT ON COLUMN mtrtb017_stco_instnca_documento.nu_motivo_stco_documento IS 'Atributo utilizado pata armazenar a referencia o motivo especifico para a situação escolhida vinculada a instancia do documento em avaliação';
COMMENT ON COLUMN mtrtb017_stco_instnca_documento.ts_inclusao IS 'Atributo utilizado para armazenar a data/hora de atribuição da situação ao dossiê.';
COMMENT ON COLUMN mtrtb017_stco_instnca_documento.co_matricula IS 'Atributo utilizado para armazenar a matrícula do empregado ou serviço que atribuiu a situação a instância do documento em avaliação.';
COMMENT ON COLUMN mtrtb017_stco_instnca_documento.nu_unidade IS 'Atributo que indica o CGC da unidade do empregado que registrou a situação da instancia do documento analisado.';

--
-- TOC entry 1897 (class 1259 OID 2025763)
-- Name: mtrtb018_unidade_tratamento; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb018_unidade_tratamento (
    nu_dossie_produto bigint NOT NULL,
    nu_unidade integer NOT NULL
);

COMMENT ON TABLE mtrtb018_unidade_tratamento IS 'Tabela utilizada para identificar as unidades de tratamento atribuídas para o dossiê naquele dado momento.
Sempre que a situação do dossiê for modificada, os registros referentes ao dossiê especificamente serão excluídos e reinseridos novos com base na nova situação.';
COMMENT ON COLUMN mtrtb018_unidade_tratamento.nu_dossie_produto IS 'Atributo utilizado para vincular o dossiê do produto com a unidade de tratamento.';
COMMENT ON COLUMN mtrtb018_unidade_tratamento.nu_unidade IS 'Atributo que indica o número do CGC da unidade responsável pelo tratamento do dossiê.';

--
-- TOC entry 1898 (class 1259 OID 2025766)
-- Name: mtrtb019_campo_formulario; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb019_campo_formulario (
    nu_campo_formulario bigint DEFAULT nextval('mtrsq019_campo_formulario'::regclass) NOT NULL,
    nu_campo_entrada bigint NOT NULL,
    nu_ordem integer NOT NULL,
    nu_versao integer NOT NULL,
    ic_obrigatorio boolean NOT NULL,
    de_expressao text,
    ic_ativo boolean NOT NULL,
    no_campo character varying(50) NOT NULL,
    nu_processo integer NOT NULL
);

COMMENT ON TABLE mtrtb019_campo_formulario IS 'Atributo utilizado para vincular o campo entrrada ao formulário.';
COMMENT ON COLUMN mtrtb019_campo_formulario.nu_campo_formulario IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb019_campo_formulario.nu_campo_entrada IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb019_campo_formulario.nu_ordem IS 'Atributo utilizado para definir a ordem de exibição dos campos do formulário.';
COMMENT ON COLUMN mtrtb019_campo_formulario.nu_versao IS 'Atributo que armazena a versão';
COMMENT ON COLUMN mtrtb019_campo_formulario.ic_obrigatorio IS 'Atributo que armazena o indicativo do obrigatoriedade do campo no formulário.';
COMMENT ON COLUMN mtrtb019_campo_formulario.de_expressao IS 'Atributo que armazena a expressão a ser aplicada pelo Java script para determinar a exposição ou não do campo no formulário.';
COMMENT ON COLUMN mtrtb019_campo_formulario.ic_ativo IS 'Atributo que indica se o campo de entrada está apto ou não para ser inserido no formulário.';
COMMENT ON COLUMN mtrtb019_campo_formulario.no_campo IS 'Atributo que indica o nome do campo. Este nome pode ser utilizado pela estrutura de programação para referenciar a campo no formulario independente do label exposto para o usuário.';

--
-- TOC entry 1899 (class 1259 OID 2025773)
-- Name: mtrtb023_produto_processo; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb023_produto_processo (
    nu_processo integer NOT NULL,
    nu_produto integer NOT NULL
);

COMMENT ON TABLE mtrtb023_produto_processo IS 'Tabela de relacionamento para vinculação do produto com o processo. 
Existe a possibilidade que um produto seja vinculado a diversos processos pois pode diferenciar a forma de realizar as ações conforme o canal de contratação, campanha, ou outro fator, como por exemplo uma conta que seja contratada pela agência física, agência virtual, CCA ou Aplicativo de abertura de contas.';
COMMENT ON COLUMN mtrtb023_produto_processo.nu_processo IS 'Atributo que representa o processo vinculado na relação com o produto.';
COMMENT ON COLUMN mtrtb023_produto_processo.nu_produto IS 'Atributo que representa o produto vinculado na relação com o processo.';

--
-- TOC entry 1900 (class 1259 OID 2025776)
-- Name: mtrtb026_relacao_processo; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb026_relacao_processo (
    nu_processo_pai integer NOT NULL,
    nu_processo_filho integer NOT NULL,
    nu_versao integer NOT NULL,
    nu_prioridade integer,
    nu_ordem integer
);

COMMENT ON TABLE mtrtb026_relacao_processo IS 'Tabela de auto relacionamento da tabela de processos utilizada para identificar a relação entre os mesmos.';
COMMENT ON COLUMN mtrtb026_relacao_processo.nu_processo_pai IS 'Atributo que representa o processo pai da relação entre os processos. Os processos "pai" são os processos que estão em nivel superior em uma visão de arvore de processos.
Os processos que não possuem registro com outro processo pai são conhecidos como processos patriarcas e estes são os processos inicialmente exibidos nas telas de tratamento e/ou visão de arvores.';
COMMENT ON COLUMN mtrtb026_relacao_processo.nu_processo_filho IS 'Atributo que representa o processo filho da relação entre os processos. Os processos "filho" são os processos que estão em nivel inferior em uma visão de organograma dos processos.';
COMMENT ON COLUMN mtrtb026_relacao_processo.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
COMMENT ON COLUMN mtrtb026_relacao_processo.nu_prioridade IS 'Atributo que determina a ordem de prioridade de atendimento do processo na fila de tratamento sob a otica do processo pai. 

Apenas processos do tipo "dossiê" deverão ter a possibilidade de ser priorizados.

Uma vez definida a prioridade para um processo, todos os registros que possuem o mesmo processo pai necessitarão ser definidos.

Quando processos são priorizados, a visão para tratamento deve ser restrita e apresentar apenas a visão do processo patriarca com o botão de captura para tratamento fazendo com que o operador não saiba o dossiê de qual processo irá tratar.';
COMMENT ON COLUMN mtrtb026_relacao_processo.nu_ordem IS 'Atributo utilizado para definir a ordem de execução dos processos filho sob a otica do processo pai determinando a sequencia de execução das etapas do processo.

Este atributo não deverá ser preenchido para os processos patriarcas e/ou processos de definição de dossiê de produto.';

--
-- TOC entry 1901 (class 1259 OID 2025779)
-- Name: mtrtb031_resposta_opcao; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb031_resposta_opcao (
    nu_opcao_campo bigint NOT NULL,
    nu_resposta_dossie bigint NOT NULL
);

COMMENT ON TABLE mtrtb031_resposta_opcao IS 'Tabela de relacionamento com finalidade de armazenar todas as respostas objetivas informadas pelo cliente a mesma pergunta no formulário de identificação do dossiê.';
COMMENT ON COLUMN mtrtb031_resposta_opcao.nu_opcao_campo IS 'Atributo que representa a opção selecionada vinculado na relação com a resposta do formulário.';
COMMENT ON COLUMN mtrtb031_resposta_opcao.nu_resposta_dossie IS 'Atributo que representa a resposta vinculada na relação com a opção selecionada do campo.';

--
-- TOC entry 1902 (class 1259 OID 2025782)
-- Name: mtrtb032_elemento_conteudo; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb032_elemento_conteudo (
    nu_elemento_conteudo bigint DEFAULT nextval('mtrsq032_elemento_conteudo'::regclass) NOT NULL,
    nu_versao integer NOT NULL,
    nu_elemento_vinculador bigint,
    nu_produto integer,
    nu_processo integer,
    nu_tipo_documento integer,
    ic_obrigatorio boolean NOT NULL,
    qt_elemento_obrigatorio integer,
    ic_validar boolean NOT NULL,
    no_campo character varying(50) NOT NULL,
    de_expressao text,
    no_elemento character varying(100)
);

COMMENT ON TABLE mtrtb032_elemento_conteudo IS 'Tabela responsável pelo armazenamento dos elementos que compõem o mapa de documentos para vinculação ao processo.
Esses elementos estão associados aos tipos de documentos para identificação dos mesmo no ato da captura.';
COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_elemento_conteudo IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';
COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_elemento_vinculador IS 'Atributo utilizado para armazenar uma outra instancia de elemento ao qual o elemento se vincula.
Esta estrutura permite criar uma estrutura hierarquizada de elementos, porem elementos só devem ser vinculados a outros elementos que não sejam finais, ou seja, não sejam associados a nenhum tipo de elemento que seja associado a um tipo de documento.';
COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_produto IS 'Atributo utilizado para vincular o produto ao elemento conteúdo.';
COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_processo IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_tipo_documento IS 'Atributo utilizado para identificar o elemento de ponta que possui vinculo com algum tipo de documento.';
COMMENT ON COLUMN mtrtb032_elemento_conteudo.ic_obrigatorio IS 'Atributo para indicar se o elemento é de submissão obrigatoria ou não de forma individual.';
COMMENT ON COLUMN mtrtb032_elemento_conteudo.qt_elemento_obrigatorio IS 'Este atributo indica a quantidade de elementos que são de tipo de elemento final obrigatórios dentro da sua árvore e só deve ser preenchido se o tipo do elemento permitir agrupamento: Exemplo:
- Identificação (Este elemento deve ter 2 filhos obrigatórios)
   |-- RG
   |-- CNH
   |-- Passaporte
   |-- CTPS';
COMMENT ON COLUMN mtrtb032_elemento_conteudo.ic_validar IS 'Atributo que indica se o documento deve ser validado quando apresentado no processo.
Caso verdadeiro, a instância do documento deve ser criada com a situação vazia
Caso false, a instância do documento deve ser criada com a situação de aprovada conforme regra de negócio realizada pelo sistema, desde que já exista outra instância do mesmo documento com situação aprovada previamente.';
COMMENT ON COLUMN mtrtb032_elemento_conteudo.no_campo IS 'Atributo que indica o nome do campo. Este nome pode ser utilizado pela estrutura de programação para referenciar a elemento do documento na interface independente do label exposto para o usuário.';
COMMENT ON COLUMN mtrtb032_elemento_conteudo.de_expressao IS 'Atributo que armazena a expressão a ser aplicada pelo javascript para determinar a exposição ou não do elemento para submissão.';
COMMENT ON COLUMN mtrtb032_elemento_conteudo.no_elemento IS 'Atributo utilizado para armazenar o nome de apresentação do tipo de elemento de conteúdo.
Este atributo deve estar preenchido quando a vinculação com o tipo de documento for nula, pois nesta situação o valor desta tabela será apresentado na interface gráfica. O registro que possuir vinculação com o tipo de documento, será o nome deste que deverá ser exposto.';

--
-- TOC entry 1903 (class 1259 OID 2025789)
-- Name: mtrtb034_garantia_produto; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb034_garantia_produto (
    nu_produto integer NOT NULL,
    nu_garantia integer NOT NULL
);

COMMENT ON TABLE mtrtb034_garantia_produto IS 'Tabela de relacionamento responsável por vincular as garantias possíveis de exibição quando selecionado um dado produto.';
COMMENT ON COLUMN mtrtb034_garantia_produto.nu_produto IS 'Atributo utilizado para armazenar a referência do produto da relação com a garantia';
COMMENT ON COLUMN mtrtb034_garantia_produto.nu_garantia IS 'Atributo utilizado para armazenar a referência da garantia da relação com o produto';

--
-- TOC entry 1904 (class 1259 OID 2025792)
-- Name: mtrtb038_nivel_documental; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb038_nivel_documental (
    nu_composicao_documental bigint NOT NULL,
    nu_dossie_cliente bigint NOT NULL
);

COMMENT ON TABLE mtrtb038_nivel_documental IS 'Tabela responsável por armazenar as referências de níveis documentais possíveis para associação a clientes e produtos.
O nível documental é uma informação pertencente ao cliente, porém o mesmo deve estar associado a um conjunto de tipos de documentos e informações que torna a informação dinâmica para o cliente, ou seja, se um cliente submete um determinado documento que aumenta sua gama de informações válidas, ele pode ganhar um determinado nível documental, porém se um documento passa a ter sua validade ultrapassada o cliente perde aquele determinado nível.';
COMMENT ON COLUMN mtrtb038_nivel_documental.nu_composicao_documental IS 'Atributo utilizado para identificar a composição documental que foi atingida ao atribuir o nível documental para o cliente.';
COMMENT ON COLUMN mtrtb038_nivel_documental.nu_dossie_cliente IS 'Atributo que representa o dossiê do cliente vinculado na atribuição do nível documental.';

--
-- TOC entry 1905 (class 1259 OID 2025795)
-- Name: mtrtb039_produto_composicao; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb039_produto_composicao (
    nu_composicao_documental bigint NOT NULL,
    nu_produto integer NOT NULL
);

COMMENT ON TABLE mtrtb039_produto_composicao IS 'Tabela de relacionamento que vincula uma composição de documentos a um ou mais produtos.
Essa associação visa identificar as necessidade documentais para um determinado produto no ato de sua contratação, permitindo ao sistema autorizar ou não a operação do ponto de vista documental. ';
COMMENT ON COLUMN mtrtb039_produto_composicao.nu_composicao_documental IS 'Atributo que representa o composição envolvida na relação com o produto';
COMMENT ON COLUMN mtrtb039_produto_composicao.nu_produto IS 'Atributo que representa o produto envolvido na relação com a composição';

--
-- TOC entry 1906 (class 1259 OID 2025798)
-- Name: mtrtb040_cadeia_tipo_sto_dossie; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb040_cadeia_tipo_sto_dossie (
    nu_tipo_situacao_atual integer NOT NULL,
    nu_tipo_situacao_seguinte integer NOT NULL
);

COMMENT ON TABLE mtrtb040_cadeia_tipo_sto_dossie IS 'Tabela utilizada para armazenar as possibilidades de tipos de situações possíveis de aplicação em um dossiê de produto a partir um determinado tipo de situação.';
COMMENT ON COLUMN mtrtb040_cadeia_tipo_sto_dossie.nu_tipo_situacao_atual IS 'Atributo que representa o tipo de situação atual na relação';
COMMENT ON COLUMN mtrtb040_cadeia_tipo_sto_dossie.nu_tipo_situacao_seguinte IS 'Atributo que representa o tipo de situação que pode ser aplicado como proximo tipo de situação de um dossiê de produto';

--
-- TOC entry 1907 (class 1259 OID 2025801)
-- Name: mtrtb041_cadeia_stco_documento; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb041_cadeia_stco_documento (
    nu_situacao_documento_atual integer NOT NULL,
    nu_situacao_documento_seguinte integer NOT NULL
);

COMMENT ON TABLE mtrtb041_cadeia_stco_documento IS 'Tabela utilizada para armazenar as possibilidades de tipos de situações possíveis de aplicação em uma instância de documento a partir um determinado tipo de situação.';
COMMENT ON COLUMN mtrtb041_cadeia_stco_documento.nu_situacao_documento_atual IS 'Atributo que representa o tipo de situação atual na relação';
COMMENT ON COLUMN mtrtb041_cadeia_stco_documento.nu_situacao_documento_seguinte IS 'Atributo que representa o tipo de situação que pode ser aplicado como proximo tipo de situação de uma instancia de documento.';

--
-- TOC entry 1908 (class 1259 OID 2025804)
-- Name: mtrtb042_cliente_garantia; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb042_cliente_garantia (
    nu_garantia_informada bigint NOT NULL,
    nu_dossie_cliente bigint NOT NULL
);

COMMENT ON TABLE mtrtb042_cliente_garantia IS 'Tabela de relacionamento entre o dossiê cliente (tb001) e a garantia informada (tb035). Representa as garantias que têm pessoas relacionadas, garantias do tipo fidejussórias (aval, fiador, etc).';
COMMENT ON COLUMN mtrtb042_cliente_garantia.nu_garantia_informada IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb042_cliente_garantia.nu_dossie_cliente IS 'Atributo que representa a chave primaria da entidade.';

--
-- TOC entry 1909 (class 1259 OID 2025807)
-- Name: mtrtb043_documento_garantia; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb043_documento_garantia (
    nu_garantia integer NOT NULL,
    nu_tipo_documento integer,
    nu_processo integer NOT NULL,
    nu_documento_garantia bigint DEFAULT nextval('mtrsq043_documento_garantia'::regclass) NOT NULL,
    nu_funcao_documental integer,
    nu_versao integer NOT NULL
);

COMMENT ON TABLE mtrtb043_documento_garantia IS 'Tabela utilizada para identificar os documentos (por tipo ou função) que são necessários para o tipo de garantia';
COMMENT ON COLUMN mtrtb043_documento_garantia.nu_garantia IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb043_documento_garantia.nu_tipo_documento IS 'Atributo que representa o tipo de documento especifico definido na relação com a a garantia de acordo com o processo composição de tipos de documentos. Caso este atributo esteja nulo, o atributo que representa a função documental deverá estar prenchido.';
COMMENT ON COLUMN mtrtb043_documento_garantia.nu_processo IS 'Atributo que representa a chave primaria da entidade.';

--
-- TOC entry 1910 (class 1259 OID 2025811)
-- Name: mtrtb044_comportamento_pesquisa; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb044_comportamento_pesquisa (
    nu_comportamento_pesquisa integer DEFAULT nextval('mtrsq044_comportamento_pesquisa'::regclass) NOT NULL,
    nu_versao integer NOT NULL,
    nu_produto integer NOT NULL,
    ic_sistema_retorno character varying(10) NOT NULL,
    ic_codigo_retorno character varying(10) NOT NULL,
    ic_bloqueio boolean NOT NULL,
    de_orientacao text NOT NULL
);

COMMENT ON TABLE mtrtb044_comportamento_pesquisa IS 'Tabela utilizada para armazenar o comportamento esperado conforme os codigos de retorno da pesquisa do SIPES com relação ao impedimento ou não de gerar uma autorização para o cliente solicitado e definição das mensagens de orientação que deverão ser encaminhadas para os usuarios.';
COMMENT ON COLUMN mtrtb044_comportamento_pesquisa.nu_comportamento_pesquisa IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb044_comportamento_pesquisa.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
COMMENT ON COLUMN mtrtb044_comportamento_pesquisa.nu_produto IS 'Atributo utilizado para identificar o produto relacionado ao comportamento do retorno da pesquisa';
COMMENT ON COLUMN mtrtb044_comportamento_pesquisa.ic_sistema_retorno IS 'Atributo utilizado para indicar o sistema relacionado com o retorno da pesquisa cadastral baseado no dominio abaixo:

SICPF
SERASA
CADIN
SINAD
CCF
SPC
SICOW';
COMMENT ON COLUMN mtrtb044_comportamento_pesquisa.ic_codigo_retorno IS 'Atributo utilizado para definir o valor do codigo de retorno que deve ser analisado para envio da mensagem e definição do comportamento de emissão ou bloqueio de autorização documental.';
COMMENT ON COLUMN mtrtb044_comportamento_pesquisa.ic_bloqueio IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao sistema especificado no atributo "ic_sistema" retorne um resultado com o codigo definido no atributo "vr_codigo_retorno".';
COMMENT ON COLUMN mtrtb044_comportamento_pesquisa.de_orientacao IS 'Atributo utilizado para armazenar a mensagem de orientação que deve ser encaminhada para o usuario do sistema. ';

--
-- TOC entry 1911 (class 1259 OID 2025818)
-- Name: mtrtb045_atributo_extracao; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb045_atributo_extracao (
    nu_atributo_extracao integer DEFAULT nextval('mtrsq045_atributo_extracao'::regclass) NOT NULL,
    nu_versao integer NOT NULL,
    nu_tipo_documento integer NOT NULL,
    no_atributo_negocial character varying(100) NOT NULL,
    no_atributo_retorno character varying(100),
    ic_ativo boolean NOT NULL,
    ic_ged boolean NOT NULL,
    no_atributo_ged character varying(100),
    ic_calculo_data boolean,
    ic_campo_sicpf character varying(50),
    ic_modo_sicpf character varying(1),
    no_atributo_documento character varying(100) NOT NULL,
    ic_tipo_ged character varying(20),
    ic_obrigatorio_ged boolean,
    ic_tipo_campo character varying(20),
    ic_obrigatorio boolean NOT NULL,
    no_atributo_sicli character varying(100),
    no_objeto_sicli character varying(100),
    ic_tipo_sicli character varying(50),
    ic_tipo_geral character varying(50),
    pc_alteracao_permitido integer
);

COMMENT ON TABLE mtrtb045_atributo_extracao IS 'Tabela utilizada para identificar os atributos que devem ser extraidos pelas rotinas automaticas de OCR/ICR ou de complementação utilizadas para alimentar a tabela 007 de atributos do documento';
COMMENT ON COLUMN mtrtb045_atributo_extracao.nu_atributo_extracao IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb045_atributo_extracao.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
COMMENT ON COLUMN mtrtb045_atributo_extracao.nu_tipo_documento IS 'Atributo que representa o tipo de documento vinculado cujo atributo deve ser extraido.';
COMMENT ON COLUMN mtrtb045_atributo_extracao.no_atributo_negocial IS 'Atributo utilizado para armazenar o nome do atributo identificado pelo negocio. Esse valor armazena o nome utilizado no dia a dia e que pode ser apresentado como algum label no sistema quando necessario.';
COMMENT ON COLUMN mtrtb045_atributo_extracao.no_atributo_retorno IS 'Atributo utilizado para armazenar o nome do atributo retornado pela rotina/serviço de extração de dados do documento. Esse valor armazena o nome utilizado no campo de retorno da informação.';
COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_ativo IS 'Atributo utilizado para definir se o atributo deve ser procurado para captura no objeto de retorno dos dados extaidos do documento.';
COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_ged IS 'Atributo utilizado para indicar que o atributo deve ser utilizado para alimentar a informação do GED';
COMMENT ON COLUMN mtrtb045_atributo_extracao.no_atributo_ged IS 'Atributo utilizado para definir o nome do atributo do GED que tem relação com o atributo do documento.';
COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_calculo_data IS 'Atrinuto utilizado para indicar se o atributo extraido do documento deve ser utilizado no calculo da data de validade baseado nas regras do "ic_validade_auto_contida" e "pz_validade_dias". Para este atributo deverá ter apenas um regsitro marcado como true para o mesmo tipo de documento.';
COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_campo_sicpf IS 'Atributo utilizado para indicar qual campo de retorno o SICPF deve ser utilizado para uma possivel comparação podendo assumi o seguinte dominio:

- NASCIMENTO
- NOME
- MAE
- ELEITOR';
COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_modo_sicpf IS 'Atributo utilizado para indicar a forma de comparação com o campo de retorno o SICPF deve ser utilizado para uma possivel comparação podendo assumi o seguinte dominio:

- E - Exato
- P - Parcial';
COMMENT ON COLUMN mtrtb045_atributo_extracao.no_atributo_documento IS 'Atributo utilizado para armazenar o nome do atributo identificado pela integração com outros sistemas. Esse valor armazena o nome utilizado nas representações JSON/XML encaminhadas nos serviços de integração e na identificação do atributo utilizado na tb007.';
COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_tipo_ged IS 'Atributo utilizado para indicar qual o tipo de atributo definido junto a classe GED. Pode assumir os seguintes valores:
- BOOLEAN
- STRING
- LONG
- DATE
';
COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_obrigatorio_ged IS 'Atributo utilizado para indicar se esta informação junto ao GED tem cunho obrigatorio.';
COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_tipo_campo IS 'Atributo utilizado para indicar o tipo de campo que deverá ser utilizado pela interface para os casos de captura da informalção quando inserida de forma manual.
Exemplos validos para este atributo são:
- INPUT_TEXT
- INPUT_PASSWORD
- INPUT_DATE
- INPUT_DATE_TIME
- INPUT_TIME
- INPUT_EMAIL
- INPUT_NUMBER
- INPUT_CPF
- INPUT_CNPJ';
COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_obrigatorio IS 'Atributo utilizado para indicar se esta informação é de captura obrigatorio para o tipo de documento associado.';
COMMENT ON COLUMN mtrtb045_atributo_extracao.no_atributo_sicli IS 'Atributo utilizado para definir o nome do atributo utilizado na atualização de dados do SICLI que tem relação com o atributo do documento.
Esse atributo deve ser utilizado em conjunto com o atributo "no_objeto_sicli" e "ic_tipo_sicli" que determina o agrupamento do atributo a ser enviado na mensagem de atualização.
Ex1:
no_objeto_sicli = renda/fontePagadora
no_atributo_sicli = nomeFantasia
ic_tipo_sicli = STRING

objeto enviado = {
    "renda" : {
        "fontePagadora" : {
            "nomeFantasia" : "XXXXXXXXXXXXXXX"
            ......................
        }
        ........................
    }
}


Ex2:
no_objeto_sicli = endereco
no_atributo_sicli = cep
ic_tipo_sicli = STRING

objeto enviado = {
    "endereco" : {
        ....................
        "cep" : "XXXXXXXXXXXXXXX"
    }
}';
COMMENT ON COLUMN mtrtb045_atributo_extracao.no_objeto_sicli IS 'Atributo utilizado para definir o objeto que agrupa o atributo utilizado na atualização de dados do SICLI que tem relação com o atributo do documento.
Esse atributo deve ser utilizado em conjunto com o atributo "no_atributo_sicli" e "ic_tipo_sicli" que determina o agrupamento do atributo a ser enviado na mensagem de atualização.
Ex1:
no_objeto_sicli = renda/fontePagadora
no_atributo_sicli = nomeFantasia
ic_tipo_sicli = STRING

objeto enviado = {
    "renda" : {
        "fontePagadora" : {
            "nomeFantasia" : "XXXXXXXXXXXXXXX"
            ........................
        }
        ......................
    }
}


Ex2:
no_objeto_sicli = endereco
no_atributo_sicli = cep
ic_tipo_sicli = STRING

objeto enviado = {
    "endereco" : {
        ...........
        "cep" : "XXXXXXXXXXXXXXX"
    }
}';
COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_tipo_sicli IS 'Atributo utilizado para indicar qual o tipo de atributo definido junto ao objeto a ser enviado na atualização de dados do SICLI. Pode assumir os seguintes valores:
- BOOLEAN
- STRING
- LONG
- DATE
- DECIMAL';
COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_tipo_geral IS 'Atributo utilizado para indicar qual o tipo de atributo definido para ações internas do SIMTR como geração de minutas, validação da informação, etc. Pode assumir os seguintes valores:
- BOOLEAN
- STRING
- LONG
- DATE
- DECIMAL';
COMMENT ON COLUMN mtrtb045_atributo_extracao.pc_alteracao_permitido IS 'Atributo que representa o percentual adicional de alteração permitido sobre o valor recebido do OCR, no formato inteiro.
Exemplo:
Considere que foi recebido do OCR um valor com 90% de assertividade.
1) Se o atributo permite alteração de até 6% do conteúdo recebido do OCR, o valor armazenado será 6, mas a aplicação deve permitir 16% de alteração (10% da margem de assertividade do OCR, mais 6% permitidos por este atributo);

2) Se o atributo permite alteração de até 10% do conteúdo recebido do OCR, o valor armazenado será 10, mas a aplicação deve permitir 20% de alteração (10% da margem de assertividade do OCR, mais 10% permitidos por este atributo);

O valor máximo para o atributo é 100. Caso a soma da margem de assertividade do OCR com o valor do atributo resulte num valor superior a 100, a aplicação deverá limitar, considerando que está permitido 100% de alteração do valor.';

--
-- TOC entry 1912 (class 1259 OID 2025825)
-- Name: mtrtb046_processo_adm; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb046_processo_adm (
    nu_processo_adm bigint DEFAULT nextval('mtrsq046_processo_adm'::regclass) NOT NULL,
    nu_versao integer NOT NULL,
    nu_processo integer NOT NULL,
    nu_ano_processo integer NOT NULL,
    nu_pregao integer,
    nu_unidade_contratacao integer,
    nu_ano_pregao integer,
    de_objeto_contratacao text NOT NULL,
    ts_inclusao timestamp without time zone NOT NULL,
    co_matricula character varying(7) NOT NULL,
    ts_finalizacao timestamp without time zone,
    co_matricula_finalizacao character varying(7),
    co_protocolo_siclg character varying(255),
    nu_unidade_demandante integer NOT NULL
);

COMMENT ON TABLE mtrtb046_processo_adm IS 'Tabela responsavel pelo armazenamento dos processos administrativo eletronicos formados quando a inicio de um fluxo de contratação.

Com base nesses processos podem ser criados registros de contratos ou apensos de penalidade baseados no andamento do processo.

O registro do processo é utilizado para realizar a vinculação de documentos eletronicos de forma a gerar um dossiê proprio para este fim.';
COMMENT ON COLUMN mtrtb046_processo_adm.nu_processo_adm IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb046_processo_adm.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
COMMENT ON COLUMN mtrtb046_processo_adm.nu_processo IS 'Atributo utilizado para armazenar o numero de identificação negocial do processo

Juntamente com o "nu_ano_processo", forma-se o numero de identificação negocial do processo.

Ex: 894-2017';
COMMENT ON COLUMN mtrtb046_processo_adm.nu_ano_processo IS 'Atributo utilizado para armazenar o ano de identificação negocial do processo.

Juntamente com o "nu_processo", forma-se o numero de identificação negocial do processo.

Ex: 894-2017';
COMMENT ON COLUMN mtrtb046_processo_adm.nu_pregao IS 'Atributo utilizado para armazenar o numero de identificação negocial do pregão originado pelo processo relativo ao registro.

Juntamente com o "nu_ano_pregao" e "nu_unidade_pregao", forma-se o numero de identificação negocial do pregão.

Ex: 912/7775-2017';
COMMENT ON COLUMN mtrtb046_processo_adm.nu_unidade_contratacao IS 'Atributo utilizado para armazenar a unidade CAIXA responsável pela execução do pregão originado pelo processo relativo ao registro.

Juntamente com o "nu_pregao" e "nu_ano_pregao, forma-se o numero de identificação negocial do pregão.

Ex: 912/7775-2017';
COMMENT ON COLUMN mtrtb046_processo_adm.nu_ano_pregao IS 'Atributo utilizado para armazenar o ano de identificação negocial do pregão originado pelo processo relativo ao registro.

Juntamente com o "nu_pregao" e "nu_unidade_pregao", forma-se o numero de identificação negocial do pregão.

Ex: 912/7775-2017';
COMMENT ON COLUMN mtrtb046_processo_adm.de_objeto_contratacao IS 'Atributo utilizado para armazenar uma descrição livre sobre o objeto de contratação relativo ao processo administrativo.

Em caso de integração com o SICLG, essa informação deve ser carregada deste sistema que armazena a identificação deste objeto.';
COMMENT ON COLUMN mtrtb046_processo_adm.ts_inclusao IS 'Atributo utilizado para armazenar a data/hora de inclusão do registro do processo administrativo.';
COMMENT ON COLUMN mtrtb046_processo_adm.co_matricula IS 'Atributo utilizado para armazenar a matricula do usuário do sistema responsável pela inclusão do registro do processo administrativo.';
COMMENT ON COLUMN mtrtb046_processo_adm.ts_finalizacao IS 'Atributo utilizado para armazenar a data/hora de finalização do registro do processo administrativo.

Após o registro ser finalizado, não deverá mais ser possivel realizar carga de documentos relacionados ao processo administrativo, incluido os contratos e apensos vinculados ao mesmo.';
COMMENT ON COLUMN mtrtb046_processo_adm.co_matricula_finalizacao IS 'Atributo utilizado para armazenar a matricula do usuário do sistema responsável pela finalização do registro do processo administrativo.

Após o registro ser finalizado, não deverá mais ser possivel realizar carga de documentos relacionados ao processo administrativo, incluido os contratos e apensos vinculados ao mesmo.';
COMMENT ON COLUMN mtrtb046_processo_adm.co_protocolo_siclg IS 'Atributo utilizado para armanezar o numero de protocolo atribuido pelo SICLG no momento da criação do fluxo do processo.

Esta informação servirá como identificação negocial do registro do processo apenas de carater informativo.';
COMMENT ON COLUMN mtrtb046_processo_adm.nu_unidade_demandante IS 'Atributo utilizado para armazenar a unidade CAIXA demaqndante da solicitação que originou o processo relativo ao registro.';

--
-- TOC entry 1913 (class 1259 OID 2025832)
-- Name: mtrtb047_contrato_adm; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb047_contrato_adm (
    nu_contrato_adm bigint DEFAULT nextval('mtrsq047_contrato_adm'::regclass) NOT NULL,
    nu_versao integer NOT NULL,
    nu_processo_adm bigint NOT NULL,
    nu_contrato integer NOT NULL,
    nu_ano_contrato integer NOT NULL,
    de_contrato text NOT NULL,
    ts_inclusao timestamp without time zone NOT NULL,
    co_matricula character varying(7) NOT NULL,
    nu_cpf_cnpj_fornecedor character varying(14) NOT NULL,
    nu_unidade_operacional integer NOT NULL
);

COMMENT ON TABLE mtrtb047_contrato_adm IS 'Tabela responsavel pelo armazenamento dos contratos eletronicos formados durante um fluxo de contratação.

Os contratos são relacionados com um processo e podem ser criados registros de apensos de penalidade ou ressarcimento baseados no andamento do contrato.

O registro do contrato é utilizado para realizar a vinculação de documentos eletronicos de forma a gerar um dossiê proprio para este fim.';
COMMENT ON COLUMN mtrtb047_contrato_adm.nu_contrato_adm IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb047_contrato_adm.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
COMMENT ON COLUMN mtrtb047_contrato_adm.nu_processo_adm IS 'Atributo que representa o processo administrativo de vinculação do contrato.';
COMMENT ON COLUMN mtrtb047_contrato_adm.nu_contrato IS 'Atributo utilizado para armazenar o numero de identificação negocial do contrato

Juntamente com o "nu_ano_contrato", forma-se o numero de identificação negocial do contrato.

Ex: 105/2018';
COMMENT ON COLUMN mtrtb047_contrato_adm.nu_ano_contrato IS 'Atributo utilizado para armazenar o ano de identificação negocial do contrato

Juntamente com o "nu_contrato", forma-se o numero de identificação negocial do contrato.

Ex: 105/2018';
COMMENT ON COLUMN mtrtb047_contrato_adm.de_contrato IS 'Atributo utilizado para armazenar uma descrição livre sobre o contrato';
COMMENT ON COLUMN mtrtb047_contrato_adm.ts_inclusao IS 'Atributo utilizado para armazenar a data/hora de inclusão do registro do contrato administrativo.';
COMMENT ON COLUMN mtrtb047_contrato_adm.co_matricula IS 'Atributo utilizado para armazenar a matricula do usuário do sistema responsável pela inclusão do registro do contrato administrativo.';
COMMENT ON COLUMN mtrtb047_contrato_adm.nu_cpf_cnpj_fornecedor IS 'Atributo utilizado para identificar o CPF/CNPJ do fornecedor vinculado ao contrato.

O atributo esta armazenado como texto para que seja armazenado o valor do atributo para os casos de CPF com 11 posições e nos casos de CNPJ com 14 posições';
COMMENT ON COLUMN mtrtb047_contrato_adm.nu_unidade_operacional IS 'Atributo utilizado para armazenar a unidade CAIXA responsável pela operacionalização do contrato.';

--
-- TOC entry 1914 (class 1259 OID 2025839)
-- Name: mtrtb048_apenso_adm; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb048_apenso_adm (
    nu_apenso_adm bigint DEFAULT nextval('mtrsq048_apenso_adm'::regclass) NOT NULL,
    nu_versao integer NOT NULL,
    nu_processo_adm bigint,
    nu_contrato_adm bigint,
    nu_cpf_cnpj_fornecedor character varying(14),
    ic_tipo_apenso character varying(2) NOT NULL,
    de_apenso text,
    ts_inclusao timestamp without time zone NOT NULL,
    co_matricula character varying(7) NOT NULL,
    co_protocolo_siclg character varying(255),
    no_titulo character varying(100)
);

COMMENT ON TABLE mtrtb048_apenso_adm IS 'Tabela responsavel pelo armazenamento dos apensos eletronicos formados durante um fluxo de contratação.

Os apensos são relacionados com um processo, neste caso representam as penalidade de processo, ou podem ser vinculados aos contratos, neste caso representam apensos de penalidade ou ressarcimento.

O registro do apenso é utilizado para realizar a vinculação de documentos eletronicos de forma a gerar um dossiê proprio para este fim.';
COMMENT ON COLUMN mtrtb048_apenso_adm.nu_apenso_adm IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb048_apenso_adm.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
COMMENT ON COLUMN mtrtb048_apenso_adm.nu_processo_adm IS 'Atributo que representa o processo administrativo vinculado ao apenso.

Quando este atributo esta preenchido o atributo que representa a vinculação com o contrato deve estar nulo.';
COMMENT ON COLUMN mtrtb048_apenso_adm.nu_contrato_adm IS 'Atributo que representa o contrato administrativo vinculado ao apenso.

Quando este atributo esta preenchido o atributo que representa a vinculação com o processo deve estar nulo.';
COMMENT ON COLUMN mtrtb048_apenso_adm.nu_cpf_cnpj_fornecedor IS 'Atributo utilizado para identificar o CPF/CNPJ do fornecedor vinculado ao apenso.

Este atributo deve ser preenchido apenas quando o apenso esta vinculado ao processo, pois quando vinculado a algum contrato, o CPF/CNPJ relativo ao contrato já esta identificado no registro do mesmo.

Para os casos de processo, não existe uma definição da pessoa relacionada pois processo pode ainda estar na fase de identificação e validação dos participanetes, mas em alguns casos torna-se necessario aplicar uma penalidade a algum desses participantes.

O atributo esta armazenado como texto para que seja armazenado o valor do atributo para os casos de CPF com 11 posições e nos casos de CNPJ com 14 posições';
COMMENT ON COLUMN mtrtb048_apenso_adm.ic_tipo_apenso IS 'Atributo utilizado para identificar o tipo de apenso sob o seguinte dominio:

RC - Ressarcimento de Contrato
PC - Penalidade de Contrato
PP - Penalidade de Processo

Este atributo só deve ser identificado como PP quando a chave estrangeiro da relação com o processo administrativo esta preenchida. Para os casos em que o apenso esteja relacionado com o contrato, este atributo deverá possuir os demais valores.';
COMMENT ON COLUMN mtrtb048_apenso_adm.de_apenso IS 'Atributo utilizado para armazenar uma descrição livre do apenso.';
COMMENT ON COLUMN mtrtb048_apenso_adm.ts_inclusao IS 'Atributo utilizado para armazenar a data/hora de inclusão do registro do apenso administrativo.';
COMMENT ON COLUMN mtrtb048_apenso_adm.co_matricula IS 'Atributo utilizado para armazenar a matricula do usuário do sistema responsável pela inclusão do registro do apenso administrativo.';
COMMENT ON COLUMN mtrtb048_apenso_adm.co_protocolo_siclg IS 'Atributo utilizado para armanezar o numero de protocolo atribuido pelo SICLG no momento da criação do fluxo do apenso.

Esta informação servirá como identificação negocial do registro do apenso.';
COMMENT ON COLUMN mtrtb048_apenso_adm.no_titulo IS 'Atributo utilizado para permitir a identificação nomeada de um apenso.

Este atributo deverá armazenar um valor unico para apensos vinculados a um mesmo contratoc ou processo, ou seja, uma chave de unicidade existente entre o numero do processo, numero do contrato e titulo do apenso, pois sempre que houver vinculo de um apenso com o processo, não haverá com o contrato e vice versa.';

--
-- TOC entry 1915 (class 1259 OID 2025846)
-- Name: mtrtb049_documento_adm; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb049_documento_adm (
    nu_documento_adm bigint DEFAULT nextval('mtrsq049_documento_adm'::regclass) NOT NULL,
    nu_versao integer NOT NULL,
    nu_documento bigint NOT NULL,
    nu_documento_substituto bigint,
    nu_processo_adm bigint,
    nu_contrato_adm bigint,
    nu_apenso_adm bigint,
    ic_valido boolean NOT NULL,
    ic_confidencial boolean NOT NULL,
    de_documento_adm text,
    de_justificativa_substituicao text,
    ts_exclusao timestamp without time zone,
    co_matricula_exclusao character varying(7),
    de_justificativa_exclusao text
);

COMMENT ON TABLE mtrtb049_documento_adm IS 'Tabela responsavel pelo armazenamento dos metadados dos documentos relacionados ao processo administrativo eletronico.

Esses documentos podem estar associados a um processo, contrato ou apenso.

Nesta tabela serão armazenadas as indicações de documento valido, confidencial e a indicação de qual documento de substituição do original invalidando o mesmo.';
COMMENT ON COLUMN mtrtb049_documento_adm.nu_documento_adm IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb049_documento_adm.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
COMMENT ON COLUMN mtrtb049_documento_adm.nu_documento IS 'Atributo utilizado para relacionar o documento que foi carregado na estrutura do ECM com a estrutura relacionada a um processo, contrato ou apenso';
COMMENT ON COLUMN mtrtb049_documento_adm.nu_documento_substituto IS 'Atributo utilizado para relacionar o documento que invalidou o documento original representado no atributo "nu_documento" de forma a substitui-lo';
COMMENT ON COLUMN mtrtb049_documento_adm.nu_processo_adm IS 'Atributo que representa o processo administrativo de vinculação do documento carregado.

Caso este atributo esteja definido os atributos que representam o contrato e o apenso deverão estar com os valores nulos.';
COMMENT ON COLUMN mtrtb049_documento_adm.nu_contrato_adm IS 'Atributo que representa o contrato administrativo de vinculação do documento carregado.

Caso este atributo esteja definido os atributos que representam o processo e o apenso deverão estar com os valores nulos.';
COMMENT ON COLUMN mtrtb049_documento_adm.nu_apenso_adm IS 'Atributo que representa o apenso administrativo de vinculação do documento carregado.

Caso este atributo esteja definido os atributos que representam o processo e o contrato deverão estar com os valores nulos.';
COMMENT ON COLUMN mtrtb049_documento_adm.ic_valido IS 'Atributo utilizdo para indicar que o documento esta valido e não foi substituido por nenhum outro documento.';
COMMENT ON COLUMN mtrtb049_documento_adm.ic_confidencial IS 'Atributo utilizado para identificar que o documento tem cunho confidencial.

Nestes casos, só deverá ser possivel realizar o download ou visualização do documento usuários que possuam perfil especifico para este fim.';
COMMENT ON COLUMN mtrtb049_documento_adm.de_documento_adm IS 'Atributo utilizado para armazenar uma descrição para identificação e pesquisa.';
COMMENT ON COLUMN mtrtb049_documento_adm.de_justificativa_substituicao IS 'Atributo utilizado para armazenar a justificativa utilizada quando da substituição de um documento adminstrativo.

Este campo deverá estar preenchi sempre que o atributo nu_documento_substituto estiver definido.';
COMMENT ON COLUMN mtrtb049_documento_adm.ts_exclusao IS 'Atributo utilizado para registrar a data e hora de execução da ação de exclusão logica do documento administrativo';
COMMENT ON COLUMN mtrtb049_documento_adm.co_matricula_exclusao IS 'Atributo utilizado para registrar a matricula do usuário que executou a ação de exclusão logica do documento administrativo

Este campo deverá estar preenchido sempre que o atributo ts_exclusao estiver definido.';
COMMENT ON COLUMN mtrtb049_documento_adm.de_justificativa_exclusao IS 'Atributo utilizado para armazenar a justificativa utilizada quando da exclusão logica de um documento adminstrativo.

Este campo deverá estar preenchido sempre que o atributo ts_exclusao estiver definido.';

--
-- TOC entry 1916 (class 1259 OID 2025853)
-- Name: mtrtb102_autorizacao_negada; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb102_autorizacao_negada (
    nu_autorizacao_negada bigint DEFAULT nextval('mtrsq102_autorizacao_negada'::regclass) NOT NULL,
    nu_versao integer NOT NULL,
    ts_registro timestamp without time zone NOT NULL,
    nu_operacao integer NOT NULL,
    nu_modalidade integer NOT NULL,
    no_produto character varying(255) NOT NULL,
    nu_cpf_cnpj bigint NOT NULL,
    ic_tipo_pessoa character varying(1) NOT NULL,
    sg_canal_solicitacao character varying(10) NOT NULL,
    de_motivo text NOT NULL
);

COMMENT ON TABLE mtrtb102_autorizacao_negada IS 'Tabela utilizada para armazenar as solicitações de autorizações relacionadas ao nivel documental que foram negadas.';
COMMENT ON COLUMN mtrtb102_autorizacao_negada.nu_autorizacao_negada IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb102_autorizacao_negada.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
COMMENT ON COLUMN mtrtb102_autorizacao_negada.ts_registro IS 'Atributo utilizado para armazenar a datae hora de recebimento da solicitação de autorização ';
COMMENT ON COLUMN mtrtb102_autorizacao_negada.nu_operacao IS 'Atributo utilizado para armazenar o codigo de operação do produto solicitado na autorização.';
COMMENT ON COLUMN mtrtb102_autorizacao_negada.nu_modalidade IS 'Atributo utilizado para armazenar o codigo da modalidade do produto solicitado na autorização.';
COMMENT ON COLUMN mtrtb102_autorizacao_negada.no_produto IS 'Atributo utilizado para armazenar o nome do produto solicitado na autorização.';
COMMENT ON COLUMN mtrtb102_autorizacao_negada.nu_cpf_cnpj IS 'Atributo utilizado para armazenar o numero do CPF/CNPJ do cliente vinculado a autorização fornecida.';
COMMENT ON COLUMN mtrtb102_autorizacao_negada.ic_tipo_pessoa IS 'Atributo utilizado para indicar o tipo de pessoa, se fisica ou juridica podendo assumir os seguintes valores:
F - Fisica
J - Juridica';
COMMENT ON COLUMN mtrtb102_autorizacao_negada.sg_canal_solicitacao IS 'Sigla de identificação do canal/sistema solicitante da autorização.
Informação é obtida pela comparação do codigo de integração enviado com o registro da tabela 006';
COMMENT ON COLUMN mtrtb102_autorizacao_negada.de_motivo IS 'Atributo utilizado para armazenar a descrição do motivo para a negativa.';

--
-- TOC entry 1917 (class 1259 OID 2025860)
-- Name: mtrtb103_autorizacao_orientacao; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb103_autorizacao_orientacao (
    nu_autorizacao_orientacao bigint DEFAULT nextval('mtrsq103_autorizacao_orientacao'::regclass) NOT NULL,
    nu_versao integer NOT NULL,
    nu_autorizacao bigint NOT NULL,
    ic_sistema character varying(10) NOT NULL,
    de_orientacao text NOT NULL
);

COMMENT ON TABLE mtrtb103_autorizacao_orientacao IS 'Tabela utilizada para armazenar as orientações encaminhadas conforme comportamentos de pesquisa definido ao momento da autorização de forma a permitir recuperar todo a informação referente a autorização fornecida.';
COMMENT ON COLUMN mtrtb103_autorizacao_orientacao.nu_autorizacao_orientacao IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb103_autorizacao_orientacao.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
COMMENT ON COLUMN mtrtb103_autorizacao_orientacao.nu_autorizacao IS 'Atributo utilizado para vincular a orientação a autorização concedida.
Quando este atributo estiver definido, o atributo nu_autorizacao_negada deverá ser nulo.';
COMMENT ON COLUMN mtrtb103_autorizacao_orientacao.ic_sistema IS 'Atributo utilizado para identificar o sistema de realização da pesquisa cadastral que originou a orientação a epoca do fornecimento da autorização.';
COMMENT ON COLUMN mtrtb103_autorizacao_orientacao.de_orientacao IS 'Atributo utilizado para armazenar a orientação concedida a época do fornecimento da autorização.';

--
-- TOC entry 1918 (class 1259 OID 2025867)
-- Name: mtrtb200_sicli_erro; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE mtrtb200_sicli_erro (
    nu_sicli_erro bigint DEFAULT nextval('mtrsq200_sicli_erro'::regclass) NOT NULL,
    co_matricula character varying(7) NOT NULL,
    ip_usuario character varying(15) NOT NULL,
    dns_usuario character varying(70) NOT NULL,
    ts_erro timestamp without time zone NOT NULL,
    co_identificacao bigint NOT NULL,
    ic_tipo_pessoa character varying(1) NOT NULL,
    de_erro text NOT NULL,
    nu_versao integer NOT NULL
);

COMMENT ON TABLE mtrtb200_sicli_erro IS 'Tabela utilizada para armazenar os erros de comunicação e resposta do SECLI';
COMMENT ON COLUMN mtrtb200_sicli_erro.nu_sicli_erro IS 'Atributo que representa a chave primária da entidade.';
COMMENT ON COLUMN mtrtb200_sicli_erro.co_matricula IS 'Atributo que representa a matricula do usuário que realizou a pesquisa';
COMMENT ON COLUMN mtrtb200_sicli_erro.ip_usuario IS 'Atributo que representa o IP da máquina do usuário que solicitou a pesquisa';
COMMENT ON COLUMN mtrtb200_sicli_erro.dns_usuario IS 'Atributo que representa o DNS da máquina do usuário que solicitou a pesquisa';
COMMENT ON COLUMN mtrtb200_sicli_erro.ts_erro IS 'Atributo utilizado para registar a data hora(com segundos) da pesquisa realizada';
COMMENT ON COLUMN mtrtb200_sicli_erro.co_identificacao IS 'Atributo utilizado para referenciar o número de identificação do cliente pesquisado no SICLI';
COMMENT ON COLUMN mtrtb200_sicli_erro.ic_tipo_pessoa IS 'Atributo que determina qual tipo de pessoa possa ter.
Pode assumir os seguintes valores:
F - Física
J - Jurídica
C - Cocli';
COMMENT ON COLUMN mtrtb200_sicli_erro.de_erro IS 'Atributo para descrever os detalhes do erro ocorrido';

--
-- TOC entry 1919 (class 1259 OID 2025874)
-- Name: schema_version; Type: TABLE; Schema: mtrsm001; Owner: -
--
CREATE TABLE schema_version (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE ONLY mtrtb001_dossie_cliente ALTER COLUMN nu_dossie_cliente SET DEFAULT nextval('mtrsq001_dossie_cliente'::regclass);
ALTER TABLE ONLY mtrtb002_dossie_produto ALTER COLUMN nu_dossie_produto SET DEFAULT nextval('mtrsq002_dossie_produto'::regclass);
ALTER TABLE ONLY mtrtb003_documento ALTER COLUMN nu_documento SET DEFAULT nextval('mtrsq003_documento'::regclass);
ALTER TABLE ONLY mtrtb006_canal ALTER COLUMN nu_canal SET DEFAULT nextval('mtrsq006_canal'::regclass);
ALTER TABLE ONLY mtrtb007_atributo_documento ALTER COLUMN nu_atributo_documento SET DEFAULT nextval('mtrsq007_atributo_documento'::regclass);
ALTER TABLE ONLY mtrtb009_tipo_documento ALTER COLUMN nu_tipo_documento SET DEFAULT nextval('mtrsq009_tipo_documento'::regclass);
ALTER TABLE ONLY mtrtb010_funcao_documental ALTER COLUMN nu_funcao_documental SET DEFAULT nextval('mtrsq010_funcao_documental'::regclass);
ALTER TABLE ONLY mtrtb012_tipo_situacao_dossie ALTER COLUMN nu_tipo_situacao_dossie SET DEFAULT nextval('mtrsq012_tipo_situacao_dossie'::regclass);
ALTER TABLE ONLY mtrtb013_situacao_dossie ALTER COLUMN nu_situacao_dossie SET DEFAULT nextval('mtrsq013_situacao_dossie'::regclass);
ALTER TABLE ONLY mtrtb014_instancia_documento ALTER COLUMN nu_instancia_documento SET DEFAULT nextval('mtrsq014_instancia_documento'::regclass);
ALTER TABLE ONLY mtrtb015_situacao_documento ALTER COLUMN nu_situacao_documento SET DEFAULT nextval('mtrsq015_situacao_documento'::regclass);
ALTER TABLE ONLY mtrtb016_motivo_stco_documento ALTER COLUMN nu_motivo_stco_documento SET DEFAULT nextval('mtrsq016_motivo_stco_documento'::regclass);
ALTER TABLE ONLY mtrtb020_processo ALTER COLUMN nu_processo SET DEFAULT nextval('mtrsq020_processo'::regclass);
ALTER TABLE ONLY mtrtb021_unidade_autorizada ALTER COLUMN nu_unidade_autorizada SET DEFAULT nextval('mtrsq021_unidade_autorizada'::regclass);
ALTER TABLE ONLY mtrtb022_produto ALTER COLUMN nu_produto SET DEFAULT nextval('mtrsq022_produto'::regclass);
ALTER TABLE ONLY mtrtb024_produto_dossie ALTER COLUMN nu_produto_dossie SET DEFAULT nextval('mtrsq024_produto_dossie'::regclass);
ALTER TABLE ONLY mtrtb025_processo_documento ALTER COLUMN nu_processo_documento SET DEFAULT nextval('mtrsq025_processo_documento'::regclass);
ALTER TABLE ONLY mtrtb027_campo_entrada ALTER COLUMN nu_campo_entrada SET DEFAULT nextval('mtrsq027_campo_entrada'::regclass);
ALTER TABLE ONLY mtrtb028_opcao_campo ALTER COLUMN nu_opcao_campo SET DEFAULT nextval('mtrsq028_opcao_campo'::regclass);
ALTER TABLE ONLY mtrtb029_campo_apresentacao ALTER COLUMN nu_campo_apresentacao SET DEFAULT nextval('mtrsq029_campo_apresentacao'::regclass);
ALTER TABLE ONLY mtrtb030_resposta_dossie ALTER COLUMN nu_resposta_dossie SET DEFAULT nextval('mtrsq030_resposta_dossie'::regclass);
ALTER TABLE ONLY mtrtb033_garantia ALTER COLUMN nu_garantia SET DEFAULT nextval('mtrsq033_garantia'::regclass);
ALTER TABLE ONLY mtrtb035_garantia_informada ALTER COLUMN nu_garantia_informada SET DEFAULT nextval('mtrsq035_garantia_informada'::regclass);
ALTER TABLE ONLY mtrtb036_composicao_documental ALTER COLUMN nu_composicao_documental SET DEFAULT nextval('mtrsq036_composicao_documental'::regclass);
ALTER TABLE ONLY mtrtb037_regra_documental ALTER COLUMN nu_regra_documental SET DEFAULT nextval('mtrsq037_regra_documental'::regclass);
ALTER TABLE ONLY mtrtb100_autorizacao ALTER COLUMN nu_autorizacao SET DEFAULT nextval('mtrsq100_autorizacao'::regclass);
ALTER TABLE ONLY mtrtb101_documento ALTER COLUMN nu_documento SET DEFAULT nextval('mtrsq101_documento'::regclass);


--
-- TOC entry 8043 (class 0 OID 0)
-- Dependencies: 1824
-- Name: mtrsq001_dossie_cliente; Type: SEQUENCE SET; Schema: mtrsm001; Owner: -
--
SELECT pg_catalog.setval('mtrsq001_dossie_cliente', 1, true);
SELECT pg_catalog.setval('mtrsq002_dossie_produto', 1, true);
SELECT pg_catalog.setval('mtrsq003_documento', 1, true);
SELECT pg_catalog.setval('mtrsq004_dossie_cliente_produto', 1, true);
SELECT pg_catalog.setval('mtrsq006_canal', 1, true);
SELECT pg_catalog.setval('mtrsq007_atributo_documento', 1, true);
SELECT pg_catalog.setval('mtrsq009_tipo_documento', 1, true);
SELECT pg_catalog.setval('mtrsq010_funcao_documental', 1, true);
SELECT pg_catalog.setval('mtrsq012_tipo_situacao_dossie', 1, true);
SELECT pg_catalog.setval('mtrsq013_situacao_dossie', 1, true);
SELECT pg_catalog.setval('mtrsq014_instancia_documento', 1, true);
SELECT pg_catalog.setval('mtrsq015_situacao_documento', 1, true);
SELECT pg_catalog.setval('mtrsq016_motivo_stco_documento', 1, true);
SELECT pg_catalog.setval('mtrsq017_situacao_instnca_dcmnto', 1, true);
SELECT pg_catalog.setval('mtrsq019_campo_formulario', 1, false);
SELECT pg_catalog.setval('mtrsq020_processo', 1, true);
SELECT pg_catalog.setval('mtrsq021_unidade_autorizada', 1, true);
SELECT pg_catalog.setval('mtrsq022_produto', 1, true);
SELECT pg_catalog.setval('mtrsq024_produto_dossie', 1, true);
SELECT pg_catalog.setval('mtrsq025_processo_documento', 1, true);
SELECT pg_catalog.setval('mtrsq027_campo_entrada', 1, true);
SELECT pg_catalog.setval('mtrsq028_opcao_campo', 1, true);
SELECT pg_catalog.setval('mtrsq029_campo_apresentacao', 1, false);
SELECT pg_catalog.setval('mtrsq030_resposta_dossie', 1, true);
SELECT pg_catalog.setval('mtrsq032_elemento_conteudo', 1, true);
SELECT pg_catalog.setval('mtrsq033_garantia', 1, true);
SELECT pg_catalog.setval('mtrsq035_garantia_informada', 1, true);
SELECT pg_catalog.setval('mtrsq036_composicao_documental', 1, true);
SELECT pg_catalog.setval('mtrsq037_regra_documental', 1, true);
SELECT pg_catalog.setval('mtrsq043_documento_garantia', 1, true);
SELECT pg_catalog.setval('mtrsq044_comportamento_pesquisa', 1, true);
SELECT pg_catalog.setval('mtrsq045_atributo_extracao', 1, true);
SELECT pg_catalog.setval('mtrsq046_processo_adm', 1, true);
SELECT pg_catalog.setval('mtrsq047_contrato_adm', 1, true);
SELECT pg_catalog.setval('mtrsq048_apenso_adm', 1, true);
SELECT pg_catalog.setval('mtrsq049_documento_adm', 1, false);
SELECT pg_catalog.setval('mtrsq100_autorizacao', 1, true);
SELECT pg_catalog.setval('mtrsq101_documento', 1, true);
SELECT pg_catalog.setval('mtrsq102_autorizacao_negada', 1, true);
SELECT pg_catalog.setval('mtrsq103_autorizacao_orientacao', 1, true);
SELECT pg_catalog.setval('mtrsq200_sicli_erro', 1, true);

--
-- TOC entry 6953 (class 2606 OID 2025909)
-- Name: pk_mtrtb001; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--
ALTER TABLE ONLY mtrtb001_dossie_cliente
    ADD CONSTRAINT pk_mtrtb001 PRIMARY KEY (nu_dossie_cliente);

ALTER TABLE ONLY mtrtb001_pessoa_fisica
    ADD CONSTRAINT pk_mtrtb001_pessoa_fisica PRIMARY KEY (nu_dossie_cliente);

ALTER TABLE ONLY mtrtb001_pessoa_juridica
    ADD CONSTRAINT pk_mtrtb001_pessoa_juridica PRIMARY KEY (nu_dossie_cliente);

ALTER TABLE ONLY mtrtb002_dossie_produto
    ADD CONSTRAINT pk_mtrtb002 PRIMARY KEY (nu_dossie_produto);

ALTER TABLE ONLY mtrtb003_documento
    ADD CONSTRAINT pk_mtrtb003 PRIMARY KEY (nu_documento);

ALTER TABLE ONLY mtrtb004_dossie_cliente_produto
    ADD CONSTRAINT pk_mtrtb004 PRIMARY KEY (nu_dossie_cliente_produto);

ALTER TABLE ONLY mtrtb005_documento_cliente
    ADD CONSTRAINT pk_mtrtb005 PRIMARY KEY (nu_documento, nu_dossie_cliente);

ALTER TABLE ONLY mtrtb006_canal
    ADD CONSTRAINT pk_mtrtb006 PRIMARY KEY (nu_canal);

ALTER TABLE ONLY mtrtb007_atributo_documento
    ADD CONSTRAINT pk_mtrtb007 PRIMARY KEY (nu_atributo_documento);

ALTER TABLE ONLY mtrtb009_tipo_documento
    ADD CONSTRAINT pk_mtrtb009 PRIMARY KEY (nu_tipo_documento);

ALTER TABLE ONLY mtrtb010_funcao_documental
    ADD CONSTRAINT pk_mtrtb010 PRIMARY KEY (nu_funcao_documental);

ALTER TABLE ONLY mtrtb011_funcao_documento
    ADD CONSTRAINT pk_mtrtb011 PRIMARY KEY (nu_tipo_documento, nu_funcao_documental);

ALTER TABLE ONLY mtrtb012_tipo_situacao_dossie
    ADD CONSTRAINT pk_mtrtb012 PRIMARY KEY (nu_tipo_situacao_dossie);

ALTER TABLE ONLY mtrtb013_situacao_dossie
    ADD CONSTRAINT pk_mtrtb013 PRIMARY KEY (nu_situacao_dossie);

ALTER TABLE ONLY mtrtb014_instancia_documento
    ADD CONSTRAINT pk_mtrtb014 PRIMARY KEY (nu_instancia_documento);

ALTER TABLE ONLY mtrtb015_situacao_documento
    ADD CONSTRAINT pk_mtrtb015 PRIMARY KEY (nu_situacao_documento);

ALTER TABLE ONLY mtrtb016_motivo_stco_documento
    ADD CONSTRAINT pk_mtrtb016 PRIMARY KEY (nu_motivo_stco_documento);

ALTER TABLE ONLY mtrtb017_stco_instnca_documento
    ADD CONSTRAINT pk_mtrtb017 PRIMARY KEY (nu_stco_instnca_documento);

ALTER TABLE ONLY mtrtb018_unidade_tratamento
    ADD CONSTRAINT pk_mtrtb018 PRIMARY KEY (nu_dossie_produto, nu_unidade);

ALTER TABLE ONLY mtrtb019_campo_formulario
    ADD CONSTRAINT pk_mtrtb019 PRIMARY KEY (nu_campo_formulario);

ALTER TABLE ONLY mtrtb020_processo
    ADD CONSTRAINT pk_mtrtb020 PRIMARY KEY (nu_processo);

ALTER TABLE ONLY mtrtb021_unidade_autorizada
    ADD CONSTRAINT pk_mtrtb021 PRIMARY KEY (nu_unidade_autorizada);

ALTER TABLE ONLY mtrtb022_produto
    ADD CONSTRAINT pk_mtrtb022 PRIMARY KEY (nu_produto);

ALTER TABLE ONLY mtrtb023_produto_processo
    ADD CONSTRAINT pk_mtrtb023 PRIMARY KEY (nu_processo, nu_produto);

ALTER TABLE ONLY mtrtb024_produto_dossie
    ADD CONSTRAINT pk_mtrtb024 PRIMARY KEY (nu_produto_dossie);

ALTER TABLE ONLY mtrtb025_processo_documento
    ADD CONSTRAINT pk_mtrtb025 PRIMARY KEY (nu_processo_documento);

ALTER TABLE ONLY mtrtb026_relacao_processo
    ADD CONSTRAINT pk_mtrtb026 PRIMARY KEY (nu_processo_pai, nu_processo_filho);

ALTER TABLE ONLY mtrtb027_campo_entrada
    ADD CONSTRAINT pk_mtrtb027 PRIMARY KEY (nu_campo_entrada);

ALTER TABLE ONLY mtrtb028_opcao_campo
    ADD CONSTRAINT pk_mtrtb028 PRIMARY KEY (nu_opcao_campo);

ALTER TABLE ONLY mtrtb029_campo_apresentacao
    ADD CONSTRAINT pk_mtrtb029 PRIMARY KEY (nu_campo_apresentacao);

ALTER TABLE ONLY mtrtb030_resposta_dossie
    ADD CONSTRAINT pk_mtrtb030 PRIMARY KEY (nu_resposta_dossie);

ALTER TABLE ONLY mtrtb031_resposta_opcao
    ADD CONSTRAINT pk_mtrtb031 PRIMARY KEY (nu_resposta_dossie, nu_opcao_campo);

ALTER TABLE ONLY mtrtb032_elemento_conteudo
    ADD CONSTRAINT pk_mtrtb032 PRIMARY KEY (nu_elemento_conteudo);

ALTER TABLE ONLY mtrtb033_garantia
    ADD CONSTRAINT pk_mtrtb033 PRIMARY KEY (nu_garantia);

ALTER TABLE ONLY mtrtb034_garantia_produto
    ADD CONSTRAINT pk_mtrtb034 PRIMARY KEY (nu_produto, nu_garantia);

ALTER TABLE ONLY mtrtb035_garantia_informada
    ADD CONSTRAINT pk_mtrtb035 PRIMARY KEY (nu_garantia_informada);

ALTER TABLE ONLY mtrtb036_composicao_documental
    ADD CONSTRAINT pk_mtrtb036 PRIMARY KEY (nu_composicao_documental);

ALTER TABLE ONLY mtrtb037_regra_documental
    ADD CONSTRAINT pk_mtrtb037 PRIMARY KEY (nu_regra_documental);

ALTER TABLE ONLY mtrtb038_nivel_documental
    ADD CONSTRAINT pk_mtrtb038 PRIMARY KEY (nu_composicao_documental, nu_dossie_cliente);

ALTER TABLE ONLY mtrtb039_produto_composicao
    ADD CONSTRAINT pk_mtrtb039 PRIMARY KEY (nu_composicao_documental, nu_produto);

ALTER TABLE ONLY mtrtb040_cadeia_tipo_sto_dossie
    ADD CONSTRAINT pk_mtrtb040 PRIMARY KEY (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte);

ALTER TABLE ONLY mtrtb041_cadeia_stco_documento
    ADD CONSTRAINT pk_mtrtb041 PRIMARY KEY (nu_situacao_documento_atual, nu_situacao_documento_seguinte);

ALTER TABLE ONLY mtrtb042_cliente_garantia
    ADD CONSTRAINT pk_mtrtb042 PRIMARY KEY (nu_garantia_informada, nu_dossie_cliente);

ALTER TABLE ONLY mtrtb043_documento_garantia
    ADD CONSTRAINT pk_mtrtb043 PRIMARY KEY (nu_documento_garantia);

ALTER TABLE ONLY mtrtb044_comportamento_pesquisa
    ADD CONSTRAINT pk_mtrtb044 PRIMARY KEY (nu_comportamento_pesquisa);

ALTER TABLE ONLY mtrtb045_atributo_extracao
    ADD CONSTRAINT pk_mtrtb045 PRIMARY KEY (nu_atributo_extracao);

ALTER TABLE ONLY mtrtb046_processo_adm
    ADD CONSTRAINT pk_mtrtb046 PRIMARY KEY (nu_processo_adm);

ALTER TABLE ONLY mtrtb047_contrato_adm
    ADD CONSTRAINT pk_mtrtb047 PRIMARY KEY (nu_contrato_adm);

ALTER TABLE ONLY mtrtb048_apenso_adm
    ADD CONSTRAINT pk_mtrtb048 PRIMARY KEY (nu_apenso_adm);

ALTER TABLE ONLY mtrtb049_documento_adm
    ADD CONSTRAINT pk_mtrtb049 PRIMARY KEY (nu_documento_adm);

ALTER TABLE ONLY mtrtb100_autorizacao
    ADD CONSTRAINT pk_mtrtb100 PRIMARY KEY (nu_autorizacao);

ALTER TABLE ONLY mtrtb101_documento
    ADD CONSTRAINT pk_mtrtb101 PRIMARY KEY (nu_documento);

ALTER TABLE ONLY mtrtb102_autorizacao_negada
    ADD CONSTRAINT pk_mtrtb102 PRIMARY KEY (nu_autorizacao_negada);

ALTER TABLE ONLY mtrtb103_autorizacao_orientacao
    ADD CONSTRAINT pk_mtrtb103 PRIMARY KEY (nu_autorizacao_orientacao);

ALTER TABLE ONLY mtrtb200_sicli_erro
    ADD CONSTRAINT pk_mtrtb200 PRIMARY KEY (nu_sicli_erro);

ALTER TABLE ONLY schema_version
    ADD CONSTRAINT schema_version_pk PRIMARY KEY (installed_rank);

--
-- TOC entry 6951 (class 1259 OID 2026020)
-- Name: ix_mtrtb001_01; Type: INDEX; Schema: mtrsm001; Owner: -
--
CREATE UNIQUE INDEX ix_mtrtb001_01 ON mtrtb001_dossie_cliente USING btree (nu_cpf_cnpj);
CREATE UNIQUE INDEX ix_mtrtb006_01 ON mtrtb006_canal USING btree (co_integracao);
CREATE UNIQUE INDEX ix_mtrtb009_01 ON mtrtb009_tipo_documento USING btree (no_tipo_documento, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, ic_validacao_cadastral);
CREATE UNIQUE INDEX ix_mtrtb009_02 ON mtrtb009_tipo_documento USING btree (co_tipologia);
CREATE UNIQUE INDEX ix_mtrtb010_01 ON mtrtb010_funcao_documental USING btree (no_funcao);
CREATE UNIQUE INDEX ix_mtrtb012_01 ON mtrtb012_tipo_situacao_dossie USING btree (no_tipo_situacao);
CREATE UNIQUE INDEX ix_mtrtb014_01 ON mtrtb014_instancia_documento USING btree (nu_documento, nu_dossie_produto);
CREATE UNIQUE INDEX ix_mtrtb015_01 ON mtrtb015_situacao_documento USING btree (no_situacao);
CREATE UNIQUE INDEX ix_mtrtb016_01 ON mtrtb016_motivo_stco_documento USING btree (nu_situacao_documento, no_motivo_stco_documento);
CREATE UNIQUE INDEX ix_mtrtb019_01 ON mtrtb019_campo_formulario USING btree (nu_processo, nu_ordem);
CREATE UNIQUE INDEX ix_mtrtb020_01 ON mtrtb020_processo USING btree (no_processo);
CREATE UNIQUE INDEX ix_mtrtb022_01 ON mtrtb022_produto USING btree (nu_operacao, nu_modalidade);
CREATE UNIQUE INDEX ix_mtrtb026_01 ON mtrtb026_relacao_processo USING btree (nu_processo_pai, nu_ordem);
CREATE UNIQUE INDEX ix_mtrtb033_01 ON mtrtb033_garantia USING btree (nu_garantia_bacen);
CREATE UNIQUE INDEX ix_mtrtb043_01 ON mtrtb043_documento_garantia USING btree (nu_garantia, nu_processo);
CREATE UNIQUE INDEX ix_mtrtb044_01 ON mtrtb044_comportamento_pesquisa USING btree (nu_produto, ic_sistema_retorno, ic_codigo_retorno);
CREATE UNIQUE INDEX ix_mtrtb046_01 ON mtrtb046_processo_adm USING btree (nu_processo, nu_ano_processo);
CREATE UNIQUE INDEX ix_mtrtb046_02 ON mtrtb046_processo_adm USING btree (nu_pregao, nu_unidade_contratacao, nu_ano_pregao);
CREATE UNIQUE INDEX ix_mtrtb047_01 ON mtrtb047_contrato_adm USING btree (nu_contrato, nu_ano_contrato);
CREATE UNIQUE INDEX ix_mtrtb048_01 ON mtrtb048_apenso_adm USING btree (co_protocolo_siclg);
CREATE UNIQUE INDEX ix_mtrtb048_02 ON mtrtb048_apenso_adm USING btree (nu_processo_adm, no_titulo);
CREATE UNIQUE INDEX ix_mtrtb048_03 ON mtrtb048_apenso_adm USING btree (nu_contrato_adm, no_titulo);
CREATE UNIQUE INDEX ix_mtrtb100_01 ON mtrtb100_autorizacao USING btree (co_autorizacao);
CREATE INDEX schema_version_s_idx ON schema_version USING btree (success);


--
-- TOC entry 7121 (class 2606 OID 2026044)
-- Name: fk_mtrtb001_mtrtb001_01; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb001_pessoa_fisica
    ADD CONSTRAINT fk_mtrtb001_mtrtb001_01 FOREIGN KEY (nu_dossie_cliente) REFERENCES mtrtb001_dossie_cliente(nu_dossie_cliente) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb001_pessoa_juridica
    ADD CONSTRAINT fk_mtrtb001_mtrtb001_02 FOREIGN KEY (nu_dossie_cliente) REFERENCES mtrtb001_dossie_cliente(nu_dossie_cliente) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb002_dossie_produto
    ADD CONSTRAINT fk_mtrtb002_mtrtb020_01 FOREIGN KEY (nu_processo_dossie) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb002_dossie_produto
    ADD CONSTRAINT fk_mtrtb002_mtrtb020_02 FOREIGN KEY (nu_processo_fase) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb003_documento
    ADD CONSTRAINT fk_mtrtb003_mtrtb006 FOREIGN KEY (nu_canal_captura) REFERENCES mtrtb006_canal(nu_canal) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb003_documento
    ADD CONSTRAINT fk_mtrtb003_mtrtb009 FOREIGN KEY (nu_tipo_documento) REFERENCES mtrtb009_tipo_documento(nu_tipo_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb013_situacao_dossie
    ADD CONSTRAINT fk_mtrtb003_mtrtb009 FOREIGN KEY (nu_tipo_situacao_dossie) REFERENCES mtrtb012_tipo_situacao_dossie(nu_tipo_situacao_dossie) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb017_stco_instnca_documento
    ADD CONSTRAINT fk_mtrtb003_mtrtb009 FOREIGN KEY (nu_situacao_documento) REFERENCES mtrtb015_situacao_documento(nu_situacao_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb004_dossie_cliente_produto
    ADD CONSTRAINT fk_mtrtb004_mtrtb001 FOREIGN KEY (nu_dossie_cliente) REFERENCES mtrtb001_dossie_cliente(nu_dossie_cliente) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb004_dossie_cliente_produto
    ADD CONSTRAINT fk_mtrtb004_mtrtb001_02 FOREIGN KEY (nu_dossie_cliente_relacionado) REFERENCES mtrtb001_dossie_cliente(nu_dossie_cliente) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb004_dossie_cliente_produto
    ADD CONSTRAINT fk_mtrtb004_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb005_documento_cliente
    ADD CONSTRAINT fk_mtrtb005_mtrtb001 FOREIGN KEY (nu_dossie_cliente) REFERENCES mtrtb001_dossie_cliente(nu_dossie_cliente) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb005_documento_cliente
    ADD CONSTRAINT fk_mtrtb005_mtrtb003 FOREIGN KEY (nu_documento) REFERENCES mtrtb003_documento(nu_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb007_atributo_documento
    ADD CONSTRAINT fk_mtrtb007_mtrtb003 FOREIGN KEY (nu_documento) REFERENCES mtrtb003_documento(nu_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb011_funcao_documento
    ADD CONSTRAINT fk_mtrtb011_mtrtb009 FOREIGN KEY (nu_tipo_documento) REFERENCES mtrtb009_tipo_documento(nu_tipo_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb011_funcao_documento
    ADD CONSTRAINT fk_mtrtb011_mtrtb010 FOREIGN KEY (nu_funcao_documental) REFERENCES mtrtb010_funcao_documental(nu_funcao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb013_situacao_dossie
    ADD CONSTRAINT fk_mtrtb013_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb014_instancia_documento
    ADD CONSTRAINT fk_mtrtb014_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb014_instancia_documento
    ADD CONSTRAINT fk_mtrtb014_mtrtb003 FOREIGN KEY (nu_documento) REFERENCES mtrtb003_documento(nu_documento) ON UPDATE RESTRICT ON DELETE CASCADE;

ALTER TABLE ONLY mtrtb014_instancia_documento
    ADD CONSTRAINT fk_mtrtb014_mtrtb004 FOREIGN KEY (nu_dossie_cliente_produto) REFERENCES mtrtb004_dossie_cliente_produto(nu_dossie_cliente_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb014_instancia_documento
    ADD CONSTRAINT fk_mtrtb014_mtrtb032 FOREIGN KEY (nu_elemento_conteudo) REFERENCES mtrtb032_elemento_conteudo(nu_elemento_conteudo) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb014_instancia_documento
    ADD CONSTRAINT fk_mtrtb014_mtrtb035 FOREIGN KEY (nu_garantia_informada) REFERENCES mtrtb035_garantia_informada(nu_garantia_informada) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb016_motivo_stco_documento
    ADD CONSTRAINT fk_mtrtb016_mtrtb015 FOREIGN KEY (nu_situacao_documento) REFERENCES mtrtb015_situacao_documento(nu_situacao_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb017_stco_instnca_documento
    ADD CONSTRAINT fk_mtrtb017_mtrtb014 FOREIGN KEY (nu_instancia_documento) REFERENCES mtrtb014_instancia_documento(nu_instancia_documento) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY mtrtb017_stco_instnca_documento
    ADD CONSTRAINT fk_mtrtb017_mtrtb016 FOREIGN KEY (nu_motivo_stco_documento) REFERENCES mtrtb016_motivo_stco_documento(nu_motivo_stco_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb018_unidade_tratamento
    ADD CONSTRAINT fk_mtrtb018_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb019_campo_formulario
    ADD CONSTRAINT fk_mtrtb019_fk_mtrtb0_mtrtb020 FOREIGN KEY (nu_processo) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb021_unidade_autorizada
    ADD CONSTRAINT fk_mtrtb021_mtrtb020 FOREIGN KEY (nu_processo) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb021_unidade_autorizada
    ADD CONSTRAINT fk_mtrtb021_mtrtb046 FOREIGN KEY (nu_processo_adm) REFERENCES mtrtb046_processo_adm(nu_processo_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb021_unidade_autorizada
    ADD CONSTRAINT fk_mtrtb021_mtrtb047 FOREIGN KEY (nu_contrato_adm) REFERENCES mtrtb047_contrato_adm(nu_contrato_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb021_unidade_autorizada
    ADD CONSTRAINT fk_mtrtb021_mtrtb048 FOREIGN KEY (nu_apenso_adm) REFERENCES mtrtb048_apenso_adm(nu_apenso_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb023_produto_processo
    ADD CONSTRAINT fk_mtrtb023_mtrtb020 FOREIGN KEY (nu_processo) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb023_produto_processo
    ADD CONSTRAINT fk_mtrtb023_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb024_produto_dossie
    ADD CONSTRAINT fk_mtrtb024_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb024_produto_dossie
    ADD CONSTRAINT fk_mtrtb024_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb025_processo_documento
    ADD CONSTRAINT fk_mtrtb025_mtrtb009 FOREIGN KEY (nu_tipo_documento) REFERENCES mtrtb009_tipo_documento(nu_tipo_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb025_processo_documento
    ADD CONSTRAINT fk_mtrtb025_mtrtb010 FOREIGN KEY (nu_funcao_documental) REFERENCES mtrtb010_funcao_documental(nu_funcao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb025_processo_documento
    ADD CONSTRAINT fk_mtrtb025_mtrtb020 FOREIGN KEY (nu_processo) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb026_relacao_processo
    ADD CONSTRAINT fk_mtrtb026_mtrtb020_01 FOREIGN KEY (nu_processo_pai) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb026_relacao_processo
    ADD CONSTRAINT fk_mtrtb026_mtrtb020_02 FOREIGN KEY (nu_processo_filho) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb028_opcao_campo
    ADD CONSTRAINT fk_mtrtb028_mtrtb027 FOREIGN KEY (nu_campo_entrada) REFERENCES mtrtb027_campo_entrada(nu_campo_entrada) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb029_campo_apresentacao
    ADD CONSTRAINT fk_mtrtb029_mtrtb019 FOREIGN KEY (nu_campo_formulario) REFERENCES mtrtb019_campo_formulario(nu_campo_formulario) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb030_resposta_dossie
    ADD CONSTRAINT fk_mtrtb030_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb030_resposta_dossie
    ADD CONSTRAINT fk_mtrtb030_mtrtb019 FOREIGN KEY (nu_campo_formulario) REFERENCES mtrtb019_campo_formulario(nu_campo_formulario) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb031_resposta_opcao
    ADD CONSTRAINT fk_mtrtb031_mtrtb028 FOREIGN KEY (nu_opcao_campo) REFERENCES mtrtb028_opcao_campo(nu_opcao_campo) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb031_resposta_opcao
    ADD CONSTRAINT fk_mtrtb031_mtrtb030 FOREIGN KEY (nu_resposta_dossie) REFERENCES mtrtb030_resposta_dossie(nu_resposta_dossie) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb032_elemento_conteudo
    ADD CONSTRAINT fk_mtrtb032_mtrtb009 FOREIGN KEY (nu_tipo_documento) REFERENCES mtrtb009_tipo_documento(nu_tipo_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb032_elemento_conteudo
    ADD CONSTRAINT fk_mtrtb032_mtrtb020 FOREIGN KEY (nu_processo) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb032_elemento_conteudo
    ADD CONSTRAINT fk_mtrtb032_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb032_elemento_conteudo
    ADD CONSTRAINT fk_mtrtb032_mtrtb032 FOREIGN KEY (nu_elemento_vinculador) REFERENCES mtrtb032_elemento_conteudo(nu_elemento_conteudo) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb034_garantia_produto
    ADD CONSTRAINT fk_mtrtb034_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb034_garantia_produto
    ADD CONSTRAINT fk_mtrtb034_mtrtb033 FOREIGN KEY (nu_garantia) REFERENCES mtrtb033_garantia(nu_garantia) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb035_garantia_informada
    ADD CONSTRAINT fk_mtrtb035_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb035_garantia_informada
    ADD CONSTRAINT fk_mtrtb035_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb035_garantia_informada
    ADD CONSTRAINT fk_mtrtb035_mtrtb033 FOREIGN KEY (nu_garantia) REFERENCES mtrtb033_garantia(nu_garantia) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb037_regra_documental
    ADD CONSTRAINT fk_mtrtb037_mtrtb006 FOREIGN KEY (nu_canal_captura) REFERENCES mtrtb006_canal(nu_canal) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb037_regra_documental
    ADD CONSTRAINT fk_mtrtb037_mtrtb009 FOREIGN KEY (nu_tipo_documento) REFERENCES mtrtb009_tipo_documento(nu_tipo_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb037_regra_documental
    ADD CONSTRAINT fk_mtrtb037_mtrtb010 FOREIGN KEY (nu_funcao_documental) REFERENCES mtrtb010_funcao_documental(nu_funcao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb037_regra_documental
    ADD CONSTRAINT fk_mtrtb037_mtrtb036 FOREIGN KEY (nu_composicao_documental) REFERENCES mtrtb036_composicao_documental(nu_composicao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb038_nivel_documental
    ADD CONSTRAINT fk_mtrtb038_mtrtb001 FOREIGN KEY (nu_dossie_cliente) REFERENCES mtrtb001_dossie_cliente(nu_dossie_cliente) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb038_nivel_documental
    ADD CONSTRAINT fk_mtrtb038_mtrtb036 FOREIGN KEY (nu_composicao_documental) REFERENCES mtrtb036_composicao_documental(nu_composicao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb039_produto_composicao
    ADD CONSTRAINT fk_mtrtb039_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb039_produto_composicao
    ADD CONSTRAINT fk_mtrtb039_mtrtb036 FOREIGN KEY (nu_composicao_documental) REFERENCES mtrtb036_composicao_documental(nu_composicao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb040_cadeia_tipo_sto_dossie
    ADD CONSTRAINT fk_mtrtb040_mtrtb012_01 FOREIGN KEY (nu_tipo_situacao_atual) REFERENCES mtrtb012_tipo_situacao_dossie(nu_tipo_situacao_dossie) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb040_cadeia_tipo_sto_dossie
    ADD CONSTRAINT fk_mtrtb040_mtrtb012_02 FOREIGN KEY (nu_tipo_situacao_seguinte) REFERENCES mtrtb012_tipo_situacao_dossie(nu_tipo_situacao_dossie) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb041_cadeia_stco_documento
    ADD CONSTRAINT fk_mtrtb041_mtrtb015_01 FOREIGN KEY (nu_situacao_documento_atual) REFERENCES mtrtb015_situacao_documento(nu_situacao_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb041_cadeia_stco_documento
    ADD CONSTRAINT fk_mtrtb041_mtrtb015_02 FOREIGN KEY (nu_situacao_documento_seguinte) REFERENCES mtrtb015_situacao_documento(nu_situacao_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb042_cliente_garantia
    ADD CONSTRAINT fk_mtrtb042_mtrtb001 FOREIGN KEY (nu_dossie_cliente) REFERENCES mtrtb001_dossie_cliente(nu_dossie_cliente) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb019_campo_formulario
    ADD CONSTRAINT fk_mtrtb042_mtrtb027 FOREIGN KEY (nu_campo_entrada) REFERENCES mtrtb027_campo_entrada(nu_campo_entrada) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb042_cliente_garantia
    ADD CONSTRAINT fk_mtrtb042_mtrtb035 FOREIGN KEY (nu_garantia_informada) REFERENCES mtrtb035_garantia_informada(nu_garantia_informada) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb043_documento_garantia
    ADD CONSTRAINT fk_mtrtb043_mtrtb009 FOREIGN KEY (nu_tipo_documento) REFERENCES mtrtb009_tipo_documento(nu_tipo_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb043_documento_garantia
    ADD CONSTRAINT fk_mtrtb043_mtrtb010 FOREIGN KEY (nu_funcao_documental) REFERENCES mtrtb010_funcao_documental(nu_funcao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb043_documento_garantia
    ADD CONSTRAINT fk_mtrtb043_mtrtb020 FOREIGN KEY (nu_processo) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb043_documento_garantia
    ADD CONSTRAINT fk_mtrtb043_mtrtb033 FOREIGN KEY (nu_garantia) REFERENCES mtrtb033_garantia(nu_garantia) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb044_comportamento_pesquisa
    ADD CONSTRAINT fk_mtrtb044_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb045_atributo_extracao
    ADD CONSTRAINT fk_mtrtb045_fk_mtrtb0_mtrtb009 FOREIGN KEY (nu_tipo_documento) REFERENCES mtrtb009_tipo_documento(nu_tipo_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb047_contrato_adm
    ADD CONSTRAINT fk_mtrtb047_mtrtb046 FOREIGN KEY (nu_processo_adm) REFERENCES mtrtb046_processo_adm(nu_processo_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb048_apenso_adm
    ADD CONSTRAINT fk_mtrtb048_mtrtb046 FOREIGN KEY (nu_processo_adm) REFERENCES mtrtb046_processo_adm(nu_processo_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb048_apenso_adm
    ADD CONSTRAINT fk_mtrtb048_mtrtb047 FOREIGN KEY (nu_contrato_adm) REFERENCES mtrtb047_contrato_adm(nu_contrato_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb049_documento_adm
    ADD CONSTRAINT fk_mtrtb049_mtrtb003 FOREIGN KEY (nu_documento) REFERENCES mtrtb003_documento(nu_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb049_documento_adm
    ADD CONSTRAINT fk_mtrtb049_mtrtb046 FOREIGN KEY (nu_processo_adm) REFERENCES mtrtb046_processo_adm(nu_processo_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb049_documento_adm
    ADD CONSTRAINT fk_mtrtb049_mtrtb047 FOREIGN KEY (nu_contrato_adm) REFERENCES mtrtb047_contrato_adm(nu_contrato_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb049_documento_adm
    ADD CONSTRAINT fk_mtrtb049_mtrtb048 FOREIGN KEY (nu_apenso_adm) REFERENCES mtrtb048_apenso_adm(nu_apenso_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb049_documento_adm
    ADD CONSTRAINT fk_mtrtb049_mtrtb049 FOREIGN KEY (nu_documento_substituto) REFERENCES mtrtb049_documento_adm(nu_documento_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb101_documento
    ADD CONSTRAINT fk_mtrtb101_mtrtb100 FOREIGN KEY (nu_autorizacao) REFERENCES mtrtb100_autorizacao(nu_autorizacao) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY mtrtb103_autorizacao_orientacao
    ADD CONSTRAINT fk_mtrtb103_mtrtb100 FOREIGN KEY (nu_autorizacao) REFERENCES mtrtb100_autorizacao(nu_autorizacao) ON UPDATE RESTRICT ON DELETE RESTRICT;


-- Completed on 2018-10-17 11:43:22

--
-- PostgreSQL database dump complete
--

