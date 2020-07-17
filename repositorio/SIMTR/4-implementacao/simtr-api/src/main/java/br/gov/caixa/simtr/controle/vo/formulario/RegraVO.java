package br.gov.caixa.simtr.controle.vo.formulario;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegraVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "tipo_regra", required = false)
    @JsonProperty("tipo_regra")
    private String tipoRegra;

    @XmlElement(name = "atributo", required = false)
    @JsonProperty("atributo")
    private String atributo;

    @XmlElement(name = "campo_resposta", required = false)
    @JsonProperty("campo_resposta")
    private String campoResposta;

    @XmlElement(name = "valor", required = false)
    @XmlElementWrapper(name = "valores")
    @JsonProperty("valores")
    private List<String> valores;

    public String getTipoRegra() {
        return tipoRegra;
    }

    public void setTipoRegra(String tipoRegra) {
        this.tipoRegra = tipoRegra;
    }

    public String getAtributo() {
        return atributo;
    }

    public void setAtributo(String atributo) {
        this.atributo = atributo;
    }

    public String getCampoResposta() {
        return campoResposta;
    }

    public void setCampoResposta(String campoResposta) {
        this.campoResposta = campoResposta;
    }

    public List<String> getValores() {
        return valores;
    }

    public void setValores(List<String> valores) {
        this.valores = valores;
    }

    @Override
    public String toString() {
        return "RegraVO [tipo_regra=" + tipoRegra + ", atributo=" + atributo + ", campo_resposta=" + campoResposta + ", valores=" + valores + "]";
    }

}
