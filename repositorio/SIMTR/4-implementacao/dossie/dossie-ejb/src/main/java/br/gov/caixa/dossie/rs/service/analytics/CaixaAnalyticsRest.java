package br.gov.caixa.dossie.rs.service.analytics;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.json.JSONObject;

/**
 * @author CTMONSI
 */
public class CaixaAnalyticsRest {

	private static final String ATRIB_RETORNO_MSGS_ERRO = "msgsErro";
	private static final String ATRIB_RETORNO_TEM_ERRO = "temErro";
	private static final String CHARSET_UTF_8 = ";charset=UTF-8";
	private static final String WS_EVENTO_REGISTRAR = "/analytics/ws/evento/registrar";
	private static final Logger LOGGER = Logger.getLogger(CaixaAnalyticsRest.class);
	public static final String ANALYTICS_CONTEXTO = "/analytics/";

	private String url;
	
	/**
	 * @param url	Endereço da aplicacao SIMMA
	 */
	public CaixaAnalyticsRest(String url) {
		this.url = url;
	}
	
	private ClientRequestFactory getService() {
		return new ClientRequestFactory(UriBuilder.fromUri(getUrl()).build());
	}
	
	public void registrar(final JSONObject json) throws Exception {

		ClientResponse<String> response = enviarJson(json,WS_EVENTO_REGISTRAR);

		if (response.getStatus() == Status.OK.getStatusCode()) {
			JSONObject objRetorno = new JSONObject(String.valueOf(response.getEntity()));
			if (objRetorno.get(ATRIB_RETORNO_TEM_ERRO).equals(Boolean.TRUE)) {
				LOGGER.warn("SIMMA - Tem Error " + objRetorno.get(ATRIB_RETORNO_TEM_ERRO));
				LOGGER.warn("SIMMA - Msg Error " + objRetorno.getJSONArray(ATRIB_RETORNO_MSGS_ERRO).getString(0));
				throw new Exception();
			}
		} else {
			LOGGER.info("SIMMA - Codigo status: " + response.getStatus());
			LOGGER.info("SIMMA - Descrição status: " + response.getResponseStatus().getReasonPhrase());
			throw new Exception();
		}
	}

	private ClientResponse<String> enviarJson(final JSONObject json, String recurso) throws Exception {
		return getService().createRelativeRequest(recurso)
				.accept(MediaType.APPLICATION_JSON + CHARSET_UTF_8)
				.body(MediaType.APPLICATION_JSON, json.toString()).post(String.class);		
	}

	public String getUrl() {
		return url;
	}
}