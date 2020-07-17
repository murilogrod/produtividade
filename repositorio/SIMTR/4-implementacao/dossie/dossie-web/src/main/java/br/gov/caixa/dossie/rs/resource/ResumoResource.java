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
import br.gov.caixa.dossie.rs.service.ResumoService;
import br.gov.caixa.dossie.rs.service.analytics.MetricsService;
import br.gov.caixa.dossie.rs.requisicao.ResumoRequisicao;
import br.gov.caixa.dossie.util.DossieConstantes;
/**
 * @author SIOGP
 */
@RequestScoped
@Path("/resumo")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class ResumoResource extends Resource {
	@EJB
	private ResumoService service;
	@EJB
	private MetricsService metrics;	
	/**
	 * init()
	 */
	@PostConstruct
	public void init() {
		metrics.track(DossieConstantes.METRICS_WEB_SERVICES, ResumoResource.class.getSimpleName());
	}
	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST @Path("/resumodocliente")
	public Response createResumodocliente(@HeaderParam("authCode") int authCode, ResumoRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, ResumoRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.createResumo(requisicao);
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
	@PUT  @Path("/resumodocliente/{id}")
	public Response updateResumodocliente(@HeaderParam("authCode") int authCode, @PathParam("id") Long id, ResumoRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, ResumoRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.updateResumo(id, requisicao);
        if (retorno != null && retorno.isTemErro()) {
            status = Status.NOT_FOUND;
        }
        return build(status, retorno);
	}
	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET @Path("/resumodocliente")
	public Response readAllResumodocliente(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL,ResumoRequisicao.class.getSimpleName());
        if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.readAllResumo();
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
	@GET @Path("/resumodocliente/{id}")
	public Response readResumodocliente(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, ResumoRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.readResumo(id);
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
	@DELETE  @Path("/resumodocliente/{id}")
	public Response deleteResumodocliente(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE,ResumoRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.deleteResumo(id);
        if (retorno != null && retorno.isTemErro()) {
            status = Status.NOT_FOUND;
        }
        return build(status, retorno);
	}
}

