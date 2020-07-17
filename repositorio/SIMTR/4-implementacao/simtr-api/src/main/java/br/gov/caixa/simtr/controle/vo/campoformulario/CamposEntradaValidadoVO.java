package br.gov.caixa.simtr.controle.vo.campoformulario;

import java.io.Serializable;

import br.gov.caixa.simtr.modelo.entidade.Garantia;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;

public class CamposEntradaValidadoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Processo processoOrigem;
    private Processo processoFase;
    private TipoRelacionamento tipoRelacionamento;
    private Produto produto;
    private Garantia garantia;
    
    public CamposEntradaValidadoVO(Processo processoOrigem, Processo processoFase, 
                                   TipoRelacionamento tipoRelacionamento, 
                                   Produto produto, Garantia garantia) {
        super();
        this.processoOrigem = processoOrigem;
        this.processoFase = processoFase;
        this.tipoRelacionamento = tipoRelacionamento;
        this.produto = produto;
        this.garantia = garantia;
    }

    public Processo getProcessoOrigem() {
        return processoOrigem;
    }

    public void setProcessoOrigem(Processo processoOrigem) {
        this.processoOrigem = processoOrigem;
    }

    public Processo getProcessoFase() {
        return processoFase;
    }

    public void setProcessoFase(Processo processoFase) {
        this.processoFase = processoFase;
    }

    public TipoRelacionamento getTipoRelacionamento() {
        return tipoRelacionamento;
    }

    public void setTipoRelacionamento(TipoRelacionamento tipoRelacionamento) {
        this.tipoRelacionamento = tipoRelacionamento;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Garantia getGarantia() {
        return garantia;
    }

    public void setGarantia(Garantia garantia) {
        this.garantia = garantia;
    }
}
