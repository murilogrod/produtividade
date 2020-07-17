package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
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

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.postgresql.util.PSQLException;

import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.simtr.controle.excecao.DossieException;
import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.modelo.entidade.ComposicaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.RegraDocumental;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.composicaodocumental.AlterarComposicaoDocumentalDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRAUD,
    ConstantesUtil.PERFIL_MTRTEC,
    ConstantesUtil.PERFIL_MTRDOSINT,
    ConstantesUtil.PERFIL_MTRDOSMTZ,
    ConstantesUtil.PERFIL_MTRDOSOPE,
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class ComposicaoDocumentalServico extends AbstractService<ComposicaoDocumental, Long> {

    @Inject
    private EntityManager entityManager;

    @Inject
    private Logger logger;

    @EJB
    private KeycloakService keycloakService;

    @Inject
    private RegraDocumentalServico regraDocumentalServico;

    private final Map<Long, ComposicaoDocumental> mapaId = new HashMap<>();
    private Calendar dataHoraUltimaAlteracao;

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Captura as composições documentais baseado em um codigo de operação e modalidade.
     *
     * @param codigoOperacao Codigo de operação ao qual a composição deve estar vinculada
     * @param codigoModalidade Codigo de modalidade ao qual a composição deve estar vinculada
     * @param indicadorConclusao Indicador se a composição deve ser de conclusão ou não. Caso seja enviado null trará composições vinculadas as duas opções
     * @return Lista contendo as composições localizadas.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public List<ComposicaoDocumental> getComposicoesByProduto(Integer codigoOperacao, Integer codigoModalidade, Boolean indicadorConclusao) {

        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT cd FROM ComposicaoDocumental cd ");
        jpql.append(" LEFT JOIN FETCH cd.regrasDocumentais rd ");
        jpql.append(" LEFT JOIN FETCH cd.produtos p ");
        jpql.append(" LEFT JOIN FETCH rd.tipoDocumento td ");
        jpql.append(" LEFT JOIN FETCH rd.funcaoDocumental fd ");
        jpql.append(" LEFT JOIN FETCH fd.tiposDocumento tds ");
        jpql.append(" WHERE p.operacao = :operacao ");
        jpql.append(" AND p.modalidade = :modalidade ");
        if (indicadorConclusao != null) {
            jpql.append(" AND cd.indicadorConclusao = :indicadorConclusao ");
        }

        TypedQuery<ComposicaoDocumental> query = this.entityManager.createQuery(jpql.toString(), ComposicaoDocumental.class);
        query.setParameter("operacao", codigoOperacao);
        query.setParameter("modalidade", codigoModalidade);
        if (indicadorConclusao != null) {
            query.setParameter("indicadorConclusao", indicadorConclusao);

        }

        return query.getResultList();
    }

    public List<ComposicaoDocumental> listComposicoesCadastroCaixa(TipoPessoaEnum tipoPessoaEnum) {
        this.carregarMapas();
        return this.mapaId.values().stream()
                .filter(c -> c.getIndicadorCadastroCAIXA())
                .filter(c -> c.getTipoPessoa().equals(tipoPessoaEnum))
                .collect(Collectors.toList());
    }

    public List<ComposicaoDocumental> list() {
        this.carregarMapas();
        return this.mapaId.values().stream().collect(Collectors.toList());        
    }
    
    public List<ComposicaoDocumental> list(boolean incluiRevogadas, boolean indicadorConclusao, TipoPessoaEnum tipoPessoaEnum) {
        this.carregarMapas();
        List<ComposicaoDocumental> composicoesRetorno = this.mapaId.values().stream()
                                                                   .filter(c -> c.getTipoPessoa().equals(tipoPessoaEnum))
                                                                   .filter(c -> c.getIndicadorConclusao().equals(indicadorConclusao))
                                                                   .collect(Collectors.toList());
        
        if (incluiRevogadas) {
            return composicoesRetorno;
        }
        
        return composicoesRetorno.stream().filter(c -> c.getDataHoraRevogacao() == null).collect(Collectors.toList());
    }

    public ComposicaoDocumental getById(Long id) {
        this.carregarMapas();
        return this.mapaId.get(id);
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
    })
    public void salvar(ComposicaoDocumental composicaoDocumental) {
        try {
            // Salva o registro incluindo no contexto transacional
            composicaoDocumental.setDataHoraInclusao(Calendar.getInstance());
            composicaoDocumental.setMatriculaInclusao(this.keycloakService.getMatricula());
            this.save(composicaoDocumental);

            // Percorre todos os elementos indicados na lista de regras, vincula a composição recem criada e salva a regra documental
            composicaoDocumental.getRegrasDocumentais().forEach(re -> {
                re.setComposicaoDocumental(composicaoDocumental);
                this.entityManager.persist(re);
            });
        } catch (PersistenceException pe) {
            // Percorre as exceções até identificar a causa raiz do problema
            Throwable problema = pe.getCause();
            while ((Objects.nonNull(problema.getCause())) && !(PSQLException.class.equals(problema.getClass()))) {
                problema = problema.getCause();
            }
            // Lança uma exceção indicando o estado impeditivo de exclusão devido a integridade de dados
            throw new SimtrEstadoImpeditivoException("CDS.s.001 - ".concat(problema.getLocalizedMessage()), pe);
        }
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
    })
    public void delete(final Long id) {
        ComposicaoDocumental composicaoDocumental = findById(id, Boolean.FALSE, Boolean.TRUE);
        try {
            composicaoDocumental.getRegrasDocumentais().forEach(re -> this.entityManager.remove(re));
            this.delete(composicaoDocumental);
        } catch (RuntimeException e) {
            final String msg = "CDS.d.001 - Erro ao excluir Composição Documental. ";
            this.logger.severe(msg.concat(e.getLocalizedMessage()));
            throw new DossieException(msg, e, true);
        }
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
    })
    public void updateRegrasDocumentais(final Long id, List<RegraDocumental> listaRegrasDocumentais) {

        ComposicaoDocumental composicaoDocumental = this.findById(id, Boolean.FALSE, Boolean.TRUE);

        if (Objects.isNull(composicaoDocumental)) {
            final String msg = MessageFormat.format("CDS.uRD.001 - Composição não encontrada. ID = {0}", id);
            throw new DossieException(msg, true);
        }

        composicaoDocumental.getRegrasDocumentais().forEach(r -> this.entityManager.remove(r));

        List<Integer> idsFuncoes = listaRegrasDocumentais.stream()
                                                         .filter(r -> Objects.nonNull(r.getFuncaoDocumental()))
                                                         .map(r -> r.getFuncaoDocumental().getId())
                                                         .collect(Collectors.toList());

        List<Integer> idsTipos = listaRegrasDocumentais.stream()
                                                       .filter(r -> Objects.nonNull(r.getTipoDocumento()))
                                                       .map(r -> r.getTipoDocumento().getId())
                                                       .collect(Collectors.toList());

        Set<RegraDocumental> regrasDocumentais = new HashSet<>();

        if (idsFuncoes != null && !idsFuncoes.isEmpty()) {

            StringBuilder jpqlFuncao = new StringBuilder();
            jpqlFuncao.append(" SELECT DISTINCT f FROM FuncaoDocumental f ");
            jpqlFuncao.append(" WHERE f.id IN (:ids) ");

            TypedQuery<FuncaoDocumental> queryFuncoes = this.entityManager.createQuery(jpqlFuncao.toString(), FuncaoDocumental.class);
            queryFuncoes.setParameter("ids", idsFuncoes);

            Set<FuncaoDocumental> funcoesDocumentais = new HashSet<>(queryFuncoes.getResultList());

            funcoesDocumentais.forEach(fd -> {
                RegraDocumental regraDocumental = new RegraDocumental();
                regraDocumental.setFuncaoDocumental(fd);
                regraDocumental.setComposicaoDocumental(composicaoDocumental);
                this.entityManager.persist(regraDocumental);
                regrasDocumentais.add(regraDocumental);
            });
        }

        if (idsTipos != null && !idsTipos.isEmpty()) {

            StringBuilder jpqlTipos = new StringBuilder();
            jpqlTipos.append(" SELECT DISTINCT t FROM TipoDocumento t ");
            jpqlTipos.append(" WHERE t.id IN (:ids) ");

            TypedQuery<TipoDocumento> queryTiposDocumento = this.entityManager.createQuery(jpqlTipos.toString(), TipoDocumento.class);
            queryTiposDocumento.setParameter("ids", idsTipos);

            Set<TipoDocumento> tiposDocumento = new HashSet<>(queryTiposDocumento.getResultList());

            tiposDocumento.forEach(td -> {
                RegraDocumental regraDocumental = new RegraDocumental();
                regraDocumental.setTipoDocumento(td);
                regraDocumental.setComposicaoDocumental(composicaoDocumental);
                this.entityManager.persist(regraDocumental);
                regrasDocumentais.add(regraDocumental);
            });
        }
        composicaoDocumental.setRegrasDocumentais(regrasDocumentais);

    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
    })
    public void aplicarPatch(Long idComposicao, AlterarComposicaoDocumentalDTO composicaoDocumentalDTO) {
        ComposicaoDocumental composicaoDocumental = this.getById(idComposicao);

        if (Objects.nonNull(composicaoDocumentalDTO.getIndicadorConclusao())) {
            composicaoDocumental.setIndicadorConclusao(composicaoDocumentalDTO.getIndicadorConclusao());
        }

        if (Objects.nonNull(composicaoDocumentalDTO.getNome())) {
            composicaoDocumental.setNome(composicaoDocumentalDTO.getNome());
        }

        this.updateRegrasDocumentais(composicaoDocumental, composicaoDocumentalDTO);
        this.update(composicaoDocumental);
    }

    // *********** Métodos Privados ***********
    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private void carregarMapas() {
        String jpqlUltimaAlteracao = "SELECT MAX(c.dataHoraUltimaAlteracao) FROM ComposicaoDocumental c";
        TypedQuery<Calendar> queryUltimaAlteracao = this.entityManager.createQuery(jpqlUltimaAlteracao, Calendar.class);
        Calendar ultimaAlteracao = queryUltimaAlteracao.getSingleResult();

        if (Objects.isNull(this.dataHoraUltimaAlteracao) || dataHoraUltimaAlteracao.before(ultimaAlteracao)) {

            StringBuilder jpql = new StringBuilder();
            jpql.append(" SELECT DISTINCT c FROM ComposicaoDocumental c ");
            jpql.append(" LEFT JOIN FETCH c.produtos p ");
            jpql.append(" LEFT JOIN FETCH c.regrasDocumentais rd ");
            jpql.append(" LEFT JOIN FETCH rd.tipoDocumento td ");
            jpql.append(" LEFT JOIN FETCH rd.funcaoDocumental fd ");
            jpql.append(" LEFT JOIN FETCH fd.tiposDocumento tdfd ");

            List<ComposicaoDocumental> composicoes = this.entityManager.createQuery(jpql.toString(), ComposicaoDocumental.class).getResultList();

            mapaId.clear();

            composicoes.forEach(composicao -> {
                mapaId.put(composicao.getId(), composicao);
            });

            this.dataHoraUltimaAlteracao = ultimaAlteracao;
        }
    }

    private ComposicaoDocumental findById(Long id, boolean carregaProdutos, boolean carregaRegras) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT cd FROM ComposicaoDocumental cd ");
        if (carregaRegras) {
            jpql.append(" LEFT JOIN FETCH cd.regrasDocumentais rd ");
        }

        if (carregaProdutos) {
            jpql.append(" LEFT JOIN FETCH cd.produtos p ");
        }

        jpql.append(" WHERE cd.id = :id ");

        TypedQuery<ComposicaoDocumental> query = this.entityManager.createQuery(jpql.toString(), ComposicaoDocumental.class);
        query.setParameter("id", id);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            this.logger.log(Level.ALL, "Composição não Localizada.", e);
            return null;
        }
    }

    private void updateRegrasDocumentais(ComposicaoDocumental composicaoDocumental, AlterarComposicaoDocumentalDTO composicaoDocumentalDTO) {
        composicaoDocumentalDTO.getRegrasExclusao().forEach(r -> this.regraDocumentalServico.delete(r));
        composicaoDocumentalDTO.getRegrasInclusao().forEach(r -> {
            RegraDocumental regra = r.prototype();
            regra.setComposicaoDocumental(composicaoDocumental);
            this.regraDocumentalServico.save(regra);
        });
    }
}
