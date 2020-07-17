package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.documento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDocumento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDocumento.XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
@ApiModel(
        value = ConstantesNegocioDocumento.API_MODEL_V1_ATRIBUTO_DOCUMENTO,
        description = "Objeto utilizado para representar os atributos de um documento extraido ou como retorno ajustados para atualização."
)
public class AtributoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDocumento.CHAVE)
    @ApiModelProperty(name = ConstantesNegocioDocumento.CHAVE, value = "Nome do atributo do documento", required = true)
    private String chave;

    @XmlElement(name = ConstantesNegocioDocumento.VALOR)
    @ApiModelProperty(name = ConstantesNegocioDocumento.VALOR, value = "Valor do atributo do documento", required = true)
    private String valor;
    
    
    @XmlElement(name = ConstantesNegocioDocumento.OPCAO_SELECIONADA)
    @XmlElementWrapper(name = ConstantesNegocioDocumento.OPCOES_SELECIONADAS)
    @JsonProperty(value = ConstantesNegocioDocumento.OPCOES_SELECIONADAS)
    @ApiModelProperty(name = ConstantesNegocioDocumento.OPCOES_SELECIONADAS, value = "Opções Selecionadas do atributo do documento", required = false)
    private List<String> opcoesSelecionadas;

    public AtributoDocumentoDTO() {
        super();
        this.opcoesSelecionadas = new ArrayList<String>();
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
        this.opcoesSelecionadas =  Objects.nonNull(atributoDocumento.getOpcoesSelecionadas()) ? 
        	atributoDocumento.getOpcoesSelecionadas().stream().map(a -> a.getValorOpcao()).collect(Collectors.toList()) : new ArrayList<>();
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
