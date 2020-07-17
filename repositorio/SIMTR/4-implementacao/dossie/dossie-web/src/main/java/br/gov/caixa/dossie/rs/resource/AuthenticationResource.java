package br.gov.caixa.dossie.rs.resource;

import java.util.ArrayList;
import java.util.List;
 
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import br.gov.caixa.dossie.rs.resource.Resource;
import br.gov.caixa.dossie.rs.retorno.Retorno;
import br.gov.caixa.dossie.util.DossieConstantes;
import br.gov.caixa.dossie.rs.requisicao.AuthenticationRequisicao;
import br.gov.caixa.dossie.rs.retorno.AuthenticationRetorno;
import br.gov.caixa.dossie.rs.service.AuthenticationService;
import br.gov.caixa.dossie.rs.service.analytics.MetricsService;

/**
 * @author SIOGP
 */
@RequestScoped
@Path("/auth")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class AuthenticationResource extends Resource {

	@EJB
	private MetricsService metrics;
	
	@EJB
	AuthenticationService service;
	
	/**
	 * init
	 */
	@PostConstruct
    public void init() {
		metrics.track(DossieConstantes.METRICS_WEB_SERVICES, DossieConstantes.METRICS_AUTH);
	}
	
	/**
	 * @param requisicao
	 * @return response
	 */	
	@POST
	@Path("/autenticar/")
	public Response autenticar(final AuthenticationRequisicao requisicao){
		
		AuthenticationRetorno retorno =	service.autenticar(requisicao);
		
		ResponseBuilder response = Response.ok(retorno);
		response.header("Access-Control-Allow-Origin", "*");
		response.header("Access-Control-Allow-Credentials", "true");
		response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.header("Access-Control-Max-Age", "1209600");
		response.header("Access-Control-Allow-Headers", "Origin, X-Request-Width, Content-Type, Accept");
		response.header("Cache-Control", "no-cache, no-store, must-revalidate");
		response.header("Pragma", "no-cache");
		response.header("Expires", 0);
		response.header("X-Frame-Options", "DENY");
		response.header("X-XSS-Protection", "1");
		if(!retorno.isTemErro()){
			response.cookie(new NewCookie(new Cookie(TOKENACESSO, retorno.getTokenAcesso(), "/", null),null,retorno.getTempoExpiracaoAcesso().intValue(),false));
			response.cookie(new NewCookie(new Cookie(CREDENCIAL, retorno.getCredencial(), "/", null),null,retorno.getTempoExpiracaoAcesso().intValue(),false));
			response.cookie(new NewCookie(new Cookie(TOKENRENOVACAO, retorno.getTokenRenovacao(), "/", null),null,retorno.getTempoExpiracaoRenovacao().intValue(),false));
		}
		return response.build();
	}
		
	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	public Response readAll(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, DossieConstantes.METRICS_READ_ALL);
		
		final Retorno retorno = new Retorno();
		
		Status status = Status.OK;
		if (authCode == auth.getCode()) {
			retorno.setTemErro(false);
			
		} else {
			status = Status.UNAUTHORIZED;
			retorno.setTemErro(true);
			final List<String> msgsErro = new ArrayList<String>();
			msgsErro.add(DossieConstantes.ERRO_CODIGO_NAO_AUTORIZADO + Integer.toString(authCode));			
			retorno.setMsgsErro(msgsErro);
		}
		
		return build(status, retorno);
	}
}