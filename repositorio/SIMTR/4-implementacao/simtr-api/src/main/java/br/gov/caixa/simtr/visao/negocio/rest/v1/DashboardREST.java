package br.gov.caixa.simtr.visao.negocio.rest.v1;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.DossieProdutoServico;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDossie;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDossieEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dashboard.DossieProdutoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dashboard.RetornoPessoalVisaoUnidadeDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Comparator;

@Api(hidden = false, value = "Apoio ao Negócio - Dashboard")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("negocio/v1/dashboard")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class DashboardREST extends AbstractREST {

    @EJB
    private DossieProdutoServico dossieProdutoServico;
    
    @EJB
	private AnalyticsServico analyticsServico;
    
    private static final String BASE_URL = "/negocio/v1/dashboard";

    private final String PREFIXO = "SDN.DBR.";
    

    @GET
    @Path("/pessoal/visao-unidade/")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Sucesso na obtenção das informações.", response = RetornoPessoalVisaoUnidadeDTO.class)
        ,
        @ApiResponse(code = 403, message = "Usuário não tem permissao para acessar a unidade indicada.", response = RetornoErroDTO.class)
    })
    @ApiOperation(hidden = false, value = "Retorna as informações utilizadas na montagem do dashboard da usuário sob a visão das informações de sua unidade.")
    public Response capturaInformacoesUnidadeUsuario(
    		@ApiParam(name = "cgc", value = "CGC da unidade desejada para obtenção dos dados do Dashboard. Caso não enviado será adotada a lotação fisica e administrativa do solicitante.", example = "5402")
            @QueryParam(value = "cgc") Integer cgc,
            @ApiParam(name = "vinculadas", value = "Indica se a captura deve contemplar as informações das unidades vinculadas ou não.")
            @DefaultValue(value = "false") 
    		@QueryParam(value = "vinculadas") Boolean vinculadas,
    		@Context HttpHeaders headers) {
    	this.analyticsServico.registraEvento(EnumMetodoHTTP.GET.name(), BASE_URL + "/pessoal/visao-unidade/", capturaEngineCliente(headers),
    			montarValores("cgc", String.valueOf(cgc), "vinculadas", String.valueOf(vinculadas)));
        RetornoPessoalVisaoUnidadeDTO retornoPessoalVisaoUnidadeDTO = new RetornoPessoalVisaoUnidadeDTO();
        try {
            List<DossieProduto> dossiesUnidadeUsuario = this.dossieProdutoServico.listByUnidade(cgc, vinculadas);
            
            boolean pendenteBPM = dossiesUnidadeUsuario.stream()
                                                .anyMatch(d -> {
                                                    String situacaoAtual = d.getSituacoesDossie().stream()
                                                            .max(Comparator.comparing(SituacaoDossie::getId))
                                                            .get()
                                                            .getTipoSituacaoDossie()
                                                            .getNome();
                                                    return SituacaoDossieEnum.getByDescricao(situacaoAtual).isSinalizaBPM();
                                                });
            
            //Caso seja identificados dossiês com situação prevista para comunicação com BPM, 
            //realiza nova captura no intuito de checar se a listagem sinalizou as instancias corretamente e modificou a situação
            if(pendenteBPM){
                dossiesUnidadeUsuario = this.dossieProdutoServico.listByUnidade(cgc, vinculadas);
            }
            dossiesUnidadeUsuario.forEach(dp -> {
                DossieProdutoDTO dossieProdutoDTO = new DossieProdutoDTO(dp);
                retornoPessoalVisaoUnidadeDTO.addBySituacao(dossieProdutoDTO.getSituacaoAtual());
                retornoPessoalVisaoUnidadeDTO.addDossieProdutoDTO(dossieProdutoDTO);
            });

            return Response.ok(retornoPessoalVisaoUnidadeDTO).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("cIUU"));
        }
    }

}
