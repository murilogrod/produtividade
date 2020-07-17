--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.5
-- Dumped by pg_dump version 9.5.5

-- Started on 2018-10-31 15:07:45

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 22 (class 2615 OID 2025537)
-- Name: mtr; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA mtr;


SET search_path = mtr, pg_catalog;

SET default_tablespace = mtrtsdt000;

SET default_with_oids = false;

--
-- TOC entry 1822 (class 1259 OID 2025540)
-- Name: mtrtb001_dossie_cliente; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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
-- TOC entry 7610 (class 0 OID 0)
-- Dependencies: 1822
-- Name: TABLE mtrtb001_dossie_cliente; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb001_dossie_cliente IS 'Tabela responsável pelo armazenamento do dossiê do cliente com seus respectivos dados.
Nesta tabela serão efetivamente armazenados os dados do cliente.';


--
-- TOC entry 7611 (class 0 OID 0)
-- Dependencies: 1822
-- Name: COLUMN mtrtb001_dossie_cliente.nu_dossie_cliente; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb001_dossie_cliente.nu_dossie_cliente IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7612 (class 0 OID 0)
-- Dependencies: 1822
-- Name: COLUMN mtrtb001_dossie_cliente.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb001_dossie_cliente.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7613 (class 0 OID 0)
-- Dependencies: 1822
-- Name: COLUMN mtrtb001_dossie_cliente.nu_cpf_cnpj; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb001_dossie_cliente.nu_cpf_cnpj IS 'Atributo que representa o número do CPF/CNPJ do cliente.';


--
-- TOC entry 7614 (class 0 OID 0)
-- Dependencies: 1822
-- Name: COLUMN mtrtb001_dossie_cliente.ic_tipo_pessoa; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb001_dossie_cliente.ic_tipo_pessoa IS 'Atributo que determina qual tipo de pessoa pode ter o documento atribuído.
		Pode assumir os seguintes valores:
		F - Física
		J - Jurídica
		S - Serviço
		A - Física ou Jurídica
		T - Todos';


--
-- TOC entry 7615 (class 0 OID 0)
-- Dependencies: 1822
-- Name: COLUMN mtrtb001_dossie_cliente.no_cliente; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb001_dossie_cliente.no_cliente IS 'Atributo que representa o nome do cliente.';


--
-- TOC entry 7616 (class 0 OID 0)
-- Dependencies: 1822
-- Name: COLUMN mtrtb001_dossie_cliente.de_telefone; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb001_dossie_cliente.de_telefone IS 'Atributo que representa o telefone informado para o cliente.';


--
-- TOC entry 1823 (class 1259 OID 2025543)
-- Name: mtrsq001_dossie_cliente; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq001_dossie_cliente
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7617 (class 0 OID 0)
-- Dependencies: 1823
-- Name: mtrsq001_dossie_cliente; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq001_dossie_cliente OWNED BY mtrtb001_dossie_cliente.nu_dossie_cliente;


--
-- TOC entry 1824 (class 1259 OID 2025545)
-- Name: mtrtb002_dossie_produto; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7618 (class 0 OID 0)
-- Dependencies: 1824
-- Name: TABLE mtrtb002_dossie_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb002_dossie_produto IS 'Tabela responsável pelo armazenamento do dossiê do produto com seus respectivos dados.
Nesta tabela serão efetivamente armazenados os dados do produto.
Para cada produto contratado ou submetido a análise deve ser gerado um novo registro representando o vínculo com o cliente.';


--
-- TOC entry 7619 (class 0 OID 0)
-- Dependencies: 1824
-- Name: COLUMN mtrtb002_dossie_produto.nu_dossie_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb002_dossie_produto.nu_dossie_produto IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7620 (class 0 OID 0)
-- Dependencies: 1824
-- Name: COLUMN mtrtb002_dossie_produto.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb002_dossie_produto.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7621 (class 0 OID 0)
-- Dependencies: 1824
-- Name: COLUMN mtrtb002_dossie_produto.nu_processo_dossie; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb002_dossie_produto.nu_processo_dossie IS 'Atributo utilizado para referenciar o processo ao qual o dossiê de produto esteja vinculado.';


--
-- TOC entry 7622 (class 0 OID 0)
-- Dependencies: 1824
-- Name: COLUMN mtrtb002_dossie_produto.nu_unidade_criacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb002_dossie_produto.nu_unidade_criacao IS 'Atributo utilizado para armazenar o CGC da unidade de criação do dossiê.';


--
-- TOC entry 7623 (class 0 OID 0)
-- Dependencies: 1824
-- Name: COLUMN mtrtb002_dossie_produto.nu_unidade_priorizado; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb002_dossie_produto.nu_unidade_priorizado IS 'Atributo que indica o CGC da unidade que deverá tratar o dossiê na próxima chamada da fila por qualquer empregado vinculado ao mesmo.';


--
-- TOC entry 7624 (class 0 OID 0)
-- Dependencies: 1824
-- Name: COLUMN mtrtb002_dossie_produto.co_matricula_priorizado; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb002_dossie_produto.co_matricula_priorizado IS 'Atributo que indica o empregado específico da unidade que deverá tratar o dossiê na próxima chamada da fila.';


--
-- TOC entry 7625 (class 0 OID 0)
-- Dependencies: 1824
-- Name: COLUMN mtrtb002_dossie_produto.nu_peso_prioridade; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb002_dossie_produto.nu_peso_prioridade IS 'Valor que indica dentre os dossiês priorizados, qual a ordem de captura na chamada da fila, sendo aplicado do maior para o menor. 
O valor é um número livre atribuído pelo usuário que realizar a priorização do dossiê e a fila será organizada pelos dossiês priorizados com valor de peso do maior para o menor, em seguida pela ordem de cadastro definido pelo atributo de data de criação do mais antigo para o mais novo.';


--
-- TOC entry 7626 (class 0 OID 0)
-- Dependencies: 1824
-- Name: COLUMN mtrtb002_dossie_produto.ts_finalizado; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb002_dossie_produto.ts_finalizado IS 'Atributo que armazena a data e hora que foi realizada a inclusão do dossiê.';


--
-- TOC entry 7627 (class 0 OID 0)
-- Dependencies: 1824
-- Name: COLUMN mtrtb002_dossie_produto.nu_instancia_processo_bpm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb002_dossie_produto.nu_instancia_processo_bpm IS 'Atributo que armazena o identificador da instancia de processo em execução perante a solução de BPM vinculada ao dossiê de produto.';


--
-- TOC entry 1825 (class 1259 OID 2025548)
-- Name: mtrsq002_dossie_produto; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq002_dossie_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7628 (class 0 OID 0)
-- Dependencies: 1825
-- Name: mtrsq002_dossie_produto; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq002_dossie_produto OWNED BY mtrtb002_dossie_produto.nu_dossie_produto;


--
-- TOC entry 1826 (class 1259 OID 2025550)
-- Name: mtrtb003_documento; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb003_documento (
    nu_documento bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_tipo_documento integer,
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
    no_formato character varying(10),
    ic_janela_extracao character varying(5)
);


--
-- TOC entry 7629 (class 0 OID 0)
-- Dependencies: 1826
-- Name: TABLE mtrtb003_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb003_documento IS 'Tabela responsável pelo armazenamento da referência dos documentos de um determinado cliente.
Esses documentos podem estar associados a um ou mais dossiês de produtos possibilitando o reaproveitamento dos mesmos em diversos produtos.
Nesta tabela serão efetivamente armazenados os dados dos documentos que pode representar o agrupamento de uma ou mais imagens na sua formação.
Também deverão ser armazenadas as propriedades do mesmo e as marcas conforme seu ciclo de vida.';


--
-- TOC entry 7630 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.nu_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.nu_documento IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 7631 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 7632 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.nu_tipo_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.nu_tipo_documento IS 'Atributo utilizado para armazenar a vinculação do tipo de documento referenciado pelo documento registrado';


--
-- TOC entry 7633 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.nu_canal_captura; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.nu_canal_captura IS 'Atributo utilizado para identificar o canal de captura utilizado para recepcionar o documento.';


--
-- TOC entry 7634 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.ts_captura; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.ts_captura IS 'Atributo que armazena a data e hora que foi realizada a captura do documento';


--
-- TOC entry 7635 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.co_matricula; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.co_matricula IS 'Atributo que armazena a matricula do usuario que realizou a captura do documento';


--
-- TOC entry 7636 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.ts_validade; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.ts_validade IS 'Atributo que armazena a data de validade do documento conforme definições corporativas calculado pelo prazo definido no tipo documento.

Sempre que definida uma data futura para o documento a hora deve ser definida com o valor de 23:59:59';


--
-- TOC entry 7637 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.ic_dossie_digital; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.ic_dossie_digital IS 'Atributo utilizado para determinar que o documento foi obtido do repositorio do dossiê digital e encontra-se disponivel no OBJECT STORE dessa solução.';


--
-- TOC entry 7638 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.ic_origem_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.ic_origem_documento IS 'Atributo utilizado para armazenar a origem do documento digitalizado submetido baseado no seguinte dominio:

O = Documento Original
S = Cópia Simples
C = Cópia Autenticada em Cartório
A = Cópia Autenticada Administrativamente

Sempre que um documento for submetido via upload ou via app mobile este atributo deverá ser preenchido como copia simples (S)';


--
-- TOC entry 7639 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.ic_temporario; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.ic_temporario IS 'Atributo utilizado para indicar se o documento ainda esta fase de analise sob a otica do dossiê digital.

Quando um documento for submetido pelo fluxo do dossiê digital, antes de utiliza-lo uma serie de verificações deve ser realizada dentro de um determinado espaço de tempo, senão esse documento será expurgado da base.

Esse atributo pode assumir o dominio abaixo:

0 - Definitivo
1 - Temporario - Extração de dados (OCR)
2 - Temporario - Antifraude';


--
-- TOC entry 7640 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.co_extracao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.co_extracao IS 'Atributo utilizado para armazenar o codigo de identificação junto ao serviço de extração de dados';


--
-- TOC entry 7641 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.ts_envio_extracao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.ts_envio_extracao IS 'Atributo utilizado para armazenar a data e hora de envio do documento para o serviço de extração de dados.';


--
-- TOC entry 7642 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.ts_retorno_extracao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.ts_retorno_extracao IS 'Atributo utilizado para armazenar a data e hora de retorno dos atributos extraidos do documento junto ao serviço de extração de dados.';


--
-- TOC entry 7643 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.co_autenticidade; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.co_autenticidade IS 'Atributo utilizado para armazenar o codigo de identificação do documento junto ao serviço de avaliação de autenticidade documental';


--
-- TOC entry 7644 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.ts_envio_autenticidade; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.ts_envio_autenticidade IS 'Atributo utilizado para armazenar a data e hora de envio do documento para o serviço de avaliação de autenticidade documental.';


--
-- TOC entry 7645 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.ts_retorno_autenticidade; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.ts_retorno_autenticidade IS 'Atributo utilizado para armazenar a data e hora de retorno do score junto ao serviço de avaliação de autenticidade documental.';


--
-- TOC entry 7646 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.ix_antifraude; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.ix_antifraude IS 'Atributo utilizado para armazenar o indice retornado pelo serviço de antifraude para o documento.
O indice devolve o percentual para o indicio de fraude no documento.
Para documentos validos este valor deve tender a zero.';


--
-- TOC entry 7647 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.co_ged; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.co_ged IS 'Atributo utilizado para identificar a localização de um documento armazenado em um sistema de Gestão Eletronica de Documentos (GED).
A utilização deste atrinbuto dependerá da estrategia de armazenamento, pois se a identificação for feita através do registro do documento, será aqui que deverá estar armazenado o identificador único deste conteudo perante o GED, porém se a identificação for individual por conteudo (pagina, imagem, etc) este atributo ficará inutilizado.';


--
-- TOC entry 7648 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.no_formato; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.no_formato IS 'Atributo utilizado para armazenar o formato do documento, podendo ser nulo para atender situações onde armazena-se um documento sem conteudos. Ex:
- PDF
- JPG
- TIFF';


--
-- TOC entry 7649 (class 0 OID 0)
-- Dependencies: 1826
-- Name: COLUMN mtrtb003_documento.ic_janela_extracao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb003_documento.ic_janela_extracao IS 'Atributo utilizado para armazenar a janela temporal solicitada para execução da atividade junto ao fornecedor do serviço podendo assumir os seguintes valores:

M0 - Indica solcitação de extração on-line no minuto 0
M30 - Indica solicitação de extração na janela temporal de SLA com 30 minutos
M60 - Indica solicitação de extração na janela temporal de SLA com 60 minutos';


--
-- TOC entry 1827 (class 1259 OID 2025558)
-- Name: mtrsq003_documento; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq003_documento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7650 (class 0 OID 0)
-- Dependencies: 1827
-- Name: mtrsq003_documento; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq003_documento OWNED BY mtrtb003_documento.nu_documento;


--
-- TOC entry 1828 (class 1259 OID 2025560)
-- Name: mtrsq004_dossie_cliente_produto; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq004_dossie_cliente_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1829 (class 1259 OID 2025562)
-- Name: mtrtb006_canal; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb006_canal (
    nu_canal integer NOT NULL,
    nu_versao integer NOT NULL,
    sg_canal character varying(10) NOT NULL,
    de_canal character varying(255) NOT NULL,
    co_integracao bigint NOT NULL,
    ic_canal_caixa character varying(3) NOT NULL,
    ic_janela_extracao_m0 boolean NOT NULL,
    ic_janela_extracao_m30 boolean NOT NULL,
    ic_janela_extracao_m60 boolean NOT NULL,
    ic_avaliacao_autenticidade boolean NOT NULL
);


--
-- TOC entry 7651 (class 0 OID 0)
-- Dependencies: 1829
-- Name: TABLE mtrtb006_canal; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb006_canal IS 'Tabela responsável pelo armazenamento dos possíveis canais de captura de um documento para identificação de sua origem.';


--
-- TOC entry 7652 (class 0 OID 0)
-- Dependencies: 1829
-- Name: COLUMN mtrtb006_canal.nu_canal; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb006_canal.nu_canal IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7653 (class 0 OID 0)
-- Dependencies: 1829
-- Name: COLUMN mtrtb006_canal.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb006_canal.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7654 (class 0 OID 0)
-- Dependencies: 1829
-- Name: COLUMN mtrtb006_canal.sg_canal; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb006_canal.sg_canal IS 'Atributo utilizado para identificar a sigla do canal de captura.
Pode assumir valor como por exemplo:
- SIMTR (Sistema Interno SIMTR)
- APMOB (Aplicativo Mobile)
- STECX (Site Corporativo da CAIXA)';


--
-- TOC entry 7655 (class 0 OID 0)
-- Dependencies: 1829
-- Name: COLUMN mtrtb006_canal.de_canal; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb006_canal.de_canal IS 'Atributo utilizado para descrever o canal de captura.
Exemplo:
Sistema Interno SIMTR
Sistemas Corporativos
Aplicativo Mobile
Site Corporativo';


--
-- TOC entry 7656 (class 0 OID 0)
-- Dependencies: 1829
-- Name: COLUMN mtrtb006_canal.co_integracao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb006_canal.co_integracao IS 'Atributo que representa o código único para o sistema de origem.';


--
-- TOC entry 7657 (class 0 OID 0)
-- Dependencies: 1829
-- Name: COLUMN mtrtb006_canal.ic_janela_extracao_m0; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb006_canal.ic_janela_extracao_m0 IS 'Atributo utilizado para indicar se o canal possui permisão ou não de consumo do serviço de extração na janela M+0';


--
-- TOC entry 7658 (class 0 OID 0)
-- Dependencies: 1829
-- Name: COLUMN mtrtb006_canal.ic_janela_extracao_m30; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb006_canal.ic_janela_extracao_m30 IS 'Atributo utilizado para indicar se o canal possui permisão ou não de consumo do serviço de extração na janela M+30';


--
-- TOC entry 7659 (class 0 OID 0)
-- Dependencies: 1829
-- Name: COLUMN mtrtb006_canal.ic_janela_extracao_m60; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb006_canal.ic_janela_extracao_m60 IS 'Atributo utilizado para indicar se o canal possui permisão ou não de consumo do serviço de extração na janela M+60';


--
-- TOC entry 7660 (class 0 OID 0)
-- Dependencies: 1829
-- Name: COLUMN mtrtb006_canal.ic_avaliacao_autenticidade; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb006_canal.ic_avaliacao_autenticidade IS 'Atributo utilizado para indicar se o canal possui permisão ou não de consumo do serviço de avaliação de autenticidade documental';


--
-- TOC entry 1830 (class 1259 OID 2025565)
-- Name: mtrsq006_canal; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq006_canal
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7661 (class 0 OID 0)
-- Dependencies: 1830
-- Name: mtrsq006_canal; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq006_canal OWNED BY mtrtb006_canal.nu_canal;


--
-- TOC entry 1831 (class 1259 OID 2025567)
-- Name: mtrtb007_atributo_documento; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7662 (class 0 OID 0)
-- Dependencies: 1831
-- Name: TABLE mtrtb007_atributo_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb007_atributo_documento IS 'Tabela responsável por armazenar os atributos capturados do documento utilizando a estrutura de chave x valor onde o nome do atributo determina o campo do documento que a informação foi extraída e o conteúdo trata-se do dado propriamente extraído.';


--
-- TOC entry 7663 (class 0 OID 0)
-- Dependencies: 1831
-- Name: COLUMN mtrtb007_atributo_documento.nu_atributo_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb007_atributo_documento.nu_atributo_documento IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7664 (class 0 OID 0)
-- Dependencies: 1831
-- Name: COLUMN mtrtb007_atributo_documento.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb007_atributo_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7665 (class 0 OID 0)
-- Dependencies: 1831
-- Name: COLUMN mtrtb007_atributo_documento.nu_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb007_atributo_documento.nu_documento IS 'Atributo utilizado para vincular o atributo definido ao documento cuja informação foi extraída.';


--
-- TOC entry 7666 (class 0 OID 0)
-- Dependencies: 1831
-- Name: COLUMN mtrtb007_atributo_documento.de_atributo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb007_atributo_documento.de_atributo IS 'Atributo utilizado para armazenar a descrição da chave que identifica o atributo.
Como exemplo, um registro que armazena a data de nascimento de um RG: 
data_nascimento = 01/09/1980
Neste caso o conteúdo deste campo seria "data nascimento" e o atributo conteúdo armazenaria "01/09/1980" tal qual extraído do documento.';


--
-- TOC entry 7667 (class 0 OID 0)
-- Dependencies: 1831
-- Name: COLUMN mtrtb007_atributo_documento.de_conteudo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb007_atributo_documento.de_conteudo IS 'Atributo utilizado para armazenar a dado extraído de um campo do documento.
Como exemplo, um registro que armazena a data de nascimento de um RG: 
data nascimento = 01/09/1980
Neste caso o conteúdo deste campo seria "01/09/1980" tal qual extraído do documento e o atributo de descrição armazenaria "data nascimento".';


--
-- TOC entry 7668 (class 0 OID 0)
-- Dependencies: 1831
-- Name: COLUMN mtrtb007_atributo_documento.ix_assertividade; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb007_atributo_documento.ix_assertividade IS 'Atributo utilizado para armazenar o indice de assertividade retornado pelo serviço de extração de dados. ';


--
-- TOC entry 7669 (class 0 OID 0)
-- Dependencies: 1831
-- Name: COLUMN mtrtb007_atributo_documento.ic_acerto_manual; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb007_atributo_documento.ic_acerto_manual IS 'Atributo que representa se houver ajuste manual no atributo extraído';


--
-- TOC entry 1832 (class 1259 OID 2025573)
-- Name: mtrsq007_atributo_documento; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq007_atributo_documento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7670 (class 0 OID 0)
-- Dependencies: 1832
-- Name: mtrsq007_atributo_documento; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq007_atributo_documento OWNED BY mtrtb007_atributo_documento.nu_atributo_documento;


--
-- TOC entry 1919 (class 1259 OID 2026475)
-- Name: mtrsq008_conteudo; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq008_conteudo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1833 (class 1259 OID 2025575)
-- Name: mtrtb009_tipo_documento; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7671 (class 0 OID 0)
-- Dependencies: 1833
-- Name: TABLE mtrtb009_tipo_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb009_tipo_documento IS 'Tabela responsável pelo armazenamento dos possíveis tipos de documento que podem ser submetidos ao vínculo com os dossiês.';


--
-- TOC entry 7672 (class 0 OID 0)
-- Dependencies: 1833
-- Name: COLUMN mtrtb009_tipo_documento.nu_tipo_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.nu_tipo_documento IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 7673 (class 0 OID 0)
-- Dependencies: 1833
-- Name: COLUMN mtrtb009_tipo_documento.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7674 (class 0 OID 0)
-- Dependencies: 1833
-- Name: COLUMN mtrtb009_tipo_documento.no_tipo_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.no_tipo_documento IS 'Atributo que identifica o tipo de documento vinculado. Como exemplo podemos ter:
- RG
- CNH
- Certidão Negativa de Debito
- Passaporte
- Etc	';


--
-- TOC entry 7675 (class 0 OID 0)
-- Dependencies: 1833
-- Name: COLUMN mtrtb009_tipo_documento.ic_tipo_pessoa; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.ic_tipo_pessoa IS 'Atributo que determina qual tipo de pessoa pode ter o documento atribuído.
Pode assumir os seguintes valores:
F - Física
J - Jurídica
S - Serviço
A - Física ou Jurídica
G - Garantia
P - Produto
T - Todos';


--
-- TOC entry 7676 (class 0 OID 0)
-- Dependencies: 1833
-- Name: COLUMN mtrtb009_tipo_documento.pz_validade_dias; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.pz_validade_dias IS 'Atributo que indica a quantidade de dias para atribuição da validade do documento a partir da sua emissão.
Caso o valor deste atributo não esteja definido, significa que o documento possui um prazo de validade indeterminado.';


--
-- TOC entry 7677 (class 0 OID 0)
-- Dependencies: 1833
-- Name: COLUMN mtrtb009_tipo_documento.ic_validade_auto_contida; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.ic_validade_auto_contida IS 'Atributo determina se a validade do documento está definida no próprio documento ou não como por exemplo no caso de certidões que possuem a validade determinada em seu corpo.
Caso o valor deste atributo seja falso, o prazo de validade deve ser calculado conforme definido no atributo de prazo de validade.';


--
-- TOC entry 7678 (class 0 OID 0)
-- Dependencies: 1833
-- Name: COLUMN mtrtb009_tipo_documento.co_tipologia; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.co_tipologia IS 'Atributo utilizado para armazenar o código da tipologia documental corporativa.';


--
-- TOC entry 7679 (class 0 OID 0)
-- Dependencies: 1833
-- Name: COLUMN mtrtb009_tipo_documento.ic_reuso; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.ic_reuso IS 'Atributo utilizado para identificar se deve ser aplicado reuso ou não na carga do documento.

Existem situações, em que é necessario carregar um novo documento do mesmo tipo de forma atualizada, pois espera-se que o documento contenha novas informações devido ao andamento da contratação.

Nestes casos, não deverá ser realizada a inclusão de uma instancia do documento de forma automatica para esta determinada tipologia';


--
-- TOC entry 7680 (class 0 OID 0)
-- Dependencies: 1833
-- Name: COLUMN mtrtb009_tipo_documento.ic_processo_administrativo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.ic_processo_administrativo IS 'Atributo utilizado para identificar se o tipo de documento faz utilização perante o Processo Administrativo Eletronico (PAE) considerando que o mesmo possui restrições de apresentação quanto as opções de seleção para o usuário.';


--
-- TOC entry 7681 (class 0 OID 0)
-- Dependencies: 1833
-- Name: COLUMN mtrtb009_tipo_documento.ic_dossie_digital; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.ic_dossie_digital IS 'Atributo utilizado para identificar se o tipo de documento faz utilização perante o Dossiê Digital.';


--
-- TOC entry 7682 (class 0 OID 0)
-- Dependencies: 1833
-- Name: COLUMN mtrtb009_tipo_documento.ic_apoio_negocio; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.ic_apoio_negocio IS 'Atributo utilizado para identificar se o tipo de documento faz utilização perante o Apoio ao Negocio.';


--
-- TOC entry 7683 (class 0 OID 0)
-- Dependencies: 1833
-- Name: COLUMN mtrtb009_tipo_documento.no_arquivo_minuta; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.no_arquivo_minuta IS 'Atributo utilizado para indicar o nome do arquivo utilizado pelo gerador de relatorio na emissão da minuta do documento';


--
-- TOC entry 7684 (class 0 OID 0)
-- Dependencies: 1833
-- Name: COLUMN mtrtb009_tipo_documento.de_tags; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.de_tags IS 'Atributo utilizado para realizar a vinculação de TAGS pre definidaas ao tipo documental. Essas Tasgs devem ser sepradas por ";" (ponto e virgula) serão utilizadas para localizar documentos que estejam associados ao tipo que tenha relação com TAG pesquisada.

Ex: 
Tipo de Documento            |  TAGS
-------------------------------------------------------------------------------------
Comunicado Interno           | CI;Comunicados
Certidão Negativa Receita | Certidões;Certidão;Certidao
';


--
-- TOC entry 7685 (class 0 OID 0)
-- Dependencies: 1833
-- Name: COLUMN mtrtb009_tipo_documento.ic_validacao_cadastral; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.ic_validacao_cadastral IS 'Indica se o documento pode ser enviado a avaliação de validade cadastral.

Atualmente essa avaliação é realizada pelo SIFRC';


--
-- TOC entry 7686 (class 0 OID 0)
-- Dependencies: 1833
-- Name: COLUMN mtrtb009_tipo_documento.ic_validacao_documental; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb009_tipo_documento.ic_validacao_documental IS 'Indica se o documento pode ser enviado a avaliação de validade documental.

Atualmente essa avaliação é realizada pelo SICOD';


--
-- TOC entry 1834 (class 1259 OID 2025581)
-- Name: mtrsq009_tipo_documento; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq009_tipo_documento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7687 (class 0 OID 0)
-- Dependencies: 1834
-- Name: mtrsq009_tipo_documento; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq009_tipo_documento OWNED BY mtrtb009_tipo_documento.nu_tipo_documento;


--
-- TOC entry 1835 (class 1259 OID 2025583)
-- Name: mtrtb010_funcao_documental; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb010_funcao_documental (
    nu_funcao_documental integer NOT NULL,
    nu_versao integer NOT NULL,
    no_funcao character varying(100) NOT NULL
);


--
-- TOC entry 7688 (class 0 OID 0)
-- Dependencies: 1835
-- Name: TABLE mtrtb010_funcao_documental; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb010_funcao_documental IS 'Tabela responsável por armazenar as possíveis funções documentais.
Essa informação permite agrupar documentos que possuem a mesma finalidade e um documento pode possui mais de uma função.
Exemplos dessa atribuição funcional, são:
- Identificação;
- Renda;
- Comprovação de Residência
- etc';


--
-- TOC entry 7689 (class 0 OID 0)
-- Dependencies: 1835
-- Name: COLUMN mtrtb010_funcao_documental.nu_funcao_documental; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb010_funcao_documental.nu_funcao_documental IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 7690 (class 0 OID 0)
-- Dependencies: 1835
-- Name: COLUMN mtrtb010_funcao_documental.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb010_funcao_documental.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7691 (class 0 OID 0)
-- Dependencies: 1835
-- Name: COLUMN mtrtb010_funcao_documental.no_funcao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb010_funcao_documental.no_funcao IS 'Atributo definido para armazenar o nome da função documental, como por exemplo:
- Identificação
- Comprovação de Renda
- Comprovação de Residência
- Regularidade Fiscal
- etc';


--
-- TOC entry 1836 (class 1259 OID 2025586)
-- Name: mtrsq010_funcao_documental; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq010_funcao_documental
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7692 (class 0 OID 0)
-- Dependencies: 1836
-- Name: mtrsq010_funcao_documental; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq010_funcao_documental OWNED BY mtrtb010_funcao_documental.nu_funcao_documental;


--
-- TOC entry 1837 (class 1259 OID 2025588)
-- Name: mtrtb012_tipo_situacao_dossie; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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
-- TOC entry 7693 (class 0 OID 0)
-- Dependencies: 1837
-- Name: TABLE mtrtb012_tipo_situacao_dossie; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb012_tipo_situacao_dossie IS 'Tabela responsável pelo armazenamento das possíveis situações vinculadas a um dossiê de produto.

Como exemplo podemos ter as possíveis situações:
- Criado
- Atualizado
- Disponível
- Em Análise
- etc';


--
-- TOC entry 7694 (class 0 OID 0)
-- Dependencies: 1837
-- Name: COLUMN mtrtb012_tipo_situacao_dossie.nu_tipo_situacao_dossie; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.nu_tipo_situacao_dossie IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 7695 (class 0 OID 0)
-- Dependencies: 1837
-- Name: COLUMN mtrtb012_tipo_situacao_dossie.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.
COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.no_tipo_situacao IS Atributo que armazena o nome do tipo de situação do dossiê.';


--
-- TOC entry 7696 (class 0 OID 0)
-- Dependencies: 1837
-- Name: COLUMN mtrtb012_tipo_situacao_dossie.no_tipo_situacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.no_tipo_situacao IS 'Atributo que armazena o nome do tipo de situação do dossiê';


--
-- TOC entry 7697 (class 0 OID 0)
-- Dependencies: 1837
-- Name: COLUMN mtrtb012_tipo_situacao_dossie.ic_resumo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.ic_resumo IS 'Atributo utilizado para indicar se o tipo de situação gera agrupamento para exibição de resumo de dossiês.';


--
-- TOC entry 7698 (class 0 OID 0)
-- Dependencies: 1837
-- Name: COLUMN mtrtb012_tipo_situacao_dossie.ic_fila_tratamento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.ic_fila_tratamento IS 'Atrinuto utilizado para indicar se o tipo de situação inclui o dossiês na fila para tratamento';


--
-- TOC entry 7699 (class 0 OID 0)
-- Dependencies: 1837
-- Name: COLUMN mtrtb012_tipo_situacao_dossie.ic_produtividade; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.ic_produtividade IS 'Atributo utilizado para indicar se o tipo de situação considera o dossiê na contagem da produtividade diária.';


--
-- TOC entry 7700 (class 0 OID 0)
-- Dependencies: 1837
-- Name: COLUMN mtrtb012_tipo_situacao_dossie.ic_tipo_inicial; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb012_tipo_situacao_dossie.ic_tipo_inicial IS 'Atributo utilizado para identificar o tipo de situação que deve ser aplicado como primeiro tipo de situação de um dossiê de produto.';


--
-- TOC entry 1838 (class 1259 OID 2025593)
-- Name: mtrsq012_tipo_situacao_dossie; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq012_tipo_situacao_dossie
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7701 (class 0 OID 0)
-- Dependencies: 1838
-- Name: mtrsq012_tipo_situacao_dossie; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq012_tipo_situacao_dossie OWNED BY mtrtb012_tipo_situacao_dossie.nu_tipo_situacao_dossie;


--
-- TOC entry 1839 (class 1259 OID 2025595)
-- Name: mtrtb013_situacao_dossie; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7702 (class 0 OID 0)
-- Dependencies: 1839
-- Name: TABLE mtrtb013_situacao_dossie; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb013_situacao_dossie IS 'Tabela responsável por armazenar o histórico de situações relativas ao dossiê do produto. Cada vez que houver uma mudança na situação apresentada pelo processo, um novo registro deve ser inserido gerando assim um histórico das situações vivenciadas durante o seu ciclo de vida.';


--
-- TOC entry 7703 (class 0 OID 0)
-- Dependencies: 1839
-- Name: COLUMN mtrtb013_situacao_dossie.nu_situacao_dossie; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb013_situacao_dossie.nu_situacao_dossie IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7704 (class 0 OID 0)
-- Dependencies: 1839
-- Name: COLUMN mtrtb013_situacao_dossie.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb013_situacao_dossie.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7705 (class 0 OID 0)
-- Dependencies: 1839
-- Name: COLUMN mtrtb013_situacao_dossie.nu_dossie_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb013_situacao_dossie.nu_dossie_produto IS 'Atributo utilizado pata armazenar a referência do dossiê do produto vinculado a situação.';


--
-- TOC entry 7706 (class 0 OID 0)
-- Dependencies: 1839
-- Name: COLUMN mtrtb013_situacao_dossie.nu_tipo_situacao_dossie; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb013_situacao_dossie.nu_tipo_situacao_dossie IS 'Atributo utilizado para armazenar o tipo situação do dossiê que será atribuido manualmente pelo operador ou pela automacao do workflow quando estruturado.';


--
-- TOC entry 7707 (class 0 OID 0)
-- Dependencies: 1839
-- Name: COLUMN mtrtb013_situacao_dossie.ts_inclusao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb013_situacao_dossie.ts_inclusao IS 'Atributo utilizado para armazenar a data/hora de atribuição da situação ao dossiê.';


--
-- TOC entry 7708 (class 0 OID 0)
-- Dependencies: 1839
-- Name: COLUMN mtrtb013_situacao_dossie.co_matricula; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb013_situacao_dossie.co_matricula IS 'Atributo utilizado para armazenar a matrícula do empregado ou serviço que atribuiu a situação ao dossiê.';


--
-- TOC entry 7709 (class 0 OID 0)
-- Dependencies: 1839
-- Name: COLUMN mtrtb013_situacao_dossie.nu_unidade; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb013_situacao_dossie.nu_unidade IS 'Atributo que indica a unidade do empregado que registrou a situação do dossiê.';


--
-- TOC entry 7710 (class 0 OID 0)
-- Dependencies: 1839
-- Name: COLUMN mtrtb013_situacao_dossie.de_observacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb013_situacao_dossie.de_observacao IS 'Informação do usuário indicando uma observação com relação ao motivo de atribuição da situação definida.';


--
-- TOC entry 1840 (class 1259 OID 2025601)
-- Name: mtrsq013_situacao_dossie; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq013_situacao_dossie
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7711 (class 0 OID 0)
-- Dependencies: 1840
-- Name: mtrsq013_situacao_dossie; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq013_situacao_dossie OWNED BY mtrtb013_situacao_dossie.nu_situacao_dossie;


--
-- TOC entry 1841 (class 1259 OID 2025603)
-- Name: mtrtb014_instancia_documento; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7712 (class 0 OID 0)
-- Dependencies: 1841
-- Name: TABLE mtrtb014_instancia_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb014_instancia_documento IS 'Tabela responsável pelo armazenamento de instâncias de documentos que estarão vinculados aos dossiês dos produtos.';


--
-- TOC entry 7713 (class 0 OID 0)
-- Dependencies: 1841
-- Name: COLUMN mtrtb014_instancia_documento.nu_instancia_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb014_instancia_documento.nu_instancia_documento IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7714 (class 0 OID 0)
-- Dependencies: 1841
-- Name: COLUMN mtrtb014_instancia_documento.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb014_instancia_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7715 (class 0 OID 0)
-- Dependencies: 1841
-- Name: COLUMN mtrtb014_instancia_documento.nu_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb014_instancia_documento.nu_documento IS 'Atributo que vincula o registro da instância de documento ao documento propriamente dito permitindo assim o reaproveitamento de documento previamente existentes.';


--
-- TOC entry 7716 (class 0 OID 0)
-- Dependencies: 1841
-- Name: COLUMN mtrtb014_instancia_documento.nu_dossie_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb014_instancia_documento.nu_dossie_produto IS 'Atributo que armazena a referência do dossiê de produto vinculado a instância do documento.';


--
-- TOC entry 7717 (class 0 OID 0)
-- Dependencies: 1841
-- Name: COLUMN mtrtb014_instancia_documento.nu_elemento_conteudo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb014_instancia_documento.nu_elemento_conteudo IS 'Atributo que representa o elemento de conteúdo do processo ao qual foi a instância foi vinculada. Utilizado apenas para os casos de documentos submetidos pelo mapa de processo. Para os casos de documento do cliente associados/utilizados no dossiê do produto este atributo não estará definido.';


--
-- TOC entry 7718 (class 0 OID 0)
-- Dependencies: 1841
-- Name: COLUMN mtrtb014_instancia_documento.nu_garantia_informada; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb014_instancia_documento.nu_garantia_informada IS 'Atributo utilizado pata armazenar a referência da garantia informada vinculada a instância do documento.';


--
-- TOC entry 1842 (class 1259 OID 2025606)
-- Name: mtrsq014_instancia_documento; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq014_instancia_documento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7719 (class 0 OID 0)
-- Dependencies: 1842
-- Name: mtrsq014_instancia_documento; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq014_instancia_documento OWNED BY mtrtb014_instancia_documento.nu_instancia_documento;


--
-- TOC entry 1843 (class 1259 OID 2025608)
-- Name: mtrtb015_situacao_documento; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb015_situacao_documento (
    nu_situacao_documento integer NOT NULL,
    nu_versao integer NOT NULL,
    no_situacao character varying(100) NOT NULL,
    ic_situacao_inicial boolean DEFAULT false NOT NULL,
    ic_situacao_final boolean DEFAULT false NOT NULL
);


--
-- TOC entry 7720 (class 0 OID 0)
-- Dependencies: 1843
-- Name: TABLE mtrtb015_situacao_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb015_situacao_documento IS 'Tabela responsável pelo armazenamento das possíveis situações vinculadas a um documento.
Essas situações também deverão agrupar motivos para atribuição desta situação.
Como exemplo podemos ter as possíveis situações e entre parênteses os motivos de agrupamento:
- Aprovado
- Rejeitado (Ilegível / Rasurado / Segurança)
- Pendente (Recaptura)';


--
-- TOC entry 7721 (class 0 OID 0)
-- Dependencies: 1843
-- Name: COLUMN mtrtb015_situacao_documento.nu_situacao_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb015_situacao_documento.nu_situacao_documento IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 7722 (class 0 OID 0)
-- Dependencies: 1843
-- Name: COLUMN mtrtb015_situacao_documento.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb015_situacao_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7723 (class 0 OID 0)
-- Dependencies: 1843
-- Name: COLUMN mtrtb015_situacao_documento.no_situacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb015_situacao_documento.no_situacao IS 'Atributo que armazena o nome da situação do documento.';


--
-- TOC entry 7724 (class 0 OID 0)
-- Dependencies: 1843
-- Name: COLUMN mtrtb015_situacao_documento.ic_situacao_inicial; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb015_situacao_documento.ic_situacao_inicial IS 'Atributo utilizado para identificar o tipo de situação que deve ser aplicado como primeiro tipo de situação de uma instância de documento.';


--
-- TOC entry 7725 (class 0 OID 0)
-- Dependencies: 1843
-- Name: COLUMN mtrtb015_situacao_documento.ic_situacao_final; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb015_situacao_documento.ic_situacao_final IS 'Atributo utilizado para identificar o tipo de situação que é final. Após ser aplicado uma situação deste tipo, o sistema não deverá permitir que sejam criados novos registros de situação para a instância dos documentos.';


--
-- TOC entry 1844 (class 1259 OID 2025613)
-- Name: mtrsq015_situacao_documento; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq015_situacao_documento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7726 (class 0 OID 0)
-- Dependencies: 1844
-- Name: mtrsq015_situacao_documento; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq015_situacao_documento OWNED BY mtrtb015_situacao_documento.nu_situacao_documento;


--
-- TOC entry 1845 (class 1259 OID 2025615)
-- Name: mtrtb016_motivo_stco_documento; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb016_motivo_stco_documento (
    nu_motivo_stco_documento integer NOT NULL,
    nu_versao integer NOT NULL,
    nu_situacao_documento integer NOT NULL,
    no_motivo_stco_documento character varying(100) NOT NULL
);


--
-- TOC entry 7727 (class 0 OID 0)
-- Dependencies: 1845
-- Name: TABLE mtrtb016_motivo_stco_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb016_motivo_stco_documento IS 'Tabela de motivos específicos para indicar a causa de uma determinada situação vinculada a um dado documento.
Exemplo:  
- Ilegível -> Rejeitado
- Rasurado -> rejeitado
- Segurança -> Rejeitado
- Recaptura -> pendente';


--
-- TOC entry 7728 (class 0 OID 0)
-- Dependencies: 1845
-- Name: COLUMN mtrtb016_motivo_stco_documento.nu_motivo_stco_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb016_motivo_stco_documento.nu_motivo_stco_documento IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 7729 (class 0 OID 0)
-- Dependencies: 1845
-- Name: COLUMN mtrtb016_motivo_stco_documento.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb016_motivo_stco_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7730 (class 0 OID 0)
-- Dependencies: 1845
-- Name: COLUMN mtrtb016_motivo_stco_documento.nu_situacao_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb016_motivo_stco_documento.nu_situacao_documento IS 'Atributo utilizado para vincular o motivo com uma situação especifica.';


--
-- TOC entry 7731 (class 0 OID 0)
-- Dependencies: 1845
-- Name: COLUMN mtrtb016_motivo_stco_documento.no_motivo_stco_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb016_motivo_stco_documento.no_motivo_stco_documento IS 'Atributo que armazena o nome do motivo da situação do documento.';


--
-- TOC entry 1846 (class 1259 OID 2025618)
-- Name: mtrsq016_motivo_stco_documento; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq016_motivo_stco_documento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7732 (class 0 OID 0)
-- Dependencies: 1846
-- Name: mtrsq016_motivo_stco_documento; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq016_motivo_stco_documento OWNED BY mtrtb016_motivo_stco_documento.nu_motivo_stco_documento;


--
-- TOC entry 1847 (class 1259 OID 2025620)
-- Name: mtrsq017_situacao_instnca_dcmnto; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq017_situacao_instnca_dcmnto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1848 (class 1259 OID 2025622)
-- Name: mtrsq019_campo_formulario; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq019_campo_formulario
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1849 (class 1259 OID 2025624)
-- Name: mtrtb020_processo; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7733 (class 0 OID 0)
-- Dependencies: 1849
-- Name: TABLE mtrtb020_processo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb020_processo IS 'Tabela responsável pelo armazenamento dos processos que podem ser atrelados aos dossiês de forma a identificar qual o processo bancário relacionado.
Processos que possuam vinculação com dossiês de produto não devem ser excluídos fisicamente, e sim atribuídos como inativo.
Exemplos de processos na linguagem negocial são:
- Concessão de Crédito Habitacional
- Conta Corrente
- Financiamento de Veículos
- Pagamento de Loterias
- Etc';


--
-- TOC entry 7734 (class 0 OID 0)
-- Dependencies: 1849
-- Name: COLUMN mtrtb020_processo.nu_processo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb020_processo.nu_processo IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 7735 (class 0 OID 0)
-- Dependencies: 1849
-- Name: COLUMN mtrtb020_processo.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb020_processo.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7736 (class 0 OID 0)
-- Dependencies: 1849
-- Name: COLUMN mtrtb020_processo.no_processo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb020_processo.no_processo IS 'Atributo utilizado para armazenar o nome de identificação negocial do processo.';


--
-- TOC entry 7737 (class 0 OID 0)
-- Dependencies: 1849
-- Name: COLUMN mtrtb020_processo.ic_ativo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb020_processo.ic_ativo IS 'Atributo que indica que se o processo esta ativo ou não para utilização pelo sistema.';


--
-- TOC entry 7738 (class 0 OID 0)
-- Dependencies: 1849
-- Name: COLUMN mtrtb020_processo.de_avatar; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb020_processo.de_avatar IS 'Atributo utilizado para armazenar o nome do avatar que será disponibilizado no pacote da ineterface grafica para montagem e apresentação das filas de captura o uinformações do processo.';


--
-- TOC entry 7739 (class 0 OID 0)
-- Dependencies: 1849
-- Name: COLUMN mtrtb020_processo.no_processo_bpm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb020_processo.no_processo_bpm IS 'Atributo que armazena o valor de identificação do processo orignador junto a solução de BPM.
Ex: bpm-simtr.ProcessoConformidade';


--
-- TOC entry 7740 (class 0 OID 0)
-- Dependencies: 1849
-- Name: COLUMN mtrtb020_processo.no_container_bpm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb020_processo.no_container_bpm IS 'Atributo que armazena o valor de identificação do container utilizado no agrupamento dos processos junto a solução de BPM que possui o processo originador vinculado.
Ex: bpm-simtr_1.0.0';


--
-- TOC entry 1850 (class 1259 OID 2025631)
-- Name: mtrsq020_processo; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq020_processo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7741 (class 0 OID 0)
-- Dependencies: 1850
-- Name: mtrsq020_processo; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq020_processo OWNED BY mtrtb020_processo.nu_processo;


--
-- TOC entry 1851 (class 1259 OID 2025633)
-- Name: mtrtb021_unidade_autorizada; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7742 (class 0 OID 0)
-- Dependencies: 1851
-- Name: TABLE mtrtb021_unidade_autorizada; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb021_unidade_autorizada IS 'Tabela responsável pelo armazenamento das unidades autorizadas a utilização do processo.';


--
-- TOC entry 7743 (class 0 OID 0)
-- Dependencies: 1851
-- Name: COLUMN mtrtb021_unidade_autorizada.nu_unidade_autorizada; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb021_unidade_autorizada.nu_unidade_autorizada IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7744 (class 0 OID 0)
-- Dependencies: 1851
-- Name: COLUMN mtrtb021_unidade_autorizada.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb021_unidade_autorizada.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7745 (class 0 OID 0)
-- Dependencies: 1851
-- Name: COLUMN mtrtb021_unidade_autorizada.nu_processo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb021_unidade_autorizada.nu_processo IS 'Atributo que representa o processo na definição de autorização a realização de algum tipo de tratamento por uma determinada unidade.';


--
-- TOC entry 7746 (class 0 OID 0)
-- Dependencies: 1851
-- Name: COLUMN mtrtb021_unidade_autorizada.nu_unidade; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb021_unidade_autorizada.nu_unidade IS 'Atributo que representa o CGC da unidade autorizada.';


--
-- TOC entry 7747 (class 0 OID 0)
-- Dependencies: 1851
-- Name: COLUMN mtrtb021_unidade_autorizada.ic_tipo_tratamento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb021_unidade_autorizada.ic_tipo_tratamento IS 'Atributo que indica as ações possiveis a serem realizadas no sistema para a determinada unidade de acordo com o registro de vinculação da autorização. A representação dos valores das ações são definidas em numeração binaria e determinam quais as permissões da unidade sobre os itens aos quais estão vinculados. Os valores possiveis são:

1 - CONSULTA_DOSSIE
2 - TRATAR_DOSSIE
4 - CRIAR_DOSSIE
8 - PRIORIZAR_DOSSIE
16 - ALIMENTAR_DOSSIE

Considerando o fato, como exemplo uma unidade que possa consultar, tratar e criar dossiês, mas não pode priorizar, nem alimentar o dossiê e deve estar representado o valor 7 no seguinte formato:

0000000111';


--
-- TOC entry 1852 (class 1259 OID 2025636)
-- Name: mtrsq021_unidade_autorizada; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq021_unidade_autorizada
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7748 (class 0 OID 0)
-- Dependencies: 1852
-- Name: mtrsq021_unidade_autorizada; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq021_unidade_autorizada OWNED BY mtrtb021_unidade_autorizada.nu_unidade_autorizada;


--
-- TOC entry 1853 (class 1259 OID 2025638)
-- Name: mtrtb022_produto; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7749 (class 0 OID 0)
-- Dependencies: 1853
-- Name: TABLE mtrtb022_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb022_produto IS 'Tabela responsável pelo armazenamento dos produtos da CAIXA que serão vinculados aos processos definidos.';


--
-- TOC entry 7750 (class 0 OID 0)
-- Dependencies: 1853
-- Name: COLUMN mtrtb022_produto.nu_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.nu_produto IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7751 (class 0 OID 0)
-- Dependencies: 1853
-- Name: COLUMN mtrtb022_produto.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7752 (class 0 OID 0)
-- Dependencies: 1853
-- Name: COLUMN mtrtb022_produto.nu_operacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.nu_operacao IS 'Atributo que armazena o número de operação corporativa do produto.';


--
-- TOC entry 7753 (class 0 OID 0)
-- Dependencies: 1853
-- Name: COLUMN mtrtb022_produto.nu_modalidade; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.nu_modalidade IS 'Atributo que armazena o número da modalidade corporativa do produto.';


--
-- TOC entry 7754 (class 0 OID 0)
-- Dependencies: 1853
-- Name: COLUMN mtrtb022_produto.no_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.no_produto IS 'Atributo que armazena o nome corporativo do produto.';


--
-- TOC entry 7755 (class 0 OID 0)
-- Dependencies: 1853
-- Name: COLUMN mtrtb022_produto.ic_contratacao_conjunta; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.ic_contratacao_conjunta IS 'Atributo utilizado para identificar se o produto permite realizar contatação conjunta com outros produtos, caso o atributo esteja setado como false, será criado um dossiê individual para o cada produto selecionado nessa consição.';


--
-- TOC entry 7756 (class 0 OID 0)
-- Dependencies: 1853
-- Name: COLUMN mtrtb022_produto.ic_pesquisa_cadin; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.ic_pesquisa_cadin IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao CADIN retorne resultado alguma restrição.';


--
-- TOC entry 7757 (class 0 OID 0)
-- Dependencies: 1853
-- Name: COLUMN mtrtb022_produto.ic_pesquisa_scpc; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.ic_pesquisa_scpc IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao SCPC retorne resultado alguma restrição.';


--
-- TOC entry 7758 (class 0 OID 0)
-- Dependencies: 1853
-- Name: COLUMN mtrtb022_produto.ic_pesquisa_serasa; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.ic_pesquisa_serasa IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao SPC/SERASA retorne resultado alguma restrição.';


--
-- TOC entry 7759 (class 0 OID 0)
-- Dependencies: 1853
-- Name: COLUMN mtrtb022_produto.ic_pesquisa_ccf; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.ic_pesquisa_ccf IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao SICCF retorne resultado alguma restrição.';


--
-- TOC entry 7760 (class 0 OID 0)
-- Dependencies: 1853
-- Name: COLUMN mtrtb022_produto.ic_pesquisa_sicow; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.ic_pesquisa_sicow IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao SICOW retorne resultado alguma restrição.';


--
-- TOC entry 7761 (class 0 OID 0)
-- Dependencies: 1853
-- Name: COLUMN mtrtb022_produto.ic_pesquisa_receita; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.ic_pesquisa_receita IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto a Receita retorne resultado alguma restrição.';


--
-- TOC entry 7762 (class 0 OID 0)
-- Dependencies: 1853
-- Name: COLUMN mtrtb022_produto.ic_tipo_pessoa; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.ic_tipo_pessoa IS 'Atributo que determina qual tipo de pessoa pode contratar o produto.
Pode assumir os seguintes valores:
F - Física
J - Jurídica
A - Física ou Jurídica
';


--
-- TOC entry 7763 (class 0 OID 0)
-- Dependencies: 1853
-- Name: COLUMN mtrtb022_produto.ic_dossie_digital; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.ic_dossie_digital IS 'Atributo utilizado para indicar se o produto já esta mapeado para executar ações vinculadas ao modelo do dossiê digital';


--
-- TOC entry 7764 (class 0 OID 0)
-- Dependencies: 1853
-- Name: COLUMN mtrtb022_produto.ic_pesquisa_sinad; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb022_produto.ic_pesquisa_sinad IS 'Atributo utilizado para identificar se a pesquisa junto a Receita deve ser realizada para o produto especificado.';


--
-- TOC entry 1854 (class 1259 OID 2025647)
-- Name: mtrsq022_produto; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq022_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7765 (class 0 OID 0)
-- Dependencies: 1854
-- Name: mtrsq022_produto; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq022_produto OWNED BY mtrtb022_produto.nu_produto;


--
-- TOC entry 1855 (class 1259 OID 2025649)
-- Name: mtrtb024_produto_dossie; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7766 (class 0 OID 0)
-- Dependencies: 1855
-- Name: TABLE mtrtb024_produto_dossie; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb024_produto_dossie IS 'Tabela de relacionamento para vinculação dos produtos selecionados para tratamento no dossiê. 
Existe a possibilidade que mais de um produto seja vinculado a um dossiê para tratamento único como é o caso do contrato de relacionamento que envolve Cartão de Credito / CROT / CDC / Conta Corrente.';


--
-- TOC entry 7767 (class 0 OID 0)
-- Dependencies: 1855
-- Name: COLUMN mtrtb024_produto_dossie.nu_produto_dossie; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.nu_produto_dossie IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7768 (class 0 OID 0)
-- Dependencies: 1855
-- Name: COLUMN mtrtb024_produto_dossie.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7769 (class 0 OID 0)
-- Dependencies: 1855
-- Name: COLUMN mtrtb024_produto_dossie.nu_dossie_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.nu_dossie_produto IS 'Atributo que representa o dossiê de vinculação da coleção de produtos em análise.';


--
-- TOC entry 7770 (class 0 OID 0)
-- Dependencies: 1855
-- Name: COLUMN mtrtb024_produto_dossie.nu_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.nu_produto IS 'Atributo que representa o produto vinculado na relação com o dossiê.';


--
-- TOC entry 7771 (class 0 OID 0)
-- Dependencies: 1855
-- Name: COLUMN mtrtb024_produto_dossie.vr_contrato; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.vr_contrato IS 'Atributo que representa o valor do produto.';


--
-- TOC entry 7772 (class 0 OID 0)
-- Dependencies: 1855
-- Name: COLUMN mtrtb024_produto_dossie.pc_juros_operacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.pc_juros_operacao IS 'Percentual de juros utilizado na contratação do produto.';


--
-- TOC entry 7773 (class 0 OID 0)
-- Dependencies: 1855
-- Name: COLUMN mtrtb024_produto_dossie.ic_periodo_juros; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.ic_periodo_juros IS 'Armazena o periodo de juros ao qual se refere a taxa. 
D - Diário
M - Mensal
A - Anual';


--
-- TOC entry 7774 (class 0 OID 0)
-- Dependencies: 1855
-- Name: COLUMN mtrtb024_produto_dossie.pz_operacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.pz_operacao IS 'Prazo utilizado na contratação do produto.';


--
-- TOC entry 7775 (class 0 OID 0)
-- Dependencies: 1855
-- Name: COLUMN mtrtb024_produto_dossie.pz_carencia; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.pz_carencia IS 'Prazo utilizado como carência na contratação do produto.';


--
-- TOC entry 7776 (class 0 OID 0)
-- Dependencies: 1855
-- Name: COLUMN mtrtb024_produto_dossie.ic_liquidacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.ic_liquidacao IS 'Atributo utilizado para indicar se a contratação do produto prevê a liquidação/renovação de um contrato.';


--
-- TOC entry 7777 (class 0 OID 0)
-- Dependencies: 1855
-- Name: COLUMN mtrtb024_produto_dossie.co_contrato_renovado; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb024_produto_dossie.co_contrato_renovado IS 'Atributo utilizado para armazenar o contrato liquidado/renovado.';


--
-- TOC entry 1856 (class 1259 OID 2025653)
-- Name: mtrsq024_produto_dossie; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq024_produto_dossie
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7778 (class 0 OID 0)
-- Dependencies: 1856
-- Name: mtrsq024_produto_dossie; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq024_produto_dossie OWNED BY mtrtb024_produto_dossie.nu_produto_dossie;


--
-- TOC entry 1857 (class 1259 OID 2025655)
-- Name: mtrtb025_processo_documento; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7779 (class 0 OID 0)
-- Dependencies: 1857
-- Name: TABLE mtrtb025_processo_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb025_processo_documento IS 'Tabela utilizada para identificar os documentos (por tipo ou função) que são necessários para os titulares do dossiê de um processo específico. Fazendo um paralelo, seria como o os elementos do conteúdo, porém voltados aos documentos do cliente, pois um dossiê pode ter a quantidade de clientes definida dinamicamente e por isso não cabe na estrutura do elemento do conteúdo. Esta estrutura ficará a cargo dos elementos específicos do produto.
Quando um dossiê é criado, todos os CNPFs/CNPJs envolvidos na operação deverão apresentar os tipos de documentos ou algum documento da função documental definidas nesta relação com o processo específico definido para o dossiê de produto.';


--
-- TOC entry 7780 (class 0 OID 0)
-- Dependencies: 1857
-- Name: COLUMN mtrtb025_processo_documento.nu_processo_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb025_processo_documento.nu_processo_documento IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7781 (class 0 OID 0)
-- Dependencies: 1857
-- Name: COLUMN mtrtb025_processo_documento.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb025_processo_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7782 (class 0 OID 0)
-- Dependencies: 1857
-- Name: COLUMN mtrtb025_processo_documento.nu_processo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb025_processo_documento.nu_processo IS 'Atributo utilizado para identificar o processo de vinculação para agrupar os documentos necessarios.';


--
-- TOC entry 7783 (class 0 OID 0)
-- Dependencies: 1857
-- Name: COLUMN mtrtb025_processo_documento.nu_funcao_documental; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb025_processo_documento.nu_funcao_documental IS 'Atrinuto utilizado para referenciar uma função documental necessaria ao processo. Quando definido, qualquer documento valido que o cliente tenha que seja desta função documental, deve ser considerado que o documento existente já atende a necessidade. Caso este atributo esteja nulo, o atributo que representa o tipo de documento deverá estar prenchido.';


--
-- TOC entry 7784 (class 0 OID 0)
-- Dependencies: 1857
-- Name: COLUMN mtrtb025_processo_documento.nu_tipo_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb025_processo_documento.nu_tipo_documento IS 'Atrinuto utilizado para referenciar um tipo de documento especifico, necessari ao processo. Quando definido, apenas a presença do documento especifica em estado valido, presente e associado ao dossiê do cliente deve ser considerado existente e já atende a necessidade. Caso este atributo esteja nulo, o atributo que representa a função documental deverá estar prenchido.';


--
-- TOC entry 7785 (class 0 OID 0)
-- Dependencies: 1857
-- Name: COLUMN mtrtb025_processo_documento.ic_tipo_relacionamento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb025_processo_documento.ic_tipo_relacionamento IS 'Atributo que indica o tipo de relacionamento do cliente com o produto, podendo assumir os valores:
- TITULAR
- AVALISTA
- CONJUGE
- SOCIO
etc.';


--
-- TOC entry 1858 (class 1259 OID 2025658)
-- Name: mtrsq025_processo_documento; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq025_processo_documento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7786 (class 0 OID 0)
-- Dependencies: 1858
-- Name: mtrsq025_processo_documento; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq025_processo_documento OWNED BY mtrtb025_processo_documento.nu_processo_documento;


--
-- TOC entry 1859 (class 1259 OID 2025660)
-- Name: mtrtb027_campo_entrada; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7787 (class 0 OID 0)
-- Dependencies: 1859
-- Name: TABLE mtrtb027_campo_entrada; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb027_campo_entrada IS 'Tabela responsável por armazenar a estrutura de entradas de dados que serão alimentados na inclusão de um novo dossiê para o processo vinculado.
Esta estrutura permitirá realizar a construção dinâmica do formulário.
Registros desta tabela só devem ser excluídos fisicamente caso não exista nenhuma resposta de formulário atrelada a este registro. Caso essa situação ocorra o registro deve ser excluído logicamente definindo seu atributo ativo como false.';


--
-- TOC entry 7788 (class 0 OID 0)
-- Dependencies: 1859
-- Name: COLUMN mtrtb027_campo_entrada.nu_campo_entrada; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.nu_campo_entrada IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7789 (class 0 OID 0)
-- Dependencies: 1859
-- Name: COLUMN mtrtb027_campo_entrada.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7790 (class 0 OID 0)
-- Dependencies: 1859
-- Name: COLUMN mtrtb027_campo_entrada.ic_tipo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.ic_tipo IS 'Atributo utilizado para armazenar o tipo de campo de formulário que será gerado. Exemplos válidos para este atributo são:
- TEXT
- SELECT
- RADIO';


--
-- TOC entry 7791 (class 0 OID 0)
-- Dependencies: 1859
-- Name: COLUMN mtrtb027_campo_entrada.ic_chave; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.ic_chave IS 'Atributo que indica se o campo do formulário pode ser utilizado como chave de pesquisa posterior.';


--
-- TOC entry 7792 (class 0 OID 0)
-- Dependencies: 1859
-- Name: COLUMN mtrtb027_campo_entrada.no_label; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.no_label IS 'Atributo que armazena o valor a ser exibido no label do campo do formulário para o usuário final.';


--
-- TOC entry 7793 (class 0 OID 0)
-- Dependencies: 1859
-- Name: COLUMN mtrtb027_campo_entrada.de_mascara; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.de_mascara IS 'Atributo que armazena o valor da máscara de formatação do campo de for o caso.';


--
-- TOC entry 7794 (class 0 OID 0)
-- Dependencies: 1859
-- Name: COLUMN mtrtb027_campo_entrada.de_placeholder; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.de_placeholder IS 'Atributo que armazena o valor do placeholder para exibição no campo de for o caso.';


--
-- TOC entry 7795 (class 0 OID 0)
-- Dependencies: 1859
-- Name: COLUMN mtrtb027_campo_entrada.nu_tamanho_minimo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.nu_tamanho_minimo IS 'Atributo que armazena o número de caracteres mínimo utilizados em campos de texto livre.';


--
-- TOC entry 7796 (class 0 OID 0)
-- Dependencies: 1859
-- Name: COLUMN mtrtb027_campo_entrada.nu_tamanho_maximo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb027_campo_entrada.nu_tamanho_maximo IS 'Atributo que armazena o número de caracteres máximo utilizados em campos de texto livre.';


--
-- TOC entry 1860 (class 1259 OID 2025663)
-- Name: mtrsq027_campo_entrada; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq027_campo_entrada
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7797 (class 0 OID 0)
-- Dependencies: 1860
-- Name: mtrsq027_campo_entrada; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq027_campo_entrada OWNED BY mtrtb027_campo_entrada.nu_campo_entrada;


--
-- TOC entry 1861 (class 1259 OID 2025665)
-- Name: mtrtb028_opcao_campo; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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
-- TOC entry 7798 (class 0 OID 0)
-- Dependencies: 1861
-- Name: TABLE mtrtb028_opcao_campo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb028_opcao_campo IS 'Tabela responsável pelo armazenamento de opções pré-definidas para alguns tipos de atributos a exemplo:
- Lista;
- Radio;
- Check;

Registros desta tabela só devem ser excluídos fisicamente caso não exista nenhuma resposta de formulário atrelada a este registro. Caso essa situação ocorra o registro deve ser excluído logicamente definindo seu atributo ativo como false.';


--
-- TOC entry 7799 (class 0 OID 0)
-- Dependencies: 1861
-- Name: COLUMN mtrtb028_opcao_campo.nu_opcao_campo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb028_opcao_campo.nu_opcao_campo IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7800 (class 0 OID 0)
-- Dependencies: 1861
-- Name: COLUMN mtrtb028_opcao_campo.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb028_opcao_campo.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7801 (class 0 OID 0)
-- Dependencies: 1861
-- Name: COLUMN mtrtb028_opcao_campo.nu_campo_entrada; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb028_opcao_campo.nu_campo_entrada IS 'Atributo que identifica o campo de entrada do formulário ao qual a opção está associada.';


--
-- TOC entry 7802 (class 0 OID 0)
-- Dependencies: 1861
-- Name: COLUMN mtrtb028_opcao_campo.no_value; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb028_opcao_campo.no_value IS 'Atributo utilizado para armazenar o valor que será definido como value da opção na interface gráfica.';


--
-- TOC entry 7803 (class 0 OID 0)
-- Dependencies: 1861
-- Name: COLUMN mtrtb028_opcao_campo.no_opcao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb028_opcao_campo.no_opcao IS 'Atributo que armazena o valor da opção que será exibida para o usuário no campo do formulário.';


--
-- TOC entry 7804 (class 0 OID 0)
-- Dependencies: 1861
-- Name: COLUMN mtrtb028_opcao_campo.ic_ativo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb028_opcao_campo.ic_ativo IS 'Atributo que indica se a opção do campo de entrada está apta ou não para ser inserido no campo de entrada do formulário.';


--
-- TOC entry 1862 (class 1259 OID 2025668)
-- Name: mtrsq028_opcao_campo; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq028_opcao_campo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7805 (class 0 OID 0)
-- Dependencies: 1862
-- Name: mtrsq028_opcao_campo; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq028_opcao_campo OWNED BY mtrtb028_opcao_campo.nu_opcao_campo;


--
-- TOC entry 1863 (class 1259 OID 2025670)
-- Name: mtrtb029_campo_apresentacao; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb029_campo_apresentacao (
    nu_campo_apresentacao bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_largura integer NOT NULL,
    ic_dispositivo character varying(1) NOT NULL,
    nu_campo_formulario bigint NOT NULL
);


--
-- TOC entry 7806 (class 0 OID 0)
-- Dependencies: 1863
-- Name: TABLE mtrtb029_campo_apresentacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb029_campo_apresentacao IS 'Tabela utilizada para armazenar informações acerca da apresentação do campo na interface gráfica conforme o dispositivo.';


--
-- TOC entry 7807 (class 0 OID 0)
-- Dependencies: 1863
-- Name: COLUMN mtrtb029_campo_apresentacao.nu_campo_apresentacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb029_campo_apresentacao.nu_campo_apresentacao IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7808 (class 0 OID 0)
-- Dependencies: 1863
-- Name: COLUMN mtrtb029_campo_apresentacao.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb029_campo_apresentacao.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7809 (class 0 OID 0)
-- Dependencies: 1863
-- Name: COLUMN mtrtb029_campo_apresentacao.nu_largura; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb029_campo_apresentacao.nu_largura IS 'Atributo que armazena o número de colunas do bootstrap ocupadas pelo campo do formulário na estrutura de tela. Este valor pode variar de 1 a 12.';


--
-- TOC entry 7810 (class 0 OID 0)
-- Dependencies: 1863
-- Name: COLUMN mtrtb029_campo_apresentacao.ic_dispositivo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb029_campo_apresentacao.ic_dispositivo IS 'Atributo utilizado para indicar o dispositivo de renderizaçãodo componente em tela.
Pode assumir as seguintes opções:
- W (Web)
- L (Low DPI)
- M (Medium DPI)
- H (High DPI)
- X (eXtra DPI)';


--
-- TOC entry 1864 (class 1259 OID 2025673)
-- Name: mtrsq029_campo_apresentacao; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq029_campo_apresentacao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7811 (class 0 OID 0)
-- Dependencies: 1864
-- Name: mtrsq029_campo_apresentacao; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq029_campo_apresentacao OWNED BY mtrtb029_campo_apresentacao.nu_campo_apresentacao;


--
-- TOC entry 1865 (class 1259 OID 2025675)
-- Name: mtrtb030_resposta_dossie; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb030_resposta_dossie (
    nu_resposta_dossie bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_dossie_produto bigint NOT NULL,
    de_resposta text,
    nu_campo_formulario bigint NOT NULL
);


--
-- TOC entry 7812 (class 0 OID 0)
-- Dependencies: 1865
-- Name: TABLE mtrtb030_resposta_dossie; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb030_resposta_dossie IS 'Tabela responsável pelo armazenamento das respostas aos itens montados dos formulários de inclusão de processos para um dossiê específico.';


--
-- TOC entry 7813 (class 0 OID 0)
-- Dependencies: 1865
-- Name: COLUMN mtrtb030_resposta_dossie.nu_resposta_dossie; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb030_resposta_dossie.nu_resposta_dossie IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7814 (class 0 OID 0)
-- Dependencies: 1865
-- Name: COLUMN mtrtb030_resposta_dossie.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb030_resposta_dossie.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7815 (class 0 OID 0)
-- Dependencies: 1865
-- Name: COLUMN mtrtb030_resposta_dossie.nu_dossie_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb030_resposta_dossie.nu_dossie_produto IS 'Atributo utilizado para identificar o dossiê de produto ao qual a resposta esta vinculada.';


--
-- TOC entry 7816 (class 0 OID 0)
-- Dependencies: 1865
-- Name: COLUMN mtrtb030_resposta_dossie.de_resposta; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb030_resposta_dossie.de_resposta IS 'Atributo utilizado para armazenar a resposta informada no formulário nos casos de atributos em texto aberto.';


--
-- TOC entry 1866 (class 1259 OID 2025681)
-- Name: mtrsq030_resposta_dossie; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq030_resposta_dossie
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7817 (class 0 OID 0)
-- Dependencies: 1866
-- Name: mtrsq030_resposta_dossie; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq030_resposta_dossie OWNED BY mtrtb030_resposta_dossie.nu_resposta_dossie;


--
-- TOC entry 1867 (class 1259 OID 2025683)
-- Name: mtrsq032_elemento_conteudo; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq032_elemento_conteudo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1868 (class 1259 OID 2025685)
-- Name: mtrtb033_garantia; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb033_garantia (
    nu_garantia integer NOT NULL,
    nu_versao integer NOT NULL,
    nu_garantia_bacen integer NOT NULL,
    no_garantia character varying(255) NOT NULL,
    ic_fidejussoria boolean NOT NULL
);


--
-- TOC entry 7818 (class 0 OID 0)
-- Dependencies: 1868
-- Name: TABLE mtrtb033_garantia; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb033_garantia IS 'Tabela responsável pelo armazenamento das garantias da CAIXA que serão vinculados aos dossiês criados.';


--
-- TOC entry 7819 (class 0 OID 0)
-- Dependencies: 1868
-- Name: COLUMN mtrtb033_garantia.nu_garantia; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb033_garantia.nu_garantia IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 7820 (class 0 OID 0)
-- Dependencies: 1868
-- Name: COLUMN mtrtb033_garantia.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb033_garantia.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7821 (class 0 OID 0)
-- Dependencies: 1868
-- Name: COLUMN mtrtb033_garantia.nu_garantia_bacen; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb033_garantia.nu_garantia_bacen IS 'Atributo que armazena o número de operação corporativa da garantia.';


--
-- TOC entry 7822 (class 0 OID 0)
-- Dependencies: 1868
-- Name: COLUMN mtrtb033_garantia.no_garantia; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb033_garantia.no_garantia IS 'Atributo que armazena o nome corporativo da garantia';


--
-- TOC entry 7823 (class 0 OID 0)
-- Dependencies: 1868
-- Name: COLUMN mtrtb033_garantia.ic_fidejussoria; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb033_garantia.ic_fidejussoria IS 'Atributo utilizado para identificar se trata-se de uma garantia fidejussoria a exemplo do avalista';


--
-- TOC entry 1869 (class 1259 OID 2025688)
-- Name: mtrsq033_garantia; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq033_garantia
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7824 (class 0 OID 0)
-- Dependencies: 1869
-- Name: mtrsq033_garantia; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq033_garantia OWNED BY mtrtb033_garantia.nu_garantia;


--
-- TOC entry 1870 (class 1259 OID 2025690)
-- Name: mtrtb035_garantia_informada; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7825 (class 0 OID 0)
-- Dependencies: 1870
-- Name: TABLE mtrtb035_garantia_informada; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb035_garantia_informada IS 'Tabela responsável por manter a lista de garantias informadas durante o ciclo de vida do dossiê do produto.
Os documentos submetidos são arquivados normalmente na tabela de documentos e vinculados ao dossiê do produto através de instâncias.';


--
-- TOC entry 7826 (class 0 OID 0)
-- Dependencies: 1870
-- Name: COLUMN mtrtb035_garantia_informada.nu_garantia_informada; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb035_garantia_informada.nu_garantia_informada IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7827 (class 0 OID 0)
-- Dependencies: 1870
-- Name: COLUMN mtrtb035_garantia_informada.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb035_garantia_informada.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7828 (class 0 OID 0)
-- Dependencies: 1870
-- Name: COLUMN mtrtb035_garantia_informada.nu_dossie_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb035_garantia_informada.nu_dossie_produto IS 'Atributo utilizado para vincular o dossiê produto a garantia.';


--
-- TOC entry 7829 (class 0 OID 0)
-- Dependencies: 1870
-- Name: COLUMN mtrtb035_garantia_informada.nu_garantia; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb035_garantia_informada.nu_garantia IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 7830 (class 0 OID 0)
-- Dependencies: 1870
-- Name: COLUMN mtrtb035_garantia_informada.nu_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb035_garantia_informada.nu_produto IS 'Atributo utilizado para vincular o produto a garantia.';


--
-- TOC entry 7831 (class 0 OID 0)
-- Dependencies: 1870
-- Name: COLUMN mtrtb035_garantia_informada.vr_garantia_informada; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb035_garantia_informada.vr_garantia_informada IS 'Valor informado da garantia oferecida no dia da simulação.';


--
-- TOC entry 7832 (class 0 OID 0)
-- Dependencies: 1870
-- Name: COLUMN mtrtb035_garantia_informada.pc_garantia_informada; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb035_garantia_informada.pc_garantia_informada IS 'Percentual da garantia informada em relação ao valor pretendido, podendo ser o valor da PMT, da operação, do saldo devedor, etc.';


--
-- TOC entry 7833 (class 0 OID 0)
-- Dependencies: 1870
-- Name: COLUMN mtrtb035_garantia_informada.ic_forma_garantia; Type: COMMENT; Schema: mtr; Owner: -
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
-- TOC entry 1871 (class 1259 OID 2025698)
-- Name: mtrsq035_garantia_informada; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq035_garantia_informada
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7834 (class 0 OID 0)
-- Dependencies: 1871
-- Name: mtrsq035_garantia_informada; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq035_garantia_informada OWNED BY mtrtb035_garantia_informada.nu_garantia_informada;


--
-- TOC entry 1872 (class 1259 OID 2025700)
-- Name: mtrtb036_composicao_documental; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7835 (class 0 OID 0)
-- Dependencies: 1872
-- Name: TABLE mtrtb036_composicao_documental; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb036_composicao_documental IS 'Tabela responsável por agrupar tipos de documentos visando criar estruturas que representam conjuntos de tipos de documentos a serem analisados conjuntamente.
Essa conjunção será utilizada na análise do nível documento e por ser formada como os exemplos a seguir:
- RG
- Contracheque
-----------------------------------------------------------------------------------
- CNH
- Conta Concessionária
- DIRPF';


--
-- TOC entry 7836 (class 0 OID 0)
-- Dependencies: 1872
-- Name: COLUMN mtrtb036_composicao_documental.nu_composicao_documental; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb036_composicao_documental.nu_composicao_documental IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7837 (class 0 OID 0)
-- Dependencies: 1872
-- Name: COLUMN mtrtb036_composicao_documental.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb036_composicao_documental.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7838 (class 0 OID 0)
-- Dependencies: 1872
-- Name: COLUMN mtrtb036_composicao_documental.no_composicao_documental; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb036_composicao_documental.no_composicao_documental IS 'Atributo utilizado para armazenar o nome negocial da composição de documentos.';


--
-- TOC entry 7839 (class 0 OID 0)
-- Dependencies: 1872
-- Name: COLUMN mtrtb036_composicao_documental.ts_inclusao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb036_composicao_documental.ts_inclusao IS 'Atributo que armazena a data/hora de cadastro do registro da composição documental.';


--
-- TOC entry 7840 (class 0 OID 0)
-- Dependencies: 1872
-- Name: COLUMN mtrtb036_composicao_documental.ts_revogacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb036_composicao_documental.ts_revogacao IS 'Atributo que armazena a data/hora de revogação do registro da composição documental.';


--
-- TOC entry 7841 (class 0 OID 0)
-- Dependencies: 1872
-- Name: COLUMN mtrtb036_composicao_documental.co_matricula_inclusao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb036_composicao_documental.co_matricula_inclusao IS 'Atributo que armazena a matrícula do usuário/serviço que realizou o cadastro do registro da composição documental.';


--
-- TOC entry 7842 (class 0 OID 0)
-- Dependencies: 1872
-- Name: COLUMN mtrtb036_composicao_documental.co_matricula_revogacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb036_composicao_documental.co_matricula_revogacao IS 'Atributo que armazena a matrícula do usuário/serviço que realizou a revogação do registro da composição documental.';


--
-- TOC entry 7843 (class 0 OID 0)
-- Dependencies: 1872
-- Name: COLUMN mtrtb036_composicao_documental.ic_conclusao_operacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb036_composicao_documental.ic_conclusao_operacao IS 'Atributo utilizado para indicar se a composição deve ser analisada no ato de conclusão da operação indicando quais são os documentos necessarios a serem entregue para permitir a finalização da operação.';


--
-- TOC entry 1873 (class 1259 OID 2025703)
-- Name: mtrsq036_composicao_documental; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq036_composicao_documental
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7844 (class 0 OID 0)
-- Dependencies: 1873
-- Name: mtrsq036_composicao_documental; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq036_composicao_documental OWNED BY mtrtb036_composicao_documental.nu_composicao_documental;


--
-- TOC entry 1874 (class 1259 OID 2025705)
-- Name: mtrtb037_regra_documental; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7845 (class 0 OID 0)
-- Dependencies: 1874
-- Name: TABLE mtrtb037_regra_documental; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb037_regra_documental IS 'Tabela utilizada para armazenar as regras de atendimento da composição. Para que uma composição documental esteja satisfeita, todas as regras a ela associadas devem ser atendidas, ou seja, a regra para cada documento definido deve ser verdadeira. Além da presença do documento vinculado valida no dossiê situações como índice mínimo do antifraude e canal devem ser respeitadas. Caso não seja atendida ao menos uma das regras, a composição não terá suas condições satisfatórias atendidas e consequentemente o nível documental não poderá ser atribuído ao dossiê do cliente.';


--
-- TOC entry 7846 (class 0 OID 0)
-- Dependencies: 1874
-- Name: COLUMN mtrtb037_regra_documental.nu_regra_documental; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb037_regra_documental.nu_regra_documental IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7847 (class 0 OID 0)
-- Dependencies: 1874
-- Name: COLUMN mtrtb037_regra_documental.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb037_regra_documental.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7848 (class 0 OID 0)
-- Dependencies: 1874
-- Name: COLUMN mtrtb037_regra_documental.nu_composicao_documental; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb037_regra_documental.nu_composicao_documental IS 'Atributo que representa composição de tipos de documentos associada aos possíveis tipos de documentos.';


--
-- TOC entry 7849 (class 0 OID 0)
-- Dependencies: 1874
-- Name: COLUMN mtrtb037_regra_documental.nu_tipo_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb037_regra_documental.nu_tipo_documento IS 'Atributo que representa o tipo de documento definido vinculado na relação com a composição de tipos de documentos. Caso este atributo esteja nulo, o atributo que representa a função documental deverá estar prenchido.';


--
-- TOC entry 7850 (class 0 OID 0)
-- Dependencies: 1874
-- Name: COLUMN mtrtb037_regra_documental.nu_funcao_documental; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb037_regra_documental.nu_funcao_documental IS 'Atributo que representa a função documental definida vinculada na relação com a composição de tipos de documentos. Caso este atributo esteja nulo, o atributo que representa o tipo de documento deverá estar prenchido.';


--
-- TOC entry 7851 (class 0 OID 0)
-- Dependencies: 1874
-- Name: COLUMN mtrtb037_regra_documental.nu_canal_captura; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb037_regra_documental.nu_canal_captura IS 'Atributo utilizado para identificar o canal de captura específico para categorizar o conjunto.
Caso este atributo seja nulo, ele permite ao conjunto valer-se de qualquer canal não especificado em outro conjunto para o mesmo documento e composição documental, porém tendo o canal especificado.';


--
-- TOC entry 7852 (class 0 OID 0)
-- Dependencies: 1874
-- Name: COLUMN mtrtb037_regra_documental.ix_antifraude; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb037_regra_documental.ix_antifraude IS 'Atributo utilizado para armazenar o valor mínimo aceitável do índice atribuído ao documento pelo sistema de antifraude para considerar o documento válido na composição documental permitindo atribuir o nível documental ao dossiê do cliente.';


--
-- TOC entry 1875 (class 1259 OID 2025708)
-- Name: mtrsq037_regra_documental; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq037_regra_documental
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7853 (class 0 OID 0)
-- Dependencies: 1875
-- Name: mtrsq037_regra_documental; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq037_regra_documental OWNED BY mtrtb037_regra_documental.nu_regra_documental;


--
-- TOC entry 1876 (class 1259 OID 2025710)
-- Name: mtrsq043_documento_garantia; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq043_documento_garantia
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1877 (class 1259 OID 2025712)
-- Name: mtrsq044_comportamento_pesquisa; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq044_comportamento_pesquisa
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1878 (class 1259 OID 2025714)
-- Name: mtrsq045_atributo_extracao; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq045_atributo_extracao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1879 (class 1259 OID 2025716)
-- Name: mtrsq046_processo_adm; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq046_processo_adm
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1880 (class 1259 OID 2025718)
-- Name: mtrsq047_contrato_adm; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq047_contrato_adm
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1881 (class 1259 OID 2025720)
-- Name: mtrsq048_apenso_adm; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq048_apenso_adm
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1882 (class 1259 OID 2025722)
-- Name: mtrsq049_documento_adm; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq049_documento_adm
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1883 (class 1259 OID 2025724)
-- Name: mtrtb100_autorizacao; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7854 (class 0 OID 0)
-- Dependencies: 1883
-- Name: TABLE mtrtb100_autorizacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb100_autorizacao IS 'Tabela utilizada para armazenar as autorizações relacionadas ao nível documental geradas e entregues para os clientes.';


--
-- TOC entry 7855 (class 0 OID 0)
-- Dependencies: 1883
-- Name: COLUMN mtrtb100_autorizacao.nu_autorizacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.nu_autorizacao IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7856 (class 0 OID 0)
-- Dependencies: 1883
-- Name: COLUMN mtrtb100_autorizacao.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7857 (class 0 OID 0)
-- Dependencies: 1883
-- Name: COLUMN mtrtb100_autorizacao.co_autorizacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.co_autorizacao IS 'Atributo utilizado para armazenar o código de autorização gerado para entrega ao sistema de negócio e armazenamento junto ao dossiê do cliente.';


--
-- TOC entry 7858 (class 0 OID 0)
-- Dependencies: 1883
-- Name: COLUMN mtrtb100_autorizacao.ts_registro; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.ts_registro IS 'Atributo utilizado para armazenar a data e hora de recebimento da solicitação de autorização.';


--
-- TOC entry 7859 (class 0 OID 0)
-- Dependencies: 1883
-- Name: COLUMN mtrtb100_autorizacao.ts_informe_negocio; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.ts_informe_negocio IS 'Atributo utilizado para armazenar a data e hora de entrega do código de autorização para o sistema de negócio solicitante.';


--
-- TOC entry 7860 (class 0 OID 0)
-- Dependencies: 1883
-- Name: COLUMN mtrtb100_autorizacao.co_protocolo_negocio; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.co_protocolo_negocio IS 'Atributo utilizado para armazenar o protocolo de confirmação de recebimento do código de autorização pelo sistema de negócio solicitante.';


--
-- TOC entry 7861 (class 0 OID 0)
-- Dependencies: 1883
-- Name: COLUMN mtrtb100_autorizacao.ts_informe_ecm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.ts_informe_ecm IS 'Atributo utilizado para armazenar a data e hora de entrega do código de autorização para o sistema de ECM para armazenamento junto ao dossiê do produto.';


--
-- TOC entry 7862 (class 0 OID 0)
-- Dependencies: 1883
-- Name: COLUMN mtrtb100_autorizacao.co_protocolo_ecm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.co_protocolo_ecm IS 'Atributo utilizado para armazenar a data e hora de entrega do código de autorização para o sistema de ECM para armazenamento junto ao dossiê do produto.';


--
-- TOC entry 7863 (class 0 OID 0)
-- Dependencies: 1883
-- Name: COLUMN mtrtb100_autorizacao.nu_operacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.nu_operacao IS 'Atributo utilizado para armazenar o código de operação do produto solicitado na autorização.';


--
-- TOC entry 7864 (class 0 OID 0)
-- Dependencies: 1883
-- Name: COLUMN mtrtb100_autorizacao.nu_modalidade; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.nu_modalidade IS 'Atributo utilizado para armazenar o codigo da modalidade do produto solicitado na autorização.';


--
-- TOC entry 7865 (class 0 OID 0)
-- Dependencies: 1883
-- Name: COLUMN mtrtb100_autorizacao.no_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.no_produto IS 'Atributo utilizado para armazenar o nome do produto solicitado na autorização.';


--
-- TOC entry 7866 (class 0 OID 0)
-- Dependencies: 1883
-- Name: COLUMN mtrtb100_autorizacao.nu_cpf_cnpj; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.nu_cpf_cnpj IS 'Atributo utilizado para armazenar o número do CPF ou CNPJ do cliente relacionado com a autorização.';


--
-- TOC entry 7867 (class 0 OID 0)
-- Dependencies: 1883
-- Name: COLUMN mtrtb100_autorizacao.ic_tipo_pessoa; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.ic_tipo_pessoa IS 'Atributo utilizado para indicar o tipo de pessoa, se física ou jurídica podendo assumir os seguintes valores:
F - Física
J - Jurídica';


--
-- TOC entry 7868 (class 0 OID 0)
-- Dependencies: 1883
-- Name: COLUMN mtrtb100_autorizacao.sg_canal_solicitacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.sg_canal_solicitacao IS 'Sigla de identificação do canal/sistema solicitante da autorização.
Informação é obtida pela comparação do codigo de integração enviado com o registro da tabela 006';


--
-- TOC entry 7869 (class 0 OID 0)
-- Dependencies: 1883
-- Name: COLUMN mtrtb100_autorizacao.ts_conclusao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb100_autorizacao.ts_conclusao IS 'Atributo utilizado para armazenar a data e hora da conclusão da operação vinculada a autorização concedida.';


--
-- TOC entry 1884 (class 1259 OID 2025727)
-- Name: mtrsq100_autorizacao; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq100_autorizacao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7870 (class 0 OID 0)
-- Dependencies: 1884
-- Name: mtrsq100_autorizacao; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq100_autorizacao OWNED BY mtrtb100_autorizacao.nu_autorizacao;


--
-- TOC entry 1885 (class 1259 OID 2025729)
-- Name: mtrtb101_documento; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb101_documento (
    nu_documento bigint NOT NULL,
    nu_versao integer NOT NULL,
    nu_autorizacao bigint NOT NULL,
    de_finalidade character varying(100) NOT NULL,
    co_documento_ged character varying(100) NOT NULL
);


--
-- TOC entry 7871 (class 0 OID 0)
-- Dependencies: 1885
-- Name: TABLE mtrtb101_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb101_documento IS 'Tabela utilizada para armazenar a informação dos documentos identificados e utilizados para a emissão da autorização.';


--
-- TOC entry 7872 (class 0 OID 0)
-- Dependencies: 1885
-- Name: COLUMN mtrtb101_documento.nu_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb101_documento.nu_documento IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7873 (class 0 OID 0)
-- Dependencies: 1885
-- Name: COLUMN mtrtb101_documento.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb101_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7874 (class 0 OID 0)
-- Dependencies: 1885
-- Name: COLUMN mtrtb101_documento.nu_autorizacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb101_documento.nu_autorizacao IS 'Atributo utilizado para identificar a autorização que está relacionada ao documento.';


--
-- TOC entry 7875 (class 0 OID 0)
-- Dependencies: 1885
-- Name: COLUMN mtrtb101_documento.de_finalidade; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb101_documento.de_finalidade IS 'Atributo que representa para qual finalidade o documento foi usado';


--
-- TOC entry 7876 (class 0 OID 0)
-- Dependencies: 1885
-- Name: COLUMN mtrtb101_documento.co_documento_ged; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb101_documento.co_documento_ged IS 'Atributo utilizado para armazenar a referencia do registro do documento junto ao GED utilizado na emissão da autorização';


--
-- TOC entry 1886 (class 1259 OID 2025732)
-- Name: mtrsq101_documento; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq101_documento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 7877 (class 0 OID 0)
-- Dependencies: 1886
-- Name: mtrsq101_documento; Type: SEQUENCE OWNED BY; Schema: mtr; Owner: -
--

ALTER SEQUENCE mtrsq101_documento OWNED BY mtrtb101_documento.nu_documento;


--
-- TOC entry 1887 (class 1259 OID 2025734)
-- Name: mtrsq102_autorizacao_negada; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq102_autorizacao_negada
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1888 (class 1259 OID 2025736)
-- Name: mtrsq103_autorizacao_orientacao; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq103_autorizacao_orientacao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1889 (class 1259 OID 2025738)
-- Name: mtrsq200_sicli_erro; Type: SEQUENCE; Schema: mtr; Owner: -
--

CREATE SEQUENCE mtrsq200_sicli_erro
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1890 (class 1259 OID 2025740)
-- Name: mtrtb001_pessoa_fisica; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7878 (class 0 OID 0)
-- Dependencies: 1890
-- Name: TABLE mtrtb001_pessoa_fisica; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb001_pessoa_fisica IS 'Tabela de especialização do dossiê de cliente para armazenar os atributos específicos de uma pessoa física.';


--
-- TOC entry 7879 (class 0 OID 0)
-- Dependencies: 1890
-- Name: COLUMN mtrtb001_pessoa_fisica.nu_dossie_cliente; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_fisica.nu_dossie_cliente IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7880 (class 0 OID 0)
-- Dependencies: 1890
-- Name: COLUMN mtrtb001_pessoa_fisica.dt_nascimento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_fisica.dt_nascimento IS 'Atributo utilizado para armazenar a data de nascimento de pessoas físicas.';


--
-- TOC entry 7881 (class 0 OID 0)
-- Dependencies: 1890
-- Name: COLUMN mtrtb001_pessoa_fisica.ic_estado_civil; Type: COMMENT; Schema: mtr; Owner: -
--

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


--
-- TOC entry 7882 (class 0 OID 0)
-- Dependencies: 1890
-- Name: COLUMN mtrtb001_pessoa_fisica.nu_nis; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_fisica.nu_nis IS 'Atributo utilizado para armazenar o número do NIS de pessoas físicas.';


--
-- TOC entry 7883 (class 0 OID 0)
-- Dependencies: 1890
-- Name: COLUMN mtrtb001_pessoa_fisica.nu_identidade; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_fisica.nu_identidade IS 'Atributo utilizado para armazenar o número de identidade de pessoas físicas.';


--
-- TOC entry 7884 (class 0 OID 0)
-- Dependencies: 1890
-- Name: COLUMN mtrtb001_pessoa_fisica.no_orgao_emissor; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_fisica.no_orgao_emissor IS 'Atributo utilizado para armazenar o órgão emissor da identidade de pessoas físicas.';


--
-- TOC entry 7885 (class 0 OID 0)
-- Dependencies: 1890
-- Name: COLUMN mtrtb001_pessoa_fisica.no_mae; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_fisica.no_mae IS 'Atributo utilizado para armazenar o nome da mãe de pessoas físicas.';


--
-- TOC entry 7886 (class 0 OID 0)
-- Dependencies: 1890
-- Name: COLUMN mtrtb001_pessoa_fisica.no_pai; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_fisica.no_pai IS 'Atributo utilizado para armazenar o nome do pai de pessoas físicas.';


--
-- TOC entry 1891 (class 1259 OID 2025746)
-- Name: mtrtb001_pessoa_juridica; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb001_pessoa_juridica (
    nu_dossie_cliente bigint NOT NULL,
    no_razao_social character varying(255) NOT NULL,
    dt_fundacao date,
    ic_segmento character varying(10),
    vr_faturamento_anual numeric(15,2),
    ic_conglomerado boolean
);


--
-- TOC entry 7887 (class 0 OID 0)
-- Dependencies: 1891
-- Name: TABLE mtrtb001_pessoa_juridica; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb001_pessoa_juridica IS 'Tabela de especialização do dossiê de cliente para armazenar os atributos específicos de uma pessoa jurídica.';


--
-- TOC entry 7888 (class 0 OID 0)
-- Dependencies: 1891
-- Name: COLUMN mtrtb001_pessoa_juridica.nu_dossie_cliente; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_juridica.nu_dossie_cliente IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7889 (class 0 OID 0)
-- Dependencies: 1891
-- Name: COLUMN mtrtb001_pessoa_juridica.no_razao_social; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_juridica.no_razao_social IS 'Atributo utilizado para armazenar a razão social de pessoas jurídicas.';


--
-- TOC entry 7890 (class 0 OID 0)
-- Dependencies: 1891
-- Name: COLUMN mtrtb001_pessoa_juridica.dt_fundacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_juridica.dt_fundacao IS 'Atributo utilizado para armazenar a data de fundação de pessoas jurídicas.';


--
-- TOC entry 7891 (class 0 OID 0)
-- Dependencies: 1891
-- Name: COLUMN mtrtb001_pessoa_juridica.ic_segmento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_juridica.ic_segmento IS 'Atributo para identificar o segmento da empresa, podendo assumir os valores oriundos da view do SIICO:
		- MEI
		- MPE
		- MGE
		- CORP';


--
-- TOC entry 7892 (class 0 OID 0)
-- Dependencies: 1891
-- Name: COLUMN mtrtb001_pessoa_juridica.ic_conglomerado; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb001_pessoa_juridica.ic_conglomerado IS 'Atributo utilizado para indicar se a empresa integra um conglomerado';


--
-- TOC entry 1892 (class 1259 OID 2025749)
-- Name: mtrtb004_dossie_cliente_produto; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7893 (class 0 OID 0)
-- Dependencies: 1892
-- Name: TABLE mtrtb004_dossie_cliente_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb004_dossie_cliente_produto IS 'Tabela de relacionamento para permitir vincular um dossiê de produto a mais de um dossiê de cliente devido a necessidades de produtos com mais de um titular.';


--
-- TOC entry 7894 (class 0 OID 0)
-- Dependencies: 1892
-- Name: COLUMN mtrtb004_dossie_cliente_produto.nu_dossie_cliente_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb004_dossie_cliente_produto.nu_dossie_cliente_produto IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7895 (class 0 OID 0)
-- Dependencies: 1892
-- Name: COLUMN mtrtb004_dossie_cliente_produto.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb004_dossie_cliente_produto.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7896 (class 0 OID 0)
-- Dependencies: 1892
-- Name: COLUMN mtrtb004_dossie_cliente_produto.nu_dossie_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb004_dossie_cliente_produto.nu_dossie_produto IS 'Atributo que armazena a referência para o dossiê do produto vinculado na relação.';


--
-- TOC entry 7897 (class 0 OID 0)
-- Dependencies: 1892
-- Name: COLUMN mtrtb004_dossie_cliente_produto.nu_dossie_cliente; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb004_dossie_cliente_produto.nu_dossie_cliente IS 'Atributo que armazena a referência para o dossiê do cliente vinculado na relação.';


--
-- TOC entry 7898 (class 0 OID 0)
-- Dependencies: 1892
-- Name: COLUMN mtrtb004_dossie_cliente_produto.nu_sequencia_titularidade; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb004_dossie_cliente_produto.nu_sequencia_titularidade IS 'Atributo que indica a sequência de titularidade dos clientes para aquele processo. Ao cadastrar um processo o operador pode incluir titulares conforme a necessidade do produto e este atributo indicara a ordinalidade dos titulares.';


--
-- TOC entry 7899 (class 0 OID 0)
-- Dependencies: 1892
-- Name: COLUMN mtrtb004_dossie_cliente_produto.ic_tipo_relacionamento; Type: COMMENT; Schema: mtr; Owner: -
--

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
-- TOC entry 1893 (class 1259 OID 2025753)
-- Name: mtrtb005_documento_cliente; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb005_documento_cliente (
    nu_documento bigint NOT NULL,
    nu_dossie_cliente bigint NOT NULL
);


--
-- TOC entry 7900 (class 0 OID 0)
-- Dependencies: 1893
-- Name: TABLE mtrtb005_documento_cliente; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb005_documento_cliente IS 'Tabela de relacionamento que vincula um documento ao dossiê de um cliente.';


--
-- TOC entry 7901 (class 0 OID 0)
-- Dependencies: 1893
-- Name: COLUMN mtrtb005_documento_cliente.nu_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb005_documento_cliente.nu_documento IS 'Atributo que representa o documento vinculado ao dossiê do cliente referenciado no registro.';


--
-- TOC entry 7902 (class 0 OID 0)
-- Dependencies: 1893
-- Name: COLUMN mtrtb005_documento_cliente.nu_dossie_cliente; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb005_documento_cliente.nu_dossie_cliente IS 'Atributo que representa a o dossiê do cliente vinculado na relação de documentos.';


--
-- TOC entry 1920 (class 1259 OID 2026477)
-- Name: mtrtb008_conteudo; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb008_conteudo (
    nu_conteudo bigint DEFAULT nextval('mtrsq008_conteudo'::regclass) NOT NULL,
    nu_versao integer NOT NULL,
    nu_documento bigint NOT NULL,
    nu_ordem integer NOT NULL,
    de_conteudo text NOT NULL
);


--
-- TOC entry 7903 (class 0 OID 0)
-- Dependencies: 1920
-- Name: TABLE mtrtb008_conteudo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb008_conteudo IS 'Tabela responsavel pelo armazenamento das referencia de conteudo que compoem o documento.
Nesta tabela serão efetivamente armazenados os dados que caracterizam o conteudo do documento (ou o binario) para manipulação da aplicação até o momento que o documento será efetivamente arquivado no repositorio em carater definitivo.';


--
-- TOC entry 7904 (class 0 OID 0)
-- Dependencies: 1920
-- Name: COLUMN mtrtb008_conteudo.nu_conteudo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb008_conteudo.nu_conteudo IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 7905 (class 0 OID 0)
-- Dependencies: 1920
-- Name: COLUMN mtrtb008_conteudo.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb008_conteudo.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 7906 (class 0 OID 0)
-- Dependencies: 1920
-- Name: COLUMN mtrtb008_conteudo.nu_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb008_conteudo.nu_documento IS 'Atributo utilizado para identificar o documento que o conteudo esta vinculado. Neste formato é possivel associar varios conteudos a um mesmo documento.';


--
-- TOC entry 7907 (class 0 OID 0)
-- Dependencies: 1920
-- Name: COLUMN mtrtb008_conteudo.nu_ordem; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb008_conteudo.nu_ordem IS 'Atributo utilizado para identificar a ordem de exibição na composição do documento. Documentos que possuem apenas um elemento, como um arquivo pdf por exemplo terão apenas um registro de conteudo com o atributo de ordem como 1';


--
-- TOC entry 7908 (class 0 OID 0)
-- Dependencies: 1920
-- Name: COLUMN mtrtb008_conteudo.de_conteudo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb008_conteudo.de_conteudo IS 'Atributo utilizado para armazenar o conteudo efetivo do documento em formato base64. Documentos que possuem apenas um elemento, como um arquivo pdf por exemplo terão apenas um registro de conteudo contendo o documento na integra.';


--
-- TOC entry 1894 (class 1259 OID 2025756)
-- Name: mtrtb011_funcao_documento; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb011_funcao_documento (
    nu_tipo_documento integer NOT NULL,
    nu_funcao_documental integer NOT NULL
);


--
-- TOC entry 7909 (class 0 OID 0)
-- Dependencies: 1894
-- Name: TABLE mtrtb011_funcao_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb011_funcao_documento IS 'Tabela associativa que vincula um tipo de documento a sua função.
Ex: 
- RG x Identificação
- DIRPF x Renda
- DIRPF x Identificação';


--
-- TOC entry 7910 (class 0 OID 0)
-- Dependencies: 1894
-- Name: COLUMN mtrtb011_funcao_documento.nu_tipo_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb011_funcao_documento.nu_tipo_documento IS 'Atributo que representa a o tipo de documento vinculado na relação com a função documental.';


--
-- TOC entry 7911 (class 0 OID 0)
-- Dependencies: 1894
-- Name: COLUMN mtrtb011_funcao_documento.nu_funcao_documental; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb011_funcao_documento.nu_funcao_documental IS 'Atributo que representa a o função documental vinculado na relação com o tipo de documento.';


--
-- TOC entry 1895 (class 1259 OID 2025759)
-- Name: mtrtb017_stco_instnca_documento; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7912 (class 0 OID 0)
-- Dependencies: 1895
-- Name: TABLE mtrtb017_stco_instnca_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb017_stco_instnca_documento IS 'Tabela responsável por armazenar o histórico de situações relativas a instância do documento em avaliação. Cada vez que houver uma mudança na situação apresentada pelo processo, um novo registro deve ser inserido gerando assim um histórico das situações vivenciadas durante o seu ciclo de vida.';


--
-- TOC entry 7913 (class 0 OID 0)
-- Dependencies: 1895
-- Name: COLUMN mtrtb017_stco_instnca_documento.nu_stco_instnca_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb017_stco_instnca_documento.nu_stco_instnca_documento IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7914 (class 0 OID 0)
-- Dependencies: 1895
-- Name: COLUMN mtrtb017_stco_instnca_documento.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb017_stco_instnca_documento.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7915 (class 0 OID 0)
-- Dependencies: 1895
-- Name: COLUMN mtrtb017_stco_instnca_documento.nu_instancia_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb017_stco_instnca_documento.nu_instancia_documento IS 'Atributo utilizado pata armazenar a referência da instancia do documento em avaliação vinculado a situação.';


--
-- TOC entry 7916 (class 0 OID 0)
-- Dependencies: 1895
-- Name: COLUMN mtrtb017_stco_instnca_documento.nu_situacao_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb017_stco_instnca_documento.nu_situacao_documento IS 'Atributo utilizado pata armazenar a referencia a situação do documento escolhida vinculada a instancia do documento em avaliação';


--
-- TOC entry 7917 (class 0 OID 0)
-- Dependencies: 1895
-- Name: COLUMN mtrtb017_stco_instnca_documento.nu_motivo_stco_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb017_stco_instnca_documento.nu_motivo_stco_documento IS 'Atributo utilizado pata armazenar a referencia o motivo especifico para a situação escolhida vinculada a instancia do documento em avaliação';


--
-- TOC entry 7918 (class 0 OID 0)
-- Dependencies: 1895
-- Name: COLUMN mtrtb017_stco_instnca_documento.ts_inclusao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb017_stco_instnca_documento.ts_inclusao IS 'Atributo utilizado para armazenar a data/hora de atribuição da situação ao dossiê.';


--
-- TOC entry 7919 (class 0 OID 0)
-- Dependencies: 1895
-- Name: COLUMN mtrtb017_stco_instnca_documento.co_matricula; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb017_stco_instnca_documento.co_matricula IS 'Atributo utilizado para armazenar a matrícula do empregado ou serviço que atribuiu a situação a instância do documento em avaliação.';


--
-- TOC entry 7920 (class 0 OID 0)
-- Dependencies: 1895
-- Name: COLUMN mtrtb017_stco_instnca_documento.nu_unidade; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb017_stco_instnca_documento.nu_unidade IS 'Atributo que indica o CGC da unidade do empregado que registrou a situação da instancia do documento analisado.';


--
-- TOC entry 1896 (class 1259 OID 2025763)
-- Name: mtrtb018_unidade_tratamento; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb018_unidade_tratamento (
    nu_dossie_produto bigint NOT NULL,
    nu_unidade integer NOT NULL
);


--
-- TOC entry 7921 (class 0 OID 0)
-- Dependencies: 1896
-- Name: TABLE mtrtb018_unidade_tratamento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb018_unidade_tratamento IS 'Tabela utilizada para identificar as unidades de tratamento atribuídas para o dossiê naquele dado momento.
Sempre que a situação do dossiê for modificada, os registros referentes ao dossiê especificamente serão excluídos e reinseridos novos com base na nova situação.';


--
-- TOC entry 7922 (class 0 OID 0)
-- Dependencies: 1896
-- Name: COLUMN mtrtb018_unidade_tratamento.nu_dossie_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb018_unidade_tratamento.nu_dossie_produto IS 'Atributo utilizado para vincular o dossiê do produto com a unidade de tratamento.';


--
-- TOC entry 7923 (class 0 OID 0)
-- Dependencies: 1896
-- Name: COLUMN mtrtb018_unidade_tratamento.nu_unidade; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb018_unidade_tratamento.nu_unidade IS 'Atributo que indica o número do CGC da unidade responsável pelo tratamento do dossiê.';


--
-- TOC entry 1897 (class 1259 OID 2025766)
-- Name: mtrtb019_campo_formulario; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7924 (class 0 OID 0)
-- Dependencies: 1897
-- Name: TABLE mtrtb019_campo_formulario; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb019_campo_formulario IS 'Atributo utilizado para vincular o campo entrrada ao formulário.';


--
-- TOC entry 7925 (class 0 OID 0)
-- Dependencies: 1897
-- Name: COLUMN mtrtb019_campo_formulario.nu_campo_formulario; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb019_campo_formulario.nu_campo_formulario IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7926 (class 0 OID 0)
-- Dependencies: 1897
-- Name: COLUMN mtrtb019_campo_formulario.nu_campo_entrada; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb019_campo_formulario.nu_campo_entrada IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7927 (class 0 OID 0)
-- Dependencies: 1897
-- Name: COLUMN mtrtb019_campo_formulario.nu_ordem; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb019_campo_formulario.nu_ordem IS 'Atributo utilizado para definir a ordem de exibição dos campos do formulário.';


--
-- TOC entry 7928 (class 0 OID 0)
-- Dependencies: 1897
-- Name: COLUMN mtrtb019_campo_formulario.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb019_campo_formulario.nu_versao IS 'Atributo que armazena a versão';


--
-- TOC entry 7929 (class 0 OID 0)
-- Dependencies: 1897
-- Name: COLUMN mtrtb019_campo_formulario.ic_obrigatorio; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb019_campo_formulario.ic_obrigatorio IS 'Atributo que armazena o indicativo do obrigatoriedade do campo no formulário.';


--
-- TOC entry 7930 (class 0 OID 0)
-- Dependencies: 1897
-- Name: COLUMN mtrtb019_campo_formulario.de_expressao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb019_campo_formulario.de_expressao IS 'Atributo que armazena a expressão a ser aplicada pelo Java script para determinar a exposição ou não do campo no formulário.';


--
-- TOC entry 7931 (class 0 OID 0)
-- Dependencies: 1897
-- Name: COLUMN mtrtb019_campo_formulario.ic_ativo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb019_campo_formulario.ic_ativo IS 'Atributo que indica se o campo de entrada está apto ou não para ser inserido no formulário.';


--
-- TOC entry 7932 (class 0 OID 0)
-- Dependencies: 1897
-- Name: COLUMN mtrtb019_campo_formulario.no_campo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb019_campo_formulario.no_campo IS 'Atributo que indica o nome do campo. Este nome pode ser utilizado pela estrutura de programação para referenciar a campo no formulario independente do label exposto para o usuário.';


--
-- TOC entry 1898 (class 1259 OID 2025773)
-- Name: mtrtb023_produto_processo; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb023_produto_processo (
    nu_processo integer NOT NULL,
    nu_produto integer NOT NULL
);


--
-- TOC entry 7933 (class 0 OID 0)
-- Dependencies: 1898
-- Name: TABLE mtrtb023_produto_processo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb023_produto_processo IS 'Tabela de relacionamento para vinculação do produto com o processo. 
Existe a possibilidade que um produto seja vinculado a diversos processos pois pode diferenciar a forma de realizar as ações conforme o canal de contratação, campanha, ou outro fator, como por exemplo uma conta que seja contratada pela agência física, agência virtual, CCA ou Aplicativo de abertura de contas.';


--
-- TOC entry 7934 (class 0 OID 0)
-- Dependencies: 1898
-- Name: COLUMN mtrtb023_produto_processo.nu_processo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb023_produto_processo.nu_processo IS 'Atributo que representa o processo vinculado na relação com o produto.';


--
-- TOC entry 7935 (class 0 OID 0)
-- Dependencies: 1898
-- Name: COLUMN mtrtb023_produto_processo.nu_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb023_produto_processo.nu_produto IS 'Atributo que representa o produto vinculado na relação com o processo.';


--
-- TOC entry 1899 (class 1259 OID 2025776)
-- Name: mtrtb026_relacao_processo; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb026_relacao_processo (
    nu_processo_pai integer NOT NULL,
    nu_processo_filho integer NOT NULL,
    nu_versao integer NOT NULL,
    nu_prioridade integer,
    nu_ordem integer
);


--
-- TOC entry 7936 (class 0 OID 0)
-- Dependencies: 1899
-- Name: TABLE mtrtb026_relacao_processo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb026_relacao_processo IS 'Tabela de auto relacionamento da tabela de processos utilizada para identificar a relação entre os mesmos.';


--
-- TOC entry 7937 (class 0 OID 0)
-- Dependencies: 1899
-- Name: COLUMN mtrtb026_relacao_processo.nu_processo_pai; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb026_relacao_processo.nu_processo_pai IS 'Atributo que representa o processo pai da relação entre os processos. Os processos "pai" são os processos que estão em nivel superior em uma visão de arvore de processos.
Os processos que não possuem registro com outro processo pai são conhecidos como processos patriarcas e estes são os processos inicialmente exibidos nas telas de tratamento e/ou visão de arvores.';


--
-- TOC entry 7938 (class 0 OID 0)
-- Dependencies: 1899
-- Name: COLUMN mtrtb026_relacao_processo.nu_processo_filho; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb026_relacao_processo.nu_processo_filho IS 'Atributo que representa o processo filho da relação entre os processos. Os processos "filho" são os processos que estão em nivel inferior em uma visão de organograma dos processos.';


--
-- TOC entry 7939 (class 0 OID 0)
-- Dependencies: 1899
-- Name: COLUMN mtrtb026_relacao_processo.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb026_relacao_processo.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 7940 (class 0 OID 0)
-- Dependencies: 1899
-- Name: COLUMN mtrtb026_relacao_processo.nu_prioridade; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb026_relacao_processo.nu_prioridade IS 'Atributo que determina a ordem de prioridade de atendimento do processo na fila de tratamento sob a otica do processo pai. 

Apenas processos do tipo "dossiê" deverão ter a possibilidade de ser priorizados.

Uma vez definida a prioridade para um processo, todos os registros que possuem o mesmo processo pai necessitarão ser definidos.

Quando processos são priorizados, a visão para tratamento deve ser restrita e apresentar apenas a visão do processo patriarca com o botão de captura para tratamento fazendo com que o operador não saiba o dossiê de qual processo irá tratar.';


--
-- TOC entry 7941 (class 0 OID 0)
-- Dependencies: 1899
-- Name: COLUMN mtrtb026_relacao_processo.nu_ordem; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb026_relacao_processo.nu_ordem IS 'Atributo utilizado para definir a ordem de execução dos processos filho sob a otica do processo pai determinando a sequencia de execução das etapas do processo.

Este atributo não deverá ser preenchido para os processos patriarcas e/ou processos de definição de dossiê de produto.';


--
-- TOC entry 1900 (class 1259 OID 2025779)
-- Name: mtrtb031_resposta_opcao; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb031_resposta_opcao (
    nu_opcao_campo bigint NOT NULL,
    nu_resposta_dossie bigint NOT NULL
);


--
-- TOC entry 7942 (class 0 OID 0)
-- Dependencies: 1900
-- Name: TABLE mtrtb031_resposta_opcao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb031_resposta_opcao IS 'Tabela de relacionamento com finalidade de armazenar todas as respostas objetivas informadas pelo cliente a mesma pergunta no formulário de identificação do dossiê.';


--
-- TOC entry 7943 (class 0 OID 0)
-- Dependencies: 1900
-- Name: COLUMN mtrtb031_resposta_opcao.nu_opcao_campo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb031_resposta_opcao.nu_opcao_campo IS 'Atributo que representa a opção selecionada vinculado na relação com a resposta do formulário.';


--
-- TOC entry 7944 (class 0 OID 0)
-- Dependencies: 1900
-- Name: COLUMN mtrtb031_resposta_opcao.nu_resposta_dossie; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb031_resposta_opcao.nu_resposta_dossie IS 'Atributo que representa a resposta vinculada na relação com a opção selecionada do campo.';


--
-- TOC entry 1901 (class 1259 OID 2025782)
-- Name: mtrtb032_elemento_conteudo; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7945 (class 0 OID 0)
-- Dependencies: 1901
-- Name: TABLE mtrtb032_elemento_conteudo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb032_elemento_conteudo IS 'Tabela responsável pelo armazenamento dos elementos que compõem o mapa de documentos para vinculação ao processo.
Esses elementos estão associados aos tipos de documentos para identificação dos mesmo no ato da captura.';


--
-- TOC entry 7946 (class 0 OID 0)
-- Dependencies: 1901
-- Name: COLUMN mtrtb032_elemento_conteudo.nu_elemento_conteudo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_elemento_conteudo IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 7947 (class 0 OID 0)
-- Dependencies: 1901
-- Name: COLUMN mtrtb032_elemento_conteudo.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrência otimista.';


--
-- TOC entry 7948 (class 0 OID 0)
-- Dependencies: 1901
-- Name: COLUMN mtrtb032_elemento_conteudo.nu_elemento_vinculador; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_elemento_vinculador IS 'Atributo utilizado para armazenar uma outra instancia de elemento ao qual o elemento se vincula.
Esta estrutura permite criar uma estrutura hierarquizada de elementos, porem elementos só devem ser vinculados a outros elementos que não sejam finais, ou seja, não sejam associados a nenhum tipo de elemento que seja associado a um tipo de documento.';


--
-- TOC entry 7949 (class 0 OID 0)
-- Dependencies: 1901
-- Name: COLUMN mtrtb032_elemento_conteudo.nu_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_produto IS 'Atributo utilizado para vincular o produto ao elemento conteúdo.';


--
-- TOC entry 7950 (class 0 OID 0)
-- Dependencies: 1901
-- Name: COLUMN mtrtb032_elemento_conteudo.nu_processo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_processo IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 7951 (class 0 OID 0)
-- Dependencies: 1901
-- Name: COLUMN mtrtb032_elemento_conteudo.nu_tipo_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.nu_tipo_documento IS 'Atributo utilizado para identificar o elemento de ponta que possui vinculo com algum tipo de documento.';


--
-- TOC entry 7952 (class 0 OID 0)
-- Dependencies: 1901
-- Name: COLUMN mtrtb032_elemento_conteudo.ic_obrigatorio; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.ic_obrigatorio IS 'Atributo para indicar se o elemento é de submissão obrigatoria ou não de forma individual.';


--
-- TOC entry 7953 (class 0 OID 0)
-- Dependencies: 1901
-- Name: COLUMN mtrtb032_elemento_conteudo.qt_elemento_obrigatorio; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.qt_elemento_obrigatorio IS 'Este atributo indica a quantidade de elementos que são de tipo de elemento final obrigatórios dentro da sua árvore e só deve ser preenchido se o tipo do elemento permitir agrupamento: Exemplo:
- Identificação (Este elemento deve ter 2 filhos obrigatórios)
   |-- RG
   |-- CNH
   |-- Passaporte
   |-- CTPS';


--
-- TOC entry 7954 (class 0 OID 0)
-- Dependencies: 1901
-- Name: COLUMN mtrtb032_elemento_conteudo.ic_validar; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.ic_validar IS 'Atributo que indica se o documento deve ser validado quando apresentado no processo.
Caso verdadeiro, a instância do documento deve ser criada com a situação vazia
Caso false, a instância do documento deve ser criada com a situação de aprovada conforme regra de negócio realizada pelo sistema, desde que já exista outra instância do mesmo documento com situação aprovada previamente.';


--
-- TOC entry 7955 (class 0 OID 0)
-- Dependencies: 1901
-- Name: COLUMN mtrtb032_elemento_conteudo.no_campo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.no_campo IS 'Atributo que indica o nome do campo. Este nome pode ser utilizado pela estrutura de programação para referenciar a elemento do documento na interface independente do label exposto para o usuário.';


--
-- TOC entry 7956 (class 0 OID 0)
-- Dependencies: 1901
-- Name: COLUMN mtrtb032_elemento_conteudo.de_expressao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.de_expressao IS 'Atributo que armazena a expressão a ser aplicada pelo javascript para determinar a exposição ou não do elemento para submissão.';


--
-- TOC entry 7957 (class 0 OID 0)
-- Dependencies: 1901
-- Name: COLUMN mtrtb032_elemento_conteudo.no_elemento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb032_elemento_conteudo.no_elemento IS 'Atributo utilizado para armazenar o nome de apresentação do tipo de elemento de conteúdo.
Este atributo deve estar preenchido quando a vinculação com o tipo de documento for nula, pois nesta situação o valor desta tabela será apresentado na interface gráfica. O registro que possuir vinculação com o tipo de documento, será o nome deste que deverá ser exposto.';


--
-- TOC entry 1902 (class 1259 OID 2025789)
-- Name: mtrtb034_garantia_produto; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb034_garantia_produto (
    nu_produto integer NOT NULL,
    nu_garantia integer NOT NULL
);


--
-- TOC entry 7958 (class 0 OID 0)
-- Dependencies: 1902
-- Name: TABLE mtrtb034_garantia_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb034_garantia_produto IS 'Tabela de relacionamento responsável por vincular as garantias possíveis de exibição quando selecionado um dado produto.';


--
-- TOC entry 7959 (class 0 OID 0)
-- Dependencies: 1902
-- Name: COLUMN mtrtb034_garantia_produto.nu_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb034_garantia_produto.nu_produto IS 'Atributo utilizado para armazenar a referência do produto da relação com a garantia';


--
-- TOC entry 7960 (class 0 OID 0)
-- Dependencies: 1902
-- Name: COLUMN mtrtb034_garantia_produto.nu_garantia; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb034_garantia_produto.nu_garantia IS 'Atributo utilizado para armazenar a referência da garantia da relação com o produto';


--
-- TOC entry 1903 (class 1259 OID 2025792)
-- Name: mtrtb038_nivel_documental; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb038_nivel_documental (
    nu_composicao_documental bigint NOT NULL,
    nu_dossie_cliente bigint NOT NULL
);


--
-- TOC entry 7961 (class 0 OID 0)
-- Dependencies: 1903
-- Name: TABLE mtrtb038_nivel_documental; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb038_nivel_documental IS 'Tabela responsável por armazenar as referências de níveis documentais possíveis para associação a clientes e produtos.
O nível documental é uma informação pertencente ao cliente, porém o mesmo deve estar associado a um conjunto de tipos de documentos e informações que torna a informação dinâmica para o cliente, ou seja, se um cliente submete um determinado documento que aumenta sua gama de informações válidas, ele pode ganhar um determinado nível documental, porém se um documento passa a ter sua validade ultrapassada o cliente perde aquele determinado nível.';


--
-- TOC entry 7962 (class 0 OID 0)
-- Dependencies: 1903
-- Name: COLUMN mtrtb038_nivel_documental.nu_composicao_documental; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb038_nivel_documental.nu_composicao_documental IS 'Atributo utilizado para identificar a composição documental que foi atingida ao atribuir o nível documental para o cliente.';


--
-- TOC entry 7963 (class 0 OID 0)
-- Dependencies: 1903
-- Name: COLUMN mtrtb038_nivel_documental.nu_dossie_cliente; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb038_nivel_documental.nu_dossie_cliente IS 'Atributo que representa o dossiê do cliente vinculado na atribuição do nível documental.';


--
-- TOC entry 1904 (class 1259 OID 2025795)
-- Name: mtrtb039_produto_composicao; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb039_produto_composicao (
    nu_composicao_documental bigint NOT NULL,
    nu_produto integer NOT NULL
);


--
-- TOC entry 7964 (class 0 OID 0)
-- Dependencies: 1904
-- Name: TABLE mtrtb039_produto_composicao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb039_produto_composicao IS 'Tabela de relacionamento que vincula uma composição de documentos a um ou mais produtos.
Essa associação visa identificar as necessidade documentais para um determinado produto no ato de sua contratação, permitindo ao sistema autorizar ou não a operação do ponto de vista documental. ';


--
-- TOC entry 7965 (class 0 OID 0)
-- Dependencies: 1904
-- Name: COLUMN mtrtb039_produto_composicao.nu_composicao_documental; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb039_produto_composicao.nu_composicao_documental IS 'Atributo que representa o composição envolvida na relação com o produto';


--
-- TOC entry 7966 (class 0 OID 0)
-- Dependencies: 1904
-- Name: COLUMN mtrtb039_produto_composicao.nu_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb039_produto_composicao.nu_produto IS 'Atributo que representa o produto envolvido na relação com a composição';


--
-- TOC entry 1905 (class 1259 OID 2025798)
-- Name: mtrtb040_cadeia_tipo_sto_dossie; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb040_cadeia_tipo_sto_dossie (
    nu_tipo_situacao_atual integer NOT NULL,
    nu_tipo_situacao_seguinte integer NOT NULL
);


--
-- TOC entry 7967 (class 0 OID 0)
-- Dependencies: 1905
-- Name: TABLE mtrtb040_cadeia_tipo_sto_dossie; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb040_cadeia_tipo_sto_dossie IS 'Tabela utilizada para armazenar as possibilidades de tipos de situações possíveis de aplicação em um dossiê de produto a partir um determinado tipo de situação.';


--
-- TOC entry 7968 (class 0 OID 0)
-- Dependencies: 1905
-- Name: COLUMN mtrtb040_cadeia_tipo_sto_dossie.nu_tipo_situacao_atual; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb040_cadeia_tipo_sto_dossie.nu_tipo_situacao_atual IS 'Atributo que representa o tipo de situação atual na relação';


--
-- TOC entry 7969 (class 0 OID 0)
-- Dependencies: 1905
-- Name: COLUMN mtrtb040_cadeia_tipo_sto_dossie.nu_tipo_situacao_seguinte; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb040_cadeia_tipo_sto_dossie.nu_tipo_situacao_seguinte IS 'Atributo que representa o tipo de situação que pode ser aplicado como proximo tipo de situação de um dossiê de produto';


--
-- TOC entry 1906 (class 1259 OID 2025801)
-- Name: mtrtb041_cadeia_stco_documento; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb041_cadeia_stco_documento (
    nu_situacao_documento_atual integer NOT NULL,
    nu_situacao_documento_seguinte integer NOT NULL
);


--
-- TOC entry 7970 (class 0 OID 0)
-- Dependencies: 1906
-- Name: TABLE mtrtb041_cadeia_stco_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb041_cadeia_stco_documento IS 'Tabela utilizada para armazenar as possibilidades de tipos de situações possíveis de aplicação em uma instância de documento a partir um determinado tipo de situação.';


--
-- TOC entry 7971 (class 0 OID 0)
-- Dependencies: 1906
-- Name: COLUMN mtrtb041_cadeia_stco_documento.nu_situacao_documento_atual; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb041_cadeia_stco_documento.nu_situacao_documento_atual IS 'Atributo que representa o tipo de situação atual na relação';


--
-- TOC entry 7972 (class 0 OID 0)
-- Dependencies: 1906
-- Name: COLUMN mtrtb041_cadeia_stco_documento.nu_situacao_documento_seguinte; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb041_cadeia_stco_documento.nu_situacao_documento_seguinte IS 'Atributo que representa o tipo de situação que pode ser aplicado como proximo tipo de situação de uma instancia de documento.';


--
-- TOC entry 1907 (class 1259 OID 2025804)
-- Name: mtrtb042_cliente_garantia; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb042_cliente_garantia (
    nu_garantia_informada bigint NOT NULL,
    nu_dossie_cliente bigint NOT NULL
);


--
-- TOC entry 7973 (class 0 OID 0)
-- Dependencies: 1907
-- Name: TABLE mtrtb042_cliente_garantia; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb042_cliente_garantia IS 'Tabela de relacionamento entre o dossiê cliente (tb001) e a garantia informada (tb035). Representa as garantias que têm pessoas relacionadas, garantias do tipo fidejussórias (aval, fiador, etc).';


--
-- TOC entry 7974 (class 0 OID 0)
-- Dependencies: 1907
-- Name: COLUMN mtrtb042_cliente_garantia.nu_garantia_informada; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb042_cliente_garantia.nu_garantia_informada IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 7975 (class 0 OID 0)
-- Dependencies: 1907
-- Name: COLUMN mtrtb042_cliente_garantia.nu_dossie_cliente; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb042_cliente_garantia.nu_dossie_cliente IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 1908 (class 1259 OID 2025807)
-- Name: mtrtb043_documento_garantia; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb043_documento_garantia (
    nu_garantia integer NOT NULL,
    nu_tipo_documento integer,
    nu_processo integer NOT NULL,
    nu_documento_garantia bigint DEFAULT nextval('mtrsq043_documento_garantia'::regclass) NOT NULL,
    nu_funcao_documental integer,
    nu_versao integer NOT NULL
);


--
-- TOC entry 7976 (class 0 OID 0)
-- Dependencies: 1908
-- Name: TABLE mtrtb043_documento_garantia; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb043_documento_garantia IS 'Tabela utilizada para identificar os documentos (por tipo ou função) que são necessários para o tipo de garantia';


--
-- TOC entry 7977 (class 0 OID 0)
-- Dependencies: 1908
-- Name: COLUMN mtrtb043_documento_garantia.nu_garantia; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb043_documento_garantia.nu_garantia IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 7978 (class 0 OID 0)
-- Dependencies: 1908
-- Name: COLUMN mtrtb043_documento_garantia.nu_tipo_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb043_documento_garantia.nu_tipo_documento IS 'Atributo que representa o tipo de documento especifico definido na relação com a a garantia de acordo com o processo composição de tipos de documentos. Caso este atributo esteja nulo, o atributo que representa a função documental deverá estar prenchido.';


--
-- TOC entry 7979 (class 0 OID 0)
-- Dependencies: 1908
-- Name: COLUMN mtrtb043_documento_garantia.nu_processo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb043_documento_garantia.nu_processo IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 1909 (class 1259 OID 2025811)
-- Name: mtrtb044_comportamento_pesquisa; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7980 (class 0 OID 0)
-- Dependencies: 1909
-- Name: TABLE mtrtb044_comportamento_pesquisa; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb044_comportamento_pesquisa IS 'Tabela utilizada para armazenar o comportamento esperado conforme os codigos de retorno da pesquisa do SIPES com relação ao impedimento ou não de gerar uma autorização para o cliente solicitado e definição das mensagens de orientação que deverão ser encaminhadas para os usuarios.';


--
-- TOC entry 7981 (class 0 OID 0)
-- Dependencies: 1909
-- Name: COLUMN mtrtb044_comportamento_pesquisa.nu_comportamento_pesquisa; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb044_comportamento_pesquisa.nu_comportamento_pesquisa IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 7982 (class 0 OID 0)
-- Dependencies: 1909
-- Name: COLUMN mtrtb044_comportamento_pesquisa.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb044_comportamento_pesquisa.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 7983 (class 0 OID 0)
-- Dependencies: 1909
-- Name: COLUMN mtrtb044_comportamento_pesquisa.nu_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb044_comportamento_pesquisa.nu_produto IS 'Atributo utilizado para identificar o produto relacionado ao comportamento do retorno da pesquisa';


--
-- TOC entry 7984 (class 0 OID 0)
-- Dependencies: 1909
-- Name: COLUMN mtrtb044_comportamento_pesquisa.ic_sistema_retorno; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb044_comportamento_pesquisa.ic_sistema_retorno IS 'Atributo utilizado para indicar o sistema relacionado com o retorno da pesquisa cadastral baseado no dominio abaixo:

SICPF
SERASA
CADIN
SINAD
CCF
SPC
SICOW';


--
-- TOC entry 7985 (class 0 OID 0)
-- Dependencies: 1909
-- Name: COLUMN mtrtb044_comportamento_pesquisa.ic_codigo_retorno; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb044_comportamento_pesquisa.ic_codigo_retorno IS 'Atributo utilizado para definir o valor do codigo de retorno que deve ser analisado para envio da mensagem e definição do comportamento de emissão ou bloqueio de autorização documental.';


--
-- TOC entry 7986 (class 0 OID 0)
-- Dependencies: 1909
-- Name: COLUMN mtrtb044_comportamento_pesquisa.ic_bloqueio; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb044_comportamento_pesquisa.ic_bloqueio IS 'Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao sistema especificado no atributo "ic_sistema" retorne um resultado com o codigo definido no atributo "vr_codigo_retorno".';


--
-- TOC entry 7987 (class 0 OID 0)
-- Dependencies: 1909
-- Name: COLUMN mtrtb044_comportamento_pesquisa.de_orientacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb044_comportamento_pesquisa.de_orientacao IS 'Atributo utilizado para armazenar a mensagem de orientação que deve ser encaminhada para o usuario do sistema. ';


--
-- TOC entry 1910 (class 1259 OID 2025818)
-- Name: mtrtb045_atributo_extracao; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 7988 (class 0 OID 0)
-- Dependencies: 1910
-- Name: TABLE mtrtb045_atributo_extracao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb045_atributo_extracao IS 'Tabela utilizada para identificar os atributos que devem ser extraidos pelas rotinas automaticas de OCR/ICR ou de complementação utilizadas para alimentar a tabela 007 de atributos do documento';


--
-- TOC entry 7989 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.nu_atributo_extracao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb045_atributo_extracao.nu_atributo_extracao IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 7990 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb045_atributo_extracao.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 7991 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.nu_tipo_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb045_atributo_extracao.nu_tipo_documento IS 'Atributo que representa o tipo de documento vinculado cujo atributo deve ser extraido.';


--
-- TOC entry 7992 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.no_atributo_negocial; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb045_atributo_extracao.no_atributo_negocial IS 'Atributo utilizado para armazenar o nome do atributo identificado pelo negocio. Esse valor armazena o nome utilizado no dia a dia e que pode ser apresentado como algum label no sistema quando necessario.';


--
-- TOC entry 7993 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.no_atributo_retorno; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb045_atributo_extracao.no_atributo_retorno IS 'Atributo utilizado para armazenar o nome do atributo retornado pela rotina/serviço de extração de dados do documento. Esse valor armazena o nome utilizado no campo de retorno da informação.';


--
-- TOC entry 7994 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.ic_ativo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_ativo IS 'Atributo utilizado para definir se o atributo deve ser procurado para captura no objeto de retorno dos dados extaidos do documento.';


--
-- TOC entry 7995 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.ic_ged; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_ged IS 'Atributo utilizado para indicar que o atributo deve ser utilizado para alimentar a informação do GED';


--
-- TOC entry 7996 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.no_atributo_ged; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb045_atributo_extracao.no_atributo_ged IS 'Atributo utilizado para definir o nome do atributo do GED que tem relação com o atributo do documento.';


--
-- TOC entry 7997 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.ic_calculo_data; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_calculo_data IS 'Atrinuto utilizado para indicar se o atributo extraido do documento deve ser utilizado no calculo da data de validade baseado nas regras do "ic_validade_auto_contida" e "pz_validade_dias". Para este atributo deverá ter apenas um regsitro marcado como true para o mesmo tipo de documento.';


--
-- TOC entry 7998 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.ic_campo_sicpf; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_campo_sicpf IS 'Atributo utilizado para indicar qual campo de retorno o SICPF deve ser utilizado para uma possivel comparação podendo assumi o seguinte dominio:

- NASCIMENTO
- NOME
- MAE
- ELEITOR';


--
-- TOC entry 7999 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.ic_modo_sicpf; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_modo_sicpf IS 'Atributo utilizado para indicar a forma de comparação com o campo de retorno o SICPF deve ser utilizado para uma possivel comparação podendo assumi o seguinte dominio:

- E - Exato
- P - Parcial';


--
-- TOC entry 8000 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.no_atributo_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb045_atributo_extracao.no_atributo_documento IS 'Atributo utilizado para armazenar o nome do atributo identificado pela integração com outros sistemas. Esse valor armazena o nome utilizado nas representações JSON/XML encaminhadas nos serviços de integração e na identificação do atributo utilizado na tb007.';


--
-- TOC entry 8001 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.ic_tipo_ged; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_tipo_ged IS 'Atributo utilizado para indicar qual o tipo de atributo definido junto a classe GED. Pode assumir os seguintes valores:
- BOOLEAN
- STRING
- LONG
- DATE
';


--
-- TOC entry 8002 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.ic_obrigatorio_ged; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_obrigatorio_ged IS 'Atributo utilizado para indicar se esta informação junto ao GED tem cunho obrigatorio.';


--
-- TOC entry 8003 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.ic_tipo_campo; Type: COMMENT; Schema: mtr; Owner: -
--

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


--
-- TOC entry 8004 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.ic_obrigatorio; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_obrigatorio IS 'Atributo utilizado para indicar se esta informação é de captura obrigatorio para o tipo de documento associado.';


--
-- TOC entry 8005 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.no_atributo_sicli; Type: COMMENT; Schema: mtr; Owner: -
--

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


--
-- TOC entry 8006 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.no_objeto_sicli; Type: COMMENT; Schema: mtr; Owner: -
--

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


--
-- TOC entry 8007 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.ic_tipo_sicli; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_tipo_sicli IS 'Atributo utilizado para indicar qual o tipo de atributo definido junto ao objeto a ser enviado na atualização de dados do SICLI. Pode assumir os seguintes valores:
- BOOLEAN
- STRING
- LONG
- DATE
- DECIMAL';


--
-- TOC entry 8008 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.ic_tipo_geral; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb045_atributo_extracao.ic_tipo_geral IS 'Atributo utilizado para indicar qual o tipo de atributo definido para ações internas do SIMTR como geração de minutas, validação da informação, etc. Pode assumir os seguintes valores:
- BOOLEAN
- STRING
- LONG
- DATE
- DECIMAL';


--
-- TOC entry 8009 (class 0 OID 0)
-- Dependencies: 1910
-- Name: COLUMN mtrtb045_atributo_extracao.pc_alteracao_permitido; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb045_atributo_extracao.pc_alteracao_permitido IS 'Atributo que representa o percentual adicional de alteração permitido sobre o valor recebido do OCR, no formato inteiro.
Exemplo:
Considere que foi recebido do OCR um valor com 90% de assertividade.
1) Se o atributo permite alteração de até 6% do conteúdo recebido do OCR, o valor armazenado será 6, mas a aplicação deve permitir 16% de alteração (10% da margem de assertividade do OCR, mais 6% permitidos por este atributo);

2) Se o atributo permite alteração de até 10% do conteúdo recebido do OCR, o valor armazenado será 10, mas a aplicação deve permitir 20% de alteração (10% da margem de assertividade do OCR, mais 10% permitidos por este atributo);

O valor máximo para o atributo é 100. Caso a soma da margem de assertividade do OCR com o valor do atributo resulte num valor superior a 100, a aplicação deverá limitar, considerando que está permitido 100% de alteração do valor.';


--
-- TOC entry 1911 (class 1259 OID 2025825)
-- Name: mtrtb046_processo_adm; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 8010 (class 0 OID 0)
-- Dependencies: 1911
-- Name: TABLE mtrtb046_processo_adm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb046_processo_adm IS 'Tabela responsavel pelo armazenamento dos processos administrativo eletronicos formados quando a inicio de um fluxo de contratação.

Com base nesses processos podem ser criados registros de contratos ou apensos de penalidade baseados no andamento do processo.

O registro do processo é utilizado para realizar a vinculação de documentos eletronicos de forma a gerar um dossiê proprio para este fim.';


--
-- TOC entry 8011 (class 0 OID 0)
-- Dependencies: 1911
-- Name: COLUMN mtrtb046_processo_adm.nu_processo_adm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb046_processo_adm.nu_processo_adm IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8012 (class 0 OID 0)
-- Dependencies: 1911
-- Name: COLUMN mtrtb046_processo_adm.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb046_processo_adm.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8013 (class 0 OID 0)
-- Dependencies: 1911
-- Name: COLUMN mtrtb046_processo_adm.nu_processo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb046_processo_adm.nu_processo IS 'Atributo utilizado para armazenar o numero de identificação negocial do processo

Juntamente com o "nu_ano_processo", forma-se o numero de identificação negocial do processo.

Ex: 894-2017';


--
-- TOC entry 8014 (class 0 OID 0)
-- Dependencies: 1911
-- Name: COLUMN mtrtb046_processo_adm.nu_ano_processo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb046_processo_adm.nu_ano_processo IS 'Atributo utilizado para armazenar o ano de identificação negocial do processo.

Juntamente com o "nu_processo", forma-se o numero de identificação negocial do processo.

Ex: 894-2017';


--
-- TOC entry 8015 (class 0 OID 0)
-- Dependencies: 1911
-- Name: COLUMN mtrtb046_processo_adm.nu_pregao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb046_processo_adm.nu_pregao IS 'Atributo utilizado para armazenar o numero de identificação negocial do pregão originado pelo processo relativo ao registro.

Juntamente com o "nu_ano_pregao" e "nu_unidade_pregao", forma-se o numero de identificação negocial do pregão.

Ex: 912/7775-2017';


--
-- TOC entry 8016 (class 0 OID 0)
-- Dependencies: 1911
-- Name: COLUMN mtrtb046_processo_adm.nu_unidade_contratacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb046_processo_adm.nu_unidade_contratacao IS 'Atributo utilizado para armazenar a unidade CAIXA responsável pela execução do pregão originado pelo processo relativo ao registro.

Juntamente com o "nu_pregao" e "nu_ano_pregao, forma-se o numero de identificação negocial do pregão.

Ex: 912/7775-2017';


--
-- TOC entry 8017 (class 0 OID 0)
-- Dependencies: 1911
-- Name: COLUMN mtrtb046_processo_adm.nu_ano_pregao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb046_processo_adm.nu_ano_pregao IS 'Atributo utilizado para armazenar o ano de identificação negocial do pregão originado pelo processo relativo ao registro.

Juntamente com o "nu_pregao" e "nu_unidade_pregao", forma-se o numero de identificação negocial do pregão.

Ex: 912/7775-2017';


--
-- TOC entry 8018 (class 0 OID 0)
-- Dependencies: 1911
-- Name: COLUMN mtrtb046_processo_adm.de_objeto_contratacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb046_processo_adm.de_objeto_contratacao IS 'Atributo utilizado para armazenar uma descrição livre sobre o objeto de contratação relativo ao processo administrativo.

Em caso de integração com o SICLG, essa informação deve ser carregada deste sistema que armazena a identificação deste objeto.';


--
-- TOC entry 8019 (class 0 OID 0)
-- Dependencies: 1911
-- Name: COLUMN mtrtb046_processo_adm.ts_inclusao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb046_processo_adm.ts_inclusao IS 'Atributo utilizado para armazenar a data/hora de inclusão do registro do processo administrativo.';


--
-- TOC entry 8020 (class 0 OID 0)
-- Dependencies: 1911
-- Name: COLUMN mtrtb046_processo_adm.co_matricula; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb046_processo_adm.co_matricula IS 'Atributo utilizado para armazenar a matricula do usuário do sistema responsável pela inclusão do registro do processo administrativo.';


--
-- TOC entry 8021 (class 0 OID 0)
-- Dependencies: 1911
-- Name: COLUMN mtrtb046_processo_adm.ts_finalizacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb046_processo_adm.ts_finalizacao IS 'Atributo utilizado para armazenar a data/hora de finalização do registro do processo administrativo.

Após o registro ser finalizado, não deverá mais ser possivel realizar carga de documentos relacionados ao processo administrativo, incluido os contratos e apensos vinculados ao mesmo.';


--
-- TOC entry 8022 (class 0 OID 0)
-- Dependencies: 1911
-- Name: COLUMN mtrtb046_processo_adm.co_matricula_finalizacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb046_processo_adm.co_matricula_finalizacao IS 'Atributo utilizado para armazenar a matricula do usuário do sistema responsável pela finalização do registro do processo administrativo.

Após o registro ser finalizado, não deverá mais ser possivel realizar carga de documentos relacionados ao processo administrativo, incluido os contratos e apensos vinculados ao mesmo.';


--
-- TOC entry 8023 (class 0 OID 0)
-- Dependencies: 1911
-- Name: COLUMN mtrtb046_processo_adm.co_protocolo_siclg; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb046_processo_adm.co_protocolo_siclg IS 'Atributo utilizado para armanezar o numero de protocolo atribuido pelo SICLG no momento da criação do fluxo do processo.

Esta informação servirá como identificação negocial do registro do processo apenas de carater informativo.';


--
-- TOC entry 8024 (class 0 OID 0)
-- Dependencies: 1911
-- Name: COLUMN mtrtb046_processo_adm.nu_unidade_demandante; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb046_processo_adm.nu_unidade_demandante IS 'Atributo utilizado para armazenar a unidade CAIXA demaqndante da solicitação que originou o processo relativo ao registro.';


--
-- TOC entry 1912 (class 1259 OID 2025832)
-- Name: mtrtb047_contrato_adm; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 8025 (class 0 OID 0)
-- Dependencies: 1912
-- Name: TABLE mtrtb047_contrato_adm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb047_contrato_adm IS 'Tabela responsavel pelo armazenamento dos contratos eletronicos formados durante um fluxo de contratação.

Os contratos são relacionados com um processo e podem ser criados registros de apensos de penalidade ou ressarcimento baseados no andamento do contrato.

O registro do contrato é utilizado para realizar a vinculação de documentos eletronicos de forma a gerar um dossiê proprio para este fim.';


--
-- TOC entry 8026 (class 0 OID 0)
-- Dependencies: 1912
-- Name: COLUMN mtrtb047_contrato_adm.nu_contrato_adm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb047_contrato_adm.nu_contrato_adm IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8027 (class 0 OID 0)
-- Dependencies: 1912
-- Name: COLUMN mtrtb047_contrato_adm.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb047_contrato_adm.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8028 (class 0 OID 0)
-- Dependencies: 1912
-- Name: COLUMN mtrtb047_contrato_adm.nu_processo_adm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb047_contrato_adm.nu_processo_adm IS 'Atributo que representa o processo administrativo de vinculação do contrato.';


--
-- TOC entry 8029 (class 0 OID 0)
-- Dependencies: 1912
-- Name: COLUMN mtrtb047_contrato_adm.nu_contrato; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb047_contrato_adm.nu_contrato IS 'Atributo utilizado para armazenar o numero de identificação negocial do contrato

Juntamente com o "nu_ano_contrato", forma-se o numero de identificação negocial do contrato.

Ex: 105/2018';


--
-- TOC entry 8030 (class 0 OID 0)
-- Dependencies: 1912
-- Name: COLUMN mtrtb047_contrato_adm.nu_ano_contrato; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb047_contrato_adm.nu_ano_contrato IS 'Atributo utilizado para armazenar o ano de identificação negocial do contrato

Juntamente com o "nu_contrato", forma-se o numero de identificação negocial do contrato.

Ex: 105/2018';


--
-- TOC entry 8031 (class 0 OID 0)
-- Dependencies: 1912
-- Name: COLUMN mtrtb047_contrato_adm.de_contrato; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb047_contrato_adm.de_contrato IS 'Atributo utilizado para armazenar uma descrição livre sobre o contrato';


--
-- TOC entry 8032 (class 0 OID 0)
-- Dependencies: 1912
-- Name: COLUMN mtrtb047_contrato_adm.ts_inclusao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb047_contrato_adm.ts_inclusao IS 'Atributo utilizado para armazenar a data/hora de inclusão do registro do contrato administrativo.';


--
-- TOC entry 8033 (class 0 OID 0)
-- Dependencies: 1912
-- Name: COLUMN mtrtb047_contrato_adm.co_matricula; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb047_contrato_adm.co_matricula IS 'Atributo utilizado para armazenar a matricula do usuário do sistema responsável pela inclusão do registro do contrato administrativo.';


--
-- TOC entry 8034 (class 0 OID 0)
-- Dependencies: 1912
-- Name: COLUMN mtrtb047_contrato_adm.nu_cpf_cnpj_fornecedor; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb047_contrato_adm.nu_cpf_cnpj_fornecedor IS 'Atributo utilizado para identificar o CPF/CNPJ do fornecedor vinculado ao contrato.

O atributo esta armazenado como texto para que seja armazenado o valor do atributo para os casos de CPF com 11 posições e nos casos de CNPJ com 14 posições';


--
-- TOC entry 8035 (class 0 OID 0)
-- Dependencies: 1912
-- Name: COLUMN mtrtb047_contrato_adm.nu_unidade_operacional; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb047_contrato_adm.nu_unidade_operacional IS 'Atributo utilizado para armazenar a unidade CAIXA responsável pela operacionalização do contrato.';


--
-- TOC entry 1913 (class 1259 OID 2025839)
-- Name: mtrtb048_apenso_adm; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 8036 (class 0 OID 0)
-- Dependencies: 1913
-- Name: TABLE mtrtb048_apenso_adm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb048_apenso_adm IS 'Tabela responsavel pelo armazenamento dos apensos eletronicos formados durante um fluxo de contratação.

Os apensos são relacionados com um processo, neste caso representam as penalidade de processo, ou podem ser vinculados aos contratos, neste caso representam apensos de penalidade ou ressarcimento.

O registro do apenso é utilizado para realizar a vinculação de documentos eletronicos de forma a gerar um dossiê proprio para este fim.';


--
-- TOC entry 8037 (class 0 OID 0)
-- Dependencies: 1913
-- Name: COLUMN mtrtb048_apenso_adm.nu_apenso_adm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb048_apenso_adm.nu_apenso_adm IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8038 (class 0 OID 0)
-- Dependencies: 1913
-- Name: COLUMN mtrtb048_apenso_adm.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb048_apenso_adm.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8039 (class 0 OID 0)
-- Dependencies: 1913
-- Name: COLUMN mtrtb048_apenso_adm.nu_processo_adm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb048_apenso_adm.nu_processo_adm IS 'Atributo que representa o processo administrativo vinculado ao apenso.

Quando este atributo esta preenchido o atributo que representa a vinculação com o contrato deve estar nulo.';


--
-- TOC entry 8040 (class 0 OID 0)
-- Dependencies: 1913
-- Name: COLUMN mtrtb048_apenso_adm.nu_contrato_adm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb048_apenso_adm.nu_contrato_adm IS 'Atributo que representa o contrato administrativo vinculado ao apenso.

Quando este atributo esta preenchido o atributo que representa a vinculação com o processo deve estar nulo.';


--
-- TOC entry 8041 (class 0 OID 0)
-- Dependencies: 1913
-- Name: COLUMN mtrtb048_apenso_adm.nu_cpf_cnpj_fornecedor; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb048_apenso_adm.nu_cpf_cnpj_fornecedor IS 'Atributo utilizado para identificar o CPF/CNPJ do fornecedor vinculado ao apenso.

Este atributo deve ser preenchido apenas quando o apenso esta vinculado ao processo, pois quando vinculado a algum contrato, o CPF/CNPJ relativo ao contrato já esta identificado no registro do mesmo.

Para os casos de processo, não existe uma definição da pessoa relacionada pois processo pode ainda estar na fase de identificação e validação dos participanetes, mas em alguns casos torna-se necessario aplicar uma penalidade a algum desses participantes.

O atributo esta armazenado como texto para que seja armazenado o valor do atributo para os casos de CPF com 11 posições e nos casos de CNPJ com 14 posições';


--
-- TOC entry 8042 (class 0 OID 0)
-- Dependencies: 1913
-- Name: COLUMN mtrtb048_apenso_adm.ic_tipo_apenso; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb048_apenso_adm.ic_tipo_apenso IS 'Atributo utilizado para identificar o tipo de apenso sob o seguinte dominio:

RC - Ressarcimento de Contrato
PC - Penalidade de Contrato
PP - Penalidade de Processo

Este atributo só deve ser identificado como PP quando a chave estrangeiro da relação com o processo administrativo esta preenchida. Para os casos em que o apenso esteja relacionado com o contrato, este atributo deverá possuir os demais valores.';


--
-- TOC entry 8043 (class 0 OID 0)
-- Dependencies: 1913
-- Name: COLUMN mtrtb048_apenso_adm.de_apenso; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb048_apenso_adm.de_apenso IS 'Atributo utilizado para armazenar uma descrição livre do apenso.';


--
-- TOC entry 8044 (class 0 OID 0)
-- Dependencies: 1913
-- Name: COLUMN mtrtb048_apenso_adm.ts_inclusao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb048_apenso_adm.ts_inclusao IS 'Atributo utilizado para armazenar a data/hora de inclusão do registro do apenso administrativo.';


--
-- TOC entry 8045 (class 0 OID 0)
-- Dependencies: 1913
-- Name: COLUMN mtrtb048_apenso_adm.co_matricula; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb048_apenso_adm.co_matricula IS 'Atributo utilizado para armazenar a matricula do usuário do sistema responsável pela inclusão do registro do apenso administrativo.';


--
-- TOC entry 8046 (class 0 OID 0)
-- Dependencies: 1913
-- Name: COLUMN mtrtb048_apenso_adm.co_protocolo_siclg; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb048_apenso_adm.co_protocolo_siclg IS 'Atributo utilizado para armanezar o numero de protocolo atribuido pelo SICLG no momento da criação do fluxo do apenso.

Esta informação servirá como identificação negocial do registro do apenso.';


--
-- TOC entry 8047 (class 0 OID 0)
-- Dependencies: 1913
-- Name: COLUMN mtrtb048_apenso_adm.no_titulo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb048_apenso_adm.no_titulo IS 'Atributo utilizado para permitir a identificação nomeada de um apenso.

Este atributo deverá armazenar um valor unico para apensos vinculados a um mesmo contratoc ou processo, ou seja, uma chave de unicidade existente entre o numero do processo, numero do contrato e titulo do apenso, pois sempre que houver vinculo de um apenso com o processo, não haverá com o contrato e vice versa.';


--
-- TOC entry 1914 (class 1259 OID 2025846)
-- Name: mtrtb049_documento_adm; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 8048 (class 0 OID 0)
-- Dependencies: 1914
-- Name: TABLE mtrtb049_documento_adm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb049_documento_adm IS 'Tabela responsavel pelo armazenamento dos metadados dos documentos relacionados ao processo administrativo eletronico.

Esses documentos podem estar associados a um processo, contrato ou apenso.

Nesta tabela serão armazenadas as indicações de documento valido, confidencial e a indicação de qual documento de substituição do original invalidando o mesmo.';


--
-- TOC entry 8049 (class 0 OID 0)
-- Dependencies: 1914
-- Name: COLUMN mtrtb049_documento_adm.nu_documento_adm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb049_documento_adm.nu_documento_adm IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8050 (class 0 OID 0)
-- Dependencies: 1914
-- Name: COLUMN mtrtb049_documento_adm.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb049_documento_adm.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8051 (class 0 OID 0)
-- Dependencies: 1914
-- Name: COLUMN mtrtb049_documento_adm.nu_documento; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb049_documento_adm.nu_documento IS 'Atributo utilizado para relacionar o documento que foi carregado na estrutura do ECM com a estrutura relacionada a um processo, contrato ou apenso';


--
-- TOC entry 8052 (class 0 OID 0)
-- Dependencies: 1914
-- Name: COLUMN mtrtb049_documento_adm.nu_documento_substituto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb049_documento_adm.nu_documento_substituto IS 'Atributo utilizado para relacionar o documento que invalidou o documento original representado no atributo "nu_documento" de forma a substitui-lo';


--
-- TOC entry 8053 (class 0 OID 0)
-- Dependencies: 1914
-- Name: COLUMN mtrtb049_documento_adm.nu_processo_adm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb049_documento_adm.nu_processo_adm IS 'Atributo que representa o processo administrativo de vinculação do documento carregado.

Caso este atributo esteja definido os atributos que representam o contrato e o apenso deverão estar com os valores nulos.';


--
-- TOC entry 8054 (class 0 OID 0)
-- Dependencies: 1914
-- Name: COLUMN mtrtb049_documento_adm.nu_contrato_adm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb049_documento_adm.nu_contrato_adm IS 'Atributo que representa o contrato administrativo de vinculação do documento carregado.

Caso este atributo esteja definido os atributos que representam o processo e o apenso deverão estar com os valores nulos.';


--
-- TOC entry 8055 (class 0 OID 0)
-- Dependencies: 1914
-- Name: COLUMN mtrtb049_documento_adm.nu_apenso_adm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb049_documento_adm.nu_apenso_adm IS 'Atributo que representa o apenso administrativo de vinculação do documento carregado.

Caso este atributo esteja definido os atributos que representam o processo e o contrato deverão estar com os valores nulos.';


--
-- TOC entry 8056 (class 0 OID 0)
-- Dependencies: 1914
-- Name: COLUMN mtrtb049_documento_adm.ic_valido; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb049_documento_adm.ic_valido IS 'Atributo utilizdo para indicar que o documento esta valido e não foi substituido por nenhum outro documento.';


--
-- TOC entry 8057 (class 0 OID 0)
-- Dependencies: 1914
-- Name: COLUMN mtrtb049_documento_adm.ic_confidencial; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb049_documento_adm.ic_confidencial IS 'Atributo utilizado para identificar que o documento tem cunho confidencial.

Nestes casos, só deverá ser possivel realizar o download ou visualização do documento usuários que possuam perfil especifico para este fim.';


--
-- TOC entry 8058 (class 0 OID 0)
-- Dependencies: 1914
-- Name: COLUMN mtrtb049_documento_adm.de_documento_adm; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb049_documento_adm.de_documento_adm IS 'Atributo utilizado para armazenar uma descrição para identificação e pesquisa.';


--
-- TOC entry 8059 (class 0 OID 0)
-- Dependencies: 1914
-- Name: COLUMN mtrtb049_documento_adm.de_justificativa_substituicao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb049_documento_adm.de_justificativa_substituicao IS 'Atributo utilizado para armazenar a justificativa utilizada quando da substituição de um documento adminstrativo.

Este campo deverá estar preenchi sempre que o atributo nu_documento_substituto estiver definido.';


--
-- TOC entry 8060 (class 0 OID 0)
-- Dependencies: 1914
-- Name: COLUMN mtrtb049_documento_adm.ts_exclusao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb049_documento_adm.ts_exclusao IS 'Atributo utilizado para registrar a data e hora de execução da ação de exclusão logica do documento administrativo';


--
-- TOC entry 8061 (class 0 OID 0)
-- Dependencies: 1914
-- Name: COLUMN mtrtb049_documento_adm.co_matricula_exclusao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb049_documento_adm.co_matricula_exclusao IS 'Atributo utilizado para registrar a matricula do usuário que executou a ação de exclusão logica do documento administrativo

Este campo deverá estar preenchido sempre que o atributo ts_exclusao estiver definido.';


--
-- TOC entry 8062 (class 0 OID 0)
-- Dependencies: 1914
-- Name: COLUMN mtrtb049_documento_adm.de_justificativa_exclusao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb049_documento_adm.de_justificativa_exclusao IS 'Atributo utilizado para armazenar a justificativa utilizada quando da exclusão logica de um documento adminstrativo.

Este campo deverá estar preenchido sempre que o atributo ts_exclusao estiver definido.';


--
-- TOC entry 1915 (class 1259 OID 2025853)
-- Name: mtrtb102_autorizacao_negada; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 8063 (class 0 OID 0)
-- Dependencies: 1915
-- Name: TABLE mtrtb102_autorizacao_negada; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb102_autorizacao_negada IS 'Tabela utilizada para armazenar as solicitações de autorizações relacionadas ao nivel documental que foram negadas.';


--
-- TOC entry 8064 (class 0 OID 0)
-- Dependencies: 1915
-- Name: COLUMN mtrtb102_autorizacao_negada.nu_autorizacao_negada; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb102_autorizacao_negada.nu_autorizacao_negada IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8065 (class 0 OID 0)
-- Dependencies: 1915
-- Name: COLUMN mtrtb102_autorizacao_negada.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb102_autorizacao_negada.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8066 (class 0 OID 0)
-- Dependencies: 1915
-- Name: COLUMN mtrtb102_autorizacao_negada.ts_registro; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb102_autorizacao_negada.ts_registro IS 'Atributo utilizado para armazenar a datae hora de recebimento da solicitação de autorização ';


--
-- TOC entry 8067 (class 0 OID 0)
-- Dependencies: 1915
-- Name: COLUMN mtrtb102_autorizacao_negada.nu_operacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb102_autorizacao_negada.nu_operacao IS 'Atributo utilizado para armazenar o codigo de operação do produto solicitado na autorização.';


--
-- TOC entry 8068 (class 0 OID 0)
-- Dependencies: 1915
-- Name: COLUMN mtrtb102_autorizacao_negada.nu_modalidade; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb102_autorizacao_negada.nu_modalidade IS 'Atributo utilizado para armazenar o codigo da modalidade do produto solicitado na autorização.';


--
-- TOC entry 8069 (class 0 OID 0)
-- Dependencies: 1915
-- Name: COLUMN mtrtb102_autorizacao_negada.no_produto; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb102_autorizacao_negada.no_produto IS 'Atributo utilizado para armazenar o nome do produto solicitado na autorização.';


--
-- TOC entry 8070 (class 0 OID 0)
-- Dependencies: 1915
-- Name: COLUMN mtrtb102_autorizacao_negada.nu_cpf_cnpj; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb102_autorizacao_negada.nu_cpf_cnpj IS 'Atributo utilizado para armazenar o numero do CPF/CNPJ do cliente vinculado a autorização fornecida.';


--
-- TOC entry 8071 (class 0 OID 0)
-- Dependencies: 1915
-- Name: COLUMN mtrtb102_autorizacao_negada.ic_tipo_pessoa; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb102_autorizacao_negada.ic_tipo_pessoa IS 'Atributo utilizado para indicar o tipo de pessoa, se fisica ou juridica podendo assumir os seguintes valores:
F - Fisica
J - Juridica';


--
-- TOC entry 8072 (class 0 OID 0)
-- Dependencies: 1915
-- Name: COLUMN mtrtb102_autorizacao_negada.sg_canal_solicitacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb102_autorizacao_negada.sg_canal_solicitacao IS 'Sigla de identificação do canal/sistema solicitante da autorização.
Informação é obtida pela comparação do codigo de integração enviado com o registro da tabela 006';


--
-- TOC entry 8073 (class 0 OID 0)
-- Dependencies: 1915
-- Name: COLUMN mtrtb102_autorizacao_negada.de_motivo; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb102_autorizacao_negada.de_motivo IS 'Atributo utilizado para armazenar a descrição do motivo para a negativa.';


--
-- TOC entry 1916 (class 1259 OID 2025860)
-- Name: mtrtb103_autorizacao_orientacao; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
--

CREATE TABLE mtrtb103_autorizacao_orientacao (
    nu_autorizacao_orientacao bigint DEFAULT nextval('mtrsq103_autorizacao_orientacao'::regclass) NOT NULL,
    nu_versao integer NOT NULL,
    nu_autorizacao bigint NOT NULL,
    ic_sistema character varying(10) NOT NULL,
    de_orientacao text NOT NULL
);


--
-- TOC entry 8074 (class 0 OID 0)
-- Dependencies: 1916
-- Name: TABLE mtrtb103_autorizacao_orientacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb103_autorizacao_orientacao IS 'Tabela utilizada para armazenar as orientações encaminhadas conforme comportamentos de pesquisa definido ao momento da autorização de forma a permitir recuperar todo a informação referente a autorização fornecida.';


--
-- TOC entry 8075 (class 0 OID 0)
-- Dependencies: 1916
-- Name: COLUMN mtrtb103_autorizacao_orientacao.nu_autorizacao_orientacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb103_autorizacao_orientacao.nu_autorizacao_orientacao IS 'Atributo que representa a chave primaria da entidade.';


--
-- TOC entry 8076 (class 0 OID 0)
-- Dependencies: 1916
-- Name: COLUMN mtrtb103_autorizacao_orientacao.nu_versao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb103_autorizacao_orientacao.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';


--
-- TOC entry 8077 (class 0 OID 0)
-- Dependencies: 1916
-- Name: COLUMN mtrtb103_autorizacao_orientacao.nu_autorizacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb103_autorizacao_orientacao.nu_autorizacao IS 'Atributo utilizado para vincular a orientação a autorização concedida.
Quando este atributo estiver definido, o atributo nu_autorizacao_negada deverá ser nulo.';


--
-- TOC entry 8078 (class 0 OID 0)
-- Dependencies: 1916
-- Name: COLUMN mtrtb103_autorizacao_orientacao.ic_sistema; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb103_autorizacao_orientacao.ic_sistema IS 'Atributo utilizado para identificar o sistema de realização da pesquisa cadastral que originou a orientação a epoca do fornecimento da autorização.';


--
-- TOC entry 8079 (class 0 OID 0)
-- Dependencies: 1916
-- Name: COLUMN mtrtb103_autorizacao_orientacao.de_orientacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb103_autorizacao_orientacao.de_orientacao IS 'Atributo utilizado para armazenar a orientação concedida a época do fornecimento da autorização.';


--
-- TOC entry 1917 (class 1259 OID 2025867)
-- Name: mtrtb200_sicli_erro; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 8080 (class 0 OID 0)
-- Dependencies: 1917
-- Name: TABLE mtrtb200_sicli_erro; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON TABLE mtrtb200_sicli_erro IS 'Tabela utilizada para armazenar os erros de comunicação e resposta do SECLI';


--
-- TOC entry 8081 (class 0 OID 0)
-- Dependencies: 1917
-- Name: COLUMN mtrtb200_sicli_erro.nu_sicli_erro; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb200_sicli_erro.nu_sicli_erro IS 'Atributo que representa a chave primária da entidade.';


--
-- TOC entry 8082 (class 0 OID 0)
-- Dependencies: 1917
-- Name: COLUMN mtrtb200_sicli_erro.co_matricula; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb200_sicli_erro.co_matricula IS 'Atributo que representa a matricula do usuário que realizou a pesquisa';


--
-- TOC entry 8083 (class 0 OID 0)
-- Dependencies: 1917
-- Name: COLUMN mtrtb200_sicli_erro.ip_usuario; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb200_sicli_erro.ip_usuario IS 'Atributo que representa o IP da máquina do usuário que solicitou a pesquisa';


--
-- TOC entry 8084 (class 0 OID 0)
-- Dependencies: 1917
-- Name: COLUMN mtrtb200_sicli_erro.dns_usuario; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb200_sicli_erro.dns_usuario IS 'Atributo que representa o DNS da máquina do usuário que solicitou a pesquisa';


--
-- TOC entry 8085 (class 0 OID 0)
-- Dependencies: 1917
-- Name: COLUMN mtrtb200_sicli_erro.ts_erro; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb200_sicli_erro.ts_erro IS 'Atributo utilizado para registar a data hora(com segundos) da pesquisa realizada';


--
-- TOC entry 8086 (class 0 OID 0)
-- Dependencies: 1917
-- Name: COLUMN mtrtb200_sicli_erro.co_identificacao; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb200_sicli_erro.co_identificacao IS 'Atributo utilizado para referenciar o número de identificação do cliente pesquisado no SICLI';


--
-- TOC entry 8087 (class 0 OID 0)
-- Dependencies: 1917
-- Name: COLUMN mtrtb200_sicli_erro.ic_tipo_pessoa; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb200_sicli_erro.ic_tipo_pessoa IS 'Atributo que determina qual tipo de pessoa possa ter.
Pode assumir os seguintes valores:
F - Física
J - Jurídica
C - Cocli';


--
-- TOC entry 8088 (class 0 OID 0)
-- Dependencies: 1917
-- Name: COLUMN mtrtb200_sicli_erro.de_erro; Type: COMMENT; Schema: mtr; Owner: -
--

COMMENT ON COLUMN mtrtb200_sicli_erro.de_erro IS 'Atributo para descrever os detalhes do erro ocorrido';


--
-- TOC entry 1918 (class 1259 OID 2025874)
-- Name: schema_version; Type: TABLE; Schema: mtr; Owner: -; Tablespace: mtrtsdt000
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


--
-- TOC entry 6921 (class 2604 OID 2025881)
-- Name: nu_dossie_cliente; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb001_dossie_cliente ALTER COLUMN nu_dossie_cliente SET DEFAULT nextval('mtrsq001_dossie_cliente'::regclass);


--
-- TOC entry 6922 (class 2604 OID 2025882)
-- Name: nu_dossie_produto; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb002_dossie_produto ALTER COLUMN nu_dossie_produto SET DEFAULT nextval('mtrsq002_dossie_produto'::regclass);


--
-- TOC entry 6923 (class 2604 OID 2025883)
-- Name: nu_documento; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb003_documento ALTER COLUMN nu_documento SET DEFAULT nextval('mtrsq003_documento'::regclass);


--
-- TOC entry 6926 (class 2604 OID 2025884)
-- Name: nu_canal; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb006_canal ALTER COLUMN nu_canal SET DEFAULT nextval('mtrsq006_canal'::regclass);


--
-- TOC entry 6927 (class 2604 OID 2025885)
-- Name: nu_atributo_documento; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb007_atributo_documento ALTER COLUMN nu_atributo_documento SET DEFAULT nextval('mtrsq007_atributo_documento'::regclass);


--
-- TOC entry 6928 (class 2604 OID 2025886)
-- Name: nu_tipo_documento; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb009_tipo_documento ALTER COLUMN nu_tipo_documento SET DEFAULT nextval('mtrsq009_tipo_documento'::regclass);


--
-- TOC entry 6929 (class 2604 OID 2025887)
-- Name: nu_funcao_documental; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb010_funcao_documental ALTER COLUMN nu_funcao_documental SET DEFAULT nextval('mtrsq010_funcao_documental'::regclass);


--
-- TOC entry 6930 (class 2604 OID 2025888)
-- Name: nu_tipo_situacao_dossie; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb012_tipo_situacao_dossie ALTER COLUMN nu_tipo_situacao_dossie SET DEFAULT nextval('mtrsq012_tipo_situacao_dossie'::regclass);


--
-- TOC entry 6933 (class 2604 OID 2025889)
-- Name: nu_situacao_dossie; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb013_situacao_dossie ALTER COLUMN nu_situacao_dossie SET DEFAULT nextval('mtrsq013_situacao_dossie'::regclass);


--
-- TOC entry 6934 (class 2604 OID 2025890)
-- Name: nu_instancia_documento; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb014_instancia_documento ALTER COLUMN nu_instancia_documento SET DEFAULT nextval('mtrsq014_instancia_documento'::regclass);


--
-- TOC entry 6935 (class 2604 OID 2025891)
-- Name: nu_situacao_documento; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb015_situacao_documento ALTER COLUMN nu_situacao_documento SET DEFAULT nextval('mtrsq015_situacao_documento'::regclass);


--
-- TOC entry 6938 (class 2604 OID 2025892)
-- Name: nu_motivo_stco_documento; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb016_motivo_stco_documento ALTER COLUMN nu_motivo_stco_documento SET DEFAULT nextval('mtrsq016_motivo_stco_documento'::regclass);


--
-- TOC entry 6939 (class 2604 OID 2025893)
-- Name: nu_processo; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb020_processo ALTER COLUMN nu_processo SET DEFAULT nextval('mtrsq020_processo'::regclass);


--
-- TOC entry 6941 (class 2604 OID 2025894)
-- Name: nu_unidade_autorizada; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb021_unidade_autorizada ALTER COLUMN nu_unidade_autorizada SET DEFAULT nextval('mtrsq021_unidade_autorizada'::regclass);


--
-- TOC entry 6942 (class 2604 OID 2025895)
-- Name: nu_produto; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb022_produto ALTER COLUMN nu_produto SET DEFAULT nextval('mtrsq022_produto'::regclass);


--
-- TOC entry 6949 (class 2604 OID 2025896)
-- Name: nu_produto_dossie; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb024_produto_dossie ALTER COLUMN nu_produto_dossie SET DEFAULT nextval('mtrsq024_produto_dossie'::regclass);


--
-- TOC entry 6951 (class 2604 OID 2025897)
-- Name: nu_processo_documento; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb025_processo_documento ALTER COLUMN nu_processo_documento SET DEFAULT nextval('mtrsq025_processo_documento'::regclass);


--
-- TOC entry 6952 (class 2604 OID 2025898)
-- Name: nu_campo_entrada; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb027_campo_entrada ALTER COLUMN nu_campo_entrada SET DEFAULT nextval('mtrsq027_campo_entrada'::regclass);


--
-- TOC entry 6953 (class 2604 OID 2025899)
-- Name: nu_opcao_campo; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb028_opcao_campo ALTER COLUMN nu_opcao_campo SET DEFAULT nextval('mtrsq028_opcao_campo'::regclass);


--
-- TOC entry 6954 (class 2604 OID 2025900)
-- Name: nu_campo_apresentacao; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb029_campo_apresentacao ALTER COLUMN nu_campo_apresentacao SET DEFAULT nextval('mtrsq029_campo_apresentacao'::regclass);


--
-- TOC entry 6955 (class 2604 OID 2025901)
-- Name: nu_resposta_dossie; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb030_resposta_dossie ALTER COLUMN nu_resposta_dossie SET DEFAULT nextval('mtrsq030_resposta_dossie'::regclass);


--
-- TOC entry 6956 (class 2604 OID 2025902)
-- Name: nu_garantia; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb033_garantia ALTER COLUMN nu_garantia SET DEFAULT nextval('mtrsq033_garantia'::regclass);


--
-- TOC entry 6957 (class 2604 OID 2025903)
-- Name: nu_garantia_informada; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb035_garantia_informada ALTER COLUMN nu_garantia_informada SET DEFAULT nextval('mtrsq035_garantia_informada'::regclass);


--
-- TOC entry 6960 (class 2604 OID 2025904)
-- Name: nu_composicao_documental; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb036_composicao_documental ALTER COLUMN nu_composicao_documental SET DEFAULT nextval('mtrsq036_composicao_documental'::regclass);


--
-- TOC entry 6961 (class 2604 OID 2025905)
-- Name: nu_regra_documental; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb037_regra_documental ALTER COLUMN nu_regra_documental SET DEFAULT nextval('mtrsq037_regra_documental'::regclass);


--
-- TOC entry 6962 (class 2604 OID 2025906)
-- Name: nu_autorizacao; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb100_autorizacao ALTER COLUMN nu_autorizacao SET DEFAULT nextval('mtrsq100_autorizacao'::regclass);


--
-- TOC entry 6963 (class 2604 OID 2025907)
-- Name: nu_documento; Type: DEFAULT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb101_documento ALTER COLUMN nu_documento SET DEFAULT nextval('mtrsq101_documento'::regclass);


--
-- TOC entry 8089 (class 0 OID 0)
-- Dependencies: 1823
-- Name: mtrsq001_dossie_cliente; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq001_dossie_cliente', 544, true);


--
-- TOC entry 8090 (class 0 OID 0)
-- Dependencies: 1825
-- Name: mtrsq002_dossie_produto; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq002_dossie_produto', 165, true);


--
-- TOC entry 8091 (class 0 OID 0)
-- Dependencies: 1827
-- Name: mtrsq003_documento; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq003_documento', 2581, true);


--
-- TOC entry 8092 (class 0 OID 0)
-- Dependencies: 1828
-- Name: mtrsq004_dossie_cliente_produto; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq004_dossie_cliente_produto', 249, true);


--
-- TOC entry 8093 (class 0 OID 0)
-- Dependencies: 1830
-- Name: mtrsq006_canal; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq006_canal', 3, true);


--
-- TOC entry 8094 (class 0 OID 0)
-- Dependencies: 1832
-- Name: mtrsq007_atributo_documento; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq007_atributo_documento', 14144, true);


--
-- TOC entry 8095 (class 0 OID 0)
-- Dependencies: 1919
-- Name: mtrsq008_conteudo; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq008_conteudo', 21, true);


--
-- TOC entry 8096 (class 0 OID 0)
-- Dependencies: 1834
-- Name: mtrsq009_tipo_documento; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq009_tipo_documento', 172, true);


--
-- TOC entry 8097 (class 0 OID 0)
-- Dependencies: 1836
-- Name: mtrsq010_funcao_documental; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq010_funcao_documental', 7, true);


--
-- TOC entry 8098 (class 0 OID 0)
-- Dependencies: 1838
-- Name: mtrsq012_tipo_situacao_dossie; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq012_tipo_situacao_dossie', 16, true);


--
-- TOC entry 8099 (class 0 OID 0)
-- Dependencies: 1840
-- Name: mtrsq013_situacao_dossie; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq013_situacao_dossie', 382, true);


--
-- TOC entry 8100 (class 0 OID 0)
-- Dependencies: 1842
-- Name: mtrsq014_instancia_documento; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq014_instancia_documento', 215, true);


--
-- TOC entry 8101 (class 0 OID 0)
-- Dependencies: 1844
-- Name: mtrsq015_situacao_documento; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq015_situacao_documento', 6, true);


--
-- TOC entry 8102 (class 0 OID 0)
-- Dependencies: 1846
-- Name: mtrsq016_motivo_stco_documento; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq016_motivo_stco_documento', 4, true);


--
-- TOC entry 8103 (class 0 OID 0)
-- Dependencies: 1847
-- Name: mtrsq017_situacao_instnca_dcmnto; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq017_situacao_instnca_dcmnto', 151, true);


--
-- TOC entry 8104 (class 0 OID 0)
-- Dependencies: 1848
-- Name: mtrsq019_campo_formulario; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq019_campo_formulario', 1, false);


--
-- TOC entry 8105 (class 0 OID 0)
-- Dependencies: 1850
-- Name: mtrsq020_processo; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq020_processo', 23, true);


--
-- TOC entry 8106 (class 0 OID 0)
-- Dependencies: 1852
-- Name: mtrsq021_unidade_autorizada; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq021_unidade_autorizada', 15, true);


--
-- TOC entry 8107 (class 0 OID 0)
-- Dependencies: 1854
-- Name: mtrsq022_produto; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq022_produto', 250, true);


--
-- TOC entry 8108 (class 0 OID 0)
-- Dependencies: 1856
-- Name: mtrsq024_produto_dossie; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq024_produto_dossie', 140, true);


--
-- TOC entry 8109 (class 0 OID 0)
-- Dependencies: 1858
-- Name: mtrsq025_processo_documento; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq025_processo_documento', 70, true);


--
-- TOC entry 8110 (class 0 OID 0)
-- Dependencies: 1860
-- Name: mtrsq027_campo_entrada; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq027_campo_entrada', 6, true);


--
-- TOC entry 8111 (class 0 OID 0)
-- Dependencies: 1862
-- Name: mtrsq028_opcao_campo; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq028_opcao_campo', 9, true);


--
-- TOC entry 8112 (class 0 OID 0)
-- Dependencies: 1864
-- Name: mtrsq029_campo_apresentacao; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq029_campo_apresentacao', 1, false);


--
-- TOC entry 8113 (class 0 OID 0)
-- Dependencies: 1866
-- Name: mtrsq030_resposta_dossie; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq030_resposta_dossie', 300, true);


--
-- TOC entry 8114 (class 0 OID 0)
-- Dependencies: 1867
-- Name: mtrsq032_elemento_conteudo; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq032_elemento_conteudo', 4, true);


--
-- TOC entry 8115 (class 0 OID 0)
-- Dependencies: 1869
-- Name: mtrsq033_garantia; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq033_garantia', 66, true);


--
-- TOC entry 8116 (class 0 OID 0)
-- Dependencies: 1871
-- Name: mtrsq035_garantia_informada; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq035_garantia_informada', 78, true);


--
-- TOC entry 8117 (class 0 OID 0)
-- Dependencies: 1873
-- Name: mtrsq036_composicao_documental; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq036_composicao_documental', 3, true);


--
-- TOC entry 8118 (class 0 OID 0)
-- Dependencies: 1875
-- Name: mtrsq037_regra_documental; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq037_regra_documental', 9, true);


--
-- TOC entry 8119 (class 0 OID 0)
-- Dependencies: 1876
-- Name: mtrsq043_documento_garantia; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq043_documento_garantia', 4, true);


--
-- TOC entry 8120 (class 0 OID 0)
-- Dependencies: 1877
-- Name: mtrsq044_comportamento_pesquisa; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq044_comportamento_pesquisa', 22, true);


--
-- TOC entry 8121 (class 0 OID 0)
-- Dependencies: 1878
-- Name: mtrsq045_atributo_extracao; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq045_atributo_extracao', 411, true);


--
-- TOC entry 8122 (class 0 OID 0)
-- Dependencies: 1879
-- Name: mtrsq046_processo_adm; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq046_processo_adm', 1, true);


--
-- TOC entry 8123 (class 0 OID 0)
-- Dependencies: 1880
-- Name: mtrsq047_contrato_adm; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq047_contrato_adm', 1, true);


--
-- TOC entry 8124 (class 0 OID 0)
-- Dependencies: 1881
-- Name: mtrsq048_apenso_adm; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq048_apenso_adm', 3, true);


--
-- TOC entry 8125 (class 0 OID 0)
-- Dependencies: 1882
-- Name: mtrsq049_documento_adm; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq049_documento_adm', 1, false);


--
-- TOC entry 8126 (class 0 OID 0)
-- Dependencies: 1884
-- Name: mtrsq100_autorizacao; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq100_autorizacao', 553, true);


--
-- TOC entry 8127 (class 0 OID 0)
-- Dependencies: 1886
-- Name: mtrsq101_documento; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq101_documento', 2512, true);


--
-- TOC entry 8128 (class 0 OID 0)
-- Dependencies: 1887
-- Name: mtrsq102_autorizacao_negada; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq102_autorizacao_negada', 3831, true);


--
-- TOC entry 8129 (class 0 OID 0)
-- Dependencies: 1888
-- Name: mtrsq103_autorizacao_orientacao; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq103_autorizacao_orientacao', 1821, true);


--
-- TOC entry 8130 (class 0 OID 0)
-- Dependencies: 1889
-- Name: mtrsq200_sicli_erro; Type: SEQUENCE SET; Schema: mtr; Owner: -
--

SELECT pg_catalog.setval('mtrsq200_sicli_erro', 975, true);


--
-- TOC entry 7507 (class 0 OID 2025540)
-- Dependencies: 1822
-- Data for Name: mtrtb001_dossie_cliente; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (357, 2, 10347424000180, 'J', 'EMPRESA ABC LTDA', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (358, 5, 94226229800, 'F', 'SEBASTIAO JOAQUIM MOREIRA', '62986540014');
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (359, 1, 191, 'F', 'RECEITA FEDERAL PARA USO DO SISTEMA', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (360, 1, 764054000123, 'J', 'Teste 123', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (454, 7, 32168567808, 'F', 'CARMINA GONCALVES SANTOS', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (458, 3, 37665405134, 'F', 'MARIA AMELIA PEREIRA DO LAGO LIMA', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (389, 1, 41901819, 'F', 'JOSE PEREIRA BORGES', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (464, 3, 97178608072, 'F', 'DENISE WISNIEWSKI DE MATTOS', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (466, 4, 957585691, 'F', 'LUIZ MOREIRA DOS SANTOS', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (394, 1, 13888000105, 'J', 'werwer', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (373, 10, 23800761890, 'F', 'JUAN FELIPE OSPINA MEJIA', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (400, 1, 70006903215, 'F', 'JOSE SOUZA DO ROSARIO', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (361, 5, 13843060134, 'F', 'MARIA ABADIA MENDES PORTELA', '62986540014');
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (447, 2, 75821320259, 'F', 'ALZENIRA DE ARAUJO FERNANDES DE ALMEIDA', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (356, 9, 65179609, 'F', 'OSVALDO ALVES PEREIRA DE MORAES', '6235736354');
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (468, 9, 5467390, 'F', 'JALENILDA BEZERRA MATOSO', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (364, 5, 6149049, 'F', 'JOSE ANTONIO FERNANDEZ PIRES', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (471, 1, 45645615700, 'F', 'teste', '23432423423');
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (456, 3, 2517444188, 'F', 'MARIA CECILIA AMARAL MADEIRA', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (474, 1, 2193048000199, 'J', 'erte', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (395, 3, 2423968442, 'F', 'TACIANA LIMEIRA CAVALCANTI', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (460, 2, 4249104621, 'F', 'ANTONIA IRANI CANDIDO', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (368, 5, 527253677, 'F', 'EVA LUCIA DE OLIVEIRA ROSA', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (476, 4, 2293524132, 'F', 'WILKER NORONHA VITOR', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (363, 6, 66849195972, 'F', 'MARIA DE FATIMA SILVA', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (401, 9, 34776389649, 'F', 'ESTEVAO BARBOSA DE FREITAS', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (470, 9, 265437709, 'F', 'JOSE MARTINS DOS SANTOS', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (478, 1, 94914000169, 'J', 'LOJA WSI', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (367, 15, 244651, 'F', 'LUCIANA RAMOS LOPES', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (524, 4, 14221382830, 'F', 'EDSON JOSE BRASIL', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (528, 2, 66659604120, 'F', 'CLAUDIA CARVALHO FROIS', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (482, 1, 71195475187, 'F', 'LAILIANA DE MOURA BARBOSA', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (484, 1, 19649762272, 'F', 'PASCAL ABOU KHALIL', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (531, 11, 8130979756, 'F', 'PAULA CERQUINHO FARIA LEMOS DA FONSECA', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (510, 3, 76451607134, 'F', 'FABIANO LIMA BARBOSA', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (472, 7, 1108567169, 'F', 'ANA FLAVIA PEREIRA DE OLIVEIRA VITOR', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (514, 23, 11938900120, 'F', 'REGIA MARIA FONTINELLE VIANA', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (409, 30, 1255191171, 'F', 'RENATO FONTINELE VIANA', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (494, 88, 29278193860, 'F', 'GILBERTO KAWAZOE', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (522, 5, 9685716706, 'F', 'RAFAEL CAMPOS DE OLIVEIRA', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (483, 8, 3332189492, 'F', 'FERNANDA LEANDRO FONSECA DE SOUZA', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (533, 4, 542286971, 'F', 'GILMAR JOSE DA SILVA', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (534, 4, 81056699515, 'F', 'FABIO SEIXAS SALES', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (491, 71, 7720463609, 'F', 'DANIEL DE ALMEIDA ROCHA', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (517, 17, 90895819104, 'F', 'MATHEUS CUNHA PESSOA', NULL);
INSERT INTO mtrtb001_dossie_cliente (nu_dossie_cliente, nu_versao, nu_cpf_cnpj, ic_tipo_pessoa, no_cliente, de_telefone) VALUES (481, 31, 1525707132, 'F', 'PEDRO CESAR DA SILVA ALVARES', NULL);


--
-- TOC entry 7575 (class 0 OID 2025740)
-- Dependencies: 1890
-- Data for Name: mtrtb001_pessoa_fisica; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (364, '1936-02-25', NULL, NULL, NULL, NULL, 'MARIA ESTER NICOLAZA F PIRES', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (367, '1974-06-16', NULL, NULL, NULL, NULL, 'CARMEN GOMES DO NASCIMENTO', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (368, '1957-10-04', NULL, NULL, NULL, NULL, 'LUZIA DE OLIVEIRA PACHECO', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (373, '1980-11-01', NULL, NULL, NULL, NULL, 'AMPARO DE JESUS MEJIA TORRES', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (361, '1950-01-01', 5, NULL, '123456', 'SSP', 'ISABEL MACIEL MENDES', 'JOAO JOAQUIM', NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (363, '1964-06-12', NULL, NULL, NULL, NULL, 'FLORENTINA BARBOSA DA SILVA', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (358, '1958-10-30', 2, NULL, '5108665', 'SPTCGO', 'LAZARA FRANCISCA MOREIRA', 'Joaquim Moreira Teles', NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (359, '1899-11-30', NULL, NULL, NULL, NULL, 'MAE DESCONHECIDA', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (389, '1926-12-22', NULL, NULL, NULL, NULL, '', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (447, '1979-03-30', NULL, NULL, NULL, NULL, 'ANITA DE ARAUJO FERNANDES', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (395, '1976-07-26', NULL, NULL, NULL, NULL, 'MARIA HELENA LIMEIRA DA SILVA', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (400, '1934-11-25', NULL, NULL, NULL, NULL, 'MARIA FRANCISCA DO ROSARIO', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (401, '1939-09-02', NULL, NULL, NULL, NULL, 'ARMINDA DE FREITAS', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (454, '1936-06-15', NULL, NULL, NULL, NULL, 'IZABEL GONCALVES DO REGO', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (456, '1976-12-28', NULL, NULL, NULL, NULL, 'MARIA ALICE AMARAL MADEIRA', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (458, '1966-07-22', NULL, NULL, NULL, NULL, 'DIOMARINA PEREIRA DO LAGO', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (460, '1969-01-03', NULL, NULL, NULL, NULL, 'ROZILDA MARIANO LIRA', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (356, '1975-08-09', 1, '12312312312', '4553320', 'SSPGO', 'MARLENE ALVES PEREIRA', 'NOME_PAI', NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (409, '1985-08-21', NULL, NULL, NULL, NULL, 'REGIA MARIA FONTINELE VIANA', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (464, '1980-05-05', NULL, NULL, NULL, NULL, 'WILMA MULLER WISNIEWSKI', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (466, '1940-07-29', NULL, NULL, NULL, NULL, 'NAIR DE SOUSA SANTOS', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (468, '1983-08-12', NULL, NULL, NULL, NULL, 'IRANILDE ALVES BEZERRA', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (470, '1971-05-27', NULL, NULL, NULL, NULL, 'IRACEMA MARTINS', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (471, '2017-05-03', 6, '23444444444', 'gdgdfgfwer', '34324', 'rfgdfgdfg', 'fgfgd', NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (472, '1988-12-31', NULL, NULL, NULL, NULL, 'VALDENIRA DIAS DE OLIVEIRA', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (476, '1989-01-12', NULL, NULL, NULL, NULL, 'LUZIA IVONE NORONHA VITOR', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (481, '1986-10-25', NULL, NULL, NULL, NULL, 'VILMA APARECIDA SILVA ALVARES', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (482, '1981-10-21', NULL, NULL, NULL, NULL, 'BERNADETE DE MOURA SILVA BARBOSA', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (483, '1981-06-23', NULL, NULL, NULL, NULL, 'MARLENE VIEIRA DA FONSECA SOUZA', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (484, '1971-03-06', NULL, NULL, NULL, NULL, 'MARIE ELIAS ABI KHALIL', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (491, '1985-09-18', NULL, NULL, NULL, NULL, 'NIRLEY ALMEIDA COSTA SANTANA ROCHA', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (494, '1976-02-03', NULL, NULL, NULL, NULL, 'ARLETE CONCEICAO VILARDO KAWAZOE', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (510, '1975-06-21', NULL, NULL, NULL, NULL, 'MARIA DO CARMO LIMA BARBOSA', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (514, '1956-06-07', NULL, NULL, NULL, NULL, 'JOSEFA RIBAMAR CARDOSO FONTINELE', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (517, '1980-09-25', NULL, NULL, NULL, NULL, 'MARIA DEUSDEME CUNHA E SILVA PESSOA', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (522, '1982-11-11', NULL, NULL, NULL, NULL, 'MARILUCIA MADUREIRA CAMPOS DE OLIVEIRA', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (524, '1971-06-25', NULL, NULL, NULL, NULL, 'MERCIA FABRI BRASIL', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (528, '1975-03-09', NULL, NULL, NULL, NULL, 'MARIA HELENA CARVALHO FROIS', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (531, '1980-02-12', NULL, NULL, NULL, NULL, 'CLAUDIA CERQUINHO FARIA LEMOS DA FONSECA', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (533, '1979-08-28', NULL, NULL, NULL, NULL, 'MARIA MATEUS DA SILVA', NULL, NULL);
INSERT INTO mtrtb001_pessoa_fisica (nu_dossie_cliente, dt_nascimento, ic_estado_civil, nu_nis, nu_identidade, no_orgao_emissor, no_mae, no_pai, vr_renda_mensal) VALUES (534, '1983-01-26', NULL, NULL, NULL, NULL, 'CELIA SEIXAS SALES', NULL, NULL);


--
-- TOC entry 7576 (class 0 OID 2025746)
-- Dependencies: 1891
-- Data for Name: mtrtb001_pessoa_juridica; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb001_pessoa_juridica (nu_dossie_cliente, no_razao_social, dt_fundacao, ic_segmento, vr_faturamento_anual, ic_conglomerado) VALUES (357, 'EMPRESA ABC LTDA', '2003-04-25', 'MPE', NULL, NULL);
INSERT INTO mtrtb001_pessoa_juridica (nu_dossie_cliente, no_razao_social, dt_fundacao, ic_segmento, vr_faturamento_anual, ic_conglomerado) VALUES (360, 'Teste 123', '2018-06-05', 'MPE', NULL, NULL);
INSERT INTO mtrtb001_pessoa_juridica (nu_dossie_cliente, no_razao_social, dt_fundacao, ic_segmento, vr_faturamento_anual, ic_conglomerado) VALUES (394, 'werwer', '2018-10-05', 'MEI', NULL, NULL);
INSERT INTO mtrtb001_pessoa_juridica (nu_dossie_cliente, no_razao_social, dt_fundacao, ic_segmento, vr_faturamento_anual, ic_conglomerado) VALUES (474, 'erte', '2018-07-06', 'MPE', NULL, NULL);
INSERT INTO mtrtb001_pessoa_juridica (nu_dossie_cliente, no_razao_social, dt_fundacao, ic_segmento, vr_faturamento_anual, ic_conglomerado) VALUES (478, 'LOJA WSI', '2018-06-07', 'MPE', NULL, NULL);

-- TOC entry 7514 (class 0 OID 2025562)
-- Dependencies: 1829
-- Data for Name: mtrtb006_canal; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb006_canal (nu_canal, nu_versao, sg_canal, de_canal, co_integracao, ic_canal_caixa, ic_janela_extracao_m0, ic_janela_extracao_m30, ic_janela_extracao_m60, ic_avaliacao_autenticidade) VALUES (1, 1, 'SIPAN', 'SIPAN', 123456789, 'AGE', true, true, true, true);
INSERT INTO mtrtb006_canal (nu_canal, nu_versao, sg_canal, de_canal, co_integracao, ic_canal_caixa, ic_janela_extracao_m0, ic_janela_extracao_m30, ic_janela_extracao_m60, ic_avaliacao_autenticidade) VALUES (3, 1, 'SIMTRWEB', 'Cliente Web do SIMTR na Intranet', 123123123, 'AGE', true, true, true, true);

--
-- TOC entry 7518 (class 0 OID 2025575)
-- Dependencies: 1833
-- Data for Name: mtrtb009_tipo_documento; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (6, 1, 'DECLARAÇÃO DE INATIVIDADE DA(S) EMPRESA(S) INATIVA(S)', 'J', NULL, true, '9', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (10, 1, 'REGISTRO DE EMANCIPAÇÃO', 'F', NULL, false, '14', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (15, 1, 'PARECER/ANÁLISE DE RISCO DE CRÉDITO EMITIDO PELA CERIS', 'A', NULL, true, '21', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (16, 1, 'CRV / CRLV', 'A', NULL, true, '27', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (17, 1, 'NOTA FISCAL', 'A', NULL, true, '28', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (18, 1, 'RECIBO', 'A', NULL, true, ' ', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (19, 1, 'LAUDO TÉCNICO DE AVALIAÇÃO DO BEM', 'A', NULL, true, '29', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (20, 1, 'CERTIDÃO NEGATIVA DE ÔNUS COM OU SEM CADEIA DOMINIAL', 'A', NULL, true, '31', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (22, 1, 'RESOLUÇÃO EMITIDA PELA ALÇADA COMPETENTE', 'A', NULL, true, '35', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (23, 1, 'RELATÓRIO SIMULADOR DE ALÇADAS, SE MGE', 'A', NULL, true, '36', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (24, 1, 'PARECER DA MESA DE NEGÓCIOS, SE MGE', 'A', NULL, false, '37', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (25, 1, 'PARECER DO JURÍDICO', 'A', NULL, false, '38', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (26, 1, 'AVALIAÇÃO DE RISCO DE CRÉDITO', 'A', NULL, true, '40', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (27, 1, 'PROCURAÇÃO', 'A', NULL, true, '41', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (1, 1, 'DOCUMENTO CONSTITUTIVO', 'J', NULL, false, '1', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (11, 1, 'MO33102 - DADOS SOBRE EMPREGADOS', 'A', NULL, true, '16', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (12, 1, 'MO33132 - ATUALIZACAO DE DÍVIDAS PARCELADAS - SCR BACEN', 'A', NULL, false, '17', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (13, 1, 'MO33350 - FICHA DE INFORMAÇÕES E PROPOSTA DE CARTÃO DE CRÉDITO', 'A', NULL, true, '18', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (14, 1, 'MO33533 - LEVANTAMENTO SOCIOECONOMICO MICROCRÉDITO PRODUTIVO ORIENTADO CAIXA', 'A', NULL, true, '19', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (34, 1, 'MO33045 - DADOS COMPLEMENTARES PESSOA FISICA', 'F', NULL, false, '34', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (3, 1, 'MO33301 - TERMO DE CONFERÊNCIA DE AUTENTICIDADE DA DOCUMENTAÇÃO', NULL, NULL, true, '3', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (4, 1, 'MO33100 - ROTEIRO DE VISITA', NULL, NULL, true, '4', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (5, 1, 'MO33355 - DEFINIÇÃO DO CONGLOMERADO', NULL, NULL, true, '7', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (33, 1, 'DECLARAÇÃO DE IMPOSTO DE RENDA PF', 'F', NULL, false, '33', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (21, 1, 'LAUDO DE AVALIAÇÃO DO IMÓVEL', 'A', NULL, true, '32', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (2, 1, 'DECLARAÇÃO DE IMPOSTO DE RENDA PJ', 'J', 365, false, '2', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (41, 1, 'TERMO DE ADESÃO A CESTA', 'A', NULL, false, NULL, 'TERMO_ADESAO', true, false, true, false, 'termo_adesao', NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (35, 1, 'DADOS DECLARADOS', 'F', 365, false, NULL, 'DADOS_DECLARADOS', true, false, true, false, 'dados_declarados', NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (40, 1, 'CONTRATO DE RELACIONAMENTO', 'A', NULL, false, '0003000100030001', 'CONTRATO_RELACIONAMENTO', true, false, true, false, 'contrato_relacionamento', NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (44, 1, 'DEMONSTRATIVO PAGAMENTO', 'F', 90, false, NULL, 'DEMONSTRATIVO_PAGAMENTO', true, false, true, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (43, 1, 'DOCUMENTO DE CONCESSIONARIA', 'A', 90, false, NULL, 'DOCUMENTO_CONCESSIONARIA', true, false, true, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (42, 1, 'CARTÃO DE ASSINATURA', 'A', NULL, false, NULL, 'CARTAO_ASSINATURA', true, false, true, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (36, 1, 'CONSULTA_CADASTRAL', 'F', 2, false, NULL, 'CONSULTA_CADASTRAL', true, false, true, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (7, 1, 'DOCUMENTO DE IDENTIDADE', 'F', 3650, false, '0001000100020005', 'CARTEIRA_IDENTIDADE', true, false, true, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (8, 1, 'CPF', 'F', NULL, false, '13', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (30, 1, 'FATURA CARTÃO DE CRÉDITO', 'A', NULL, true, NULL, 'DOCUMENTO_CONCESSIONARIA', true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (28, 1, 'CONTA DE ÁGUA', 'A', 90, false, NULL, 'DOCUMENTO_CONCESSIONARIA', true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (29, 1, 'CONTA DE ENERGIA', 'A', 90, false, NULL, 'DOCUMENTO_CONCESSIONARIA', true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (31, 1, 'MO33045 - DADOS COMPLEMENTARES PESSOA JURÍDICA', 'J', NULL, true, '46', NULL, true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (32, 1, 'CONTRACHEQUE', 'F', 90, false, '0001000100030009', 'DEMONSTRATIVO_PAGAMENTO', true, false, false, true, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (152, 1, 'ADITIVO CONTRATUAL', 'A', NULL, false, NULL, 'ADITIVO_CONTRATUAL', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (153, 1, 'ATA', 'A', NULL, false, NULL, 'ATA', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (154, 1, 'ATA DE REGISTO DE PREÇO', 'A', NULL, false, NULL, 'ATA_REGISTRO_PRECO', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (155, 1, 'ATESTE', 'A', NULL, false, NULL, 'ATESTE', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (156, 1, 'COMUNICAÇÃO ELETRÔNICA', 'A', NULL, false, NULL, 'COMUNICACAO_ELETRONICA', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (157, 1, 'COMUNICAÇÃO INTERNA', 'A', NULL, false, NULL, 'COMUNICACAO_INTERNA', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (158, 1, 'CONTRATO', 'A', NULL, false, NULL, 'CONTRATO', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (159, 1, 'DECLARAÇÃO DE EXECUÇÃO CONTRATUAL', 'A', NULL, false, NULL, 'DEC', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (160, 1, 'DIARIO OFICIAL DA UNIÃO', 'A', NULL, false, NULL, 'DIARIO_OFICIAL_UNIAO', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (161, 1, 'DOCUMENTOS DIVERSOS', 'A', NULL, false, NULL, 'ANEXOS', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (162, 1, 'EDITAL', 'A', NULL, false, NULL, 'EDITAL', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (163, 1, 'GARANTIA', 'A', NULL, false, NULL, 'GARANTIA', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (164, 1, 'NOTA FISCAL', 'A', NULL, false, NULL, 'NOTA_FISCAL', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (165, 1, 'NOTA JURIDICA', 'A', NULL, false, NULL, 'NOTA_JURIDICA', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (166, 1, 'NOTA TECNICA', 'A', NULL, false, NULL, 'NOTA_TECNICA', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (167, 1, 'OFICIO', 'A', NULL, false, NULL, 'OFICIO', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (168, 1, 'ORDEM DE FORNECIMENTO', 'A', NULL, false, NULL, 'ORDEM_DE_FORNECIMENTO', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (169, 1, 'PARECER', 'A', NULL, false, NULL, 'PARECER', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (170, 1, 'PLANILHA DE CUSTOS', 'A', NULL, false, NULL, 'PLANILHA_DE_CUSTOS', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (171, 1, 'PROCURACAO', 'A', NULL, false, NULL, 'PROCURACAO', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (172, 1, 'RECURSO', 'A', NULL, false, NULL, 'RECURSO', false, true, false, false, NULL, NULL, false, false);
INSERT INTO mtrtb009_tipo_documento (nu_tipo_documento, nu_versao, no_tipo_documento, ic_tipo_pessoa, pz_validade_dias, ic_validade_auto_contida, co_tipologia, no_classe_ged, ic_reuso, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, no_arquivo_minuta, de_tags, ic_validacao_cadastral, ic_validacao_documental) VALUES (9, 1, 'CNH', 'F', 3650, false, '0001000100020007', 'CNH', true, false, true, true, NULL, NULL, false, false);


--
-- TOC entry 7520 (class 0 OID 2025583)
-- Dependencies: 1835
-- Data for Name: mtrtb010_funcao_documental; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb010_funcao_documental (nu_funcao_documental, nu_versao, no_funcao) VALUES (1, 1, 'DOCUMENTO DE IDENTIFICAÇÃO');
INSERT INTO mtrtb010_funcao_documental (nu_funcao_documental, nu_versao, no_funcao) VALUES (3, 1, 'COMPROVANTE DE RENDA');
INSERT INTO mtrtb010_funcao_documental (nu_funcao_documental, nu_versao, no_funcao) VALUES (4, 1, 'DOCUMENTO DE CONSTITUIÇÃO DA EMPRESA');
INSERT INTO mtrtb010_funcao_documental (nu_funcao_documental, nu_versao, no_funcao) VALUES (5, 1, 'COMPROVANTE DE POSSE DO IMÓVEL');
INSERT INTO mtrtb010_funcao_documental (nu_funcao_documental, nu_versao, no_funcao) VALUES (6, 1, 'PROCURAÇÃO');
INSERT INTO mtrtb010_funcao_documental (nu_funcao_documental, nu_versao, no_funcao) VALUES (7, 1, 'DECLARACAO ATIVIDADE FISCAL PJ');
INSERT INTO mtrtb010_funcao_documental (nu_funcao_documental, nu_versao, no_funcao) VALUES (2, 1, 'COMPROVANTE DE ENDEREÇO');


--
-- TOC entry 7579 (class 0 OID 2025756)
-- Dependencies: 1894
-- Data for Name: mtrtb011_funcao_documento; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb011_funcao_documento (nu_tipo_documento, nu_funcao_documental) VALUES (1, 4);
INSERT INTO mtrtb011_funcao_documento (nu_tipo_documento, nu_funcao_documental) VALUES (7, 1);
INSERT INTO mtrtb011_funcao_documento (nu_tipo_documento, nu_funcao_documental) VALUES (9, 1);
INSERT INTO mtrtb011_funcao_documento (nu_tipo_documento, nu_funcao_documental) VALUES (28, 2);
INSERT INTO mtrtb011_funcao_documento (nu_tipo_documento, nu_funcao_documental) VALUES (29, 2);
INSERT INTO mtrtb011_funcao_documento (nu_tipo_documento, nu_funcao_documental) VALUES (30, 2);
INSERT INTO mtrtb011_funcao_documento (nu_tipo_documento, nu_funcao_documental) VALUES (32, 3);
INSERT INTO mtrtb011_funcao_documento (nu_tipo_documento, nu_funcao_documental) VALUES (2, 7);
INSERT INTO mtrtb011_funcao_documento (nu_tipo_documento, nu_funcao_documental) VALUES (6, 7);
INSERT INTO mtrtb011_funcao_documento (nu_tipo_documento, nu_funcao_documental) VALUES (43, 2);
INSERT INTO mtrtb011_funcao_documento (nu_tipo_documento, nu_funcao_documental) VALUES (44, 3);


--
-- TOC entry 7522 (class 0 OID 2025588)
-- Dependencies: 1837
-- Data for Name: mtrtb012_tipo_situacao_dossie; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb012_tipo_situacao_dossie (nu_tipo_situacao_dossie, nu_versao, no_tipo_situacao, ic_resumo, ic_fila_tratamento, ic_produtividade, ic_tipo_inicial, ic_tipo_final) VALUES (1, 1, 'Rascunho', true, false, true, true, false);
INSERT INTO mtrtb012_tipo_situacao_dossie (nu_tipo_situacao_dossie, nu_versao, no_tipo_situacao, ic_resumo, ic_fila_tratamento, ic_produtividade, ic_tipo_inicial, ic_tipo_final) VALUES (2, 1, 'Criado', true, false, true, false, false);
INSERT INTO mtrtb012_tipo_situacao_dossie (nu_tipo_situacao_dossie, nu_versao, no_tipo_situacao, ic_resumo, ic_fila_tratamento, ic_produtividade, ic_tipo_inicial, ic_tipo_final) VALUES (3, 1, 'Aguardando Tratamento', true, true, true, false, false);
INSERT INTO mtrtb012_tipo_situacao_dossie (nu_tipo_situacao_dossie, nu_versao, no_tipo_situacao, ic_resumo, ic_fila_tratamento, ic_produtividade, ic_tipo_inicial, ic_tipo_final) VALUES (4, 1, 'Em Tratamento', true, false, true, false, false);
INSERT INTO mtrtb012_tipo_situacao_dossie (nu_tipo_situacao_dossie, nu_versao, no_tipo_situacao, ic_resumo, ic_fila_tratamento, ic_produtividade, ic_tipo_inicial, ic_tipo_final) VALUES (5, 1, 'Conforme', true, false, true, false, false);
INSERT INTO mtrtb012_tipo_situacao_dossie (nu_tipo_situacao_dossie, nu_versao, no_tipo_situacao, ic_resumo, ic_fila_tratamento, ic_produtividade, ic_tipo_inicial, ic_tipo_final) VALUES (6, 1, 'Análise Segurança', true, false, true, false, false);
INSERT INTO mtrtb012_tipo_situacao_dossie (nu_tipo_situacao_dossie, nu_versao, no_tipo_situacao, ic_resumo, ic_fila_tratamento, ic_produtividade, ic_tipo_inicial, ic_tipo_final) VALUES (7, 1, 'Pendente Segurança', true, false, true, false, false);
INSERT INTO mtrtb012_tipo_situacao_dossie (nu_tipo_situacao_dossie, nu_versao, no_tipo_situacao, ic_resumo, ic_fila_tratamento, ic_produtividade, ic_tipo_inicial, ic_tipo_final) VALUES (8, 1, 'Segurança Finalizado', true, false, true, false, false);
INSERT INTO mtrtb012_tipo_situacao_dossie (nu_tipo_situacao_dossie, nu_versao, no_tipo_situacao, ic_resumo, ic_fila_tratamento, ic_produtividade, ic_tipo_inicial, ic_tipo_final) VALUES (9, 1, 'Finalizado Conforme', true, false, true, false, true);
INSERT INTO mtrtb012_tipo_situacao_dossie (nu_tipo_situacao_dossie, nu_versao, no_tipo_situacao, ic_resumo, ic_fila_tratamento, ic_produtividade, ic_tipo_inicial, ic_tipo_final) VALUES (10, 1, 'Finalizado Inconforme', true, false, true, false, true);
INSERT INTO mtrtb012_tipo_situacao_dossie (nu_tipo_situacao_dossie, nu_versao, no_tipo_situacao, ic_resumo, ic_fila_tratamento, ic_produtividade, ic_tipo_inicial, ic_tipo_final) VALUES (11, 1, 'Cancelado', true, false, true, false, true);
INSERT INTO mtrtb012_tipo_situacao_dossie (nu_tipo_situacao_dossie, nu_versao, no_tipo_situacao, ic_resumo, ic_fila_tratamento, ic_produtividade, ic_tipo_inicial, ic_tipo_final) VALUES (12, 1, 'Aguardando Alimentação', true, false, true, false, false);
INSERT INTO mtrtb012_tipo_situacao_dossie (nu_tipo_situacao_dossie, nu_versao, no_tipo_situacao, ic_resumo, ic_fila_tratamento, ic_produtividade, ic_tipo_inicial, ic_tipo_final) VALUES (13, 1, 'Em Alimentação', true, false, true, false, false);
INSERT INTO mtrtb012_tipo_situacao_dossie (nu_tipo_situacao_dossie, nu_versao, no_tipo_situacao, ic_resumo, ic_fila_tratamento, ic_produtividade, ic_tipo_inicial, ic_tipo_final) VALUES (14, 1, 'Pendente Informação', true, false, true, false, false);
INSERT INTO mtrtb012_tipo_situacao_dossie (nu_tipo_situacao_dossie, nu_versao, no_tipo_situacao, ic_resumo, ic_fila_tratamento, ic_produtividade, ic_tipo_inicial, ic_tipo_final) VALUES (15, 1, 'Alimentação Finalizada', true, false, true, false, false);
INSERT INTO mtrtb012_tipo_situacao_dossie (nu_tipo_situacao_dossie, nu_versao, no_tipo_situacao, ic_resumo, ic_fila_tratamento, ic_produtividade, ic_tipo_inicial, ic_tipo_final) VALUES (16, 1, 'Finalizado', true, false, true, false, true);


--
-- TOC entry 7524 (class 0 OID 2025595)
-- Dependencies: 1839
-- Data for Name: mtrtb013_situacao_dossie; Type: TABLE DATA; Schema: mtr; Owner: -
--



--
-- TOC entry 7526 (class 0 OID 2025603)
-- Dependencies: 1841
-- Data for Name: mtrtb014_instancia_documento; Type: TABLE DATA; Schema: mtr; Owner: -
--



--
-- TOC entry 7528 (class 0 OID 2025608)
-- Dependencies: 1843
-- Data for Name: mtrtb015_situacao_documento; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb015_situacao_documento (nu_situacao_documento, nu_versao, no_situacao, ic_situacao_inicial, ic_situacao_final) VALUES (1, 1, 'Criado', true, false);
INSERT INTO mtrtb015_situacao_documento (nu_situacao_documento, nu_versao, no_situacao, ic_situacao_inicial, ic_situacao_final) VALUES (2, 1, 'Rejeitado', false, true);
INSERT INTO mtrtb015_situacao_documento (nu_situacao_documento, nu_versao, no_situacao, ic_situacao_inicial, ic_situacao_final) VALUES (4, 1, 'Rejeitado por Fraude', false, true);
INSERT INTO mtrtb015_situacao_documento (nu_situacao_documento, nu_versao, no_situacao, ic_situacao_inicial, ic_situacao_final) VALUES (5, 1, 'Conforme', false, true);
INSERT INTO mtrtb015_situacao_documento (nu_situacao_documento, nu_versao, no_situacao, ic_situacao_inicial, ic_situacao_final) VALUES (3, 1, 'Suspeita de Fraude', false, false);
INSERT INTO mtrtb015_situacao_documento (nu_situacao_documento, nu_versao, no_situacao, ic_situacao_inicial, ic_situacao_final) VALUES (6, 1, 'Substituido', false, true);


--
-- TOC entry 7530 (class 0 OID 2025615)
-- Dependencies: 1845
-- Data for Name: mtrtb016_motivo_stco_documento; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb016_motivo_stco_documento (nu_motivo_stco_documento, nu_versao, nu_situacao_documento, no_motivo_stco_documento) VALUES (1, 1, 2, 'Obstrução da imagem');
INSERT INTO mtrtb016_motivo_stco_documento (nu_motivo_stco_documento, nu_versao, nu_situacao_documento, no_motivo_stco_documento) VALUES (2, 1, 2, 'Documento Ilegível');
INSERT INTO mtrtb016_motivo_stco_documento (nu_motivo_stco_documento, nu_versao, nu_situacao_documento, no_motivo_stco_documento) VALUES (4, 1, 2, 'Documento Irregular');
INSERT INTO mtrtb016_motivo_stco_documento (nu_motivo_stco_documento, nu_versao, nu_situacao_documento, no_motivo_stco_documento) VALUES (3, 1, 2, 'Documento fora do Padrão Arquivístico');


--
-- TOC entry 7580 (class 0 OID 2025759)
-- Dependencies: 1895
-- Data for Name: mtrtb017_stco_instnca_documento; Type: TABLE DATA; Schema: mtr; Owner: -
--



--
-- TOC entry 7581 (class 0 OID 2025763)
-- Dependencies: 1896
-- Data for Name: mtrtb018_unidade_tratamento; Type: TABLE DATA; Schema: mtr; Owner: -
--



--
-- TOC entry 7582 (class 0 OID 2025766)
-- Dependencies: 1897
-- Data for Name: mtrtb019_campo_formulario; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb019_campo_formulario (nu_campo_formulario, nu_campo_entrada, nu_ordem, nu_versao, ic_obrigatorio, de_expressao, ic_ativo, no_campo, nu_processo) VALUES (25, 4, 4, 1, false, NULL, true, 'codigo_avaliacao_tomador', 16);
INSERT INTO mtrtb019_campo_formulario (nu_campo_formulario, nu_campo_entrada, nu_ordem, nu_versao, ic_obrigatorio, de_expressao, ic_ativo, no_campo, nu_processo) VALUES (26, 5, 5, 1, false, NULL, true, 'data_vigencia', 16);
INSERT INTO mtrtb019_campo_formulario (nu_campo_formulario, nu_campo_entrada, nu_ordem, nu_versao, ic_obrigatorio, de_expressao, ic_ativo, no_campo, nu_processo) VALUES (1, 1, 1, 1, false, NULL, true, 'empresa_avaliada_CERIS', 18);
INSERT INTO mtrtb019_campo_formulario (nu_campo_formulario, nu_campo_entrada, nu_ordem, nu_versao, ic_obrigatorio, de_expressao, ic_ativo, no_campo, nu_processo) VALUES (2, 2, 2, 1, false, NULL, true, 'possui_socios', 18);
INSERT INTO mtrtb019_campo_formulario (nu_campo_formulario, nu_campo_entrada, nu_ordem, nu_versao, ic_obrigatorio, de_expressao, ic_ativo, no_campo, nu_processo) VALUES (3, 3, 3, 1, false, NULL, true, 'empresa_participante_conglomerado', 18);
INSERT INTO mtrtb019_campo_formulario (nu_campo_formulario, nu_campo_entrada, nu_ordem, nu_versao, ic_obrigatorio, de_expressao, ic_ativo, no_campo, nu_processo) VALUES (32, 8, 8, 1, false, NULL, true, 'telefone_fixo', 21);
INSERT INTO mtrtb019_campo_formulario (nu_campo_formulario, nu_campo_entrada, nu_ordem, nu_versao, ic_obrigatorio, de_expressao, ic_ativo, no_campo, nu_processo) VALUES (33, 9, 9, 1, false, NULL, true, 'celular_1', 21);
INSERT INTO mtrtb019_campo_formulario (nu_campo_formulario, nu_campo_entrada, nu_ordem, nu_versao, ic_obrigatorio, de_expressao, ic_ativo, no_campo, nu_processo) VALUES (34, 10, 10, 1, false, NULL, true, 'celular_2', 21);
INSERT INTO mtrtb019_campo_formulario (nu_campo_formulario, nu_campo_entrada, nu_ordem, nu_versao, ic_obrigatorio, de_expressao, ic_ativo, no_campo, nu_processo) VALUES (36, 13, 13, 1, false, NULL, true, 'estado_civil', 22);
INSERT INTO mtrtb019_campo_formulario (nu_campo_formulario, nu_campo_entrada, nu_ordem, nu_versao, ic_obrigatorio, de_expressao, ic_ativo, no_campo, nu_processo) VALUES (35, 12, 12, 1, false, NULL, true, 'email', 22);
INSERT INTO mtrtb019_campo_formulario (nu_campo_formulario, nu_campo_entrada, nu_ordem, nu_versao, ic_obrigatorio, de_expressao, ic_ativo, no_campo, nu_processo) VALUES (29, 1, 1, 1, false, NULL, true, 'empresa_avaliada_CERIS', 10);
INSERT INTO mtrtb019_campo_formulario (nu_campo_formulario, nu_campo_entrada, nu_ordem, nu_versao, ic_obrigatorio, de_expressao, ic_ativo, no_campo, nu_processo) VALUES (27, 2, 2, 1, false, NULL, true, 'possui_socios', 10);
INSERT INTO mtrtb019_campo_formulario (nu_campo_formulario, nu_campo_entrada, nu_ordem, nu_versao, ic_obrigatorio, de_expressao, ic_ativo, no_campo, nu_processo) VALUES (30, 5, 5, 1, false, NULL, true, 'data_vigencia', 10);
INSERT INTO mtrtb019_campo_formulario (nu_campo_formulario, nu_campo_entrada, nu_ordem, nu_versao, ic_obrigatorio, de_expressao, ic_ativo, no_campo, nu_processo) VALUES (31, 4, 4, 1, false, NULL, true, 'codigo_avaliacao_tomador', 10);
INSERT INTO mtrtb019_campo_formulario (nu_campo_formulario, nu_campo_entrada, nu_ordem, nu_versao, ic_obrigatorio, de_expressao, ic_ativo, no_campo, nu_processo) VALUES (28, 1, 1, 1, false, NULL, true, 'possui_socios', 12);


--
-- TOC entry 7534 (class 0 OID 2025624)
-- Dependencies: 1849
-- Data for Name: mtrtb020_processo; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (10, 1, 'AVALIAÇÃO DA OPERAÇÃO', true, NULL, false, false, 'J', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (12, 1, 'CONFORMIDADE CONCESSÃO COMERCIAL', true, NULL, false, false, 'J', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (14, 1, 'CONFORMIDADE GARANTIA REATIVA', true, NULL, false, false, 'J', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (15, 1, 'LIBERAÇÃO DE CRÉDITO', true, NULL, false, false, 'J', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (18, 1, 'AVALIACAO TOMADOR - DADOS', true, NULL, false, false, 'J', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (16, 1, 'AVALIAÇÃO TOMADOR - RESULTADO', true, NULL, false, false, 'J', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (5, 1, 'CONTA CORRENTE', true, NULL, true, false, 'F', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (6, 1, 'FINANCIAMENTO DE VEÍCULOS', true, NULL, true, false, 'F', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (7, 1, 'PAGAMENTO DE LOTERIAS', true, NULL, true, false, 'F', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (8, 1, 'CRÉDITO RURAL', true, NULL, true, false, 'F', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (9, 1, 'AVALIAÇÃO DO TOMADOR', true, NULL, true, false, 'J', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (2, 1, 'PESSOA FÍSICA', true, NULL, false, false, 'F', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (3, 1, 'CONCESSÃO CRÉDITO COMERCIAL', true, NULL, true, false, 'J', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (11, 1, 'VERIFICAÇÃO REGIME DE ALÇADA', true, NULL, false, false, 'J', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (21, 1, 'ATUALIZAÇÃO CADASTRAL', true, NULL, true, false, 'J', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (24, 1, 'CADASTRO SICLI', true, NULL, false, false, 'J', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (22, 1, 'SUBMISSÃO DE DOCUMENTOS', true, NULL, false, false, 'J', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (19, 1, 'DOSSIE DIGITAL', true, NULL, true, true, 'A', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (4, 1, 'CONCESSÃO CRÉDITO HABITACIONAL', true, NULL, true, false, 'F', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (13, 1, 'CONFORMIDADE CONCESSÃO HABITACIONAL', true, NULL, true, false, 'F', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (23, 1, 'DOSSIE DIGITAL - CONTRATACAO', true, NULL, false, true, 'A', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (1, 1, 'PESSOA JURÍDICA', true, 'glyphicon glyphicon-play', false, false, 'J', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (25, 1, 'PROC. GERA DOSSIE PF', true, NULL, true, false, 'F', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (26, 1, 'PROC. GERA DOSSIE PJ', true, NULL, true, false, 'J', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (27, 1, 'PROC. ETAPA PF', true, NULL, false, false, 'F', NULL, NULL);
INSERT INTO mtrtb020_processo (nu_processo, nu_versao, no_processo, ic_ativo, de_avatar, ic_dossie, ic_controla_validade_documento, ic_tipo_pessoa, no_processo_bpm, no_container_bpm) VALUES (28, 1, 'PROC. ETAPA PJ', true, NULL, false, false, 'J', NULL, NULL);


--
-- TOC entry 7536 (class 0 OID 2025633)
-- Dependencies: 1851
-- Data for Name: mtrtb021_unidade_autorizada; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb021_unidade_autorizada (nu_unidade_autorizada, nu_versao, nu_processo, nu_unidade, ic_tipo_tratamento, nu_apenso_adm, nu_contrato_adm, nu_processo_adm) VALUES (1, 1, 3, 7875, '3', NULL, NULL, NULL);
INSERT INTO mtrtb021_unidade_autorizada (nu_unidade_autorizada, nu_versao, nu_processo, nu_unidade, ic_tipo_tratamento, nu_apenso_adm, nu_contrato_adm, nu_processo_adm) VALUES (2, 1, 9, 7875, '3', NULL, NULL, NULL);
INSERT INTO mtrtb021_unidade_autorizada (nu_unidade_autorizada, nu_versao, nu_processo, nu_unidade, ic_tipo_tratamento, nu_apenso_adm, nu_contrato_adm, nu_processo_adm) VALUES (3, 1, 21, 7875, '3', NULL, NULL, NULL);
INSERT INTO mtrtb021_unidade_autorizada (nu_unidade_autorizada, nu_versao, nu_processo, nu_unidade, ic_tipo_tratamento, nu_apenso_adm, nu_contrato_adm, nu_processo_adm) VALUES (4, 1, 6, 7875, '3', NULL, NULL, NULL);
INSERT INTO mtrtb021_unidade_autorizada (nu_unidade_autorizada, nu_versao, nu_processo, nu_unidade, ic_tipo_tratamento, nu_apenso_adm, nu_contrato_adm, nu_processo_adm) VALUES (5, 1, 1, 7875, '11101110', NULL, NULL, NULL);
INSERT INTO mtrtb021_unidade_autorizada (nu_unidade_autorizada, nu_versao, nu_processo, nu_unidade, ic_tipo_tratamento, nu_apenso_adm, nu_contrato_adm, nu_processo_adm) VALUES (6, 1, 2, 5402, '11101111', NULL, NULL, NULL);
INSERT INTO mtrtb021_unidade_autorizada (nu_unidade_autorizada, nu_versao, nu_processo, nu_unidade, ic_tipo_tratamento, nu_apenso_adm, nu_contrato_adm, nu_processo_adm) VALUES (9, 1, 10, 5402, '1', NULL, NULL, NULL);
INSERT INTO mtrtb021_unidade_autorizada (nu_unidade_autorizada, nu_versao, nu_processo, nu_unidade, ic_tipo_tratamento, nu_apenso_adm, nu_contrato_adm, nu_processo_adm) VALUES (11, 1, 1, 5402, '11', NULL, NULL, NULL);
INSERT INTO mtrtb021_unidade_autorizada (nu_unidade_autorizada, nu_versao, nu_processo, nu_unidade, ic_tipo_tratamento, nu_apenso_adm, nu_contrato_adm, nu_processo_adm) VALUES (12, 1, 3, 5402, '111', NULL, NULL, NULL);
INSERT INTO mtrtb021_unidade_autorizada (nu_unidade_autorizada, nu_versao, nu_processo, nu_unidade, ic_tipo_tratamento, nu_apenso_adm, nu_contrato_adm, nu_processo_adm) VALUES (13, 1, 9, 5402, '0010', NULL, NULL, NULL);
INSERT INTO mtrtb021_unidade_autorizada (nu_unidade_autorizada, nu_versao, nu_processo, nu_unidade, ic_tipo_tratamento, nu_apenso_adm, nu_contrato_adm, nu_processo_adm) VALUES (14, 1, 11, 5402, '10101', NULL, NULL, NULL);
INSERT INTO mtrtb021_unidade_autorizada (nu_unidade_autorizada, nu_versao, nu_processo, nu_unidade, ic_tipo_tratamento, nu_apenso_adm, nu_contrato_adm, nu_processo_adm) VALUES (15, 1, 1, 7470, '111', NULL, NULL, NULL);


--
-- TOC entry 7538 (class 0 OID 2025638)
-- Dependencies: 1853
-- Data for Name: mtrtb022_produto; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (1, 1, 7630, 0, 'CRED ESP EMP   PARC   PRE   FORN GDES CO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (2, 1, 7600, 0, 'CRED ESP EMP - PRE - FORN GDES CORP -MPE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (3, 1, 6226, 0, 'CRED RURAL-FGPP AGRIC-BEN, AGRO E CER-PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (4, 1, 6901, 0, 'FINAN RURAL COOPERAT DE CRED-CUST PECUAR ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (5, 1, 6199, 0, 'CRED RURAL CAIXA   PRONAF + ALIM   CC IA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (6, 1, 3145, 0, 'FINANC A EXPORTACAO CAIXA-COMP-PJ PRIVAD ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (7, 1, 841, 0, 'CRED ESP EMP   PARC   PRE   FORN GDES CO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (8, 1, 4758, 0, 'APOIO A PRODUCAO PJ COM ANTECIPACAO-FGTS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (9, 1, 4821, 0, 'MGE-SBPE-FORA SFH POS FIXADA COM.DESL.PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (10, 1, 4783, 0, 'OP. ESTRUTURADAS EM ENERGIA - ST.PUBLICO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (11, 1, 5654, 0, 'AQUISICAO/REFORMA IMOV RESID - SBPE - PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (12, 1, 5656, 0, 'AQUISICAO IP COMERCIAL REABILITACAO-SBPE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (13, 1, 5659, 0, 'AQUISICAO/REFORMA IMOV USO MISTO - SBPE- ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (14, 1, 6168, 0, 'CREDITO RURAL-PRONAMP INVEST PECUARIO PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (15, 1, 6171, 0, 'CREDITO RURAL-PRONAMP INVEST AGRICOLA PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (16, 1, 6175, 0, 'CRED RURAL-COOP CRED-PRONAMP INVEST AGRI ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (17, 1, 6187, 0, 'CRED RURAL - FEPM PECUARIA-COOPERATIVA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (18, 1, 1093, 0, 'CART HIP NOR/NAO ESP IM RES PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (19, 1, 7430, 0, 'NCE/CCE - PJ - SETOR PRIVADO - POS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (20, 1, 6909, 0, 'FINANCIAMENTO RURAL - INVEST PECUARIO PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (21, 1, 606, 0, 'CRED.EMPRESA-POS PARC. ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (22, 1, 611, 0, 'CAIXA GIRO SUS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (23, 1, 2540, 0, 'MICROCREDITO CAIXA - PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (24, 1, 352, 0, 'FINAME PARA ENTES PUBLICOS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (25, 1, 4144, 0, 'FINISA - OPERACOES ESPECIAIS - SETOR PUB ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (26, 1, 100, 0, 'ARRECADACAO PREFEITURAS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (27, 1, 288, 0, 'CX PROG PAG SALAR SIACC ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (28, 1, 288, 1, 'CX PROG PAG SALAR SIACC ESTRATEGIA DE NEG PJ PUBLICA', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (29, 1, 3, 100, 'CONTA CORRENTE PESSOA JURIDICA CONTAS ENCERRADAS', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (30, 1, 100, 900, 'ARRECADACAO PREFEITURAS - Negociação Exclusiva IPTU', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (31, 1, 288, 900, 'CX PROG PAG SALAR SIACC - PJ PUBLICA - Negociação Folha Pagamento', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (32, 1, 361, 0, 'CONSTRUGIRO AQUIS RECEBIVEIS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (33, 1, 601, 0, 'CRED EMPRESA-PRE JURO ANTECIP ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (34, 1, 605, 0, 'CRED. EMPRESA-PRE.PARC. ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (35, 1, 4129, 0, 'HAB CCFGTS ASS ESP ST PRIV ENT CAIXA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (36, 1, 4781, 0, 'CAP GIRO CONSTRUTORAS - CART RECEBIVEIS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (37, 1, 4726, 0, 'CAP GIRO CONSTRUTORAS - CUSTO TOTAL PROD ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (38, 1, 4113, 0, 'HAB  CREDITOS ESPECIAIS ST PRIV CX ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (39, 1, 4112, 0, 'HAB CREDITOS ESPECIAIS ST PRIV CX ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (40, 1, 469, 0, 'PROGRAMA EMERGENCIAL FINANC - BNDES ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (41, 1, 610, 0, 'CAIXA HOSPITAIS PRE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (42, 1, 198, 0, 'CHEQUE ESPECIAL CAIXA - SETOR PUBLICO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (43, 1, 734, 0, 'GIROCAIXA FACIL ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (44, 1, 183, 0, 'GIRO CAIXA INSTANTANEO MULTIP ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (45, 1, 3147, 0, 'FINANC A EXPORT CAIXA-COMP-PJ PUBLICA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (46, 1, 4097, 0, 'FINANC INFRAESTRUTURA ST PUB - FINISA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (47, 1, 4154, 0, 'FINISA INFRAESTRUTURA - SETOR PRIVADO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (48, 1, 417, 0, 'ALIEN IMOV PATRIM COMERC CX-PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (49, 1, 797, 0, 'CARTAO DE CREDITO - PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (50, 1, 717, 0, 'BNDES GIRO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (51, 1, 22, 100, 'DEPOSITO POUPANCA - PJ CONTAS ENCERRADAS', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (52, 1, 4016, 0, 'HAB PRO - MORADIA ST PUBLICO FGTS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (53, 1, 4127, 0, 'FINANC A PROD EMPREST PJ - REC FGTS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (54, 1, 4560, 0, 'CRED ADQ BAMERINDUS SEM FCVS - PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (55, 1, 4258, 0, 'FCP/RODOVIAS - BNDES ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (56, 1, 4397, 0, 'DES PRO-TRANSP-ST PUB-OR FGTS-ENT CAI ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (57, 1, 4559, 0, 'CRED ADQ BANORTE SEM FCVS - PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (58, 1, 4629, 0, 'SANEAMENTO PARA TODOS - ST PUBLICO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (59, 1, 4630, 0, 'SANEAMENTO PARA TODOS - ST. PRIVADO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (60, 1, 4635, 0, 'ECONOMICO - CONTRATO PESSOA JURIDICA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (61, 1, 767, 0, 'CRED ESP EMPRESA-GRANDES CORPOR ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (62, 1, 711, 0, 'BNDES AUT-NIV PAD-EMP GR PORT ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (63, 1, 748, 0, 'FINAME EVENTOS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (64, 1, 763, 0, 'CREDITO ESPECIAL - SETOR PUBLICO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (65, 1, 776, 0, 'CRED ESP   ST PUBLICO - INVEST ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (66, 1, 4681, 0, 'FINAN A PROD MPE SBPE EMPR RESID PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (67, 1, 4682, 0, 'FINANC A PRODUCAO EMPREST PJ - REC SBPE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (68, 1, 4698, 0, 'CRED IMOBILIARIO-PJ-AQUIS IMOV ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (69, 1, 4747, 0, 'IMOVEL COML - PJ - PRODUCAO - REC SBPE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (70, 1, 4759, 0, 'IMOVEL COML - PJ - EMPREEND - REC SBPE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (71, 1, 4785, 0, 'OP. ESTRUT. INFR ESTR- ST.PUBLICO-BNDES ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (72, 1, 4815, 0, 'PEC - MGE - RECURSOS FGTS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (73, 1, 4816, 0, 'PEC - MGE - RECURSOS SBPE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (74, 1, 4817, 0, 'PLANO EMP CONSTRUCAO CIVIL - MGE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (75, 1, 4818, 0, 'PLANO EMPR CONST CIVIL - MGE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (76, 1, 649, 0, 'BCD - MAQ EQUIPAMENTOS-PRE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (77, 1, 716, 0, 'BNDES REFINANCIAMENTO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (78, 1, 4680, 0, 'CAMINHO DA ESCOLA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (79, 1, 4754, 0, 'FDS - PHP MCMV REPASSE EO- ENTIDADES ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (80, 1, 4872, 0, 'FINAN A PROD MPE FGTS EMPR RESID PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (81, 1, 4894, 0, 'FINISA-MODAL 9 N-RES CMN 2827-ST PUB ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (82, 1, 5014, 0, 'FINANC EMPREENDIMENTO VILA PAN ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (83, 1, 5231, 0, 'INFRAESTRUTURA P/ EMPREEND HABITABIT ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (84, 1, 5658, 0, 'AQUISICAO/REFORMA IMOV COMERCIAL-SBPE-PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (85, 1, 6174, 0, 'CRED RURAL-COOP CRED-PRONAMP INVEST PEC ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (86, 1, 6176, 0, 'CREDITO RURAL - FEPM AGRICOLA- PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (87, 1, 6181, 0, 'CRED RURAL - FEE AGRICOLA - COOPERATIVA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (88, 1, 6179, 0, 'CREDITO RURAL - FEE AGRICOLA - PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (89, 1, 6178, 0, 'CRED RURAL - FEPM AGRICOLA- COOPERATIVA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (90, 1, 6182, 0, 'CRED RURAL-ADIANT COOPERADOS - AGRICOLA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (91, 1, 6184, 0, 'CRED RURAL - FEE PECUARIA - COOPERATIVA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (92, 1, 6191, 0, 'CRED RURAL CAIXA PRONAMP CC CUSTEIO AGR ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (93, 1, 6210, 0, 'CRED RURAL CAIXA-INVEST AGRIC-PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (94, 1, 6200, 0, 'CRED RURAL CAIXA PRONAF + ALIM CC IP ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (95, 1, 6183, 0, 'CRED RURAL - ADIANT COOPERADOS-PECUARIA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (96, 1, 6192, 0, 'CRED RURAL CAIXA PRONAMP CC CUSTEIO PEC ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (97, 1, 6186, 0, 'CREDITO RURAL - FEE PECUARIA - PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (98, 1, 6189, 0, 'CREDITO RURAL - FEPM PECUARIA- PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (99, 1, 6196, 0, 'CRED RURAL CAIXA PRONAMP CUST AGRIC PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (100, 1, 6198, 0, 'CRED RURAL CAIXA PRONAMP CUSTEIO PEC PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (101, 1, 6224, 0, 'CRED RURAL-FGPP PEC-BEN, AGRO E CER-PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (102, 1, 6225, 0, 'CREDITO RURAL-FGPP AGRICOLA-COOPERATIVA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (103, 1, 6900, 0, 'FINAN RURAL COOPERAT DE CRED-CUST AGRIC ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (104, 1, 6902, 0, 'FINAN RURAL COOPERAT CRED-INVEST AGRIC ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (105, 1, 6204, 0, 'CRED RURAL CAIXA   PRONAF   IA PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (106, 1, 6221, 0, 'CREDITO RURAL CAIXA PRONAF COOP CRED-CP ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (107, 1, 6223, 0, 'CREDITO RURAL-FGPP PECUARIA-COOPERATIVA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (108, 1, 6172, 0, 'C RURAL-LCA RL-COMERCIALIZACAO AGR-PF ', false, false, false, false, false, false, false, 'F', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (109, 1, 6222, 0, 'C RURAL PRONAF  COOP DE CRED CUSTEIO AGR ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (110, 1, 6194, 0, 'C RURAL-LCA RL-COMERCIALIZACAO PEC-PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (111, 1, 6202, 0, 'CRED RURAL CAIXA   PRONAF + ALIM   CP IP ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (112, 1, 6207, 0, 'CRED RURAL CAIXA-INVEST AGRIC-COOP CRED ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (113, 1, 6208, 0, 'CRED RURAL CAIXA-INVEST AGRIC-COOP PROD ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (114, 1, 6211, 0, 'CRED RURAL CAIXA-INVEST PEC-COOP CRED ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (115, 1, 4898, 0, 'FINANCIAMENTO FUNDO DA MARINHA MERCANTE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (116, 1, 6212, 0, 'CRED RURAL CAIXA-INVEST PEC-COOP PROD ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (117, 1, 6214, 0, 'CRED RURAL CAIXA-INVEST PECUARIO-PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (118, 1, 6215, 0, 'CREDITO RURAL CAIXA PRONAF CUST PEC PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (119, 1, 6217, 0, 'CREDITO RURAL CAIXA PRONAF CUST AGR PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (120, 1, 6220, 0, 'C RURAL - LCA RL - CUSTEIO AGRICOLA PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (121, 1, 6230, 0, 'CREDITO RURAL - CUSTEIO BENEF AGR - COOP ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (122, 1, 6231, 0, 'CREDITO RURAL - CUSTEIO BENEF PEC - COOP ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (123, 1, 6239, 0, 'FINAME AGRICOLA - AGRICULTURA PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (124, 1, 6241, 0, 'FINAME AGRICOLA - PECUARIA PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (125, 1, 6254, 0, 'CR. RURAL - R. LIVRES - INVEST. AGR PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (126, 1, 6906, 0, 'FINAN RURAL COOPERAT PROD-INVEST AGRIC ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (127, 1, 0, 11, 'GARANTIA HABITACIONAL - PJ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (128, 1, 0, 12, 'COMERCIAL GEVOP - PJ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (129, 1, 0, 15, 'AUTORIZ. CADASTRO POSITIVO PJ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (130, 1, 0, 17, 'CANCEL. CADASTRO POSITIVO PJ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (131, 1, 7605, 0, 'CREDITO ESPECIAL EMPRESA - PRE - MPE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (132, 1, 465, 0, 'APLIC FUNGETUR ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (133, 1, 6917, 0, 'C RURAL - LCA RD - CUSTEIO PECUARIO PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (134, 1, 704, 0, 'GIROCAIXA RECURS CAIXA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (135, 1, 1048, 0, 'DESCONTO DE DUPLICATAS SINCE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (136, 1, 6201, 0, 'CRéD RURAL CAIXA   PRONAF + ALIM   CP IA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (137, 1, 6206, 0, 'CRED RURAL CAIXA   PRONAF   IP PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (138, 1, 6251, 0, 'CR. RURAL - R. LIVRES - COMERC. PEC. PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (139, 1, 6252, 0, 'CR. RURAL - REC. LIVRES - CUSTEIO AGR PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (140, 1, 7000, 0, 'FIANCA BANCARIA CAIXA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (141, 1, 7001, 1, 'CREDITO RURAL - PJ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (142, 1, 6253, 0, 'CR. RURAL - REC. LIVRES - CUSTEIO PEC PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (143, 1, 6255, 0, 'CR. RURAL - R. LIVRES - INVEST. PEC PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (144, 1, 6918, 0, 'FINAN AGROIND-INVEST AGROIND OUT FINS PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (145, 1, 6919, 0, 'FINAN AGROIND-INVEST SERVICOS PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (146, 1, 3, 0, 'CONTA CORRENTE PESSOA JURIDICA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (147, 1, 466, 0, 'PROVIAS-PROG.DE INTERVENCOES VIARIAS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (148, 1, 4917, 0, 'OPERACOES ESTRUTURADAS-ST PRIV-BNDES ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (149, 1, 4920, 0, 'APOIO A PRODUCAO PJ COM ANTECIPACAO-SBPE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (150, 1, 4926, 0, 'FINISA OPER ESPECIAIS ST PRIVADO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (151, 1, 558, 0, 'REDESHOP ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (152, 1, 691, 0, 'RENEGOCIACAO - PJ - PRE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (153, 1, 714, 0, 'FINAME NIV ESP-MIC-PEQ-MED EMPRESA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (154, 1, 7615, 0, 'GIRO CAIXA EMPRESARIAL ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (155, 1, 6193, 0, 'C RURAL-LCA RL-COMERCIALIZACAO PEC-PF ', false, false, false, false, false, false, false, 'F', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (156, 1, 5657, 0, 'AQUISICAO DE IMOV COM - REABILITADO - SB ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (157, 1, 4532, 0, 'FCP-OPER ESTRUT ENERGIA - BNDES PRIV ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (158, 1, 792, 0, 'ROTATIVO CARTAO BNDES ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (159, 1, 0, 21, 'LOTERICOS', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (160, 1, 93, 0, 'CHEQUE ORDEM DE PAGAMENTO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (161, 1, 20, 0, 'CHEQUE ADMINISTRATIVO PRONTO PAGAMENTO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (162, 1, 19, 0, 'DEP FIANCA - POLICIA FEDERAL ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (163, 1, 1301, 0, 'FGTS EMP PROD OP ESP - PJ - RESIDENCIAL ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (164, 1, 349, 0, 'BNDES AUTOMATICO/FINEM PARA ENTES PUBLIC ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (165, 1, 0, 20, 'ESTRATÉGIA - PJ PUBLICA', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (166, 1, 22, 0, 'DEPOSITO POUPANCA - PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (167, 1, 5613, 0, 'BNDES CAIXA  CREDITO SIEMP ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (168, 1, 6, 0, 'DEP. ENT. PUBLICAS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (169, 1, 7, 0, 'DEP INSTIT FINANCEIRAS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (170, 1, 9, 0, 'DEP JUD JUSTIC DO TRABALHO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (171, 1, 10, 0, 'DEP SOB CAUCAO ENT PUBLICAS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (172, 1, 5623, 0, 'CAIXA MASTERCARD BNDES ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (173, 1, 194, 0, 'CONTA GARANTIDA CAIXA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (174, 1, 625, 0, 'MICROCREDITO PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (175, 1, 15, 0, 'DEP JUDICIAIS ESP - JUST COMUM ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (176, 1, 779, 0, 'CARTAO DE CREDITO BNDES FIN.ROT.RENEG ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (177, 1, 791, 0, 'PARCELADO CARTAO BNDES ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (178, 1, 606, 19, 'CRED.EMPRESA-POS PARC. PRE CONTRATO', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (179, 1, 41, 0, 'DEP. JUD. JUST.FEDER. ESPEC. ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (180, 1, 7400, 0, 'BCD MAQ EQUIP POS MPE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (181, 1, 1334, 0, 'CRED RURAL - INDUSTRIALIZACAO PECUARIA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (182, 1, 1333, 0, 'CRED RURAL - INDUSTRIALIZACAO AGRICOLA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (183, 1, 4866, 0, 'PROGR CONSTRUIR CAIXA PJ IMOVEL RESIDENC ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (184, 1, 4930, 0, 'CREDITO INVESTIMENTOS CORPORATE -  FDA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (185, 1, 3001, 0, 'ACC-ACE -ADIANTAMENT.CREDITO EXPORTACAO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (186, 1, 4938, 0, 'INFRA - FDCO - SETOR PRIVADO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (187, 1, 6, 1, 'DEP. ENT. PUBLICAS NEG. CONTRAPARTIDAS PJ PUB', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (188, 1, 6915, 0, 'FINANCIAMENTO RURAL - INVEST AGRICOLA PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (189, 1, 777, 0, 'CRED ESP EMP - GDES CORP - INVEST ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (190, 1, 709, 0, 'BNDES EXIM PRE-EMBARQUE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (191, 1, 710, 0, 'BNDES AUT-NIV ESP-MIC-PEQ-MED EMP. ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (192, 1, 715, 0, 'FINAME NIV PAD-EMP GDE PORTE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (193, 1, 731, 0, 'PROGER MICRO PEQ EMPR INV FIXO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (194, 1, 1049, 0, 'DESCONTO DE CHEQUES PRE-DATADOS SINCE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (195, 1, 4929, 0, 'CREDITO INVESTIMENTOS CORPORATE - FDCO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (196, 1, 197, 0, 'CHEQUE EMPRESA CAIXA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (197, 1, 4931, 0, 'CREDITO INVESTIMENTOS CORPORATE - FDNE ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (198, 1, 540, 0, 'ADMINISTRACAO DO FDS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (199, 1, 555, 0, 'EMGEA-CREDITOS ADMINISTRAD. ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (200, 1, 6903, 0, 'FINAN RURAL COOPERAT CRED-INVEST PECUAR ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (201, 1, 6904, 0, 'FINAN RURAL COOPERAT DE PROD-CUST AGRIC ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (202, 1, 6905, 0, 'FINAN RURAL COOPERAT DE PROD-CUST PECUA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (203, 1, 6907, 0, 'FINAN RURAL COOPERAT PROD-INVEST PECUA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (204, 1, 6911, 0, 'FINANCIAMENTO RURAL-CUSTEIO AGRICOLA PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (205, 1, 6913, 0, 'FINANCIAMENTO RURAL-CUSTEIO PECUARIO PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (206, 1, 275, 0, 'CREDITO ESPECIAL EMPRESA   GARANTIA FGO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (207, 1, 279, 0, 'CRED ESP EMP   POS GARANTIA FGO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (208, 1, 277, 0, 'GIROCAIXA REC PIS - GARANTIA FGO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (209, 1, 291, 0, 'GIROCAIXA REC CAIXA   GAR FGO ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (210, 1, 215, 0, 'CRED ESP EMP - PARC - FLUT ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (211, 1, 650, 0, 'BCD - MAQ EQUIPAMENTOS-POS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (212, 1, 653, 0, 'CREDFROTA - FINAN DE VEICULOS PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (213, 1, 690, 0, 'RENEGOCIACAO - PJ - POS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (214, 1, 697, 0, 'PRODUCARD CAIXA - PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (215, 1, 699, 0, 'EMPRESTIMOS CONTROLE MANUAL-PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (216, 1, 702, 0, 'GIROCAIXA - PJ - REC PIS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (217, 1, 193, 0, 'CONSTRUGIRO ANTEC RECEBIVEIS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (218, 1, 6173, 0, 'C RURAL-LCA RL-COMERCIALIZACAO AGR PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (219, 1, 6250, 0, 'CR. RURAL - R. LIVRES - COMERC. AGR. PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (220, 1, 168, 0, 'CAIXA FIC PREVIDENCIA RF 300 ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (221, 1, 5543, 0, 'AQUIS ROYALTIES E COMPENS FINANC GOVERNA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (222, 1, 6289, 0, 'C RURAL - LCA RL - CUSTEIO PECUARIO PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (223, 1, 6291, 0, 'C RURAL LCA RD - CUSTEIO AGRICOLA PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (224, 1, 556, 0, 'BANCO 24 HORAS ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (225, 1, 712, 0, 'PROGR APOIO HOSP FILAN - BNDES ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (226, 1, 1798, 100, 'CERTIFICADO DIGITAL - PJ AC CAIXA PJ SSL', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (227, 1, 1798, 200, 'CERTIFICADO DIGITAL - PJ AC CAIXA JUS SSL', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (228, 1, 1798, 300, 'CERTIFICADO DIGITAL - PJ AC CAIXA SPB', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (229, 1, 1798, 400, 'CERTIFICADO DIGITAL - PJ AC CAIXA TIMESTAMPING', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (230, 1, 1798, 500, 'CERTIFICADO DIGITAL - PJ REVOGAÇÃO PJ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (231, 1, 1336, 0, 'CRED RURAL PRONAF AGROIND AGRICOLA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (232, 1, 5226, 100, 'ORDEM DE PAGAMENTO DO EXTERIOR PESSOA JURÍDICA', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (233, 1, 1798, 0, 'CERTIFICADO DIGITAL - PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (234, 1, 5227, 100, 'ORDEM DE PAGTO PARA O EXTERIOR PESSOA JURÍDICA', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (235, 1, 402, 100, 'DISPONIB. MOEDAS ESTRAN. LIVRE PESSOA JURÍDICA', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (236, 1, 1337, 0, 'CRED RURAL - PRONAF AGROIND CUSTEIO PEC ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (237, 1, 4472, 0, 'DES PRO-TRANSP ST PRIV-OR FGTS ENT CAIXA ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (238, 1, 6291, 100, 'C RURAL LCA RD - CUSTEIO AGRICOLA PJ PRODUTOR PJ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (239, 1, 6291, 101, 'C RURAL LCA RD - CUSTEIO AGRICOLA PJ COOPERATIVA DE PRODUÇÃO', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (240, 1, 6291, 102, 'C RURAL LCA RD - CUSTEIO AGRICOLA PJ AGROINDÚSTRIA', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (241, 1, 6220, 100, 'C RURAL - LCA RL - CUSTEIO AGRICOLA PJ PRODUTOR PJ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (242, 1, 6220, 101, 'C RURAL - LCA RL - CUSTEIO AGRICOLA PJ COOPERATIVA DE PRODUÇÃO', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (243, 1, 6220, 102, 'C RURAL - LCA RL - CUSTEIO AGRICOLA PJ AGROINDUSTRIA', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (244, 1, 0, 24, ' ESTRATÉGIA - PJ PRIVADA', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (245, 1, 543, 1, 'PROGRAMA DE ARREND RESIDENC - PJ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (246, 1, 606, 99, 'CRED.EMPRESA-POS PARC. PRE-CONTRATAÇÃO', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (247, 1, 794, 0, 'ARREC DA CONTRIB SINDICAL URB ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (248, 1, 900, 0, 'ARRECADACAO CONVENIOS DIVERSOS (SICAP/SIGTA) - PJ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (249, 1, 4876, 0, 'FINAN A PROD MPE SBPE EMPR MISTO PJ ', false, false, false, false, false, false, false, 'J', false, true);
INSERT INTO mtrtb022_produto (nu_produto, nu_versao, nu_operacao, nu_modalidade, no_produto, ic_contratacao_conjunta, ic_pesquisa_cadin, ic_pesquisa_scpc, ic_pesquisa_serasa, ic_pesquisa_ccf, ic_pesquisa_sicow, ic_pesquisa_receita, ic_tipo_pessoa, ic_dossie_digital, ic_pesquisa_sinad) VALUES (250, 1, 1, 0, 'Conta Corrente PF', true, true, true, true, true, true, true, 'F', true, true);


--
-- TOC entry 7583 (class 0 OID 2025773)
-- Dependencies: 1898
-- Data for Name: mtrtb023_produto_processo; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb023_produto_processo (nu_processo, nu_produto) VALUES (9, 7);
INSERT INTO mtrtb023_produto_processo (nu_processo, nu_produto) VALUES (3, 1);
INSERT INTO mtrtb023_produto_processo (nu_processo, nu_produto) VALUES (3, 2);
INSERT INTO mtrtb023_produto_processo (nu_processo, nu_produto) VALUES (10, 3);
INSERT INTO mtrtb023_produto_processo (nu_processo, nu_produto) VALUES (10, 7);
INSERT INTO mtrtb023_produto_processo (nu_processo, nu_produto) VALUES (1, 8);
INSERT INTO mtrtb023_produto_processo (nu_processo, nu_produto) VALUES (1, 19);
INSERT INTO mtrtb023_produto_processo (nu_processo, nu_produto) VALUES (3, 3);
INSERT INTO mtrtb023_produto_processo (nu_processo, nu_produto) VALUES (4, 30);


--
-- TOC entry 7540 (class 0 OID 2025649)
-- Dependencies: 1855
-- Data for Name: mtrtb024_produto_dossie; Type: TABLE DATA; Schema: mtr; Owner: -
--



--
-- TOC entry 7542 (class 0 OID 2025655)
-- Dependencies: 1857
-- Data for Name: mtrtb025_processo_documento; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (25, 1, 18, NULL, 1, 'EMPRESA_CONGLOMERADO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (26, 1, 18, 7, NULL, 'EMPRESA_CONGLOMERADO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (27, 1, 18, NULL, 31, 'EMPRESA_CONGLOMERADO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (1, 1, 3, 1, NULL, 'TOMADOR_PRIMEIRO_TITULAR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (3, 1, 3, 3, NULL, 'TOMADOR_PRIMEIRO_TITULAR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (7, 1, 3, 1, NULL, 'AVALISTA', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (9, 1, 3, 5, NULL, 'AVALISTA', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (11, 1, 3, 2, NULL, 'FIADOR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (12, 1, 3, 3, NULL, 'FIADOR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (13, 1, 3, 6, NULL, 'CONJUGE', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (15, 1, 3, 3, NULL, 'CONJUGE', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (18, 1, 3, 2, NULL, 'SOCIO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (21, 1, 3, 2, NULL, 'AVALISTA', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (5, 1, 3, 4, NULL, 'TOMADOR_PRIMEIRO_TITULAR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (8, 1, 3, 3, NULL, 'AVALISTA', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (10, 1, 3, 4, NULL, 'AVALISTA', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (14, 1, 3, 1, NULL, 'CONJUGE', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (16, 1, 3, 1, NULL, 'FIADOR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (17, 1, 3, 6, NULL, 'AVALISTA', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (19, 1, 3, 2, NULL, 'CONJUGE', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (20, 1, 3, 6, NULL, 'FIADOR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (63, 1, 21, 7, NULL, 'SEGUNDO_TITULAR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (56, 1, 21, 1, NULL, 'CONJUGE_SOCIO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (57, 1, 21, 2, NULL, 'CONJUGE_SOCIO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (58, 1, 21, 3, NULL, 'CONJUGE_SOCIO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (2, 1, 3, NULL, 30, 'TOMADOR_PRIMEIRO_TITULAR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (64, 1, 21, 7, NULL, 'TOMADOR_PRIMEIRO_TITULAR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (28, 1, 18, NULL, 33, 'SOCIO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (29, 1, 18, NULL, 34, 'SOCIO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (31, 1, 18, NULL, 8, 'SOCIO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (23, 1, 18, NULL, 2, 'TOMADOR_PRIMEIRO_TITULAR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (24, 1, 18, NULL, 31, 'TOMADOR_PRIMEIRO_TITULAR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (30, 1, 18, 1, NULL, 'SOCIO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (22, 1, 9, 4, NULL, 'TOMADOR_PRIMEIRO_TITULAR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (32, 1, 21, 1, NULL, 'FIADOR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (33, 1, 21, 1, NULL, 'AVALISTA', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (34, 1, 21, 2, NULL, 'AVALISTA', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (35, 1, 21, 3, NULL, 'AVALISTA', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (36, 1, 21, 1, NULL, 'CONJUGE', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (37, 1, 21, 2, NULL, 'CONJUGE', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (38, 1, 21, 3, NULL, 'CONJUGE', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (39, 1, 21, 2, NULL, 'EMPRESA_CONGLOMERADO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (40, 1, 21, 4, NULL, 'EMPRESA_CONGLOMERADO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (41, 1, 21, 7, NULL, 'EMPRESA_CONGLOMERADO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (42, 1, 21, 1, NULL, 'FIADOR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (43, 1, 21, 2, NULL, 'FIADOR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (44, 1, 21, 3, NULL, 'FIADOR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (45, 1, 21, 1, NULL, 'SOCIO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (46, 1, 21, 2, NULL, 'SOCIO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (47, 1, 21, 3, NULL, 'SOCIO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (48, 1, 21, 1, NULL, 'TOMADOR_PRIMEIRO_TITULAR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (49, 1, 21, 2, NULL, 'TOMADOR_PRIMEIRO_TITULAR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (50, 1, 21, 3, NULL, 'TOMADOR_PRIMEIRO_TITULAR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (51, 1, 21, 4, NULL, 'TOMADOR_PRIMEIRO_TITULAR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (52, 1, 21, 7, NULL, 'TOMADOR_PRIMEIRO_TITULAR ', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (53, 1, 21, 2, NULL, 'CONGLOMERADO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (54, 1, 21, 4, NULL, 'CONGLOMERADO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (55, 1, 21, 7, NULL, 'CONGLOMERADO', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (59, 1, 21, 1, NULL, 'SEGUNDO_TITULAR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (60, 1, 21, 2, NULL, 'SEGUNDO_TITULAR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (61, 1, 21, 3, NULL, 'SEGUNDO_TITULAR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (62, 1, 21, 4, NULL, 'SEGUNDO_TITULAR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (4, 1, 3, 1, NULL, 'SOCIO', true, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (6, 1, 3, 3, NULL, 'SOCIO', true, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (68, 1, 4, 1, NULL, 'TOMADOR_PRIMEIRO_TITULAR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (70, 1, 4, 3, NULL, 'TOMADOR_PRIMEIRO_TITULAR', false, false);
INSERT INTO mtrtb025_processo_documento (nu_processo_documento, nu_versao, nu_processo, nu_funcao_documental, nu_tipo_documento, ic_tipo_relacionamento, ic_obrigatorio, ic_validar) VALUES (69, 1, 4, 2, NULL, 'TOMADOR_PRIMEIRO_TITULAR', false, false);


--
-- TOC entry 7584 (class 0 OID 2025776)
-- Dependencies: 1899
-- Data for Name: mtrtb026_relacao_processo; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (21, 22, 1, NULL, 1);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (21, 24, 1, NULL, 2);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (2, 4, 1, NULL, NULL);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (2, 5, 1, NULL, NULL);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (2, 6, 1, NULL, NULL);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (2, 8, 1, NULL, NULL);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (2, 13, 1, NULL, NULL);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (9, 18, 1, NULL, 2);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (9, 16, 1, NULL, 1);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (2, 7, 1, NULL, NULL);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (1, 3, 1, NULL, NULL);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (1, 9, 1, NULL, NULL);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (1, 21, 1, NULL, NULL);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (3, 10, 1, NULL, 1);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (3, 11, 1, NULL, 2);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (3, 12, 1, NULL, 3);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (3, 14, 1, NULL, 4);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (3, 15, 1, NULL, 5);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (19, 23, 1, NULL, 1);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (2, 25, 1, NULL, NULL);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (1, 26, 1, NULL, NULL);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (26, 28, 1, NULL, 1);
INSERT INTO mtrtb026_relacao_processo (nu_processo_pai, nu_processo_filho, nu_versao, nu_prioridade, nu_ordem) VALUES (25, 27, 1, NULL, 1);


--
-- TOC entry 7544 (class 0 OID 2025660)
-- Dependencies: 1859
-- Data for Name: mtrtb027_campo_entrada; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb027_campo_entrada (nu_campo_entrada, nu_versao, ic_tipo, ic_chave, no_label, de_mascara, de_placeholder, nu_tamanho_minimo, nu_tamanho_maximo) VALUES (1, 1, 'INPUT_RADIO', false, 'Empresa avaliada pela CERIS?', NULL, NULL, NULL, NULL);
INSERT INTO mtrtb027_campo_entrada (nu_campo_entrada, nu_versao, ic_tipo, ic_chave, no_label, de_mascara, de_placeholder, nu_tamanho_minimo, nu_tamanho_maximo) VALUES (2, 1, 'INPUT_RADIO', false, 'Possui sócios?', NULL, NULL, NULL, NULL);
INSERT INTO mtrtb027_campo_entrada (nu_campo_entrada, nu_versao, ic_tipo, ic_chave, no_label, de_mascara, de_placeholder, nu_tamanho_minimo, nu_tamanho_maximo) VALUES (3, 1, 'INPUT_RADIO', true, 'Empresa participante de conglomerado?', NULL, NULL, NULL, NULL);
INSERT INTO mtrtb027_campo_entrada (nu_campo_entrada, nu_versao, ic_tipo, ic_chave, no_label, de_mascara, de_placeholder, nu_tamanho_minimo, nu_tamanho_maximo) VALUES (5, 1, 'DATA', true, 'Data Vigência Avaliação', 'dd/mm/aaaa', '99/99/9999', NULL, 10);
INSERT INTO mtrtb027_campo_entrada (nu_campo_entrada, nu_versao, ic_tipo, ic_chave, no_label, de_mascara, de_placeholder, nu_tamanho_minimo, nu_tamanho_maximo) VALUES (4, 1, 'INPUT', true, 'Código Avaliação Tomador', NULL, NULL, NULL, 10);
INSERT INTO mtrtb027_campo_entrada (nu_campo_entrada, nu_versao, ic_tipo, ic_chave, no_label, de_mascara, de_placeholder, nu_tamanho_minimo, nu_tamanho_maximo) VALUES (6, 1, 'INPUT', false, 'Campo não ligado a ninguem', '', NULL, NULL, NULL);
INSERT INTO mtrtb027_campo_entrada (nu_campo_entrada, nu_versao, ic_tipo, ic_chave, no_label, de_mascara, de_placeholder, nu_tamanho_minimo, nu_tamanho_maximo) VALUES (8, 1, 'INPUT', false, 'Telefone Fixo', '999 - 9999 - 9999', NULL, NULL, 11);
INSERT INTO mtrtb027_campo_entrada (nu_campo_entrada, nu_versao, ic_tipo, ic_chave, no_label, de_mascara, de_placeholder, nu_tamanho_minimo, nu_tamanho_maximo) VALUES (9, 1, 'INPUT', false, 'Telefone Celular 1', '999 - 9 9999 - 9999', NULL, NULL, 12);
INSERT INTO mtrtb027_campo_entrada (nu_campo_entrada, nu_versao, ic_tipo, ic_chave, no_label, de_mascara, de_placeholder, nu_tamanho_minimo, nu_tamanho_maximo) VALUES (10, 1, 'INPUT', false, 'Telefone Celular 2', '999 - 9 9999 - 9999', NULL, NULL, 12);
INSERT INTO mtrtb027_campo_entrada (nu_campo_entrada, nu_versao, ic_tipo, ic_chave, no_label, de_mascara, de_placeholder, nu_tamanho_minimo, nu_tamanho_maximo) VALUES (12, 1, 'INPUT', false, 'E-mail', NULL, NULL, NULL, NULL);
INSERT INTO mtrtb027_campo_entrada (nu_campo_entrada, nu_versao, ic_tipo, ic_chave, no_label, de_mascara, de_placeholder, nu_tamanho_minimo, nu_tamanho_maximo) VALUES (13, 1, 'SELECT', false, 'Estado Civil', NULL, NULL, NULL, NULL);


--
-- TOC entry 7546 (class 0 OID 2025665)
-- Dependencies: 1861
-- Data for Name: mtrtb028_opcao_campo; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb028_opcao_campo (nu_opcao_campo, nu_versao, nu_campo_entrada, no_value, no_opcao, ic_ativo) VALUES (1, 1, 1, '1', 'SIM', true);
INSERT INTO mtrtb028_opcao_campo (nu_opcao_campo, nu_versao, nu_campo_entrada, no_value, no_opcao, ic_ativo) VALUES (2, 1, 1, '2', 'NÃO', true);
INSERT INTO mtrtb028_opcao_campo (nu_opcao_campo, nu_versao, nu_campo_entrada, no_value, no_opcao, ic_ativo) VALUES (3, 1, 2, '1', 'SIM', true);
INSERT INTO mtrtb028_opcao_campo (nu_opcao_campo, nu_versao, nu_campo_entrada, no_value, no_opcao, ic_ativo) VALUES (4, 1, 2, '2', 'NÃO', true);
INSERT INTO mtrtb028_opcao_campo (nu_opcao_campo, nu_versao, nu_campo_entrada, no_value, no_opcao, ic_ativo) VALUES (5, 1, 3, '1', 'SIM', true);
INSERT INTO mtrtb028_opcao_campo (nu_opcao_campo, nu_versao, nu_campo_entrada, no_value, no_opcao, ic_ativo) VALUES (6, 1, 3, '2', 'NÃO', true);
INSERT INTO mtrtb028_opcao_campo (nu_opcao_campo, nu_versao, nu_campo_entrada, no_value, no_opcao, ic_ativo) VALUES (13, 1, 13, '2', 'SOLTEIRO', true);
INSERT INTO mtrtb028_opcao_campo (nu_opcao_campo, nu_versao, nu_campo_entrada, no_value, no_opcao, ic_ativo) VALUES (14, 1, 13, '3', 'CASADO', true);
INSERT INTO mtrtb028_opcao_campo (nu_opcao_campo, nu_versao, nu_campo_entrada, no_value, no_opcao, ic_ativo) VALUES (16, 1, 13, '4', 'DIVORCIADO', true);
INSERT INTO mtrtb028_opcao_campo (nu_opcao_campo, nu_versao, nu_campo_entrada, no_value, no_opcao, ic_ativo) VALUES (9, 1, 13, '1', 'NÃO INFORMADO', true);
INSERT INTO mtrtb028_opcao_campo (nu_opcao_campo, nu_versao, nu_campo_entrada, no_value, no_opcao, ic_ativo) VALUES (17, 1, 13, '5', 'SEPARADO JUDICIALMENTE', true);
INSERT INTO mtrtb028_opcao_campo (nu_opcao_campo, nu_versao, nu_campo_entrada, no_value, no_opcao, ic_ativo) VALUES (18, 1, 13, '5', 'VIÚVO', true);
INSERT INTO mtrtb028_opcao_campo (nu_opcao_campo, nu_versao, nu_campo_entrada, no_value, no_opcao, ic_ativo) VALUES (20, 1, 13, '6', 'UNIÃO ESTÁVEL', true);
INSERT INTO mtrtb028_opcao_campo (nu_opcao_campo, nu_versao, nu_campo_entrada, no_value, no_opcao, ic_ativo) VALUES (21, 1, 13, '7', 'CASADO COMUNHÃO TOTAL BENS', true);
INSERT INTO mtrtb028_opcao_campo (nu_opcao_campo, nu_versao, nu_campo_entrada, no_value, no_opcao, ic_ativo) VALUES (23, 1, 13, '9', 'CASADO COMUNHÃO PARCIAL BENS', true);
INSERT INTO mtrtb028_opcao_campo (nu_opcao_campo, nu_versao, nu_campo_entrada, no_value, no_opcao, ic_ativo) VALUES (22, 1, 13, '8', 'CASADO SEM COMUNHÃO BENS', true);


--
-- TOC entry 7548 (class 0 OID 2025670)
-- Dependencies: 1863
-- Data for Name: mtrtb029_campo_apresentacao; Type: TABLE DATA; Schema: mtr; Owner: -
--



--
-- TOC entry 7550 (class 0 OID 2025675)
-- Dependencies: 1865
-- Data for Name: mtrtb030_resposta_dossie; Type: TABLE DATA; Schema: mtr; Owner: -
--



--
-- TOC entry 7585 (class 0 OID 2025779)
-- Dependencies: 1900
-- Data for Name: mtrtb031_resposta_opcao; Type: TABLE DATA; Schema: mtr; Owner: -
--



--
-- TOC entry 7586 (class 0 OID 2025782)
-- Dependencies: 1901
-- Data for Name: mtrtb032_elemento_conteudo; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb032_elemento_conteudo (nu_elemento_conteudo, nu_versao, nu_elemento_vinculador, nu_produto, nu_processo, nu_tipo_documento, ic_obrigatorio, qt_elemento_obrigatorio, ic_validar, no_campo, de_expressao, no_elemento) VALUES (3, 1, NULL, 21, NULL, 2, true, NULL, false, 'DIRPJ', NULL, NULL);
INSERT INTO mtrtb032_elemento_conteudo (nu_elemento_conteudo, nu_versao, nu_elemento_vinculador, nu_produto, nu_processo, nu_tipo_documento, ic_obrigatorio, qt_elemento_obrigatorio, ic_validar, no_campo, de_expressao, no_elemento) VALUES (1, 1, NULL, NULL, NULL, NULL, true, 1, false, 'folder1', NULL, 'Documentos ABCD');
INSERT INTO mtrtb032_elemento_conteudo (nu_elemento_conteudo, nu_versao, nu_elemento_vinculador, nu_produto, nu_processo, nu_tipo_documento, ic_obrigatorio, qt_elemento_obrigatorio, ic_validar, no_campo, de_expressao, no_elemento) VALUES (4, 2, 1, NULL, 10, 9, true, NULL, false, 'TESTE', NULL, 'DOC CDEF');
INSERT INTO mtrtb032_elemento_conteudo (nu_elemento_conteudo, nu_versao, nu_elemento_vinculador, nu_produto, nu_processo, nu_tipo_documento, ic_obrigatorio, qt_elemento_obrigatorio, ic_validar, no_campo, de_expressao, no_elemento) VALUES (2, 1, 1, 21, NULL, 28, false, NULL, true, 'conta_agua', NULL, NULL);


--
-- TOC entry 7553 (class 0 OID 2025685)
-- Dependencies: 1868
-- Data for Name: mtrtb033_garantia; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (1, 1, 101, 'CESSAO DE DIREITOS CREDITORIOS - DUPLICATAS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (2, 1, 102, 'CESSAO DE DIREITOS CREDITORIOS - CHEQUES', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (3, 1, 103, 'CESSAO DE DIREITOS CREDITORIOS - FATURA CARTAO DE CREDITO', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (4, 1, 104, 'CESSAO DE DIREITOS CREDITORIOS - APLICACOES FINANCEIRAS RENDA FIXA', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (5, 1, 105, 'CESSAO DE DIREITOS CREDITORIOS - APLICACOES FINANCEIRAS RENDA VARIAVEL', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (6, 1, 106, 'CESSAO DE DIREITOS CREDITORIOS - ACOES E DEBENTURES', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (7, 1, 107, 'CESSAO DE DIREITOS CREDITORIOS - TRIBUTOS E RECEITAS ORCAMENTARIAS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (8, 1, 108, 'CESSAO DE DIREITOS CREDITORIOS - DIREITOS SOBRE ALUGUEIS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (9, 1, 199, 'CESSAO DE DIREITOS CREDITORIOS - NOTAS PROMISSORIAS E OUTROS DIREITOS DE CREDITO', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (10, 1, 201, 'CAUCAO - DUPLICATAS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (11, 1, 202, 'CAUCAO - CHEQUES', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (12, 1, 203, 'CAUCAO - FATURA CARTAO DE CREDITO', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (13, 1, 204, 'CAUCAO - APLICACOES FINANCEIRAS RENDA FIXA', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (14, 1, 205, 'CAUCAO - APLICACOES FINANCEIRAS RENDA VARIAVEL', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (15, 1, 206, 'CAUCAO - ACOES E DEBENTURES', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (16, 1, 207, 'CAUCAO - TRIBUTOS E RECEITAS ORCAMENTARIAS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (17, 1, 208, 'CAUCAO - DIREITOS SOBRE ALUGUEIS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (18, 1, 209, 'CAUCAO - DEPOSITO DE TITULOS EMITIDOS POR ENTIDADES ESPECIFICAS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (19, 1, 210, 'CAUCAO - DEPOSITO A VISTA, A PRAZO, POUPANCA, OURO OU TITULOS PUBLICOS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (20, 1, 299, 'CAUCAO - NOTAS PROMISSORIAS E OUTROS DIREITOS DE CREDITO', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (21, 1, 321, 'PENHOR - PRODUTOS AGROPECUARIOS COM WARRANT', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (22, 1, 322, 'PENHOR - PRODUTOS AGROPECUARIOS SEM WARRANT', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (23, 1, 323, 'PENHOR - EQUIPAMENTOS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (24, 1, 324, 'PENHOR - VEICULOS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (25, 1, 325, 'PENHOR - IMOVEIS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (26, 1, 350, 'PENHOR - CIVIL', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (27, 1, 399, 'PENHOR - OUTROS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (28, 1, 423, 'ALIENACAO FIDUCIARIA - EQUIPAMENTOS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (29, 1, 424, 'ALIENACAO FIDUCIARIA - VEICULOS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (30, 1, 426, 'ALIENACAO FIDUCIARIA - IMOVEIS RESIDENCIAIS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (31, 1, 427, 'ALIENACAO FIDUCIARIA - OUTROS IMOVEIS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (32, 1, 499, 'ALIENACAO FIDUCIARIA - OUTROS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (33, 1, 562, 'HIPOTECA - OUTROS GRAUS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (34, 1, 563, 'HIPOTECA - PRIMEIRO GRAUS IMOVEIS RESIDENCIAIS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (46, 1, 886, 'SEGURO E ASSEMELHADO - FUNDO GARANTIDOR / DE AVAL', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (35, 1, 564, 'HIPOTECA - PRIMEIRO GRAUS OUTROS IMOVEIS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (36, 1, 671, 'OPERACAO GARANTIDA PELO GOVERNO FEDERAL', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (37, 1, 672, 'OPERACAO GARANTIDA PELO GOVERNO ESTADUAL OU DISTRITAL', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (38, 1, 673, 'OPERACAO GARANTIDA PELO GOVERNO MUNICIPAL', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (39, 1, 674, 'OPERACAO GARANTIDA POR GARANTIA PRESTADA PELA STN OU BACEN', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (40, 1, 799, 'OUTRA GARANTIA NAO FIDEJUSSORIA - OUTRAS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (41, 1, 881, 'SEGURO E ASSEMELHADO - SEGURO RURAL', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (42, 1, 882, 'SEGURO E ASSEMELHADO - PROAGRO', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (43, 1, 883, 'SEGURO E ASSEMELHADO - SBCE - SOC. BRAS. CRED. EXPORTACAO', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (44, 1, 884, 'SEGURO E ASSEMELHADO - FCVS - FUNDO COMP.VARIAC. SALARIAIS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (45, 1, 885, 'SEGURO E ASSEMELHADO - APOLICE DE CREDITO A EXPORTACAO', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (47, 1, 887, 'SEGURO E ASSEMELHADO - CCR - CONVENIO DE CREDITOS RECIPROCOS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (48, 1, 888, 'SEGURO E ASSEMELHADO - FGPC - FUNDO GAR. PROM. COMPETIVIDADE', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (49, 1, 899, 'SEGURO E ASSEMELHADO - OUTROS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (50, 1, 1001, 'BEM ARRENDADO - VEICULO AUTOMOTOR', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (51, 1, 1002, 'BEM ARRENDADO - OUTROS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (52, 1, 1101, 'GARANTIA INTERNACIONAL - MITIGADORA', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (53, 1, 1102, 'GARANTIA INTERNACIONAL - NAO MITIGADORA', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (54, 1, 1201, 'OPERACAO GARANTIDA POR OUTRA ENTIDADE - EMD - ENT.MULTILAT. DESENVOLVIMENTO', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (55, 1, 1202, 'OPERACAO GARANTIDA POR OUTRA ENTIDADE - FUNDOS OU MECANISMO COBERTURA RISCO CREDITO', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (56, 1, 1203, 'OPERACAO GARANTIDA POR OUTRA ENTIDADE - FGPC - FUNDO GAR. PROM. COMPETIVIDADE', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (57, 1, 1204, 'OPERACAO GARANTIDA POR OUTRA ENTIDADE - FUNDOS ESPECIFICOS', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (58, 1, 1301, 'ACORDO DE COMPENSACAO - ACORDOS NO AMBITO DO SFN - SIST. FIN. NACIONAL', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (65, 1, 8899, 'OUTRAS GARANTIAS FIDEJUSSORIAS', true);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (66, 1, 8803, 'AVAL', true);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (59, 1, 901, 'GARANTIA FIDEJUSSORIA - PESSOA FISICA', true);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (60, 1, 902, 'GARANTIA FIDEJUSSORIA - PESSOA JURIDICA', true);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (61, 1, 903, 'GARANTIA FIDEJUSSORIA - PESSOA FISICA NO EXTERIOR', true);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (62, 1, 904, 'GARANTIA FIDEJUSSORIA - PESSOA JURIDICA NO EXTERIOR', true);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (63, 1, 8801, 'FIANCA SIMPLES', false);
INSERT INTO mtrtb033_garantia (nu_garantia, nu_versao, nu_garantia_bacen, no_garantia, ic_fidejussoria) VALUES (64, 1, 8802, 'FIANCA BANCARIA', false);


--
-- TOC entry 7587 (class 0 OID 2025789)
-- Dependencies: 1902
-- Data for Name: mtrtb034_garantia_produto; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (1, 1);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (1, 24);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (1, 59);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (1, 60);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (1, 61);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (1, 62);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (1, 63);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (1, 64);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (1, 65);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (1, 66);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (23, 1);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (23, 59);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (23, 24);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (23, 60);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (23, 61);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (23, 62);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (23, 63);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (23, 64);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (23, 65);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (23, 66);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (29, 1);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (29, 24);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (29, 59);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (29, 60);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (29, 61);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (29, 62);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (29, 63);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (29, 64);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (29, 65);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (29, 66);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (127, 1);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (127, 24);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (127, 59);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (127, 60);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (127, 61);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (127, 62);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (127, 63);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (127, 64);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (127, 65);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (127, 66);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (146, 1);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (146, 24);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (146, 59);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (146, 60);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (146, 61);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (146, 62);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (146, 63);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (146, 64);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (146, 65);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (146, 66);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (2, 1);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (2, 24);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (2, 59);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (2, 60);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (2, 61);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (2, 62);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (2, 63);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (2, 64);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (2, 65);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (2, 66);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (3, 1);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (3, 24);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (3, 59);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (3, 60);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (3, 61);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (3, 62);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (3, 63);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (3, 64);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (3, 65);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (3, 66);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (7, 1);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (7, 24);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (7, 59);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (7, 60);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (7, 61);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (7, 62);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (7, 63);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (7, 64);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (7, 65);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (7, 66);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (21, 1);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (21, 24);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (21, 59);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (21, 60);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (21, 61);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (21, 62);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (21, 63);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (21, 64);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (21, 65);
INSERT INTO mtrtb034_garantia_produto (nu_produto, nu_garantia) VALUES (21, 66);


--
-- TOC entry 7555 (class 0 OID 2025690)
-- Dependencies: 1870
-- Data for Name: mtrtb035_garantia_informada; Type: TABLE DATA; Schema: mtr; Owner: -
--



--
-- TOC entry 7557 (class 0 OID 2025700)
-- Dependencies: 1872
-- Data for Name: mtrtb036_composicao_documental; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb036_composicao_documental (nu_composicao_documental, nu_versao, no_composicao_documental, ts_inclusao, ts_revogacao, co_matricula_inclusao, co_matricula_revogacao, ic_conclusao_operacao) VALUES (2, 1, 'COMPOSICAO BASICA COM IDENTIFICACAO RENDA E RESIDENCIA', '2018-04-16 13:39:05.17072', NULL, 'c090347', NULL, false);
INSERT INTO mtrtb036_composicao_documental (nu_composicao_documental, nu_versao, no_composicao_documental, ts_inclusao, ts_revogacao, co_matricula_inclusao, co_matricula_revogacao, ic_conclusao_operacao) VALUES (3, 1, 'CONCLUSAO CONTRATACAO CONTA CORRENTE', '2018-07-03 10:40:01.340319', NULL, 'c090347', NULL, true);


--
-- TOC entry 7559 (class 0 OID 2025705)
-- Dependencies: 1874
-- Data for Name: mtrtb037_regra_documental; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb037_regra_documental (nu_regra_documental, nu_versao, nu_composicao_documental, nu_tipo_documento, nu_funcao_documental, nu_canal_captura, ix_antifraude) VALUES (3, 1, 2, NULL, 2, NULL, 0.00000);
INSERT INTO mtrtb037_regra_documental (nu_regra_documental, nu_versao, nu_composicao_documental, nu_tipo_documento, nu_funcao_documental, nu_canal_captura, ix_antifraude) VALUES (4, 1, 2, NULL, 3, NULL, 0.00000);
INSERT INTO mtrtb037_regra_documental (nu_regra_documental, nu_versao, nu_composicao_documental, nu_tipo_documento, nu_funcao_documental, nu_canal_captura, ix_antifraude) VALUES (2, 1, 2, NULL, 1, NULL, 0.00000);
INSERT INTO mtrtb037_regra_documental (nu_regra_documental, nu_versao, nu_composicao_documental, nu_tipo_documento, nu_funcao_documental, nu_canal_captura, ix_antifraude) VALUES (6, 1, 3, 40, NULL, NULL, NULL);
INSERT INTO mtrtb037_regra_documental (nu_regra_documental, nu_versao, nu_composicao_documental, nu_tipo_documento, nu_funcao_documental, nu_canal_captura, ix_antifraude) VALUES (7, 1, 3, 41, NULL, NULL, NULL);
INSERT INTO mtrtb037_regra_documental (nu_regra_documental, nu_versao, nu_composicao_documental, nu_tipo_documento, nu_funcao_documental, nu_canal_captura, ix_antifraude) VALUES (8, 1, 2, 42, NULL, NULL, 0.00000);
INSERT INTO mtrtb037_regra_documental (nu_regra_documental, nu_versao, nu_composicao_documental, nu_tipo_documento, nu_funcao_documental, nu_canal_captura, ix_antifraude) VALUES (9, 1, 2, 35, NULL, NULL, 0.00000);


--
-- TOC entry 7588 (class 0 OID 2025792)
-- Dependencies: 1903
-- Data for Name: mtrtb038_nivel_documental; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb038_nivel_documental (nu_composicao_documental, nu_dossie_cliente) VALUES (2, 356);
INSERT INTO mtrtb038_nivel_documental (nu_composicao_documental, nu_dossie_cliente) VALUES (2, 409);
INSERT INTO mtrtb038_nivel_documental (nu_composicao_documental, nu_dossie_cliente) VALUES (2, 472);
INSERT INTO mtrtb038_nivel_documental (nu_composicao_documental, nu_dossie_cliente) VALUES (2, 491);
INSERT INTO mtrtb038_nivel_documental (nu_composicao_documental, nu_dossie_cliente) VALUES (2, 522);
INSERT INTO mtrtb038_nivel_documental (nu_composicao_documental, nu_dossie_cliente) VALUES (2, 534);


--
-- TOC entry 7589 (class 0 OID 2025795)
-- Dependencies: 1904
-- Data for Name: mtrtb039_produto_composicao; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb039_produto_composicao (nu_composicao_documental, nu_produto) VALUES (2, 250);
INSERT INTO mtrtb039_produto_composicao (nu_composicao_documental, nu_produto) VALUES (3, 250);


--
-- TOC entry 7590 (class 0 OID 2025798)
-- Dependencies: 1905
-- Data for Name: mtrtb040_cadeia_tipo_sto_dossie; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (1, 2);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (1, 11);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (2, 3);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (2, 12);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (3, 4);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (3, 13);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (4, 5);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (4, 7);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (4, 14);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (5, 9);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (5, 12);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (7, 6);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (6, 8);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (8, 3);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (8, 10);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (8, 14);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (12, 13);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (13, 11);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (13, 15);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (13, 12);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (14, 13);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (15, 3);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (15, 16);
INSERT INTO mtrtb040_cadeia_tipo_sto_dossie (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte) VALUES (15, 12);


--
-- TOC entry 7591 (class 0 OID 2025801)
-- Dependencies: 1906
-- Data for Name: mtrtb041_cadeia_stco_documento; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb041_cadeia_stco_documento (nu_situacao_documento_atual, nu_situacao_documento_seguinte) VALUES (1, 2);
INSERT INTO mtrtb041_cadeia_stco_documento (nu_situacao_documento_atual, nu_situacao_documento_seguinte) VALUES (1, 3);
INSERT INTO mtrtb041_cadeia_stco_documento (nu_situacao_documento_atual, nu_situacao_documento_seguinte) VALUES (1, 5);
INSERT INTO mtrtb041_cadeia_stco_documento (nu_situacao_documento_atual, nu_situacao_documento_seguinte) VALUES (3, 2);
INSERT INTO mtrtb041_cadeia_stco_documento (nu_situacao_documento_atual, nu_situacao_documento_seguinte) VALUES (3, 4);
INSERT INTO mtrtb041_cadeia_stco_documento (nu_situacao_documento_atual, nu_situacao_documento_seguinte) VALUES (3, 5);


--
-- TOC entry 7592 (class 0 OID 2025804)
-- Dependencies: 1907
-- Data for Name: mtrtb042_cliente_garantia; Type: TABLE DATA; Schema: mtr; Owner: -
--



--
-- TOC entry 7593 (class 0 OID 2025807)
-- Dependencies: 1908
-- Data for Name: mtrtb043_documento_garantia; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb043_documento_garantia (nu_garantia, nu_tipo_documento, nu_processo, nu_documento_garantia, nu_funcao_documental, nu_versao) VALUES (63, 8, 3, 1, NULL, 1);
INSERT INTO mtrtb043_documento_garantia (nu_garantia, nu_tipo_documento, nu_processo, nu_documento_garantia, nu_funcao_documental, nu_versao) VALUES (66, 8, 3, 2, NULL, 1);
INSERT INTO mtrtb043_documento_garantia (nu_garantia, nu_tipo_documento, nu_processo, nu_documento_garantia, nu_funcao_documental, nu_versao) VALUES (30, 21, 3, 3, NULL, 1);


--
-- TOC entry 7594 (class 0 OID 2025811)
-- Dependencies: 1909
-- Data for Name: mtrtb044_comportamento_pesquisa; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (4, 1, 250, 'SICPF', 'SICPF_0', false, 'CPF Em Situação Regular');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (5, 1, 250, 'SICPF', 'SICPF_1', true, 'CPF Cancelado por Encerramento de Espólio');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (6, 1, 250, 'SICPF', 'SICPF_2', true, 'CPF Suspenso');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (7, 1, 250, 'SICPF', 'SICPF_3', true, 'CPF Cancelado por Óbito sem Espolio ');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (8, 1, 250, 'SICPF', 'SICPF_4', false, 'CPF Pendente de Regularização ');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (9, 1, 250, 'SICPF', 'SICPF_5', true, 'CPF Cancelado por Multiplicidade');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (10, 1, 250, 'SICPF', 'SICPF_8', true, 'CPF Nulo');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (16, 1, 250, 'SICPF', 'SICPF_9', true, 'CPF Cancelado de ofício');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (1, 1, 250, 'SICOW', 'SICOW_5', false, 'Registro pode ser restritivo para contratações comerciais ou habitacionais');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (2, 1, 250, 'SICOW', 'SICOW_7', false, 'Registro pode ser restritivo para contratações comerciais ou habitacionais');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (3, 1, 250, 'SICOW', 'SICOW_2', false, 'Registro pode ser restritivo para contratações comerciais ou habitacionais');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (11, 1, 250, 'SICCF', 'SICCF_2', false, 'Registro pode ser restritivo para contratações comerciais ou habitacionais');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (12, 1, 250, 'SINAD', 'SINAD_2_1', false, 'Registro pode ser restritivo para contratações comerciais ou habitacionais');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (13, 1, 250, 'SERASA', 'SERASA_2', false, 'Registro pode ser restritivo para contratações comerciais ou habitacionais');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (14, 1, 250, 'CADIN', 'CADIN_3', false, 'Registro pode ser restritivo para contratações comerciais ou habitacionais');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (15, 1, 250, 'SINAD', 'SINAD_2_2', false, 'Registro pode ser restritivo para contratações comerciais ou habitacionais');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (17, 1, 250, 'SPC', 'SPC_2', false, 'Registro pode ser restritivo para contratações comerciais ou habitacionais');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (18, 1, 250, 'SICOW', 'SICOW_1P', false, 'Registro pode ser restritivo para contratações comerciais ou habitacionais');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (19, 1, 250, 'SICOW', 'SICOW_1R', false, 'Registro pode ser restritivo para contratações comerciais ou habitacionais');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (20, 1, 250, 'SICOW', 'SICOW_6', false, 'Registro pode ser restritivo para contratações comerciais ou habitacionais');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (21, 1, 250, 'SICOW', 'SICOW_3', false, 'Registro pode ser restritivo para contratações comerciais ou habitacionais');
INSERT INTO mtrtb044_comportamento_pesquisa (nu_comportamento_pesquisa, nu_versao, nu_produto, ic_sistema_retorno, ic_codigo_retorno, ic_bloqueio, de_orientacao) VALUES (22, 1, 250, 'SICOW', 'SICOW_4', false, 'Registro pode ser restritivo para contratações comerciais ou habitacionais');


--
-- TOC entry 7595 (class 0 OID 2025818)
-- Dependencies: 1910
-- Data for Name: mtrtb045_atributo_extracao; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (390, 1, 9, 'Orgão Emissor', 'EMISSOR', true, true, 'EMISSOR', false, NULL, NULL, 'orgao_emissor', 'STRING', true, 'INPUT_TEXT', true, 'descrOrgaoEmissor', 'cnh', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (135, 1, 41, 'Data Debito', NULL, true, false, NULL, false, NULL, NULL, 'data_debito', NULL, NULL, NULL, false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (136, 1, 41, 'Composição', NULL, true, false, NULL, false, NULL, NULL, 'composicao', NULL, NULL, NULL, false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (220, 1, 40, 'Token', NULL, true, false, NULL, false, NULL, NULL, 'token', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (373, 1, 164, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (352, 1, 154, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (190, 1, 40, 'Nome Adicional 2', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_nome_2', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (191, 1, 40, 'CPF Adicional 2', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_cpf_2', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (192, 1, 40, 'Data de Nascimento Adicional 2', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_nascimento_2', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (353, 1, 154, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (194, 1, 40, 'Nome Adicional 3', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_nome_3', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (195, 1, 40, 'CPF Adicional 3', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_cpf_3', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (196, 1, 40, 'Data de Nascimento Adicional 3', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_nascimento_3', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (354, 1, 155, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (198, 1, 40, 'Nome Adicional 4', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_nome_4', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (199, 1, 40, 'CPF Adicional 4', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_cpf_4', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (200, 1, 40, 'Data de Nascimento Adicional 4', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_nascimento_4', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (355, 1, 155, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (202, 1, 40, 'Nome Adicional 5', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_nome_5', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (203, 1, 40, 'CPF Adicional 5', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_cpf_5', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (204, 1, 40, 'Data de Nascimento Adicional 5', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_nascimento_5', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (356, 1, 156, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (206, 1, 40, 'Bloco ADEP', NULL, true, false, NULL, false, NULL, NULL, 'bloco_adep', NULL, false, 'INPUT_CHECKBOX', false, NULL, NULL, NULL, 'BOOLEAN', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (207, 1, 40, 'Bloco DDA', NULL, true, false, NULL, false, NULL, NULL, 'bloco_dda', NULL, false, 'INPUT_CHECKBOX', false, NULL, NULL, NULL, 'BOOLEAN', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (208, 1, 40, 'Bloco Assinatura Eletronica', NULL, true, false, NULL, false, NULL, NULL, 'bloco_assinatura_eletronica', NULL, false, 'INPUT_CHECKBOX', false, NULL, NULL, NULL, 'BOOLEAN', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (209, 1, 40, 'Bloco SMS', NULL, true, false, NULL, false, NULL, NULL, 'bloco_sms', NULL, false, 'INPUT_CHECKBOX', false, NULL, NULL, NULL, 'BOOLEAN', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (210, 1, 40, 'Celular para SMS', NULL, true, false, NULL, false, NULL, NULL, 'sms_celular', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (357, 1, 156, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (358, 1, 157, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (213, 1, 40, 'SMS para FGTS', NULL, true, false, NULL, false, NULL, NULL, 'sms_fgts', NULL, false, 'INPUT_CHECKBOX', false, NULL, NULL, NULL, 'BOOLEAN', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (214, 1, 40, 'SMS para Produtos/Serviços', NULL, true, false, NULL, false, NULL, NULL, 'sms_produtos_servicos', NULL, false, 'INPUT_CHECKBOX', false, NULL, NULL, NULL, 'BOOLEAN', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (215, 1, 40, 'SMS para Cartão de Crédito', NULL, true, false, NULL, false, NULL, NULL, 'sms_cartao_credito', NULL, false, 'INPUT_CHECKBOX', false, NULL, NULL, NULL, 'BOOLEAN', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (216, 1, 40, 'Local de Impressão', NULL, true, false, NULL, false, NULL, NULL, 'local_impressao', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (217, 1, 40, 'Dia da Impressão', NULL, true, false, NULL, false, NULL, NULL, 'dia_impressao', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (218, 1, 40, 'Mês da Impressão', NULL, true, false, NULL, false, NULL, NULL, 'mes_impressao', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (219, 1, 40, 'Ano da Impressão', NULL, true, false, NULL, false, NULL, NULL, 'ano_impressao', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (359, 1, 157, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (49, 1, 35, 'Nome', NULL, true, true, 'NOME', NULL, NULL, NULL, 'nome', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (38, 1, 36, 'Nome', NULL, true, true, 'NOME_CLIENTE', NULL, NULL, NULL, 'nome', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (51, 1, 35, 'Finalidade 1', NULL, true, true, 'FINALIDADE_1', NULL, NULL, NULL, 'finalidade_1', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (55, 1, 35, 'Finalidade 2', NULL, true, true, 'FINALIDADE_2', NULL, NULL, NULL, 'finalidade_2', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (59, 1, 35, 'Finalidade 3', NULL, true, true, 'FINALIDADE_3', NULL, NULL, NULL, 'finalidade_3', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (63, 1, 35, 'Finalidade 4', NULL, true, true, 'FINALIDADE_4', NULL, NULL, NULL, 'finalidade_4', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (167, 1, 40, 'Limite Cheque Especial', NULL, true, false, NULL, false, NULL, NULL, 'limite_cheque_especial', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (169, 1, 40, 'Taxa Efetiva Mensal', NULL, true, false, NULL, false, NULL, NULL, 'taxa_efetiva_mensal', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (170, 1, 40, 'Taxa Efetiva Anual', NULL, true, false, NULL, false, NULL, NULL, 'taxa_efetiva_anual', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (171, 1, 40, 'CET Mensal', NULL, true, false, NULL, false, NULL, NULL, 'cet_mensal', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (45, 1, 42, 'Nome', 'NOME', true, true, 'NOME_CLIENTE', NULL, 'NOME', 'E', 'nome', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (44, 1, 42, 'CPF', 'IDENTIFICADOR_CLIENTE', true, true, 'IDENTIFICADOR_CLIENTE', NULL, 'CPF', 'E', 'cpf', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (137, 1, 41, 'Local Impressão', NULL, true, false, NULL, false, NULL, NULL, 'local_impressao', NULL, NULL, NULL, false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (138, 1, 41, 'Dia Impressão', NULL, true, false, NULL, false, NULL, NULL, 'dia_impressao', NULL, NULL, NULL, false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (139, 1, 41, 'Mês Impressão', NULL, true, false, NULL, false, NULL, NULL, 'mes_impressao', NULL, NULL, NULL, false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (140, 1, 41, 'Ano Impressão', NULL, true, false, NULL, false, NULL, NULL, 'ano_impressao', NULL, NULL, NULL, false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (141, 1, 41, 'Modalidade', NULL, true, false, NULL, false, NULL, NULL, 'modalidade', NULL, NULL, NULL, false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (142, 1, 35, 'Nome Social', NULL, true, false, NULL, false, NULL, NULL, 'nome_social', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (143, 1, 35, 'Tipo de Renda', NULL, true, false, NULL, false, NULL, NULL, 'tipo_renda', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (144, 1, 35, 'Renda', NULL, true, false, NULL, false, NULL, NULL, 'valor_renda', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'DECIMAL', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (145, 1, 35, 'CNPJ da Fonte Pagadora', NULL, true, false, NULL, false, NULL, NULL, 'cnpj_fonte_pagadora', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (146, 1, 35, 'Naturalidade', NULL, true, false, NULL, false, NULL, NULL, 'naturalidade', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (147, 1, 35, 'Endereço Comercial', NULL, true, false, NULL, false, NULL, NULL, 'endereco_comercial', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (148, 1, 35, 'Endereço Residencial', NULL, true, false, NULL, false, NULL, NULL, 'endereco_residencial', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (149, 1, 35, 'Profissão', NULL, true, false, NULL, false, NULL, NULL, 'profissao', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (150, 1, 35, 'Meio de Comunicação 1', NULL, true, false, NULL, false, NULL, NULL, 'meio_comunicacao_1', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (151, 1, 35, 'Meio de Comunicação 2', NULL, true, false, NULL, false, NULL, NULL, 'meio_comunicacao_2', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (152, 1, 35, 'Meio de Comunicação 3', NULL, true, false, NULL, false, NULL, NULL, 'meio_comunicacao_3', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (153, 1, 35, 'Meio de Comunicação 4', NULL, true, false, NULL, false, NULL, NULL, 'meio_comunicacao_4', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (154, 1, 35, 'Meio de Comunicação 5', NULL, true, false, NULL, false, NULL, NULL, 'meio_comunicacao_5', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (155, 1, 35, 'Meio de Comunicação 6', NULL, true, false, NULL, false, NULL, NULL, 'meio_comunicacao_6', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (156, 1, 35, 'Telefone SMS', NULL, true, false, NULL, false, NULL, NULL, 'sms_telefone', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (157, 1, 35, 'Valor de Saque e Transferência', NULL, true, false, NULL, false, NULL, NULL, 'sms_valor_saque_trans', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'DECIMAL', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (158, 1, 35, 'Valor de Compras no Debito', NULL, true, false, NULL, false, NULL, NULL, 'sms_valor_cartao', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'DECIMAL', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (159, 1, 35, 'FGTS', NULL, true, false, NULL, false, NULL, NULL, 'sms_fgts', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (160, 1, 35, 'Produtos e Serviços CAIXA', NULL, true, false, NULL, false, NULL, NULL, 'sms_produtos_servicos', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (161, 1, 35, 'Patrimônio Consolidado Informal', NULL, true, false, NULL, false, NULL, NULL, 'consolidado_informal', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (162, 1, 35, 'Valor Patrimônio', NULL, true, false, NULL, false, NULL, NULL, 'valor_total_patrimonio', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (163, 1, 35, 'Token', NULL, true, false, NULL, false, NULL, NULL, 'token', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (172, 1, 40, 'CET Anual', NULL, true, false, NULL, false, NULL, NULL, 'cet_anual', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (129, 1, 44, 'Emissor', 'EMISSOR', true, true, 'EMISSOR', NULL, NULL, NULL, 'emissor', 'STRING', true, 'INPUT_TEXT', false, 'nomeRazao', 'renda/fontePagadora', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (164, 1, 40, 'Operação', NULL, true, false, NULL, false, NULL, NULL, 'numero_operacao', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (165, 1, 40, 'Tipo de Conta', NULL, true, false, NULL, false, NULL, NULL, 'tipo_conta', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (166, 1, 40, 'Bloco Cheque Especial', NULL, true, false, NULL, false, NULL, NULL, 'bloco_cheque_especial', NULL, false, 'INPUT_CHECKBOX', false, NULL, NULL, NULL, 'BOOLEAN', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (168, 1, 40, 'Dia do Debito', NULL, true, false, NULL, false, NULL, NULL, 'dia_debito', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'LONG', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (348, 1, 152, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (349, 1, 152, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (350, 1, 153, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (175, 1, 40, 'Bloco CDC', NULL, true, false, NULL, false, NULL, NULL, 'bloco_cdc', NULL, false, 'INPUT_CHECKBOX', false, NULL, NULL, NULL, 'BOOLEAN', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (176, 1, 40, 'Bloco Cartão de Crédito', NULL, true, false, NULL, false, NULL, NULL, 'bloco_cartao', NULL, false, 'INPUT_CHECKBOX', false, NULL, NULL, NULL, 'BOOLEAN', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (177, 1, 40, 'Bandeira do Cartão', NULL, true, false, NULL, false, NULL, NULL, 'cartao_bandeira', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (178, 1, 40, 'Variante do Cartão', NULL, true, false, NULL, false, NULL, NULL, 'cartao_variante', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (351, 1, 153, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (180, 1, 40, 'Vencimento do Cartão', NULL, true, false, NULL, false, NULL, NULL, 'cartao_vencimento', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'LONG', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (181, 1, 40, 'Email para Fatura do Cartão', NULL, true, false, NULL, false, NULL, NULL, 'cartao_email', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (182, 1, 40, 'Programa de Pontos do Cartão', NULL, true, false, NULL, false, NULL, NULL, 'cartao_pontos', NULL, false, 'INPUT_CHECKBOX', false, NULL, NULL, NULL, 'BOOLEAN', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (183, 1, 40, 'Seguro do Cartão', NULL, true, false, NULL, false, NULL, NULL, 'cartao_seguro', NULL, false, 'INPUT_CHECKBOX', false, NULL, NULL, NULL, 'BOOLEAN', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (184, 1, 40, 'Avaliação de Crédito do Cartão', NULL, true, false, NULL, false, NULL, NULL, 'cartao_avaliacao', NULL, false, 'INPUT_CHECKBOX', false, NULL, NULL, NULL, 'BOOLEAN', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (185, 1, 40, 'Participação em Campanhas do Cartão', NULL, true, false, NULL, false, NULL, NULL, 'cartao_campanha', NULL, false, 'INPUT_CHECKBOX', false, NULL, NULL, NULL, 'BOOLEAN', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (186, 1, 40, 'Nome Adicional 1', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_nome_1', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (187, 1, 40, 'CPF Adicional 1', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_cpf_1', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (188, 1, 40, 'Data de Nascimento Adicional 1', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_nascimento_1', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (173, 1, 40, 'Garantia (%)', NULL, true, false, NULL, false, NULL, NULL, 'garantia_percentual', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (174, 1, 40, 'Garantia ($)', NULL, true, false, NULL, false, NULL, NULL, 'garantia_valor', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (179, 1, 40, 'Limite do Cartão', NULL, true, false, NULL, false, NULL, NULL, 'cartao_limite', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (189, 1, 40, 'Limite Adicional 1', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_limite_1', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (193, 1, 40, 'Limite Adicional 2', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_limite_2', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (197, 1, 40, 'Limite Adicional 3', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_limite_3', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (201, 1, 40, 'Limite Adicional 4', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_limite_4', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (205, 1, 40, 'Limite Adicional 5', NULL, true, false, NULL, false, NULL, NULL, 'cartao_adicional_limite_5', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (211, 1, 40, 'Valor Saque para SMS', NULL, true, false, NULL, false, NULL, NULL, 'sms_valor_saque', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (212, 1, 40, 'Valor Debito para SMS', NULL, true, false, NULL, false, NULL, NULL, 'sms_valor_debito', NULL, false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (360, 1, 158, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (361, 1, 158, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (362, 1, 159, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (11, 1, 9, 'Data Emissão', 'DATA_EMISSAO', true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', false, 'INPUT_TEXT', false, 'dtEmissao', 'cnh', 'DATE', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (363, 1, 159, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (364, 1, 160, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (365, 1, 160, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (366, 1, 161, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (367, 1, 161, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (368, 1, 162, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (369, 1, 162, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (370, 1, 163, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (371, 1, 163, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (52, 1, 35, 'DDD 1', NULL, true, true, 'DDD_1', NULL, NULL, NULL, 'ddd_1', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (372, 1, 164, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (53, 1, 35, 'Telefone 1', NULL, true, true, 'TELEFONE_1', NULL, NULL, NULL, 'telefone_1', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (57, 1, 35, 'Telefone 2', NULL, true, true, 'TELEFONE_2', NULL, NULL, NULL, 'telefone_2', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (61, 1, 35, 'Telefone 3', NULL, true, true, 'TELEFONE_3', NULL, NULL, NULL, 'telefone_3', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (65, 1, 35, 'Telefone 4', NULL, true, true, 'TELEFONE_4', NULL, NULL, NULL, 'telefone_4', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (54, 1, 35, 'Contato 1', NULL, true, true, 'CONTATO_1', NULL, NULL, NULL, 'contato_1', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (58, 1, 35, 'Contato 2', NULL, true, true, 'CONTATO_2', NULL, NULL, NULL, 'contato_2', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (62, 1, 35, 'Contato 3', NULL, true, true, 'CONTATO_3', NULL, NULL, NULL, 'contato_3', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (66, 1, 35, 'Contato 4', NULL, true, true, 'CONTATO_4', NULL, NULL, NULL, 'contato_4', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (1, 1, 7, 'CPF', NULL, true, true, 'IDENTIFICADOR_CLIENTE', NULL, NULL, NULL, 'cpf', 'STRING', true, 'INPUT_TEXT', false, 'cpf', 'dadosPessoais', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (5, 1, 7, 'UF', NULL, true, true, 'UF_EXPEDICAO', NULL, NULL, NULL, 'uf', 'STRING', false, 'INPUT_TEXT', false, 'uf', 'dadosPessoais', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (36, 1, 35, 'Data Nascimento Conjuge', NULL, true, true, 'DATA_NASCIMENTO_CONJUGE', NULL, NULL, NULL, 'conjuge_nascimento', 'DATE', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (67, 1, 35, 'Email Principal', NULL, true, true, 'EMAIL_1', NULL, NULL, NULL, 'email_principal', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (68, 1, 35, 'Email Secundario', NULL, true, true, 'EMAIL_2', NULL, NULL, NULL, 'email_secundario', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (69, 1, 40, 'Agencia', NULL, true, true, 'NUM_AGENCIA', false, NULL, NULL, 'numero_agencia', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (70, 1, 40, 'Conta', NULL, true, true, 'NUM_CONTA', false, NULL, NULL, 'numero_conta', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (221, 1, 41, 'Token', NULL, true, false, NULL, false, NULL, NULL, 'token', NULL, NULL, NULL, false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (134, 1, 41, 'Bloco Adesão', NULL, true, false, NULL, false, NULL, NULL, 'bloco_adesao', NULL, NULL, NULL, false, NULL, NULL, NULL, 'BOOLEAN', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (374, 1, 165, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (12, 1, 9, 'Data Validade', 'DATA_VALIDADE', true, true, 'DATA_VALIDADE', true, NULL, NULL, 'data_validade', 'DATE', true, 'INPUT_TEXT', false, 'dtValidade', 'cnh', 'DATE', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (14, 1, 9, 'Numero Identidade', 'NUMERO_DOC_IDENTIDADE_EMISSOR _UF', true, true, 'NUMERO_DOC_IDENTIDADE_EMISSOR_UF', false, NULL, NULL, 'numero_identidade', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, NULL, 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (16, 1, 9, 'Data 1a Habilitação', 'DATA_1A_HABILITACAO', true, true, 'DATA_1A_HABILITACAO', false, NULL, NULL, 'data_1a_habilitacao', 'DATE', false, 'INPUT_TEXT', false, 'dtPrimeiraHab', 'cnh', 'DATE', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (7, 1, 7, 'Naturalidade', 'NATURALIDADE', true, true, 'NATURALIDADE', NULL, NULL, NULL, 'naturalidade', 'STRING', false, 'INPUT_TEXT', false, 'nomeMunicipio', 'dadosPessoais', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (375, 1, 165, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (376, 1, 166, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (20, 1, 44, 'Referência', 'DATA_REFERENCIA', true, true, 'DATA_REFERENCIA', true, NULL, NULL, 'referencia', 'DATE', true, 'INPUT_TEXT', false, 'referencia', 'renda', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (377, 1, 166, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (378, 1, 167, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (379, 1, 167, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (380, 1, 168, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (381, 1, 168, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (382, 1, 169, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (383, 1, 169, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (71, 1, 40, 'DV', NULL, true, true, 'NUM_DIGITO_CONTA', false, NULL, NULL, 'numero_dv', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (72, 1, 40, 'Data Abertura', NULL, true, true, 'DATA_ABERTURA_CONTA', false, NULL, NULL, 'data_abertura', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (73, 1, 40, 'Data Encerramento', NULL, true, true, 'DATA_ENCERRAMENTO_CONTA', false, NULL, NULL, 'data_encerramento', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (74, 1, 40, 'Nome Titular 1', NULL, true, true, 'NOME_TITULAR_1', false, NULL, NULL, 'nome_titular_1', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (75, 1, 40, 'Identificador Titular 1', NULL, true, true, 'IDENTIFICADOR_TITULAR_1', false, NULL, NULL, 'identificador_titular_1', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (76, 1, 40, 'Nome Titular 2', NULL, true, true, 'NOME_TITULAR_2', false, NULL, NULL, 'nome_titular_2', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (77, 1, 40, 'Identificador Titular 2', NULL, true, true, 'IDENTIFICADOR_TITULAR_2', false, NULL, NULL, 'identificador_titular_2', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (78, 1, 40, 'Nome Titular 3', NULL, true, true, 'NOME_TITULAR_3', false, NULL, NULL, 'nome_titular_3', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (79, 1, 40, 'Identificador Titular 3', NULL, true, true, 'IDENTIFICADOR_TITULAR_3', false, NULL, NULL, 'identificador_titular_3', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (80, 1, 40, 'Nome Titular 4', NULL, true, true, 'NOME_TITULAR_4', false, NULL, NULL, 'nome_titular_4', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (81, 1, 40, 'Identificador Titular 4', NULL, true, true, 'IDENTIFICADOR_TITULAR_4', false, NULL, NULL, 'identificador_titular_4', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (82, 1, 40, 'Nome Titular 5', NULL, true, true, 'NOME_TITULAR_5', false, NULL, NULL, 'nome_titular_5', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (83, 1, 40, 'Identificador Titular 5', NULL, true, true, 'IDENTIFICADOR_TITULAR_5', false, NULL, NULL, 'identificador_titular_5', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (84, 1, 40, 'Nome Titular 6', NULL, true, true, 'NOME_TITULAR_6', false, NULL, NULL, 'nome_titular_6', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (85, 1, 40, 'Identificador Titular 6', NULL, true, true, 'IDENTIFICADOR_TITULAR_6', false, NULL, NULL, 'identificador_titular_6', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (13, 1, 9, 'Data Nascimento', 'DATA_NASCIMENTO', true, true, 'DATA_NASCIMENTO', false, 'NASCIMENTO', 'E', 'data_nascimento', 'DATE', false, 'INPUT_TEXT', false, 'dtNascimento', 'dadosPessoais', 'DATE', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (27, 1, 35, 'CPF', NULL, true, true, 'IDENTIFICADOR_CLIENTE', NULL, NULL, NULL, 'cpf', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (37, 1, 36, 'CPF', NULL, true, true, 'IDENTIFICADOR_CLIENTE', NULL, NULL, NULL, 'cpf', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (86, 1, 40, 'Nome Titular 7', NULL, true, true, 'NOME_TITULAR_7', false, NULL, NULL, 'nome_titular_7', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (87, 1, 40, 'Identificador Titular 7', NULL, true, true, 'IDENTIFICADOR_TITULAR_7', false, NULL, NULL, 'identificador_titular_7', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (88, 1, 40, 'Nome Titular 8', NULL, true, true, 'NOME_TITULAR_8', false, NULL, NULL, 'nome_titular_8', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (89, 1, 40, 'Identificador Titular 8', NULL, true, true, 'IDENTIFICADOR_TITULAR_8', false, NULL, NULL, 'identificador_titular_8', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (90, 1, 40, 'Nome Titular 9', NULL, true, true, 'NOME_TITULAR_9', false, NULL, NULL, 'nome_titular_9', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (91, 1, 40, 'Identificador Titular 9', NULL, true, true, 'IDENTIFICADOR_TITULAR_9', false, NULL, NULL, 'identificador_titular_9', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (92, 1, 41, 'Agencia', NULL, true, true, 'NUM_AGENCIA', false, NULL, NULL, 'numero_agencia', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (93, 1, 41, 'Conta', NULL, true, true, 'NUM_CONTA', false, NULL, NULL, 'numero_conta', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (94, 1, 41, 'DV', NULL, true, true, 'NUM_DIGITO_CONTA', false, NULL, NULL, 'numero_dv', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (95, 1, 41, 'Data Abertura', NULL, true, true, 'DATA_ABERTURA_CONTA', false, NULL, NULL, 'data_abertura', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (96, 1, 41, 'Data Encerramento', NULL, true, true, 'DATA_ENCERRAMENTO_CONTA', false, NULL, NULL, 'data_encerramento', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (97, 1, 41, 'Nome Titular 1', NULL, true, true, 'NOME_TITULAR_1', false, NULL, NULL, 'nome_titular_1', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (98, 1, 41, 'Identificador Titular 1', NULL, true, true, 'IDENTIFICADOR_TITULAR_1', false, NULL, NULL, 'identificador_titular_1', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (18, 1, 44, 'CPF', NULL, true, true, 'IDENTIFICADOR_CLIENTE', NULL, NULL, NULL, 'cpf', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (99, 1, 41, 'Nome Titular 2', NULL, true, true, 'NOME_TITULAR_2', false, NULL, NULL, 'nome_titular_2', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (100, 1, 41, 'Identificador Titular 2', NULL, true, true, 'IDENTIFICADOR_TITULAR_2', false, NULL, NULL, 'identificador_titular_2', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (126, 1, 43, 'Emissor', 'EMISSOR', true, true, 'EMISSOR', false, NULL, NULL, 'emissor', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (384, 1, 170, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (385, 1, 170, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (386, 1, 171, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (101, 1, 41, 'Nome Titular 3', NULL, true, true, 'NOME_TITULAR_3', false, NULL, NULL, 'nome_titular_3', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (102, 1, 41, 'Identificador Titular 3', NULL, true, true, 'IDENTIFICADOR_TITULAR_3', false, NULL, NULL, 'identificador_titular_3', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (103, 1, 41, 'Nome Titular 4', NULL, true, true, 'NOME_TITULAR_4', false, NULL, NULL, 'nome_titular_4', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (104, 1, 41, 'Identificador Titular 4', NULL, true, true, 'IDENTIFICADOR_TITULAR_4', false, NULL, NULL, 'identificador_titular_4', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (105, 1, 41, 'Nome Titular 5', NULL, true, true, 'NOME_TITULAR_5', false, NULL, NULL, 'nome_titular_5', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (106, 1, 41, 'Identificador Titular 5', NULL, true, true, 'IDENTIFICADOR_TITULAR_5', false, NULL, NULL, 'identificador_titular_5', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (107, 1, 41, 'Nome Titular 6', NULL, true, true, 'NOME_TITULAR_6', false, NULL, NULL, 'nome_titular_6', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (108, 1, 41, 'Identificador Titular 6', NULL, true, true, 'IDENTIFICADOR_TITULAR_6', false, NULL, NULL, 'identificador_titular_6', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (109, 1, 41, 'Nome Titular 7', NULL, true, true, 'NOME_TITULAR_7', false, NULL, NULL, 'nome_titular_7', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (110, 1, 41, 'Identificador Titular 7', NULL, true, true, 'IDENTIFICADOR_TITULAR_7', false, NULL, NULL, 'identificador_titular_7', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (111, 1, 41, 'Nome Titular 8', NULL, true, true, 'NOME_TITULAR_8', false, NULL, NULL, 'nome_titular_8', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (28, 1, 35, 'Sexo', NULL, true, true, 'SEXO', NULL, NULL, NULL, 'sexo', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (29, 1, 35, 'Grau Instrução', NULL, true, true, 'GRAU_INSTRUCAO', NULL, NULL, NULL, 'grau_instrucao', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (30, 1, 35, 'Tipo Ocupação', NULL, true, true, 'TIPO_OCUPACAO', NULL, NULL, NULL, 'tipo_ocupacao', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (31, 1, 35, 'Codigo Ocupação', NULL, true, true, 'CODIGO_OCUPACAO', NULL, NULL, NULL, 'codigo_ocupacao', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (33, 1, 35, 'Estado Civil', NULL, true, true, 'ESTADO_CIVIL', NULL, NULL, NULL, 'estado_civil', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (56, 1, 35, 'DDD 2', NULL, true, true, 'DDD_2', NULL, NULL, NULL, 'ddd_2', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (60, 1, 35, 'DDD 3', NULL, true, true, 'DDD_3', NULL, NULL, NULL, 'ddd_3', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (64, 1, 35, 'DDD 4', NULL, true, true, 'DDD_4', NULL, NULL, NULL, 'ddd_4', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (34, 1, 35, 'Nome Conjuge', NULL, true, true, 'NOME_CONJUGE', NULL, NULL, NULL, 'conjuge_nome', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (35, 1, 35, 'CPF Conjuge', NULL, true, true, 'CPF_CONJUGE', NULL, NULL, NULL, 'conjuge_cpf', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (112, 1, 41, 'Identificador Titular 8', NULL, true, true, 'IDENTIFICADOR_TITULAR_8', false, NULL, NULL, 'identificador_titular_8', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (113, 1, 41, 'Nome Titular 9', NULL, true, true, 'NOME_TITULAR_9', false, NULL, NULL, 'nome_titular_9', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (114, 1, 41, 'Identificador Titular 9', NULL, true, true, 'IDENTIFICADOR_TITULAR_9', false, NULL, NULL, 'identificador_titular_9', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (115, 1, 2, 'Data Emissão', NULL, true, true, 'DATA_REFERENCIA', true, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (116, 1, 2, 'CPF', NULL, true, true, 'IDENTIFICADOR_CLIENTE', false, NULL, NULL, 'cpf', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (117, 1, 2, 'Renda Bruta', NULL, true, true, 'VALOR_RENDA_BRUTA', false, NULL, NULL, 'renda_bruta', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (118, 1, 2, 'Renda Liquida', NULL, true, true, 'VALOR_RENDA_LIQUIDA', false, NULL, NULL, 'renda_liquida', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (119, 1, 21, 'CPF', NULL, true, true, 'IDENTIFICADOR_CLIENTE', false, NULL, NULL, 'cpf', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (120, 1, 21, 'Nome', NULL, true, true, 'NOME', false, NULL, NULL, 'nome', 'STRING', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (32, 1, 35, 'Endereço', NULL, true, true, 'ENDERECO_COMERCIAL', NULL, NULL, NULL, 'endereco', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (2, 1, 7, 'Nome', 'NOME', true, true, 'NOME', NULL, 'NOME', 'E', 'nome', 'STRING', false, 'INPUT_TEXT', false, 'nome', 'dadosPessoais', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (3, 1, 7, 'Numero Registro', 'NUMERO_REGISTRO', true, true, 'NUMERO_REGISTRO', NULL, NULL, NULL, 'numero_registro', 'STRING', false, 'INPUT_TEXT', false, 'numeroDocumento', 'identidade', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (4, 1, 7, 'Data Nascimento', 'DATA_NASCIMENTO', true, true, 'DATA_NASCIMENTO', NULL, 'NASCIMENTO', 'E', 'data_nascimento', 'DATE', false, 'INPUT_TEXT', false, 'dtNascimento', 'dadosPessoais', 'DATE', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (6, 1, 7, 'Filiação', 'FILIACAO1', true, true, 'FILIACAO', NULL, 'MAE', 'P', 'filiacao', 'STRING', false, 'INPUT_TEXT', false, 'nomeMae', 'dadosPessoais', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (387, 1, 171, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (19, 1, 44, 'Nome', 'NOME', true, true, 'NOME', NULL, 'NOME', 'E', 'nome', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (8, 1, 9, 'CPF', 'IDENTIFICADOR_CLIENTE', true, true, 'IDENTIFICADOR_CLIENTE', false, NULL, NULL, 'cpf', 'STRING', true, 'INPUT_TEXT', false, 'cpf', 'dadosPessoais', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (388, 1, 172, 'Emissor do Documento', NULL, true, true, 'EMISSOR', false, NULL, NULL, 'emissor_documento', 'STRING', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (15, 1, 9, 'Filiação', 'FILIACAO', true, true, 'FILIACAO', false, 'MAE', 'P', 'filiacao', 'STRING', false, 'INPUT_TEXT', false, 'nomeMae', 'dadosPessoais', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (389, 1, 172, 'Data de Emissão', NULL, true, true, 'DATA_EMISSAO', false, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', true, NULL, NULL, NULL, 'DATE', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (130, 1, 35, 'Informações Patrimoniais', NULL, true, true, 'TIPO_PATRIMONIO', NULL, NULL, NULL, 'tipo_patrimonio', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (131, 1, 35, 'Declaração FATCA/CRS', NULL, true, true, 'DECLARACAO_FATCA_CRS', NULL, NULL, NULL, 'declaracao_fatca_crs', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (132, 1, 35, 'Número TIN', NULL, true, true, 'N_TIN', NULL, NULL, NULL, 'n_tin', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (133, 1, 35, 'Declaração de Propósitos da CC', NULL, true, true, 'DECLARACAO_PROPOSITO', NULL, NULL, NULL, 'declaracao_proposito', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (22, 1, 44, 'Data Admissão', 'DATA_ADMISSAO_INICIO_ATIVIDADE', true, true, 'DATA_ADMISSAO_INICIO_ATIVIDADE', NULL, NULL, NULL, 'data_admissao', 'DATE', false, 'INPUT_TEXT', false, 'dtAdmissao', 'renda', 'DATE', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (23, 1, 44, 'Renda Bruta', 'VALOR_RENDA_BRUTA', true, true, 'VALOR_RENDA_BRUTA', NULL, NULL, NULL, 'renda_bruta', 'STRING', true, 'INPUT_TEXT', false, 'rendaBruta', 'renda', 'DECIMAL', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (24, 1, 44, 'Renda Liquida', 'VALOR_RENDA_LIQUIDA', true, true, 'VALOR_RENDA_LIQUIDA', NULL, NULL, NULL, 'renda_liquida', 'STRING', true, 'INPUT_TEXT', false, 'rendaLiquida', 'renda', 'DECIMAL', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (121, 1, 43, 'Nome', 'NOME', true, true, 'NOME', false, NULL, NULL, 'nome', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (26, 1, 44, 'IRRF', 'IMPOSTO_RENDA_RETIDO_FONTE', true, true, 'IMPOSTO_RENDA_RETIDO_FONTE', NULL, NULL, NULL, 'irrf', 'STRING', false, 'INPUT_TEXT', false, 'irpf', 'renda', 'DECIMAL', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (128, 1, 7, 'Data Expedição', NULL, true, true, 'DATA_EMISSAO', true, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', false, 'dtEmissao', 'identidade', 'DATE', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (122, 1, 43, 'Endereço', 'ENDERECO', true, true, 'ENDERECO', false, NULL, NULL, 'endereco', 'STRING', true, 'INPUT_TEXT', false, 'logradouro', 'endereco', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (10, 1, 9, 'Numero Documento', 'NUMERO_REGISTRO', true, true, 'NUMERO_REGISTRO', false, NULL, NULL, 'numero_documento', 'STRING', false, 'INPUT_TEXT', false, 'numeroDocumento', 'cnh', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (123, 1, 43, 'CEP', 'CEP', true, true, 'CEP', false, NULL, NULL, 'cep', 'STRING', true, 'INPUT_TEXT', false, 'cep', 'endereco', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (124, 1, 43, 'Data Referência', 'DATA_REFERENCIA', true, true, 'DATA_REFERENCIA', false, NULL, NULL, 'data_referencia', 'DATE', true, 'INPUT_TEXT', false, 'dtApuracao', 'endereco', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (125, 1, 43, 'Data Emissão', 'DATA_EMISSAO', true, true, 'DATA_EMISSAO', true, NULL, NULL, 'data_emissao', 'DATE', true, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (9, 1, 9, 'Nome', 'NOME', true, true, 'NOME', false, 'NOME', 'E', 'nome', 'STRING', false, 'INPUT_TEXT', false, 'nome', 'dadosPessoais', 'STRING', 'STRING', 50);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (391, 1, 35, 'Cep Residencial', NULL, true, false, NULL, false, NULL, NULL, 'cep_residencial', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (392, 1, 35, 'Tipo Logradouro Residencial', NULL, true, false, NULL, false, NULL, NULL, 'tipo_logradouro_residencial', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (393, 1, 35, 'Logradouro Residencial', NULL, true, false, NULL, false, NULL, NULL, 'logradouro_residencial', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (394, 1, 35, 'Número Residencial', NULL, true, false, NULL, false, NULL, NULL, 'numero_residencial', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (395, 1, 35, 'Complemento Residencial', NULL, true, false, NULL, false, NULL, NULL, 'complemento_residencial', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (396, 1, 35, 'Bairro Residencial', NULL, true, false, NULL, false, NULL, NULL, 'bairro_residencial', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (397, 1, 35, 'UF Residencial', NULL, true, false, NULL, false, NULL, NULL, 'uf_residencial', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (398, 1, 35, 'Cidade Residencial', NULL, true, false, NULL, false, NULL, NULL, 'cidade_residencial', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (399, 1, 35, 'Cep Comercial', NULL, true, false, NULL, false, NULL, NULL, 'cep_comercial', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (400, 1, 35, 'Tipo Logradouro Comercial', NULL, true, false, NULL, false, NULL, NULL, 'tipo_logradouro_comercial', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (401, 1, 35, 'Logradouro Comercial', NULL, true, false, NULL, false, NULL, NULL, 'logradouro_comercial', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (402, 1, 35, 'Número Comercial', NULL, true, false, NULL, false, NULL, NULL, 'numero_comercial', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (403, 1, 35, 'Complemento Comercial', NULL, true, false, NULL, false, NULL, NULL, 'complemento_comercial', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (404, 1, 35, 'Bairro Comercial', NULL, true, false, NULL, false, NULL, NULL, 'bairro_comercial', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (405, 1, 35, 'UF Comercial', NULL, true, false, NULL, false, NULL, NULL, 'uf_comercial', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (406, 1, 35, 'Cidade Comercial', NULL, true, false, NULL, false, NULL, NULL, 'cidade_comercial', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (407, 1, 35, 'Celular Selecionado', NULL, true, false, NULL, false, NULL, NULL, 'celular_selecionado', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (48, 1, 9, 'UF Expedição', NULL, true, true, 'UF_EXPEDICAO', false, NULL, NULL, 'uf_expedicao', 'STRING', false, 'INPUT_TEXT', false, NULL, NULL, NULL, 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (17, 1, 9, 'UF Emissão', 'UF_EXPEDICAO', true, true, 'SIGLA_UF_EMISSAO', false, NULL, NULL, 'uf_emissao', 'STRING', false, 'INPUT_TEXT', false, 'uf', 'cnh', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (25, 1, 44, 'Ocupação', 'CODIGO_OCUPACAO', true, true, 'CODIGO_OCUPACAO', NULL, NULL, NULL, 'ocupacao', 'STRING', false, 'INPUT_TEXT', false, 'descrOcupacao', 'renda', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (21, 1, 44, 'CPF/CNPJ Fonte Pagadora', 'CPF_CNPJ_FONTE_PAGADORA', true, true, 'CPF_CNPJ_FONTE_PAGADORA', NULL, NULL, NULL, 'fonte_pagadora', 'STRING', false, 'INPUT_TEXT', false, 'cpfCnpj', 'renda/fontePagadora', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (408, 1, 43, 'Tipo de Logradouro', NULL, true, true, 'TIPO_LOGRADOURO', false, NULL, NULL, 'tipo_logradouro', 'STRING', false, 'INPUT_TEXT', false, 'tpLogradouro', 'endereco', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (409, 1, 43, 'Bairro', NULL, true, true, 'BAIRRO', false, NULL, NULL, 'bairro', 'STRING', false, 'INPUT_TEXT', false, 'bairro', 'endereco', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (410, 1, 43, 'Localidade', NULL, true, true, 'NOME_LOCALIDADE', false, NULL, NULL, 'localidade', 'STRING', false, 'INPUT_TEXT', false, 'nomeLocalidade', 'endereco', 'STRING', 'STRING', 100);
INSERT INTO mtrtb045_atributo_extracao (nu_atributo_extracao, nu_versao, nu_tipo_documento, no_atributo_negocial, no_atributo_retorno, ic_ativo, ic_ged, no_atributo_ged, ic_calculo_data, ic_campo_sicpf, ic_modo_sicpf, no_atributo_documento, ic_tipo_ged, ic_obrigatorio_ged, ic_tipo_campo, ic_obrigatorio, no_atributo_sicli, no_objeto_sicli, ic_tipo_sicli, ic_tipo_geral, pc_alteracao_permitido) VALUES (411, 1, 43, 'UF', NULL, true, true, 'UF', false, NULL, NULL, 'uf', 'STRING', false, 'INPUT_TEXT', false, 'uf', 'endereco', 'STRING', 'STRING', 100);


--
-- TOC entry 7596 (class 0 OID 2025825)
-- Dependencies: 1911
-- Data for Name: mtrtb046_processo_adm; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb046_processo_adm (nu_processo_adm, nu_versao, nu_processo, nu_ano_processo, nu_pregao, nu_unidade_contratacao, nu_ano_pregao, de_objeto_contratacao, ts_inclusao, co_matricula, ts_finalizacao, co_matricula_finalizacao, co_protocolo_siclg, nu_unidade_demandante) VALUES (1, 1, 12345, 2018, NULL, NULL, NULL, 'Teste de inclusão', '2018-05-22 02:30:56.432', 'c090347', NULL, NULL, NULL, 0);


--
-- TOC entry 7597 (class 0 OID 2025832)
-- Dependencies: 1912
-- Data for Name: mtrtb047_contrato_adm; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb047_contrato_adm (nu_contrato_adm, nu_versao, nu_processo_adm, nu_contrato, nu_ano_contrato, de_contrato, ts_inclusao, co_matricula, nu_cpf_cnpj_fornecedor, nu_unidade_operacional) VALUES (1, 1, 1, 1010, 2018, 'Descrição do contrato', '2018-05-22 02:53:20.046', 'c090347', '17777461000104', 0);


--
-- TOC entry 7598 (class 0 OID 2025839)
-- Dependencies: 1913
-- Data for Name: mtrtb048_apenso_adm; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO mtrtb048_apenso_adm (nu_apenso_adm, nu_versao, nu_processo_adm, nu_contrato_adm, nu_cpf_cnpj_fornecedor, ic_tipo_apenso, de_apenso, ts_inclusao, co_matricula, co_protocolo_siclg, no_titulo) VALUES (1, 1, 1, NULL, '81056699515', 'RC', 'Apenso 01', '2018-05-22 02:55:53.451', 'c090347', 'PROT1', NULL);
INSERT INTO mtrtb048_apenso_adm (nu_apenso_adm, nu_versao, nu_processo_adm, nu_contrato_adm, nu_cpf_cnpj_fornecedor, ic_tipo_apenso, de_apenso, ts_inclusao, co_matricula, co_protocolo_siclg, no_titulo) VALUES (3, 1, NULL, 1, '81056699515', 'RC', 'Apenso 01 C', '2018-05-22 02:58:06.027', 'c090347', 'PROT2', NULL);

--
-- TOC entry 7603 (class 0 OID 2025874)
-- Dependencies: 1918
-- Data for Name: schema_version; Type: TABLE DATA; Schema: mtr; Owner: -
--

INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (1, NULL, '<< Flyway Schema Creation >>', 'SCHEMA', '"mtrsm001"', NULL, 'postgres', '2017-12-05 15:19:40.63659', 0, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (2, '1.0.0.1', 'SIMTR DDL', 'SQL', 'V1_0_0_1__SIMTR_DDL.sql', -1668326178, 'postgres', '2017-12-05 15:19:41.479698', 11968, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (3, '1.0.0.2', 'SIMTR DDL', 'SQL', 'V1_0_0_2__SIMTR_DDL.sql', 670186567, 'postgres', '2017-12-11 11:57:35.049371', 11886, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (4, '1.0.0.3', 'SIMTR DDL', 'SQL', 'V1_0_0_3__SIMTR_DDL.sql', 304408848, 'postgres', '2017-12-18 15:35:01.124848', 1037, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (5, '1.0.0.4', 'SIMTR DDL', 'SQL', 'V1_0_0_4__SIMTR_DDL.sql', 185086822, 'postgres', '2017-12-27 12:06:20.833289', 1762, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (6, '1.0.0.5', 'SIMTR DDL', 'SQL', 'V1_0_0_5__SIMTR_DDL.sql', -445875678, 'postgres', '2018-01-23 13:57:44.913005', 563, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (7, '1.0.0.6', 'SIMTR DDL', 'SQL', 'V1_0_0_6__SIMTR_DDL.sql', 1084094441, 'postgres', '2018-01-30 16:14:35.932983', 509, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (9, '1.0.0.8', 'SIMTR DDL', 'SQL', 'V1_0_0_8__SIMTR_DDL.sql', -648577851, 'postgres', '2018-02-22 11:45:59.951396', 1407, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (10, '1.0.0.9', 'SIMTR DDL', 'SQL', 'V1_0_0_9__SIMTR_DDL.sql', -1786303145, 'postgres', '2018-02-22 18:46:45.970611', 117, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (8, '1.0.0.7', 'SIMTR DDL', 'SQL', 'V1_0_0_7__SIMTR_DDL.sql', -562753097, 'postgres', '2018-02-22 11:45:57.53079', 2136, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (11, '1.0.0.10', 'SIMTR DDL', 'SQL', 'V1_0_0_10__SIMTR_DDL.sql', 1467763916, 'postgres', '2018-03-20 10:30:46.328237', 1432, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (12, '1.0.0.11', 'SIMTR DDL', 'SQL', 'V1_0_0_11__SIMTR_DDL.sql', 1550210151, 'postgres', '2018-03-20 10:30:48.037225', 166, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (13, '1.0.0.12', 'SIMTR DDL', 'SQL', 'V1_0_0_12__SIMTR_DDL.sql', -559297317, 'postgres', '2018-03-26 19:29:04.304925', 3310, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (14, '1.0.0.13', 'SIMTR DDL', 'SQL', 'V1_0_0_13__SIMTR_DDL.sql', -1861505220, 'postgres', '2018-03-27 12:00:32.817593', 219, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (15, '1.0.0.14', 'SIMTR DDL', 'SQL', 'V1_0_0_14__SIMTR_DDL.sql', -1134889804, 'postgres', '2018-04-01 20:55:12.058043', 289, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (16, '1.0.0.15', 'SIMTR DDL', 'SQL', 'V1_0_0_15__SIMTR_DDL.sql', 1104600083, 'postgres', '2018-04-03 10:11:30.276989', 119, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (17, '1.0.0.16', 'SIMTR Equalizacao Modelo', 'SQL', 'V1_0_0_16__SIMTR_Equalizacao_Modelo.sql', 504727107, 'postgres', '2018-05-04 11:06:41.158106', 7573, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (18, '1.0.0.17', 'Dossie Digital Validacao SICPF', 'SQL', 'V1_0_0_17__Dossie_Digital_Validacao_SICPF.sql', -2029522896, 'postgres', '2018-05-18 11:19:05.39372', 178, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (19, '1.0.0.18', 'Dossie Digital Atributo Integracao', 'SQL', 'V1_0_0_18__Dossie_Digital_Atributo_Integracao.sql', 1282773141, 'postgres', '2018-05-22 16:51:55.960485', 370, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (20, '1.0.0.19', 'Dossie Digital Documento Autorizacao Codigo GED', 'SQL', 'V1_0_0_19__Dossie_Digital_Documento_Autorizacao_Codigo_GED.sql', 303415322, 'postgres', '2018-06-05 18:30:49.702588', 406, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (21, '1.0.0.20', 'Instancia Documento Vinculo Cliente', 'SQL', 'V1_0_0_20__Instancia_Documento_Vinculo_Cliente.sql', -264933225, 'postgres', '2018-06-08 10:26:12.130337', 201, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (22, '1.0.0.21', 'Conteudo Base64', 'SQL', 'V1_0_0_21__Conteudo_Base64.sql', -720632115, 'postgres', '2018-06-15 12:08:20.874992', 544, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (24, '1.0.0.23', 'Inicio BPM', 'SQL', 'V1_0_0_23__Inicio_BPM.sql', -2000706851, 'postgres', '2018-06-29 09:31:36.201446', 206, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (25, '1.0.0.24', 'Dossie Digital Conclusao Autorizacao', 'SQL', 'V1_0_0_24__Dossie_Digital_Conclusao_Autorizacao.sql', -858546746, 'postgres', '2018-07-03 11:10:39.562799', 178, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (23, '1.0.0.22', 'Atributos Extracao e Composicao Documental', 'SQL', 'V1_0_0_22__Atributos_Extracao_e_Composicao_Documental.sql', -444834111, 'postgres', '2018-06-25 16:24:59.291025', 622, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (26, '1.0.0.25', 'Descricao Documento Administrativo', 'SQL', 'V1_0_0_25__Descricao_Documento_Administrativo.sql', -569667553, 'postgres', '2018-07-18 10:53:24.181166', 469, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (27, '1.0.0.26', 'Atributos Controle SICLI', 'SQL', 'V1_0_0_26__Atributos_Controle_SICLI.sql', 845682480, 'postgres', '2018-07-25 13:40:41.805971', 419, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (28, '1.0.0.27', 'Minuta Documento', 'SQL', 'V1_0_0_27__Minuta_Documento.sql', 336006583, 'postgres', '2018-08-01 18:53:54.23648', 199, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (29, '1.0.0.28', 'Atributos PAE', 'SQL', 'V1_0_0_28__Atributos PAE.sql', 1115553701, 'postgres', '2018-08-08 12:08:12.180718', 908, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (30, '1.0.0.29', 'Percentual Alteracao Atributos', 'SQL', 'V1_0_0_29__Percentual_Alteracao_Atributos.sql', -424271984, 'postgres', '2018-08-15 12:17:15.73821', 140, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (31, '1.0.0.30', 'Inclusao Atributos Controle Antifraude', 'SQL', 'V1_0_0_30__Inclusao_Atributos_Controle_Antifraude.sql', 743520958, 'postgres', '2018-09-14 12:33:37.034238', 382, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (32, '1.0.0.31', 'Inclusao Tipologia PAE', 'SQL', 'V1_0_0_31__Inclusao_Tipologia_PAE.sql', 831611075, 'postgres', '2018-09-19 11:21:52.237403', 10577, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (33, '1.0.0.32', 'Exclusao Logica PAE', 'SQL', 'V1_0_0_32__Exclusao_Logica_PAE.sql', -638230746, 'postgres', '2018-09-20 12:15:09.242599', 627, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (34, '1.0.0.33', 'Titulo Apenso PAE', 'SQL', 'V1_0_0_33__Titulo_Apenso_PAE.sql', -185477720, 'postgres', '2018-09-21 14:04:22.514527', 138, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (36, '1.0.0.35', 'Tablespace', 'SQL', 'V1_0_0_35__Tablespace.sql', 1771783399, 'postgres', '2018-10-26 19:07:58.649865', 240, true);
INSERT INTO schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (35, '1.0.0.34', 'Ajustes atributos controle solicitacoes publicas', 'SQL', 'V1_0_0_34__Ajustes_atributos_controle_solicitacoes_publicas.sql', -382827451, 'postgres', '2018-10-16 09:26:47.412472', 591, true);


SET default_tablespace = mtrtsix000;

--
-- TOC entry 6982 (class 2606 OID 2025909)
-- Name: pk_mtrtb001; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb001_dossie_cliente
    ADD CONSTRAINT pk_mtrtb001 PRIMARY KEY (nu_dossie_cliente);


--
-- TOC entry 7048 (class 2606 OID 2025911)
-- Name: pk_mtrtb001_pessoa_fisica; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb001_pessoa_fisica
    ADD CONSTRAINT pk_mtrtb001_pessoa_fisica PRIMARY KEY (nu_dossie_cliente);


--
-- TOC entry 7050 (class 2606 OID 2025913)
-- Name: pk_mtrtb001_pessoa_juridica; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb001_pessoa_juridica
    ADD CONSTRAINT pk_mtrtb001_pessoa_juridica PRIMARY KEY (nu_dossie_cliente);


--
-- TOC entry 6984 (class 2606 OID 2025915)
-- Name: pk_mtrtb002; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb002_dossie_produto
    ADD CONSTRAINT pk_mtrtb002 PRIMARY KEY (nu_dossie_produto);


--
-- TOC entry 6986 (class 2606 OID 2025917)
-- Name: pk_mtrtb003; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb003_documento
    ADD CONSTRAINT pk_mtrtb003 PRIMARY KEY (nu_documento);


--
-- TOC entry 7052 (class 2606 OID 2025919)
-- Name: pk_mtrtb004; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb004_dossie_cliente_produto
    ADD CONSTRAINT pk_mtrtb004 PRIMARY KEY (nu_dossie_cliente_produto);


--
-- TOC entry 7054 (class 2606 OID 2025921)
-- Name: pk_mtrtb005; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb005_documento_cliente
    ADD CONSTRAINT pk_mtrtb005 PRIMARY KEY (nu_documento, nu_dossie_cliente);


--
-- TOC entry 6989 (class 2606 OID 2025923)
-- Name: pk_mtrtb006; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb006_canal
    ADD CONSTRAINT pk_mtrtb006 PRIMARY KEY (nu_canal);


--
-- TOC entry 6991 (class 2606 OID 2025925)
-- Name: pk_mtrtb007; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb007_atributo_documento
    ADD CONSTRAINT pk_mtrtb007 PRIMARY KEY (nu_atributo_documento);


--
-- TOC entry 7118 (class 2606 OID 2026501)
-- Name: pk_mtrtb008; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb008_conteudo
    ADD CONSTRAINT pk_mtrtb008 PRIMARY KEY (nu_conteudo);


--
-- TOC entry 6995 (class 2606 OID 2025927)
-- Name: pk_mtrtb009; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb009_tipo_documento
    ADD CONSTRAINT pk_mtrtb009 PRIMARY KEY (nu_tipo_documento);


--
-- TOC entry 6998 (class 2606 OID 2025929)
-- Name: pk_mtrtb010; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb010_funcao_documental
    ADD CONSTRAINT pk_mtrtb010 PRIMARY KEY (nu_funcao_documental);


--
-- TOC entry 7056 (class 2606 OID 2025931)
-- Name: pk_mtrtb011; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb011_funcao_documento
    ADD CONSTRAINT pk_mtrtb011 PRIMARY KEY (nu_tipo_documento, nu_funcao_documental);


--
-- TOC entry 7001 (class 2606 OID 2025933)
-- Name: pk_mtrtb012; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb012_tipo_situacao_dossie
    ADD CONSTRAINT pk_mtrtb012 PRIMARY KEY (nu_tipo_situacao_dossie);


--
-- TOC entry 7003 (class 2606 OID 2025935)
-- Name: pk_mtrtb013; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb013_situacao_dossie
    ADD CONSTRAINT pk_mtrtb013 PRIMARY KEY (nu_situacao_dossie);


--
-- TOC entry 7006 (class 2606 OID 2025937)
-- Name: pk_mtrtb014; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb014_instancia_documento
    ADD CONSTRAINT pk_mtrtb014 PRIMARY KEY (nu_instancia_documento);


--
-- TOC entry 7009 (class 2606 OID 2025939)
-- Name: pk_mtrtb015; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb015_situacao_documento
    ADD CONSTRAINT pk_mtrtb015 PRIMARY KEY (nu_situacao_documento);


--
-- TOC entry 7012 (class 2606 OID 2025941)
-- Name: pk_mtrtb016; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb016_motivo_stco_documento
    ADD CONSTRAINT pk_mtrtb016 PRIMARY KEY (nu_motivo_stco_documento);


--
-- TOC entry 7058 (class 2606 OID 2025943)
-- Name: pk_mtrtb017; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb017_stco_instnca_documento
    ADD CONSTRAINT pk_mtrtb017 PRIMARY KEY (nu_stco_instnca_documento);


--
-- TOC entry 7060 (class 2606 OID 2025945)
-- Name: pk_mtrtb018; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb018_unidade_tratamento
    ADD CONSTRAINT pk_mtrtb018 PRIMARY KEY (nu_dossie_produto, nu_unidade);


--
-- TOC entry 7063 (class 2606 OID 2025947)
-- Name: pk_mtrtb019; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb019_campo_formulario
    ADD CONSTRAINT pk_mtrtb019 PRIMARY KEY (nu_campo_formulario);


--
-- TOC entry 7015 (class 2606 OID 2025949)
-- Name: pk_mtrtb020; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb020_processo
    ADD CONSTRAINT pk_mtrtb020 PRIMARY KEY (nu_processo);


--
-- TOC entry 7017 (class 2606 OID 2025951)
-- Name: pk_mtrtb021; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb021_unidade_autorizada
    ADD CONSTRAINT pk_mtrtb021 PRIMARY KEY (nu_unidade_autorizada);


--
-- TOC entry 7020 (class 2606 OID 2025953)
-- Name: pk_mtrtb022; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb022_produto
    ADD CONSTRAINT pk_mtrtb022 PRIMARY KEY (nu_produto);


--
-- TOC entry 7065 (class 2606 OID 2025955)
-- Name: pk_mtrtb023; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb023_produto_processo
    ADD CONSTRAINT pk_mtrtb023 PRIMARY KEY (nu_processo, nu_produto);


--
-- TOC entry 7022 (class 2606 OID 2025957)
-- Name: pk_mtrtb024; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb024_produto_dossie
    ADD CONSTRAINT pk_mtrtb024 PRIMARY KEY (nu_produto_dossie);


--
-- TOC entry 7024 (class 2606 OID 2025959)
-- Name: pk_mtrtb025; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb025_processo_documento
    ADD CONSTRAINT pk_mtrtb025 PRIMARY KEY (nu_processo_documento);


--
-- TOC entry 7068 (class 2606 OID 2025961)
-- Name: pk_mtrtb026; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb026_relacao_processo
    ADD CONSTRAINT pk_mtrtb026 PRIMARY KEY (nu_processo_pai, nu_processo_filho);


--
-- TOC entry 7026 (class 2606 OID 2025963)
-- Name: pk_mtrtb027; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb027_campo_entrada
    ADD CONSTRAINT pk_mtrtb027 PRIMARY KEY (nu_campo_entrada);


--
-- TOC entry 7028 (class 2606 OID 2025965)
-- Name: pk_mtrtb028; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb028_opcao_campo
    ADD CONSTRAINT pk_mtrtb028 PRIMARY KEY (nu_opcao_campo);


--
-- TOC entry 7030 (class 2606 OID 2025967)
-- Name: pk_mtrtb029; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb029_campo_apresentacao
    ADD CONSTRAINT pk_mtrtb029 PRIMARY KEY (nu_campo_apresentacao);


--
-- TOC entry 7032 (class 2606 OID 2025969)
-- Name: pk_mtrtb030; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb030_resposta_dossie
    ADD CONSTRAINT pk_mtrtb030 PRIMARY KEY (nu_resposta_dossie);


--
-- TOC entry 7070 (class 2606 OID 2025971)
-- Name: pk_mtrtb031; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb031_resposta_opcao
    ADD CONSTRAINT pk_mtrtb031 PRIMARY KEY (nu_resposta_dossie, nu_opcao_campo);


--
-- TOC entry 7072 (class 2606 OID 2025973)
-- Name: pk_mtrtb032; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb032_elemento_conteudo
    ADD CONSTRAINT pk_mtrtb032 PRIMARY KEY (nu_elemento_conteudo);


--
-- TOC entry 7035 (class 2606 OID 2025975)
-- Name: pk_mtrtb033; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb033_garantia
    ADD CONSTRAINT pk_mtrtb033 PRIMARY KEY (nu_garantia);


--
-- TOC entry 7074 (class 2606 OID 2025977)
-- Name: pk_mtrtb034; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb034_garantia_produto
    ADD CONSTRAINT pk_mtrtb034 PRIMARY KEY (nu_produto, nu_garantia);


--
-- TOC entry 7037 (class 2606 OID 2025979)
-- Name: pk_mtrtb035; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb035_garantia_informada
    ADD CONSTRAINT pk_mtrtb035 PRIMARY KEY (nu_garantia_informada);


--
-- TOC entry 7039 (class 2606 OID 2025981)
-- Name: pk_mtrtb036; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb036_composicao_documental
    ADD CONSTRAINT pk_mtrtb036 PRIMARY KEY (nu_composicao_documental);


--
-- TOC entry 7041 (class 2606 OID 2025983)
-- Name: pk_mtrtb037; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb037_regra_documental
    ADD CONSTRAINT pk_mtrtb037 PRIMARY KEY (nu_regra_documental);


--
-- TOC entry 7076 (class 2606 OID 2025985)
-- Name: pk_mtrtb038; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb038_nivel_documental
    ADD CONSTRAINT pk_mtrtb038 PRIMARY KEY (nu_composicao_documental, nu_dossie_cliente);


--
-- TOC entry 7078 (class 2606 OID 2025987)
-- Name: pk_mtrtb039; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb039_produto_composicao
    ADD CONSTRAINT pk_mtrtb039 PRIMARY KEY (nu_composicao_documental, nu_produto);


--
-- TOC entry 7080 (class 2606 OID 2025989)
-- Name: pk_mtrtb040; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb040_cadeia_tipo_sto_dossie
    ADD CONSTRAINT pk_mtrtb040 PRIMARY KEY (nu_tipo_situacao_atual, nu_tipo_situacao_seguinte);


--
-- TOC entry 7082 (class 2606 OID 2025991)
-- Name: pk_mtrtb041; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb041_cadeia_stco_documento
    ADD CONSTRAINT pk_mtrtb041 PRIMARY KEY (nu_situacao_documento_atual, nu_situacao_documento_seguinte);


--
-- TOC entry 7084 (class 2606 OID 2025993)
-- Name: pk_mtrtb042; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb042_cliente_garantia
    ADD CONSTRAINT pk_mtrtb042 PRIMARY KEY (nu_garantia_informada, nu_dossie_cliente);


--
-- TOC entry 7087 (class 2606 OID 2025995)
-- Name: pk_mtrtb043; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb043_documento_garantia
    ADD CONSTRAINT pk_mtrtb043 PRIMARY KEY (nu_documento_garantia);


--
-- TOC entry 7090 (class 2606 OID 2025997)
-- Name: pk_mtrtb044; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb044_comportamento_pesquisa
    ADD CONSTRAINT pk_mtrtb044 PRIMARY KEY (nu_comportamento_pesquisa);


--
-- TOC entry 7092 (class 2606 OID 2025999)
-- Name: pk_mtrtb045; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb045_atributo_extracao
    ADD CONSTRAINT pk_mtrtb045 PRIMARY KEY (nu_atributo_extracao);


--
-- TOC entry 7096 (class 2606 OID 2026001)
-- Name: pk_mtrtb046; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb046_processo_adm
    ADD CONSTRAINT pk_mtrtb046 PRIMARY KEY (nu_processo_adm);


--
-- TOC entry 7099 (class 2606 OID 2026003)
-- Name: pk_mtrtb047; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb047_contrato_adm
    ADD CONSTRAINT pk_mtrtb047 PRIMARY KEY (nu_contrato_adm);


--
-- TOC entry 7104 (class 2606 OID 2026005)
-- Name: pk_mtrtb048; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb048_apenso_adm
    ADD CONSTRAINT pk_mtrtb048 PRIMARY KEY (nu_apenso_adm);


--
-- TOC entry 7106 (class 2606 OID 2026007)
-- Name: pk_mtrtb049; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb049_documento_adm
    ADD CONSTRAINT pk_mtrtb049 PRIMARY KEY (nu_documento_adm);


--
-- TOC entry 7044 (class 2606 OID 2026009)
-- Name: pk_mtrtb100; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb100_autorizacao
    ADD CONSTRAINT pk_mtrtb100 PRIMARY KEY (nu_autorizacao);


--
-- TOC entry 7046 (class 2606 OID 2026011)
-- Name: pk_mtrtb101; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb101_documento
    ADD CONSTRAINT pk_mtrtb101 PRIMARY KEY (nu_documento);


--
-- TOC entry 7108 (class 2606 OID 2026013)
-- Name: pk_mtrtb102; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb102_autorizacao_negada
    ADD CONSTRAINT pk_mtrtb102 PRIMARY KEY (nu_autorizacao_negada);


--
-- TOC entry 7110 (class 2606 OID 2026015)
-- Name: pk_mtrtb103; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb103_autorizacao_orientacao
    ADD CONSTRAINT pk_mtrtb103 PRIMARY KEY (nu_autorizacao_orientacao);


--
-- TOC entry 7112 (class 2606 OID 2026017)
-- Name: pk_mtrtb200; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY mtrtb200_sicli_erro
    ADD CONSTRAINT pk_mtrtb200 PRIMARY KEY (nu_sicli_erro);


--
-- TOC entry 7114 (class 2606 OID 2026019)
-- Name: schema_version_pk; Type: CONSTRAINT; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

ALTER TABLE ONLY schema_version
    ADD CONSTRAINT schema_version_pk PRIMARY KEY (installed_rank);


--
-- TOC entry 6980 (class 1259 OID 2026020)
-- Name: ix_mtrtb001_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb001_01 ON mtrtb001_dossie_cliente USING btree (nu_cpf_cnpj);


--
-- TOC entry 6987 (class 1259 OID 2026021)
-- Name: ix_mtrtb006_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb006_01 ON mtrtb006_canal USING btree (co_integracao);


--
-- TOC entry 7116 (class 1259 OID 2026502)
-- Name: ix_mtrtb008_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb008_01 ON mtrtb008_conteudo USING btree (nu_documento, nu_ordem);


--
-- TOC entry 6992 (class 1259 OID 2026022)
-- Name: ix_mtrtb009_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb009_01 ON mtrtb009_tipo_documento USING btree (no_tipo_documento, ic_processo_administrativo, ic_dossie_digital, ic_apoio_negocio, ic_validacao_cadastral);


--
-- TOC entry 6993 (class 1259 OID 2026023)
-- Name: ix_mtrtb009_02; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb009_02 ON mtrtb009_tipo_documento USING btree (co_tipologia);


--
-- TOC entry 6996 (class 1259 OID 2026024)
-- Name: ix_mtrtb010_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb010_01 ON mtrtb010_funcao_documental USING btree (no_funcao);


--
-- TOC entry 6999 (class 1259 OID 2026025)
-- Name: ix_mtrtb012_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb012_01 ON mtrtb012_tipo_situacao_dossie USING btree (no_tipo_situacao);


--
-- TOC entry 7004 (class 1259 OID 2026026)
-- Name: ix_mtrtb014_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb014_01 ON mtrtb014_instancia_documento USING btree (nu_documento, nu_dossie_produto);


--
-- TOC entry 7007 (class 1259 OID 2026027)
-- Name: ix_mtrtb015_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb015_01 ON mtrtb015_situacao_documento USING btree (no_situacao);


--
-- TOC entry 7010 (class 1259 OID 2026028)
-- Name: ix_mtrtb016_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb016_01 ON mtrtb016_motivo_stco_documento USING btree (nu_situacao_documento, no_motivo_stco_documento);


--
-- TOC entry 7061 (class 1259 OID 2026029)
-- Name: ix_mtrtb019_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb019_01 ON mtrtb019_campo_formulario USING btree (nu_processo, nu_ordem);


--
-- TOC entry 7013 (class 1259 OID 2026030)
-- Name: ix_mtrtb020_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb020_01 ON mtrtb020_processo USING btree (no_processo);


--
-- TOC entry 7018 (class 1259 OID 2026031)
-- Name: ix_mtrtb022_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb022_01 ON mtrtb022_produto USING btree (nu_operacao, nu_modalidade);


--
-- TOC entry 7066 (class 1259 OID 2026032)
-- Name: ix_mtrtb026_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb026_01 ON mtrtb026_relacao_processo USING btree (nu_processo_pai, nu_ordem);


--
-- TOC entry 7033 (class 1259 OID 2026033)
-- Name: ix_mtrtb033_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb033_01 ON mtrtb033_garantia USING btree (nu_garantia_bacen);


--
-- TOC entry 7085 (class 1259 OID 2026034)
-- Name: ix_mtrtb043_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb043_01 ON mtrtb043_documento_garantia USING btree (nu_garantia, nu_processo);


--
-- TOC entry 7088 (class 1259 OID 2026035)
-- Name: ix_mtrtb044_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb044_01 ON mtrtb044_comportamento_pesquisa USING btree (nu_produto, ic_sistema_retorno, ic_codigo_retorno);


--
-- TOC entry 7093 (class 1259 OID 2026036)
-- Name: ix_mtrtb046_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb046_01 ON mtrtb046_processo_adm USING btree (nu_processo, nu_ano_processo);


--
-- TOC entry 7094 (class 1259 OID 2026037)
-- Name: ix_mtrtb046_02; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb046_02 ON mtrtb046_processo_adm USING btree (nu_pregao, nu_unidade_contratacao, nu_ano_pregao);


--
-- TOC entry 7097 (class 1259 OID 2026038)
-- Name: ix_mtrtb047_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb047_01 ON mtrtb047_contrato_adm USING btree (nu_contrato, nu_ano_contrato);


--
-- TOC entry 7100 (class 1259 OID 2026039)
-- Name: ix_mtrtb048_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb048_01 ON mtrtb048_apenso_adm USING btree (co_protocolo_siclg);


--
-- TOC entry 7101 (class 1259 OID 2026040)
-- Name: ix_mtrtb048_02; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb048_02 ON mtrtb048_apenso_adm USING btree (nu_processo_adm, no_titulo);


--
-- TOC entry 7102 (class 1259 OID 2026041)
-- Name: ix_mtrtb048_03; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb048_03 ON mtrtb048_apenso_adm USING btree (nu_contrato_adm, no_titulo);


--
-- TOC entry 7042 (class 1259 OID 2026042)
-- Name: ix_mtrtb100_01; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE UNIQUE INDEX ix_mtrtb100_01 ON mtrtb100_autorizacao USING btree (co_autorizacao);


--
-- TOC entry 7115 (class 1259 OID 2026043)
-- Name: schema_version_s_idx; Type: INDEX; Schema: mtr; Owner: -; Tablespace: mtrtsix000
--

CREATE INDEX schema_version_s_idx ON schema_version USING btree (success);


--
-- TOC entry 7153 (class 2606 OID 2026044)
-- Name: fk_mtrtb001_mtrtb001_01; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb001_pessoa_fisica
    ADD CONSTRAINT fk_mtrtb001_mtrtb001_01 FOREIGN KEY (nu_dossie_cliente) REFERENCES mtrtb001_dossie_cliente(nu_dossie_cliente) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7154 (class 2606 OID 2026049)
-- Name: fk_mtrtb001_mtrtb001_02; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb001_pessoa_juridica
    ADD CONSTRAINT fk_mtrtb001_mtrtb001_02 FOREIGN KEY (nu_dossie_cliente) REFERENCES mtrtb001_dossie_cliente(nu_dossie_cliente) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7120 (class 2606 OID 2026054)
-- Name: fk_mtrtb002_mtrtb020_01; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb002_dossie_produto
    ADD CONSTRAINT fk_mtrtb002_mtrtb020_01 FOREIGN KEY (nu_processo_dossie) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7119 (class 2606 OID 2026059)
-- Name: fk_mtrtb002_mtrtb020_02; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb002_dossie_produto
    ADD CONSTRAINT fk_mtrtb002_mtrtb020_02 FOREIGN KEY (nu_processo_fase) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7122 (class 2606 OID 2026064)
-- Name: fk_mtrtb003_mtrtb006; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb003_documento
    ADD CONSTRAINT fk_mtrtb003_mtrtb006 FOREIGN KEY (nu_canal_captura) REFERENCES mtrtb006_canal(nu_canal) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7121 (class 2606 OID 2026069)
-- Name: fk_mtrtb003_mtrtb009; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb003_documento
    ADD CONSTRAINT fk_mtrtb003_mtrtb009 FOREIGN KEY (nu_tipo_documento) REFERENCES mtrtb009_tipo_documento(nu_tipo_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7125 (class 2606 OID 2026074)
-- Name: fk_mtrtb003_mtrtb009; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb013_situacao_dossie
    ADD CONSTRAINT fk_mtrtb003_mtrtb009 FOREIGN KEY (nu_tipo_situacao_dossie) REFERENCES mtrtb012_tipo_situacao_dossie(nu_tipo_situacao_dossie) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7164 (class 2606 OID 2026079)
-- Name: fk_mtrtb003_mtrtb009; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb017_stco_instnca_documento
    ADD CONSTRAINT fk_mtrtb003_mtrtb009 FOREIGN KEY (nu_situacao_documento) REFERENCES mtrtb015_situacao_documento(nu_situacao_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7157 (class 2606 OID 2026084)
-- Name: fk_mtrtb004_mtrtb001; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb004_dossie_cliente_produto
    ADD CONSTRAINT fk_mtrtb004_mtrtb001 FOREIGN KEY (nu_dossie_cliente) REFERENCES mtrtb001_dossie_cliente(nu_dossie_cliente) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7156 (class 2606 OID 2026089)
-- Name: fk_mtrtb004_mtrtb001_02; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb004_dossie_cliente_produto
    ADD CONSTRAINT fk_mtrtb004_mtrtb001_02 FOREIGN KEY (nu_dossie_cliente_relacionado) REFERENCES mtrtb001_dossie_cliente(nu_dossie_cliente) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7155 (class 2606 OID 2026094)
-- Name: fk_mtrtb004_mtrtb002; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb004_dossie_cliente_produto
    ADD CONSTRAINT fk_mtrtb004_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7159 (class 2606 OID 2026099)
-- Name: fk_mtrtb005_mtrtb001; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb005_documento_cliente
    ADD CONSTRAINT fk_mtrtb005_mtrtb001 FOREIGN KEY (nu_dossie_cliente) REFERENCES mtrtb001_dossie_cliente(nu_dossie_cliente) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7158 (class 2606 OID 2026104)
-- Name: fk_mtrtb005_mtrtb003; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb005_documento_cliente
    ADD CONSTRAINT fk_mtrtb005_mtrtb003 FOREIGN KEY (nu_documento) REFERENCES mtrtb003_documento(nu_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7123 (class 2606 OID 2026109)
-- Name: fk_mtrtb007_mtrtb003; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb007_atributo_documento
    ADD CONSTRAINT fk_mtrtb007_mtrtb003 FOREIGN KEY (nu_documento) REFERENCES mtrtb003_documento(nu_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7205 (class 2606 OID 2026503)
-- Name: fk_mtrtb008_mtrtb003; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb008_conteudo
    ADD CONSTRAINT fk_mtrtb008_mtrtb003 FOREIGN KEY (nu_documento) REFERENCES mtrtb003_documento(nu_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7161 (class 2606 OID 2026114)
-- Name: fk_mtrtb011_mtrtb009; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb011_funcao_documento
    ADD CONSTRAINT fk_mtrtb011_mtrtb009 FOREIGN KEY (nu_tipo_documento) REFERENCES mtrtb009_tipo_documento(nu_tipo_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7160 (class 2606 OID 2026119)
-- Name: fk_mtrtb011_mtrtb010; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb011_funcao_documento
    ADD CONSTRAINT fk_mtrtb011_mtrtb010 FOREIGN KEY (nu_funcao_documental) REFERENCES mtrtb010_funcao_documental(nu_funcao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7124 (class 2606 OID 2026124)
-- Name: fk_mtrtb013_mtrtb002; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb013_situacao_dossie
    ADD CONSTRAINT fk_mtrtb013_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7130 (class 2606 OID 2026129)
-- Name: fk_mtrtb014_mtrtb002; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb014_instancia_documento
    ADD CONSTRAINT fk_mtrtb014_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7129 (class 2606 OID 2026134)
-- Name: fk_mtrtb014_mtrtb003; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb014_instancia_documento
    ADD CONSTRAINT fk_mtrtb014_mtrtb003 FOREIGN KEY (nu_documento) REFERENCES mtrtb003_documento(nu_documento) ON UPDATE RESTRICT ON DELETE CASCADE;


--
-- TOC entry 7128 (class 2606 OID 2026139)
-- Name: fk_mtrtb014_mtrtb004; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb014_instancia_documento
    ADD CONSTRAINT fk_mtrtb014_mtrtb004 FOREIGN KEY (nu_dossie_cliente_produto) REFERENCES mtrtb004_dossie_cliente_produto(nu_dossie_cliente_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7127 (class 2606 OID 2026144)
-- Name: fk_mtrtb014_mtrtb032; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb014_instancia_documento
    ADD CONSTRAINT fk_mtrtb014_mtrtb032 FOREIGN KEY (nu_elemento_conteudo) REFERENCES mtrtb032_elemento_conteudo(nu_elemento_conteudo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7126 (class 2606 OID 2026149)
-- Name: fk_mtrtb014_mtrtb035; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb014_instancia_documento
    ADD CONSTRAINT fk_mtrtb014_mtrtb035 FOREIGN KEY (nu_garantia_informada) REFERENCES mtrtb035_garantia_informada(nu_garantia_informada) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7131 (class 2606 OID 2026154)
-- Name: fk_mtrtb016_mtrtb015; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb016_motivo_stco_documento
    ADD CONSTRAINT fk_mtrtb016_mtrtb015 FOREIGN KEY (nu_situacao_documento) REFERENCES mtrtb015_situacao_documento(nu_situacao_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7163 (class 2606 OID 2026159)
-- Name: fk_mtrtb017_mtrtb014; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb017_stco_instnca_documento
    ADD CONSTRAINT fk_mtrtb017_mtrtb014 FOREIGN KEY (nu_instancia_documento) REFERENCES mtrtb014_instancia_documento(nu_instancia_documento) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 7162 (class 2606 OID 2026164)
-- Name: fk_mtrtb017_mtrtb016; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb017_stco_instnca_documento
    ADD CONSTRAINT fk_mtrtb017_mtrtb016 FOREIGN KEY (nu_motivo_stco_documento) REFERENCES mtrtb016_motivo_stco_documento(nu_motivo_stco_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7165 (class 2606 OID 2026169)
-- Name: fk_mtrtb018_mtrtb002; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb018_unidade_tratamento
    ADD CONSTRAINT fk_mtrtb018_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7167 (class 2606 OID 2026174)
-- Name: fk_mtrtb019_fk_mtrtb0_mtrtb020; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb019_campo_formulario
    ADD CONSTRAINT fk_mtrtb019_fk_mtrtb0_mtrtb020 FOREIGN KEY (nu_processo) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7135 (class 2606 OID 2026179)
-- Name: fk_mtrtb021_mtrtb020; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb021_unidade_autorizada
    ADD CONSTRAINT fk_mtrtb021_mtrtb020 FOREIGN KEY (nu_processo) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7134 (class 2606 OID 2026184)
-- Name: fk_mtrtb021_mtrtb046; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb021_unidade_autorizada
    ADD CONSTRAINT fk_mtrtb021_mtrtb046 FOREIGN KEY (nu_processo_adm) REFERENCES mtrtb046_processo_adm(nu_processo_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7133 (class 2606 OID 2026189)
-- Name: fk_mtrtb021_mtrtb047; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb021_unidade_autorizada
    ADD CONSTRAINT fk_mtrtb021_mtrtb047 FOREIGN KEY (nu_contrato_adm) REFERENCES mtrtb047_contrato_adm(nu_contrato_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7132 (class 2606 OID 2026194)
-- Name: fk_mtrtb021_mtrtb048; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb021_unidade_autorizada
    ADD CONSTRAINT fk_mtrtb021_mtrtb048 FOREIGN KEY (nu_apenso_adm) REFERENCES mtrtb048_apenso_adm(nu_apenso_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7169 (class 2606 OID 2026199)
-- Name: fk_mtrtb023_mtrtb020; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb023_produto_processo
    ADD CONSTRAINT fk_mtrtb023_mtrtb020 FOREIGN KEY (nu_processo) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7168 (class 2606 OID 2026204)
-- Name: fk_mtrtb023_mtrtb022; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb023_produto_processo
    ADD CONSTRAINT fk_mtrtb023_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7137 (class 2606 OID 2026209)
-- Name: fk_mtrtb024_mtrtb002; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb024_produto_dossie
    ADD CONSTRAINT fk_mtrtb024_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7136 (class 2606 OID 2026214)
-- Name: fk_mtrtb024_mtrtb022; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb024_produto_dossie
    ADD CONSTRAINT fk_mtrtb024_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7140 (class 2606 OID 2026219)
-- Name: fk_mtrtb025_mtrtb009; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb025_processo_documento
    ADD CONSTRAINT fk_mtrtb025_mtrtb009 FOREIGN KEY (nu_tipo_documento) REFERENCES mtrtb009_tipo_documento(nu_tipo_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7139 (class 2606 OID 2026224)
-- Name: fk_mtrtb025_mtrtb010; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb025_processo_documento
    ADD CONSTRAINT fk_mtrtb025_mtrtb010 FOREIGN KEY (nu_funcao_documental) REFERENCES mtrtb010_funcao_documental(nu_funcao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7138 (class 2606 OID 2026229)
-- Name: fk_mtrtb025_mtrtb020; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb025_processo_documento
    ADD CONSTRAINT fk_mtrtb025_mtrtb020 FOREIGN KEY (nu_processo) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7171 (class 2606 OID 2026234)
-- Name: fk_mtrtb026_mtrtb020_01; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb026_relacao_processo
    ADD CONSTRAINT fk_mtrtb026_mtrtb020_01 FOREIGN KEY (nu_processo_pai) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7170 (class 2606 OID 2026239)
-- Name: fk_mtrtb026_mtrtb020_02; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb026_relacao_processo
    ADD CONSTRAINT fk_mtrtb026_mtrtb020_02 FOREIGN KEY (nu_processo_filho) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7141 (class 2606 OID 2026244)
-- Name: fk_mtrtb028_mtrtb027; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb028_opcao_campo
    ADD CONSTRAINT fk_mtrtb028_mtrtb027 FOREIGN KEY (nu_campo_entrada) REFERENCES mtrtb027_campo_entrada(nu_campo_entrada) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7142 (class 2606 OID 2026249)
-- Name: fk_mtrtb029_mtrtb019; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb029_campo_apresentacao
    ADD CONSTRAINT fk_mtrtb029_mtrtb019 FOREIGN KEY (nu_campo_formulario) REFERENCES mtrtb019_campo_formulario(nu_campo_formulario) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7144 (class 2606 OID 2026254)
-- Name: fk_mtrtb030_mtrtb002; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb030_resposta_dossie
    ADD CONSTRAINT fk_mtrtb030_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7143 (class 2606 OID 2026259)
-- Name: fk_mtrtb030_mtrtb019; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb030_resposta_dossie
    ADD CONSTRAINT fk_mtrtb030_mtrtb019 FOREIGN KEY (nu_campo_formulario) REFERENCES mtrtb019_campo_formulario(nu_campo_formulario) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7173 (class 2606 OID 2026264)
-- Name: fk_mtrtb031_mtrtb028; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb031_resposta_opcao
    ADD CONSTRAINT fk_mtrtb031_mtrtb028 FOREIGN KEY (nu_opcao_campo) REFERENCES mtrtb028_opcao_campo(nu_opcao_campo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7172 (class 2606 OID 2026269)
-- Name: fk_mtrtb031_mtrtb030; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb031_resposta_opcao
    ADD CONSTRAINT fk_mtrtb031_mtrtb030 FOREIGN KEY (nu_resposta_dossie) REFERENCES mtrtb030_resposta_dossie(nu_resposta_dossie) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7177 (class 2606 OID 2026274)
-- Name: fk_mtrtb032_mtrtb009; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb032_elemento_conteudo
    ADD CONSTRAINT fk_mtrtb032_mtrtb009 FOREIGN KEY (nu_tipo_documento) REFERENCES mtrtb009_tipo_documento(nu_tipo_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7176 (class 2606 OID 2026279)
-- Name: fk_mtrtb032_mtrtb020; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb032_elemento_conteudo
    ADD CONSTRAINT fk_mtrtb032_mtrtb020 FOREIGN KEY (nu_processo) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7175 (class 2606 OID 2026284)
-- Name: fk_mtrtb032_mtrtb022; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb032_elemento_conteudo
    ADD CONSTRAINT fk_mtrtb032_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7174 (class 2606 OID 2026289)
-- Name: fk_mtrtb032_mtrtb032; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb032_elemento_conteudo
    ADD CONSTRAINT fk_mtrtb032_mtrtb032 FOREIGN KEY (nu_elemento_vinculador) REFERENCES mtrtb032_elemento_conteudo(nu_elemento_conteudo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7179 (class 2606 OID 2026294)
-- Name: fk_mtrtb034_mtrtb022; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb034_garantia_produto
    ADD CONSTRAINT fk_mtrtb034_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7178 (class 2606 OID 2026299)
-- Name: fk_mtrtb034_mtrtb033; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb034_garantia_produto
    ADD CONSTRAINT fk_mtrtb034_mtrtb033 FOREIGN KEY (nu_garantia) REFERENCES mtrtb033_garantia(nu_garantia) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7147 (class 2606 OID 2026304)
-- Name: fk_mtrtb035_mtrtb002; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb035_garantia_informada
    ADD CONSTRAINT fk_mtrtb035_mtrtb002 FOREIGN KEY (nu_dossie_produto) REFERENCES mtrtb002_dossie_produto(nu_dossie_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7146 (class 2606 OID 2026309)
-- Name: fk_mtrtb035_mtrtb022; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb035_garantia_informada
    ADD CONSTRAINT fk_mtrtb035_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7145 (class 2606 OID 2026314)
-- Name: fk_mtrtb035_mtrtb033; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb035_garantia_informada
    ADD CONSTRAINT fk_mtrtb035_mtrtb033 FOREIGN KEY (nu_garantia) REFERENCES mtrtb033_garantia(nu_garantia) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7151 (class 2606 OID 2026319)
-- Name: fk_mtrtb037_mtrtb006; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb037_regra_documental
    ADD CONSTRAINT fk_mtrtb037_mtrtb006 FOREIGN KEY (nu_canal_captura) REFERENCES mtrtb006_canal(nu_canal) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7150 (class 2606 OID 2026324)
-- Name: fk_mtrtb037_mtrtb009; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb037_regra_documental
    ADD CONSTRAINT fk_mtrtb037_mtrtb009 FOREIGN KEY (nu_tipo_documento) REFERENCES mtrtb009_tipo_documento(nu_tipo_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7149 (class 2606 OID 2026329)
-- Name: fk_mtrtb037_mtrtb010; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb037_regra_documental
    ADD CONSTRAINT fk_mtrtb037_mtrtb010 FOREIGN KEY (nu_funcao_documental) REFERENCES mtrtb010_funcao_documental(nu_funcao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7148 (class 2606 OID 2026334)
-- Name: fk_mtrtb037_mtrtb036; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb037_regra_documental
    ADD CONSTRAINT fk_mtrtb037_mtrtb036 FOREIGN KEY (nu_composicao_documental) REFERENCES mtrtb036_composicao_documental(nu_composicao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7181 (class 2606 OID 2026339)
-- Name: fk_mtrtb038_mtrtb001; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb038_nivel_documental
    ADD CONSTRAINT fk_mtrtb038_mtrtb001 FOREIGN KEY (nu_dossie_cliente) REFERENCES mtrtb001_dossie_cliente(nu_dossie_cliente) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7180 (class 2606 OID 2026344)
-- Name: fk_mtrtb038_mtrtb036; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb038_nivel_documental
    ADD CONSTRAINT fk_mtrtb038_mtrtb036 FOREIGN KEY (nu_composicao_documental) REFERENCES mtrtb036_composicao_documental(nu_composicao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7183 (class 2606 OID 2026349)
-- Name: fk_mtrtb039_mtrtb022; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb039_produto_composicao
    ADD CONSTRAINT fk_mtrtb039_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7182 (class 2606 OID 2026354)
-- Name: fk_mtrtb039_mtrtb036; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb039_produto_composicao
    ADD CONSTRAINT fk_mtrtb039_mtrtb036 FOREIGN KEY (nu_composicao_documental) REFERENCES mtrtb036_composicao_documental(nu_composicao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7185 (class 2606 OID 2026359)
-- Name: fk_mtrtb040_mtrtb012_01; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb040_cadeia_tipo_sto_dossie
    ADD CONSTRAINT fk_mtrtb040_mtrtb012_01 FOREIGN KEY (nu_tipo_situacao_atual) REFERENCES mtrtb012_tipo_situacao_dossie(nu_tipo_situacao_dossie) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7184 (class 2606 OID 2026364)
-- Name: fk_mtrtb040_mtrtb012_02; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb040_cadeia_tipo_sto_dossie
    ADD CONSTRAINT fk_mtrtb040_mtrtb012_02 FOREIGN KEY (nu_tipo_situacao_seguinte) REFERENCES mtrtb012_tipo_situacao_dossie(nu_tipo_situacao_dossie) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7187 (class 2606 OID 2026369)
-- Name: fk_mtrtb041_mtrtb015_01; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb041_cadeia_stco_documento
    ADD CONSTRAINT fk_mtrtb041_mtrtb015_01 FOREIGN KEY (nu_situacao_documento_atual) REFERENCES mtrtb015_situacao_documento(nu_situacao_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7186 (class 2606 OID 2026374)
-- Name: fk_mtrtb041_mtrtb015_02; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb041_cadeia_stco_documento
    ADD CONSTRAINT fk_mtrtb041_mtrtb015_02 FOREIGN KEY (nu_situacao_documento_seguinte) REFERENCES mtrtb015_situacao_documento(nu_situacao_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7189 (class 2606 OID 2026379)
-- Name: fk_mtrtb042_mtrtb001; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb042_cliente_garantia
    ADD CONSTRAINT fk_mtrtb042_mtrtb001 FOREIGN KEY (nu_dossie_cliente) REFERENCES mtrtb001_dossie_cliente(nu_dossie_cliente) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7166 (class 2606 OID 2026384)
-- Name: fk_mtrtb042_mtrtb027; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb019_campo_formulario
    ADD CONSTRAINT fk_mtrtb042_mtrtb027 FOREIGN KEY (nu_campo_entrada) REFERENCES mtrtb027_campo_entrada(nu_campo_entrada) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7188 (class 2606 OID 2026389)
-- Name: fk_mtrtb042_mtrtb035; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb042_cliente_garantia
    ADD CONSTRAINT fk_mtrtb042_mtrtb035 FOREIGN KEY (nu_garantia_informada) REFERENCES mtrtb035_garantia_informada(nu_garantia_informada) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7193 (class 2606 OID 2026394)
-- Name: fk_mtrtb043_mtrtb009; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb043_documento_garantia
    ADD CONSTRAINT fk_mtrtb043_mtrtb009 FOREIGN KEY (nu_tipo_documento) REFERENCES mtrtb009_tipo_documento(nu_tipo_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7192 (class 2606 OID 2026399)
-- Name: fk_mtrtb043_mtrtb010; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb043_documento_garantia
    ADD CONSTRAINT fk_mtrtb043_mtrtb010 FOREIGN KEY (nu_funcao_documental) REFERENCES mtrtb010_funcao_documental(nu_funcao_documental) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7191 (class 2606 OID 2026404)
-- Name: fk_mtrtb043_mtrtb020; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb043_documento_garantia
    ADD CONSTRAINT fk_mtrtb043_mtrtb020 FOREIGN KEY (nu_processo) REFERENCES mtrtb020_processo(nu_processo) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7190 (class 2606 OID 2026409)
-- Name: fk_mtrtb043_mtrtb033; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb043_documento_garantia
    ADD CONSTRAINT fk_mtrtb043_mtrtb033 FOREIGN KEY (nu_garantia) REFERENCES mtrtb033_garantia(nu_garantia) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7194 (class 2606 OID 2026414)
-- Name: fk_mtrtb044_mtrtb022; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb044_comportamento_pesquisa
    ADD CONSTRAINT fk_mtrtb044_mtrtb022 FOREIGN KEY (nu_produto) REFERENCES mtrtb022_produto(nu_produto) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7195 (class 2606 OID 2026419)
-- Name: fk_mtrtb045_fk_mtrtb0_mtrtb009; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb045_atributo_extracao
    ADD CONSTRAINT fk_mtrtb045_fk_mtrtb0_mtrtb009 FOREIGN KEY (nu_tipo_documento) REFERENCES mtrtb009_tipo_documento(nu_tipo_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7196 (class 2606 OID 2026424)
-- Name: fk_mtrtb047_mtrtb046; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb047_contrato_adm
    ADD CONSTRAINT fk_mtrtb047_mtrtb046 FOREIGN KEY (nu_processo_adm) REFERENCES mtrtb046_processo_adm(nu_processo_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7198 (class 2606 OID 2026429)
-- Name: fk_mtrtb048_mtrtb046; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb048_apenso_adm
    ADD CONSTRAINT fk_mtrtb048_mtrtb046 FOREIGN KEY (nu_processo_adm) REFERENCES mtrtb046_processo_adm(nu_processo_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7197 (class 2606 OID 2026434)
-- Name: fk_mtrtb048_mtrtb047; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb048_apenso_adm
    ADD CONSTRAINT fk_mtrtb048_mtrtb047 FOREIGN KEY (nu_contrato_adm) REFERENCES mtrtb047_contrato_adm(nu_contrato_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7203 (class 2606 OID 2026439)
-- Name: fk_mtrtb049_mtrtb003; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb049_documento_adm
    ADD CONSTRAINT fk_mtrtb049_mtrtb003 FOREIGN KEY (nu_documento) REFERENCES mtrtb003_documento(nu_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7202 (class 2606 OID 2026444)
-- Name: fk_mtrtb049_mtrtb046; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb049_documento_adm
    ADD CONSTRAINT fk_mtrtb049_mtrtb046 FOREIGN KEY (nu_processo_adm) REFERENCES mtrtb046_processo_adm(nu_processo_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7201 (class 2606 OID 2026449)
-- Name: fk_mtrtb049_mtrtb047; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb049_documento_adm
    ADD CONSTRAINT fk_mtrtb049_mtrtb047 FOREIGN KEY (nu_contrato_adm) REFERENCES mtrtb047_contrato_adm(nu_contrato_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7200 (class 2606 OID 2026454)
-- Name: fk_mtrtb049_mtrtb048; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb049_documento_adm
    ADD CONSTRAINT fk_mtrtb049_mtrtb048 FOREIGN KEY (nu_apenso_adm) REFERENCES mtrtb048_apenso_adm(nu_apenso_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7199 (class 2606 OID 2026459)
-- Name: fk_mtrtb049_mtrtb049; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb049_documento_adm
    ADD CONSTRAINT fk_mtrtb049_mtrtb049 FOREIGN KEY (nu_documento_substituto) REFERENCES mtrtb049_documento_adm(nu_documento_adm) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7152 (class 2606 OID 2026464)
-- Name: fk_mtrtb101_mtrtb100; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb101_documento
    ADD CONSTRAINT fk_mtrtb101_mtrtb100 FOREIGN KEY (nu_autorizacao) REFERENCES mtrtb100_autorizacao(nu_autorizacao) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 7204 (class 2606 OID 2026469)
-- Name: fk_mtrtb103_mtrtb100; Type: FK CONSTRAINT; Schema: mtr; Owner: -
--

ALTER TABLE ONLY mtrtb103_autorizacao_orientacao
    ADD CONSTRAINT fk_mtrtb103_mtrtb100 FOREIGN KEY (nu_autorizacao) REFERENCES mtrtb100_autorizacao(nu_autorizacao) ON UPDATE RESTRICT ON DELETE RESTRICT;


-- Completed on 2018-10-31 15:07:57

--
-- PostgreSQL database dump complete
--

