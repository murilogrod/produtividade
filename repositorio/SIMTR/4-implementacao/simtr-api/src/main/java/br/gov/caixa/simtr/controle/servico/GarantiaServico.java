package br.gov.caixa.simtr.controle.servico;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.modelo.entidade.Garantia;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
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
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class GarantiaServico extends AbstractService<Garantia, Integer> {

    @Inject
    private EntityManager entityManager;

    @Inject
    private Logger logger;

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Realizar a captura de uma Garantia baseado no identificador d garantia ou no codigo BACEN de vinculação da mesma.
     *
     * @param id Identificador da garanria desejada. Caso informado, a consulta será realizada pelo id
     * @param vinculacoesProdutos Indica se as informações dos produtos asociados devem ser carregadas
     * @param vinculacoesTipologia Indica se as vinculações que identificam o tipo de documento possivel de comprovação da garantia devem devem ser carregadas
     * @param vinculacoesGarantiaInformada Indica se as vinculações com os registros de garantia informada devem ser carregados
     * @return Garantia localizado com as informações paramterizadas carregadas ou null caso a garantia não seja localizada
     */
    public Garantia getById(final Integer id, final boolean vinculacoesProdutos, final boolean vinculacoesTipologia, final boolean vinculacoesGarantiaInformada) {
        return this.getByIdOrCodigoBacen(id, null, vinculacoesProdutos, vinculacoesTipologia, vinculacoesGarantiaInformada);
    }

    /**
     * Realizar a captura de uma Garantia baseado no codigo BACEN de vinculação da mesma.
     *
     * @param codigoBacen Codigo de do bacen utilizado também corporativamente para identificação do tipo de garantia.
     * @param vinculacoesProdutos Indica se as informações dos produtos asociados devem ser carregadas
     * @param vinculacoesTipologia Indica se as vinculações que identificam o tipo de documento possivel de comprovação da garantia devem devem ser carregadas
     * @param vinculacoesGarantiaInformada Indica se as vinculações com os registros de garantia informada devem ser carregados
     * @return Garantia localizado com as informações paramterizadas carregadas ou null caso a garantia não seja localizada
     */
    public Garantia getByCodigoBacen(final Integer codigoBacen, final boolean vinculacoesProdutos, final boolean vinculacoesTipologia, final boolean vinculacoesGarantiaInformada) {
        return this.getByIdOrCodigoBacen(null, codigoBacen, vinculacoesProdutos, vinculacoesTipologia, vinculacoesGarantiaInformada);
    }

    /**
     * Realizar a captura de uma Garantia baseado no identificador d garantia ou no codigo BACEN de vinculação da mesma.
     *
     * @param id Identificador da garanria desejada. Caso informado, a consulta será realizada pelo id
     * @param codigoBacen Codigo de do bacen utilizado também corporativamente para identificação do tipo de garantia.
     * @param vinculacoesProdutos Indica se as informações dos produtos asociados devem ser carregadas
     * @param vinculacoesTipologia Indica se as vinculações que identificam o tipo de documento possivel de comprovação da garantia devem devem ser carregadas
     * @param vinculacoesGarantiaInformada Indica se as vinculações com os registros de garantia informada devem ser carregados
     * @return Garantia localizado com as informações paramterizadas carregadas ou null caso a garantia não seja localizada
     */
    private Garantia getByIdOrCodigoBacen(final Integer id, final Integer codigoBacen, final boolean vinculacoesProdutos, final boolean vinculacoesTipologia, final boolean vinculacoesGarantiaInformada) {
        final StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT g FROM Garantia g ");
        if (vinculacoesProdutos) {
            jpql.append(" LEFT JOIN FETCH g.produtos p ");
        }
        if (vinculacoesTipologia) {
            jpql.append(" LEFT JOIN FETCH g.documentosGarantia dg ");
            jpql.append(" LEFT JOIN FETCH dg.tipoDocumento td ");
            jpql.append(" LEFT JOIN FETCH dg.funcaoDocumental fd ");
            jpql.append(" LEFT JOIN FETCH fd.tiposDocumento tdfd ");
        }
        if (vinculacoesGarantiaInformada) {
            jpql.append(" LEFT JOIN FETCH g.garantiasInformadas gi ");
        }
        if (id != null) {
            jpql.append(" WHERE g.id = :id ");
        } else {
            jpql.append(" WHERE g.garantiaBacen = :codigoBacen ");
        }
        TypedQuery<Garantia> query = this.entityManager.createQuery(jpql.toString(), Garantia.class);
        if (id != null) {
            query.setParameter("id", id);
        } else {
            query.setParameter("codigoBacen", codigoBacen);
        }
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            this.logger.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }
}
