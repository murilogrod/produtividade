	package br.gov.caixa.dossie.rs.retorno;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.dossie.rs.entity.PrincipalEntity;
/**
 * @author SIOGP
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PrincipalRetorno extends Retorno {
	@XmlElementWrapper(name="tipos")
	@XmlElement(name="tipo")
	private List<PrincipalEntity> data;
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public PrincipalRetorno() {
		data = new ArrayList<PrincipalEntity>();		
	}
	public List<PrincipalEntity> getData() {
		return data;
	}
	public void setData(List<PrincipalEntity> data) {
		this.data = data;
	}
}

