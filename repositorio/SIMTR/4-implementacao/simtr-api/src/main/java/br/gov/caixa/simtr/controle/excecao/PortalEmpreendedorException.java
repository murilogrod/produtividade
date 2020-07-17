package br.gov.caixa.simtr.controle.excecao;

public class PortalEmpreendedorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final boolean servicoDisponivel;

    public PortalEmpreendedorException(String mensagem, boolean servicoDisponivel) {
        super(mensagem);
        this.servicoDisponivel = servicoDisponivel;
    }

    public PortalEmpreendedorException(String mensagem, Throwable causa, boolean servicoDisponivel) {
        super(mensagem, causa);
        this.servicoDisponivel = servicoDisponivel;
    }

    public boolean isServicoDisponivel() {
        return servicoDisponivel;
    }
}
