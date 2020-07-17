
package br.gov.caixa.dossie.rs.requisicao;

import java.io.Serializable;

public class AuthenticationRequisicao implements Serializable {
	
	private static final long serialVersionUID = 4111884586308035492L;
	
	private String usuario;
	private String senha;
	private String navegador;
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getNavegador() {
		return navegador;
	}
	public void setNavegador(String navegador) {
		this.navegador = navegador;
	}
}

