package br.gov.caixa.simtr.visao.negocio.rest.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.ProcessoServico;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.processo.MacroProcessoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.processo.ProcessoDossieDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.processo.ProcessoFaseDTO;
import br.gov.caixa.simtr.util.CalendarUtil;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(hidden = false, value = "Apoio ao Negocio - Processo")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("negocio/v1/processo")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ProcessoREST extends AbstractREST {

    @EJB
    private CalendarUtil calendarUtil;

    @EJB
    private KeycloakService keycloakService;
    
    @EJB
    private ProcessoServico processoServico;
    
    @EJB
    private AnalyticsServico analyticsServico;
    
    private static final String BASE_URL = "/negocio/v1/processo";

    private final String PREFIXO = "SDN.PR.";

    @GET
    @Path("/patriarca")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Lista de todos os processos patriarcas contendo a hierarquia recursiva com os demais processos e as informações a ele vinculadas.", response = MacroProcessoDTO.class)
        ,
        @ApiResponse(code = 404, message = "Não foram localizados processos com carateristica de patriarca.")
    })
    @ApiOperation(hidden = false, value = "Lista os processos patriarcas com toda a hierarquia dos mesmos.")
    public Response listPatriarcas(@Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/patriarca", capturaEngineCliente(headers));
        try {
            List<Processo> processosPatriarcas = this.processoServico.listPatriarcas();
            if (processosPatriarcas.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            Map<String, Object> retorno = new HashMap<>();

            Integer lotacaoAdministrativa = this.keycloakService.getLotacaoAdministrativa() == null ? 9999 : this.keycloakService.getLotacaoAdministrativa();
            Integer lotacaoFisica = this.keycloakService.getLotacaoFisica() == null ? 9999 : this.keycloakService.getLotacaoFisica();
            
            List<MacroProcessoDTO> processosDTO = new ArrayList<>();
            processosPatriarcas.forEach(processoPatriarca -> processosDTO.add(new MacroProcessoDTO(processoPatriarca, lotacaoAdministrativa, lotacaoFisica)));

            retorno.put("atualizacao", this.calendarUtil.toString(processoServico.getDataHoraUltimaAlteracao(), "yyyy-MM-dd HH:mm:ss"));
            retorno.put("processos", processosDTO);
            return Response.ok(retorno).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("lP"));
        }
    }

    @GET
    @Path("/{id}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Processo identificado pelo codigo encaminhado contendo a hierarquia recursiva com os demais processos e as informações a ele vinculadas. <br>"
                + "A consulta pode retornar 3 opções de processo conforme o identificador solicitado. <br>"
                + "Caso seja um macro processo, o objeto retornado esta representado como definião padrão do retorno. <br>"
                + "Caso seja um processo gerador de dossiê trata-se do objeto representado sob o elemento processo_filho (2 nivel). <br>"
                + "Caso seja um processo fase trata-se do objeto representado sob o elemento processo_fillho.processo_filho (3 nivel). <br>"
                + "Caso necessario solicite esclarecimentos a equipe tecnica sobre a definição do objeto de retorno devido a problemas de recursividade.", response = MacroProcessoDTO.class)
        ,
        @ApiResponse(code = 404, message = "Processo não localizado pelo identificador encaminhado.")
    })
    @ApiOperation(hidden = false, value = "Captura o processo especificado com toda a hierarquia do mesmo.")
    @ApiParam(name = "id", value = "Identificador do processo desejado", required = true)
    public Response getProcesso(@PathParam("id") Integer id, @Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/{id}", capturaEngineCliente(headers), montarValores("id", String.valueOf(id)));
        try {
            Processo processo = this.processoServico.getById(id);
            if (processo == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            Map<String, Object> retorno = new HashMap<>();
            
            Integer lotacaoAdministrativa = this.keycloakService.getLotacaoAdministrativa() == null ? 9999 : this.keycloakService.getLotacaoAdministrativa();
            Integer lotacaoFisica = this.keycloakService.getLotacaoFisica() == null ? 9999 : this.keycloakService.getLotacaoFisica();

            retorno.put("atualizacao", this.calendarUtil.toString(processoServico.getDataHoraUltimaAlteracao(), "yyyy-MM-dd HH:mm:ss"));
            if ((Boolean.TRUE.equals(processo.getIndicadorGeracaoDossie()))) {
                retorno.put("processo", new ProcessoDossieDTO(processo, lotacaoAdministrativa, lotacaoFisica));
            } else {
                Processo processoPai = this.processoServico.getPaiById(id);
                if (Objects.nonNull(processoPai)) {
                    retorno.put("processo", new ProcessoFaseDTO(processo, processoPai));
                } else {
                    retorno.put("processo", new MacroProcessoDTO(processo, lotacaoAdministrativa, lotacaoFisica));
                }
            }

            return Response.ok(retorno).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gP"));
        }
    }
}
