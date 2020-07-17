package br.gov.caixa.simtr.visao.negocio.rest.v1;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.arquitetura.servico.localidade.dto.LocalidadeResponseDTO;
import br.gov.caixa.pedesgo.arquitetura.servico.sicpf.SicpfResponseDTO;
import br.gov.caixa.pedesgo.arquitetura.servico.sicpf.SicpfResultadoCpf;
import br.gov.caixa.pedesgo.arquitetura.sicli.dto.RetornoClasse1DTO;
import br.gov.caixa.pedesgo.arquitetura.sicli.dto.inclusao.pf.SicliPFRequestDTO;
import br.gov.caixa.pedesgo.arquitetura.sicli.dto.inclusao.pj.SicliPJRequestDTO;
import br.gov.caixa.pedesgo.arquitetura.siiso.dto.RetornoPessoasFisicasDTO;
import br.gov.caixa.pedesgo.arquitetura.siiso.dto.RetornoPessoasJuridicasDTO;
import br.gov.caixa.pedesgo.arquitetura.siiso.servico.CadastroReceitaPJServico;
import br.gov.caixa.pedesgo.arquitetura_common.entities.ComunicacaoWSDTO;
import br.gov.caixa.simtr.controle.excecao.BpmException;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.BPMServico;
import br.gov.caixa.simtr.controle.servico.CadastroCaixaServico;
import br.gov.caixa.simtr.controle.servico.CadastroReceitaServico;
import br.gov.caixa.simtr.controle.servico.DocumentoServico;
import br.gov.caixa.simtr.controle.servico.DossieClienteServico;
import br.gov.caixa.simtr.controle.servico.DossieProdutoServico;
import br.gov.caixa.simtr.controle.servico.LocalidadeServico;
import br.gov.caixa.simtr.controle.servico.NivelDocumentalServico;
import br.gov.caixa.simtr.controle.servico.ProcessoServico;
import br.gov.caixa.simtr.controle.servico.SituacaoDossieServico;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.DossieClientePF;
import br.gov.caixa.simtr.modelo.entidade.DossieClientePJ;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.OpcaoSelecionada;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDossie;
import br.gov.caixa.simtr.modelo.entidade.TipoSituacaoDossie;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDossieEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.documento.AtributoDocumentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.DossieClienteDOC;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.DossieClienteDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.DossieClientePFDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.DossieClientePJDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.NivelDocumentalDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.ProcessoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.manutencao.DocumentoInclusaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.manutencao.DossieClienteAlteracaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.manutencao.DossieClienteInclusaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.siric.AvaliacaoSiricDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.jaxrs.PATCH;

@Api(hidden = false, value = "Apoio ao Negócio - Dossiê Cliente")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")})
@Path("negocio/v1/dossie-cliente")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class DossieClienteREST extends AbstractREST {

    @EJB
    private BPMServico bpmServico;

    @EJB
    private CadastroReceitaPJServico cadastroReceitaPJServico;

    @EJB
    private DossieClienteServico dossieClienteServico;

    @EJB
    private DocumentoServico documentoServico;

    @EJB
    private DossieProdutoServico dossieProdutoServico;

    @EJB
    private NivelDocumentalServico nivelDocumentalServico;

    @EJB
    private ProcessoServico processoServico;

    @EJB
    private CadastroCaixaServico sicliServico;

    @EJB
    private CadastroReceitaServico sicpfServico;

    @EJB
    private SituacaoDossieServico situacaoDossieServico;
    
    @EJB
    private AnalyticsServico analyticsServico;

    @EJB
    private LocalidadeServico localidadeServico;

    private static final Logger LOGGER = Logger.getLogger(DossieClienteREST.class.getName());
    
    private static final String BASE_URL = "/negocio/v1/dossie-cliente";

    private final String PREFIXO = "SDN.DCR.";

    @GET
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Retorna o dossiê de cliente solicitado.", response = DossieClienteDOC.class)
        ,
	@ApiResponse(code = 404, message = "Dossiê de cliente solicitado não foi localizado com o ID informado.")})
    @ApiOperation(hidden = false, value = "Captura o dossiê de cliente baseado no ID do mesmo.")
    @ApiParam(name = "id", value = "Identificador único do Dossiê de Cliente")
    public Response getById(@PathParam("id") Long id, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/{id}", capturaEngineCliente(headers),montarValores("id", String.valueOf(id)));
    	
        DossieCliente dossieCliente = this.dossieClienteServico.getById(id, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        if (dossieCliente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        this.ajustaDossiesProdutoVinculados(dossieCliente);

        DossieClienteDTO dossieClienteDTO = null;
        if (DossieClientePF.class.equals(dossieCliente.getClass())) {
            dossieClienteDTO = new DossieClientePFDTO((DossieClientePF) dossieCliente);
        } else if (DossieClientePJ.class.equals(dossieCliente.getClass())) {
            dossieClienteDTO = new DossieClientePJDTO((DossieClientePJ) dossieCliente);
        }

        if ((dossieClienteDTO != null) && (dossieClienteDTO.getDossiesProdutoDTO() != null)) {
            dossieClienteDTO.getDossiesProdutoDTO().forEach(dossieProdutoDTO -> {
                Processo processo = this.processoServico.getPatriarcaById(dossieProdutoDTO.getProcessoDossieDTO().getId());
                dossieProdutoDTO.setProcessoPatriarcaDTO(new ProcessoDTO(processo));
            });
        }

        return Response.ok(dossieClienteDTO).build();
    }

    @POST
    @Path("/{id}/nivel-documental")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Apuração realizada com sucesso.", response = NivelDocumentalDTO.class)
        ,
	@ApiResponse(code = 404, message = "Dossiê de cliente solicitado não foi localizado com o ID informado.")})
    @ApiOperation(hidden = false, value = "Realiza uma nova apuração do nivel documental do cliente identificado.")
    @ApiParam(name = "id", value = "Identificador único do Dossiê de Cliente")
    public Response atualizaNivelDocumental(@PathParam("id") Long id, @Context HttpHeaders headers) {
        try {
        	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/{id}/nivel-documental", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
            DossieCliente dossieCliente = this.nivelDocumentalServico.atualizaNiveisDocumentaisCliente(id);
            NivelDocumentalDTO nivelDocumentalDTO = new NivelDocumentalDTO(dossieCliente);
            return Response.ok(nivelDocumentalDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aND"));
        }
    }

    @GET
    @Path("/cpf/{cpf}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Retorna o dossiê de cliente solicitado.", response = DossieClientePFDTO.class)
        ,
	@ApiResponse(code = 404, message = "Dossiê de cliente solicitado não foi localizado com o CPF informado.")})
    @ApiOperation(hidden = false, value = "Captura o dossiê de cliente baseado no CPF do mesmo.")
    @ApiParam(name = "cpfCnpj", value = "CPF do cliente solicitado.")
    public Response getByCpf(@PathParam("cpf") Long cpf, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/cpf/{cpf}", capturaEngineCliente(headers),montarValores("cpf", String.valueOf(cpf)));
        DossieCliente dossieCliente = this.dossieClienteServico.getByCpfCnpj(cpf, TipoPessoaEnum.F, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        if (dossieCliente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        this.ajustaDossiesProdutoVinculados(dossieCliente);

        DossieClienteDTO dossieClienteDTO = new DossieClientePFDTO((DossieClientePF) dossieCliente);
        if (Objects.nonNull(dossieClienteDTO.getDossiesProdutoDTO())) {
            dossieClienteDTO.getDossiesProdutoDTO().forEach(dossieProdutoDTO -> {
                Processo processo = this.processoServico.getPatriarcaById(dossieProdutoDTO.getProcessoDossieDTO().getId());
                dossieProdutoDTO.setProcessoPatriarcaDTO(new ProcessoDTO(processo));
            });
        }
        return Response.ok(dossieClienteDTO).build();

    }

    @GET
    @Path("/cnpj/{cnpj}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Retorna o dossiê de cliente solicitado.", response = DossieClientePJDTO.class)
        ,
	@ApiResponse(code = 404, message = "Dossiê de cliente solicitado não foi localizado com o CNPJ informado.")})
    @ApiOperation(hidden = false, value = "Captura o dossiê de cliente baseado no CNPJ do mesmo.")
    @ApiParam(name = "cpfCnpj", value = "CNPJ do cliente solicitado.")
    public Response getByCnpj(@PathParam("cnpj") Long cnpj, @Context UriInfo uriInfo, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/cnpj/{cnpj}", capturaEngineCliente(headers), montarValores("cnpj", String.valueOf(cnpj)));
        DossieCliente dossieCliente = this.dossieClienteServico.getByCpfCnpj(cnpj, TipoPessoaEnum.J, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        if (dossieCliente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        this.ajustaDossiesProdutoVinculados(dossieCliente);

        DossieClienteDTO dossieClienteDTO = new DossieClientePJDTO((DossieClientePJ) dossieCliente);
        if (Objects.nonNull(dossieClienteDTO.getDossiesProdutoDTO())) {
            dossieClienteDTO.getDossiesProdutoDTO().forEach(dossieProdutoDTO -> {
                Processo processo = this.processoServico.getPatriarcaById(dossieProdutoDTO.getProcessoDossieDTO().getId());
                dossieProdutoDTO.setProcessoPatriarcaDTO(new ProcessoDTO(processo));
            });
        }
        return Response.ok(dossieClienteDTO).build();
    }

    @GET
    @Path("/cpf/{cpf}/sicli")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Retorna o dados provenientes do SICLI para o CPF solicitado.", response = RetornoClasse1DTO.class)
        ,
	@ApiResponse(code = 404, message = "Cliente solicitado não foi localizado no SICLI com o CPF informado.")
        ,
	@ApiResponse(code = 503, message = "Falha ao consumir serviço do SICLI com o CPF informado.")})
    @ApiOperation(hidden = false, value = "Captura as informações disponiveis do SICLI perante a integração com o SIMTR baseado no CPF do mesmo.")
    @ApiParam(name = "cpfCnpj", value = "CPF do cliente solicitado.")
    public Response getDadosCadastroCaixaPessoaFisica(@PathParam("cpf") Long cpf, @Context HttpHeaders headers) {
        try {
        	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/cpf/{cpf}/sicli", capturaEngineCliente(headers), montarValores("cpf", String.valueOf(cpf)));
            RetornoClasse1DTO retorno = this.sicliServico.consultaCadastroClienteClaseHomologada(cpf, TipoPessoaEnum.F);
            if (retorno == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(retorno).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gDCCPF"));
        } catch (Exception e) {
            return this.montaRespostaExcecao(e, this.PREFIXO.concat("gDCCPF"));
        }
    }

    @PUT
    @Path("/cpf/{cpf}/sicli")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Submete a atualização de dados ao SICLI para o CPF solicitado.", response = ComunicacaoWSDTO.class)})
    @ApiOperation(hidden = false, value = "Inclui dados de novo cliente junto ao SICLI perante a integração com o SIMTR baseado no CPF do mesmo.")
    @ApiParam(name = "cnpj", value = "CPF do cliente solicitado.")
    public Response atualizaCadastroCaixaPessoaFisica(@PathParam("cpf") Long cpf, SicliPFRequestDTO sicliPFRequestDTO, @Context HttpHeaders headers) {
        try {
        	this.analyticsServico.registraEvento(EnumMetodoHTTP.PUT.name(), BASE_URL + "/cpf/{cpf}/sicli", capturaEngineCliente(headers), montarValores("cpf", String.valueOf(cpf)));
            ComunicacaoWSDTO comunicacaoWSDTO = this.sicliServico.atualizaCadastroClientePessoaFisica(cpf, TipoPessoaEnum.F, sicliPFRequestDTO);
            return Response.ok(comunicacaoWSDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aCCPF"));
        } catch (Exception e) {
            return this.montaRespostaExcecao(e, this.PREFIXO.concat("aCCPF"));
        }
    }

    @GET
    @Path("/cpf/{cpf}/sicpf")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Retorna o dados provenientes do SICPF para o CPF solicitado.", response = RetornoPessoasFisicasDTO.class)
        ,
	@ApiResponse(code = 404, message = "Cliente solicitado não foi localizado no SICPF com o CPF informado.")
        ,
	@ApiResponse(code = 503, message = "Falha ao consumir serviço do SICPF com o CPF informado.")})
    @ApiOperation(hidden = false, value = "Captura as informações disponiveis do SICPF perante a integração com o SIMTR baseado no CPF do cliente.")
    @ApiParam(name = "cpf", value = "CPF do cliente solicitado.")
    @Deprecated
    public Response getDadosSICPFByCpf(@PathParam("cpf") Long cpf, @Context HttpHeaders headers) {
        try {
        	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/cpf/{cpf}/sicpf", capturaEngineCliente(headers), montarValores("cpf", String.valueOf(cpf)));
            RetornoPessoasFisicasDTO retornoPessoasFisicasDTO = this.sicpfServico.consultaCadastroPF(cpf);

            if (retornoPessoasFisicasDTO == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            Long tituloEleitor = retornoPessoasFisicasDTO.getTituloEleitor() == null && !retornoPessoasFisicasDTO.getTituloEleitor().isEmpty()
                    ? Long.valueOf(retornoPessoasFisicasDTO.getTituloEleitor()) : null;

            SicpfResultadoCpf sicpfResultadoCpf = new SicpfResultadoCpf();
            sicpfResultadoCpf.setDataNascimento(retornoPessoasFisicasDTO.getDataNascimento());
            sicpfResultadoCpf.setNomeContribuinte(retornoPessoasFisicasDTO.getNomeContribuinte());
            sicpfResultadoCpf.setNomeMae(retornoPessoasFisicasDTO.getNomeMae());
            sicpfResultadoCpf.setSituacao(Integer.valueOf(retornoPessoasFisicasDTO.getCodigoSituacaoCpf()));
            sicpfResultadoCpf.setTituloEleitor(tituloEleitor);
            SicpfResponseDTO retorno = new SicpfResponseDTO();
            retorno.setSicpfResultadoCpf(sicpfResultadoCpf);

            return Response.ok(retorno).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gDSicpfBC"));
        }
    }

    @GET
    @Path("/cnpj/{cnpj}/sicli")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Retorna o dados provenientes do SICLI para o CNPJ solicitado.", response = RetornoClasse1DTO.class)
        ,
	@ApiResponse(code = 404, message = "Cliente solicitado não foi localizado no SICLI com o CNPJ informado.")})
    @ApiOperation(hidden = false, value = "Captura as informações disponiveis do SICLI perante a integração com o SIMTR baseado no CNPJ do mesmo.")
    @ApiParam(name = "cnpj", value = "CNPJ do cliente solicitado.")
    public Response getDadosCadastroCaixaPessoaJuridica(@PathParam("cnpj") Long cnpj, @Context HttpHeaders headers) {
        try {
        	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/cnpj/{cnpj}/sicli", capturaEngineCliente(headers), montarValores("cnpj", String.valueOf(cnpj)));
            RetornoClasse1DTO retorno = this.sicliServico.consultaCadastroClienteClaseHomologada(cnpj, TipoPessoaEnum.J);
            if (retorno == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(retorno).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gDCCPJ"));
        } catch (Exception e) {
            return this.montaRespostaExcecao(e, this.PREFIXO.concat("gDCCPJ"));
        }
    }

    @PUT
    @Path("/cnpj/{cnpj}/sicli")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Submete a atualização de dados ao SICLI para o CNPJ solicitado.", response = ComunicacaoWSDTO.class)})
    @ApiOperation(hidden = false, value = "Inclui dados de novo cliente junto ao SICLI perante a integração com o SIMTR baseado no CNPJ do mesmo.")
    @ApiParam(name = "cnpj", value = "CNPJ do cliente solicitado.")
    public Response incluiCadastroCaixaPessoaJuridica(@PathParam("cnpj") Long cnpj, SicliPJRequestDTO sicliPJRequestDTO, @Context HttpHeaders headers) {
        try {
        	this.analyticsServico.registraEvento(EnumMetodoHTTP.PUT.name(), BASE_URL + "/cnpj/{cnpj}/sicli", capturaEngineCliente(headers), montarValores("cnpj", String.valueOf(cnpj)));
            ComunicacaoWSDTO comunicacaoWSDTO = this.sicliServico.atualizaCadastroClientePessoaJuridica(cnpj, TipoPessoaEnum.J, sicliPJRequestDTO);
            return Response.ok(comunicacaoWSDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("iCCPJ"));
        } catch (Exception e) {
            return this.montaRespostaExcecao(e, this.PREFIXO.concat("iCCPJ"));
        }
    }

    @GET
    @Path("/cpf/{cpf}/receita")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Retorna o dados provenientes do cadastro da Receita Federal para o CPF solicitado", response = RetornoPessoasFisicasDTO.class)
        ,
	@ApiResponse(code = 404, message = "Cliente solicitado não foi localizado no cadastro da Receita Federal com o CPF informado")
        ,
	@ApiResponse(code = 503, message = "Falha ao consumir serviço de cadastro da Receita Federal")})
    @ApiOperation(hidden = false, value = "Captura as informações disponiveis do cadastro da Receita Federal perante a integração com o SIMTR baseado no CPF do cliente")
    @ApiParam(name = "cpf", value = "CPF do cliente solicitado.")
    public Response getDadosReceitaFederalPessoaFisica(@PathParam("cpf") Long cpf, @Context HttpHeaders headers) {
        try {
        	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/cpf/{cpf}/receita", capturaEngineCliente(headers), montarValores("cpf", String.valueOf(cpf)));
            RetornoPessoasFisicasDTO retornoPessoasFisicasDTO = this.sicpfServico.consultaCadastroPF(cpf);

            if (retornoPessoasFisicasDTO == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            return Response.ok(retornoPessoasFisicasDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gDRFPF"));
        }
    }
    
    @GET
    @Path("/cnpj/{cnpj}/receita")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Retorna o dados provenientes do cadastro da Receita Federal para o CNPJ solicitado.", response = RetornoPessoasJuridicasDTO.class)
        ,
	@ApiResponse(code = 404, message = "Cliente solicitado não foi localizado no cadastro da Receita Federal com o CNPJ informado.")
        ,
	@ApiResponse(code = 503, message = "Falha ao consumir serviço de cadastro da Receita Federal.")})
    @ApiOperation(hidden = false, value = "Captura as informações disponiveis do cadastro da Receita Federal perante a integração com o SIMTR baseado no CNPJ do cliente")
    @ApiParam(name = "cnpj", value = "CNPJ do cliente solicitado.")
    public Response getDadosReceitaFederalPessoaJuridica(@PathParam("cnpj") Long cnpj, @Context HttpHeaders headers) {
        try {
        	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/cnpj/{cnpj}/receita", capturaEngineCliente(headers), montarValores("cnpj", String.valueOf(cnpj)));
            RetornoPessoasJuridicasDTO retornoPessoasJuridicasDTO = this.cadastroReceitaPJServico.consultarPJ(String.valueOf(cnpj));

            if (retornoPessoasJuridicasDTO == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            return Response.ok(retornoPessoasJuridicasDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gDRFPJ"));
        } catch (Exception e) {
            return this.montaRespostaExcecao(e, this.PREFIXO.concat("gDRFPJ"));
        }
    }

    @POST
    @Path("/{id}/cadastro-caixa")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Cadastro CAIXA do cliente atualizado com sucesso.")
        ,
	@ApiResponse(code = 412, message = "Não foram identificados documentos aptos a atualização cadastral. Verificar dossiê do cliente.")
        ,
	@ApiResponse(code = 503, message = "Falha ao consumir serviço do cadastro CAIXA")})
    @ApiOperation(hidden = false, value = "Realiza a atualização do cadastro CAIXA utilizando o documento capturado mais recente de cada tipologia perante o dossiê do cliente, que possua atributos extraidos e ainda não tenha sido utilizado na atualização cadastral.")
    @ApiParam(name = "id", value = "Identificador do dossiê do cliente desejado.")
    public Response atualizaCadastroCaixa(@PathParam("id") Long id, @Context HttpHeaders headers) {
        try {
        	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/{id}/cadastro-caixa", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
            
            this.dossieClienteServico.atualizaCadastroCaixa(id);
            
            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aCC"));
        }
    }
    
    @POST
    @Path("/{id}/documento")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Documentos armazenados com sucesso.")
        ,
	@ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.", response = RetornoErroDTO.class)
        ,
	@ApiResponse(code = 409, message = "Falha ao persistir o dossiê de produto por inconsistência de parâmetros.")})
    @ApiOperation(hidden = false, value = "Inclui um documento encaminhados para armazenamento em um Dossiê de Cliente.")
    public synchronized Response incluiDocumentoDossie(@PathParam("id") Long idDossie,
            DocumentoInclusaoDTO documentoInclusaoDTO, @Context HttpHeaders headers) {
        try {
        	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/{id}/documento", capturaEngineCliente(headers), montarValores("id", String.valueOf(idDossie)));
            List<AtributoDocumento> atributosDocumento = documentoInclusaoDTO.getAtributosDocumentoDTO().stream()
                    .map(atributoDTO -> {
                        AtributoDocumento atributoDocumento = new AtributoDocumento();
                        atributoDocumento.setDescricao(atributoDTO.getChave());
                        atributoDocumento.setAcertoManual(Boolean.TRUE);
                        //Caso exista opcoes selecionadas preenchidas no DTO enviado pelo front, deve se mapear as opcoes em OpcaoSelecionada do atributo documento.
                        if(Objects.nonNull(atributoDTO.getOpcoesSelecionadas())  && !atributoDTO.getOpcoesSelecionadas().isEmpty()) {
                            atributoDocumento.setOpcoesSelecionadas(atributoDTO.getOpcoesSelecionadas().stream().map(valorOpcao -> {
                                OpcaoSelecionada opcao = new OpcaoSelecionada();
                                opcao.setAtributoDocumento(atributoDocumento);
                                opcao.setDescricaoOpcao(atributoDTO.getChave());
                                opcao.setValorOpcao(valorOpcao);
                                return opcao;
                            }).collect(Collectors.toSet()));    
                        }else {
                            //Caso não exista opções selecionadas significa que o campo é subjetivo e deve ser salvo no conteudo do Atributo Documento.
                            atributoDocumento.setConteudo(atributoDTO.getValor());
                        }
                        
                        return atributoDocumento;
                    }).collect(Collectors.toList());
            OrigemDocumentoEnum origemDocumentoEnum = documentoInclusaoDTO.getOrigemDocumentoEnum() == null ? OrigemDocumentoEnum.S : documentoInclusaoDTO.getOrigemDocumentoEnum();

            this.dossieClienteServico.criaDocumentoDossieCliente(idDossie,
                    documentoInclusaoDTO.getTipoDocumento(), TemporalidadeDocumentoEnum.VALIDO,
                    origemDocumentoEnum, documentoInclusaoDTO.getMimeType(), atributosDocumento,
                    documentoInclusaoDTO.getBinario());

            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("iDD"));
        }
    }

    @DELETE
    @Path("/{idCliente}/documento/{idDocumento}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Documentos removidos com sucesso.")
        ,
	@ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.", response = RetornoErroDTO.class)
        ,
	@ApiResponse(code = 409, message = "Falha ao persistir o dossiê de produto por inconsistência de parâmetros.")})
    @ApiOperation(hidden = false, value = "Exclui um documento ainda não utilizado juntamente com seu vinculo com o Dossiê de Cliente. Caso em uso, inabilita para novos negócios.")
    public synchronized Response removeDocumentosDossie(
            @PathParam("idCliente") Long idDossie,
            @PathParam("idDocumento") Long idDocumento, @Context HttpHeaders headers) {

        try {
        	this.analyticsServico.registraEvento(EnumMetodoHTTP.DELETE.name(), BASE_URL + "/{idCliente}/documento/{idDocumento}", capturaEngineCliente(headers), montarValores("idCliente", String.valueOf(idDossie), "idDocumento", String.valueOf(idDocumento)));
            this.documentoServico.deleteDocumentoDossieCliente(idDossie, idDocumento);
        } catch (EJBException ee) {
            RetornoErroDTO resourceError = new RetornoErroDTO(Response.Status.CONFLICT.getStatusCode(), "Falha ao remover documento do cliente", ee.getLocalizedMessage());
            return Response.status(Response.Status.CONFLICT).entity(resourceError).build();
        }
        return Response.noContent().build();

    }

    @POST
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Dossiê de cliente armazenado com sucesso.", response = DossieClienteDOC.class)
        ,
	@ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.", response = RetornoErroDTO.class)
        ,
	@ApiResponse(code = 409, message = "Falha ao persistir o dossiê de produto por inconsistência de parâmetros.")})
    @ApiOperation(hidden = false, value = "Cadastra um novo Dossiê de Cliente conforme informações existentes no objeto.")
    public <T extends DossieClienteInclusaoDTO> Response insereDossie(T dossieClienteInclusaoDTO,
            @Context UriInfo uriInfo, @Context HttpHeaders headers) throws URISyntaxException {

    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/", capturaEngineCliente(headers));
        DossieCliente dossieCliente = dossieClienteInclusaoDTO.prototype();
        try {
            this.dossieClienteServico.save(dossieCliente);
        } catch (RuntimeException e) {
            LOGGER.log(Level.ALL, e.getLocalizedMessage(), e);
            RetornoErroDTO resourceError = new RetornoErroDTO(Response.Status.CONFLICT.getStatusCode(),
                    "Falha ao persistir o Dossiê de Cliente", e.getLocalizedMessage());
            return Response.status(Response.Status.CONFLICT).entity(resourceError).build();
        }
        DossieClienteDTO dossieClienteDTO = null;
        if (DossieClientePF.class.equals(dossieCliente.getClass())) {
            dossieClienteDTO = new DossieClientePFDTO((DossieClientePF) dossieCliente);
        } else if (DossieClientePJ.class.equals(dossieCliente.getClass())) {
            dossieClienteDTO = new DossieClientePJDTO((DossieClientePJ) dossieCliente);
        }
        return Response.created(new URI(uriInfo.getPath() + "/" + dossieCliente.getId())).entity(dossieClienteDTO).build();

    }

    @PATCH
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Dossiê de cliente atualizado com sucesso.")
        ,
	@ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo da mensagem de solicitação.", response = RetornoErroDTO.class)})
    @ApiOperation(hidden = false, value = "Atualiza as informações de um Dossiê de Cliente.")
    public <T extends DossieClienteAlteracaoDTO> Response atualizaDossie(@PathParam("id") Long id, T dossieClienteAlteracaoDTO, @Context HttpHeaders headers) {
        try {
        	this.analyticsServico.registraEvento(EnumMetodoHTTP.PATCH.name(), BASE_URL + "/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
            this.dossieClienteServico.aplicaPatch(id, dossieClienteAlteracaoDTO.prototype());
            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aD"));
        }
    }

    @GET
    @Path("/localidade/cep/{cep}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Retorna o dados provenientes do Serviço de Localidade para o CEP solicitado.", response = LocalidadeResponseDTO.class)
        ,
	@ApiResponse(code = 404, message = "CEP solicitado não foi localizado no serviço de Localidade com o CEP informado.")
        ,
	@ApiResponse(code = 503, message = "Falha ao consumir serviço de Localidade com o CEP informado.")})
    @ApiOperation(hidden = false, value = "Captura as informações disponiveis do serviço de localidade perante a integração com o SIMTR baseado no CEP do mesmo.")
    @ApiParam(name = "cep", value = "CEP da localidade solicitada.")
    @Deprecated
    public Response getLocalidadeByCepAndComplemento(@PathParam("cep") String cep) {
        LocalidadeResponseDTO localidadeResponseDTO = null;
        try {
            String cepParteInicial = cep.substring(0, 5);
            String cepParteFinal = "";
            if (cep.length() >= 8) {
                cepParteFinal = cep.substring(5, 8);
            }
            localidadeResponseDTO = this.localidadeServico.consultaCEP(cepParteInicial, cepParteFinal);
            if (localidadeResponseDTO == null || localidadeResponseDTO.getEnderecos().isEmpty()) {
                return Response.noContent().build();
            }
        } catch (StringIndexOutOfBoundsException sibe) {
            LOGGER.log(Level.ALL, sibe.getLocalizedMessage(), sibe);
            RetornoErroDTO resourceError = new RetornoErroDTO(Response.Status.BAD_REQUEST.getStatusCode(),
                    "CEP inválido: => Formato correto: 99999-999", sibe.toString());
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(resourceError).build();
        } catch (Exception e) {
            LOGGER.log(Level.ALL, e.getLocalizedMessage(), e);
            RetornoErroDTO resourceError = new RetornoErroDTO(
                    Response.Status.SERVICE_UNAVAILABLE.getStatusCode(),
                    "Falha ao comunicar com o serviço de Localidade", e.toString());
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(resourceError).build();
        }
        return Response.ok(localidadeResponseDTO).build();
    }

    @GET
    @Path("/{id}/dados-declarados")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Documento Dados Declarados do Cliente encontrado", response = AtributoDocumentoDTO.class, responseContainer = "List")
        ,
        @ApiResponse(code = 404, message = "Documento Dados Declarados do Cliente não foi encontrado")})
    @ApiOperation(hidden = false, value = "Obtem o Documento Dados Declarados mais recente do Cliente.")
    public Response getDocumentoDadosDeclarados(
            @PathParam("id")
            @ApiParam(name = "id", value = "Identificador do Dossie de Cliente")
            final Long id, @Context HttpHeaders headers) {
        try {

        	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/{id}/dados-declarados", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
            Documento documento = documentoServico.getDadosDeclarados(id);

            if (Objects.isNull(documento)) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            List<AtributoDocumentoDTO> atributos = new ArrayList<>();
            if (Objects.nonNull(documento.getAtributosDocumento())) {
                atributos = documento.getAtributosDocumento().stream()
                        .map(atributo -> new AtributoDocumentoDTO(atributo))
                        .collect(Collectors.toList());
            }

            return Response.ok(atributos).build();

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gDDD"));
        }
    }
    
    

    @GET
    @Path("/{id}/avaliacao-risco")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Retorna a consulta do Dossiê Cliente junto ao SIRIC.", response = AvaliacaoSiricDTO.class)
        ,
	@ApiResponse(code = 404, message = "Dossiê de cliente solicitado não foi localizado com o ID informado.")})
    @ApiOperation(hidden = false, value = "Captura o dossiê de cliente baseado no ID do mesmo.")
    @ApiParam(name = "id", value = "Identificador único do Dossiê de Cliente")
    public Response getAvaliacaoRisco(@PathParam("id") Long id, @Context HttpHeaders headers) {
    	try {
    		this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/{id}/avalicao-risco", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
	        DossieCliente dossieCliente = this.dossieClienteServico.getById(id, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
	        if (dossieCliente == null) {
	            return Response.status(Response.Status.NOT_FOUND).build();
	        }
	        
	        String cpfCnpj = "";
	        if(dossieCliente.getTipoPessoa().equals(TipoPessoaEnum.F)) {
	        	cpfCnpj = StringUtils.leftPad(String.valueOf(dossieCliente.getCpfCnpj()), 11, '0');
	        }else {
	        	cpfCnpj = StringUtils.leftPad(String.valueOf(dossieCliente.getCpfCnpj()), 14, '0');
	        }

	        return Response.ok(this.dossieClienteServico.consultaSiric(cpfCnpj)).build();
		} catch (EJBException ee) {
		    return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gDDD"));
		} catch (Exception e) {
			return this.montaRespostaExcecao(e, this.PREFIXO.concat("gDDD"));
		}
    }
    
    @POST
    @Path("/{id}/avaliacao-risco")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Avaliação de Risco solicitada com sucesso.", response = AvaliacaoSiricDTO.class)
        ,
	@ApiResponse(code = 400, message = "Dossiê de cliente solicitado não foi localizado com o ID informado."),
	@ApiResponse(code = 503, message = "Falha em consumir o serviço do SIRIC com o Dossiê Cliente informado.")})
    @ApiParam(name = "id", value = "Identificador único do Dossiê de Cliente")
    public Response avaliacaoRisco(@PathParam("id") Long id, @Context HttpHeaders headers) {
    	try {
            this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/{id}/avaliacao-risco", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
            DossieCliente dossieCliente = this.dossieClienteServico.getById(id, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
            if (dossieCliente == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            @SuppressWarnings("unused")
			String cpfCnpj = "";
            if(dossieCliente.getTipoPessoa().equals(TipoPessoaEnum.F)) {
                    cpfCnpj = StringUtils.leftPad(String.valueOf(dossieCliente.getCpfCnpj()), 11, '0');
            }else {
                    cpfCnpj = StringUtils.leftPad(String.valueOf(dossieCliente.getCpfCnpj()), 14, '0');
            }
            //TODO sera redefinido o comportamento deste serviço e implementado conforme a nova definição. igor.oliveira
//	        return Response.ok(this.dossieClienteServico.enviaAvaliacaoRiscoSiric(cpfCnpj)).build();
            return Response.serverError().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gDDD"));
        } catch (Exception e) {
                return this.montaRespostaExcecao(e, this.PREFIXO.concat("gDDD"));
        }
    }

    /**
     * Realiza ajustes necessários aos dossiês de produto associados ao dossiê
     * de cliente, incluindo data/hora de finalização para aqueles que possuem
     * situação atual do tipo final e cria/sinaliza a instancia de BPM no caso
     * de situação atual que defina comunicação com o BPM
     *
     * @param dossieCliente Dossiê de cliente a ter os dossiês de produtos
     * vinculados ajustados
     */
    private void ajustaDossiesProdutoVinculados(DossieCliente dossieCliente) {

        dossieCliente.getDossiesClienteProduto().stream()
                .filter(dcp -> dcp.getDossieProduto().getDataHoraFinalizacao() == null)
                .forEach(dcp -> {
            
                    DossieProduto dossieProduto = dcp.getDossieProduto();

                    TipoSituacaoDossie tipoSituacaoRascunho = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.RASCUNHO);
                    TipoSituacaoDossie tipoSituacaoCriado = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.CRIADO);
                    SituacaoDossie situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream().max(Comparator.comparing(SituacaoDossie::getId)).get();

                    try{
                        // Caso o dossiê seja identificado em situação do tipo final e não tenha data hora de finalização, indica finalização da operação
                        if (situacaoAtualDossie.getTipoSituacaoDossie().getTipoFinal() && dossieProduto.getDataHoraFinalizacao() == null) {
                            this.dossieProdutoServico.finalizaDossieProduto(dossieProduto.getId());
                        }

                        //Realiza a notificação do BPM caso a situação tenha ficado bloqueada sobre uma situação não esperada.
                        boolean comunicouBPM = Boolean.FALSE;
                        if (tipoSituacaoCriado.equals(situacaoAtualDossie.getTipoSituacaoDossie()) && dossieProduto.getIdInstanciaProcessoBPM() == null) {
                            this.dossieProdutoServico.atribuiIntanciaBPM(dossieProduto.getId());
                        } else if (!tipoSituacaoRascunho.equals(situacaoAtualDossie.getTipoSituacaoDossie())) {
                            comunicouBPM = this.bpmServico.notificaBPM(dossieProduto);
                        } 
                        
                        // Comunicação com o jBPM bem sucedida
                        // Remove o valor existente definido para o controle de falha de comunicação associado ao dossiê de produto
                        if(comunicouBPM && dossieProduto.getDataHoraFalhaBPM() != null){
                            this.dossieProdutoServico.removeDataHoraFalhaBPM(dossieProduto.getId());
                        }
                        
                    }catch (EJBException e){
                        if (BpmException.class.equals(e.getCause().getClass())) {
                            this.dossieProdutoServico.atribuiDataHoraFalhaBPM(dossieProduto.getId());
                        }
                        
                        Object[] params = new Object[]{
                            dossieCliente.getTipoPessoa().name(),
                            String.valueOf(dossieCliente.getCpfCnpj()),
                            String.valueOf(dossieProduto.getId())
                        };
                        String mensagem = MessageFormat.format("DCR.sBPM.001 - Falha ao realizar comunicação com o BPM. CLIENTE {0}: {1} | DOSSIE PRODUTO: {2} ", params);
                        LOGGER.log(Level.ALL, mensagem, e);
                        LOGGER.log(Level.SEVERE, mensagem);
                    }        
                });
    }
}
