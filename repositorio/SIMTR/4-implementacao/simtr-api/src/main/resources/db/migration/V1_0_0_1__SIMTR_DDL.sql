--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.5
-- Dumped by pg_dump version 9.5.5

-- Started on 2017-12-01 11:53:26


SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
--SET row_security = off;

--
-- TOC entry 44 (class 2615 OID 1338258)
-- Name: mtrsm001; Type: SCHEMA; Schema: -; Owner: -
--

--CREATE SCHEMA mtrsm001;


SET search_path = mtrsm001, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 2237 (class 1259 OID 1338259)
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


--
-- TOC entry 8732 (class 0 OID 0)
-- Dependencies: 2237
-- Name: TABLE mtrtb001_dossie_cliente; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb001_dossie_cliente IS 'Tabela responsavel pelo armazenamento do dossie do cliente com seus respectivos dados.
Nesta tabela serão efetivamente armazenados os dados do cliente.';


--
-- TOC entry 8733 (class 0 OID 0)
-- Dependencies: 2237
-- Name: COLUMN mtrtb001_dossie_cliente.nu_dossie_cliente; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb001_dossie_cliente.nu_dossie_cliente IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8734 (class 0 OID 0)
-- Dependencies: 2237
-- Name: COLUMN mtrtb001_dossie_cliente.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb001_dossie_cliente.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8735 (class 0 OID 0)
-- Dependencies: 2237
-- Name: COLUMN mtrtb001_dossie_cliente.nu_cpf_cnpj; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb001_dossie_cliente.nu_cpf_cnpj IS 'Atroibuto que representa o numero do CPF/CNPJ do cliente';


--
-- TOC entry 8736 (class 0 OID 0)
-- Dependencies: 2237
-- Name: COLUMN mtrtb001_dossie_cliente.ic_tipo_pessoa; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb001_dossie_cliente.ic_tipo_pessoa IS 'Atributo que determina qual tipo de pessoa pode ter o documento atribuido.
Pode assumir os seguintes valores:
F - Fisica
J - Juridica
S - Serviço
A - Fisica ou Juridica
T - Todos';


--
-- TOC entry 8737 (class 0 OID 0)
-- Dependencies: 2237
-- Name: COLUMN mtrtb001_dossie_cliente.no_cliente; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb001_dossie_cliente.no_cliente IS 'Atributo que representa o nome do cliente';


--
-- TOC entry 8738 (class 0 OID 0)
-- Dependencies: 2237
-- Name: COLUMN mtrtb001_dossie_cliente.de_telefone; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb001_dossie_cliente.de_telefone IS 'Atributo que representa o telefone informado para o cliente';


--
-- TOC entry 2238 (class 1259 OID 1338262)
-- Name: mtrtb001_dossie_cliente_nu_dossie_cliente_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb001_dossie_cliente_nu_dossie_cliente_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8739 (class 0 OID 0)
-- Dependencies: 2238
-- Name: mtrtb001_dossie_cliente_nu_dossie_cliente_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb001_dossie_cliente_nu_dossie_cliente_seq OWNED BY mtrtb001_dossie_cliente.nu_dossie_cliente;


--
-- TOC entry 2239 (class 1259 OID 1338264)
-- Name: mtrtb001_pessoa_fisica; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb001_pessoa_fisica (
    nu_dossie_cliente bigint NOT NULL,
    dt_nascimento date,
    ic_estado_civil_antigo character varying(1),
    nu_nis character varying(20),
    nu_identidade character varying(15),
    no_orgao_emissor character varying(15),
    no_mae character varying(255),
    no_pai character varying(255),
    ic_estado_civil integer
);


--
-- TOC entry 8740 (class 0 OID 0)
-- Dependencies: 2239
-- Name: TABLE mtrtb001_pessoa_fisica; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb001_pessoa_fisica IS 'Tabela de especialização do dossiê de cliente para armazenar os atributos especificos de uma pessoa fisica';


--
-- TOC entry 8741 (class 0 OID 0)
-- Dependencies: 2239
-- Name: COLUMN mtrtb001_pessoa_fisica.dt_nascimento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_fisica.dt_nascimento IS 'Atributo utilizado para armazenar a data de nascimento de pessoas fisicas';


--
-- TOC entry 8742 (class 0 OID 0)
-- Dependencies: 2239
-- Name: COLUMN mtrtb001_pessoa_fisica.ic_estado_civil_antigo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_fisica.ic_estado_civil_antigo IS 'Atributo utilizado para armazenar o estado civil de pessoas fisicas. Pose assumir:
/* Casado */
C,
/* Solteiro */
S,
/* Divorciado */
D,
/* Desquitado */
Q,
/* Viuvo */
V,
/* Outros */
O';


--
-- TOC entry 8743 (class 0 OID 0)
-- Dependencies: 2239
-- Name: COLUMN mtrtb001_pessoa_fisica.nu_nis; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_fisica.nu_nis IS 'Atributo utilizado para armazenar o numero do NIS de pessoas fisicas';


--
-- TOC entry 8744 (class 0 OID 0)
-- Dependencies: 2239
-- Name: COLUMN mtrtb001_pessoa_fisica.nu_identidade; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_fisica.nu_identidade IS 'Atributo utilizado para armazenar o numero de identidade de pessoas fisicas';


--
-- TOC entry 8745 (class 0 OID 0)
-- Dependencies: 2239
-- Name: COLUMN mtrtb001_pessoa_fisica.no_orgao_emissor; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_fisica.no_orgao_emissor IS 'Atributo utilizado para armazenar o orgão emissor da identidade de pessoas fisicas';


--
-- TOC entry 8746 (class 0 OID 0)
-- Dependencies: 2239
-- Name: COLUMN mtrtb001_pessoa_fisica.no_mae; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_fisica.no_mae IS 'Atributo utilizado para armazenar o nome da mãe de pessoas fisicas';


--
-- TOC entry 8747 (class 0 OID 0)
-- Dependencies: 2239
-- Name: COLUMN mtrtb001_pessoa_fisica.no_pai; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_fisica.no_pai IS 'Atributo utilizado para armazenar o nome do pai de pessoas fisicas';


--
-- TOC entry 8748 (class 0 OID 0)
-- Dependencies: 2239
-- Name: COLUMN mtrtb001_pessoa_fisica.ic_estado_civil; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_fisica.ic_estado_civil IS 'Atributo utilizado para armazenar o estado civil de pessoas fisicas.

0 - Não Informado
1 - Solteiro (a)
2  - Casado (a)
3 - Divorciado (a)
4 - Separado (a) Judicialmente
5 - Viúvo (a)
6 - Com União Estável
7 - Casado (a) com comunhão total de bens
8 - Casado (a) sem comunhão de bens
9 - Casado (a) com comunhão parcial de bens';


--
-- TOC entry 2240 (class 1259 OID 1338270)
-- Name: mtrtb001_pessoa_juridica; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb001_pessoa_juridica (
    nu_dossie_cliente bigint NOT NULL,
    no_razao_social character varying(255) NOT NULL,
    dt_fundacao date,
    ic_segmento character varying(10)
);


--
-- TOC entry 8749 (class 0 OID 0)
-- Dependencies: 2240
-- Name: TABLE mtrtb001_pessoa_juridica; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb001_pessoa_juridica IS 'Tabela de especialização do dossiê de cliente para armazenar os atributos especificos de uma pessoajuridica';


--
-- TOC entry 8750 (class 0 OID 0)
-- Dependencies: 2240
-- Name: COLUMN mtrtb001_pessoa_juridica.no_razao_social; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_juridica.no_razao_social IS 'Atributo utilizado para armazenar a razão social de pessoas juridicas';


--
-- TOC entry 8751 (class 0 OID 0)
-- Dependencies: 2240
-- Name: COLUMN mtrtb001_pessoa_juridica.dt_fundacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_juridica.dt_fundacao IS 'Atributo utilizado para armazenar a data de fundação de pessoas juridicas';


--
-- TOC entry 8752 (class 0 OID 0)
-- Dependencies: 2240
-- Name: COLUMN mtrtb001_pessoa_juridica.ic_segmento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_juridica.ic_segmento IS 'Atributo para identificar o segmento da empresa, podendo assumir os valores oriundos da view do SIICO ICOTBXXX:
- MEI
- MPE
- MGE
- CORP';


--
-- TOC entry 2241 (class 1259 OID 1338273)
-- Name: mtrtb002_dossie_produto; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb002_dossie_produto (
    nu_dossie_produto bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_processo integer NOT NULL,
    nu_cgc_criacao integer NOT NULL,
    nu_cgc_priorizado integer,
    co_matricula_priorizado character varying(7),
    nu_peso_prioridade integer,
    nu_fase_utilizacao integer NOT NULL
);


--
-- TOC entry 8753 (class 0 OID 0)
-- Dependencies: 2241
-- Name: TABLE mtrtb002_dossie_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb002_dossie_produto IS 'Tabela responsavel pelo armazenamento do dossie do produto com seus respectivos dados.
Nesta tabela serão efetivamente armazenados os dados do produto.
Para cada produto contratato ou submetido a analise deve ser gerado um novo registro representando o vinculo com o cliente.';


--
-- TOC entry 8754 (class 0 OID 0)
-- Dependencies: 2241
-- Name: COLUMN mtrtb002_dossie_produto.nu_dossie_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb002_dossie_produto.nu_dossie_produto IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8755 (class 0 OID 0)
-- Dependencies: 2241
-- Name: COLUMN mtrtb002_dossie_produto.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb002_dossie_produto.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8756 (class 0 OID 0)
-- Dependencies: 2241
-- Name: COLUMN mtrtb002_dossie_produto.nu_processo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb002_dossie_produto.nu_processo IS 'Atributo utilizado para referenciar o processo ao qual o dossiê de produto esteja vinculado.';


--
-- TOC entry 8757 (class 0 OID 0)
-- Dependencies: 2241
-- Name: COLUMN mtrtb002_dossie_produto.nu_cgc_criacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb002_dossie_produto.nu_cgc_criacao IS 'Atributo utrilizado para armazenar o CGC da undade de criação do dossiê';


--
-- TOC entry 8758 (class 0 OID 0)
-- Dependencies: 2241
-- Name: COLUMN mtrtb002_dossie_produto.nu_cgc_priorizado; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb002_dossie_produto.nu_cgc_priorizado IS 'Atributo que indica o CGC da unidade que deverá tratar o dissiê na proxima chamada da fila por qualquer empregado vinculado ao mesmo.';


--
-- TOC entry 8759 (class 0 OID 0)
-- Dependencies: 2241
-- Name: COLUMN mtrtb002_dossie_produto.co_matricula_priorizado; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb002_dossie_produto.co_matricula_priorizado IS 'Atributo que indica o empregado especifico da unidade que deverá tratar o dissiê na próxima chamada da fila.';


--
-- TOC entry 8760 (class 0 OID 0)
-- Dependencies: 2241
-- Name: COLUMN mtrtb002_dossie_produto.nu_peso_prioridade; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb002_dossie_produto.nu_peso_prioridade IS 'Valor que indica dentre os dossiês priorizados, qual a ordem de captura na chamada da fila, sendo aplicado do maior para o menor. 
O valor é um numero livre atribuido pelo usuario que realizar a priorização do dossiê e a fila será organizada pelos dossiês priorizados com valor de peso do maior para o menor, em seguida pela ordem de cadastro definido pelo atributo de data de criação do mais antigo para o mais novo. ';


--
-- TOC entry 8761 (class 0 OID 0)
-- Dependencies: 2241
-- Name: COLUMN mtrtb002_dossie_produto.nu_fase_utilizacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb002_dossie_produto.nu_fase_utilizacao IS 'Atributo utilizado para identificar a fase de associação do mapa junto ao dossiê.
Conforme a fase definida no dossiê do produto, o conjunto de imagens e campos do formulario ficam disponiveis para preenchimento, caso selecionado uma fase do processo diferente da atual do dossiê, o formulario e relação de documentos referente a outra fase ficam disponiveis apenas para consulta.';


--
-- TOC entry 2242 (class 1259 OID 1338276)
-- Name: mtrtb002_dossie_produto_nu_dossie_produto_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb002_dossie_produto_nu_dossie_produto_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8762 (class 0 OID 0)
-- Dependencies: 2242
-- Name: mtrtb002_dossie_produto_nu_dossie_produto_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb002_dossie_produto_nu_dossie_produto_seq OWNED BY mtrtb002_dossie_produto.nu_dossie_produto;


--
-- TOC entry 2243 (class 1259 OID 1338278)
-- Name: mtrtb003_documento; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb003_documento (
    nu_documento bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_tipo_documento integer NOT NULL,
    nu_canal_captura integer,
    ts_captura timestamp without time zone NOT NULL,
    co_matricula character varying(7) NOT NULL,
    dt_validade date NOT NULL,
    ic_dossie_digital boolean
);


--
-- TOC entry 8763 (class 0 OID 0)
-- Dependencies: 2243
-- Name: TABLE mtrtb003_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb003_documento IS 'Tabela responsavel pelo armazenamento da referência dos documentos de um determinado cliente.
Esses documentos podem estar associados a um ou mais dossiês de produtos possibilitando o reaproveitamento dos mesmos em diversos produtos.
Nesta tabela serão efetivamente armazenados os dados dos documentos que pode representar o agrupamento de uma ou mais imagens na sua formação.
Também deverão ser armazenadas as propriedades do mesmo e as marcas conforme seu ciclo de vida';


--
-- TOC entry 8764 (class 0 OID 0)
-- Dependencies: 2243
-- Name: COLUMN mtrtb003_documento.nu_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.nu_documento IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8765 (class 0 OID 0)
-- Dependencies: 2243
-- Name: COLUMN mtrtb003_documento.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8766 (class 0 OID 0)
-- Dependencies: 2243
-- Name: COLUMN mtrtb003_documento.nu_tipo_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.nu_tipo_documento IS 'Atributo utilizado para armazenar a vinculação do tipo de documento referenciado pelo documento registrado';


--
-- TOC entry 8767 (class 0 OID 0)
-- Dependencies: 2243
-- Name: COLUMN mtrtb003_documento.nu_canal_captura; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.nu_canal_captura IS 'Atributo utilizado para identificar o canal de captura utilizado para recepcionar o documento.';


--
-- TOC entry 8768 (class 0 OID 0)
-- Dependencies: 2243
-- Name: COLUMN mtrtb003_documento.ts_captura; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.ts_captura IS 'Atributo que armazena a data e hora que foi realizada a captura do documento';


--
-- TOC entry 8769 (class 0 OID 0)
-- Dependencies: 2243
-- Name: COLUMN mtrtb003_documento.co_matricula; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.co_matricula IS 'Atributo que armazena a matricula do usuario que realizou a captura do documento';


--
-- TOC entry 8770 (class 0 OID 0)
-- Dependencies: 2243
-- Name: COLUMN mtrtb003_documento.dt_validade; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.dt_validade IS 'Atributo que armazena a data de validade do documento conforme definições corporativas calculado pelo prazo definido no tipo documento';


--
-- TOC entry 8771 (class 0 OID 0)
-- Dependencies: 2243
-- Name: COLUMN mtrtb003_documento.ic_dossie_digital; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.ic_dossie_digital IS 'Atributo utilizado para determinar que o documento passou por todas as etapas definidas pelo modelo do dossiê digital e que esta apto para reaproveitmanto em qualquer processo que utilize este modelo de conformidade.';


--
-- TOC entry 2244 (class 1259 OID 1338281)
-- Name: mtrtb003_documento_nu_documento_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb003_documento_nu_documento_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8772 (class 0 OID 0)
-- Dependencies: 2244
-- Name: mtrtb003_documento_nu_documento_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb003_documento_nu_documento_seq OWNED BY mtrtb003_documento.nu_documento;


--
-- TOC entry 2245 (class 1259 OID 1338283)
-- Name: mtrtb004_dossie_cliente_produto; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb004_dossie_cliente_produto (
    nu_dossie_cliente_produto bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_dossie_produto bigint NOT NULL,
    nu_dossie_cliente bigint NOT NULL,
    nu_sequencia_titularidade integer,
    ic_tipo_relacionamento character varying(50) NOT NULL
);


--
-- TOC entry 8773 (class 0 OID 0)
-- Dependencies: 2245
-- Name: TABLE mtrtb004_dossie_cliente_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb004_dossie_cliente_produto IS 'Tabela de relacionamento para permitir vincular um dossiê de produto a mais de um dossiê de cliente devido a necessidades de produtos com mais de um titular';


--
-- TOC entry 8774 (class 0 OID 0)
-- Dependencies: 2245
-- Name: COLUMN mtrtb004_dossie_cliente_produto.nu_dossie_cliente_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb004_dossie_cliente_produto.nu_dossie_cliente_produto IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8775 (class 0 OID 0)
-- Dependencies: 2245
-- Name: COLUMN mtrtb004_dossie_cliente_produto.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb004_dossie_cliente_produto.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8776 (class 0 OID 0)
-- Dependencies: 2245
-- Name: COLUMN mtrtb004_dossie_cliente_produto.nu_dossie_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb004_dossie_cliente_produto.nu_dossie_produto IS 'Atributo que armazena a referencia para o dossiê do produto vinculado na relação';


--
-- TOC entry 8777 (class 0 OID 0)
-- Dependencies: 2245
-- Name: COLUMN mtrtb004_dossie_cliente_produto.nu_dossie_cliente; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb004_dossie_cliente_produto.nu_dossie_cliente IS 'Atributo que armazena a referencia para o dossiê do cliente vinculado na relação';


--
-- TOC entry 8778 (class 0 OID 0)
-- Dependencies: 2245
-- Name: COLUMN mtrtb004_dossie_cliente_produto.nu_sequencia_titularidade; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb004_dossie_cliente_produto.nu_sequencia_titularidade IS 'Atributo que indica a sequencia de titularidade dos clientes para aquele processo.
Ao cadastraf um processo o operador pode incluir titulares conforme a necessidade do produto e este atributo indicara a ordinalidade dos titulares.';


--
-- TOC entry 8779 (class 0 OID 0)
-- Dependencies: 2245
-- Name: COLUMN mtrtb004_dossie_cliente_produto.ic_tipo_relacionamento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb004_dossie_cliente_produto.ic_tipo_relacionamento IS 'Atributo que indica o tipo de relacionamento do cliente com o produto, podendo assumir os valores:
- TITULAR
- AVALISTA
- CONJUGE
- SOCIO
etc.';


--
-- TOC entry 2246 (class 1259 OID 1338286)
-- Name: mtrtb004_dossie_cliente_produto_nu_dossie_cliente_produto_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb004_dossie_cliente_produto_nu_dossie_cliente_produto_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8780 (class 0 OID 0)
-- Dependencies: 2246
-- Name: mtrtb004_dossie_cliente_produto_nu_dossie_cliente_produto_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb004_dossie_cliente_produto_nu_dossie_cliente_produto_seq OWNED BY mtrtb004_dossie_cliente_produto.nu_dossie_cliente_produto;


--
-- TOC entry 2247 (class 1259 OID 1338288)
-- Name: mtrtb005_documento_cliente; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb005_documento_cliente (
    nu_documento bigint NOT NULL,
    nu_dossie_cliente bigint NOT NULL
);


--
-- TOC entry 8781 (class 0 OID 0)
-- Dependencies: 2247
-- Name: TABLE mtrtb005_documento_cliente; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb005_documento_cliente IS 'Tabela de relacionamento que vincula um documento ao dossiê de um cliente.';


--
-- TOC entry 8782 (class 0 OID 0)
-- Dependencies: 2247
-- Name: COLUMN mtrtb005_documento_cliente.nu_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb005_documento_cliente.nu_documento IS 'Atributo que representa o documento vinculado ao dossiê do cliente referenciado no registro.';


--
-- TOC entry 8783 (class 0 OID 0)
-- Dependencies: 2247
-- Name: COLUMN mtrtb005_documento_cliente.nu_dossie_cliente; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb005_documento_cliente.nu_dossie_cliente IS 'Atributo que representa a o dossiê do cliente vinculado na relação de documentos.';


--
-- TOC entry 2248 (class 1259 OID 1338291)
-- Name: mtrtb006_canal_captura; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb006_canal_captura (
    nu_canal_captura integer NOT NULL,
    nu_versao integer NOT NULL,
    sg_canal_captura character varying(5) NOT NULL,
    de_canal_captura character varying(255) NOT NULL
);


--
-- TOC entry 8784 (class 0 OID 0)
-- Dependencies: 2248
-- Name: TABLE mtrtb006_canal_captura; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb006_canal_captura IS 'Tabela responsavel pelo armazenamento dos possiveis canais de captura de um documento para identificação de sua origem.';


--
-- TOC entry 8785 (class 0 OID 0)
-- Dependencies: 2248
-- Name: COLUMN mtrtb006_canal_captura.nu_canal_captura; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb006_canal_captura.nu_canal_captura IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8786 (class 0 OID 0)
-- Dependencies: 2248
-- Name: COLUMN mtrtb006_canal_captura.sg_canal_captura; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb006_canal_captura.sg_canal_captura IS 'Atributo utilizado para identificar a sigla do canal de captura
Pode assumir valor como por exemplo
- SIMTR (Sistema Interno SIMTR)
- APMOB (Aplicativo Mobile)
- STECX (Site Corporativo da CAIXA)';


--
-- TOC entry 8787 (class 0 OID 0)
-- Dependencies: 2248
-- Name: COLUMN mtrtb006_canal_captura.de_canal_captura; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb006_canal_captura.de_canal_captura IS 'Atributo utilizado para descrever o canal de captura.
Ex:
Sistema Interno SIMTR
Sistemas Corporativos
Aplicativo Mobile
Site Corporativo';


--
-- TOC entry 2249 (class 1259 OID 1338294)
-- Name: mtrtb006_canal_captura_nu_canal_captura_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb006_canal_captura_nu_canal_captura_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8788 (class 0 OID 0)
-- Dependencies: 2249
-- Name: mtrtb006_canal_captura_nu_canal_captura_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb006_canal_captura_nu_canal_captura_seq OWNED BY mtrtb006_canal_captura.nu_canal_captura;


--
-- TOC entry 2250 (class 1259 OID 1338296)
-- Name: mtrtb007_atributo_documento; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb007_atributo_documento (
    nu_atributo_documento bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_documento bigint NOT NULL,
    de_atributo character varying(100) NOT NULL,
    de_conteudo text NOT NULL
);


--
-- TOC entry 8789 (class 0 OID 0)
-- Dependencies: 2250
-- Name: TABLE mtrtb007_atributo_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb007_atributo_documento IS 'Tabela responsavel por armazenar os atributos capturados do documento utilizando a estrutura de chave x valor onde o nome do atributo determina o campo do documento que a informação foi extraida e o conteudo trata-se do dado propriamente extraido.';


--
-- TOC entry 8790 (class 0 OID 0)
-- Dependencies: 2250
-- Name: COLUMN mtrtb007_atributo_documento.nu_atributo_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb007_atributo_documento.nu_atributo_documento IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8791 (class 0 OID 0)
-- Dependencies: 2250
-- Name: COLUMN mtrtb007_atributo_documento.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb007_atributo_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8792 (class 0 OID 0)
-- Dependencies: 2250
-- Name: COLUMN mtrtb007_atributo_documento.nu_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb007_atributo_documento.nu_documento IS 'Atributo utilizado para vincular o atributo definido ao documento cuja informação foi extraida';


--
-- TOC entry 8793 (class 0 OID 0)
-- Dependencies: 2250
-- Name: COLUMN mtrtb007_atributo_documento.de_atributo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb007_atributo_documento.de_atributo IS 'Atributo utilizado para armazenar a descrição da chave que identifica o atributo.
Como uxemplo, um registro que armazena o data de nascimento de um RG 

data_nascimento = 01/09/1980

Neste caso o conteudo deste campo seria "data_nascimento" e o atributo conteudo armazenaria "01/09/1980" tal qual extraido do documento';


--
-- TOC entry 8794 (class 0 OID 0)
-- Dependencies: 2250
-- Name: COLUMN mtrtb007_atributo_documento.de_conteudo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb007_atributo_documento.de_conteudo IS 'Atributo utilizado para armazenar a dado extraido de um campo do documento.
Como uxemplo, um registro que armazena o data de nascimento de um RG 

data_nascimento = 01/09/1980

Neste caso o conteudo deste campo seria "01/09/1980" tal qual extraido do documento e o atributo de descricao armazenaria "data_nascimento"';


--
-- TOC entry 2251 (class 1259 OID 1338302)
-- Name: mtrtb007_atributo_documento_nu_atributo_documento_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb007_atributo_documento_nu_atributo_documento_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8795 (class 0 OID 0)
-- Dependencies: 2251
-- Name: mtrtb007_atributo_documento_nu_atributo_documento_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb007_atributo_documento_nu_atributo_documento_seq OWNED BY mtrtb007_atributo_documento.nu_atributo_documento;


--
-- TOC entry 2252 (class 1259 OID 1338304)
-- Name: mtrtb008_conteudo; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb008_conteudo (
    nu_conteudo bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_documento bigint NOT NULL,
    de_uri character varying(255),
    co_ged character varying(255),
    nu_ordem integer NOT NULL,
    no_formato character varying(10) NOT NULL
);


--
-- TOC entry 8796 (class 0 OID 0)
-- Dependencies: 2252
-- Name: TABLE mtrtb008_conteudo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb008_conteudo IS 'Tabela responsavel pelo armazenamento das referencia de conteudo que compoem o documento.
Nesta tabela serão efetivamente armazenados os dados que caracterizam a imagem (ou o binario) e dados para localização do arquivo propriamente dito no repositorio.';


--
-- TOC entry 8797 (class 0 OID 0)
-- Dependencies: 2252
-- Name: COLUMN mtrtb008_conteudo.nu_conteudo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb008_conteudo.nu_conteudo IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8798 (class 0 OID 0)
-- Dependencies: 2252
-- Name: COLUMN mtrtb008_conteudo.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb008_conteudo.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8799 (class 0 OID 0)
-- Dependencies: 2252
-- Name: COLUMN mtrtb008_conteudo.nu_ordem; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb008_conteudo.nu_ordem IS 'Atributo utilizado para identificar a ordem de exibição na composição do documento. Documentos que possuem apenas um elemento, como um arquivo pdf por exemplo tera apenas um registro de coteudo com o atributo de ordem como 1';


--
-- TOC entry 8800 (class 0 OID 0)
-- Dependencies: 2252
-- Name: COLUMN mtrtb008_conteudo.no_formato; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb008_conteudo.no_formato IS 'Atributo utilizado para armazenar o formato do documento. Ex:
- pdf
- jpg
- tiff';


--
-- TOC entry 2253 (class 1259 OID 1338310)
-- Name: mtrtb008_conteudo_nu_conteudo_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb008_conteudo_nu_conteudo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8801 (class 0 OID 0)
-- Dependencies: 2253
-- Name: mtrtb008_conteudo_nu_conteudo_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb008_conteudo_nu_conteudo_seq OWNED BY mtrtb008_conteudo.nu_conteudo;


--
-- TOC entry 2254 (class 1259 OID 1338312)
-- Name: mtrtb009_tipo_documento; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb009_tipo_documento (
    nu_tipo_documento integer NOT NULL,
    nu_versao integer NOT NULL,
    no_tipo_documento character varying(100) NOT NULL,
    ic_tipo_pessoa character(1) NOT NULL,
    pz_validade_dias integer,
    ic_validade_auto_contida boolean NOT NULL,
    co_tipologia character varying(100) NOT NULL
);


--
-- TOC entry 8802 (class 0 OID 0)
-- Dependencies: 2254
-- Name: TABLE mtrtb009_tipo_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb009_tipo_documento IS 'Tabela responsavel pelo armazenamento dos possiveis tipos de documento que podem ser submetidos ao vinculo com os dossiês';


--
-- TOC entry 8803 (class 0 OID 0)
-- Dependencies: 2254
-- Name: COLUMN mtrtb009_tipo_documento.nu_tipo_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.nu_tipo_documento IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8804 (class 0 OID 0)
-- Dependencies: 2254
-- Name: COLUMN mtrtb009_tipo_documento.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8805 (class 0 OID 0)
-- Dependencies: 2254
-- Name: COLUMN mtrtb009_tipo_documento.no_tipo_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.no_tipo_documento IS 'Atributo que identifica o tipo de documento vinculado. Como exemplo podemos ter:
- RG
- CNH
- Certidão Negativa de Debito
- Passaporte
- Etc';


--
-- TOC entry 8806 (class 0 OID 0)
-- Dependencies: 2254
-- Name: COLUMN mtrtb009_tipo_documento.ic_tipo_pessoa; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.ic_tipo_pessoa IS 'Atributo que determina qual tipo de pessoa pode ter o documento atribuido.
Pode assumir os seguintes valores:
F - Fisica
J - Juridica
S - Serviço
A - Fisica ou Juridica
T - Todos';


--
-- TOC entry 8807 (class 0 OID 0)
-- Dependencies: 2254
-- Name: COLUMN mtrtb009_tipo_documento.pz_validade_dias; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.pz_validade_dias IS 'Atributo que indica a quantidade de dias para atribuição da validade do documento a partir da sua emissão.
Caso o valor deste atributo não esteja definido, significa que o documento possui um prazo de validade indeterminado.';


--
-- TOC entry 8808 (class 0 OID 0)
-- Dependencies: 2254
-- Name: COLUMN mtrtb009_tipo_documento.ic_validade_auto_contida; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.ic_validade_auto_contida IS 'Atributo determina se a validade do documento esta definida no proprio documento ou não como por exemplo no caso de certidões que possuem a validade determinada em seu corpo.
Caso o valor deste atributo seja falso, o prazo de validade deve ser calculado conforme definido no atributo de prazo de validade.';


--
-- TOC entry 8809 (class 0 OID 0)
-- Dependencies: 2254
-- Name: COLUMN mtrtb009_tipo_documento.co_tipologia; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.co_tipologia IS 'Atributo utilizado para armazenar o codigo da tipologia documental corporativa';


--
-- TOC entry 2255 (class 1259 OID 1338315)
-- Name: mtrtb009_tipo_documento_nu_tipo_documento_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb009_tipo_documento_nu_tipo_documento_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8810 (class 0 OID 0)
-- Dependencies: 2255
-- Name: mtrtb009_tipo_documento_nu_tipo_documento_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb009_tipo_documento_nu_tipo_documento_seq OWNED BY mtrtb009_tipo_documento.nu_tipo_documento;


--
-- TOC entry 2256 (class 1259 OID 1338317)
-- Name: mtrtb010_funcao_documental; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb010_funcao_documental (
    nu_funcao_documental integer NOT NULL,
    nu_versao integer NOT NULL,
    no_funcao character varying(100) NOT NULL,
    ic_ativo boolean NOT NULL
);


--
-- TOC entry 8811 (class 0 OID 0)
-- Dependencies: 2256
-- Name: TABLE mtrtb010_funcao_documental; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb010_funcao_documental IS 'Tabela responsavel por armazenar as possiveis funções documentais.
Essa informação permite agrupar documentos que possuem a mesma finalidade e um documento pode possui mais de uma função.
Exemplos dessa atribuição funcional, são:
- Identificação;
- Renda;
- Comprovação de Residencia
- etc';


--
-- TOC entry 8812 (class 0 OID 0)
-- Dependencies: 2256
-- Name: COLUMN mtrtb010_funcao_documental.nu_funcao_documental; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb010_funcao_documental.nu_funcao_documental IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8813 (class 0 OID 0)
-- Dependencies: 2256
-- Name: COLUMN mtrtb010_funcao_documental.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb010_funcao_documental.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8814 (class 0 OID 0)
-- Dependencies: 2256
-- Name: COLUMN mtrtb010_funcao_documental.no_funcao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb010_funcao_documental.no_funcao IS 'Atributo definido para armazenar o nome da função documental, como por exemplo:
- Identificação
- Comprovação de Renda
- Comprovação de Residencia
- Regularidade Fiscal
- etc
';


--
-- TOC entry 8815 (class 0 OID 0)
-- Dependencies: 2256
-- Name: COLUMN mtrtb010_funcao_documental.ic_ativo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb010_funcao_documental.ic_ativo IS 'Atributo que indica se a função documental esta ativa ou não para utilização no sistema.
Uma função só pode ser excluida fisicamente caso ela não possua relação com nenhum tipo de documento previamente.';


--
-- TOC entry 2257 (class 1259 OID 1338320)
-- Name: mtrtb010_funcao_documental_nu_funcao_documental_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb010_funcao_documental_nu_funcao_documental_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8816 (class 0 OID 0)
-- Dependencies: 2257
-- Name: mtrtb010_funcao_documental_nu_funcao_documental_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb010_funcao_documental_nu_funcao_documental_seq OWNED BY mtrtb010_funcao_documental.nu_funcao_documental;


--
-- TOC entry 2258 (class 1259 OID 1338322)
-- Name: mtrtb011_funcao_documento; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb011_funcao_documento (
    nu_tipo_documento integer NOT NULL,
    nu_funcao_documental integer NOT NULL
);


--
-- TOC entry 8817 (class 0 OID 0)
-- Dependencies: 2258
-- Name: TABLE mtrtb011_funcao_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb011_funcao_documento IS 'Tabela associativa que vincula um tipo de documento a sua função.
Ex: 
- RG x Identificação
- DIRPF x Renda
- DIRPF x Identificação
';


--
-- TOC entry 8818 (class 0 OID 0)
-- Dependencies: 2258
-- Name: COLUMN mtrtb011_funcao_documento.nu_tipo_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb011_funcao_documento.nu_tipo_documento IS 'Atributo que representa a o tipo de documento vinculado na relação com a função documental.';


--
-- TOC entry 8819 (class 0 OID 0)
-- Dependencies: 2258
-- Name: COLUMN mtrtb011_funcao_documento.nu_funcao_documental; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb011_funcao_documento.nu_funcao_documental IS 'Atributo que representa a o função documental vinculado na relação com o tipo de documento.';


--
-- TOC entry 2259 (class 1259 OID 1338325)
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


--
-- TOC entry 8820 (class 0 OID 0)
-- Dependencies: 2259
-- Name: TABLE mtrtb012_tipo_situacao_dossie; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb012_tipo_situacao_dossie IS 'Tabela responsavel pelo armazenamento das possiveis situações vinculadas a um dossiê de produto.

Como exemplo podemos ter as possiveis situações:
- Criado
- Atualizado
- Disponivel
- Em Analise
- etc';


--
-- TOC entry 8821 (class 0 OID 0)
-- Dependencies: 2259
-- Name: COLUMN mtrtb012_tipo_situacao_dossie.nu_tipo_situacao_dossie; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.nu_tipo_situacao_dossie IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8822 (class 0 OID 0)
-- Dependencies: 2259
-- Name: COLUMN mtrtb012_tipo_situacao_dossie.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8823 (class 0 OID 0)
-- Dependencies: 2259
-- Name: COLUMN mtrtb012_tipo_situacao_dossie.no_tipo_situacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.no_tipo_situacao IS 'Atributo que armazena o nome do tipo de situação do dossiê';


--
-- TOC entry 8824 (class 0 OID 0)
-- Dependencies: 2259
-- Name: COLUMN mtrtb012_tipo_situacao_dossie.ic_resumo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.ic_resumo IS 'Atrinuto utilizado para indicar se o tipo de situação gera agrupamento para exibição de resumo de dossiês';


--
-- TOC entry 8825 (class 0 OID 0)
-- Dependencies: 2259
-- Name: COLUMN mtrtb012_tipo_situacao_dossie.ic_fila_tratamento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.ic_fila_tratamento IS 'Atrinuto utilizado para indicar se o tipo de situação inclui o dossiês na fila para tratamento';


--
-- TOC entry 8826 (class 0 OID 0)
-- Dependencies: 2259
-- Name: COLUMN mtrtb012_tipo_situacao_dossie.ic_produtividade; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.ic_produtividade IS 'Atributo utilizado para indicar se o tipo de situação considera o dossiê na contagem da produtividade diaria.';


--
-- TOC entry 2260 (class 1259 OID 1338330)
-- Name: mtrtb012_tipo_situacao_dossie_nu_tipo_situacao_dossie_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb012_tipo_situacao_dossie_nu_tipo_situacao_dossie_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8827 (class 0 OID 0)
-- Dependencies: 2260
-- Name: mtrtb012_tipo_situacao_dossie_nu_tipo_situacao_dossie_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb012_tipo_situacao_dossie_nu_tipo_situacao_dossie_seq OWNED BY mtrtb012_tipo_situacao_dossie.nu_tipo_situacao_dossie;


--
-- TOC entry 2261 (class 1259 OID 1338332)
-- Name: mtrtb013_situacao_dossie; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb013_situacao_dossie (
    nu_situacao_dossie bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_dossie_produto bigint NOT NULL,
    nu_tipo_situacao_dossie integer NOT NULL,
    ts_inclusao timestamp without time zone NOT NULL,
    co_matricula character varying(7) NOT NULL,
    nu_cgc_unidade integer NOT NULL,
    de_observacao text
);


--
-- TOC entry 8828 (class 0 OID 0)
-- Dependencies: 2261
-- Name: TABLE mtrtb013_situacao_dossie; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb013_situacao_dossie IS 'Tabela responsavel por armazenar o historico de situações relativas ao dossiê do produto. Cada vez que houver uma mudança na situação apresentada pelo processo, um novo registro deve ser inserido gerando assim um historico das situações vivenciadas durante o seu ciclo de vida.';


--
-- TOC entry 8829 (class 0 OID 0)
-- Dependencies: 2261
-- Name: COLUMN mtrtb013_situacao_dossie.nu_situacao_dossie; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb013_situacao_dossie.nu_situacao_dossie IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8830 (class 0 OID 0)
-- Dependencies: 2261
-- Name: COLUMN mtrtb013_situacao_dossie.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb013_situacao_dossie.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8831 (class 0 OID 0)
-- Dependencies: 2261
-- Name: COLUMN mtrtb013_situacao_dossie.nu_dossie_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb013_situacao_dossie.nu_dossie_produto IS 'Atributo utilizado pata armazenar a referencia do dossiê do produto vinculado a situação';


--
-- TOC entry 8832 (class 0 OID 0)
-- Dependencies: 2261
-- Name: COLUMN mtrtb013_situacao_dossie.nu_tipo_situacao_dossie; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb013_situacao_dossie.nu_tipo_situacao_dossie IS 'Atributo utilizado para armazenar o tipo situação do dossiê que será atribuido manualmente pelo operador ou pela automacao do workflow quando estruturado.';


--
-- TOC entry 8833 (class 0 OID 0)
-- Dependencies: 2261
-- Name: COLUMN mtrtb013_situacao_dossie.ts_inclusao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb013_situacao_dossie.ts_inclusao IS 'Atributo utilizado para armazenar a data/hora de atribuição da situação ao dossiê';


--
-- TOC entry 8834 (class 0 OID 0)
-- Dependencies: 2261
-- Name: COLUMN mtrtb013_situacao_dossie.co_matricula; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb013_situacao_dossie.co_matricula IS 'Atributo utilizado para armazenar a matricula do empregado ou serviço que atribuiu a situação ao dossiê';


--
-- TOC entry 8835 (class 0 OID 0)
-- Dependencies: 2261
-- Name: COLUMN mtrtb013_situacao_dossie.nu_cgc_unidade; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb013_situacao_dossie.nu_cgc_unidade IS 'Atributo que indica a unidade do empregado que registrou a situação do dossiê';


--
-- TOC entry 8836 (class 0 OID 0)
-- Dependencies: 2261
-- Name: COLUMN mtrtb013_situacao_dossie.de_observacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb013_situacao_dossie.de_observacao IS 'Informação do usuario indicando o motivo de atribuição da situação definida.';


--
-- TOC entry 2262 (class 1259 OID 1338338)
-- Name: mtrtb013_situacao_dossie_nu_situacao_dossie_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb013_situacao_dossie_nu_situacao_dossie_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8837 (class 0 OID 0)
-- Dependencies: 2262
-- Name: mtrtb013_situacao_dossie_nu_situacao_dossie_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb013_situacao_dossie_nu_situacao_dossie_seq OWNED BY mtrtb013_situacao_dossie.nu_situacao_dossie;


--
-- TOC entry 2263 (class 1259 OID 1338340)
-- Name: mtrtb014_instancia_documento; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb014_instancia_documento (
    nu_instancia_documento bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_documento bigint NOT NULL,
    nu_dossie_produto bigint NOT NULL,
    nu_elemento_conteudo bigint,
    nu_garantia_informada bigint
);


--
-- TOC entry 8838 (class 0 OID 0)
-- Dependencies: 2263
-- Name: TABLE mtrtb014_instancia_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb014_instancia_documento IS 'Tabela responsavel pelo armazenamento de instancias de documentos que estarão vinculados aos dossiês dos produtos';


--
-- TOC entry 8839 (class 0 OID 0)
-- Dependencies: 2263
-- Name: COLUMN mtrtb014_instancia_documento.nu_instancia_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb014_instancia_documento.nu_instancia_documento IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8840 (class 0 OID 0)
-- Dependencies: 2263
-- Name: COLUMN mtrtb014_instancia_documento.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb014_instancia_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8841 (class 0 OID 0)
-- Dependencies: 2263
-- Name: COLUMN mtrtb014_instancia_documento.nu_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb014_instancia_documento.nu_documento IS 'Atributo que vincula o registro da instancia de documento ao documento propriamente dito permitndo assim o reaproveitamento de documento previamente existentes.';


--
-- TOC entry 8842 (class 0 OID 0)
-- Dependencies: 2263
-- Name: COLUMN mtrtb014_instancia_documento.nu_dossie_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb014_instancia_documento.nu_dossie_produto IS 'Atributo que armazena a referência do dossiê de produto vinculado a instancia do documento.';


--
-- TOC entry 8843 (class 0 OID 0)
-- Dependencies: 2263
-- Name: COLUMN mtrtb014_instancia_documento.nu_elemento_conteudo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb014_instancia_documento.nu_elemento_conteudo IS 'Atributo que representa o elemento do mapa de processo ao qual foi a instancia foi vinculada. Utilizado apenas para os casos de documentos submetidos pelo mapa de processo. Para os casos de documento do cliente associados/utilizados no dossiê do produto este atributo não estara definido.';


--
-- TOC entry 8844 (class 0 OID 0)
-- Dependencies: 2263
-- Name: COLUMN mtrtb014_instancia_documento.nu_garantia_informada; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb014_instancia_documento.nu_garantia_informada IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 2264 (class 1259 OID 1338343)
-- Name: mtrtb014_instancia_documento_nu_instancia_documento_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb014_instancia_documento_nu_instancia_documento_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8845 (class 0 OID 0)
-- Dependencies: 2264
-- Name: mtrtb014_instancia_documento_nu_instancia_documento_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb014_instancia_documento_nu_instancia_documento_seq OWNED BY mtrtb014_instancia_documento.nu_instancia_documento;


--
-- TOC entry 2265 (class 1259 OID 1338345)
-- Name: mtrtb015_situacao_documento; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb015_situacao_documento (
    nu_situacao_documento integer NOT NULL,
    nu_versao integer NOT NULL,
    no_situacao character varying(100) NOT NULL,
    ic_situacao_inicial boolean DEFAULT false NOT NULL,
    ic_situacao_final boolean DEFAULT false NOT NULL
);


--
-- TOC entry 8846 (class 0 OID 0)
-- Dependencies: 2265
-- Name: TABLE mtrtb015_situacao_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb015_situacao_documento IS 'Tabela responsavel pelo armazenamento das possiveis situações vinculadas a um documento.
Essas situações também deverão agrupar motivos para atribuição desta situação.
Como exemplo podemos ter as possiveis situações e entre parenteses os motivos de agrupamento:
- Aprovado
- Rejeitado (Ilegivel / Rasurado / Segurança)
- Pendente (Recaptura)';


--
-- TOC entry 8847 (class 0 OID 0)
-- Dependencies: 2265
-- Name: COLUMN mtrtb015_situacao_documento.nu_situacao_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb015_situacao_documento.nu_situacao_documento IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8848 (class 0 OID 0)
-- Dependencies: 2265
-- Name: COLUMN mtrtb015_situacao_documento.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb015_situacao_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8849 (class 0 OID 0)
-- Dependencies: 2265
-- Name: COLUMN mtrtb015_situacao_documento.no_situacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb015_situacao_documento.no_situacao IS 'Atributo que armazena o nome da situação do documento';


--
-- TOC entry 2266 (class 1259 OID 1338350)
-- Name: mtrtb015_situacao_documento_nu_situacao_documento_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb015_situacao_documento_nu_situacao_documento_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8850 (class 0 OID 0)
-- Dependencies: 2266
-- Name: mtrtb015_situacao_documento_nu_situacao_documento_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb015_situacao_documento_nu_situacao_documento_seq OWNED BY mtrtb015_situacao_documento.nu_situacao_documento;


--
-- TOC entry 2267 (class 1259 OID 1338352)
-- Name: mtrtb016_motivo_situacao_dcto; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb016_motivo_situacao_dcto (
    nu_motivo_situacao_dcto integer NOT NULL,
    nu_versao integer NOT NULL,
    nu_situacao_documento integer NOT NULL,
    no_motivo_situacao_dcto character varying(100) NOT NULL
);


--
-- TOC entry 8851 (class 0 OID 0)
-- Dependencies: 2267
-- Name: TABLE mtrtb016_motivo_situacao_dcto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb016_motivo_situacao_dcto IS 'Tabela de motivos especificos para indicar a causa de uma determinada situação vinculada a um dado documento.
Ex:  
- Ilegivel -> Rejeitado
- Rasurado -> Rejeitado
- Segurança -> Rejeitado
- Recaptura -> Pendente';


--
-- TOC entry 8852 (class 0 OID 0)
-- Dependencies: 2267
-- Name: COLUMN mtrtb016_motivo_situacao_dcto.nu_motivo_situacao_dcto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb016_motivo_situacao_dcto.nu_motivo_situacao_dcto IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8853 (class 0 OID 0)
-- Dependencies: 2267
-- Name: COLUMN mtrtb016_motivo_situacao_dcto.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb016_motivo_situacao_dcto.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8854 (class 0 OID 0)
-- Dependencies: 2267
-- Name: COLUMN mtrtb016_motivo_situacao_dcto.nu_situacao_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb016_motivo_situacao_dcto.nu_situacao_documento IS 'Atributo utilizado para vincular o motivo com uma situação especifica.';


--
-- TOC entry 8855 (class 0 OID 0)
-- Dependencies: 2267
-- Name: COLUMN mtrtb016_motivo_situacao_dcto.no_motivo_situacao_dcto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb016_motivo_situacao_dcto.no_motivo_situacao_dcto IS 'Atributo que armazena o nome do motivo da situação do documento';


--
-- TOC entry 2268 (class 1259 OID 1338355)
-- Name: mtrtb016_motivo_situacao_dcto_nu_motivo_situacao_dcto_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb016_motivo_situacao_dcto_nu_motivo_situacao_dcto_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8856 (class 0 OID 0)
-- Dependencies: 2268
-- Name: mtrtb016_motivo_situacao_dcto_nu_motivo_situacao_dcto_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb016_motivo_situacao_dcto_nu_motivo_situacao_dcto_seq OWNED BY mtrtb016_motivo_situacao_dcto.nu_motivo_situacao_dcto;


--
-- TOC entry 2269 (class 1259 OID 1338357)
-- Name: mtrtb017_situacao_instancia_dct; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb017_situacao_instancia_dct (
    nu_situacao_instancia_dcto bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_instancia_documento bigint NOT NULL,
    nu_situacao_documento integer NOT NULL,
    nu_motivo_situacao_dcto integer,
    ts_inclusao timestamp without time zone NOT NULL,
    co_matricula character varying(7) NOT NULL,
    nu_cgc_unidade integer NOT NULL
);


--
-- TOC entry 8857 (class 0 OID 0)
-- Dependencies: 2269
-- Name: TABLE mtrtb017_situacao_instancia_dct; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb017_situacao_instancia_dct IS 'Tabela responsavel por armazenar o historico de situações relativas a instancia do documento em avaliação. Cada vez que houver uma mudança na situação apresentada pelo processo, um novo registro deve ser inserido gerando assim um historico das situações vivenciadas durante o seu ciclo de vida.';


--
-- TOC entry 8858 (class 0 OID 0)
-- Dependencies: 2269
-- Name: COLUMN mtrtb017_situacao_instancia_dct.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb017_situacao_instancia_dct.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8859 (class 0 OID 0)
-- Dependencies: 2269
-- Name: COLUMN mtrtb017_situacao_instancia_dct.nu_instancia_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb017_situacao_instancia_dct.nu_instancia_documento IS 'Atributo utilizado pata armazenar a referencia da instancia do documento em avaliação vinculado a situação';


--
-- TOC entry 8860 (class 0 OID 0)
-- Dependencies: 2269
-- Name: COLUMN mtrtb017_situacao_instancia_dct.nu_situacao_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb017_situacao_instancia_dct.nu_situacao_documento IS 'Atributo utilizado pata armazenar a referencia a situação do documento escolhida vinculada a instancia do documento em avaliação';


--
-- TOC entry 8861 (class 0 OID 0)
-- Dependencies: 2269
-- Name: COLUMN mtrtb017_situacao_instancia_dct.nu_motivo_situacao_dcto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb017_situacao_instancia_dct.nu_motivo_situacao_dcto IS 'Atributo utilizado pata armazenar a referencia o motivo especifico para a situação escolhida vinculada a instancia do documento em avaliação';


--
-- TOC entry 8862 (class 0 OID 0)
-- Dependencies: 2269
-- Name: COLUMN mtrtb017_situacao_instancia_dct.ts_inclusao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb017_situacao_instancia_dct.ts_inclusao IS 'Atributo utilizado para armazenar a data/hora de atribuição da situação ao dossiê';


--
-- TOC entry 8863 (class 0 OID 0)
-- Dependencies: 2269
-- Name: COLUMN mtrtb017_situacao_instancia_dct.co_matricula; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb017_situacao_instancia_dct.co_matricula IS 'Atributo utilizado para armazenar a matricula do empregado ou serviço que atribuiu a situação a instancia do documento em avaliação';


--
-- TOC entry 8864 (class 0 OID 0)
-- Dependencies: 2269
-- Name: COLUMN mtrtb017_situacao_instancia_dct.nu_cgc_unidade; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb017_situacao_instancia_dct.nu_cgc_unidade IS 'Atributo que indica a unidade do empregado que registrou a situação da instancia do documento analisado';


--
-- TOC entry 2270 (class 1259 OID 1338360)
-- Name: mtrtb017_situacao_instancia_dct_nu_situacao_instancia_dcto_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb017_situacao_instancia_dct_nu_situacao_instancia_dcto_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8865 (class 0 OID 0)
-- Dependencies: 2270
-- Name: mtrtb017_situacao_instancia_dct_nu_situacao_instancia_dcto_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb017_situacao_instancia_dct_nu_situacao_instancia_dcto_seq OWNED BY mtrtb017_situacao_instancia_dct.nu_situacao_instancia_dcto;


--
-- TOC entry 2271 (class 1259 OID 1338362)
-- Name: mtrtb018_unidade_tratamento; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb018_unidade_tratamento (
    nu_dossie_produto bigint NOT NULL,
    nu_cgc_unidade integer NOT NULL
);


--
-- TOC entry 8866 (class 0 OID 0)
-- Dependencies: 2271
-- Name: TABLE mtrtb018_unidade_tratamento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb018_unidade_tratamento IS 'Tabela utilizada para identificar as unidades de tratamento atribuidas para o dissê naquele dado momento.
Sempre que a situação do dossiê for modificada, os registros referentes ao dossiê especificamente serão excluidos e reinseridos novos com base na nova situação.';


--
-- TOC entry 8867 (class 0 OID 0)
-- Dependencies: 2271
-- Name: COLUMN mtrtb018_unidade_tratamento.nu_dossie_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb018_unidade_tratamento.nu_dossie_produto IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8868 (class 0 OID 0)
-- Dependencies: 2271
-- Name: COLUMN mtrtb018_unidade_tratamento.nu_cgc_unidade; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb018_unidade_tratamento.nu_cgc_unidade IS 'Atributo que indica o numero do CGC da unidade que term permissão de tratamento naquele dado momento do dossiê.';


--
-- TOC entry 2272 (class 1259 OID 1338365)
-- Name: mtrtb019_macroprocesso; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb019_macroprocesso (
    nu_macroprocesso integer NOT NULL,
    nu_versao integer NOT NULL,
    no_macroprocesso character varying(255) NOT NULL,
    ic_ativo boolean NOT NULL,
    de_avatar character varying(255)
);


--
-- TOC entry 8869 (class 0 OID 0)
-- Dependencies: 2272
-- Name: TABLE mtrtb019_macroprocesso; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb019_macroprocesso IS 'Tabela utilizada para identificar o macroprocesso que agrupa os processos';


--
-- TOC entry 8870 (class 0 OID 0)
-- Dependencies: 2272
-- Name: COLUMN mtrtb019_macroprocesso.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb019_macroprocesso.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8871 (class 0 OID 0)
-- Dependencies: 2272
-- Name: COLUMN mtrtb019_macroprocesso.no_macroprocesso; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb019_macroprocesso.no_macroprocesso IS 'Atributo utilizado para armazenar o nome do macroprocesso';


--
-- TOC entry 8872 (class 0 OID 0)
-- Dependencies: 2272
-- Name: COLUMN mtrtb019_macroprocesso.ic_ativo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb019_macroprocesso.ic_ativo IS 'Atributo que indica que se o processo esta ativo ou não para utilização pelo sistema.';


--
-- TOC entry 8873 (class 0 OID 0)
-- Dependencies: 2272
-- Name: COLUMN mtrtb019_macroprocesso.de_avatar; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb019_macroprocesso.de_avatar IS 'Atributo utilizado para armazenar o nome do avatar que será disponibilizado no pacote da ineterface grafica para montagem e apresentação das filas de captura o uinformações do processo.';


--
-- TOC entry 2273 (class 1259 OID 1338371)
-- Name: mtrtb019_macroprocesso_nu_macroprocesso_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb019_macroprocesso_nu_macroprocesso_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8874 (class 0 OID 0)
-- Dependencies: 2273
-- Name: mtrtb019_macroprocesso_nu_macroprocesso_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb019_macroprocesso_nu_macroprocesso_seq OWNED BY mtrtb019_macroprocesso.nu_macroprocesso;


--
-- TOC entry 2274 (class 1259 OID 1338373)
-- Name: mtrtb020_processo; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb020_processo (
    nu_processo integer NOT NULL,
    nu_versao integer NOT NULL,
    nu_macroprocesso integer NOT NULL,
    no_processo character varying(255) NOT NULL,
    ic_ativo boolean NOT NULL,
    de_avatar character varying(255),
    nu_prioridade_macroprocesso integer NOT NULL,
    nu_quantidade_tratamento integer NOT NULL
);


--
-- TOC entry 8875 (class 0 OID 0)
-- Dependencies: 2274
-- Name: TABLE mtrtb020_processo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb020_processo IS 'Tabela responsavel pelo armazenamento dos processos que podem ser atrelados aos dossiês de forma a identificar qual o processo bancario relacionado.
Processos que possuam vinculação com dossiês de produto não devem ser excluidos fisicamente, e sim atribuidos como inativo.
Exemplos de processos na linguagem negocial são:
- Concessão de Cretido Habitacional
- Conta Corrente
- Financiamento de Veiculos
- Pagamento de Loterias
- Etc';


--
-- TOC entry 8876 (class 0 OID 0)
-- Dependencies: 2274
-- Name: COLUMN mtrtb020_processo.nu_processo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb020_processo.nu_processo IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8877 (class 0 OID 0)
-- Dependencies: 2274
-- Name: COLUMN mtrtb020_processo.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb020_processo.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8878 (class 0 OID 0)
-- Dependencies: 2274
-- Name: COLUMN mtrtb020_processo.nu_macroprocesso; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb020_processo.nu_macroprocesso IS 'Atributo que identifica o macroprocesso de vinculação do processo especificado.';


--
-- TOC entry 8879 (class 0 OID 0)
-- Dependencies: 2274
-- Name: COLUMN mtrtb020_processo.no_processo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb020_processo.no_processo IS 'Atributo utilizado para armazenar o nome de identificação negocial do processo';


--
-- TOC entry 8880 (class 0 OID 0)
-- Dependencies: 2274
-- Name: COLUMN mtrtb020_processo.ic_ativo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb020_processo.ic_ativo IS 'Atributo que indica que se o processo esta ativo ou não para utilização pelo sistema.';


--
-- TOC entry 8881 (class 0 OID 0)
-- Dependencies: 2274
-- Name: COLUMN mtrtb020_processo.de_avatar; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb020_processo.de_avatar IS 'Atributo utilizado para armazenar o nome do avatar que será disponibilizado no pacote da ineterface grafica para montagem e apresentação das filas de captura o uinformações do processo.';


--
-- TOC entry 8882 (class 0 OID 0)
-- Dependencies: 2274
-- Name: COLUMN mtrtb020_processo.nu_prioridade_macroprocesso; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb020_processo.nu_prioridade_macroprocesso IS 'Atributo que determina a ordem de prioridade de atendimento do processo dentro da fila do macroprocesso.';


--
-- TOC entry 8883 (class 0 OID 0)
-- Dependencies: 2274
-- Name: COLUMN mtrtb020_processo.nu_quantidade_tratamento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb020_processo.nu_quantidade_tratamento IS 'Atributo utilizado para determinar a quantidade de dossiês que vão formar a fila de tratamento em formato de revezamento com base na prioridade definida sob o macroprocesso.
Exemplo:
Processo X - Prioridade 1 - Quantidade 4
Processo P - Prioridade 3 - Quantidade 2
Processo T - Prioridade 4 - Quantidade 5
Processo O - Prioridade 2 - Quantidade 3

Neste exemplo a fila de um maroprocesso seria montada assim: 
........PPOOOXXXXTTTTTPPOOOXXXXTTTTTPPOOOXXXX

(* Leitura da direita pra esquerda, onde cada letra representa um dossiê)';


--
-- TOC entry 2275 (class 1259 OID 1338379)
-- Name: mtrtb020_processo_nu_processo_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb020_processo_nu_processo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8884 (class 0 OID 0)
-- Dependencies: 2275
-- Name: mtrtb020_processo_nu_processo_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb020_processo_nu_processo_seq OWNED BY mtrtb020_processo.nu_processo;


--
-- TOC entry 2276 (class 1259 OID 1338381)
-- Name: mtrtb021_unidade_autorizada; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb021_unidade_autorizada (
    nu_unidade_autorizada integer NOT NULL,
    nu_versao integer NOT NULL,
    nu_processo integer NOT NULL,
    nu_cgc integer NOT NULL,
    ic_tipo_tratamento integer NOT NULL
);


--
-- TOC entry 8885 (class 0 OID 0)
-- Dependencies: 2276
-- Name: TABLE mtrtb021_unidade_autorizada; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb021_unidade_autorizada IS 'Tabela responsavel pelo armazenamento das unidades autorizadas a utilização do processo
';


--
-- TOC entry 8886 (class 0 OID 0)
-- Dependencies: 2276
-- Name: COLUMN mtrtb021_unidade_autorizada.nu_unidade_autorizada; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb021_unidade_autorizada.nu_unidade_autorizada IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8887 (class 0 OID 0)
-- Dependencies: 2276
-- Name: COLUMN mtrtb021_unidade_autorizada.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb021_unidade_autorizada.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8888 (class 0 OID 0)
-- Dependencies: 2276
-- Name: COLUMN mtrtb021_unidade_autorizada.ic_tipo_tratamento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb021_unidade_autorizada.ic_tipo_tratamento IS 'Atributo que indica as ações possiveis a serem realizadas no processo para a determinada unidade. A soma dos valores das ações determinam quais as permissões da unidade sobre os dossiês do processo. Os valores possiveis são:
1 - CONSULTA_DOSSIE
2 - TRATAR_DOSSIE
4 - CRIAR_DOSSIE
8 - PRIORIZAR_DOSSIE

Considerando o fato, como exemplo uma unidade que possua o valor 7 atribuido pode consultar, tratar e criardossiês, mas não pode priorizar.';


--
-- TOC entry 2277 (class 1259 OID 1338384)
-- Name: mtrtb021_unidade_autorizada_nu_unidade_autorizada_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb021_unidade_autorizada_nu_unidade_autorizada_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8889 (class 0 OID 0)
-- Dependencies: 2277
-- Name: mtrtb021_unidade_autorizada_nu_unidade_autorizada_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb021_unidade_autorizada_nu_unidade_autorizada_seq OWNED BY mtrtb021_unidade_autorizada.nu_unidade_autorizada;


--
-- TOC entry 2278 (class 1259 OID 1338386)
-- Name: mtrtb022_produto; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb022_produto (
    nu_produto integer NOT NULL,
    nu_versao integer NOT NULL,
    nu_operacao integer NOT NULL,
    nu_modalidade integer NOT NULL,
    no_produto character varying(255) NOT NULL,
    ic_contratacao_conjunta boolean NOT NULL,
    ic_pesquisas boolean DEFAULT false NOT NULL,
    ic_bloqueio_cadin boolean DEFAULT false NOT NULL,
    ic_bloqueio_scpc boolean DEFAULT false NOT NULL,
    ic_bloqueio_serasa boolean DEFAULT false NOT NULL,
    ic_bloqueio_ccf boolean DEFAULT false NOT NULL,
    ic_bloqueio_sicow boolean DEFAULT false NOT NULL,
    ic_bloqueio_receita boolean DEFAULT false NOT NULL
);


--
-- TOC entry 8890 (class 0 OID 0)
-- Dependencies: 2278
-- Name: TABLE mtrtb022_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb022_produto IS 'Tabela responsavel pelo armazenamento dos produtos da CAIXA que serão vinculados aos processos definidos';


--
-- TOC entry 8891 (class 0 OID 0)
-- Dependencies: 2278
-- Name: COLUMN mtrtb022_produto.nu_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.nu_produto IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8892 (class 0 OID 0)
-- Dependencies: 2278
-- Name: COLUMN mtrtb022_produto.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8893 (class 0 OID 0)
-- Dependencies: 2278
-- Name: COLUMN mtrtb022_produto.nu_operacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.nu_operacao IS 'Atributo que armazena o numero de operação corporativa do produto';


--
-- TOC entry 8894 (class 0 OID 0)
-- Dependencies: 2278
-- Name: COLUMN mtrtb022_produto.nu_modalidade; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.nu_modalidade IS 'Atributo que armazena o numero da modalidade corporativa do produto';


--
-- TOC entry 8895 (class 0 OID 0)
-- Dependencies: 2278
-- Name: COLUMN mtrtb022_produto.no_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.no_produto IS 'Atributo que armazena o nome corporativo do produto';


--
-- TOC entry 8896 (class 0 OID 0)
-- Dependencies: 2278
-- Name: COLUMN mtrtb022_produto.ic_contratacao_conjunta; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.ic_contratacao_conjunta IS 'Atributo utilizado para identificar se o produto permite realizar contatação conjunta com outros produtos, caso o atributo esteja setado como false, será criado um dossiê individual para o cada produto selecionado nessa consição.';


--
-- TOC entry 8897 (class 0 OID 0)
-- Dependencies: 2278
-- Name: COLUMN mtrtb022_produto.ic_pesquisas; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.ic_pesquisas IS 'Atributo utilizado para identificar se as pesquisas do SIPES devem ser realizadas quando solicitado autorização para o produto do registro.';


--
-- TOC entry 8898 (class 0 OID 0)
-- Dependencies: 2278
-- Name: COLUMN mtrtb022_produto.ic_bloqueio_cadin; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.ic_bloqueio_cadin IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao CADIN retorne resultado alguma restrição.';


--
-- TOC entry 8899 (class 0 OID 0)
-- Dependencies: 2278
-- Name: COLUMN mtrtb022_produto.ic_bloqueio_scpc; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.ic_bloqueio_scpc IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao SCPC retorne resultado alguma restrição.';


--
-- TOC entry 8900 (class 0 OID 0)
-- Dependencies: 2278
-- Name: COLUMN mtrtb022_produto.ic_bloqueio_serasa; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.ic_bloqueio_serasa IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao SPC/SERASA retorne resultado alguma restrição.';


--
-- TOC entry 8901 (class 0 OID 0)
-- Dependencies: 2278
-- Name: COLUMN mtrtb022_produto.ic_bloqueio_ccf; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.ic_bloqueio_ccf IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao SICCF retorne resultado alguma restrição.';


--
-- TOC entry 8902 (class 0 OID 0)
-- Dependencies: 2278
-- Name: COLUMN mtrtb022_produto.ic_bloqueio_sicow; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.ic_bloqueio_sicow IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao SICOW retorne resultado alguma restrição.';


--
-- TOC entry 8903 (class 0 OID 0)
-- Dependencies: 2278
-- Name: COLUMN mtrtb022_produto.ic_bloqueio_receita; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.ic_bloqueio_receita IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto a Receita retorne resultado alguma restrição.';


--
-- TOC entry 2279 (class 1259 OID 1338396)
-- Name: mtrtb022_produto_nu_produto_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb022_produto_nu_produto_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8904 (class 0 OID 0)
-- Dependencies: 2279
-- Name: mtrtb022_produto_nu_produto_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb022_produto_nu_produto_seq OWNED BY mtrtb022_produto.nu_produto;


--
-- TOC entry 2280 (class 1259 OID 1338398)
-- Name: mtrtb023_produto_processo; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb023_produto_processo (
    nu_processo integer NOT NULL,
    nu_produto integer NOT NULL
);


--
-- TOC entry 8905 (class 0 OID 0)
-- Dependencies: 2280
-- Name: TABLE mtrtb023_produto_processo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb023_produto_processo IS 'Tabela de relacionamento para vinculação do produto com o processo. 
Existe a possibilidade que um produto seja vinculado a diversos processos pois pode diferenciar a forma de realizar as ações conforme o canal de contratação, campanha, ou outro fator, como por exemplo uma conta que seja contratada pela agencia fisica, agencia virtual, CCA ou Aplicativo de abertura de contas';


--
-- TOC entry 8906 (class 0 OID 0)
-- Dependencies: 2280
-- Name: COLUMN mtrtb023_produto_processo.nu_processo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb023_produto_processo.nu_processo IS 'Atributo que representa o processo vinculado na relação com o produto.';


--
-- TOC entry 8907 (class 0 OID 0)
-- Dependencies: 2280
-- Name: COLUMN mtrtb023_produto_processo.nu_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb023_produto_processo.nu_produto IS 'Atributo que representa o produto vinculado na relação com o processo.';


--
-- TOC entry 2281 (class 1259 OID 1338401)
-- Name: mtrtb024_produto_dossie; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb024_produto_dossie (
    nu_produto_dossie bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_dossie_produto bigint NOT NULL,
    nu_produto integer NOT NULL,
    vr_contrato numeric(15,2) NOT NULL,
    pc_juros_operacao numeric(5,2) NOT NULL,
    pz_operacao integer NOT NULL,
    pz_carencia integer NOT NULL,
    ic_liquidacao boolean NOT NULL,
    co_contrato_renovado character varying(100),
    ic_periodo_juros character(1) NOT NULL
);


--
-- TOC entry 8908 (class 0 OID 0)
-- Dependencies: 2281
-- Name: TABLE mtrtb024_produto_dossie; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb024_produto_dossie IS 'Tabela de relacionamento para vinculação dos produtos selecionados para tratamento no doissiê. 
Existe a possibilidade que mais de um produto seja vinculado a um dossiê para tratamento unico como é o caso do contrato de relacionamento que encolve Cartão de Credito / CROT / CDC / Conta Corrente';


--
-- TOC entry 8909 (class 0 OID 0)
-- Dependencies: 2281
-- Name: COLUMN mtrtb024_produto_dossie.nu_produto_dossie; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.nu_produto_dossie IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8910 (class 0 OID 0)
-- Dependencies: 2281
-- Name: COLUMN mtrtb024_produto_dossie.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8911 (class 0 OID 0)
-- Dependencies: 2281
-- Name: COLUMN mtrtb024_produto_dossie.nu_dossie_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.nu_dossie_produto IS 'Atributo que representa o dossiê de vinculação da coleção de produtos em analise.';


--
-- TOC entry 8912 (class 0 OID 0)
-- Dependencies: 2281
-- Name: COLUMN mtrtb024_produto_dossie.nu_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.nu_produto IS 'Atributo que representa o produto vinculado na relação com o dossiê.';


--
-- TOC entry 8913 (class 0 OID 0)
-- Dependencies: 2281
-- Name: COLUMN mtrtb024_produto_dossie.pc_juros_operacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.pc_juros_operacao IS 'Percentual de juros utilizado na contratação do produto';


--
-- TOC entry 8914 (class 0 OID 0)
-- Dependencies: 2281
-- Name: COLUMN mtrtb024_produto_dossie.pz_operacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.pz_operacao IS 'Prazo utilizado na contratação do produto';


--
-- TOC entry 8915 (class 0 OID 0)
-- Dependencies: 2281
-- Name: COLUMN mtrtb024_produto_dossie.pz_carencia; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.pz_carencia IS 'Prazo utilizado como carencia na contratação do produto';


--
-- TOC entry 8916 (class 0 OID 0)
-- Dependencies: 2281
-- Name: COLUMN mtrtb024_produto_dossie.ic_liquidacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.ic_liquidacao IS 'Atributo utilizado para indicar se a contratação do produto prevê a liquidação/renovação de um contrato.';


--
-- TOC entry 8917 (class 0 OID 0)
-- Dependencies: 2281
-- Name: COLUMN mtrtb024_produto_dossie.co_contrato_renovado; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.co_contrato_renovado IS 'Atributo utilizado para armazenar o contrato liquidado/renovado';


--
-- TOC entry 8918 (class 0 OID 0)
-- Dependencies: 2281
-- Name: COLUMN mtrtb024_produto_dossie.ic_periodo_juros; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.ic_periodo_juros IS 'Armazena o periodo de juros ao qual se refere a taxa. 
D - Diário
M - Mensal
A - Anual'';
';


--
-- TOC entry 2282 (class 1259 OID 1338404)
-- Name: mtrtb024_produto_dossie_nu_produto_dossie_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb024_produto_dossie_nu_produto_dossie_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8919 (class 0 OID 0)
-- Dependencies: 2282
-- Name: mtrtb024_produto_dossie_nu_produto_dossie_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb024_produto_dossie_nu_produto_dossie_seq OWNED BY mtrtb024_produto_dossie.nu_produto_dossie;


--
-- TOC entry 2283 (class 1259 OID 1338406)
-- Name: mtrtb025_processo_documento; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb025_processo_documento (
    nu_processo_documento bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_processo integer NOT NULL,
    nu_funcao_documental integer,
    nu_tipo_documento integer,
    ic_tipo_relacionamento character varying(50) NOT NULL
);


--
-- TOC entry 8920 (class 0 OID 0)
-- Dependencies: 2283
-- Name: TABLE mtrtb025_processo_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb025_processo_documento IS 'Tabela utilizada para identificar os documentos (por tipo ou função) que são necessario para os titulares do dossIê de um processo especifico. Fazendo um parelelo, seria como o os elementos do mapa de processo, porem voltados aos documentos do cliente, pois um dossiê pode ter a quantidade de clientes definida dinamicamente e por isso não cabe na estrutura do mapa do prcesso. Esta estrutura ficará a cargo dos elementos especificos do produto.
Quando um dossiê é criado, todos os CNPFs/CNPJs envolvidos na operação deverão apresentar os tipos de documentos ou algum documento da função documental definidas nesta relação com o processo especifico definido para o dossiê de produto.';


--
-- TOC entry 8921 (class 0 OID 0)
-- Dependencies: 2283
-- Name: COLUMN mtrtb025_processo_documento.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb025_processo_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8922 (class 0 OID 0)
-- Dependencies: 2283
-- Name: COLUMN mtrtb025_processo_documento.nu_processo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb025_processo_documento.nu_processo IS 'Atributo utilizado para identificar o processo de vinculação para agrupar os documentos necessarios.';


--
-- TOC entry 8923 (class 0 OID 0)
-- Dependencies: 2283
-- Name: COLUMN mtrtb025_processo_documento.nu_funcao_documental; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb025_processo_documento.nu_funcao_documental IS 'Atrinuto utilizado para referenciar uma função documental necessaria ao processo. Quando definido, qualquer documento valido que o cliente tenha que seja desta função documental, deve ser considerado que o documento existente já atende a necessidade. Caso este atributo esteja nulo, o atributo que representa o tipo de documento deverá estar prenchido.
';


--
-- TOC entry 8924 (class 0 OID 0)
-- Dependencies: 2283
-- Name: COLUMN mtrtb025_processo_documento.nu_tipo_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb025_processo_documento.nu_tipo_documento IS 'Atrinuto utilizado para referenciar um tipo de documento especifico, necessari ao processo. Quando definido, apenas a presença do documento especifica em estado valido, presente e associado ao dossiê do cliente deve ser considerado existente e já atende a necessidade. Caso este atributo esteja nulo, o atributo que representa a função documental deverá estar prenchido.
';


--
-- TOC entry 8925 (class 0 OID 0)
-- Dependencies: 2283
-- Name: COLUMN mtrtb025_processo_documento.ic_tipo_relacionamento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb025_processo_documento.ic_tipo_relacionamento IS 'Atributo que indica o tipo de relacionamento do cliente com o produto, podendo assumir os valores:
- TITULAR
- AVALISTA
- CONJUGE
- SOCIO
etc.';


--
-- TOC entry 2284 (class 1259 OID 1338409)
-- Name: mtrtb025_processo_documento_nu_processo_documento_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb025_processo_documento_nu_processo_documento_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8926 (class 0 OID 0)
-- Dependencies: 2284
-- Name: mtrtb025_processo_documento_nu_processo_documento_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb025_processo_documento_nu_processo_documento_seq OWNED BY mtrtb025_processo_documento.nu_processo_documento;


--
-- TOC entry 2285 (class 1259 OID 1338411)
-- Name: mtrtb026_formulario; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb026_formulario (
    nu_formulario integer NOT NULL,
    nu_versao integer NOT NULL,
    nu_processo integer NOT NULL,
    nu_fase_utilizacao integer NOT NULL
);


--
-- TOC entry 8927 (class 0 OID 0)
-- Dependencies: 2285
-- Name: TABLE mtrtb026_formulario; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb026_formulario IS 'Tabela responsavel por agrupar um conjunto de campos de entrada representando um formulario dinamico que podem ser utilizados para alimentar dados necessarios ao dossiê do produto de um determinado processo em uma determinada fase de utilização.
Dessa forma é possivel representar formularios distintos para cada fase do dossiê do produto.';


--
-- TOC entry 8928 (class 0 OID 0)
-- Dependencies: 2285
-- Name: COLUMN mtrtb026_formulario.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb026_formulario.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8929 (class 0 OID 0)
-- Dependencies: 2285
-- Name: COLUMN mtrtb026_formulario.nu_processo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb026_formulario.nu_processo IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8930 (class 0 OID 0)
-- Dependencies: 2285
-- Name: COLUMN mtrtb026_formulario.nu_fase_utilizacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb026_formulario.nu_fase_utilizacao IS 'Atributo utilizado para identificar a fase de associação do mapa junto ao dossiê.
Conforme a fase definida no dossiê do produto, o conjunto de imagens e campos do formulario ficam disponiveis para preenchimento, caso selecionado uma fase do processo diferente da atual do dossiê, o formulario e relação de documentos referente a outra fase ficam disponiveis apenas para consulta.
Caso o valor esteja preenchido com 0 significa que o mapa não deverá ser utilizado.';


--
-- TOC entry 2286 (class 1259 OID 1338414)
-- Name: mtrtb026_formulario_nu_formulario_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb026_formulario_nu_formulario_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8931 (class 0 OID 0)
-- Dependencies: 2286
-- Name: mtrtb026_formulario_nu_formulario_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb026_formulario_nu_formulario_seq OWNED BY mtrtb026_formulario.nu_formulario;


--
-- TOC entry 2287 (class 1259 OID 1338416)
-- Name: mtrtb027_campo_entrada; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb027_campo_entrada (
    nu_campo_entrada bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_formulario integer NOT NULL,
    no_campo character varying(50) NOT NULL,
    ic_tipo character varying(20) NOT NULL,
    ic_chave boolean NOT NULL,
    no_label character varying(50) NOT NULL,
    nu_largura integer NOT NULL,
    ic_obrigatorio boolean NOT NULL,
    de_mascara character varying(100),
    de_placeholder character varying(100),
    nu_tamanho_minimo integer,
    nu_tamanho_maximo integer,
    de_expressao text,
    ic_ativo boolean NOT NULL,
    nu_ordem integer NOT NULL
);


--
-- TOC entry 8932 (class 0 OID 0)
-- Dependencies: 2287
-- Name: TABLE mtrtb027_campo_entrada; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb027_campo_entrada IS 'Tabela responsavel por armazenar a estrutura de entradas de dados que serão alimentados na inclusão de um novo dossiê para o processo vinculado.
Esta estrutura permitirá realizar a construção dinamica do formulario
Registros desta tabela só devem ser excluidos fisicamente caso não exista nenhuma resposta de formulario atrelada a este registro. Caso essa situação ocorra o registro deve ser exluido logicamente definindo seu atributo ativo como false.';


--
-- TOC entry 8933 (class 0 OID 0)
-- Dependencies: 2287
-- Name: COLUMN mtrtb027_campo_entrada.nu_campo_entrada; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.nu_campo_entrada IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8934 (class 0 OID 0)
-- Dependencies: 2287
-- Name: COLUMN mtrtb027_campo_entrada.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8935 (class 0 OID 0)
-- Dependencies: 2287
-- Name: COLUMN mtrtb027_campo_entrada.no_campo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.no_campo IS 'Atributo que indica o nome do campo. Este nome pode ser utilizado pela estrutura de programação para referenciar a campo no formulario independente do label exposto para o usuário.';


--
-- TOC entry 8936 (class 0 OID 0)
-- Dependencies: 2287
-- Name: COLUMN mtrtb027_campo_entrada.ic_tipo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.ic_tipo IS 'Atributo utilizado para armazenar o tipo de campo de formulario que será gerado. Exemplos validos para este atributo são:
- TEXT
- SELECT
- RADIO';


--
-- TOC entry 8937 (class 0 OID 0)
-- Dependencies: 2287
-- Name: COLUMN mtrtb027_campo_entrada.ic_chave; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.ic_chave IS 'Atributo que indica se o campo do formulario pode ser utilizado como chave de pesquisa posterior.';


--
-- TOC entry 8938 (class 0 OID 0)
-- Dependencies: 2287
-- Name: COLUMN mtrtb027_campo_entrada.no_label; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.no_label IS 'Atributo que armazena o valor a ser exibido no label do campo do formulario para o usuario final.';


--
-- TOC entry 8939 (class 0 OID 0)
-- Dependencies: 2287
-- Name: COLUMN mtrtb027_campo_entrada.nu_largura; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.nu_largura IS 'Atributo que armazena o numero de colunas do bootstrap ocupadas pelo campo do formulario na estrutura de tela. Este valor pode variar de 1 a 12';


--
-- TOC entry 8940 (class 0 OID 0)
-- Dependencies: 2287
-- Name: COLUMN mtrtb027_campo_entrada.ic_obrigatorio; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.ic_obrigatorio IS 'Atributo que armazena o indicativo do obrigatoriedade do campo no formulario.';


--
-- TOC entry 8941 (class 0 OID 0)
-- Dependencies: 2287
-- Name: COLUMN mtrtb027_campo_entrada.de_mascara; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.de_mascara IS 'Atributo que armazena o valor da mascara de formatação do campo de for o caso.';


--
-- TOC entry 8942 (class 0 OID 0)
-- Dependencies: 2287
-- Name: COLUMN mtrtb027_campo_entrada.de_placeholder; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.de_placeholder IS 'Atributo que armazena o valor do placeholder para exibição no campo de for o caso.';


--
-- TOC entry 8943 (class 0 OID 0)
-- Dependencies: 2287
-- Name: COLUMN mtrtb027_campo_entrada.nu_tamanho_minimo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.nu_tamanho_minimo IS 'Atributo que armazena o numero de caracteres minimo utilizados em campos de texto livre.';


--
-- TOC entry 8944 (class 0 OID 0)
-- Dependencies: 2287
-- Name: COLUMN mtrtb027_campo_entrada.nu_tamanho_maximo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.nu_tamanho_maximo IS 'Atributo que armazena o numero de caracteres maximo utilizados em campos de texto livre.';


--
-- TOC entry 8945 (class 0 OID 0)
-- Dependencies: 2287
-- Name: COLUMN mtrtb027_campo_entrada.de_expressao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.de_expressao IS 'Atributo que armazena a expressão a ser aplicada pelo javascript para determinar a exposição ou não do campo no formulario.';


--
-- TOC entry 8946 (class 0 OID 0)
-- Dependencies: 2287
-- Name: COLUMN mtrtb027_campo_entrada.ic_ativo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.ic_ativo IS 'Atributo que indica se o campo de entrada esta apto ou não para ser inserido no formulario.';


--
-- TOC entry 8947 (class 0 OID 0)
-- Dependencies: 2287
-- Name: COLUMN mtrtb027_campo_entrada.nu_ordem; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.nu_ordem IS 'Atributo utilizado para definir a ordem de exibição dos campos do formulario.';


--
-- TOC entry 2288 (class 1259 OID 1338422)
-- Name: mtrtb027_campo_entrada_nu_campo_entrada_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb027_campo_entrada_nu_campo_entrada_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8948 (class 0 OID 0)
-- Dependencies: 2288
-- Name: mtrtb027_campo_entrada_nu_campo_entrada_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb027_campo_entrada_nu_campo_entrada_seq OWNED BY mtrtb027_campo_entrada.nu_campo_entrada;


--
-- TOC entry 2289 (class 1259 OID 1338424)
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


--
-- TOC entry 8949 (class 0 OID 0)
-- Dependencies: 2289
-- Name: TABLE mtrtb028_opcao_campo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb028_opcao_campo IS 'Tabela responsavel pelo armazenamento de opções pré-definidas para alguns tipos de atributos a exemplo:
- Lista;
- Radio;
- Check;

Registros desta tabela só devem ser excluidos fisicamente caso não exista nenhuma resposta de formulario atrelada a este registro. Caso essa situação ocorra o registro deve ser exluido logicamente definindo seu atributo ativo como false.';


--
-- TOC entry 8950 (class 0 OID 0)
-- Dependencies: 2289
-- Name: COLUMN mtrtb028_opcao_campo.nu_opcao_campo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb028_opcao_campo.nu_opcao_campo IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8951 (class 0 OID 0)
-- Dependencies: 2289
-- Name: COLUMN mtrtb028_opcao_campo.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb028_opcao_campo.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8952 (class 0 OID 0)
-- Dependencies: 2289
-- Name: COLUMN mtrtb028_opcao_campo.nu_campo_entrada; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb028_opcao_campo.nu_campo_entrada IS 'Atributo que identifica o campo de entrada do formulario ao qual a opção esta associada.';


--
-- TOC entry 8953 (class 0 OID 0)
-- Dependencies: 2289
-- Name: COLUMN mtrtb028_opcao_campo.no_value; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb028_opcao_campo.no_value IS 'Atributo utilizado para armazenar o valor que será definido como value da opção na interface grafica.';


--
-- TOC entry 8954 (class 0 OID 0)
-- Dependencies: 2289
-- Name: COLUMN mtrtb028_opcao_campo.no_opcao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb028_opcao_campo.no_opcao IS 'Atributo que armazena o valor da opção que será exibida para o usuario no campo do formulario.';


--
-- TOC entry 8955 (class 0 OID 0)
-- Dependencies: 2289
-- Name: COLUMN mtrtb028_opcao_campo.ic_ativo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb028_opcao_campo.ic_ativo IS 'Atributo que indica se a opção do campo de entrada esta apta ou não para ser inserido no campo de entrada do formulario.';


--
-- TOC entry 2290 (class 1259 OID 1338427)
-- Name: mtrtb028_opcao_campo_nu_opcao_campo_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb028_opcao_campo_nu_opcao_campo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8956 (class 0 OID 0)
-- Dependencies: 2290
-- Name: mtrtb028_opcao_campo_nu_opcao_campo_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb028_opcao_campo_nu_opcao_campo_seq OWNED BY mtrtb028_opcao_campo.nu_opcao_campo;


--
-- TOC entry 2291 (class 1259 OID 1338429)
-- Name: mtrtb029_campo_apresentacao; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb029_campo_apresentacao (
    nu_campo_apresentacao bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_campo_entrada bigint NOT NULL,
    nu_largura integer NOT NULL,
    ic_dispositivo character varying(1) NOT NULL
);


--
-- TOC entry 8957 (class 0 OID 0)
-- Dependencies: 2291
-- Name: TABLE mtrtb029_campo_apresentacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb029_campo_apresentacao IS 'Tabela utilizada para armazr informações acerca da apresentação do campo na interface grafica coforme o dispositivo';


--
-- TOC entry 8958 (class 0 OID 0)
-- Dependencies: 2291
-- Name: COLUMN mtrtb029_campo_apresentacao.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb029_campo_apresentacao.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8959 (class 0 OID 0)
-- Dependencies: 2291
-- Name: COLUMN mtrtb029_campo_apresentacao.nu_campo_entrada; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb029_campo_apresentacao.nu_campo_entrada IS 'Atributo que representa a o campo de entrada ao qual a forma de exibição referencia.';


--
-- TOC entry 8960 (class 0 OID 0)
-- Dependencies: 2291
-- Name: COLUMN mtrtb029_campo_apresentacao.nu_largura; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb029_campo_apresentacao.nu_largura IS 'Atributo que armazena o numero de colunas do bootstrap ocupadas pelo campo do formulario na estrutura de tela. Este valor pode variar de 1 a 12';


--
-- TOC entry 8961 (class 0 OID 0)
-- Dependencies: 2291
-- Name: COLUMN mtrtb029_campo_apresentacao.ic_dispositivo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb029_campo_apresentacao.ic_dispositivo IS 'Atributo utilizado para indicar o dispositivo de renderizaçãodo componente em tela.
Pode assumir as seguintes opções:
- W (Web)
- L (Low DPI)
- M (Medium DPI)
- H (High DPI)
- X (eXtra DPI)';


--
-- TOC entry 2292 (class 1259 OID 1338432)
-- Name: mtrtb029_campo_apresentacao_nu_campo_apresentacao_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb029_campo_apresentacao_nu_campo_apresentacao_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8962 (class 0 OID 0)
-- Dependencies: 2292
-- Name: mtrtb029_campo_apresentacao_nu_campo_apresentacao_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb029_campo_apresentacao_nu_campo_apresentacao_seq OWNED BY mtrtb029_campo_apresentacao.nu_campo_apresentacao;


--
-- TOC entry 2293 (class 1259 OID 1338434)
-- Name: mtrtb030_resposta_dossie; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb030_resposta_dossie (
    nu_resposta_dossie bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_dossie_produto bigint NOT NULL,
    nu_campo_entrada bigint NOT NULL,
    de_resposta text
);


--
-- TOC entry 8963 (class 0 OID 0)
-- Dependencies: 2293
-- Name: TABLE mtrtb030_resposta_dossie; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb030_resposta_dossie IS 'Tabela responsavel pelo armazenamento das respostas aos itens montados dos formularios de inclusão de processos para um dosiê especifico.';


--
-- TOC entry 8964 (class 0 OID 0)
-- Dependencies: 2293
-- Name: COLUMN mtrtb030_resposta_dossie.nu_resposta_dossie; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb030_resposta_dossie.nu_resposta_dossie IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8965 (class 0 OID 0)
-- Dependencies: 2293
-- Name: COLUMN mtrtb030_resposta_dossie.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb030_resposta_dossie.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8966 (class 0 OID 0)
-- Dependencies: 2293
-- Name: COLUMN mtrtb030_resposta_dossie.nu_dossie_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb030_resposta_dossie.nu_dossie_produto IS 'Atributo utilizado para identificar o dossiê de produto ao qual a resposta esta vinculada.';


--
-- TOC entry 8967 (class 0 OID 0)
-- Dependencies: 2293
-- Name: COLUMN mtrtb030_resposta_dossie.nu_campo_entrada; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb030_resposta_dossie.nu_campo_entrada IS 'Atributo utilizado para identificar o campo do formulario dinamico ao qual a resposta esta vinculada.';


--
-- TOC entry 8968 (class 0 OID 0)
-- Dependencies: 2293
-- Name: COLUMN mtrtb030_resposta_dossie.de_resposta; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb030_resposta_dossie.de_resposta IS 'Atributo utilizado para armazenar a resposta informada no formulario nos casos de atributos em texto aberto.';


--
-- TOC entry 2294 (class 1259 OID 1338440)
-- Name: mtrtb030_resposta_dossie_nu_resposta_dossie_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb030_resposta_dossie_nu_resposta_dossie_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8969 (class 0 OID 0)
-- Dependencies: 2294
-- Name: mtrtb030_resposta_dossie_nu_resposta_dossie_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb030_resposta_dossie_nu_resposta_dossie_seq OWNED BY mtrtb030_resposta_dossie.nu_resposta_dossie;


--
-- TOC entry 2295 (class 1259 OID 1338442)
-- Name: mtrtb031_resposta_opcao; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb031_resposta_opcao (
    nu_opcao_campo bigint NOT NULL,
    nu_resposta_dossie bigint NOT NULL
);


--
-- TOC entry 8970 (class 0 OID 0)
-- Dependencies: 2295
-- Name: TABLE mtrtb031_resposta_opcao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb031_resposta_opcao IS 'Tabela de relacionamento com finalidade de armazenar todas as respostas objetivas informadas pelo cliente a mesma pergunta no formulario de identificação do dossiê.';


--
-- TOC entry 8971 (class 0 OID 0)
-- Dependencies: 2295
-- Name: COLUMN mtrtb031_resposta_opcao.nu_opcao_campo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb031_resposta_opcao.nu_opcao_campo IS 'Atributo que representa a opção selecionada vinculado na relação com a resposta do formulario.';


--
-- TOC entry 8972 (class 0 OID 0)
-- Dependencies: 2295
-- Name: COLUMN mtrtb031_resposta_opcao.nu_resposta_dossie; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb031_resposta_opcao.nu_resposta_dossie IS 'Atributo que representa a resposta vinculada na relação com a opção selecionada do campo.';


--
-- TOC entry 2296 (class 1259 OID 1338445)
-- Name: mtrtb032_elemento_conteudo; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb032_elemento_conteudo (
    nu_elemento_conteudo bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_elemento_vinculador bigint,
    nu_produto integer,
    nu_processo integer,
    nu_tipo_documento integer,
    ic_obrigatorio boolean NOT NULL,
    nu_qtde_obrigatorio integer,
    ic_validar boolean NOT NULL,
    no_campo character varying(50) NOT NULL,
    de_expressao text,
    nu_fase_utilizacao integer NOT NULL,
    no_elemento character varying(100)
);


--
-- TOC entry 8973 (class 0 OID 0)
-- Dependencies: 2296
-- Name: TABLE mtrtb032_elemento_conteudo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb032_elemento_conteudo IS 'Tabela responsavel pelo armazenamento dos elementos que compoem o mapa de documentos para vinculacao ao processo.
Esse elementos estão associados aos tipos de documentos para identicação dos mesmo na atoa da captura.';


--
-- TOC entry 8974 (class 0 OID 0)
-- Dependencies: 2296
-- Name: COLUMN mtrtb032_elemento_conteudo.nu_elemento_conteudo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_elemento_conteudo IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8975 (class 0 OID 0)
-- Dependencies: 2296
-- Name: COLUMN mtrtb032_elemento_conteudo.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8976 (class 0 OID 0)
-- Dependencies: 2296
-- Name: COLUMN mtrtb032_elemento_conteudo.nu_elemento_vinculador; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_elemento_vinculador IS 'Atributo utilizado para armazenar uma outra instancia de elemento ao qual o elemento se vincula.
Esta estrutura permite criar uma estrutura hierarquizada de elementos, porem elementos só devem ser vinculados a outros elementos que não sejam finais, ou seja, não sejam associados a nenhum tipo de elemento que seja associado a um tipo de documento.';


--
-- TOC entry 8977 (class 0 OID 0)
-- Dependencies: 2296
-- Name: COLUMN mtrtb032_elemento_conteudo.nu_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_produto IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8978 (class 0 OID 0)
-- Dependencies: 2296
-- Name: COLUMN mtrtb032_elemento_conteudo.nu_processo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_processo IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8979 (class 0 OID 0)
-- Dependencies: 2296
-- Name: COLUMN mtrtb032_elemento_conteudo.nu_tipo_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_tipo_documento IS 'Atributo utilizado para identificar o elemento de ponta que possui vinculo com algum tipo de documento.';


--
-- TOC entry 8980 (class 0 OID 0)
-- Dependencies: 2296
-- Name: COLUMN mtrtb032_elemento_conteudo.ic_obrigatorio; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.ic_obrigatorio IS 'Atributo para indicar se o elemento é de submissão obrigatoria ou não de forma individual.';


--
-- TOC entry 8981 (class 0 OID 0)
-- Dependencies: 2296
-- Name: COLUMN mtrtb032_elemento_conteudo.nu_qtde_obrigatorio; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_qtde_obrigatorio IS 'Este atributo indica a quantidade de elementos que são de tipo de elemento final obrigatorios dentro da sua arvore e só deve ser preenchido se o tipo do elemento permitir agrupamento: Exemplo:
- Identificação (Este elemento deve ter 2 filhos obrigatorios)
   |-- RG
   |-- CNH
   |-- Pasaporte
   |-- CTPS';


--
-- TOC entry 8982 (class 0 OID 0)
-- Dependencies: 2296
-- Name: COLUMN mtrtb032_elemento_conteudo.ic_validar; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.ic_validar IS 'Atributo que indica se o documento deve ser validado quando apresentado no processo.
Caso verdadeiro, a instancia do documento deve ser criada com a situação vazia.
Caso false, a instancia do documento deve ser criada com a situação de aprovada conforme regra de negocio realizada pelo sistema, desde que já exista outra instancia do mesmo documento com situação aprovada previamente.';


--
-- TOC entry 8983 (class 0 OID 0)
-- Dependencies: 2296
-- Name: COLUMN mtrtb032_elemento_conteudo.no_campo; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.no_campo IS 'Atributo que indica o nome do campo. Este nome pode ser utilizado pela estrutura de programação para referenciar a elemento do documento na interface independente do label exposto para o usuário.';


--
-- TOC entry 8984 (class 0 OID 0)
-- Dependencies: 2296
-- Name: COLUMN mtrtb032_elemento_conteudo.de_expressao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.de_expressao IS 'Atributo que armazena a expressão a ser aplicada pelo javascript para determinar a exposição ou não do elemento para submissão.';


--
-- TOC entry 8985 (class 0 OID 0)
-- Dependencies: 2296
-- Name: COLUMN mtrtb032_elemento_conteudo.nu_fase_utilizacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_fase_utilizacao IS 'Atributo utilizado para identificar a fase de associação do mapa junto ao dossiê.
Conforme a fase definida no dossiê do produto, o conjunto de imagens e campos do formulario ficam disponiveis para preenchimento, caso selecionado uma fase do processo diferente da atual do dossiê, o formulario e relação de documentos referente a outra fase ficam disponiveis apenas para consulta.
Caso o valor esteja preenchido com 0 significa que o mapa não deverá ser utilizado.';


--
-- TOC entry 8986 (class 0 OID 0)
-- Dependencies: 2296
-- Name: COLUMN mtrtb032_elemento_conteudo.no_elemento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.no_elemento IS 'Atributo utilizado para armazenar o nome de apresentação do tipo de elemento de conteudo.
Este atributo deve estar preenchido quando a vinculação com o tipo de documento for nula, pois nesta situação o valor desta tabela será apresentado na interface grafica. O registro que possuir vinculação com o tipo de documento, será o nome deste que deverá ser exposto.';


--
-- TOC entry 2297 (class 1259 OID 1338451)
-- Name: mtrtb032_elemento_conteudo_nu_elemento_conteudo_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb032_elemento_conteudo_nu_elemento_conteudo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8987 (class 0 OID 0)
-- Dependencies: 2297
-- Name: mtrtb032_elemento_conteudo_nu_elemento_conteudo_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb032_elemento_conteudo_nu_elemento_conteudo_seq OWNED BY mtrtb032_elemento_conteudo.nu_elemento_conteudo;


--
-- TOC entry 2298 (class 1259 OID 1338453)
-- Name: mtrtb033_garantia; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb033_garantia (
    nu_garantia integer NOT NULL,
    nu_versao integer NOT NULL,
    nu_operacao integer NOT NULL,
    no_garantia character varying(255) NOT NULL
);


--
-- TOC entry 8988 (class 0 OID 0)
-- Dependencies: 2298
-- Name: TABLE mtrtb033_garantia; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb033_garantia IS 'Tabela responsavel pelo armazenamento das garantias da CAIXA que serão vinculados aos dossiês criados';


--
-- TOC entry 8989 (class 0 OID 0)
-- Dependencies: 2298
-- Name: COLUMN mtrtb033_garantia.nu_garantia; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb033_garantia.nu_garantia IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8990 (class 0 OID 0)
-- Dependencies: 2298
-- Name: COLUMN mtrtb033_garantia.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb033_garantia.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8991 (class 0 OID 0)
-- Dependencies: 2298
-- Name: COLUMN mtrtb033_garantia.nu_operacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb033_garantia.nu_operacao IS 'Atributo que armazena o numero de operação corporativa da garantia';


--
-- TOC entry 8992 (class 0 OID 0)
-- Dependencies: 2298
-- Name: COLUMN mtrtb033_garantia.no_garantia; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb033_garantia.no_garantia IS 'Atributo que armazena o nome corporativo da garantia';


--
-- TOC entry 2299 (class 1259 OID 1338456)
-- Name: mtrtb033_garantia_nu_garantia_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb033_garantia_nu_garantia_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 8993 (class 0 OID 0)
-- Dependencies: 2299
-- Name: mtrtb033_garantia_nu_garantia_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb033_garantia_nu_garantia_seq OWNED BY mtrtb033_garantia.nu_garantia;


--
-- TOC entry 2300 (class 1259 OID 1338458)
-- Name: mtrtb034_garantia_produto; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb034_garantia_produto (
    nu_produto integer NOT NULL,
    nu_garantia integer NOT NULL
);


--
-- TOC entry 8994 (class 0 OID 0)
-- Dependencies: 2300
-- Name: TABLE mtrtb034_garantia_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb034_garantia_produto IS 'Tabela de rlacionamento responsavel por vincular as garantias possiveis de exibição quando selecionado um dado produto.';


--
-- TOC entry 8995 (class 0 OID 0)
-- Dependencies: 2300
-- Name: COLUMN mtrtb034_garantia_produto.nu_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb034_garantia_produto.nu_produto IS 'Atributo utilizado para armazenar a referência do produto da relação com a garantia';


--
-- TOC entry 8996 (class 0 OID 0)
-- Dependencies: 2300
-- Name: COLUMN mtrtb034_garantia_produto.nu_garantia; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb034_garantia_produto.nu_garantia IS 'Atributo utilizado para armazenar a referência da garantia da relação com o produto';


--
-- TOC entry 2301 (class 1259 OID 1338461)
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
    ic_forma_garantia character varying(3) DEFAULT '0'::character varying NOT NULL
);


--
-- TOC entry 8997 (class 0 OID 0)
-- Dependencies: 2301
-- Name: TABLE mtrtb035_garantia_informada; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb035_garantia_informada IS 'Tabela responsavel por manter a lista de garantias informadas durante o ciclpo de vida do dossiê do produto.
Os documentos submetidos são arquivados normalmente na tabela de documentos e vinculados ao dossiê do produto atrvés de instâncias.';


--
-- TOC entry 8998 (class 0 OID 0)
-- Dependencies: 2301
-- Name: COLUMN mtrtb035_garantia_informada.nu_garantia_informada; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb035_garantia_informada.nu_garantia_informada IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8999 (class 0 OID 0)
-- Dependencies: 2301
-- Name: COLUMN mtrtb035_garantia_informada.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb035_garantia_informada.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 9000 (class 0 OID 0)
-- Dependencies: 2301
-- Name: COLUMN mtrtb035_garantia_informada.nu_dossie_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb035_garantia_informada.nu_dossie_produto IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 9001 (class 0 OID 0)
-- Dependencies: 2301
-- Name: COLUMN mtrtb035_garantia_informada.nu_garantia; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb035_garantia_informada.nu_garantia IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 9002 (class 0 OID 0)
-- Dependencies: 2301
-- Name: COLUMN mtrtb035_garantia_informada.nu_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb035_garantia_informada.nu_produto IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 9003 (class 0 OID 0)
-- Dependencies: 2301
-- Name: COLUMN mtrtb035_garantia_informada.vr_garantia_informada; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb035_garantia_informada.vr_garantia_informada IS 'Valor informado da garantia oferecida no dia da simulação';


--
-- TOC entry 9004 (class 0 OID 0)
-- Dependencies: 2301
-- Name: COLUMN mtrtb035_garantia_informada.pc_garantia_informada; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb035_garantia_informada.pc_garantia_informada IS 'Percentual da garantia informada em relação ao valor pretendido, podendo ser o valor da PMT, da operação, do saldo devedor, etc.';


--
-- TOC entry 9005 (class 0 OID 0)
-- Dependencies: 2301
-- Name: COLUMN mtrtb035_garantia_informada.ic_forma_garantia; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb035_garantia_informada.ic_forma_garantia IS 'Definição da forma de utilização da garantia, para o campo pc_garantia_informada.
Valores possíveis:
SDD - Saldo devedor; 
VRC - Valor contratado; 
PMT - PMT;
LMD - Limite Disponibilizado; 
LMC - Limite Contratado;
';


--
-- TOC entry 2302 (class 1259 OID 1338466)
-- Name: mtrtb035_garantia_informada_nu_garantia_informada_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb035_garantia_informada_nu_garantia_informada_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 9006 (class 0 OID 0)
-- Dependencies: 2302
-- Name: mtrtb035_garantia_informada_nu_garantia_informada_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb035_garantia_informada_nu_garantia_informada_seq OWNED BY mtrtb035_garantia_informada.nu_garantia_informada;


--
-- TOC entry 2303 (class 1259 OID 1338468)
-- Name: mtrtb036_composicao_documental; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb036_composicao_documental (
    nu_composicao_documental bigint NOT NULL,
    nu_versao integer NOT NULL,
    no_composicao_documental character varying(100) NOT NULL,
    ts_inclusao timestamp without time zone NOT NULL,
    ts_revogacao timestamp without time zone,
    co_matricula_inclusao character varying(7) NOT NULL,
    co_matricula_revogacao character varying(7)
);


--
-- TOC entry 9007 (class 0 OID 0)
-- Dependencies: 2303
-- Name: TABLE mtrtb036_composicao_documental; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb036_composicao_documental IS 'Tabela responsavel por agrupar tipos de documentos visando criar estruturas que representam conjuntos de tipos de documentos a serem analisados conjuntamente.
Essa conjunção será utilizada na analise do nivel documento e por ser formada como os exemplos a seguir:
- RG
- Conta Concercionaria
- Contra Cheque
-----------------------------------------------------------------------------------
- CNH
- Conta Concercionaria
- DIRPF';


--
-- TOC entry 9008 (class 0 OID 0)
-- Dependencies: 2303
-- Name: COLUMN mtrtb036_composicao_documental.nu_composicao_documental; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb036_composicao_documental.nu_composicao_documental IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 9009 (class 0 OID 0)
-- Dependencies: 2303
-- Name: COLUMN mtrtb036_composicao_documental.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb036_composicao_documental.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 9010 (class 0 OID 0)
-- Dependencies: 2303
-- Name: COLUMN mtrtb036_composicao_documental.no_composicao_documental; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb036_composicao_documental.no_composicao_documental IS 'Atributo utilizado para armazenar o nome negovial da composição de documentos';


--
-- TOC entry 9011 (class 0 OID 0)
-- Dependencies: 2303
-- Name: COLUMN mtrtb036_composicao_documental.ts_inclusao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb036_composicao_documental.ts_inclusao IS 'Atributo que armazena a data/hora de cadastro do registro da composição documental';


--
-- TOC entry 9012 (class 0 OID 0)
-- Dependencies: 2303
-- Name: COLUMN mtrtb036_composicao_documental.ts_revogacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb036_composicao_documental.ts_revogacao IS 'Atributo que armazena a data/hora de revogação do registro da composição documental';


--
-- TOC entry 9013 (class 0 OID 0)
-- Dependencies: 2303
-- Name: COLUMN mtrtb036_composicao_documental.co_matricula_inclusao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb036_composicao_documental.co_matricula_inclusao IS 'Atributo que armazena a matricula do usuario/serviço que realizou o cadastro do registro da composição documental';


--
-- TOC entry 9014 (class 0 OID 0)
-- Dependencies: 2303
-- Name: COLUMN mtrtb036_composicao_documental.co_matricula_revogacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb036_composicao_documental.co_matricula_revogacao IS 'Atributo que armazena a matricula do usuario/serviço que realizou a revogação do registro da composição documental';


--
-- TOC entry 2304 (class 1259 OID 1338471)
-- Name: mtrtb036_composicao_documental_nu_composicao_documental_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb036_composicao_documental_nu_composicao_documental_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 9015 (class 0 OID 0)
-- Dependencies: 2304
-- Name: mtrtb036_composicao_documental_nu_composicao_documental_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb036_composicao_documental_nu_composicao_documental_seq OWNED BY mtrtb036_composicao_documental.nu_composicao_documental;


--
-- TOC entry 2305 (class 1259 OID 1338473)
-- Name: mtrtb037_regra_documental; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb037_regra_documental (
    nu_regra_documental bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_composicao_documental bigint NOT NULL,
    nu_tipo_documento integer,
    nu_funcao_documental integer,
    nu_canal_captura integer,
    in_antifraude numeric(10,5)
);


--
-- TOC entry 9016 (class 0 OID 0)
-- Dependencies: 2305
-- Name: TABLE mtrtb037_regra_documental; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb037_regra_documental IS 'Tabela utilizada para armazenar as regras de atendimento da composição. Para que uma composição documental esteja satisfeita, todas as regras a ela associadas devem ser atendidas, ou seja, a regra para cada documento definido deve ser verdadeira. Além da presença do documento vinculado valida no dossiê situações como indice minimo do antifraude e canal devem ser respeitadas. Caso não seja atendida ao menos uma das regras, a composição não terá suas condições satisfatorias atendidas e consequentemente o nivel documental não poderá ser atribuido ao dossiê do cliente.';


--
-- TOC entry 9017 (class 0 OID 0)
-- Dependencies: 2305
-- Name: COLUMN mtrtb037_regra_documental.nu_regra_documental; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb037_regra_documental.nu_regra_documental IS 'Atributo que representa a chave primaria da entidade';


--
-- TOC entry 9018 (class 0 OID 0)
-- Dependencies: 2305
-- Name: COLUMN mtrtb037_regra_documental.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb037_regra_documental.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 9019 (class 0 OID 0)
-- Dependencies: 2305
-- Name: COLUMN mtrtb037_regra_documental.nu_composicao_documental; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb037_regra_documental.nu_composicao_documental IS 'Atributo que representa composição de tipos de documentos associada aos possiveis tipos de documentos.';


--
-- TOC entry 9020 (class 0 OID 0)
-- Dependencies: 2305
-- Name: COLUMN mtrtb037_regra_documental.nu_tipo_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb037_regra_documental.nu_tipo_documento IS 'Atributo que representa o tipo de documento definido vinculado na relação com a composição de tipos de documentos. Caso este atributo esteja nulo, o atributo que representa a função documental deverá estar prenchido.';


--
-- TOC entry 9021 (class 0 OID 0)
-- Dependencies: 2305
-- Name: COLUMN mtrtb037_regra_documental.nu_funcao_documental; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb037_regra_documental.nu_funcao_documental IS 'Atributo que representa a função documental definida vinculada na relação com a composição de tipos de documentos. Caso este atributo esteja nulo, o atributo que representa o tipo de documento deverá estar prenchido.';


--
-- TOC entry 9022 (class 0 OID 0)
-- Dependencies: 2305
-- Name: COLUMN mtrtb037_regra_documental.nu_canal_captura; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb037_regra_documental.nu_canal_captura IS 'Atributo utilizado para identificar o canal de captura especifico para categorizar o conjunto.
Caso este atributo seja nulo, ele permite ao conjunto valer-se de qualquer canal não especificado em outro conjunto para o mesmo documento e composição documental, porem tendo o canal especificado.';


--
-- TOC entry 9023 (class 0 OID 0)
-- Dependencies: 2305
-- Name: COLUMN mtrtb037_regra_documental.in_antifraude; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb037_regra_documental.in_antifraude IS 'Atributo utilizado para armazenar o valor minimo aceitavel do indice atribuido ao documento pelo sistema de antifraude para considerar o documento valido na composição documental permitindo atribuir o nivel documental ao dossiê do cliente.';


--
-- TOC entry 2306 (class 1259 OID 1338476)
-- Name: mtrtb037_regra_documental_nu_regra_documental_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb037_regra_documental_nu_regra_documental_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 9024 (class 0 OID 0)
-- Dependencies: 2306
-- Name: mtrtb037_regra_documental_nu_regra_documental_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb037_regra_documental_nu_regra_documental_seq OWNED BY mtrtb037_regra_documental.nu_regra_documental;


--
-- TOC entry 2307 (class 1259 OID 1338478)
-- Name: mtrtb038_nivel_documental; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb038_nivel_documental (
    nu_composicao_documental bigint NOT NULL,
    nu_dossie_cliente bigint NOT NULL
);


--
-- TOC entry 9025 (class 0 OID 0)
-- Dependencies: 2307
-- Name: TABLE mtrtb038_nivel_documental; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb038_nivel_documental IS 'Tabela responsavel por armazenar as referências de niveis dicumentais possiveis para associação a clientes e produtos.
O nivel documental é uma informação pertencente ao cliente, porém o mesmo deve estar associado a um conjunto de tipos de documentos e informações que torna a informação dinamica para o cliente, ou seja, se um cliente submete um determinado documento que aumenta sua gama de informações validas, ele pode ganhar um determinado nivel documental, porém se um documento passa a ter sua validade ultrapassada o cliente perde aquele determinado nivel.';


--
-- TOC entry 9026 (class 0 OID 0)
-- Dependencies: 2307
-- Name: COLUMN mtrtb038_nivel_documental.nu_composicao_documental; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb038_nivel_documental.nu_composicao_documental IS 'Atributo utilizado para identificar a composição documental que foi atingida ao atribuir o o nivel documental para o cliente';


--
-- TOC entry 9027 (class 0 OID 0)
-- Dependencies: 2307
-- Name: COLUMN mtrtb038_nivel_documental.nu_dossie_cliente; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb038_nivel_documental.nu_dossie_cliente IS 'Atributo que representa o dossiê do cliente vinculado na atribuição do nivel documental';


--
-- TOC entry 2308 (class 1259 OID 1338481)
-- Name: mtrtb039_produto_composicao; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb039_produto_composicao (
    nu_composicao_documental bigint NOT NULL,
    nu_produto integer NOT NULL
);


--
-- TOC entry 9028 (class 0 OID 0)
-- Dependencies: 2308
-- Name: TABLE mtrtb039_produto_composicao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb039_produto_composicao IS 'Tabela de relacionamento que vincula uma composição de documentos a um ou mais produtos.
Essa associação visa identificar as necessidade documentais para um determinado produto no ato de sua contratação, permitindo ao sistema autorizar ou não a operação do ponto de vista documental. ';


--
-- TOC entry 9029 (class 0 OID 0)
-- Dependencies: 2308
-- Name: COLUMN mtrtb039_produto_composicao.nu_composicao_documental; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb039_produto_composicao.nu_composicao_documental IS 'Atributo que representa o composição envolvida na relação com o produto';


--
-- TOC entry 9030 (class 0 OID 0)
-- Dependencies: 2308
-- Name: COLUMN mtrtb039_produto_composicao.nu_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb039_produto_composicao.nu_produto IS 'Atributo que representa o produto envolvido na relação com a composição';


--
-- TOC entry 2309 (class 1259 OID 1338484)
-- Name: mtrtb040_cadeia_situacao_dossie; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb040_cadeia_situacao_dossie (
    nu_tipo_situacao_atual integer NOT NULL,
    nu_tipo_situacao_seguinte integer NOT NULL
);


--
-- TOC entry 9031 (class 0 OID 0)
-- Dependencies: 2309
-- Name: TABLE mtrtb040_cadeia_situacao_dossie; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb040_cadeia_situacao_dossie IS 'Tabela utilizada para armazenar as possibilidades de tipos de situações  possiveis de aplicação em um dossiê de produto a partir um determinado tipo de situação';


--
-- TOC entry 9032 (class 0 OID 0)
-- Dependencies: 2309
-- Name: COLUMN mtrtb040_cadeia_situacao_dossie.nu_tipo_situacao_atual; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb040_cadeia_situacao_dossie.nu_tipo_situacao_atual IS 'Atributo que representa o tipo de situação atual na relação';


--
-- TOC entry 9033 (class 0 OID 0)
-- Dependencies: 2309
-- Name: COLUMN mtrtb040_cadeia_situacao_dossie.nu_tipo_situacao_seguinte; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb040_cadeia_situacao_dossie.nu_tipo_situacao_seguinte IS 'Atributo que representa o tipo de situação que pode ser aplicado como proximo tipo de situação de um dossiê de produto';


--
-- TOC entry 2310 (class 1259 OID 1338487)
-- Name: mtrtb041_cadeia_situacao_dcto; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb041_cadeia_situacao_dcto (
    nu_situacao_documento_atual integer NOT NULL,
    nu_situacao_documento_seguinte integer NOT NULL
);


--
-- TOC entry 9034 (class 0 OID 0)
-- Dependencies: 2310
-- Name: TABLE mtrtb041_cadeia_situacao_dcto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb041_cadeia_situacao_dcto IS 'Tabela utilizada para armazenar as possibilidades de tipos de situações possiveis de aplicação em uma instancia de documento a partir um determinado tipo de situação';


--
-- TOC entry 9035 (class 0 OID 0)
-- Dependencies: 2310
-- Name: COLUMN mtrtb041_cadeia_situacao_dcto.nu_situacao_documento_atual; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb041_cadeia_situacao_dcto.nu_situacao_documento_atual IS 'Atributo que representa o tipo de situação atual na relação';


--
-- TOC entry 9036 (class 0 OID 0)
-- Dependencies: 2310
-- Name: COLUMN mtrtb041_cadeia_situacao_dcto.nu_situacao_documento_seguinte; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb041_cadeia_situacao_dcto.nu_situacao_documento_seguinte IS 'Atributo que representa o tipo de situação que pode ser aplicado como proximo tipo de situação de uma instancia de documento.';


--
-- TOC entry 2311 (class 1259 OID 1338490)
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
    ic_tipo_pessoa character varying(1) NOT NULL
);


--
-- TOC entry 9037 (class 0 OID 0)
-- Dependencies: 2311
-- Name: TABLE mtrtb100_autorizacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb100_autorizacao IS 'Tabela utilizada para armazenar as autorizações relacionadas ao nivel documental geradas e entregues para os clientes.';


--
-- TOC entry 9038 (class 0 OID 0)
-- Dependencies: 2311
-- Name: COLUMN mtrtb100_autorizacao.nu_autorizacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.nu_autorizacao IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 9039 (class 0 OID 0)
-- Dependencies: 2311
-- Name: COLUMN mtrtb100_autorizacao.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 9040 (class 0 OID 0)
-- Dependencies: 2311
-- Name: COLUMN mtrtb100_autorizacao.co_autorizacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.co_autorizacao IS 'Atributo utilizado para armazenar o codigo de autorização gerado para entrega ao sistema de negocio e armazenamento junto ao dossiê do cliente';


--
-- TOC entry 9041 (class 0 OID 0)
-- Dependencies: 2311
-- Name: COLUMN mtrtb100_autorizacao.ts_registro; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.ts_registro IS 'Atributo utilizado para armazenar a datae hora de recebimento da solicitação de autorização ';


--
-- TOC entry 9042 (class 0 OID 0)
-- Dependencies: 2311
-- Name: COLUMN mtrtb100_autorizacao.ts_informe_negocio; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.ts_informe_negocio IS 'Atrbuto utilizado para armazenar a data e hora de entrega do codigo de autorização para o sistema de negocio solicitante.';


--
-- TOC entry 9043 (class 0 OID 0)
-- Dependencies: 2311
-- Name: COLUMN mtrtb100_autorizacao.co_protocolo_negocio; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.co_protocolo_negocio IS 'Atributo utilizado para armazenar o protocolo de confirmação de recebimento do codigo de autorização pelo sistema de negocio solicitante.';


--
-- TOC entry 9044 (class 0 OID 0)
-- Dependencies: 2311
-- Name: COLUMN mtrtb100_autorizacao.ts_informe_ecm; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.ts_informe_ecm IS 'Atrbuto utilizado para armazenar a data e hora de entrega do codigo de autorização para o sistema de ECM para armazenamento junto ao dossie do produto..';


--
-- TOC entry 9045 (class 0 OID 0)
-- Dependencies: 2311
-- Name: COLUMN mtrtb100_autorizacao.co_protocolo_ecm; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.co_protocolo_ecm IS 'Atributo utilizado para armazenar o protocolo de confirmação de recebimento do codigo de autorização pelo sistema de ECM.';


--
-- TOC entry 9046 (class 0 OID 0)
-- Dependencies: 2311
-- Name: COLUMN mtrtb100_autorizacao.nu_operacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.nu_operacao IS 'Atributo utilizado para armazenar o codigo de operação do produto solicitado na autorização.';


--
-- TOC entry 9047 (class 0 OID 0)
-- Dependencies: 2311
-- Name: COLUMN mtrtb100_autorizacao.nu_modalidade; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.nu_modalidade IS 'Atributo utilizado para armazenar o codigo da modalidade do produto solicitado na autorização.';


--
-- TOC entry 9048 (class 0 OID 0)
-- Dependencies: 2311
-- Name: COLUMN mtrtb100_autorizacao.no_produto; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.no_produto IS 'Atributo utilizado para armazenar o nome do produto solicitado na autorização.';


--
-- TOC entry 9049 (class 0 OID 0)
-- Dependencies: 2311
-- Name: COLUMN mtrtb100_autorizacao.nu_cpf_cnpj; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.nu_cpf_cnpj IS 'Atributo utilizado para armazenar o numero do CPF ou CNPJ do cliente relacionado com a autorização. ';


--
-- TOC entry 9050 (class 0 OID 0)
-- Dependencies: 2311
-- Name: COLUMN mtrtb100_autorizacao.ic_tipo_pessoa; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.ic_tipo_pessoa IS 'Atributo utilizado para indicar o tipo de pessoa, se fisica ou juridica podendo assumir os seguintes valores:
F - Fisica
J - Juridica';


--
-- TOC entry 2312 (class 1259 OID 1338493)
-- Name: mtrtb100_autorizacao_nu_autorizacao_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb100_autorizacao_nu_autorizacao_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 9051 (class 0 OID 0)
-- Dependencies: 2312
-- Name: mtrtb100_autorizacao_nu_autorizacao_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb100_autorizacao_nu_autorizacao_seq OWNED BY mtrtb100_autorizacao.nu_autorizacao;


--
-- TOC entry 2313 (class 1259 OID 1338495)
-- Name: mtrtb101_documento; Type: TABLE; Schema: mtrsm001; Owner: -
--

CREATE TABLE mtrtb101_documento (
    nu_documento bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_autorizacao bigint NOT NULL,
    nu_documento_cliente bigint NOT NULL
);


--
-- TOC entry 9052 (class 0 OID 0)
-- Dependencies: 2313
-- Name: TABLE mtrtb101_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON TABLE mtrtb101_documento IS 'Tabela utilizada para armazenar a informação dos documentos identificados e utilizados para a emissão da autorização.';


--
-- TOC entry 9053 (class 0 OID 0)
-- Dependencies: 2313
-- Name: COLUMN mtrtb101_documento.nu_documento; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb101_documento.nu_documento IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 9054 (class 0 OID 0)
-- Dependencies: 2313
-- Name: COLUMN mtrtb101_documento.nu_versao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb101_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 9055 (class 0 OID 0)
-- Dependencies: 2313
-- Name: COLUMN mtrtb101_documento.nu_autorizacao; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb101_documento.nu_autorizacao IS 'Atributo utilizado para identificar a autorização que esta relacionada ao documento';


--
-- TOC entry 9056 (class 0 OID 0)
-- Dependencies: 2313
-- Name: COLUMN mtrtb101_documento.nu_documento_cliente; Type: COMMENT; Schema: mtrsm001; Owner: -
--

COMMENT ON COLUMN mtrtb101_documento.nu_documento_cliente IS 'Atributo utilizado para armazenar a referencia do registro do documento utilizado na emissão da autorização';


--
-- TOC entry 2314 (class 1259 OID 1338498)
-- Name: mtrtb101_documento_nu_documento_seq; Type: SEQUENCE; Schema: mtrsm001; Owner: -
--

CREATE SEQUENCE mtrtb101_documento_nu_documento_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 9057 (class 0 OID 0)
-- Dependencies: 2314
-- Name: mtrtb101_documento_nu_documento_seq; Type: SEQUENCE OWNED BY; Schema: mtrsm001; Owner: -
--

ALTER SEQUENCE mtrtb101_documento_nu_documento_seq OWNED BY mtrtb101_documento.nu_documento;


--
-- TOC entry 2315 (class 1259 OID 1338500)
-- Name: vw_dossie_cliente; Type: VIEW; Schema: mtrsm001; Owner: -
--

CREATE VIEW vw_dossie_cliente AS
 SELECT DISTINCT dossieclie0_.nu_dossie_cliente AS nu_dossi1_0_0_,
    documento2_.nu_documento AS nu_docum1_4_1_,
    conteudos3_.nu_conteudo AS nu_conte1_9_2_,
    tipodocume4_.nu_tipo_documento AS nu_tipo_1_10_3_,
    funcaodocu6_.nu_funcao_documental AS nu_funca1_11_4_,
    dossieclie0_.nu_versao AS nu_versa2_0_0_,
    dossieclie0_.nu_cpf_cnpj AS nu_cpf_c3_0_0_,
    dossieclie0_.no_cliente AS no_clien4_0_0_,
    dossieclie0_.de_telefone AS de_telef5_0_0_,
    dossieclie0_.ic_tipo_pessoa AS ic_tipo_6_0_0_,
    dossieclie0_1_.dt_nascimento AS dt_nasci1_1_0_,
    dossieclie0_1_.ic_estado_civil_antigo AS ic_estad2_1_0_,
    dossieclie0_1_.no_mae AS no_mae3_1_0_,
    dossieclie0_1_.no_pai AS no_pai4_1_0_,
    dossieclie0_1_.nu_identidade AS nu_ident5_1_0_,
    dossieclie0_1_.nu_nis AS nu_nis6_1_0_,
    dossieclie0_1_.no_orgao_emissor AS no_orgao7_1_0_,
    dossieclie0_2_.dt_fundacao AS dt_funda1_2_0_,
    dossieclie0_2_.no_razao_social AS no_razao2_2_0_,
    dossieclie0_2_.ic_segmento AS ic_segme3_2_0_,
        CASE
            WHEN (dossieclie0_1_.nu_dossie_cliente IS NOT NULL) THEN 1
            WHEN (dossieclie0_2_.nu_dossie_cliente IS NOT NULL) THEN 2
            WHEN (dossieclie0_.nu_dossie_cliente IS NOT NULL) THEN 0
            ELSE NULL::integer
        END AS clazz_0_,
    documento2_.nu_versao AS nu_versa2_4_1_,
    documento2_.nu_canal_captura AS nu_canal7_4_1_,
    documento2_.ts_captura AS ts_captu3_4_1_,
    documento2_.dt_validade AS dt_valid4_4_1_,
    documento2_.ic_dossie_digital AS ic_dossi5_4_1_,
    documento2_.co_matricula AS co_matri6_4_1_,
    documento2_.nu_tipo_documento AS nu_tipo_8_4_1_,
    documentos1_.nu_dossie_cliente AS nu_dossi1_6_0__,
    documentos1_.nu_documento AS nu_docum2_6_0__,
    conteudos3_.nu_versao AS nu_versa2_9_2_,
    conteudos3_.co_ged AS co_ged3_9_2_,
    conteudos3_.nu_documento AS nu_docum7_9_2_,
    conteudos3_.no_formato AS no_forma4_9_2_,
    conteudos3_.nu_ordem AS nu_ordem5_9_2_,
    conteudos3_.de_uri AS de_uri6_9_2_,
    conteudos3_.nu_documento AS nu_docum7_9_1__,
    conteudos3_.nu_conteudo AS nu_conte1_9_1__,
    tipodocume4_.nu_versao AS nu_versa2_10_3_,
    tipodocume4_.co_tipologia AS co_tipol3_10_3_,
    tipodocume4_.no_tipo_documento AS no_tipo_4_10_3_,
    tipodocume4_.pz_validade_dias AS pz_valid5_10_3_,
    tipodocume4_.ic_tipo_pessoa AS ic_tipo_6_10_3_,
    tipodocume4_.ic_validade_auto_contida AS ic_valid7_10_3_,
    funcaodocu6_.nu_versao AS nu_versa2_11_4_,
    funcaodocu6_.ic_ativo AS ic_ativo3_11_4_,
    funcaodocu6_.no_funcao AS no_funca4_11_4_,
    funcoesdoc5_.nu_tipo_documento AS nu_tipo_1_12_2__,
    funcoesdoc5_.nu_funcao_documental AS nu_funca2_12_2__
   FROM ((((((((mtrtb001_dossie_cliente dossieclie0_
     LEFT JOIN mtrtb001_pessoa_fisica dossieclie0_1_ ON ((dossieclie0_.nu_dossie_cliente = dossieclie0_1_.nu_dossie_cliente)))
     LEFT JOIN mtrtb001_pessoa_juridica dossieclie0_2_ ON ((dossieclie0_.nu_dossie_cliente = dossieclie0_2_.nu_dossie_cliente)))
     LEFT JOIN mtrtb005_documento_cliente documentos1_ ON ((dossieclie0_.nu_dossie_cliente = documentos1_.nu_dossie_cliente)))
     LEFT JOIN mtrtb003_documento documento2_ ON ((documentos1_.nu_documento = documento2_.nu_documento)))
     LEFT JOIN mtrtb008_conteudo conteudos3_ ON ((documento2_.nu_documento = conteudos3_.nu_documento)))
     LEFT JOIN mtrtb009_tipo_documento tipodocume4_ ON ((documento2_.nu_tipo_documento = tipodocume4_.nu_tipo_documento)))
     LEFT JOIN mtrtb011_funcao_documento funcoesdoc5_ ON ((tipodocume4_.nu_tipo_documento = funcoesdoc5_.nu_tipo_documento)))
     LEFT JOIN mtrtb010_funcao_documental funcaodocu6_ ON ((funcoesdoc5_.nu_funcao_documental = funcaodocu6_.nu_funcao_documental)))
  WHERE ((dossieclie0_.nu_cpf_cnpj = 92476899291::bigint) AND ((dossieclie0_.ic_tipo_pessoa)::text = 'F'::text));


--
-- TOC entry 8188 (class 2604 OID 1338505)
-- Name: nu_dossie_cliente; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb001_dossie_cliente ALTER COLUMN nu_dossie_cliente SET DEFAULT nextval('mtrtb001_dossie_cliente_nu_dossie_cliente_seq'::regclass);


--
-- TOC entry 8189 (class 2604 OID 1338506)
-- Name: nu_dossie_produto; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb002_dossie_produto ALTER COLUMN nu_dossie_produto SET DEFAULT nextval('mtrtb002_dossie_produto_nu_dossie_produto_seq'::regclass);


--
-- TOC entry 8190 (class 2604 OID 1338507)
-- Name: nu_documento; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb003_documento ALTER COLUMN nu_documento SET DEFAULT nextval('mtrtb003_documento_nu_documento_seq'::regclass);


--
-- TOC entry 8191 (class 2604 OID 1338508)
-- Name: nu_dossie_cliente_produto; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb004_dossie_cliente_produto ALTER COLUMN nu_dossie_cliente_produto SET DEFAULT nextval('mtrtb004_dossie_cliente_produto_nu_dossie_cliente_produto_seq'::regclass);


--
-- TOC entry 8192 (class 2604 OID 1338509)
-- Name: nu_canal_captura; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb006_canal_captura ALTER COLUMN nu_canal_captura SET DEFAULT nextval('mtrtb006_canal_captura_nu_canal_captura_seq'::regclass);


--
-- TOC entry 8193 (class 2604 OID 1338510)
-- Name: nu_atributo_documento; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb007_atributo_documento ALTER COLUMN nu_atributo_documento SET DEFAULT nextval('mtrtb007_atributo_documento_nu_atributo_documento_seq'::regclass);


--
-- TOC entry 8194 (class 2604 OID 1338511)
-- Name: nu_conteudo; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb008_conteudo ALTER COLUMN nu_conteudo SET DEFAULT nextval('mtrtb008_conteudo_nu_conteudo_seq'::regclass);


--
-- TOC entry 8195 (class 2604 OID 1338512)
-- Name: nu_tipo_documento; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb009_tipo_documento ALTER COLUMN nu_tipo_documento SET DEFAULT nextval('mtrtb009_tipo_documento_nu_tipo_documento_seq'::regclass);


--
-- TOC entry 8196 (class 2604 OID 1338513)
-- Name: nu_funcao_documental; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb010_funcao_documental ALTER COLUMN nu_funcao_documental SET DEFAULT nextval('mtrtb010_funcao_documental_nu_funcao_documental_seq'::regclass);


--
-- TOC entry 8197 (class 2604 OID 1338514)
-- Name: nu_tipo_situacao_dossie; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb012_tipo_situacao_dossie ALTER COLUMN nu_tipo_situacao_dossie SET DEFAULT nextval('mtrtb012_tipo_situacao_dossie_nu_tipo_situacao_dossie_seq'::regclass);


--
-- TOC entry 8200 (class 2604 OID 1338515)
-- Name: nu_situacao_dossie; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb013_situacao_dossie ALTER COLUMN nu_situacao_dossie SET DEFAULT nextval('mtrtb013_situacao_dossie_nu_situacao_dossie_seq'::regclass);


--
-- TOC entry 8201 (class 2604 OID 1338516)
-- Name: nu_instancia_documento; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb014_instancia_documento ALTER COLUMN nu_instancia_documento SET DEFAULT nextval('mtrtb014_instancia_documento_nu_instancia_documento_seq'::regclass);


--
-- TOC entry 8202 (class 2604 OID 1338517)
-- Name: nu_situacao_documento; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb015_situacao_documento ALTER COLUMN nu_situacao_documento SET DEFAULT nextval('mtrtb015_situacao_documento_nu_situacao_documento_seq'::regclass);


--
-- TOC entry 8205 (class 2604 OID 1338518)
-- Name: nu_motivo_situacao_dcto; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb016_motivo_situacao_dcto ALTER COLUMN nu_motivo_situacao_dcto SET DEFAULT nextval('mtrtb016_motivo_situacao_dcto_nu_motivo_situacao_dcto_seq'::regclass);


--
-- TOC entry 8206 (class 2604 OID 1338519)
-- Name: nu_situacao_instancia_dcto; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb017_situacao_instancia_dct ALTER COLUMN nu_situacao_instancia_dcto SET DEFAULT nextval('mtrtb017_situacao_instancia_dct_nu_situacao_instancia_dcto_seq'::regclass);


--
-- TOC entry 8207 (class 2604 OID 1338520)
-- Name: nu_macroprocesso; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb019_macroprocesso ALTER COLUMN nu_macroprocesso SET DEFAULT nextval('mtrtb019_macroprocesso_nu_macroprocesso_seq'::regclass);


--
-- TOC entry 8208 (class 2604 OID 1338521)
-- Name: nu_processo; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb020_processo ALTER COLUMN nu_processo SET DEFAULT nextval('mtrtb020_processo_nu_processo_seq'::regclass);


--
-- TOC entry 8209 (class 2604 OID 1338522)
-- Name: nu_unidade_autorizada; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb021_unidade_autorizada ALTER COLUMN nu_unidade_autorizada SET DEFAULT nextval('mtrtb021_unidade_autorizada_nu_unidade_autorizada_seq'::regclass);


--
-- TOC entry 8210 (class 2604 OID 1338523)
-- Name: nu_produto; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb022_produto ALTER COLUMN nu_produto SET DEFAULT nextval('mtrtb022_produto_nu_produto_seq'::regclass);


--
-- TOC entry 8218 (class 2604 OID 1338524)
-- Name: nu_produto_dossie; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb024_produto_dossie ALTER COLUMN nu_produto_dossie SET DEFAULT nextval('mtrtb024_produto_dossie_nu_produto_dossie_seq'::regclass);


--
-- TOC entry 8219 (class 2604 OID 1338525)
-- Name: nu_processo_documento; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb025_processo_documento ALTER COLUMN nu_processo_documento SET DEFAULT nextval('mtrtb025_processo_documento_nu_processo_documento_seq'::regclass);


--
-- TOC entry 8220 (class 2604 OID 1338526)
-- Name: nu_formulario; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb026_formulario ALTER COLUMN nu_formulario SET DEFAULT nextval('mtrtb026_formulario_nu_formulario_seq'::regclass);


--
-- TOC entry 8221 (class 2604 OID 1338527)
-- Name: nu_campo_entrada; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb027_campo_entrada ALTER COLUMN nu_campo_entrada SET DEFAULT nextval('mtrtb027_campo_entrada_nu_campo_entrada_seq'::regclass);


--
-- TOC entry 8222 (class 2604 OID 1338528)
-- Name: nu_opcao_campo; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb028_opcao_campo ALTER COLUMN nu_opcao_campo SET DEFAULT nextval('mtrtb028_opcao_campo_nu_opcao_campo_seq'::regclass);


--
-- TOC entry 8223 (class 2604 OID 1338529)
-- Name: nu_campo_apresentacao; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb029_campo_apresentacao ALTER COLUMN nu_campo_apresentacao SET DEFAULT nextval('mtrtb029_campo_apresentacao_nu_campo_apresentacao_seq'::regclass);


--
-- TOC entry 8224 (class 2604 OID 1338530)
-- Name: nu_resposta_dossie; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb030_resposta_dossie ALTER COLUMN nu_resposta_dossie SET DEFAULT nextval('mtrtb030_resposta_dossie_nu_resposta_dossie_seq'::regclass);


--
-- TOC entry 8225 (class 2604 OID 1338531)
-- Name: nu_elemento_conteudo; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb032_elemento_conteudo ALTER COLUMN nu_elemento_conteudo SET DEFAULT nextval('mtrtb032_elemento_conteudo_nu_elemento_conteudo_seq'::regclass);


--
-- TOC entry 8226 (class 2604 OID 1338532)
-- Name: nu_garantia; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb033_garantia ALTER COLUMN nu_garantia SET DEFAULT nextval('mtrtb033_garantia_nu_garantia_seq'::regclass);


--
-- TOC entry 8227 (class 2604 OID 1338533)
-- Name: nu_garantia_informada; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb035_garantia_informada ALTER COLUMN nu_garantia_informada SET DEFAULT nextval('mtrtb035_garantia_informada_nu_garantia_informada_seq'::regclass);


--
-- TOC entry 8230 (class 2604 OID 1338534)
-- Name: nu_composicao_documental; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb036_composicao_documental ALTER COLUMN nu_composicao_documental SET DEFAULT nextval('mtrtb036_composicao_documental_nu_composicao_documental_seq'::regclass);


--
-- TOC entry 8231 (class 2604 OID 1338535)
-- Name: nu_regra_documental; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb037_regra_documental ALTER COLUMN nu_regra_documental SET DEFAULT nextval('mtrtb037_regra_documental_nu_regra_documental_seq'::regclass);


--
-- TOC entry 8232 (class 2604 OID 1338536)
-- Name: nu_autorizacao; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb100_autorizacao ALTER COLUMN nu_autorizacao SET DEFAULT nextval('mtrtb100_autorizacao_nu_autorizacao_seq'::regclass);


--
-- TOC entry 8233 (class 2604 OID 1338537)
-- Name: nu_documento; Type: DEFAULT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb101_documento ALTER COLUMN nu_documento SET DEFAULT nextval('mtrtb101_documento_nu_documento_seq'::regclass);


--
-- TOC entry 8236 (class 2606 OID 1338546)
-- Name: pk_mtrtb001_dossie_cliente; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb001_dossie_cliente
    ADD CONSTRAINT pk_mtrtb001_dossie_cliente PRIMARY KEY (nu_dossie_cliente);


--
-- TOC entry 8238 (class 2606 OID 1338548)
-- Name: pk_mtrtb001_pessoa_fisica; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb001_pessoa_fisica
    ADD CONSTRAINT pk_mtrtb001_pessoa_fisica PRIMARY KEY (nu_dossie_cliente);


--
-- TOC entry 8240 (class 2606 OID 1338550)
-- Name: pk_mtrtb001_pessoa_juridica; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb001_pessoa_juridica
    ADD CONSTRAINT pk_mtrtb001_pessoa_juridica PRIMARY KEY (nu_dossie_cliente);


--
-- TOC entry 8242 (class 2606 OID 1338552)
-- Name: pk_mtrtb002_dossie_produto; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb002_dossie_produto
    ADD CONSTRAINT pk_mtrtb002_dossie_produto PRIMARY KEY (nu_dossie_produto);


--
-- TOC entry 8244 (class 2606 OID 1338554)
-- Name: pk_mtrtb003_documento; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb003_documento
    ADD CONSTRAINT pk_mtrtb003_documento PRIMARY KEY (nu_documento);


--
-- TOC entry 8246 (class 2606 OID 1338556)
-- Name: pk_mtrtb004_dossie_cliente_pro; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb004_dossie_cliente_produto
    ADD CONSTRAINT pk_mtrtb004_dossie_cliente_pro PRIMARY KEY (nu_dossie_cliente_produto);


--
-- TOC entry 8248 (class 2606 OID 1338558)
-- Name: pk_mtrtb005_documento_cliente; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb005_documento_cliente
    ADD CONSTRAINT pk_mtrtb005_documento_cliente PRIMARY KEY (nu_documento, nu_dossie_cliente);


--
-- TOC entry 8250 (class 2606 OID 1338560)
-- Name: pk_mtrtb006_canal_captura; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb006_canal_captura
    ADD CONSTRAINT pk_mtrtb006_canal_captura PRIMARY KEY (nu_canal_captura);


--
-- TOC entry 8252 (class 2606 OID 1338562)
-- Name: pk_mtrtb007_atributo_documento; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb007_atributo_documento
    ADD CONSTRAINT pk_mtrtb007_atributo_documento PRIMARY KEY (nu_atributo_documento);


--
-- TOC entry 8254 (class 2606 OID 1338564)
-- Name: pk_mtrtb008_conteudo; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb008_conteudo
    ADD CONSTRAINT pk_mtrtb008_conteudo PRIMARY KEY (nu_conteudo);


--
-- TOC entry 8258 (class 2606 OID 1338566)
-- Name: pk_mtrtb009_tipo_documento; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb009_tipo_documento
    ADD CONSTRAINT pk_mtrtb009_tipo_documento PRIMARY KEY (nu_tipo_documento);


--
-- TOC entry 8261 (class 2606 OID 1338568)
-- Name: pk_mtrtb010_funcao_documental; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb010_funcao_documental
    ADD CONSTRAINT pk_mtrtb010_funcao_documental PRIMARY KEY (nu_funcao_documental);


--
-- TOC entry 8263 (class 2606 OID 1338570)
-- Name: pk_mtrtb011_funcao_documento; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb011_funcao_documento
    ADD CONSTRAINT pk_mtrtb011_funcao_documento PRIMARY KEY (nu_tipo_documento, nu_funcao_documental);


--
-- TOC entry 8266 (class 2606 OID 1338572)
-- Name: pk_mtrtb012_tipo_situacao_doss; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb012_tipo_situacao_dossie
    ADD CONSTRAINT pk_mtrtb012_tipo_situacao_doss PRIMARY KEY (nu_tipo_situacao_dossie);


--
-- TOC entry 8268 (class 2606 OID 1338574)
-- Name: pk_mtrtb013_situacao_dossie; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb013_situacao_dossie
    ADD CONSTRAINT pk_mtrtb013_situacao_dossie PRIMARY KEY (nu_situacao_dossie);


--
-- TOC entry 8271 (class 2606 OID 1338576)
-- Name: pk_mtrtb014_instancia_document; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb014_instancia_documento
    ADD CONSTRAINT pk_mtrtb014_instancia_document PRIMARY KEY (nu_instancia_documento);


--
-- TOC entry 8274 (class 2606 OID 1338578)
-- Name: pk_mtrtb015_situacao_documento; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb015_situacao_documento
    ADD CONSTRAINT pk_mtrtb015_situacao_documento PRIMARY KEY (nu_situacao_documento);


--
-- TOC entry 8277 (class 2606 OID 1338580)
-- Name: pk_mtrtb016_motivo_situacao_dc; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb016_motivo_situacao_dcto
    ADD CONSTRAINT pk_mtrtb016_motivo_situacao_dc PRIMARY KEY (nu_motivo_situacao_dcto);


--
-- TOC entry 8279 (class 2606 OID 1338582)
-- Name: pk_mtrtb017_situacao_instancia; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb017_situacao_instancia_dct
    ADD CONSTRAINT pk_mtrtb017_situacao_instancia PRIMARY KEY (nu_situacao_instancia_dcto);


--
-- TOC entry 8281 (class 2606 OID 1338584)
-- Name: pk_mtrtb018_unidade_tratamento; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb018_unidade_tratamento
    ADD CONSTRAINT pk_mtrtb018_unidade_tratamento PRIMARY KEY (nu_dossie_produto, nu_cgc_unidade);


--
-- TOC entry 8284 (class 2606 OID 1338586)
-- Name: pk_mtrtb019_macroprocesso; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb019_macroprocesso
    ADD CONSTRAINT pk_mtrtb019_macroprocesso PRIMARY KEY (nu_macroprocesso);


--
-- TOC entry 8287 (class 2606 OID 1338588)
-- Name: pk_mtrtb020_processo; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb020_processo
    ADD CONSTRAINT pk_mtrtb020_processo PRIMARY KEY (nu_processo);


--
-- TOC entry 8289 (class 2606 OID 1338590)
-- Name: pk_mtrtb021_unidade_autorizada; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb021_unidade_autorizada
    ADD CONSTRAINT pk_mtrtb021_unidade_autorizada PRIMARY KEY (nu_unidade_autorizada);


--
-- TOC entry 8292 (class 2606 OID 1338592)
-- Name: pk_mtrtb022_produto; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb022_produto
    ADD CONSTRAINT pk_mtrtb022_produto PRIMARY KEY (nu_produto);


--
-- TOC entry 8294 (class 2606 OID 1338594)
-- Name: pk_mtrtb023_produto_processo; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb023_produto_processo
    ADD CONSTRAINT pk_mtrtb023_produto_processo PRIMARY KEY (nu_processo, nu_produto);


--
-- TOC entry 8296 (class 2606 OID 1338596)
-- Name: pk_mtrtb024_produto_dossie; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb024_produto_dossie
    ADD CONSTRAINT pk_mtrtb024_produto_dossie PRIMARY KEY (nu_produto_dossie);


--
-- TOC entry 8298 (class 2606 OID 1338598)
-- Name: pk_mtrtb025_processo_documento; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb025_processo_documento
    ADD CONSTRAINT pk_mtrtb025_processo_documento PRIMARY KEY (nu_processo_documento);


--
-- TOC entry 8301 (class 2606 OID 1338600)
-- Name: pk_mtrtb026_formulario; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb026_formulario
    ADD CONSTRAINT pk_mtrtb026_formulario PRIMARY KEY (nu_formulario);


--
-- TOC entry 8304 (class 2606 OID 1338602)
-- Name: pk_mtrtb027_campo_entrada; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb027_campo_entrada
    ADD CONSTRAINT pk_mtrtb027_campo_entrada PRIMARY KEY (nu_campo_entrada);


--
-- TOC entry 8306 (class 2606 OID 1338604)
-- Name: pk_mtrtb028_opcao_campo; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb028_opcao_campo
    ADD CONSTRAINT pk_mtrtb028_opcao_campo PRIMARY KEY (nu_opcao_campo);


--
-- TOC entry 8308 (class 2606 OID 1338606)
-- Name: pk_mtrtb029_campo_apresentacao; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb029_campo_apresentacao
    ADD CONSTRAINT pk_mtrtb029_campo_apresentacao PRIMARY KEY (nu_campo_apresentacao);


--
-- TOC entry 8310 (class 2606 OID 1338608)
-- Name: pk_mtrtb030_resposta_dossie; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb030_resposta_dossie
    ADD CONSTRAINT pk_mtrtb030_resposta_dossie PRIMARY KEY (nu_resposta_dossie);


--
-- TOC entry 8312 (class 2606 OID 1338610)
-- Name: pk_mtrtb031_resposta_opcao; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb031_resposta_opcao
    ADD CONSTRAINT pk_mtrtb031_resposta_opcao PRIMARY KEY (nu_resposta_dossie, nu_opcao_campo);


--
-- TOC entry 8314 (class 2606 OID 1338612)
-- Name: pk_mtrtb032_elemento_conteudo; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb032_elemento_conteudo
    ADD CONSTRAINT pk_mtrtb032_elemento_conteudo PRIMARY KEY (nu_elemento_conteudo);


--
-- TOC entry 8317 (class 2606 OID 1338614)
-- Name: pk_mtrtb033_garantia; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb033_garantia
    ADD CONSTRAINT pk_mtrtb033_garantia PRIMARY KEY (nu_garantia);


--
-- TOC entry 8319 (class 2606 OID 1338616)
-- Name: pk_mtrtb034_garantia_produto; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb034_garantia_produto
    ADD CONSTRAINT pk_mtrtb034_garantia_produto PRIMARY KEY (nu_produto, nu_garantia);


--
-- TOC entry 8321 (class 2606 OID 1338618)
-- Name: pk_mtrtb035_garantia_informada; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb035_garantia_informada
    ADD CONSTRAINT pk_mtrtb035_garantia_informada PRIMARY KEY (nu_garantia_informada);


--
-- TOC entry 8323 (class 2606 OID 1338620)
-- Name: pk_mtrtb036_composicao_documen; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb036_composicao_documental
    ADD CONSTRAINT pk_mtrtb036_composicao_documen PRIMARY KEY (nu_composicao_documental);


--
-- TOC entry 8325 (class 2606 OID 1338622)
-- Name: pk_mtrtb037_regra_documental; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb037_regra_documental
    ADD CONSTRAINT pk_mtrtb037_regra_documental PRIMARY KEY (nu_regra_documental);


--
-- TOC entry 8327 (class 2606 OID 1338624)
-- Name: pk_mtrtb038_nivel_documental; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb038_nivel_documental
    ADD CONSTRAINT pk_mtrtb038_nivel_documental PRIMARY KEY (nu_composicao_documental, nu_dossie_cliente);


--
-- TOC entry 8329 (class 2606 OID 1338626)
-- Name: pk_mtrtb039_produto_composicao; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb039_produto_composicao
    ADD CONSTRAINT pk_mtrtb039_produto_composicao PRIMARY KEY (nu_composicao_documental, nu_produto);


--
-- TOC entry 8331 (class 2606 OID 1338628)
-- Name: pk_mtrtb040_cadeia_situacao_do; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb040_cadeia_situacao_dossie
    ADD CONSTRAINT pk_mtrtb040_cadeia_situacao_do PRIMARY KEY (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte);


--
-- TOC entry 8333 (class 2606 OID 1338630)
-- Name: pk_mtrtb041_cadeia_situacao_dc; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb041_cadeia_situacao_dcto
    ADD CONSTRAINT pk_mtrtb041_cadeia_situacao_dc PRIMARY KEY (nu_situacao_documento_atual, nu_situacao_documento_seguinte);


--
-- TOC entry 8336 (class 2606 OID 1338632)
-- Name: pk_mtrtb100_autorizacao; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb100_autorizacao
    ADD CONSTRAINT pk_mtrtb100_autorizacao PRIMARY KEY (nu_autorizacao);


--
-- TOC entry 8338 (class 2606 OID 1338634)
-- Name: pk_mtrtb101_documento; Type: CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb101_documento
    ADD CONSTRAINT pk_mtrtb101_documento PRIMARY KEY (nu_documento);


--
-- TOC entry 8334 (class 1259 OID 1338635)
-- Name: ix_dostb100_01; Type: INDEX; Schema: mtrsm001; Owner: -
--

CREATE UNIQUE INDEX ix_dostb100_01 ON mtrtb100_autorizacao USING btree (co_autorizacao);


--
-- TOC entry 8234 (class 1259 OID 1338636)
-- Name: ix_mtrtb001_01; Type: INDEX; Schema: mtrsm001; Owner: -
--

CREATE UNIQUE INDEX ix_mtrtb001_01 ON mtrtb001_dossie_cliente USING btree (nu_cpf_cnpj);


--
-- TOC entry 8255 (class 1259 OID 1338637)
-- Name: ix_mtrtb005_01; Type: INDEX; Schema: mtrsm001; Owner: -
--

CREATE UNIQUE INDEX ix_mtrtb005_01 ON mtrtb009_tipo_documento USING btree (no_tipo_documento);


--
-- TOC entry 8256 (class 1259 OID 1338638)
-- Name: ix_mtrtb005_02; Type: INDEX; Schema: mtrsm001; Owner: -
--

CREATE UNIQUE INDEX ix_mtrtb005_02 ON mtrtb009_tipo_documento USING btree (co_tipologia);


--
-- TOC entry 8259 (class 1259 OID 1338639)
-- Name: ix_mtrtb011_01; Type: INDEX; Schema: mtrsm001; Owner: -
--

CREATE UNIQUE INDEX ix_mtrtb011_01 ON mtrtb010_funcao_documental USING btree (no_funcao);


--
-- TOC entry 8264 (class 1259 OID 1338640)
-- Name: ix_mtrtb012_02; Type: INDEX; Schema: mtrsm001; Owner: -
--

CREATE UNIQUE INDEX ix_mtrtb012_02 ON mtrtb012_tipo_situacao_dossie USING btree (no_tipo_situacao);


--
-- TOC entry 8269 (class 1259 OID 1338641)
-- Name: ix_mtrtb014_01; Type: INDEX; Schema: mtrsm001; Owner: -
--

CREATE UNIQUE INDEX ix_mtrtb014_01 ON mtrtb014_instancia_documento USING btree (nu_documento, nu_dossie_produto);


--
-- TOC entry 8272 (class 1259 OID 1338642)
-- Name: ix_mtrtb015_01; Type: INDEX; Schema: mtrsm001; Owner: -
--

CREATE UNIQUE INDEX ix_mtrtb015_01 ON mtrtb015_situacao_documento USING btree (no_situacao);


--
-- TOC entry 8275 (class 1259 OID 1338643)
-- Name: ix_mtrtb016_01; Type: INDEX; Schema: mtrsm001; Owner: -
--

CREATE UNIQUE INDEX ix_mtrtb016_01 ON mtrtb016_motivo_situacao_dcto USING btree (nu_situacao_documento, no_motivo_situacao_dcto);


--
-- TOC entry 8282 (class 1259 OID 1338644)
-- Name: ix_mtrtb019_01; Type: INDEX; Schema: mtrsm001; Owner: -
--

CREATE UNIQUE INDEX ix_mtrtb019_01 ON mtrtb019_macroprocesso USING btree (no_macroprocesso);


--
-- TOC entry 8285 (class 1259 OID 1338645)
-- Name: ix_mtrtb020_01; Type: INDEX; Schema: mtrsm001; Owner: -
--

CREATE UNIQUE INDEX ix_mtrtb020_01 ON mtrtb020_processo USING btree (no_processo);


--
-- TOC entry 8290 (class 1259 OID 1338646)
-- Name: ix_mtrtb022_01; Type: INDEX; Schema: mtrsm001; Owner: -
--

CREATE UNIQUE INDEX ix_mtrtb022_01 ON mtrtb022_produto USING btree (nu_operacao, nu_modalidade);


--
-- TOC entry 8299 (class 1259 OID 1338647)
-- Name: ix_mtrtb026_01; Type: INDEX; Schema: mtrsm001; Owner: -
--

CREATE UNIQUE INDEX ix_mtrtb026_01 ON mtrtb026_formulario USING btree (nu_processo, nu_fase_utilizacao);


--
-- TOC entry 8302 (class 1259 OID 1338648)
-- Name: ix_mtrtb027_01; Type: INDEX; Schema: mtrsm001; Owner: -
--

CREATE UNIQUE INDEX ix_mtrtb027_01 ON mtrtb027_campo_entrada USING btree (nu_ordem, nu_formulario);


--
-- TOC entry 8315 (class 1259 OID 1338649)
-- Name: ix_mtrtb034_01; Type: INDEX; Schema: mtrsm001; Owner: -
--

CREATE UNIQUE INDEX ix_mtrtb034_01 ON mtrtb033_garantia USING btree (nu_operacao);


--
-- TOC entry 8339 (class 2606 OID 1338650)
-- Name: fk_mtrtb001_reference_mtrtb001; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb001_pessoa_fisica
    ADD CONSTRAINT fk_mtrtb001_reference_mtrtb001 FOREIGN KEY (nu_dossie_cliente) REFERENCES mtrtb001_dossie_cliente(nu_dossie_cliente) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8340 (class 2606 OID 1338655)
-- Name: fk_mtrtb001_reference_mtrtb001; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb001_pessoa_juridica
    ADD CONSTRAINT fk_mtrtb001_reference_mtrtb001 FOREIGN KEY (nu_dossie_cliente) REFERENCES mtrtb001_dossie_cliente(nu_dossie_cliente) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8341 (class 2606 OID 1338660)
-- Name: fk_mtrtb002_reference_mtrtb020; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb002_dossie_produto
    ADD CONSTRAINT fk_mtrtb002_reference_mtrtb020 FOREIGN KEY (nu_processo) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8343 (class 2606 OID 1338665)
-- Name: fk_mtrtb003_reference_mtrtb006; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb003_documento
    ADD CONSTRAINT fk_mtrtb003_reference_mtrtb006 FOREIGN KEY (nu_canal_captura) REFERENCES mtrtb006_canal_captura(nu_canal_captura) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8342 (class 2606 OID 1338670)
-- Name: fk_mtrtb003_reference_mtrtb009; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb003_documento
    ADD CONSTRAINT fk_mtrtb003_reference_mtrtb009 FOREIGN KEY (nu_tipo_documento) REFERENCES mtrtb009_tipo_documento(nu_tipo_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8345 (class 2606 OID 1338675)
-- Name: fk_mtrtb004_reference_mtrtb001; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb004_dossie_cliente_produto
    ADD CONSTRAINT fk_mtrtb004_reference_mtrtb001 FOREIGN KEY (nu_dossie_cliente) REFERENCES mtrtb001_dossie_cliente(nu_dossie_cliente) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8344 (class 2606 OID 1338680)
-- Name: fk_mtrtb004_reference_mtrtb002; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb004_dossie_cliente_produto
    ADD CONSTRAINT fk_mtrtb004_reference_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8347 (class 2606 OID 1338685)
-- Name: fk_mtrtb005_reference_mtrtb001; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb005_documento_cliente
    ADD CONSTRAINT fk_mtrtb005_reference_mtrtb001 FOREIGN KEY (nu_dossie_cliente) REFERENCES mtrtb001_dossie_cliente(nu_dossie_cliente) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8346 (class 2606 OID 1338690)
-- Name: fk_mtrtb005_reference_mtrtb003; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb005_documento_cliente
    ADD CONSTRAINT fk_mtrtb005_reference_mtrtb003 FOREIGN KEY (nu_documento) REFERENCES mtrtb003_documento(nu_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8348 (class 2606 OID 1338695)
-- Name: fk_mtrtb007_reference_mtrtb003; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb007_atributo_documento
    ADD CONSTRAINT fk_mtrtb007_reference_mtrtb003 FOREIGN KEY (nu_documento) REFERENCES mtrtb003_documento(nu_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8349 (class 2606 OID 1338700)
-- Name: fk_mtrtb008_reference_mtrtb003; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb008_conteudo
    ADD CONSTRAINT fk_mtrtb008_reference_mtrtb003 FOREIGN KEY (nu_documento) REFERENCES mtrtb003_documento(nu_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8351 (class 2606 OID 1338705)
-- Name: fk_mtrtb011_reference_mtrtb009; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb011_funcao_documento
    ADD CONSTRAINT fk_mtrtb011_reference_mtrtb009 FOREIGN KEY (nu_tipo_documento) REFERENCES mtrtb009_tipo_documento(nu_tipo_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8350 (class 2606 OID 1338710)
-- Name: fk_mtrtb011_reference_mtrtb010; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb011_funcao_documento
    ADD CONSTRAINT fk_mtrtb011_reference_mtrtb010 FOREIGN KEY (nu_funcao_documental) REFERENCES mtrtb010_funcao_documental(nu_funcao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8353 (class 2606 OID 1338715)
-- Name: fk_mtrtb013_reference_mtrtb002; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb013_situacao_dossie
    ADD CONSTRAINT fk_mtrtb013_reference_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8352 (class 2606 OID 1338720)
-- Name: fk_mtrtb013_reference_mtrtb012; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb013_situacao_dossie
    ADD CONSTRAINT fk_mtrtb013_reference_mtrtb012 FOREIGN KEY (nu_tipo_situacao_dossie) REFERENCES mtrtb012_tipo_situacao_dossie(nu_tipo_situacao_dossie) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8357 (class 2606 OID 1338725)
-- Name: fk_mtrtb014_reference_mtrtb002; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb014_instancia_documento
    ADD CONSTRAINT fk_mtrtb014_reference_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8356 (class 2606 OID 1338730)
-- Name: fk_mtrtb014_reference_mtrtb003; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb014_instancia_documento
    ADD CONSTRAINT fk_mtrtb014_reference_mtrtb003 FOREIGN KEY (nu_documento) REFERENCES mtrtb003_documento(nu_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8355 (class 2606 OID 1338735)
-- Name: fk_mtrtb014_reference_mtrtb032; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb014_instancia_documento
    ADD CONSTRAINT fk_mtrtb014_reference_mtrtb032 FOREIGN KEY (nu_elemento_conteudo) REFERENCES mtrtb032_elemento_conteudo(nu_elemento_conteudo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8354 (class 2606 OID 1338740)
-- Name: fk_mtrtb014_reference_mtrtb035; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb014_instancia_documento
    ADD CONSTRAINT fk_mtrtb014_reference_mtrtb035 FOREIGN KEY (nu_garantia_informada) REFERENCES mtrtb035_garantia_informada(nu_garantia_informada) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8358 (class 2606 OID 1338745)
-- Name: fk_mtrtb016_reference_mtrtb015; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb016_motivo_situacao_dcto
    ADD CONSTRAINT fk_mtrtb016_reference_mtrtb015 FOREIGN KEY (nu_situacao_documento) REFERENCES mtrtb015_situacao_documento(nu_situacao_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8361 (class 2606 OID 1338750)
-- Name: fk_mtrtb017_reference_mtrtb014; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb017_situacao_instancia_dct
    ADD CONSTRAINT fk_mtrtb017_reference_mtrtb014 FOREIGN KEY (nu_instancia_documento) REFERENCES mtrtb014_instancia_documento(nu_instancia_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8360 (class 2606 OID 1338755)
-- Name: fk_mtrtb017_reference_mtrtb015; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb017_situacao_instancia_dct
    ADD CONSTRAINT fk_mtrtb017_reference_mtrtb015 FOREIGN KEY (nu_situacao_documento) REFERENCES mtrtb015_situacao_documento(nu_situacao_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8359 (class 2606 OID 1338760)
-- Name: fk_mtrtb017_reference_mtrtb016; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb017_situacao_instancia_dct
    ADD CONSTRAINT fk_mtrtb017_reference_mtrtb016 FOREIGN KEY (nu_motivo_situacao_dcto) REFERENCES mtrtb016_motivo_situacao_dcto(nu_motivo_situacao_dcto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8362 (class 2606 OID 1338765)
-- Name: fk_mtrtb018_reference_mtrtb002; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb018_unidade_tratamento
    ADD CONSTRAINT fk_mtrtb018_reference_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8363 (class 2606 OID 1338770)
-- Name: fk_mtrtb020_reference_mtrtb019; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb020_processo
    ADD CONSTRAINT fk_mtrtb020_reference_mtrtb019 FOREIGN KEY (nu_macroprocesso) REFERENCES mtrtb019_macroprocesso(nu_macroprocesso) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8364 (class 2606 OID 1338775)
-- Name: fk_mtrtb021_reference_mtrtb020; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb021_unidade_autorizada
    ADD CONSTRAINT fk_mtrtb021_reference_mtrtb020 FOREIGN KEY (nu_processo) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8366 (class 2606 OID 1338780)
-- Name: fk_mtrtb023_reference_mtrtb020; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb023_produto_processo
    ADD CONSTRAINT fk_mtrtb023_reference_mtrtb020 FOREIGN KEY (nu_processo) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8365 (class 2606 OID 1338785)
-- Name: fk_mtrtb023_reference_mtrtb022; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb023_produto_processo
    ADD CONSTRAINT fk_mtrtb023_reference_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8368 (class 2606 OID 1338790)
-- Name: fk_mtrtb024_reference_mtrtb002; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb024_produto_dossie
    ADD CONSTRAINT fk_mtrtb024_reference_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8367 (class 2606 OID 1338795)
-- Name: fk_mtrtb024_reference_mtrtb022; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb024_produto_dossie
    ADD CONSTRAINT fk_mtrtb024_reference_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8371 (class 2606 OID 1338800)
-- Name: fk_mtrtb025_reference_mtrtb009; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb025_processo_documento
    ADD CONSTRAINT fk_mtrtb025_reference_mtrtb009 FOREIGN KEY (nu_tipo_documento) REFERENCES mtrtb009_tipo_documento(nu_tipo_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8370 (class 2606 OID 1338805)
-- Name: fk_mtrtb025_reference_mtrtb010; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb025_processo_documento
    ADD CONSTRAINT fk_mtrtb025_reference_mtrtb010 FOREIGN KEY (nu_funcao_documental) REFERENCES mtrtb010_funcao_documental(nu_funcao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8369 (class 2606 OID 1338810)
-- Name: fk_mtrtb025_reference_mtrtb020; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb025_processo_documento
    ADD CONSTRAINT fk_mtrtb025_reference_mtrtb020 FOREIGN KEY (nu_processo) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8372 (class 2606 OID 1338815)
-- Name: fk_mtrtb026_reference_mtrtb020; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb026_formulario
    ADD CONSTRAINT fk_mtrtb026_reference_mtrtb020 FOREIGN KEY (nu_processo) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8373 (class 2606 OID 1338820)
-- Name: fk_mtrtb027_reference_mtrtb026; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb027_campo_entrada
    ADD CONSTRAINT fk_mtrtb027_reference_mtrtb026 FOREIGN KEY (nu_formulario) REFERENCES mtrtb026_formulario(nu_formulario) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8374 (class 2606 OID 1338825)
-- Name: fk_mtrtb028_reference_mtrtb027; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb028_opcao_campo
    ADD CONSTRAINT fk_mtrtb028_reference_mtrtb027 FOREIGN KEY (nu_campo_entrada) REFERENCES mtrtb027_campo_entrada(nu_campo_entrada) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8375 (class 2606 OID 1338830)
-- Name: fk_mtrtb029_reference_mtrtb027; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb029_campo_apresentacao
    ADD CONSTRAINT fk_mtrtb029_reference_mtrtb027 FOREIGN KEY (nu_campo_entrada) REFERENCES mtrtb027_campo_entrada(nu_campo_entrada) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8377 (class 2606 OID 1338835)
-- Name: fk_mtrtb030_reference_mtrtb002; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb030_resposta_dossie
    ADD CONSTRAINT fk_mtrtb030_reference_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8376 (class 2606 OID 1338840)
-- Name: fk_mtrtb030_reference_mtrtb027; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb030_resposta_dossie
    ADD CONSTRAINT fk_mtrtb030_reference_mtrtb027 FOREIGN KEY (nu_campo_entrada) REFERENCES mtrtb027_campo_entrada(nu_campo_entrada) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8379 (class 2606 OID 1338845)
-- Name: fk_mtrtb031_reference_mtrtb028; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb031_resposta_opcao
    ADD CONSTRAINT fk_mtrtb031_reference_mtrtb028 FOREIGN KEY (nu_opcao_campo) REFERENCES mtrtb028_opcao_campo(nu_opcao_campo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8378 (class 2606 OID 1338850)
-- Name: fk_mtrtb031_reference_mtrtb030; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb031_resposta_opcao
    ADD CONSTRAINT fk_mtrtb031_reference_mtrtb030 FOREIGN KEY (nu_resposta_dossie) REFERENCES mtrtb030_resposta_dossie(nu_resposta_dossie) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8383 (class 2606 OID 1338855)
-- Name: fk_mtrtb032_reference_mtrtb009; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb032_elemento_conteudo
    ADD CONSTRAINT fk_mtrtb032_reference_mtrtb009 FOREIGN KEY (nu_tipo_documento) REFERENCES mtrtb009_tipo_documento(nu_tipo_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8382 (class 2606 OID 1338860)
-- Name: fk_mtrtb032_reference_mtrtb020; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb032_elemento_conteudo
    ADD CONSTRAINT fk_mtrtb032_reference_mtrtb020 FOREIGN KEY (nu_processo) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8381 (class 2606 OID 1338865)
-- Name: fk_mtrtb032_reference_mtrtb022; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb032_elemento_conteudo
    ADD CONSTRAINT fk_mtrtb032_reference_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8380 (class 2606 OID 1338870)
-- Name: fk_mtrtb032_reference_mtrtb032; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb032_elemento_conteudo
    ADD CONSTRAINT fk_mtrtb032_reference_mtrtb032 FOREIGN KEY (nu_elemento_vinculador) REFERENCES mtrtb032_elemento_conteudo(nu_elemento_conteudo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8385 (class 2606 OID 1338875)
-- Name: fk_mtrtb034_reference_mtrtb022; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb034_garantia_produto
    ADD CONSTRAINT fk_mtrtb034_reference_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8384 (class 2606 OID 1338880)
-- Name: fk_mtrtb034_reference_mtrtb033; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb034_garantia_produto
    ADD CONSTRAINT fk_mtrtb034_reference_mtrtb033 FOREIGN KEY (nu_garantia) REFERENCES mtrtb033_garantia(nu_garantia) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8388 (class 2606 OID 1338885)
-- Name: fk_mtrtb035_reference_mtrtb002; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb035_garantia_informada
    ADD CONSTRAINT fk_mtrtb035_reference_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8387 (class 2606 OID 1338890)
-- Name: fk_mtrtb035_reference_mtrtb022; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb035_garantia_informada
    ADD CONSTRAINT fk_mtrtb035_reference_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8386 (class 2606 OID 1338895)
-- Name: fk_mtrtb035_reference_mtrtb033; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb035_garantia_informada
    ADD CONSTRAINT fk_mtrtb035_reference_mtrtb033 FOREIGN KEY (nu_garantia) REFERENCES mtrtb033_garantia(nu_garantia) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8392 (class 2606 OID 1338900)
-- Name: fk_mtrtb037_reference_mtrtb006; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb037_regra_documental
    ADD CONSTRAINT fk_mtrtb037_reference_mtrtb006 FOREIGN KEY (nu_canal_captura) REFERENCES mtrtb006_canal_captura(nu_canal_captura) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8391 (class 2606 OID 1338905)
-- Name: fk_mtrtb037_reference_mtrtb009; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb037_regra_documental
    ADD CONSTRAINT fk_mtrtb037_reference_mtrtb009 FOREIGN KEY (nu_tipo_documento) REFERENCES mtrtb009_tipo_documento(nu_tipo_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8390 (class 2606 OID 1338910)
-- Name: fk_mtrtb037_reference_mtrtb010; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb037_regra_documental
    ADD CONSTRAINT fk_mtrtb037_reference_mtrtb010 FOREIGN KEY (nu_funcao_documental) REFERENCES mtrtb010_funcao_documental(nu_funcao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8389 (class 2606 OID 1338915)
-- Name: fk_mtrtb037_reference_mtrtb036; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb037_regra_documental
    ADD CONSTRAINT fk_mtrtb037_reference_mtrtb036 FOREIGN KEY (nu_composicao_documental) REFERENCES mtrtb036_composicao_documental(nu_composicao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8394 (class 2606 OID 1338920)
-- Name: fk_mtrtb038_reference_mtrtb001; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb038_nivel_documental
    ADD CONSTRAINT fk_mtrtb038_reference_mtrtb001 FOREIGN KEY (nu_dossie_cliente) REFERENCES mtrtb001_dossie_cliente(nu_dossie_cliente) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8393 (class 2606 OID 1338925)
-- Name: fk_mtrtb038_reference_mtrtb036; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb038_nivel_documental
    ADD CONSTRAINT fk_mtrtb038_reference_mtrtb036 FOREIGN KEY (nu_composicao_documental) REFERENCES mtrtb036_composicao_documental(nu_composicao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8396 (class 2606 OID 1338930)
-- Name: fk_mtrtb039_reference_mtrtb022; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb039_produto_composicao
    ADD CONSTRAINT fk_mtrtb039_reference_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8395 (class 2606 OID 1338935)
-- Name: fk_mtrtb039_reference_mtrtb036; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb039_produto_composicao
    ADD CONSTRAINT fk_mtrtb039_reference_mtrtb036 FOREIGN KEY (nu_composicao_documental) REFERENCES mtrtb036_composicao_documental(nu_composicao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8398 (class 2606 OID 1338940)
-- Name: fk_mtrtb040_reference2_mtrtb012; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb040_cadeia_situacao_dossie
    ADD CONSTRAINT fk_mtrtb040_reference2_mtrtb012 FOREIGN KEY (nu_tipo_situacao_seguinte) REFERENCES mtrtb012_tipo_situacao_dossie(nu_tipo_situacao_dossie) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8397 (class 2606 OID 1338945)
-- Name: fk_mtrtb040_reference_mtrtb012; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb040_cadeia_situacao_dossie
    ADD CONSTRAINT fk_mtrtb040_reference_mtrtb012 FOREIGN KEY (nu_tipo_situacao_atual) REFERENCES mtrtb012_tipo_situacao_dossie(nu_tipo_situacao_dossie) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8400 (class 2606 OID 1338950)
-- Name: fk_mtrtb041_reference2_mtrtb015; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb041_cadeia_situacao_dcto
    ADD CONSTRAINT fk_mtrtb041_reference2_mtrtb015 FOREIGN KEY (nu_situacao_documento_seguinte) REFERENCES mtrtb015_situacao_documento(nu_situacao_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8399 (class 2606 OID 1338955)
-- Name: fk_mtrtb041_reference_mtrtb015; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb041_cadeia_situacao_dcto
    ADD CONSTRAINT fk_mtrtb041_reference_mtrtb015 FOREIGN KEY (nu_situacao_documento_atual) REFERENCES mtrtb015_situacao_documento(nu_situacao_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 8401 (class 2606 OID 1338960)
-- Name: fk_mtrtb101_reference_mtrtb100; Type: FK CONSTRAINT; Schema: mtrsm001; Owner: -
--

ALTER TABLE ONLY mtrtb101_documento
    ADD CONSTRAINT fk_mtrtb101_reference_mtrtb100 FOREIGN KEY (nu_autorizacao) REFERENCES mtrtb100_autorizacao(nu_autorizacao) ON UPDATE RESTRICT ON DELETE RESTRICT;


-- Completed on 2017-12-01 11:53:31

--
-- PostgreSQL database dump complete
--

