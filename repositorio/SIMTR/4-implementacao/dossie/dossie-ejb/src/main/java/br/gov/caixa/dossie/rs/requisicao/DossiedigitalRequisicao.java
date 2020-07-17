	package br.gov.caixa.dossie.rs.requisicao;

/**
 * @author SIOGP
 */
public class DossiedigitalRequisicao extends Requisicao {
	private static final long serialVersionUID = 3075165143170327078L;
	private Long id;
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public DossiedigitalRequisicao() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}

