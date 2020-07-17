
package br.gov.caixa.dossie.util;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import br.gov.caixa.dossie.enumerate.EnumVariaveisAmbiente;

public final class Util {

	private static final String SEPARADOR_DIRETORIO_PAGINA = "/";
	private static final Logger LOGGER = Logger.getLogger(Util.class);
	
	public static final String SIGLA_SISTEMA = "simma";
	
	/**.
	 * Recupera valor de propriedade do arquivo de propriedades ou do ambiente.
	 * @param nmVariavel
	 * @return String
	 */
	public static String getSystemEnvOrProperty(final String nmVariavel) {
		return System.getProperty(nmVariavel) != null
	         ? System.getProperty(nmVariavel)
	         : System.getenv(nmVariavel);	
	}
	public Util() {}

	
	public static boolean isNull(Object o) {
		return (o == null);
	}

	public static boolean isBlankOrNull(final Integer value) {
		return (isNull(value) || value.intValue() == 0);
	}

	public static boolean isBlankOrNull(final Long value) {
		return (isNull(value) || value.longValue() == 0L);
	}

	public static boolean isBlankOrNull(final BigDecimal value) {
		return (isNull(value) || value.longValue() == 0L);
	}

	public static boolean isBlankOrNull(final Double value) {
		return (isNull(value) || value.doubleValue() == 0L);
	}

	public static boolean isBlankOrNull(final String value) {
		return (isNull(value) || "".equals(value.trim()));
	}
	
	private static String getUrl(String var){
		String url = getVariavel(var);
		return url.endsWith(SEPARADOR_DIRETORIO_PAGINA) ? url : url.concat(SEPARADOR_DIRETORIO_PAGINA);
	}
	
	public static String getUrlSimma(){	
		return getUrl(EnumVariaveisAmbiente.VAR_JBOSS_URL_SIMMA.getValor());
	}
	
	public static String getUrlSicpu(){			
		return getUrl(EnumVariaveisAmbiente.VAR_JBOSS_URL_SICPU.getValor());
	}
	
	public static String getAmbienteSimma(){				
		return getVariavel(EnumVariaveisAmbiente.VAR_JBOSS_AMBIENTE.getValor());
	}
	
	public static String getAmbienteSicpu(){				
		return getVariavel(EnumVariaveisAmbiente.VAR_JBOSS_URL_SICPU.getValor());
	}
		
	public static String getVariavel(String variavel){		
		String var = Util.getSystemEnvOrProperty(variavel);
		if (var==null || var.isEmpty()) {
			LOGGER.error("Falha ao obter a propriedade " + variavel + " via jboss");
		}else {			
			LOGGER.debug("Variavel de ambiente " + variavel + " com valor " + var + " foi obtida com sucesso");
		}
		return var;
	}
	
	public static String urlSemContexto(String urlOriginal, String contexto) {
		String url = urlOriginal;
		url = url.replaceAll(contexto, "");
		return url;
	}
	
	public static String getContextoDeMetodo(String metodo) {
		String contexto = "";
		if (metodo!=null && !metodo.isEmpty()) {
			String[] urlParte = metodo.split("/");
			if (urlParte.length>1) {
				contexto = urlParte[1];
				contexto = contexto.replaceAll("/", "");
			}			
		}
		return contexto;
	}
}