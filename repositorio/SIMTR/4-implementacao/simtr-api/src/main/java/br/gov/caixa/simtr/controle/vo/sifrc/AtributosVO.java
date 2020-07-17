package br.gov.caixa.simtr.controle.vo.sifrc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "atributos")
@XmlAccessorType(XmlAccessType.FIELD)
public class AtributosVO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


    @XmlElement(name = "classe")
    private String classe;

    @XmlElement(name = "tipo")
    private String tipo;

    @XmlElement(name = "mimeType")
    private String mimeType;

    @XmlElement(name = "nome")
    private String nome;

    @XmlElement(name = "campo")
    private List<CampoVO> campos;

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<CampoVO> getCampos() {
        return campos;
    }

    public void setCampos(List<CampoVO> campos) {
        this.campos = campos;
    }

    public void addCamposDTO(final CampoVO... camposDTO) {
        if (this.campos == null) {
            this.campos = new ArrayList<>();
        }

        this.campos.addAll(Arrays.asList(camposDTO));
    }

}
