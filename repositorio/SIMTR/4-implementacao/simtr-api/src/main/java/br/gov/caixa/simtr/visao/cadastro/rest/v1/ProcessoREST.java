package br.gov.caixa.simtr.visao.cadastro.rest.v1;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.ProcessoServico;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.util.CalendarUtil;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.processo.ManterProcessoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.processo.MacroProcessoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import io.swagger.jaxrs.PATCH;

@Api(hidden = false, value = "Cadastro - Processo")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("negocio/v2/processo")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ProcessoREST extends AbstractREST {

    @EJB
    private CalendarUtil calendarUtil;

    @EJB
    private KeycloakService keycloakService;
    
    @EJB
    private ProcessoServico processoServico;
    
    @EJB
    private AnalyticsServico analyticsServico;
    
    private static final String BASE_URL = "/negocio/v2/processo";

    @GET
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Lista de todos os processos.", response = MacroProcessoDTO.class)
        ,
        @ApiResponse(code = 404, message = "Não foram localizados processos.")
    })
    @ApiOperation(hidden = false, value = "Lista os processos.")
    public Response listaProcessos(@Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/", capturaEngineCliente(headers));

        List<Processo> processos = this.processoServico.listaProcessos();
        if (processos.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Map<String, Object> retorno = new HashMap<>();

        Integer lotacaoAdministrativa = this.keycloakService.getLotacaoAdministrativa() == null ? 9999 : this.keycloakService.getLotacaoAdministrativa();
        Integer lotacaoFisica = this.keycloakService.getLotacaoFisica() == null ? 9999 : this.keycloakService.getLotacaoFisica();
        
        List<MacroProcessoDTO> processosDTO = new ArrayList<>();
        processos.forEach(processo -> processosDTO.add(new MacroProcessoDTO(processo, lotacaoAdministrativa, lotacaoFisica)));

        retorno.put("atualizacao", this.calendarUtil.toString(processoServico.getDataHoraUltimaAlteracao(), "yyyy-MM-dd HH:mm:ss"));
        retorno.put("processos", processosDTO);
        return Response.ok(retorno).build();
    }

    @DELETE
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Processo removido com sucesso.")
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.", response = RetornoErroDTO.class)})
    @ApiOperation(hidden = false, value = "Exclui o Processo pelo Id Informado.")
    public synchronized Response removeProcesso(@PathParam("id") Integer idProcesso, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.DELETE.name(), BASE_URL + "/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(idProcesso)));
        try {

            this.processoServico.delete(idProcesso);

            return Response.noContent().build();

        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, "CAD.PR.rP");
        }
    }

    @POST
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Processo salvo com sucesso", responseHeaders = {
            @ResponseHeader(name = "location", description = "URL de retorno utilizada para a consulta do Processo com identificação do registro criado.", response = String.class)
        })
        ,
	@ApiResponse(code = 400, message = "Erro na requisição")
        ,
	@ApiResponse(code = 503, message = "Erro de Execução no Servidor")
    })
    @ApiOperation(hidden = false, value = "Criar um Processo.")
    public Response salvarProcesso(final ManterProcessoDTO processoDto, @Context UriInfo uriInfo, 
    		@Context HttpHeaders headers) throws URISyntaxException {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {

            final Processo processo = processoDto.prototype();

            processoServico.save(processo);

            return Response.created(new URI(uriInfo.getPath().concat("/"))).build();
        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, "CAD.PR.sP");
        }
    }

    @PATCH
    @Path("/alterar/")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Processo atualizado com sucesso")
        ,
	@ApiResponse(code = 400, message = "Erro na requisição")
        ,
	@ApiResponse(code = 503, message = "Erro de Execução no Servidor")
    })
    @ApiOperation(hidden = false, value = "Atualizar um Processo.")
    public Response alterarProcesso(ManterProcessoDTO processo, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.PATCH.name(), BASE_URL + "/alterar/", capturaEngineCliente(headers));
        try {
            processoServico.aplicarPatch(processo);

            return Response.noContent().build();
        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, "CAD.PR.aP");
        }
    }

}
