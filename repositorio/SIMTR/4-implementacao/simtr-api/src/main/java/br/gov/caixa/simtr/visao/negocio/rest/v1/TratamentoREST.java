package br.gov.caixa.simtr.visao.negocio.rest.v1;

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

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.simtr.controle.excecao.BpmException;
import br.gov.caixa.simtr.controle.excecao.SimtrVerificacaoInvalidaException;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.BPMServico;
import br.gov.caixa.simtr.controle.servico.DossieProdutoServico;
import br.gov.caixa.simtr.controle.servico.ProcessoServico;
import br.gov.caixa.simtr.controle.servico.TratamentoServico;
import br.gov.caixa.simtr.controle.vo.checklist.VerificacaoVO;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDossieEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.tratamento.DossieProdutoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tratamento.ProcessoPatriarcaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tratamento.VerificacaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tratamento.apuracao.VerificacaoDesconsideradaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tratamento.apuracao.VerificacaoInvalidaDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(hidden = false, value = "Apoio ao Negócio - Tratamento")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("negocio/v1/tratamento")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class TratamentoREST extends AbstractREST {

    private final String PREFIXO = "NEG.TR";

    @EJB
    private DossieProdutoServico dossieProdutoServico;

    @EJB
    private BPMServico bpmServico;

    @EJB
    private ProcessoServico processoServico;

    @EJB
    private TratamentoServico tratamentoServico;
    
    @EJB
	private AnalyticsServico analyticsServico;
    
    private static final String BASE_URL = "/negocio/v1/tratamento";

    @GET
    @Path("/processo")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Lista os processos definidos para unidade do usuário com a quantidade de dossiês na situação \"AGUARDANDO TRATAMENTO\" inluindo os processos hierarquicamente vinculados.", response = ProcessoPatriarcaDTO.class, responseContainer = "List")
        ,
        @ApiResponse(code = 404, message = "Não foram localizados processos com carateristica contendo dossiês de produto em situação aguardando tratamento.")
    })
    @ApiOperation(hidden = false, value = "Lista os processos com o quantitativo de dossiês para tratamento de acordo com o usuário solicitante.")
    public Response listProcessosTratamento(@Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/processo", capturaEngineCliente(headers));
        try {
            List<Processo> processosPatriarcaTratamento = this.processoServico.listaProcessosPorSituacao(SituacaoDossieEnum.AGUARDANDO_TRATAMENTO);
            if (processosPatriarcaTratamento.isEmpty()) {
                return Response.status(Response.Status.NO_CONTENT).build();
            }
            List<ProcessoPatriarcaDTO> processosDTO = new ArrayList<>();
            processosPatriarcaTratamento.forEach(processo -> processosDTO.add(new ProcessoPatriarcaDTO(processo)));
            return Response.ok(processosDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("lPT"));
        }
    }

    @POST
    @Path("/processo/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Documentos Dossiê de produto localizado para tratamento.", response = DossieProdutoDTO.class)
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta ou corpo da mensagem de solicitação.", response = RetornoErroDTO.class)
        ,
        @ApiResponse(code = 404, message = "Não há dossiê de produto para o processo indicado dispnivel para tratamento.")
    })
    @ApiOperation(hidden = false, value = "Captura um dossiê de produto para tratamento incluindo uma nova siuação \"Em Tratamento\" vinculada ao mesmo.")
    public Response capturaDossieTratamento(
            @ApiParam(name = "id", value = "Identificador do processo de vinculação do dossiê")
            @PathParam("id") Integer id, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/processo/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));

        try {
            DossieProduto dossieProduto = this.tratamentoServico.capturaDossieTratamento(id);
            if (dossieProduto == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            DossieProdutoDTO dossieProdutoDTO = new DossieProdutoDTO(dossieProduto, Boolean.FALSE);

            return Response.ok(dossieProdutoDTO).build();

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("cDT"));
        }
    }

    @POST
    @Path("/dossie-produto/{id}/retorna-fila")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Dossiê devolvido pra fila de tratamento.")
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo do dossiê de produto.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Retorna um Dossiê que estava na situação \"Em Tratamento\" para a situação \"Aguardando Tratamento\"")
    public Response retornaFilaTratamento(
            @ApiParam(name = "id", value = "Identificador do dossiê de produto a ser retornado para a fila.")
            @PathParam("id") Long id, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/dossie-produto/{id}/retorna-fila", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            this.tratamentoServico.retornaFilaTratamento(id);

            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("rFT"));
        }
    }

    @POST
    @Path("/dossie-produto/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Verificação do dossiê aplicada com sucesso.", response = VerificacaoDesconsideradaDTO.class, responseContainer = "List")
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo do dossiê de produto.", response = VerificacaoInvalidaDTO.class, responseContainer = "List")
    })
    @ApiOperation(hidden = false, value = "Aplica a verificação dos checklists esperados para o dossiê de produto esperados na situação atual do dossiê.")
    public Response executaTratamento(
            @ApiParam(name = "id", value = "Identificador do dossiê de produto relacionado ao tratamento executado.")
            @PathParam("id") Long id,
            List<VerificacaoDTO> verificacoesDTO, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/dossie-produto/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            List<VerificacaoVO> listaVerificacoes = verificacoesDTO.stream().map(v -> v.prototype()).collect(Collectors.toList());

            List<VerificacaoVO> listaVerificacoesDesconsideradas = this.tratamentoServico.executaTratamento(id, listaVerificacoes);

            List<VerificacaoDesconsideradaDTO> verificacoesDesconsideradasDTO = new ArrayList<>();
            listaVerificacoesDesconsideradas.forEach(registro -> {
                verificacoesDesconsideradasDTO.add(new VerificacaoDesconsideradaDTO(registro.getIdentificadorChecklist(), registro.getIdentificadorInstanciaDocumento()));
            });

            DossieProduto dossieProduto = this.dossieProdutoServico.getById(id, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);

            try {
                this.bpmServico.notificaBPM(dossieProduto);
            }catch (EJBException ee){
                // Caso seja capturada uma exceção de comunicação com jBPM, atribui o controle de falha de comunicação
                if (BpmException.class.equals(ee.getCause().getClass())) {
                    this.dossieProdutoServico.atribuiDataHoraFalhaBPM(dossieProduto.getId());
                }
            }

            return Response.ok(verificacoesDesconsideradasDTO).build();
        } catch (EJBException ee) {
            if (SimtrVerificacaoInvalidaException.class.equals(ee.getCause().getClass())) {
                SimtrVerificacaoInvalidaException verificacaoException = (SimtrVerificacaoInvalidaException) ee.getCause();

                VerificacaoInvalidaDTO verificacaoInvalidaDTO = new VerificacaoInvalidaDTO(
                        verificacaoException.getListaIdentificadoresInstanciasDocumentoInvalidos(),
                        verificacaoException.getListaChecklistsFasePendentes(),
                        verificacaoException.getMapaVerificacoesInesperadasChecklistInstancia(),
                        verificacaoException.getMapaVerificacoesChecklistsReplicados(),
                        verificacaoException.getMapaVerificacoesInstanciasReplicados(),
                        verificacaoException.getMapaInstanciasChecklistsPendentes(),
                        verificacaoException.getListaChecklistsApontamentosPendentes());

                return Response.status(Response.Status.BAD_REQUEST).entity(verificacaoInvalidaDTO).build();
            } else {
                return this.montaRespostaExcecao(ee, this.PREFIXO.concat("eT"));
            }
        }
    }
    
    @POST
    @Path("/seletivo/dossie-produto/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Documentos Dossiê de produto localizado para tratamento.", response = DossieProdutoDTO.class)
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta ou corpo da mensagem de solicitação.", response = RetornoErroDTO.class)
        ,
        @ApiResponse(code = 403, message = "Este processo não permite realizar o tratamento seletivo. Utilize a opção de captura de dossiês para tratamento por fila. ", response = RetornoErroDTO.class)
        ,
        @ApiResponse(code = 404, message = "Não há dossiê de produto para o ID indicado.")
    })
    @ApiOperation(hidden = false, value = "Seleciona um dossiê de produto para tratamento incluindo uma nova siuação \"Em Tratamento\" vinculada ao mesmo.")
    public Response selecionaDossieProduto(
    		@ApiParam(name = "id", value = "Identificador do Dossiê de produto")
    		@PathParam("id") Long id, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/seletivo/dossie-produto/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));

        try {
            DossieProduto dossieProduto = this.tratamentoServico.selecionaDossieProduto(id);
            if (dossieProduto == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            dossieProduto = this.dossieProdutoServico.getById(dossieProduto.getId(), Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);

            DossieProdutoDTO dossieProdutoDTO = new DossieProdutoDTO(dossieProduto, Boolean.FALSE);
            return Response.ok(dossieProdutoDTO).build();
            
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("sDP"));
        }
    }
    
    @POST
    @Path("/dossie-produto/{id}/renovar-tempo")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Nova situação incluída no histórico do dossiê com sucesso.")
        ,
        @ApiResponse(code = 400, message = "Caso o dossiê de cliente não seja localizado, ou haja algum outro tipo de falha por parte do solicitante.", response = RetornoErroDTO.class)
        ,
        @ApiResponse(code = 403, message = "Caso usuário não possua permissão para renovar o tempo de sessão.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Adiciona nova Situação no dossiê de produto para renovar o tempo de tratamento.")
    public Response renovarTempo(
                @ApiParam(name = "id", value = "Identificador do Dossiê de produto") 
                @PathParam("id") Long id, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/dossie-produto/{id}/renovar-tempo", capturaEngineCliente(headers));
        try {
            
            this.tratamentoServico.renovarTempoTratamento(id);
            return Response.noContent().build();
            
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("rT"));
        }
    }
}
