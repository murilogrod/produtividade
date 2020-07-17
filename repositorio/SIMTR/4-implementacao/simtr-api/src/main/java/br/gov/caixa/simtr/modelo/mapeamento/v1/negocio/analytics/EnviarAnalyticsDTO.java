package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.analytics;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EnviarAnalyticsDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "chave")
	private String chave;
	
	@JsonProperty(value = "sessao")
	private String sessao;
	
	@JsonProperty(value = "categoria")
	private String categoria;
	
	@JsonProperty(value = "acao")
	private String acao;
	
	@JsonProperty(value = "etiqueta")
	private String etiqueta;
	
	@JsonProperty(value = "valor")
	private String valor;
	
	@JsonProperty(value = "userAgente")
	private String userAgente;
	
	@JsonProperty(value = "token")
	private String token;
	
	@JsonProperty(value = "nomeSistemaOperacional")
	private String nomeSistemaOperacional;
	
	@JsonProperty(value = "versaoSistemaOperacional")
	private String versaoSistemaOperacional;
	
	@JsonProperty(value = "nomeNavegador")
	private String nomeNavegador;
	
	@JsonProperty(value = "versaoNavegador")
	private String versaoNavegador;
	
	@JsonProperty(value = "dispositivo")
	private String dispositivo;
	
	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getSessao() {
		return sessao;
	}

	public void setSessao(String sessao) {
		this.sessao = sessao;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public String getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getUserAgente() {
		return userAgente;
	}

	public void setUserAgente(String userAgente) {
		this.userAgente = userAgente;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String getNomeSistemaOperacional() {
		return nomeSistemaOperacional;
	}

	public void setNomeSistemaOperacional(String nomeSistemaOperacional) {
		this.nomeSistemaOperacional = nomeSistemaOperacional;
	}

	public String getVersaoSistemaOperacional() {
		return versaoSistemaOperacional;
	}

	public void setVersaoSistemaOperacional(String versaoSistemaOperacional) {
		this.versaoSistemaOperacional = versaoSistemaOperacional;
	}

	public String getNomeNavegador() {
		return nomeNavegador;
	}

	public void setNomeNavegador(String nomeNavegador) {
		this.nomeNavegador = nomeNavegador;
	}

	public String getVersaoNavegador() {
		return versaoNavegador;
	}

	public void setVersaoNavegador(String versaoNavegador) {
		this.versaoNavegador = versaoNavegador;
	}

	public String getDispositivo() {
		return dispositivo;
	}

	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}
	
}
