package br.gov.caixa.simtr.visao.cadastro.rest.v1;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.FuncaoDocumentalServico;
import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.funcaodocumental.FuncaoDocumentalDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.funcaodocumental.FuncaoDocumentalManutencaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.funcaodocumental.FuncaoDocumentalNovoDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import io.swagger.jaxrs.PATCH;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

@Api(hidden = false, value = "Cadastro - Função Documental")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("/cadastro/v1/funcao-documental")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class FuncaoDocumentalREST extends AbstractREST {

    @EJB
    FuncaoDocumentalServico funcaoDocumentalServico;

    private final String PREFIXO = "CAD.FDR."; 
    
    @EJB
	private AnalyticsServico analyticsServico;
    
    private static final String BASE_URL = "/cadastro/v1/funcao-documental";

    @GET
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Lista de funções documentais disponíveis carregada com sucesso.", response = FuncaoDocumentalDTO.class, responseContainer = "List")
        ,
        @ApiResponse(code = 204, message = "Não existem funções documentais disponíveis.")})
    @ApiOperation(hidden = false, value = "Consulta lista de funções documentais disponíveis")
    public Response listFuncoesDocumentais(
            @ApiParam(name = "tipos", value = "Indica se a lista de tipos de documentos associados a cada função deve ser carregada conjuntamente")
            @QueryParam(value = "tipos") boolean carregaTiposDocumento, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            List<FuncaoDocumental> listaFuncoes = this.funcaoDocumentalServico.list();
            if (Objects.nonNull(listaFuncoes) && !listaFuncoes.isEmpty()) {
                List<FuncaoDocumentalDTO> listaFuncoesDocumentaisDTO = listaFuncoes.stream()
                        .map(funcaoDocumental -> new FuncaoDocumentalDTO(funcaoDocumental, carregaTiposDocumento))
                        .collect(Collectors.toList());
                return Response.ok(listaFuncoesDocumentaisDTO).build();
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
        @ApiResponse(code = 200, message = "Função documental localizada com sucesso", response = FuncaoDocumentalDTO.class)
        ,
        @ApiResponse(code = 404, message = "A função documental solicitada não foi localizada")})
    @ApiOperation(hidden = false, value = "Consulta de uma função documental especifica pelo identificador")
    public Response getFuncaoDocumentalByID(
            @ApiParam(name = "id", value = "Identificador da função documental a ser localizada")
            @PathParam("id") Integer id,
            @ApiParam(name = "tipos", value = "Indica se a lista de tipos de documentos associados a função deve ser carregada conjuntamente")
            @QueryParam(value = "tipos") boolean carregaTiposDocumento, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            FuncaoDocumental funcaoDocumental = this.funcaoDocumentalServico.getById(id);
            if (Objects.nonNull(funcaoDocumental)) {
                FuncaoDocumentalDTO funcaoDocumentalDTO = new FuncaoDocumentalDTO(funcaoDocumental, carregaTiposDocumento);
                return Response.ok(funcaoDocumentalDTO).build();
            } else {
                return Response.noContent().build();
            }
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gFDBI"));
        }
    }

    @POST
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Função documental criada com sucesso", responseHeaders = {
            @ResponseHeader(name = "location", description = "Endereço com caminho a ser utilizado para consultar o recurso criado")
        })
        ,
        @ApiResponse(code = 409, message = "A representação do objeto conflita com outro registro já existente. Verificar detalhamento no retorno.")
    })
    @ApiOperation(hidden = false, value = "Realiza a inclusão de uma nova função documental")
    public Response createFuncaoDocumental(FuncaoDocumentalNovoDTO funcaoDocumentalNovoDTO, @Context UriInfo uriInfo, 
    		@Context HttpHeaders headers) throws URISyntaxException {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            FuncaoDocumental funcaoDocumental = funcaoDocumentalNovoDTO.prototype();
            this.funcaoDocumentalServico.save(funcaoDocumental, funcaoDocumentalNovoDTO.getTiposDocumentoInclusaoVinculo());
            return Response.created(new URI(uriInfo.getPath().concat("/").concat(funcaoDocumental.getId().toString()))).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("cFD"));
        }
    }

    @PATCH
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Modificações encaminhadas executadas com sucesso")
        ,
        @ApiResponse(code = 409, message = "A representação do objeto conflita com outro registro já existente. Verificar detalhamento no retorno.")
    })
    @ApiOperation(hidden = false, value = "Realiza a alteração de uma função documental especifica")
    public Response applyPatchFuncaoDocumental(
            @ApiParam(name = "id", value = "Identificador da função documental a ser modificada")
            @PathParam("id") Integer id,
            FuncaoDocumentalManutencaoDTO funcaoDocumentalManutencaoDTO, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.PATCH.name(), BASE_URL + "/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {

            this.funcaoDocumentalServico.aplicaPatch(id, funcaoDocumentalManutencaoDTO);

            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aPFD"));
        }
    }

    @DELETE
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Função documental removida com sucesso")
        ,
        @ApiResponse(code = 409, message = "A função documental possui associações que a impedem de ser excluida.")
    })
    @ApiOperation(hidden = false, value = "Realiza a exclusão de uma função documental especifica")
    public Response removeFuncaoDocumental(
            @ApiParam(name = "id", value = "Identificador do função documental a ser excluída")
            @PathParam("id") Integer id, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.DELETE.name(), BASE_URL + "/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            this.funcaoDocumentalServico.delete(id);
            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("rFD"));
        }
    }
}
