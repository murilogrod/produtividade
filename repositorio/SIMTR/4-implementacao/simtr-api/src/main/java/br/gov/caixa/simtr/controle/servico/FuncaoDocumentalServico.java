package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.exception.ConstraintViolationException;
import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.controle.excecao.DossieException;
import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.funcaodocumental.FuncaoDocumentalManutencaoDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRTEC,
    ConstantesUtil.PERFIL_MTRAUD,
    ConstantesUtil.PERFIL_MTRDOSINT,
    ConstantesUtil.PERFIL_MTRDOSMTZ,
    ConstantesUtil.PERFIL_MTRDOSOPE,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class FuncaoDocumentalServico extends AbstractService<FuncaoDocumental, Integer> {

    @Inject
    private EntityManager entityManager;

    @EJB
    private TipoDocumentoServico tipoDocumentoServico;

    @Inject
    private Logger logger;

    private final Map<Integer, FuncaoDocumental> mapaId = new HashMap<>();
    private final Map<String, FuncaoDocumental> mapaNome = new HashMap<>();
    private Calendar dataHoraUltimaAlteracao;

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    public Calendar getDataHoraUltimaAlteracao() {
        return dataHoraUltimaAlteracao;
    }

    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private void carregarMapas() {
        String jpqlUltimaAlteracao = " SELECT MAX(fd.dataHoraUltimaAlteracao) FROM FuncaoDocumental fd ";
        TypedQuery<Calendar> queryUltimaAlteracao = this.entityManager.createQuery(jpqlUltimaAlteracao, Calendar.class);
        Calendar ultimaAlteracao = queryUltimaAlteracao.getSingleResult();

        if (Objects.isNull(this.dataHoraUltimaAlteracao) || dataHoraUltimaAlteracao.before(ultimaAlteracao)) {

            StringBuilder jpql = new StringBuilder();
            jpql.append(" SELECT fd FROM FuncaoDocumental fd ");
            jpql.append(" LEFT JOIN FETCH fd.tiposDocumento td ");
            jpql.append(" LEFT JOIN FETCH td.atributosExtracao ae ");
            jpql.append(" WHERE (td.atributosExtracao IS EMPTY) OR (ae.ativo = true) ");

            List<FuncaoDocumental> funcoes = this.entityManager.createQuery(jpql.toString(), FuncaoDocumental.class).getResultList();

            mapaNome.clear();
            mapaId.clear();

            funcoes.forEach(funcao -> {
                mapaId.put(funcao.getId(), funcao);
                mapaNome.put(funcao.getNome(), funcao);
            });

            this.dataHoraUltimaAlteracao = ultimaAlteracao;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<FuncaoDocumental> list() {
        this.carregarMapas();
        return new ArrayList<>(this.mapaId.values());
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public FuncaoDocumental getByNome(final String nome) {
        this.carregarMapas();
        return mapaNome.get(nome);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public FuncaoDocumental getById(final Integer id) {
        this.carregarMapas();
        return mapaId.get(id);
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void delete(final Integer id) {
        try {
            StringBuilder jpql = new StringBuilder();
            jpql.append(" DELETE FROM FuncaoDocumental fd ");
            jpql.append(" WHERE fd.id = :id ");
            Query queryFuncao = this.entityManager.createQuery(jpql.toString());
            queryFuncao.setParameter("id", id);
            queryFuncao.executeUpdate();
            this.dataHoraUltimaAlteracao = null;
        } catch (ConstraintViolationException e) {
            final String msg = "FDS.dFD.001 - Erro ao Excluir Parametrização de Função Documental. Este Registro está vinculado a outros. ";
            throw new DossieException(msg, e, true);
        }
    }

    /**
     * Metodo utilizado para localizar uma função documental baseada em seu identificador respeitando a consdição do contexto tsnsacional.
     *
     * @param id Identificador da função documental a ser localizada
     * @return Função Documental localizada ou nulo a mesam não seja localizada
     */
    public FuncaoDocumental findById(final Integer id) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT fd FROM FuncaoDocumental fd ");
        jpql.append(" LEFT JOIN FETCH fd.tiposDocumento td ");
        jpql.append(" WHERE fd.id = :id ");

        TypedQuery<FuncaoDocumental> query = this.entityManager.createQuery(jpql.toString(), FuncaoDocumental.class);
        query.setParameter("id", id);

        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            this.logger.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }

    /**
     * Método utilizado para salvar um novo registro de função documental com a lista de identificadores de tipos documentais que devem ser associados iniclamente a
     * função.
     *
     * @param funcaoDocumental Objeto que representa o prototipo de função documental a ser persistido.
     * @param identificadoresTiposDocumentosInclusaoVinculo Lista de identificadores dos tipos de documento que devem ser associados a função documental em
     *        persistência.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void save(FuncaoDocumental funcaoDocumental, List<Integer> identificadoresTiposDocumentosInclusaoVinculo) {

        // Atualiza a data e hora de ultima alteração do registro ára viabilizar a sensibilização de nova carga dos mapas
        funcaoDocumental.setDataHoraUltimaAlteracao(Calendar.getInstance());
        
        // Percorre todos os elementos indicados na lista, captura o tipo de documento no contexto transacional e inclui o vinculo com a função
        if (Objects.nonNull(identificadoresTiposDocumentosInclusaoVinculo) && !identificadoresTiposDocumentosInclusaoVinculo.isEmpty()) {
            identificadoresTiposDocumentosInclusaoVinculo.forEach(idInclusao -> {
                TipoDocumento tipoDocumento = this.tipoDocumentoServico.getById(idInclusao);
                tipoDocumento.addFuncoesDocumentais(funcaoDocumental);

                if (!funcaoDocumental.getTiposDocumento().contains(tipoDocumento)) {
                    funcaoDocumental.addTiposDocumento(tipoDocumento);
                }
            });
        }

        // Salva o registro incluindo no contexto transacional
        this.save(funcaoDocumental);
    }

    /**
     * Aplica as alterações encaminhadas na função documental indicada.
     *
     * @param id Identificador da função documental a ser alterada.
     * @param funcaoDocumentalManutencaoDTO Objeto contendo os atributos a serem alterados na aplicação do patch
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void aplicaPatch(Integer id, FuncaoDocumentalManutencaoDTO funcaoDocumentalManutencaoDTO) {

        FuncaoDocumental funcaoDocumental = this.findById(id);

        String mensagemValidacao = MessageFormat.format("FDS.aP.001 - Função documental não localizada para o identificador informado. ID = {0}", id);
        this.validaRecursoLocalizado(funcaoDocumental, mensagemValidacao);

        if (Objects.nonNull(funcaoDocumentalManutencaoDTO.getNome())) {
            funcaoDocumental.setNome(funcaoDocumentalManutencaoDTO.getNome());
        }

        if (Objects.nonNull(funcaoDocumentalManutencaoDTO.getIndicadorApoioNegocio())) {
            funcaoDocumental.setUsoApoioNegocio(funcaoDocumentalManutencaoDTO.getIndicadorApoioNegocio());
        }

        if (Objects.nonNull(funcaoDocumentalManutencaoDTO.getIndicadorDossieDigital())) {
            funcaoDocumental.setUsoDossieDigital(funcaoDocumentalManutencaoDTO.getIndicadorDossieDigital());
        }

        if (Objects.nonNull(funcaoDocumentalManutencaoDTO.getIndicadorProcessoAdministrativo())) {
            funcaoDocumental.setUsoProcessoAdministrativo(funcaoDocumentalManutencaoDTO.getIndicadorProcessoAdministrativo());
        }

        if (Objects.nonNull(funcaoDocumentalManutencaoDTO.getIndicadorProcessoAdministrativo())) {
            funcaoDocumental.setUsoProcessoAdministrativo(funcaoDocumentalManutencaoDTO.getIndicadorProcessoAdministrativo());
        }

        // Percorre todos os elementos indicados na lista, captura o tipo de documento no contexto transacional e remove o vinculo com a função
        if (Objects.nonNull(funcaoDocumentalManutencaoDTO.getIdentificadoresTipoDocumentoExclusaoVinculo())
            && !funcaoDocumentalManutencaoDTO.getIdentificadoresTipoDocumentoExclusaoVinculo().isEmpty()) {
            funcaoDocumentalManutencaoDTO.getIdentificadoresTipoDocumentoExclusaoVinculo().forEach(idExclusao -> {
                TipoDocumento tipoDocumento = this.tipoDocumentoServico.getById(idExclusao);
                tipoDocumento.removeFuncoesDocumentais(funcaoDocumental);

                funcaoDocumental.removeTiposDocumento(tipoDocumento);
            });
        }

        // Percorre todos os elementos indicados na lista, captura o tipo de documento no contexto transacional e inclui o vinculo com a função
        if (Objects.nonNull(funcaoDocumentalManutencaoDTO.getIdentificadoresTipoDocumentoInclusaoVinculo())
            && !funcaoDocumentalManutencaoDTO.getIdentificadoresTipoDocumentoInclusaoVinculo().isEmpty()) {
            funcaoDocumentalManutencaoDTO.getIdentificadoresTipoDocumentoInclusaoVinculo().forEach(idInclusao -> {
                TipoDocumento tipoDocumento = this.tipoDocumentoServico.getById(idInclusao);
                tipoDocumento.addFuncoesDocumentais(funcaoDocumental);

                if (!funcaoDocumental.getTiposDocumento().contains(tipoDocumento)) {
                    funcaoDocumental.addTiposDocumento(tipoDocumento);
                }
            });
        }

        // Atualiza a data e hora de ultima alteração do registro ára viabilizar a sensibilização de nova carga dos mapas
        funcaoDocumental.setDataHoraUltimaAlteracao(Calendar.getInstance());

        this.update(funcaoDocumental);
    }
}
