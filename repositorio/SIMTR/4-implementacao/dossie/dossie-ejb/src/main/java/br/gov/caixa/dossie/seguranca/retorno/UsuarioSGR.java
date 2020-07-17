package br.gov.caixa.dossie.seguranca.retorno;

import java.io.Serializable;

/**
 * 
 * @author SIOGP
 *
 */
public class UsuarioSGR implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String tipoAmbiente;
	private String ambiente;
	private String login;
	private String nome;
	private String certificado;
	private String unidade;
	private String processo;
	private String nivelAutenticacao;
	private String ip;
	private String email;
	private String perfil;
	private String tipoCredencial;

	public String getTipoCredencial() {
		return this.tipoCredencial;
	}
	
	public void setTipoCredencial(String tpCredencial) {
		this.tipoCredencial = tpCredencial;
	}
	
	public String getTipoAmbiente() {
		return tipoAmbiente;
	}
	public void setTipoAmbiente(String tpAmbiente) {
		this.tipoAmbiente = tpAmbiente;
	}
	public String getAmbiente() {
		return ambiente;
	}
	public void setAmbiente(String ambient) {
		this.ambiente = ambient;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String loginUsuario) {
		this.login = loginUsuario;
	}
	public String getNome() {
		return nome;
	}
	
	public String getPrimeiroNome() {
		if(nome == null){
			return "";
		}
		String[] nomes = nome.split(" ");
		if(nomes.length > 0){
			return nomes[0];
		}else{
			return "";
		}
	}
	
	public void setNome(String nomeUsuario){
		this.nome = nomeUsuario;
	}
	
	public String getCertificado() {
		return certificado;
	}
	
	public void setCertificado(String certificadoUsuario){
		this.certificado = certificadoUsuario;
	}
	
	public String getUnidade() {
		return unidade;
	}
	public void setUnidade(String unidadeUsuario) {
		this.unidade = unidadeUsuario;
	}
	public String getProcesso() {
		return processo;
	}
	public void setProcesso(String process) {
		this.processo = process;
	}
	public String getNivelAutenticacao() {
		return nivelAutenticacao;
	}
	public void setNivelAutenticacao(String nivelAutenticacaoUsuario) {
		this.nivelAutenticacao = nivelAutenticacaoUsuario;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ipUsuario) {
		this.ip = ipUsuario;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String emailUsuario) {
		this.email = emailUsuario;
	}
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfilUsuario) {
		this.perfil = perfilUsuario;
	}
	
}

