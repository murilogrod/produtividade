package br.gov.caixa.simtr.controle.vo.portalempreendedor.validacao;

import java.io.Serializable;
import java.util.List;

import br.gov.caixa.simtr.modelo.entidade.DossieCliente;

public class DossiesClienteValidadoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private DossieCliente dossieClienteResponsavel;
    private DossieCliente dossieClienteConjuge;
    private List<DossieCliente> listaDossieClienteQuadroSocietarioValidado;
    private DossieCliente dossieClientePJ;
    
    public DossiesClienteValidadoVO(DossieCliente dossieClienteResponsavel, DossieCliente dossieClienteConjuge, List<DossieCliente> listaDossieClienteQuadroSocietarioValidado, DossieCliente dossieClientePJ) {
        super();
        this.dossieClienteResponsavel = dossieClienteResponsavel;
        this.dossieClienteConjuge = dossieClienteConjuge;
        this.listaDossieClienteQuadroSocietarioValidado = listaDossieClienteQuadroSocietarioValidado;
        this.dossieClientePJ = dossieClientePJ;
    }

    public DossieCliente getDossieClienteResponsavel() {
        return dossieClienteResponsavel;
    }

    public void setDossieClienteResponsavel(DossieCliente dossieClienteResponsavel) {
        this.dossieClienteResponsavel = dossieClienteResponsavel;
    }

    public DossieCliente getDossieClienteConjuge() {
        return dossieClienteConjuge;
    }

    public void setDossieClienteConjuge(DossieCliente dossieClienteConjuge) {
        this.dossieClienteConjuge = dossieClienteConjuge;
    }

    public List<DossieCliente> getListaDossieClienteQuadroSocietarioValidado() {
        return listaDossieClienteQuadroSocietarioValidado;
    }

    public void setListaDossieClienteQuadroSocietarioValidado(List<DossieCliente> listaDossieClienteQuadroSocietarioValidado) {
        this.listaDossieClienteQuadroSocietarioValidado = listaDossieClienteQuadroSocietarioValidado;
    }

    public DossieCliente getDossieClientePJ() {
        return dossieClientePJ;
    }

    public void setDossieClientePJ(DossieCliente dossieClientePJ) {
        this.dossieClientePJ = dossieClientePJ;
    }
}
