package br.gov.caixa.simtr.modelo.mapeamento.constantes;

/**
 *
 * @author c090347
 */
public final class ConstantesNegocioDossieProduto {

    private ConstantesNegocioDossieProduto() {
    }

    //************* API MODEL *************
    public static final String API_MODEL_V1_CANAL = "v1.negocio.dossieproduto.CanalDTO";
    public static final String API_MODEL_V1_CHECKLIST = "v1.negocio.dossieproduto.ChecklistDTO";
    public static final String API_MODEL_V1_CONTEUDO = "v1.negocio.dossieproduto.ConteudoDTO";
    public static final String API_MODEL_V1_DOCUMENTO = "v1.negocio.dossieproduto.DocumentoDTO";
    public static final String API_MODEL_V1_DOSSIE_CLIENTE = "v1.negocio.dossieproduto.DossieClienteDTO";
    public static final String API_MODEL_V1_DOSSIE_CLIENTE_PF = "v1.negocio.dossieproduto.DossieClientePFDTO";
    public static final String API_MODEL_V1_DOSSIE_CLIENTE_PJ = "v1.negocio.dossieproduto.DossieClientePJDTO";
    public static final String API_MODEL_V1_DOSSIE_PRODUTO = "v1.negocio.dossieproduto.DossieProdutoDTO";
    public static final String API_MODEL_V1_GARANTIA_INFORMADA = "v1.negocio.dossieproduto.GarantiaInformadaDTO";
    public static final String API_MODEL_V1_INSTANCIA_DOCUMENTO = "v1.negocio.dossieproduto.InstanciaDocumentoDTO";
    public static final String API_MODEL_V1_OPCAO_CAMPO = "v1.negocio.dossieproduto.OpcaoCampoDTO";
    public static final String API_MODEL_V1_PARECER = "v1.negocio.dossieproduto.ParecerDTO";
    public static final String API_MODEL_V1_PROCESSO = "v1.negocio.dossieproduto.ProcessoDTO";
    public static final String API_MODEL_V1_PRODUTO_CONTRATADO = "v1.negocio.dossieproduto.ProdutoContratadoDTO";
    public static final String API_MODEL_V1_RESPOSTA_FORMULARIO = "v1.negocio.dossieproduto.RespostaFormularioDTO";
    public static final String API_MODEL_V1_SITUACAO_DOSSIE = "v1.negocio.dossieproduto.SituacaoDossieDTO";
    public static final String API_MODEL_V1_SITUACAO_INSTANCIA = "v1.negocio.dossieproduto.SituacaoInstanciaDTO";
    public static final String API_MODEL_V1_TIPO_DOCUMENTO = "v1.negocio.dossieproduto.TipoDocumentoDTO";
    public static final String API_MODEL_V1_TIPO_RELACIONAMENTO = "v1.negocio.dossieproduto.TipoRelacionamentoDTO";
    public static final String API_MODEL_V1_VERIFICACAO = "v1.negocio.dossieproduto.VerificacaoDTO";
    public static final String API_MODEL_V1_VINCULO_PESSOA = "v1.negocio.dossieproduto.VinculoPessoaDTO";

    //************* XML Root *************
    public static final String XML_ROOT_ELEMENT_CANAL = "canal";
    public static final String XML_ROOT_ELEMENT_CHECKLIST = "checklist";
    public static final String XML_ROOT_ELEMENT_CONTEUDO = "conteudo";
    public static final String XML_ROOT_ELEMENT_DOCUMENTO = "documento";
    public static final String XML_ROOT_ELEMENT_DOSSIE_CLIENTE = "dossie_cliente";
    public static final String XML_ROOT_ELEMENT_DOSSIE_CLIENTE_PF = "dossie_cliente_pf";
    public static final String XML_ROOT_ELEMENT_DOSSIE_CLIENTE_PJ = "dossie_cliente_pj";
    public static final String XML_ROOT_ELEMENT_DOSSIE_PRODUTO = "dossie_produto";
    public static final String XML_ROOT_ELEMENT_GARANTIA_INFORMADA = "garantia_informada";
    public static final String XML_ROOT_ELEMENT_INSTANCIA_DOCUMENTO = "instancia_documento";
    public static final String XML_ROOT_ELEMENT_OPCAO_CAMPO = "opcao_campo";
    public static final String XML_ROOT_ELEMENT_PARECER = "parecer";
    public static final String XML_ROOT_ELEMENT_PROCESSO = "processo";
    public static final String XML_ROOT_ELEMENT_PRODUTO_CONTRATADO = "produto_contratado";
    public static final String XML_ROOT_ELEMENT_RESPOSTA_FORMULARIO = "resposta_formulario";
    public static final String XML_ROOT_ELEMENT_SITUACAO_DOSSIE = "situacao_dossie";
    public static final String XML_ROOT_ELEMENT_SITUACAO_INSTANCIA = "situacao_instancia";
    public static final String XML_ROOT_ELEMENT_TIPO_DOCUMENTO = "tipo_documento";
    public static final String XML_ROOT_ELEMENT_TIPO_RELACIONAMENTO = "tipo_relacionamento";
    public static final String XML_ROOT_ELEMENT_VERIFICACAO = "verificacao";
    public static final String XML_ROOT_ELEMENT_VINCULO_PESSOA = "vinculo_pessoa";

    //************* NOMES DE ATRIBUTOS *************
    //Comuns
    public static final String DATA_HORA_INCLUSAO = "data_hora_inclusao"; //Situacao_Dossie, Situacao_Instancia
    public static final String DOCUMENTO = "documento";//Instancia_Documento
    public static final String DOSSIE_CLIENTE = "dossie_cliente";//Garantia_Informada, Vinculo_Pessoa
    public static final String HISTORICO_SITUACOES = "historico_situacoes";//Dossiê_Produto, Instancia_Documento
    public static final String SITUACAO_ATUAL = "situacao_atual";//Dossie Produto, , Instancia Documento
    public static final String ID = "id"; //Dossie_Cliente, Dossie_Produto, Garantia, Instancia_Documento, Processo, Produto_Contratado, Tipo_Documento, Vinculo_Pessoa
    public static final String INSTANCIA_DOCUMENTO = "instancia_documento";//Processo, Produto_Contratado, Verificacao, Vinculo_Pessoa
    public static final String NOME = "nome";//Checklist, Dossie_Cliente, Processo, Produto_Contratado, Situacao_Documento, Situacao_Instancia, Tipo_Documento
    public static final String PROCESSO_FASE = "processo_fase";//Dossie_Produto, Verificacao
    public static final String RESPOSTAS_FORMULARIO = "respostas_formulario";//Dossie_Produto, Processo_Fase
    public static final String RESPOSTA_FORMULARIO = "resposta_formulario";//Dossie_Produto, Processo_Fase
    public static final String UNIDADE = "unidade";//SituacaoDossie, Verificacao

    //Canal
    public static final String CANAL_CAIXA = "canal_caixa";//Canal, Dossie Produto
    public static final String DESCRICAO_CANAL = "descricao_canal";
    public static final String SIGLA_CANAL = "sigla_canal";

    //Documento e Tipologia
    public static final String ATRIBUTOS_DOCUMENTO = "atributos_documento";
    public static final String ATRIBUTO_DOCUMENTO = "atributo_documento";
    public static final String AVATAR = "avatar";
    public static final String BINARIO = "binario";
    public static final String CANAL_CAPTURA = "canal_captura";
    public static final String CODIGO_GED = "codigo_ged";
    public static final String CONTEUDO = "conteudo";
    public static final String CONTEUDOS = "conteudos";
    public static final String COR_FUNDO = "cor_fundo";
    public static final String DATA_HORA_CAPTURA = "data_hora_captura";
    public static final String DATA_HORA_SITUACAO_ATUAL = "data_hora_situacao_atual";
    public static final String DATA_HORA_VALIDADE = "data_hora_validade";
    public static final String DATA_HORA_VINCULACAO = "data_hora_vinculacao";
    public static final String DOSSIE_DIGITAL = "dossie_digital";
    public static final String ID_DOSSIE_CLIENTE_PRODUTO = "id_dossie_cliente_produto";
    public static final String ID_ELEMENTO_CONTEUDO = "id_elemento_conteudo";
    public static final String ID_GARANTIA_INFORMADA = "id_garantia_informada";
    public static final String MATRICULA_CAPTURA = "matricula_captura";
    public static final String MOTIVO = "motivo";
    public static final String ORIGEM_DOCUMENTO = "origem_documento";
    public static final String SEQUENCIA_APRESENTACAO = "sequencia_apresentacao";
    public static final String SITUACAO_INSTANCIA = "situacao_instancia";
    public static final String TIPO_DOCUMENTO = "tipo_documento";
    
    //Dossie Cliente
    public static final String CPF = "cpf";
    public static final String CNPJ = "cnpj";
    public static final String DATA_FUNDACAO = "data_fundacao";
    public static final String DATA_NASCIMENTO = "data_nascimento";
    public static final String EMAIL = "email";
    public static final String NOME_MAE = "nome_mae";
    public static final String PORTE = "porte";
    public static final String RAZAO_SOCIAL = "razao_social";
    public static final String TIPO_PESSOA = "tipo_pessoa";
    public static final String CONGLOMERADO = "conglomerado";
    
    
    //Dossie Produto e Situacao Dossie
    public static final String DATA_HORA_SAIDA = "data_hora_saida";
    public static final String OBSERVACAO = "observacao";
    public static final String ALTERACAO = "alteracao";
    public static final String UNIDADE_CRIACAO = "unidade_criacao";
    public static final String UNIDADE_PRIORIZADO = "unidade_priorizado";
    public static final String MATRICULA_PRIORIZADO = "matricula_priorizado";
    public static final String PESO_PRIORIDADE = "peso_prioridade";
    public static final String DATA_HORA_FINALIZACAO = "data_hora_finalizacao";
    public static final String UNIDADES_TRATAMENTO = "unidades_tratamento";
    public static final String DATA_HORA_SITUACAO = "data_hora_situacao";
    public static final String SITUACAO_DOSSIE = "situacao_dossie";
    public static final String PROCESSO_DOSSIE = "processo_dossie";
    public static final String PRODUTOS_CONTRATADOS = "produtos_contratados";
    public static final String PRODUTO_CONTRATADO = "produto_contratado";
    public static final String VINCULOS_PESSOAS = "vinculos_pessoas";
    public static final String VINCULO_PESSOA = "vinculo_pessoa";
    public static final String GARANTIAS_INFORMADAS = "garantias_informadas";
    public static final String GARANTIA_INFORMADA = "garantia_informada";
    public static final String VERIFICACOES = "verificacoes";
    public static final String VERIFICACAO = "verificacao";
    public static final String QUANTIDADE_CONTEUDOS = "quantidade_conteudos";
    public static final String SUCESSO_BPM = "sucesso_bpm";
    
    //Checklist e Verificação
    public static final String APROVADA = "aprovada";
    public static final String CHECKLIST = "checklist";
    public static final String DATA_HORA_VERIFICACAO = "data_hora_verificacao";
    public static final String IDENTIFICADOR_CHECKLIST = "identificador_checklist";
    public static final String IDENTIFICADOR_VERIFICACAO = "identificador_verificacao";
    public static final String PARECER_APONTAMENTO = "parecer_apontamento";
    public static final String PARECER_APONTAMENTOS = "parecer_apontamentos";
    public static final String REALIZADA = "realizada";

    //Formulario
    public static final String APROVADO = "aprovado";
    public static final String COMENTARIO_ANALISTA = "comentario_tratamento";
    public static final String DESCRICAO_OPCAO = "descricao_opcao";
    public static final String ID_CAMPO_FORMULARIO = "id_campo_formulario";
    public static final String ID_PROCESSO_FASE = "id_processo_fase";
    public static final String ID_PRODUTO_CONTRATADO = "id_produto_contratado";
    public static final String ID_VINCULO_PESSOA = "id_vinculo_pessoa";
    public static final String IDENTIFICADOR_APONTAMENTO = "identificador_apontamento";
    public static final String IDENTIFICADOR_PARECER = "identificador_parecer";
    public static final String LABEL_CAMPO = "label_campo";
    public static final String NOME_CAMPO = "nome_campo";
    public static final String OPCAO_SELECIONADA = "opcao_selecionada";
    public static final String OPCOES_SELECIONADAS = "opcoes_selecionadas";
    public static final String ORIENTACAO = "orientacao";
    public static final String RESPOSTA_ABERTA = "resposta_aberta";
    public static final String TIPO_CAMPO = "tipo_campo";
    public static final String TITULO = "titulo";
    public static final String VALOR_OPCAO = "valor_opcao";

    //Vinculo Pessoa e Tipo de Relacionamento
    public static final String TIPO_RELACIONAMENTO = "tipo_relacionamento";
    public static final String DOSSIE_CLIENTE_RELACIONADO = "dossie_cliente_relacionado";
    public static final String INDICA_RELACIONADO = "indica_relacionado";
    public static final String INDICA_SEQUENCIA = "indica_sequencia";
    public static final String PRINCIPAL = "principal";
    public static final String SEQUENCIA_TITULARIDADE = "sequencia_titularidade";
    
    
    //Garantia Informada
    public static final String CODIGO_BACEN = "codigo_bacen";
    public static final String DESCRICAO = "descricao";
    public static final String DOSSIES_CLIENTE = "dossies_cliente";
    public static final String FORMA_GARANTIA = "forma_garantia";
    public static final String GARANTIA = "garantia";
    public static final String NOME_GARANTIA = "nome_garantia";
    public static final String PERCENTUAL_GARANTIA = "percentual_garantia";
    public static final String PRODUTO = "produto";
    public static final String PRODUTO_MODALIDADE = "produto_modalidade";
    public static final String PRODUTO_NOME = "produto_nome";
    public static final String PRODUTO_OPERACAO = "produto_operacao";
    public static final String VALOR = "valor";
    
    //Produto Contratado
    public static final String CODIGO_OPERACAO = "codigo_operacao";
    public static final String CODIGO_MODALIDADE = "codigo_modalidade";
    
    //Processo
    public static final String INSTANCIAS_DOCUMENTO = "instancias_documento";
    
}
