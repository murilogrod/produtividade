package br.gov.caixa.dossie.rs.requisicao;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * @author SIOGP
 */
public class TokenRequisicao implements Serializable {
	
	private static final long serialVersionUID = 3499509441009227489L;
	
	@NotNull
	private String tokenAcesso;
	
	private String tokenRenovacao;
	
	private String credencial;
	
	private String tempoExpiracaoRenovacao;
	
	private String tempoExpiracaoAcesso;
		
	/**
	 * @return
	 */
	public String getTokenAcesso() {
		return tokenAcesso;
	}

	public void setTokenAcesso(String tokenAcesso) {
		this.tokenAcesso = tokenAcesso;
	}

	public String getTokenRenovacao() {
		return tokenRenovacao;
	}

	public void setTokenRenovacao(String tokenRenovacao) {
		this.tokenRenovacao = tokenRenovacao;
	}

	public String getCredencial() {
		return credencial;
	}

	public void setCredencial(String credencial) {
		this.credencial = credencial;
	}

	public String getTempoExpiracaoRenovacao() {
		return tempoExpiracaoRenovacao;
	}

	public void setTempoExpiracaoRenovacao(String tempoExpiracaoRenovacao) {
		this.tempoExpiracaoRenovacao = tempoExpiracaoRenovacao;
	}

	public String getTempoExpiracaoAcesso() {
		return tempoExpiracaoAcesso;
	}

	public void setTempoExpiracaoAcesso(String tempoExpiracaoAcesso) {
		this.tempoExpiracaoAcesso = tempoExpiracaoAcesso;
	}

	@Override
	public String toString() {
		return "TokenRequisicao [tokenAcesso=" + tokenAcesso + ", tokenRenovacao=" + tokenRenovacao + ", credencial="
				+ credencial + "]";
	}
}