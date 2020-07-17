package br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.extracaodados;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.OpcaoSelecionada;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesRetaguardaExtracaoDados;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlRootElement(name = ConstantesRetaguardaExtracaoDados.XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesRetaguardaExtracaoDados.API_MODEL_ATRIBUTO_DOCUMENTO,
        description = "Objeto utilizado para representar os atributos extraídos de um documento."
)
public class AtributoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesRetaguardaExtracaoDados.CHAVE)
    @ApiModelProperty(name = ConstantesRetaguardaExtracaoDados.CHAVE, required = true, value = "Nome do atributo do documento")
    protected String chave;

    @XmlElement(name = ConstantesRetaguardaExtracaoDados.VALOR)
    @ApiModelProperty(name = ConstantesRetaguardaExtracaoDados.VALOR, required = true, value = "Valor do atributo extraído do documento")
    protected String valor;

    @XmlElement(name = ConstantesRetaguardaExtracaoDados.OPCAO_SELECIONADA)
    @XmlElementWrapper(name = ConstantesRetaguardaExtracaoDados.OPCOES_SELECIONADAS)
    @JsonProperty(value = ConstantesRetaguardaExtracaoDados.OPCOES_SELECIONADAS)
    @ApiModelProperty(name = ConstantesRetaguardaExtracaoDados.OPCOES_SELECIONADAS, required = true, value = "Indica as opções selecionadas pelo usuário nos casos de campos multi valorados")
    protected List<String> opcoesSelecionadas;

    public AtributoDocumentoDTO() {
        super();
        this.opcoesSelecionadas = new ArrayList<>();
    }

    public AtributoDocumentoDTO(String chave, String valor) {
        this();
        this.chave = chave;
        this.valor = valor;
    }
  
    public AtributoDocumentoDTO(String chave, String valor, Collection<OpcaoSelecionada> opcoesSelecionadas) {
        this();
        this.chave = chave;
        this.valor = valor;

        if (opcoesSelecionadas != null) {
            this.opcoesSelecionadas = opcoesSelecionadas.stream().map(os -> os.getValorOpcao()).collect(Collectors.toList());
        } else {
            this.opcoesSelecionadas = null;
        }
    }

    public AtributoDocumentoDTO(AtributoDocumento atributoDocumento) {
        this(atributoDocumento.getDescricao(), atributoDocumento.getConteudo(), atributoDocumento.getOpcoesSelecionadas());
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

    public List<String> getOpcoesSelecionadas() {
        return opcoesSelecionadas;
    }

    public void setOpcoesSelecionadas(List<String> opcoesSelecionadas) {
        this.opcoesSelecionadas = opcoesSelecionadas;
    }

}
