--
-- PostgreSQL database dump
-- Aplicado até a versão abaixo do Flyway:
-- 1.0.0.33, do arquivo 'V1_0_0_33__Titulo_Apenso_PAE.sql'
--

-- Dumped from database version 9.4.5
-- Dumped by pg_dump version 9.5.5

-- Started on 2018-10-17 11:44:13

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 20 (class 2615 OID 2026474)
-- Name: mtrsm002; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA mtrsm002;

SET search_path = mtrsm002, pg_catalog;

--
-- TOC entry 1920 (class 1259 OID 2026475)
-- Name: mtrsq008_conteudo; Type: SEQUENCE; Schema: mtrsm002; Owner: -
--
CREATE SEQUENCE mtrsq008_conteudo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 1921 (class 1259 OID 2026477)
-- Name: mtrtb008_conteudo; Type: TABLE; Schema: mtrsm002; Owner: -
--
CREATE TABLE mtrtb008_conteudo (
    nu_conteudo bigint DEFAULT nextval('mtrsq008_conteudo'::regclass) NOT NULL,
    nu_versao integer NOT NULL,
    nu_documento bigint NOT NULL,
    nu_ordem integer NOT NULL,
    de_conteudo text NOT NULL
);

--
-- TOC entry 7150 (class 0 OID 0)
-- Dependencies: 1921
-- Name: TABLE mtrtb008_conteudo; Type: COMMENT; Schema: mtrsm002; Owner: -
--
COMMENT ON TABLE mtrtb008_conteudo IS 'Tabela responsavel pelo armazenamento das referencia de conteudo que compoem o documento.
Nesta tabela serão efetivamente armazenados os dados que caracterizam o conteudo do documento (ou o binario) para manipulação da aplicação até o momento que o documento será efetivamente arquivado no repositorio em carater definitivo.';
COMMENT ON COLUMN mtrtb008_conteudo.nu_conteudo IS 'Atributo que representa a chave primaria da entidade.';
COMMENT ON COLUMN mtrtb008_conteudo.nu_versao IS 'Campo de controle das versões do registro para viabilizar a concorrencia otimista';
COMMENT ON COLUMN mtrtb008_conteudo.nu_documento IS 'Atributo utilizado para identificar o documento que o conteudo esta vinculado. Neste formato é possivel associar varios conteudos a um mesmo documento.';
COMMENT ON COLUMN mtrtb008_conteudo.nu_ordem IS 'Atributo utilizado para identificar a ordem de exibição na composição do documento. Documentos que possuem apenas um elemento, como um arquivo pdf por exemplo terão apenas um registro de conteudo com o atributo de ordem como 1';
COMMENT ON COLUMN mtrtb008_conteudo.de_conteudo IS 'Atributo utilizado para armazenar o conteudo efetivo do documento em formato base64. Documentos que possuem apenas um elemento, como um arquivo pdf por exemplo terão apenas um registro de conteudo contendo o documento na integra.';

--
-- TOC entry 7156 (class 0 OID 0)
-- Dependencies: 1920
-- Name: mtrsq008_conteudo; Type: SEQUENCE SET; Schema: mtrsm002; Owner: -
--
SELECT pg_catalog.setval('mtrsq008_conteudo', 21, true);


--
-- TOC entry 6841 (class 2606 OID 2026501)
-- Name: pk_mtrtb008; Type: CONSTRAINT; Schema: mtrsm002; Owner: -
--

ALTER TABLE ONLY mtrtb008_conteudo
    ADD CONSTRAINT pk_mtrtb008 PRIMARY KEY (nu_conteudo);

--
-- TOC entry 6839 (class 1259 OID 2026502)
-- Name: ix_mtrtb008_01; Type: INDEX; Schema: mtrsm002; Owner: -
--
CREATE UNIQUE INDEX ix_mtrtb008_01 ON mtrtb008_conteudo USING btree (nu_documento, nu_ordem);


--
-- TOC entry 6842 (class 2606 OID 2026503)
-- Name: fk_mtrtb008_mtrtb003; Type: FK CONSTRAINT; Schema: mtrsm002; Owner: -
--
ALTER TABLE ONLY mtrtb008_conteudo
    ADD CONSTRAINT fk_mtrtb008_mtrtb003 FOREIGN KEY (nu_documento) REFERENCES mtrsm001.mtrtb003_documento(nu_documento) ON UPDATE RESTRICT ON DELETE RESTRICT;


-- Completed on 2018-10-17 11:44:20

--
-- PostgreSQL database dump complete
--

