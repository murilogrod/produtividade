package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.CampoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.DocumentoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMCriaDossieDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMCriaTransacaoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMDadoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMGravaDocumentoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMRespostaDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMRespostaDefaultDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMRespostaExtracaoDadosDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.GravaDocumentoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.RespostaGravaDocumentoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.RetornoPesquisaDTO;
import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.GEDService;
import br.gov.caixa.pedesgo.arquitetura.util.UtilParametro;
import br.gov.caixa.pedesgo.arquitetura.util.UtilRest;
import br.gov.caixa.pedesgo.arquitetura.util.UtilXml;
import br.gov.caixa.simtr.controle.excecao.SiecmException;
import br.gov.caixa.simtr.controle.servico.helper.SiecmObjetosHelper;
import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.util.KeycloakUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRAUD,
    ConstantesUtil.PERFIL_MTRTEC,
    ConstantesUtil.PERFIL_MTRDOSINT,
    ConstantesUtil.PERFIL_MTRDOSMTZ,
    ConstantesUtil.PERFIL_MTRDOSOPE,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SiecmServico {

    @EJB
    private GEDService gedService;

    @EJB
    private SiecmObjetosHelper siecmObjetosHelper;

    @Inject
    private KeycloakUtil keycloakUtil;

    private String urlSIECM;

    private static final String FALHA_SIECM = "Falha na comunicação com o SIECM. Recebido objeto nulo como resposta.";

    private static final String CAMPO_STATUS = "STATUS";
    private static final String PASTA_TRANSACOES = "TRANSACOES";

    private static final String ENDPOINT_CRIA_DOSSIE = "/ECM/reuso/dossie";
    private static final String ENDPOINT_CRIA_TRANSACAO = "/ECM/reuso/dossie/transacao";
    private static final String ENDPOINT_GRAVA_DOCUMENTO_CLIENTE = "/ECM/reuso/dossie/gravaDocumento";
    private static final String ENDPOINT_GRAVA_DOCUMENTO_TRANSACAO = "/ECM/gravaDocumento";

    private static final Logger LOGGER = Logger.getLogger(SiecmServico.class.getName());

    public Map<String, String> executaExtracaoDadosDocumento(TipoDocumento tipoDocumento, FormatoConteudoEnum formatoConteudoEnum, String binario) {

        // Realiza a chamada de extração dos dados ao SIECM-Datacap
        ECMRespostaExtracaoDadosDTO ecmRespostaExtracaoDadosDTO = this.gedService.extractInfo(tipoDocumento.getNomeClasseSIECM(), formatoConteudoEnum.name(), binario, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);

        if (ecmRespostaExtracaoDadosDTO == null) {
            throw new SiecmException("SS.eEDD.001 - ".concat(FALHA_SIECM), Boolean.FALSE);
        }

        if (ecmRespostaExtracaoDadosDTO.getCodigoRetorno() > 0) {
            throw new SiecmException(MessageFormat.format("SS.eEDD.002 - Falha ao extrair dados no SIECM: {0}", ecmRespostaExtracaoDadosDTO.getMensagem()), Boolean.TRUE);
        }

        // Identifica apenas os atributos esperados para o documento
        List<AtributoExtracao> atributosEsperados = tipoDocumento.getAtributosExtracao().stream()
                                                                 .filter(ae -> ae.getNomeAtributoSIECM() != null)
                                                                 .collect(Collectors.toList());

        // Transforma os dados retornados em um map para otimizar a busca das informações e passa a ter como chave o valor definido no atributo documento
        return ecmRespostaExtracaoDadosDTO.getEcmDadosImagemDTO().getDadosDTO().stream()
                                          .filter(dado -> atributosEsperados.stream().anyMatch(atributo -> atributo.getNomeAtributoSIECM().equals(dado.getNome())))
                                          .collect(Collectors.toMap(dado -> atributosEsperados.stream()
                                                                                              .filter(ae -> ae.getNomeAtributoSIECM().equals(dado.getNome()))
                                                                                              .findFirst().get().getNomeAtributoDocumento(), ECMDadoDTO::getValor));
    }

    public String armazenaDocumentoPessoalSIECM(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, Documento documento, TemporalidadeDocumentoEnum temporalidadeDocumentoEnum, String binario, String objectStore) {

        // Formata o CPF/CNPJ para ser utilizado como identificador da pasta no SIECM
        String cpfCnpjFormatado = StringUtils.leftPad(String.valueOf(cpfCnpj), TipoPessoaEnum.F.equals(tipoPessoaEnum) ? 11 : 14, '0');

        // Cria o dossiê do cliente
        this.criaDossieClienteSIECM(cpfCnpj, tipoPessoaEnum, objectStore);

        // Monta a requisição a ser enviada para o SIECM solicitando a criação do documento do cliente junto ao SIECM
        ECMGravaDocumentoDTO ecmGravaDocumentoDTO = this.siecmObjetosHelper.montaObjetoDocumentoCliente(cpfCnpj, tipoPessoaEnum, documento, binario, objectStore);

        // Prepara os parametros utilizados nas mensagens de log
        Long threadId = Thread.currentThread().getId();
        String xmlOriginal = UtilXml.converterParaXml(ecmGravaDocumentoDTO);
        String xmlLimpo = xmlOriginal.replaceAll("<binario>.*</binario>", "<binario>[CONTEUDO_BASE64]</binario>");

        // Monta as mensagens de log a serem utilizadas
        String mensagemLogEnvioLimpo = MessageFormat.format("COMUNICACAO INICIADA COM O SIECM: ARMAZENANDO DOCUMENTO PARA O CLIENTE = {0} | THREAD ID = {1} | CORPO = {2}", cpfCnpjFormatado, threadId, xmlLimpo);
        String mensagemLogEnvioOriginal = MessageFormat.format("COMUNICACAO INICIADA COM O SIECM: ARMAZENANDO DOCUMENTO PARA O CLIENTE = {0} | THREAD ID = {1} | CORPO = {2}", cpfCnpjFormatado, threadId, xmlLimpo);

        // Registra os logs de envio da solicitação
        LOGGER.info(mensagemLogEnvioLimpo);
        LOGGER.fine(mensagemLogEnvioOriginal);

        // Executa a chamada ao serviço do SIECM para armazenar o documento do cliente
        this.carregarDadosAcesso();
        String endpoint = urlSIECM.concat(ENDPOINT_GRAVA_DOCUMENTO_CLIENTE);
        Response response = UtilRest.consumirServicoOAuth2(endpoint, EnumMetodoHTTP.PUT, new HashMap<>(), new HashMap<>(), xmlOriginal, keycloakUtil.getTokenServico(), MediaType.APPLICATION_XML, MediaType.APPLICATION_XML);
        String respostaSiecm = response.getEntity() == null ? "null" : response.getEntity().toString();

        // Registra o retorno obtido na requisição
        String mensagemRetorno = MessageFormat.format("COMUNICACAO FINALIZADA COM O SIECM: RETORNO DO ARMAZENAMENTO DE DOCUMENTO PARA O CLIENTE | THREAD = {0} | HTTP STATUS = {1} | CORPO = {2}", threadId, response.getStatus(), respostaSiecm);
        LOGGER.info(mensagemRetorno);

        // Lança uma exceção de comunicação com o SIECM caso o retorno seja nulo.
        if (response.getEntity() == null) {
            throw new SiecmException("SS.aDPS.001 - ".concat(FALHA_SIECM), Boolean.FALSE);
        }

        // Converte a resposta obtida com o SIECM
        RespostaGravaDocumentoDTO respostaGravaDocumentoDTO;
        try{
            respostaGravaDocumentoDTO = UtilXml.converterParaObjeto(respostaSiecm, RespostaGravaDocumentoDTO.class);
        } catch (Exception e){
            String mensagem = MessageFormat.format("SS.aDPS.005 - Falha ao converter retorno do SIECM para gravação do documento pessoal. Mensagem = {0} | Causa = {1}", respostaSiecm, e.getLocalizedMessage());
            throw new SiecmException(mensagem, Boolean.FALSE);
        }

        // Identifica se houve algum código de erro apontado pelo SIECM na requisição
        String mensagem;
        StringBuilder respostasDocumento = new StringBuilder();
        if(Objects.nonNull(respostaGravaDocumentoDTO.getDocumentos())) {
            for(DocumentoDTO respostaDocumento : respostaGravaDocumentoDTO.getDocumentos()) {
                respostasDocumento.append(" Documento gravado: " + (respostaDocumento.isGravado() ? "Sim" : "Não") + 
                        "; mensagem: " + respostaDocumento.getMensagem() + 
                        "; atributos do documento: " + respostaDocumento.getAtributos().getId() + 
                        ", " + respostaDocumento.getAtributos().getClasse() + 
                        ", " + respostaDocumento.getAtributos().getNome() + 
                        ", " + respostaDocumento.getAtributos().getTipo() + 
                        ", " + respostaDocumento.getAtributos().getMimeType());
            }
        }
        if (Status.OK.getStatusCode() == response.getStatus()) {
            if (respostaGravaDocumentoDTO.getCodigoRetorno() > 0) {
                mensagem = MessageFormat.format("SS.aDPS.002 - Falha ao gravar documentos no SIECM para cliente {0}. Retorno: {1}", cpfCnpjFormatado, respostaGravaDocumentoDTO.getMensagem() + respostasDocumento.toString());
                throw new SiecmException(mensagem, Boolean.TRUE);
            }
        } else if (Status.SERVICE_UNAVAILABLE.getStatusCode() == response.getStatus()) {
            mensagem = MessageFormat.format("SS.aDPS.003 - Falha ao incluir documento no SIECM para cliente {0}. Retorno: {1}", cpfCnpjFormatado, respostaGravaDocumentoDTO.getMensagem() + respostasDocumento.toString());
            throw new SiecmException(mensagem, Boolean.FALSE);
        } else if (Status.Family.SERVER_ERROR.equals(response.getStatusInfo().getFamily())) {
            mensagem = MessageFormat.format("SS.aDPS.004 - Falha ao incluir documento no SIECM para cliente {0}. Retorno: {1}", cpfCnpjFormatado, respostaGravaDocumentoDTO.getMensagem() +  respostasDocumento.toString());
            throw new SiecmException(mensagem, Boolean.TRUE);
        }

        return respostaGravaDocumentoDTO.getDocumentos().get(0).getAtributos().getId();
    }

    public String armazenaDocumentoOperacaoSIECM(Documento documento, TemporalidadeDocumentoEnum temporalidadeDocumentoEnum, String binario, String objectStore, String subPasta) {
        // Realiza a criação da subpasta abaixo da pasta TRANSACOES
        this.criaTransacaoSIECM(subPasta, objectStore);

        return this.armazenaDocumentoOperacaoSIECM(documento, temporalidadeDocumentoEnum, binario, objectStore, PASTA_TRANSACOES, subPasta, null);
    }

    public String armazenaDocumentoOperacaoSIECM(Documento documento, TemporalidadeDocumentoEnum temporalidadeDocumentoEnum, String binario, String objectStore, String pasta, String subPasta, List<CampoDTO> camposAdicionais) {

        // Monta a requisição a ser enviada para o SIECM solicitando a criação do documento do cliente junto ao SIECM
        GravaDocumentoDTO gravaDocumentoDTO = this.siecmObjetosHelper.montaObjetoOperacao(documento, binario, pasta, subPasta, objectStore);

        // Percorre os campos enviados adicionalmente, atualiza a informação do objeto da lista se já existir, ou inclui caso contrario
        if (camposAdicionais != null) {
            camposAdicionais.forEach(campo -> {
                if (gravaDocumentoDTO.getDocumentos().get(0).getAtributos().getCampos().stream().noneMatch(c -> c.getNome().equals(campo.getNome()))) {
                    gravaDocumentoDTO.getDocumentos().get(0).getAtributos().getCampos().add(campo);
                } else {
                    gravaDocumentoDTO.getDocumentos().get(0).getAtributos().getCampos().stream().filter(c -> c.getNome().equals(campo.getNome())).findFirst().ifPresent(c -> {
                        c.setValor(campo.getValor());
                    });
                }
            });
        }

        // Prepara os parametros utilizados nas mensagens de log
        Long threadId = Thread.currentThread().getId();
        String xmlOriginal = UtilXml.converterParaXml(gravaDocumentoDTO);
        String xmlLimpo = xmlOriginal.replaceAll("<binario>.*</binario>", "<binario>[CONTEUDO_BASE64]</binario>");

        // Monta as mensagens de log a serem utilizadas
        String mensagemLogEnvioLimpo = MessageFormat.format("COMUNICACAO INICIADA COM O SIECM: ARMAZENANDO DOCUMENTO DE OPERACAO NA PASTA {0} | ID DOCUMENTO = {1} | THREAD ID = {2} | CORPO = {3}", pasta, String.valueOf(documento.getId()), threadId, xmlLimpo);
        String mensagemLogEnvioOriginal = MessageFormat.format("COMUNICACAO INICIADA COM O SIECM: ARMAZENANDO DOCUMENTO DE OPERACAO NA PASTA {0} | ID DOCUMENTO = {1} THREAD ID = {2} | CORPO = {3}", pasta, String.valueOf(documento.getId()), threadId, xmlLimpo);

        // Registra os logs de envio da solicitação
        LOGGER.info(mensagemLogEnvioLimpo);
        LOGGER.fine(mensagemLogEnvioOriginal);

        // Executa a chamada ao serviço do SIECM para armazenar o documento do cliente
        this.carregarDadosAcesso();
        String endpoint = urlSIECM.concat(ENDPOINT_GRAVA_DOCUMENTO_TRANSACAO);
        Response response = UtilRest.consumirServicoOAuth2(endpoint, EnumMetodoHTTP.PUT, new HashMap<>(), new HashMap<>(), xmlOriginal, keycloakUtil.getTokenServico(), MediaType.APPLICATION_XML, MediaType.APPLICATION_XML);
        String respostaSiecm = response.getEntity() == null ? "null" : response.getEntity().toString();

        // Registra o retorno obtido na requisição
        String mensagemRetorno = MessageFormat.format("COMUNICACAO FINALIZADA COM O SIECM: RETORNO DO ARMAZENAMENTO DE DOCUMENTO PARA OPERACAO | THREAD = {0} | HTTP STATUS = {1} | CORPO = {2}", threadId, response.getStatus(), respostaSiecm);
        LOGGER.info(mensagemRetorno);

        // Lança uma exceção de comunicação com o SIECM caso o retorno seja nulo.
        if (response.getEntity() == null) {
            throw new SiecmException("SS.aDOS.001 - ".concat(FALHA_SIECM), Boolean.FALSE);
        }

        // Converte a resposta obtida com o SIECM
        RespostaGravaDocumentoDTO respostaGravaDocumentoDTO;
        try{
            respostaGravaDocumentoDTO = UtilXml.converterParaObjeto(respostaSiecm, RespostaGravaDocumentoDTO.class);
        } catch (Exception e){
            String mensagem = MessageFormat.format("SS.aDOS.005 - Falha ao converter retorno do SIECM para gravação do documento. Mensagem = {0} | Causa = {1}", respostaSiecm, e.getLocalizedMessage());
            throw new SiecmException(mensagem, Boolean.FALSE);
        }

        // Identifica se houve algum código de erro apontado pelo SIECM na requisição
        String mensagem;
        if (Status.OK.getStatusCode() == response.getStatus()) {
            if (respostaGravaDocumentoDTO.getCodigoRetorno() > 0) {
                mensagem = MessageFormat.format("SS.aDOS.002 - Falha ao gravar documento no SIECM para operacao na pasta {0}. Retorno: {1}", pasta, respostaGravaDocumentoDTO.getMensagem());
                throw new SiecmException(mensagem, Boolean.TRUE);
            }
        } else if (Status.SERVICE_UNAVAILABLE.getStatusCode() == response.getStatus()) {
            mensagem = MessageFormat.format("SS.aDOS.003 - Falha ao incluir documento no SIECM para operacao na pasta {0}. Retorno: {1}", pasta, respostaGravaDocumentoDTO.getMensagem());
            throw new SiecmException(mensagem, Boolean.FALSE);
        } else if (Status.Family.SERVER_ERROR.equals(response.getStatusInfo().getFamily())) {
            mensagem = MessageFormat.format("SS.aDOS.004 - Falha ao incluir documento no SIECM para operação na pasta {0}. Retorno: {1}", pasta, respostaGravaDocumentoDTO.getMensagem());
            throw new SiecmException(mensagem, Boolean.TRUE);
        }

        return respostaGravaDocumentoDTO.getDocumentos().get(0).getAtributos().getId();
    }

    public String obtemLinkAcessoDocumentoSIECM(String identificadorGED, String objectStore) {
        // Realiza a busca do documento junto ao SIECM para obtenção do link do documento
        RetornoPesquisaDTO retornoPesquisaDTO = this.gedService.searchDocument(identificadorGED, objectStore);
        if (retornoPesquisaDTO == null) {
            throw new SiecmException("SS.oLADS.001 - ".concat(FALHA_SIECM), Boolean.FALSE);
        }
        if (retornoPesquisaDTO.getCodigoRetorno() > 0) {
            throw new SiecmException(MessageFormat.format("SS.oLADS.001 - Falha ao capturar link do documento no SIECM. Identificador GED: {0}", identificadorGED), Boolean.TRUE);
        }

        return retornoPesquisaDTO.getDadosDocumentoLocalizados().get(0).getLink();
    }

    public void alteraSituacaoTemporalidadeDocumentoSIECM(String identificadorGED, TemporalidadeDocumentoEnum temporalidadeDocumentoEnum, String objectStore) {

        final List<CampoDTO> camposDTO = new ArrayList<>();
        camposDTO.add(new CampoDTO(CAMPO_STATUS, temporalidadeDocumentoEnum.getId(), TipoAtributoEnum.STRING.name(), Boolean.TRUE));

        ECMRespostaDefaultDTO respostaAtualizacaoAtributosDocumentoECM = this.gedService.updateInfoDocument(identificadorGED, camposDTO, objectStore);
        if (respostaAtualizacaoAtributosDocumentoECM == null) {
            throw new SiecmException("SS.aSTDS.001 - Falha na comunicação com o SIECM. Recebido objeto nulo na inclusão do documento", Boolean.FALSE);
        }
        if (respostaAtualizacaoAtributosDocumentoECM.getCodigoRetorno() > 0) {
            String mensagem = MessageFormat.format("SS.aSTDS.002 - Falha na comunicação com o SIECM. Retorno: {0}", respostaAtualizacaoAtributosDocumentoECM.getMensagem());
            throw new SiecmException(mensagem, Boolean.TRUE);
        }
    }

    /**
     * Atualiza os dados do documento junto ao SIECM baseado em um documento com os atributos extraidos. Esta chamada deve ser utilizada para atualizar os atributos a partir de dados pre-existentes no
     * objeto do documento
     * 
     * @param documento Documento a ter seus dados atualizados junto so SIECM
     */
    public void atualizaAtributosDocumento(Documento documento) {

        List<CampoDTO> camposAtualizacao = this.siecmObjetosHelper.montaCamposDocumentoSIECM(documento.getTipoDocumento(), documento.getAtributosDocumento());

        if (documento.getCodigoGED() != null) {
            ECMRespostaDefaultDTO respostaAtualizacaoStatusDocumentoECM = this.gedService.updateInfoDocument(documento.getCodigoGED(), camposAtualizacao, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
            if (respostaAtualizacaoStatusDocumentoECM == null) {
                throw new SiecmException("SS.aAD.001 - ".concat(FALHA_SIECM), Boolean.FALSE);
            }
            if (respostaAtualizacaoStatusDocumentoECM.getCodigoRetorno() > 0) {
                throw new SiecmException("SS.aAD.002 - Falha ao atualizar atributos do documento original. Motivo: ".concat(respostaAtualizacaoStatusDocumentoECM.getMensagem()), Boolean.TRUE);
            }
        }

        if (documento.getCodigoSiecmTratado() != null) {
            ECMRespostaDefaultDTO respostaAtualizacaoStatusDocumentoECM = this.gedService.updateInfoDocument(documento.getCodigoSiecmTratado(), camposAtualizacao, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
            if (respostaAtualizacaoStatusDocumentoECM == null) {
                throw new SiecmException("SS.aAD.003 - ".concat(FALHA_SIECM), Boolean.FALSE);
            }
            if (respostaAtualizacaoStatusDocumentoECM.getCodigoRetorno() > 0) {
                throw new SiecmException("SS.aAD.004 - Falha ao atualizar atributos do documento tratado. Motivo: ".concat(respostaAtualizacaoStatusDocumentoECM.getMensagem()), Boolean.TRUE);
            }
        }
    }

    /**
     * Atualiza os dados do documento junto ao SIECM baseado em atributos especificos informados. Esta chamada pode ser utilizada por exemplo para atualizar
     * 
     * @param identificadorDocumento Identificador do documento a ser atualizado
     * @param objectStore ObjectStore que deve ser referenciado em que o documento esta armazenado
     * @param camposAtualizacao Campos do documento que devem ser atualizados
     */
    public void atualizaAtributosDocumento(String identificadorDocumento, String objectStore, CampoDTO... camposAtualizacao) {
        /**
         * Este método trata-se de uma sobrecarga, por este motivo a sequencia das mensagens de exceção dão continuidade do método anterior
         */

        ECMRespostaDefaultDTO respostaAtualizacaoStatusDocumentoECM = this.gedService.updateInfoDocument(identificadorDocumento, Arrays.asList(camposAtualizacao), objectStore);
        if (respostaAtualizacaoStatusDocumentoECM == null) {
            throw new SiecmException("SS.aAD.005 - ".concat(FALHA_SIECM), Boolean.FALSE);
        }
        if (respostaAtualizacaoStatusDocumentoECM.getCodigoRetorno() > 0) {
            throw new SiecmException("SS.aAD.006 - Falha ao atualizar atributos do documento. Motivo: ".concat(respostaAtualizacaoStatusDocumentoECM.getMensagem()), Boolean.TRUE);
        }
    }

    /**
     * Método utilizado para executar a criação de uma pasta de transação junto a solução de ECM vinculado a um cliente
     *
     * @param cpfCnpj CPF/CNPJ do cliente
     * @param tipoPessoaEnum Indicação do tipo de pessoa relacionado com o CPF/CNPJ informado
     * @param diretorio Nome do diretorio que representa a transação junto ao SIECM
     * @param objectStore Indicação do objectStore do SIECM cuja tranalção deverá ser criada
     * @throws SiecmException Lançada quando houve falha na comunicação com a soluução de ECM indicando o motivo apresentado
     */
    public void criaTransacaoClienteSIECM(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, String diretorio, String objectStore) {

        // Cria uma subpasta com o CPF/CNPJ do cliente informado sob o diretorio criado na chamada acima
        ECMRespostaDTO retornoTransacaoECM = this.gedService.createFolderTransactional(cpfCnpj, TipoPessoaEnum.F.equals(tipoPessoaEnum), diretorio, objectStore);

        // Caso gere erros no processamente procede o cancelamento, código de retorno = 1 significa pasta já criada
        if (retornoTransacaoECM.getCodigoRetorno() > 1) {
            throw new SiecmException("SS.cTCS.001 - Falha ao criar transação para o ciente no SIECM. Motivo: ".concat(retornoTransacaoECM.getMensagem()), Boolean.TRUE);
        }
    }

    /**
     * Método utilizado para executar a criação de uma pasta de transação junto a solução de ECM e vincular os documentos utilizados na concessão da autorização
     *
     * @param cpfCnpj CPF/CNPJ do cliente
     * @param tipoPessoaEnum Indicação do tipo de pessoa relacionado com o CPF/CNPJ informado
     * @param diretorio Nome do diretorio que representa a transação junto ao SIECM
     * @param identificadoresDocumentos Identificadores dos documentos do SIECM que devem ser associados a transação
     * @throws SiecmException Lançada quando houve falha na comunicação com a soluução de ECM indicando o motivo apresentado
     */
    public void vinculaDocumentosTransacaoSIECM(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, String diretorio, final List<String> identificadoresDocumentos) {
        ECMRespostaDTO retornoDocumentosECM = this.gedService.attachDocumentToTransactinalFolder(cpfCnpj, TipoPessoaEnum.F.equals(tipoPessoaEnum), diretorio, identificadoresDocumentos, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);

        // Caso gere erros no processamente (codigo 1 ou 2) procede o cancelamento
        if (retornoDocumentosECM.getCodigoRetorno() > 0) {
            throw new SiecmException("SS.vDTS.001 - Falha ao vincular documentos a transação no SIECM. Motivo: ".concat(retornoDocumentosECM.getMensagem()), Boolean.TRUE);
        }
    }

    // *********** Métodos privados auxiliares ***************
    private void carregarDadosAcesso() {
        urlSIECM = UtilParametro.getParametro("url_siecm_ged", Boolean.TRUE);
        if (urlSIECM.endsWith("/")) {
            urlSIECM = urlSIECM.substring(0, urlSIECM.length() - 1);
        }
    }

    /**
     * Método utilizado para executar a criação de uma pasta de transação junto a solução de ECM
     *
     * @param diretorio Nome do diretorio que representa a transação junto ao SIECM
     * @param objectStore Indicação do objectStore do SIECM cuja tranalção deverá ser criada
     * @throws SiecmException Lançada quando houve falha na comunicação com a soluução de ECM indicando o motivo apresentado
     */
    private void criaTransacaoSIECM(final String diretorio, final String objectStore) {
        // Monta a requisição a ser enviada para o SIECM solicitando a criação da transação
        ECMCriaTransacaoDTO ecmCriaTransacaoDTO = this.siecmObjetosHelper.montaObjetoTransacao(diretorio, objectStore);

        // Transfoma o objeto que representa o corpo da requisição a ser enviada em um XML
        String xmlOriginal = UtilXml.converterParaXml(ecmCriaTransacaoDTO);

        // Executa a chamada ao serviço do SIECM para cria a pasta da transação
        this.carregarDadosAcesso();
        String endpoint = urlSIECM.concat(ENDPOINT_CRIA_TRANSACAO);
        Response response = UtilRest.consumirServicoOAuth2(endpoint, EnumMetodoHTTP.PUT, new HashMap<>(), new HashMap<>(), xmlOriginal, keycloakUtil.getTokenServico(), MediaType.APPLICATION_XML, MediaType.APPLICATION_XML);
        String respostaSiecm = response.getEntity() == null ? null : response.getEntity().toString();

        // Caso a solicitação não gere retorno procede o cancelamento
        if (respostaSiecm == null) {
            throw new SiecmException("SS.cTS.001 - Serviço do SIECM indisponível", Boolean.FALSE);
        }

        // Transfoma o XML que representa o corpo da respota do SIECM em objeto para manipulação
        ECMRespostaDTO respostaDTO;
        try{
            respostaDTO = UtilXml.converterParaObjeto(respostaSiecm, ECMRespostaDTO.class);
        } catch (Exception e){
            String mensagem = MessageFormat.format("SS.cTS.003 - Falha ao converter retorno do SIECM para criação da Transação. Mensagem = {0} | Causa = {1}", respostaSiecm, e.getLocalizedMessage());
            throw new SiecmException(mensagem, Boolean.FALSE);
        }

        // Caso gere erros no processamento (codigo 2) procede o cancelamento
        if (respostaDTO.getCodigoRetorno() > 1) {
            String mensagem = MessageFormat.format("SS.cTS.002 - Falha ao criar pasta da operação junto ao SIECM. Codigo de Retorno = {0} | Mensagem = {1}", respostaDTO.getCodigoRetorno(), respostaDTO.getMensagem());
            throw new SiecmException(mensagem, Boolean.TRUE);
        }
    }

    private void criaDossieClienteSIECM(final Long cpfCnpj, final TipoPessoaEnum tipoPessoaEnum, final String objectStore) {
        // Monta a requisição a ser enviada para o SIECM solicitando a criação da transação
        ECMCriaDossieDTO ecmCriaDossieDTO = this.siecmObjetosHelper.montaObjetoDossie(cpfCnpj, tipoPessoaEnum, objectStore);

        // Transfoma o objeto que representa o corpo da requisição a ser enviada em um XML
        String xmlOriginal = UtilXml.converterParaXml(ecmCriaDossieDTO);

        // Executa a chamada ao serviço do SIECM para cria a pasta da transação
        this.carregarDadosAcesso();
        String endpoint = urlSIECM.concat(ENDPOINT_CRIA_DOSSIE);
        Response response = UtilRest.consumirServicoOAuth2(endpoint, EnumMetodoHTTP.PUT, new HashMap<>(), new HashMap<>(), xmlOriginal, keycloakUtil.getTokenServico(), MediaType.APPLICATION_XML, MediaType.APPLICATION_XML);
        String respostaSiecm = response.getEntity() == null ? null : response.getEntity().toString();

        // Caso a solicitação não gere retorno procede o cancelamento
        if (respostaSiecm == null) {
            throw new SiecmException("SS.cDCS.001 - Serviço do SIECM indisponível", Boolean.FALSE);
        }
        
        // Transfoma o XML que representa o corpo da respota do SIECM em objeto para manipulação
        ECMRespostaDTO respostaDTO;
        try{
            respostaDTO = UtilXml.converterParaObjeto(respostaSiecm, ECMRespostaDTO.class);
        } catch (Exception e){
            String mensagem = MessageFormat.format("SS.cDCS.003 - Falha ao converter retorno do SIECM para criação do Dossiê de Cliente. Mensagem = {0} | Causa = {1}", respostaSiecm, e.getLocalizedMessage());
            throw new SiecmException(mensagem, Boolean.FALSE);
        }

        // Caso gere erros no processamento (codigo 2) procede o cancelamento
        if (respostaDTO.getCodigoRetorno() > 1) {
            String mensagem = MessageFormat.format("SS.cDCS.002 - Falha ao criar pasta da cliente junto ao SIECM. Codigo de Retorno = {0} | Mensagem = {1}", respostaDTO.getCodigoRetorno(), respostaDTO.getMensagem());
            throw new SiecmException(mensagem, Boolean.TRUE);
        }
    }
}
