package br.gov.caixa.simtr.controle.servico;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.controle.excecao.SimtrPermissaoException;
import br.gov.caixa.simtr.modelo.entidade.ProdutoDossie;
import br.gov.caixa.simtr.modelo.enumerator.PeriodoJurosEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRAUD,
    ConstantesUtil.PERFIL_MTRTEC,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class ProdutoDossieServico extends AbstractService<ProdutoDossie, Long> {

    @Inject
    private EntityManager entityManager;

    private static final Logger LOGGER = Logger.getLogger(ProdutoDossieServico.class.getName());

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Realizar a captura da relação existente entre um Produto e um Dossiê de produto baseado no identificador informado.
     *
     * @param id Identificador da relação desejada entre o produto e o dossiê de produto.
     * @param vinculacaoDossie Indica se a vinculação relativas ao dossiê de produto vinculado deve ser carregada.
     * @param vinculacaoProduto Indica se a vinculação relativas ao produto vinculado deve ser carregada.
     * @return Relação Produto localizado com as informações paramterizadas carregadas ou null caso o produto não seja localizado
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public ProdutoDossie getById(final Long id, final boolean vinculacaoProduto, final boolean vinculacaoDossie) {
        final StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT pd FROM ProdutoDossie pd ");
        if (vinculacaoProduto) {
            jpql.append(" LEFT JOIN FETCH pd.produto p ");
        }
        if (vinculacaoDossie) {
            jpql.append(" LEFT JOIN FETCH pd.dossieProduto dp ");
        }
        jpql.append(" WHERE pd.id = :id ");

        TypedQuery<ProdutoDossie> query = this.entityManager.createQuery(jpql.toString(), ProdutoDossie.class);
        query.setParameter("id", id);

        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTO,
        ConstantesUtil.PERFIL_MTRSDNTTG
    })
    public void atualizaProdutoDossie(Long idProdutoDossie) {
        ProdutoDossie produtoDossie = this.getById(idProdutoDossie, Boolean.FALSE, Boolean.FALSE);

        // Registra produto dossiê associada ao dossiê de produto
        this.update(produtoDossie);

    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    @Override
    public void delete(ProdutoDossie objeto) {
        throw new SimtrPermissaoException("PDS.d.001 - Este metodo não deve ser chamdo diretamente. Para remover vinculos entre o Dossiê de Produto e o Produto, utilizar o método \"DossieProdutoServico.removeVinculoProdutoDossieProduto()\"");
    }

}
