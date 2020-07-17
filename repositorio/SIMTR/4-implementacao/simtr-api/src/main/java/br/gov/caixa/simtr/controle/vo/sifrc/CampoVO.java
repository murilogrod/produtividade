package br.gov.caixa.simtr.controle.vo.sifrc;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "campo")
@XmlAccessorType(XmlAccessType.FIELD)
public class CampoVO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


    @XmlElement(name = "nome")
    private String nome;

    @XmlElement(name = "valor")
    private String valor;

    @XmlElement(name = "tipo")
    private String tipo;

    public CampoVO() {
    }

    /**
     * Responsável pela criação de novas instâncias desta classe.
     *
     * @param nome
     * @param valor
     * @param tipo
     *
     */
    public CampoVO(String nome, String valor, String tipo) {
        super();
        this.nome = nome;
        this.valor = valor;
        this.tipo = tipo;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return this.valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
