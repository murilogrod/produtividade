package br.gov.caixa.simtr.controle.servico;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.modelo.entidade.OpcaoAtributo;
import br.gov.caixa.simtr.util.ConstantesUtil;

/**
 * 
 * @author f525904
 *
 */
@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class OpcaoAtributoServico extends AbstractService<OpcaoAtributo, Integer> {
	
	@Inject
	private EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}
}
