package br.gov.caixa.simtr.visao.cadastro.rest.v1;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.ComposicaoDocumentalServico;
import br.gov.caixa.simtr.modelo.entidade.ComposicaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.RegraDocumental;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.composicaodocumental.AlterarComposicaoDocumentalDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.composicaodocumental.ComposicaoDocumentalDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.composicaodocumental.CriarComposicaoDocumentalDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.composicaodocumental.RegraDocumentalDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import io.swagger.jaxrs.PATCH;

@Api(hidden = false, value = "Cadastro - Composição Documental")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("dossie-digital/v1/cadastro/composicao-documental")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ComposicaoDocumentalREST extends AbstractREST {

    @EJB
    private ComposicaoDocumentalServico composicaoDocumentalServico;
    
    @EJB
	private AnalyticsServico analyticsServico;
    
    private static final String BASE_URL = "/dossie-digital/v1/cadastro/composicao-documental";

    @GET
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Lista de Composição Documental.", response = ComposicaoDocumentalDTO.class, responseContainer = "List")
        ,
        @ApiResponse(code = 404, message = "Registro não localizado.")
    })
    @ApiOperation(hidden = false, value = "Função para listar Composição Documental")
    public Response listarComposicaoDocumental(@QueryParam("regras") final boolean carregarRegras, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            List<ComposicaoDocumentalDTO> listaComposicaoDocumentalDTO = new ArrayList<>();
            List<ComposicaoDocumental> listaComposicaoDocumental = composicaoDocumentalServico.list();
            listaComposicaoDocumental.forEach(c -> listaComposicaoDocumentalDTO.add(new ComposicaoDocumentalDTO(c, carregarRegras)));
            return Response.ok(listaComposicaoDocumentalDTO).build();

        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, "CD.lCD.001");
        }

    }

    @DELETE
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Composição Documental removida com sucesso.")
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.", response = RetornoErroDTO.class)})
    @ApiOperation(hidden = false, value = "Exclui a Composição Documental pelo Id Informado.")
    public synchronized Response removeComposicaoDocumental(@PathParam("id") Long idComposicaoDocumental, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.DELETE.name(), BASE_URL + "/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(idComposicaoDocumental)));
        try {

            this.composicaoDocumentalServico.delete(idComposicaoDocumental);
            return Response.noContent().build();

        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, "CD.rCD.001");
        }
    }

    @POST
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Composição Documental salva com sucesso", responseHeaders = {
            @ResponseHeader(name = "location", description = "URL de retorno utilizada para a consulta da Composição Documental com a identificação do registro criado.", response = String.class)
        })
        ,
	@ApiResponse(code = 400, message = "Erro na requisição")
        ,
	@ApiResponse(code = 503, message = "Erro de Execução no Servidor")
    })
    @ApiOperation(hidden = false, value = "Criar uma Composicao Documental.")
    public Response salvarComposicaoDocumental(final CriarComposicaoDocumentalDTO composicaoDocumentalDto, @Context UriInfo uriInfo,
    		@Context HttpHeaders headers) throws URISyntaxException {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {

            final ComposicaoDocumental composicaoDocumental = composicaoDocumentalDto.prototype();

            composicaoDocumentalServico.salvar(composicaoDocumental);

            return Response.created(new URI(uriInfo.getPath().concat("/"))).build();
        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, "CD.sCD.001");
        }
    }

    @PUT
    @Path("/{id}/regras-documentais")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Composição Documental salva com sucesso")
        ,
	@ApiResponse(code = 400, message = "Erro na requisição")
        ,
	@ApiResponse(code = 503, message = "Erro de Execução no Servidor")
    })
    @ApiOperation(hidden = false, value = "Criar ou Atualizar uma Composicao Documental.")
    public Response alterarComposicaoDocumentalRegrasDocumentais(@PathParam("id") Long idComposicaoDocumental, 
    		List<RegraDocumentalDTO> listaRegrasDocumentaisDTO, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.PUT.name(), BASE_URL + "/{id}/regras-documentais", capturaEngineCliente(headers), montarValores("id", String.valueOf(idComposicaoDocumental)));
        try {

            List<RegraDocumental> listaRegras = listaRegrasDocumentaisDTO.stream().map(RegraDocumentalDTO::prototype).collect(Collectors.toList());
            this.composicaoDocumentalServico.updateRegrasDocumentais(idComposicaoDocumental, listaRegras);
            return Response.noContent().build();
        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, "CD.aCD.001");
        }
    }

    @PATCH
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Composição Documental atualizada com sucesso")
        ,
	@ApiResponse(code = 400, message = "Erro na requisição")
        ,
	@ApiResponse(code = 503, message = "Erro de Execução no Servidor")
    })
    @ApiOperation(hidden = false, value = "Atualizar uma Composição Documental.")
    public Response alterarComposicaoDocumental(@PathParam("id") Long idComposicaoDocumental, 
    		AlterarComposicaoDocumentalDTO composicaoDocumental, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.PATCH.name(), BASE_URL + "/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(idComposicaoDocumental)));
        try {
            composicaoDocumentalServico.aplicarPatch(idComposicaoDocumental, composicaoDocumental);

            return Response.noContent().build();
        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, "PR.aCD.001");
        }
    }

}
