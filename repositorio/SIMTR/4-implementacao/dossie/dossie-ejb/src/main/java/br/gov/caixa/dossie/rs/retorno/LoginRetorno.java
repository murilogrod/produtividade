	package br.gov.caixa.dossie.rs.retorno;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.dossie.rs.entity.LoginEntity;
/**
 * @author SIOGP
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginRetorno extends Retorno {
	@XmlElementWrapper(name="tipos")
	@XmlElement(name="tipo")
	private List<LoginEntity> data;
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public LoginRetorno() {
		data = new ArrayList<LoginEntity>();		
	}
	public List<LoginEntity> getData() {
		return data;
	}
	public void setData(List<LoginEntity> data) {
		this.data = data;
	}
}

