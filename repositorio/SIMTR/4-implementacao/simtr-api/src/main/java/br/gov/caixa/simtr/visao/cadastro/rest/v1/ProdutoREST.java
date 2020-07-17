package br.gov.caixa.simtr.visao.cadastro.rest.v1;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.ProdutoServico;
import br.gov.caixa.simtr.modelo.entidade.Produto;
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

import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.produto.ProdutoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.produto.ProdutoManutencaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.produto.ProdutoNovoDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import io.swagger.jaxrs.PATCH;

@Api(hidden = false, value = "Cadastro - Produto")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("/cadastro/v1/produto")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ProdutoREST extends AbstractREST {

    private final String PREFIXO = "CAD.PR.";

    @EJB
    ProdutoServico produtoServico;
    
    @EJB
	private AnalyticsServico analyticsServico;
    
    private static final String BASE_URL = "/negocio/v2/processo";


    @GET
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Lista de produtos disponíveis carregados com sucesso.", response = ProdutoDTO.class, responseContainer = "List")
        ,
        @ApiResponse(code = 204, message = "Não existem produtos disponíveis.")})
    @ApiOperation(hidden = false, value = "Consulta lista de produtos disponíveis")
    public Response listProduto(@Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            List<Produto> listaProdutos = this.produtoServico.list();
            if (Objects.nonNull(listaProdutos) && !listaProdutos.isEmpty()) {
                List<ProdutoDTO> listaProdutosDTO = listaProdutos.stream().map(produto -> new ProdutoDTO(produto)).collect(Collectors.toList());
                return Response.ok(listaProdutosDTO).build();
            } else {
                return Response.noContent().build();
            }
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("lP"));
        }
    }

    @GET
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Produto localizado com sucesso", response = ProdutoDTO.class)
        ,
        @ApiResponse(code = 404, message = "O produto solicitado não foi localizado")})
    @ApiOperation(hidden = false, value = "Consulta de um produto especifico pelo identificador")
    public Response getProdutoByID(
            @ApiParam(name = "id", value = "Identificador do produto a ser localizado")
            @PathParam("id") Integer id, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            Produto produto = this.produtoServico.getById(id);
            if (Objects.nonNull(produto)) {
                ProdutoDTO produtoDTO = new ProdutoDTO(produto);
                return Response.ok(produtoDTO).build();
            } else {
                return Response.noContent().build();
            }
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gPBI"));
        }
    }

    @POST
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Produto criado com sucesso", responseHeaders = {
            @ResponseHeader(name = "location", description = "Endereço com caminho a ser utilizado para consultar o recurso criado")
        })
        ,
        @ApiResponse(code = 409, message = "A representação do objeto conflita com outro registro já existente. Verificar detalhamento no retorno.")
    })
    @ApiOperation(hidden = false, value = "Realiza a inclusão de um novo produto")
    public Response createProduto(ProdutoNovoDTO produtoDTO, @Context UriInfo uriInfo, @Context HttpHeaders headers) throws URISyntaxException {
        try {
        	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/", capturaEngineCliente(headers));
            Produto produto = produtoDTO.prototype();
            this.produtoServico.save(produto);
            return Response.created(new URI(uriInfo.getPath().concat("/").concat(produto.getId().toString()))).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("cP"));
        }
    }

    @PATCH
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Modificações encaminhadas executadas com sucesso")
        ,
        @ApiResponse(code = 409, message = "A representação do objeto conflita com outro registro já existente. Verificar detalhamento no retorno.")
    })
    @ApiOperation(hidden = false, value = "Realiza a alteração de um produto especifico")
    public Response applyPatch(
            @ApiParam(name = "id", value = "Identificador do produto a ser modificado")
            @PathParam("id") Integer id,
            ProdutoManutencaoDTO produtoManutencaoDTO, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            this.produtoServico.aplicaPatch(id, produtoManutencaoDTO);
            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aPP"));
        }
    }

    @DELETE
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Produto removido com sucesso")
        ,
        @ApiResponse(code = 409, message = "O produto possui associações que o tornam impedido de ser excluido.")
    })
    @ApiOperation(hidden = false, value = "Realiza a exclusão de um produto especifico")
    public Response remove(
            @ApiParam(name = "id", value = "Identificador do produto a ser modificado")
            @PathParam("id") Integer id, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            this.produtoServico.deleteByID(id);
            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("rC"));
        }
    }
}
