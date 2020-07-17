package br.gov.caixa.simtr.modelo.enumerator;

public enum OperadorComparacaoEnum {
    MAIOR(">"), MENOR("<"), IGUAL("="), DIFERENTE("!="), MAIOR_IGUAL(">="), MENOR_IGUAL("<=");
    
    private String operador;
    
    OperadorComparacaoEnum(String operador) {
        this.operador = operador;
    }
    
    String getOpeador(){
        return this.operador;
    }
}
