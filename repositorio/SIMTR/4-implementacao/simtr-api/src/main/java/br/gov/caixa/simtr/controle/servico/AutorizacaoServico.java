package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SipesResponseDTO;
import br.gov.caixa.pedesgo.arquitetura.siiso.dto.RetornoPessoasFisicasDTO;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.simtr.controle.excecao.DossieAutorizacaoException;
import br.gov.caixa.simtr.controle.excecao.SiecmException;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.helper.AutorizacaoConsultaHelper;
import br.gov.caixa.simtr.controle.vo.autorizacao.AutorizacaoDocumentoVO;
import br.gov.caixa.simtr.controle.vo.autorizacao.AutorizacaoVO;
import br.gov.caixa.simtr.controle.vo.autorizacao.MensagemOrientacaoVO;
import br.gov.caixa.simtr.modelo.entidade.Autorizacao;
import br.gov.caixa.simtr.modelo.entidade.AutorizacaoOrientacao;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.ComposicaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DocumentoAutorizacao;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.entidade.RegraDocumental;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.SistemaPesquisaEnum;
import br.gov.caixa.simtr.modelo.enumerator.SistemaPesquisaTipoRetornoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao.AnaliseRegraDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao.RetornoAutorizacaoConjuntaDTO;
import br.gov.caixa.simtr.util.CalendarUtil;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRDOSINT,
    ConstantesUtil.PERFIL_MTRDOSMTZ,
    ConstantesUtil.PERFIL_MTRDOSOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class AutorizacaoServico extends AbstractService<Autorizacao, Long> {

    @Inject
    private EntityManager entityManager;

    @EJB
    private AutorizacaoConsultaHelper autorizacaoConsultasHelper;

    @EJB
    private CanalServico canalService;

    @EJB
    private DocumentoServico documentoServico;

    @EJB
    private DossieClienteServico dossieClienteServico;

    @EJB
    private KeycloakService keycloakService;

    @EJB
    private NivelDocumentalServico nivelDocumentalService;

    @EJB
    private ProdutoServico produtoServico;

    @EJB
    private RelatorioServico relatorioServico;

    @EJB
    private SiecmServico siecmServico;

    @EJB
    private TipoDocumentoServico tipoDocumentoServico;

    @Inject
    private CalendarUtil calendarUtil;

    @EJB
    private ComposicaoDocumentalServico composicaoDocumentalService;

    // **********************
    private final static String MENSAGEM_CANAL_NAO_LOCALIZADO = "Canal de comunicação não localizado para client ID vinculado ao token de autenticação.";
    private final static String MENSAGEM_CANAL_NAO_AUTORIZADO = "Canal de comunicação não autorizado para consumo dos serviços do dossiê digital.";
    
    private static final Logger LOGGER = Logger.getLogger(AutorizacaoServico.class.getName());

    private final ThreadLocal<List<MensagemOrientacaoVO>> orientacoes = new ThreadLocal<>();

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Gera uma autorização para contratação de um produto;<br>
     * Para realizar essa ação, o método realiza dos seguintes passos: <br>
     * <ol>
     * <li>Identifica as composições documentais cadastradas para a operação e modalidade solicitada</li>
     * <ol>
     * <li>Caso não seja localizado nenhuma composição é retornado uma negativa informando que o produto não foi localizado</li>
     * </ol>
     * <li>Analisa os documentos do cliente x compsoições documentais da seguinte forma:</li>
     * <ol>
     * <li>Percorre todas as composições de documento, para cada composição;</li>
     * <li>Percorre todas as regras documentais vinculadas;</li>
     * <li>Para cada regra documental localizada, percorre os documentos do cliente localizados no ECM e caso encontre documento valido armazena documento utilizado e pula para proxima regra, caso
     * contrario para marca que o documento especifico ou função documental não foi localizado conforme o caso e pula para a proxima regra. Repete até não ter mais regras;</li>
     * </ol>
     * <li>Ao terminar o ciclo:</li>
     * <ul>
     * <li>Caso não tenha atendido a nenhuma composição documental, retorna o objeto de autorização com a negativa a lista de documentos ausentes</li>
     * <li>Caso tenha atendido a pelo menos uma composição documental, cria o NSU da autorização, atribui os documentos utilizados na autorização, salva a autorização na base e retorna o objeto com o
     * codigo de autorização</li>
     * </ul>
     * </ol>
     *
     * @param id Identificador do Dossiê do Cliente a ter o pedido de autorização analisado
     * @param operacao Código da operação CAIXA que deseja solicitar a autorização
     * @param modalidade - Código da modalidade especifica do produto desejado
     * @return Objeto de autorização contendo o codigo NSU da autorização caso a operação tenha sido bem sucedida ou em caso negativo o motivo da rejeição
     * @throws DossieAutorizacaoException Lançada quando existe algum impedimento para gerar a autorização identificando as causas do impedimento apresentado
     */
    public AutorizacaoVO getAutorizacao(Long id, Integer operacao, Integer modalidade) {
        // Inicializa a lista de mensagens de orientações do ThreadLocal
        orientacoes.set(new ArrayList<>());

        // Captura o canal de comunicação para utilizar na vinculação do documento
        Canal canal = this.canalService.getByClienteSSO();
        this.canalService.validaRecursoLocalizado(canal, "AS.gA.001 - ".concat(MENSAGEM_CANAL_NAO_LOCALIZADO));
        this.canalService.validaOutorgaCanal(canal, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, "AS.gA.002 - ".concat(MENSAGEM_CANAL_NAO_AUTORIZADO));

        // ***** Passo 1
        // ***** Passo 1.1 - Captura do Dossiê de Cliente
        DossieCliente dossieCliente = this.dossieClienteServico.getById(id, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (dossieCliente == null) {
            throw new SimtrRecursoDesconhecidoException("AS.gA.002 - Dossiê de cliente não localizado sob identificador informado para vinculação com o documento");
        }

        // ***** Passo 1.2 - Produdo existente sob operação/modalidade
        Produto produtoSolicitado = this.produtoServico.getByOperacaoModalidade(operacao, modalidade, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if ((produtoSolicitado == null) || (!produtoSolicitado.getDossieDigital())) {
            String mensagem = MessageFormat.format("Produto solicitado não disponivel para o Dossiê Digital. Operação: {0} | Modalidade: {1}", operacao, modalidade);
            throw new DossieAutorizacaoException(mensagem, null, null, dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), operacao, modalidade, canal.getSigla(), Boolean.FALSE);
        }

        // ***** Passo 2
        // ***** Passo 2.1 - Produto configurado para o Tipo de Pessoa
        if ((!TipoPessoaEnum.A.equals(produtoSolicitado.getTipoPessoa())) && (!dossieCliente.getTipoPessoa().equals(produtoSolicitado.getTipoPessoa()))) {
            throw new DossieAutorizacaoException("Produto solicitado não disponivel para o tipo de pessoa indicada.", null, null, dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), produtoSolicitado, canal.getSigla(), Boolean.FALSE);
        }

        RetornoPessoasFisicasDTO retornoPessoasFisicasDTO = null;
        if (TipoPessoaEnum.F.equals(dossieCliente.getTipoPessoa())) {
            // ***** Passo 2.2 - Consulta Receita Federal
            retornoPessoasFisicasDTO = this.autorizacaoConsultasHelper.realizaConsultaReceitaFederal(dossieCliente.getCpfCnpj(), produtoSolicitado, canal, orientacoes);
        }
        
        // ***** Passo 3 - Consulta SIPES
        SipesResponseDTO sipesResponseDTO = this.autorizacaoConsultasHelper.realizaConsultaSIPES(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), produtoSolicitado, canal, orientacoes);

        // ***** Passo 4 - Captura o mapa de documentos definitivos onde o documento é a chave e o link de acesso ao conteudo no SIECM é o valor
        Map<Documento, String> documentosDefinitivos = this.documentoServico.listDocumentosDefinitivosDossieDigital(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa());

        // ***** Passo 5 - Atualiza Nivel Documental
        this.nivelDocumentalService.atualizaNiveisDocumentaisCliente(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), new ArrayList<>(documentosDefinitivos.keySet()));

        // ***** Passo 6 - Capturar a autorização
        AutorizacaoVO autorizacaoConcedida = this.requestAutorizacao(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), produtoSolicitado, documentosDefinitivos, canal.getSigla(), sipesResponseDTO);

        // ***** Passo 7 - Criar Transacao ECM
        Long nsu = autorizacaoConcedida.getAutorizacao().getCodigoNSU();
        try {
            this.siecmServico.criaTransacaoClienteSIECM(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), nsu.toString(), ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
            final List<String> ids = autorizacaoConcedida.getDocumentosUtilizados().stream()
                                                         .map(documentoUtilizado -> documentoUtilizado.getDocumento().getCodigoGED())
                                                         .collect(Collectors.toList());
            this.siecmServico.vinculaDocumentosTransacaoSIECM(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), nsu.toString(), ids);
        } catch (EJBException e) {
            SiecmException siecmException = (SiecmException) e.getCause();
            this.invalidaPorFalhaECM(nsu, siecmException.getLocalizedMessage());
            throw siecmException;
        }

        // ***** Passo 8 - Grava Documento Consulta Cadastral
        Documento documentoPesquisas = this.gravaDocumentoConsultaCadastral(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), nsu, produtoSolicitado, canal, retornoPessoasFisicasDTO, sipesResponseDTO);

        // ***** Passo 8.1 - Vincula o documento de pesquisas cadastrais com a autorização concedida
        DocumentoAutorizacao documentoAutorizacaoPesquisas = new DocumentoAutorizacao();
        documentoAutorizacaoPesquisas.setAutorizacao(autorizacaoConcedida.getAutorizacao());
        documentoAutorizacaoPesquisas.setCodigoDocumentoGED(documentoPesquisas.getCodigoGED());
        documentoAutorizacaoPesquisas.setFinalidade(documentoPesquisas.getTipoDocumento().getNome());
        this.entityManager.persist(documentoAutorizacaoPesquisas);
        
        autorizacaoConcedida.setMensagensOrientacoes(orientacoes.get());
        autorizacaoConcedida.setResultadoPesquisaSIPES(sipesResponseDTO);

        return autorizacaoConcedida;
    }

    /**
     * Gera uma autorização para contratação de um produto;<br>
     * Para realizar essa ação, o método realiza dos seguintes passos: <br>
     * <ol>
     * <li>Identifica as composições documentais cadastradas para a operação e modalidade solicitada</li>
     * <ol>
     * <li>Caso não seja localizado nenhuma composição é retornado uma negativa informando que o produto não foi localizado</li>
     * </ol>
     * <li>Analisa os documentos do cliente x compsoições documentais da seguinte forma:</li>
     * <ol>
     * <li>Percorre todas as composições de documento, para cada composição;</li>
     * <li>Percorre todas as regras documentais vinculadas;</li>
     * <li>Para cada regra documental localizada, percorre os documentos do cliente localizados no ECM e caso encontre documento valido armazena documento utilizado e pula para proxima regra, caso
     * contrario para marca que o documento especifico ou função documental não foi localizado conforme o caso e pula para a proxima regra. Repete até não ter mais regras;</li>
     * </ol>
     * <li>Ao terminar o ciclo:</li>
     * <ul>
     * <li>Caso não tenha atendido a nenhuma composição documental, retorna o objeto de autorização com a negativa a lista de documentos ausentes</li>
     * <li>Caso tenha atendido a pelo menos uma composição documental, cria o NSU da autorização, atribui os documentos utilizados na autorização, salva a autorização na base e retorna o objeto com o
     * codigo de autorização</li>
     * </ul>
     * </ol>
     *
     * @param cpfCnpj Numero do CPF/CNPJ do cliente encaminhado na requisição
     * @param tipoPessoaEnum Tipo de pessoa identificado
     * @param operacao Código da operação CAIXA que deseja solicitar a autorização
     * @param modalidade - Código da modalidade especifica do produto desejado
     * @return Lista contendo informações sobre a documentaçao analisada indicando quando presente/ausente no dossiêdo cliente e a composição
     */
    public List<AnaliseRegraDTO> verificaDisponibilidadeAutorizacao(Long id, Integer operacao, Integer modalidade) {        
        // Captura o canal de comunicação
        Canal canal = this.canalService.getByClienteSSO();
        this.canalService.validaRecursoLocalizado(canal, "AS.vDA.001 - ".concat(MENSAGEM_CANAL_NAO_LOCALIZADO));
        this.canalService.validaOutorgaCanal(canal, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, "AS.vDA.002 - ".concat(MENSAGEM_CANAL_NAO_AUTORIZADO));

        // ***** Passo 1
        // ***** Passo 1.1 - Captura do Dossiê de Cliente
        DossieCliente dossieCliente = this.dossieClienteServico.getById(id, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (dossieCliente == null) {
            throw new SimtrRecursoDesconhecidoException("AS.vDA.003 - Dossiê de cliente não localizado sob identificador informado para vinculação com o documento");
        }

        // ***** Passo 1.2 - Produdo existente sob operação/modalidade
        Produto produtoSolicitado = this.produtoServico.getByOperacaoModalidade(operacao, modalidade, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if ((produtoSolicitado == null) || (!produtoSolicitado.getDossieDigital())) {
            String mensagem = MessageFormat.format("AS.vDA.004 - Produto solicitado não disponivel para o Dossiê Digital. Operação: {0} | Modalidade: {1}", operacao, modalidade);
            throw new DossieAutorizacaoException(mensagem, null, null, dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), operacao, modalidade, canal.getSigla(), Boolean.FALSE);
        }

        // ***** Passo 2
        // ***** Passo 2.1 - Produto configurado para o Tipo de Pessoa
        if ((!TipoPessoaEnum.A.equals(produtoSolicitado.getTipoPessoa())) && (!dossieCliente.getTipoPessoa().equals(produtoSolicitado.getTipoPessoa()))) {
            throw new DossieAutorizacaoException("AS.vDA.005 - Produto solicitado não disponivel para o tipo de pessoa indicada.", null, null, dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), produtoSolicitado, canal.getSigla(), Boolean.FALSE);
        }

        // ***** Passo 3 - Captura o mapa de documentos definitivos onde o documento é a chave e o link de acesso ao conteudo no SIECM é o valor
        Map<Documento, String> documentosDefinitivos = this.documentoServico.listDocumentosDefinitivosDossieDigital(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa());

        // ***** Passo 4 - Capturar a autorização
        return this.verificaDisponibilidadeAutorizacao(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), produtoSolicitado, documentosDefinitivos);
    }

    /**
     * Captura a autorização concedida pelo codigo de autorização informado na etapa anterior
     *
     * @param autorizacao Numero da autorização a ser localizado
     * @param vinculacoesDocumentos Indicador se as vinculações relativas aos documentos utilizados na autorização devem ser carregados
     * @param vinculacoesOrientacoes Indicador se as vinculações relativas as orientações fornecidas junto com a autorização devem ser carregados
     * @return Objeto que representa a autorizaçao
     */
    public Autorizacao getByCodigoAutorizacao(final Long autorizacao, final boolean vinculacoesDocumentos, final boolean vinculacoesOrientacoes) {
        final StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT a FROM Autorizacao a ");
        if (vinculacoesDocumentos) {
            jpql.append(" LEFT JOIN FETCH a.documentosAutorizacao ");
        }
        if (vinculacoesOrientacoes) {
            jpql.append(" LEFT JOIN FETCH a.autorizacaoOrientacoes ");
        }
        jpql.append(" WHERE a.codigoNSU = :codigoNSU ");

        try {
            TypedQuery<Autorizacao> query = this.entityManager.createQuery(jpql.toString(), Autorizacao.class);
            query.setParameter(ConstantesUtil.KEY_CODIGO_NSU, autorizacao);

            return query.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public RetornoAutorizacaoConjuntaDTO setAutorizacaoConjunta(List<Long> codigosAutorizacao, Integer operacao, Integer modalidade) {
        RetornoAutorizacaoConjuntaDTO autorizacaoConjuntaRetornoDTO = new RetornoAutorizacaoConjuntaDTO();
        if ((Objects.isNull(codigosAutorizacao)) || (codigosAutorizacao.size() < 2)) {
            String mensagem = MessageFormat.format("Necessário informar pelo menos dois codigos de autorização para solicitação de autorização conjunta. Operação = {0}, Modalidade = {1} ", operacao, modalidade);
            throw new SimtrRequisicaoException(mensagem, null);
        }
        Produto produto = this.produtoServico.getByOperacaoModalidade(operacao, modalidade, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (Objects.isNull(produto)) {
            String mensagem = MessageFormat.format("Produto não localido sob parametros informados. Operação = {0}, Modalidade = {1} ", operacao, modalidade);
            throw new SimtrRequisicaoException(mensagem, null);
        }
        // Localiza as autorizações individuais baseado nos codigos encaminhados
        // e mapeia os codigos que não possuem autorização ou que a autorização localizada não possui relação com a operção e modalidade indicados.
        List<Autorizacao> listaAutorizacoesIndividuais = new ArrayList<>();
        List<Long> codigosAutorizacaoNaoValidos = new ArrayList<>();
        codigosAutorizacao.forEach((codigoAutorizacao) -> {
            Autorizacao autorizacao = this.getByCodigoAutorizacao(codigoAutorizacao, Boolean.TRUE, Boolean.FALSE);
            if ((Objects.isNull(autorizacao)) || ((!autorizacao.getProdutoOperacao().equals(operacao)) || (!autorizacao.getProdutoModalidade().equals(modalidade)))) {
                codigosAutorizacaoNaoValidos.add(codigoAutorizacao);
            } else {
                listaAutorizacoesIndividuais.add(autorizacao);
            }
        });
        // Para o fluxo e retorna indicação de não autorização para os casos de codigo de autorização indivisual não concedida.
        if (!codigosAutorizacaoNaoValidos.isEmpty()) {
            String mensagem = MessageFormat.format("Código(s) de autorização não válido(s) para operação solicitada: {0} ", codigosAutorizacaoNaoValidos.toString());
            throw new SimtrRequisicaoException(mensagem, null);
        }
        Long codigoAutorizacaoConjunta = createNSUAutorizacao();
        listaAutorizacoesIndividuais.forEach(autorizacao -> autorizacao.setCodigoNSUConjunta(codigoAutorizacaoConjunta));
        
        // Percorre a lista de autorizações individuais localizadas e cria a pasta de transação no SIECM para cada uma de forma a vincular os documentos de cada cliente
        listaAutorizacoesIndividuais.forEach((aut) -> {
            this.siecmServico.criaTransacaoClienteSIECM(aut.getCpfCnpj(), aut.getTipoPessoa(), codigoAutorizacaoConjunta.toString(), ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);

            List<String> idsDocumentos = aut.getDocumentosAutorizacao().stream().map(d -> d.getCodigoDocumentoGED()).collect(Collectors.toList());
            this.siecmServico.vinculaDocumentosTransacaoSIECM(aut.getCpfCnpj(), aut.getTipoPessoa(), codigoAutorizacaoConjunta.toString(), idsDocumentos);
        });
        autorizacaoConjuntaRetornoDTO.setAutorizacao(codigoAutorizacaoConjunta);
        autorizacaoConjuntaRetornoDTO.setAutorizado(true);
        autorizacaoConjuntaRetornoDTO.setProdutoLocalizado(true);
        autorizacaoConjuntaRetornoDTO.setOperacao(operacao);
        autorizacaoConjuntaRetornoDTO.setModalidade(modalidade);
        autorizacaoConjuntaRetornoDTO.setNomeProduto(produto.getNome());
        return autorizacaoConjuntaRetornoDTO;
    }

    public void finalizaOperacao(Autorizacao autorizacao) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" UPDATE Autorizacao a SET a.dataHoraConclusao = :dataHora WHERE a.id = :id ");
        Query query = this.entityManager.createQuery(jpql.toString());
        query.setParameter("id", autorizacao.getId());
        query.setParameter(ConstantesUtil.KEY_DATA_HORA, Calendar.getInstance());
        query.executeUpdate();
    }
    
    // *********** Métodos Privados ***********

    /**
     * Metodo utilizado para realizar a avaliação da concessão de uma autorização documental baseado na lista de doumentos apresentada para o produto informado.
     *
     * @param cpfCnpj Numero do CPF/CNPJ utilizado na solicitação de autorização
     * @param tipoPessoaEnum Identificação do tipo de pessoa (Fisica ou Juridica)
     * @param produtoSolicitado Produto identificado na solicitação da autorização
     * @param mapaDocumentosCliente Lista de documentos localizados no dossiê do cliente junto a solução de ECM asociado aos links localizados na checagem do documento no momento de capturar os
     *        documentos validos.
     * @param sistemaSolicitante Sigla do sistema solicitante da autorização
     * @return Objeto contendo a autorizacao concedida com a lista de documentos utilizados na concessão
     * @throws DossieAutorizacaoException Lançada quando existe algum impedimento para gerar a autorização identificando as causas do impedimento apresentado
     */
    private AutorizacaoVO requestAutorizacao(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, Produto produtoSolicitado, Map<Documento, String> mapaDocumentosCliente, String sistemaSolicitante, SipesResponseDTO sipesResponseDTO) {
        List<AutorizacaoDocumentoVO> documentosUtilizados = new ArrayList<>();
        List<RegraDocumental> regrasNaoAtendidas = new ArrayList<>();
        List<String> documentosAusentes = new ArrayList<>();
        Set<Documento> documentosCliente = mapaDocumentosCliente.keySet();

        List<ComposicaoDocumental> composicoesDocumentais = this.composicaoDocumentalService.getComposicoesByProduto(produtoSolicitado.getOperacao(), produtoSolicitado.getModalidade(), Boolean.FALSE);

        // Caso não sejam localizadas composições retorna indicação da negativa
        if (composicoesDocumentais.isEmpty()) {
            throw new DossieAutorizacaoException("Não foram localizadas composições documentais para o produto solicitado", null, sipesResponseDTO, cpfCnpj, tipoPessoaEnum, produtoSolicitado, sistemaSolicitante, Boolean.FALSE);
        }

        // Percorre todas as composições localizadas para o produto solicitado
        for (ComposicaoDocumental composicaoDocumental : composicoesDocumentais) {
            regrasNaoAtendidas.clear();
            Set<RegraDocumental> regrasDocumentais = composicaoDocumental.getRegrasDocumentais();
            // Percorre todas as regras localizadas para cada composição
            for (RegraDocumental regraDocumental : regrasDocumentais) {
                // Regra atende por função documental ou tipo especifico?
                if (Objects.nonNull(regraDocumental.getFuncaoDocumental())) {
                    FuncaoDocumental funcaoDocumental = regraDocumental.getFuncaoDocumental();
                    // Percorre documentos do cliente em busca de um que atenda a função definida na regra
                    boolean documentoLocalizado = false;
                    for (Documento documentoCliente : documentosCliente) {
                        Set<TipoDocumento> tiposDocumento = funcaoDocumental.getTiposDocumento();
                        for (TipoDocumento tipoDocumento : tiposDocumento) {

                            if (tipoDocumento.equals(documentoCliente.getTipoDocumento())) {
                                String link = mapaDocumentosCliente.get(documentoCliente);
                                AutorizacaoDocumentoVO documentoUtilizado = new AutorizacaoDocumentoVO();
                                documentoUtilizado.setDocumento(documentoCliente);
                                documentoUtilizado.setLink(link);
                                documentoUtilizado.setFinalidade(funcaoDocumental.getNome());
                                documentosUtilizados.add(documentoUtilizado);
                                documentoLocalizado = true;
                                break;
                            }
                        }
                        if (documentoLocalizado) {
                            break;
                        }
                    }
                    // Caso não localize nenhum documento marca regra não atendida e a função documental pendente para retorno.
                    if (!documentoLocalizado) {
                        regrasNaoAtendidas.add(regraDocumental);
                        documentosAusentes.add(funcaoDocumental.getNome());
                    }
                } else {
                    TipoDocumento tipoDocumento = regraDocumental.getTipoDocumento();
                    // Percorre documentos do cliente em busca de um que seja do tipo de documento definido na regra
                    boolean documentoLocalizado = false;
                    for (Documento documentoCliente : documentosCliente) {
                        if (tipoDocumento.equals(documentoCliente.getTipoDocumento())) {
                            String link = mapaDocumentosCliente.get(documentoCliente);
                            AutorizacaoDocumentoVO documentoUtilizado = new AutorizacaoDocumentoVO();
                            documentoUtilizado.setDocumento(documentoCliente);
                            documentoUtilizado.setLink(link);
                            documentoUtilizado.setFinalidade(tipoDocumento.getNome());
                            documentosUtilizados.add(documentoUtilizado);
                            documentoLocalizado = true;
                            break;
                        }
                    }
                    // Caso não localize marca regra não atendida e a função documental pendente para retorno.
                    if (!documentoLocalizado) {
                        regrasNaoAtendidas.add(regraDocumental);
                        documentosAusentes.add(tipoDocumento.getNome());
                    }
                }
            }
            // Se todas as regras da composição foram atendidas, a coleção estara vazia, logo não precisa analisar a proxima composição
            if (documentosAusentes.isEmpty()) {
                break;
            }
        }

        if (!regrasNaoAtendidas.isEmpty()) {
            DossieAutorizacaoException dossieAutorizacaoException = new DossieAutorizacaoException("Não foram identificados documentos suficientes para esta operação.", null, sipesResponseDTO, cpfCnpj, tipoPessoaEnum, produtoSolicitado, sistemaSolicitante, Boolean.TRUE);
            dossieAutorizacaoException.addDocumentosAusentes(documentosAusentes.toArray(new String[documentosAusentes.size()]));
            dossieAutorizacaoException.addDocumentosUtilizados(documentosUtilizados.toArray(new AutorizacaoDocumentoVO[documentosUtilizados.size()]));
            throw dossieAutorizacaoException;
        }
        Autorizacao autorizacao = this.createAutorizacao(cpfCnpj, tipoPessoaEnum, produtoSolicitado, documentosUtilizados, sistemaSolicitante);

        return new AutorizacaoVO(autorizacao, documentosUtilizados);

    }

    /**
     * Metodo utilizado para realizar a avaliação da concessão de uma autorização documental baseado na lista de doumentos apresentada para o produto informado.
     *
     * @param cpfCnpj Numero do CPF/CNPJ utilizado na solicitação de autorização
     * @param tipoPessoaEnum Identificação do tipo de pessoa (Fisica ou Juridica)
     * @param produtoSolicitado Produto identificado na solicitação da autorização
     * @param mapaDocumentosCliente Lista de documentos localizados no dossiê do cliente junto a solução de ECM asociado aos links localizados na checagem do documento no momento de capturar os
     *        documentos validos.
     * @param sistemaSolicitante Sigla do sistema solicitante da autorização
     * @return Objeto contendo a autorizacao concedida com a lista de documentos utilizados na concessão
     * @throws DossieAutorizacaoException Lançada quando existe algum impedimento para gerar a autorização identificando as causas do impedimento apresentado
     */
    private List<AnaliseRegraDTO> verificaDisponibilidadeAutorizacao(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, Produto produtoSolicitado, Map<Documento, String> mapaDocumentosCliente) {
        List<AnaliseRegraDTO> tipologiaAnalisada = new ArrayList<>();
        List<RegraDocumental> regrasNaoAtendidas = new ArrayList<>();

        Set<Documento> documentosCliente = mapaDocumentosCliente.keySet();

        List<ComposicaoDocumental> composicoesDocumentais = this.composicaoDocumentalService.getComposicoesByProduto(produtoSolicitado.getOperacao(), produtoSolicitado.getModalidade(), Boolean.FALSE);

        // Caso não sejam localizadas composições retorna indicação da negativa
        if (composicoesDocumentais.isEmpty()) {
            throw new SimtrConfiguracaoException("Não foram localizadas composições documentais associadas ao produto indicado.");
        }

        // Percorre todas as composições localizadas para o produto solicitado
        for (ComposicaoDocumental composicaoDocumental : composicoesDocumentais) {
            regrasNaoAtendidas.clear();
            Set<RegraDocumental> regrasDocumentais = composicaoDocumental.getRegrasDocumentais();

            // Percorre todas as regras localizadas para cada composição
            for (RegraDocumental regraDocumental : regrasDocumentais) {
                // Cria o objetoque representa a analise da função documental definida na regra da composição.
                AnaliseRegraDTO documentoAnalisado = new AnaliseRegraDTO();
                documentoAnalisado.setIdentificadorComposicao(composicaoDocumental.getId());
                documentoAnalisado.setDossieDigital(Boolean.FALSE);

                // Regra atende por função documental ou tipo especifico?
                if (Objects.nonNull(regraDocumental.getFuncaoDocumental())) {
                    FuncaoDocumental funcaoDocumental = regraDocumental.getFuncaoDocumental();

                    documentoAnalisado.setFuncaoDocumental(funcaoDocumental.getNome());

                    // Percorre a lista de tipos vinculada a função documental para verificar se algum documento do cliente atende a necessidade definida.
                    Set<TipoDocumento> tiposDocumento = funcaoDocumental.getTiposDocumento();
                    for (TipoDocumento tipoDocumento : tiposDocumento) {

                        // Percorre documentos do cliente em busca de um que atenda a função definida na regra
                        for (Documento documentoCliente : documentosCliente) {
                            if (tipoDocumento.equals(documentoCliente.getTipoDocumento())) {
                                documentoAnalisado.setDossieDigital(Boolean.TRUE);
                                break;
                            }
                        }
                        if (!documentoAnalisado.isDossieDigital()) {
                            regrasNaoAtendidas.add(regraDocumental);
                        }
                    }
                } else {
                    TipoDocumento tipoDocumento = regraDocumental.getTipoDocumento();

                    documentoAnalisado.setTipoDocumento(tipoDocumento.getNome());

                    // Percorre documentos do cliente em busca de um que seja do tipo de documento definido na regra
                    for (Documento documentoCliente : documentosCliente) {
                        if (tipoDocumento.equals(documentoCliente.getTipoDocumento())) {
                            documentoAnalisado.setDossieDigital(Boolean.TRUE);
                            break;
                        }
                    }
                    if (!documentoAnalisado.isDossieDigital()) {
                        regrasNaoAtendidas.add(regraDocumental);
                    }
                }
                tipologiaAnalisada.add(documentoAnalisado);
            }
            // Se todas as regras da composição foram atendidas, a coleção estara vazia, logo não precisa analisar a proxima composição
            // Remove da tipologia todos os documentos por ventura vinculados, mas pertencentes a outras composições não atendida e retorna a coleção.
            if (regrasNaoAtendidas.isEmpty()) {
                tipologiaAnalisada.removeIf(d -> !composicaoDocumental.getId().equals(d.getIdentificadorComposicao()));
                return tipologiaAnalisada;
            }
        }
        // Retorna a coleção com a representalçao de todas as regras (tipos ou funções) indicadas se atentidas ou não e agrupas pelo identificador da composição.
        return tipologiaAnalisada;
    }

    /**
     * Método utilizado para invalidar a autorização persistida por falha na comunicação com o ECM nas etapas subsequentes do processo de solicitação de autorização
     *
     * @param codigoAutorizacao Numero do codigo de autorização a ser cancelado
     * @param mensagemFalha Mensagem com a descriação a ser aplicada na identificação do cancelamento
     */
    private void invalidaPorFalhaECM(Long codigoAutorizacao, String mensagemFalha) {
        mensagemFalha = mensagemFalha.length() > 90 ? "FalhaECM: " + mensagemFalha.substring(0, 90) : "FalhaECM: " + mensagemFalha;

        StringBuilder jpql = new StringBuilder();
        jpql.append(" UPDATE Autorizacao SET dataHoraInformeECM = :dataHora, protocoloECM = :protocoloECM WHERE codigoNSU = :codigoNSU ");

        Query query = this.entityManager.createQuery(jpql.toString());
        query.setParameter(ConstantesUtil.KEY_CODIGO_NSU, codigoAutorizacao);
        query.setParameter("protocoloECM", mensagemFalha);
        query.setParameter(ConstantesUtil.KEY_DATA_HORA, Calendar.getInstance(), TemporalType.TIMESTAMP);

        query.executeUpdate();
    }

    /**
     * Metodo utilizado para criar a instancia da entidade Autorizacao a partir de um conjunto de informações minimas necessarias e persistir na base de dados
     *
     * @param cpfCnpj Numero do CPF/CNPJ utilizado na solicitação de autorização
     * @param tipoPessoaEnum Identificação do tipo de pessoa (Fisica ou Juridica)
     * @param produto Produto identificado na solicitação da autorização
     * @param documentosUtilizados Lista de documentos utilizados na concessão da autorização
     * @param sistemaSolicitante Sigla do sistema solicitante da autorização
     * @return Prototipo do objeto que representa a Autorizacao com as informações iniciais para complementação e persistencia.
     */
    private Autorizacao createAutorizacao(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, Produto produto, List<AutorizacaoDocumentoVO> documentosUtilizados, String sistemaSolicitante) {
        Autorizacao autorizacao = new Autorizacao();
        autorizacao.setCpfCnpj(cpfCnpj);
        autorizacao.setTipoPessoa(tipoPessoaEnum);
        autorizacao.setCodigoNSU(this.createNSUAutorizacao());
        autorizacao.setDataHoraRegistro(Calendar.getInstance());
        autorizacao.setProdutoOperacao(produto.getOperacao());
        autorizacao.setProdutoModalidade(produto.getModalidade());
        autorizacao.setProdutoNome(produto.getNome());
        autorizacao.setSiglaCanalSolicitacao(sistemaSolicitante);

        documentosUtilizados.stream()
                            .map(documentoUtilizado -> {
                                DocumentoAutorizacao documentoAutorizacao = new DocumentoAutorizacao();
                                documentoAutorizacao.setAutorizacao(autorizacao);
                                documentoAutorizacao.setCodigoDocumentoGED(documentoUtilizado.getDocumento().getCodigoGED());
                                documentoAutorizacao.setFinalidade(documentoUtilizado.getFinalidade());
                                return documentoAutorizacao;
                            })
                            .forEachOrdered(documentoAutorizacao -> autorizacao.addDocumentosAutorizacao(documentoAutorizacao));

        this.orientacoes.get().stream().map(mensagemOrientacao -> {
            AutorizacaoOrientacao autorizacaoOrientacao = new AutorizacaoOrientacao();
            autorizacaoOrientacao.setAutorizacao(autorizacao);
            autorizacaoOrientacao.setSistemaPesquisa(mensagemOrientacao.getSistemaPesquisa());
            autorizacaoOrientacao.setDescricaoOrientacao(mensagemOrientacao.getMensagemOrientacao());
            return autorizacaoOrientacao;
        }).forEachOrdered(autorizacaoOrientacao -> autorizacao.addAutorizacaoOrientacoes(autorizacaoOrientacao));

        this.save(autorizacao);
        return autorizacao;
    }

    /**
     * Método responsável por criar um código baseado em data e hora para respresentar a autorização concedida
     *
     * @return Código gerado para representar a autorização concedida.
     */
    private Long createNSUAutorizacao() {
        return Calendar.getInstance().getTimeInMillis() * Thread.currentThread().getId();
    }

    /**
     * Metodo utilizado para criar um registro junto ao dossiê do cliente referente a consulta cadastral e gravar o documento de consulta cadastral junto ao SIECM
     *
     * @param cpfCnpj Numero do CPF/CNPJ utilizado na solicitação de autorização
     * @param tipoPessoaEnum Identificação do tipo de pessoa (Fisica ou Juridica)
     * @param codigoAutorizacao Codigo de autorização fornecido para compor o nome da pasta a ser armazenado o documento
     * @param produtoSolicitado Produto identificado na solicitação da autorização
     * @param canal Canal de comunicação identificado pelo codigo de integração na solicitação de autorização
     * @param retornoPessoasFisicasDTO Objeto que representa a resposta da consulta realizada junto ao SICPF
     * @param sipesResponseDTO Objeto que representa a resposta da consulta realizada junto ao SIPES
     * @throws DossieAutorizacaoException Lançada quando houve falha na comunicação com a soluução de ECM indicando o motivo apresentado
     */
    private Documento gravaDocumentoConsultaCadastral(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, Long codigoAutorizacao, Produto produtoSolicitado, Canal canal, RetornoPessoasFisicasDTO retornoPessoasFisicasDTO, SipesResponseDTO sipesResponseDTO) {
        // Obtem o objeto que representa o tipo de documento das consultas cadastrais
        TipoDocumento tipoDocumentoConsultaCadastral = this.tipoDocumentoServico.getByTipologia(ConstantesUtil.TIPOLOGIA_CONSULTA_CADASTRAL);

        // Cria a lista de atributos adicionais necessarios ao documento de pesquisas cadastrais
        Map<String, String> atributos = new HashMap<>();
        
        // Formata o CPF/CNPJ do cliente para utilizar nas operações do SIECM
        String cpfCnpjFormatado = "";
        if (TipoPessoaEnum.F.equals(tipoPessoaEnum)) {
            cpfCnpjFormatado = StringUtils.leftPad(cpfCnpj.toString(), 11, '0');
            atributos.put("cpf", cpfCnpjFormatado);
        } else if (TipoPessoaEnum.J.equals(tipoPessoaEnum)) {
            cpfCnpjFormatado = StringUtils.leftPad(cpfCnpj.toString(), 14, '0');
            atributos.put("cnpj", cpfCnpjFormatado);
        }

        byte[] relatorioPesquisas;
        try {
            // Gera o documento que representa o resultado das consultas cadastrais
            String minuta = "dossiedigital/".concat(tipoDocumentoConsultaCadastral.getNomeArquivoMinuta());
            relatorioPesquisas = this.createDocumentoPesquisasCadastrais(cpfCnpj, retornoPessoasFisicasDTO, minuta, this.orientacoes.get());
        } catch (Exception e) {
            throw new DossieAutorizacaoException("AS.gDCC.01 - Falha ao gerar relatorio das consultas cadastrais", e, sipesResponseDTO, cpfCnpj, tipoPessoaEnum, produtoSolicitado, canal.getSigla(), Boolean.FALSE);
        }

        // Calcula a data de validade para o documento
        final Calendar dataValidade = Calendar.getInstance();
        dataValidade.add(Calendar.DAY_OF_MONTH, tipoDocumentoConsultaCadastral.getPrazoValidade());

        // Utiliza o código da autorização fornecida como nomeclatura do diretorio para armazenamento do documento
        final String folder = codigoAutorizacao.toString();

        // Converte o relatorio gerado em formato Base64 para armazenamento
        String sicpfBase64 = new String(Base64.getEncoder().encode(relatorioPesquisas));

        // Constroi o prototipo do documento para armazenamento junto ao SIMTR e atribui a data de validade do mesmo
        Documento documentoConsultaCadastralSIMTR = documentoServico.prototype(canal, Boolean.TRUE, tipoDocumentoConsultaCadastral, TemporalidadeDocumentoEnum.VALIDO, OrigemDocumentoEnum.O, FormatoConteudoEnum.PDF, atributos, sicpfBase64);
        documentoConsultaCadastralSIMTR.setDataHoraValidade(dataValidade);

        try {
            // Realiza o armazenamento do documento junto do SIECM e atribui o identificador retornado ao registro do documentoarmazenado
            String identificadorSiecm = this.siecmServico.armazenaDocumentoOperacaoSIECM(documentoConsultaCadastralSIMTR, TemporalidadeDocumentoEnum.VALIDO, sicpfBase64, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL, folder);
            documentoConsultaCadastralSIMTR.setCodigoGED(identificadorSiecm);
        } catch (EJBException ee) {
            String mensagem = MessageFormat.format("AS.gDCC.02 - Falha ao gravar arquivo das consultas cadastrais no GED: {0}", ee.getLocalizedMessage());
            throw new DossieAutorizacaoException(mensagem, null, sipesResponseDTO, cpfCnpj, tipoPessoaEnum, produtoSolicitado, canal.getSigla(), Boolean.FALSE);
        }

        // Realiza o armazenamento do documento na base de dados do SIMTR
        this.documentoServico.save(documentoConsultaCadastralSIMTR);

        return documentoConsultaCadastralSIMTR;
    }

    private byte[] createDocumentoPesquisasCadastrais(Long cpf, RetornoPessoasFisicasDTO retornoPessoasFisicasDTO, String minuta, List<MensagemOrientacaoVO> orientacoes) throws Exception {
        String cpfFormatado = StringUtils.leftPad(String.valueOf(cpf), ConstantesUtil.TAMANHO_CPF, '0');
        Map<String, Object> dados = new HashMap<>();

        // Define o atributo do CPF do cliente
        dados.put("cpf", cpfFormatado);

        // Define o objeto que representa o retorno da consulta ao SICPF do cliente
        Map<String, Object> dadosSICPF = new HashMap<>();
        if (Objects.nonNull(retornoPessoasFisicasDTO)) {
            dadosSICPF.put("nome", retornoPessoasFisicasDTO.getNomeContribuinte());
            dadosSICPF.put("nascimento", this.calendarUtil.toString(retornoPessoasFisicasDTO.getDataNascimento(), "dd/MM/yyyy"));
            dadosSICPF.put("mae", retornoPessoasFisicasDTO.getNomeMae());
            dadosSICPF.put("titulo_eleitor", retornoPessoasFisicasDTO.getTituloEleitor());

            if (SistemaPesquisaTipoRetornoEnum.SICPF_0.getNumeroTipoPesquisa().equals(Integer.valueOf(retornoPessoasFisicasDTO.getCodigoSituacaoCpf()))) {
                dadosSICPF.put(ConstantesUtil.KEY_SITUACAO, SistemaPesquisaTipoRetornoEnum.SICPF_0.getDescricaoOcorrencia().toUpperCase());
            } else if (SistemaPesquisaTipoRetornoEnum.SICPF_1.getNumeroTipoPesquisa().equals(Integer.valueOf(retornoPessoasFisicasDTO.getCodigoSituacaoCpf()))) {
                dadosSICPF.put(ConstantesUtil.KEY_SITUACAO, SistemaPesquisaTipoRetornoEnum.SICPF_1.getDescricaoOcorrencia().toUpperCase());
            } else if (SistemaPesquisaTipoRetornoEnum.SICPF_2.getNumeroTipoPesquisa().equals(Integer.valueOf(retornoPessoasFisicasDTO.getCodigoSituacaoCpf()))) {
                dadosSICPF.put(ConstantesUtil.KEY_SITUACAO, SistemaPesquisaTipoRetornoEnum.SICPF_2.getDescricaoOcorrencia().toUpperCase());
            } else if (SistemaPesquisaTipoRetornoEnum.SICPF_3.getNumeroTipoPesquisa().equals(Integer.valueOf(retornoPessoasFisicasDTO.getCodigoSituacaoCpf()))) {
                dadosSICPF.put(ConstantesUtil.KEY_SITUACAO, SistemaPesquisaTipoRetornoEnum.SICPF_3.getDescricaoOcorrencia().toUpperCase());
            } else if (SistemaPesquisaTipoRetornoEnum.SICPF_4.getNumeroTipoPesquisa().equals(Integer.valueOf(retornoPessoasFisicasDTO.getCodigoSituacaoCpf()))) {
                dadosSICPF.put(ConstantesUtil.KEY_SITUACAO, SistemaPesquisaTipoRetornoEnum.SICPF_4.getDescricaoOcorrencia().toUpperCase());
            } else if (SistemaPesquisaTipoRetornoEnum.SICPF_5.getNumeroTipoPesquisa().equals(Integer.valueOf(retornoPessoasFisicasDTO.getCodigoSituacaoCpf()))) {
                dadosSICPF.put(ConstantesUtil.KEY_SITUACAO, SistemaPesquisaTipoRetornoEnum.SICPF_5.getDescricaoOcorrencia().toUpperCase());
            } else if (SistemaPesquisaTipoRetornoEnum.SICPF_8.getNumeroTipoPesquisa().equals(Integer.valueOf(retornoPessoasFisicasDTO.getCodigoSituacaoCpf()))) {
                dadosSICPF.put(ConstantesUtil.KEY_SITUACAO, SistemaPesquisaTipoRetornoEnum.SICPF_8.getDescricaoOcorrencia().toUpperCase());
            } else if (SistemaPesquisaTipoRetornoEnum.SICPF_9.getNumeroTipoPesquisa().equals(Integer.valueOf(retornoPessoasFisicasDTO.getCodigoSituacaoCpf()))) {
                dadosSICPF.put(ConstantesUtil.KEY_SITUACAO, SistemaPesquisaTipoRetornoEnum.SICPF_9.getDescricaoOcorrencia().toUpperCase());
            }
        }
        dados.put("sicpf", dadosSICPF);

        boolean cadin = Boolean.FALSE;
        boolean serasa = Boolean.FALSE;
        boolean siccf = Boolean.FALSE;
        boolean sicow = Boolean.FALSE;
        boolean sinad = Boolean.FALSE;
        boolean spc = Boolean.FALSE;

        List<Map<String, Object>> dadosSIPES = new ArrayList<>();
        for (MensagemOrientacaoVO mensagemOrientacaoVO : orientacoes) {
            if (SistemaPesquisaEnum.CADIN.equals(mensagemOrientacaoVO.getSistemaPesquisa())) {
                cadin = Boolean.TRUE;
            }
            if (SistemaPesquisaEnum.SERASA.equals(mensagemOrientacaoVO.getSistemaPesquisa())) {
                serasa = Boolean.TRUE;
            }
            if (SistemaPesquisaEnum.SICCF.equals(mensagemOrientacaoVO.getSistemaPesquisa())) {
                siccf = Boolean.TRUE;
            }
            if (SistemaPesquisaEnum.SICOW.equals(mensagemOrientacaoVO.getSistemaPesquisa())) {
                sicow = Boolean.TRUE;
            }
            if (SistemaPesquisaEnum.SINAD.equals(mensagemOrientacaoVO.getSistemaPesquisa())) {
                sinad = Boolean.TRUE;
            }
            if (SistemaPesquisaEnum.SPC.equals(mensagemOrientacaoVO.getSistemaPesquisa())) {
                spc = Boolean.TRUE;
            }
            if (SistemaPesquisaEnum.RECEITA.equals(mensagemOrientacaoVO.getSistemaPesquisa())) {
                continue;
            }
            Map<String, Object> mapaOcorrencia = new HashMap<>();
            mapaOcorrencia.put(ConstantesUtil.KEY_SISTEMA, mensagemOrientacaoVO.getSistemaPesquisa().name());
            String ocorrencia = mensagemOrientacaoVO.getGrupoOcorrencia() == null
                                                                                  ? mensagemOrientacaoVO.getMensagemOrientacao() : mensagemOrientacaoVO.getGrupoOcorrencia().toUpperCase();
            mapaOcorrencia.put(ConstantesUtil.KEY_OCORRENCIA, ocorrencia);
            dadosSIPES.add(mapaOcorrencia);
        }
        if (!cadin) {
            Map<String, Object> mapaOcorrencia = new HashMap<>();
            mapaOcorrencia.put(ConstantesUtil.KEY_SISTEMA, SistemaPesquisaEnum.CADIN.name());
            mapaOcorrencia.put(ConstantesUtil.KEY_OCORRENCIA, ConstantesUtil.KEY_NADA_CONSTA);
            dadosSIPES.add(mapaOcorrencia);
        }
        if (!serasa) {
            Map<String, Object> mapaOcorrencia = new HashMap<>();
            mapaOcorrencia.put(ConstantesUtil.KEY_SISTEMA, SistemaPesquisaEnum.SERASA.name());
            mapaOcorrencia.put(ConstantesUtil.KEY_OCORRENCIA, ConstantesUtil.KEY_NADA_CONSTA);
            dadosSIPES.add(mapaOcorrencia);
        }
        if (!siccf) {
            Map<String, Object> mapaOcorrencia = new HashMap<>();
            mapaOcorrencia.put(ConstantesUtil.KEY_SISTEMA, SistemaPesquisaEnum.SICCF.name());
            mapaOcorrencia.put(ConstantesUtil.KEY_OCORRENCIA, ConstantesUtil.KEY_NADA_CONSTA);
            dadosSIPES.add(mapaOcorrencia);
        }
        if (!sicow) {
            Map<String, Object> mapaOcorrencia = new HashMap<>();
            mapaOcorrencia.put(ConstantesUtil.KEY_SISTEMA, SistemaPesquisaEnum.SICOW.name());
            mapaOcorrencia.put(ConstantesUtil.KEY_OCORRENCIA, ConstantesUtil.KEY_NADA_CONSTA);
            dadosSIPES.add(mapaOcorrencia);
        }
        if (!sinad) {
            Map<String, Object> mapaOcorrencia = new HashMap<>();
            mapaOcorrencia.put(ConstantesUtil.KEY_SISTEMA, SistemaPesquisaEnum.SINAD.name());
            mapaOcorrencia.put(ConstantesUtil.KEY_OCORRENCIA, ConstantesUtil.KEY_NADA_CONSTA);
            dadosSIPES.add(mapaOcorrencia);
        }
        if (!spc) {
            Map<String, Object> mapaOcorrencia = new HashMap<>();
            mapaOcorrencia.put(ConstantesUtil.KEY_SISTEMA, SistemaPesquisaEnum.SPC.name());
            mapaOcorrencia.put(ConstantesUtil.KEY_OCORRENCIA, ConstantesUtil.KEY_NADA_CONSTA);
            dadosSIPES.add(mapaOcorrencia);
        }

        dados.put("sipes", dadosSIPES);

        String json = UtilJson.converterParaJson(dados);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("LOGO_CAIXA", ConstantesUtil.RELATORIO_CAMINHO_IMAGENS.concat("caixa.png"));

        // Retorna o relatório.
        return this.relatorioServico.gerarRelatorioPDFJsonDataSource(minuta, json, parametros);
    }

}
