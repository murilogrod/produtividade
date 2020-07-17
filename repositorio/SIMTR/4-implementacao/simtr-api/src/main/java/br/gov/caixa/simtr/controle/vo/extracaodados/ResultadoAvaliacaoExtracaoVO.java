package br.gov.caixa.simtr.controle.vo.extracaodados;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "resultado-avaliacao-extracao")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResultadoAvaliacaoExtracaoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "tipo_documento", required = false)
    @JsonProperty("tipo_documento")
    private String tipoDocumento;

    @XmlElement(name = "mimetype", required = false)
    @JsonProperty("mimetype")
    private String mimetype;

    @XmlElement(name = "indice_avaliacao_autenticidade", required = false)
    @JsonProperty("indice_avaliacao_autenticidade")
    private BigDecimal indiceAvaliacaoAutenticidade;

    @XmlElement(name = "codigo_rejeicao", required = false)
    @JsonProperty("codigo_rejeicao")
    private String codigoRejeicao;

    @XmlElement(name = "binario", required = false)
    @JsonProperty("binario")
    private String binario;

    @XmlElement(name = "atributo")
    @XmlElementWrapper(name = "atibutos")
    @JsonProperty("atributos")
    private List<AtributoDocumentoVO> atributosDocumentoVO;

    public ResultadoAvaliacaoExtracaoVO() {
        super();
        this.atributosDocumentoVO = new ArrayList<>();
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public BigDecimal getIndiceAvaliacaoAutenticidade() {
        return indiceAvaliacaoAutenticidade;
    }

    public void setIndiceAvaliacaoAutenticidade(BigDecimal indiceAvaliacaoAutenticidade) {
        this.indiceAvaliacaoAutenticidade = indiceAvaliacaoAutenticidade;
    }

    public String getCodigoRejeicao() {
        return codigoRejeicao;
    }

    public void setCodigoRejeicao(String codigoRejeicao) {
        this.codigoRejeicao = codigoRejeicao;
    }

    public String getBinario() {
        return binario;
    }

    public void setBinario(String binario) {
        this.binario = binario;
    }

    public List<AtributoDocumentoVO> getAtributosDocumentoVO() {
        return atributosDocumentoVO;
    }

    public void setAtributosDocumentoVO(List<AtributoDocumentoVO> atributosDocumentoVO) {
        this.atributosDocumentoVO = atributosDocumentoVO;
    }

    //*************************************/
    public void addAtributosDocumentoVO(AtributoDocumentoVO... atributosDocumentoVO) {
        this.atributosDocumentoVO.addAll(Arrays.asList(atributosDocumentoVO));
    }
}
