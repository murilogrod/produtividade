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
import javax.ws.rs.core.UriInfo;

import com.sun.jersey.core.header.ContentDisposition;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.ProcessoAdministrativoServico;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.ProcessoAdministrativo;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.pae.documento.DocumentoAdministrativoCadastroDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.pae.processo.ProcessoAdministrativoNovoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.pae.processo.ProcessoAdministrativoManutencaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.pae.processo.ProcessoAdministrativoDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.jaxrs.PATCH;

@Api(hidden = false, value = "Processo Administrativo - Processo")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("/processoadministrativo/v1")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ProcessoAdministrativoREST extends AbstractREST {

    @EJB
    ProcessoAdministrativoServico processoAdministrativoServico;

    private final String PREFIXO = "PAE.PAR.";
    
    @EJB
	private AnalyticsServico analyticsServico;
    
    private static final String BASE_URL = "/processoadministrativo/v1";

    @POST
    @Path("/processo")
    @ApiResponses({
        @ApiResponse(code = 201, message = "O Processo encaminhado foi persistido com sucesso.")
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo do processo.")
        ,
        @ApiResponse(code = 409, message = "Falha ao persistir o processo por inconsistência de parâmetros.")
    })
    @ApiOperation(hidden = false, value = "Inclusão de Processo Administrativo")
    public Response incluirProcesso(ProcessoAdministrativoNovoDTO processoDTO, @Context UriInfo uriInfo,
    		@Context HttpHeaders headers) throws URISyntaxException {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/processo", capturaEngineCliente(headers));
        try {
            ProcessoAdministrativo processo = processoDTO.prototype();
            this.processoAdministrativoServico.save(processo);
            return Response.created(new URI(uriInfo.getPath() + "/" + processo.getId())).entity(new ProcessoAdministrativoDTO(processo)).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("iP"));
        }

    }

    @PATCH
    @Path("/processo/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "As alterações do processo encaminhadas foram atualizadas com sucesso.")
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo do processo.")
        ,
        @ApiResponse(code = 404, message = "Documento não localizado sob o identificador informado.")
    })
    @ApiOperation(hidden = false, value = "Atualização de informações de um Processo Administrativo")
    public Response atualizarProcesso(
            @ApiParam(name = "id", value = "Identificador do processo a ser alterado", required = true)
            @PathParam("id") Long id, ProcessoAdministrativoManutencaoDTO patchDTO,
            @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/processo/{id}", capturaEngineCliente(headers));
        try {
            this.processoAdministrativoServico.aplicaPatch(id, patchDTO.prototype());
            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aP"));
        }
    }

    @GET
    @Path("/processo/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Processo Administrativo localizado com sucesso.", response = ProcessoAdministrativoDTO.class)
        ,
        @ApiResponse(code = 204, message = "Processo Administrativo não localizado pelo identificador informado.")})
    @ApiOperation(hidden = false, value = "Consulta de Processo Administrativo pelo ID")
    public Response getProcessoById(
            @ApiParam(name = "id", value = "Identificador do processo a ser consultado", required = true)
            @PathParam("id") Long id,
            @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/processo/id", capturaEngineCliente(headers));
        try {
            ProcessoAdministrativo processo = this.processoAdministrativoServico.getById(id, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
            if (processo == null) {
                return Response.noContent().build();
            }
            ProcessoAdministrativoDTO processoDTO = new ProcessoAdministrativoDTO(processo);
            return Response.ok(processoDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gPBI"));
        }
    }

    @GET
    @Path("/processo/numero/{numero}-{ano}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Processo Administrativo localizado com sucesso.", response = ProcessoAdministrativoDTO.class)
        ,
        @ApiResponse(code = 204, message = "Processo Administrativo não localizado pelo numero e ano informados.")})
    @ApiOperation(hidden = false, value = "Consulta de Processo Administrativo pelo Numero/Ano")
    public Response getProcessoByNumeroAno(
            @ApiParam(name = "numero", value = "Numero do processo a ser consultado", required = true)
            @PathParam("numero") Integer numero,
            @ApiParam(name = "ano", value = "Ano de criação do processo a ser consultado", required = true)
            @PathParam("ano") Integer ano, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/processo/numero/{numero}-{ano}", capturaEngineCliente(headers));
        try {
            ProcessoAdministrativo processo = this.processoAdministrativoServico.getByNumeroAno(numero, ano, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
            if (processo == null) {
                return Response.noContent().build();
            }
            ProcessoAdministrativoDTO processoDTO = new ProcessoAdministrativoDTO(processo);
            return Response.ok(processoDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gPBNA"));
        }
    }

    @GET
    @Path("/processo/descricao/{descricao}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Processo Administrativo localizado com sucesso.", response = ProcessoAdministrativoDTO.class, responseContainer = "List")
        ,
        @ApiResponse(code = 204, message = "Processo Administrativo não localizado contendo a descrição informada.")})
    @ApiOperation(hidden = false, value = "Consulta de Processo Administrativo pela descrção do objeto de contatação.")
    public Response listProcessoByDescricao(
            @ApiParam(name = "descricao", value = "Descrição (total ou parcial) do processo a ser consultado", required = true)
            @PathParam("descricao") String descricao, @Context HttpHeaders headers) {
        try {
        	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/processo/descricao/{descricao}", capturaEngineCliente(headers));
            List<ProcessoAdministrativo> processos = this.processoAdministrativoServico.listByDescricao(descricao, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            if (processos == null || processos.isEmpty()) {
                return Response.noContent().build();
            }

            List<ProcessoAdministrativoDTO> processosDTO = new ArrayList<>();
            processos.forEach(processo -> processosDTO.add(new ProcessoAdministrativoDTO(processo)));

            return Response.ok(processosDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("lPBD"));
        }

    }

    @GET
    @Path("/processo/{id}/exportar/{completo}")
    @Produces({"application/zip", MediaType.APPLICATION_JSON})
    @ApiResponses({
        @ApiResponse(code = 200, message = "Processo Administrativo localizado com sucesso.")
        ,
        @ApiResponse(code = 204, message = "Processo Administrativo não localizado pelo identificador informado.")})
    @ApiOperation(hidden = false, value = "Exporta Processo Administrativo pelo ID")
    public Response exportarProcessoDocumentos(
            @ApiParam(name = "id", value = "Identificador do processo a ser exportado", required = true)
            @PathParam("id") Long id,
            @ApiParam(name = "completo", value = "Indica se o processo deve ser exportado incluindo os apensados vinculados ou não", required = true)
            @PathParam("completo") boolean completo, @Context HttpHeaders headers) throws IOException {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/processo/{id}/exportar/{completo}", capturaEngineCliente(headers));
        try {
            String nomeArquivo = "PROCESSO_" + id;

            byte[] relatorio = this.processoAdministrativoServico.criaRelatorioPAE(id, completo, true);

            ContentDisposition contentDisposition = ContentDisposition.type("attachment").fileName(nomeArquivo + ".zip").build();
            return Response.ok(relatorio, "application/zip").header("Access-Control-Expose-Headers", "Content-Disposition").header("Content-Disposition", contentDisposition).build();
        } catch (EJBException ee) {
            Response response = this.montaRespostaExcecao(ee, this.PREFIXO.concat("ePD"));
            ResponseBuilder responseBuilder = Response.fromResponse(response);
            responseBuilder.type(MediaType.APPLICATION_JSON);
            return responseBuilder.build();
        }
    }

    @POST
    @Path("/processo/{id}/documento")
    @ApiResponses({
        @ApiResponse(code = 201, message = "O documento encaminhado foi persistido com sucesso.")
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo do documento.")
    })
    @ApiOperation(hidden = false, value = "Inclusão de Documento Administrativo vinculado a um Processo Administrativo")
    public Response incluirDocumentoProcesso(
            @ApiParam(name = "id", value = "Identificador do processo a ter o documento vinculado", required = true)
            @PathParam("id") Long id, DocumentoAdministrativoCadastroDTO documentoAdmistrativoCadastroDTO, @Context UriInfo uriInfo,
            @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/processo/{id}/documento", capturaEngineCliente(headers));
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
            
            this.processoAdministrativoServico.criaDocumentoProcessoAdministrativo(id, documentoAdmistrativoCadastroDTO.getCodigoTipoDocumento(), documentoAdmistrativoCadastroDTO.getCodigoDocumentoSubstituicao(), documentoAdmistrativoCadastroDTO.getJustificativaSubstituicao(), origemDocumentoEnum, documentoAdmistrativoCadastroDTO.isConfidencial(), documentoAdmistrativoCadastroDTO.isValido(), documentoAdmistrativoCadastroDTO.getDescricaoDocumento(), documentoAdmistrativoCadastroDTO.getMimeType(), atributosDocumento, binario);

            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("iDP"));
        }
    }
}
