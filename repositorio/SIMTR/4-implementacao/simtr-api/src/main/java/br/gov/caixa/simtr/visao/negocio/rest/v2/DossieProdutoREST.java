package br.gov.caixa.simtr.visao.negocio.rest.v2;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.DossieDigitalServico;
import br.gov.caixa.simtr.controle.servico.IntegracaoSistemasServico;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(hidden = false, value = "Apoio ao Negócio - Dossiê Produto")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("negocio/v2")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class DossieProdutoREST extends AbstractREST {
    
    @EJB
    private AnalyticsServico analyticsServico;
    
    @EJB
    private IntegracaoSistemasServico integracaoSistemasServico;
    
    @EJB
    private DossieDigitalServico dossieDigitalServico;

    private static final String BASE_URL = "/negocio/v2/dossie-produto";

    private final String PREFIXO = "SDN.DPR.";

    @POST
    @Path("/dossie-produto/{id}/cadastro-caixa/{classe}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Atualização realizada com sucesso"),
        @ApiResponse(code = 400, message = "Os documentos do cliente possuem dados incompletos, ou foram informados documentos não pertencentes do dossiê indicado", response = RetornoErroDTO.class),
        @ApiResponse(code = 428, message = "Não foram encontrados documentos aptos a executar atualização cadastral", response = RetornoErroDTO.class),
        @ApiResponse(code = 503, message = "Erro de comunicação com o sistema de Cadastro de clientes da CAIXA", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Atualização dos dados cadastrais do cliente ao cadastro CAIXA com guarda permanente dos documentos.")
    public Response atualizaCadastroCaixaDossieProduto(
             @ApiParam("Identificador do dossiê de produto a ser utilizado como base para a operação de atualização cadastral")
             @PathParam("id") final Long id,
             @ApiParam("Identificador do tipo da classe a ser utilizada no corpo da requisição feita ao SICLI.")
             @PathParam("classe") final Integer classe,
             @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "{id}/cadastro-caixa/{classe}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            this.integracaoSistemasServico.atualizaCadastroCaixaDossieProduto(id, classe);
            
            this.dossieDigitalServico.atualizarDCadastroCaixaDossieProdutoPf(id);
            
            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aCCDP"));
        }
    }
    
}
