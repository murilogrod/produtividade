package br.gov.caixa.simtr.controle.vo.portalempreendedor;

import java.io.Serializable;

public class NecessidadeVO implements Serializable{
    private static final long serialVersionUID = 1L;
    
	private Integer codigo;
    private String descricao;
    
    public Integer getCodigo() {
		return codigo;
	}
    
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
