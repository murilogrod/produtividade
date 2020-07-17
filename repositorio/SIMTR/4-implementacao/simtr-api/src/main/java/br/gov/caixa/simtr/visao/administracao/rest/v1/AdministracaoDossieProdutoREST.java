package br.gov.caixa.simtr.visao.administracao.rest.v1;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.DossieProdutoServico;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDossieEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.DossieProdutoDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(hidden = false, value = "Administração - Dossiê de Produto")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("/administracao/v1")
@RequestScoped
@Produces({
    MediaType.APPLICATION_JSON,
    MediaType.APPLICATION_XML
})
@Consumes({
    MediaType.APPLICATION_JSON,
    MediaType.APPLICATION_XML
})
public class AdministracaoDossieProdutoREST extends AbstractREST {

    @EJB
    private DossieProdutoServico dossieProdutoServico;

    @EJB
    private AnalyticsServico analyticsServico;

    private static final String BASE_URL = "/administracao/v1";

    private final String PREFIXO = "SDN.ADPR.";

    @POST
    @Path("/dossie-produto/{id}/situacao/{tipo-situacao}")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Dossiê de Produto alterado com sucesso"),
        @ApiResponse(code = 403, message = "Usuário não autorizado a alterar o dossiê de produto", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Adiciona situação em um dossie de produto.")
    @Consumes({
        MediaType.TEXT_PLAIN
    })
    public Response adicionaSituacaoDossieProduto(@ApiParam(name = "id", value = "Identificador do dossiê de produto a ser alterado",
                                                            required = true) @PathParam("id") Long id, @ApiParam(name = "tipo-situacao", value = "Nova situacao a ser incluída no dossiê de produto",
                                                                                                                 required = true) @PathParam("tipo-situacao") SituacaoDossieEnum tipoSituacao, @ApiParam(name = "observacao",
                                                                                                                                                                                                         value = "Texto de observação que serve de comentário para a alteração da situação do dossie de produto.",
                                                                                                                                                                                                         required = false) String observacao, @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL
                                                                         + "/dossie-produto/{id}/situacao/{tipo-situacao}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id), "tipo-situacao", tipoSituacao.getDescricao()));
        try {
            this.dossieProdutoServico.adicionaSituacaoDossieProduto(id, tipoSituacao, observacao);
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aSDP"));
        }
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/dossie-produto/{id}/situacao/{quantidade}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Situações do Dossiê de Produto excluídas com sucesso."),
        @ApiResponse(code = 403, message = "Usuário não autorizado a excluir situações de dossiê de produto.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Exclui situações de um dossiê de produto")
    public Response excluiSituacaoDossieProduto(@ApiParam(name = "id", value = "Identificador do dossiê de produto a ser alterado",
                                                          required = true) @PathParam("id") Long id, @ApiParam(name = "quantidade", value = "Quantidade de situações a serem removidas",
                                                                                                               required = true) @PathParam("quantidade") Integer quantidade, @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.DELETE.name(), BASE_URL
                                                                           + "/dossie-produto/{id}/situacao/{quantidade}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id), "quantidade", String.valueOf(quantidade)));
        try {
            this.dossieProdutoServico.excluiSituacaoDossieProduto(id, quantidade);
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("eSDP"));
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PUT
    @Path("/dossie-produto/{id}/unidades-tratamento")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Dossiê de produto alterado com sucesso", response = DossieProdutoDTO.class),
        @ApiResponse(code = 404, message = "Dossiê de produto não localizado sob identificador informado.", response = RetornoErroDTO.class)
    })
    @ApiParam(name = "id", value = "ID do Dossie Produto.")
    public Response atualizarUnidadesManipuladora(@PathParam("id") Long idDossieProduto, List<Integer> listaUnidadesTratamento, @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.PUT.name(), BASE_URL
                                                                        + "/dossie-produto/{id}/unidades-tratamento", capturaEngineCliente(headers), montarValores("id", String.valueOf(idDossieProduto)));

        try {
            dossieProdutoServico.atualizarUnidadesTratamento(idDossieProduto, listaUnidadesTratamento);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aUM"));
        }

    }

    @DELETE
    @Path("/dossie-produto/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Dossiê Produto removido com sucesso."),
        @ApiResponse(code = 400, message = "Não foi identificado nenhum registro em situação de remoção com base no cógigo informado.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Exclui um Dossiê produto bem como todas as dependências e vinculos relacionados a ele. Realiza o cancelamento dos documentos no GED e do processo no BPM.")
    public synchronized Response removeDocumentosDossie(@PathParam("id") Long id, @Context HttpHeaders headers) {

        try {
            this.analyticsServico.registraEvento(EnumMetodoHTTP.DELETE.name(), BASE_URL + "/dossie-produto/{id}", capturaEngineCliente(headers), montarValores("idCliente", String.valueOf(id)));
            this.dossieProdutoServico.deleteDossieProduto(id);
        } catch (EJBException ee) {
            RetornoErroDTO resourceError = new RetornoErroDTO(Response.Status.CONFLICT.getStatusCode(), "Falha ao remover dossie produto", ee.getLocalizedMessage());
            return Response.status(Response.Status.CONFLICT).entity(resourceError).build();
        }
        return Response.noContent().build();

    }
}
