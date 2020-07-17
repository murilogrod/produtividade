package br.gov.caixa.simtr.controle.vo;

import java.io.Serializable;

public class ProcessosVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer idProcessoDossie;
    private Integer idProcessoFase;
    
    public ProcessosVO(Integer idProcessoDossie, Integer idProcessoFase) {
        this.idProcessoDossie = idProcessoDossie;
        this.idProcessoFase = idProcessoFase;
    }
    
    public Integer getIdProcessoDossie() {
        return idProcessoDossie;
    }
    
    public void setIdProcessoDossie(Integer idProcessoDossie) {
        this.idProcessoDossie = idProcessoDossie;
    }
    
    public Integer getIdProcessoFase() {
        return idProcessoFase;
    }
    
    public void setIdProcessoFase(Integer idProcessoFase) {
        this.idProcessoFase = idProcessoFase;
    }
}
