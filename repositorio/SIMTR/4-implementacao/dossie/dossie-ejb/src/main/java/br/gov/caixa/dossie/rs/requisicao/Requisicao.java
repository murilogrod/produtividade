package br.gov.caixa.dossie.rs.requisicao;

import java.io.Serializable;

/**
 * 
 * @author SIOGP
 *
 */
public class Requisicao implements Serializable {
	private static final long serialVersionUID = 8603035402853943940L;
	
	private String token;	
	
	public String getToken() { return token; }
	public void setToken(String token) { this.token = token; }
	
}
