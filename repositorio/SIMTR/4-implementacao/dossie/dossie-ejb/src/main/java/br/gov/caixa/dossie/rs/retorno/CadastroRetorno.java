	package br.gov.caixa.dossie.rs.retorno;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.dossie.rs.entity.CadastroEntity;
/**
 * @author SIOGP
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CadastroRetorno extends Retorno {
	@XmlElementWrapper(name="tipos")
	@XmlElement(name="tipo")
	private List<CadastroEntity> data;
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public CadastroRetorno() {
		data = new ArrayList<CadastroEntity>();		
	}
	public List<CadastroEntity> getData() {
		return data;
	}
	public void setData(List<CadastroEntity> data) {
		this.data = data;
	}
}

