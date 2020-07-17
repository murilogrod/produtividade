package br.gov.caixa.simtr.controle.servico;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.modelo.entidade.ComportamentoPesquisa;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.enumerator.SistemaPesquisaEnum;
import br.gov.caixa.simtr.modelo.enumerator.SistemaPesquisaTipoRetornoEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRDOSINT,
    ConstantesUtil.PERFIL_MTRDOSMTZ,
    ConstantesUtil.PERFIL_MTRDOSOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class ComportamentoPesquisaServico extends AbstractService<ComportamentoPesquisa, Integer> {

    @Inject
    private EntityManager entityManager;

    @Inject
    private Logger logger;

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    public List<ComportamentoPesquisa> getBySistemas(final Produto produto, final SistemaPesquisaEnum... sistemas) {
        final StringBuilder jpql = new StringBuilder();

        jpql.append(" SELECT cp FROM ComportamentoPesquisa cp ");
        jpql.append(" WHERE cp.produto = :produto ");
        jpql.append(" AND cp.sistemaRetorno IN (:sistemaRetorno) ");

        TypedQuery<ComportamentoPesquisa> query = this.entityManager.createQuery(jpql.toString(), ComportamentoPesquisa.class);
        query.setParameter("produto", produto);
        query.setParameter("sistemaRetorno", Arrays.asList(sistemas));

        return query.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ComportamentoPesquisa getBySistemaAndCodigoTipoPesquisa(final Produto produto, final SistemaPesquisaEnum sistema, final Integer tipoPesquisa, final Integer subTipoPesquisa) {

        final StringBuilder jpql = new StringBuilder();

        jpql.append(" SELECT cp FROM ComportamentoPesquisa cp ");
        jpql.append(" WHERE cp.produto = :produto ");
        jpql.append(" AND cp.sistemaRetorno = :sistemaRetorno ");
        jpql.append(" AND cp.valorCodigoRetorno = :valorCodigoRetorno ");

        TypedQuery<ComportamentoPesquisa> queryComportamento = this.entityManager.createQuery(jpql.toString(), ComportamentoPesquisa.class);
        queryComportamento.setParameter("produto", produto);
        queryComportamento.setParameter("sistemaRetorno", sistema);
        queryComportamento.setParameter("valorCodigoRetorno", SistemaPesquisaTipoRetornoEnum.getBySistemaCodigo(SistemaPesquisaEnum.SICPF, tipoPesquisa, subTipoPesquisa));

        try {
            ComportamentoPesquisa comportamentosPesquisa = queryComportamento.getSingleResult();
            this.entityManager.clear();
            return comportamentosPesquisa;
        } catch (NoResultException nre) {
            this.logger.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }

}
