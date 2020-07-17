package br.gov.caixa.simtr.controle.excecao;

public class SimtrPreCondicaoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SimtrPreCondicaoException(String mensagem) {
        super(mensagem);
    }

    public SimtrPreCondicaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

}
