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
import br.gov.caixa.dossie.rs.service.DossiedigitalService;
import br.gov.caixa.dossie.rs.service.analytics.MetricsService;
import br.gov.caixa.dossie.rs.requisicao.DossiedigitalRequisicao;
import br.gov.caixa.dossie.util.DossieConstantes;
/**
 * @author SIOGP
 */
@RequestScoped
@Path("/dossiedigital")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class DossiedigitalResource extends Resource {
	@EJB
	private DossiedigitalService service;
	@EJB
	private MetricsService metrics;	
	/**
	 * init()
	 */
	@PostConstruct
	public void init() {
		metrics.track(DossieConstantes.METRICS_WEB_SERVICES, DossiedigitalResource.class.getSimpleName());
	}
	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST @Path("/dossidigital")
	public Response createDossidigital(@HeaderParam("authCode") int authCode, DossiedigitalRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, DossiedigitalRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.createDossiedigital(requisicao);
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
	@PUT  @Path("/dossidigital/{id}")
	public Response updateDossidigital(@HeaderParam("authCode") int authCode, @PathParam("id") Long id, DossiedigitalRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, DossiedigitalRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.updateDossiedigital(id, requisicao);
        if (retorno != null && retorno.isTemErro()) {
            status = Status.NOT_FOUND;
        }
        return build(status, retorno);
	}
	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET @Path("/dossidigital")
	public Response readAllDossidigital(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL,DossiedigitalRequisicao.class.getSimpleName());
        if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.readAllDossiedigital();
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
	@GET @Path("/dossidigital/{id}")
	public Response readDossidigital(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, DossiedigitalRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.readDossiedigital(id);
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
	@DELETE  @Path("/dossidigital/{id}")
	public Response deleteDossidigital(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE,DossiedigitalRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
        	return semAutorizacao(authCode);
        }        
		Status status = Status.OK;
        Retorno retorno = service.deleteDossiedigital(id);
        if (retorno != null && retorno.isTemErro()) {
            status = Status.NOT_FOUND;
        }
        return build(status, retorno);
	}
}

