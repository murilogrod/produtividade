package br.gov.caixa.simtr.visao.cadastro.rest.v1;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.TipoDocumentoServico;
import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.OpcaoAtributo;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento.AtributoExtracaoManutencaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento.AtributoExtracaoNovoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento.OpcaoAtributoNovoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento.TipoDocumentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento.TipoDocumentoDadosDeclaradosDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento.TipoDocumentoManutencaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento.TipoDocumentoNovoDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import io.swagger.jaxrs.PATCH;

@Api(hidden = false, value = "Cadastro - Tipo Documento")
@ApiResponses({
    @ApiResponse(code = 400, message = "Falha ao processar a requisição. Requisição mal formada sob apontamentos indicados.")
    ,
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("/cadastro/v1/tipo-documento")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class TipoDocumentoREST extends AbstractREST {

    @EJB
    TipoDocumentoServico tipoDocumentoServico;
    
    @EJB
    private AnalyticsServico analyticsServico;
    
    private static final String BASE_URL = "/cadastro/v1/tipo-documento";

    private final String PREFIXO = "CAD.TDR.";

    @GET
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Lista de tipos de documentos disponíveis carregada com sucesso.", response = TipoDocumentoDTO.class, responseContainer = "List")
        ,
        @ApiResponse(code = 204, message = "Não existem tipos de documentos disponíveis.")})
    @ApiOperation(hidden = false, value = "Consulta lista de tipos de documentos disponíveis")
    public Response listTiposDocumento(@Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            List<TipoDocumento> listaTiposDocumento = this.tipoDocumentoServico.listInativosIncluidos();
            if (Objects.nonNull(listaTiposDocumento) && !listaTiposDocumento.isEmpty()) {
                List<TipoDocumentoDTO> listaTiposDocumentoDTO = listaTiposDocumento.stream()
                        .map(tipoDocumento -> new TipoDocumentoDTO(tipoDocumento.getId(), tipoDocumento.getNome(), 
                                                                   tipoDocumento.getTipoPessoaEnum(), tipoDocumento.getCodigoTipologia(), 
                                                                   tipoDocumento.getUsoApoioNegocio(), tipoDocumento.getUsoDossieDigital(), 
                                                                   tipoDocumento.getUsoProcessoAdministrativo(), tipoDocumento.getAtivo()))
                        .collect(Collectors.toList());
                return Response.ok(listaTiposDocumentoDTO).build();
            } else {
                return Response.noContent().build();
            }
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("lTP"));
        }
    }
    
    @GET
    @Path("/dossie-digital")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Lista de tipos de documentos disponíveis carregada com sucesso.", response = br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento.dossiedigital.TipoDocumentoDTO.class, responseContainer = "List")
        ,
        @ApiResponse(code = 204, message = "Não existem tipos de documentos disponíveis.")})
    @ApiOperation(hidden = false, value = "Lista a tipologia documental definida habilitada para as operações realizadas sob o conceito do Dossiê Digital")
    public Response listTiposDocumentoDossieDigital(@Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/dossie-digital", capturaEngineCliente(headers));
        try {
            List<TipoDocumento> listaTiposDocumento = this.tipoDocumentoServico.listTipologiaByCategoria(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
            if (Objects.nonNull(listaTiposDocumento) && !listaTiposDocumento.isEmpty()) {
                List<br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento.dossiedigital.TipoDocumentoDTO> listaTiposDocumentoDTO = listaTiposDocumento.stream()
                        .map(tipoDocumento -> new br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento.dossiedigital.TipoDocumentoDTO(tipoDocumento))
                        .collect(Collectors.toList());
                return Response.ok(listaTiposDocumentoDTO).build();
            } else {
                return Response.noContent().build();
            }
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("lTDDD"));
        }
    }
    
    @GET
    @Path("/processo-administrativo")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Retorna lista com os tipos de documento do Processo Administrativo.", response = br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento.processoadministrativo.TipoDocumentoDTO.class, responseContainer = "List")})
    @ApiOperation(hidden = false, value = "Retorna lista com os tipos de documento do Processo Administrativo.")
    public Response listTiposDocumentoProcessoAdministrativo(@Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/processo-administrativo", capturaEngineCliente(headers));
        try {
            List<TipoDocumento> listaTiposDocumento = this.tipoDocumentoServico.listTipologiaByCategoria(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
            if (Objects.nonNull(listaTiposDocumento) && !listaTiposDocumento.isEmpty()) {
                List<br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento.processoadministrativo.TipoDocumentoDTO> listaTiposDocumentoDTO = listaTiposDocumento.stream()
                        .map(tipoDocumento -> new br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento.processoadministrativo.TipoDocumentoDTO(tipoDocumento))
                        .collect(Collectors.toList());
                return Response.ok(listaTiposDocumentoDTO).build();
            } else {
                return Response.noContent().build();
            }
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("lTDDD"));
        }
    }

    @GET
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Tipo de Documento localizado com sucesso", response = TipoDocumentoDTO.class)
        ,
        @ApiResponse(code = 404, message = "O tipo de documento solicitado não foi localizado")})
    @ApiOperation(hidden = false, value = "Consulta de um tipo de documento especifico pelo identificador")
    public Response getTipoDocumentoByID(
            @ApiParam(name = "id", value = "Identificador do tipo de documento a ser localizado")
            @PathParam("id") Integer id, @Context HttpHeaders headers
    ) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            TipoDocumento tipoDocumento = this.tipoDocumentoServico.findById(id);
            if (Objects.nonNull(tipoDocumento)) {
                TipoDocumentoDTO tipoDocumentoDTO = new TipoDocumentoDTO(tipoDocumento);
                return Response.ok(tipoDocumentoDTO).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gTDBI"));
        }
    }

    @GET
    @Path("/dados-declarados/pessoa-fisica")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Tipo de Documento encontrado com sucesso", response = TipoDocumentoDadosDeclaradosDTO.class)
        ,
        @ApiResponse(code = 404, message = "Tipo de Documento não foi encontrado")})
    @ApiOperation(hidden = false, value = "Obtem a estrutura do Tipo de Documento Dados Declarados.")
    public Response getDadosDeclaradosPF(@Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/dados-declarados/pessoa-fisica", capturaEngineCliente(headers));
        try {
            TipoDocumento tipoDocumentoDadosDeclarados = tipoDocumentoServico.getByTipologia(ConstantesUtil.TIPOLOGIA_DADOS_DECLARADOS_PF);

            if (Objects.isNull(tipoDocumentoDadosDeclarados)) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            TipoDocumentoDadosDeclaradosDTO tipoDocumentoDadosDeclaradosDTO = new TipoDocumentoDadosDeclaradosDTO(tipoDocumentoDadosDeclarados, Boolean.TRUE);

            return Response.ok(tipoDocumentoDadosDeclaradosDTO).build();

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gDD"));
        }
    }
    
    @GET
    @Path("/dados-declarados/pessoa-juridica")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Tipo de Documento encontrado com sucesso", response = TipoDocumentoDadosDeclaradosDTO.class)
        ,
        @ApiResponse(code = 404, message = "Tipo de Documento não foi encontrado")})
    @ApiOperation(hidden = false, value = "Obtem a estrutura do Tipo de Documento Dados Declarados.")
    public Response getDadosDeclaradosPJ(@Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            TipoDocumento tipoDocumentoDadosDeclarados = tipoDocumentoServico.getByTipologia(ConstantesUtil.TIPOLOGIA_DADOS_DECLARADOS_PJ);

            if (Objects.isNull(tipoDocumentoDadosDeclarados)) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            TipoDocumentoDadosDeclaradosDTO tipoDocumentoDadosDeclaradosDTO = new TipoDocumentoDadosDeclaradosDTO(tipoDocumentoDadosDeclarados, Boolean.TRUE);

            return Response.ok(tipoDocumentoDadosDeclaradosDTO).build();

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gDD"));
        }
    }

    @POST
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Tipo de documento criado com sucesso", responseHeaders = {
            @ResponseHeader(name = "location", description = "Endereço com caminho a ser utilizado para consultar o recurso criado", response = String.class)
        })
        ,
        @ApiResponse(code = 409, message = "A representação do objeto conflita com outro registro já existente. Verificar detalhamento no retorno.")
    })
    @ApiOperation(hidden = false, value = "Realiza a inclusão de um novo tipo de documento")
    public Response createTipoDocumento(TipoDocumentoNovoDTO tipoDocumentoNovoDTO, @Context UriInfo uriInfo, 
    		@Context HttpHeaders headers) throws URISyntaxException {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/", capturaEngineCliente(headers));
        try {
            TipoDocumento tipoDocumento = tipoDocumentoNovoDTO.prototype();

            this.tipoDocumentoServico.save(tipoDocumento, tipoDocumentoNovoDTO.getFuncoesDocumentoInclusaoVinculo());
            return Response.created(new URI(uriInfo.getPath().concat("/").concat(tipoDocumento.getId().toString()))).entity(tipoDocumento.getId()).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("cTD"));
        }
    }

    @POST
    @Path("/{id}/atributo-extracao")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Atributo extração criado com sucesso", responseHeaders = {
            @ResponseHeader(name = "location", description = "Endereço com caminho a ser utilizado para consultar o recurso criado", response = String.class)
        })
        ,
        @ApiResponse(code = 409, message = "A representação do objeto conflita com outro registro já existente. Verificar detalhamento no retorno.")
    })
    @ApiOperation(hidden = false, value = "Realiza a inclusão de novo atributo extração vinculado ao tipo de documento indicado")
    public Response createAtributoExtracaoTipoDocumento(
            @ApiParam(name = "id", value = "Identificador do tipo de documento com o atributo vinculado")
            @PathParam("id") Integer idTipoDocumento,
            AtributoExtracaoNovoDTO atributoExtracaoNovoDTO,
            @Context UriInfo uriInfo, @Context HttpHeaders headers) throws URISyntaxException {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/{id}/atributo-extracao", capturaEngineCliente(headers), montarValores("id", String.valueOf(idTipoDocumento)));
        try {

            AtributoExtracao atributoExtracao = atributoExtracaoNovoDTO.prototype();

            this.tipoDocumentoServico.saveAtributoExtracao(idTipoDocumento, atributoExtracaoNovoDTO.getIdentificadorAtributoPartilha(), atributoExtracao);
            
            String location = uriInfo.getPath().replace("/atributo-extracao", "?atributos=true");
            return Response.created(new URI(location)).entity(atributoExtracao.getId()).build();
            
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("cAETD"));
        }
    }

    @PATCH
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Modificações encaminhadas executadas com sucesso")
        ,
        @ApiResponse(code = 404, message = "O tipo de documento solicitado não foi localizado")
        ,
        @ApiResponse(code = 409, message = "A representação do objeto conflita com outro registro já existente. Verificar detalhamento no retorno.")
    })
    @ApiOperation(hidden = false, value = "Realiza a alteração de um tipo de documento")
    public Response applyPatchTipoDocumento(
            @ApiParam(name = "id", value = "Identificador do tipo de documento a ser modificada")
            @PathParam("id") Integer id,
            TipoDocumentoManutencaoDTO tipoDocumentoManutencaoDTO, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.PATCH.name(), BASE_URL + "/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            this.tipoDocumentoServico.aplicaPatch(id, tipoDocumentoManutencaoDTO);
            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aPTD"));
        }
    }

    @PATCH
    @Path("/{idTipo}/atributo-extracao/{idAtributo}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Modificações encaminhadas executadas com sucesso")
        ,
        @ApiResponse(code = 404, message = "O tipo de documento ou atributo extração solicitado não foi localizado")
        ,
        @ApiResponse(code = 409, message = "A representação do objeto conflita com outro registro já existente. Verificar detalhamento no retorno.")
    })
    @ApiOperation(hidden = false, value = "Realiza a alteração de um atributo extração vinculado ao tipo de documento indicado")
    public Response applyPatchAtributoTipoDocumento(
            @ApiParam(name = "idTipo", value = "Identificador do tipo de documento com o atributo vinculado")
            @PathParam("idTipo") Integer idTipoDocumento,
            @ApiParam(name = "idAtributo", value = "Identificador do atributo extração vinculado ao documento que será aplicado a alteração")
            @PathParam("idAtributo") Integer idAtributo,
            AtributoExtracaoManutencaoDTO atributoExtracaoManutencaoDTO, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.PATCH.name(), BASE_URL + "/{idTipo}/atributo-extracao/{idAtributo}", capturaEngineCliente(headers), montarValores("idTipo", String.valueOf(idTipoDocumento), "idAtributo", String.valueOf(idAtributo)));
        try {
        	
            this.tipoDocumentoServico.aplicaPatchAtributoExtracao(idTipoDocumento, idAtributo, atributoExtracaoManutencaoDTO);
            return Response.noContent().build();
            
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aPATD"));
        }
    }

    @DELETE
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Tipo de documento removido com sucesso")
        ,
        @ApiResponse(code = 404, message = "O tipo de documento solicitado não foi localizado")
        ,
        @ApiResponse(code = 409, message = "O tipo de documento possui associações que a impedem de ser excluido.")
    })
    @ApiOperation(hidden = false, value = "Realiza a exclusão de um tipo de documento especifico")
    public Response removeTipoDocumento(
            @ApiParam(name = "id", value = "Identificador do tipo de documento a ser excluído")
            @PathParam("id") Integer id, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.DELETE.name(), BASE_URL + "/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            this.tipoDocumentoServico.delete(id);
            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("rTD"));
        }
    }

    @DELETE
    @Path("/{idTipo}/atributo-extracao/{idAtributo}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Atributo removido com sucesso")
        ,
        @ApiResponse(code = 404, message = "O atributo relacionado ao tipo de documento solicitado não foi localizado")
        ,
        @ApiResponse(code = 409, message = "O tipo de documento possui associações que a impedem de ser excluido.")
    })
    @ApiOperation(hidden = false, value = "Realiza a exclusão de um atributo extração vinculado ao tipo de documento indicado")
    public Response removeAtributoTipoDocumento(
            @ApiParam(name = "idTipo", value = "Identificador do tipo de documento de vinculação do atributo")
            @PathParam("idTipo") Integer idTipoDocumento,
            @ApiParam(name = "idAtributo", value = "Identificador atributo a ser excluído")
            @PathParam("idAtributo") Integer idAtributo, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.DELETE.name(), BASE_URL + "/{idTipo}/atributo-extracao/{idAtributo}", capturaEngineCliente(headers), montarValores("idtipo", String.valueOf(idTipoDocumento), "idAtributo", String.valueOf(idAtributo)));
        try {
        	
            this.tipoDocumentoServico.deleteAtributoExtracao(idTipoDocumento, idAtributo);
            return Response.noContent().build();
            
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("rATD"));
        }
    }
    
    @POST
    @Path("/{id}/atributo-extracao/{idAtributo}/opcao")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Opção atributo incluído com sucesso", responseHeaders = {
            @ResponseHeader(name = "location", description = "Endereço com caminho a ser utilizado para consultar o recurso criado", response = String.class)
        })
        ,
        @ApiResponse(code = 400, message = "Falha na validação de informações.")
    })
    @ApiOperation(hidden = false, value = "Realiza a inclusão de nova opção atributo vinculado ao tipo de documento e atributo extração indicado")
    public Response insereOpcaoAtributo(
            @ApiParam(name = "id", value = "Identificador do tipo de documento")
            @PathParam("id") Integer idTipoDocumento,
            @ApiParam(name = "idAtributo", value = "Identificador do atributo extração vinculado ao tipo de documento")
            @PathParam("idAtributo") Integer idAtributo,
            OpcaoAtributoNovoDTO opcaoAtributoNovoDTO,
            @Context UriInfo uriInfo, @Context HttpHeaders headers) throws URISyntaxException {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/{id}/atributo-extracao/{idAtributo}/opcao", capturaEngineCliente(headers), montarValores("id", String.valueOf(idTipoDocumento), "idAtributo", String.valueOf(idAtributo)));
        try {

            OpcaoAtributo opcaoAtributo = opcaoAtributoNovoDTO.prototype();

            this.tipoDocumentoServico.insereNovaOpcaoAtributo(idTipoDocumento, idAtributo, opcaoAtributo);
            
            // separa pra pegar até o primeiro paramentro passado na url
            String pathArray []= uriInfo.getPath().split("/atributo-extracao/");
            
            String location = pathArray[0].concat("?atributos=true"); 
            return Response.created(new URI(location)).build();
         
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("iOA"));
        }
    }

    @DELETE
    @Path("/{idTipo}/atributo-extracao/{idAtributo}/opcao/{idOpcao}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Opção do atributo excluída com sucesso.")
        ,
        @ApiResponse(code = 404, message = "Tipo de Documento, atributo extração ou opção de atributo não localizado sob o identificador informado.")
    })
    @ApiOperation(hidden = false, value = "Realiza a exclusão de uma opção de atributo relacionado a um atributo de extração de um tipo de documento.")
    public Response removeOpcaoAtributoTipoDocumento(
            @ApiParam(name = "idTipo", value = "Identificador do tipo de documento cujo atributo de extração esteja vinculado.")
            @PathParam("idTipo") Integer idTipoDocumento,
            @ApiParam(name = "idAtributo", value = "Identificador do atributo de extração do tipo de documento ao qual a opção deverá ser vinculada.")
            @PathParam("idAtributo") Integer idAtributo,
            @ApiParam(name = "idOpcao", value = "Identificador da opção predefinida vinculado ao atributo de extração que deverá ser removido.")
            @PathParam("idOpcao") Integer idOpcao, @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.DELETE.name(), BASE_URL + "/{idTipo}/atributo-extracao/{idAtributo}/opcao/{idOpcao}", capturaEngineCliente(headers), montarValores("idtipo", String.valueOf(idTipoDocumento), "idAtributo", String.valueOf(idAtributo), "idOpcao", String.valueOf(idOpcao)));
        try {
        	
            this.tipoDocumentoServico.deleteOpcaoAtributoExtracao(idTipoDocumento, idAtributo, idOpcao);
            return Response.noContent().build();
            
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("rOATD"));
        }
    }
}
