package br.gov.caixa.dossie.rs.resource;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.gov.caixa.dossie.rs.retorno.Retorno;
import br.gov.caixa.dossie.rs.service.analytics.MetricsService;
import br.gov.caixa.dossie.rs.service.analytics.AnalyticsService;

/**
 * @author SIOGP
 */
@RequestScoped
@Path("/analyticres")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class AnalyticsResource extends Resource {
	
	@EJB
	private AnalyticsService service;
	
	@EJB
	private MetricsService metrics;	
	
	@PostConstruct
    public void init() {
		metrics.track("webservice","analyticres");
    }
    
	@GET @Path("{url}")
	public Response read() {
		metrics.track("read","AnalyticsResource");
		Response response;
		Retorno retorno;
        Status status = Status.OK;
        
        retorno = service.read();
        if(retorno.isTemErro()) {
        	status = Status.NOT_FOUND;
        }
        response = build(status, retorno);
		return response;                      
	}
}