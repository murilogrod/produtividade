package br.gov.caixa.dossie.rs.retorno.analytics;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.dossie.rs.retorno.Retorno;

/**
 * @author SIOGP
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AnalyticsRetorno extends Retorno {
	
	@XmlElementWrapper(name="urls")
	@XmlElement(name="url")
	private String url;
	private String urlsicpu;
	
	/**
	 * Construtor
	 */
	public AnalyticsRetorno() {
		url = "";
		urlsicpu = "";
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrlsicpu() {
		return urlsicpu;
	}
	public void setUrlsicpu(String urlsicpu) {
		this.urlsicpu = urlsicpu;
	}
}