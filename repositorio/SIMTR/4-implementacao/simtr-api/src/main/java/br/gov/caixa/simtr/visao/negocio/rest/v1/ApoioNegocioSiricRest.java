package br.gov.caixa.simtr.visao.negocio.rest.v1;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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
import br.gov.caixa.pedesgo.arquitetura.siric.dto.propostas.AvaliacaoOperacaoRespDTO;
import br.gov.caixa.pedesgo.arquitetura.siric.dto.propostas.AvaliacaoTomadorRespDTO;
import br.gov.caixa.pedesgo.arquitetura.siric.exceptions.SiricException;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.ApoioNegocioSiricServico;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric.AvaliacaoOperacaoSimplesPJDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric.AvaliacaoTomadorPJDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric.PropostaOperacaoRespostaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric.PropostaTomadorRespostaDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @author f525904
 *
 */

@Api(hidden = false, value = "Apoio ao Negócio - SIRIC")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("negocio/v1")
@RequestScoped
@Produces({
    MediaType.APPLICATION_JSON
})
@Consumes({
    MediaType.APPLICATION_JSON
})
public class ApoioNegocioSiricRest extends AbstractREST {

    @EJB
    private ApoioNegocioSiricServico apoioNegocioSiricServico;

    @EJB
    private AnalyticsServico analyticsServico;

    private final String PREFIXO = "ANSR.";

    private static final String BASE_URL = "/negocio/v1";

    private static final String URL_CALLBACK_TOMADOR = "/avaliacao-tomador-pj/callback";
    
    private static final String URL_CALLBACK_OPERACAO = "/avaliacao-operacao-pj/simples/callback";

    @POST
    @Path("/avaliacao-tomador-pj/solicitacao")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Avaliação recebida com sucesso."),
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Solicitar avaliação do tomador pessoa jurídica.")
    public Response avalicaoTomadorPJ(AvaliacaoTomadorPJDTO avaliacaoTomadorPJDTO, @Context HttpHeaders headers, @Context UriInfo uriInfo, @Context HttpServletRequest request) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {

            String url = request.getRequestURL().toString();
            String urlCallBack = url.replace("/avaliacao-tomador-pj/solicitacao", URL_CALLBACK_TOMADOR);

            avaliacaoTomadorPJDTO.setCallbackUrl(urlCallBack);

            PropostaTomadorRespostaDTO resposta = this.apoioNegocioSiricServico.solicitarAvaliacaoTomadorPJ(avaliacaoTomadorPJDTO);

            if (resposta != null) {
                return Response.ok().entity(resposta).build();
            } else {
                return Response.noContent().build();
            }

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aTPJ"));
        } catch (SiricException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aTPJ"));
        }
    }
    
    @POST
    @Path("/avaliacao-operacao-pj/simples/solicitacao")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Avaliação recebida com sucesso."),
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Solicitar avaliação da operação simples pessoa jurídica.")
    public Response avalicaoOperacaoSimplesPJ(AvaliacaoOperacaoSimplesPJDTO avaliacaoOperacaoPJDTO, 
             @Context HttpHeaders headers, @Context UriInfo uriInfo, @Context HttpServletRequest request) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {

            String url = request.getRequestURL().toString();
            String urlCallBack = url.replace("/avaliacao-operacao-pj/simples/solicitacao", URL_CALLBACK_OPERACAO);
            
            avaliacaoOperacaoPJDTO.setCallbackUrl(urlCallBack);

            PropostaOperacaoRespostaDTO resposta = this.apoioNegocioSiricServico.solicitarAvaliacaoOperacaoSimplesPJ(avaliacaoOperacaoPJDTO);

            if (resposta != null) {
                return Response.ok().entity(resposta).build();
            } else {
                return Response.noContent().build();
            }

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aOSPJ"));
        } catch (SiricException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aOSPJ"));
        }
    }
    
    // aguarda a proxima sprint
    /*
    @POST
    @Path("/avaliacao-operacao-microcredito-pj/")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Avaliação recebida com sucesso."),
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Solicitar avaliação da operação micro-crédito pessoa jurídica.")
    public Response avalicaoOperacaoMicroCreditoPJ(AvaliacaoOperacaoPJDTO avaliacaoOperacaoPJDTO,
             @Context HttpHeaders headers, @Context UriInfo uriInfo, @Context HttpServletRequest request) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {

            String url = request.getRequestURL().toString();
            String urlCallBack = url.replace("/avaliacao-operacao-microcredito-pj/", URL_CALLBACK_OPERACAO);
            
            avaliacaoOperacaoPJDTO.setCallbackUrl(urlCallBack);
            
            PropostaOperacaoRespostaDTO resposta = this.apoioNegocioSiricServico.solicitarAvaliacaoOperacaoPJ(avaliacaoOperacaoPJDTO);

            if (resposta != null) {
                return Response.ok().entity(resposta).build();
            } else {
                return Response.noContent().build();
            }

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aOMCPJ"));
        } catch (SiricException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aOMCPJ"));
        }
    }
    */

    @POST
    @Path(URL_CALLBACK_TOMADOR)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Resposta callback recebida com sucesso."),
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Solicitar processamento da resposta callback do tomador pessoa jurídica.")
    public Response processarRespostaCallback(PropostaTomadorRespostaDTO propostaRespostaCallback, @Context HttpHeaders headers, @Context UriInfo uriInfo) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {

            this.apoioNegocioSiricServico.processarPropostaCallback(propostaRespostaCallback);
            return Response.ok().build();

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("pRC"));
        } catch (SiricException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("pRC"));
        }
    }
    
    @POST
    @Path(URL_CALLBACK_OPERACAO)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Resposta callback recebida com sucesso."),
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Solicitar processamento da resposta callback do operacao pessoa jurídica.")
    public Response processarRespostaCallbackOperacao(PropostaOperacaoRespostaDTO propostaRespostaCallbackOperacao, @Context HttpHeaders headers, @Context UriInfo uriInfo) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {

            this.apoioNegocioSiricServico.processarPropostaCallbackOperacao(propostaRespostaCallbackOperacao);
            return Response.ok().build();

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("pRCO"));
        } catch (SiricException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("pRCO"));
        }
    }

    @GET
    @Path("/avaliacao-tomador-pj/resultado/{cnpj}/{codigo-avaliacao}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Resultado da avaliação do tomador localizado com sucesso.", response = br.gov.caixa.pedesgo.arquitetura.siric.dto.propostas.AvaliacaoTomadorRespDTO.class,
                     responseContainer = "List"),
        @ApiResponse(code = 204, message = "Resultado da avaliação do tomador não foi localizado.")
    })
    @ApiOperation(hidden = false, value = "Solicitar Resultado da avaliação do tomador pessoa jurídica.")
    public Response getResultadoAvaliacaoTomadorPJ(@ApiParam(name = "cnpj",
                                                             value = "Identificador do tomador") @PathParam("cnpj") String cnpj, @ApiParam(name = "codigo-avaliacao",
                                                                                                                                                 value = "Código da avaliação do tomador") @PathParam("codigo-avaliacao") String codigoAvaliacao, @Context HttpHeaders headers, @Context UriInfo uriInfo) {

        this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {

            List<AvaliacaoTomadorRespDTO> listaAvaliacoes = this.apoioNegocioSiricServico.consultarResultadoAvalicaoTomador(cnpj, codigoAvaliacao);

            if (listaAvaliacoes != null && !listaAvaliacoes.isEmpty()) {
                return Response.ok(listaAvaliacoes).build();
            } else {
                return Response.noContent().build();
            }
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gRATPJ"));
        } catch (SiricException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gRATPJ"));
        }
    }

    @GET
    @Path("/avaliacao-tomador-pj/resultado/{cnpj}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Resultado da avaliação do tomador localizado com sucesso.", response = br.gov.caixa.pedesgo.arquitetura.siric.dto.propostas.AvaliacaoTomadorRespDTO.class,
                     responseContainer = "List"),
        @ApiResponse(code = 204, message = "Resultado da avaliação do tomador não foi localizado.")
    })
    @ApiOperation(hidden = false, value = "Solicitar Resultado da avaliação do tomador pessoa jurídica.")
    public Response getResultadoAvaliacaoTomadorPJ(@ApiParam(name = "cnpj",
                                                             value = "Identificador do tomador") @PathParam("cnpj") String cnpj, @Context HttpHeaders headers, @Context UriInfo uriInfo) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {

            List<AvaliacaoTomadorRespDTO> listaAvaliacoes = this.apoioNegocioSiricServico.consultarResultadoAvalicaoTomador(cnpj);

            if (listaAvaliacoes != null && !listaAvaliacoes.isEmpty()) {
                return Response.ok(listaAvaliacoes).build();
            } else {
                return Response.noContent().build();
            }
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gRATPJ"));
        } catch (SiricException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gRATPJ"));
        }
    }
    
    @GET
    @Path("/avaliacao-operacao-pj/resultado/{cnpj}/{codigo-avaliacao}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Resultado da avaliação da operação localizado com sucesso.", response = br.gov.caixa.pedesgo.arquitetura.siric.dto.propostas.AvaliacaoOperacaoRespDTO.class,
                     responseContainer = "List"),
        @ApiResponse(code = 204, message = "Resultado da avaliação da operação não foi localizado.")
    })
    @ApiOperation(hidden = false, value = "Solicitar Resultado da avaliação da operação pessoa jurídica.")
    public Response getResultadoAvaliacaoOperacaoPJ(@ApiParam(name = "cnpj",
                                                             value = "Identificador do tomador") @PathParam("cnpj") String cnpj, @ApiParam(name = "codigo-avaliacao",
                                                                                                                                                 value = "Código da avaliação da operacao") @PathParam("codigo-avaliacao") String codigoAvaliacao, @Context HttpHeaders headers, @Context UriInfo uriInfo) {

        this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {

            List<AvaliacaoOperacaoRespDTO> listaAvaliacoes = this.apoioNegocioSiricServico.consultarResultadoAvalicaoOperacao(cnpj, codigoAvaliacao);

            if (listaAvaliacoes != null && !listaAvaliacoes.isEmpty()) {
                return Response.ok(listaAvaliacoes).build();
            } else {
                return Response.noContent().build();
            }
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gRAOPJ"));
        } catch (SiricException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gRAOPJ"));
        }
    }
    
    @GET
    @Path("/avaliacao-operacao-pj/resultado/{cnpj}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Resultado da avaliação da operação localizado com sucesso.", response = br.gov.caixa.pedesgo.arquitetura.siric.dto.propostas.AvaliacaoOperacaoRespDTO.class,
                     responseContainer = "List"),
        @ApiResponse(code = 204, message = "Resultado da avaliação da operação não foi localizado.")
    })
    @ApiOperation(hidden = false, value = "Solicitar Resultado da avaliação da operação pessoa jurídica.")
    public Response getResultadoAvaliacaoOperacaoPJ(@ApiParam(name = "cnpj",
                                                             value = "Identificador do tomador") @PathParam("cnpj") String cnpj, @Context HttpHeaders headers, @Context UriInfo uriInfo) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {

            List<AvaliacaoOperacaoRespDTO> listaAvaliacoes = this.apoioNegocioSiricServico.consultarResultadoAvalicaoOperacao(cnpj);

            if (listaAvaliacoes != null && !listaAvaliacoes.isEmpty()) {
                return Response.ok(listaAvaliacoes).build();
            } else {
                return Response.noContent().build();
            }
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gRAOPJ"));
        } catch (SiricException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gRAOPJ"));
        }
    }
}
