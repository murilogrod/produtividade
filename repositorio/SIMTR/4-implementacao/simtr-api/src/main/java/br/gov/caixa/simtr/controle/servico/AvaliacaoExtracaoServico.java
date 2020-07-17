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

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.pedesgo.arquitetura.util.UtilParametro;
import br.gov.caixa.pedesgo.arquitetura.util.UtilRest;
import br.gov.caixa.simtr.controle.excecao.OutsourcingException;
import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.controle.excecao.SimtrPermissaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.vo.extracaodados.AtributoDocumentoVO;
import br.gov.caixa.simtr.controle.vo.extracaodados.RespostaAvaliacaoExtracaoVO;
import br.gov.caixa.simtr.controle.vo.extracaodados.ResultadoAvaliacaoExtracaoVO;
import br.gov.caixa.simtr.controle.vo.extracaodados.SolicitacaoAvaliacaoExtracaoVO;
import br.gov.caixa.simtr.controle.vo.outsourcing.SubmissaoDocumentoVO;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.Conteudo;
import br.gov.caixa.simtr.modelo.entidade.ControleDocumento;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.JanelaTemporalExtracaoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.outsourcing.ResultadoAvaliacaoExtracaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.outsourcing.SolicitacaoAvaliacaoExtracaoDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.util.KeycloakUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRSDNEXT,
    ConstantesUtil.PERFIL_MTRSDNINT
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class AvaliacaoExtracaoServico {

    @EJB
    private CanalServico canalServico;

    @EJB
    private DocumentoServico documentoServico;
    
    @EJB
    private DossieClienteServico dossieClienteServico;

    @EJB
    private KeycloakUtil keycloakUtil;
    
    @EJB
    private SiecmServico siecmServico;

    @EJB
    private TipoDocumentoServico tipoDocumentoServico;

    @Inject
    private EntityManager entityManager;

    @Resource
    private SessionContext sessionContext;

    private static final String DOCUMENTO_TRATADO = "DOCUMENTO_TRATADO";
    private static final String PROCESSO_INTERNO = "INTERNO";
    private static final String SERVICO_SUBMISSAO_DOCUMENTO = "/v1/analise-documento";
    private static final String SERVICO_ATUALIZACAO_TOKEN = "/v1/updateToken";
    private static final String SERVICO_VERIFICACAO_COMUNICACAO = "/ping";
    
    private static final Logger LOGGER = Logger.getLogger(AvaliacaoExtracaoServico.class.getName());

    /**
     * Obtem a lista de documentos pendentes de extração de dados
     *
     * @param incluiTipologiaServicoExterno Indica se a captura deve incluir a tipoogia definida para envio ao fornecedor externo
     * @return Mapa contendo o tipo de documento como cahve e a lista de documentos pendentes de extração como valor
     */
    public Map<TipoDocumento, List<Documento>> listaDocumentosPendentesExtracao(boolean incluiTipologiaServicoExterno) {

        // Captura e valida o canal de comunicação
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "AES.lDPE.001 - ".concat(ConstantesUtil.MSG_CANAL_NAO_LOCALIZADO_SSO));

        // Valida se o usuário possui perfil para extração de dados
        this.validaPerfilUsuario();

        // Remove o registro de controle para qualquer documento que possua registro em aberto a 10 minutos ou mais
        this.removeRegistroControleCAIXA(null, 10);

        // Cria o objeto que representa a lista de documentos com pendencia de extração
        Map<TipoDocumento, List<Documento>> mapaRetorno = new HashMap<>();

        // Monta a query de captura da lista de pendências de extração para documentos com tipologia definida com as seguintes premissas:
        // 1 - Tipo de documento deve possuir definidos atributos extração ativos, com parametrização de nomeAtributoRetorno e indicação de estar presente no documento
        // 2 - Não pode existir registro de controle em aberto, ou seja, sem data de Rejeição definida ou sem dataHoraRetornoExtracao
        // 3 - Documento não deve possui atributos extraidos
        StringBuilder jpqlComTipo = new StringBuilder();
        jpqlComTipo.append(" SELECT DISTINCT d FROM Documento d ");
        jpqlComTipo.append(" LEFT JOIN FETCH d.tipoDocumento td ");
        jpqlComTipo.append(" LEFT JOIN FETCH d.controlesDocumento cd ");
        jpqlComTipo.append(" LEFT JOIN td.atributosExtracao ae ");
        jpqlComTipo.append(" LEFT JOIN d.atributosDocumento ad ");
        jpqlComTipo.append(" WHERE td.usoApoioNegocio = true ");
        jpqlComTipo.append(" AND EXISTS (SELECT a FROM AtributoExtracao a WHERE ativo = TRUE AND nomeAtributoRetorno IS NOT NULL AND presenteDocumento = TRUE AND tipoDocumento = td) ");
        jpqlComTipo.append(" AND NOT EXISTS ( ");
        jpqlComTipo.append("   SELECT cd FROM ControleDocumento WHERE documento = d AND dataHoraEnvio IS NOT NULL ");
        jpqlComTipo.append("   AND ( ");
        jpqlComTipo.append("      (dataHoraRetornoRejeicao IS NOT NULL) OR ");
        jpqlComTipo.append("      (indicativoExtracao = true AND dataHoraRetornoExtracao IS NULL) ");
        jpqlComTipo.append("   ) ");
        jpqlComTipo.append(" ) ");
        jpqlComTipo.append(" AND (");
        jpqlComTipo.append("    (d.atributosDocumento IS EMPTY) ");
        jpqlComTipo.append("    OR (NOT EXISTS(SELECT DISTINCT a FROM AtributoDocumento a WHERE a.documento = d AND a.acertoManual = TRUE))");
        jpqlComTipo.append(" ))");
        if (!incluiTipologiaServicoExterno) {
            jpqlComTipo.append(" AND td.enviaExtracaoExterna IS FALSE ");
        }

        TypedQuery<Documento> queryDocumentoComTipo = this.entityManager.createQuery(jpqlComTipo.toString(), Documento.class);
        List<Documento> documentosElegiveisComTipo = queryDocumentoComTipo.getResultList();

        // Mapeia os documentos elegiveis com tipologia definida no mapa de retorno
        documentosElegiveisComTipo.forEach(doc -> {
            List<Documento> documentosMapeados = mapaRetorno.getOrDefault(doc.getTipoDocumento(), new ArrayList<>());
            documentosMapeados.add(doc);
            mapaRetorno.put(doc.getTipoDocumento(), documentosMapeados);
        });

        // Monta a query de captura da lista de pendências de extração para documentos sem tipologia definida com as seguintes premissas:
        // 1 - Não pode existir registro de controle em aberto, ou seja, sem data de Rejeição definida ou sem dataHoraRetornoExtracao
        // 2 - Documento não deve possui atributos extraidos
        StringBuilder jpqlSemTipo = new StringBuilder();
        jpqlSemTipo.append(" SELECT DISTINCT d FROM Documento d ");
        jpqlSemTipo.append(" LEFT JOIN FETCH d.tipoDocumento td ");
        jpqlSemTipo.append(" LEFT JOIN FETCH d.controlesDocumento cd ");
        jpqlSemTipo.append(" LEFT JOIN d.atributosDocumento ad ");
        jpqlSemTipo.append(" WHERE d.tipoDocumento IS NULL ");
        jpqlSemTipo.append(" AND NOT EXISTS ( ");
        jpqlSemTipo.append("   SELECT cd FROM ControleDocumento WHERE documento = d AND dataHoraEnvio IS NOT NULL ");
        jpqlSemTipo.append("   AND ( ");
        jpqlSemTipo.append("      (dataHoraRetornoRejeicao IS NOT NULL) OR ");
        jpqlSemTipo.append("      (indicativoExtracao = true AND dataHoraRetornoExtracao IS NULL) ");
        jpqlSemTipo.append("   ) ");
        jpqlSemTipo.append(" ) ");
        jpqlSemTipo.append(" AND d.atributosDocumento IS EMPTY ");

        TypedQuery<Documento> queryDocumentoSemTipo = this.entityManager.createQuery(jpqlSemTipo.toString(), Documento.class);
        List<Documento> documentosElegiveisSemTipo = queryDocumentoSemTipo.getResultList();

        // Cria um tipo de documento "fake" para associação dos documentos sem
        // classificação de tipologia definida
        TipoDocumento tipoDocumentoClassificar = new TipoDocumento();
        tipoDocumentoClassificar.setId(0);
        tipoDocumentoClassificar.setNome("A CLASSIFICAR");

        // Mapeia os documentos elegiveis com tipologia definida no mapa de retorno
        documentosElegiveisSemTipo.forEach(doc -> {
            List<Documento> documentosMapeados = mapaRetorno.getOrDefault(tipoDocumentoClassificar, new ArrayList<>());
            documentosMapeados.add(doc);
            mapaRetorno.put(tipoDocumentoClassificar, documentosMapeados);
        });

        return mapaRetorno;
    }

    /**
     * Captura o documento mais antigo que encontra-se com pendência de extração baseado no tipo de documento informado, caso não seja informado um tipo de documento será capturado um documento "a
     * classificar", ou seja, sem tipologia definida, obedecendo a ordem de precedência pela data de captura.
     *
     * @param identificadorTipoDocumento Identificador do tipo de documento associado ao documnento a ser capturado
     * @return Documento localizado para captura ou nulo caso o tipo indicado não possua documentos disponiveis
     */
    public Documento capturaDocumentoExtracaoByTipoDocumento(Integer identificadorTipoDocumento) {
        return this.capturaDocumentoExtracao(identificadorTipoDocumento, null);
    }

    /**
     * Captura o documento com base no identificador encaminhado, desde que o referido documento encontra-se com pendência de extração de dados.
     *
     * @param identificadorDocumento Identificador do documento que deseja ser capturado para extração de dados
     * @return Documento localizado para captura ou nulo caso o tipo indicado não possua documentos disponiveis
     */
    public Documento capturaDocumentoExtracaoByIdDocumento(Long identificadorDocumento) {
        return this.capturaDocumentoExtracao(null, identificadorDocumento);
    }

    /**
     * Atualiza as informações do registro de documento e controle com base nas informações retornadas do serviço de extração de dados
     *
     * @param codigoControle Código associado ao documento que identifica o registro de controle do fluxo de manipulação da informação
     * @param resultadoAvaliacaoExtracaoVO Objeto que representa o resultado da execução do serviço de extração de dados e avaliação de autenticidade
     * @return Retorna o documento atualizado com as informações encaminhadas
     */
    public Documento atualizaInformacaoDocumentoOutsourcing(String codigoControle, ResultadoAvaliacaoExtracaoVO resultadoAvaliacaoExtracaoVO) {

        // Captura e valida o canal de comunicação
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "AES.aID.001 - ".concat(ConstantesUtil.MSG_CANAL_NAO_LOCALIZADO_SSO));

        //Valida se o canal possui permissão de execução da atividade de atualização de informações do documento
        if (!canal.getIndicadorAtualizacaoDocumento()) {
            throw new SimtrPermissaoException("AES.aID.002 - Este canal de comunicação não possui permissão para atualizar informações de documentos. Necessário contactar a equipe do SIMTR.");
        }

        // Valida se o usuário possui perfil para extração de dados
        this.validaPerfilUsuario();

        // Caso o canal de comunicação seja a interface WEB do SIMTR, atribui ação executada por equipe CAIXA
        boolean equipeCaixa = canal.getClienteSSO().equals(ConstantesUtil.CLIENT_ID_SIMTR_WEB);
        
        return this.atualizaInformacaoDocumento(codigoControle, equipeCaixa, resultadoAvaliacaoExtracaoVO);
    }

    /**
     * Executa a remoção do registro de controle identificado pelo código informado.
     *
     * @param codigoControle Código de controle a ser removido
     * @throws SimtrRequisicaoException Caso não seja indicado nenhum registro remvido
     */
    public void removeRegistroControle(String codigoControle) {
        // Captura e valida o canal de comunicação
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "AES.rRC.001 - ".concat(ConstantesUtil.MSG_CANAL_NAO_LOCALIZADO_SSO));

        // Valida se o usuário possui perfil para extração de dados
        this.validaPerfilUsuario();

        // Remove o registro de controle para qualquer identificado pelo código de controle independente do tempo em operação
        int qtde = this.removeRegistroControleCAIXA(codigoControle, 0);

        if (qtde == 0) {
            String mensagem = MessageFormat.format("AES.rRC002 - Não foi identificado registro de controle em situação ativa para viabilizar remoção. Código informado: {0}", codigoControle);
            throw new SimtrRequisicaoException(mensagem);
        }
    }

    /**
     * Cria um registro de documento no SIMTR e conforme definição de tipologia, submete o documento ao fornecedor para execução do serviço de classificação, extração de dados e avaliação de
     * autenticidade documental.
     *
     * @param cpfCnpj CPF/CNPJ do cliente caso o documento seja vinculado a um dossiê de cliente
     * @param tipoPessoaEnum Identificador do tipo de pessoa a ser vinculado o documento se fisica o juridica para viabilizar a identificação do dossiê do cliente
     * @param solicitacaoAvaliacaoExtracaoDTO Objeto que representa os dados da solicitação do serviço indicando os parametros utilizados na solicitação do serviço junto ao fornecedor
     * @param processo Informação utilizada para identificar possivel atuação diferenciada na execução das atividades. Valores repassados na solicitação e devidamente acordados sob documento ANO
     * @return Documento do SIMTR armazenado com as indicações informações encaminhadas na solicitação e com adição das informações de data/hora da submissão ao fornecedor, identificação do usuário
     *         vinculado ao token, etc
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public Documento submeteDocumentoAvaliacaoExtracao(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, SolicitacaoAvaliacaoExtracaoDTO solicitacaoAvaliacaoExtracaoDTO, String processo) {

        // Captura do canal de comunicação
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "AES.sDAE.001 - ".concat(ConstantesUtil.MSG_CANAL_NAO_LOCALIZADO_SSO));

        // Verifica se a origem do documento foi informada
        if (solicitacaoAvaliacaoExtracaoDTO.getOrigemDocumentoEnum() == null) {
            throw new SimtrRequisicaoException("AES.sDAE.002 - ".concat(ConstantesUtil.MSG_DOCUMENTO_ORIGEM_INVALIDA));
        }

        // Valida se o canal solicitante possui permissão para solicitar o serviço de extração
        if (solicitacaoAvaliacaoExtracaoDTO.isExecutaExtracao()) {
            if (solicitacaoAvaliacaoExtracaoDTO.getJanelaTemporalExtracaoEnum() == null) {
                throw new SimtrRequisicaoException("AES.sDAE.003 - ".concat(ConstantesUtil.MSG_JANELA_EXTRACAO_NAO_DEFINIDA));
            }
            if (((solicitacaoAvaliacaoExtracaoDTO.getJanelaTemporalExtracaoEnum().equals(JanelaTemporalExtracaoEnum.M0)) && (!canal.getJanelaExtracaoM0()))
                || ((solicitacaoAvaliacaoExtracaoDTO.getJanelaTemporalExtracaoEnum().equals(JanelaTemporalExtracaoEnum.M30)) && (!canal.getJanelaExtracaoM30()))
                || ((solicitacaoAvaliacaoExtracaoDTO.getJanelaTemporalExtracaoEnum().equals(JanelaTemporalExtracaoEnum.M60)) && (!canal.getJanelaExtracaoM60()))) {
                throw new SimtrRequisicaoException("AES.sDAE.004 - ".concat(ConstantesUtil.MSG_CANAL_JANELA_EXTRACAO_NAO_AUTORIZADO));
            }
        }

        // Valida se o canal solicitante possui permissão para solicitar o serviço de avaliação de autenticidade
        if (solicitacaoAvaliacaoExtracaoDTO.isExecutaAutenticidade() && !canal.getIndicadorAvaliacaoAutenticidade()) {
            throw new SimtrRequisicaoException("AES.sDAE.005 - ".concat(ConstantesUtil.MSG_CANAL_SERVICO_AUTENTICIDADE_NAO_AUTORIZADO));
        }

        // Valida se o tipo de documento, caso encaminhado, trata-se de um tipo valido
        TipoDocumento tipoDocumento = null;
        if (solicitacaoAvaliacaoExtracaoDTO.getTipoDocumento() != null) {
            tipoDocumento = this.tipoDocumentoServico.getByTipologia(solicitacaoAvaliacaoExtracaoDTO.getTipoDocumento());
            if (tipoDocumento == null) {
                throw new SimtrRequisicaoException("AES.sDAE.006 - ".concat(ConstantesUtil.MSG_TIPO_DOCUMENTO_NAO_LOCALIZADO));
            }
        }

        // Valida se o conteudo foi enviado um conteudo e se trata-se de um base64 valido
        if ((solicitacaoAvaliacaoExtracaoDTO.getBinario() == null) || (!solicitacaoAvaliacaoExtracaoDTO.getBinario().matches(ConstantesUtil.REGEX_BASE64))) {
            throw new SimtrRequisicaoException("AES.sDAE.007 - ".concat(ConstantesUtil.MSG_CONTEUDO_ENCAMINHADO_INVALIDO));
        }

        // Cria o documento a ser armazenado no SIMTR
        String binario = new String();
        Documento documento = this.documentoServico.prototype(canal, Boolean.FALSE, tipoDocumento, TemporalidadeDocumentoEnum.VALIDO, solicitacaoAvaliacaoExtracaoDTO.getOrigemDocumentoEnum(), FormatoConteudoEnum.getByMimeType(solicitacaoAvaliacaoExtracaoDTO.getMimetype()), new ArrayList<>(), binario);

        //Caso seja enviado CPF/CNPJ vincula o documento ao dossiê de cliente na solicitação
        if (cpfCnpj != null) {
            // Captura o dossiê de cliente para vinculação com o documento
            DossieCliente dossieCliente = this.dossieClienteServico.getByCpfCnpj(cpfCnpj, tipoPessoaEnum, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
            if(dossieCliente == null) {
                String mensagem = "AES.sDAE.008 - Dossiê de Cliente não localizado sob CPF informado";
                if(TipoPessoaEnum.J.equals(tipoPessoaEnum)){
                    mensagem = "AES.sDAE.008 - Dossiê de Cliente não localizado sob CNPJ informado";
                }
                throw new SimtrRecursoDesconhecidoException(mensagem);
            }
            dossieCliente.addDocumentos(documento);
            documento.addDossiesCliente(dossieCliente);
            
            TipoDocumento tipoDocumentoSelfie = this.tipoDocumentoServico.getByTipologia(ConstantesUtil.TIPOLOGIA_SELFIE);
            if(solicitacaoAvaliacaoExtracaoDTO.getSelfies() != null){
            solicitacaoAvaliacaoExtracaoDTO.getSelfies().forEach(selfie -> {
                Documento documentoSelfie = this.documentoServico.prototype(canal, Boolean.FALSE, tipoDocumentoSelfie, TemporalidadeDocumentoEnum.VALIDO, OrigemDocumentoEnum.O, FormatoConteudoEnum.JPEG, new ArrayList<>(), selfie);
                documentoSelfie.addDossiesCliente(dossieCliente);
                dossieCliente.addDocumentos(documentoSelfie);
            });
            }
        }
        
        // Se o execução estiver definida para execução como processo interno, não envia
        if (!PROCESSO_INTERNO.equals(processo)) {
            boolean indicadorClassificacao = tipoDocumento == null ? Boolean.TRUE : Boolean.FALSE;
            ControleDocumento controleDocumento = this.associaRegistroControleDocumento(documento, indicadorClassificacao, solicitacaoAvaliacaoExtracaoDTO.isExecutaExtracao(), solicitacaoAvaliacaoExtracaoDTO.isExecutaAutenticidade(), solicitacaoAvaliacaoExtracaoDTO.getJanelaTemporalExtracaoEnum(), Boolean.FALSE);
            controleDocumento.setNomeProcesso(processo);

            SubmissaoDocumentoVO submissaoDocumentoVO = new SubmissaoDocumentoVO(solicitacaoAvaliacaoExtracaoDTO);
            submissaoDocumentoVO.setControleDocumento(controleDocumento);
            String codigoControle = this.submeteDocumentoOutsourcing(submissaoDocumentoVO);
            controleDocumento.setCodigoFornecedor(codigoControle);
        }

        // Salva o registro do documento e o registro de controle em cascata.
        this.documentoServico.save(documento);

        return documento;
    }

    /**
     * Localiza o registro de documento no SIMTR baseado no eu identificador e conforme definição de tipologia, submete o documento ao fornecedor para execução do serviço de extração de dados e
     * avaliação de autenticidade documental.
     *
     * @param id Identificador do documento a ser submetido ao serviço de outsourcing
     * @param processo Informação utilizada para identificar possivel atuação diferenciada na execução das atividades. Valores repassados na solicitação e devidamente acordados sob documento ANO
     * @return Documento do SIMTR armazenado atualizado com a informação do controle de documento que indica a informação de data/hora da submissão ao fornecedor, processo, etc
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public Documento submeteDocumentoAvaliacaoExtracao(Long id, String processo) {

        // Captura do canal de comunicação
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "AES.sDAE.101 - ".concat(ConstantesUtil.MSG_CANAL_NAO_LOCALIZADO_SSO));

        Documento documento = this.documentoServico.getById(id, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);

        String binario = documento.getConteudos().stream().map(c -> c.getBase64()).findFirst()
                                  .orElseThrow(() -> new SimtrEstadoImpeditivoException("AES.sDAE.102 - Documento sem binario associado para execução das atividades de outsourcing documental"));

        // Valida se o canal solicitante possui permissão para solicitar o serviço de outsourcing documental
        if ((!canal.getJanelaExtracaoM0())
            && (!canal.getJanelaExtracaoM30())
            && (!canal.getJanelaExtracaoM60())
            && (!canal.getIndicadorAvaliacaoAutenticidade())) {
            throw new SimtrRequisicaoException("AES.sDAE.103 - Canal de comunicação não autorizado a solicitar o serviço de outsourcing documental");
        }

        // Valida se o tipo de documento, caso encaminhado, trata-se de um tipo valido
        TipoDocumento tipoDocumento = documento.getTipoDocumento();
        if (!tipoDocumento.getEnviaExtracaoExterna() && !tipoDocumento.getEnviaAvaliacaoDocumental()) {
            throw new SimtrRequisicaoException("AES.sDAE.104 - Tipo de documento vinculado ao documento não possui permissão para envio ao serviço de outsourcing documental.");
        }

        boolean registroPendente = documento.getControlesDocumento().stream()
                                            .filter(cd -> cd.getDataHoraRetornoRejeicao() == null)
                                            .filter(cd -> (cd.getIndicativoClassificacao() && cd.getDataHoraRetornoClassificacao() == null)
                                                          || (cd.getIndicativoExtracao() && cd.getDataHoraRetornoExtracao() == null)
                                                          || (cd.getIndicativoAvaliacaoAutenticidade() && cd.getDataHoraRetornoAvaliacaoAutenticidade() == null)
                                                          || (cd.getIndicativoAvaliacaoCadastral() && cd.getDataHoraRetornoAvaliacaoCadastral() == null))
                                            .findAny().isPresent();

        if (registroPendente) {
            throw new SimtrEstadoImpeditivoException("AES.sDAES.105 - O registro esta pendente de atendimento e não pode ser solicitado novamente.");
        }

        // Monta do registro de controle de documento a ser vinculado ao documento
        boolean executaExtracao = (canal.getJanelaExtracaoM0()) || (canal.getJanelaExtracaoM30()) || (canal.getJanelaExtracaoM60());
        JanelaTemporalExtracaoEnum janelaTemporalExtracaoEnum = tipoDocumento.getPermiteExtracaoM0() ? JanelaTemporalExtracaoEnum.M0 : JanelaTemporalExtracaoEnum.M30;
        boolean executaAutenticidade = canal.getIndicadorAvaliacaoAutenticidade();
        ControleDocumento controleDocumento = this.associaRegistroControleDocumento(documento, Boolean.FALSE, executaExtracao, executaAutenticidade, janelaTemporalExtracaoEnum, Boolean.FALSE);
        controleDocumento.setNomeProcesso(processo);

        // Submete o documento ao serviço de extração de dados
        SubmissaoDocumentoVO submissaoDocumentoVO = new SubmissaoDocumentoVO(tipoDocumento.getCodigoTipologia(), processo, executaExtracao, janelaTemporalExtracaoEnum, executaAutenticidade, binario, controleDocumento);
        String codigoControle = this.submeteDocumentoOutsourcing(submissaoDocumentoVO);
        controleDocumento.setCodigoFornecedor(codigoControle);

        // Salva o registro de controle do documento
        this.entityManager.persist(controleDocumento);

        return documento;
    }

    /**
     * Submete documento a empresa de outsourcing para realizar atividades e acordo com as parametrizações da solicitação
     * 
     * @param submissaoDocumentoVO Objeto que representa as parametrizações necessárias a realizar a solicitação
     * @return Retorna o codigo de controle fornecido pela empresa de outsourcing
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public String submeteDocumentoOutsourcing(final SubmissaoDocumentoVO submissaoDocumentoVO) {

        SolicitacaoAvaliacaoExtracaoVO solicitacaoAvaliacaoExtracaoVO = new SolicitacaoAvaliacaoExtracaoVO();
        solicitacaoAvaliacaoExtracaoVO.setTipoDocumento(submissaoDocumentoVO.getTipoDocumento());
        solicitacaoAvaliacaoExtracaoVO.setExecutaExtracao(submissaoDocumentoVO.isExecutaExtracao());
        solicitacaoAvaliacaoExtracaoVO.setJanelaTemporalExtracaoEnum(submissaoDocumentoVO.getJanelaTemporalExtracaoEnum());
        solicitacaoAvaliacaoExtracaoVO.setExecutaAutenticidade(submissaoDocumentoVO.isExecutaAutenticidade());
        if (submissaoDocumentoVO.getDados() != null) {
            solicitacaoAvaliacaoExtracaoVO.setAtributos(submissaoDocumentoVO.getDados().stream().map(db -> new AtributoDocumentoVO(db.getChave(), db.getValor())).collect(Collectors.toList()));
        }
        if (submissaoDocumentoVO.getBinarios() != null) {
            solicitacaoAvaliacaoExtracaoVO.getBinarios().addAll(submissaoDocumentoVO.getBinarios());
        }
        if (submissaoDocumentoVO.getSelfies() != null) {
            solicitacaoAvaliacaoExtracaoVO.getSelfies().addAll(submissaoDocumentoVO.getSelfies());
        }

        final String mensagemEnvio;
        try {
            mensagemEnvio = UtilJson.converterParaJson(solicitacaoAvaliacaoExtracaoVO);
        } catch (Exception ex) {
            throw new OutsourcingException("AES.sDAE.001 - Falha ao converter objeto para envio de solicitação ao fornecedor de serviço de extração de dados e avaliação de autenticidade.", ex, Boolean.FALSE);
        }
        final String urlWebService = UtilParametro.getParametro("url.servico.extracao.autenticidade", true);
        LOGGER.fine(MessageFormat.format("Chamando serviço de Extração de Dados: url {0} passando o body {1}", urlWebService, mensagemEnvio));

        // Obtem o token de comunicação a ser utilizado para autenticar no serviço do fornecedor
        final String token = UtilParametro.getParametro("token.servico.extracao.autenticidade", true);
        final Map<String, String> cabecalhos = new HashMap<>();
        cabecalhos.put(ConstantesUtil.HEADER_AUTHORIZATION, token);

        // Monta o end point que deve ser chamado para submissão do documento
        String endpoint = urlWebService.concat(SERVICO_SUBMISSAO_DOCUMENTO);

        // Dispara a requisição para o serviço de outsourcing documental
        Response response = UtilRest.consumirServicoNoAuthJSON(endpoint, EnumMetodoHTTP.POST, cabecalhos, new HashMap<>(), mensagemEnvio);
        if (response.getEntity() == null) {
            throw new OutsourcingException("AES.sDAE.002 - Falha ao comunicar com o serviço de Extração de Dados. Resposta recebida nula", Boolean.FALSE);
        }
        String mensagemRecebida = response.getEntity().toString();

        LOGGER.fine(MessageFormat.format("Mensagem recebida: {0}", mensagemRecebida));

        try {
            RespostaAvaliacaoExtracaoVO resposta = (RespostaAvaliacaoExtracaoVO) UtilJson.converterDeJson(mensagemRecebida, RespostaAvaliacaoExtracaoVO.class);
            return resposta.getCodigoControle();
        } catch (Exception ex) {
            throw new OutsourcingException("AES.sDAE.003 - Falha ao converter resposta encaminhada pelo fornecedor de serviço de extração de dados e avaliação de autenticidade.", ex, Boolean.TRUE);
        }
    }

    /**
     * Metodo utilizado para capturar o resultado do serviço de avaliação de autenticidade documental e extração de dados junto a solução do fornecedor. Caso o resultado ainda não esteja
     * disponibilizado o resultado será enviado com a data/hora de retorno da captura do resultado com o valor nulo. Caso seja realizada a busca de um documento por identificamdor não localizado ou de
     * um canal divergente daquele de submissão do documento uma exceção será lançada indicando o motivo.
     *
     * @param id Identificador do documento que se deseja obter o resultado da avaliação de autenticidade / extração de dados
     * @return Documento com as informações obtidas até o momento.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public Documento capturaResultadoAvaliacaoExtracao(Long id) {

        // Captura do canal de comunicação
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "AES.cRAE.001 - ".concat(ConstantesUtil.MSG_CANAL_NAO_LOCALIZADO_SSO));

        // Captura o documento na na base de dados do SIMTR baseado no identificador do solicitado e
        // verifica se o canal vinculado ao documento é o mesmo que esta solicitando as informações
        Documento documento = this.documentoServico.getById(id, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);

        if (documento == null) {
            throw new SimtrRequisicaoException(ConstantesUtil.MSG_DOCUMENTO_NAO_LOCALIZADO);
        }

        if (!canal.getSigla().equals(documento.getCanalCaptura().getSigla())) {
            throw new SimtrRequisicaoException(ConstantesUtil.MSG_DOCUMENTO_CANAL_NAO_AUTORIZADO);
        }

        // Verifica se existe algum solicitação de serviço pendente de retorno
        if (documento.getControlesDocumento() != null) {
            ControleDocumento controlePendente = documento.getControlesDocumento().stream().filter(cd -> {
                // Controle não pode estar rejeitado e deve conter a indicação de realização de
                // pelo menos alguma atividade sem retorno definido
                return ((cd.getDataHoraRetornoRejeicao() == null)
                        && (((cd.getIndicativoClassificacao()) && (cd.getDataHoraRetornoClassificacao() == null))
                            || ((cd.getIndicativoExtracao()) && (cd.getDataHoraRetornoExtracao() == null))
                            || ((cd.getIndicativoAvaliacaoAutenticidade()) && (cd.getDataHoraRetornoAvaliacaoAutenticidade() == null))));
            }).findFirst().orElse(null);

            if (controlePendente != null) {
                this.capturaInformacaoServicoAutenticidadeExtracaoFornecedor(documento, controlePendente.getCodigoFornecedor());
            }
        }

        return documento;
    }
    
    
    public void executaAtualizacaoTokenOutsourcing() {
        Canal canal = this.canalServico.getByClienteSSO();
        
        if(!canal.getClienteSSO().equals(ConstantesUtil.CLIENT_ID_SIMTR_BPM) && !sessionContext.isCallerInRole(ConstantesUtil.PERFIL_MTRADM)) {
            LOGGER.info("OUTSOURCING DOCUMENTAL: USUARIO NAO AUTORIZADO");
            throw new SimtrPermissaoException("AES.aTO.001 - Requisição não autorizada a efetuar a atualização de token para o serviço de outsourcing");
        }
        
        this.atualizaTokenOutsourcing();
    }
    
    @PermitAll
    public Response executaPingOutsourcing() {
        // Captura paramatros do serviço de outsourcing
        String token = UtilParametro.getParametro("token.servico.extracao.autenticidade", Boolean.TRUE);
        String url = UtilParametro.getParametro("url.servico.extracao.autenticidade", Boolean.TRUE);

        // Monta o end point que deve ser chamado para submissão do documento
        String endpoint = url.concat(SERVICO_VERIFICACAO_COMUNICACAO);
        
        // Monta o cabeçalho com as credenciais para atualizar o token
        final Map<String, String> cabecalhos = new HashMap<>();
        cabecalhos.put(ConstantesUtil.HEADER_AUTHORIZATION, token);
        
        // Dispara a requisição para atualização do token
        return UtilRest.consumirServicoNoAuthJSON(endpoint, EnumMetodoHTTP.GET, cabecalhos, new HashMap<>(), null);
    }
    
    // *********** Métodos Privados ***********
    /**
     * Valida se o usuário possui o perfil de extração de dados
     *
     * @throws SimtrPermissaoException Caso o usuário não possua o perfil de extração de dados ou perfil administrativo [MTRSDNEXT, MTRADM]
     */
    private void validaPerfilUsuario() {
        if (!sessionContext.isCallerInRole(ConstantesUtil.PERFIL_MTRSDNEXT) && !sessionContext.isCallerInRole(ConstantesUtil.PERFIL_MTRDOSINT)
            && !sessionContext.isCallerInRole(ConstantesUtil.PERFIL_MTRADM)) {
            throw new SimtrPermissaoException("Usuário sem permissão para atualizar informações de documentos. Necessário estar vinculado ao perfil de Extração de Dados.");
        }
    }

    /**
     * Remove o registro de controle do fluxo de extração fornecido para a estrutura interna da CAIXA para viabilizar que o documento possa ser identificado novamente como pendência de extração com
     * base numa quantidade de minutos informado que deve ser considerado como inatividade da operação indicada pelo controle
     *
     * @param codigoControle Identificador do documento a ter o registro de controle em aberto excluido, caso não informado serão removidos de todos os documento que contemplarem o prazo informado
     * @param tempoMinutos Indica a quantidade de minutos que o registro deve ter em aberto para ser excluido
     * @return Número de registros afetados na operação de exclusão
     */
    private int removeRegistroControleCAIXA(String codigoControle, int tempoMinutos) {
        // Remove todos os registros de controle em aberto a mais de 10 minutos
        Calendar c = Calendar.getInstance();
        c.roll(Calendar.MINUTE, (tempoMinutos * -1));

        StringBuilder jpqlRemocao = new StringBuilder();
        jpqlRemocao.append(" DELETE FROM ControleDocumento cd ");
        jpqlRemocao.append(" WHERE cd.dataHoraEnvio < :data ");
        if (codigoControle != null) {
            jpqlRemocao.append(" AND cd.codigoFornecedor = :codigoControle ");
        }
        jpqlRemocao.append(" AND cd.dataHoraRetornoRejeicao IS NULL ");
        jpqlRemocao.append(" AND cd.dataHoraRetornoExtracao IS NULL ");
        jpqlRemocao.append(" AND LOCATE('CX', UPPER(cd.codigoFornecedor)) > 0  ");

        Query queryRemocao = this.entityManager.createQuery(jpqlRemocao.toString());
        queryRemocao.setParameter("data", c, TemporalType.TIMESTAMP);
        if (codigoControle != null) {
            queryRemocao.setParameter("codigoControle", codigoControle);
        }

        return queryRemocao.executeUpdate();
    }

    /**
     * Método utilizado para criar um registro relacionado ao controle de envio do documento para serviço de extração de dados e avaliação de autenticidade documental
     *
     * @param documento Documento utilizado para envio das informações ao serviço
     * @param indicadorClassificacao Indica se a atividade de classificação do documento deve ou não ser executada
     * @param indicadorExtracao Indica se a atividade de extração de dados do documento deve ou não ser executada
     * @param indicadorAutenticidade Indica se a atividade de avaliação de autenticidade documental deve ou não ser executada
     * @param janelaTemporalExtracaoEnum Caso seja indicado a necessidade de execução da atividade de extração de dados, a janela temporal devera ser informada
     * @param execucaoCaixa Indica se o registro de controle esta sendo criado para execução da atividade por equipe interna da CAIXA (true) ou por fornecedor externo (false)
     * @return objeto controle documento criado e associado ao documento referenciado
     */
    private ControleDocumento associaRegistroControleDocumento(Documento documento, boolean indicadorClassificacao, boolean indicadorExtracao, boolean indicadorAutenticidade, JanelaTemporalExtracaoEnum janelaTemporalExtracaoEnum, boolean execucaoCaixa) {
        ControleDocumento controleDocumento = new ControleDocumento();
        controleDocumento.setDocumento(documento);
        controleDocumento.setDataHoraEnvio(Calendar.getInstance());

        // Atribui a indicação fornecida da atividade de classificação
        controleDocumento.setIndicativoClassificacao(indicadorClassificacao);

        // Atribui a indicação fornecida de ativiade de extração de dados e janela temporal
        controleDocumento.setIndicativoExtracao(indicadorExtracao);
        controleDocumento.setJanelaTemporalExtracaoEnum(janelaTemporalExtracaoEnum);

        // Atribui a indicação fornecida da atividade de avaliação de autenticidade
        controleDocumento.setIndicativoAvaliacaoAutenticidade(indicadorAutenticidade);

        // Atribui a indicação fornecida de atividade executa por fornecedor ou corpo CAIXA
        controleDocumento.setExecucaoCaixa(execucaoCaixa);

        // vincula o controle do documento criado ao documento encaminhado
        documento.addControlesDocumento(controleDocumento);

        return controleDocumento;
    }

    /**
     * Captura o documento especifico informado ou o mais antigo da que esta com pendência de extraçao com base no tipo de documento informado, caso não seja informado um tpo de documento e nem o
     * identificador do documento desejado, será captura um documento "a classificar", ou seja, sem tipologia definida, obecendo a ordem de precedência pela data de captura.
     *
     * @param identificadorDocumento Identificador do documento especifico desejado para ser consultado quanto a disponibilidade de extração
     * @param identificadorTipoDocumento Identificador do tipo de documento associado ao documnento a ser capturado
     * @return Documento localizado para captura ou nulo caso o tipo indicado não possua documentos disponiveis
     */
    private Documento capturaDocumentoExtracao(Integer identificadorTipoDocumento, Long identificadorDocumento) {
        // Captura e valida o canal de comunicação
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "AES.cDE.001 - ".concat(ConstantesUtil.MSG_CANAL_NAO_LOCALIZADO_SSO));

        // Valida se o usuário possui perfil para extração de dados
        this.validaPerfilUsuario();

        // Monta a query de captura do documemnto a ter os dados extraidos conforme cada cenario abaixo:
        StringBuilder jpql = new StringBuilder();

        if (Objects.nonNull(identificadorDocumento)) {
            // Com definição do documento específico
            jpql.append(" SELECT DISTINCT d FROM Documento d ");
            jpql.append(" LEFT JOIN FETCH d.tipoDocumento td ");
            jpql.append(" LEFT JOIN FETCH d.controlesDocumento cd ");
            jpql.append(" LEFT JOIN FETCH d.conteudos c ");
            jpql.append(" LEFT JOIN td.atributosExtracao ae ");
            jpql.append(" LEFT JOIN d.atributosDocumento ad ");
            jpql.append(" WHERE EXISTS (SELECT a FROM AtributoExtracao a WHERE ativo = TRUE AND nomeAtributoRetorno IS NOT NULL AND presenteDocumento = TRUE AND tipoDocumento = td) ");
            jpql.append(" AND NOT EXISTS ( ");
            jpql.append("   SELECT cd FROM ControleDocumento WHERE documento = d AND dataHoraEnvio IS NOT NULL ");
            jpql.append("   AND ( ");
            jpql.append("      (dataHoraRetornoRejeicao IS NOT NULL) OR ");
            jpql.append("      (indicativoExtracao = true AND dataHoraRetornoExtracao IS NULL) ");
            jpql.append("   ) ");
            jpql.append(" ) ");
            jpql.append(" AND d.id = :idDocumento ");
            jpql.append(" ORDER BY d.dataHoraCaptura ");
        } else if (Objects.nonNull(identificadorTipoDocumento)) {
            // Com definição de tipologia e sem definição do documento específico
            jpql.append(" SELECT DISTINCT d FROM Documento d ");
            jpql.append(" LEFT JOIN FETCH d.tipoDocumento td ");
            jpql.append(" LEFT JOIN FETCH d.controlesDocumento cd ");
            jpql.append(" LEFT JOIN FETCH d.conteudos c ");
            jpql.append(" LEFT JOIN td.atributosExtracao ae ");
            jpql.append(" LEFT JOIN d.atributosDocumento ad ");
            jpql.append(" WHERE td.usoApoioNegocio = true ");
            jpql.append(" AND EXISTS (SELECT a FROM AtributoExtracao a WHERE ativo = TRUE AND nomeAtributoRetorno IS NOT NULL AND presenteDocumento = TRUE AND tipoDocumento = td) ");
            jpql.append(" AND NOT EXISTS ( ");
            jpql.append("   SELECT cd FROM ControleDocumento WHERE documento = d AND dataHoraEnvio IS NOT NULL ");
            jpql.append("   AND ( ");
            jpql.append("      (dataHoraRetornoRejeicao IS NOT NULL) OR ");
            jpql.append("      (indicativoExtracao = true AND dataHoraRetornoExtracao IS NULL) ");
            jpql.append("   ) ");
            jpql.append(" ) ");
            jpql.append(" AND ( ");
            jpql.append("    (d.atributosDocumento IS EMPTY) ");
            jpql.append("    OR (NOT EXISTS(SELECT DISTINCT a FROM AtributoDocumento a WHERE a.documento = d AND a.acertoManual = TRUE)) ");
            jpql.append(" ) ");
            jpql.append(" AND td.id = :idTipoDocumento ");
            jpql.append(" ORDER BY d.dataHoraCaptura ");
        } else {
            // Sem definição de tipologia e sem definição do documento específico
            jpql.append(" SELECT DISTINCT d FROM Documento d ");
            jpql.append(" LEFT JOIN FETCH d.tipoDocumento td ");
            jpql.append(" LEFT JOIN FETCH d.controlesDocumento cd ");
            jpql.append(" LEFT JOIN FETCH d.conteudos c ");
            jpql.append(" LEFT JOIN td.atributosExtracao ae ");
            jpql.append(" LEFT JOIN d.atributosDocumento ad ");
            jpql.append(" WHERE d.tipoDocumento IS NULL ");
            jpql.append(" AND NOT EXISTS ( ");
            jpql.append("   SELECT cd FROM ControleDocumento WHERE documento = d AND dataHoraEnvio IS NOT NULL ");
            jpql.append("   AND ( ");
            jpql.append("      (dataHoraRetornoRejeicao IS NOT NULL) OR ");
            jpql.append("      (indicativoExtracao = true AND dataHoraRetornoExtracao IS NULL) ");
            jpql.append("   ) ");
            jpql.append(" ) ");
            jpql.append(" AND ( ");
            jpql.append("    (d.atributosDocumento IS EMPTY) ");
            jpql.append("    OR (NOT EXISTS(SELECT DISTINCT a FROM AtributoDocumento a WHERE a.documento = d AND a.acertoManual = TRUE)) ");
            jpql.append(" ) ");
            jpql.append(" AND d.atributosDocumento IS EMPTY ");
            jpql.append(" ORDER BY d.dataHoraCaptura ");
        }

        // Cria a query de acordo com o cenário e define os valores dos parâmetros especificos necessário
        TypedQuery<Documento> queryDocumento = this.entityManager.createQuery(jpql.toString(), Documento.class);
        queryDocumento.setMaxResults(1);
        if (Objects.nonNull(identificadorDocumento)) {
            queryDocumento.setParameter("idDocumento", identificadorDocumento);
        } else if (Objects.nonNull(identificadorTipoDocumento)) {
            queryDocumento.setParameter("idTipoDocumento", identificadorTipoDocumento);
        }

        try {
            // Obtem o documento do banco que atende aos criterios definidos na consulta
            Documento documento = queryDocumento.getSingleResult();

            // Inclui no registro de controle a indicação de documento a classificar caso o mesmo não possua tipologia pré-definida
            boolean indicadorClassificacao = Objects.isNull(documento.getTipoDocumento()) ? Boolean.TRUE : Boolean.FALSE;

            // Cria o registro de controle para indicar que o documento não esta na lista de pendências
            ControleDocumento controleDocumento = this.associaRegistroControleDocumento(documento, indicadorClassificacao, Boolean.TRUE, Boolean.FALSE, JanelaTemporalExtracaoEnum.M60, Boolean.TRUE);

            // Cria o numero de identificador do registro de controle
            String codigoControle = "CX".concat(documento.getId().toString()).concat("-").concat(Integer.toString(documento.getControlesDocumento().size()));
            controleDocumento.setCodigoFornecedor(codigoControle);

            return documento;
        } catch (NoResultException nre) {
            // Caso não seja identificado nenhum documento nos criterios estabelecidos, retorna nulo
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }

    private Documento atualizaInformacaoDocumento(String codigoControle, boolean equipeCaixa,  ResultadoAvaliacaoExtracaoVO resultadoAvaliacaoExtracaoVO) {

        // Captura a hora de inicio da execução do metodo para utilização na identificação do retorno da informação
        Calendar dataHoraRetorno = Calendar.getInstance();

        // Captura o documento na na base de dados do SIMTR baseado no código de controle e
        // verifica se o canal vinculado ao documento é o mesmo que esta atualizando as informações
        final StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT d FROM Documento d ");
        jpql.append(" LEFT JOIN FETCH d.dossiesCliente dc ");
        jpql.append(" LEFT JOIN FETCH d.canalCaptura c ");
        jpql.append(" LEFT JOIN FETCH d.controlesDocumento cd ");
        jpql.append(" LEFT JOIN FETCH d.atributosDocumento ad ");
        jpql.append(" LEFT JOIN FETCH d.tipoDocumento td ");
        jpql.append(" LEFT JOIN FETCH td.atributosExtracao ae ");
        jpql.append(" LEFT JOIN FETCH ae.opcoesAtributo oa ");
        jpql.append(" WHERE cd.codigoFornecedor = :codigoFornecedor");

        Documento documento;
        try {
            TypedQuery<Documento> queryDocumento = this.entityManager.createQuery(jpql.toString(), Documento.class);
            queryDocumento.setParameter("codigoFornecedor", codigoControle);

            documento = queryDocumento.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            String mensagem = MessageFormat.format("Documento não localizado sob codigo de controle vinculado: {0}", codigoControle);

            throw new SimtrRecursoDesconhecidoException(mensagem);
        }

        // Identifica o registro de controle vinculado ao documento
        ControleDocumento controleDocumento = documento.getControlesDocumento().stream()
                                                       .filter(cd -> codigoControle.equals(cd.getCodigoFornecedor()))
                                                       .findFirst().get();

        // Verifica se o a resposta encaminhada indica rejeição ou atualização do documento e aplica os resultados
        if (resultadoAvaliacaoExtracaoVO.getCodigoRejeicao() != null) {
            this.aplicaResultadoRejeicao(controleDocumento, dataHoraRetorno, documento, resultadoAvaliacaoExtracaoVO.getCodigoRejeicao());
        }

        // Verifica se houve retorno de infomação da execução da atividade de classificação documental e atualiza as informações de data/hora de retorno e tipologia
        if (resultadoAvaliacaoExtracaoVO.getTipoDocumento() != null) {
            this.aplicaResultadoClassificacao(controleDocumento, dataHoraRetorno, documento, resultadoAvaliacaoExtracaoVO.getTipoDocumento());

            // Invalida os documento anteriores existentes no dossiê do cliente de mesma tipologia para fins de novos negocios            
            if(documento.getTipoDocumento() != null && !documento.getTipoDocumento().getPermiteMultiplosAtivos()){
                documento.getDossiesCliente().forEach(dc -> {
                    this.documentoServico.invalidarDocumentosAtivosCliente(dc.getId(), null, null, documento.getTipoDocumento(), Boolean.FALSE);
                });
            }
        }

        // Verifica se houve retorno de informação da execução da atividade de extração de dados e atualiza as informações de data/hora de retorno e atributos
        if ((resultadoAvaliacaoExtracaoVO.getAtributosDocumentoVO() != null) && (!resultadoAvaliacaoExtracaoVO.getAtributosDocumentoVO().isEmpty())) {
            this.aplicaResultadoExtracao(controleDocumento, dataHoraRetorno, documento, resultadoAvaliacaoExtracaoVO.getAtributosDocumentoVO(), equipeCaixa);
        }

        // Verifica se houve retorno de informação da execução da atividade de avaliacao de autenticidade e
        // atualiza as informações de data/hora de retorno e indice vinculado ao documento
        if (resultadoAvaliacaoExtracaoVO.getIndiceAvaliacaoAutenticidade() != null) {
            this.aplicaResultadoAutenticidade(controleDocumento, dataHoraRetorno, documento, resultadoAvaliacaoExtracaoVO.getIndiceAvaliacaoAutenticidade());
        }

        // Verifica se houve retorno de informação da execução da atividade de tratamento do binario e
        // atualiza as informações de data/hora de retorno e armazena o novo binario vinculado ao documento
        if (resultadoAvaliacaoExtracaoVO.getBinario() != null) {
            this.aplicaResultadoTratamento(controleDocumento, dataHoraRetorno, documento, resultadoAvaliacaoExtracaoVO.getMimetype(), resultadoAvaliacaoExtracaoVO.getBinario());
        }

        // Valida os atributos encaminhados para o documentos, se estes foram definidos e são esperados na definição da tipologia
        if ((documento.getAtributosDocumento() != null) && (!documento.getAtributosDocumento().isEmpty())) {
            this.documentoServico.validaAtributosDocumento(documento);
        }
        
        this.entityManager.merge(documento);

        return documento;
    }
    
    /**
     * Atualiza o registro de controle indicando conclusão por rejeição e atribui a data de validade para novos negocios no documento indicado
     *
     * @param controleDocumento Registro de controle a ser atualizado
     * @param dataHoraRetorno Data/Hora a ser utilizada na conclusão do registro de controle e validade do documento para fins de novos negócios
     * @param documento Documento a ser definida data fim de validade
     * @param codigoRejeicao Código de rejeição a ser aplicado no registro de controle
     */
    private void aplicaResultadoRejeicao(ControleDocumento controleDocumento, Calendar dataHoraRetorno, Documento documento, String codigoRejeicao) {
        // Verifica se já houve algum retorno previo de rejeição para o documento através do registro de controle vinculado ao codigo informado
        if (controleDocumento.getCodigoRejeicao() == null) {
            controleDocumento.setCodigoRejeicao(codigoRejeicao);
            controleDocumento.setDataHoraRetornoRejeicao(dataHoraRetorno);

            // Invalida o documento para novos negocios se o mesmo ainda não estiver invalidado
            if ((documento.getDataHoraValidade() == null) || (documento.getDataHoraValidade().after(Calendar.getInstance()))) {
                documento.setDataHoraValidade(dataHoraRetorno);
            }
        }
    }

    /**
     *
     * @param controleDocumento Registro de controle a ser atualizado
     * @param dataHoraRetorno Data/Hora a ser utilizada na conclusão do registro de controle e validade do documento para fins de novos negócios
     * @param documento Documento a ser definida data fim de validade
     * @param tipologiaDocumento Codigo enviado para o tipo de documento a classificar o documento
     */
    private void aplicaResultadoClassificacao(ControleDocumento controleDocumento, Calendar dataHoraRetorno, Documento documento, String tipologiaDocumento) {
        // Verifica se houve retorno de infomação da execução da atividade de classificação documental e atualiza as informações de data/hora de retorno e tipologia
        // Caso o registro de controle não possua fechamento da atividade de classificação, finaliza a mesma
        if (controleDocumento.getDataHoraRetornoClassificacao() == null && controleDocumento.getIndicativoClassificacao()) {

            // Valida se o tipo de documento, caso encaminhado, trata-se de um tipo valido
            TipoDocumento tipoDocumento = this.tipoDocumentoServico.getByTipologia(tipologiaDocumento);
            if (tipoDocumento == null) {
                throw new SimtrRequisicaoException(ConstantesUtil.MSG_TIPO_DOCUMENTO_NAO_LOCALIZADO);
            }

            // Atribui a data/hora de fechamento da atividade e armazena o valor indicado para a classificação
            controleDocumento.setDataHoraRetornoClassificacao(dataHoraRetorno);
            controleDocumento.setValorRetornoClassificacao(tipologiaDocumento);

            // Define o tipo de documento caso a tipologia ainda não esteja definida ou o tipo enviado seja diferente do associado ao documento,
            // desde que este ainda não tenha atributos previamente definidos
            boolean documentoComAtributos = ((Objects.nonNull(documento.getAtributosDocumento())) && (!documento.getAtributosDocumento().isEmpty()));
            if ((documento.getTipoDocumento() == null) || (!documentoComAtributos)) {
                documento.setTipoDocumento(tipoDocumento);
            }
        }
    }

    /**
     * Atualiza o registro de controle indicando conclusão da atividade de extração de dados e inclui/atualiza os atributos do documento indicado
     *
     * @param controleDocumento Registro de controle a ser atualizado
     * @param dataHoraRetorno Data/Hora a ser utilizada na conclusão do registro de controle para a atividade de extração
     * @param documento Documento a ter os dados atualizados
     * @param atributosDocumentoVO Lista de objetos que representa os atributos a serem atualizados
     * @param equipeCaixa Atributo utilizado para indicar se a ação foi realiza por equipe CAIXA, o que indica envolvimento humano. Neste caso, os atributos mesmo não existente serão marcados como
     *        acerto manual positivo
     */
    private void aplicaResultadoExtracao(ControleDocumento controleDocumento, Calendar dataHoraRetorno, Documento documento, List<AtributoDocumentoVO> atributosDocumentoVO, boolean equipeCaixa) {

        // Registra a resposta apenas se o registro de controle indicar solicitação para atividade de extração de dados e se o registro ainda não estiver com data/hora de retorno definida
        if ((controleDocumento.getIndicativoExtracao()) && (controleDocumento.getDataHoraRetornoExtracao() == null)) {
            try {
                String retornoExtracao = UtilJson.converterParaJson(atributosDocumentoVO);
                controleDocumento.setValorRetornoExtracao(retornoExtracao);
                controleDocumento.setDataHoraRetornoExtracao(dataHoraRetorno);
            } catch (Exception ex) {
                LOGGER.log(Level.ALL, "Falha ao converter retorno de atributos para String JSON visando armazenamento junto ao registro de controle", ex);
                throw new SimtrRequisicaoException(ConstantesUtil.MSG_FALHA_ATUALIZACAO_ATRIBUTOS_DOCUMENTO);
            }

            // Captura o tipo de documento com a estrutura competa com base no tipo definido no documento encaminhado
            TipoDocumento tipoDocumento = this.tipoDocumentoServico.getById(documento.getTipoDocumento().getId());

            // Inicia a lista de atributos não esperados para lançar uma exceção caso não seja identificado algum valor enviado
            List<String> atributosNaoEsperados = new ArrayList<>();

            // Atualiza ou sobrepõe informações dos atributos encaminhados ao documento caso ainda não estejam definidas
            atributosDocumentoVO.forEach(ad -> {

                // Captura o atributo extração definido na tipologia considerando que a informação venha com base no nome transitado internamente
                AtributoExtracao atributoExtracao = tipoDocumento.getAtributosExtracao().stream()
                                                                 .filter(ae -> ad.getChave().equals(ae.getNomeAtributoDocumento()))
                                                                 .findFirst().orElse(null);

                // Caso a ação venha de equipe externa (Fornecedor ou sistema terceiro) obtem o atributo extração com base no nome definido como retorno externo
                if (!equipeCaixa) {
                    atributoExtracao = tipoDocumento.getAtributosExtracao().stream()
                                                    .filter(ae -> ad.getChave().equals(ae.getNomeAtributoRetorno()))
                                                    .findFirst().orElse(null);
                }

                // Caso o atributo nao seja localizado na tipologia acresnta a informação na lista de atributo não esperados para definir a pendência
                if (atributoExtracao == null) {
                    atributosNaoEsperados.add(ad.getChave());

                    // Quebra o ciclo e passa para o proximo atributo
                    return;
                }

                final String nomeAtributo = atributoExtracao.getNomeAtributoDocumento();

                // Captura o atributo previamente definido no documento para a chave encaminhada
                AtributoDocumento atributoDocumento = documento.getAtributosDocumento().stream()
                                                               .filter(atributo -> nomeAtributo.equals(atributo.getDescricao()))
                                                               .findAny().orElse(null);

                // Caso o atributo não seja identificado no documento cria um novo registro
                if (atributoDocumento == null) {
                    atributoDocumento = new AtributoDocumento();

                    atributoDocumento.setAcertoManual(equipeCaixa);
                    atributoDocumento.setDescricao(atributoExtracao.getNomeAtributoDocumento());
                    atributoDocumento.setConteudo(ad.getValor());
                    atributoDocumento.setIndiceAssertividade(ad.getIndiceAssertividade());
                    atributoDocumento.setDocumento(documento);
                    documento.addAtributosDocumento(atributoDocumento);
                } else {
                    // Atualiza o valor do atributo, indice de assertividade e indicação de acerto manual
                    atributoDocumento.setConteudo(ad.getValor());
                    atributoDocumento.setIndiceAssertividade(ad.getIndiceAssertividade());
                    atributoDocumento.setAcertoManual(equipeCaixa);
                }
            });
            
            // Caso seja identificados atributos não localizados na tipologia, levanta uma exceção ao fluxo
            if (!atributosNaoEsperados.isEmpty()) {
                String mensagem = MessageFormat.format("AES.aRE.001 - Atributos {0} não esperados para o tipo de documento [{1}]. Codigo de controle: {2}", Arrays.toString(atributosNaoEsperados.toArray()), tipoDocumento.getNome(), controleDocumento.getCodigoFornecedor());
                throw new SimtrRequisicaoException(mensagem);
            }
            
            //Calcula o PAD do documento
            /*
            PAD = (∑IAA / QAE) * (QAR / QAE);

            Onde:
            PAA = Percentual de Assertividade do Documento
            ∑IAA = Soma dos índices de assertividade dos atributos recebidos
            QAE = Quantidade de atributos esperados
            QAR = Quantidade de atributos recebidos
             */
            long qtdeAtributosEsperados = documento.getTipoDocumento().getAtributosExtracao().stream().filter(ae -> ae.getAtivo()).count();
            long qtdeAtributosRecebidos = documento.getAtributosDocumento().size();
            double somaIndicesAssertividade = documento.getAtributosDocumento().stream().mapToDouble(ad -> ad.getIndiceAssertividade().doubleValue()).sum();

            double fator1 = (somaIndicesAssertividade / qtdeAtributosEsperados);
            double fator2 = ((double) qtdeAtributosRecebidos / (double) qtdeAtributosEsperados);
            double pad = fator1 * fator2;
            controleDocumento.setPercentualAssertividadeDocumento(new BigDecimal(pad));
            documento.setPercentualAssertividadeDocumento(new BigDecimal(pad));

            try {
                this.siecmServico.atualizaAtributosDocumento(documento);
            } catch (Exception e) {
                String mensagem = MessageFormat.format("AES.aRE.002 - Não foi possivel atualizar os atributos do documento junto ao SIECM. Causa: {0}", e.getLocalizedMessage());
                LOGGER.log(Level.WARNING, mensagem);
                LOGGER.log(Level.FINE, mensagem, e);
            }
        }
    }

    /**
     * Atualiza o registro de controle indicando conclusão da atividade de avaliação de autenticidade documental e inclui/atualiza o indice do documento indicado
     *
     * @param controleDocumento Registro de controle a ser atualizado
     * @param dataHoraRetorno Data/Hora a ser utilizada na conclusão do registro de controle para a atividade de avaliação de autenticidade
     * @param documento Documento a ter os dados atualizados
     * @param indiceAutencicidade Valor definido para o indice da avaliação de autenticidade atribuido ao documento
     */
    private void aplicaResultadoAutenticidade(ControleDocumento controleDocumento, Calendar dataHoraRetorno, Documento documento, BigDecimal indiceAutencicidade) {

        // Registra a resposta apenas se o registro de controle indicar solicitação para atividade de extração de dados e
        // se o registro ainda não estiver com data/hora de retorno definida para a atividade
        if ((controleDocumento.getIndicativoAvaliacaoAutenticidade()) && (controleDocumento.getDataHoraRetornoAvaliacaoAutenticidade() == null)) {
            controleDocumento.setValorRetornoAvaliacaoAutenticidade(indiceAutencicidade);
            controleDocumento.setDataHoraRetornoAvaliacaoAutenticidade(dataHoraRetorno);

            // Atualiza ou sobrepõe informações do indice de avaliação de autenticidade atribuido ao documento
            documento.setIndiceAntifraude(indiceAutencicidade);
        }
    }

    /**
     * Atualiza o registro de controle indicando conclusão da atividade de tratamento do documento e armazena o novo binario do incluindo seu novo indice junto ao SIECM parado documento indicado
     *
     * @param controleDocumento Registro de controle a ser atualizado
     * @param dataHoraRetorno Data/Hora a ser utilizada na conclusão do registro de controle para a atividade de avaliação de autenticidade
     * @param documento Documento a ter os dados atualizados
     * @param binario String ewm formato base64 que representa o binario com aplicação dos tratamentos realizados ao mesmo tratado
     */
    private void aplicaResultadoTratamento(ControleDocumento controleDocumento, Calendar dataHoraRetorno, Documento documento, String mimetype, String binario) {

        // Caso o registro de controle da solicitação de serviço não possua fechamento
        // da atividade de atualiação do conteudo, finaliza a mesma
        if ((documento.getCodigoSiecmTratado() == null) && (controleDocumento.getDataHoraRetornoConteudo() == null)) {

            // Atualiza as informações caso o conteudo tenha sido enviado
            // Valida se o conteudo foi enviado e todos tratam-se de base64 validos
            if (!binario.matches(ConstantesUtil.REGEX_BASE64)) {
                throw new SimtrRequisicaoException(ConstantesUtil.MSG_CONTEUDO_ENCAMINHADO_INVALIDO);
            }

            // Valida o mimetype indicado para o conteudo encaminhado
            FormatoConteudoEnum formatoConteudoEnum;
            try {
                formatoConteudoEnum = FormatoConteudoEnum.getByMimeType(mimetype);
                documento.setFormatoConteudoTratadoEnum(formatoConteudoEnum);
            } catch (IllegalArgumentException ile) {
                throw new SimtrRequisicaoException(ConstantesUtil.MSG_FORMATO_CONTEUDO_INVALIDO);
            }

            boolean armazenaLocal = Boolean.getBoolean("simtr_imagens_local");
            if (!armazenaLocal) {
                // Limpa os conteudos do documento antes de salva-los visto que os mesmo serão enviados para o GED
                documento.getConteudos().clear();

                // Realiza a gravação do documento no GED
                String pastaDocumento = DOCUMENTO_TRATADO.concat("_").concat(documento.getId().toString());
                String identificador = this.siecmServico.armazenaDocumentoOperacaoSIECM(documento, TemporalidadeDocumentoEnum.VALIDO, binario, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL, pastaDocumento);

                documento.setCodigoSiecmTratado(identificador);
            } else {

                documento.getConteudos().forEach((c) -> this.entityManager.remove(c));

                Set<Conteudo> novosConteudos = new HashSet<>();
                novosConteudos.add(new Conteudo(documento, binario, novosConteudos.size() + 1));

                // Limpa os conteudos para armazenar os novos atualizados
                documento.setConteudos(new HashSet<>());
                this.documentoServico.update(documento);

                documento.setConteudos(novosConteudos);

                novosConteudos.forEach(c -> this.entityManager.persist(c));
            }
        } 

        // Define a data/hora de retorno no registro de controle para inibir novas atualizações de conteudo 
        // ou indicar o envio da informação pelo fornecedor para conclusão do registro.
        controleDocumento.setDataHoraRetornoConteudo(dataHoraRetorno);
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    private void capturaInformacaoServicoAutenticidadeExtracaoFornecedor(Documento documento, String codigoFornecedor) {

        String urlWebService = UtilParametro.getParametro("url.servico.extracao.autenticidade", false);
        if (urlWebService != null) {
            // Obtem o token de comunicação a ser utilizado para autenticar no serviço do fornecedor
            final String token = UtilParametro.getParametro("token.servico.extracao.autenticidade", true);
            final Map<String, String> cabecalhos = new HashMap<>();
            cabecalhos.put(ConstantesUtil.HEADER_AUTHORIZATION, token);
            
            urlWebService = urlWebService.concat(SERVICO_SUBMISSAO_DOCUMENTO).concat("/").concat(codigoFornecedor);
            LOGGER.info(MessageFormat.format("Chamando serviço do fornecedor: url {0}", urlWebService));
            Response response = UtilRest.consumirServicoNoAuthJSON(urlWebService, EnumMetodoHTTP.GET, cabecalhos, new HashMap<>(), null);
            LOGGER.info(MessageFormat.format("Mensagem recebida: {0}", response.getEntity()));

            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                String mensagem = MessageFormat.format("Falha ao comunicar com o serviço de Oursourcing Documental. Codigo Retorno = {0} | Resposta recebida: {1}", response.getStatus(), response.getEntity());
                throw new OutsourcingException(mensagem, Boolean.FALSE);
            }

            ResultadoAvaliacaoExtracaoDTO resultadoAvaliacaoExtracaoDTO;
            try {
                resultadoAvaliacaoExtracaoDTO = (ResultadoAvaliacaoExtracaoDTO) UtilJson.converterDeJson(response.getEntity().toString(), ResultadoAvaliacaoExtracaoDTO.class);
            } catch (Exception ex) {
                String mensagem = MessageFormat.format("Falha ao converter o objeto retornado pelo fornecedor do serviço de extração de dados e avaliação de autenticidade. Mensagem recebida: {0}", response.getEntity()
                                                                                                                                                                                                             .toString());
                throw new OutsourcingException(mensagem, ex, Boolean.TRUE);
            }

            // Atualiza as informações do documeto baseado no retorno da resultado do serviço de extração de dados e avaliação de autenticidade
            this.atualizaInformacaoDocumento(codigoFornecedor, Boolean.FALSE, resultadoAvaliacaoExtracaoDTO.prototype());

            this.documentoServico.update(documento);
        }
    }

    @Schedule(persistent = false, dayOfMonth = "*", hour = "*/6")
    private void atualizaTokenOutsourcing() {
        LOGGER.info("OUTSOURCING DOCUMENTAL: INICIANDO ATUALIZACAO TOKEN");

        // Captura paramatros do serviço de outsourcing
        String outsourcingClientSecret = UtilParametro.getParametro("outsourcing_sso_client_secret", Boolean.FALSE);
        String token = UtilParametro.getParametro("token.servico.extracao.autenticidade", Boolean.TRUE);
        String url = UtilParametro.getParametro("url.servico.extracao.autenticidade", Boolean.TRUE);

        if (outsourcingClientSecret != null) {
            // Separa os valores de ClientID e Secret
            String[] credenciaisSSO = outsourcingClientSecret.split(";");
            
            // Obtem token de acesso a ser enviado
            String accessToken = keycloakUtil.obtemNovoToken(credenciaisSSO[0], credenciaisSSO[1]);

            // Monta o end point que deve ser chamado para submissão do documento
            String endpoint = url.concat(SERVICO_ATUALIZACAO_TOKEN);
            
            // Monta o cabeçalho com as credenciais para atualizar o token
            final Map<String, String> cabecalhos = new HashMap<>();
            cabecalhos.put(ConstantesUtil.HEADER_AUTHORIZATION, token);

            //Monta o corpo da mensagem de envio
            Map<String, String> body = new HashMap<>();
            body.put("token", accessToken);
            String mensagemEnvio;
            try {
                mensagemEnvio = UtilJson.converterParaJson(body);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "OUTSOURCING DOCUMENTAL: FALHA AO CONVERTER O CORPO DE MENSAGEM DE ENVIO", e);
                return;
            }
            
            // Dispara a requisição para atualização do token
            Response response = UtilRest.consumirServicoNoAuthJSON(endpoint, EnumMetodoHTTP.POST, cabecalhos, new HashMap<>(), mensagemEnvio);
            
            if(Family.SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
                LOGGER.info("OUTSOURCING DOCUMENTAL: FINALIZANDO ROTINA DE ATUALIZACAO DE TOKEN COM SUCESSO");
                return;
            }
            
            String mensagem = MessageFormat.format("OUTSOURCING DOCUMENTAL: TOKEN NAO ATUALIZADO | HTTP STATUS {0} | {1}", response.getStatus(), response.getEntity());
            LOGGER.info(mensagem);
        } else {
            LOGGER.info("OUTSOURCING DOCUMENTAL: TOKEN NAO ATUALIZADO | PROPRIEDADE [outsourcing_sso_client_secret] NAO DEFINIDA");
        }
    }
}
