package br.gov.caixa.simtr.visao.pae.rest.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
import br.gov.caixa.simtr.controle.servico.DocumentoAdministrativoServico;
import br.gov.caixa.simtr.modelo.entidade.DocumentoAdministrativo;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.pae.documento.DocumentoAdministrativoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.pae.documento.DocumentoAdministrativoManutencaoDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.jaxrs.PATCH;

@Api(hidden = false, value = "Processo Administrativo - Documento")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("/processoadministrativo/v1")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class DocumentoAdministrativoREST extends AbstractREST {

    @EJB
    private DocumentoAdministrativoServico documentoAdministrativoServico;

    private final String PREFIXO = "PAE.DAR.";
    
    @EJB
	private AnalyticsServico analyticsServico;
    
    private static final String BASE_URL = "/processoadministrativo/v1";

    @GET
    @Path("/documento/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Documento Administrativo localizado com sucesso.", response = DocumentoAdministrativoDTO.class)
        ,
        @ApiResponse(code = 204, message = "Documento Administrativo não localizado pelo identificador informado.")})
    @ApiOperation(hidden = false, value = "Consulta de Documento Administrativo pelo ID")
    public Response getDocumentoId(
            @ApiParam(name = "id", value = "Identificador do documento a ser consultado", required = true)
            @PathParam("id") Long id, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/documento/{id}", capturaEngineCliente(headers));
        try {
            DocumentoAdministrativo documento = this.documentoAdministrativoServico.getDocumentoById(id, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);

            if (documento == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            DocumentoAdministrativoDTO documentoDTO = new DocumentoAdministrativoDTO(documento);
            return Response.ok(documentoDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gDBI"));
        }
    }

    @PATCH
    @Path("/documento/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "As alterações do documento encaminhadas foram atualizadas com sucesso.")
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo do processo.")
    })
    @ApiOperation(hidden = false, value = "Atualização de informações de um Documento Administrativo")
    public Response atualizarDocumento(
            @ApiParam(name = "id", value = "Identificador do documento a ser alterado", required = true)
            @PathParam("id") Long id, DocumentoAdministrativoManutencaoDTO documentoDTO, @Context UriInfo uriInfo,
            @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.PATCH.name(), BASE_URL + "/documento/{id}", capturaEngineCliente(headers));
        try {
            Map<String, String> atributos = null;
            if (documentoDTO.getAtributosDocumento() != null) {
                atributos = documentoDTO.getAtributosDocumento().stream().collect(Collectors.toMap(ad -> ad.getChave(), ad -> ad.getValor()));
            }

            this.documentoAdministrativoServico.aplicaPatch(id, documentoDTO.getConfidencial(), documentoDTO.isValido(), documentoDTO.getDescricaoDocumento(), documentoDTO.getJustificativaSubstituicao(), documentoDTO.getOrigemDocumentoEnum(), documentoDTO.getCodigoTipoDocumento(), atributos);

            return Response.noContent().build();

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aD"));
        }

    }

    @GET
    @Path("/documento/descricao/{descricao}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Documento Administrativo localizado com sucesso.", response = DocumentoAdministrativoDTO.class, responseContainer = "List")
        ,
        @ApiResponse(code = 204, message = "Documento Administrativo não localizado contendo a descrição informada.")})
    @ApiOperation(hidden = false, value = "Consulta de Documento Administrativo pela descrição do documento.")
    public Response getDocumentoByDescricao(
            @ApiParam(name = "descricao", value = "Descrição (total ou parcial) do documento a ser localizado", required = true)
            @PathParam("descricao") String descricao, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/documento/descricao/{descricao}", capturaEngineCliente(headers));
        try {
            List<DocumentoAdministrativo> documentos = this.documentoAdministrativoServico.listByDescricao(descricao);
            if (documentos == null || documentos.isEmpty()) {
                return Response.noContent().build();
            }

            List<DocumentoAdministrativoDTO> documentosDTO = new ArrayList<>();
            documentos.forEach(documento -> documentosDTO.add(new DocumentoAdministrativoDTO(documento)));

            return Response.ok(documentosDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gDBD"));
        }
    }

    @DELETE
    @Path("/documento/{id}/{justificativa}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Documento removido com sucesso.")
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta da solicitação.", response = RetornoErroDTO.class)
        ,
        @ApiResponse(code = 404, message = "Documento não localizado sob o identificador informado.")
    })
    @ApiOperation(hidden = false, value = "Exclui um documento administrativo.")
    public Response removeDocumentoAdministrativo(
            @ApiParam(name = "id", value = "Identificador do contrato a ser excluido", required = true)
            @PathParam("id") Long id,
            @ApiParam(name = "justificativa", value = "Justificativa enviada para manter associado ao registro de exclusão do documento.", required = true)
            @PathParam("justificativa") String justificativa, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.DELETE.name(), BASE_URL + "/documento/{id}/{justificativa}", capturaEngineCliente(headers));
        try {
            this.documentoAdministrativoServico.deleteDocumentoAdministrativo(id, justificativa);
            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, "DAR.rDA");
        }

    }

}
