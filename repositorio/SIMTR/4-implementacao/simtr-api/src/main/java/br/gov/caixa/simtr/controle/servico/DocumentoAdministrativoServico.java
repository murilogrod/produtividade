package br.gov.caixa.simtr.controle.servico;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.CampoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.DadosDocumentoLocalizadoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.RetornoPesquisaDTO;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.GEDService;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.pedesgo.arquitetura.util.UtilWS;
import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.controle.excecao.SimtrPermissaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.ApensoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.Conteudo;
import br.gov.caixa.simtr.modelo.entidade.ContratoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DocumentoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.ProcessoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.GEDTipoAtributoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.util.CalendarUtil;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.util.KeycloakUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRPAEMTZ,
    ConstantesUtil.PERFIL_MTRPAEOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class DocumentoAdministrativoServico extends AbstractService<DocumentoAdministrativo, Long> {


    private static final String SIECM_PASTA_PAE = "SIMTR";
    
    private static final Logger LOGGER = Logger.getLogger(DocumentoAdministrativoServico.class.getName());

    @EJB
    private ApensoAdministrativoServico apensoAdministrativoServico;

    @EJB
    private CalendarUtil calendarUtil;

    @EJB
    private CanalServico canalServico;

    @EJB
    private ContratoAdministrativoServico contratoAdministrativoServico;

    @EJB
    private DocumentoServico documentoServico;

    @EJB
    private GEDService gedService;

    @EJB
    private KeycloakService keycloakService;

    @EJB
    private ProcessoAdministrativoServico processoAdministrativoServico;
    
    @EJB
    private SiecmServico siecmServico;
    
    @EJB
    private TipoDocumentoServico tipoDocumentoServico;


    @Inject
    private EntityManager entityManager;

    @Inject
    private KeycloakUtil keycloakUtil;

    @Resource
    private SessionContext sessionContext;

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE
    })
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public DocumentoAdministrativo getDocumentoById(Long id, boolean vinculacoesConteudo, boolean vinculacoesProcesso, boolean vinculacoesContrato, boolean vinculacoesApenso) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT da FROM DocumentoAdministrativo da ");
        jpql.append(" LEFT JOIN FETCH da.documento d ");
        jpql.append(" LEFT JOIN FETCH d.tipoDocumento td ");
        jpql.append(" LEFT JOIN FETCH d.atributosDocumento ad ");
        jpql.append(" LEFT JOIN FETCH d.canalCaptura cc ");
        if (vinculacoesConteudo) {
            jpql.append(" LEFT JOIN FETCH d.conteudos dc ");
        }
        if (vinculacoesProcesso) {
            jpql.append(" LEFT JOIN FETCH da.processoAdministrativo pa ");
        }
        if (vinculacoesContrato) {
            jpql.append(" LEFT JOIN FETCH da.contratoAdministrativo ca ");
        }
        if (vinculacoesApenso) {
            jpql.append(" LEFT JOIN FETCH da.apensoAdministrativo aa ");
        }
        jpql.append(" WHERE da.id = :id AND da.dataHoraExclusao is null ");

        TypedQuery<DocumentoAdministrativo> query = this.entityManager.createQuery(jpql.toString(), DocumentoAdministrativo.class);
        query.setParameter("id", id);
        try {
            DocumentoAdministrativo documentoAdministrativo = query.getSingleResult();

            Boolean visualizarConfidencial = sessionContext.isCallerInRole(ConstantesUtil.PERFIL_MTRPAESIG);

            if (documentoAdministrativo.getConfidencial() && !visualizarConfidencial) {
                throw new SimtrPermissaoException("DAS.gDBI.001 - Usuário sem perfil para capturar registros de documentos confidenciais");
            }

            Documento documento = documentoAdministrativo.getDocumento();
            if (vinculacoesConteudo && documento.getCodigoGED() != null) {
                RetornoPesquisaDTO retornoGED = this.gedService.searchDocument(documento.getCodigoGED(), ConstantesUtil.SIECM_OS_PROCESSO_ADMINISTRATIVO, keycloakUtil.getIpFromToken());

                int ordem = 1;
                for (DadosDocumentoLocalizadoDTO dadosDocumento : retornoGED.getDadosDocumentoLocalizados()) {
                    String link = dadosDocumento.getLink();
                    byte[] bytes = UtilWS.obterBytes(link);
                    String conteudoBase64 = Base64.getEncoder().encodeToString(bytes);
                    Conteudo c = new Conteudo(conteudoBase64, ordem++);
                    documento.addConteudos(c);
                }
            }

            return documentoAdministrativo;
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE
    })
    public List<DocumentoAdministrativo> listByDescricao(String descricao) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT da FROM DocumentoAdministrativo da ");
        jpql.append(" LEFT JOIN FETCH da.documento dad ");
        jpql.append(" WHERE UPPER( da.descricao ) LIKE UPPER( CONCAT( '%', :descricao, '%' ) ) AND da.dataHoraExclusao is null ");

        TypedQuery<DocumentoAdministrativo> queryProcesso = this.entityManager.createQuery(jpql.toString(), DocumentoAdministrativo.class);
        queryProcesso.setParameter("descricao", descricao);
        try {
            return queryProcesso.getResultList();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return new ArrayList<>();
        }
    }

    public void insereDocumentoProcessoAdministrativo(Long idProcessoAdministrativo, Documento documento, Long idDocumentoSubstituido, String justificativaSubstituicao, boolean confidencial, boolean valido, String descricao) {
        ProcessoAdministrativo processoAdministrativo = this.processoAdministrativoServico.getById(idProcessoAdministrativo, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (processoAdministrativo == null) {
            String mensagem = MessageFormat.format("Processo Administrativo não localizado pelo identificador {0}", idProcessoAdministrativo);
            throw new SimtrRecursoDesconhecidoException(mensagem);

        }

        validarDocumento(documento);

        String numeroProcesso = StringUtils.leftPad(String.valueOf(processoAdministrativo.getNumeroProcesso()), 8, '0')
                                           .concat("/")
                                           .concat(processoAdministrativo.getAnoProcesso().toString());

        String subPasta = numeroProcesso.replaceAll("/", "_");
        
        boolean armazenaLocal = Boolean.getBoolean("simtr_imagens_local");
        if (!armazenaLocal) {
            List<String> conteudosBase64 = documento.getConteudos().stream().map(c -> c.getBase64()).collect(Collectors.toList());

            // Realiza a gravação do documento no GED
            String codigoSIECM = this.gravaDocumentoGED(numeroProcesso, confidencial, documento, conteudosBase64.get(0), SIECM_PASTA_PAE, subPasta, ConstantesUtil.SIECM_OS_PROCESSO_ADMINISTRATIVO);
            documento.setCodigoGED(codigoSIECM);
            documento.getConteudos().clear();
        }
        // Salva o registro do documento encaminhado
        this.entityManager.persist(documento);

        DocumentoAdministrativo documentoAdministrativo = new DocumentoAdministrativo();
        documentoAdministrativo.setConfidencial(confidencial);
        documentoAdministrativo.setValido(valido);
        documentoAdministrativo.setDescricao(descricao);
        documentoAdministrativo.setProcessoAdministrativo(processoAdministrativo);
        documentoAdministrativo.setContratoAdministrativo(null);
        documentoAdministrativo.setApensoAdministrativo(null);
        documentoAdministrativo.setDocumento(documento);

        this.save(documentoAdministrativo);

        if (idDocumentoSubstituido != null) {
            DocumentoAdministrativo documentoSubstituido = this.getById(idDocumentoSubstituido);
            if (documentoSubstituido.getDocumentoSubstituto() == null) {
                documentoSubstituido.setDocumentoSubstituto(documentoAdministrativo);
                documentoSubstituido.setJustificativaSubstituicao(justificativaSubstituicao);
                this.update(documentoSubstituido);
            }
        }

    }

    public void insereDocumentoContratoAdministrativo(Long idContratoAdministrativo, Documento documento, Long idDocumentoSubstituido, boolean confidencial, boolean valido, String descricao) {
        ContratoAdministrativo contratoAdministrativo = this.contratoAdministrativoServico.getById(idContratoAdministrativo, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
        if (contratoAdministrativo == null) {
            String mensagem = MessageFormat.format("Contrato Administrativo não localizado pelo identificador {0}", idContratoAdministrativo);
            throw new SimtrRecursoDesconhecidoException(mensagem);

        }

        validarDocumento(documento);

        List<String> conteudosBase64 = documento.getConteudos().stream().map(c -> c.getBase64()).collect(Collectors.toList());

        ProcessoAdministrativo processoAdministrativo = contratoAdministrativo.getProcessoAdministrativo();
        String numeroProcesso = StringUtils.leftPad(String.valueOf(processoAdministrativo.getNumeroProcesso()), 8, '0').concat("/")
                                           .concat(processoAdministrativo.getAnoProcesso().toString());

        String subPasta = numeroProcesso.replaceAll("/", "_");
        
        boolean armazenaLocal = Boolean.getBoolean("simtr_imagens_local");
        if (!armazenaLocal) {
            String codigoSIECM = this.gravaDocumentoGED(numeroProcesso, confidencial, documento, conteudosBase64.get(0), SIECM_PASTA_PAE, subPasta, ConstantesUtil.SIECM_OS_PROCESSO_ADMINISTRATIVO);
            documento.setCodigoGED(codigoSIECM);
            documento.getConteudos().clear();
        }
        // Salva o registro do documento encaminhado
        this.entityManager.persist(documento);

        DocumentoAdministrativo documentoAdministrativo = new DocumentoAdministrativo();
        documentoAdministrativo.setConfidencial(confidencial);
        documentoAdministrativo.setValido(valido);
        documentoAdministrativo.setDescricao(descricao);
        documentoAdministrativo.setProcessoAdministrativo(null);
        documentoAdministrativo.setContratoAdministrativo(contratoAdministrativo);
        documentoAdministrativo.setApensoAdministrativo(null);

        documentoAdministrativo.setDocumento(documento);

        this.save(documentoAdministrativo);

        if (idDocumentoSubstituido != null) {
            DocumentoAdministrativo documentoSubstituido = this.getById(idDocumentoSubstituido);
            if (documentoSubstituido.getDocumentoSubstituto() == null) {
                documentoSubstituido.setDocumentoSubstituto(documentoAdministrativo);
                this.update(documentoSubstituido);
            }
        }
    }

    public void insereDocumentoApensoAdministrativo(Long idApensoAdministrativo, Documento documento, boolean confidencial, boolean valido, String descricao, Long idDocumentoSubstituido, String justificativaSubstituicao) {
        ApensoAdministrativo apensoAdministrativo = this.apensoAdministrativoServico.getById(idApensoAdministrativo, Boolean.TRUE, Boolean.TRUE);
        if (apensoAdministrativo == null) {
            String mensagem = MessageFormat.format("Apenso Administrativo não localizado pelo identificador {0}", idApensoAdministrativo);
            throw new SimtrRecursoDesconhecidoException(mensagem);
        }

        validarDocumento(documento);

        List<String> conteudosBase64 = documento.getConteudos().stream().map(c -> c.getBase64()).collect(Collectors.toList());

        ProcessoAdministrativo processoAdministrativo = apensoAdministrativo.getProcessoAdministrativo();
        if (processoAdministrativo == null) {
            Long idContratoAdministrativo = apensoAdministrativo.getContratoAdministrativo().getId();
            ContratoAdministrativo contratoAdministrativo = this.contratoAdministrativoServico.getById(idContratoAdministrativo, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
            processoAdministrativo = contratoAdministrativo.getProcessoAdministrativo();
        }
        String numeroProcesso = StringUtils.leftPad(String.valueOf(processoAdministrativo.getNumeroProcesso()), 8, '0')
                                           .concat("/")
                                           .concat(processoAdministrativo.getAnoProcesso().toString());

        String subPasta = numeroProcesso.replaceAll("/", "_");
        
        boolean armazenaLocal = Boolean.getBoolean("simtr_imagens_local");
        if (!armazenaLocal) {
            // Realiza a gravação do documento no GED
            String codigoSIECM = this.gravaDocumentoGED(numeroProcesso, confidencial, documento, conteudosBase64.get(0), SIECM_PASTA_PAE, subPasta, ConstantesUtil.SIECM_OS_PROCESSO_ADMINISTRATIVO);
            documento.setCodigoGED(codigoSIECM);
            documento.getConteudos().clear();
        }

        // Salva o registro do documento encaminhado
        this.entityManager.persist(documento);

        DocumentoAdministrativo documentoAdministrativo = new DocumentoAdministrativo();
        documentoAdministrativo.setConfidencial(confidencial);
        documentoAdministrativo.setValido(valido);
        documentoAdministrativo.setDescricao(descricao);
        documentoAdministrativo.setProcessoAdministrativo(null);
        documentoAdministrativo.setContratoAdministrativo(null);
        documentoAdministrativo.setApensoAdministrativo(apensoAdministrativo);

        documentoAdministrativo.setDocumento(documento);

        this.save(documentoAdministrativo);

        if (idDocumentoSubstituido != null) {
            DocumentoAdministrativo documentoSubstituido = this.getById(idDocumentoSubstituido);
            if (documentoSubstituido.getDocumentoSubstituto() == null) {
                documentoSubstituido.setDocumentoSubstituto(documentoAdministrativo);
                documentoSubstituido.setJustificativaSubstituicao(justificativaSubstituicao);
                this.update(documentoSubstituido);
            }
        }
    }

    public void aplicaPatch(Long idDocumento, Boolean confidencial, Boolean valido, String descricao, String justificativaSubstituicao, OrigemDocumentoEnum origemDocumentoEnum, Integer codigoTipoDocumento, Map<String, String> atributosDocumento) {

        // Captura do canal de comunicação
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "AAS.dAP.001 - Canal de comunicação não localizado para codigo de integração fornecido.");

        DocumentoAdministrativo documentoAdministrativo = this.getById(idDocumento);
        validarDocumentoAdmnistrativo(idDocumento, documentoAdministrativo);

        if (documentoAdministrativo.getDocumentoSubstituto() != null) {
            String mensagem = MessageFormat.format(" AAS.dAP.003 -  Alteração não permitida, Documento Administrativo substituido pelo ID {0}", documentoAdministrativo.getDocumentoSubstituto()
                                                                                                                                                                       .getId());
            throw new SimtrEstadoImpeditivoException(mensagem);
        }

        if (documentoAdministrativo.getDataHoraExclusao() != null) {
            String mensagem = MessageFormat.format(" AAS.dAP.004 -  Alteração não permitida, Documento Administrativo excluido pelo usuário {0} em {1}", documentoAdministrativo.getMatriculaExclusao(), this.calendarUtil.toString(documentoAdministrativo.getDataHoraExclusao(), "dd/MM/yyyy HH:mm:ss"));
            throw new SimtrEstadoImpeditivoException(mensagem);
        }

        Documento documento = documentoAdministrativo.getDocumento();

        if (confidencial != null) {
            documentoAdministrativo.setConfidencial(confidencial);
        }

        if (valido != null) {
            documentoAdministrativo.setValido(valido);
        }

        if (descricao != null) {
            documentoAdministrativo.setDescricao(descricao);
        }

        if (justificativaSubstituicao != null) {
            documentoAdministrativo.setJustificativaSubstituicao(justificativaSubstituicao);
        }

        if (origemDocumentoEnum != null) {
            documento.setOrigemDocumento(origemDocumentoEnum);
        }

        if (codigoTipoDocumento != null) {
            TipoDocumento tipoDocumento = tipoDocumentoServico.getById(codigoTipoDocumento);
            if (tipoDocumento == null) {
                String mensagem = MessageFormat.format(" AAS.dAP.004 - Tipo de documento não encontrado. Codigo do Tipo Documento Encaminhado = {0}", codigoTipoDocumento);
                throw new SimtrRequisicaoException(mensagem);
            }
            documento.setTipoDocumento(tipoDocumento);
        }

        if (atributosDocumento != null) {
            atributosDocumento.entrySet().forEach(registro -> {
                for (AtributoDocumento ad : documento.getAtributosDocumento()) {
                    if (ad.getDescricao().equalsIgnoreCase(registro.getKey())) {
                        ad.setConteudo(registro.getValue());
                        return;
                    }
                }
                AtributoDocumento atributoDocumento = new AtributoDocumento();
                atributoDocumento.setDescricao(registro.getKey());
                atributoDocumento.setConteudo(registro.getValue());
                atributoDocumento.setAcertoManual(Boolean.TRUE);
                atributoDocumento.setDocumento(documento);
                documento.addAtributosDocumento(atributoDocumento);
            });

            this.documentoServico.validaAtributosDocumento(documento);
        }

        this.entityManager.merge(documento);

        this.update(documentoAdministrativo);
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRPAEMTZ
    })
    public void deleteDocumentoAdministrativo(Long idDocumento, String justificativa) {

        if (justificativa == null || justificativa.replaceAll(" ", "").length() < 5) {
            throw new SimtrRequisicaoException("AAS.cDDA.001 - Justificativa deve ter pelo menos 5 caractes.");
        }

        DocumentoAdministrativo documentoAdministrativo = this.getById(idDocumento);
        if (documentoAdministrativo == null) {
            String mensagem = MessageFormat.format("AAS.cDDA.002 -  Documento Administrativo não localizado pelo ID informado. ID = {0}", idDocumento);
            throw new SimtrRecursoDesconhecidoException(mensagem);

        }

        documentoAdministrativo.setValido(false);
        documentoAdministrativo.setMatriculaExclusao(this.keycloakService.getMatricula());
        documentoAdministrativo.setJustificativaExclusao(justificativa);
        documentoAdministrativo.setDataHoraExclusao(Calendar.getInstance());

        // Não excluir fisicamente, apena inabilita o registro.
        this.update(documentoAdministrativo);
        removeDocumentoAdministrativoRecursivamente(documentoAdministrativo);

    }

    public boolean validaMimetypeConteudo(FormatoConteudoEnum formatoConteudoEnum, String base64) {
        boolean validado = false;
        try {
            switch (formatoConteudoEnum) {
                case PDF:
                    PDDocument document = PDDocument.load(new ByteArrayInputStream(com.itextpdf.text.pdf.codec.Base64.decode(base64)));
                    document.close();
                    validado = true;
                    break;

                default:
                    break;
            }

        } catch (IOException ex) {
            throw new SimtrRequisicaoException("DAS.vMC.001 - Falha ao validar Mimetype.", ex);
        }
        return validado;
    }

    // *********** Métodos Privados ***********
    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Realiza a gravação de um documento administrativo junto ao GED
     *
     * @param numeroProcessoAdministrativo Numero do processo adminitrativo que o documento esta vinculado.
     * @param confidencial Atributo utilizado para indicar a classificação de sigilo que será definido no atributo da clase GED
     * @param documento Documento a ser gravado. Necessario possuir os atributos definidos e o Tipo de Documento preenchido com os atributos de extração
     * @param binario Conteudo que compoe o documento.
     * @param pasta Identifica a pasta do GED em que o documento deverá ser armazenado
     * @param subpasta Identifica a subpasta do GED em que o documento deverá ser armazenado
     * @param objectStore Identifica objectstore em que o documento deverá ser armazenado
     * @return Resposta informada pela solução do SIECM com o resultado da operação
     */
    private String gravaDocumentoGED(String numeroProcessoAdministrativo, boolean confidencial, Documento documento, String binario, String pasta, String subPasta, String objectStore) {

        /**
         * Não esta sendo enviada a subpasta, pois não tem método para viabilizar a criação de subpasta encadeada em fluxo distinto da pasta de transações que não funciona com o OS_PADM
         */
        
        CampoDTO campoNumeroProcesso = new CampoDTO("NUMERO_PROCESSO", numeroProcessoAdministrativo, GEDTipoAtributoEnum.STRING.name(), Boolean.TRUE);
        CampoDTO campoClassificaoSigilo = new CampoDTO("CLASSIFICACAO_SIGILO", confidencial ? "CONFIDENCIAL" : "PUBLICO", GEDTipoAtributoEnum.STRING.name(), Boolean.TRUE);
        return this.siecmServico.armazenaDocumentoOperacaoSIECM(documento, TemporalidadeDocumentoEnum.VALIDO, binario, objectStore, pasta, null, Arrays.asList(campoNumeroProcesso, campoClassificaoSigilo));        
    }

    private void removeDocumentoAdministrativoRecursivamente(DocumentoAdministrativo docAdm) {
        if (docAdm.getDocumentoSubstituto() != null) {
            DocumentoAdministrativo docAdmSubstituto = this.getById(docAdm.getDocumentoSubstituto().getId());
            if (docAdmSubstituto != null) {
                docAdmSubstituto.setValido(docAdm.getValido());
                docAdmSubstituto.setMatriculaExclusao(docAdm.getMatriculaExclusao());
                docAdmSubstituto.setJustificativaExclusao(docAdm.getJustificativaExclusao());
                docAdmSubstituto.setDataHoraExclusao(docAdm.getDataHoraExclusao());
                // Não excluir fisicamente, apenas inabilita o registro.
                this.update(docAdmSubstituto);
                this.removeDocumentoAdministrativoRecursivamente(docAdmSubstituto);
            }
        }
    }

    private void validarDocumento(Documento documento) {
        if (documento == null) {
            SimtrRequisicaoException requisicaoException = new SimtrRequisicaoException(ConstantesUtil.MSG_DOCUMENTO_ENCAMINHADO_NULO);
            throw requisicaoException;
        }
        if (documento.getTipoDocumento() == null) {
            SimtrRequisicaoException requisicaoException = new SimtrRequisicaoException(ConstantesUtil.MSG_DOCUMENTO_NAO_CONTEM_TIPO);
            throw requisicaoException;
        }
        if (documento.getAtributosDocumento() == null) {
            SimtrRequisicaoException requisicaoException = new SimtrRequisicaoException(ConstantesUtil.MSG_DOCUMENTO_NAO_CONTEM_ATRIBUTOS);
            throw requisicaoException;
        }
        if (documento.getCanalCaptura() == null) {
            SimtrRequisicaoException requisicaoException = new SimtrRequisicaoException(ConstantesUtil.MSG_DOCUMENTO_SEM_CANAL);
            throw requisicaoException;
        }
    }

    private void validarDocumentoAdmnistrativo(Long idDocumento, DocumentoAdministrativo documentoAdministrativo) {
        if (documentoAdministrativo == null) {
            String mensagem = MessageFormat.format(" AAS.dAP.002 -  Documento Administrativo não localizado pelo identificador {0}", idDocumento);
            throw new SimtrRecursoDesconhecidoException(mensagem);
        }
    }
}
