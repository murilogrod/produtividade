package br.gov.caixa.simtr.modelo.enumerator;

public enum BPMSinalEnum {

    ALIMENTACAO_FINALIZADA("ALIMENTACAO", SituacaoDossieEnum.ALIMENTACAO_FINALIZADA.name()),
    CRIADO("CRIADO", SituacaoDossieEnum.CRIADO.name()),
    TRATAMENTO_PENDENTE("TRATAMENTO", SituacaoDossieEnum.AGUARDANDO_TRATAMENTO.name()),
    TRATAMENTO_CONFORME("TRATAMENTO", SituacaoDossieEnum.CONFORME.name()),
    TRATAMENTO_INFORMACAO("TRATAMENTO", SituacaoDossieEnum.PENDENTE_INFORMACAO.name()),
    TRATAMENTO_SEGURANCA("TRATAMENTO", SituacaoDossieEnum.PENDENTE_SEGURANCA.name()),
    SEGURANCA_FINALIZADO("SEGURANCA", SituacaoDossieEnum.SEGURANCA_FINALIZADO.name());

    String sinal;
    String situacao;

    private BPMSinalEnum(String sinal, String mensagem) {
        this.sinal = sinal;
        this.situacao = mensagem;
    }

    public String getSinal() {
        return sinal;
    }

    public String getSituacao() {
        return situacao;
    }
    
    public String getSituacaoJSON() {
        return "{\"situacao\": \"".concat(situacao).concat("\"}");
    }

    public static BPMSinalEnum getByNome(final String nome) {
        for (final BPMSinalEnum item : BPMSinalEnum.values()) {
            if (item.name().equalsIgnoreCase(nome)) {
                return item;
            }
        }

        throw new IllegalArgumentException("Sinal de BPM não conhecido: " + nome);
    }
}
