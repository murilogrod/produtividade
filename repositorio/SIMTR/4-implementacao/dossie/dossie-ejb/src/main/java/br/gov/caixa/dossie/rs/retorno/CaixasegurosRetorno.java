	package br.gov.caixa.dossie.rs.retorno;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.dossie.rs.entity.CaixasegurosEntity;
/**
 * @author SIOGP
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CaixasegurosRetorno extends Retorno {
	@XmlElementWrapper(name="tipos")
	@XmlElement(name="tipo")
	private List<CaixasegurosEntity> data;
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public CaixasegurosRetorno() {
		data = new ArrayList<CaixasegurosEntity>();		
	}
	public List<CaixasegurosEntity> getData() {
		return data;
	}
	public void setData(List<CaixasegurosEntity> data) {
		this.data = data;
	}
}

