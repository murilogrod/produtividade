package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroOpcaoAtributo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesCadastroOpcaoAtributo.XML_ROOT_ELEMENT_OPCAO_ATRIBUTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesCadastroOpcaoAtributo.API_MODEL_OPCAO_ATRIBUTO_DADOS_DECLARADOS,
        description = "Objeto utilizado para representar os atributos de um documento extraido ou como retorno ajustados para atualização."
)
public class OpcaoAtributoDadosDeclaradosDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesCadastroOpcaoAtributo.CHAVE)
    @ApiModelProperty(name = ConstantesCadastroOpcaoAtributo.CHAVE, required = true, value = "Identificador a ser utilizado na opção escolhida pelo usuário")
    private String chave;

    @XmlElement(name = ConstantesCadastroOpcaoAtributo.VALOR)
    @ApiModelProperty(name = ConstantesCadastroOpcaoAtributo.VALOR, required = true, value = "Descrição da opção a ser exibida na opção para o usuário")
    private String valor;
    
    @XmlElement(name = ConstantesCadastroOpcaoAtributo.ATIVO)
    @ApiModelProperty(name = ConstantesCadastroOpcaoAtributo.ATIVO, required = true, value = "Indica se a opção esta disponivel para seleção ou se não deve mais permitir sua seleção")
    private boolean ativo;

    public OpcaoAtributoDadosDeclaradosDTO() {
        super();
    }
    
    public OpcaoAtributoDadosDeclaradosDTO(String chave, String valor) {
        this();
        this.chave = chave;
        this.valor = valor;
        this.ativo = Boolean.TRUE;
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

    public boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
