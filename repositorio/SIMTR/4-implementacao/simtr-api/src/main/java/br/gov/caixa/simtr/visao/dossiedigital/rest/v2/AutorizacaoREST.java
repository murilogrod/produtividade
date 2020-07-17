package br.gov.caixa.simtr.visao.dossiedigital.rest.v2;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
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
import javax.ws.rs.core.Response.Status;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.simtr.controle.excecao.DossieAutorizacaoException;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.AutorizacaoNegadaServico;
import br.gov.caixa.simtr.controle.servico.AutorizacaoServico;
import br.gov.caixa.simtr.controle.servico.DossieDigitalServico;
import br.gov.caixa.simtr.controle.vo.autorizacao.AutorizacaoVO;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao.AnaliseRegraDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao.DocumentoUtilizadoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao.MensagemOrientacaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao.RetornoAutorizacaoConjuntaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao.RetornoAutorizacaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v2.dossiedigital.autorizacao.DocumentoConclusaoDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(hidden = false)
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("dossie-digital/v2/autorizacao")
@RequestScoped
@Consumes({
    MediaType.APPLICATION_JSON,
    MediaType.APPLICATION_XML
})
@Produces({
    MediaType.APPLICATION_JSON,
    MediaType.APPLICATION_XML
})
public class AutorizacaoREST extends AbstractREST {

    @EJB
    private AutorizacaoServico autorizacaoServico;

    @EJB
    private AutorizacaoNegadaServico autorizacaoNegadaServico;
    
    @EJB
    private DossieDigitalServico dossieDigitalServico;

    private final String PREFIXO = "DOS.AR.V2.";

    @EJB
    private AnalyticsServico analyticsServico;

    private static final String BASE_URL = "/dossie-digital/v2/autorizacao";

    @POST
    @Path("/dossie-cliente/{id}/operacao/{operacao}/modalidade/{modalidade}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Solicitação processada com sucesso. Necessario avaliar se a autorização foi concedida.", response = RetornoAutorizacaoDTO.class),
        @ApiResponse(code = 400, message = "Falha na requisição encaminhada. Verificar ", response = RetornoAutorizacaoDTO.class),
        @ApiResponse(code = 500, message = "Erro de configuração em algum recurso interno do serviço.", response = RetornoAutorizacaoDTO.class),
        @ApiResponse(code = 503, message = "Erro de comunicação com algum sistema de integração.", response = RetornoAutorizacaoDTO.class)
    })
    @ApiOperation(hidden = false, 
                  value = "Solicitação de Autorização Documental",
                  tags = {
                      "Dossiê Digital - Autorização"
                  })
    public Response solicitaAutorizacao(
            @ApiParam(name = "id", value = "Identificador do cliente a ser analisado o pedido de autorização", example = "10") 
            @PathParam("id") Long id, 
            @ApiParam(name = "operacao", value = "Código de operação do produto relacionado com o pedido de autorização", example = "13") 
            @PathParam("operacao") Integer codigoOperacao, 
            @ApiParam(name = "modalidade", value = "Código da modalidade do produto relacionado com o pedido de autorização", example = "0") 
            @PathParam("modalidade") Integer codigoModalidade, 
            @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/dossie-cliente/{id}/operacao/{operacao}/modalidade/{modalidade}", capturaEngineCliente(headers));
        try {
            AutorizacaoVO autorizacaoVO = this.autorizacaoServico.getAutorizacao(id, codigoOperacao, codigoModalidade);

            RetornoAutorizacaoDTO autorizacaoRetornoDTO = new RetornoAutorizacaoDTO(autorizacaoVO.getAutorizacao(), autorizacaoVO.getDocumentosUtilizados(), autorizacaoVO.getMensagensOrientacoes(), autorizacaoVO.getResultadoPesquisaSIPES());

            return Response.ok(autorizacaoRetornoDTO).build();

        } catch (EJBException ee) {
            if (DossieAutorizacaoException.class.equals(ee.getCause().getClass())) {
                return this.montaRespostaAutorizacaoNegada((DossieAutorizacaoException) ee.getCause());
            } else {
                return this.montaRespostaExcecao(ee, this.PREFIXO.concat("sA"));
            }
        }
    }

    @GET
    @Path("/dossie-cliente/{id}/operacao/{operacao}/modalidade/{modalidade}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Solicitação processada com sucesso.", response = AnaliseRegraDTO.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Falha na requisição encaminhada."),
        @ApiResponse(code = 500, message = "Erro de configuração em algum recurso interno do serviço."),
        @ApiResponse(code = 503, message = "Erro de comunicação com algum sistema de integração.")
    })
    @ApiOperation(hidden = false,
                  value = "Verifica a disponibilidade de autorização para um produto indicando a lista de documentos que estão presentes/ausentes no dossiê do cliente de acordo com as possiveis coleções definidas.",
                  tags = {
                      "Dossiê Digital - Autorização"
                  })
    public Response simulaAutorizacao(
            @ApiParam(name = "id", value = "Identificador do cliente a ser analisado o pedido de autorização", example = "10") 
            @PathParam("id") Long id, 
            @ApiParam(name = "operacao", value = "Código de operação do produto relacionado com o pedido de autorização", example = "13") 
            @PathParam("operacao") Integer codigoOperacao, 
            @ApiParam(name = "modalidade", value = "Código da modalidade do produto relacionado com o pedido de autorização", example = "0") 
            @PathParam("modalidade") Integer codigoModalidade, 
            @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/dossie-cliente/{id}/operacao/{operacao}/modalidade/{modalidade}", capturaEngineCliente(headers));
        try {
            List<AnaliseRegraDTO> listaDocumentosAnalisados = this.autorizacaoServico.verificaDisponibilidadeAutorizacao(id, codigoOperacao, codigoModalidade);

            return Response.ok(listaDocumentosAnalisados).build();

        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, PREFIXO.concat("sA"));
        }
    }

    @POST
    @Path("/conjunta/operacao/{operacao}/modalidade/{modalidade}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Solicitação processada com sucesso.", response = RetornoAutorizacaoConjuntaDTO.class),
        @ApiResponse(code = 400, message = "Erro na requisição"),
        @ApiResponse(code = 503, message = "Erro de comunicação com algum sistema")
    })
    @ApiOperation(hidden = false, 
                  value = "Solicitação de Autorização Conjunta.",
                  tags = {
                      "Dossiê Digital - Autorização"
                  })
    public Response getAutorizacaoConjunta(
            @ApiParam(name = "operacao", value = "Código de operação do produto relacionado com o pedido de autorização", example = "13") 
            @PathParam("operacao") Integer codigoOperacao, 
            @ApiParam(name = "modalidade", value = "Código da modalidade do produto relacionado com o pedido de autorização", example = "0") 
            @PathParam("modalidade") Integer codigoModalidade, 
            @Context HttpHeaders headers,
            @ApiParam(name = "body", value = "Lista de autorizações individuais obtidas para o produto para cada cliente a ser envolvido na operação conjunta")
            List<Long> autorizacoesIndividuais) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/conjunta/operacao/{operacao}/modalidade/{modalidade}", capturaEngineCliente(headers));

        try {
            RetornoAutorizacaoConjuntaDTO retornoDTO = this.autorizacaoServico.setAutorizacaoConjunta(autorizacoesIndividuais, codigoOperacao, codigoModalidade);

            return Response.ok(retornoDTO).build();

        } catch (EJBException ee) {
            if (DossieAutorizacaoException.class.equals(ee.getCause().getClass())) {
                return this.montaRespostaAutorizacaoNegada((DossieAutorizacaoException) ee.getCause());
            } else {
                return this.montaRespostaExcecao(ee, this.PREFIXO.concat("sA"));
            }
        }
    }

    @POST
    @Path("autorizacao/{autorizacao}/conclusao")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Operação concluida e documentos armazenados com sucesso"),
        @ApiResponse(code = 400, message = "Erro na requisição"),
        @ApiResponse(code = 503, message = "Erro de comunicação com algum sistema")
    })
    @ApiOperation(hidden = false, 
                  value = "Solicitação conclusão da operação enviando os documentos da operação para guarda.", 
                  tags = {
                      "Dossiê Digital - Autorização"
                  })
    public Response conclusaoOperacao(
            @ApiParam(name = "autorizacao", value = "Código da autorização concedida vinculada a operação em conclusão")
            @PathParam("autorizacao") final Long codigoAutorizacao, 
            @ApiParam(name = "body", value = "Lista de documentos encaminhadas para guarda após a finalização da operação relacionada com a autorização")
            final List<DocumentoConclusaoDTO> documentos, 
            @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "autorizacao/{autorizacao}/conclusao", capturaEngineCliente(headers), montarValores("codigoAutorizacao", String.valueOf(codigoAutorizacao)));
        try {

            this.dossieDigitalServico.guardaDocumentoOperacaoDossieDigital(codigoAutorizacao, documentos);

            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("cO"));
        }
    }

    private Response montaRespostaAutorizacaoNegada(DossieAutorizacaoException dae) {

        // Monta o objeto de retorno da solicitação de autorização com base nas informações da exceção
        RetornoAutorizacaoDTO autorizacaoRetornoDTO = new RetornoAutorizacaoDTO();
        autorizacaoRetornoDTO.setAutorizado(Boolean.FALSE);
        autorizacaoRetornoDTO.setIndicadorProsseguimento(dae.getIndicadorProsseguimento());
        if (TipoPessoaEnum.F.equals(dae.getTipoPessoaEnum())) {
            autorizacaoRetornoDTO.setCpfCliente(dae.getCpfCnpj());
        } else if (TipoPessoaEnum.J.equals(dae.getTipoPessoaEnum())) {
            autorizacaoRetornoDTO.setCnpjCliente(dae.getCpfCnpj());
        }

        autorizacaoRetornoDTO.setOperacao(dae.getOperacaoSolicitada());
        autorizacaoRetornoDTO.setModalidade(dae.getModalidadeSolicitada());
        if (dae.getProduto() != null) {
            autorizacaoRetornoDTO.setProdutoLocalizado(Boolean.TRUE);
            autorizacaoRetornoDTO.setNomeProduto(dae.getProduto().getNome());
            autorizacaoRetornoDTO.setProdutoLocalizado(Boolean.TRUE);
        } else {
            autorizacaoRetornoDTO.setProdutoLocalizado(Boolean.FALSE);
        }
        if (dae.getDocumentosAusentes() != null) {
            autorizacaoRetornoDTO.addDocumentosAusentes(dae.getDocumentosAusentes().toArray(new String[dae.getDocumentosAusentes().size()]));
        }
        if (dae.getDocumentosUtilizados() != null) {
            dae.getDocumentosUtilizados().forEach(documentoUtilizado -> autorizacaoRetornoDTO.addDocumentosUtilizados(new DocumentoUtilizadoDTO(documentoUtilizado)));
        }

        autorizacaoRetornoDTO.setObservacao(dae.getLocalizedMessage());
        dae.getMensagensOrientacao().forEach(orientacao -> autorizacaoRetornoDTO.addMensagensOrientacoes(new MensagemOrientacaoDTO(orientacao[0], orientacao[1], orientacao[2])));
        autorizacaoRetornoDTO.setResultadoPesquisa(dae.getResultadoPesquisaSIPES());

        // Registra a negativa de autorização na base de dados
        this.autorizacaoNegadaServico.createAutorizacaoNegada(dae.getCpfCnpj(), dae.getTipoPessoaEnum(), dae.getOperacaoSolicitada(), dae.getModalidadeSolicitada(), dae.getSistemaSolicitante(), dae.getMessage());

        autorizacaoRetornoDTO.setObservacao(dae.getLocalizedMessage());

        return Response.status(Status.OK).entity(autorizacaoRetornoDTO).build();
    }
}
