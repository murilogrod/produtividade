package br.gov.caixa.simtr.visao;

import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SipesResponseDTO;

/**
 * 
 * @author f538462
 *
 */
public class DossieExceptionDTO {
	private Boolean falhaRequisicao;
    private Boolean falhaSIECM;
    private Boolean falhaSICPF;
    private Boolean falhaSIPES;
    private Boolean falhaSIMTR;
    private Boolean falhaSICLI;
    private SipesResponseDTO resultadoPesquisaSIPES; 
    private Boolean falhaSIFRC;
    private Boolean documentoAprovado;
    
    public DossieExceptionDTO() {
	}
    
	public DossieExceptionDTO(SipesResponseDTO sipesResponseDTO) {
		this.resultadoPesquisaSIPES = sipesResponseDTO;
	}
    
	public Boolean getDocumentoAprovado() {
		return documentoAprovado;
	}

	public void setDocumentoAprovado(Boolean documentoAprovado) {
		this.documentoAprovado = documentoAprovado;
	}

	public Boolean getFalhaSIFRC() {
		return falhaSIFRC;
	}

	public void setFalhaSIFRC(Boolean falhaSIFRC) {
		this.falhaSIFRC = falhaSIFRC;
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
	public Boolean getFalhaSICPF() {
		return falhaSICPF;
	}
	public void setFalhaSICPF(Boolean falhaSICPF) {
		this.falhaSICPF = falhaSICPF;
	}
	public Boolean getFalhaSIPES() {
		return falhaSIPES;
	}
	public void setFalhaSIPES(Boolean falhaSIPES) {
		this.falhaSIPES = falhaSIPES;
	}
	public Boolean getFalhaSIMTR() {
		return falhaSIMTR;
	}
	public void setFalhaSIMTR(Boolean falhaSIMTR) {
		this.falhaSIMTR = falhaSIMTR;
	}
	public SipesResponseDTO getResultadoPesquisaSIPES() {
		return resultadoPesquisaSIPES;
	}
	public void setResultadoPesquisaSIPES(SipesResponseDTO resultadoPesquisaSIPES) {
		this.resultadoPesquisaSIPES = resultadoPesquisaSIPES;
	}
	public Boolean getFalhaSICLI() {
		return falhaSICLI;
	}
	public void setFalhaSICLI(Boolean falhaSICLI) {
		this.falhaSICLI = falhaSICLI;
	}
	

}
