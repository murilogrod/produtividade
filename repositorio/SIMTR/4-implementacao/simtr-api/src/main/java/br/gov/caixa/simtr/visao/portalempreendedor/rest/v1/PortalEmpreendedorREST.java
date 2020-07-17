package br.gov.caixa.simtr.visao.portalempreendedor.rest.v1;

import java.net.URISyntaxException;

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
import javax.ws.rs.core.UriInfo;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.PortalEmpreendedorServico;
import br.gov.caixa.simtr.modelo.mapeamento.v1.portalempreendedor.ProtocoloDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(hidden = false, value = "Dossiê de Produto - Portal do Empreendedor")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("portal-empreendedor/v1/credmei")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class PortalEmpreendedorREST extends AbstractREST {
    
    private final String PREFIXO = "DP.PE.";
    
    @EJB
    private AnalyticsServico analyticsServico;
    
    @EJB
    private PortalEmpreendedorServico portalEmpreendedorServico;
    
    private static final String BASE_URL = "/portal-empreendedor/v1/credmei";
    
    @POST
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Solicitação processada com sucesso."),
        @ApiResponse(code = 400, message = "Protocolo não localizado no serviço do portal."),
        @ApiResponse(code = 408, message = "Tempo de requisição expirado (Timeout)."),
        @ApiResponse(code = 500, message = "Erro interno do servidor."),
        @ApiResponse(code = 502, message = " Falha na comunicação com serviço externo (API Manager, jBPM ou Portal do Empreendedor)."),
        @ApiResponse(code = 503, message = "Serviço externo indisponível para consumo (API Manager, jBPM ou Portal do Empreendedor)."),
        @ApiResponse(code = 504, message = "Tempo expirado na comunicação com um sistema externo.")
    })
    @ApiOperation(hidden = false, value = "Realiza a criação de um dossie de produto pelo protocolo do portal do empreendedor")
    public Response criaDossieProdutoPeloProtocoloPortalEmpreendedor(
                ProtocoloDTO protocoloDTO,
                @Context UriInfo uriInfo, 
                @Context HttpHeaders headers) throws URISyntaxException {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            this.portalEmpreendedorServico.montaDossieProdutoMEI(protocoloDTO.getProtocolo());
            return Response.ok().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("cDPPPPE"));
        }
    }
}
