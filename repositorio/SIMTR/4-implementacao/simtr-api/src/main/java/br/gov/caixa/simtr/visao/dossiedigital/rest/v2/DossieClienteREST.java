package br.gov.caixa.simtr.visao.dossiedigital.rest.v2;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
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
import br.gov.caixa.simtr.controle.servico.DossieClienteServico;
import br.gov.caixa.simtr.controle.servico.DossieDigitalServico;
import br.gov.caixa.simtr.controle.vo.DocumentoVO;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.SolicitacaoExtracaoDadosDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v2.dossiedigital.dossiecliente.RetornoExtracaoDadosDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(hidden = false, tags = {
    "Dossiê Digital - Dossiê Cliente"
})
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("dossie-digital/v2/dossie-cliente/")
@Consumes({
    MediaType.APPLICATION_JSON,
    MediaType.APPLICATION_XML
})
@Produces({
    MediaType.APPLICATION_JSON,
    MediaType.APPLICATION_XML
})
public class DossieClienteREST extends AbstractREST {

    @EJB
    private DossieDigitalServico dossieDigitalServico;

    @EJB
    private DossieClienteServico dossieClienteServico;

    @EJB
    private AnalyticsServico analyticsServico;

    private static final String BASE_URL = "/dossie-digital/v2/dossie-cliente/";

    private final String PREFIXO = "DOS.DCR.V1.";

    @POST
    @Path("{id}/extracao-dados")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Documento encaminhado para extração de dados com sucesso.", response = RetornoExtracaoDadosDTO.class),
        @ApiResponse(code = 400, message = "Binario que representa o documento não é um binario Base64 valido."),
        @ApiResponse(code = 403, message = "Canal de comunicação não associado ao cliente ID da autenticação ou canal não autorizado a consumir os serviçosa do Dossiê Digital."),
        @ApiResponse(code = 404, message = "Dossiê de cliente não localizado sob o identificador informado"),
        @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada."),
        @ApiResponse(code = 503, message = "Falha de comunicação com o SIECM")
    })
    @ApiOperation(hidden = false, value = "Extração de dados da imagem do documento (OCR) vinculado a um cliente indicado", tags = {
        "Dossiê Digital - Dossiê Cliente"
    })
    public Response extrairDadosImagem(@HeaderParam("processo") @DefaultValue("PADRAO") String processo,@PathParam("id")
    final Long id, final SolicitacaoExtracaoDadosDTO solicitacaoExtracaoDadosDTO, @Context HttpHeaders headers) {
        RetornoExtracaoDadosDTO respostaExtracaoDadosDTO = new RetornoExtracaoDadosDTO();
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "{id}/extracao-dados", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));

        try {
            FormatoConteudoEnum formatoConteudoEnum = FormatoConteudoEnum.getByMimeType(solicitacaoExtracaoDadosDTO.getMimetype());
            DocumentoVO documentoVo = this.dossieDigitalServico.extraiDadosDocumentoDossieDigitalExterno(id, solicitacaoExtracaoDadosDTO, formatoConteudoEnum, processo);

            Documento documento = documentoVo.getDocumentoSIMTR();
            respostaExtracaoDadosDTO.setIdentificadorSimtr(documento.getId());

            FormatoConteudoEnum formatoConteudoRetornoEnum = documento.getCodigoSiecmTratado() == null ? documento.getFormatoConteudoEnum() : documento.getFormatoConteudoTratadoEnum();
            respostaExtracaoDadosDTO.setMimetype(formatoConteudoRetornoEnum.getMimeType());

            String codigoSiecm = documento.getCodigoSiecmTratado() == null ? documento.getCodigoGED() : documento.getCodigoSiecmTratado();
            respostaExtracaoDadosDTO.setIdentificadorSiecm(codigoSiecm);
            respostaExtracaoDadosDTO.setObjectStore(ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
            respostaExtracaoDadosDTO.setLink(documentoVo.getLinkGED());

            return Response.ok(respostaExtracaoDadosDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("sED"));
        }
    }
}
