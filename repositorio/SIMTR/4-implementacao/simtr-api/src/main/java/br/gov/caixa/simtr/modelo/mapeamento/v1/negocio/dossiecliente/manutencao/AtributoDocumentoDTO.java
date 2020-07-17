package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.manutencao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieClienteManutencao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieClienteManutencao.XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
@ApiModel(
        value = ConstantesNegocioDossieClienteManutencao.API_MODEL_V1_ATRIBUTO_DOCUMENTO,
        description = "Objeto utilizado para representar um atributo com o valor extraido de um documento ou como dados de ajuste para atualização da informação perante o documento"
)
public class AtributoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.CHAVE)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.CHAVE, required = true, value = "Nome do atributo do documento")
    private String chave;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.VALOR)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.VALOR, required = true, value = "Valor do atributo do documento")
    private String valor;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.OBJETO)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.OBJETO, required = false, value = "array do atributo do documento")
    private String objeto;
    
    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.OPCAO_SELECIONADA)
    @XmlElementWrapper(name = ConstantesNegocioDossieClienteManutencao.OPCOES_SELECIONADAS)
    @JsonProperty(value = ConstantesNegocioDossieClienteManutencao.OPCOES_SELECIONADAS)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.OPCOES_SELECIONADAS, value = "Opções Selecionadas para o atributo do documento caso este seja de cunho objetivo", required = false)
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

    public AtributoDocumentoDTO(String chave, String valor, String array) {
        this();
        this.chave = chave;
        this.valor = valor;
        this.objeto = array;
    }

    public AtributoDocumentoDTO(AtributoDocumento atributoDocumento) {
        this();
        if(Objects.nonNull(atributoDocumento)) {
            this.chave = atributoDocumento.getDescricao();
            this.valor = atributoDocumento.getConteudo();
        }
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

	public String getObjeto() {
		return objeto;
	}

	public void setObjeto(String objeto) {
		this.objeto = objeto;
	}

}
