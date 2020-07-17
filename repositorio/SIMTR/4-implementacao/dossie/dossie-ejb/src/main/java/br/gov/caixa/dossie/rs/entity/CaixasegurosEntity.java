	package br.gov.caixa.dossie.rs.entity;

/**
 * @author SIOGP
 */
public class CaixasegurosEntity implements BaseEntity {
	private static final long serialVersionUID = 6933641140920629711L;
	private Long id;
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public CaixasegurosEntity() {
		super();
	}
	@Override
	public Long getId() {
		return id;
	}
	public void setId(final Long valor) {
		id = valor;
	}
}

