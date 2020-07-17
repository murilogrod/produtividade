	package br.gov.caixa.dossie.rs.retorno;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.dossie.rs.entity.PortfolioacessosEntity;
/**
 * @author SIOGP
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PortfolioacessosRetorno extends Retorno {
	@XmlElementWrapper(name="tipos")
	@XmlElement(name="tipo")
	private List<PortfolioacessosEntity> data;
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public PortfolioacessosRetorno() {
		data = new ArrayList<PortfolioacessosEntity>();		
	}
	public List<PortfolioacessosEntity> getData() {
		return data;
	}
	public void setData(List<PortfolioacessosEntity> data) {
		this.data = data;
	}
}

