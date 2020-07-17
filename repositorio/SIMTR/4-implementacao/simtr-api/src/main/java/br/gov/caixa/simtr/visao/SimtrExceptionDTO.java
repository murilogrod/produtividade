package br.gov.caixa.simtr.visao;


/**
 * 
 * @author f538462
 *
 */
public class SimtrExceptionDTO {
	
	private Boolean falhaFornecedor;
    private Boolean falhaPermissao;
    private Boolean falhaRequisicao;
    private Boolean falhaSIECM;
    private Boolean falhaSIMTR;
     
	public Boolean getFalhaFornecedor() {
		return falhaFornecedor;
	}
	public void setFalhaFornecedor(Boolean falhaFornecedor) {
		this.falhaFornecedor = falhaFornecedor;
	}
	public Boolean getFalhaPermissao() {
		return falhaPermissao;
	}
	public void setFalhaPermissao(Boolean falhaPermissao) {
		this.falhaPermissao = falhaPermissao;
	}
	public Boolean getFalhaRequisicao() {
		return falhaRequisicao;
	}
	public void setFalhaRequisicao(Boolean falhaRequisicao) {
		this.falhaRequisicao = falhaRequisicao;
	}
	public Boolean getFalhaSIECM() {
		return falhaSIECM;
	}
	public void setFalhaSIECM(Boolean falhaSIECM) {
		this.falhaSIECM = falhaSIECM;
	}
	public Boolean getFalhaSIMTR() {
		return falhaSIMTR;
	}
	public void setFalhaSIMTR(Boolean falhaSIMTR) {
		this.falhaSIMTR = falhaSIMTR;
	}

}
