package br.gov.caixa.simtr.visao.administracao.rest.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.simtr.controle.servico.AdministracaoServico;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.AvaliacaoExtracaoServico;
import br.gov.caixa.simtr.controle.servico.DossieProdutoServico;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.mapeamento.v1.administracao.FalhaBPMDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dashboard.DossieProdutoDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(hidden = true, value = "Administracao - Gestão da Plataforma")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("/administracao/v1")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON})
public class AdministracaoREST extends AbstractREST {

    private final String PREFIXO = "AGP.AR.";
    
    @EJB
    private AdministracaoServico adminitracaoServico;

    @EJB
    private AvaliacaoExtracaoServico avaliacaoExtracaoServico;
    
    @EJB
    private AnalyticsServico analyticsServico;
    
    @EJB
    private DossieProdutoServico dossieProdutoServico;

    private static final String BASE_URL = "/administracao/v1";

    @POST
    @Path("/dados")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Solicitacao executada com sucesso.")
    })
    @ApiOperation(hidden = false, value = "Executa consulta administrativa")
    @Consumes({
        MediaType.TEXT_PLAIN
    })
    @Produces({
        MediaType.APPLICATION_JSON
    })
    public Response executaConsultaAdministrativo(@ApiParam(value = "Indicativo de comando de consulta ou sensibilização de informação. ") @HeaderParam("consulta") @DefaultValue("true") boolean consulta, @ApiParam(value = "Indica o nivel do log a ser utilizado",
                                                                                                                                                                                                                      allowableValues = "OFF, SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, ALL",
                                                                                                                                                                                                                      defaultValue = "ALL") @HeaderParam("log") @DefaultValue("ALL") String level, String body, @Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/dados", capturaEngineCliente(headers));

        JSONArray json;
        Level logLevel = Level.parse(level.toUpperCase());
        if (consulta) {
            json = this.adminitracaoServico.executaConsultaAdministrativa(body, logLevel);
        } else {
            json = this.adminitracaoServico.executaComandoAdministrativo(body, logLevel);
        }

        return Response.ok(json.toString()).build();
    }

    @POST
    @Path("/outsourcing/token")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Token atualizado com sucesso junto ao serviço de Outsourcing Documental.")
    })
    @ApiOperation(hidden = false, value = "Força a atualização do token de comunicação junto ao serviço de Outsourcing Documental")
    public Response executaAtualizacaoTokenOutsourcing(@Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.POST.name(), BASE_URL + "/token/outsourcing", capturaEngineCliente(headers));

        this.avaliacaoExtracaoServico.executaAtualizacaoTokenOutsourcing();

        return Response.ok().build();
    }
    
    @GET
    @Path("/outsourcing/ping")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Comunicação realizada com sucesso junto ao serviço de Outsourcing Documental.")
    })
    @ApiOperation(hidden = false, value = "Verifica a disponibilidade de comunicação junto ao serviço de Outsourcing Documental")
    public Response executaPingOutsourcing(@Context HttpHeaders headers) {
        this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/ping/outsourcing", capturaEngineCliente(headers));

        return this.avaliacaoExtracaoServico.executaPingOutsourcing();
    }
    
    @GET
    @Path("/dossie-produto/falha-bpm")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Consulta realizada com sucesso junto ao serviço de Falha de Comunicação com o BPM.", response = FalhaBPMDTO.class),
        @ApiResponse(code = 404, message = "FalhaBPM não localizado.")
    })
    @ApiOperation(hidden = true, value = "Captura a lista de operações com falha de comunicação com o BPM")
    public Response getConsultaFalhaBPM(@Context HttpHeaders headers) {
	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/dossie-produto/falha-bpm", capturaEngineCliente(headers));
	try {
	    List<DossieProduto> dossieProduto = this.dossieProdutoServico.consultaFalhaBPM();
	    if (Objects.nonNull(dossieProduto)) {
		List<FalhaBPMDTO> listFalhaBPMTO = dossieProduto.stream().map(dp -> new FalhaBPMDTO(dp)).collect(Collectors.toList());
		return Response.ok(listFalhaBPMTO).build();
	    } else {
		return Response.status(Response.Status.NOT_FOUND).build();
	    }
	} catch (EJBException ee) {
	    return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gCFBPM"));
	}
    }
}
