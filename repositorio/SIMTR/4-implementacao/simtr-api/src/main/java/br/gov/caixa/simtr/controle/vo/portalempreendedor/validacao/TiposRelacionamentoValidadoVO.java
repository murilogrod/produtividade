package br.gov.caixa.simtr.controle.vo.portalempreendedor.validacao;

import java.io.Serializable;

import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;

public class TiposRelacionamentoValidadoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private TipoRelacionamento responsavelLegal;
    private TipoRelacionamento conjuge;
    private TipoRelacionamento tomadorContrato;
    private TipoRelacionamento socioPF;
    private TipoRelacionamento socioPJ;

    public TiposRelacionamentoValidadoVO(TipoRelacionamento responsavelLegal, TipoRelacionamento conjuge, TipoRelacionamento tomadorContrato, TipoRelacionamento socioPF, TipoRelacionamento socioPJ) {
        super();
        this.responsavelLegal = responsavelLegal;
        this.conjuge = conjuge;
        this.tomadorContrato = tomadorContrato;
        this.socioPF = socioPF;
        this.socioPJ = socioPJ;
    }

    public TipoRelacionamento getResponsavelLegal() {
        return responsavelLegal;
    }

    public void setResponsavelLegal(TipoRelacionamento responsavelLegal) {
        this.responsavelLegal = responsavelLegal;
    }

    public TipoRelacionamento getConjuge() {
        return conjuge;
    }

    public void setConjuge(TipoRelacionamento conjuge) {
        this.conjuge = conjuge;
    }

    public TipoRelacionamento getTomadorContrato() {
        return tomadorContrato;
    }

    public void setTomadorContrato(TipoRelacionamento tomadorContrato) {
        this.tomadorContrato = tomadorContrato;
    }

    public TipoRelacionamento getSocioPF() {
        return socioPF;
    }

    public void setSocioPF(TipoRelacionamento socioPF) {
        this.socioPF = socioPF;
    }

    public TipoRelacionamento getSocioPJ() {
        return socioPJ;
    }

    public void setSocioPJ(TipoRelacionamento socioPJ) {
        this.socioPJ = socioPJ;
    }
}
