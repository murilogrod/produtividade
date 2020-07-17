package br.gov.caixa.simtr.visao.negocio.rest.v2;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.arquitetura.siiso.servico.CadastroReceitaPJServico;
import br.gov.caixa.simtr.controle.servico.AnalyticsServico;
import br.gov.caixa.simtr.controle.servico.BPMServico;
import br.gov.caixa.simtr.controle.servico.CadastroCaixaServico;
import br.gov.caixa.simtr.controle.servico.CadastroReceitaServico;
import br.gov.caixa.simtr.controle.servico.DocumentoServico;
import br.gov.caixa.simtr.controle.servico.DossieClienteServico2;
import br.gov.caixa.simtr.controle.servico.DossieProdutoServico;
import br.gov.caixa.simtr.controle.servico.LocalidadeServico;
import br.gov.caixa.simtr.controle.servico.NivelDocumentalServico;
import br.gov.caixa.simtr.controle.servico.ProcessoServico;
import br.gov.caixa.simtr.controle.servico.SituacaoDossieServico;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.OpcaoSelecionada;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.manutencao.DocumentoInclusaoDTO;
import br.gov.caixa.simtr.visao.rest.AbstractREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(hidden = false, value = "Apoio ao Negócio - Dossiê Cliente")
@ApiResponses({
    @ApiResponse(code = 500, message = "Falha não mapeada ao processar a requisição. A equipe técnica do SIMTR deve ser acionada.")})
@Path("negocio/v2/dossie-cliente")
@RequestScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class DossieClienteREST extends AbstractREST {

    @EJB
    private BPMServico bpmServico;

    @EJB
    private CadastroReceitaPJServico cadastroReceitaPJServico;

    @EJB
    private DossieClienteServico2 dossieClienteServico;

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
    
    private static final String BASE_URL = "/negocio/v2/dossie-cliente";

    private final String PREFIXO = "SDN.DCR.";
    
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
                    	atributoDocumento.setDeObjeto(atributoDTO.getObjeto());
                        //Caso exista opcoes selecionadas preenchidas no DTO enviado pelo front, deve se mapear as opcoes em OpcaoSelecionada do atributo documento.
                        if(Objects.nonNull(atributoDTO.getOpcoesSelecionadas()) && !atributoDTO.getOpcoesSelecionadas().isEmpty()) {
                            atributoDocumento.setOpcoesSelecionadas(atributoDTO.getOpcoesSelecionadas().stream().map(valorOpcao -> {
                                OpcaoSelecionada opcao = new OpcaoSelecionada();
                                opcao.setAtributoDocumento(atributoDocumento);
                                opcao.setDescricaoOpcao(atributoDTO.getChave());
                                opcao.setValorOpcao(valorOpcao);
                                return opcao;
                            }).collect(Collectors.toSet()));    
                        }
                        else {
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
}
