package br.gov.caixa.simtr.modelo.enumerator;

public enum GEDTipoAtributoEnum {

    BOOLEAN, DATE, LONG, STRING;

    public static GEDTipoAtributoEnum getByNome(final String nome) {
        for (final GEDTipoAtributoEnum item : GEDTipoAtributoEnum.values()) {
            if (item.name().equalsIgnoreCase(nome)) {
                return item;
            }
        }

        throw new IllegalArgumentException("Tipo de Atributo não conhecido: " + nome);
    }
}
