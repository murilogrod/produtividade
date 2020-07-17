package br.gov.caixa.simtr.visao.negocio.rest.v1;

import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.verificapdf.entidade.InfoAssinatura;
import br.gov.caixa.pedesgo.verificapdf.servico.VerificadorAssinaturaPdf;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.DocumentoServico;
import br.gov.caixa.simtr.controle.servico.DominioServico;
import br.gov.caixa.simtr.controle.servico.FuncaoDocumentalServico;
import br.gov.caixa.simtr.controle.servico.TipoDocumentoServico;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.Dominio;
import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.documento.DocumentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tipologia.TipologiaDocumentalDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tipologia.tipo.TipoDocumentoDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(hidden = false, value = "Apoio ao Negócio - Documento")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("negocio/v1/documento")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class DocumentoREST extends AbstractREST {

    @EJB
    private DocumentoServico documentoServico;

    @EJB
    private FuncaoDocumentalServico funcaoDocumentalServico;

    @EJB
    private TipoDocumentoServico tipoDocumentoServico;
    
    @EJB
    private DominioServico dominioServico;
    
    @EJB
	private AnalyticsServico analyticsServico;
    
    private final String PREFIXO = "SDN.DR.";

    private static final Logger LOGGER = Logger.getLogger(DocumentoREST.class.getName());
    
    private static final String BASE_URL = "/negocio/v1/documento";

    @GET
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Retorna ao documento formato base64.", response = DocumentoDTO.class)
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.", response = RetornoErroDTO.class)
        ,
        @ApiResponse(code = 409, message = "Falha ao persistir o dossiê de produto por inconsistência de parâmetros.")
    })
    @ApiOperation(hidden = false, value = "Captura o conteudo de um documento solicitado junto ao GED, converte para base64 no formato de armazenamento submetido.")
    public Response capturaImagemSIECM(@PathParam("id") Long id, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            Documento documento = this.documentoServico.getById(id, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);

            String binario = documento.getConteudos().stream().map(c -> c.getBase64()).findFirst().orElse(null);
            DocumentoDTO documentoDTO = new DocumentoDTO(documento, binario);

            return Response.ok(documentoDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("cIS"));
        }
    }

    @GET
    @Path("/tipologia")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Retorna a lista de tipos documetais definios no sistema.", response = TipologiaDocumentalDTO.class)
    })
    @ApiOperation(hidden = false, value = "Lista a tipologia documental definida para o sistema. Possui uma lista pela ótica das funções e uma lista pelo ótica do tipo de documento.")
    public Response listaTiposDocumentais(@Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/tipologia", capturaEngineCliente(headers));
        try {
            List<TipoDocumento> tiposDocumento = this.tipoDocumentoServico.list().stream().filter(td -> td.getUsoApoioNegocio() != null && td.getUsoApoioNegocio()).collect(Collectors.toList());
            List<FuncaoDocumental> funcoesDocumentais = this.funcaoDocumentalServico.list().stream().filter(fd -> fd.getUsoApoioNegocio() != null && fd.getUsoApoioNegocio()).collect(Collectors.toList());
            List<Dominio> listaDominios = this.dominioServico.list();
            TipologiaDocumentalDTO tipologiaDocumentalDTO = new TipologiaDocumentalDTO(this.tipoDocumentoServico.getDataHoraUltimaAlteracao(), this.funcaoDocumentalServico.getDataHoraUltimaAlteracao(), funcoesDocumentais, tiposDocumento, listaDominios);

            return Response.ok(tipologiaDocumentalDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("lTD"));
        }
    }

    @GET
    @Path("/tipo/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Retorna o tipo de documento solicitado.", response = TipoDocumentoDTO.class)
        ,
        @ApiResponse(code = 404, message = "Tipo de documento não localizado.")
    })
    @ApiOperation(hidden = false, value = "Captura o tipo de documento especificado pelo identificador informado.")
    @ApiParam(name = "id", value = "Identificador do tipo de documento desejado")
    @Deprecated
    public Response getTipoDocumento(@PathParam("id") Integer id, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/tipo/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            final TipoDocumento tipoDocumento = this.tipoDocumentoServico.getById(id);
            if (tipoDocumento == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            TipoDocumentoDTO tipoDocumentoDTO = new TipoDocumentoDTO(tipoDocumento);
            return Response.ok(tipoDocumentoDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gTD"));
        }
    }

    @POST
    @Path("/assinatura")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Retorna confirmação de existênica de assinatura digital em um pdf.", response = Boolean.class)
    })
    @ApiOperation(hidden = false, value = "Verifica se um documento pdf contém assinatura digital.")
    @ApiParam(name = "arquivo", value = "Arquivo pdf que pode ter uma assinatura digital.")
    public Response verificaAssinaturaDocumento(String arquivo, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/assinatura", capturaEngineCliente(headers));
        try {
            byte[] byteArrayFile = Base64.getDecoder().decode(arquivo.getBytes("UTF-8"));
            PdfReader reader = new PdfReader(byteArrayFile);
            AcroFields acroFields = reader.getAcroFields();
            List<String> signatureNames = acroFields.getSignatureNames();
            if (signatureNames.isEmpty()) {
                return Response.ok(false).build();
            }
            VerificadorAssinaturaPdf verificadorPDF = new VerificadorAssinaturaPdf();
            InfoAssinatura infoAssinatura = verificadorPDF.verificarAssinatura(null, byteArrayFile);
            LOGGER.log(Level.ALL, infoAssinatura.toString());
            return Response.ok(true).build();
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getLocalizedMessage(), e);
            RetornoErroDTO resourceError = new RetornoErroDTO(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    "Falha ao verificar assinatura degital do documento.", e.getLocalizedMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resourceError).build();
        }
    }
}
