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
import br.gov.caixa.dossie.rs.service.PortfolioacessosService;
import br.gov.caixa.dossie.rs.service.analytics.MetricsService;
import br.gov.caixa.dossie.rs.requisicao.PortfolioacessosRequisicao;
import br.gov.caixa.dossie.util.DossieConstantes;
/**
 * @author SIOGP
 */
@RequestScoped
@Path("/portfolioacessos")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class PortfolioacessosResource extends Resource {
	@EJB
	private PortfolioacessosService service;
	@EJB
	private MetricsService metrics;	
	/**
	 * init()
	 */
	@PostConstruct
	public void init() {
		metrics.track(DossieConstantes.METRICS_WEB_SERVICES, PortfolioacessosResource.class.getSimpleName());
	}
	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST @Path("/portfliodeacessos")
	public Response createPortfliodeacessos(@HeaderParam("authCode") int authCode, PortfolioacessosRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, PortfolioacessosRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.createPortfolioacessos(requisicao);
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
	@PUT  @Path("/portfliodeacessos/{id}")
	public Response updatePortfliodeacessos(@HeaderParam("authCode") int authCode, @PathParam("id") Long id, PortfolioacessosRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, PortfolioacessosRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.updatePortfolioacessos(id, requisicao);
        if (retorno != null && retorno.isTemErro()) {
            status = Status.NOT_FOUND;
        }
        return build(status, retorno);
	}
	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET @Path("/portfliodeacessos")
	public Response readAllPortfliodeacessos(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL,PortfolioacessosRequisicao.class.getSimpleName());
        if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.readAllPortfolioacessos();
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
	@GET @Path("/portfliodeacessos/{id}")
	public Response readPortfliodeacessos(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, PortfolioacessosRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.readPortfolioacessos(id);
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
	@DELETE  @Path("/portfliodeacessos/{id}")
	public Response deletePortfliodeacessos(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE,PortfolioacessosRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.deletePortfolioacessos(id);
        if (retorno != null && retorno.isTemErro()) {
            status = Status.NOT_FOUND;
        }
        return build(status, retorno);
	}
}

