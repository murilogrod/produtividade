package br.gov.caixa.simtr.controle.vo.sifrc;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "documento")
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentoVO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "binario")
    private String binario;

    @XmlElement(name = "atributos")
    private AtributosVO atributos;

    public DocumentoVO() {
        this.atributos = new AtributosVO();
    }

    public String getBinario() {
        return binario;
    }

    public void setBinario(String binario) {
        this.binario = binario;
    }

    public AtributosVO getAtributos() {
        return atributos;
    }

    public void setAtributos(AtributosVO atributos) {
        this.atributos = atributos;
    }

    //**********************************
    public String getClasse() {
        return this.atributos.getClasse();
    }

    public void setClasse(String classe) {
        this.atributos.setClasse(classe);
    }

    public String getTipo() {
        return this.atributos.getTipo();
    }

    public void setTipo(String tipo) {
        this.atributos.setTipo(tipo);
    }

    public String getMimeType() {
        return this.atributos.getMimeType();
    }

    public void setMimeType(String mimeType) {
        this.atributos.setMimeType(mimeType);
    }

    public String getNome() {
        return this.atributos.getNome();
    }

    public void setNome(String nome) {
        this.atributos.setNome(nome);
    }

    public List<CampoVO> getCampos() {
        return this.getAtributos().getCampos();
    }

    public void setCampos(List<CampoVO> campos) {
        this.atributos.setCampos(campos);
    }

    public void addCamposDTO(final CampoVO... camposDTO) {
        this.atributos.addCamposDTO(camposDTO);
    }

}
