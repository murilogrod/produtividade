package br.gov.caixa.simtr.controle.servico;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.DadosDocumentoLocalizadoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.RetornoPesquisaDTO;
import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.GEDService;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.pedesgo.arquitetura.util.UtilParametro;
import br.gov.caixa.pedesgo.arquitetura.util.UtilRest;
import br.gov.caixa.pedesgo.arquitetura.util.UtilWS;
import br.gov.caixa.pedesgo.arquitetura.util.UtilXml;
import br.gov.caixa.simtr.controle.excecao.SicodException;
import br.gov.caixa.simtr.controle.excecao.SiecmException;
import br.gov.caixa.simtr.controle.excecao.SifrcException;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.controle.vo.sifrc.CampoVO;
import br.gov.caixa.simtr.controle.vo.sifrc.DocumentoVO;
import br.gov.caixa.simtr.controle.vo.sifrc.RespostaAvaliacaoSifrcVO;
import br.gov.caixa.simtr.controle.vo.sifrc.SolicitacaoAvaliacaoSifrcVO;
import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.ControleDocumento;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.enumerator.SIFRCOperacaoEnum;
import br.gov.caixa.simtr.modelo.enumerator.SIFRCRecomendacaoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.CalendarUtil;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.util.KeycloakUtil;

/**
 *
 * @author c090347
 */
@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRDOSINT,
    ConstantesUtil.PERFIL_MTRDOSMTZ,
    ConstantesUtil.PERFIL_MTRDOSOPE,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class AvaliacaoFraudeServico {

    @Inject
    private EntityManager entityManager;

    @EJB
    private GEDService gedService;

    @EJB
    private KeycloakService keycloakService;

    @Inject
    private CalendarUtil calendarUtil;

    @Inject
    private KeycloakUtil keycloakUtil;

    private static final Logger LOGGER = Logger.getLogger(AvaliacaoFraudeServico.class.getName());

    private static final String ATRIBUTO_SICOD_CGC_UNIDADE_CAIXA = "nuCgcUnidade";
    private static final String ATRIBUTO_SICOD_CPF_USUARIO_CAIXA = "nuCpfUsuarioCaixa";
    private static final String ATRIBUTO_SICOD_MATRICULA_USUARIO_CAIXA = "nuMatriculaUsuarioCaixa";
    private static final String ATRIBUTO_SICOD_SITUACAO_CNH = "situacaoCNH";

    public boolean recuperaResultadoAvaliacaoAutenticidade(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, Documento documento) {
        Boolean aprovadoSICOD = Boolean.TRUE;
        if (documento.getTipoDocumento().getEnviaAvaliacaoSICOD()) {
            aprovadoSICOD = submeteDocumentoSICOD(documento);
        }
        return aprovadoSICOD;
    }

    public boolean submeteDocumentoSIFRC(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, Documento documento, boolean novoCadastro) {

        RetornoPesquisaDTO retornoGED = this.gedService.searchDocument(documento.getCodigoGED(), ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL, keycloakUtil.getIpFromToken());

        if (retornoGED.getQuantidade() > 0) {
            DadosDocumentoLocalizadoDTO dadosDocumento = retornoGED.getDadosDocumentoLocalizados().stream().findFirst().get();

            SolicitacaoAvaliacaoSifrcVO solicitacaoAvaliacaoSifrcDTO = new SolicitacaoAvaliacaoSifrcVO();
            String cpfCnpjFormatado = StringUtils.leftPad(String.valueOf(cpfCnpj), TipoPessoaEnum.F.equals(tipoPessoaEnum) ? 11 : 14, '0');
            solicitacaoAvaliacaoSifrcDTO.setIdentificadorCliente(cpfCnpjFormatado);
            solicitacaoAvaliacaoSifrcDTO.setSifrcOperacaoEnum(novoCadastro ? SIFRCOperacaoEnum.NOVO : SIFRCOperacaoEnum.ATUALIZACAO);

            DocumentoVO documentoVO = new DocumentoVO();

            documentoVO.setClasse(dadosDocumento.getAtributos().getClasse());
            documentoVO.setMimeType(dadosDocumento.getAtributos().getMimeType());
            documentoVO.setNome(dadosDocumento.getAtributos().getNome());
            documentoVO.setTipo(dadosDocumento.getAtributos().getTipo());

            dadosDocumento.getAtributos().getCampos().forEach(a -> documentoVO.addCamposDTO(new CampoVO(a.getNome(), a.getValor(), a.getTipo())));

            String link = dadosDocumento.getLink();
            byte[] bytes = UtilWS.obterBytes(link);

            documentoVO.setBinario(Base64.getEncoder().encodeToString(bytes));

            solicitacaoAvaliacaoSifrcDTO.setDocumento(documentoVO);

            String mensagemEnvio = UtilXml.converterParaXml(solicitacaoAvaliacaoSifrcDTO);

            final String urlWebService = UtilParametro.getParametro("url.servico.avalia.documento.sifrc", true);
            LOGGER.info(MessageFormat.format("Chamando avaliação do SIFRC: url {0} passando o XML {1}", urlWebService, mensagemEnvio));
            Response response = UtilRest.consumirServicoOAuth2(urlWebService, EnumMetodoHTTP.POST, new HashMap<>(), new HashMap<>(), mensagemEnvio, this.keycloakService.getAccessTokenString(), "application/xml", "*");
            LOGGER.info(MessageFormat.format("Mensagem recebida: {0}", response.getEntity()));

            switch (response.getStatusInfo().getFamily()) {
                case SERVER_ERROR:
                    throw new SifrcException("Falha ao comunicar com o SIFRC. Serviço Indisponivel", Boolean.FALSE);
                case CLIENT_ERROR:
                    throw new SifrcException(MessageFormat.format("Falha ao comunicar com o SIFRC. Resposta recebida: {0}", response.getEntity()), Boolean.TRUE);
                default:
                    ControleDocumento controleDocumento = new ControleDocumento();
                    controleDocumento.setDocumento(documento);
                    controleDocumento.setIndicativoAvaliacaoCadastral(Boolean.TRUE);
                    controleDocumento.setDataHoraEnvio(Calendar.getInstance());
                    controleDocumento.setExecucaoCaixa(Boolean.TRUE);
                    RespostaAvaliacaoSifrcVO respostaSIFRC = UtilXml.converterParaObjeto(response.getEntity().toString(), RespostaAvaliacaoSifrcVO.class);
                    controleDocumento.setDataHoraRetornoAvaliacaoCadastral(Calendar.getInstance());
                    controleDocumento.setCodigoFornecedor(respostaSIFRC.getIdentificadorTransacao().toString());
                    controleDocumento.setValorRetornoAvaliacaoCadastral(respostaSIFRC.getRecomendacao().toString());

                    this.entityManager.persist(controleDocumento);

                    return SIFRCRecomendacaoEnum.SIM.equals(respostaSIFRC.getRecomendacao());
            }
        } else {
            throw new SiecmException(MessageFormat.format("Documento não localizado no GED sob ID: {0}", documento.getCodigoGED()), Boolean.TRUE);
        }
    }

    private boolean submeteDocumentoSICOD(Documento documento) {
        // Transforma todos os atributos definido no tipo de documento em um Map para facilitar a busca
        final Map<String, AtributoExtracao> mapaAtributosTipoDocumento = new HashMap<>();
        documento.getTipoDocumento().getAtributosExtracao().forEach(atributoExtracao -> {
            if (atributoExtracao.getNomeAtributoSICOD() != null) {
                mapaAtributosTipoDocumento.put(atributoExtracao.getNomeAtributoDocumento(), atributoExtracao);
            }
        });

        Calendar calendar = Calendar.getInstance();
        this.calendarUtil.removeHora(calendar);
        boolean documentoValido = (calendar.getTimeInMillis() - documento.getDataHoraValidade().getTimeInMillis()) > 0;

        Map<String, String> dadosSolicitacao = new HashMap<>();
        dadosSolicitacao.put(ATRIBUTO_SICOD_SITUACAO_CNH, documentoValido ? "válida" : "inválida");
        dadosSolicitacao.put(ATRIBUTO_SICOD_CPF_USUARIO_CAIXA, this.keycloakService.getCpf());
        dadosSolicitacao.put(ATRIBUTO_SICOD_MATRICULA_USUARIO_CAIXA, this.keycloakService.getMatricula());
        dadosSolicitacao.put(ATRIBUTO_SICOD_CGC_UNIDADE_CAIXA, String.valueOf(this.keycloakService.getLotacaoAdministrativa()));

        documento.getAtributosDocumento().forEach(atributo -> {
            AtributoExtracao atributoExtracao = mapaAtributosTipoDocumento.get(atributo.getDescricao());
            if (atributoExtracao != null) {
                String valor;
                try {
                    valor = String.valueOf(this.converteValorAtributo(atributoExtracao.getTipoAtributoSicodEnum(), atributo.getConteudo()));
                    dadosSolicitacao.put(atributoExtracao.getNomeAtributoSICOD(), valor);
                } catch (ParseException ex) {
                    String mensagem = MessageFormat.format("AFS.sDS.001 - Falha ao converter atributo para envio ao SICOD. Atributo com valor inválido. Atributo: {0} | Tipo Dado: {1} | Valor Atributo: {2}", atributo.getDescricao(), atributoExtracao.getTipoAtributoSicodEnum()
                                                                                                                                                                                                                                                        .name(), atributo.getConteudo());
                    throw new SimtrConfiguracaoException(mensagem, ex);
                }
            }
        });

        String json;
        try {
            json = UtilJson.converterParaJson(dadosSolicitacao);
        } catch (Exception ex) {
            String mensagem = MessageFormat.format("AFS.sDS.002 - Falha ao converter mapa de atributos para json de envio ao SICOD. Falha: {0}", ex.getLocalizedMessage());
            LOGGER.log(Level.ALL, mensagem);
            throw new SimtrConfiguracaoException(mensagem, ex);
        }

        final String urlWebService = UtilParametro.getParametro("url.servico.avalia.documento.sicod", true);
        Response response = UtilRest.consumirServicoNoAuthJSON(urlWebService, EnumMetodoHTTP.POST, new HashMap<>(), new HashMap<>(), json);
        if (!Response.Status.Family.SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
            boolean servicoDisponivel = Response.Status.Family.SERVER_ERROR.equals(response.getStatusInfo().getFamily());
            throw new SicodException("AFS.sDS.003 - Falha ao comunicar com o SICOD", servicoDisponivel);
        }

        return Boolean.TRUE;
    }

    private Object converteValorAtributo(TipoAtributoEnum tipoAtributoEnum, String valor) throws ParseException {
        switch (tipoAtributoEnum) {
            case BOOLEAN:
                return Boolean.valueOf(valor);
            case DATE:
                Calendar c = this.calendarUtil.toCalendar(valor, Boolean.TRUE);
                return this.calendarUtil.toString(c, "dd/MM/yyyy");
            case DECIMAL:
                return new BigDecimal(valor);
            case LONG:
                return Long.valueOf(valor);
            case STRING:
            default:
                return valor;
        }
    }
}
