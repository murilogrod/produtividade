package br.gov.caixa.dossie.rs.service.analytics;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

import org.apache.log4j.Logger;

import br.gov.caixa.dossie.rs.retorno.analytics.AnalyticsRetorno;

/**
 * @author SIOGP
 */
@Stateless
@LocalBean
@Named
public class AnalyticsService {
	
	private static final Logger LOGGER = Logger.getLogger(AnalyticsService.class);
	
	private static final String SEPARADOR_DIRETORIO_PAGINA = "/";
	private static final String VAR_JBOSS_URL_SIMMA = "simma.local";
	private static final String VAR_JBOSS_URL_SICPU = "sicpu.local";
	
	/**
	 * @return retorno
	 */
	public AnalyticsRetorno read() {	
		LOGGER.info("Chamando o metodo: read()");

		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add("Leitura feita com sucesso.");
		AnalyticsRetorno retorno = new AnalyticsRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.setUrlsicpu(getUrlvariavelJBoss(VAR_JBOSS_URL_SICPU));
		retorno.setUrl(getUrlSimma());
		return retorno;
	}
	
	public String getUrlSimma(){		
		String url = getSystemEnvOrProperty(VAR_JBOSS_URL_SIMMA);
		if (url==null || (url.isEmpty())) {
			LOGGER.error("Falha ao obter a propriedade " + VAR_JBOSS_URL_SIMMA + " via jboss");
		}else {
			url = url.endsWith(SEPARADOR_DIRETORIO_PAGINA) ? url : url.concat(SEPARADOR_DIRETORIO_PAGINA);
			LOGGER.debug("URL ANALYTICS: " + url);
		}
		return url;
	}
	
	private String getUrlvariavelJBoss(final String nmVariavel){
		String url = getSystemEnvOrProperty(nmVariavel);
		if (url==null || (url.isEmpty())) {
			LOGGER.error("Falha ao obter a propriedade " + nmVariavel + " via jboss");
		}else {
			if(nmVariavel.equalsIgnoreCase(VAR_JBOSS_URL_SICPU) || nmVariavel.equalsIgnoreCase(VAR_JBOSS_URL_SIMMA)){
			 url = url.endsWith(SEPARADOR_DIRETORIO_PAGINA) ? url : url.concat(SEPARADOR_DIRETORIO_PAGINA);
			}
			LOGGER.debug("URL : " + nmVariavel.substring(0, nmVariavel.indexOf(".")).toUpperCase() + url);
		}
		return url;
	}
	
	/**
	 * @param nmVariavel
	 * @return retorno
	 */
	public String getSystemEnvOrProperty(final String nmVariavel) {
		return System.getProperty(nmVariavel) != null ? System.getProperty(nmVariavel) : System.getenv(nmVariavel);
	}
}