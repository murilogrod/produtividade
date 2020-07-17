/**
 * Bruno Henrique - Fóton
 */
package br.gov.caixa.simtr.modelo.enumerator;

public enum TipoRelatorioEnum {

    PDF(".pdf"),
    XLS(".xls");

    private final String extensao;

    private TipoRelatorioEnum(String extensao) {
        this.extensao = extensao;
    }

    public String getExtensao() {
        return extensao;
    }

}
