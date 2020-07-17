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
import br.gov.caixa.dossie.rs.service.RoteironegocialService;
import br.gov.caixa.dossie.rs.service.analytics.MetricsService;
import br.gov.caixa.dossie.rs.requisicao.RoteironegocialRequisicao;
import br.gov.caixa.dossie.util.DossieConstantes;
/**
 * @author SIOGP
 */
@RequestScoped
@Path("/roteironegocial")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class RoteironegocialResource extends Resource {
	@EJB
	private RoteironegocialService service;
	@EJB
	private MetricsService metrics;	
	/**
	 * init()
	 */
	@PostConstruct
	public void init() {
		metrics.track(DossieConstantes.METRICS_WEB_SERVICES, RoteironegocialResource.class.getSimpleName());
	}
	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST @Path("/roteironegocial")
	public Response createRoteironegocial(@HeaderParam("authCode") int authCode, RoteironegocialRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, RoteironegocialRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.createRoteironegocial(requisicao);
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
	@PUT  @Path("/roteironegocial/{id}")
	public Response updateRoteironegocial(@HeaderParam("authCode") int authCode, @PathParam("id") Long id, RoteironegocialRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, RoteironegocialRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.updateRoteironegocial(id, requisicao);
        if (retorno != null && retorno.isTemErro()) {
            status = Status.NOT_FOUND;
        }
        return build(status, retorno);
	}
	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET @Path("/roteironegocial")
	public Response readAllRoteironegocial(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL,RoteironegocialRequisicao.class.getSimpleName());
        if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.readAllRoteironegocial();
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
	@GET @Path("/roteironegocial/{id}")
	public Response readRoteironegocial(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, RoteironegocialRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.readRoteironegocial(id);
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
	@DELETE  @Path("/roteironegocial/{id}")
	public Response deleteRoteironegocial(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE,RoteironegocialRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.deleteRoteironegocial(id);
        if (retorno != null && retorno.isTemErro()) {
            status = Status.NOT_FOUND;
        }
        return build(status, retorno);
	}
}

