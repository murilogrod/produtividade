package br.gov.caixa.simtr.controle.servico;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.controle.excecao.SimtrException;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.GarantiaInformada;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.visao.SimtrExceptionDTO;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE,
    ConstantesUtil.PERFIL_MTRSDNTTO,
    ConstantesUtil.PERFIL_MTRSDNTTG
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class GarantiaInformadaServico extends AbstractService<GarantiaInformada, Long> {

    @Inject
    private EntityManager entityManager;

    @EJB
    private DossieClienteServico dossieClienteServico;

    @Inject
    private Logger logger;

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Realizar a captura da garantia informada para um dossiê de produto baseado no identificador informado.
     *
     * @param id Identificador da relação desejada entre o produto e o dossiê de produto.
     * @param vinculacaoGarantia Indica se a vinculação relativas a garantia vinculada deve ser carregada.
     * @param vinculacaoProduto Indica se a vinculação relativas ao produto vinculado deve ser carregada.
     * @param vinculacaoDossie Indica se a vinculação relativas ao dossiê de produto vinculado deve ser carregada.
     * @param vinculacaoCliente Indica se a vinculação dos dossiês de cliente vinculados quando trata-se de garantia fidejussoria
     * @return Relação Produto localizado com as informações paramterizadas carregadas ou null caso o produto não seja localizado
     */
    public GarantiaInformada getById(final Long id, final boolean vinculacaoGarantia, final boolean vinculacaoProduto, final boolean vinculacaoDossie, final boolean vinculacaoCliente) {
        final StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT gi FROM GarantiaInformada gi ");
        if (vinculacaoGarantia) {
            jpql.append(" LEFT JOIN FETCH gi.garantia g ");
        }
        if (vinculacaoProduto) {
            jpql.append(" LEFT JOIN FETCH gi.produto p ");
        }
        if (vinculacaoDossie) {
            jpql.append(" LEFT JOIN FETCH gi.dossieProduto dp ");
        }
        if (vinculacaoCliente) {
            jpql.append(" LEFT JOIN FETCH gi.dossiesCliente dc ");
        }

        jpql.append(" WHERE gi.id = :id ");

        TypedQuery<GarantiaInformada> query = this.entityManager.createQuery(jpql.toString(), GarantiaInformada.class);
        query.setParameter("id", id);

        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            this.logger.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }

    public void atualizaGarantiaInformada(Long idGarantiaInformada, BigDecimal valorGarantia, List<Long> idClientesAssociados) {
        GarantiaInformada garantiaInformada = this.getById(idGarantiaInformada, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
        if ((garantiaInformada.getGarantia().getFidejussoria()) && (idClientesAssociados == null || idClientesAssociados.isEmpty())) {
            SimtrExceptionDTO simtrExceptionDTO = new SimtrExceptionDTO();
            simtrExceptionDTO.setFalhaRequisicao(Boolean.TRUE);
            throw new SimtrException("GIS.aGI.001 - Para garantias do tipo fidejussorias, é necessario indicar os dossiês de cliente dos avalistas", simtrExceptionDTO);
        }

        garantiaInformada.setValorGarantia(valorGarantia);
        garantiaInformada.getDossiesCliente().clear();
        if (idClientesAssociados != null) {
            idClientesAssociados.forEach(idClienteAssociado -> {
                DossieCliente dc = this.dossieClienteServico.getById(idClienteAssociado, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
                if ((dc == null)) {
                    SimtrExceptionDTO simtrExceptionDTO = new SimtrExceptionDTO();
                    simtrExceptionDTO.setFalhaRequisicao(Boolean.TRUE);
                    throw new SimtrException(MessageFormat.format("Dossiê de Cliente não localizado para identificador fornecido. ID = {0}", idClienteAssociado), simtrExceptionDTO);
                }
                garantiaInformada.addDossiesCliente(dc);
            });
        }

        // Registra garantia informada associada ao dossiê de produto
        this.update(garantiaInformada);

    }

    @Override
    public void delete(GarantiaInformada garantiaInformada) {
        throw new SimtrException("GIS.d.001 - Este metodo não deve ser chamdo diretamente. Para remover vinculos entre o Dossiê de Produto e a garantia, utilizar o método \"DossieProdutoServico.removeVinculoGarantiaDossieProduto()\"");
    }

}
