package br.gov.caixa.simtr.visao.retaguarda.rest.v1;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
import br.gov.caixa.simtr.controle.servico.AvaliacaoExtracaoServico;
import br.gov.caixa.simtr.controle.servico.CanalServico;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.outsourcing.ResultadoAvaliacaoExtracaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.outsourcing.RetornoAvaliacaoExtracaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.outsourcing.SolicitacaoAvaliacaoExtracaoDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import io.swagger.jaxrs.PATCH;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@Api(hidden = false, value = "Retaguarda - Outsourcing", tags = {"Retaguarda - Outsourcing"})
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("retaguarda/v1/outsourcing")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class OutsourcingREST extends AbstractREST {

    @EJB
    private AvaliacaoExtracaoServico avaliacaoExtracaoServico;
    
    @EJB
    private CanalServico canalServico;

    private final String PREFIXO = "V1.RET.OR.";
    
    @EJB
    private AnalyticsServico analyticsServico;
    
    private static final Logger LOGGER = Logger.getLogger(OutsourcingREST.class.getName());
    private static final String BASE_URL = "/retaguarda/v1/outsourcing";

    @POST
    @Path("/cpf/{cpf}/documento")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Documento submetido com sucesso para execução dos serviços solicitados.", responseHeaders = {
            @ResponseHeader(name = "location", description = "URL de retorno utilizada para a consulta do documento com a identificação do registro criado.", response = String.class)
        })
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.", response = RetornoErroDTO.class)
        ,
        @ApiResponse(code = 404, message = "Dossiê do cliente não localizado pelo CPF informado.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Submete documento para o serviço de classificação, extração de dados e avaliação de autenticidade vinculando o mesmo ao dossiê de cliente identificado pelo CPF informado.")
    public Response submeteDocumentoAvaliacaoExtracaoVinculoCPF(
                @HeaderParam("processo") @DefaultValue("PADRAO") String processo, 
                @PathParam("cpf") Long cpf, 
                SolicitacaoAvaliacaoExtracaoDTO solicitacaoAvaliacaoExtracaoDTO, 
    		@Context UriInfo uriInfo,
    		@Context HttpHeaders headers) throws URISyntaxException {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/documento", capturaEngineCliente(headers));
        try {

            Documento documento = this.avaliacaoExtracaoServico.submeteDocumentoAvaliacaoExtracao(cpf, TipoPessoaEnum.F, solicitacaoAvaliacaoExtracaoDTO, processo.toUpperCase());

            return Response.created(new URI(uriInfo.getPath() + "/" + documento.getId())).build();
        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, this.PREFIXO.concat("sDAE"));
        }
    }
    
    @POST
    @Path("/cnpj/{cnpj}/documento")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Documento submetido com sucesso para execução dos serviços solicitados.", responseHeaders = {
            @ResponseHeader(name = "location", description = "URL de retorno utilizada para a consulta do documento com a identificação do registro criado.", response = String.class)
        })
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.", response = RetornoErroDTO.class)
        ,
        @ApiResponse(code = 404, message = "Dossiê do cliente não localizado pelo CNPJ informado.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Submete documento para o serviço de classificação, extração de dados e avaliação de autenticidade vinculando o mesmo ao dossiê de cliente identificado pelo CNPJ informado.")
    public Response submeteDocumentoAvaliacaoExtracaoVinculoCNPJ(
                @HeaderParam("processo") @DefaultValue("PADRAO") String processo, 
                @PathParam("cnpj") Long cnpj, 
                SolicitacaoAvaliacaoExtracaoDTO solicitacaoAvaliacaoExtracaoDTO, 
    		@Context UriInfo uriInfo,
    		@Context HttpHeaders headers) throws URISyntaxException {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/documento", capturaEngineCliente(headers));
        try {

            Documento documento = this.avaliacaoExtracaoServico.submeteDocumentoAvaliacaoExtracao(cnpj, TipoPessoaEnum.J, solicitacaoAvaliacaoExtracaoDTO, processo.toUpperCase());

            return Response.created(new URI(uriInfo.getPath() + "/" + documento.getId())).build();
        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, this.PREFIXO.concat("sDAE"));
        }
    }
    
    @POST
    @Path("/documento")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Documento submetido com sucesso para execução dos serviços solicitados.", responseHeaders = {
            @ResponseHeader(name = "location", description = "URL de retorno utilizada para a consulta do documento com a identificação do registro criado.", response = String.class)
        })
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Submete documento para o serviço de classificação, extração de dados e avaliação de autenticidade.")
    public Response submeteDocumentoAvaliacaoExtracao(
                @HeaderParam("processo") @DefaultValue("PADRAO") String processo, 
                SolicitacaoAvaliacaoExtracaoDTO solicitacaoAvaliacaoExtracaoDTO, 
    		@Context UriInfo uriInfo,
    		@Context HttpHeaders headers) throws URISyntaxException {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/documento", capturaEngineCliente(headers));
        try {

            Documento documento = this.avaliacaoExtracaoServico.submeteDocumentoAvaliacaoExtracao(null, null, solicitacaoAvaliacaoExtracaoDTO, processo.toUpperCase());

            return Response.created(new URI(uriInfo.getPath() + "/" + documento.getId())).build();
        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, this.PREFIXO.concat("sDAE"));
        }
    }
    
    @POST
    @Path("/documento/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Documento submetido com sucesso para execução dos serviços solicitados.")
        ,
        @ApiResponse(code = 400, message = "Documento já encontra-se em processamento externo.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Submete o documento indicado para o serviço de extração de dados e avaliação de autenticidade.")
    public Response submeteDocumentoAvaliacaoExtracao(
                @HeaderParam("processo") @DefaultValue("PADRAO") String processo,
                @ApiParam(value = "Código de identificação do documento utilizado no envio e controle de fluxo do registro")
                @PathParam("id") Long id, 
                @Context UriInfo uriInfo,
                @Context HttpHeaders headers) throws URISyntaxException {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/documento/{id}", capturaEngineCliente(headers));
        try {

            this.avaliacaoExtracaoServico.submeteDocumentoAvaliacaoExtracao(id, processo.toUpperCase());

            return Response.ok().build();
        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, this.PREFIXO.concat("sDAE"));
        }
    }

    @GET
    @Path("/documento/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Retorna o documento contendo o resultado da extração de dados e avaliação de autenticidade documental.", response = RetornoAvaliacaoExtracaoDTO.class)
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.", response = RetornoErroDTO.class)
        ,
        @ApiResponse(code = 409, message = "Falha ao persistir o dossiê de produto por inconsistência de parâmetros.")
    })
    @ApiOperation(hidden = false, value = "Captura o resultado referente ao serviço de avaliação de autenticidade e extração de dados.")
    public Response capturaResultadoAvaliacaoExtracao(@PathParam("id") Long id, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/documento/{id}", capturaEngineCliente(headers));
        try {
            Documento documento = this.avaliacaoExtracaoServico.capturaResultadoAvaliacaoExtracao(id);

            RetornoAvaliacaoExtracaoDTO retornoAvaliacaoExtracaoDTO = new RetornoAvaliacaoExtracaoDTO(documento);

            return Response.ok(retornoAvaliacaoExtracaoDTO).build();
        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, this.PREFIXO.concat("cRAE"));
        }
    }
    
    @PATCH
    @Path("/resultado/{codigo-controle}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Documento atualizado com sucesso com os resultados informados.")
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Serviço utilizado pelo fornecedor externo para atualizar as informações do documento referente as atividades extração de dados, avaliação de autenticidade e tratamentodo do documento.")
    public Response resultadoAvaliacaoExtracao(
            @ApiParam(value = "Código de controle associado ao documento utilizado na sua identificado e correta atualização do controle de fluxo do registro")
            @PathParam("codigo-controle") String codigoControle,
            ResultadoAvaliacaoExtracaoDTO resultadoAvaliacaoExtracaoDTO, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.PATCH.name(), BASE_URL + "/resultado/{codigo-controle}", capturaEngineCliente(headers));
        try {

            Documento documento = this.avaliacaoExtracaoServico.atualizaInformacaoDocumentoOutsourcing(codigoControle, resultadoAvaliacaoExtracaoDTO.prototype());
            try{
                this.canalServico.notificaCanalSubmissor(documento);
            }catch(Exception ee){
                String mensagem = MessageFormat.format("OR.rAE.001 - Falha ao comunicar retorno de callback ao canal submissor. Causa: {0} | Documento: {1} | Canal: {2}", ee.getLocalizedMessage(), documento.getId(), documento.getCanalCaptura().getSigla());
                LOGGER.log(Level.ALL, mensagem, ee);
                LOGGER.log(Level.WARNING, mensagem);
            }

            return Response.noContent().build();
        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, this.PREFIXO.concat("rAE"));
        }
    }
}
