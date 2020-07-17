package br.gov.caixa.simtr.controle.vo.portalempreendedor.validacao;

import java.io.Serializable;

import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;

public class TiposDocumentoValidadoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private TipoDocumento identificacaoPF;
    private TipoDocumento enderecoPF;
    private TipoDocumento constituicaoPJ;
    private TipoDocumento faturamentoPJ;
    
    public TiposDocumentoValidadoVO(TipoDocumento identificacaoPF, TipoDocumento enderecoPF, TipoDocumento constituicaoPJ, TipoDocumento faturamentoPJ) {
        super();
        this.identificacaoPF = identificacaoPF;
        this.enderecoPF = enderecoPF;
        this.constituicaoPJ = constituicaoPJ;
        this.faturamentoPJ = faturamentoPJ;
    }

    public TipoDocumento getIdentificacaoPF() {
        return identificacaoPF;
    }

    public void setIdentificacaoPF(TipoDocumento identificacaoPF) {
        this.identificacaoPF = identificacaoPF;
    }

    public TipoDocumento getEnderecoPF() {
        return enderecoPF;
    }

    public void setEnderecoPF(TipoDocumento enderecoPF) {
        this.enderecoPF = enderecoPF;
    }

    public TipoDocumento getConstituicaoPJ() {
        return constituicaoPJ;
    }

    public void setConstituicaoPJ(TipoDocumento constituicaoPJ) {
        this.constituicaoPJ = constituicaoPJ;
    }

    public TipoDocumento getFaturamentoPJ() {
        return faturamentoPJ;
    }

    public void setFaturamentoPJ(TipoDocumento faturamentoPJ) {
        this.faturamentoPJ = faturamentoPJ;
    }
}
