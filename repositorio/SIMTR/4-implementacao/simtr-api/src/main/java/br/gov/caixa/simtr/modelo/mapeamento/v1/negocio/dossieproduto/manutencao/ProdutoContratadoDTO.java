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

@XmlRootElement(name = ConstantesNegocioDossieProdutoManutencao.XML_ROOT_ELEMENT_PRODUTO_CONTRATADO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoManutencao.API_MODEL_V1_PRODUTO_CONTRATADO,
        description = "Objeto utilizado para realizar a inclusão dos produtos selecionados na contratação de um dossiê de produto inicial limitando as possibilidades de definição dos atributos."
)
public class ProdutoContratadoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.ID, required = true, value = "Identificador do produto selecionado para vinculação com o dossiê de produto. Caso não seja informado, será necessario informar em conjunto os atributos <codigo_operacao> e <codigo_modalidade>")
    private Integer idProduto;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.CODIGO_OPERACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.CODIGO_OPERACAO, required = true, value = "Número da operação corporativa do produto. Deve ser preenchido em conjunto com o <codigo_modalidade> caso o <id> não seja informado.")
    private Integer operacao;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.CODIGO_MODALIDADE)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.CODIGO_MODALIDADE, required = true, value = "Número da modalidade corporativa do produto. Deve ser preenchido em conjunto com o <codigo_operacao> caso o <id> não seja informado.")
    private Integer modalidade;

    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.ELEMENTOS_CONTEUDO)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.ELEMENTO_CONTEUDO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.ELEMENTOS_CONTEUDO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.ELEMENTOS_CONTEUDO, required = false, value = "Lista de elementos de conteudo carregados para atender definições do produto relacionado")
    private List<ElementoConteudoDTO> elementosConteudoDTO;
    
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.RESPOSTAS_FORMULARIO)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.RESPOSTA_FORMULARIO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.RESPOSTAS_FORMULARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.RESPOSTAS_FORMULARIO, required = false, value = "Lista de respostas de formulario submetidas ao dossiê de produto associadas ao produto contratado")
    private List<RespostaFormularioDTO> respostasFormularioDTO;

    public ProdutoContratadoDTO() {
        super();
        this.elementosConteudoDTO = new ArrayList<>();
        this.respostasFormularioDTO = new ArrayList<>();
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public Integer getOperacao() {
        return operacao;
    }

    public void setOperacao(Integer operacao) {
        this.operacao = operacao;
    }

    public Integer getModalidade() {
        return modalidade;
    }

    public void setModalidade(Integer modalidade) {
        this.modalidade = modalidade;
    }

    public List<ElementoConteudoDTO> getElementosConteudoDTO() {
        return elementosConteudoDTO;
    }

    public void setElementosConteudoDTO(List<ElementoConteudoDTO> elementosConteudoDTO) {
        this.elementosConteudoDTO = elementosConteudoDTO;
    }

    public List<RespostaFormularioDTO> getRespostasFormularioDTO() {
        return respostasFormularioDTO;
    }

    public void setRespostasFormularioDTO(List<RespostaFormularioDTO> respostasFormularioDTO) {
        this.respostasFormularioDTO = respostasFormularioDTO;
    }
}
