/***************************
	DOSSIE DIGITAL
***************************/
--Remove o nivel documental dos clientes
DELETE FROM mtrsm001.mtrtb038_nivel_documental;

--Remove os documentos carrgados
DELETE FROM mtrsm001.mtrtb007_atributo_documento;
DELETE FROM mtrsm001.mtrtb005_documento_cliente;
DELETE FROM mtrsm001.mtrtb003_documento WHERE ic_dossie_digital = true;

--Remove as autorizacoes
DELETE FROM mtrsm001.mtrtb102_autorizacao_negada;
DELETE FROM mtrsm001.mtrtb103_autorizacao_orientacao;
DELETE FROM mtrsm001.mtrtb101_documento;
DELETE FROM mtrsm001.mtrtb100_autorizacao;

/***************************
	PAE
***************************/
--Remove os documentos carrgados
DELETE FROM mtrsm001.mtrtb007_atributo_documento WHERE nu_documento IN (SELECT nu_documento FROM mtrsm001.mtrtb049_documento_adm);
DELETE FROM mtrsm001.mtrtb003_documento WHERE ic_dossie_digital = false AND nu_documento IN (SELECT nu_documento FROM mtrsm001.mtrtb049_documento_adm);
DELETE FROM mtrsm001.mtrtb049_documento_adm;

--Remove as definidções de unidades autorizadas 
DELETE FROM mtrsm001.mtrtb021_unidade_autorizada WHERE nu_apenso_adm IS NOT NULL OR nu_contrato_adm IS NOT NULL OR nu_processo_adm IS NOT NULL;

--Remove os apensos administrativos
DELETE FROM mtrsm001.mtrtb048_apenso_adm;

--Remove os contratos administrativos
DELETE FROM mtrsm001.mtrtb047_contrato_adm;

--Remove os processos administrativos
DELETE FROM mtrsm001.mtrtb046_processo_adm;

/***************************
	APOIO NEGOCIO
***************************/
--Remove as informações dos documentos carregados
DELETE FROM mtrsm001.mtrtb017_stco_instnca_documento;
DELETE FROM mtrsm001.mtrtb014_instancia_documento;
DELETE FROM mtrsm001.mtrtb007_atributo_documento;
DELETE FROM mtrsm001.mtrtb005_documento_cliente;
DELETE FROM mtrsm002.mtrtb008_conteudo;
DELETE FROM mtrsm001.mtrtb003_documento WHERE ic_dossie_digital = false;

--Remove as garantias informadas
DELETE FROM mtrsm001.mtrtb042_cliente_garantia;
DELETE FROM mtrsm001.mtrtb035_garantia_informada;

--Remove as respostas de formulario
DELETE FROM mtrsm001.mtrtb031_resposta_opcao;
DELETE FROM mtrsm001.mtrtb030_resposta_dossie;

--Remove as informações dos dossiês de produto
DELETE FROM mtrsm001.mtrtb004_dossie_cliente_produto;
DELETE FROM mtrsm001.mtrtb018_unidade_tratamento;
DELETE FROM mtrsm001.mtrtb013_situacao_dossie;
DELETE FROM mtrsm001.mtrtb024_produto_dossie;
DELETE FROM mtrsm001.mtrtb002_dossie_produto;

-- Remove informações dos dossiês de cliente
DELETE FROM mtrsm001.mtrtb001_pessoa_fisica ;
DELETE FROM mtrsm001.mtrtb001_pessoa_juridica;
DELETE FROM mtrsm001.mtrtb001_dossie_cliente WHERE nu_dossie_cliente NOT IN (SELECT nu_dossie_cliente FROM mtrsm001.mtrtb038_nivel_documental);


