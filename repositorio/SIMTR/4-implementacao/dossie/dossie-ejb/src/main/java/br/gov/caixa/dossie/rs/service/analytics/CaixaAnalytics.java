package br.gov.caixa.dossie.rs.service.analytics;

import org.apache.log4j.Logger;

/**
 * @author CTMONSI
 */
public class CaixaAnalytics {

	private static final Logger LOGGER = Logger.getLogger(CaixaAnalytics.class);

	private final transient String appName;
	private final transient String key;
	private final transient Integer session;
		
	private static final String INIT_CHAVE = "INIT";	
	private static final String PAGE_CHAVE = "page";
	private static final String ERROR = "error";
	private static final String WARN = "warn";
	
	private CaixaAnalyticsRest rest;
	
	/**
	 * 
	 * @param key	Chave do sistema
	 * @param session	Sessão criada pelo script
	 * @param appName	Nome da aplicação ou categoria
	 * @param urlSimma	Url do sistema de metricas
	 */
	public CaixaAnalytics(String key, Integer session, String appName, String urlSimma) {
		this.key = key;
		this.appName = appName;
		this.session = session;
				
		rest = new CaixaAnalyticsRest(urlSemContexto(urlSimma));		
		
		if (key != null) {
			init(key, session, appName);
		}
	}

	private String urlSemContexto(String urlSimma) {
		String url = urlSimma;
		url = url.replaceAll(CaixaAnalyticsRest.ANALYTICS_CONTEXTO, "");
		return url;
	}

	private void init(String key, Integer session, String appName) {
		LOGGER.info(appName);

		if (session != null) {
			trackEvent(INIT_CHAVE, session.toString());
		} else {
			trackEvent(INIT_CHAVE);
		}
	}

	public void trackWarn(String message, int code) {
		track(WARN, message, code);
	}

	public void trackWarn(String message) {
		track(WARN, message, null);
	}

	public void trackError(String message, int code) {
		track(ERROR, message, code);
	}

	public void trackError(String message) {
		track(ERROR, message, null);
	}

	public void trackPage(String page, int version) {
		track(PAGE_CHAVE, page, version);
	}

	public void trackPage(String page) {
		track(PAGE_CHAVE, page);
	}

	public void trackEvent(String action, String label, Integer value) {
		track(action, label, value);
	}

	public void trackEvent(String action, String label) {
		track(action, label, null);
	}

	public void trackEvent(String action) {
		track(action, null, null);
	}

	public void track(String action, String label) {
		track(action, label, null);
	}

	public void track(String action) {
		track(action, null, null);
	}

	public void track(String action, String label, Integer value) {
		
//		final JSONObject json = new JSONObject();		
//		try {
//			if (value != null) {				
//				json.put("chave", key);
//				json.put("sessao", session);
//				json.put("categoria", appName);
//				json.put("acao", action);
//				json.put("etiqueta", label);
//				json.put("valor", value);
//				json.put("nomeSistemaOperacional", "");
//				json.put("versaoSistemaOperacional", "");
//				json.put("nomeNavegador", "");
//				json.put("versaoNavegador", "");
//				json.put("dispositivo", "");
//				json.put("userAgent","");
//				json.put("token", "");
//
//				rest.registrar(json);
//			} else if (label != null) {
//				
//				json.put("chave", key);
//				json.put("sessao", session);
//				json.put("categoria", appName);
//				json.put("acao", action);
//				json.put("etiqueta", label);
//				json.put("valor", "");
//				json.put("nomeSistemaOperacional", "");
//				json.put("versaoSistemaOperacional", "");
//				json.put("nomeNavegador", "");
//				json.put("versaoNavegador", "");
//				json.put("dispositivo", "");
//				json.put("userAgent","");
//				json.put("token", "");
//
//				rest.registrar(json);				
//			} else {
//							
//				json.put("chave", key);
//				json.put("sessao", session);
//				json.put("categoria", appName);
//				json.put("acao", action);
//				json.put("etiqueta", "");
//				json.put("valor", "");
//				json.put("nomeSistemaOperacional", "");
//				json.put("versaoSistemaOperacional", "");
//				json.put("nomeNavegador", "");
//				json.put("versaoNavegador", "");
//				json.put("dispositivo", "");
//				json.put("userAgent","");
//				json.put("token", "");
//
//				rest.registrar(json);
//			}
//		}catch (ConnectException ce) {
//			LOGGER.error("Ocorreu um erro ao se conectar o serviço de envio de evento : " + json + ": " + ce.getMessage());
//		}catch (Exception e) {
//			LOGGER.error("Ocorreu um erro ao enviar o evento : " + json + ": " + e.getMessage());
//		}
	}
}