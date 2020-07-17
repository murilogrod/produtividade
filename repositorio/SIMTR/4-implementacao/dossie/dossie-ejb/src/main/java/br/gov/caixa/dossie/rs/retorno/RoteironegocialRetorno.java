	package br.gov.caixa.dossie.rs.retorno;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.dossie.rs.entity.RoteironegocialEntity;
/**
 * @author SIOGP
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RoteironegocialRetorno extends Retorno {
	@XmlElementWrapper(name="tipos")
	@XmlElement(name="tipo")
	private List<RoteironegocialEntity> data;
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public RoteironegocialRetorno() {
		data = new ArrayList<RoteironegocialEntity>();		
	}
	public List<RoteironegocialEntity> getData() {
		return data;
	}
	public void setData(List<RoteironegocialEntity> data) {
		this.data = data;
	}
}

