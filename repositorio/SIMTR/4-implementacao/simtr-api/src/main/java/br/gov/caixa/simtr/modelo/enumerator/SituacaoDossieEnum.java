package br.gov.caixa.simtr.modelo.enumerator;

import java.text.MessageFormat;

public enum SituacaoDossieEnum {
    RASCUNHO("Rascunho", Boolean.FALSE),
    CRIADO("Criado", Boolean.TRUE),
    AGUARDANDO_TRATAMENTO("Aguardando Tratamento", Boolean.FALSE),
    EM_TRATAMENTO("Em Tratamento", Boolean.FALSE),
    CONFORME("Conforme", Boolean.TRUE),
    ANALISE_SEGURANCA("Análise Segurança", Boolean.FALSE),
    PENDENTE_SEGURANCA("Pendente Segurança", Boolean.TRUE),
    SEGURANCA_FINALIZADO("Segurança Finalizado", Boolean.TRUE),
    FINALIZADO_CONFORME("Finalizado Conforme", Boolean.FALSE),
    FINALIZADO_INCONFORME("Finalizado Inconforme", Boolean.FALSE),
    CANCELADO("Cancelado", Boolean.FALSE),
    AGUARDANDO_ALIMENTACAO("Aguardando Alimentação", Boolean.FALSE),
    EM_ALIMENTACAO("Em Alimentação", Boolean.FALSE),
    PENDENTE_INFORMACAO("Pendente Informação", Boolean.TRUE),
    ALIMENTACAO_FINALIZADA("Alimentação Finalizada", Boolean.TRUE),
    FINALIZADO("Finalizado", Boolean.FALSE),
    AGUARDANDO_COMPLEMENTACAO("Aguardando Complementação", Boolean.FALSE),
    EM_COMPLEMENTACAO("Em Complementação", Boolean.FALSE);

    private final String descricao;
    private final boolean sinalizaBPM;

    private SituacaoDossieEnum(final String descricao, final boolean sinalizaBPM) {
        this.descricao = descricao;
        this.sinalizaBPM = sinalizaBPM;
    }
    
    public String getDescricao() {
        return descricao;
    }

    public boolean isSinalizaBPM() {
        return sinalizaBPM;
    }

    public static SituacaoDossieEnum getByDescricao(final String descricao) {
        for (final SituacaoDossieEnum enumTemplate : SituacaoDossieEnum.values()) {
            if (enumTemplate.getDescricao().equalsIgnoreCase(descricao)) {
                return enumTemplate;
            }
        }

        throw new IllegalArgumentException(MessageFormat.format("Situação Dossiê não identificada sob descrição: {0}", descricao));
    }
}
