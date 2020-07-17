	package br.gov.caixa.dossie.rs.requisicao;

/**
 * @author SIOGP
 */
public class PrincipalRequisicao extends Requisicao {
	private static final long serialVersionUID = 3075165143170327078L;
	private Long id;
	private String imgDossielogopng = "";
	private String numCpf2 = "";
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public PrincipalRequisicao() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImgdossielogopng() {
		return imgDossielogopng;
	}
	public void setImgdossielogopng(String valor) {
		imgDossielogopng = valor;
	}
	public String getNumcpf2() {
		return numCpf2;
	}
	public void setNumcpf2(String valor) {
		numCpf2 = valor;
	}
}

