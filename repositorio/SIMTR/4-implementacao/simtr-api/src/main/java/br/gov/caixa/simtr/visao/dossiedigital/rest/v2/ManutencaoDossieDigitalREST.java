package br.gov.caixa.simtr.visao.dossiedigital.rest.v2;

import java.util.ArrayList;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.DossieClienteServico;
import br.gov.caixa.simtr.controle.servico.DossieDigitalServico;
import br.gov.caixa.simtr.controle.servico.SiecmServico;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalOperacao;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.operacao.SolicitacaoInclusaoDadosDeclaradosDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(hidden = false, value = "Dossiê Digital")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")
})
@Path("dossie-digital/v2/")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ManutencaoDossieDigitalREST extends AbstractREST {

    @EJB
    private DossieClienteServico dossieClienteServico;
    
    @EJB
    private DossieDigitalServico dossieDigitalServico;
    
    @EJB
    private SiecmServico siecmServico;

    private final String PREFIXO = "DOS.MDDR.V2.";
    
    @GET
    @Path("documento/cartao-assinatura/cpf/{cpf}")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Cartão assinatura gerado com sucesso. Retorno no corpo da mensagem em formato Base64")
        ,
        @ApiResponse(code = 400, message = "Erro na requisição")
        ,
        @ApiResponse(code = 503, message = "Erro de comunicação com algum sistema")
    })
    @ApiOperation(hidden = false, value = "Geração do documento de cartão de assinatura digitalizado para coleta da assinatura do cliente.", tags = {"Dossiê Digital - Remover_042020"})
    @Produces({MediaType.TEXT_PLAIN})
    @Deprecated
    public Response gerarCartaoAssinatura(@PathParam(value = "cpf") final Long cpf) {

        try {

         // Realiza captura do dossiê do cliente com base no CPF/CNPJ informado.
            DossieCliente dossieCliente = this.dossieClienteServico.getByCpfCnpj(cpf, TipoPessoaEnum.F, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            if (dossieCliente == null) {
                throw new SimtrRequisicaoException("Dossiê do cliente não localizado para o CPF informado.");
            }

            byte[] relatorio = this.dossieDigitalServico.geraCartaoAssinatura(dossieCliente.getId());
            // Retorna a resposta.
            return Response.ok(Base64.getEncoder().encode(relatorio)).build();

        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("gCA"));
        }
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

            Documento documento = this.dossieDigitalServico.atualizaDadosDeclaradosDossieDigital(dossieCliente.getId(), listaAtributosDeclarados);

            Map<String, String> retorno= new HashMap<>();
            retorno.put(ConstantesDossieDigitalOperacao.IDENTIFICADOR, documento.getCodigoGED());
            return Response.ok(retorno).build();
        } catch (EJBException ee) {
            return this.montaRespostaExcecao(ee, this.PREFIXO.concat("aDD"));
        }
    }
}
