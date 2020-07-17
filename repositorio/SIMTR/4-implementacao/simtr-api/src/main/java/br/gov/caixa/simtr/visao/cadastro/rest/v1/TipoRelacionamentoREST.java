package br.gov.caixa.simtr.visao.cadastro.rest.v1;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.EJBException;
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
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.TipoRelacionamentoServico;
import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tiporelacionamento.TipoRelacionamentoAlteracaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tiporelacionamento.TipoRelacionamentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tiporelacionamento.TipoRelacionamentoInclusaoDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import io.swagger.jaxrs.PATCH;

@Api(hidden = false, value = "Cadastro - Tipo de Relacionamento")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("cadastro/v1/tipo-relacionamento")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class TipoRelacionamentoREST extends AbstractREST {
    
    private final String PREFIXO = "CAD.TRR.";
    
    @EJB
    private TipoRelacionamentoServico tipoRelacionamentoServico;
    
    @EJB
    private AnalyticsServico analyticsServico;
    
    private static final String BASE_URL = "cadastro/v1/tipo-relacionamento";
    
    @GET
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Lista de Tipo de Relacionamento carregados com sucesso.", response = TipoRelacionamentoDTO.class, responseContainer = "List"),
        
        @ApiResponse(code = 204, message = "Não foi encontrado nenhum tipo de relacionamento correspondente.")})
    @ApiOperation(hidden = false, value = "Consulta lista de tipo de relacionamento.")
    public Response carregaTipoRelacionamento(@Context HttpHeaders headers) {
        
        this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            List<TipoRelacionamento> listaTipoRelacionamento = this.tipoRelacionamentoServico.listTodos();
            if (Objects.nonNull(listaTipoRelacionamento) && !listaTipoRelacionamento.isEmpty()) {
                List<TipoRelacionamentoDTO> listaTipoRelacionamentoDTO = listaTipoRelacionamento.stream().map(tipoRelacionamento -> new TipoRelacionamentoDTO(tipoRelacionamento)).collect(Collectors.toList());
                return Response.ok(listaTipoRelacionamentoDTO).build();
            } else {
                return Response.noContent().build();
            }
        }catch (EJBException e) {
            return this.montaRespostaExcecao(e, this.PREFIXO.concat("cTR"));
        }
    }
    

    @GET
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Tipo de Relacionamento localizado.", response = TipoRelacionamentoDTO.class)
        ,
        @ApiResponse(code = 404, message = "Tipo de Relacionamento não localizado com identificador informado.")})
    @ApiOperation(hidden = false, value = "Consulta um de tipo de relacionamento pelo identificador informado.")
    public Response getTipoRelacionamento(
             @ApiParam(name = "id", value = "Identificador do tipo relacionamento", required = true)
             @PathParam("id") Integer id,
             @Context HttpHeaders headers) {
        
        this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            TipoRelacionamento tipoRelacionamento = this.tipoRelacionamentoServico.findById(id);
            return Response.ok(new TipoRelacionamentoDTO(tipoRelacionamento)).build();
        }catch (EJBException e) {
            return this.montaRespostaExcecao(e, this.PREFIXO.concat("gTR"));
        }
    }
    
    @POST
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Tipo de relacionamento incluído com sucesso.", responseHeaders = {
            @ResponseHeader(name = "location", description = "Endereço com caminho a ser utilizado para consultar o recurso criado.", response = String.class)
        }),
        @ApiResponse(code = 400, message = "Falha na validação de informações.")})
    @ApiOperation(hidden = false, value = "Cria um tipo de relacionamento.")
    public Response inclusao(TipoRelacionamentoInclusaoDTO tipoRelacionamentoInclusaoDTO, @Context UriInfo uriInfo, @Context HttpHeaders headers) throws URISyntaxException {
        
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/", capturaEngineCliente(headers));
        
        try {
            TipoRelacionamento tipoRelacionamento = tipoRelacionamentoInclusaoDTO.prototype();
            this.tipoRelacionamentoServico.criaNovoTipoRelacionamento(tipoRelacionamento);
            
            String location = uriInfo.getPath().concat("/").concat(tipoRelacionamento.getId().toString());
            return Response.created(new URI(location)).entity(tipoRelacionamento).build();
            
        }catch (EJBException e) {
            return this.montaRespostaExcecao(e, this.PREFIXO.concat("i"));
        }
    }
    
    @PATCH
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Alterações aplicadas com sucesso."),
        @ApiResponse(code = 404, message = "Tipo de relacionamento não localizado sob identificador informado."),
        @ApiResponse(code = 400, message = "Falha na validação de informações.")})
    @ApiOperation(hidden = false, value = "Altera um tipo de relacionamento.")
    public Response alteraTipoRelacionamento(
             @ApiParam(name = "id", value = "Identificador do tipo de relacionamento.", required = true)
             @PathParam("id") Integer id, 
             TipoRelacionamentoAlteracaoDTO tipoRelacionamentoAlteracaoDTO, 
             @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.PATCH.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            this.tipoRelacionamentoServico.alteraTipoRelacionamento(id, tipoRelacionamentoAlteracaoDTO);
            return Response.status(Response.Status.NO_CONTENT).build();
        }catch (EJBException e) {
            return this.montaRespostaExcecao(e, this.PREFIXO.concat("aTR"));
        }
    }
    
    @DELETE
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Tipo de relacionamento removido com sucesso."),
        @ApiResponse(code = 400, message = "Tipo de relacionamento não localizado sob identificador informado."),
        @ApiResponse(code = 409, message = "Tipo de relacionamento não removido devido a interdependência com outros objetos do banco.")})
    @ApiOperation(hidden = false, value = "Deleta um tipo de relacionamento.")
    public Response deletaTipoRelacionamento(
             @ApiParam(name = "id", value = "Identificador do tipo de relacionamento.", required = true)
             @PathParam("id") Integer id, 
             @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.DELETE.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            this.tipoRelacionamentoServico.excluiTipoRelacionamento(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        }catch (EJBException e) {
            return this.montaRespostaExcecao(e, this.PREFIXO.concat("dTR"));
        }
    }
}
