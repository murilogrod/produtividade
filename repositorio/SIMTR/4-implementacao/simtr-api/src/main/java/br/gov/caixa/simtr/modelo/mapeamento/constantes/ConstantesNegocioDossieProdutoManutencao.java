package br.gov.caixa.simtr.modelo.mapeamento.constantes;

/**
 *
 * @author c090347
 */
public final class ConstantesNegocioDossieProdutoManutencao {

    private ConstantesNegocioDossieProdutoManutencao() {
    }

    //************* API MODEL *************
    public static final String API_MODEL_V1_ATRIBUTO_DOCUMENTO = "v1.negocio.dossieproduto.manutencao.AtributoDocumentoDTO";
    public static final String API_MODEL_V1_DOCUMENTO = "v1.negocio.dossieproduto.manutencao.DocumentoDTO";
    public static final String API_MODEL_V1_DOSSIE_PRODUTO_ALTERACAO = "v1.negocio.dossieproduto.manutencao.DossieProdutoAlteracaoDTO";
    public static final String API_MODEL_V1_DOSSIE_PRODUTO_INCLUSAO = "v1.negocio.dossieproduto.manutencao.DossieProdutoInclusaoDTO";
    public static final String API_MODEL_V1_ELEMENTO_CONTEUDO = "v1.negocio.dossieproduto.manutencao.ElementoConteudoDTO";
    public static final String API_MODEL_V1_GARANTIA_INFORMADA = "v1.negocio.dossieproduto.manutencao.GarantiaInformadaDTO";
    public static final String API_MODEL_V1_GARANTIA_INFORMADA_ALTERACAO = "v1.negocio.dossieproduto.manutencao.GarantiaInformadaAlteracaoDTO";
    public static final String API_MODEL_V1_PRODUTO_CONTRATADO = "v1.negocio.dossieproduto.manutencao.ProdutoContratadoDTO";
    public static final String API_MODEL_V1_PRODUTO_CONTRATADO_ALTERACAO = "v1.negocio.dossieproduto.manutencao.ProdutoContratadoAlteracaoDTO";
    public static final String API_MODEL_V1_RESPOSTA_FORMULARIO = "v1.negocio.dossieproduto.manutencao.RespostaFormularioDTO";
    public static final String API_MODEL_V1_VINCULO_PESSOA = "v1.negocio.dossieproduto.manutencao.VinculoPessoaDTO";
    public static final String API_MODEL_V1_VINCULO_PESSOA_ALTERACAO = "v1.negocio.dossieproduto.manutencao.VinculoPessoaAlteracaoDTO";
    
    public static final String API_MODEL_V1_PROCESSO = "v1.negocio.dossieproduto.complementacao.ProcessoDTO";

    //************* XML Root *************
    public static final String XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO = "atributo_documento";
    public static final String XML_ROOT_ELEMENT_DOCUMENTO = "documento";
    public static final String XML_ROOT_ELEMENT_DOSSIE_PRODUTO_ALTERACAO = "dossie_produto_alteracao";
    public static final String XML_ROOT_ELEMENT_DOSSIE_PRODUTO_INCLUSAO = "dossie_produto_inclusao";
    public static final String XML_ROOT_ELEMENT_ELEMENTO_CONTEUDO = "elemento_conteudo";
    public static final String XML_ROOT_ELEMENT_GARANTIA_INFORMADA = "garantia_informada";
    public static final String XML_ROOT_ELEMENT_GARANTIA_INFORMADA_ALTERACAO = "garantia_informada_alteracao";
    public static final String XML_ROOT_ELEMENT_PRODUTO_CONTRATADO = "produto_contratado";
    public static final String XML_ROOT_ELEMENT_PRODUTO_CONTRATADO_ALTERACAO = "produto_contratado_alteracao";
    public static final String XML_ROOT_ELEMENT_PROCESSO = "processo";
    public static final String XML_ROOT_ELEMENT_RESPOSTA_FORMULARIO = "resposta_formulario";
    public static final String XML_ROOT_ELEMENT_VINCULO_PESSOA = "vinculo_pessoa";
    public static final String XML_ROOT_ELEMENT_VINCULO_PESSOA_ALTERACAO = "vinculo_pessoa_alteracao";
    

    //************* NOMES DE ATRIBUTOS *************
    //Comuns
    public static final String DOCUMENTO = "documento";//Elemento_Conteudo e Garantia_Informada
    public static final String EXCLUSAO = "exclusao";//Produto_Contratado_Alteracao, Garantia_Informada_Alteracao e Vinculo_Pessoa_Alteracao
    public static final String ID = "id"; //Produto_Contratado e Garantia_Informada
    public static final String VALOR = "valor"; //Documento e Produto_Contratado
    
    //Atributos Dossiê Produto
    public static final String CANCELAMENTO = "cancelamento";
    public static final String FINALIZACAO = "finalizacao";
    public static final String JUSTIFICATIVA = "justificativa";
    public static final String PROCESSO_ORIGEM = "processo_origem";
    public static final String RASCUNHO = "rascunho";
    public static final String RETORNO = "retorno";
    /////////////////
    public static final String ELEMENTO_CONTEUDO = "elemento_conteudo";
    public static final String ELEMENTOS_CONTEUDO = "elementos_conteudo";
    public static final String GARANTIA_INFORMADA = "garantia_informada";
    public static final String GARANTIAS_INFORMADAS = "garantias_informadas";
    public static final String PRODUTO_CONTRATADO = "produto_contratado";
    public static final String PRODUTOS_CONTRATADOS = "produtos_contratados";
    public static final String RESPOSTA_FORMULARIO = "resposta_formulario";
    public static final String RESPOSTAS_FORMULARIO = "respostas_formulario";
    public static final String VINCULO_PESSOA = "vinculo_pessoa";
    public static final String VINCULOS_PESSOAS = "vinculos_pessoas";

    //Atributos Elemento Conteudo e Documento
    public static final String ATRIBUTO = "atributo";
    public static final String ATRIBUTOS = "atributos";
    public static final String CHAVE = "chave";
    public static final String BINARIO = "binario";
    public static final String IDENTIFICADOR_ELEMENTO = "identificador_elemento";
    public static final String MIME_TYPE = "mime_type";
    public static final String ORIGEM_DOCUMENTO = "origem_documento";
    public static final String TIPO_DOCUMENTO = "tipo_documento";
    
    //Formulário
    public static final String CAMPO_FORMULARIO = "campo_formulario";
    public static final String RESPOSTA = "resposta";
    public static final String OPCAO_SELECIONADA = "opcao_selecionada";
    public static final String OPCOES_SELECIONADAS = "opcoes_selecionadas";
    
    //Vinculo de Pessoas
    public static final String DOCUMENTO_NOVO = "documento_novo";
    public static final String DOCUMENTOS_NOVOS = "documentos_novos";
    public static final String DOCUMENTO_UTILIZADO = "documento_utilizado";
    public static final String DOCUMENTOS_UTILIZADOS = "documentos_utilizados";
    public static final String DOSSIE_CLIENTE = "dossie_cliente";
    public static final String DOSSIE_CLIENTE_RELACIONADO = "dossie_cliente_relacionado";
    public static final String DOSSIE_CLIENTE_RELACIONADO_ANTERIOR = "dossie_cliente_relacionado_anterior";
    public static final String SEQUENCIA_TITULARIDADE = "sequencia_titularidade";
    public static final String SEQUENCIA_TITULARIDADE_ANTERIOR = "sequencia_titularidade_anterior";
    public static final String TIPO_RELACIONAMENTO = "tipo_relacionamento";

    //Atributos do Produto Contratado
    public static final String CARENCIA = "carencia";
    public static final String CODIGO_MODALIDADE = "codigo_modalidade";
    public static final String CODIGO_OPERACAO = "codigo_operacao";
    public static final String LIQUIDACAO = "liquidacao";
    public static final String NUMERO_CONTRATO = "numero_contrato";
    public static final String PERIODO_JUROS = "periodo_juros";
    public static final String PRAZO = "prazo";
    public static final String TAXA_JUROS = "taxa_juros";

    //Atributos da Garantia Informada
    public static final String CLIENTES_AVALISTAS = "clientes_avalistas";
    public static final String DESCRICAO = "descricao";
    public static final String FORMA_GARANTIA = "forma_garantia";
    public static final String IDENTIFICADOR_GARANTIA = "identificador_garantia";
    public static final String IDENTIFICADOR_PRODUTO = "identificador_produto";
    public static final String PERCENTUAL_GARANTIA = "percentual_garantia";
    public static final String VALOR_GARANTIA = "valor_garantia";
    
    
    //************ OBJETOS DA COMPLEMENTAÇÃO *************//
    //Processo
    public static final String AVATAR = "avatar";
    public static final String QUANTIDADE_DOSSIES = "quantidade_dossies";
    public static final String NOME = "nome";
    public static final String PROCESSO_FILHO = "processo_filho";
    public static final String PROCESSOS_FILHO = "processos_filho";
    public static final String UNIDADE_AUTORIZADA = "unidade_autorizada";
    public static final String UNIDADES_AUTORIZADAS = "unidades_autorizadas";
    
    
}
