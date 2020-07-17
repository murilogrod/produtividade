package br.gov.caixa.dossie.rs.requisicao;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author SIOGP
 */
public class MensagemPushRequisicao implements Serializable {
	
	private static final long serialVersionUID = 8109371014766620491L;
	
	@NotNull
	@Size(max=120)
	private String nome;
	
	@NotNull
	@Size(max=255)
	private String titulo;
	
	@NotNull
	private String mensagem;
		
	private String site;
	
	@Pattern(regexp = ".+@.+\\.[a-z]+")
	private String destinatario;
	
	private String categoria;
	
	private String confirmar;
	
	private String cancelar;
	
    private String fechar;
	
	@NotNull
	private String token;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

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

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getConfirmar() {
		return confirmar;
	}

	public void setConfirmar(String confirmar) {
		this.confirmar = confirmar;
	}

	public String getCancelar() {
		return cancelar;
	}

	public void setCancelar(String cancelar) {
		this.cancelar = cancelar;
	}

	public String getFechar() {
		return fechar;
	}

	public void setFechar(String fechar) {
		this.fechar = fechar;
	}

	@Override
	public String toString() {
		return "MensagemSicpuRequisicao [nome=" + nome + ", titulo=" + titulo + ", mensagem=" + mensagem + ", site="
				+ site + ", destinatario=" + destinatario + ", categoria=" + categoria + ", confirmar=" + confirmar
				+ ", cancelar=" + cancelar + ", fechar=" + fechar + ", token=" + token + "]";
	}
}