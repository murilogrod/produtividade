package br.gov.caixa.simtr.visao.dossiedigital.rest.v1;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBException;
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
import javax.ws.rs.core.Response.Status;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.DossieDigitalServico;
import br.gov.caixa.simtr.controle.vo.DocumentoVO;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.AtributoDocumentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.DossieClienteDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.DossieClientePFDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.RetornoExtracaoDadosDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.RetornoInclusaoDadosDeclaradosDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.SolicitacaoAtualizacaoCadastroCaixaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.SolicitacaoExtracaoDadosDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.SolicitacaoInclusaoCartaoAssinaturaDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(hidden = false, tags = {
    "Dossiê Digital - Dossiê Cliente"
})
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.", response = RetornoErroDTO.class)
})
@Path("dossie-digital/v1/dossie-cliente")
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
    private AnalyticsServico analyticsServico;

    private static final String BASE_URL = "/dossie-digital/v1/dossie-cliente/";

    private final String PREFIXO = "DOS.DCR.V1.";

    @GET
    @Path("/cpf/{cpf}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Consulta ao dossiê de cliente realizada com sucesso", response = DossieClientePFDTO.class),
        @ApiResponse(code = 404, message = "CPF informado não localizado junto a Receita Federal")
    })
    @ApiOperation(hidden = false, value = "Consulta do dossiê do cliente pelo número do seu CPF", tags = {
        "Dossiê Digital - Dossiê Cliente"
    })
    public Response consultaDossieClienteByCPF(@PathParam("cpf")
    @ApiParam("Numero do CPF do dossiê do cliente a ser utilizado como base para a operação de atualização cadastral")
    final Long cpf, @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "cpf/{cpf}", capturaEngineCliente(headers), montarValores("cpf", String.valueOf(cpf)));
        try {
            DossieClienteDTO dossieClienteDTO = this.dossieDigitalServico.getByCpfCnpj(cpf, TipoPessoaEnum.F);
            if (dossieClienteDTO == null) {
                return Response.status(Status.NOT_FOUND).build();
            }

            return Response.ok((DossieClientePFDTO) dossieClienteDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("cDCBCPF"));
        }
    }

    @GET
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Consulta ao dossiê de cliente realizada com sucesso", response = DossieClientePFDTO.class),
        @ApiResponse(code = 400, message = "ID informado trata-se de dossiê cliente PJ ainda não estruturado para fluxo do Dossiê Digital"),
        @ApiResponse(code = 404, message = "ID informado não localizado na base do SIMTR"),
    })
    @ApiOperation(hidden = false, value = "Consulta do dossiê do cliente pelo número do seu CPF", tags = {
        "Dossiê Digital - Dossiê Cliente"
    })
    public Response consultaDossieClienteById(@PathParam("id")
    @ApiParam("Identificador do dossiê do cliente a ser utilizado como base para a operação de atualização cadastral")
    final Long id, @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            DossieClienteDTO dossieClienteDTO = this.dossieDigitalServico.getById(id);
            if (dossieClienteDTO == null) {
                return Response.status(Status.NOT_FOUND).build();
            }

            if (TipoPessoaEnum.F.equals(dossieClienteDTO.getTipoPessoa())) {
                return Response.ok((DossieClientePFDTO) dossieClienteDTO).build();
            } else {
                SimtrRequisicaoException simtrRequisicaoException = new SimtrRequisicaoException("Identificador informado trata-se de dossiê de Pessoa Juridica ainda não implementado no SIMTR.");
                return this.montaRespostaExcecao(simtrRequisicaoException, this.PREFIXO.concat("cDCBI"));
            }
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("cDCBI"));
        }
    }

    @POST
    @Path("/{id}/extracao-dados")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Extração realizada com sucesso", response = RetornoExtracaoDadosDTO.class),
        @ApiResponse(code = 400, message = "Binario que representa o documento não é um binario Base64 valido."),
        @ApiResponse(code = 403, message = "Canal de comunicação não associado ao cliente ID da autenticação ou canal não autorizado a consumir os serviçosa do Dossiê Digital."),
        @ApiResponse(code = 404, message = "Dossiê de cliente não localizado sob o identificador informado"),
        @ApiResponse(code = 503, message = "Falha de comunicação com o SIECM")
    })
    @ApiOperation(hidden = false, value = "Extração de dados da imagem do documento (OCR) vinculado a um cliente indicado", tags = {
        "Dossiê Digital - Dossiê Cliente"
    })
    public Response extrairDadosImagem(@PathParam("id")
    final Long id, final SolicitacaoExtracaoDadosDTO solicitacaoExtracaoDadosDTO, @Context HttpHeaders headers) {
        RetornoExtracaoDadosDTO respostaExtracaoDadosDTO = new RetornoExtracaoDadosDTO();
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "{id}/extracao-dados", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));

        try {
            FormatoConteudoEnum formatoConteudoEnum = FormatoConteudoEnum.getByMimeType(solicitacaoExtracaoDadosDTO.getMimetype());
            DocumentoVO documentoVo = this.dossieDigitalServico.extraiDadosDocumentoDossieDigital(id, formatoConteudoEnum, solicitacaoExtracaoDadosDTO.getTipoDocumento(), solicitacaoExtracaoDadosDTO.getBinario());

            Documento documento = documentoVo.getDocumentoSIMTR();
            respostaExtracaoDadosDTO.setIdentificadorSimtr(documento.getId());

            FormatoConteudoEnum formatoConteudoRetornoEnum = documento.getCodigoSiecmTratado() == null ? documento.getFormatoConteudoEnum() : documento.getFormatoConteudoTratadoEnum();
            respostaExtracaoDadosDTO.setMimetype(formatoConteudoRetornoEnum.getMimeType());

            String codigoSiecm = documento.getCodigoSiecmTratado() == null ? documento.getCodigoGED() : documento.getCodigoSiecmTratado();
            respostaExtracaoDadosDTO.setIdentificadorSiecm(codigoSiecm);
            respostaExtracaoDadosDTO.setObjectStore(ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
            respostaExtracaoDadosDTO.setLink(documentoVo.getLinkGED());

            documento.getAtributosDocumento().forEach(ad -> {
                respostaExtracaoDadosDTO.addAtributosDocumentoDTO(new AtributoDocumentoDTO(ad));
            });

            return Response.ok(respostaExtracaoDadosDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("sED"));
        }
    }

    @GET
    @Path("/{id}/documento/{identificador}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Documento capturado com sucesso", response = br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.documento.DocumentoDTO.class),
        @ApiResponse(code = 400, message = "Documento informado não se refere a um documento vinculado cliente"),
        @ApiResponse(code = 404, message = "Documento não localizado sob o identificador informado"),
        @ApiResponse(code = 503, message = "Falha de comunicação com o SIECM para captura do binário", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Captura um documento baseado no seu identificador e retorna o mesmo sob o contexto do dossiê digital", tags = {
        "Dossiê Digital - Dossiê Cliente"
    })
    public Response capturaDocumento(@PathParam("id") Long idDossieCliente, @PathParam("identificador") Long identificadorDocumento, @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/{id}/documento/{identificador}", capturaEngineCliente(headers),
                        montarValores("id", String.valueOf(idDossieCliente), "identificador", String.valueOf(identificadorDocumento)));

        try {

            Documento documento = this.dossieDigitalServico.getDocumentoById(idDossieCliente, identificadorDocumento);
            
            br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.documento.DocumentoDTO documentoDTO = new br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.documento.DocumentoDTO(documento);
            
            return Response.ok(documentoDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aD"));
        }

    }
    
    @POST
    @Path("/{id}/documento/{identificador}/atualizacao-dados")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Atualização dos dados executada com sucesso"),
        @ApiResponse(code = 400, message = "Erro na requisição"),
        @ApiResponse(code = 503, message = "Erro de comunicação com algum sistema")
    })
    @ApiOperation(hidden = false, value = "Ajustes nos dados extraidos do documento.", tags = {
        "Dossiê Digital - Dossiê Cliente"
    })
    public Response atualizacaoDados(@PathParam("id") Long idDossieCliente, @PathParam("identificador") Long identificadorDocumento, final List<AtributoDocumentoDTO> dadosDocumento, @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/{id}/documento/{identificador}/atualizacao-dados", capturaEngineCliente(headers), 
        		montarValores("id", String.valueOf(idDossieCliente), "identificador", String.valueOf(identificadorDocumento)));

        try {
            Map<String, String> mapaAtributosAlteracao = new HashMap<>();
            // Transforma os atributos a serem alterados em um Map para ser repassado a camada de serviço
            if (dadosDocumento != null) {
                dadosDocumento.forEach(atributoDocumentoDTO -> mapaAtributosAlteracao.put(atributoDocumentoDTO.getChave(), atributoDocumentoDTO.getValor()));
            }

            this.dossieDigitalServico.executaAtualizacaoDocumentoDossieDigital(idDossieCliente, identificadorDocumento, mapaAtributosAlteracao);
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aD"));
        }

        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/cartao-assinatura")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Cartão de assinaturas gerado com sucesso. Retorno no corpo da mensagem em formato Base64"),
        @ApiResponse(code = 400, message = "Dossiê de cliente impossibilitado de ter o cartão de assinaturas gerado por não se tratar de pessoa física"),
        @ApiResponse(code = 404, message = "Dossiê de cliente não localizado sob o identificador informado"),
        @ApiResponse(code = 500, message = "Falha interna do servidor ao gerar o cartão de assinaturas"),
        @ApiResponse(code = 503, message = "Falha de comunicação com o serviço da Receita Federal")
    })
    @ApiOperation(hidden = false, value = "Geração do documento de cartão de assinatura digitalizado para coleta da assinatura do cliente.", tags = {
        "Dossiê Digital - Dossiê Cliente"
    })
    @Produces({
        MediaType.TEXT_PLAIN,
        MediaType.APPLICATION_JSON
    })
    public Response gerarCartaoAssinatura(@PathParam(value = "id")
    final Long id, @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "{id}/cartao-assinatura", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));

        try {

            byte[] relatorio = this.dossieDigitalServico.geraCartaoAssinatura(id);
            return Response.ok(Base64.getEncoder().encode(relatorio)).build();

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gCA"));
        }
    }

    @POST
    @Path("/{id}/cartao-assinatura")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Cartão assinaturas armazenado com sucesso junto ao dossiê do cliente"),
        @ApiResponse(code = 400, message = "Requisição inválida"),
        @ApiResponse(code = 503, message = "Erro de comunicação com algum sistema")
    })
    @ApiOperation(hidden = false, value = "Encaminhamento do cartão de assinatura digitalizado para guarda do documento junto ao dossiê do cliente.", tags = {
        "Dossiê Digital - Dossiê Cliente"
    })
    public Response cadastraCartaoAssinatura(@PathParam("id")
    final Long id, final SolicitacaoInclusaoCartaoAssinaturaDTO solicitacaoInclusaoCartaoAssinaturaDTO, @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "{id}/cartao-assinatura", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            FormatoConteudoEnum formatoConteudoEnum = FormatoConteudoEnum.getByMimeType(solicitacaoInclusaoCartaoAssinaturaDTO.getMimetype());
            this.dossieDigitalServico.atualizaCartaoAssinatura(id, formatoConteudoEnum, solicitacaoInclusaoCartaoAssinaturaDTO.getBinario());

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("cCA"));
        }

        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/dados-declarados")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Dados declarados obtidos com sucesso", response = AtributoDocumentoDTO.class, responseContainer = "List"),
        @ApiResponse(code = 204, message = "Dados declarados não existentes para o identificador de cliente informado", response = AtributoDocumentoDTO.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Dossiê do cliente não localizado sob identificador informado"),
        @ApiResponse(code = 500, message = "Tipologia de Dados Declarados não parametrizada corretamente no sistema. A equipe tecnica deve ser acionada")
    })
    @ApiOperation(hidden = false, value = "Consulta os atributos do documento de dados declarados mais recente do cliente.", tags = {
        "Dossiê Digital - Dossiê Cliente"
    })
    public Response consultarDadosDeclarados(@PathParam(value = "id")
    final Long id, @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "{id}/dados-declarados", capturaEngineCliente(headers));

        try {
            List<AtributoDocumentoDTO> atributos = this.dossieDigitalServico.consultaDadosDeclaradosDossieDigital(id);
            if(atributos.isEmpty()) {
                return Response.noContent().build(); 
            }
            return Response.ok(atributos).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("cDD"));
        }
    }

    @POST
    @Path("/{id}/dados-declarados")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Dados declarados atualizado com sucesso", response = RetornoInclusaoDadosDeclaradosDTO.class),
        @ApiResponse(code = 400, message = "Dossiê do cliente não localizado sob identificador informado, atributo inesperado encaminhado para o documento ou atributo obrigatório não encaminhado"),
        @ApiResponse(code = 500, message = "Tipologia de Dados Declarados não parametrizada corretamente no sistema. A equipe tecnica deve ser acionada"),
        @ApiResponse(code = 503, message = "Falha ao comunicar com o SIECM")
    })
    @ApiOperation(hidden = false, value = "Encaminhamento dos dados declarados emissão e guarda do documento. Opções de atributos objetivos não esperadas para o atributo serão descartadas.")
    public Response atualizaDadosDeclarados(@PathParam("id")
    final Long id, final List<AtributoDocumentoDTO> dadosDeclaradosDTO, @Context UriInfo uriInfo, @Context HttpHeaders headers) throws URISyntaxException {
        
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/{id}/dados-declarados", capturaEngineCliente(headers));

        try {
            Documento documento = this.dossieDigitalServico.atualizaDadosDeclaradosDossieDigital(id, dadosDeclaradosDTO);
            RetornoInclusaoDadosDeclaradosDTO respostaAtualizacaoDadosDeclaradosDTO = new RetornoInclusaoDadosDeclaradosDTO(documento);

            return Response.created(new URI(uriInfo.getPath())).entity(respostaAtualizacaoDadosDeclaradosDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aDD"));
        }
    }

    @POST
    @Path("/{id}/cadastro-caixa")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Atualização realizada com sucesso"),
        @ApiResponse(code = 400, message = "Os documentos do cliente possuem dados incompletos, ou foram informados documentos não pertencentes do dossiê indicado", response = RetornoErroDTO.class),
        @ApiResponse(code = 428, message = "Não foram encontrados documentos aptos a executar atualização cadastral", response = RetornoErroDTO.class),
        @ApiResponse(code = 503, message = "Erro de comunicação com o sistema de Cadastro de clientes da CAIXA", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Atualização dos dados cadastrais do cliente o cadastro CAIXA com guarda permanente dos documentos", tags = {
        "Dossiê Digital - Dossiê Cliente"
    })
    public Response atualizaCadastroCaixaDossieDigitalById(@PathParam("id")
    @ApiParam("Identificador do dossiê do cliente a ser utilizado como base para a operação de atualização cadastral")
    final Long id, @ApiParam("Objeto contendo a lista de documentos a ser utilizado no processo de atualização, caso não informado, serão utilizados todos os documentos do cliente que possuem os dados já extraídos e ainda atualizados junto ao Cadastro CAIXA")
    final SolicitacaoAtualizacaoCadastroCaixaDTO solicitacaoAtualizacaoCadastroCaixaDTO, @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "{id}/cadastro-caixa", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            this.dossieDigitalServico.atualizaCadastroCaixaDossieCliente(id, solicitacaoAtualizacaoCadastroCaixaDTO.getIdentificadoresDocumentosSIMTR(), solicitacaoAtualizacaoCadastroCaixaDTO.getIdentificadoresDocumentosSIECM());
            return Response.noContent().build();
            
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aCCDDBI"));
        }
    }
}
