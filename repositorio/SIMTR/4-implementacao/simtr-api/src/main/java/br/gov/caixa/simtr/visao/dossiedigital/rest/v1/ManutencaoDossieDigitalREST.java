package br.gov.caixa.simtr.visao.dossiedigital.rest.v1;

import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.DocumentoServico;
import br.gov.caixa.simtr.controle.servico.DossieClienteServico;
import br.gov.caixa.simtr.controle.servico.DossieDigitalServico;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sun.jersey.core.header.ContentDisposition;

import br.gov.caixa.simtr.controle.servico.ManutencaoDossieDigitalServico;
import br.gov.caixa.simtr.controle.vo.DocumentoVO;
import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.AtributoDocumentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.operacao.AtributoExtraidoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.operacao.RetornoExtracaoDadosDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.operacao.SolicitacaoAtualizacaoCadastroDTO;

import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.operacao.SolicitacaoCorrecaoDadosDocumentoDTO;

import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.operacao.SolicitacaoExtracaoDadosDTO;

import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.operacao.SolicitacaoInclusaoCartaoAssinaturaDTO;

import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.operacao.SolicitacaoInclusaoDadosDeclaradosDTO;

import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.operacao.SolicitacaoMinutaDocumentoDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Deprecated
@Api(hidden = false, value = "Dossiê Digital")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("dossie-digital/v1/")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ManutencaoDossieDigitalREST extends AbstractREST {

    @EJB
    private DocumentoServico documentoServico;
    
    @EJB
    private DossieClienteServico dossieClienteServico;
    
    @EJB
    private DossieDigitalServico dossieDigitalServico;
    
    @EJB
    private ManutencaoDossieDigitalServico manutencaoDossieServico;

    private final String PREFIXO = "DOS.MDDR.V1.";

    @POST
    @Path("documento/extracao-dados")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Extração feita com sucesso", response = RetornoExtracaoDadosDTO.class)
        ,
        @ApiResponse(code = 400, message = "Erro na requisição")
        ,
	@ApiResponse(code = 503, message = "Erro de comunicação com algum sistema")
    })
    @ApiOperation(hidden = false, value = "Extração de dados da imagem do documento (OCR)", tags = {"Dossiê Digital - Remover_042020"})
    @Deprecated
    public Response extrairDadosImagem(final SolicitacaoExtracaoDadosDTO solicitacaoExtracaoDadosDTO) {
        RetornoExtracaoDadosDTO respostaExtracaoDadosDTO = new RetornoExtracaoDadosDTO();

        Long cpfCnpj = null;
        TipoPessoaEnum tipoPessoaEnum = TipoPessoaEnum.F;
        if (solicitacaoExtracaoDadosDTO.getCpfCliente() != null) {
            cpfCnpj = solicitacaoExtracaoDadosDTO.getCpfCliente();
            respostaExtracaoDadosDTO.setCpfCliente(cpfCnpj);
        } else if (solicitacaoExtracaoDadosDTO.getCnpjCliente() != null) {
            cpfCnpj = solicitacaoExtracaoDadosDTO.getCnpjCliente();
            tipoPessoaEnum = TipoPessoaEnum.J;
            respostaExtracaoDadosDTO.setCnpjCliente(cpfCnpj);
        }

        try {
            
         // Realiza captura do dossiê do cliente com base no CPF/CNPJ informado.
            DossieCliente dossieCliente = this.dossieClienteServico.getByCpfCnpj(cpfCnpj, tipoPessoaEnum, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            if (dossieCliente == null) {
                String mensagem = TipoPessoaEnum.F.equals(tipoPessoaEnum) ? "Dossiê do cliente não localizado para o CPF informado."
                                                                          : "Dossiê do cliente não localizado para o CNPJ informado.";
                throw new SimtrRequisicaoException(mensagem);
            }
            
            String binario = solicitacaoExtracaoDadosDTO.getImagens() == null ? null : solicitacaoExtracaoDadosDTO.getImagens().stream().findFirst().get();
            DocumentoVO documentoVo = this.dossieDigitalServico.extraiDadosDocumentoDossieDigital(dossieCliente.getId(), solicitacaoExtracaoDadosDTO.getFormato(), solicitacaoExtracaoDadosDTO.getTipoDocumento(), binario);
            Documento documento = documentoVo.getDocumentoSIMTR();
            respostaExtracaoDadosDTO.setFormato(solicitacaoExtracaoDadosDTO.getFormato());
            respostaExtracaoDadosDTO.setIdentificador(documento.getCodigoGED());

            if (solicitacaoExtracaoDadosDTO.getImagens() != null) {
                respostaExtracaoDadosDTO.setTamanhoDocumento(documento.getQuantidadeBytes());
            }

            respostaExtracaoDadosDTO.setLink(documentoVo.getLinkGED());

            Map<String, AtributoExtracao> mapaAtributos = documentoVo.getDocumentoSIMTR().getTipoDocumento().getAtributosExtracao()
                    .stream().collect(Collectors.toMap(ae -> ae.getNomeAtributoDocumento(), ae -> ae));

            documento.getAtributosDocumento().forEach(ad -> {
                AtributoExtraidoDTO atributoDTO = new AtributoExtraidoDTO(ad);
                AtributoExtracao atributoExtracao = mapaAtributos.get(ad.getDescricao());
                if (atributoExtracao.getPercentualAlteracaoPermitido() != null) {
                    atributoDTO.setPercentualAlteracaoPermitido(new BigDecimal(0L).add(new BigDecimal(atributoExtracao.getPercentualAlteracaoPermitido())));
                }
                respostaExtracaoDadosDTO.addAtributosDocumentoDTO(atributoDTO);
            });

            return Response.ok(respostaExtracaoDadosDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("eDI"));
        }
    }

    @POST
    @Path("documento/atualizacao-dados")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Atualização dos dados executada com sucesso")
        ,
	@ApiResponse(code = 400, message = "Erro na requisição")
        ,
	@ApiResponse(code = 503, message = "Erro de comunicação com algum sistema")
    })
    @ApiOperation(hidden = false, value = "Ajustes nos dados extraidos do documento.", tags = {"Dossiê Digital - Remover_042020"})
    @Deprecated
    public Response atualizacaoDados(final SolicitacaoCorrecaoDadosDocumentoDTO solicitacaoCorrecaoDadosDocumentoDTO) {

        Long cpfCnpj = null;
        TipoPessoaEnum tipoPessoaEnum = TipoPessoaEnum.F;
        if (solicitacaoCorrecaoDadosDocumentoDTO.getCpfCliente() != null) {
            cpfCnpj = solicitacaoCorrecaoDadosDocumentoDTO.getCpfCliente();
        } else if (solicitacaoCorrecaoDadosDocumentoDTO.getCnpjCliente() != null) {
            cpfCnpj = solicitacaoCorrecaoDadosDocumentoDTO.getCnpjCliente();
            tipoPessoaEnum = TipoPessoaEnum.J;
        }

        try {
            Map<String, String> mapaAtributosAlteracao = new HashMap<>();
            //Transforma os atributos a serem alterados em um Map para ser repassado a camada de serviço
            if (solicitacaoCorrecaoDadosDocumentoDTO.getDadosDocumento() != null) {
                solicitacaoCorrecaoDadosDocumentoDTO.getDadosDocumento()
                        .forEach(atributoDocumentoDTO -> mapaAtributosAlteracao.put(atributoDocumentoDTO.getChave(), atributoDocumentoDTO.getValor()));
            }
            
            // Realiza captura do dossiê do cliente com base no CPF/CNPJ informado.
            DossieCliente dossieCliente = this.dossieClienteServico.getByCpfCnpj(cpfCnpj, tipoPessoaEnum, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            if (dossieCliente == null) {
                String mensagem = TipoPessoaEnum.F.equals(tipoPessoaEnum) ? "Dossiê do cliente não localizado para o CPF informado."
                                                                          : "Dossiê do cliente não localizado para o CNPJ informado.";
                throw new SimtrRequisicaoException(mensagem);
            }

            // Realiza captura do documento com base no identificador informado.
            Documento documento = this.documentoServico.getByGedId(solicitacaoCorrecaoDadosDocumentoDTO.getCodigoIdentificadorDocumento(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            if (documento == null) {
                String mensagem = ConstantesUtil.MSG_FALHA_ID_NAO_LOCALIZADO_SIECM + solicitacaoCorrecaoDadosDocumentoDTO.getCodigoIdentificadorDocumento();
                throw new SimtrRequisicaoException(mensagem);
            }
            
            this.dossieDigitalServico.executaAtualizacaoDocumentoDossieDigital(dossieCliente.getId(), documento.getId(), mapaAtributosAlteracao);
            
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aD"));
        }

        return Response.noContent().build();
    }

    @POST
    @Path("documento/dados-declarados")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Dados declarados executado com sucesso")
        ,
	@ApiResponse(code = 400, message = "Erro na requisição")
        ,
	@ApiResponse(code = 503, message = "Erro de comunicação com algum sistema")
    })
    @ApiOperation(hidden = false, value = "Encaminhamento dos dados declarados emissão e guarda do documento.", tags = {"Dossiê Digital - Remover_042020"})
    @Deprecated
    public Response atualizaDadosDeclarados(final SolicitacaoInclusaoDadosDeclaradosDTO solicitacaoInclusaoDadosDeclaradosDTO) {

        Long cpfCnpj = null;
        TipoPessoaEnum tipoPessoaEnum = TipoPessoaEnum.F;
        if (solicitacaoInclusaoDadosDeclaradosDTO.getCpfCliente() != null) {
            cpfCnpj = solicitacaoInclusaoDadosDeclaradosDTO.getCpfCliente();
        } else if (solicitacaoInclusaoDadosDeclaradosDTO.getCnpjCliente() != null) {
            cpfCnpj = solicitacaoInclusaoDadosDeclaradosDTO.getCnpjCliente();
            tipoPessoaEnum = TipoPessoaEnum.J;
        }

        try {
         
            // Realiza captura do dossiê do cliente com base no CPF/CNPJ informado.
            DossieCliente dossieCliente = this.dossieClienteServico.getByCpfCnpj(cpfCnpj, tipoPessoaEnum, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            if (dossieCliente == null) {
                String mensagem = TipoPessoaEnum.F.equals(tipoPessoaEnum) ? "Dossiê do cliente não localizado para o CPF informado."
                                                                          : "Dossiê do cliente não localizado para o CNPJ informado.";
                throw new SimtrRequisicaoException(mensagem);
            }
            
            List<br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.AtributoDocumentoDTO> listaAtributosDeclarados = new ArrayList<>();
            solicitacaoInclusaoDadosDeclaradosDTO.getDadosDocumento().forEach(ad -> {
                br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.AtributoDocumentoDTO atributo = new br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.AtributoDocumentoDTO();
                atributo.setChave(ad.getChave());
                atributo.setValor(ad.getValor());
                listaAtributosDeclarados.add(atributo);
            });

            this.dossieDigitalServico.atualizaDadosDeclaradosDossieDigital(dossieCliente.getId(), listaAtributosDeclarados);

            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aDD"));
        }
    }

    @GET
    @Path("documento/dados-declarados/cpf/{cpf}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Dados declarados obtidos com sucesso", response = AtributoDocumentoDTO.class, responseContainer = "List")
        ,
	@ApiResponse(code = 400, message = "Erro na requisição")
        ,
	@ApiResponse(code = 503, message = "Erro de comunicação com algum sistema")
    })
    @ApiOperation(hidden = false, value = "Consulta dados declarados.", tags = {"Dossiê Digital - Remover_042020"})
    @Deprecated
    public Response consultarDadosDeclarados(@PathParam(value = "cpf") final Long cpf) {
        
        // Realiza captura do dossiê do cliente com base no CPF/CNPJ informado.
        DossieCliente dossieCliente = this.dossieClienteServico.getByCpfCnpj(cpf, TipoPessoaEnum.F, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (dossieCliente == null) {
            throw new SimtrRequisicaoException("Dossiê do cliente não localizado para o CPF informado.");
        }

        try {
            List<br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.AtributoDocumentoDTO> atributos = this.dossieDigitalServico.consultaDadosDeclaradosDossieDigital(dossieCliente.getId());
            return Response.ok(atributos).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("cDD"));
        }
    }

    @GET
    @Path("documento/cartao-assinatura/cpf/{cpf}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Cartão assinatura gerado com sucesso")
        ,
	@ApiResponse(code = 400, message = "Erro na requisição")
        ,
	@ApiResponse(code = 503, message = "Erro de comunicação com algum sistema")
    })
    @ApiOperation(hidden = false, value = "Geração do documento de cartão de assinatura digitalizado para coleta da assinatura do cliente.", tags = {"Dossiê Digital - Remover_042020"})
    @Produces({"application/pdf"})
    @Deprecated
    public Response gerarCartaoAssinatura(@PathParam(value = "cpf") final Long cpf) {

        try {
            
            // Realiza captura do dossiê do cliente com base no CPF/CNPJ informado.
            DossieCliente dossieCliente = this.dossieClienteServico.getByCpfCnpj(cpf, TipoPessoaEnum.F, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            if (dossieCliente == null) {
                throw new SimtrRequisicaoException("Dossiê do cliente não localizado para o CPF informado.");
            }

            byte[] relatorio = this.dossieDigitalServico.geraCartaoAssinatura(dossieCliente.getId());
            ContentDisposition contentDisposition = ContentDisposition.type("attachment").fileName("cartao_assinatura_".concat(cpf.toString()).concat(".pdf")).build();
            // Retorna a resposta.
            return Response.ok(relatorio, "application/pdf").header("Content-Disposition", contentDisposition).build();

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gCA"));
        }
    }

    @POST
    @Path("documento/cartao-assinatura")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Cartão assinatura armazenado com sucesso")
        ,
	@ApiResponse(code = 400, message = "Erro na requisição")
        ,
	@ApiResponse(code = 503, message = "Erro de comunicação com algum sistema")
    })
    @ApiOperation(hidden = false, value = "Encaminhamento do cartão de assinatura digitalizado para guarda do documento.", tags = {"Dossiê Digital - Remover_042020"})
    @Deprecated
    public Response cadastraCartaoAssinatura(final SolicitacaoInclusaoCartaoAssinaturaDTO solicitacaoInclusaoCartaoAssinaturaDTO) {

        Long cpfCnpj = null;
        TipoPessoaEnum tipoPessoaEnum = TipoPessoaEnum.F;
        if (solicitacaoInclusaoCartaoAssinaturaDTO.getCpfCliente() != null) {
            cpfCnpj = solicitacaoInclusaoCartaoAssinaturaDTO.getCpfCliente();
        } else if (solicitacaoInclusaoCartaoAssinaturaDTO.getCnpjCliente() != null) {
            cpfCnpj = solicitacaoInclusaoCartaoAssinaturaDTO.getCnpjCliente();
            tipoPessoaEnum = TipoPessoaEnum.J;
        }

        try {

            // Realiza captura do dossiê do cliente com base no CPF/CNPJ informado.
            DossieCliente dossieCliente = this.dossieClienteServico.getByCpfCnpj(cpfCnpj, tipoPessoaEnum, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            if (dossieCliente == null) {
                String mensagem = TipoPessoaEnum.F.equals(tipoPessoaEnum) ? "Dossiê do cliente não localizado para o CPF informado."
                                                                          : "Dossiê do cliente não localizado para o CNPJ informado.";
                throw new SimtrRequisicaoException(mensagem);
            }
            
            this.dossieDigitalServico.atualizaCartaoAssinatura(dossieCliente.getId(), solicitacaoInclusaoCartaoAssinaturaDTO.getFormato(), solicitacaoInclusaoCartaoAssinaturaDTO.getImagem());

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("cCA"));
        }

        return Response.noContent().build();
    }

    @POST
    @Path("cadastro/atualizacao")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Atualização realizada com sucesso")
        ,
	@ApiResponse(code = 400, message = "Erro na requisição")
        ,
        @ApiResponse(code = 503, message = "Erro de comunicação com algum sistema")
    })
    @ApiOperation(hidden = false, value = "Atualização dos dados cadastrais do cliente perante o SICLI com guarda permanente dos documentos.", tags = {"Dossiê Digital - Remover_042020"})
    @Deprecated
    public Response atualizaCadastroDocumentos(final SolicitacaoAtualizacaoCadastroDTO solicitacaoAtualizacaoCadastroDTO) {

        Long cpfCnpj = null;
        TipoPessoaEnum tipoPessoaEnum = TipoPessoaEnum.F;
        if (solicitacaoAtualizacaoCadastroDTO.getCpfCliente() != null) {
            cpfCnpj = solicitacaoAtualizacaoCadastroDTO.getCpfCliente();
        } else if (solicitacaoAtualizacaoCadastroDTO.getCnpjCliente() != null) {
            cpfCnpj = solicitacaoAtualizacaoCadastroDTO.getCnpjCliente();
            tipoPessoaEnum = TipoPessoaEnum.J;
        }

        try {
            // Realiza captura do dossiê do cliente com base no CPF/CNPJ informado.
            DossieCliente dossieCliente = this.dossieClienteServico.getByCpfCnpj(cpfCnpj, tipoPessoaEnum, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            if (dossieCliente == null) {
                String mensagem = TipoPessoaEnum.F.equals(tipoPessoaEnum) ? "Dossiê do cliente não localizado para o CPF informado."
                                                                          : "Dossiê do cliente não localizado para o CNPJ informado.";
                throw new SimtrRequisicaoException(mensagem);
            }        
        
            this.dossieDigitalServico.atualizaCadastroCaixaDossieCliente(dossieCliente.getId(), null, solicitacaoAtualizacaoCadastroDTO.getDocumentos());

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aCD"));
        }
        return Response.noContent().build();
    }

    @POST
    @Path("documento/minuta")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Minuta de documento emitida com sucesso")
        ,
	@ApiResponse(code = 400, message = "Erro na requisição")
        ,
	@ApiResponse(code = 503, message = "Erro de comunicação com algum sistema")
    })
    @ApiOperation(hidden = false, value = "Solicitação de emissão de minutas de documento para conferência/emissão por parte do solicitante.", tags = {"Dossiê Digital - Remover_042020"})
    @Deprecated
    public Response emissaoMinutaDocumento(final SolicitacaoMinutaDocumentoDTO solicitacaoMinutaDocumentoDTO) {

        try {
            Response response = null;
            switch (solicitacaoMinutaDocumentoDTO.getFormatoConteudoEnum()) {

                case PDF:
                    byte[] minuta = this.manutencaoDossieServico.geraPDFMinutaDocumento(solicitacaoMinutaDocumentoDTO.getTipoDocumento(), solicitacaoMinutaDocumentoDTO.getAtributosDocumento());
                    ContentDisposition contentDisposition = ContentDisposition.type("attachment").fileName("minuta.pdf").build();
                    response = Response.ok(minuta, "application/pdf").header("Content-Disposition", contentDisposition).build();
                    break;
                case BASE64:
                    String minutaBase64 = this.manutencaoDossieServico.geraMinutaBase64(solicitacaoMinutaDocumentoDTO.getTipoDocumento(), solicitacaoMinutaDocumentoDTO.getAtributosDocumento());
                    response = Response.ok(minutaBase64).build();
                    break;
                default:
                    RetornoErroDTO resourceError = new RetornoErroDTO();
                    resourceError.setMensagem("eMD Requisição Invalida.");
                    resourceError.setCodigoHTTP(Status.BAD_REQUEST.getStatusCode());
                    resourceError.setDetail(MessageFormat.format("Formato de emissão solicitado não disponível. Formato Solicitado: {0}", solicitacaoMinutaDocumentoDTO.getFormatoConteudoEnum()) + "Formatos válidos: " + FormatoConteudoEnum.PDF + ", " + FormatoConteudoEnum.BASE64);
                    response = Response.status(Status.BAD_REQUEST).entity(resourceError).build();
                    break;
            }

            return response;

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("eMD"));
        }
    }
}
