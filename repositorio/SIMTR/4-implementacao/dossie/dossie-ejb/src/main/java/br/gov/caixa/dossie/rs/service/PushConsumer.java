
package br.gov.caixa.dossie.rs.service;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import br.gov.caixa.dossie.util.ConsumerUtil;

public class PushConsumer {
	
	private static final Logger LOGGER = Logger.getLogger(PushConsumer.class);

	private static final String WS_AUTENTICAR = "ws/user/auth/01.00";
	private static final String WS_CRIAR_USUARIO = "ws/user/create/01.00";
	
	private static final String WS_ENVIAR_MSG = "ws/push/sendimmediate/01.00";
	
	private static final String WS_INSCRITOS = "ws/email/subscribes/01.00";
	
	private static final String WS_INCLUIR_EMAIL = "ws/email/new/01.00";
	private static final String WS_ENVIAR_EMAIL = "ws/email/sendimmediate/01.00";
	private static final String WS_INSCRITOS_MSG = "ws/subscribe/list/01.00";

	private String url;
	private String contexto;
	
	private ConsumerUtil util = new ConsumerUtil();
	
	/**
	 * @param url	Endereço da aplicacao SIMMA
	 */
	public PushConsumer(String url, String contexto) {
		this.url = url;
		this.contexto = contexto;
	}
	
	
	public JSONObject enviar(final JSONObject json, String metodo) throws Exception {
		LOGGER.info(metodo);		
		JSONObject jo = null;
		try{
			jo = util.enviar(json, juntarContextMetodo(metodo), getUrl());
		}catch (Exception e) {			
			throw new Exception(e);
		}
		return jo;
	}
	
	public JSONObject autenticar(final JSONObject json) throws Exception {
		return enviar(json, WS_AUTENTICAR);						
	}

		
	public JSONObject enviarMensagem(final JSONObject json) throws Exception {
		return enviar(json, WS_ENVIAR_MSG);
	}
		
	public JSONObject listarInscritos(final JSONObject json) throws Exception {
		return enviar(json, WS_INSCRITOS);
	}
	public JSONObject incluirEmail(final JSONObject json) throws Exception {
		return enviar(json, WS_INCLUIR_EMAIL);
	}
	public JSONObject enviarEmail(final JSONObject json) throws Exception {
		return enviar(json, WS_ENVIAR_EMAIL);
	}
	public JSONObject criarUsuario(final JSONObject json) throws Exception {
		return enviar(json, WS_CRIAR_USUARIO);
	}
	public JSONObject listarInscritosMensagem(final JSONObject json) throws Exception {
		return enviar(json, WS_INSCRITOS_MSG);
	}
	
	public String getUrl() {
		return url;
	}
	
	private String juntarContextMetodo(String metodo) {
		return getContexto() + metodo;
	}

	public String getContexto() {
		return contexto;
	}

	public ConsumerUtil getUtil() {
		return util;
	}


	public void setUtil(ConsumerUtil util) {
		this.util = util;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public void setContexto(String contexto) {
		this.contexto = contexto;
	}
}