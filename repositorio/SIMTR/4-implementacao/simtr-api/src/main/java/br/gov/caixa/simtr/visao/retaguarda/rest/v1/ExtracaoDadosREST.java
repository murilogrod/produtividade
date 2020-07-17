package br.gov.caixa.simtr.visao.retaguarda.rest.v1;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.AvaliacaoExtracaoServico;
import br.gov.caixa.simtr.controle.servico.CanalServico;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.gov.caixa.simtr.controle.servico.DocumentoServico;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.extracaodados.DocumentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.extracaodados.PendenciaExtracaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.extracaodados.ResultadoExtracaoDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.jaxrs.PATCH;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.DELETE;

@Api(hidden = false, value = "Retaguarda - Extração de Dados", tags = {"Retaguarda - Extração de Dados"})
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("retaguarda/v1/extracao-dados")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ExtracaoDadosREST extends AbstractREST {

    @EJB
    private AvaliacaoExtracaoServico avaliacaoExtracaoServico;
    
    @EJB
    private CanalServico canalServico;
    
    @EJB
    private DocumentoServico documentoServico;


    private final String PREFIXO = "V1.RET.EDR.";
    
    @EJB
	private AnalyticsServico analyticsServico;
    
    private static final String BASE_URL = "/retaguarda/v1/extracao-dados";

    @GET
    @Path("/tipo-documento/pendente")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Lista de tipos de documentos indicados para extração de dados com a quantidade de documentos pendentes.", response = PendenciaExtracaoDTO.class, responseContainer = "List")
        ,
        @ApiResponse(code = 204, message = "Não foram localizados documentos pendentes de extração de dados.")
    })
    @ApiOperation(hidden = false, value = "Lista a quantidade de documenos que estão pendentes de extração de dados com a identificação da tipologia.")
    public Response listDocumentosExtracao(@Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/tipo-documento/pendente", capturaEngineCliente(headers));
        try {
            Map<TipoDocumento, List<Documento>> mapaDocumentosExtracao = this.avaliacaoExtracaoServico.listaDocumentosPendentesExtracao(Boolean.FALSE);
            if (mapaDocumentosExtracao.isEmpty()) {
                return Response.status(Response.Status.NO_CONTENT).build();
            }
            List<PendenciaExtracaoDTO> pendenciasDTO = new ArrayList<>();
            mapaDocumentosExtracao.forEach((td, docs) -> pendenciasDTO.add(new PendenciaExtracaoDTO(td, docs.size())));
            return Response.ok(pendenciasDTO).build();
        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, this.PREFIXO.concat("lDE"));
        }
    }

    @POST
    @Path("/tipo-documento/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Documento capturado para realização da extração de dados.", response = DocumentoDTO.class)
        ,
        @ApiResponse(code = 404, message = "Não foram localizados documentos pendentes de extração de dados vinculados ao tipo informado.")
    })
    @ApiOperation(hidden = false, value = "Captura o documento mais antigo pendente de extração de dados para a tipologia informada")
    public Response capturaDocumentoExtracaoByTipoDocumento(
            @ApiParam(name = "id", value = "Atributo utilizado para identificar o tipo de documento utilizado para realizar a captura", allowEmptyValue = false)
            @PathParam("id") Integer idTipoDocumento, 
            @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/tipo-documento/{id}", capturaEngineCliente(headers));
        try {
            Documento documento = this.avaliacaoExtracaoServico.capturaDocumentoExtracaoByTipoDocumento(idTipoDocumento);
            if (documento == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            Documento documentoRetorno = this.documentoServico.getById(documento.getId(), Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);

            DocumentoDTO documentoDTO = new DocumentoDTO(documentoRetorno);
            return Response.ok(documentoDTO).build();
        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, this.PREFIXO.concat("cDEBTD"));
        }
    }

    @POST
    @Path("/documento/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Documento capturado para realização da extração de dados.", response = DocumentoDTO.class)
        ,
        @ApiResponse(code = 404, message = "Não foi localizado documento vinculados ao identificador informado.")
    })
    @ApiOperation(hidden = false, value = "Captura o documento definido pelo identificador para efetivar a extração de dados")
    public Response capturaDocumentoExtracaoByIdentificadorDocumento(
            @ApiParam(name = "id", value = "Representa o identificador do documento utilizado para realizar a captura", allowEmptyValue = false, required = true)
            @PathParam("id") Long idDocumento,
            @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/documento/{id}", capturaEngineCliente(headers));
        try {
            Documento documento = this.avaliacaoExtracaoServico.capturaDocumentoExtracaoByIdDocumento(idDocumento);
            if (documento == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            Documento documentoRetorno = this.documentoServico.getById(documento.getId(), Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);

            DocumentoDTO documentoDTO = new DocumentoDTO(documentoRetorno);
            return Response.ok(documentoDTO).build();
        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, this.PREFIXO.concat("cDEBID"));
        }
    }

    @PATCH
    @Path("/resultado/{codigo-controle}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Documento atualizado com sucesso com os resultados informados.")
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Atualiza as informações do documento referente a extração de dados.")
    public Response resultadoAvaliacaoExtracao(
            @ApiParam(value = "Código de controle associado ao documento utilizado na sua identificado e correta atualização do controle de fluxo do registro")
            @PathParam("codigo-controle") String codigoControle,
            ResultadoExtracaoDTO resultadoAvaliacaoExtracaoDTO,
            @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.PATCH.name(), BASE_URL + "/resultado/{codigo-controle}", capturaEngineCliente(headers));
        try {

            Documento documento = this.avaliacaoExtracaoServico.atualizaInformacaoDocumentoOutsourcing(codigoControle, resultadoAvaliacaoExtracaoDTO.prototype());
            this.canalServico.notificaCanalSubmissor(documento);

            return Response.noContent().build();
        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, this.PREFIXO.concat("rAE"));
        }
    }

    @DELETE
    @Path("/controle/{codigo-controle}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Registro de controle excluido com sucesso")
        ,
        @ApiResponse(code = 400, message = "Não foi identificado nenhum registro em situação de remoção com base no cógigo de controle informado.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Remove o registro de controle para o serviço de extração de dados. Ação realizada na desistência de execução da operação.")
    public Response removeRegistroControle(
            @ApiParam(name = "codigo-controle", value = "Atributo utilizado para identificar o código de controle a ser removido", allowEmptyValue = false, required = true)
            @PathParam("codigo-controle") String codigoControle,
            @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.DELETE.name(), BASE_URL + "/controle/{codigo-controle}", capturaEngineCliente(headers));
        try {

            this.avaliacaoExtracaoServico.removeRegistroControle(codigoControle);
            
            return Response.noContent().build();
        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, this.PREFIXO.concat("rRC"));
        }
    }
}
