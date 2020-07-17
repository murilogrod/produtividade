package br.gov.caixa.simtr.modelo.enumerator;

import java.text.MessageFormat;

public enum TipoExpressaoFormulario {
    
    FORMULARIO_FASE("FF"),
    GRID_CLIENTE("GC"),
    GRID_GARANTIA("GG"),
    GRID_PRODUTO("GP");
    
    private final String descricao;
    
    private TipoExpressaoFormulario(String descricao) {
        this.descricao = descricao;
    }
    
    public static TipoExpressaoFormulario lookup(String descricao) {
        for(TipoExpressaoFormulario tipo : TipoExpressaoFormulario.values()) {
            if(tipo.getDescricao().equals(descricao)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format("Tipo Expressão Formulário não identificado: {0}", descricao));
    }

    public String getDescricao() {
        return descricao;
    }
}
