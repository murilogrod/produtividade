package br.gov.caixa.simtr.modelo.enumerator;

import java.text.MessageFormat;

public enum ResultadoAvaliacaoProdutoSIRICEnum {

    NAO_ACEITO(0, "Não aceito"),
    ACEITO(1, "Aceito"),
    NAO_AVALIADO(2, "Não Avaliado"),
    QUADRO_DE_VALOR(3, "Quadro de Valor"),
    CONDICIONADO(4, "Condicionado"),
    SEM_REQUISITO(5, "Sem Requisito");
	
	
    private final String descricao;
    
    private final Integer codigo;

    private ResultadoAvaliacaoProdutoSIRICEnum(Integer codigo, String descricao) {
    	this.codigo = codigo;
        this.descricao = descricao;
    }
   
    public static ResultadoAvaliacaoProdutoSIRICEnum getResultadoAvaliacao(final Integer codigo) {
        for (final ResultadoAvaliacaoProdutoSIRICEnum enumTemplate : ResultadoAvaliacaoProdutoSIRICEnum.values()) {
            if (enumTemplate.getCodigo() == codigo) {
                return enumTemplate;
            }
        }

        throw new IllegalArgumentException(MessageFormat.format("Resultado não conhecido: {0}", codigo));
    }
    
    public String getDescricao() {
        return descricao;
    }

	public Integer getCodigo() {
		return codigo;
	}
}
