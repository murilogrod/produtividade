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
import br.gov.caixa.simtr.controle.servico.ChecklistServico;
import br.gov.caixa.simtr.controle.vo.ChecklistVO;
import br.gov.caixa.simtr.modelo.entidade.Apontamento;
import br.gov.caixa.simtr.modelo.entidade.Checklist;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.PendenciaCadastroDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist.AlteracaoApontamentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist.CadastroApontamentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist.CadastroChecklistDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist.ChecklistDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist.ChecklistsDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import io.swagger.jaxrs.PATCH;

@Api(hidden = false, value = "Cadastro - Checklist")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("cadastro/v1/checklist")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class CheckListREST extends AbstractREST{
    
    private final String PREFIXO = "CAD.CLR.";
    
    @EJB
    private ChecklistServico checkListServico;
    
    @EJB
    private AnalyticsServico analyticsServico;
    
    private static final String BASE_URL = "/cadastro/v1/checklist";
    
    
    @POST
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Checklist criado com sucesso.", responseHeaders = {
            @ResponseHeader(name = "location", description = "Endereço com caminho a ser utilizado para consultar o recurso criado.", response = String.class)
        }),
        @ApiResponse(code = 409, message = "A requisição passada não contempla os requisitos necessários para criação de um novo checklist.", response = PendenciaCadastroDTO.class, responseContainer = "List"),
        @ApiResponse(code = 403, message = "Usuário não autorizado a criar um checklist.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Cria um checklist para uso no tratamento de documentos.")
    public Response criaChecklist(CadastroChecklistDTO checklistDTO,  @Context UriInfo uriInfo, @Context HttpHeaders headers) throws URISyntaxException {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/", capturaEngineCliente(headers));
        Checklist checklist = checklistDTO.prototype();
        try {
            this.checkListServico.criaNovoChecklist(checklist);
            String location = uriInfo.getPath().concat("/").concat(checklist.getId().toString());
            return Response.created(new URI(location)).entity(checklist.getId()).build();
        }catch(EJBException e) {
            return montaRespostaExcecao(e, this.PREFIXO.concat("cCl"));
        }
    }
    
    @PATCH
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Checklist alterado com sucesso."),
        @ApiResponse(code = 409, message = "Falha na validação de informações para alteração de checklist.", response = PendenciaCadastroDTO.class, responseContainer = "List"),
        @ApiResponse(code = 403, message = " Checklist não pode ser alterado pois já possui operações associadas.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Altera um checklist para uso no tratamento de documentos.")
    public Response atualizaChecklist(
             @ApiParam(name = "id", value = "Identificador do checklist", required = true)
             @PathParam("id") Integer id, 
             CadastroChecklistDTO checklistDTO,  
             @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.PATCH.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            this.checkListServico.alteraChecklist(id, checklistDTO);
            return Response.status(Response.Status.NO_CONTENT).build();
        }catch(EJBException e) {
            return montaRespostaExcecao(e, this.PREFIXO.concat("cCl"));
        }
    }
    
    @GET
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Checklists carregados com sucesso.", response = ChecklistsDTO.class, responseContainer = "List"),
        
        @ApiResponse(code = 204, message = "Não foi encontrado nenhum checklist correspondente.")})
    @ApiOperation(hidden = false, value = "Consulta lista de checklist inteira.")
    public Response carregaCheckLists(@Context HttpHeaders headers) { 
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            List<ChecklistVO> listaCheckist = this.checkListServico.carregaChecklists();
            if (Objects.nonNull(listaCheckist) && !listaCheckist.isEmpty()) {
                List<ChecklistsDTO> listaCheckistDTO = listaCheckist.stream().map(checklist -> new ChecklistsDTO(checklist)).collect(Collectors.toList());
                return Response.ok(listaCheckistDTO).build();
            }
        }catch(EJBException e) {
            return montaRespostaExcecao(e, this.PREFIXO.concat("cCL"));
        }
        return Response.noContent().build();
    }
    
    @GET
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Checklist capturado com sucesso.", response = ChecklistDTO.class),
        @ApiResponse(code = 404, message = "Checklist não localizado sob identificador informado.")})
    @ApiOperation(hidden = false, value = "Consulta um checklist pelo seu identificador.")
    public Response carregaCheckList(
             @ApiParam(name = "id", value = "Identificador do checklist", required = true)
             @PathParam("id") Integer id, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            ChecklistVO checkist = this.checkListServico.carregaChecklistById(id);
            if (Objects.nonNull(checkist)) {
                ChecklistDTO checklistDTO = new ChecklistDTO(checkist);
                return Response.ok(checklistDTO).build();
            }
        }catch(EJBException e) {
            return montaRespostaExcecao(e, this.PREFIXO.concat("cCL"));
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @DELETE
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Exclusão de checklist realizada."),
        @ApiResponse(code = 400, message = "Checklist não localizado sob o identificador informado.")})
    @ApiOperation(hidden = false, value = "Deleta um checklist.")
    public Response deletaChecklist(
             @ApiParam(name = "id", value = "Identificador do checklist.", required = true)
             @PathParam("id") Integer id, 
             @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.DELETE.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            this.checkListServico.excluiChecklist(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        }catch (EJBException e) {
            return this.montaRespostaExcecao(e, this.PREFIXO.concat("dCL"));
        }
    }
    
    @POST
    @Path("/{id}/apontamento")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Apontamento incluído com sucesso.", responseHeaders = {
            @ResponseHeader(name = "location", description = "Endereço com caminho a ser utilizado para consultar o recurso criado.", response = String.class)
        }),
        @ApiResponse(code = 400, message = " Falha na validação de informações.", response = PendenciaCadastroDTO.class, responseContainer = "List"),
        @ApiResponse(code = 403, message = "Usuário não autorizado a criar um apontamento.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Cria um apontamento para uso no tratamento de documentos.")
    public Response cadastraApontamento(
             @ApiParam(name = "id", value = "Identificador do checklist.", required = true)
             @PathParam("id") Integer id, 
             CadastroApontamentoDTO apontamentoDTO,  
             @Context UriInfo uriInfo, 
             @Context HttpHeaders headers) throws URISyntaxException {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/", capturaEngineCliente(headers));
        Apontamento apontamento = apontamentoDTO.prototype();
        try {
            this.checkListServico.cadastraApontamentoChecklistAtual(id, apontamento);
            String location = uriInfo.getPath().concat("/").concat(apontamento.getId().toString());
            return Response.created(new URI(location)).build();
        }catch(EJBException e) {
            return montaRespostaExcecao(e, this.PREFIXO.concat("cA"));
        }
    }
    
    @PATCH
    @Path("/{id}/apontamento/{idApontamento}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Alterações aplicadas com sucesso."),
        @ApiResponse(code = 400, message = "Falha na validação de informações.", response = PendenciaCadastroDTO.class, responseContainer = "List"),
        @ApiResponse(code = 403, message = "Usuário não autorizado a criar um apontamento.", response = RetornoErroDTO.class),
        @ApiResponse(code = 409, message = "Checklist impossibilitado de realizar modificações.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Altera um apontamento para uso no tratamento de documentos.")
    public Response atualizaApontamento(
             @ApiParam(name = "id", value = "Identificador do checklist.", required = true)
             @PathParam("id") Integer id,
             @ApiParam(name = "idApontamento", value = "Identificador do apontamento.", required = true)
             @PathParam("idApontamento") Long idApontamento, 
             AlteracaoApontamentoDTO apontamentoDTO,
             @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.PATCH.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            this.checkListServico.alteraApontamentoChecklistAtual(id, idApontamento, apontamentoDTO);
            return Response.status(Response.Status.NO_CONTENT).build();
        }catch(EJBException e) {
            return montaRespostaExcecao(e, this.PREFIXO.concat("aA"));
        }
    }
    
    @DELETE
    @Path("/{id}/apontamento/{idApontamento}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Exclusão realizada com sucesso."),
        @ApiResponse(code = 400, message = "Falha na validação de informações.", response = PendenciaCadastroDTO.class, responseContainer = "List"),
        @ApiResponse(code = 409, message = "Checklist impossibilitado de realizar modificações.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Deleta um apontamento de um checklist.")
    public Response deletaApontamento(
             @ApiParam(name = "id", value = "Identificador do checklist.", required = true)
             @PathParam("id") Integer id,
             @ApiParam(name = "idApontamento", value = "Identificador do apontamento.", required = true)
             @PathParam("idApontamento") Long idApontamento,
             @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.DELETE.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            this.checkListServico.deletaApontamentoChecklistAtual(id, idApontamento);
            return Response.status(Response.Status.NO_CONTENT).build();
        }catch(EJBException e) {
            return montaRespostaExcecao(e, this.PREFIXO.concat("dA"));
        }
    }
}
