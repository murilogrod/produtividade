	package br.gov.caixa.dossie.rs.requisicao;
import java.util.ArrayList;
/*
	Esta classe armazena a informacao recebida e enviada entre o webservice e a interface
*/
import java.util.List;
/**
 * @author SIOGP
 */
public class LoginRequisicao extends Requisicao {
	private static final long serialVersionUID = 3075165143170327078L;
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
	public LoginRequisicao() {
		super();
        cargocontent.add("Analista");
        cargocontent.add("Coordenador");
        cargocontent.add("Programador");
         cargo = cargocontent.get(0);
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String valor) {
		usuario = valor;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String valor) {
		senha = valor;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String valor) {
		login = valor;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String valor) {
		email = valor;
	}
	public String getNomecompleto() {
		return nomecompleto;
	}
	public void setNomecompleto(String valor) {
		nomecompleto = valor;
	}
	public String getConfirmasenha() {
		return confirmasenha;
	}
	public void setConfirmasenha(String valor) {
		confirmasenha = valor;
	}
	public String getImagem() {
		return imagem;
	}
	public void setImagem(String valor) {
		imagem = valor;
	}
	public String getDatadecadastro() {
		return datadecadastro;
	}
	public void setDatadecadastro(String valor) {
		datadecadastro = valor;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String valor) {
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
	public void setDescricao(String valor) {
		descricao = valor;
	}
}

