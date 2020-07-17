package br.gov.caixa.simtr.visao.cadastro.rest.v1;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import br.gov.caixa.simtr.controle.servico.VinculacaoChecklistServico;
import br.gov.caixa.simtr.modelo.entidade.VinculacaoChecklist;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.PendenciaCadastroDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist.CadastroVinculacaoChecklistDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@Api(hidden = false, value = "Cadastro - Vinculação de Checklist")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("cadastro/v1/vinculacao-checklist")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class VinculacaoCheckListREST extends AbstractREST {
    
    private final String PREFIXO = "CAD.VCR.";
    
    private static final String BASE_URL_CHECKLIST = "/cadastro/v1/checklist";
    private static final String BASE_URL = "/cadastro/v1/vinculacao-checklist";
    
    @EJB
    private AnalyticsServico analyticsServico;
    
    @EJB
    private VinculacaoChecklistServico vinculacaoCheckListServico;
    
    @POST
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Vinculação checklist criada com sucesso.", responseHeaders = {
            @ResponseHeader(name = "location", description = "Endereço com caminho a ser utilizado para consultar o recurso criado.", response = String.class)
        }),
        @ApiResponse(code = 400, message = "Falha na validação das informações.", response = PendenciaCadastroDTO.class, responseContainer = "List"),
    })
    @ApiOperation(hidden = false, value = "Cria uma vinculação de checklist para uso no tratamento de documentos.")
    public Response criaVinculacaoChecklist(CadastroVinculacaoChecklistDTO vinculacaoChecklistDTO,  @Context UriInfo uriInfo, @Context HttpHeaders headers) throws URISyntaxException {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            VinculacaoChecklist vinculacaoChecklist =  this.vinculacaoCheckListServico.montaNovaVinculacaoChecklist(vinculacaoChecklistDTO);
            
            String url = uriInfo.getPath().replace(BASE_URL, BASE_URL_CHECKLIST);
            String location = url.concat("/").concat(vinculacaoChecklist.getChecklist().getId().toString());
            return Response.created(new URI(location)).build();
        }catch(EJBException e) {
            return montaRespostaExcecao(e, this.PREFIXO.concat("cVCl"));
        }
    }
    
    @DELETE
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Alterações aplicadas com sucesso."),
        @ApiResponse(code = 400, message = "Falha na validação de informações.")})
    @ApiOperation(hidden = false, value = "Exclusão da Vinculação de Checklist.")
    public Response deletaChecklist(
             @ApiParam(name = "id", value = "Identificador da Vinculação de Checklist a ser excluída", required = true)
             @PathParam("id") Long id, 
             @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.DELETE.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            this.vinculacaoCheckListServico.excluirVinculacaoCheckList(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        }catch (EJBException e) {
            return this.montaRespostaExcecao(e, this.PREFIXO.concat("dCL"));
        }
    }
}
