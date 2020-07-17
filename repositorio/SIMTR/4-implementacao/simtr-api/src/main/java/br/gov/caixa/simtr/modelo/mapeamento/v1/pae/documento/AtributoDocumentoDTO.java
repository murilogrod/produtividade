package br.gov.caixa.simtr.modelo.mapeamento.v1.pae.documento;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesPAE;

@XmlRootElement(name = ConstantesPAE.XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesPAE.API_MODEL_ATRIBUTO_DOCUMENTO,
        description = "Objeto utilizado para representar os atributos de um documento na otica do PAE."
)
public class AtributoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesPAE.CHAVE)
    @ApiModelProperty(name = ConstantesPAE.CHAVE, required = true, value = "Nome do atributo do documento")
    private String chave;

    @XmlElement(name = ConstantesPAE.VALOR)
    @ApiModelProperty(name = ConstantesPAE.VALOR, required = true, value = "Valor do atributo do documento")
    private String valor;

    public AtributoDocumentoDTO() {
        super();
    }

    public AtributoDocumentoDTO(String chave, String valor) {
        super();
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
}
