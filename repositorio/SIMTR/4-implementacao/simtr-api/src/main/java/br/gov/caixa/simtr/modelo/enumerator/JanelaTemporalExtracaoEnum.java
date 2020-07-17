package br.gov.caixa.simtr.modelo.enumerator;

public enum JanelaTemporalExtracaoEnum {

    M0, M30, M60;

    public static JanelaTemporalExtracaoEnum getByNome(final String nome) {
        for (final JanelaTemporalExtracaoEnum item : JanelaTemporalExtracaoEnum.values()) {
            if (item.name().equalsIgnoreCase(nome)) {
                return item;
            }
        }

        throw new IllegalArgumentException("Janela temporal de extração não definida: " + nome);
    }
}
