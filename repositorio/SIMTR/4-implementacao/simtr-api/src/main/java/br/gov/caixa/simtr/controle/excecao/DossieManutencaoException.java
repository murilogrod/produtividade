package br.gov.caixa.simtr.controle.excecao;

import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.visao.DossieExceptionDTO;

public class DossieManutencaoException extends RuntimeException {

    /**
     * Indica que houve um problema na extração de dados do documento por parte
     * do Dossiê Digital
     */
    private static final long serialVersionUID = 1L;

    private final Long cpfCnpj;
    private final TipoPessoaEnum tipoPessoaEnum;
    private final Boolean documentoAprovado;
    private final Boolean falhaRequisicao;
    private final Boolean falhaSICPF;
    private final Boolean falhaSIECM;
    private final Boolean falhaSIFRC;
    private final Boolean falhaSIMTR;
    private final Boolean falhaSICLI;

    public DossieManutencaoException(String mensagem, Throwable causa, DossieExceptionDTO dossieExceptionDTO) {
        super(mensagem, causa);
        this.cpfCnpj = null;
        this.tipoPessoaEnum = null;
        this.falhaSIECM = dossieExceptionDTO.getFalhaSIECM();
        this.falhaSICPF = dossieExceptionDTO.getFalhaSICPF();
        this.falhaSIMTR = dossieExceptionDTO.getFalhaSIMTR();
        this.falhaSIFRC = dossieExceptionDTO.getFalhaSIFRC();
        this.falhaRequisicao = dossieExceptionDTO.getFalhaRequisicao();
        this.falhaSICLI = dossieExceptionDTO.getFalhaSICLI();
        this.documentoAprovado = null;
    }
    
    public DossieManutencaoException(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, String mensagem, Throwable causa, DossieExceptionDTO dossieExceptionDTO) {
        super(mensagem, causa);
        this.cpfCnpj = cpfCnpj;
        this.tipoPessoaEnum = tipoPessoaEnum;
        this.falhaSIECM = dossieExceptionDTO.getFalhaSIECM();
        this.falhaSICPF = dossieExceptionDTO.getFalhaSICPF();
        this.falhaSIMTR = dossieExceptionDTO.getFalhaSIMTR();
        this.falhaSIFRC = dossieExceptionDTO.getFalhaSIFRC();
        this.falhaRequisicao = dossieExceptionDTO.getFalhaRequisicao();
        this.falhaSICLI = dossieExceptionDTO.getFalhaSICLI();
        this.documentoAprovado = null;
    }

    public Long getCpfCnpj() {
        return cpfCnpj;
    }

    public TipoPessoaEnum getTipoPessoaEnum() {
        return tipoPessoaEnum;
    }

    public Boolean getDocumentoAprovado() {
        return documentoAprovado;
    }

    public Boolean getFalhaRequisicao() {
        return falhaRequisicao;
    }

    public Boolean getFalhaSICPF() {
        return falhaSICPF;
    }

    public Boolean getFalhaSIECM() {
        return falhaSIECM;
    }

    public Boolean getFalhaSIFRC() {
        return falhaSIFRC;
    }

    public Boolean getFalhaSIMTR() {
        return falhaSIMTR;
    }

	public Boolean getFalhaSICLI() {
		return falhaSICLI;
	}
    

}
