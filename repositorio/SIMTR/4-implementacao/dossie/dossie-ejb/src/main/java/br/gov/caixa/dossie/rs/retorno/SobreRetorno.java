	package br.gov.caixa.dossie.rs.retorno;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.dossie.rs.entity.SobreEntity;
/**
 * @author SIOGP
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SobreRetorno extends Retorno {
	@XmlElementWrapper(name="tipos")
	@XmlElement(name="tipo")
	private List<SobreEntity> data;
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public SobreRetorno() {
		data = new ArrayList<SobreEntity>();		
	}
	public List<SobreEntity> getData() {
		return data;
	}
	public void setData(List<SobreEntity> data) {
		this.data = data;
	}
}

