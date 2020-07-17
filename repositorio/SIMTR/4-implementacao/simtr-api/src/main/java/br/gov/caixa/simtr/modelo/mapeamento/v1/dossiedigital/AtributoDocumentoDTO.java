package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalOperacao;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalOperacao.XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
@ApiModel(
        value = ConstantesDossieDigitalOperacao.API_MODEL_ATRIBUTO_DOCUMENTO,
        description = "Objeto utilizado para representar os atributos de um documento ou como retorno ajustados para atualização."
)
public class AtributoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalOperacao.CHAVE)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.CHAVE, required = true, value = "Nome do atributo do documento")
    private String chave;

    @XmlElement(name = ConstantesDossieDigitalOperacao.VALOR)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.VALOR, required = true, value = "Valor do atributo do documento")
    private String valor;

    public AtributoDocumentoDTO() {
        super();
    }

    public AtributoDocumentoDTO(String chave, String valor) {
        this();
        this.chave = chave;
        this.valor = valor;
    }

    public AtributoDocumentoDTO(AtributoDocumento atributoDocumento) {
        this();
        if (atributoDocumento != null) {
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

    public AtributoDocumento prototype() {
        AtributoDocumento atributoDocumento = new AtributoDocumento();
        atributoDocumento.setDescricao(this.chave);
        atributoDocumento.setConteudo(this.valor);
        return atributoDocumento;
    }
}
