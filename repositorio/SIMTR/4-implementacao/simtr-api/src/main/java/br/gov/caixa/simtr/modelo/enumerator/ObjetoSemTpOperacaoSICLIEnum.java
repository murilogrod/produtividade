package br.gov.caixa.simtr.modelo.enumerator;

public enum ObjetoSemTpOperacaoSICLIEnum {
    DADOS_BASICOS("dadosBasicos"),
    PESSOA_JURIDICA("pessoaJuridica");
    
    private String descricao;
    
    private ObjetoSemTpOperacaoSICLIEnum(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return this.descricao;
    }
}
