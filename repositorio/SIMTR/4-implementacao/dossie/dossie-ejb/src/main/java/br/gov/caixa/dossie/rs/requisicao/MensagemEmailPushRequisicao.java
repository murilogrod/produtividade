package br.gov.caixa.dossie.rs.requisicao;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * @author SIOGP
 */
public class MensagemEmailPushRequisicao implements Serializable {
	
	private static final long serialVersionUID = -5917536765900344671L;
	
	@NotNull
	private String titulo;
	
	@NotNull
	private String mensagem;
	
	@NotNull
	private String categoria;
	
	private String arquivos;
	
	private String emails;
	
	private String emailAtivo;
	
	@NotNull
	private String token;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}	

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getArquivos() {
		return arquivos;
	}

	public void setArquivos(String arquivos) {
		this.arquivos = arquivos;
	}

	public String getEmails() {
		return emails;
	}

	public void setEmails(String emails) {
		this.emails = emails;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmailAtivo() {
		return emailAtivo;
	}

	public void setEmailAtivo(String emailAtivo) {
		this.emailAtivo = emailAtivo;
	}

	@Override
	public String toString() {
		return "MensagemEmailSicpuRequisicao [titulo=" + titulo + ", mensagem=" + mensagem + ", categoria=" + categoria
				+ ", arquivos=" + arquivos + ", emails=" + emails + ", emailAtivo=" + emailAtivo + ", token=" + token
				+ "]";
	}
}