package br.gov.caixa.dossie.util;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CookieUtil {
	
	private Map<String, Object> cookies;
	private Boolean tokenTemAtualizacao = false;
	
	
	public void builder(final String credencial, final String tokenAcesso, final String tokenRenovacao,  
			final long tempoExpiracaoAcesso,  final long tempoExpiracaoRenovacao){
		
		cookies = new HashMap<String, Object>();
		cookies.put(DossieConstantes.CREDENCIAL, credencial);
		cookies.put(DossieConstantes.TOKENACESSO, tokenAcesso);
		cookies.put(DossieConstantes.TOKENRENOVACAO, tokenRenovacao);		
		cookies.put(DossieConstantes.TEMPOEXPIRACAOACESSO, tempoExpiracaoAcesso);
		cookies.put(DossieConstantes.TEMPOEXPIRACAORENOVACAO, tempoExpiracaoAcesso);
		
	}

	public Boolean getTokenTemAtualizacao() {
		return tokenTemAtualizacao;
	}


	public void setTokenTemAtualizacao(Boolean tokenTemAtualizacao) {
		this.tokenTemAtualizacao = tokenTemAtualizacao;
	}


	public Map<String, Object> getCookies() {
		return cookies;
	}

}
