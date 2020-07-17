package br.gov.caixa.simtr.modelo.mapeamento.constantes;

/**
 *
 * @author c090347
 */
public final class ConstantesNegocioDossieCliente {

    private ConstantesNegocioDossieCliente() {
    }

    //************* API MODEL *************
    public static final String API_MODEL_V1_ATRIBUTO_DOCUMENTO = "v1.negocio.dossiecliente.AtributoDocumentoDTO";
    public static final String API_MODEL_V1_CANAL = "v1.negocio.dossiecliente.CanalDTO";
    public static final String API_MODEL_V1_CONTEUDO = "v1.negocio.dossiecliente.ConteudoDTO";
    public static final String API_MODEL_V1_DOCUMENTO = "v1.negocio.dossiecliente.DocumentoDTO";
    public static final String API_MODEL_V1_DOSSIE_CLIENTE = "v1.negocio.dossiecliente.DossieClienteDTO";
    public static final String API_MODEL_V1_DOSSIE_CLIENTE_PF = "v1.negocio.dossiecliente.DossieClientePFDTO";
    public static final String API_MODEL_V1_DOSSIE_CLIENTE_PJ = "v1.negocio.dossiecliente.DossieClientePJDTO";
    public static final String API_MODEL_V1_DOSSIE_CLIENTE_DOC = "v1.negocio.dossiecliente.DossieClienteDOC";
    public static final String API_MODEL_V1_DOSSIE_PRODUTO = "v1.negocio.dossiecliente.DossieProdutoDTO";
    public static final String API_MODEL_V1_FUNCAO_DOCUMENTAL = "v1.negocio.dossiecliente.FuncaoDocumentalDTO";
    public static final String API_MODEL_V1_NIVEL_DOCUMENTAL = "v1.negocio.dossiecliente.NivelDocumentalDTO";
    public static final String API_MODEL_V1_PROCESSO = "v1.negocio.dossiecliente.ProcessoDTO";
    public static final String API_MODEL_V1_PRODUTO_CONTRATADO = "v1.negocio.dossiecliente.ProdutoContratadoDTO";
    public static final String API_MODEL_V1_PRODUTO_HABILITADO = "v1.negocio.dossiecliente.ProdutoHabilitadoDTO";
    public static final String API_MODEL_V1_TIPO_DOCUMENTO = "v1.negocio.dossiecliente.TipoDocumentoDTO";

    //************* XML Root *************
    public static final String XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO = "atributo_documento";
    public static final String XML_ROOT_ELEMENT_CANAL = "canal";
    public static final String XML_ROOT_ELEMENT_CONTEUDO = "conteudo";
    public static final String XML_ROOT_ELEMENT_DOCUMENTO = "documento";
    public static final String XML_ROOT_ELEMENT_DOSSIE_CLIENTE = "dossie_cliente";
    public static final String XML_ROOT_ELEMENT_DOSSIE_CLIENTE_PF = "dossie_cliente_pf";
    public static final String XML_ROOT_ELEMENT_DOSSIE_CLIENTE_PJ = "dossie_cliente_pj";
    public static final String XML_ROOT_ELEMENT_DOSSIE_PRODUTO = "dossie_produto";
    public static final String XML_ROOT_ELEMENT_FUNCAO_DOCUMENTAL = "funcao_documental";
    public static final String XML_ROOT_ELEMENT_NIVEL_DOCUMENTAL = "nivel_documental";
    public static final String XML_ROOT_ELEMENT_PROCESSO = "processo";
    public static final String XML_ROOT_ELEMENT_PRODUTO_CONTRATADO = "produto_contratado";
    public static final String XML_ROOT_ELEMENT_PRODUTO_HABILITADO = "produto_habilitado";
    public static final String XML_ROOT_ELEMENT_TIPO_DOCUMENTO = "tipo_documento";

    //************* NOMES DE ATRIBUTOS *************
    //Comuns
    public static final String CANAL_CAIXA = "canal_caixa";//canal, dossie produto
    public static final String ID = "id"; //dossie_cliente, documento, funcao_documental, processo, tipo_documento
    public static final String NOME = "nome";//dossie_cliente, funcao_documental, produto_contratado, produto_habilitado, processo, tipo_documento

    //Canal
    public static final String DESCRICAO_CANAL = "descricao_canal";
    public static final String SIGLA_CANAL = "sigla_canal";

    //Documento e Tipologia
    public static final String BINARIO = "binario";
    public static final String CANAL_CAPTURA = "canal_captura";
    public static final String CHAVE = "chave";
    public static final String CODIGO_GED = "codigo_ged";
    public static final String CONTEUDO = "conteudo";
    public static final String CONTEUDOS = "conteudos";
    public static final String DATA_HORA_CAPTURA = "data_hora_captura";
    public static final String DATA_HORA_VALIDADE = "data_hora_validade";
    public static final String DOSSIE_DIGITAL = "dossie_digital";
    public static final String FUNCAO_DOCUMENTAL = "funcao_documental";
    public static final String FUNCOES_DOCUMENTAIS = "funcoes_documentais";
    public static final String MATRICULA_CAPTURA = "matricula_captura";
    public static final String MIME_TYPE = "mime_type";
    public static final String ORIGEM_DOCUMENTO = "origem_documento";
    public static final String TIPO_DOCUMENTO = "tipo_documento";
    public static final String VALOR = "valor";

    //Dossie Cliente
    public static final String CPF = "cpf";
    public static final String CNPJ = "cnpj";
    public static final String CONGLOMERADO = "conglomerado";
    public static final String DATA_FUNDACAO = "data_fundacao";
    public static final String DATA_NASCIMENTO = "data_nascimento";
    public static final String DATA_HORA_APURACAO_NIVEL = "data_hora_apuracao_nivel";
    public static final String DOCUMENTO = "documento";
    public static final String DOCUMENTOS = "documentos";
    public static final String DOSSIE_PRODUTO = "dossie_produto";
    public static final String DOSSIES_PRODUTO = "dossies_produto";
    public static final String EMAIL = "email";
    public static final String NOME_MAE = "nome_mae";
    public static final String PRODUTO = "produto";
    public static final String PRODUTOS_HABILITADOS = "produtos_habilitados";
    public static final String RAZAO_SOCIAL = "razao_social";
    public static final String TIPO_PESSOA = "tipo_pessoa";
    public static final String SIGLA_PORTE = "sigla_porte";

    //Dossie Produto e Situacao Dossie
    public static final String DATA_HORA_FINALIZACAO = "data_hora_finalizacao";
    public static final String DATA_HORA_SITUACAO = "data_hora_situacao";
    public static final String ID_DOSSIE_PRODUTO = "id_dossie_produto";
    public static final String PROCESSO_DOSSIE = "processo_dossie";
    public static final String PROCESSO_FASE = "processo_fase";
    public static final String PROCESSO_PATRIARCA = "processo_patriarca";
    public static final String PRODUTO_CONTRATADO = "produto_contratado";
    public static final String PRODUTOS_CONTRATADOS = "produtos_contratados";
    public static final String UNIDADES_TRATAMENTO = "unidades_tratamento";
    public static final String UNIDADE_TRATAMENTO = "unidade_tratamento";
    public static final String SITUACAO_ATUAL = "situacao_atual";
    public static final String TIPO_RELACIONAMENTO = "tipo_relacionamento";
    public static final String UNIDADE_CRIACAO = "unidade_criacao";
    public static final String UNIDADE_PRIORIZADO = "unidade_priorizado";
    public static final String UNIDADE_SITUACAO = "unidade_situacao";

    //Nivel Documental e Produtos
    public static final String CODIGO_OPERACAO = "codigo_operacao";
    public static final String CODIGO_MODALIDADE = "codigo_modalidade";
    public static final String VALOR_CONTRATO = "valor_contrato";
}
