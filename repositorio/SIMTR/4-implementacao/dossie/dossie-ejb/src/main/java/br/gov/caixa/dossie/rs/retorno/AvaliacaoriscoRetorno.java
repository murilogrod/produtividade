	package br.gov.caixa.dossie.rs.retorno;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.dossie.rs.entity.AvaliacaoriscoEntity;
/**
 * @author SIOGP
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AvaliacaoriscoRetorno extends Retorno {
	@XmlElementWrapper(name="tipos")
	@XmlElement(name="tipo")
	private List<AvaliacaoriscoEntity> data;
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public AvaliacaoriscoRetorno() {
		data = new ArrayList<AvaliacaoriscoEntity>();		
	}
	public List<AvaliacaoriscoEntity> getData() {
		return data;
	}
	public void setData(List<AvaliacaoriscoEntity> data) {
		this.data = data;
	}
}

