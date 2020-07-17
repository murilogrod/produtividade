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
public final class ConstantesPAE {

    private ConstantesPAE() {
    }

    //************* API MODEL *************
    public static final String API_MODEL_UNIDADE_AUTORIZADA = "v1.pae.UnidadeAutorizadaDTO";

    //Identificadores de objetos na visão do processo administrativo
    public static final String VISAO_APENSO_API_MODEL_PROCESSO_ADMINISTRATIVO = "v1.pae.apenso.ProcessoAdministrativoDTO";
    public static final String VISAO_CONTRATO_API_MODEL_PROCESSO_ADMINISTRATIVO = "v1.pae.contrato.ProcessoAdministrativoDTO";
    public static final String VISAO_PROCESSO_API_MODEL_PROCESSO_ADMINISTRATIVO = "v1.pae.processo.ProcessoAdministrativoDTO";
    public static final String VISAO_PROCESSO_API_MODEL_PROCESSO_ADMINISTRATIVO_MANUTENCAO = "v1.pae.processo.ProcessoAdministrativoManutencaoDTO";
    public static final String VISAO_PROCESSO_API_MODEL_PROCESSO_ADMINISTRATIVO_NOVO = "v1.pae.processo.ProcessoAdministrativoNovoDTO";

    //Identificadores de objetos na visão do contrato administrativo
    public static final String VISAO_APENSO_API_MODEL_CONTRATO_ADMINISTRATIVO = "v1.pae.apenso.ContratoAdministrativoDTO";
    public static final String VISAO_CONTRATO_API_MODEL_CONTRATO_ADMINISTRATIVO = "v1.pae.contrato.ContratoAdministrativoDTO";
    public static final String VISAO_CONTRATO_API_MODEL_CONTRATO_ADMINISTRATIVO_MANUTENCAO = "v1.pae.contrato.ContratoAdministrativoManutencaoDTO";
    public static final String VISAO_CONTRATO_API_MODEL_CONTRATO_ADMINISTRATIVO_NOVO = "v1.pae.contrato.ContratoAdministrativoNovoDTO";
    public static final String VISAO_PROCESSO_API_MODEL_CONTRATO_ADMINISTRATIVO = "v1.pae.processo.ContratoAdministrativoDTO";

    //Identificadores de objetos na visão do apenso administrativo
    public static final String VISAO_APENSO_API_MODEL_APENSO_ADMINISTRATIVO_MANUTENCAO = "v1.pae.apenso.ApensoAdministrativoManutencaoDTO";
    public static final String VISAO_APENSO_API_MODEL_APENSO_ADMINISTRATIVO_NOVO = "v1.pae.apenso.ApensoAdministrativoNovoDTO";
    public static final String VISAO_APENSO_API_MODEL_APENSO_ADMINISTRATIVO = "v1.pae.apenso.ApensoAdministrativoDTO";
    public static final String VISAO_CONTRATO_API_MODEL_APENSO_ADMINISTRATIVO = "v1.pae.contrato.ApensoAdministrativoDTO";
    public static final String VISAO_PROCESSO_API_MODEL_APENSO_ADMINISTRATIVO = "v1.pae.processo.ApensoAdministrativoDTO";

    //Identificadores de objetos na visão do documento administrativo
    public static final String API_MODEL_ATRIBUTO_DOCUMENTO = "v1.pae.documento.AtributoDocumentoDTO";
    public static final String API_MODEL_DOCUMENTO_ADMINISTRATIVO = "v1.pae.documento.DocumentoAdministrativoDTO";
    public static final String API_MODEL_DOCUMENTO_ADMINISTRATIVO_CADASTRO = "v1.pae.documento.DocumentoAdministrativoCadastroDTO";
    public static final String API_MODEL_DOCUMENTO_ADMINISTRATIVO_MANUTENCAO = "v1.pae.documento.DocumentoAdministrativoManutencaoDTO";

    //************* XML Root *************
    public static final String XML_ROOT_ELEMENT_APENSO = "apenso";
    public static final String XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO = "atributo_documento";
    public static final String XML_ROOT_ELEMENT_CONTRATO = "contrato";
    public static final String XML_ROOT_ELEMENT_DOCUMENTO_ADMINISTRATIVO = "documento_administrativo";
    public static final String XML_ROOT_ELEMENT_PROCESSO = "processo";
    public static final String XML_ROOT_ELEMENT_UNIDADE_AUTORIZADA = "unidade_autorizada";

    //************* NOMES DE ATRIBUTOS *************
    public static final String CODIGO_INTEGRACAO = "codigo_integracao";

    //Atributos do Processo Adminstrativo
    public static final String ANO_CONTRATACAO = "ano_contratacao";
    public static final String ANO_PREGAO = "ano_pregao";
    public static final String ANO_PROCESSO = "ano_processo";
    public static final String APENSO = "apenso";
    public static final String APENSOS = "apensos";
    public static final String ATRIBUTO_EXTRACAO = "atributo_extracao";
    public static final String ATRIBUTOS_EXTRACAO = "atributos_extracao";
    public static final String AUTORIZADA = "autorizada";
    public static final String AUTORIZADAS = "autorizadas";
    public static final String CONTRATO = "contrato";
    public static final String CONTRATOS = "contratos";
    public static final String DATA_HORA_INCLUSAO = "data_hora_inclusao";
    public static final String DATA_HORA_FINALIZACAO = "data_hora_finalizacao";
    public static final String DOCUMENTO = "documento";
    public static final String DOCUMENTOS = "documentos";
    public static final String ID = "id";
    public static final String MATRICULA_INCLUSAO = "matricula_inclusao";
    public static final String MATRICULA_FINALIZACAO = "matricula_finalizacao";
    public static final String NUMERO_CONTRATACAO = "numero_contratacao";
    public static final String NUMERO_PREGAO = "numero_pregao";
    public static final String NUMERO_PROCESSO = "numero_processo";
    public static final String OBJETO_CONTRATACAO = "objeto_contratacao";
    public static final String PROTOCOLO_SICLG = "protocolo_siclg";
    public static final String UNIDADE_CONTRATACAO = "unidade_contratacao";
    public static final String UNIDADE_DEMANDANTE = "unidade_demandante";

    //Atributos do Contrato Adminstrativo
    public static final String ANO_CONTRATO = "ano_contrato";
    public static final String CPF_FORNECEDOR = "cpf_fornecedor";
    public static final String CNPJ_FORNECEDOR = "cnpj_fornecedor";
    public static final String DESCRICAO_CONTRATO = "descricao_contrato";
    public static final String ID_PROCESSO_VINCULADO = "id_processo_vinculado";
    public static final String NUMERO_CONTRATO = "numero_contrato";
    public static final String PROCESSO_ADMINISTRATIVO = "processo_administrativo";
    public static final String UNIDADE_OPERACIONAL = "unidade_operacional";

    //Atributos do Apenso Adminstrativo
    public static final String CONTRATO_ADMINISTRATIVO = "contrato_administrativo";
    public static final String DESCRICAO_APENSO = "descricao_apenso";
    public static final String ID_CONTRATO_VINCULADO = "id_contrato_vinculado";
    public static final String TIPO_APENSO = "tipo_apenso";
    public static final String TITULO_APENSO = "titulo_apenso";

    //Atributos do Atributo Documento
    public static final String CHAVE = "chave";
    public static final String VALOR = "valor";

    //Atributos do Documento Adminstrativo
    public static final String ATRIBUTO_DOCUMENTO = "atributo_documento";
    public static final String ATRIBUTOS_DOCUMENTO = "atributos_documento";
    public static final String CONFIDENCIAL = "confidencial";
    public static final String CONTEUDO = "conteudo";
    public static final String CONTEUDOS = "conteudos";
    public static final String DATA_HORA_CAPTURA = "data_hora_captura";
    public static final String DESCRICAO_DOCUMENTO = "descricao_documento";
    public static final String DOCUMENTO_SUBSTITUTO = "documento_substituto";
    public static final String DOCUMENTO_SUBSTITUICAO = "documento_substituicao";
    public static final String JUSTIFICATIVA_SUBSTITUICAO = "justificativa_substituicao";
    public static final String MATRICULA_CAPTURA = "matricula_captura";
    public static final String MIME_TYPE = "mime_type";
    public static final String ORIGEM_DOCUMENTO = "origem_documento";
    public static final String TIPO_DOCUMENTO = "tipo_documento";
    public static final String VALIDO = "valido";

    //Atributos da Unidade Autorizada
    public static final String CGC_UNIDADE = "cgc_unidade";
    public static final String TRATAMENTO_AUTORIZADO = "tratamento_autorizado";
    public static final String TRATAMENTOS_AUTORIZADOS = "tratamentos_autorizados";

}
