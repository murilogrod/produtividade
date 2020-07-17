package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoManutencao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProdutoManutencao.XML_ROOT_ELEMENT_DOSSIE_PRODUTO_ALTERACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoManutencao.API_MODEL_V1_DOSSIE_PRODUTO_ALTERACAO,
        description = "Objeto utilizado para realizar a alteração de um Dossiê de Produto limitando as possibilidades de definição dos atributos"
)
public class DossieProdutoAlteracaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.CANCELAMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.CANCELAMENTO, required = true, value = "Indica se o dossiê deverá ser cancelado. Só terá efeito se o usuário da requisição pertencer a unidade de origem do dossiê de produto. Se informado será necessario informar uma justificativa")
    private boolean cancelamento;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.FINALIZACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.FINALIZACAO, required = true, value = "Indica se o dossiê deverá ter a etapa finalizada. Para dossiês em rascunho, gera a situação \"CRIADO\" e para os casos \"Em Alimentação\" gera situação \"Alimentação Finalizada\".")
    private boolean finalizacao;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.RETORNO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.RETORNO, required = true, value = "Indica se houve desistência da ação e o dossiê deverá retornar para situação anterior. Só pode ser utilizado se o dossiê estiver nas opções \"Em Alimentação\", ou \"Análise Segurança\".")
    private boolean retorno;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.JUSTIFICATIVA)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.JUSTIFICATIVA, required = false, value = "Utilizado para encaminhar a justificativa utilizada nas situações que são necessarias encaminhamento de tal informação, como por exemplo nos casos de envio de atributo \"cancelamento\" como verdadeiro.")
    private String justificativa;
    
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.ELEMENTOS_CONTEUDO)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.ELEMENTO_CONTEUDO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.ELEMENTOS_CONTEUDO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.ELEMENTOS_CONTEUDO, required = false, value = "Lista de elementos de conteudo carregados para atender definições de carga do processo definido")
    private List<ElementoConteudoDTO> elementosConteudoDTO;

    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.PRODUTOS_CONTRATADOS)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.PRODUTO_CONTRATADO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.PRODUTOS_CONTRATADOS)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.PRODUTOS_CONTRATADOS, required = false, value = "Lista de produtos contratados a serem vinculados ao dossiê de produto")
    private List<ProdutoContratadoAlteracaoDTO> produtosContratadosAlteracaoDTO;

    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.VINCULOS_PESSOAS)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.VINCULO_PESSOA)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.VINCULOS_PESSOAS)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.VINCULOS_PESSOAS, required = false, value = "Lista de dossiês de cliente a serem vinculados ao dossiê de produto com a respectiva identificação do tipo de vinculo.")
    private List<VinculoPessoaAlteracaoDTO> vinculosDossieClienteAlteracaoDTO;

    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.GARANTIAS_INFORMADAS)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.GARANTIA_INFORMADA)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.GARANTIAS_INFORMADAS)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.GARANTIAS_INFORMADAS, required = false, value = "Lista de garantias informadas para a incluidas vinculadas aos produtos ou para o dossiê em geral.")
    private List<GarantiaInformadaAlteracaoDTO> garantiasInformadasAlteracaoDTO;

    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.RESPOSTAS_FORMULARIO)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.RESPOSTA_FORMULARIO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.RESPOSTAS_FORMULARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.RESPOSTAS_FORMULARIO, required = false, value = "Lista de respostas de formulario submetidas ao dossiê de produto.")
    private List<RespostaFormularioDTO> respostasFormularioDTO;

    public DossieProdutoAlteracaoDTO() {
        super();
        this.elementosConteudoDTO = new ArrayList<>();
        this.produtosContratadosAlteracaoDTO = new ArrayList<>();
        this.vinculosDossieClienteAlteracaoDTO = new ArrayList<>();
        this.garantiasInformadasAlteracaoDTO = new ArrayList<>();
        this.respostasFormularioDTO = new ArrayList<>();
    }

    public boolean isCancelamento() {
        return cancelamento;
    }

    public void setCancelamento(boolean cancelamento) {
        this.cancelamento = cancelamento;
    }

    public boolean isFinalizacao() {
        return finalizacao;
    }

    public void setFinalizacao(boolean finalizacao) {
        this.finalizacao = finalizacao;
    }

    public boolean isRetorno() {
        return retorno;
    }

    public void setRetorno(boolean retorno) {
        this.retorno = retorno;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public List<ElementoConteudoDTO> getElementosConteudoDTO() {
        return elementosConteudoDTO;
    }

    public void setElementosConteudoDTO(List<ElementoConteudoDTO> elementosConteudoDTO) {
        this.elementosConteudoDTO = elementosConteudoDTO;
    }

    public List<ProdutoContratadoAlteracaoDTO> getProdutosContratadosAlteracaoDTO() {
        return produtosContratadosAlteracaoDTO;
    }

    public void setProdutosContratadosAlteracaoDTO(List<ProdutoContratadoAlteracaoDTO> produtosContratadosAlteracaoDTO) {
        this.produtosContratadosAlteracaoDTO = produtosContratadosAlteracaoDTO;
    }

    public List<VinculoPessoaAlteracaoDTO> getVinculosDossieClienteAlteracaoDTO() {
        return vinculosDossieClienteAlteracaoDTO;
    }

    public void setVinculosDossieClienteAlteracaoDTO(List<VinculoPessoaAlteracaoDTO> vinculosDossieClienteAlteracaoDTO) {
        this.vinculosDossieClienteAlteracaoDTO = vinculosDossieClienteAlteracaoDTO;
    }

    public List<GarantiaInformadaAlteracaoDTO> getGarantiasInformadasAlteracaoDTO() {
        return garantiasInformadasAlteracaoDTO;
    }

    public void setGarantiasInformadasAlteracaoDTO(List<GarantiaInformadaAlteracaoDTO> garantiasInformadasAlteracaoDTO) {
        this.garantiasInformadasAlteracaoDTO = garantiasInformadasAlteracaoDTO;
    }

    public List<RespostaFormularioDTO> getRespostasFormularioDTO() {
        return respostasFormularioDTO;
    }

    public void setRespostasFormularioDTO(List<RespostaFormularioDTO> respostasFormularioDTO) {
        this.respostasFormularioDTO = respostasFormularioDTO;
    }

}
