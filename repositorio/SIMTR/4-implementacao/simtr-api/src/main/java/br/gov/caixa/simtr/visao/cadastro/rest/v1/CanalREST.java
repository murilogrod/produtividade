package br.gov.caixa.simtr.visao.cadastro.rest.v1;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.CanalServico;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.canal.CanalDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.canal.CanalManutencaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.canal.CanalNovoDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import io.swagger.jaxrs.PATCH;

@Api(hidden = false, value = "Cadastro - Canal")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("/cadastro/v1/canal")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class CanalREST extends AbstractREST {

    private final String PREFIXO = "CAD.CR.";

    @EJB
    CanalServico canalServico;
    
    @EJB
    private AnalyticsServico analyticsServico;
    
    private static final String BASE_URL = "/cadastro/v1/canal";

    @GET
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Lista de canais disponíveis carregados com sucesso.", response = CanalDTO.class, responseContainer = "List")
        ,
        @ApiResponse(code = 204, message = "Não existem canais disponíveis.")})
    @ApiOperation(hidden = false, value = "Consulta lista de canais disponíveis")
    public Response listCanal(@Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            List<Canal> listaCanais = this.canalServico.list();
            if (Objects.nonNull(listaCanais) && !listaCanais.isEmpty()) {
                List<CanalDTO> listaCanaisDTO = listaCanais.stream().map(canal -> new CanalDTO(canal)).collect(Collectors.toList());
                return Response.ok(listaCanaisDTO).build();
            } else {
                return Response.noContent().build();
            }
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("lC"));
        }
    }

    @GET
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Canal localizado com sucesso", response = CanalDTO.class)
        ,
        @ApiResponse(code = 404, message = "O canal solicitado não foi localizado")})
    @ApiOperation(hidden = false, value = "Consulta de um canal especifico pelo identificador")
    public Response getCanalByID(
            @ApiParam(name = "id", value = "Identificador do canal a ser localizado")
            @PathParam("id") Integer id, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            Canal canal = this.canalServico.getById(id);
            if (Objects.nonNull(canal)) {
                CanalDTO canalDTO = new CanalDTO(canal);
                return Response.ok(canalDTO).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gCBI"));
        }
    }

    @POST
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Canal criado com sucesso", responseHeaders = {
            @ResponseHeader(name = "location", description = "Endereço com caminho a ser utilizado para consultar o recurso criado")
        })
        ,
        @ApiResponse(code = 409, message = "A representação do objeto conflita com outro registro já existente. Verificar detalhamento no retorno.")
    })
    @ApiOperation(hidden = false, value = "Realiza a inclusão de um novo canal de comunicação")
    public Response createCanal(
                CanalNovoDTO canalDTO, 
                @Context UriInfo uriInfo, 
    		@Context HttpHeaders headers) throws URISyntaxException {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            Canal canal = canalDTO.prototype();
            this.canalServico.save(canal);
            return Response.created(new URI(uriInfo.getPath().concat("/").concat(canal.getId().toString()))).entity(canal).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("cC"));
        }
    }

    @PATCH
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Modificações encaminhadas executadas com sucesso")
        ,
        @ApiResponse(code = 409, message = "A representação do objeto conflita com outro registro já existente. Verificar detalhamento no retorno.")
    })
    @ApiOperation(hidden = false, value = "Realiza a alteração de um canal de comunicação especifico")
    public Response applyPatchCanal(
            @ApiParam(name = "id", value = "Identificador do canal a ser modificado")
            @PathParam("id") Integer id,
            CanalManutencaoDTO canalManutencaoDTO, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.PATCH.name(), BASE_URL + "/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            this.canalServico.aplicaPatch(id, canalManutencaoDTO);
            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aPC"));
        }
    }

    @DELETE
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Canal removido com sucesso")
        ,
        @ApiResponse(code = 409, message = "O canal possui associações que o tornam impedido de ser excluido.")
    })
    @ApiOperation(hidden = false, value = "Realiza a exclusão de um canal de comunicação especifico")
    public Response removeCanal(
            @ApiParam(name = "id", value = "Identificador do canal a ser modificado")
            @PathParam("id") Integer id, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.DELETE.name(), BASE_URL + "/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            this.canalServico.deleteCanalByID(id);
            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("rC"));
        }
    }
}
