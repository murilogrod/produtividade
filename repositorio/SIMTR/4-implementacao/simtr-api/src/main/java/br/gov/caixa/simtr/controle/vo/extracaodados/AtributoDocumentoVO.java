package br.gov.caixa.simtr.controle.vo.extracaodados;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AtributoDocumentoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "chave", required = false)
    @JsonProperty("chave")
    private String chave;
    
    @XmlElement(name = "valor", required = false)
    @JsonProperty("valor")
    private String valor;
    
    @XmlElement(name = "indice_assertividade", required = false)
    @JsonProperty("indice_assertividade")
    @JsonInclude(value = Include.NON_EMPTY)
    private BigDecimal indiceAssertividade;

    public AtributoDocumentoVO() {
    }

    public AtributoDocumentoVO(String chave, String valor) {
        super();
        this.chave = chave;
        this.valor = valor;
    }

    public AtributoDocumentoVO(String chave, String valor, BigDecimal indiceAssertividade) {
        this(chave, valor);
        this.indiceAssertividade = indiceAssertividade;
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

    public BigDecimal getIndiceAssertividade() {
        return indiceAssertividade;
    }

    public void setIndiceAssertividade(BigDecimal indiceAssertividade) {
        this.indiceAssertividade = indiceAssertividade;
    }
}
