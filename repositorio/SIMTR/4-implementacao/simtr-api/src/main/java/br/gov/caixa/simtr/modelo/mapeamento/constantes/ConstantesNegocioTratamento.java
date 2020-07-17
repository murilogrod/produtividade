package br.gov.caixa.simtr.modelo.mapeamento.constantes;

/**
 *
 * @author c090347
 */
public final class ConstantesNegocioTratamento {

    private ConstantesNegocioTratamento() {
    }

    //************* API MODEL APURACAO *************
    public static final String API_MODEL_V1_APURACAO__APONTAMENTO = "v1.negocio.tratamento.apuracao.ApontamentoDTO";
    public static final String API_MODEL_V1_APURACAO__CHECKLIST_APONTAMENTO_PENDENTE = "v1.negocio.tratamento.apuracao.ChecklistApontamentoPendenteDTO";
    public static final String API_MODEL_V1_APURACAO__CHECKLIST_FASE_REPLICADO = "v1.negocio.tratamento.apuracao.ChecklistFaseReplicadoDTO";
    public static final String API_MODEL_V1_APURACAO__CHECKLIST_INESPERADO = "v1.negocio.tratamento.apuracao.ChecklistInesperadoDTO";
    public static final String API_MODEL_V1_APURACAO__CHECKLIST_PENDENTE = "v1.negocio.tratamento.apuracao.ChecklistPendenteDTO";
    public static final String API_MODEL_V1_APURACAO__INSTANCIA_DOCUMENTO_PENDENTE = "v1.negocio.tratamento.apuracao.InstanciaDocumetoPendenteDTO";
    public static final String API_MODEL_V1_APURACAO__INSTANCIA_DOCUMENTO_REPLICADA = "v1.negocio.tratamento.apuracao.InstanciaDocumetoReplicadaDTO";
    public static final String API_MODEL_V1_APURACAO__VERIFICACAO_DESCONSIDERADA = "v1.negocio.tratamento.apuracao.VerificacaoDesconsideradaDTO";
    public static final String API_MODEL_V1_APURACAO__VERIFICACAO_INVALIDA = "v1.negocio.tratamento.apuracao.VerificacaoInvalidaDTO";

    //************* API MODEL TRATAMENTO *************
    public static final String API_MODEL_V1_PARECER_APONTAMENTO = "v1.negocio.tratamento.ParecerApontamentoDTO";
    public static final String API_MODEL_V1_PROCESSO_DOSSIE = "v1.negocio.tratamento.ProcessoDossieDTO";
    public static final String API_MODEL_V1_PROCESSO_FASE = "v1.negocio.tratamento.ProcessoFaseDTO";
    public static final String API_MODEL_V1_PROCESSO_PATRIARCA = "v1.negocio.tratamento.ProcessoPatriarcaDTO";
    public static final String API_MODEL_V1_VERIFICACAO = "v1.negocio.tratamento.VerificacaoDTO";

    //************* XML Root *************
    public static final String XML_ROOT_ELEMENT_APONTAMENTO = "apontamento";
    public static final String XML_ROOT_ELEMENT_CHECKLIST_APONTAMENTO_PENDENTE = "checklist_apontamento_pendente";
    public static final String XML_ROOT_ELEMENT_CHECKLIST_FASE_REPLICADO = "checklist_fase_replicado";
    public static final String XML_ROOT_ELEMENT_CHECKLIST_INESPERADO = "checklist_inesperado";
    public static final String XML_ROOT_ELEMENT_CHECKLIST_PENDENTE = "checklist_pendente";
    public static final String XML_ROOT_ELEMENT_INSTANCIA_DOCUMENTO_PENDENTE = "instancia_documento_pendente";
    public static final String XML_ROOT_ELEMENT_INSTANCIA_DOCUMENTO_REPLICADA = "instancia_documento_replicada";
    public static final String XML_ROOT_ELEMENT_PARECER_APONTAMENTO = "parecer_apontamento";
    public static final String XML_ROOT_ELEMENT_PROCESSO_DOSSIE = "processo_dossie";
    public static final String XML_ROOT_ELEMENT_PROCESSO_FASE = "processo_fases";
    public static final String XML_ROOT_ELEMENT_PROCESSO_PATRIARCA = "processo_patriarca";
    public static final String XML_ROOT_ELEMENT_VERIFICACAO = "verificacao";
    public static final String XML_ROOT_ELEMENT_VERIFICACAO_DESCONSIDERADA = "verificacao_desconsiderada";
    public static final String XML_ROOT_ELEMENT_VERIFICACAO_INVALIDA = "verificacao_invalida";

    //************* NOMES DE ATRIBUTOS *************  
    //Atributos Comuns
    public static final String NOME = "nome";

    //Atributos do Processo
    public static final String AVATAR = "avatar";
    public static final String ID = "id";
    public static final String PRIORIZADO = "priorizado";
    public static final String PROCESSO_FILHO = "processo_filho";
    public static final String PROCESSOS_FILHO = "processos_filho";
    public static final String QUANTIDADE_DOSSIES = "quantidade_dossies";

    //Atributos da verificacao
    public static final String ANALISE_REALIZADA = "analise_realizada";
    public static final String APONTAMENTOS_PENDENTES = "apontamentos_pendentes";
    public static final String APONTAMENTO_PENDENTE = "apontamento_pendente";
    public static final String APROVADO = "aprovado";
    public static final String CHECKLIST_ESPERADO = "checklist_esperado";
    public static final String CHECKLIST_INCOMPLETO = "checklist_incompleto";
    public static final String CHECKLIST_INESPERADO = "checklist_inesperado";
    public static final String CHECKLIST_NAO_DOCUMENTAL_AUSENTE = "checklist_nao_documental_ausente";
    public static final String CHECKLIST_NAO_DOCUMENTAL_REPLICADO = "checklist_nao_documental_replicado";
    public static final String CHECKLISTS_INCOMPLETOS = "checklists_incompletos";
    public static final String CHECKLISTS_INESPERADOS = "checklists_inesperados";
    public static final String CHECKLISTS_NAO_DOCUMENTAIS_AUSENTES = "checklists_nao_documentais_ausentes";
    public static final String CHECKLISTS_NAO_DOCUMENTAIS_REPLICADOS = "checklists_nao_documentais_replicados";
    public static final String COMENTARIO = "comentario";
    public static final String IDENTIFICADOR_APONTAMENTO = "identificador_apontamento";
    public static final String IDENTIFICADOR_CHECKLIST = "identificador_checklist";
    public static final String IDENTIFICADOR_INSTANCIA = "identificador_instancia";
    public static final String IDENTIFICADOR_INSTANCIA_DOCUMENTO = "identificador_instancia_documento";
    public static final String IDENTIFICADOR_INSTANCIA_INVALIDA = "identificador_instancia_invalida";
    public static final String IDENTIFICADORES_INSTANCIA_INVALIDA = "identificadores_instancia_invalida";
    public static final String INSTANCIA_PENDENTE = "instancia_pendente";
    public static final String INSTANCIA_VERIFICACAO_REPLICADA = "instancia_verificacao_replicada";
    public static final String INSTANCIAS_PENDENTES = "instancias_pendentes";
    public static final String INSTANCIAS_VERIFICACAO_REPLICADA = "instancias_verificacao_replicada";
    public static final String NOME_CHECKLIST = "nome_checklist";
    public static final String PARECER_APONTAMENTO = "parecer_apontamento";
    public static final String PARECER_APONTAMENTOS = "parecer_apontamentos";
    public static final String QUANTIDADE = "quantidade";
    public static final String TIPO_DOCUMENTO = "tipo_documento";
}
