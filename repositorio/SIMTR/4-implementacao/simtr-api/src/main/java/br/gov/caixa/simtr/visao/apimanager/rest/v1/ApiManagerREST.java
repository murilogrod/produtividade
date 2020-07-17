package br.gov.caixa.simtr.visao.apimanager.rest.v1;

import java.util.Objects;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.simtr.controle.servico.AdministracaoServico;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.apimanager.ApiManagerDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(hidden = true, value = "Api Manager")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("/api-manager")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ApiManagerREST extends AbstractREST {

    private final String PREFIXO = "SDN.AM.";

    @EJB
    private AdministracaoServico administracaoServico;
    
    @EJB
	private AnalyticsServico analyticsServico;
    
    private static final String BASE_URL = "/api-manager";

    @POST
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Serviço que disponibiliza uma ponte de conexão do SIMTR com os serviços disponíveis no ApiManager.")
        ,
        @ApiResponse(code = 401, message = "Autorização inválida."),
        @ApiResponse(code = 403, message = "Usuário não possui permissão para consumir este serviço.")})
    	
    @ApiOperation(hidden = false, value = "Disponibliza uma ponte para consumir serviços do Api Manager")
    public Response consumirServicoApiManager(ApiManagerDTO corpo, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
        	
        	if(Objects.isNull(corpo)) {
        		return Response.status(Response.Status.BAD_REQUEST).build();
        	}
        	
        	Response response = administracaoServico.consumirServicoApiManager(corpo.getVerbo(), corpo.getServico(), corpo.getCorpo(), corpo.getCabecalhos());
        	return Response.status(response.getStatus()).entity(response.getEntity()).build();    		

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("cSAM"));
        }
    }

}
