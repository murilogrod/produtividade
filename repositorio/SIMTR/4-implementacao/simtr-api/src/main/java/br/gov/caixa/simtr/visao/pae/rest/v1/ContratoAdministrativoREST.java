package br.gov.caixa.simtr.visao.pae.rest.v1;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.sun.jersey.core.header.ContentDisposition;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.ContratoAdministrativoServico;
import br.gov.caixa.simtr.controle.servico.ProcessoAdministrativoServico;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.ContratoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.ProcessoAdministrativo;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.pae.documento.DocumentoAdministrativoCadastroDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.pae.contrato.ContratoAdministrativoNovoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.pae.contrato.ContratoAdministrativoManutencaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.pae.contrato.ContratoAdministrativoDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.jaxrs.PATCH;

@Api(hidden = false, value = "Processo Administrativo - Contrato")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("/processoadministrativo/v1")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ContratoAdministrativoREST extends AbstractREST {

    @EJB
    private ProcessoAdministrativoServico processoAdministrativoServico;

    @EJB
    private ContratoAdministrativoServico contratoAdministrativoServico;

    private final String PREFIXO = "PAE.CAR.";
    
    @EJB
	private AnalyticsServico analyticsServico;
    
    private static final String BASE_URL = "/processoadministrativo/v1";

    @POST
    @Path("/processo/{id}/contrato")
    @ApiResponses({
        @ApiResponse(code = 201, message = "O Contrato encaminhado foi persistido com sucesso.")
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo do processo.")
        ,
        @ApiResponse(code = 409, message = "Falha ao persistir o processo por inconsistência de parâmetros.")
    })
    @ApiOperation(hidden = false, value = "Inclusão de Contrato Administrativo")
    public Response incluirContrato(
            @ApiParam(name = "id", value = "Identificador do processo de vinculação do Contrato", required = true)
            @PathParam("id") Long id,
            ContratoAdministrativoNovoDTO contratoDTO,
            @Context UriInfo uriInfo, @Context HttpHeaders headers) throws URISyntaxException {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/processo/{id}/contrato", capturaEngineCliente(headers));
        try {
            if ((contratoDTO != null) && (id != null)) {
                ProcessoAdministrativo processoAdministrativo = this.processoAdministrativoServico.getById(id, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
                if (processoAdministrativo == null) {
                    RetornoErroDTO resourceError = new RetornoErroDTO(Status.CONFLICT.getStatusCode(), "Parametro Incorreto", "Não foi possivel localizar o Processo Admiistrativo com o ID informado.");
                    return Response.status(Status.CONFLICT).entity(resourceError).build();
                }

                ContratoAdministrativo contratoAdministrativo = contratoDTO.prototype();
                contratoAdministrativo.setProcessoAdministrativo(processoAdministrativo);
                this.contratoAdministrativoServico.save(contratoAdministrativo);
                return Response.created(new URI(uriInfo.getPath() + "/" + contratoAdministrativo.getId())).entity(new ContratoAdministrativoDTO(contratoAdministrativo)).build();
            }

            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("iC"));
        }
    }

    @PATCH
    @Path("/contrato/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "As alterações do contrato encaminhadas foram atualizadas com sucesso.")
        , 
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo do contrato.")
    })
    @ApiOperation(hidden = false, value = "Atualização de informações de um Contrato Administrativo")
    public Response atualizarContrato(
            @ApiParam(name = "id", value = "Identificador do contrato a ser atualizado", required = true)
            @PathParam("id") Long id, ContratoAdministrativoManutencaoDTO patchDTO, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.PATCH.name(), BASE_URL + "/contrato/{id}", capturaEngineCliente(headers));
        try {
            if (patchDTO != null) {
                this.contratoAdministrativoServico.aplicaPatch(id, patchDTO.prototype());
                return Response.noContent().build();
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aC"));
        }
    }

    @GET
    @Path("/contrato/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Contrato Administrativo localizado com sucesso.", response = ContratoAdministrativoDTO.class)
        ,
        @ApiResponse(code = 204, message = "Contrato Administrativo não localizado pelo identificador informado.")})
    @ApiOperation(hidden = false, value = "Consulta de Contrato Administrativo pelo ID")
    public Response getContratoBId(
            @ApiParam(name = "id", value = "Identificador do contrato de a ser consultado", required = true)
            @PathParam("id") Long id, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/contrato/{id}", capturaEngineCliente(headers));
        try {
            ContratoAdministrativo contrato = this.contratoAdministrativoServico.getById(id, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
            if (contrato == null) {
                return Response.noContent().build();
            }
            ContratoAdministrativoDTO contratoDTO = new ContratoAdministrativoDTO(contrato);
            return Response.ok(contratoDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gCBI"));
        }
    }

    @GET
    @Path("/contrato/numero/{numero}-{ano}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Contrato Administrativo localizado com sucesso.", response = ContratoAdministrativoDTO.class)
        ,
        @ApiResponse(code = 204, message = "Contrato Administrativo não localizado pelo numero e ano informados, vinculados ao identificador de processo informado.")})
    @ApiOperation(hidden = false, value = "Consulta de Contrato Administrativo pelo Numero/Ano")
    public Response getContratoByNumeroAno(
            @ApiParam(name = "numero", value = "Numero do contrato a ser consultado", required = true)
            @PathParam("numero") Integer numero,
            @ApiParam(name = "ano", value = "Ano de criação do contrato a ser consultado", required = true)
            @PathParam("ano") Integer ano, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/contrato/numero/{numero}-{ano}", capturaEngineCliente(headers));
        try {
            ContratoAdministrativo contratoAdministrativo = this.contratoAdministrativoServico.getByNumeroAno(numero, ano, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
            if (contratoAdministrativo == null) {
                return Response.noContent().build();
            }

            ContratoAdministrativoDTO contratoDTO = new ContratoAdministrativoDTO(contratoAdministrativo);
            return Response.ok(contratoDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gCBNA"));
        }
    }

    @GET
    @Path("/contrato/descricao/{descricao}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Contrato(s) Administrativo localizado com sucesso.", response = ContratoAdministrativoDTO.class, responseContainer = "List")
        ,
        @ApiResponse(code = 204, message = "Contrato Administrativo não localizado contendo a descrição informada.")})
    @ApiOperation(hidden = false, value = "Consulta de Contrato Administrativo pela descrção do objeto de contatação.")
    public Response listContratoByDescricao(
            @ApiParam(name = "descricao", value = "Descrição (total ou parcial) do contrato a ser consultado", required = true)
            @PathParam("descricao") String descricao, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/contrato/descricao/{descricao}", capturaEngineCliente(headers));
        try {
            List<ContratoAdministrativo> contratos = this.contratoAdministrativoServico.listByDescricao(descricao, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
            if (contratos == null || contratos.isEmpty()) {
                return Response.noContent().build();
            }

            List<ContratoAdministrativoDTO> contratosDTO = new ArrayList<>();
            contratos.forEach(contrato -> contratosDTO.add(new ContratoAdministrativoDTO(contrato)));

            return Response.ok(contratosDTO).build();

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("lCBD"));
        }
    }

    @GET
    @Path("/contrato/{id}/exportar/{completo}")
    @Produces({"application/zip", MediaType.APPLICATION_JSON})
    @ApiResponses({
        @ApiResponse(code = 200, message = "Contrato Administrativo localizado com sucesso.")
        ,
        @ApiResponse(code = 204, message = "Contrato Administrativo não localizado pelo identificador informado.")})
    @ApiOperation(hidden = false, value = "Exporta Contrato Administrativo pelo ID")
    public Response exportarContratoDocumentos(
            @ApiParam(name = "id", value = "Identificador do contrato a ser exportado", required = true)
            @PathParam("id") Long id,
            @ApiParam(name = "completo", value = "indica se o contrato deve ser exportado incluindo os apensados vinculados ou não", required = true)
            @PathParam("completo") boolean completo, @Context HttpHeaders headers) throws IOException {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/contrato/{id}/exportar/{completo}", capturaEngineCliente(headers));
        try {
            String nomeArquivo = "CONTRATO_" + id;
            byte[] relatorio = this.contratoAdministrativoServico.criaRelatorioPAE(id, completo, true);
            ContentDisposition contentDisposition = ContentDisposition.type("attachment").fileName(nomeArquivo + ".zip").build();
            return Response.ok(relatorio, "application/zip").header("Content-Disposition", contentDisposition).build();

        } catch (EJBException ee) {
            Response response = this.montaRespostaExcecao(ee, this.PREFIXO.concat("eCD"));
            ResponseBuilder responseBuilder = Response.fromResponse(response);
            responseBuilder.type(MediaType.APPLICATION_JSON);
            return responseBuilder.build();
        }
    }

    @POST
    @Path("/contrato/{id}/documento")
    @ApiResponses({
        @ApiResponse(code = 201, message = "O documento encaminhado foi persistido com sucesso.")
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo do documento.")
        ,
        @ApiResponse(code = 409, message = "Falha ao persistir o documento por inconsistência de parâmetros.")
    })
    @ApiOperation(hidden = false, value = "Inclusão de Documento Administrativo vinculado a um Contrato Administrativo")
    public Response incluirDocumentoContrato(
            @ApiParam(name = "id", value = "Identificador do contrato a ter o documento vinculado", required = true)
            @PathParam("id") Long id,
            DocumentoAdministrativoCadastroDTO documentoAdmistrativoCadastroDTO,
            @Context UriInfo uriInfo, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/contrato/{id}/documento", capturaEngineCliente(headers));
        try {
            List<AtributoDocumento> atributosDocumento = documentoAdmistrativoCadastroDTO.getAtributosDocumento()
                    .stream().map(atributoDTO -> {
                        AtributoDocumento atributoDocumento = new AtributoDocumento();
                        atributoDocumento.setDescricao(atributoDTO.getChave());
                        atributoDocumento.setConteudo(atributoDTO.getValor());
                        atributoDocumento.setAcertoManual(Boolean.TRUE);
                        return atributoDocumento;
                    }).collect(Collectors.toList());
            OrigemDocumentoEnum origemDocumentoEnum = documentoAdmistrativoCadastroDTO.getOrigemDocumentoEnum() == null ? OrigemDocumentoEnum.S : documentoAdmistrativoCadastroDTO.getOrigemDocumentoEnum();

            String binario = documentoAdmistrativoCadastroDTO.getConteudos().stream().findFirst().orElse(null);
            this.contratoAdministrativoServico.criaDocumentoContratoAdministrativo(id, documentoAdmistrativoCadastroDTO.getCodigoTipoDocumento(), documentoAdmistrativoCadastroDTO.getCodigoDocumentoSubstituicao(), documentoAdmistrativoCadastroDTO.getJustificativaSubstituicao(), origemDocumentoEnum, documentoAdmistrativoCadastroDTO.isConfidencial(), documentoAdmistrativoCadastroDTO.isValido(), documentoAdmistrativoCadastroDTO.getDescricaoDocumento(), documentoAdmistrativoCadastroDTO.getMimeType(), atributosDocumento, binario);

            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("iDC"));
        }
    }

}
