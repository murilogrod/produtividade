package br.gov.caixa.simtr.modelo.mapeamento.constantes;

/**
 *
 * @author c090347
 */
public final class ConstantesNegocioProcesso {

    private ConstantesNegocioProcesso() {
    }

    //************* API MODEL *************
    public static final String API_MODEL_V1_APONTAMENTO = "v1.negocio.processo.ApontamentoDTO";
    public static final String API_MODEL_V1_CAMPO_APRESENTACAO = "v1.negocio.processo.CampoApresentacaoDTO";
    public static final String API_MODEL_V1_CAMPO_FORMULARIO = "v1.negocio.processo.CampoFormularioDTO";
    public static final String API_MODEL_V1_CHECKLIST = "v1.negocio.processo.ChecklistDTO";
    public static final String API_MODEL_V1_DOCUMENTO_VINCULO = "v1.negocio.processo.DocumentoVinculoDTO";
    public static final String API_MODEL_V1_ELEMENTO_CONTEUDO = "v1.negocio.processo.ElementoConteudoDTO";
    public static final String API_MODEL_V1_FUNCAO_DOCUMENTAL = "v1.negocio.processo.FuncaoDocumentalDTO";
    public static final String API_MODEL_V1_FUNCAO_DOCUMENTAL_SIMPLES = "v1.negocio.processo.FuncaoDocumentalSimplesDTO";
    public static final String API_MODEL_V1_GARANTIA = "v1.negocio.processo.GarantiaDTO";
    public static final String API_MODEL_V1_MACRO_PROCESSO = "v1.negocio.processo.MacroProcessoDTO";
    public static final String API_MODEL_V1_OPCAO_CAMPO = "v1.negocio.processo.OpcaoCampoDTO";
    public static final String API_MODEL_V1_PROCESSO_DOSSIE = "v1.negocio.processo.ProcessoDossieDTO";
    public static final String API_MODEL_V1_PROCESSO_FASE = "v1.negocio.processo.ProcessoFaseDTO";
    public static final String API_MODEL_V1_PRODUTO = "v1.negocio.processo.ProdutoDTO";
    public static final String API_MODEL_V1_TIPO_DOCUMENTO = "v1.negocio.processo.TipoDocumentoDTO";
    public static final String API_MODEL_V1_TIPO_RELACIONAMENTO = "v1.negocio.processo.TipoRelacionamentoDTO";
    public static final String API_MODEL_V1_UNIDADE_AUTORIZADA = "v1.negocio.processo.UnidadeAutorizadaDTO";

    //************* XML Root *************
    public static final String XML_ROOT_ELEMENT_APONTAMENTO = "apontamento";
    public static final String XML_ROOT_ELEMENT_CAMPO_APRESENTACAO = "campo_apresentacao";
    public static final String XML_ROOT_ELEMENT_CAMPO_FORMULARIO = "campo_formulario";
    public static final String XML_ROOT_ELEMENT_CHECKLIST = "checklist";
    public static final String XML_ROOT_ELEMENT_DOCUMENTO_VINCULO = "documento_vinculo";
    public static final String XML_ROOT_ELEMENT_ELEMENTO_CONTEUDO = "elemento_conteudo";
    public static final String XML_ROOT_ELEMENT_FUNCAO_DOCUMENTAL = "funcao_documental";
    public static final String XML_ROOT_ELEMENT_FUNCAO_DOCUMENTAL_SIMPLES = "funcao_documental_simples";
    public static final String XML_ROOT_ELEMENT_GARANTIA = "garantia";
    public static final String XML_ROOT_ELEMENT_MACRO_PROCESSO = "macro_processo";
    public static final String XML_ROOT_ELEMENT_OPCAO_CAMPO = "opcao_campo";
    public static final String XML_ROOT_ELEMENT_PROCESSO_DOSSIE = "processo_dossie";
    public static final String XML_ROOT_ELEMENT_PROCESSO_FASE = "processo_fases";
    public static final String XML_ROOT_ELEMENT_PRODUTO = "produto";
    public static final String XML_ROOT_ELEMENT_TIPO_DOCUMENTO = "tipo_documento";
    public static final String XML_ROOT_ELEMENT_TIPO_RELACIONAMENTO = "tipo_relacionamento";
    public static final String XML_ROOT_ELEMENT_UNIDADE_AUTORIZADA = "unidade_autorizada";

    //************* NOMES DE ATRIBUTOS *************
    //Atributos Comuns
    public static final String EXPRESSAO_INTERFACE = "expressao_interface";
    public static final String ID = "id";
    public static final String INTEGRACAO = "integracao";
    public static final String LABEL = "label";
    public static final String NOME = "nome";
    public static final String OBRIGATORIO = "obrigatorio";

    //Atributos Checklists
    public static final String APONTAMENTO = "apontamento";
    public static final String APONTAMENTOS = "apontamentos";
    public static final String DATA_REVOGACAO = "data_revogacao";
    public static final String DESCRICAO = "descricao";
    public static final String TITULO = "titulo";
    public static final String VERIFICACAO_PREVIA = "verificacao_previa";

    //Atributos Elemento Conteudo e Formulário
    public static final String DESCRICAO_OPCAO = "descricao_opcao";
    public static final String FORMA_APRESENTACAO = "forma_apresentacao";
    public static final String LARGURA = "largura";
    public static final String LISTA_APRESENTACOES = "lista_apresentacoes";
    public static final String MASCARA_CAMPO = "mascara_campo";
    public static final String NOME_CAMPO = "nome_campo";
    public static final String OPCAO_DISPONIVEL = "opcao_disponivel";
    public static final String OPCOES_DISPONIVEIS = "opcoes_disponiveis";
    public static final String ORDEM_APRESENTACAO = "ordem_apresentacao";
    public static final String PLACEHOLDER_CAMPO = "placeholder_campo";
    public static final String TAMANHO_MINIMO = "tamanho_minimo";
    public static final String TAMANHO_MAXIMO = "tamanho_maximo";
    public static final String TIPO_CAMPO = "tipo_campo";
    public static final String TIPO_DISPOSITIVO = "tipo_dispositivo";
    public static final String VALOR_OPCAO = "valor_opcao";

    //Atributos Garantia
    public static final String CODIGO_BACEN = "codigo_bacen";
    public static final String NOME_GARANTIA = "nome_garantia";

    //Atributos Produto
    public static final String CODIGO_MODALIDADE = "codigo_modalidade";
    public static final String CODIGO_OPERACAO = "codigo_operacao";

    //Atributos do Processo
    public static final String AVATAR = "avatar";
    public static final String CAMPO_FORMULARIO = "campo_formulario";
    public static final String CAMPOS_FORMULARIO = "campos_formulario";
    public static final String CHECKLIST = "checklist";
    public static final String CHECKLISTS = "checklists";
    public static final String DOCUMENTO_VINCULO = "documento_vinculo";
    public static final String DOCUMENTOS_VINCULO = "documentos_vinculo";
    public static final String ELEMENTO_CONTEUDO = "elemento_conteudo";
    public static final String ELEMENTOS_CONTEUDO = "elementos_conteudo";
    public static final String FUNCAO_DOCUMENTAL = "funcao_documental";
    public static final String FUNCOES_DOCUMENTAIS = "funcoes_documentais";
    public static final String GARANTIA_VINCULADA = "garantia_vinculada";
    public static final String GARANTIAS_VINCULADAS = "garantias_vinculadas";
    public static final String GERA_DOSSIE = "gera_dossie";
    public static final String INDICADOR_FIDEJUSSORIA = "indicador_fidejussoria";
    public static final String INDICADOR_INTERFACE = "indicador_interface";
    public static final String IDENTIFICADOR_ELEMENTO = "identificador_elemento";
    public static final String IDENTIFICADOR_ELEMENTO_VINCULADOR = "identificador_elemento_vinculador";
    public static final String INDICA_RECEITA_PF = "indica_receita_pf";
    public static final String INDICA_RECEITA_PJ = "indica_receita_pj";
    public static final String INDICA_RELACIONADO = "indica_relacionado";
    public static final String INDICA_SEQUENCIA = "indica_sequencia";
    public static final String NOME_ELEMENTO = "nome_elemento";
    public static final String ORIENTACAO_OPERADOR = "orientacao_operador";
    public static final String ORIENTACAO_USUARIO = "orientacao_usuario";
    public static final String PERMITE_REUSO = "permite_reuso";
    public static final String PRINCIPAL = "principal";
    public static final String PRIORIZADO = "priorizado";
    public static final String PROCESSO_FILHO = "processo_filho";
    public static final String PROCESSOS_FILHO = "processos_filho";
    public static final String PRODUTO_VINCULADO = "produto_vinculado";
    public static final String PRODUTOS_VINCULADOS = "produtos_vinculados";
    public static final String QUANTIDADE_OBRIGATORIOS = "quantidade_obrigatorios";
    public static final String SEQUENCIA = "sequencia";
    public static final String TIPO_DOCUMENTO = "tipo_documento";
    public static final String TIPOS_DOCUMENTO = "tipos_documento";
    public static final String TIPO_PESSOA = "tipo_pessoa";
    public static final String TIPO_RELACIONAMENTO = "tipo_relacionamento";
    public static final String TIPOS_RELACIONAMENTO = "tipos_relacionamento";
    public static final String TRATAMENTO_AUTORIZADO = "tratamento_autorizado";
    public static final String TRATAMENTOS_AUTORIZADOS = "tratamentos_autorizados";
    public static final String UNIDADE = "unidade";
    public static final String UNIDADE_AUTORIZADA = "unidade_autorizada";
    public static final String TEMPO_TRATAMENTO = "tempo_tratamento";
    public static final String TRATAMENTO_SELETIVO = "tratamento_seletivo";

}
