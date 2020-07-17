package br.gov.caixa.simtr.modelo.enumerator;

public enum SIFRCRecomendacaoEnum {

    SIM("sim"),
    NAO("nao");

    private final String descricao;

    private SIFRCRecomendacaoEnum(final String description) {
        this.descricao = description;
    }

    public static SIFRCRecomendacaoEnum getByDescricao(final String descricao) {
        for (final SIFRCRecomendacaoEnum item : SIFRCRecomendacaoEnum.values()) {
            if (item.getDescricao().equals(descricao)) {
                return item;
            }
        }

        throw new IllegalArgumentException("Recomendação não encontrada pela descrição: " + descricao);
    }

    public String getDescricao() {
        return this.descricao;
    }

}
