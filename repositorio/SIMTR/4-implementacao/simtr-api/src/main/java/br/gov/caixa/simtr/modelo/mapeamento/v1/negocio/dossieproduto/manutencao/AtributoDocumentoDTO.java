package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoManutencao;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlRootElement(name = ConstantesNegocioDossieProdutoManutencao.XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoManutencao.API_MODEL_V1_ATRIBUTO_DOCUMENTO,
        description = "Objeto utilizado para representar os atributos de um documento no momento de cadastro do dossiê de produto na ótica do Apoio ao Negócio."
)
public class AtributoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.CHAVE)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.CHAVE, required = true, value = "Nome do atributo do documento")
    private String chave;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.VALOR)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.VALOR, required = true, value = "Valor do atributo do documento")
    private String valor;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.OPCAO_SELECIONADA)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.OPCOES_SELECIONADAS)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.OPCOES_SELECIONADAS)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.OPCOES_SELECIONADAS, value = "Opções Selecionadas para o atributo do documento caso este seja de cunho objetivo", required = false)
    private Set<String> opcoesSelecionadas;

    public AtributoDocumentoDTO() {
        super();
        this.opcoesSelecionadas = new HashSet<>();
    }

    public AtributoDocumentoDTO(String chave, String valor) {
        this();
        this.chave = chave;
        this.valor = valor;
    }

    public AtributoDocumentoDTO(AtributoDocumento atributoDocumento) {
        this();
        this.chave = atributoDocumento.getDescricao();
        this.valor = atributoDocumento.getConteudo();
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Set<String> getOpcoesSelecionadas() {
        return opcoesSelecionadas;
    }

    public void setOpcoesSelecionadas(Set<String> opcoesSelecionadas) {
        this.opcoesSelecionadas = opcoesSelecionadas;
    }
}
