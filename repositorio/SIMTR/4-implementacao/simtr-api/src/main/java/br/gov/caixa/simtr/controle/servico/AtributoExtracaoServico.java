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

import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
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
public class AtributoExtracaoServico extends AbstractService<AtributoExtracao, Integer>{
	
	@Inject
	private EntityManager entityManager;
	
	private static final Logger LOGGER = Logger.getLogger(AtributoExtracaoServico.class.getName());

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}
	
	public AtributoExtracao retornaAtributoExtracaoPorTipoDocumento(Integer idAtributo, Integer idTipoDocumento) {
		final StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT ae FROM AtributoExtracao ae ");
        jpql.append(" LEFT JOIN FETCH ae.tipoDocumento td ");
        jpql.append(" WHERE ae.id = :idAtributo AND td.id = :idTipoDocumento ");
        
        TypedQuery<AtributoExtracao> query = this.entityManager.createQuery(jpql.toString(), AtributoExtracao.class);
        query.setParameter("idAtributo", idAtributo);
        query.setParameter("idTipoDocumento", idTipoDocumento);
        
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
	}
}
