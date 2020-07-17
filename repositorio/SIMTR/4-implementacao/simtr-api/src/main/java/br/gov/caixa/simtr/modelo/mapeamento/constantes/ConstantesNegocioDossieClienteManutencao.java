package br.gov.caixa.simtr.modelo.mapeamento.constantes;

/**
 *
 * @author c090347
 */
public final class ConstantesNegocioDossieClienteManutencao {

    private ConstantesNegocioDossieClienteManutencao() {
    }

    //************* API MODEL *************
    public static final String API_MODEL_V1_ATRIBUTO_DOCUMENTO = "v1.negocio.dossiecliente.manutencao.AtributoDocumentoDTO";
    public static final String API_MODEL_V1_ATRIBUTO_DOCUMENTO_SIISO = "v1.negocio.dossiecliente.manutencao.AtributoDocumentoSiisoDTO";
    public static final String API_MODEL_V1_ATRIBUTO_DOCUMENTO_SIRIC = "v1.negocio.dossiecliente.manutencao.ProdutoAvalidadorTomadorDTO";
    public static final String API_MODEL_V1_ATRIBUTO_MOTIVO_REPROVACAO = "v1.negocio.dossiecliente.manutencao.MotivoReprovacaoDTO";
    public static final String API_MODEL_V1_DOCUMENTO_INCLUSAO = "v1.negocio.dossiecliente.manutencao.DocumentoInclusaoDTO";
    public static final String API_MODEL_V1_DOSSIE_CLIENTE_ALTERACAO = "v1.negocio.dossiecliente.manutencao.DossieClienteAlteracaoDTO";
    public static final String API_MODEL_V1_DOSSIE_CLIENTE_ALTERACAO_PF = "v1.negocio.dossiecliente.manutencao.DossieClienteAlteracaoPFDTO";
    public static final String API_MODEL_V1_DOSSIE_CLIENTE_ALTERACAO_PJ = "v1.negocio.dossiecliente.manutencao.DossieClienteAlteracaoPJDTO";
    public static final String API_MODEL_V1_DOSSIE_CLIENTE_INCLUSAO = "v1.negocio.dossiecliente.manutencao.DossieClienteInclusaoDTO";
    public static final String API_MODEL_V1_DOSSIE_CLIENTE_INCLUSAO_PF = "v1.negocio.dossiecliente.manutencao.DossieClienteInclusaoPFDTO";
    public static final String API_MODEL_V1_DOSSIE_CLIENTE_INCLUSAO_PJ = "v1.negocio.dossiecliente.manutencao.DossieClienteInclusaoPJDTO";

    //************* XML Root *************
    public static final String XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO = "atributo_documento";
    public static final String XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO_SIRIC = "atributo_documento_siric";
    public static final String XML_ROOT_ELEMENT_ATRIBUTO_MOTIVO_REPROVACAO = "atributo_motivo_reprovacao";
    public static final String XML_ROOT_ELEMENT_DOCUMENTO = "documento";
    public static final String XML_ROOT_ELEMENT_DOSSIE_CLIENTE_ALTERACAO = "dossie_cliente_alteracao";
    public static final String XML_ROOT_ELEMENT_DOSSIE_CLIENTE_ALTERACAO_PF = "dossie_cliente_alteracao_pf";
    public static final String XML_ROOT_ELEMENT_DOSSIE_CLIENTE_ALTERACAO_PJ = "dossie_cliente_alteracao_pj";
    public static final String XML_ROOT_ELEMENT_DOSSIE_CLIENTE_INCLUSAO = "dossie_cliente_inclusao";
    public static final String XML_ROOT_ELEMENT_DOSSIE_CLIENTE_INCLUSAO_PF = "dossie_cliente_inclusao_pf";
    public static final String XML_ROOT_ELEMENT_DOSSIE_CLIENTE_INCLUSAO_PJ = "dossie_cliente_inclusao_pj";

    //************* NOMES DE ATRIBUTOS *************
    //Documento e Tipologia
    public static final String ATRIBUTO = "atributo";
    public static final String ATRIBUTOS = "atributos";
    public static final String BINARIO = "binario";
    public static final String CHAVE = "chave";
    public static final String CODIGO_INTEGRACAO = "codigo_integracao";
    public static final String IMAGEM = "imagem";
    public static final String IMAGENS = "imagens";
    public static final String MIME_TYPE = "mime_type";
    public static final String ORIGEM_DOCUMENTO = "origem_documento";
    public static final String TIPO_DOCUMENTO = "tipo_documento";
    public static final String VALOR = "valor";
    public static final String ENDERECO = "endereco";
    public static final String ESTABELECIMENTO = "estabelecimento";
    public static final String SOCIOS = "socios";
    public static final String SUCESSAO = "sucessao";
    
    //************* NOMES DE PRODUTO SIRIC *************
    public static final String RATING = "rating";
    public static final String CODIGO_MODALIDADE = "codigo_modalidade";
    public static final String CODIGO_PRODUTO = "codigo_produto";
    public static final String RESULTADO_AVALIACAO = "resultado_avaliacao";
    public static final String VALOR_DISPONIVEL = "valor_disponivel";
    public static final String VALOR_CALCULADO_TOTAL = "valor_calculado_total";
    public static final String PRAZO_MESES = "prazo_meses";
    public static final String MORIVOS_REPROVACAO = "motivo_reprovacao";

    //Dossie Cliente
    public static final String CPF = "cpf";
    public static final String CNPJ = "cnpj";
    public static final String CONGLOMERADO = "conglomerado";
    public static final String DATA_FUNDACAO = "data_fundacao";
    public static final String DATA_NASCIMENTO = "data_nascimento";
    public static final String EMAIL = "email";
    public static final String NOME = "nome";
    public static final String NOME_MAE = "nome_mae";
    public static final String OPCAO_SELECIONADA = "opcao_selecionada";
    public static final String OPCOES_SELECIONADAS = "opcoes_selecionadas";
    public static final String RAZAO_SOCIAL = "razao_social";
    public static final String TIPO_PESSOA = "tipo_pessoa";
    public static final String SIGLA_PORTE = "sigla_porte";
    public static final String OBJETO = "objeto";
}
