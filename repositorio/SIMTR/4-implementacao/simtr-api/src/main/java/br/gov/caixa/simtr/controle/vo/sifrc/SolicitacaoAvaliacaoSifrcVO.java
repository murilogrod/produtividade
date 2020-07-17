package br.gov.caixa.simtr.controle.vo.sifrc;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.enumerator.SIFRCOperacaoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.SIFRCOperacaoAdapter;

@XmlRootElement(name = "dados")
@XmlAccessorType(XmlAccessType.FIELD)
public class SolicitacaoAvaliacaoSifrcVO implements Serializable {

    /**
     * Atributo serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "identificador_cliente")
    private String identificadorCliente;

    @XmlElement(name = "tipo_operacao")
    @XmlJavaTypeAdapter(value = SIFRCOperacaoAdapter.class)
    private SIFRCOperacaoEnum sifrcOperacaoEnum;

    @XmlElement(name = "documento")
    @JsonProperty("documento")
    private DocumentoVO documento;

    public String getIdentificadorCliente() {
        return identificadorCliente;
    }

    public void setIdentificadorCliente(String identificadorCliente) {
        this.identificadorCliente = identificadorCliente;
    }

    public SIFRCOperacaoEnum getSifrcOperacaoEnum() {
        return sifrcOperacaoEnum;
    }

    public void setSifrcOperacaoEnum(SIFRCOperacaoEnum sifrcOperacaoEnum) {
        this.sifrcOperacaoEnum = sifrcOperacaoEnum;
    }

    public DocumentoVO getDocumento() {
        return documento;
    }

    public void setDocumento(DocumentoVO documento) {
        this.documento = documento;
    }

}
