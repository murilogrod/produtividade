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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.CampoFormularioServico;
import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario.AlteracaoCadastroDefinicaoCampoFormularioDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario.CadasdroDefinicaoCampoFormularioDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario.CampoFormularioDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario.DefinicaoCampoFormularioDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import io.swagger.jaxrs.PATCH;

@Api(hidden = false, value = "Cadastro - Formulários")
@ApiResponses({
    @ApiResponse(code = 400, message = "Falha ao processar a requisição. Requisição mal formada sob apontamentos indicados.")
    ,
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("/cadastro/v1/formulario")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class FormularioRest extends AbstractREST {
	
    @EJB
    private CampoFormularioServico formularioServico;
	
    @EJB
    private AnalyticsServico analyticsServico;
    
    private static final String BASE_URL = "/cadastro/v1/formulario";

    private final String PREFIXO = "CAD.FR.";

    @GET
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Lista de Formulários obtida com sucesso.", response = CampoFormularioDTO.class, responseContainer = "List")
        ,
        @ApiResponse(code = 204, message = "Não foram localizados formulários definidos na plataforma")})
    @ApiOperation(hidden = false, value = "Consulta lista deformulários disponíveis")
    public Response listCamposFormularios(@Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
        	
            List<CampoFormularioDTO> listaCamposFormularios = this.formularioServico.listaFormulario();
            
            if (Objects.nonNull(listaCamposFormularios) && !listaCamposFormularios.isEmpty()) {
                return Response.ok(listaCamposFormularios).build();
            } else {
                return Response.noContent().build();
            }
        	
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("lCF"));
        }
    }
    
    
    @GET
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Formulário obtido com sucesso.", response = CampoFormularioDTO.class, responseContainer = "List")
        ,
        @ApiResponse(code = 204, message = "Não foram localizados registros com base nos parâmetros informados.")
        ,
        @ApiResponse(code = 400, message = "Estrutura de parametros informado incorretamente.")
        ,
        @ApiResponse(code = 404, message = "Registro não localizado sob identificador informado.")})
    @ApiOperation(hidden = false, value = "Consulta um formulário.")
    public Response buscaFormulario(
            @ApiParam(name = "id", required = true, value = "Identificador do processo originador ao qual o campo formulário deverá estar vinculado.")
            @PathParam("id") Integer id,
            @ApiParam(name = "processo-fase", required = false, value = "Identificador do processo fase ao qual o campo de formulário deverá estar vinculado.")
            @QueryParam("processo-fase") Integer idProcessoFase,
            @ApiParam(name = "tipo-relacionamento", required = false, value = "Identificador do tipo de relacionamento ao qual o campo de formulário deverá estar vinculado.")
            @QueryParam("tipo-relacionamento") Integer idTipoRelacionamento,
            @ApiParam(name = "produto", required = false, value = "Identificador do produto ao qual o campo de formulário deverá estar vinculado.")
            @QueryParam("produto") Integer idProduto,
            @ApiParam(name = "garantia", required = false, value = "Identificador da garantia fase ao qual o campo de formulário deverá estar vinculado.")
            @QueryParam("garantia") Integer idGarantia,
            @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/{id}", capturaEngineCliente(headers));
        try {
            List<CampoFormulario> listaCampoFormulario = this.formularioServico.consultaFormularioPorProcessoOrigem(id, idProcessoFase, idTipoRelacionamento, idProduto, idGarantia);
            if (Objects.nonNull(listaCampoFormulario) && !listaCampoFormulario.isEmpty()) {
                List<DefinicaoCampoFormularioDTO> listaDefinicoesCampoFormulario = listaCampoFormulario.stream().map(campoFormulario -> new DefinicaoCampoFormularioDTO(campoFormulario)).collect(Collectors.toList());
                return Response.ok(listaDefinicoesCampoFormulario).build();
            } else {
                return Response.noContent().build();
            }
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("bF"));
        }
    }
    
    @POST
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Campo de formulário cadastrado com sucesso.", responseHeaders = {
            @ResponseHeader(name = "location", description = "Endereço com caminho a ser utilizado para consultar o recurso criado.", response = String.class)
        }),
        @ApiResponse(code = 400, message = "Falha na validação da requisição.")
        ,
        @ApiResponse(code = 404, message = "Registro não localizado sob identificador informado.")})
    @ApiOperation(hidden = false, value = "Cadastra um campo de formulário.")
    public Response salvaCampoFormulario(
            @ApiParam(name = "id", required = true, value = "Identificador do processo originador ao qual o campo formulário deverá estar vinculado.")
            @PathParam("id") Integer id,
            @ApiParam(name = "processo-fase", required = false, value = "Identificador do processo fase ao qual o campo de formulário deverá estar vinculado.")
            @QueryParam("processo-fase") Integer idProcessoFase,
            @ApiParam(name = "tipo-relacionamento", required = false, value = "Identificador do tipo de relacionamento ao qual o campo de formulário deverá estar vinculado.")
            @QueryParam("tipo-relacionamento") Integer idTipoRelacionamento,
            @ApiParam(name = "produto", required = false, value = "Identificador do produto ao qual o campo de formulário deverá estar vinculado.")
            @QueryParam("produto") Integer idProduto,
            @ApiParam(name = "garantia", required = false, value = "Identificador da garantia fase ao qual o campo de formulário deverá estar vinculado.")
            @QueryParam("garantia") Integer idGarantia,
            CadasdroDefinicaoCampoFormularioDTO cadasdroDefinicaoCampoFormularioDTO,
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers) throws URISyntaxException{
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/{id}", capturaEngineCliente(headers));
        try {
            CampoFormulario campoFormulario = cadasdroDefinicaoCampoFormularioDTO.prototype();
            this.formularioServico.cadastraFormularioPorProcessoOrigem(campoFormulario, id, idProcessoFase, idTipoRelacionamento, idProduto, idGarantia);
            String location = uriInfo.getPath().concat("/").concat(campoFormulario.getId().toString());
            return Response.created(new URI(location)).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("sCF"));
        }
    }
    
    @PATCH
    @Path("/campo/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Campo de formulário atualizado com sucesso.")
        ,
        @ApiResponse(code = 400, message = "Falha na validação da requisição.")
        ,
        @ApiResponse(code = 404, message = "Registro não localizado sob identificador informado.")})
    @ApiOperation(hidden = false, value = "Altera um campo de formulário.")
    public Response atualizaCampoFormulario(
            @ApiParam(name = "id", required = true, value = "Identificador do campo formulário a ser alterado.")
            @PathParam("id") Long id,
            AlteracaoCadastroDefinicaoCampoFormularioDTO alteracaoCadasdroCampoFormularioDTO,
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers) throws URISyntaxException{
        this.analyticsServico.registraEvento(EnumMetodoHTTP.PATCH.name(), BASE_URL + "/campo/{id}", capturaEngineCliente(headers));
        try {
            this.formularioServico.alteraCampoFormulario(id, alteracaoCadasdroCampoFormularioDTO);
            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aCF"));
        }
    }

    @DELETE
    @Path("/campo/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Campo de formulário excluído com sucesso.")
        ,
        @ApiResponse(code = 404, message = "Campo de formulário não localizado sob o identificador informado.")})
    @ApiOperation(hidden = false, value = "Exclui um campo de formulário.")
    public Response deletaCampoFormulario(
            @ApiParam(name = "id", required = true, value = " Identificador do campo de formulário a ser removido ou inabilitado.")
            @PathParam("id") Long id,
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers) throws URISyntaxException{
        this.analyticsServico.registraEvento(EnumMetodoHTTP.DELETE.name(), BASE_URL + "/campo/{id}", capturaEngineCliente(headers));
        try {
            this.formularioServico.excluiCampoFormulario(id);
            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("dCF"));
        }
    }
}
