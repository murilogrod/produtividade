package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento;

import br.gov.caixa.simtr.modelo.entidade.OpcaoAtributo;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroOpcaoAtributo;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesCadastroOpcaoAtributo.XML_ROOT_ELEMENT_OPCAO_ATRIBUTO_NOVO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesCadastroOpcaoAtributo.API_MODEL_OPCAO_ATRIBUTO_NOVO,
        description = "Objeto utilizado para representar as opções de atributos de um documento extraido."
)
public class OpcaoAtributoNovoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesCadastroOpcaoAtributo.CHAVE)
    @ApiModelProperty(name = ConstantesCadastroOpcaoAtributo.CHAVE, required = true, value = "Identificador a ser utilizado na opção escolhida pelo usuário")
    private String chave;

    @XmlElement(name = ConstantesCadastroOpcaoAtributo.VALOR)
    @ApiModelProperty(name = ConstantesCadastroOpcaoAtributo.VALOR, required = true, value = "Descrição da opção a ser exibida na opção para o usuário")
    private String valor;

    public OpcaoAtributoNovoDTO() {
        super();
    }

    public OpcaoAtributoNovoDTO(String chave, String valor) {
        this();
        this.chave = chave;
        this.valor = valor;
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
    
    public OpcaoAtributo prototype() {
        OpcaoAtributo opcaoAtributo = new OpcaoAtributo();
        opcaoAtributo.setDescricaoOpcao(this.valor);
        opcaoAtributo.setValorOpcao(this.chave);
        return opcaoAtributo;
    }
}
