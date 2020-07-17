	package br.gov.caixa.dossie.rs.resource;
/*
	Nesta classe é definido o Webservice do seu projeto. 	
	Para este template cada funcionalidade possui seu proprio Webservice que é compartilhado por suas telas
*/
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.HeaderParam;
import br.gov.caixa.dossie.rs.retorno.Retorno;
import br.gov.caixa.dossie.rs.service.PrincipalService;
import br.gov.caixa.dossie.rs.service.analytics.MetricsService;
import br.gov.caixa.dossie.rs.requisicao.PrincipalRequisicao;
import br.gov.caixa.dossie.util.DossieConstantes;
/**
 * @author SIOGP
 */
@RequestScoped
@Path("/principal")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class PrincipalResource extends Resource {
	@EJB
	private PrincipalService service;
	@EJB
	private MetricsService metrics;	
	/**
	 * init()
	 */
	@PostConstruct
	public void init() {
		metrics.track(DossieConstantes.METRICS_WEB_SERVICES, PrincipalResource.class.getSimpleName());
	}
	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST @Path("/principal")
	public Response createPrincipal(@HeaderParam("authCode") int authCode, PrincipalRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, PrincipalRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.createPrincipal(requisicao);
        if (retorno != null && retorno.isTemErro()) {
            status = Status.NOT_FOUND;
        }
        return build(status, retorno);
	}
/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT  @Path("/principal/{id}")
	public Response updatePrincipal(@HeaderParam("authCode") int authCode, @PathParam("id") Long id, PrincipalRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, PrincipalRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.updatePrincipal(id, requisicao);
        if (retorno != null && retorno.isTemErro()) {
            status = Status.NOT_FOUND;
        }
        return build(status, retorno);
	}
	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET @Path("/principal")
	public Response readAllPrincipal(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL,PrincipalRequisicao.class.getSimpleName());
        if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.readAllPrincipal();
        if (retorno != null && retorno.isTemErro()) {
            status = Status.NOT_FOUND;
        }
        return build(status, retorno);
	}
	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET @Path("/principal/{id}")
	public Response readPrincipal(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, PrincipalRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.readPrincipal(id);
        if (retorno != null && retorno.isTemErro()) {
            status = Status.NOT_FOUND;
        }
        return build(status, retorno);
	}
	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE  @Path("/principal/{id}")
	public Response deletePrincipal(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE,PrincipalRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.deletePrincipal(id);
        if (retorno != null && retorno.isTemErro()) {
            status = Status.NOT_FOUND;
        }
        return build(status, retorno);
	}
}

