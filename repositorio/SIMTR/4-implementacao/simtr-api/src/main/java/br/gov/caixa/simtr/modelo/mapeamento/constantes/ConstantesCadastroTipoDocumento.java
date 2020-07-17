/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.caixa.simtr.modelo.mapeamento.constantes;

/**
 *
 * @author c090347
 */
public final class ConstantesCadastroTipoDocumento {

    private ConstantesCadastroTipoDocumento() {
    }

    // ************* API MODEL *************
    // Identificadores de objetos na visão das operações de cadastro interno a plataforma
    public static final String API_MODEL_ATRIBUTO_EXTRACAO = "cadastro.tipodocumento.AtributoExtracaoDTO";
    public static final String API_MODEL_ATRIBUTO_EXTRACAO_MANUTENCAO = "cadastro.tipodocumento.AtributoExtracaoManutencaoDTO";
    public static final String API_MODEL_ATRIBUTO_EXTRACAO_NOVO = "cadastro.tipodocumento.AtributoExtracaoNovoDTO";
    public static final String API_MODEL_DADOS_DECLARADOS = "cadastro.tipodocumento.DadosDeclaradosDTO";
    public static final String API_MODEL_FUNCAO_DOCUMENTAL = "cadastro.tipodocumento.FuncaoDocumentalDTO";

    public static final String API_MODEL_TIPO_DOCUMENTO = "cadastro.tipodocumento.TipoDocumentoDTO";
    public static final String API_MODEL_TIPO_DOCUMENTO_MANUTENCAO = "cadastro.tipodocumento.TipoDocumentoManutencaoDTO";
    public static final String API_MODEL_TIPO_DOCUMENTO_NOVO = "cadastro.tipodocumento.TipoDocumentoNovoDTO";

    // Identificadores de objetos na visão das operações de consulta de dados externo a plataforma
    public static final String API_MODEL_TIPO_DOCUMENTO__DOSSIEDIGITAL = "cadastro.tipodocumento.dossiedigital.TipoDocumentoDTO";
    public static final String API_MODEL_ATRIBUTO_EXTRACAO__DOSSIEDIGITAL = "cadastro.tipodocumento.dossiedigital.AtributoExtracaoDTO";

    // Identificadores de objetos na visão das operações de consulta de dados externo a plataforma
    public static final String API_MODEL_TIPO_DOCUMENTO__PROCESSOADMINISTRATIVO = "cadastro.tipodocumento.processoadministrativo.TipoDocumentoDTO";
    public static final String API_MODEL_ATRIBUTO_EXTRACAO__PROCESSOADMINISTRATIVO = "cadastro.tipodocumento.processoadministrativo.AtributoExtracaoDTO";

    // ************* XML Root *************
    public static final String XML_ROOT_ELEMENT_TIPO_DOCUMENTO = "tipo_documento";
    public static final String XML_ROOT_ELEMENT_ATRIBUTO_DADOS_DECLARADOS = "atributo_dados_declarados";
    public static final String XML_ROOT_ELEMENT_ATRIBUTO_EXTRACAO = "atributo_extracao";

    // ************* NOMES DE ATRIBUTOS *************
    public static final String ARQUIVO_MINUTA = "arquivo_minuta";
    public static final String ATIVO = "ativo";
    public static final String ATRIBUTO_EXTRACAO = "atributo_extracao";
    public static final String ATRIBUTOS_EXTRACAO = "atributos_extracao";
    public static final String AVATAR = "avatar";
    public static final String CHAVE = "chave";
    public static final String CLASSE_SIECM = "classe_siecm";
    public static final String CODIGO_TIPOLOGIA = "codigo_tipologia";
    public static final String COR_BOX = "cor_box";
    public static final String DATA_HORA_ULTIMA_ALTERACAO = "data_hora_ultima_alteracao";
    public static final String DESCRICAO = "descricao";
    public static final String EXPRESSAO_INTERFACE = "expressao_interface";
    public static final String FUNCOES_DOCUMENTAIS = "funcoes_documentais";
    public static final String FUNCOES_DOCUMENTO_INCLUSAO_VINCULO = "funcoes_documento_inclusao_vinculo";
    public static final String FUNCOES_DOCUMENTO_EXCLUSAO_VINCULO = "funcoes_documento_exclusao_vinculo";
    public static final String GRUPO_ATRIBUTO = "grupo_atributo";
    public static final String ID = "id";
    public static final String IDENTIFICADOR_ATRIBUTO_EXTRACAO = "identificador_atributo_extracao";
    public static final String IDENTIFICADOR_ATRIBUTO_PARTILHA = "identificador_atributo_partilha";
    public static final String IDENTIFICADOR_ESTRATEGIA_PARTILHA = "indicador_estrategia_partilha";
    public static final String IDENTIFICADOR_FUNCAO_DOCUMENTAL = "identificador_funcao_documental";
    public static final String IDENTIFICADOR_TIPO_DOCUMENTO = "identificador_tipo_documento";
    public static final String INDICADOR_APOIO_NEGOCIO = "indicador_uso_apoio_negocio";
    public static final String INDICADOR_CALCULO_VALIDADE = "indicador_calculo_validade";
    public static final String INDICADOR_CALCULO_DATA_VALIDADE = "indicador_calculo_data_validade";
    public static final String INDICADOR_CAMPO_COMPARACAO_RECEITA = "indicador_campo_comparacao_receita";
    public static final String INDICADOR_DOSSIE_DIGITAL = "indicador_uso_dossie_digital";
    public static final String INDICADOR_EMISSAO_MINUTA = "indicador_emissao_minuta";
    public static final String INDICADOR_EXTRACAO_EXTERNA = "indicador_extracao_externa";
    public static final String INDICADOR_EXTRACAO_M0 = "indicador_extracao_m0";
    public static final String INDICADOR_IDENTIFICADOR_PESSOA = "indicador_identificador_pessoa";
    public static final String INDICADOR_MULTIPLOS = "indicador_multiplos";
    public static final String INDICADOR_MODO_COMPARACAO_RECEITA = "indicador_modo_comparacao_receita";
    public static final String INDICADOR_OBRIGATORIO = "indicador_obrigatorio";
    public static final String INDICADOR_OBRIGATORIO_SIECM = "indicador_obrigatorio_siecm";
    public static final String INDICADOR_PRESENTE_DOCUMENTO = "indicador_presente_documento";
    public static final String INDICADOR_REUSO = "indicador_reuso";
    public static final String INDICADOR_TIPO_PESSOA = "indicador_tipo_pessoa";
    public static final String INDICADOR_GUARDA_BINARIO_OUTSOURCING = "indicador_guarda_binario_outsourcing";
    public static final String INDICADOR_MODO_PARTILHA = "indicador_modo_partilha";
    public static final String INDICADOR_PROCESSO_ADMINISTRATIVO = "indicador_uso_processo_administrativo";
    public static final String INDICADOR_VALIDACAO_CADASTRAL = "indicador_validacao_cadastral";
    public static final String INDICADOR_VALIDACAO_DOCUMENTAL = "indicador_validacao_documental";
    public static final String INDICADOR_VALIDACAO_SICOD = "indicador_validacao_sicod";
    public static final String INDICADOR_VALIDADE_AUTOCONTIDA = "indicador_validade_autocontida";
    public static final String IDENTIFICADOR_MODO_PARTILHA = "identificador_modo_partilha";
    public static final String NOME = "nome";
    public static final String NOME_ATRIBUTO_SICLI = "nome_atributo_sicli";
    public static final String NOME_ATRIBUTO_DOCUMENTO = "nome_atributo_documento";
    public static final String NOME_ATRIBUTO_NEGOCIAL = "nome_atributo_negocial";
    public static final String NOME_ATRIBUTO_RETORNO = "nome_atributo_retorno";
    public static final String NOME_ATRIBUTO_SICOD = "nome_sicod";
    public static final String NOME_ATRIBUTO_SIECM = "nome_atributo_siecm";
    public static final String NOME_DOCUMENTO = "nome_documento";
    public static final String NOME_FUNCAO_DOCUMENTAL = "nome_funcao_documental";
    public static final String NOME_INTEGRACAO = "nome_integracao";
    public static final String NOME_NEGOCIAL = "nome_negocial";
    public static final String NOME_OBJETO_SICLI = "nome_objeto_sicli";
    public static final String NOME_RETORNO = "nome_retorno";
    public static final String NOME_TIPO_DOCUMENTO = "nome_tipo_documento";
    public static final String OPCAO = "opcao";
    public static final String OPCOES = "opcoes";
    public static final String OPCOES_ATRIBUTO = "opcoes_atributo";
    public static final String OPCOES_ATRIBUTOS = "opcoes_atributos";
    public static final String ORDEM_APRESENTACAO = "ordem_apresentacao";
    public static final String ORIENTACAO = "orientacao";
    public static final String ORIENTACAO_PREENCHIMENTO = "orientacao_preenchimento";
    public static final String PERCENTUAL_ALTERACAO_PERMITIDO = "percentual_alteracao_permitido";
    public static final String PRAZO_VALIDADE_DIAS = "prazo_validade_dias";
    public static final String TAGS = "tags";
    public static final String TIPO_ATRIBUTO = "tipo_atributo";
    public static final String TIPO_PESSOA = "tipo_pessoa";
    public static final String TIPO_ATRIBUTO_GERAL = "tipo_atributo_geral";
    public static final String TIPO_ATRIBUTO_SICLI = "tipo_atributo_sicli";
    public static final String TIPO_ATRIBUTO_SICOD = "tipo_atributo_sicod";
    public static final String TIPO_ATRIBUTO_SIECM = "tipo_atributo_siecm";
    public static final String TIPO_CAMPO = "tipo_campo";
    public static final String VALOR = "valor";
    public static final String VALOR_PADRAO = "valor_padrao";

}
