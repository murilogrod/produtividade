	package br.gov.caixa.dossie.rs.retorno;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.dossie.rs.entity.ProdutosEntity;
/**
 * @author SIOGP
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProdutosRetorno extends Retorno {
	@XmlElementWrapper(name="tipos")
	@XmlElement(name="tipo")
	private List<ProdutosEntity> data;
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public ProdutosRetorno() {
		data = new ArrayList<ProdutosEntity>();		
	}
	public List<ProdutosEntity> getData() {
		return data;
	}
	public void setData(List<ProdutosEntity> data) {
		this.data = data;
	}
}

