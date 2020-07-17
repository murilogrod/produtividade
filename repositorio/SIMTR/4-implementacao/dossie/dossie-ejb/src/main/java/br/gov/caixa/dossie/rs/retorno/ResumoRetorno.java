	package br.gov.caixa.dossie.rs.retorno;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.dossie.rs.entity.ResumoEntity;
/**
 * @author SIOGP
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ResumoRetorno extends Retorno {
	@XmlElementWrapper(name="tipos")
	@XmlElement(name="tipo")
	private List<ResumoEntity> data;
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public ResumoRetorno() {
		data = new ArrayList<ResumoEntity>();		
	}
	public List<ResumoEntity> getData() {
		return data;
	}
	public void setData(List<ResumoEntity> data) {
		this.data = data;
	}
}

