package br.gov.caixa.simtr.controle.excecao;

public class DossieException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final Boolean falhaRequisicao;

	public DossieException(String mensagem, boolean falhaRequisicao) {
		super(mensagem);
		this.falhaRequisicao = falhaRequisicao;

	}

	public DossieException(String mensagem, Throwable causa, boolean falhaRequisicao) {
		super(mensagem, causa);
		this.falhaRequisicao = falhaRequisicao;

	}

	public Boolean getFalhaRequisicao() {
		return falhaRequisicao;
	}

}
