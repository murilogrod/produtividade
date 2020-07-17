package br.gov.caixa.simtr.modelo.enumerator;

public enum TipoAtributoEnum {

    BOOLEAN, DATE, LONG, DECIMAL, STRING;

    public static TipoAtributoEnum getByNome(final String nome) {
        for (final TipoAtributoEnum item : TipoAtributoEnum.values()) {
            if (item.name().equalsIgnoreCase(nome)) {
                return item;
            }
        }

        throw new IllegalArgumentException("Tipo de Atributo não conhecido: " + nome);
    }
}
