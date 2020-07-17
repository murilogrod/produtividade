	package br.gov.caixa.dossie.rs.entity;

/**
 * @author SIOGP
 */
public class PrincipalEntity implements BaseEntity {
	private static final long serialVersionUID = 6933641140920629711L;
	private Long id;
	private String imgDossielogopng = "";
	private String numCpf2 = "";
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public PrincipalEntity() {
		super();
	}
	@Override
	public Long getId() {
		return id;
	}
	public void setId(final Long valor) {
		id = valor;
	}
	public String getImgdossielogopng() {
		return imgDossielogopng;
	}
	public void setImgdossielogopng(final String valor) {
		imgDossielogopng = valor;
	}
	public String getNumcpf2() {
		return numCpf2;
	}
	public void setNumcpf2(final String valor) {
		numCpf2 = valor;
	}
}

