	package br.gov.caixa.dossie.rs.retorno;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.dossie.rs.entity.DossiedigitalEntity;
/**
 * @author SIOGP
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DossiedigitalRetorno extends Retorno {
	@XmlElementWrapper(name="tipos")
	@XmlElement(name="tipo")
	private List<DossiedigitalEntity> data;
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public DossiedigitalRetorno() {
		data = new ArrayList<DossiedigitalEntity>();		
	}
	public List<DossiedigitalEntity> getData() {
		return data;
	}
	public void setData(List<DossiedigitalEntity> data) {
		this.data = data;
	}
}

