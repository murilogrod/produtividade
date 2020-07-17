package br.gov.caixa.dossie.rs.retorno;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 
 * @author CTMONSI
 * @since 31/08/2017
 * @version 1.0
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DataRetorno {
	@XmlElement
	private boolean temErro;
	
	@XmlElement
	private List<String> msgsErro;
	
	@XmlElement
	private String data;

	public boolean isTemErro() {
		return temErro;
	}

	public void setTemErro(final boolean temErro) {
		this.temErro = temErro;
	}

	public List<String> getMsgsErro() {
		return msgsErro;
	}

	public void setMsgsErro(final List<String> msgsErro) {
		this.msgsErro = msgsErro;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}		
}