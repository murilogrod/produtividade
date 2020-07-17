	package br.gov.caixa.dossie.rs.entity;
import java.util.ArrayList;
/*
	Esta classe a fronteira entre sua aplição e o seu banco de dados 	
	Aqui poderao ser implementadas todas as rotinas de persistencia necessarias a sua aplicacao
*/
import java.util.List;
/**
 * @author SIOGP
 */
public class LoginEntity implements BaseEntity {
	private static final long serialVersionUID = 6933641140920629711L;
	private Long id;
	private String usuario = "Nome de Entrada";
	private String senha = "Rapoza Dois";
	private String login = "";
	private String email = "usuario@email.com.br";
	private String nomecompleto = "Nome de Entrada";
	private String confirmasenha = "Rapoza Dois";
	private String imagem = "";
	private String datadecadastro = "2016-01-11T16:03:45.130Z";
	private String cargo = "";
	private List<String> cargocontent = new ArrayList<String>();
	private String descricao = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus at augue id sem rhoncus ultrices. Aliquam sollicitudin erat quis enim tincidunt, eget consequat neque auctor. Suspendisse nulla purus, lacinia in orci eu, condimentum iaculis augue. Fusce quis ultricies nisi, nec suscipit libero. Aliquam maximus vulputate porttitor. Nunc nec aliquam sem, ac vehicula metus. Donec sit amet nulla nunc.";
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public LoginEntity() {
		super();
		cargocontent.add("Analista");
		cargocontent.add("Coordenador");
		cargocontent.add("Programador");
        cargo = cargocontent.get(0);
	}
	@Override
	public Long getId() {
		return id;
	}
	public void setId(final Long valor) {
		id = valor;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(final String valor) {
		usuario = valor;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(final String valor) {
		senha = valor;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(final String valor) {
		login = valor;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(final String valor) {
		email = valor;
	}
	public String getNomecompleto() {
		return nomecompleto;
	}
	public void setNomecompleto(final String valor) {
		nomecompleto = valor;
	}
	public String getConfirmasenha() {
		return confirmasenha;
	}
	public void setConfirmasenha(final String valor) {
		confirmasenha = valor;
	}
	public String getImagem() {
		return imagem;
	}
	public void setImagem(final String valor) {
		imagem = valor;
	}
	public String getDatadecadastro() {
		return datadecadastro;
	}
	public void setDatadecadastro(final String valor) {
		datadecadastro = valor;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(final String valor) {
		cargo = valor;
	}
	public List<String> getCargoContent() {
        return cargocontent;
	}
	public void setCargoContent(List<String> lista) {
		this.cargocontent = lista;
	}	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(final String valor) {
		descricao = valor;
	}
}

