package br.gov.caixa.simtr.modelo.mapeamento.v1.pae.documento;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.enumerator.TipoCampoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesPAETipoDocumento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

@XmlRootElement(name = ConstantesPAETipoDocumento.XML_ROOT_ELEMENT_ATRIBUTO_EXTRACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesPAETipoDocumento.API_MODEL_ATRIBUTO_EXTRACAO,
        description = "Objeto utilizado para representar os atributos definidos para um tipo de documento sob a otica do PAE."
)
public class AtributoExtracaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesPAETipoDocumento.NOME_NEGOCIAL)
    @ApiModelProperty(name = ConstantesPAETipoDocumento.NOME_NEGOCIAL, required = true, value = "Valor a ser utilizado como label do formulario utilizado nos casos de captura manual.")
    private String nomeNegocial;

    @XmlElement(name = ConstantesPAETipoDocumento.NOME_INTEGRACAO)
    @ApiModelProperty(name = ConstantesPAETipoDocumento.NOME_INTEGRACAO, required = true, value = "Valor a ser utilizado como chave do atributo utilizado na integrações para identificação do campo perante o envio e recepção de informações do documento.")
    private String nomeIntegracao;

    @XmlElement(name = ConstantesPAETipoDocumento.TIPO_CAMPO)
    @ApiModelProperty(name = ConstantesPAETipoDocumento.TIPO_CAMPO, required = true, value = "Definição sobre a forma de apresentar o campo para captura da informação do documento.")
    private TipoCampoEnum tipoCampo;

    @XmlElement(name = ConstantesPAETipoDocumento.OBRIGATORIO)
    @ApiModelProperty(name = ConstantesPAETipoDocumento.OBRIGATORIO, required = true, value = "Indicativo de atributo obrigatorio ou não para captura.")
    private boolean obrigatorio;

    public AtributoExtracaoDTO() {
        super();
    }

    public AtributoExtracaoDTO(AtributoExtracao atributoExtracao) {
        super();
        if (Objects.nonNull(atributoExtracao)) {
            this.nomeNegocial = atributoExtracao.getNomeNegocial();
            this.nomeIntegracao = atributoExtracao.getNomeAtributoDocumento();
            this.tipoCampo = atributoExtracao.getTipoCampoEnum();
            this.obrigatorio = atributoExtracao.getObrigatorio();
        }
    }

    public String getNomeNegocial() {
        return nomeNegocial;
    }

    public void setNomeNegocial(String nomeNegocial) {
        this.nomeNegocial = nomeNegocial;
    }

    public String getNomeIntegracao() {
        return nomeIntegracao;
    }

    public void setNomeIntegracao(String nomeIntegracao) {
        this.nomeIntegracao = nomeIntegracao;
    }

    public TipoCampoEnum getTipoCampo() {
        return tipoCampo;
    }

    public void setTipoCampo(TipoCampoEnum tipoCampo) {
        this.tipoCampo = tipoCampo;
    }

    public boolean isObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

}
