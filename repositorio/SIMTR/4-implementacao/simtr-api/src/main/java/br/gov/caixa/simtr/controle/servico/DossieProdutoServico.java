package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.simtr.controle.excecao.BpmException;
import br.gov.caixa.simtr.controle.excecao.SimtrPermissaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.helper.DossieProdutoValidacaoHelper;
import br.gov.caixa.simtr.controle.servico.helper.DossieProdutoVinculosHelper;
import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.ChecklistAssociado;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.DossieClienteProduto;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.Garantia;
import br.gov.caixa.simtr.modelo.entidade.GarantiaInformada;
import br.gov.caixa.simtr.modelo.entidade.Parecer;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.ProcessoFaseDossie;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.entidade.ProdutoDossie;
import br.gov.caixa.simtr.modelo.entidade.RespostaDossie;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDossie;
import br.gov.caixa.simtr.modelo.entidade.SituacaoInstanciaDocumento;
import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;
import br.gov.caixa.simtr.modelo.entidade.TipoSituacaoDossie;
import br.gov.caixa.simtr.modelo.entidade.UnidadeTratamento;
import br.gov.caixa.simtr.modelo.entidade.UnidadeTratamentoId;
import br.gov.caixa.simtr.modelo.entidade.Verificacao;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDossieEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.DossieProdutoAlteracaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.DossieProdutoInclusaoDTO;
import br.gov.caixa.simtr.util.CalendarUtil;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class DossieProdutoServico extends AbstractService<DossieProduto, Long> {

    @EJB
    private DossieProdutoServico _self;

    @EJB
    private DossieProdutoValidacaoHelper dossieProdutoValidacaoHelper;

    @EJB
    private DossieProdutoVinculosHelper dossieProdutoVinculosHelper;

    ////////////////////////////////////////////////

    @EJB
    private BPMServico bpmServico;

    @EJB
    CampoFormularioServico campoFormularioServico;

    @EJB
    private CanalServico canalServico;

    @EJB
    private DossieClienteServico dossieClienteServico;

    @EJB
    private GarantiaServico garantiaServico;
    
    @EJB
    private SiecmServico siecmServico;
    
    @EJB
    private GarantiaInformadaServico garantiaInformadaServico;

    @EJB
    private KeycloakService keycloakService;

    @EJB
    private ProdutoServico produtoServico;

    @EJB
    private ProdutoDossieServico produtoDossieServico;

    @EJB
    private ProcessoServico processoServico;

    @EJB
    private SituacaoDossieServico situacaoDossieServico;

    @EJB
    private TipoRelacionamentoServico tipoRelacionamentoServico;

    @Resource
    private SessionContext sessionContext;

    @Inject
    CalendarUtil calendarUtil;

    @Inject
    private EntityManager entityManager;
    
    private static final Logger LOGGER = Logger.getLogger(DossieProdutoServico.class.getName());

    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Utilizado para realizar a captura de um Dossiê de Produto baseado no ID fornecido. Sempre carrega as informações de:
     * <ul>
     * <li>Processo</li>
     * <li>ProcessoFase</li>
     * <li>Situações</li>
     * <li>Unidades de Tratamento</li>
     * </ul>
     *
     * @param id Identificador do dossiê de produto a ser localizado.
     * @param vinculacoesDossieCliente Indicador se os vinculos relativos aos dosiês de cliente devem ser carregados
     * @param vinculacoesProdutosContratados Indicador se os vinculos relativos aos produtos contratados devem ser carregados
     * @param vinculacoesDocumentos Indicador se os vinculos relativos as instancias de documento e relacionados com o dossiê de cliente devem ser carregados
     * @param vinculacoesGarantias Indicador se os vinculos relativos as garantias informadas pelo dossiê devem ser carregadas
     * @param vinculacoesFormulario Indicador se os vinculos relativos as respostas de formulario concedidas devem ser carregados
     * @param vinculacoesChecklist Indicador se os vinculos relativos as verificações realizadas nas etapas de tratamento devem ser carregados
     * @return Dossiê de Produto carregado conforme parametros indicados ou null caso o mesmo não seja localizado.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTO,
        ConstantesUtil.PERFIL_MTRSDNTTG
    })
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public DossieProduto getById(Long id, boolean vinculacoesDossieCliente, boolean vinculacoesProdutosContratados, boolean vinculacoesDocumentos, boolean vinculacoesGarantias, boolean vinculacoesFormulario, boolean vinculacoesChecklist) {

        if (id == null) {
            throw new SimtrRequisicaoException("DPS.gBI.001 - Não é possivel localizar Dossiê de Produto com ID nulo");
        }

        DossieProduto dossieProdutoRetorno;
        DossieProduto dossieProdutoTemp;

        String jpqlBase = this.getQuery(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        jpqlBase = jpqlBase.concat(" WHERE dp.id = :id ");
        TypedQuery<DossieProduto> queryDossieProduto = this.entityManager.createQuery(jpqlBase, DossieProduto.class);
        queryDossieProduto.setParameter("id", id);
        try {
            dossieProdutoRetorno = queryDossieProduto.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }

        if (vinculacoesDossieCliente) {
            String jpql = this.getQuery(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            jpql = jpql.concat(" WHERE dp.id = :id ");
            TypedQuery<DossieProduto> query = this.entityManager.createQuery(jpql, DossieProduto.class);
            query.setParameter("id", id);

            dossieProdutoTemp = query.getSingleResult();
            dossieProdutoRetorno.setDossiesClienteProduto(dossieProdutoTemp.getDossiesClienteProduto());
        }

        if (vinculacoesProdutosContratados) {
            String jpql = this.getQuery(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            jpql = jpql.concat(" WHERE dp.id = :id ");
            TypedQuery<DossieProduto> query = this.entityManager.createQuery(jpql, DossieProduto.class);
            query.setParameter("id", id);

            dossieProdutoTemp = query.getSingleResult();
            dossieProdutoRetorno.setProdutosDossie(dossieProdutoTemp.getProdutosDossie());
        }

        if (vinculacoesDocumentos) {
            String jpql = this.getQuery(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            jpql = jpql.concat(" WHERE dp.id = :id ");
            TypedQuery<DossieProduto> query = this.entityManager.createQuery(jpql, DossieProduto.class);
            query.setParameter("id", id);

            dossieProdutoTemp = query.getSingleResult();
            dossieProdutoRetorno.setInstanciasDocumento(dossieProdutoTemp.getInstanciasDocumento());
        }

        if (vinculacoesGarantias) {
            String jpql = this.getQuery(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
            jpql = jpql.concat(" WHERE dp.id = :id ");
            TypedQuery<DossieProduto> query = this.entityManager.createQuery(jpql, DossieProduto.class);
            query.setParameter("id", id);

            dossieProdutoTemp = query.getSingleResult();
            dossieProdutoRetorno.setGarantiasInformadas(dossieProdutoTemp.getGarantiasInformadas());
        }

        if (vinculacoesFormulario) {
            String jpql = this.getQuery(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
            jpql = jpql.concat(" WHERE dp.id = :id ");
            TypedQuery<DossieProduto> query = this.entityManager.createQuery(jpql, DossieProduto.class);
            query.setParameter("id", id);

            dossieProdutoTemp = query.getSingleResult();
            dossieProdutoRetorno.setRespostasDossie(dossieProdutoTemp.getRespostasDossie());
        }

        if (vinculacoesChecklist) {
            String jpql = this.getQuery(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
            jpql = jpql.concat(" WHERE dp.id = :id ");
            TypedQuery<DossieProduto> query = this.entityManager.createQuery(jpql, DossieProduto.class);
            query.setParameter("id", id);

            dossieProdutoTemp = query.getSingleResult();

            dossieProdutoRetorno.setChecklistsAssociados(dossieProdutoTemp.getChecklistsAssociados());
        }

        return dossieProdutoRetorno;
    }

    /**
     * Captura o proximo dossiê disponivel para complementação vinculado ao processo indicado. Localizado dentre os processos vinculado em toda a hierarquia associada ao processo solicitado.
     *
     * @param idProcesso Identificador do processo desejado
     * @return Dossie de Produto localizado para complementação ou null caso não tenha nenhum dossiê nesta sityuação
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTO,
        ConstantesUtil.PERFIL_MTRSDNTTG
    })
    public DossieProduto capturaDossieComplementacao(final Integer idProcesso) {
        Processo processo = this.processoServico.getById(idProcesso);
        if (processo == null) {
            String mensagem = MessageFormat.format("DPS.cDC.001 - Identificador do processo informado invalido. ID = {0}", idProcesso);
            throw new SimtrRequisicaoException(mensagem);
        }

        TipoSituacaoDossie tipoSituacaoDossieAgurdandoTratamento = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.AGUARDANDO_COMPLEMENTACAO);

        DossieProduto dossieProduto = this.localizaMaisAntigoByProcessoAndTipoSituacao(processo, tipoSituacaoDossieAgurdandoTratamento);

        if (dossieProduto != null) {
            TipoSituacaoDossie tipoSituacaoDossieEmComplementacao = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.EM_COMPLEMENTACAO);
            this.situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoDossieEmComplementacao, null, null);

            return _self.getById(dossieProduto.getId(), Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        }

        return dossieProduto;
    }

    /**
     * Localiza o dossiê de produto que esteja na situação indicada vinculado ao processo informado com data de inclusãoi na situação mais entiga.
     *
     * @param processo Processo de vinculação desejado.
     * @param tipoSituacaoDossie Tipo de situação que o dossiê deve estar vinculado
     * @return Dossiê de produto localizado ou null caso nenhum dossiê de produto se enquadre neste cenario
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    public DossieProduto localizaMaisAntigoByProcessoAndTipoSituacao(final Processo processo, final TipoSituacaoDossie tipoSituacaoDossie) {

        List<Processo> listaProcessosVinculados = this.processoServico.listProcessosVinculados(processo);

        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT dp FROM DossieProduto dp ");
        jpql.append(" LEFT JOIN FETCH dp.situacoesDossie sda ");
        jpql.append(" LEFT JOIN FETCH sda.tipoSituacaoDossie tsd ");
        jpql.append(" LEFT JOIN FETCH dp.unidadesTratamento ut2 ");
        jpql.append(" LEFT JOIN FETCH dp.processo p ");
        jpql.append(" LEFT JOIN FETCH dp.processosFaseDossie pfd ");
        jpql.append(" LEFT JOIN FETCH pfd.processoFase pf ");
        jpql.append(" WHERE ut2.unidade IN (:lotacaoAdministrativa, :lotacaoFisica) ");
        jpql.append(" AND dp.dataHoraFinalizacao IS NULL ");
        jpql.append(" AND (p IN (:processos) OR pf IN (:processos)) ");
        jpql.append(" AND pfd.dataHoraSaida IS NULL ");
        jpql.append(" AND sda.tipoSituacaoDossie = :tipoSituacaoDossie ");
        jpql.append(" AND sda.id = (SELECT MAX(sdb.id) FROM SituacaoDossie sdb WHERE sdb.dossieProduto = dp) ");
        jpql.append(" ORDER BY dp.unidadePriorizado DESC NULLS LAST, sda.dataHoraInclusao ASC ");

        TypedQuery<DossieProduto> query = this.entityManager.createQuery(jpql.toString(), DossieProduto.class);
        query.setParameter("lotacaoAdministrativa", this.keycloakService.getLotacaoAdministrativa());
        query.setParameter("lotacaoFisica", this.keycloakService.getLotacaoFisica());
        query.setParameter("processos", listaProcessosVinculados);
        query.setParameter("tipoSituacaoDossie", tipoSituacaoDossie);

        try {
            return query.setMaxResults(1).getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }

    /**
     * <p>
     * Retorna a lista de dossiês de produto que estão em uma determinada situação e que estejam vinculados a um determinado processo de forma que originou o dossiê e/ou relacionado com sua fase
     * atual.
     * </p>
     * <p>
     * Também é levado em consideração a unidade de vinculação do usuario (fisica ou administrativa) que precisa estar definida como unidade de tratamento para o dossiê no momento da consulta.
     * </p>
     *
     * @param processo Processo de vinculação do dossiê de produto, como originador ou fase
     * @param situacaoDossieEnum Situação desejado em que se encontram os dossiês de produto

     * @return Lista de processos localizados com base nas indicações realizadas
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    public List<DossieProduto> listByProcessoAndSituacao(final Processo processo, final SituacaoDossieEnum situacaoDossieEnum) {

        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT dp FROM DossieProduto dp ");
        jpql.append(" LEFT JOIN FETCH dp.situacoesDossie sd1 ");
        jpql.append(" LEFT JOIN FETCH sd1.tipoSituacaoDossie tsd ");
        jpql.append(" LEFT JOIN FETCH dp.unidadesTratamento ut1 ");
        jpql.append(" LEFT JOIN FETCH dp.processo p ");
        jpql.append(" LEFT JOIN FETCH dp.processosFaseDossie pfd ");
        jpql.append(" LEFT JOIN FETCH pfd.processoFase pf ");
        jpql.append(" WHERE ut1.unidade IN (:lotacaoAdministrativa, :lotacaoFisica) ");
        jpql.append(" AND dp.dataHoraFinalizacao IS NULL ");
        jpql.append(" AND (p = :processo OR pf = :processo) ");
        jpql.append(" AND pfd.dataHoraSaida IS NULL ");

        TypedQuery<DossieProduto> query = this.entityManager.createQuery(jpql.toString(), DossieProduto.class);
        query.setParameter("lotacaoAdministrativa", this.keycloakService.getLotacaoAdministrativa());
        query.setParameter("lotacaoFisica", this.keycloakService.getLotacaoFisica());
        query.setParameter("processo", processo);

        List<DossieProduto> dossiesProduto = query.getResultList();

        // Remove dossiês de produto vinculado ao processo como geração de dossiês cuja ultima situação do dossiê seja diferente da situação indicada em parâmetro
        dossiesProduto.removeIf(dp -> {
            //Remove se a situação atual for diferente da situação indicada
            SituacaoDossie situacaoDossie = dp.getSituacoesDossie().stream().max(Comparator.comparing(SituacaoDossie::getId)).get();          
            return !situacaoDossieEnum.getDescricao().equalsIgnoreCase(situacaoDossie.getTipoSituacaoDossie().getNome());
        });

        return dossiesProduto;
    }

    /**
     * <p>
     * Retorna a lista de dossiês de produto vinculados a um determinado usuario solicitante baseado na lotação (fisica ou administrativa) capturada no token de acesso que esteja vinculada como
     * unidade de criação do dossiê.
     * </p>
     *
     * @param cgc Código da unidade desejada para obtenção dos dados de dossiês de produtos vinculados. Caso não enviado será adotado a lotação fisica e administrativo da usuário.
     * @param vinculadas Indica se as unidades vinculadas devem ser carregadas ou não.
     * @return Lista de Dossiês de Produto vinculados a lotação do solicitante com as informações de Processo, Processo Fase, Situações e Produtos Constratados
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    @Transactional(dontRollbackOn = {BpmException.class})
    public List<DossieProduto> listByUnidade(Integer cgc, boolean vinculadas) {

        // Obtem as lotações administrativa e fisica do usuário
        Integer lotacaoAdministrativa = this.keycloakService.getLotacaoAdministrativa();
        Integer lotacaoFisica = this.keycloakService.getLotacaoFisica();

        // Obtem a lista de unidades vinculadas a unidade do usuário
        // @TODO: Obter a lista de unidade com base nas informações do SIICO
        List<Integer> unidadesVinculadas = Arrays.asList(lotacaoAdministrativa, lotacaoFisica);

        // Lança exceção de permissão caso a unidade do solicitante não possua relação hierarquica com a unidade solicitada e o solicitante não possua perfil de
        // administrador.
        // @TODO: Retirar permissão do grupo MTRSDNMTZ após a captura da lista de unidades vinculadas
        if (cgc != null && !unidadesVinculadas.contains(cgc)
                && !sessionContext.isCallerInRole(ConstantesUtil.PERFIL_MTRADM)
                && !sessionContext.isCallerInRole(ConstantesUtil.PERFIL_MTRSDNMTZ)) {
            String mensagem = MessageFormat.format("DPS.lBU.001 - Usuário não autorizado a consultar as informações da unidade solicitada. CGC Informado: {0}", String.valueOf(cgc));
            throw new SimtrPermissaoException(mensagem);
        }

        // Usuário aprovado a obter os dados, caso tenha sido indicado um cgc, sobrepõe as informações de lotação para realizar a consulta
        if (cgc != null) {
            lotacaoAdministrativa = cgc;
            lotacaoFisica = cgc;
        }

        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT dp FROM DossieProduto dp ");
        jpql.append(" LEFT JOIN FETCH dp.canal ca ");
        jpql.append(" LEFT JOIN FETCH dp.unidadesTratamento ut ");
        jpql.append(" LEFT JOIN FETCH dp.processo p ");
        jpql.append(" LEFT JOIN FETCH dp.processosFaseDossie pfd ");
        jpql.append(" LEFT JOIN FETCH pfd.processoFase pf ");
        jpql.append(" LEFT JOIN FETCH dp.dossiesClienteProduto dcp ");
        jpql.append(" LEFT JOIN FETCH dcp.dossieCliente dc ");
        jpql.append(" LEFT JOIN FETCH dcp.tipoRelacionamento tr ");
        jpql.append(" LEFT JOIN FETCH dp.situacoesDossie sd1 ");
        jpql.append(" LEFT JOIN FETCH sd1.tipoSituacaoDossie tsd ");
        jpql.append(" LEFT JOIN FETCH dp.produtosDossie pd ");
        jpql.append(" LEFT JOIN FETCH pd.produto prd ");

        String sqlUnidadeCriacao = jpql.toString();
        sqlUnidadeCriacao = sqlUnidadeCriacao.concat(" WHERE dp.unidadeCriacao IN (:lotacaoAdministrativa, :lotacaoFisica) ");
        
        String sqlUnidadeTratamento = jpql.toString();
        sqlUnidadeTratamento = sqlUnidadeTratamento.concat(" WHERE dp.unidadeCriacao NOT IN (:lotacaoAdministrativa, :lotacaoFisica) ");
        sqlUnidadeTratamento = sqlUnidadeTratamento.concat(" AND ut.unidade IN (:lotacaoAdministrativa, :lotacaoFisica) ");
        
        TypedQuery<DossieProduto> queryUnidadeCriacao = this.entityManager.createQuery(sqlUnidadeCriacao, DossieProduto.class);
        queryUnidadeCriacao.setParameter("lotacaoAdministrativa", lotacaoAdministrativa);
        queryUnidadeCriacao.setParameter("lotacaoFisica", lotacaoFisica);
        List<DossieProduto> dossiesProduto = queryUnidadeCriacao.getResultList();
        
        TypedQuery<DossieProduto> queryUnidadeTratamento = this.entityManager.createQuery(sqlUnidadeTratamento, DossieProduto.class);
        queryUnidadeTratamento.setParameter("lotacaoAdministrativa", lotacaoAdministrativa);
        queryUnidadeTratamento.setParameter("lotacaoFisica", lotacaoFisica);
        dossiesProduto.addAll(queryUnidadeTratamento.getResultList());

        TipoSituacaoDossie tipoSituacaoRascunho = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.RASCUNHO);
        TipoSituacaoDossie tipoSituacaoCriado = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.CRIADO);

        dossiesProduto.forEach(dossieProduto -> {

            SituacaoDossie situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream().max(Comparator.comparing(SituacaoDossie::getId)).get();

            try {
                // Caso o dossiê seja identificado em situação do tipo final e não tenha data hora de finalização, indica finalização da operação
                if (situacaoAtualDossie.getTipoSituacaoDossie().getTipoFinal() && dossieProduto.getDataHoraFinalizacao() == null) {
                    _self.finalizaDossieProduto(dossieProduto.getId());
                }

                // Realiza a notificação do BPM caso a situação tenha ficado bloqueada sobre uma situação não esperada.
                boolean comunicouBPM = Boolean.FALSE;
                if (tipoSituacaoCriado.equals(situacaoAtualDossie.getTipoSituacaoDossie()) && dossieProduto.getIdInstanciaProcessoBPM() == null) {
                    _self.atribuiIntanciaBPM(dossieProduto.getId());
                } else if (!tipoSituacaoRascunho.equals(situacaoAtualDossie.getTipoSituacaoDossie())) {
                    // Realiza notificação do processo para andamento das ações
                    comunicouBPM = this.bpmServico.notificaBPM(dossieProduto);
                }
                
                // Comunicação com o jBPM bem sucedida
                // Remove o valor existente definido para o controle de falha de comunicação associado ao dossiê de produto
                if(comunicouBPM && dossieProduto.getDataHoraFalhaBPM() != null){
                    _self.removeDataHoraFalhaBPM(dossieProduto.getId());
                }
            } catch (EJBException ee) {
                if (ee.getCause().getClass().equals(BpmException.class)) {
                    _self.atribuiDataHoraFalhaBPM(dossieProduto.getId());
                    String mensagem = MessageFormat.format("DPS.lBU.001 - Falha ao comunicar com o BPM a consulta de dossiês por unidade. ID: {0} | Causa: {1}", dossieProduto.getId(), ee.getCause());
                    LOGGER.log(Level.ALL, mensagem, ee);
                    LOGGER.log(Level.WARNING, mensagem);
                }
            }
        });

        return dossiesProduto;
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    @Transactional(dontRollbackOn = {BpmException.class})
    public void atribuiIntanciaBPM(Long idDossie) {
        //Monta a String que representa a consulta JPQL a ser executada
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT dp FROM DossieProduto dp ");
        jpql.append(" LEFT JOIN FETCH dp.processo pro ");
        jpql.append(" WHERE dp.id = :id ");
        
        //Cria o objeto que representa a query a ser executada a preenche o parametro com o identificador do dossiê
        TypedQuery<DossieProduto> queryDossieProduto = this.entityManager.createQuery(jpql.toString(), DossieProduto.class);
        queryDossieProduto.setParameter("id", idDossie);
        
        //Captura o dossiê de produto indicado em modo transacional para verificar definições de container e processo BPM
        DossieProduto dossieProduto = queryDossieProduto.getSingleResult();

        //Atualiza as definições de processo e containerBPM caso não exitam no dossiê e tenham sido definidas no processo
        if (Objects.isNull(dossieProduto.getNomeContainerBPM()) || Objects.isNull(dossieProduto.getNomeProcessoBPM())) {
            dossieProduto.setNomeContainerBPM(dossieProduto.getProcesso().getNomeContainerBPM());
            dossieProduto.setNomeProcessoBPM(dossieProduto.getProcesso().getNomeProcessoBPM());
        }
        
        //Obtem o identificador do processo BPM
        Long idInstanciaBPM = this.bpmServico.criaProcessoBPM(dossieProduto);
        if (Objects.nonNull(idInstanciaBPM)) {
            dossieProduto.setIdInstanciaProcessoBPM(idInstanciaBPM);
            _self.update(dossieProduto);
           
            // Realiza notificação do processo para andamento das ações
            this.bpmServico.notificaBPM(dossieProduto);
            
            if(dossieProduto.getDataHoraFalhaBPM() != null){
                dossieProduto.setDataHoraFalhaBPM(null);
                this.update(dossieProduto);
            }
        }
    }
    
    /**
     * <p>Busca o dossiê de produto e registra a nova situação passada por argumento.
     * Caso a nova situação do dossiê for 'Finalizado' e ele possuir uma instância de processo BPM,
     * essa instância será abortada no BPM.
     * </p>
     * @param idDossieProduto Identificador do dossiê de produto a ser alterado.
     * @param situacaoDossie Nova situação a ser definida ao dossiê de produto.
     * @param observacao Observação a ser incluida no registro da situação do dossiê.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void adicionaSituacaoDossieProduto(Long idDossieProduto, SituacaoDossieEnum situacaoDossie, String observacao) {
        DossieProduto dossieProduto = this.getById(idDossieProduto, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        SituacaoDossie situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream().max(Comparator.comparing(SituacaoDossie::getId)).get();
        if (situacaoAtualDossie.getTipoSituacaoDossie().getTipoFinal()) {
            String mensagem = "DPS.aSDP.001 - O dossiê de produto já está com uma situação final, não podendo ser alterada nessa circustância.";
            throw new SimtrPermissaoException(mensagem);
        }
        TipoSituacaoDossie tipoSituacaoDossieNova = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(situacaoDossie);
        this.situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoDossieNova, null, observacao);

        if (tipoSituacaoDossieNova.getTipoFinal() && !Objects.isNull(dossieProduto.getIdInstanciaProcessoBPM())) {
            String containerId = dossieProduto.getNomeContainerBPM();
            Long instanceId = dossieProduto.getIdInstanciaProcessoBPM();
            bpmServico.abortarProcessoBPM(containerId, instanceId, idDossieProduto);
        }
    }

    public void vinculaUnidadeTratamento(Long idDossie, Integer unidade) {

        DossieProduto dossieProduto = this.getById(idDossie, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);

        if (!dossieProduto.getUnidadesTratamento().stream().anyMatch(ut -> unidade.equals(ut.getUnidade()))) {
            UnidadeTratamento unidadeTratamento = new UnidadeTratamento();
            unidadeTratamento.setDossieProduto(dossieProduto);
            unidadeTratamento.setUnidade(unidade);

            this.entityManager.persist(unidadeTratamento);
        }

    }
    
    /**
     * Atualiza a data/hora de finalização do dossiê de produto, caso a situação
     * atual do mesmo seja do tipo final
     *
     * @param idDossie Identificador do dossie de produto a ser finalizado
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void finalizaDossieProduto(Long idDossie) {
        //Monta a String que representa a consulta JPQL a ser executada
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT dp FROM DossieProduto dp ");
        jpql.append(" LEFT JOIN FETCH dp.processo pro ");
        jpql.append(" LEFT JOIN FETCH dp.situacoesDossie sd ");
        jpql.append(" LEFT JOIN FETCH sd.tipoSituacaoDossie tsd ");
        jpql.append(" WHERE dp.id = :id ");
        
        //Cria p pbjeto que representa a query a ser executada a preenche o parametro com o identificador do dossiê
        TypedQuery<DossieProduto> queryDossieProduto = this.entityManager.createQuery(jpql.toString(), DossieProduto.class);
        queryDossieProduto.setParameter("id", idDossie);
        
        //Captura o dossiê de produto indicado em modo transacional para verificar definições de container e processo BPM
        DossieProduto dossieProduto = queryDossieProduto.getSingleResult();

        SituacaoDossie situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream().max(Comparator.comparing(SituacaoDossie::getId)).get();
        if(situacaoAtualDossie.getTipoSituacaoDossie().getTipoFinal()){
            dossieProduto.setDataHoraFinalizacao(situacaoAtualDossie.getDataHoraInclusao());
            dossieProduto.setDataHoraFalhaBPM(null);
        }
    }
    
    /**
     * Atualiza a data/hora de falha na comunicação com o jBPM para o dossiê de
     * produto indicado, caso ainda não exista marcação referente
     *
     * @param idDossie Identificador do dossie de produto a ser finalizado
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void atribuiDataHoraFalhaBPM(Long idDossie) {
        //Monta a String que representa a consulta JPQL a ser executada
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT dp FROM DossieProduto dp ");
        jpql.append(" LEFT JOIN FETCH dp.processo pro ");
        jpql.append(" WHERE dp.id = :id ");
        
        //Cria p pbjeto que representa a query a ser executada a preenche o parametro com o identificador do dossiê
        TypedQuery<DossieProduto> queryDossieProduto = this.entityManager.createQuery(jpql.toString(), DossieProduto.class);
        queryDossieProduto.setParameter("id", idDossie);
        
        //Captura o dossiê de produto indicado em modo transacional para verificar definições de container e processo BPM
        DossieProduto dossieProduto = queryDossieProduto.getSingleResult();

        //Caso o registro de data/hora de controle de falha de comunicação com o jBPM não esteja definido atribui o valor de data/hora atual
        if(dossieProduto.getDataHoraFalhaBPM() == null){
            dossieProduto.setDataHoraFalhaBPM(Calendar.getInstance());
        }
    }
    
    /**
     * Remove a referência de data/hora de falha na comunicação com o jBPM para
     * o dossiê de produto indicado.
     *
     * @param idDossie Identificador do dossie de produto a ser finalizado
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeDataHoraFalhaBPM(Long idDossie) {       
        //Monta a String que representa a consulta JPQL a ser executada
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT dp FROM DossieProduto dp ");
        jpql.append(" LEFT JOIN FETCH dp.processo pro ");
        jpql.append(" WHERE dp.id = :id ");
        
        //Cria p pbjeto que representa a query a ser executada a preenche o parametro com o identificador do dossiê
        TypedQuery<DossieProduto> queryDossieProduto = this.entityManager.createQuery(jpql.toString(), DossieProduto.class);
        queryDossieProduto.setParameter("id", idDossie);
        
        //Captura o dossiê de produto indicado em modo transacional para verificar definições de container e processo BPM
        DossieProduto dossieProduto = queryDossieProduto.getSingleResult();

        //Remove o valor de data/hora de controle por ventura definido junto ao dossiê de produto
        dossieProduto.setDataHoraFinalizacao(null);
    }

    ///////////////////////////////////////////////
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public Long novoDossieProduto(DossieProdutoInclusaoDTO dossieProdutoInclusaoDTO) {
        // Captura do canal de comunicação
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "DPS.nDP.001 - ".concat(ConstantesUtil.MSG_CANAL_NAO_LOCALIZADO_SSO));

        // Captura o processo originador do dossiê de produto
        Processo processoOrigem = this.processoServico.getById(dossieProdutoInclusaoDTO.getProcessoOrigem());
        if (processoOrigem == null) {
            throw new SimtrRequisicaoException("DPS.nDP.002 - Processo de originação do dossiê não localizado para identificador fornecido.");
        }
        
        // Captura a lotação do usuário solicitante
        Integer lotacaoAdministrativa = this.keycloakService.getLotacaoAdministrativa();
        Integer lotacaoFisica = this.keycloakService.getLotacaoFisica();
        
        // Verifica a lotação fisica ou administrativa do usuário esta na lista de unidades autorizadas a criar operações para o processo
        boolean unidadeAutorizada = processoOrigem.getUnidadesAutorizadas().stream()
                .anyMatch(ua -> ua.getUnidade().equals(lotacaoAdministrativa) || ua.getUnidade().equals(lotacaoFisica));
        
        //Levanta uma exceção caso a unidade de vinculação não esteja autorizada a criar operações deste processo
        if(!unidadeAutorizada){
            String mensagem = MessageFormat.format("DPS.nDP.013 - Unidade de vinculação do usuário [{0}, {1}] não autorizada a criar operações o processo {2}", lotacaoAdministrativa, lotacaoFisica, processoOrigem.getNome());
            throw new SimtrPermissaoException(mensagem);
        }

        // Valida a solicitação se contempla todos os documentos necessarios para o dossiê em inclusão caso a operação não seja RASCUNHO.
        if (!dossieProdutoInclusaoDTO.isRascunho()) {
            this.dossieProdutoValidacaoHelper.validaDossieProdutoInclusao(processoOrigem, dossieProdutoInclusaoDTO);
        }

        // Executa a criação do dosiê de produto de forma basica contendo apenas a vinculação com o processo e a situação inicial
        DossieProduto dossieProduto = _self.createDossieProduto(processoOrigem, canal, dossieProdutoInclusaoDTO.isRascunho(), Boolean.FALSE);

        this.dossieProdutoVinculosHelper.insereDocumentosElementoConteudoProcesso(canal, dossieProduto, dossieProdutoInclusaoDTO.getElementosConteudoDTO());

        // Cria vinculo do produto informado nas condições indicadas, com dossiê de produto criado
        if (dossieProdutoInclusaoDTO.getProdutosContratadosDTO() != null) {
            dossieProdutoInclusaoDTO.getProdutosContratadosDTO().forEach(produtoContratadoDTO -> {
                Produto produto;
                if (produtoContratadoDTO.getIdProduto() != null) {
                    produto = this.produtoServico.getById(produtoContratadoDTO.getIdProduto(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
                } else {
                    produto = this.produtoServico.getByOperacaoModalidade(produtoContratadoDTO.getOperacao(), produtoContratadoDTO.getModalidade(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
                }
                if (produto == null) {
                    throw new SimtrRequisicaoException(MessageFormat.format("DPS.nDP.003 - Produto não localizado para identificador fornecido. ID = {0}", produtoContratadoDTO.getIdProduto()));
                }

                // Cria o vinculo do produto com o dossiê criado
                ProdutoDossie produtoDossie = this.dossieProdutoVinculosHelper.criaVinculoProduto(dossieProduto, produto);

                this.dossieProdutoVinculosHelper.insereDocumentosElementoConteudoProduto(canal, dossieProduto, produtoContratadoDTO);

                // Cria vinculo das respostas de formulario associadas a garantia informada nas condições indicadas, com dossiê de produto criado
                if (produtoContratadoDTO.getRespostasFormularioDTO() != null) {
                    produtoContratadoDTO.getRespostasFormularioDTO().forEach(respostaFormulario -> {
                        CampoFormulario campoFormulario = this.campoFormularioServico.getById(respostaFormulario.getIdCampoFomulario(), Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
                        if (campoFormulario == null) {
                            throw new SimtrRequisicaoException(MessageFormat.format("DPS.nDP.004 - Campo Formulario não localizado para identificador fornecido. ID = {0}", respostaFormulario.getIdCampoFomulario()));
                        }

                        this.dossieProdutoVinculosHelper.vinculaRespostaFormularioDossieProduto(dossieProduto, campoFormulario, respostaFormulario.getResposta(), respostaFormulario.getOpcoesSelecionadas(), null, produtoDossie, null);
                    });
                }
            });
        }

        // Cria o vinculo dos dossiês de cliente indicados com o dossiê de produto criado
        if (dossieProdutoInclusaoDTO.getVinculosDossieClienteDTO() != null) {
            dossieProdutoInclusaoDTO.getVinculosDossieClienteDTO().forEach(vinculoCliente -> {
                DossieCliente dossieCliente = this.dossieClienteServico.getById(vinculoCliente.getIdDossieCliente(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
                DossieCliente dossieClienteRelacionado = null;
                if (vinculoCliente.getIdDossieClienteRelacionado() != null) {
                    dossieClienteRelacionado = this.dossieClienteServico.getById(vinculoCliente.getIdDossieClienteRelacionado(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
                }
                TipoRelacionamento tipoRelacionamento = null;
                if (vinculoCliente.getTipoRelacionamento() != null) {
                    tipoRelacionamento = this.tipoRelacionamentoServico.getById(vinculoCliente.getTipoRelacionamento());
                }

                if (dossieCliente == null) {
                    throw new SimtrRequisicaoException(MessageFormat.format("DPS.nDP.005 - Dossiê de Cliente não localizado para identificador fornecido. ID = {0}", vinculoCliente.getIdDossieCliente()));
                }
                if ((vinculoCliente.getIdDossieClienteRelacionado() != null) && (dossieClienteRelacionado == null)) {
                    throw new SimtrRequisicaoException(MessageFormat.format("DPS.nDP.006 - Dossiê de Cliente Relacionado não localizado para identificador fornecido. ID = {0}", vinculoCliente.getIdDossieClienteRelacionado()));
                }
                if (tipoRelacionamento == null) {
                    throw new SimtrRequisicaoException(MessageFormat.format("DPS.nDP.007 - Tipo de relacionamento não localizado para identificador fornecido. ID = {0}", vinculoCliente.getTipoRelacionamento()));
                }

                // Cria relação entre o dossiê de cliente e o dossiê de produto definindo o tipo de vinculo
                DossieClienteProduto dossieClienteProduto = this.dossieProdutoVinculosHelper.criaVinculoDossieCliente(dossieCliente, dossieClienteRelacionado, dossieProduto, tipoRelacionamento, vinculoCliente.getSequenciaTitularidade());

                this.dossieProdutoVinculosHelper.insereDocumentosVinculoDossieCliente(canal, dossieProduto, dossieClienteProduto, vinculoCliente);

                // Cria vinculo das respostas de formulario associadas ao vinculo de cliente nas condições indicadas, com dossiê de produto criado
                if (vinculoCliente.getRespostasFormularioDTO() != null) {
                    vinculoCliente.getRespostasFormularioDTO().forEach(respostaFormulario -> {
                        CampoFormulario campoFormulario = this.campoFormularioServico.getById(respostaFormulario.getIdCampoFomulario(), Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
                        if (campoFormulario == null) {
                            throw new SimtrRequisicaoException(MessageFormat.format("DPS.nDP.008 - Campo Formulario não localizado para identificador fornecido. ID = {0}", respostaFormulario.getIdCampoFomulario()));
                        }

                        this.dossieProdutoVinculosHelper.vinculaRespostaFormularioDossieProduto(dossieProduto, campoFormulario, respostaFormulario.getResposta(), respostaFormulario.getOpcoesSelecionadas(), dossieClienteProduto, null, null);
                    });
                }
            });
        }

        // Cria vinculo da garantia informada nas condições indicadas, com dossiê de produto criado
        if (dossieProdutoInclusaoDTO.getGarantiasInformadasDTO() != null) {
            dossieProdutoInclusaoDTO.getGarantiasInformadasDTO().forEach(garantiaInformadaDTO -> {
                Garantia garantia = this.garantiaServico.getById(garantiaInformadaDTO.getIdentificadorGarantia(), Boolean.FALSE, Boolean.TRUE, Boolean.TRUE);
                if (garantia == null) {
                    throw new SimtrRequisicaoException(MessageFormat.format("DPS.nDP.009 - Garantia não localizada para identificador fornecido. ID Garantia = {0}", garantiaInformadaDTO.getIdentificadorGarantia()));
                }

                Produto produto = null;
                if (garantiaInformadaDTO.getIdentificadorProduto() != null) {
                    produto = this.produtoServico.getById(garantiaInformadaDTO.getIdentificadorProduto(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
                    if (produto == null) {
                        throw new SimtrRequisicaoException(MessageFormat.format("DPS.nDP.010 - Produto não localizado para identificador fornecido. ID = {0}", garantiaInformadaDTO.getIdentificadorProduto()));
                    }
                }

                GarantiaInformada garantiaInformada = this.dossieProdutoVinculosHelper.criaVinculoGarantiaInformada(dossieProduto, garantia, produto, garantiaInformadaDTO);

                this.dossieProdutoVinculosHelper.insereDocumentosGarantiaInformada(canal, dossieProduto, garantiaInformada, garantiaInformadaDTO);

                // Cria vinculo das respostas de formulario associadas a garantia informada nas condições indicadas, com dossiê de produto criado
                if (garantiaInformadaDTO.getRespostasFormularioDTO() != null) {
                    garantiaInformadaDTO.getRespostasFormularioDTO().forEach(respostaFormulario -> {
                        CampoFormulario campoFormulario = this.campoFormularioServico.getById(respostaFormulario.getIdCampoFomulario(), Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
                        if (campoFormulario == null) {
                            throw new SimtrRequisicaoException(MessageFormat.format("DPS.nDP.011 - Campo Formulario não localizado para identificador fornecido. ID = {0}", respostaFormulario.getIdCampoFomulario()));
                        }

                        this.dossieProdutoVinculosHelper.vinculaRespostaFormularioDossieProduto(dossieProduto, campoFormulario, respostaFormulario.getResposta(), respostaFormulario.getOpcoesSelecionadas(), null, null, garantiaInformada);
                    });
                }

            });
        }

        // Cria vinculo das respostas de formulario informada nas condições indicadas, com dossiê de produto criado
        if (dossieProdutoInclusaoDTO.getRespostasFormularioDTO() != null) {
            dossieProdutoInclusaoDTO.getRespostasFormularioDTO().forEach(respostaFormulario -> {
                CampoFormulario campoFormulario = this.campoFormularioServico.getById(respostaFormulario.getIdCampoFomulario(), Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
                if (campoFormulario == null) {
                    throw new SimtrRequisicaoException(MessageFormat.format("DPS.nDP.012 - Campo Formulario não localizado para identificador fornecido. ID = {0}", respostaFormulario.getIdCampoFomulario()));
                }

                this.dossieProdutoVinculosHelper.vinculaRespostaFormularioDossieProduto(dossieProduto, campoFormulario, respostaFormulario.getResposta(), respostaFormulario.getOpcoesSelecionadas(), null, null, null);
            });
        }

        return dossieProduto.getId();
    }

    /**
     *
     * @param id Identificador do dossiê de produto a ser alterado
     * @param dossieProdutoAlteracaoDTO Objeto contendo a representação das modificações a serem realizadas no dossiê de produto indicado.
     * @throws SimtrRequisicaoException
     * @throws SimtrPermissaoException
     *         <p>
     *         Levanta exceção caso exista algum impedimento que permita a aplicação do patch por completo no dossiê.
     *         </p>
     *         <p>
     *         Os codigos de erro das exceções seguem os seguintes grupo:
     *         <ul>
     *         <li>MNDPS.pDP.000 - Problema ocorrido em ambito geral antes de iniciar a aplicação das modificações. Por exemplo, usuário não esta apto a realizar a modificação.</li>
     *         <li>MNDPS.pDP.100 - Problema ocorrido no momento de aplicar as exclusões. Por exemplo, Produto informado não associado ao dossiê.</li>
     *         <li>MNDPS.pDP.200 - Problema ocorrido no momento de aplicar as inclusões ou alterações de vinculos de produtos. Por exemplo, tentativa de inclusão de produto após submissão do
     *         dossiê.</li>
     *         <li>MNDPS.pDP.300 - Problema ocorrido no momento de aplicar as inclusões ou alterações de vinculos de pessoas. Por exemplo, tentativa de alteração de vinculo após aplicação de
     *         tratamento em situação conforme.</li>
     *         <li>MNDPS.pDP.400 - Problema ocorrido no momento de aplicar as inclusões ou alterações de vinculos de garantias. Por exemplo, garantia não localizada sob o ID informado.</li>
     *         <li>MNDPS.pDP.500 - Problema ocorrido no momento de aplicar as inclusões ou alterações de respostas do formulário. Por exemplo, campo de formulario não localizado sob ID informado.</li>
     *         </ul>
     *         </p>
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTO,
        ConstantesUtil.PERFIL_MTRSDNTTG
    })
    public void patchDossieProduto(Long id, DossieProdutoAlteracaoDTO dossieProdutoAlteracaoDTO) {
        // Captura do canal de comunicação
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, ConstantesUtil.MSG_CANAL_NAO_LOCALIZADO_SSO);

        // Captura o dossiê de produto para alteração conforme parametros informados.
        DossieProduto dossieProduto = _self.getById(id, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);

        // Identifica as informações do usuário da requisição
        String matricula = this.keycloakService.getMatricula();
        Integer lotacaoAdministrativa = this.keycloakService.getLotacaoAdministrativa();
        Integer lotacaoFisica = this.keycloakService.getLotacaoFisica();

        // Identifica a situação atual do dossiê de produto
        SituacaoDossie situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream().max(Comparator.comparing(SituacaoDossie::getId)).get();

        // Captura os tipo de situação utilizado para aplicar o cancelamento/retorno do dossiê
        TipoSituacaoDossie tipoSituacaoAguardandoAlimentacao = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.AGUARDANDO_ALIMENTACAO);
        TipoSituacaoDossie tipoSituacaoAguardandoTratamento = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.AGUARDANDO_TRATAMENTO);
        TipoSituacaoDossie tipoSituacaoAlimentacaoFinalizada = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.ALIMENTACAO_FINALIZADA);
        TipoSituacaoDossie tipoSituacaoAnaliseSeguranca = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.ANALISE_SEGURANCA);
        TipoSituacaoDossie tipoSituacaoCancelado = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.CANCELADO);
        TipoSituacaoDossie tipoSituacaoConforme = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.CONFORME);
        TipoSituacaoDossie tipoSituacaoCriado = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.CRIADO);
        TipoSituacaoDossie tipoSituacaoEmAlimentacao = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.EM_ALIMENTACAO);
        TipoSituacaoDossie tipoSituacaoEmComplementacao = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.EM_COMPLEMENTACAO);
        TipoSituacaoDossie tipoSituacaoEmTratamento = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.EM_TRATAMENTO);
        TipoSituacaoDossie tipoSituacaoPendenteInformacao = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.PENDENTE_INFORMACAO);
        TipoSituacaoDossie tipoSituacaoPendenteSeguranca = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.PENDENTE_SEGURANCA);
        TipoSituacaoDossie tipoSituacaoRascunho = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.RASCUNHO);
        TipoSituacaoDossie tipoSituacaoSegurancaFinalizado = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.SEGURANCA_FINALIZADO);

        // Identifica se já houve alguma situação de conforme vinculada ao dossiê
        boolean situacaoConformeExistente = dossieProduto.getSituacoesDossie().stream().filter(sd -> tipoSituacaoConforme.equals(sd.getTipoSituacaoDossie())).findAny().isPresent();

        // Verifica se a lotação do usuário está autorizada a manipular o dossiê no momento.
        if (!dossieProduto.getUnidadesTratamento().stream().filter(ut -> (ut.getUnidade().equals(lotacaoAdministrativa) || (ut.getUnidade().equals(lotacaoFisica)))).findAny()
                          .isPresent()) {
            if (!(dossieProduto.getUnidadeCriacao().equals(lotacaoAdministrativa)) && !(dossieProduto.getUnidadeCriacao().equals(lotacaoFisica))) {
                throw new SimtrRequisicaoException("DPS.pDP.001 - A unidade de lotação do usuário não esta apta a realizar alterações neste dossiê.");
            }
        }

        // Verifica se o dossiê encontra-se em estado de alimentação, complementação ou rascunho
        if (!tipoSituacaoEmAlimentacao.equals(situacaoAtualDossie.getTipoSituacaoDossie())
            && !tipoSituacaoEmComplementacao.equals(situacaoAtualDossie.getTipoSituacaoDossie())
            && !tipoSituacaoRascunho.equals(situacaoAtualDossie.getTipoSituacaoDossie())) {

            String nomeSituacao = situacaoAtualDossie.getTipoSituacaoDossie().getNome(); 
            String mensagem = MessageFormat.format("DPS.pDP.008 - O dossiê de produto encontra-se em situação impeditiva de manipulação. Para efetuar alterações é necessário que o dossiê esteja com a situação atual \"Em Alimentação\" ou \"Em Complementação\". Situação Atual = {0}.", nomeSituacao);
            throw new SimtrRequisicaoException(mensagem);
        }

        // Verifica se o usuário que submeteu o patch é o mesmo que capturou o dossiê para alimentação
        if (!tipoSituacaoRascunho.equals(situacaoAtualDossie.getTipoSituacaoDossie())) {
            if (!situacaoAtualDossie.getResponsavel().equalsIgnoreCase(matricula) && !dossieProdutoAlteracaoDTO.isRetorno()) {
                String mensagem = MessageFormat.format("DPS.pDP.002 - O dossiê não pode ser alterado pois esta em manutenção pelo usuário {0}.", situacaoAtualDossie.getResponsavel());
                throw new SimtrRequisicaoException(mensagem);
            }
        }

        // Realiza o cancelamento do dossiê
        if (dossieProdutoAlteracaoDTO.isCancelamento()) {
            // Verifica se a unidade de criação é a mesma da lotação administrativa ou fisica do usuário
            if ((dossieProduto.getUnidadeCriacao().equals(lotacaoAdministrativa)) || (dossieProduto.getUnidadeCriacao().equals(lotacaoFisica))) {
                // Verifica se o dossiê encontra-se em fase na situação "Em Alimentação" ou "Rascunho"
                if ((tipoSituacaoEmAlimentacao.equals(situacaoAtualDossie.getTipoSituacaoDossie())) || (tipoSituacaoRascunho.equals(situacaoAtualDossie.getTipoSituacaoDossie()))) {
                    String justificativa = dossieProdutoAlteracaoDTO.getJustificativa();
                    if ((Objects.isNull(justificativa)) || (justificativa.length() < 10)) {
                        String mensagem = "DPS.pDP.007 - A justificativa para o cancelamento é obrigatoria e deve possui no minimo 10 caracteres.";
                        throw new SimtrRequisicaoException(mensagem);
                    }

                    // Registra a nova situação junto ao dossiê de produto
                    this.situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoCancelado, null, "JUSTIFICATIVA: ".concat(justificativa));

                    // Caso o dossiê de produto possua instancia de processo BPM definida, encaminha sinal de cancelamento ao mesmo
                    if (dossieProduto.getIdInstanciaProcessoBPM() != null) {
                        this.bpmServico.abortarProcessoBPM(dossieProduto.getNomeContainerBPM(), dossieProduto.getIdInstanciaProcessoBPM(), dossieProduto.getId());
                    }
                } else {
                    String nomeSituacaoAtual = situacaoAtualDossie.getTipoSituacaoDossie().getNome();
                    String mensagem = MessageFormat.format("DPS.pDP.003 - O dossiê de produto não pode ser cancelado. Situação Atual = {0}", nomeSituacaoAtual);
                    throw new SimtrRequisicaoException(mensagem);
                }
            }
            // Finaliza a operação
            return;
        }

        // Realiza o retorno para a ultima situação por desistência da aplicação da operação
        if (dossieProdutoAlteracaoDTO.isRetorno()) {

            // Verifica se o dossiê encontra-se em fase na situação "Analise Segurança"
            if (tipoSituacaoAnaliseSeguranca.equals(situacaoAtualDossie.getTipoSituacaoDossie())) {
                this.situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoPendenteSeguranca, "DESISTÊNCIA DA OPERAÇÃO DE AVALIAÇÂO DA SEGURANÇA", null);
                // Finaliza a operação
                return;
            }

            // Verifica se o dossiê encontra-se em fase na situação "Em Complementação"
            if (tipoSituacaoEmComplementacao.equals(situacaoAtualDossie.getTipoSituacaoDossie())) {
                // Situação Previa = AGUARDANDO COMPLEMENTAÇÃO
                // Remove situação atual indicando observação para permanecer com posição de captura
                String dataAlimentacao = this.calendarUtil.toString(situacaoAtualDossie.getDataHoraInclusao(), "dd/MM/yyyy HH:mm:ss");
                String dataDevolucao = this.calendarUtil.toString(Calendar.getInstance(), "dd/MM/yyyy HH:mm:ss");
                String mensagem = MessageFormat.format("DOSSIÊ CAPTURADO PARA COMPLEMENTACAO POR {0} EM {1} E LIBERADO SEM MODIFICAÇÃO EM {2} POR {3}", situacaoAtualDossie.getResponsavel(), dataAlimentacao, dataDevolucao, matricula);
                this.situacaoDossieServico.removeUltimaSituacaoDossie(dossieProduto, mensagem);

                // Finaliza a operação
                return;
            }

            // Verifica se o dossiê encontra-se em fase na situação "Em Alimentação"
            if (tipoSituacaoEmAlimentacao.equals(situacaoAtualDossie.getTipoSituacaoDossie())) {
                SituacaoDossie situacaoPreviaDossie = dossieProduto.getSituacoesDossie().stream()
                                                                   .sorted(Comparator.comparing(SituacaoDossie::getId).reversed())
                                                                   .filter(sd -> ((tipoSituacaoAguardandoAlimentacao.equals(sd.getTipoSituacaoDossie()))
                                                                                  || (tipoSituacaoAguardandoTratamento.equals(sd.getTipoSituacaoDossie()))
                                                                                  || (tipoSituacaoPendenteInformacao.equals(sd.getTipoSituacaoDossie()))))
                                                                   .findFirst().orElseThrow(() -> {
                                                                       // Situação Previa = DEMAIS SITACÕES -> NÃO DEVE ACONTECER, FORA DO CICLO DE VIDA DEFINIDO!
                                                                       String mensagem = MessageFormat.format("DPS.pDP.004 - O dossiê de produto possui uma situação prévia ao momento de alimentação não prevista. Favor entrar em contato com a equipe do SIMTR. Dossiê de Produito = {0}", dossieProduto.getId());
                                                                       return new SimtrPermissaoException(mensagem);
                                                                   });

                String mensagemObservacao = situacaoAtualDossie.getResponsavel()
                                                               .equalsIgnoreCase(matricula)
                                                                                            ? "DESISTÊNCIA DA OPERAÇÃO DE ALIMENTAÇÃO DE DADOS"
                                                                                            : MessageFormat.format("RETORNO DA OPERAÇÃO REALIZADA PELO USUARIO {0}", matricula);

                if (tipoSituacaoAguardandoAlimentacao.equals(situacaoPreviaDossie.getTipoSituacaoDossie())) {
                    // Situação Previa = AGUARDANDO ALIMENTAÇÂO
                    this.situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoAguardandoAlimentacao, mensagemObservacao, null);
                } else if (tipoSituacaoPendenteInformacao.equals(situacaoPreviaDossie.getTipoSituacaoDossie())) {
                    // Situação Previa = PENDENTE INFORMAÇÃO
                    this.situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoPendenteInformacao, mensagemObservacao, null);
                } else if (tipoSituacaoAguardandoTratamento.equals(situacaoPreviaDossie.getTipoSituacaoDossie())) {
                    // Situação Previa = AGUARDANDO TRATAMENTO
                    String dataAlimentacao = this.calendarUtil.toString(situacaoAtualDossie.getDataHoraInclusao(), "dd/MM/yyyy HH:mm:ss");
                    String dataDevolucao = this.calendarUtil.toString(Calendar.getInstance(), "dd/MM/yyyy HH:mm:ss");
                    String mensagem = MessageFormat.format("DOSSIÊ CAPTURADO PARA ALTERAÇÃO POR {0} EM {1} E LIBERADO SEM MODIFICAÇÃO EM {2} POR {3}", situacaoAtualDossie.getResponsavel(), dataAlimentacao, dataDevolucao, matricula);
                    this.situacaoDossieServico.removeUltimaSituacaoDossie(dossieProduto, mensagem);
                }

                // Finaliza a operação
                return;
            }

            if (tipoSituacaoEmTratamento.equals(situacaoAtualDossie.getTipoSituacaoDossie())) {
                String mensagem = "DPS.pDP.005 - Para desistir da operação de tratamento deve ser utilizado o endpoint POST /{id}/retornafilatratamento";
                throw new SimtrPermissaoException(mensagem);
            }
            
            String nomeSituacaoAtual = situacaoAtualDossie.getTipoSituacaoDossie().getNome();
            String mensagem = MessageFormat.format("DPS.pDP.006 - O dossiê de produto não pode ter a situação retornada. Situação Atual = {0}", nomeSituacaoAtual);
            throw new SimtrPermissaoException(mensagem);
        }
        
        // Aplica as exclusões de produtos contratados
        if (dossieProdutoAlteracaoDTO.getProdutosContratadosAlteracaoDTO() != null) {
            dossieProdutoAlteracaoDTO.getProdutosContratadosAlteracaoDTO().stream().filter(pc -> pc.isExclusao()).forEach(produtoContratadoAlteracaoDTO -> {
                // Verifica se o produto solicitado exclusão esta vinculado ao dossiê de produto indicado
                dossieProduto.getProdutosDossie().stream().filter(pd -> pd.getProduto().getId().equals(produtoContratadoAlteracaoDTO.getIdProduto())).findAny().orElseThrow(() -> {
                    String mensagem = MessageFormat.format("DPS.pDP.101 - O Produto que foi solicitado exclusão não foi localizado vinculado ao dossiê indicado. ID Produto = {0}", produtoContratadoAlteracaoDTO.getIdProduto());
                    return new SimtrPermissaoException(mensagem);

                });

                // Permite excluir produtos apenas se dossiê estiver em situação definida como tipo inicial (RASCUNHO)
                dossieProduto.getSituacoesDossie().stream().filter(sd -> !sd.getTipoSituacaoDossie().getTipoInicial()).findAny().ifPresent(sd -> {
                    String mensagem = MessageFormat.format("DPS.pDP.102 - O dossiê de produto encontra-se em situação impeditiva de exclusão de produtos. ID Produto = {0}", produtoContratadoAlteracaoDTO.getIdProduto());
                    throw new SimtrPermissaoException(mensagem);
                });

                // Efetua a exclusão da relação
                dossieProduto.getProdutosDossie().forEach(pd -> {
                    if (pd.getProduto().getId().equals(produtoContratadoAlteracaoDTO.getIdProduto())) {
                        this.dossieProdutoVinculosHelper.removeVinculoProduto(pd.getDossieProduto().getId(), pd.getProduto().getId());
                    }
                });
            });
        }

        // Aplica as exclusões de vinculos de dossies de cliente
        if (dossieProdutoAlteracaoDTO.getVinculosDossieClienteAlteracaoDTO() != null) {
            dossieProdutoAlteracaoDTO.getVinculosDossieClienteAlteracaoDTO().stream().filter(vdcp -> vdcp.isExclusao()).forEach(dossieClientePatchDTO -> {
                // Verifica se o dossie cliente solicitado exclusão esta vinculado ao dossiê de produto indicado
                DossieClienteProduto dossieClienteProduto;
                dossieClienteProduto = dossieProduto.getDossiesClienteProduto().stream().filter(dcp -> {

                    boolean criterioDossieCliente = dcp.getDossieCliente().getId().equals(dossieClientePatchDTO.getIdDossieCliente());
                    boolean criterioTipoRelacionamento = dcp.getTipoRelacionamento().getId().equals(dossieClientePatchDTO.getTipoRelacionamento());
                    boolean criterioRelacionado = (dcp.getDossieClienteRelacionado() == null && dossieClientePatchDTO.getIdDossieClienteRelacionado() == null)
                                                  || (Objects.nonNull(dcp.getDossieClienteRelacionado()) && dcp.getDossieClienteRelacionado().getId().equals(dossieClientePatchDTO.getIdDossieClienteRelacionado()));
                    boolean criterioSequencial = (dcp.getSequenciaTitularidade() == null && dossieClientePatchDTO.getSequenciaTitularidade() == null)
                                                 || (dcp.getSequenciaTitularidade().equals(dossieClientePatchDTO.getSequenciaTitularidade()));

                    return (criterioDossieCliente && criterioTipoRelacionamento && criterioRelacionado && criterioSequencial);
                }).findAny().orElseThrow(() -> {
                    Object[] parametros = new Object[] {
                        dossieClientePatchDTO.getIdDossieCliente(),
                        dossieClientePatchDTO.getTipoRelacionamento(),
                        dossieClientePatchDTO.getIdDossieClienteRelacionado(),
                        dossieClientePatchDTO.getSequenciaTitularidade()
                    };
                    String mensagem = MessageFormat.format("DPS.pDP.103 - Não foi localizado vinculado ao dossiê de cliente vinculado com as caracteristicas indicadas. ID Cliente = {0} | Tipo Relacionamento = {1} | ID Cliente Relacionado = {2} | Sequencia Titularidade = {3}", parametros);
                    return new SimtrRequisicaoException(mensagem);
                });

                // Permite excluir relacionamentos apenas se dossiê estiver em situação definida como tipo inicial (RASCUNHO)
                dossieProduto.getSituacoesDossie().stream().filter(sd -> !sd.getTipoSituacaoDossie().getTipoInicial()).findAny().ifPresent(sd -> {

                    String nomeSituacao = sd.getTipoSituacaoDossie().getNome();
                    String mensagem = MessageFormat.format("DPS.pDP.104 - O dossiê de produto encontra-se em situação impeditiva de exclusão de relacionamento de vinculos de pessoas. Situacao Atual = {0} | ID Dosie Cliente = {1}", nomeSituacao, dossieClientePatchDTO.getIdDossieCliente());
                    throw new SimtrPermissaoException(mensagem);
                });

                // Efetua a exclusão da relação
                this.dossieProdutoVinculosHelper.removeVinculoDossieCliente(dossieClienteProduto);
            });
        }

        // Aplica as exclusões de garantias informadas
        if (dossieProdutoAlteracaoDTO.getGarantiasInformadasAlteracaoDTO() != null) {
            dossieProdutoAlteracaoDTO.getGarantiasInformadasAlteracaoDTO().stream().filter(gi -> gi.isExclusao()).forEach(garantiaInformadaAlteracaoDTO -> {
                // Verifica se o garantia informada que foi solicitado exclusão esta vinculado ao dossiê de produto indicado

                String mensagemGarantia = MessageFormat.format("DPS.pDP.105 - A garantia que foi solicitado exclusão não foi localizada vinculada ao dossiê indicado. ID Garatia = {0} | ID Produto = {1}", garantiaInformadaAlteracaoDTO.getIdentificadorGarantia(), garantiaInformadaAlteracaoDTO.getIdentificadorProduto());
                // Valida se a condição entre a garantia e produto indicados para exclusão da garantia informada existe.
                if (Objects.isNull(garantiaInformadaAlteracaoDTO.getIdentificadorProduto())) {
                    // Caso o patch para garantia informada indique um produto: Filtra as garantias informadas dossiê em busca de de alguma que contenha a garantia indicada e
                    // tenhha o produto vinculado nulo.
                    // Lança a exceção caso não encontre nenhuma garantia informada vinculado ao dossiê de produto com a garantia indicada e o produto nulo.
                    dossieProduto.getGarantiasInformadas().stream().filter(gi -> {
                        return ((gi.getGarantia().getId().equals(garantiaInformadaAlteracaoDTO.getIdentificadorGarantia()))
                                && (gi.getProduto() == null));
                    }).findAny().orElseThrow(() -> {
                        return new SimtrRequisicaoException(mensagemGarantia);
                    });
                } else {
                    // Caso o patch para garantia informada não indique um produto: Filtra as garantias informadas dossiê em busca de de alguma que contenha a garantia indicada e
                    // tenhha o produto vinculado indicado tamnbém.
                    // Lança a exceção caso não encontre nenhuma garantia informada vinculado ao dossiê de produto com a garantia e produto indicados.
                    dossieProduto.getGarantiasInformadas().stream().filter(gi -> {
                        return ((gi.getGarantia().getId().equals(garantiaInformadaAlteracaoDTO.getIdentificadorGarantia()))
                                && (Objects.nonNull(gi.getProduto()))
                                && (gi.getProduto().getId().equals(garantiaInformadaAlteracaoDTO.getIdentificadorProduto())));
                    }).findAny().orElseThrow(() -> {
                        return new SimtrRequisicaoException(mensagemGarantia);
                    });
                }

                // Permite excluir garantias apenas se dossiê estiver em situação definida como tipo inicial (RASCUNHO)
                dossieProduto.getSituacoesDossie().stream().filter(sd -> !sd.getTipoSituacaoDossie().getTipoInicial()).findAny().ifPresent(sd -> {
                    String nomeSituacao = sd.getTipoSituacaoDossie().getNome();
                    String mensagemSituacao = MessageFormat.format("DPS.pDP.106 - O dossiê de produto encontra-se em situação impeditiva de exclusão de relacionamento de vinculos de garantias. Situacao Atual = {0} | ID Garantia = {1} | ID Produto = {2}", nomeSituacao, garantiaInformadaAlteracaoDTO.getIdentificadorGarantia(), garantiaInformadaAlteracaoDTO.getIdentificadorProduto());
                    throw new SimtrRequisicaoException(mensagemSituacao);
                });

                // Efetua a exclusão da relação
                if (Objects.isNull(garantiaInformadaAlteracaoDTO.getIdentificadorProduto())) {
                    dossieProduto.getGarantiasInformadas().forEach(gi -> {
                        if ((gi.getGarantia().getId().equals(garantiaInformadaAlteracaoDTO.getIdentificadorGarantia())) && (gi.getProduto() == null)) {
                            this.dossieProdutoVinculosHelper.removeVinculoGarantiaInformada(gi.getDossieProduto().getId(), gi.getGarantia().getId(), null);
                        }
                    });
                } else {
                    dossieProduto.getGarantiasInformadas().forEach(gi -> {
                        if ((gi.getGarantia().getId().equals(garantiaInformadaAlteracaoDTO.getIdentificadorGarantia())) && (gi.getProduto() != null)
                            && (gi.getProduto().getId().equals(garantiaInformadaAlteracaoDTO.getIdentificadorProduto()))) {
                            this.dossieProdutoVinculosHelper.removeVinculoGarantiaInformada(gi.getDossieProduto().getId(), gi.getGarantia().getId(), gi.getProduto().getId());
                        }
                    });
                }
            });
        }

        DossieProduto dossieProdutoAtualizado = _self.getById(id, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);

        // PROCESSO
        if (Objects.nonNull(dossieProdutoAlteracaoDTO.getElementosConteudoDTO())) {
            this.dossieProdutoVinculosHelper.insereDocumentosElementoConteudoProcesso(canal, dossieProduto, dossieProdutoAlteracaoDTO.getElementosConteudoDTO());
        }

        // PRODUTOS
        if (Objects.nonNull(dossieProdutoAlteracaoDTO.getProdutosContratadosAlteracaoDTO())) {
            dossieProdutoAlteracaoDTO.getProdutosContratadosAlteracaoDTO().stream().filter(pc -> !pc.isExclusao()).forEach(produtoContratadoPatchDTO -> {

                // Atualiza o registro do produto associado ao dossiê se o id do produto for encontrado for encontrado.
                // - Lança uma exceção indicando impedimento caso o dossiê já possua alguma situação conforme vinculada
                ProdutoDossie produtoDossieRetornado = dossieProdutoAtualizado.getProdutosDossie().stream()
                                                                              .filter(pd -> pd.getProduto().getId().equals(produtoContratadoPatchDTO.getIdProduto()))
                                                                              .findFirst()
                                                                              .map(pd -> {
                                                                                  // Lança exceção indicando impedimento de alteração do vinculo de produtos ao dossiê se algum
                                                                                  // tratamento conforme já foi realizado.
                                                                                  if (situacaoConformeExistente) {
                                                                                      String mensagemSituacao = "DPS.pDP.201 - O dossiê de produto encontra-se em situação impeditiva de alteração de vinculos de produtos por já possuir tratamento conforme realizado.";
                                                                                      throw new SimtrRequisicaoException(mensagemSituacao);
                                                                                  }
                                                                                  this.produtoDossieServico.atualizaProdutoDossie(pd.getId());
                                                                                  return pd;
                                                                              })
                                                                              // Caso o produto indicado não seja localizado no dossiê:
                                                                              // - Inclui um novo vinculo de produto se o dossiê ainda estiver em situação rascunho; ou
                                                                              // - Lança uma exceção indicando impedimento caso o dossiê já tenha sido submetido.
                                                                              .orElseGet(() -> {
                                                                                  if (situacaoAtualDossie.getTipoSituacaoDossie().equals(tipoSituacaoRascunho)
                                                                                      || situacaoAtualDossie.getTipoSituacaoDossie().equals(tipoSituacaoEmAlimentacao)) {
                                                                                      Produto produto;
                                                                                      if (produtoContratadoPatchDTO.getIdProduto() != null) {
                                                                                          produto = this.produtoServico.getById(produtoContratadoPatchDTO.getIdProduto(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
                                                                                      } else {
                                                                                          produto = this.produtoServico.getByOperacaoModalidade(produtoContratadoPatchDTO.getOperacao(), produtoContratadoPatchDTO.getModalidade(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
                                                                                      }
                                                                                      return this.dossieProdutoVinculosHelper.criaVinculoProduto(dossieProduto, produto);
                                                                                  } else {
                                                                                      String mensagemSituacao = MessageFormat.format("DPS.pDP.202 - O dossiê de produto encontra-se em situação impeditiva de inclusão de vinculos de produtos. Situacao Atual = {0} | ID Produto = {1}", situacaoAtualDossie.getTipoSituacaoDossie()
                                                                                                                                                                                                                                                                                                               .getNome(), produtoContratadoPatchDTO.getIdProduto());
                                                                                      throw new SimtrRequisicaoException(mensagemSituacao);
                                                                                  }
                                                                              });
                this.dossieProdutoVinculosHelper.insereDocumentosElementoConteudoProduto(canal, dossieProduto, produtoContratadoPatchDTO);

                // Realiza o vinculo das respostas de formulario associadas ao produto contratado nas condições indicadas, com dossiê de produto criado
                if (produtoContratadoPatchDTO.getRespostasFormularioDTO() != null) {
                    produtoContratadoPatchDTO.getRespostasFormularioDTO().forEach(respostaFormulario -> {
                        CampoFormulario campoFormulario = this.campoFormularioServico.getById(respostaFormulario.getIdCampoFomulario(), Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
                        if (campoFormulario == null) {
                            throw new SimtrRequisicaoException(MessageFormat.format("DPS.pDP.203 - Campo Formulario não localizado para identificador fornecido. ID = {0}", respostaFormulario.getIdCampoFomulario()));
                        }

                        this.dossieProdutoVinculosHelper.vinculaRespostaFormularioDossieProduto(dossieProduto, campoFormulario, respostaFormulario.getResposta(), respostaFormulario.getOpcoesSelecionadas(), null, produtoDossieRetornado, null);
                    });
                }
            });
        }

        // CLIENTES
        if (dossieProdutoAlteracaoDTO.getVinculosDossieClienteAlteracaoDTO() != null) {
            // Captura a lista de IDs dos dossiês de cliente vinculados ao dossiê de produto, somados aos novos informados,
            // para checagem de existencia previa no caso de inclusão de novo vinculo;
            List<Long> idsDossiesClienteVinculoPrincipal = dossieProdutoAtualizado.getDossiesClienteProduto().stream()
                                                                                  .map(dcp -> dcp.getDossieCliente().getId()).distinct()
                                                                                  .collect(Collectors.toList());
            idsDossiesClienteVinculoPrincipal.addAll(dossieProdutoAlteracaoDTO.getVinculosDossieClienteAlteracaoDTO().stream()
                                                                              .filter(vdcp -> !vdcp.isExclusao())
                                                                              .map(vdcp -> vdcp.getIdDossieCliente())
                                                                              .filter(i -> !idsDossiesClienteVinculoPrincipal.contains(i)).distinct()
                                                                              .collect(Collectors.toList()));

            // Percorre a lista de alterações indicadas aos vinculos de pessoas para execução das alterações
            dossieProdutoAlteracaoDTO.getVinculosDossieClienteAlteracaoDTO().stream()
                                     .filter(vdcp -> !vdcp.isExclusao())
                                     .forEach(dossieClientePatchDTO -> {
                                         DossieClienteProduto dossieClienteProdutoRetornado;
                                         dossieClienteProdutoRetornado = dossieProdutoAtualizado.getDossiesClienteProduto().stream().filter(dcp -> {

                                             boolean criterioDossieCliente = dcp.getDossieCliente().getId().equals(dossieClientePatchDTO.getIdDossieCliente());
                                             boolean criterioTipoRelacionamento = dcp.getTipoRelacionamento().getId().equals(dossieClientePatchDTO.getTipoRelacionamento());
                                             boolean criterioRelacionado = (dcp.getDossieClienteRelacionado() == null && dossieClientePatchDTO.getIdDossieClienteRelacionado() == null)
                                                                           || (dcp.getDossieClienteRelacionado() != null
                                                                               && dcp.getDossieClienteRelacionado().getId().equals(dossieClientePatchDTO.getIdDossieClienteRelacionado()))
                                                                           || (dcp.getDossieClienteRelacionado() != null
                                                                               && dcp.getDossieClienteRelacionado().getId().equals(dossieClientePatchDTO.getIdDossieClienteRelacionadoAnterior()));
                                             boolean criterioSequencial = (dcp.getSequenciaTitularidade() == null && dossieClientePatchDTO.getSequenciaTitularidade() == null)
                                                                          || dcp.getSequenciaTitularidade().equals(dossieClientePatchDTO.getSequenciaTitularidadeAnterior())
                                                                          || dcp.getSequenciaTitularidade().equals(dossieClientePatchDTO.getSequenciaTitularidade());

                                             return (criterioDossieCliente && criterioTipoRelacionamento && criterioRelacionado && criterioSequencial);
                                         }).findFirst().map(dossieClienteProduto -> {
                                             // Se o registro for localizado, realiza as alterações pertinentes

                                             // Atualiza o vinculo modificando o dossiê de cliente relacionado ou realiza alteração de sequencia de titularidade conforme o caso.
                                             if (Objects.nonNull(dossieClientePatchDTO.getIdDossieClienteRelacionadoAnterior())) {

                                                 if (situacaoConformeExistente) {
                                                     String mensagemSituacao = "DPS.pDP.301 - O dossiê de produto encontra-se em situação impeditiva de alteração de vinculos de pessoas existentes por já possuir tratamento conforme realizado.";
                                                     throw new SimtrRequisicaoException(mensagemSituacao);
                                                 }

                                                 // Checa se o ID relacionado esta na lista de IDs principais indicando que o mesmo ainda será incluido;
                                                 boolean idDossieRelacionadoExistente = idsDossiesClienteVinculoPrincipal.contains(dossieClientePatchDTO.getIdDossieClienteRelacionado());

                                                 // Captura o Dossiê de Cliente desejado dentre os dossiês já existente na relação de vinculos da operação
                                                 DossieCliente novoDossieClienteRelacionado = null;
                                                 if (idDossieRelacionadoExistente) {
                                                     novoDossieClienteRelacionado = this.dossieClienteServico.getById(dossieClientePatchDTO.getIdDossieClienteRelacionado(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
                                                 }

                                                 // Se o novo dossiê de cliente relacionado não estiver associado ao dossiê, levanta uma exceção de impossibilidade de alteração.
                                                 if (Objects.isNull(novoDossieClienteRelacionado)) {
                                                     String mensagemSituacao = MessageFormat.format("DPS.pDP.302 - O dossiê de cliente relacionado não pode ser vinculado por não fazer parte dos atuais vinculos do dossiê de produto. Dossie de Cliente Relacionado = {0} | Dossie de Cliente Relacionado Anterior = {1}", dossieClientePatchDTO.getIdDossieClienteRelacionado(), dossieClientePatchDTO.getIdDossieClienteRelacionadoAnterior());
                                                     throw new SimtrRequisicaoException(mensagemSituacao);
                                                 }

                                                 // Ajusta o vinculo de dossiê de cliente localizado
                                                 this.dossieProdutoVinculosHelper.atualizaVinculoDossieClienteRelacionado(dossieClienteProduto, novoDossieClienteRelacionado);

                                             } else if (Objects.nonNull(dossieClientePatchDTO.getSequenciaTitularidade())) {
                                                 if (situacaoConformeExistente) {
                                                     String mensagemSituacao = "DPS.pDP.301 - O dossiê de produto encontra-se em situação impeditiva de alteração de vinculos de pessoas existentes por já possuir tratamento conforme realizado.";
                                                     throw new SimtrRequisicaoException(mensagemSituacao);
                                                 }
                                                 this.dossieProdutoVinculosHelper.atualizaVinculoDossieClienteSequencial(dossieClienteProduto, dossieClientePatchDTO.getSequenciaTitularidade());
                                             }

                                             return dossieClienteProduto;
                                         }).orElseGet(() -> {
                                             // Se registro não localizado realiza a inclusão de novo vinculo

                                             // Captura o dossiê de cliente para criação do vinculo
                                             DossieCliente dossieCliente = this.dossieClienteServico.getById(dossieClientePatchDTO.getIdDossieCliente(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);

                                             // Captura o dossiê de cliente relacionado se for informado para criação do vinculo
                                             DossieCliente dossieClienteRelacionado = null;
                                             if (Objects.nonNull(dossieClientePatchDTO.getIdDossieClienteRelacionado())) {
                                                 dossieClienteRelacionado = this.dossieClienteServico.getById(dossieClientePatchDTO.getIdDossieClienteRelacionado(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
                                             }

                                             // Valida se o dossiê de cliente informado foi localizado
                                             if (Objects.isNull(dossieCliente)) {
                                                 String mensagemSituacao = MessageFormat.format("DPS.pDP.303 - Dossiê de Cliente não localizado para identificador fornecido. ID = {0}", dossieClientePatchDTO.getIdDossieCliente());
                                                 throw new SimtrRequisicaoException(mensagemSituacao);
                                             }

                                             // Valida se o dossiê de cliente relacionado informado foi localizado
                                             if ((Objects.nonNull(dossieClientePatchDTO.getIdDossieClienteRelacionado()))
                                                 && (Objects.isNull(dossieClienteRelacionado))) {
                                                 String mensagemSituacao = MessageFormat.format("DPS.pDP.304 - Dossiê de Cliente Relacionado não localizado para identificador fornecido. ID = {0}", dossieClientePatchDTO.getIdDossieClienteRelacionado());
                                                 throw new SimtrRequisicaoException(mensagemSituacao);
                                             }

                                             TipoRelacionamento tipoRelacionamento = this.tipoRelacionamentoServico.getById(dossieClientePatchDTO.getTipoRelacionamento());

                                             // Realiza as validações necessarias se o tipo de relacionamento informado indica necessidade de dossiê relacionado.
                                             if (tipoRelacionamento.getIndicadorRelacionado()) {
                                                 // Valida o dossiê de cliente relacionado informado se existente.
                                                 if (dossieClienteRelacionado == null) {
                                                     String mensagemSituacao = MessageFormat.format("DPS.pDP.305 - Dossiê de Cliente Relacionado é obrigatório para o tipo de relacionamento informado. Tipo de Relacionamento = {0}", tipoRelacionamento.getNome());
                                                     throw new SimtrRequisicaoException(mensagemSituacao);
                                                 } else {
                                                     // Verifica se o dossiê de cliente relacionado possui referencia:
                                                     // Na lista de dossiês de cliente vinculados ao dossiê de produto já existente; ou
                                                     // Em processo de inserção.
                                                     Long idDossieRelacionado = dossieClienteRelacionado.getId();
                                                     boolean dossieRelacionadoPresente = idsDossiesClienteVinculoPrincipal.stream()
                                                                                                                          .filter(i -> i.equals(idDossieRelacionado))
                                                                                                                          .findAny()
                                                                                                                          .isPresent();
                                                     if (!dossieRelacionadoPresente) {
                                                         String mensagemSituacao = MessageFormat.format("DPS.pDP.306 - Dossiê de Cliente Relacionado informado não possui vinculo no dossiê de produto. Dossiê de Cliente Relacionado = {0}", dossieClientePatchDTO.getIdDossieClienteRelacionado());
                                                         throw new SimtrRequisicaoException(mensagemSituacao);
                                                     }
                                                 }
                                             }

                                             // Realiza as validações necessarias se o tipo de relacionamento informado indica necessidade informação de sequência.
                                             if (tipoRelacionamento.getIndicadorSequencia()) {
                                                 // Valida a sequencia de titularidade informada e se maior ou igual a 1
                                                 if ((dossieClientePatchDTO.getSequenciaTitularidade() < 1)
                                                     || (Objects.isNull(dossieClientePatchDTO.getSequenciaTitularidade()))) {
                                                     String mensagemSituacao = MessageFormat.format("DPS.pDP.307 - Sequencia de titularidade informada é invalida. Sequencia de Titularidade = {0}", dossieClientePatchDTO.getSequenciaTitularidade());
                                                     throw new SimtrRequisicaoException(mensagemSituacao);
                                                 }
                                             }

                                             // Cria relação entre o dossiê de cliente e o dossiê de produto definindo o tipo de vinculo
                                             return this.dossieProdutoVinculosHelper.criaVinculoDossieCliente(dossieCliente, dossieClienteRelacionado, dossieProduto, tipoRelacionamento, dossieClientePatchDTO.getSequenciaTitularidade());
                                         });
                                         // Atualiza os documentos enviados
                                         this.dossieProdutoVinculosHelper.insereDocumentosVinculoDossieCliente(canal, dossieProduto, dossieClienteProdutoRetornado, dossieClientePatchDTO);

                                         // Realiza o vinculo das respostas de formulario associadas ao vinculo de pessoa nas condições indicadas, com dossiê de produto criado
                                         if (dossieClientePatchDTO.getRespostasFormularioDTO() != null) {
                                             dossieClientePatchDTO.getRespostasFormularioDTO().forEach(respostaFormulario -> {
                                                 CampoFormulario campoFormulario = this.campoFormularioServico.getById(respostaFormulario.getIdCampoFomulario(), Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
                                                 if (campoFormulario == null) {
                                                     throw new SimtrRequisicaoException(MessageFormat.format("DPS.pDP.308 - Campo Formulario não localizado para identificador fornecido. ID = {0}", respostaFormulario.getIdCampoFomulario()));
                                                 }

                                                 this.dossieProdutoVinculosHelper.vinculaRespostaFormularioDossieProduto(dossieProduto, campoFormulario, respostaFormulario.getResposta(), respostaFormulario.getOpcoesSelecionadas(), dossieClienteProdutoRetornado, null, null);
                                             });
                                         }
                                     });
        }

        // GARANTIAS
        if (dossieProdutoAlteracaoDTO.getGarantiasInformadasAlteracaoDTO() != null) {
            dossieProdutoAlteracaoDTO.getGarantiasInformadasAlteracaoDTO().stream().filter(gi -> !gi.isExclusao()).forEach(garantiaInformadaAlteracaoDTO -> {
                // Realiza a manutenção da garantia informada relacionada a um produto
                if (garantiaInformadaAlteracaoDTO.getIdentificadorProduto() == null) {
                    GarantiaInformada garantiaInformadaRetornada = dossieProdutoAtualizado.getGarantiasInformadas().stream().filter(gi -> {
                        return (garantiaInformadaAlteracaoDTO.getIdentificadorGarantia() != null && gi.getId().equals(garantiaInformadaAlteracaoDTO.getId())
                                && (gi.getGarantia().getId().equals(garantiaInformadaAlteracaoDTO.getIdentificadorGarantia()))
                                && (Objects.isNull(gi.getProduto())));
                    }).findFirst().map(garantiaInformada -> {
                        // Atualiza as informações relacionadas a garantia informada apenas se ainda não houve registro de tratamento realizado com resultado conforme
                        if (!situacaoConformeExistente) {
                            this.garantiaInformadaServico.atualizaGarantiaInformada(garantiaInformada.getId(), garantiaInformadaAlteracaoDTO.getValor(), garantiaInformadaAlteracaoDTO.getIdentificadoresClientesAvalistas());
                        }
                        return garantiaInformada;
                    }).orElseGet(() -> {
                        Garantia garantia = this.garantiaServico.getById(garantiaInformadaAlteracaoDTO.getIdentificadorGarantia(), Boolean.FALSE, Boolean.TRUE, Boolean.TRUE);
                        if (Objects.isNull(garantia)) {
                            throw new SimtrRequisicaoException(MessageFormat.format("DPS.pDP.401 - Garantia não localizada para identificador fornecido utilizado no registro de garantia. ID Garantia = {0} | ID Produto = {1}", garantiaInformadaAlteracaoDTO.getIdentificadorGarantia(), garantiaInformadaAlteracaoDTO.getIdentificadorProduto()));
                        }
                        return this.dossieProdutoVinculosHelper.criaVinculoGarantiaInformada(dossieProduto, garantia, null, garantiaInformadaAlteracaoDTO);
                    });

                    // Insere os documentos enviados vinculados ao registro de garantia informada localizada ou para o novo registro criado
                    this.dossieProdutoVinculosHelper.insereDocumentosGarantiaInformada(canal, dossieProduto, garantiaInformadaRetornada, garantiaInformadaAlteracaoDTO);

                    // Realiza o vinculo das respostas de formulario associadas ao vinculo de pessoa nas condições indicadas, com dossiê de produto criado
                    if (garantiaInformadaAlteracaoDTO.getRespostasFormularioDTO() != null) {
                        garantiaInformadaAlteracaoDTO.getRespostasFormularioDTO().forEach(respostaFormulario -> {
                            CampoFormulario campoFormulario = this.campoFormularioServico.getById(respostaFormulario.getIdCampoFomulario(), Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
                            if (campoFormulario == null) {
                                throw new SimtrRequisicaoException(MessageFormat.format("DPS.pDP.402 - Campo Formulario não localizado para identificador fornecido. ID = {0}", respostaFormulario.getIdCampoFomulario()));
                            }

                            this.dossieProdutoVinculosHelper.vinculaRespostaFormularioDossieProduto(dossieProduto, campoFormulario, respostaFormulario.getResposta(), respostaFormulario.getOpcoesSelecionadas(), null, null, garantiaInformadaRetornada);
                        });
                    }

                } else {
                    // Realiza a manutenção da garantia informada relacionada a um produto
                    GarantiaInformada garantiaInformadaRetornada;
                    garantiaInformadaRetornada = dossieProdutoAtualizado.getGarantiasInformadas().stream().filter(gi -> {
                        return ((garantiaInformadaAlteracaoDTO.getIdentificadorGarantia() != null && gi.getId().equals(garantiaInformadaAlteracaoDTO.getId()))
                                && (gi.getGarantia().getId().equals(garantiaInformadaAlteracaoDTO.getIdentificadorGarantia()))
                                && (Objects.nonNull(gi.getProduto()) && (gi.getProduto().getId().equals(garantiaInformadaAlteracaoDTO.getIdentificadorProduto()))));
                    }).findFirst().map(garantiaInformada -> {
                        // Atualiza as informações relacionadas a garantia informada apenas se ainda não houve registro de tratamento realizado com resultado conforme
                        if (!situacaoConformeExistente) {
                            this.garantiaInformadaServico.atualizaGarantiaInformada(garantiaInformada.getId(), garantiaInformadaAlteracaoDTO.getValor(), garantiaInformadaAlteracaoDTO.getIdentificadoresClientesAvalistas());
                        }
                        return garantiaInformada;
                    }).orElseGet(() -> {
                        Garantia garantia = this.garantiaServico.getById(garantiaInformadaAlteracaoDTO.getIdentificadorGarantia(), Boolean.FALSE, Boolean.TRUE, Boolean.TRUE);
                        Produto produto = this.produtoServico.getById(garantiaInformadaAlteracaoDTO.getIdentificadorProduto(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
                        if (Objects.isNull(garantia)) {
                            throw new SimtrRequisicaoException(MessageFormat.format("DPS.pDP.403 - Garantia não localizada para identificador fornecido utilizado no registro de garantia. ID Garantia = {0} | ID Produto = {1}", garantiaInformadaAlteracaoDTO.getIdentificadorGarantia(), garantiaInformadaAlteracaoDTO.getIdentificadorProduto()));
                        }
                        if (Objects.isNull(produto)) {
                            throw new SimtrRequisicaoException(MessageFormat.format("DPS.pDP.404 - Produto não localizado para identificador fornecido  utilizado no registro de garantia. ID Garantia = {0} | ID Produto = {1}", garantiaInformadaAlteracaoDTO.getIdentificadorGarantia(), garantiaInformadaAlteracaoDTO.getIdentificadorProduto()));
                        }
                        return this.dossieProdutoVinculosHelper.criaVinculoGarantiaInformada(dossieProduto, garantia, produto, garantiaInformadaAlteracaoDTO);
                    });

                    this.dossieProdutoVinculosHelper.insereDocumentosGarantiaInformada(canal, dossieProduto, garantiaInformadaRetornada, garantiaInformadaAlteracaoDTO);

                    // Realiza o vinculo das respostas de formulario associadas ao vinculo de pessoa nas condições indicadas, com dossiê de produto criado
                    if (garantiaInformadaAlteracaoDTO.getRespostasFormularioDTO() != null) {
                        garantiaInformadaAlteracaoDTO.getRespostasFormularioDTO().forEach(respostaFormulario -> {
                            CampoFormulario campoFormulario = this.campoFormularioServico.getById(respostaFormulario.getIdCampoFomulario(), Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
                            if (campoFormulario == null) {
                                throw new SimtrRequisicaoException(MessageFormat.format("DPS.pDP.405 - Campo Formulario não localizado para identificador fornecido. ID = {0}", respostaFormulario.getIdCampoFomulario()));
                            }

                            this.dossieProdutoVinculosHelper.vinculaRespostaFormularioDossieProduto(dossieProduto, campoFormulario, respostaFormulario.getResposta(), respostaFormulario.getOpcoesSelecionadas(), null, null, garantiaInformadaRetornada);
                        });
                    }
                }
            });
        }

        // FORMULARIO FASE
        if (dossieProdutoAlteracaoDTO.getRespostasFormularioDTO() != null) {
            ProcessoFaseDossie processoFaseAtual = dossieProduto.getProcessosFaseDossie().stream()
                    .max(Comparator.comparing(ProcessoFaseDossie::getId)).get();
            Processo processoFase = this.processoServico.getById(processoFaseAtual.getProcessoFase().getId());
            dossieProdutoAlteracaoDTO.getRespostasFormularioDTO().forEach(respostaFormularioDTO -> {
                CampoFormulario campoFormulario;
                campoFormulario = processoFase.getCamposFormularioFase().stream()
                                              .filter(cf -> cf.getId().equals(respostaFormularioDTO.getIdCampoFomulario()))
                                              .findFirst().orElseThrow(() -> {
                                                  String mensagem = MessageFormat.format("DPS.pDP.501 - O campo de formulario encaminhado para atualização não foi localizado vinculado ao processo fase do dossiê de produto. ID Campo Formulario = {0}", respostaFormularioDTO.getIdCampoFomulario());
                                                  return new SimtrRequisicaoException(mensagem);
                                              });

                this.dossieProdutoVinculosHelper.vinculaRespostaFormularioDossieProduto(dossieProduto, campoFormulario, respostaFormularioDTO.getResposta(), respostaFormularioDTO.getOpcoesSelecionadas(), null, null, null);
            });
        }

        // Realiza a finalização do dossiê de produto
        if (dossieProdutoAlteracaoDTO.isFinalizacao()) {
             dossieProduto.setPesoPrioridade(null);
             dossieProduto.setMatriculaPriorizado(null);
             dossieProduto.setUnidadePriorizado(null);
            // Verifica a situação que dossiê se encontra e aplica a fase seguinte definida pelo ciclo de vida.
            // Para os caso de aguardando tratamento deverá ser utilizado endpoint especifico.
            if (tipoSituacaoRascunho.equals(situacaoAtualDossie.getTipoSituacaoDossie())) {
                dossieProduto.setNomeContainerBPM(dossieProduto.getProcesso().getNomeContainerBPM());
                dossieProduto.setNomeProcessoBPM(dossieProduto.getProcesso().getNomeProcessoBPM());
                this.situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoCriado, null, null);
                this.update(dossieProduto);
            } else if (tipoSituacaoEmAlimentacao.equals(situacaoAtualDossie.getTipoSituacaoDossie())) {
                this.situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoAlimentacaoFinalizada, null, null);
                //Caso o processo BPM esteja na situação "AGUARDANDO_TRATAMENTO" é porque o processo foi retirado da fila e devolve o mesmo
                if (this.bpmServico.verificaInstanciaEsperaSinalTratamento(dossieProduto)) {
                    this.situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoAguardandoTratamento, null, null);
                } 
            } else if (tipoSituacaoEmComplementacao.equals(situacaoAtualDossie.getTipoSituacaoDossie())) {
                this.situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoAlimentacaoFinalizada, null, null);
            } else if (tipoSituacaoAnaliseSeguranca.equals(situacaoAtualDossie.getTipoSituacaoDossie())) {
                this.situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoSegurancaFinalizado, null, null);
            }
        } else {
            // Registra mesma situação apresentada no dossiê de produto indicando a intervenção de um usuário
            this.situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, situacaoAtualDossie.getTipoSituacaoDossie(), null, null);
        }
    }

    /**
     *<p>Exclui em ordem decrescente as últimas situações de um dossiê de produto.</p>
     * @param idDossieProduto Identificador do dossiê de produto.
     * @param quantidade Número de situações de dossiê de produto a serem excluídas.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
    })
    public void excluiSituacaoDossieProduto(Long idDossieProduto, Integer quantidade) {
        DossieProduto dossieProduto = this.getById(idDossieProduto, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (Objects.isNull(dossieProduto)) {
            String mensagem = MessageFormat.format("DPS.eSDP.001 - Dossiê de produto não localizado sob identificador informado. ID = {0}", idDossieProduto);
            throw new SimtrRequisicaoException(mensagem);
        }
        List<SituacaoDossie> situacoesDossie = dossieProduto.getSituacoesDossie().stream()
                                                                                .sorted(Comparator.comparingLong(SituacaoDossie::getId)
                                                                                .reversed())
                                                                                .collect(Collectors.toList());
        if(Objects.isNull(quantidade) || quantidade.equals(0) || quantidade >= situacoesDossie.size()) {
            StringBuilder builder = new StringBuilder();
            builder.append("DPS.eSDP.002 - A quantidade de situações a serem removidas deve ser um número superior a 0 e inferior ao número de situações do dossiê de produto. ");
            builder.append("Valor Informado = {0} | Total de Situações do Dossiê de Produto = {1}");
            String mensagem = MessageFormat.format(builder.toString(), quantidade, situacoesDossie.size());
            throw new SimtrRequisicaoException(mensagem);
        }
        for (int i = 0; i < quantidade; i++) {
            this.situacaoDossieServico.delete(situacoesDossie.get(i));
        }
    }

    public DossieProduto createDossieProduto(Processo processo, Canal canal, boolean rascunho, boolean finalizado) {
        Calendar dataHoraAtual = Calendar.getInstance();
        String responsavel = this.keycloakService.getMatricula().length() < 20 ? this.keycloakService.getMatricula() : canal.getSigla();
        Integer unidade = this.keycloakService.getLotacaoAdministrativa() == null ? 9999 : this.keycloakService.getLotacaoAdministrativa();

        Processo processoFaseInicial = processo.getRelacoesProcessoVinculoPai().stream().sorted(Comparator.comparing(rp -> rp.getOrdem())).findFirst().get().getProcessoFilho();

        DossieProduto dossieProduto = new DossieProduto();
        dossieProduto.setCanal(canal);
        dossieProduto.setProcesso(processo);
        dossieProduto.setUnidadeCriacao(this.keycloakService.getLotacaoAdministrativa());
        if (!rascunho || ConstantesUtil.CLIENT_ID_SIMTR_BPM.equals(canal.getClienteSSO())) {
            dossieProduto.setNomeContainerBPM(processo.getNomeContainerBPM());
            dossieProduto.setNomeProcessoBPM(processo.getNomeProcessoBPM());
        }
        
        ProcessoFaseDossie processoFaseDossie = new ProcessoFaseDossie();
        processoFaseDossie.setDataHoraInclusao(dataHoraAtual);
        processoFaseDossie.setDossieProduto(dossieProduto);
        processoFaseDossie.setProcessoFase(processoFaseInicial);
        processoFaseDossie.setResponsavel(responsavel);
        processoFaseDossie.setUnidade(unidade);

        dossieProduto.addProcessosFaseDossie(processoFaseDossie);

        SituacaoDossie situacaoDossie = new SituacaoDossie();
        situacaoDossie.setDataHoraInclusao(dataHoraAtual);
        situacaoDossie.setDossieProduto(dossieProduto);
        situacaoDossie.setResponsavel(responsavel);
        situacaoDossie.setUnidade(unidade);
        if (finalizado) {
            situacaoDossie.setTipoSituacaoDossie(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.FINALIZADO));
            dossieProduto.setDataHoraFinalizacao(Calendar.getInstance());
        } else if (rascunho) {
            situacaoDossie.setTipoSituacaoDossie(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.RASCUNHO));
        } else {
            situacaoDossie.setTipoSituacaoDossie(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.CRIADO));
        }

        dossieProduto.addSituacoesDossie(situacaoDossie);

        this.save(dossieProduto);

        this.entityManager.persist(situacaoDossie);
        this.entityManager.persist(processoFaseDossie);

        return dossieProduto;
    }

    // *************** METODOS PRIVADS *****************************//
    private String getQuery(boolean vinculacoesDossieCliente, boolean vinculacoesProdutosContratados, boolean vinculacoesDocumentos, boolean vinculacoesGarantias, boolean vinculacoesFormulario, boolean vinculacoesChecklist) {
        boolean carregaBase = !vinculacoesDossieCliente && !vinculacoesProdutosContratados && !vinculacoesDocumentos
                              && !vinculacoesGarantias && !vinculacoesFormulario && !vinculacoesChecklist;

        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT dp FROM DossieProduto dp ");

        if (carregaBase) {
            // Canal
            jpql.append(" LEFT JOIN FETCH dp.canal c ");
            // Processos de vinculação e fase
            jpql.append(" LEFT JOIN FETCH dp.processo pro ");
            jpql.append(" LEFT JOIN FETCH dp.processosFaseDossie pfd ");
            jpql.append(" LEFT JOIN FETCH pfd.processoFase prof ");
            // Situações do Dossiê
            jpql.append(" LEFT JOIN FETCH dp.situacoesDossie sd ");
            jpql.append(" LEFT JOIN FETCH sd.tipoSituacaoDossie tsd ");
            // Unidades de Tratamento
            jpql.append(" LEFT JOIN FETCH dp.unidadesTratamento ut ");
        }

        if (vinculacoesDossieCliente) {
            // Vinculos de Dossiês de Cliente
            jpql.append(" LEFT JOIN FETCH dp.dossiesClienteProduto dcp ");
            jpql.append(" LEFT JOIN FETCH dcp.tipoRelacionamento tr ");
            jpql.append(" LEFT JOIN FETCH dcp.dossieCliente dc ");
            jpql.append(" LEFT JOIN FETCH dcp.dossieClienteRelacionado dcr ");
        }
        if (vinculacoesProdutosContratados) {
            // Produtos Contratados
            jpql.append(" LEFT JOIN FETCH dp.produtosDossie pd ");
            jpql.append(" LEFT JOIN FETCH pd.produto prd ");
        }

        if (vinculacoesDocumentos) {
            // Informações do documento
            jpql.append(" LEFT JOIN FETCH dp.instanciasDocumento id ");
            jpql.append(" LEFT JOIN FETCH id.documento d ");
            jpql.append(" LEFT JOIN FETCH d.canalCaptura ccd ");
            jpql.append(" LEFT JOIN FETCH d.atributosDocumento add ");
            jpql.append(" LEFT JOIN FETCH d.tipoDocumento tpd ");
            jpql.append(" LEFT JOIN FETCH tpd.funcoesDocumentais fd ");
            jpql.append(" LEFT JOIN FETCH d.dossiesCliente dcid ");

            // Situações da Instancia
            jpql.append(" LEFT JOIN FETCH id.situacoesInstanciaDocumento sid ");
            jpql.append(" LEFT JOIN FETCH sid.situacaoDocumento sdoc ");

            // Vinculação com Elemento de Conteudo
            jpql.append(" LEFT JOIN FETCH id.elementoConteudo ec ");
            jpql.append(" LEFT JOIN FETCH ec.processo ecproc ");
            jpql.append(" LEFT JOIN FETCH ec.produto ecprod ");

            // Vinculação com Garantia Informada
            jpql.append(" LEFT JOIN FETCH id.garantiaInformada gi ");

            // Vinculação com a relação entre Dossiê Produto e Dossiê Cliente
            jpql.append(" LEFT JOIN FETCH id.dossieClienteProduto iddcp ");
        }

        if (vinculacoesGarantias) {
            // Vinculação com Garantia Informada
            jpql.append(" LEFT JOIN FETCH dp.garantiasInformadas gis ");
            jpql.append(" LEFT JOIN FETCH gis.garantia ggi ");
            jpql.append(" LEFT JOIN FETCH gis.produto pgi ");
            jpql.append(" LEFT JOIN FETCH gis.dossiesCliente dcgi ");
        }

        if (vinculacoesFormulario) {
            // Vinculação das respostas de formulario
            jpql.append(" LEFT JOIN FETCH dp.respostasDossie rd ");
            jpql.append(" LEFT JOIN FETCH rd.campoFormulario cf ");
            jpql.append(" LEFT JOIN FETCH rd.processoFase pfrd ");
            jpql.append(" LEFT JOIN FETCH rd.dossieClienteProduto dcprd ");
            jpql.append(" LEFT JOIN FETCH rd.produtoDossie pdrd ");
            jpql.append(" LEFT JOIN FETCH rd.garantiaInformada gird ");
            jpql.append(" LEFT JOIN FETCH cf.campoEntrada ce ");
            jpql.append(" LEFT JOIN FETCH cf.processo pcf ");
            jpql.append(" LEFT JOIN FETCH cf.processoFase pfcf ");
            jpql.append(" LEFT JOIN FETCH cf.tipoRelacionamento trcf ");
            jpql.append(" LEFT JOIN FETCH cf.produto pcf ");
            jpql.append(" LEFT JOIN FETCH cf.garantia gcf ");
            jpql.append(" LEFT JOIN FETCH rd.opcoesCampo oc ");
        }

        if (vinculacoesChecklist) {
            // Vinculação das informações de tratamento realizados
            jpql.append(" LEFT JOIN FETCH dp.checklistsAssociados cadp ");
            jpql.append(" LEFT JOIN FETCH cadp.checklist ccadp ");
            jpql.append(" LEFT JOIN FETCH cadp.processoFase pfcadp ");
            jpql.append(" LEFT JOIN FETCH ccadp.apontamentos accadp ");
            jpql.append(" LEFT JOIN FETCH cadp.instanciaDocumento idcadp ");
            jpql.append(" LEFT JOIN FETCH idcadp.documento didcadp ");
            jpql.append(" LEFT JOIN FETCH didcadp.canalCaptura ccdidcadp ");
            jpql.append(" LEFT JOIN FETCH didcadp.tipoDocumento tpddidcadp ");
            jpql.append(" LEFT JOIN FETCH cadp.verificacoes vcadp ");
            jpql.append(" LEFT JOIN FETCH vcadp.pareceres pvcadp ");
        }

        return jpql.toString();
    }
    
    /**
     * <p>
     * Método responsável por realizar a substituição das unidade tratamento (mtrtb018_unidade_tratamento) que determina as unidades que possuem permissão para manipular o dossiê de 
     * produto no momento atual. 
     * </p>
     * 
     * @param idDossieProduto - Identificado do Dossiê de Produto
     * @param listaUnidadesTratamento - Lista das unidades tratamento a serem substituidas na tabela (mtrtb018_unidade_tratamento)
     * 
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void atualizarUnidadesTratamento(Long idDossieProduto, List<Integer> listaUnidadesTratamento) {
       
        //Obtem o DossieProduto a partir do idDossieProduto informado 
        DossieProduto dossieProduto = this.getById(idDossieProduto, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        
        if (dossieProduto != null) {
            // Verifica no DossieProduto sé existe algum vinculo de UnidadeTratamento
            if (!dossieProduto.getUnidadesTratamento().isEmpty()) {
                // Remove o vinculo de Todas Unidade Tratamento 
                this.desvincularUnidadeTratamento(dossieProduto.getUnidadesTratamento());
            }
            
            // Verifica se foi informado alguma UnidadeTratamento para ser vinculada ao DossieProduto
            if (!listaUnidadesTratamento.isEmpty()) {
                listaUnidadesTratamento.forEach(unidadeTratamento -> {
                    // Cria o vinculo do DossieProduto com a Unidade Tratamento
                    this.vinculaUnidadeTratamento(dossieProduto, unidadeTratamento);
                });
            }
        } else {
            throw new SimtrRequisicaoException(MessageFormat.format("DPS.aUT.001 - Dossiê de produto não localizado sob identificador informado. ID = {0}", idDossieProduto));
        }
        
    }
    
    
    /**
     * <p>
     *    Método résponsavel por remover todo(s) o(s) vinculo(s) de Dossiê Produto a Unidade(s) de Tratamento
     * </p>
     * 
     * @param unidadesTratamento - Lista de Unidade(s) Tratamento 
     */
    public void desvincularUnidadeTratamento(Set<UnidadeTratamento> unidadesTratamento) {
        
        try {
            unidadesTratamento.forEach(ut -> {
            	UnidadeTratamentoId unidadeTratamentoId = new UnidadeTratamentoId(ut.getDossieProduto().getId(), ut.getUnidade());
                UnidadeTratamento unidade = this.entityManager.find(UnidadeTratamento.class, unidadeTratamentoId);
                this.entityManager.remove(unidade);
            });
        } catch (RuntimeException e) {
            throw new SimtrRequisicaoException("DPS.dUT.001 - Erro ao remover vinculo(s) do Dossiê Produto a(s) Unidade(s) Tratamento.");
        }
    }

    /**
     * <p>
     *  Método résponsavel por vincular um determinado Dossiê Produto a uma Unidade Tratamento
     * </p>
     * @param dossieProduto - Dossiê Produto informado para ser vinculado
     * @param unidade - Id da Unidade Tratamento para ser vinculada
     */
    public void vinculaUnidadeTratamento(DossieProduto dossieProduto, Integer unidade) {
        try {
            
            UnidadeTratamento unidadeTratamento = new UnidadeTratamento();
            unidadeTratamento.setDossieProduto(dossieProduto);
            unidadeTratamento.setUnidade(unidade);
            this.entityManager.persist(unidadeTratamento);
            
        } catch (Exception e) {
            throw new SimtrRequisicaoException("DPS.vUT.001 - Erro ao vincular Dossiê Produto a uma Unidade Tratamento");
        }
    }

    /**
     * Método Realiza 3 macro ações para deletar um Dossie Produto.
     * Altera o status dos documentos junto ao GED
     * Aborta o processo junto ao BPM
     * Remove os registros de todas as entidades relacionadas ao dossiê produto junto a base de dados do SIMTR.
     * 
     * @param id Identificador do Dosiê Produto 
     * 
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
	public void deleteDossieProduto(Long id) {
    	//Obtem o DossieProduto a partir do idDossieProduto informado 
        DossieProduto dossieProduto = this.getById(id, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        Set<String> identificadoresSiecm = _self.deleteDossieProduto(dossieProduto);
        identificadoresSiecm.forEach(identificador -> {
        	siecmServico.alteraSituacaoTemporalidadeDocumentoSIECM(identificador, TemporalidadeDocumentoEnum.REMOVER, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
        });
    	
    }
    
    public Set<String> deleteDossieProduto(DossieProduto dossieProduto) {
        //Remove Todas as Situacoes do Dossie Produto        
        this.excluirSituacoesDossie(dossieProduto.getSituacoesDossie());
        
    	//Remove Todas as Situacoes do Dossie Produto
    	this.excluirRespostasDossieProduto(dossieProduto.getRespostasDossie());
    	
    	//Remove todas as unidades tratamento do Dossie Produto
    	this.desvincularUnidadeTratamento(dossieProduto.getUnidadesTratamento());

    	//Excluir os Checklist Assosciados
    	this.excluirChecklistsAssociados(dossieProduto.getChecklistsAssociados(), dossieProduto.getId());	
    	
    	Set<String> identificadoresSiecm = new HashSet<>();
    	dossieProduto.getInstanciasDocumento().forEach(instancia -> {
    		this.excluirSituacoesInstanciaDocumento(instancia.getSituacoesInstanciaDocumento());
			
			this.entityManager.remove(instancia);

			if(instancia.getDocumento().getInstanciasDocumento().isEmpty() && instancia.getDocumento().getDossiesCliente().isEmpty()) {
				if(Objects.nonNull(instancia.getDocumento().getCodigoGED())) {
					identificadoresSiecm.add(instancia.getDocumento().getCodigoGED());
				}
				if(Objects.nonNull(instancia.getDocumento().getCodigoSiecmTratado())) {
					identificadoresSiecm.add(instancia.getDocumento().getCodigoSiecmTratado());
				}
				this.entityManager.remove(instancia.getDocumento());
			}
			
			
    	});
    	
    	//caso o Dossiê Produto contenha um identificador GEd, deve ser abortado o processo junto ao BPM.
    	if(Objects.nonNull(dossieProduto.getNomeContainerBPM()) && Objects.nonNull(dossieProduto.getIdInstanciaProcessoBPM())) {
            bpmServico.abortarProcessoBPM(dossieProduto.getNomeContainerBPM(), dossieProduto.getIdInstanciaProcessoBPM(), dossieProduto.getId());
    	}
    	
        this.entityManager.remove(dossieProduto);
        
        return identificadoresSiecm;
		
	}
    
    /**
     * Consulta de todos os dossiês de produto que possuem a informação de Data/Hora de falha na comunicação com o BPM diferente de nulo realizando junção com a tabela de processos e retornando a lista de operações.
     *
     * @return Lista de Dossiê de produto localizado ou null caso nenhum dossiê de produto se enquadre neste cenario
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public List<DossieProduto> consultaFalhaBPM() {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT dp FROM DossieProduto dp ");
        jpql.append(" LEFT JOIN FETCH dp.processo p ");
        jpql.append(" WHERE dp.dataHoraFalhaBPM IS NOT NULL ");

        TypedQuery<DossieProduto> query = this.entityManager.createQuery(jpql.toString(), DossieProduto.class);
        
        return new ArrayList<>(query.getResultList());
    }

    /**
    * Exclusao das situacoes da instancia do Documento do Dossie Produto
    * 
    * @param situacoesInstanciaDocumento
    */
    private void excluirSituacoesInstanciaDocumento(Set<SituacaoInstanciaDocumento> situacoesInstanciaDocumento) {
    	 try {
    		 situacoesInstanciaDocumento.forEach(situacao -> {
	                SituacaoInstanciaDocumento situacaoRemover = this.entityManager.find(SituacaoInstanciaDocumento.class, situacao.getId());
	                if(Objects.nonNull(situacaoRemover)) {
	                	this.entityManager.remove(situacaoRemover);
	                }
	            });
	        } catch (RuntimeException e) {
	            throw new SimtrRequisicaoException("DPS.eSID.001 - Erro ao remover situações(s) da instancia do Documento do Dossiê Produto.");
	        }
    }
    
    /**
     * Realizada a exclusao das Verificações, Pareceres e dos checklists associados ao Dossie Produto.
     * 
     * @param checklistsAssociados
     * @param idDossieProduto 
     */
    private void excluirChecklistsAssociados(Set<ChecklistAssociado> checklistsAssociados, Long idDossieProduto) {
    	
    	checklistsAssociados.forEach(checklistAssociado ->{
    		//Removendo Verificacoes e Pareceres do Dossie Produto
    		checklistAssociado.getVerificacoes().forEach(verificacao -> {
    			try {
    				verificacao.getPareceres().forEach(parecer -> {
        				Parecer parecerExcluir = this.entityManager.find(Parecer.class, parecer.getId());
        				//Checagem realizada para evitar que caso o objeto ja tenha sido excluido, evite erro ao tentar excluir novamente
        				if(Objects.nonNull(parecerExcluir)) {
        					this.entityManager.remove(parecerExcluir);
        				}
        			});
        			Verificacao verificacaoExcluir = this.entityManager.find(Verificacao.class, verificacao.getId());
        			if(Objects.nonNull(verificacaoExcluir)) {
        				this.entityManager.remove(verificacaoExcluir);
        			}
				} catch (Exception e) {
					throw new SimtrRequisicaoException("DPS.eCA.001 - Erro ao remover verificações(s) do Dossiê Produto.");
				}

    		});

    		//Exclui o checklist associado
    		this.entityManager.remove(checklistAssociado);
    	});
    	
    	
	}

    /**
    * Realiza a exclusão da Situação do documento de um checklist envia ao SIECM a mudança de status do GED
	* 
	* @param checklistAssociado
	*/
	private void excluirSituacaoDocumento(ChecklistAssociado checklistAssociado) {
		if(Objects.nonNull(checklistAssociado.getInstanciaDocumento()) && Objects.nonNull(checklistAssociado.getInstanciaDocumento().getId())) {
			if(Objects.isNull(checklistAssociado.getInstanciaDocumento().getDocumento().getDossiesCliente()) || checklistAssociado.getInstanciaDocumento().getDocumento().getDossiesCliente().isEmpty()) {
				if(Objects.nonNull(checklistAssociado.getInstanciaDocumento().getDocumento().getCodigoGED())) {
					siecmServico.alteraSituacaoTemporalidadeDocumentoSIECM(checklistAssociado.getInstanciaDocumento().getDocumento().getCodigoGED(), TemporalidadeDocumentoEnum.VENCIDO, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
					
				}
				this.excluirSituacoesInstanciaDocumento(checklistAssociado.getInstanciaDocumento().getSituacoesInstanciaDocumento());
				this.entityManager.remove(checklistAssociado.getInstanciaDocumento());
				this.entityManager.remove(checklistAssociado.getInstanciaDocumento().getDocumento());
			}else {
				this.entityManager.remove(checklistAssociado.getInstanciaDocumento());
					
			}
		}
	}
    
    /**
     * Método que remove as situações do Dossiê Produto
     * 
     * @param situaacoes Respostas do dossie Produto
     */
    private void excluirSituacoesDossie(Set<SituacaoDossie> situaacoes) {
	  try {
            situaacoes.forEach(resposta -> {
            	SituacaoDossie situacaoDossie = this.entityManager.find(SituacaoDossie.class, resposta.getId());
				if(Objects.nonNull(situacaoDossie)) {
					this.entityManager.remove(situacaoDossie);
				}
            });
        } catch (RuntimeException e) {
            throw new SimtrRequisicaoException("DPS.eSD.001 - Erro ao remover situação(ões) do Dossiê Produto.");
        }
	}

    /**
    * Método que remove as respostas do Dossiê Produto
    * 
    * @param respostas Respostas do dossie Produto
    */
    private void excluirRespostasDossieProduto(Set<RespostaDossie> respostas) {
        try {
            respostas.forEach(resposta -> {
                RespostaDossie respostaDossie = this.entityManager.find(RespostaDossie.class, resposta.getId());
                if(Objects.nonNull(respostaDossie)) {
                	this.entityManager.remove(respostaDossie);
                }
            });
        } catch (RuntimeException e) {
            throw new SimtrRequisicaoException("DPS.eRDP.001 - Erro ao remover resposta(s) do Dossiê Produto.");
        }
    } 
}
