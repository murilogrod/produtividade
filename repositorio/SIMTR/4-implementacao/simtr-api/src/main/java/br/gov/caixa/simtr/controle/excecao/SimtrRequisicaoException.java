package br.gov.caixa.simtr.controle.excecao;

public class SimtrRequisicaoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SimtrRequisicaoException(String mensagem) {
        super(mensagem);
    }

    public SimtrRequisicaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

}
