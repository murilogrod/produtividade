
package br.gov.caixa.dossie.rs.service;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.gov.caixa.dossie.rs.requisicao.MensagemEmailPushRequisicao;
import br.gov.caixa.dossie.rs.requisicao.MensagemPushRequisicao;
import br.gov.caixa.dossie.rs.requisicao.TokenRequisicao;
import br.gov.caixa.dossie.rs.retorno.DataRetorno;
import br.gov.caixa.dossie.rs.retorno.Retorno;
import br.gov.caixa.dossie.util.Util;

@Stateless
@LocalBean
@Named
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PushService {
	
	private static final String MESSAGE_ID_PADRAO = "0";
	private static final String IMAGEM_SIMMA_BASE64 = "data:image/png;base64,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";

	private static final Logger LOGGER = Logger.getLogger(PushService.class);
	
	private String sicpuCode = "";
	private String usuarioServico = "";
	private String senhaUsuarioServico = "";
	
	private String url;
	
	private static final String SICPU_CONTEXTO = "/sicpu/";
	
	private PushConsumer pushConsumer;
	
	@PostConstruct
	public void init(){
		try {			
			url = Util.urlSemContexto(Util.getUrlSicpu(), SICPU_CONTEXTO);
			pushConsumer = new PushConsumer(url, SICPU_CONTEXTO);
		}catch (Exception e) {
			LOGGER.error("Ocorreu um ero ao instanciar o push consumer: " + e.getMessage());
		}
		
	}
	
	/**
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	public TokenRequisicao autenticar() throws Exception{
		TokenRequisicao token = null;
		try {			
			
			JSONObject resposta = pushConsumer.autenticar(montarJsonAutenticacao());
						
			token = popularToken(resposta);
								
			LOGGER.info("Token SICPU: " + token + " Usuario: " + usuarioServico);
		} catch (Exception se) {
			LOGGER.error("Ocorreu um erro ao se autenticar " + se.getMessage());
			throw se;
		}
		return token;
	}
	
	/**
	 * @param requisicao
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public Retorno enviarMensagem(MensagemPushRequisicao requisicao, TokenRequisicao token) throws Exception{
		
		Retorno retorno = new Retorno();
		try {
			
			JSONObject json = new JSONObject();
			
			json.put("messageId", MESSAGE_ID_PADRAO);
			json.put("app", getSicpuCode());
			json.put("name", requisicao.getNome());
			json.put("title", requisicao.getTitulo());
			json.put("message", requisicao.getMensagem());
			json.put("category", requisicao.getCategoria());
			json.put("url", requisicao.getSite());
			json.put("note", "");
			json.put("image", IMAGEM_SIMMA_BASE64);
			json.put("user", requisicao.getDestinatario());
			json.put("actionConfirm", requisicao.getConfirmar());
			json.put("actionCancel", requisicao.getCancelar());
			json.put("actionClose", requisicao.getFechar());
						
			montarJsonToken(json, token, Boolean.FALSE);
						
			JSONObject resposta = pushConsumer.enviarMensagem(json);
			retorno.setTemErro(Boolean.parseBoolean(resposta.getString("temErro")));
			retorno.setMsgsErro(new ArrayList<String>());
			retorno.getMsgsErro().add(resposta.getString("msgsErro"));
			
		} catch (Exception se) {
			LOGGER.error("Ocorreu um erro ao enviar a mensagem " + se.getMessage());
			throw se;
		}
		return retorno;
	}
	
	/**
	 * @param requisicao
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public Retorno enviarMensagemEmail(MensagemEmailPushRequisicao requisicao, TokenRequisicao token) throws Exception{
		
		Retorno retorno = new Retorno();
		try {
			
			JSONObject json = new JSONObject();
			
			json.put("app", sicpuCode);
			json.put("user", usuarioServico);
			json.put("password", senhaUsuarioServico);

			json.put("title", requisicao.getTitulo());
			json.put("content", requisicao.getMensagem());
			json.put("category", requisicao.getCategoria());			

			JSONArray arqs = new JSONArray(requisicao.getArquivos());
			json.put("attachment", arqs);

			JSONArray emls = new JSONArray(requisicao.getEmails());
			json.put("recipient", emls);

			JSONObject resposta = pushConsumer.enviarEmail(json);

			boolean temErro = Boolean.parseBoolean(resposta.getString("temErro"));
			retorno.setTemErro(temErro);
			retorno.setMsgsErro(new ArrayList<String>());

			if (temErro) {
				retorno.getMsgsErro().add(resposta.getString("msgsErro"));
			} else {
				retorno.getMsgsErro().add("Mensagem de email enviada com sucesso!");
			}
		} catch (Exception se) {
			LOGGER.error("Ocorreu um erro ao enviar a mensagem de email " + se.getMessage());
			throw se;
		}
		return retorno;
	}
	
	/**
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public DataRetorno listarInscritos(TokenRequisicao token) throws Exception{
		
		DataRetorno retorno = new DataRetorno();
		try {			
			JSONObject json = new JSONObject();
						
			json.put("app", getSicpuCode());			
			
			montarJsonToken(json, token, Boolean.FALSE);
			
			JSONObject resposta = pushConsumer.listarInscritos(json);
									
			boolean temErro = Boolean.parseBoolean(resposta.getString("temErro"));
			
			retorno.setTemErro(temErro);
			retorno.setMsgsErro(new ArrayList<String>());
			retorno.getMsgsErro().add(resposta.getJSONArray("msgsErro").getString(0));
			if (!resposta.isNull("data")) {
				retorno.setData(resposta.getString("data"));
			}
		} catch (JSONException je) {			
			throw new Exception(je);
		} catch (Exception e) {
			LOGGER.error("Ocorreu um erro ao enviar a mensagem de email " + e.getMessage());
			throw e;
		} 
		return retorno;
	}
	
	private TokenRequisicao popularToken(JSONObject resposta) throws JSONException {		
		TokenRequisicao token = new TokenRequisicao();
		token.setTokenAcesso(obterAtributoObjetoResposta(resposta, "tokenAcesso"));
		token.setTokenRenovacao(obterAtributoObjetoResposta(resposta, "tokenRenovacao"));
		token.setCredencial(obterAtributoObjetoResposta(resposta, "credencial"));
		token.setTempoExpiracaoRenovacao(obterAtributoObjetoResposta(resposta, "tempoExpiracaoRenovacao"));
		token.setTempoExpiracaoAcesso(obterAtributoObjetoResposta(resposta, "tempoExpiracaoAcesso"));

		return token;
	}
	
	public void montarJsonToken(JSONObject joOrigem, TokenRequisicao token, boolean tokenRenovacao) throws JSONException{
		if (joOrigem!=null && token !=null && token.getTokenAcesso()!=null && joOrigem.length()>0) {
			
			joOrigem.put("tokenAcesso", token.getTokenAcesso());
			if (tokenRenovacao) {
				joOrigem.put("tokenRenovacao", token.getTokenRenovacao());
			}
			joOrigem.put("credencial", token.getCredencial());
			
			montarJsonApp(joOrigem);					
		}
	}	
	
	public void montarJsonApp(JSONObject joOrigem) throws JSONException{
		if (joOrigem!=null && joOrigem.length()>0) {			
			joOrigem.put("app", getSicpuCode());			
		}
	}
		
	private String obterAtributoObjetoResposta(JSONObject resposta, String parametro) throws JSONException {
		String valor = null;
		JSONArray array = (JSONArray)resposta.get("data");
		for (int i =0 ; i< array.length(); i++) {
			JSONObject o = (JSONObject)array.get(i);
			
			valor = o.get(parametro).toString();
			if (valor!=null && !valor.isEmpty()) {
				return valor;
			}
		}
		return valor;
	}
	
	public String getUrl() {
		return url;
	}

	public String getSicpuCode() {
		return sicpuCode;
	}

	public PushConsumer getPushConsumer() {
		return pushConsumer;
	}

	public void setPushConsumer(PushConsumer pushConsumer) {
		this.pushConsumer = pushConsumer;
	}

	public void setSicpuCode(String sicpuCode) {
		this.sicpuCode = sicpuCode;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	private JSONObject montarJsonAutenticacao() {
		JSONObject json = new JSONObject();
		try {			
			json.put("user", usuarioServico);
			json.put("password", senhaUsuarioServico); 
									
			montarJsonApp(json);			
		} catch (JSONException je) {
			LOGGER.error("Erro ao construir o json de autenticacao " + je.getMessage());
		} catch (Exception e) {
			LOGGER.error("Erro ao montar json de autenticacao " + e.getMessage());
		}
		return json;
	}
	
	public DataRetorno listarInscritosMensagem(TokenRequisicao token) throws Exception{
		DataRetorno retorno = new DataRetorno();
		try {			
			JSONObject json = new JSONObject();
						
			json.put("app", getSicpuCode());			
			
			montarJsonToken(json, token, Boolean.FALSE);
			
			JSONObject resposta = pushConsumer.listarInscritosMensagem(json);
									
			boolean temErro = Boolean.parseBoolean(resposta.getString("temErro"));
			
			retorno.setTemErro(temErro);
			retorno.setMsgsErro(new ArrayList<String>());
			retorno.getMsgsErro().add(resposta.getJSONArray("msgsErro").getString(0));
			if (!resposta.isNull("data")) {
				retorno.setData(resposta.getString("data"));
			}
			
		} catch (JSONException je) {			
			throw new Exception(je);
		} catch (Exception e) {
			LOGGER.error("Ocorreu um erro ao enviar a mensagem de email " + e.getMessage());
			throw e;
		} 
		
		return retorno;
	}
}