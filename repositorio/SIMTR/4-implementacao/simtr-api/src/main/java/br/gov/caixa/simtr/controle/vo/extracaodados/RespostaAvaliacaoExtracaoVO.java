package br.gov.caixa.simtr.controle.vo.extracaodados;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "resposta-avaliacao-extracao")
@XmlAccessorType(XmlAccessType.FIELD)
public class RespostaAvaliacaoExtracaoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "codigo_controle", required = true)
    @JsonProperty(value = "codigo_controle", required = true)
    private String codigoControle;

    public RespostaAvaliacaoExtracaoVO() {
        super();
    }

    public String getCodigoControle() {
        return codigoControle;
    }

    public void setCodigoControle(String codigoControle) {
        this.codigoControle = codigoControle;
    }

}
