
package br.gov.caixa.dossie.rs.retorno;

import java.io.Serializable;

public class AuthenticationRetorno extends Retorno implements Serializable {
	
	private static final long serialVersionUID = -5966756498452267013L;
	
	private String credencial;
	private String tokenAcesso;
	private String tokenRenovacao;
	private Long tempoExpiracaoAcesso;
	private Long tempoExpiracaoRenovacao;
	
	public String getCredencial() {
		return credencial;
	}
	public void setCredencial(String credencial) {
		this.credencial = credencial;
	}
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
	public Long getTempoExpiracaoAcesso() {
		return tempoExpiracaoAcesso;
	}
	public void setTempoExpiracaoAcesso(Long tempoExpiracaoAcesso) {
		this.tempoExpiracaoAcesso = tempoExpiracaoAcesso;
	}
	public Long getTempoExpiracaoRenovacao() {
		return tempoExpiracaoRenovacao;
	}
	public void setTempoExpiracaoRenovacao(Long tempoExpiracaoRenovacao) {
		this.tempoExpiracaoRenovacao = tempoExpiracaoRenovacao;
	}
}

