package br.gov.caixa.simtr.controle.servico;

import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.controle.excecao.DossieException;
import br.gov.caixa.simtr.modelo.entidade.RegraDocumental;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRDOSMTZ
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class RegraDocumentalServico extends AbstractService<RegraDocumental, Integer> {

    @Inject
    private EntityManager entityManager;

    private static final Logger LOGGER = Logger.getLogger(RegraDocumentalServico.class.getName());

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void delete(Long id) {
        RegraDocumental regra = this.entityManager.find(RegraDocumental.class, id);
        try {
            this.delete(regra);
        } catch (RuntimeException e) {
            final String msg = "RDS.d.001 - Erro ao excluir Regra Documental. ";
            LOGGER.severe(msg.concat(e.getLocalizedMessage()));
            throw new DossieException(msg, e, true);
        }
    }
}
