package br.gov.caixa.simtr.visao.dossiedigital.rest.v1;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.gov.caixa.simtr.controle.excecao.DossieAutorizacaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.AutorizacaoNegadaServico;
import br.gov.caixa.simtr.controle.servico.AutorizacaoServico;
import br.gov.caixa.simtr.controle.servico.DossieClienteServico;
import br.gov.caixa.simtr.controle.servico.DossieDigitalServico;
import br.gov.caixa.simtr.controle.servico.ManutencaoDossieDigitalServico;
import br.gov.caixa.simtr.controle.vo.autorizacao.AutorizacaoVO;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao.AnaliseRegraDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao.DocumentoUtilizadoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao.MensagemOrientacaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao.RetornoAutorizacaoConjuntaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao.RetornoAutorizacaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao.SolicitacaoAutorizacaoConjuntaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao.SolicitacaoAutorizacaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao.SolicitacaoConclusaoOperacaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v2.dossiedigital.AtributoDocumentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v2.dossiedigital.autorizacao.DocumentoConclusaoDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(hidden = false, value = "Dossiê Digital")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("dossie-digital/v1/autorizacao")
@RequestScoped
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class AutorizacaoREST extends AbstractREST {

    @EJB
    private AutorizacaoServico autorizacaoServico;

    @EJB
    private AutorizacaoNegadaServico autorizacaoNegadaServico;
    
    @EJB
    private DossieClienteServico dossieClienteServico;
    
    @EJB
    private DossieDigitalServico dossieDigitalServico;

    @EJB
    private ManutencaoDossieDigitalServico manutencaoDossieDigitalServico;

    private final String PREFIXO = "DOS.AR.V1.";

    @POST
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Solicitação processada com sucesso. Necessario avaliar se a autorização foi concedida.", response = RetornoAutorizacaoDTO.class)
        ,
	@ApiResponse(code = 400, message = "Falha na requisição encaminhada. Verificar ", response = RetornoAutorizacaoDTO.class)
        ,
	@ApiResponse(code = 500, message = "Erro de configuração em algum recurso interno do serviço.", response = RetornoAutorizacaoDTO.class)
        ,
	@ApiResponse(code = 503, message = "Erro de comunicação com algum sistema de integração.", response = RetornoAutorizacaoDTO.class)})
    @ApiOperation(hidden = false, value = "Solicitação de Autorização Documental", tags = {"Dossiê Digital - Remover_042020"})
    @Deprecated
    public Response solicitaAutorizacao(SolicitacaoAutorizacaoDTO solicitacaoAutorizacaoDTO) {
        try {
            Long cpfCnpj = null;
            TipoPessoaEnum tipoPessoaEnum = TipoPessoaEnum.F;
            if (solicitacaoAutorizacaoDTO.getCpfCliente() != null) {
                cpfCnpj = solicitacaoAutorizacaoDTO.getCpfCliente();
            } else if (solicitacaoAutorizacaoDTO.getCnpjCliente() != null) {
                cpfCnpj = solicitacaoAutorizacaoDTO.getCnpjCliente();
                tipoPessoaEnum = TipoPessoaEnum.J;
            }
            
            // Realiza captura do dossiê do cliente com base no CPF/CNPJ informado.
            DossieCliente dossieCliente = this.dossieClienteServico.getByCpfCnpj(cpfCnpj, tipoPessoaEnum, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            if (dossieCliente == null) {
                String mensagem = TipoPessoaEnum.F.equals(tipoPessoaEnum) ? "Dossiê do cliente não localizado para o CPF informado."
                                                                          : "Dossiê do cliente não localizado para o CNPJ informado.";
                throw new SimtrRequisicaoException(mensagem);
            }

            AutorizacaoVO autorizacaoVO = this.autorizacaoServico.getAutorizacao(dossieCliente.getId(), solicitacaoAutorizacaoDTO.getOperacao(), solicitacaoAutorizacaoDTO.getModalidade());

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
    @Path("/")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Solicitação processada com sucesso.", response = AnaliseRegraDTO.class)
        ,
	@ApiResponse(code = 400, message = "Falha na requisição encaminhada.")
        ,
	@ApiResponse(code = 500, message = "Erro de configuração em algum recurso interno do serviço.")
        ,
	@ApiResponse(code = 503, message = "Erro de comunicação com algum sistema de integração.")})
    @ApiOperation(hidden = false, value = "Verifica a disponibilidade de autorização para um produto indicando a lista de documentos que estão presentes/ausentes no dossiê do cliente de acordo com as possiveis coleções definidas.", tags = {"Dossiê Digital - Remover_042020"})
    @Deprecated
    public Response simulaAutorizacao(
            @ApiParam(name = "integracao", value = "Código de integração utilizado na identificação do canal de comunicação.")
            @HeaderParam("integracao") Long codigoIntegracao,
            @ApiParam(name = "cpf_cliente", value = "CPF do cliente caso o mesmo seja pessoa fisica. Deve ser informado se o cliente for PF. Zeros a esquerda não devem ser incluidos.", example = "11122233399")
            @QueryParam("cpf_cliente") Long cpfCliente,
            @ApiParam(name = "cnpj_cliente", value = "CNPJ do cliente pessoa fisica. Deve ser informado se o cliente for PJ. Zeros a esquerda não devem ser incluidos.", example = "11222333000099")
            @QueryParam("cnpj_cliente") Long cnpjCliente,
            @ApiParam(name = "operacao", required = true, value = "Número da operação do produto desejado.")
            @QueryParam("operacao") Integer operacao,
            @ApiParam(name = "modalidade", required = true, value = "Número da modalidade do produto desejado.")
            @QueryParam("modalidade") Integer modalidade) {
        try {
            Long cpfCnpj = null;
            TipoPessoaEnum tipoPessoaEnum = TipoPessoaEnum.F;
            if (cpfCliente != null) {
                cpfCnpj = cpfCliente;
            } else if (cnpjCliente != null) {
                cpfCnpj = cnpjCliente;
                tipoPessoaEnum = TipoPessoaEnum.J;
            }
            
            // Realiza captura do dossiê do cliente com base no CPF/CNPJ informado.
            DossieCliente dossieCliente = this.dossieClienteServico.getByCpfCnpj(cpfCnpj, tipoPessoaEnum, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            if (dossieCliente == null) {
                String mensagem = TipoPessoaEnum.F.equals(tipoPessoaEnum) ? "Dossiê do cliente não localizado para o CPF informado."
                                                                          : "Dossiê do cliente não localizado para o CNPJ informado.";
                throw new SimtrRequisicaoException(mensagem);
            }

            List<AnaliseRegraDTO> listaDocumentosAnalisados = this.autorizacaoServico.verificaDisponibilidadeAutorizacao(dossieCliente.getId(), operacao, modalidade);

            return Response.ok(listaDocumentosAnalisados).build();

        } catch (EJBException ee) {
            return montaRespostaExcecao(ee, PREFIXO.concat("sA"));
        }
    }

    @POST
    @Path("/conjunta")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Solicitação processada com sucesso.", response = RetornoAutorizacaoConjuntaDTO.class)
        ,
        @ApiResponse(code = 400, message = "Erro na requisição")
        ,
        @ApiResponse(code = 503, message = "Erro de comunicação com algum sistema")
    })
    @ApiOperation(hidden = false, value = "Solicitação de Autorização Conjunta.", tags = {"Dossiê Digital - Remover_042020"})
    @Deprecated
    public Response getAutorizacaoConjunta(SolicitacaoAutorizacaoConjuntaDTO req) {

        try {

            RetornoAutorizacaoConjuntaDTO retornoDTO = autorizacaoServico.setAutorizacaoConjunta(req.getAutorizacoes(), req.getOperacao(), req.getModalidade());

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
        @ApiResponse(code = 204, message = "Operação concluida e documentos armazenados com sucesso")
        ,
	@ApiResponse(code = 400, message = "Erro na requisição")
        ,
	@ApiResponse(code = 503, message = "Erro de comunicação com algum sistema")
    })
    @ApiOperation(hidden = false, value = "Solicitação conclusão da operação enviando os documentos da operação para guarda.", tags = {"Dossiê Digital - Remover_042020"})
    @Deprecated
    public Response conclusaoOperacao(
            @ApiParam(name = "autorizacao", value = "Código da autorização concedida vinculada a operação em conclusão")
            @PathParam("autorizacao") final Long codigoAutorizacao,
            final SolicitacaoConclusaoOperacaoDTO solicitacaoConclusaoOperacaoDTO) {

        try {

            List<DocumentoConclusaoDTO> documentosConclusao = solicitacaoConclusaoOperacaoDTO.getDocumentos().stream().map(documentoDTO -> {                   
                List<AtributoDocumentoDTO> atributos = documentoDTO.getAtributosDocumento().stream().map(ad -> {
                    AtributoDocumentoDTO atributo = new AtributoDocumentoDTO();
                    atributo.setChave(ad.getChave());
                    atributo.setValor(ad.getValor());
                    return atributo;
                }).collect(Collectors.toList());
                
                DocumentoConclusaoDTO documento = new DocumentoConclusaoDTO();
                documento.setConteudo(documentoDTO.getConteudo());
                documento.setMimetype(FormatoConteudoEnum.PDF.getMimeType());
                documento.setTipoDocumento(documentoDTO.getTipoDocumento());
                documento.setAtributosDocumento(atributos);
                
                return documento;
            }).collect(Collectors.toList());

            this.dossieDigitalServico.guardaDocumentoOperacaoDossieDigital(codigoAutorizacao, documentosConclusao);

            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("cO"));
        }
    }

    private Response montaRespostaAutorizacaoNegada(DossieAutorizacaoException dae) {

        //Monta o objeto de retorno da solicitação de autorização com base nas informações da exceção
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
        dae.getMensagensOrientacao().forEach(orientacao
                -> autorizacaoRetornoDTO.addMensagensOrientacoes(new MensagemOrientacaoDTO(orientacao[0], orientacao[1], orientacao[2]))
        );
        autorizacaoRetornoDTO.setResultadoPesquisa(dae.getResultadoPesquisaSIPES());

        //Registra a negativa de autorização na base de dados
        this.autorizacaoNegadaServico.createAutorizacaoNegada(dae.getCpfCnpj(), dae.getTipoPessoaEnum(), dae.getOperacaoSolicitada(), dae.getModalidadeSolicitada(), dae.getSistemaSolicitante(), dae.getMessage());

        autorizacaoRetornoDTO.setObservacao(dae.getLocalizedMessage());

        return Response.status(Status.OK).entity(autorizacaoRetornoDTO).build();
    }
}
