package br.gov.caixa.simtr.controle.vo.portalempreendedor;

import java.io.Serializable;

public class TelefoneCelularVO implements Serializable {
    private static final long serialVersionUID = 1L; 
    
    private String ddd;
    private String numero;
    
	public String getDdd() {
		return ddd;
	}
	public void setDdd(String ddd) {
		this.ddd = ddd;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
    
    

}
