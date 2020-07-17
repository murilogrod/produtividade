package br.gov.caixa.simtr.visao.negocio.rest.v1;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
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
import javax.ws.rs.core.UriInfo;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.simtr.controle.excecao.BpmException;
import br.gov.caixa.simtr.controle.excecao.SimtrDossieProdutoException;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.BPMServico;
import br.gov.caixa.simtr.controle.servico.DossieDigitalServico;
import br.gov.caixa.simtr.controle.servico.DossieProdutoServico;
import br.gov.caixa.simtr.controle.servico.ProcessoServico;
import br.gov.caixa.simtr.controle.servico.SituacaoDossieServico;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDossie;
import br.gov.caixa.simtr.modelo.entidade.TipoSituacaoDossie;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDossieEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.DossieProdutoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.complementacao.ProcessoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.DossieProdutoAlteracaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.DossieProdutoInclusaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao.PendenciaDossieProdutoDTO;
import br.gov.caixa.simtr.util.CalendarUtil;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.jaxrs.PATCH;

@Api(hidden = false, value = "Apoio ao Negócio - Dossiê Produto")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("negocio/v1")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class DossieProdutoREST extends AbstractREST {

    @EJB
    private BPMServico bpmServico;

    @EJB
    private CalendarUtil calendarUtil;

    @EJB
    private DossieProdutoServico dossieProdutoServico;

    @EJB
    private KeycloakService keycloakService;

    @EJB
    private ProcessoServico processoServico;

    @EJB
    private SituacaoDossieServico situacaoDossieServico;
    
    @EJB
    private DossieDigitalServico dossieDigitalServico;

    @EJB
    private AnalyticsServico analyticsServico;

    private static final String BASE_URL = "/negocio/v1/dossie-produto";

    private final String PREFIXO = "SDN.DPR.";

    @POST
    @Path("/dossie-produto/")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Dossiê de Produto criado com sucesso", response = DossieProdutoDTO.class)
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo do dossiê de produto.", response = PendenciaDossieProdutoDTO.class)
        ,
        @ApiResponse(code = 403, message = "Usuário não autorizado a criar o dossiê de produto", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Cadastra um novo Dossiê de Produto")
    public Response insereDossieProduto(DossieProdutoInclusaoDTO dossieProdutoInclusaoDTO, @Context UriInfo uriInfo,
            @Context HttpHeaders headers) throws URISyntaxException {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/", capturaEngineCliente(headers));
        
        DossieProduto dossieProduto = null;
        DossieProdutoDTO dossieProdutoDTO;
        try {
            Long id = this.dossieProdutoServico.novoDossieProduto(dossieProdutoInclusaoDTO);

            dossieProduto = this.dossieProdutoServico.getById(id, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);

            SituacaoDossie situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream().max(Comparator.comparing(SituacaoDossie::getId)).get();
            TipoSituacaoDossie tipoSituacaoCriado = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.CRIADO);
            if (tipoSituacaoCriado.equals(situacaoAtualDossie.getTipoSituacaoDossie())) {
                this.dossieProdutoServico.atribuiIntanciaBPM(id);
            }
            dossieProdutoDTO = new DossieProdutoDTO(dossieProduto, Boolean.FALSE, Boolean.TRUE);
            return Response.created(new URI(uriInfo.getPath() + "/" + dossieProduto.getId())).entity(dossieProdutoDTO).build();
            
        } catch (EJBException ee) {
            if (BpmException.class.equals(ee.getCause().getClass()) && dossieProduto != null) {
                this.dossieProdutoServico.atribuiDataHoraFalhaBPM(dossieProduto.getId());
                dossieProdutoDTO = new DossieProdutoDTO(dossieProduto, Boolean.FALSE, Boolean.FALSE);
                return Response.created(new URI(uriInfo.getPath() + "/" + dossieProduto.getId())).entity(dossieProdutoDTO).build();
                
            } else if (SimtrDossieProdutoException.class.equals(ee.getCause().getClass())) {
                SimtrDossieProdutoException dossieProdutoException = (SimtrDossieProdutoException) ee.getCause();

                PendenciaDossieProdutoDTO pendenciaDossieProdutoDTO = new PendenciaDossieProdutoDTO(
                        dossieProdutoException.getPendenciasVinculosPessoasDTO(),
                        dossieProdutoException.getPendenciasVinculosProdutosDTO(),
                        dossieProdutoException.getPendenciasProcessoDossieDTO(),
                        dossieProdutoException.getPendenciasProcessoFaseDTO(),
                        dossieProdutoException.getPendenciasGarantiasDTO());

                return Response.status(Response.Status.BAD_REQUEST).entity(pendenciaDossieProdutoDTO).build();
            } else {
                return this.montaRespostaExcecao(ee, this.PREFIXO.concat("iDP"));
            }
        }
    }

    @PATCH
    @Path("/dossie-produto/{id}")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Dossiê de Produto alterado com sucesso.")
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta do corpo do dossiê de produto.", response = RetornoErroDTO.class)
        ,
        @ApiResponse(code = 403, message = "Solicitante não autorizado a realizar a alteração.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Atualização de informações de um Dossiê de Produto")
    public Response atualizaDossieProduto(
            @ApiParam(name = "id", value = "Identificador do dossiê de produto a ser alterado")
            @PathParam("id") Long id,
            DossieProdutoAlteracaoDTO dossieProdutoAlteracaoDTO, @Context HttpHeaders headers
    ) throws URISyntaxException {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.PATCH.name(), BASE_URL + "/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            this.dossieProdutoServico.patchDossieProduto(id, dossieProdutoAlteracaoDTO);

            DossieProduto dossieProduto = this.dossieProdutoServico.getById(id, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);

            TipoSituacaoDossie situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream().max(Comparator.comparing(SituacaoDossie::getId)).get().getTipoSituacaoDossie();
            TipoSituacaoDossie tipoSituacaoDossieCriado = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.CRIADO);
            //Caso o dossiê tenha sido criado neste momento, ou seja, modificado de rascunho para criado, cria a instancia.
            boolean comunicouBPM = Boolean.FALSE;
            if (tipoSituacaoDossieCriado.equals(situacaoAtualDossie)) {
                this.dossieProdutoServico.atribuiIntanciaBPM(dossieProduto.getId());
            } else {
                comunicouBPM = this.bpmServico.notificaBPM(dossieProduto);
            }
            
            // Comunicação com o jBPM bem sucedida
            // Remove o valor existente definido para o controle de falha de comunicação associado ao dossiê de produto
            if(comunicouBPM && dossieProduto.getDataHoraFalhaBPM() != null){
                this.dossieProdutoServico.removeDataHoraFalhaBPM(dossieProduto.getId());
            }
            
            //Realiza nova consulta para garantir a ultima situação caso o jBPM tenha realizado alteração da situação do dossiê
            dossieProduto = this.dossieProdutoServico.getById(id, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);

            // Caso o dossiê seja identificado em situação do tipo final e não tenha data hora de finalização, indica finalização da operação
            situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream().max(Comparator.comparing(SituacaoDossie::getId)).get().getTipoSituacaoDossie();
            if (situacaoAtualDossie.getTipoFinal() && dossieProduto.getDataHoraFinalizacao() == null) {
                this.dossieProdutoServico.finalizaDossieProduto(dossieProduto.getId());
            }

            return Response.noContent().build();
        } catch (EJBException ee) {
            if (BpmException.class.equals(ee.getCause().getClass())) {
                this.dossieProdutoServico.atribuiDataHoraFalhaBPM(id);
            }
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aDP"));
        }
    }

    @GET
    @Path("/dossie-produto/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Retorna o dossiê produto solicitado.", response = DossieProdutoDTO.class)
        ,
        @ApiResponse(code = 204, message = "Dossiê produto solicitado não foi localizado com o ID informado.")
    })
    @ApiParam(name = "id", value = "Identificador do Dossiê de Produto a ser localizado")
    @ApiOperation(hidden = false, value = "Captura um Dossiê de Produto baseado no seu ID")
    public Response getDossieProduto(@PathParam("id") Long id, @Context HttpHeaders headers) {
        
        this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));

        DossieProduto dossieProduto = this.dossieProdutoServico.getById(id, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (dossieProduto == null) {
            return Response.noContent().build();
        }

        TipoSituacaoDossie tipoSituacaoCriado = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.CRIADO);
        SituacaoDossie situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream().max(Comparator.comparing(SituacaoDossie::getId)).get();

        try {
            //Realiza a criação da instância de processo BPM caso a situação tenha ficado como criada de forma não esperada.
            boolean comunicouBPM = Boolean.FALSE;
            if (tipoSituacaoCriado.equals(situacaoAtualDossie.getTipoSituacaoDossie()) && dossieProduto.getIdInstanciaProcessoBPM() == null) {
                this.dossieProdutoServico.atribuiIntanciaBPM(id);
            } else {
                // Realiza a notificação do BPM caso a situação tenha ficado bloqueada sobre uma situação não esperada.
                comunicouBPM = this.bpmServico.notificaBPM(dossieProduto);
            } 
            
            // Comunicação com o jBPM bem sucedida
            // Remove o valor existente definido para o controle de falha de comunicação associado ao dossiê de produto
            if(comunicouBPM && dossieProduto.getDataHoraFalhaBPM() != null){
                this.dossieProdutoServico.removeDataHoraFalhaBPM(dossieProduto.getId());
            }
        } catch (EJBException ee) {
            // Caso seja capturada uma exceção de comunicação com jBPM, atribui o controle de falha de comunicação
            if (BpmException.class.equals(ee.getCause().getClass())) {
                this.dossieProdutoServico.atribuiDataHoraFalhaBPM(dossieProduto.getId());
            }
        }

        //Captura novamente o dossiê, desta vez completo para incluir a data de finalização deste
        dossieProduto = this.dossieProdutoServico.getById(id, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);

        // Caso o dossiê seja identificado em situação do tipo final e não tenha data hora de finalização, indica finalização da operação
        situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream().max(Comparator.comparing(SituacaoDossie::getId)).get();
        if (situacaoAtualDossie.getTipoSituacaoDossie().getTipoFinal() && dossieProduto.getDataHoraFinalizacao() == null) {
            this.dossieProdutoServico.finalizaDossieProduto(dossieProduto.getId());
        }

        
        //Monta o objeto de retorno representando o dossiê de produto
        DossieProdutoDTO dossieProdutoDTO = new DossieProdutoDTO(dossieProduto, Boolean.FALSE);

        return Response.ok(dossieProdutoDTO).build();
    }

    @POST
    @Path("/dossie-produto/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Retorna o dossiê produto solicitado indicando a possibilidade ou não de alteração", response = DossieProdutoDTO.class)
        ,
        @ApiResponse(code = 404, message = "Dossiê produto solicitado não foi localizado com o ID informado.")
    })
    @ApiParam(name = "id", value = "Identificador do Dossiê de Produto a ser localizado")
    @ApiOperation(hidden = false, value = "Tenta capturar um Dossiê de Produto baseado no seu ID em modo de alteração.")
    public Response getDossieProdutoEmAlimentacao(@PathParam("id") Long id, @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        DossieProduto dossieProduto = this.dossieProdutoServico.getById(id, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (dossieProduto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        TipoSituacaoDossie tipoSituacaoCriado = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.CRIADO);
        SituacaoDossie situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream().max(Comparator.comparing(SituacaoDossie::getId)).get();

        try{
            //Realiza a criação da instância de processo BPM caso a situação tenha ficado como criada de forma não esperada.
            Boolean comunicouBPM = Boolean.FALSE;
            if (tipoSituacaoCriado.equals(situacaoAtualDossie.getTipoSituacaoDossie()) && dossieProduto.getIdInstanciaProcessoBPM() == null) {
                this.dossieProdutoServico.atribuiIntanciaBPM(id);
            } else {
                // Realiza a notificação do BPM caso a situação tenha ficado bloqueada sobre uma situação não esperada.
                comunicouBPM = this.bpmServico.notificaBPM(dossieProduto);
            }
            
            // Comunicação com o jBPM bem sucedida
            // Remove o valor existente definido para o controle de falha de comunicação associado ao dossiê de produto
            if(comunicouBPM && dossieProduto.getDataHoraFalhaBPM() != null){
                this.dossieProdutoServico.removeDataHoraFalhaBPM(dossieProduto.getId());
            }
            
        } catch (EJBException ee) {
            // Caso seja capturada uma exceção de comunicação com jBPM, atribui o controle de falha de comunicação
            if (BpmException.class.equals(ee.getCause().getClass())) {
                this.dossieProdutoServico.atribuiDataHoraFalhaBPM(dossieProduto.getId());
            }
        }

        // Realiza notificação da instância do BPM após captura/criação da instância
        dossieProduto = this.dossieProdutoServico.getById(id, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        
        // Caso o dossiê seja identificado em situação do tipo final e não tenha data hora de finalização, indica finalização da operação
        if (situacaoAtualDossie.getTipoSituacaoDossie().getTipoFinal() && dossieProduto.getDataHoraFinalizacao() == null) {
            this.dossieProdutoServico.finalizaDossieProduto(dossieProduto.getId());
        }
        
        // Identifica so o dossiê pode ser enviado em modo alteração
        boolean alteracao = this.determinaModoAlteracao(dossieProduto);
        
        //Monta o objeto de retorno representando o dossiê de produto
        DossieProdutoDTO dossieProdutoDTO = new DossieProdutoDTO(dossieProduto, alteracao);
        
        return Response.ok(dossieProdutoDTO).build();
    }

    @GET
    @Path("/dossie-produto/complementacao/processo")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Lista os processos definidos para unidade do usuário com a quantidade de dossiês na situação \"AGUARDANDO COMPLEMENTACAO\" inluindo os processos hierarquicamente vinculados.")
        ,
        @ApiResponse(code = 404, message = "Não foram localizados processos com carateristica contendo dossiês de produto em situação \"AGUARDANDO COMPLEMENTACAO\".")
    })
    @ApiOperation(hidden = false, value = "Lista os processos com o quantitativo de dossiês para complementação de acordo com o usuário solicitante.")
    public Response listProcessosComplementacao(@Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/complementacao/processo", capturaEngineCliente(headers));
        try {
            List<Processo> processosPatriarcaComplementacao = this.processoServico.listaProcessosPorSituacao(SituacaoDossieEnum.AGUARDANDO_COMPLEMENTACAO);
            if (processosPatriarcaComplementacao.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            List<ProcessoDTO> processosDTO = new ArrayList<>();
            processosPatriarcaComplementacao.forEach(processo -> processosDTO.add(new ProcessoDTO(processo)));
            return Response.ok(processosDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("lPC"));
        }
    }

    @POST
    @Path("/dossie-produto/complementacao/processo/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Documentos Dossiê de produto localizado para complementação.", response = DossieProdutoDTO.class)
        ,
        @ApiResponse(code = 400, message = "Falha ao processar a requisição por definição incorreta ou corpo da mensagem de solicitação.", response = RetornoErroDTO.class)
        ,
        @ApiResponse(code = 404, message = "Não há dossiê de produto para o processo indicado dispnivel para complementação.")
    })
    @ApiOperation(hidden = false, value = "Captura um dossiê de produto para complementação incluindo uma nova siuação \"Em Complementação\" vinculada ao mesmo.")
    public Response capturaDossieComplementacao(
            @ApiParam(name = "id", value = "Identificador do processo de vinculação do dossiê")
            @PathParam("id") Integer idProcesso, @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/complementacao/processo/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(idProcesso)));

        try {
            DossieProduto dossieProduto = this.dossieProdutoServico.capturaDossieComplementacao(idProcesso);
            if (dossieProduto == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            DossieProdutoDTO dossieProdutoDTO = new DossieProdutoDTO(dossieProduto, Boolean.TRUE);

            return Response.ok(dossieProdutoDTO).build();

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("cDC"));
        }
    }
    
    
    /**
     * Determina se o dossiê de produto pode ser retornado em modo de alteração
     * de acordo com as caracteristicas do seu ciclo de vida atual
     * @param dossieProduto Dossiê de Produto carregado a ser analisado
     * @return true se o dossiê puder ser retornado em modo de alteração
     */    
    private boolean determinaModoAlteracao(DossieProduto dossieProduto){
        
        // Inicializa variavel de controle com valor negativo por padrão
        boolean alteracao = Boolean.FALSE;
        
        // Captura os registros do tipo de situação da estrutura em cache para utilização na verificação
        TipoSituacaoDossie tipoSituacaoAguardandoAlimentacao = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.AGUARDANDO_ALIMENTACAO);
        TipoSituacaoDossie tipoSituacaoAguardandoTratamento = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.AGUARDANDO_TRATAMENTO);
        TipoSituacaoDossie tipoSituacaoEmAlimentacao = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.EM_ALIMENTACAO);
        TipoSituacaoDossie tipoSituacaoPendenteInformacao = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.PENDENTE_INFORMACAO);
        TipoSituacaoDossie tipoSituacaoRascunho = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.RASCUNHO);
        
        // Captura o registro que representa a situação atual do dossiê de produto em análise
        SituacaoDossie situacaoAtual = dossieProduto.getSituacoesDossie().stream().max(Comparator.comparing(SituacaoDossie::getId)).get();

        // Identifica se a unidade so usuário esta definida para manipulação do dossiÇe de produto no momento
        boolean isUnidadeTratamento = dossieProduto.getUnidadesTratamento().stream()
                .filter(ut -> ((ut.getUnidade().equals(this.keycloakService.getLotacaoFisica())) || (ut.getUnidade().equals(this.keycloakService.getLotacaoAdministrativa()))))
                .findAny().isPresent();
        
        // Identifica se a unidade so usuário é a undiade de criação da operação
        // boolean isUnidadeOrigem = dossieProduto.getUnidadeCriacao().equals(this.keycloakService.getLotacaoFisica()) || dossieProduto.getUnidadeCriacao().equals(this.keycloakService.getLotacaoAdministrativa());

        //Verifica se o dossiê encontra-se em alguma das três situações abaixo
        //Situação = Aguardando Tratamento com Unidade Origem ou Unidade de Tratamente definida para a unidade do usuário 
        //Situação = Aguardando Alimentação com Unidade de Tratamento definida para a unidade do usuário 
        //Situação = Pendente de Informação com Unidade de Tratamento definida para a unidade do usuário
        if (((tipoSituacaoAguardandoTratamento.equals(situacaoAtual.getTipoSituacaoDossie())) && (isUnidadeTratamento))
                || ((tipoSituacaoAguardandoAlimentacao.equals(situacaoAtual.getTipoSituacaoDossie())) && (isUnidadeTratamento))
                || ((tipoSituacaoPendenteInformacao.equals(situacaoAtual.getTipoSituacaoDossie())) && (isUnidadeTratamento))) {
            String mensagem = MessageFormat.format("CAPTURADO PARA ATUALIZAÇÃO DE DADOS POR {0} EM {1}", this.keycloakService.getMatricula(), this.calendarUtil.toString(Calendar.getInstance(), "dd/MM/yyyy HH:mm:ss"));
            this.situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoEmAlimentacao, mensagem, null);
            
            //Caso o dossiê seja capturado pela unidade origem que não esta previsto para tratamento no momento, inclui a mesma na vinculação
            //Isso pode ocorrer caso a unidade capture para cancelar a operação por exemplo
            if (!isUnidadeTratamento) {
                this.dossieProdutoServico.vinculaUnidadeTratamento(dossieProduto.getId(), this.keycloakService.getLotacaoAdministrativa());
            }
            alteracao = Boolean.TRUE;
        } else if (tipoSituacaoEmAlimentacao.equals(situacaoAtual.getTipoSituacaoDossie()) && (isUnidadeTratamento)) {
            alteracao = Boolean.TRUE;
        } else if (tipoSituacaoRascunho.equals(situacaoAtual.getTipoSituacaoDossie())) {
            alteracao = Boolean.TRUE;
        }
        
        return alteracao;
    }
    
    @POST
    @Path("/dossie-produto/{id}/cadastro-caixa")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Atualização realizada com sucesso"),
        @ApiResponse(code = 400, message = "Os documentos do cliente possuem dados incompletos, ou foram informados documentos não pertencentes do dossiê indicado", response = RetornoErroDTO.class),
        @ApiResponse(code = 428, message = "Não foram encontrados documentos aptos a executar atualização cadastral", response = RetornoErroDTO.class),
        @ApiResponse(code = 503, message = "Erro de comunicação com o sistema de Cadastro de clientes da CAIXA", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Atualização dos dados cadastrais do cliente o cadastro CAIXA com guarda permanente dos documentos.")
    public Response atualizaCadastroCaixaDossieDigitalById(
             @ApiParam("Identificador do dossiê de produto a ser utilizado como base para a operação de atualização cadastral")
             @PathParam("id") final Long id,
             @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "{id}/cadastro-caixa", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            this.dossieDigitalServico.atualizaCadastroCaixaDossieProduto(id);            
            return Response.noContent().build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aCCDDBI"));
        }
    }
}
