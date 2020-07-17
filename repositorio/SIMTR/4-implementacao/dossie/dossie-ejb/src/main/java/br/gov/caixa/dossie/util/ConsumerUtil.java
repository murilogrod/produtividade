package br.gov.caixa.dossie.util;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author SIOGP
 */
public class ConsumerUtil {

	private static final Logger LOGGER = Logger.getLogger(ConsumerUtil.class);
	
	private static final String CHARSET_UTF_8 = ";charset=UTF-8";
	
	private static final String ATRIB_RETORNO_MSGS_ERRO = "msgsErro";

	private static final String ATRIB_RETORNO_TEM_ERRO = "temErro";
	
	private ClientRequestFactory getService() {
		return new ClientRequestFactory();
	}
	
	private ClientResponse<String> getResposta(final JSONObject json, final String metodo, String url) throws Exception {
		
		try{
			return getService().createRequest(url+metodo)
					.accept(MediaType.APPLICATION_JSON + CHARSET_UTF_8)
					.body(MediaType.APPLICATION_JSON, json.toString()).post(String.class);
		}catch (Exception e) {
			throw new Exception(e);
		}
		
	}
	
	public JSONObject enviar(final JSONObject json, final String metodo, String url) throws Exception {
		JSONObject jsonResposta;
		ClientResponse<String> response = null;
		try {			
			if (json!=null && metodo!=null && url!=null) {
				response = getResposta(json,metodo, url);
			}			
		}catch (Exception e) {			
			LOGGER.error("Erro na integração: ",e);
			LOGGER.error("JSON DO ERRO: " + json);
			throw new Exception(" Sem sucesso na chamada a url: " + url, e);
		}
		
		try{
			if (response!=null && response.getStatus() == Status.OK.getStatusCode()){
				jsonResposta = new JSONObject(String.valueOf(response.getEntity()));
				if (jsonResposta.get(ATRIB_RETORNO_TEM_ERRO).equals(Boolean.TRUE)) {
					LOGGER.warn(url + " - Tem Erro " + jsonResposta.get(ATRIB_RETORNO_TEM_ERRO));
					LOGGER.warn(url + " - Msg erro " + jsonResposta.getJSONArray(ATRIB_RETORNO_MSGS_ERRO).getString(0));
					throw new Exception(" Chamada a url " + url + " apresentou erro na resposta: " + jsonResposta.getJSONArray(ATRIB_RETORNO_MSGS_ERRO).getString(0));
				}
			} else {
				if (response!=null) {				
					LOGGER.error("Resposta da solicitação: " + response.getEntity());
					LOGGER.error(url + " - Codigo status: " + response.getStatus());
					LOGGER.error(url + " - Descrição status: " + response.getResponseStatus().getReasonPhrase());
				}
				throw new Exception("Resposta da solicitação: " + response.getEntity());
			}
		}catch (JSONException je) {
			LOGGER.error("Falha ao tratar a resposta da solicitação ", je);
			throw new Exception(je);
		}
		return jsonResposta;
	}
}