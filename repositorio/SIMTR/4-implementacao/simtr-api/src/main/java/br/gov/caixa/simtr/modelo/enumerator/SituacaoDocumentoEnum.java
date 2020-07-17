package br.gov.caixa.simtr.modelo.enumerator;

import java.text.MessageFormat;

public enum SituacaoDocumentoEnum {
    CONFORME("Conforme"),
    CRIADO("Criado"),
    IGNORADO("Ignorado"),
    INVALIDO("Invalido"),
    PENDENTE("Pendente"),
    QUARENTENA("Quarentena"),
    RECUSADO("Recusado"),
    REJEITADO("Rejeitado"),
    REPROVADO("Reprovado"),
    SUBSTITUIDO("Substituido");

    private final String descricao;

    private SituacaoDocumentoEnum(final String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static SituacaoDocumentoEnum getByDescricao(final String descricao) {
        for (final SituacaoDocumentoEnum enumTemplate : SituacaoDocumentoEnum.values()) {
            if (enumTemplate.getDescricao().equalsIgnoreCase(descricao)) {
                return enumTemplate;
            }
        }

        throw new IllegalArgumentException(MessageFormat.format("Situação Documento não identificada sob descrição: {0}", descricao));
    }

}
