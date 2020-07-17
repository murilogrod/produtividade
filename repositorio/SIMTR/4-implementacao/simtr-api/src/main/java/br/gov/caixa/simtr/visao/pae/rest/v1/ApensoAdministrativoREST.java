package br.gov.caixa.simtr.visao.pae.rest.v1;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import br.gov.caixa.simtr.controle.servico.ApensoAdministrativoServico;
import br.gov.caixa.simtr.controle.servico.ContratoAdministrativoServico;
import br.gov.caixa.simtr.controle.servico.ProcessoAdministrativoServico;
import br.gov.caixa.simtr.modelo.entidade.ApensoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.ContratoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.ProcessoAdministrativo;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.pae.apenso.ApensoAdministrativoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.pae.apenso.ApensoAdministrativoManutencaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.pae.apenso.ApensoAdministrativoNovoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.pae.documento.DocumentoAdministrativoCadastroDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.jaxrs.PATCH;

@Api(hidden = false, value = "Processo Administrativo - Apenso")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("/processoadministrativo/v1")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ApensoAdministrativoREST extends AbstractREST {

    @EJB
    private ProcessoAdministrativoServico processoAdministrativoServico;

    @EJB
    private ContratoAdministrativoServico contratoAdministrativoServico;

    @EJB
    private ApensoAdministrativoServico apensoAdministrativoServico;

    private static final Logger LOGGER = Logger.getLogger(ApensoAdministrativoREST.class.getName());

    private final String PREFIXO = "PAE.AAR.";
    
    @EJB
	private AnalyticsServico analyticsServico;
    
    private static final String BASE_URL = "/processoadministrativo/v1";

    @PATCH
    @Path("/apenso/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "As alterações do apenso encaminhadas foram atualizadas com sucesso.")
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo do apenso.")
        ,
        @ApiResponse(code = 409, message = "Falha ao persistir o apenso por inconsistência de parâmetros.")
    })
    @ApiOperation(hidden = false, value = "Atualização de informações de um Apenso Administrativo")
    public Response atualizarApenso(
            @ApiParam(name = "id", value = "Identificador do apenso a ser atuaizado")
            @PathParam("id") Long id, ApensoAdministrativoManutencaoDTO patchDTO,
            @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.PATCH.name(), BASE_URL + "/apenso/{id}", capturaEngineCliente(headers));
        try {
            if (patchDTO != null) {
                try {
                    this.apensoAdministrativoServico.aplicaPatch(id, patchDTO.prototype());
                } catch (RuntimeException e) {
                    LOGGER.log(Level.ALL, e.getLocalizedMessage(), e);
                    return Response.status(Response.Status.CONFLICT).build();
                }
                return Response.noContent().build();
            }

            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aA"));
        }

    }

    @POST
    @Path("/contrato/{id}/apenso")
    @ApiResponses({
        @ApiResponse(code = 201, message = "O Apenso encaminhado foi persistido com sucesso.")
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo do apenso.")
        ,
        @ApiResponse(code = 409, message = "Falha ao persistir o processo por inconsistência de parâmetros.")
    })
    @ApiOperation(hidden = false, value = "Inclusão de Apenso Administrativo vinculado a um Contrato Administrativo")
    public Response incluirApensoContrato(
            @ApiParam(name = "id", value = "Identificador do contrato de vinculação do Apenso")
            @PathParam("id") Long idContrato,
            ApensoAdministrativoNovoDTO apensoDTO,
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers) throws URISyntaxException {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.PATCH.name(), BASE_URL + "/apenso/{id}", capturaEngineCliente(headers));
        try {
            if ((apensoDTO != null) && (idContrato != null)) {
                ContratoAdministrativo contratoAdministrativo = this.contratoAdministrativoServico.getById(idContrato, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
                if (contratoAdministrativo == null) {
                    RetornoErroDTO resourceError = new RetornoErroDTO(Status.NOT_FOUND.getStatusCode(), "Parametro Incorreto", "Não foi possivel localizar o Contrato Administrativo com o ID informado.");
                    return Response.status(Status.NOT_FOUND).entity(resourceError).build();
                }

                ApensoAdministrativo apensoAdministrativo = apensoDTO.prototype();
                apensoAdministrativo.setContratoAdministrativo(contratoAdministrativo);
                try {
                    this.apensoAdministrativoServico.save(apensoAdministrativo);
                } catch (RuntimeException e) {
                    LOGGER.log(Level.ALL, e.getLocalizedMessage(), e);
                    return Response.status(Response.Status.CONFLICT).build();
                }
                return Response.created(new URI(uriInfo.getPath() + "/" + apensoAdministrativo.getId())).entity(new ApensoAdministrativoDTO(apensoAdministrativo)).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("iAC"));
        }

    }

    @GET
    @Path("/apenso/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Apenso Administrativo localizado com sucesso.", response = ApensoAdministrativoDTO.class)
        ,
        @ApiResponse(code = 204, message = "Apenso Administrativo não localizado pelo identificador informado.")})
    @ApiOperation(hidden = false, value = "Consulta de Apenso Administrativo pelo ID")
    public Response getApensoById(
            @ApiParam(name = "id", value = "Identificador do apenso a ser consultado")
            @PathParam("id") Long id, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/apenso/{id}", capturaEngineCliente(headers));
        try {
            ApensoAdministrativo apenso = this.apensoAdministrativoServico.getById(id, Boolean.TRUE, Boolean.TRUE);
            if (apenso == null) {
                return Response.noContent().build();
            }
            ApensoAdministrativoDTO apensoDTO = new ApensoAdministrativoDTO(apenso);
            return Response.ok(apensoDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gABI"));
        }
    }

    @GET
    @Path("/apenso/{id}/exportar")
    @Produces({"application/zip", MediaType.APPLICATION_JSON})
    @ApiResponses({
        @ApiResponse(code = 200, message = "Apenso Administrativo localizado com sucesso.")
        ,
        @ApiResponse(code = 204, message = "Apenso Administrativo não localizado pelo identificador informado.")})
    @ApiOperation(hidden = false, value = "Exporta Apenso Administrativo pelo ID")
    public Response exportarApensoDocumentos(
            @ApiParam(name = "id", value = "Identificador do apenso a ser exportado")
            @PathParam("id") Long id, @Context HttpHeaders headers) throws IOException {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/apenso/{id}/exportar", capturaEngineCliente(headers));
        try {
            String nomeArquivo = "APENSO_" + id;
            byte[] relatorio = this.apensoAdministrativoServico.criaRelatorioPAE(id, true, true);

            ContentDisposition contentDisposition = ContentDisposition.type("attachment").fileName(nomeArquivo + ".zip").build();
            return Response.ok(relatorio, "application/zip").header("Content-Disposition", contentDisposition).build();
        } catch (EJBException ee) {
            Response response = this.montaRespostaExcecao(ee, this.PREFIXO.concat("eAD"));
            ResponseBuilder responseBuilder = Response.fromResponse(response);
            responseBuilder.type(MediaType.APPLICATION_JSON);
            return responseBuilder.build();
        }
    }

    @GET
    @Path("/apenso/protocolosiclg/{protocolo}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Apenso Administrativo localizado com sucesso.", response = ApensoAdministrativoDTO.class)
        ,
        @ApiResponse(code = 204, message = "Apenso Administrativo não localizado pelo protocolo informado.")})
    @ApiOperation(hidden = false, value = "Consulta de Apenso Administrativo pelo Protocolo SICLG")
    public Response getByProtocoloSICLG(
            @ApiParam(name = "protocolo", value = "Numero do protocolo do SICLG vinculado ao apenso")
            @PathParam("protocolo") String protocolo, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/apenso/protocolosiclg/{protocolo}", capturaEngineCliente(headers));
        try {
            ApensoAdministrativo apenso = this.apensoAdministrativoServico.getByProtocoloSICLG(protocolo, Boolean.TRUE, Boolean.TRUE);
            if (apenso == null) {
                return Response.noContent().build();
            }
            ApensoAdministrativoDTO apensoDTO = new ApensoAdministrativoDTO(apenso);
            return Response.ok(apensoDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gBPS"));
        }

    }

    @POST
    @Path("/apenso/{id}/documento")
    @ApiResponses({
        @ApiResponse(code = 201, message = "O documento encaminhado foi persistido com sucesso.")
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo do documento.")
        ,
        @ApiResponse(code = 409, message = "Falha ao persistir o documento por inconsistência de parâmetros.")
    })
    @ApiOperation(hidden = false, value = "Inclusão de Documento Administrativo vinculado a um Apenso Administrativo")
    public Response incluirDocumentoApenso(
            @ApiParam(name = "id", value = "Identificador do apenso de vinculação do documento")
            @PathParam("id") Long id,
            DocumentoAdministrativoCadastroDTO documentoAdmistrativoCadastroDTO,
            @Context UriInfo uriInfo, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/apenso/{id}/documento", capturaEngineCliente(headers));
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
            this.apensoAdministrativoServico.criaDocumentoApensoAdministrativo(id, documentoAdmistrativoCadastroDTO.getCodigoTipoDocumento(), documentoAdmistrativoCadastroDTO.getCodigoDocumentoSubstituicao(), documentoAdmistrativoCadastroDTO.getJustificativaSubstituicao(), origemDocumentoEnum, documentoAdmistrativoCadastroDTO.isConfidencial(), documentoAdmistrativoCadastroDTO.isValido(), documentoAdmistrativoCadastroDTO.getDescricaoDocumento(), documentoAdmistrativoCadastroDTO.getMimeType(), atributosDocumento, binario);

            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("iDA"));
        }
    }

    @POST
    @Path("/processo/{id}/apenso")
    @ApiResponses({
        @ApiResponse(code = 201, message = "O Apenso encaminhado foi persistido com sucesso.")
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo do apenso.")
        ,
        @ApiResponse(code = 409, message = "Falha ao persistir o processo por inconsistência de parâmetros.")
    })
    @ApiOperation(hidden = false, value = "Inclusão de Apenso Administrativo vinculado a um Processo Administrativo")
    public Response incluirApensoProcesso(
            @ApiParam(name = "id", value = "Identificador do processo de vinculação do Apenso")
            @PathParam("id") Long idProcesso, ApensoAdministrativoNovoDTO apensoDTO, @Context UriInfo uriInfo,
            @Context HttpHeaders headers) throws URISyntaxException {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/processo/{id}/apenso", capturaEngineCliente(headers));
        try {
            ProcessoAdministrativo processoAdministrativo = this.processoAdministrativoServico.getById(idProcesso, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            if (processoAdministrativo == null) {
                RetornoErroDTO resourceError = new RetornoErroDTO(Status.CONFLICT.getStatusCode(), "Parametro Incorreto", "Não foi possivel localizar o Processo Administrativo com o ID informado.");
                return Response.status(Status.CONFLICT).entity(resourceError).build();
            }

            ApensoAdministrativo apensoAdministrativo = apensoDTO.prototype();
            apensoAdministrativo.setProcessoAdministrativo(processoAdministrativo);
            this.apensoAdministrativoServico.save(apensoAdministrativo);

            return Response.created(new URI(uriInfo.getPath() + "/" + apensoAdministrativo.getId())).entity(new ApensoAdministrativoDTO(apensoAdministrativo)).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("iAP"));
        }
    }
}
