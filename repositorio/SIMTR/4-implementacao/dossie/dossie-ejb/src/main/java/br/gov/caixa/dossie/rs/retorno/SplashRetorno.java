	package br.gov.caixa.dossie.rs.retorno;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.dossie.rs.entity.SplashEntity;
/**
 * @author SIOGP
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SplashRetorno extends Retorno {
	@XmlElementWrapper(name="tipos")
	@XmlElement(name="tipo")
	private List<SplashEntity> data;
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public SplashRetorno() {
		data = new ArrayList<SplashEntity>();		
	}
	public List<SplashEntity> getData() {
		return data;
	}
	public void setData(List<SplashEntity> data) {
		this.data = data;
	}
}

