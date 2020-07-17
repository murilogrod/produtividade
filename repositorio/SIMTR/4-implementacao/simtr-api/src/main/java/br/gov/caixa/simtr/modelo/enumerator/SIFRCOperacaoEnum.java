package br.gov.caixa.simtr.modelo.enumerator;

public enum SIFRCOperacaoEnum {

    NOVO("novo_cadastro"),
    ATUALIZACAO("atualizacao_cadastro");

    private final String descricao;

    private SIFRCOperacaoEnum(final String description) {
        this.descricao = description;
    }

    public static SIFRCOperacaoEnum getByOperacao(final String operacao) {
        for (final SIFRCOperacaoEnum item : SIFRCOperacaoEnum.values()) {
            if (item.name().equals(operacao)) {
                return item;
            }
        }

        throw new IllegalArgumentException("Operação não encontrada: " + operacao);
    }

    public static SIFRCOperacaoEnum getByDescricao(final String descricao) {
        for (final SIFRCOperacaoEnum item : SIFRCOperacaoEnum.values()) {
            if (item.getDescricao().equals(descricao)) {
                return item;
            }
        }

        throw new IllegalArgumentException("Operação não encontrada pela descrição: " + descricao);
    }

    public String getDescricao() {
        return this.descricao;
    }

}
