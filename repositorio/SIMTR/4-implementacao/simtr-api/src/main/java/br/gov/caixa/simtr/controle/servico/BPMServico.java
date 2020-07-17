/*
 * Copyright (c) 2018 Caixa Econômica Federal. Todos os direitos
 * reservados.
 *
 * Caixa Econômica Federal - simtr-api
 *
 * Este software foi desenvolvido sob demanda da CAIXA e está
 * protegido por leis de direitos autorais e tratados internacionais. As
 * condições de cópia e utilização da totalidade ou partes dependem de autorização da
 * empresa. Cópias não são permitidas sem expressa autorização. Não pode ser
 * comercializado ou utilizado para propósitos particulares.
 *
 * Uso exclusivo da Caixa Econômica Federal. A reprodução ou distribuição não
 * autorizada deste programa ou de parte dele, resultará em punições civis e
 * criminais e os infratores incorrem em sanções previstas na legislação em
 * vigor.
 *
 * Histórico do TFS:
 *
 * LastChangedRevision: $Revision$
 * LastChangedBy: $Author$
 * LastChangedDate: $Date$
 *
 * HeadURL: $HeadURL$
 *
 */
package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.pedesgo.arquitetura.util.UtilParametro;
import br.gov.caixa.pedesgo.arquitetura.util.UtilRest;
import br.gov.caixa.simtr.controle.excecao.BpmException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDossie;
import br.gov.caixa.simtr.modelo.entidade.TipoSituacaoDossie;
import br.gov.caixa.simtr.modelo.enumerator.BPMSinalEnum;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDossieEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.bpm.DossieProdutoDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.util.KeyStoreUtil;
import br.gov.caixa.simtr.util.KeycloakUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE,
    ConstantesUtil.PERFIL_MTRSDNTTO,
    ConstantesUtil.PERFIL_MTRSDNTTG
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class BPMServico {

    private static final String PROPRIEDADE_URL_SSO = "url_sso_intranet";
    private static final String PROPRIEDADE_URL_API = "url.simtr.api";
    private static final String PROPRIEDADE_URL_API_KEY = "simtr_apikey";
    private static final String PROPRIEDADE_URL_API_MANAGER = "url.servico.apimanager";
    private static final String PROPRIEDADE_URL_BPM = "url.servico.bpm";
    private static final String PROPRIEDADE_USER_BPM = "usuario.nome.servico.bpm";
    private static final String PROPRIEDADE_PASS_BPM = "usuario.senha.servico.bpm";
    private static final String PROPRIEDADE_KEYSTORE_URL = "simtr.keystore.url";
    private static final String PROPRIEDADE_KEYSTORE_SECRET = "simtr.keystore.secret";
    private static final String PROPRIEDADE_KEYSTORE_TYPE = "simtr.keystore.type";
    private static final String PROPRIEDADE_KEYSTORE_BPM_ALIAS = "simtr.keystore.bpm.alias";
    private static final String PROPRIEDADE_KEYSTORE_BPM_SECRET = "simtr.keystore.bpm.secret";
    // *******************************************
    private static final String ENDPOINT_ABORT_INSTANCE = "/server/containers/{cId}/processes/instances/{instanceId}";
    private static final String ENDPOINT_CHECK_SIGNAL = "/server/containers/{cId}/processes/instances/{instanceId}/signals";
    private static final String ENDPOINT_CONSULT_INSTANCE = "/server/queries/processes/instance/correlation/{correlationKey}";
    private static final String ENDPOINT_START_PROCESS = "/server/containers/{cId}/processes/{pId}/instances/correlation/{correlationKey}";
    private static final String ENDPOINT_SIGNAL_INSTANCE = "/server/containers/{cId}/processes/instances/{instanceId}/signal/{signalName}";

    private static final String PARAMETRO_CID = "{cId}";
    private static final String PARAMETRO_CORRELATIONKEY = "{correlationKey}";
    private static final String PARAMETRO_INSTANCEID = "{instanceId}";
    private static final String PARAMETRO_PID = "{pId}";
    private static final String PARAMETRO_SIGNALNAME = "{signalName}";

    private String apiKey;
    private String urlAPI;
    private String urlAPIManager;
    private String urlBPM;
    private String urlKeystore;
    private String urlSSO;
    private String basicAuth;

    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> params = new HashMap<>();

    private TipoSituacaoDossie tipoSituacaoDossieAlimentacaoFinalizada;
    private TipoSituacaoDossie tipoSituacaoDossieConforme;
    private TipoSituacaoDossie tipoSituacaoDossieCriado;
    private TipoSituacaoDossie tipoSituacaoDossiePendenteInformacao;
    private TipoSituacaoDossie tipoSituacaoDossiePendenteSeguranca;
    private TipoSituacaoDossie tipoSituacaoDossieSegurancaFinalizado;
    private TipoSituacaoDossie tipoSituacaoDossieCancelado;

    private static final Logger LOGGER = Logger.getLogger(BPMServico.class.getName());
   
    @EJB
    private KeycloakUtil keycloakUtil;

    @EJB
    private SituacaoDossieServico situacaoDossieServico;

    /**
     * Cria uma instancia de processo junto a solução de BPM e retorna o identificador gerado por este.
     *
     * @param dossieProduto - Objeto dossiê de produto utilizado para criar a instancia do processo junto ao BPM baseado nas configurações definidas junto ao
     *        processo gerador de dossiê parametrizados na base do SIMTR
     * @return Identificador do processo criado no BPM que será utilizado para encaminhar os sinais de continuidade de execução
     * @throws BpmException Lançada caso o identificador de processo indique um processo que não esteja corretamente configurado com as informações necessarias a
     *         criação da instancia de processo junto ao BPM ou ocorra alguma falha de comunicação com a solução
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long criaProcessoBPM(DossieProduto dossieProduto) {

        TipoSituacaoDossie situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream().max(Comparator.comparing(SituacaoDossie::getId)).get().getTipoSituacaoDossie();

        // Dossiê recem criado
        if (this.realizarComunicacaoBPM(dossieProduto) && tipoSituacaoDossieCriado.equals(situacaoAtualDossie)) {

            if (dossieProduto.getId() == null) {
                String mensagem = "BS.cPBPM.001 - O Dossiê de produto informado não possui definição de identificador para utilização em chave de correlacionamento junto ao BPM.";
                throw new SimtrRequisicaoException(mensagem);
            }

            String endpointConsultaProcesso = this.urlBPM.concat(ENDPOINT_CONSULT_INSTANCE.replace(PARAMETRO_CORRELATIONKEY, dossieProduto.getId().toString()));

            Response responseConsulta;
            String mensagemLogEnvio = MessageFormat.format("COMUNICACAO INICIADA COM O BPM: CONSULTANDO EXISTENCIA DE INSTANCIA DE PROCESSO PARA O DOSSIE = {0}", dossieProduto.getId());
            LOGGER.info(mensagemLogEnvio);
            responseConsulta = this.consumirServico(endpointConsultaProcesso, EnumMetodoHTTP.GET, null);
            String mensagemLogResposta = MessageFormat.format("COMUNICACAO FINALIZADA COM O BPM PARA CONSULTA DA INSTANCIA. DOSSIE = {0} | HTTP STATUS = {1} | RESPOSTA RECEBIDA: {2}", dossieProduto.getId(), responseConsulta.getStatus(), responseConsulta.getEntity());
            LOGGER.info(mensagemLogResposta);

            if (Status.OK.getStatusCode() == responseConsulta.getStatus()) {
                String json = responseConsulta.getEntity().toString();
                return Long.valueOf(UtilJson.consultarAtributo(json, "process-instance-id"));
            } else if (Status.NOT_FOUND.getStatusCode() == responseConsulta.getStatus()) {
                String endpointInclusaoProcesso = this.urlBPM.concat(ENDPOINT_START_PROCESS.replace(PARAMETRO_CID, dossieProduto.getNomeContainerBPM())
                                                                                           .replace(PARAMETRO_PID, dossieProduto.getNomeProcessoBPM())
                                                                                           .replace(PARAMETRO_CORRELATIONKEY, dossieProduto.getId().toString()));

                DossieProdutoDTO dossieProdutoDTO = new DossieProdutoDTO(dossieProduto, this.urlAPI, this.urlSSO, this.urlAPIManager, this.apiKey);
                Response responseInclusao = null;
                try {
                    String corpoMensagem = UtilJson.converterParaJson(dossieProdutoDTO);

                    mensagemLogEnvio = MessageFormat.format("COMUNICACAO INICIADA COM O BPM: CRIANDO INSTANCIA DE PROCESSO PARA O DOSSIE = {0} | BODY = {1}", dossieProduto.getId(), corpoMensagem);
                    LOGGER.info(mensagemLogEnvio);
                    responseInclusao = this.consumirServico(endpointInclusaoProcesso, EnumMetodoHTTP.POST, corpoMensagem);
                    mensagemLogResposta = MessageFormat.format("COMUNICACAO FINALIZADA COM O BPM PARA CRIACAO DA INSTANCIA. DOSSIE = {0} | HTTP STATUS = {1} | RESPOSTA RECEBIDA: {2}", dossieProduto.getId(), responseInclusao.getStatus(), responseInclusao.getEntity());
                    LOGGER.info(mensagemLogResposta);

                    return Long.valueOf(responseInclusao.getEntity().toString());
                } catch (Exception ex) {
                    boolean servicoDisponivel = (responseInclusao != null && Status.SERVICE_UNAVAILABLE.getStatusCode() != responseInclusao.getStatus());
                    throw new BpmException("BS.cPBPM.002 - Falha ao comunicar com o BPM", ex, servicoDisponivel);
                }
            }
        }

        return null;
    }

    /**
     * Executa uma ação de notificação junto a solução de BPM de acordo com ultima situação apresentada no dossiê de produto. O método possui anotação de permissão
     * para todos os usuários devido a possibilidade de visualização do Dashboard em que o BPM deve ser notificado para os casos de dossiês em situações de
     * comunicação prevista.
     *
     * @param dossieProduto - Objeto dossiê de produto utilizado para realizar a comunicação junto ao BPM baseado nas configurações definidas no processo gerador de
     *        dossiê parametrizados na base do SIMTR
     * @return true se a comunicação foi realiza e false caso contrario
     * @throws BpmException Lançada caso ocorra alguma falha de comunicação com a solução
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean notificaBPM(DossieProduto dossieProduto) {

        if (this.realizarComunicacaoBPM(dossieProduto) && dossieProduto.getIdInstanciaProcessoBPM() != null) {

            String containerId = dossieProduto.getNomeContainerBPM();
            Long instanceId = dossieProduto.getIdInstanciaProcessoBPM();
            Long dossieId = dossieProduto.getId();

            TipoSituacaoDossie situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream().max(Comparator.comparing(SituacaoDossie::getId)).get().getTipoSituacaoDossie();

            // Dossiê cancelado pela unidade de criação da operação
            if (tipoSituacaoDossieCancelado.equals(situacaoAtualDossie)) {
                this.abortarProcessoBPM(containerId, instanceId, dossieId);
                return Boolean.TRUE;
            }

            // Identifica o sinal adequado de acordo com a situação do dossiê
            BPMSinalEnum sinal = this.definirSinalEnvio(situacaoAtualDossie);

            // Caso seja identificado algum sinal para a situação envia notificação ao BPM
            if (sinal != null) {
                // Sensibiliza a instancia de processo principal
                this.enviarSinal(containerId, instanceId, sinal);
            }
            
            return Boolean.TRUE;
        }
        
        return Boolean.FALSE;
    }

    public boolean verificaInstanciaEsperaSinalTratamento(DossieProduto dossieProduto) {
        if (this.realizarComunicacaoBPM(dossieProduto) && dossieProduto.getIdInstanciaProcessoBPM() != null) {
            return checarSinalEsperadoBPM(dossieProduto.getNomeContainerBPM(), dossieProduto.getIdInstanciaProcessoBPM(), BPMSinalEnum.TRATAMENTO_PENDENTE);
        }

        return Boolean.FALSE;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void abortarProcessoBPM(String containerId, Long instanceId, Long dossieId) {

        String endpoint = this.urlBPM.concat(ENDPOINT_ABORT_INSTANCE.replace(PARAMETRO_CID, containerId)
                                                                    .replace(PARAMETRO_INSTANCEID, instanceId.toString()));

        try {
            String mensagemLogEnvio = MessageFormat.format("COMUNICACAO INICIADA COM O BPM: ABORTANDO INSTANCIA BPM = {0}", instanceId);
            LOGGER.info(mensagemLogEnvio);
            Response response = this.consumirServico(endpoint, EnumMetodoHTTP.DELETE, null);
            String mensagemLogResposta = MessageFormat.format("COMUNICACAO FINALIZADA COM O BPM PARA ABORTAR PROCESSO. INSTANCIA = {0} | HTTP STATUS = {1}", instanceId, response.getStatus());
            LOGGER.info(mensagemLogResposta);
            if (Status.SERVICE_UNAVAILABLE.getStatusCode() == response.getStatus()) {
                throw new BpmException("BS.aPBPM.001 - Falha ao comunicar com o BPM. Serviço Indisponivel", Boolean.FALSE);
            } else if (Status.Family.SERVER_ERROR.equals(response.getStatusInfo().getFamily())) {
                throw new BpmException("BS.aPBPM.002 - Falha não mapeada ao comunicar com o BPM.", Boolean.TRUE);
            }
        } catch (NullPointerException ex) {
            throw new BpmException("BS.aPBPM.003 - Falha ao comunicar com o BPM. Resposta Nula.", ex, Boolean.FALSE);
        }
    }

    // *********** Métodos Privados ***********
    @PostConstruct
    private void carregarDadosAcesso() {
        urlAPI = UtilParametro.getParametro(PROPRIEDADE_URL_API, Boolean.TRUE);
        urlSSO = UtilParametro.getParametro(PROPRIEDADE_URL_SSO, Boolean.TRUE);
        urlBPM = UtilParametro.getParametro(PROPRIEDADE_URL_BPM, Boolean.TRUE);
        apiKey = UtilParametro.getParametro(PROPRIEDADE_URL_API_KEY, Boolean.TRUE);
        urlAPIManager = UtilParametro.getParametro(PROPRIEDADE_URL_API_MANAGER, Boolean.TRUE);
        urlKeystore = UtilParametro.getParametro(PROPRIEDADE_KEYSTORE_URL, Boolean.FALSE);

        if (urlKeystore != null) {
            this.carregarDadosAcessoKeystore();
        } else {
            this.carregarDadosAcessoPropriedade();
        }

        tipoSituacaoDossieAlimentacaoFinalizada = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.ALIMENTACAO_FINALIZADA);
        tipoSituacaoDossieConforme = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.CONFORME);
        tipoSituacaoDossieCriado = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.CRIADO);
        tipoSituacaoDossiePendenteInformacao = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.PENDENTE_INFORMACAO);
        tipoSituacaoDossiePendenteSeguranca = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.PENDENTE_SEGURANCA);
        tipoSituacaoDossieSegurancaFinalizado = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.SEGURANCA_FINALIZADO);
        tipoSituacaoDossieCancelado = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.CANCELADO);
    }

    private void carregarDadosAcessoPropriedade() {
        String usuario = UtilParametro.getParametro(PROPRIEDADE_USER_BPM, Boolean.FALSE);
        String senha = UtilParametro.getParametro(PROPRIEDADE_PASS_BPM, Boolean.FALSE);
        if (usuario != null && senha != null) {
            this.basicAuth = Base64.getEncoder().encodeToString(usuario.concat(":").concat(senha).getBytes());
        }
    }

    private void carregarDadosAcessoKeystore() {
        // Captura de senha do usuário baseado no armazenamento em repositorio JCEKS
        String usuario = UtilParametro.getParametro(PROPRIEDADE_USER_BPM, Boolean.TRUE);
        String keyStoreSecret = UtilParametro.getParametro(PROPRIEDADE_KEYSTORE_SECRET, Boolean.TRUE);
        String keyStoreAliasBPM = UtilParametro.getParametro(PROPRIEDADE_KEYSTORE_BPM_ALIAS, Boolean.TRUE);
        String keyStoreSecretBPM = UtilParametro.getParametro(PROPRIEDADE_KEYSTORE_BPM_SECRET, Boolean.TRUE);
        String keyStoreType = UtilParametro.getParametro(PROPRIEDADE_KEYSTORE_TYPE, Boolean.TRUE);

        // Seta o valor de basicAuth utilizado para realizar a comunicação com
        String senha = KeyStoreUtil.getDataFromKeyStore(urlKeystore, keyStoreType, keyStoreSecret, keyStoreAliasBPM, keyStoreSecretBPM);
        this.basicAuth = Base64.getEncoder().encodeToString(usuario.concat(":").concat(senha).getBytes());
    }

    private boolean realizarComunicacaoBPM(DossieProduto dossieProduto) {
        if (Objects.isNull(urlBPM)) {
            this.carregarDadosAcesso();
        }

        if (dossieProduto.getDataHoraFinalizacao() != null) {
            return Boolean.FALSE;
        }
        
        //Caso o dossiê de produto não possua definição de processo e container BPM, registra um aviso em log indicando a ausencia da informação no dossiê 
        if (Objects.isNull(dossieProduto.getNomeContainerBPM()) || Objects.isNull(dossieProduto.getNomeProcessoBPM())) {
            Long idDossie = dossieProduto.getId();
            Integer idProcesso = dossieProduto.getProcesso().getId();
            String nomeProcesso = dossieProduto.getProcesso().getNome();
            String mensagem = MessageFormat.format("BS.vPD.001 - Dossiê de Produto {0} vinculado ao processo {1} - {2} não possui os parametros de container e processo definidos para o BPM", idDossie, idProcesso, nomeProcesso);
            LOGGER.log(Level.WARNING, mensagem);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private BPMSinalEnum definirSinalEnvio(TipoSituacaoDossie situacaoAtualDossie) {
        // Dossiê recem criado
        if (tipoSituacaoDossieCriado.equals(situacaoAtualDossie)) {
            return BPMSinalEnum.CRIADO;
        }

        // Finalização da alimentação por uma unidade
        if (tipoSituacaoDossieAlimentacaoFinalizada.equals(situacaoAtualDossie)) {
            return BPMSinalEnum.ALIMENTACAO_FINALIZADA;
        }

        // Dossiê pós tratamento em situação conforme
        if (tipoSituacaoDossieConforme.equals(situacaoAtualDossie)) {
            return BPMSinalEnum.TRATAMENTO_CONFORME;
        }

        // Dossiê pós tratamento com pendencias de informação/documento rejeitado
        if (tipoSituacaoDossiePendenteInformacao.equals(situacaoAtualDossie)) {
            return BPMSinalEnum.TRATAMENTO_INFORMACAO;
        }

        // Dossiê pós tratamento com pendencias de analise da area de segurança
        if (tipoSituacaoDossiePendenteSeguranca.equals(situacaoAtualDossie)) {
            return BPMSinalEnum.TRATAMENTO_SEGURANCA;
        }

        // Finalização da análise da área de segurança
        if (tipoSituacaoDossieSegurancaFinalizado.equals(situacaoAtualDossie)) {
            return BPMSinalEnum.SEGURANCA_FINALIZADO;
        }

        // Caso a situação não preveja envio de sinal, retorna o valor null
        return null;
    }

    private void enviarSinal(String containerId, Long instanceId, BPMSinalEnum bpmSinalEnum) {

        if (this.checarSinalEsperadoBPM(containerId, instanceId, bpmSinalEnum)) {
            String endpoint = this.urlBPM.concat(ENDPOINT_SIGNAL_INSTANCE.replace(PARAMETRO_CID, containerId)
                                                                         .replace(PARAMETRO_INSTANCEID, instanceId.toString())
                                                                         .replace(PARAMETRO_SIGNALNAME, bpmSinalEnum.getSinal()));

            try {
                String mensagemLogEnvio = MessageFormat.format("COMUNICACAO INICIADA COM O BPM: ENVIADO SINAL = {0} | INSTANCIA BPM = {1} | BODY = {2}", bpmSinalEnum.getSinal(), instanceId, bpmSinalEnum.getSituacaoJSON());
                LOGGER.info(mensagemLogEnvio);
                Response response = this.consumirServico(endpoint, EnumMetodoHTTP.POST, bpmSinalEnum.getSituacaoJSON());
                String mensagemLogResposta = MessageFormat.format("COMUNICACAO FINALIZADA COM O BPM PARA ENVIO DE SINAL. INSTANCIA = {0} | HTTP STATUS = {1}", instanceId, response.getStatus());
                LOGGER.info(mensagemLogResposta);

                if (Status.SERVICE_UNAVAILABLE.getStatusCode() == response.getStatus()) {
                    throw new BpmException("BS.eS.001 - Falha ao comunicar com o BPM. Serviço Indisponivel", Boolean.FALSE);
                } else if (Status.Family.SERVER_ERROR.equals(response.getStatusInfo().getFamily())) {
                    throw new BpmException("BS.eS.002 - Falha não mapeada ao comunicar com o BPM.", Boolean.TRUE);
                }
            } catch (NullPointerException ex) {
                throw new BpmException("BS.eS.003 - Falha ao comunicar com o BPM. Resposta Nula", ex, Boolean.FALSE);
            }
        }
    }

    private boolean checarSinalEsperadoBPM(String containerId, Long instanceId, BPMSinalEnum sinalChecagem) {

        String endpoint = this.urlBPM.concat(ENDPOINT_CHECK_SIGNAL.replace(PARAMETRO_CID, containerId)
                                                                  .replace(PARAMETRO_INSTANCEID, instanceId.toString()));

        Response response = null;
        try {
            String mensagemLogEnvio = MessageFormat.format("COMUNICACAO INICIADA COM O BPM: CHECANDO SINAIS DISPONIVEIS PARA A INSTANCIA = {0}", instanceId);
            LOGGER.info(mensagemLogEnvio);
            response = this.consumirServico(endpoint, EnumMetodoHTTP.GET, null);
            String mensagemLogResposta = MessageFormat.format("COMUNICACAO FINALIZADA COM O BPM CHECAGEM DE SINAIS. INSTANCIA = {0} | HTTP STATUS = {1} | SINAIS DISPONIVEIS = {2}", instanceId, response.getStatus(), response.getEntity());
            LOGGER.info(mensagemLogResposta);

            if (Status.SERVICE_UNAVAILABLE.getStatusCode() == response.getStatus()) {
                throw new BpmException("BS.cSEBPM.001 - Falha ao comunicar com o BPM. Serviço Indisponivel", Boolean.FALSE);
            } else if (Status.Family.SERVER_ERROR.equals(response.getStatusInfo().getFamily())) {
                throw new BpmException("BS.cSEBPM.002 - Falha não mapeada ao comunicar com o BPM.", Boolean.TRUE);
            }
        } catch (NullPointerException ex) {
            throw new BpmException("BS.cSEBPM.003 - Falha ao comunicar com o BPM. Resposta Nula.", ex, Boolean.FALSE);
        }
        return response.getEntity().toString().toUpperCase().contains(sinalChecagem.getSinal());
    }

    private Response consumirServico(String endpoint, EnumMetodoHTTP metodoHTTP, String body) {
        if (this.basicAuth != null) {
            return UtilRest.consumirServicoBasicAuthJSON(endpoint, metodoHTTP, this.headers, this.params, body, this.basicAuth);
        } else {
            String token = this.keycloakUtil.getTokenServico();
            return UtilRest.consumirServicoOAuth2JSON(endpoint, metodoHTTP, this.headers, this.params, body, token);
        }
    }
}
