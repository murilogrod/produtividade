package br.gov.caixa.simtr.controle.vo.excecao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PendenciasVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String campo;
    private final List<String> apontamentos;

    public PendenciasVO(String campo) {
        super();
        this.campo = campo;
        this.apontamentos = new ArrayList<>();
    }

    public PendenciasVO(String campo, List<String> apontamentos) {
        super();
        this.campo = campo;
        this.apontamentos = apontamentos;
    }

    public String getCampo() {
        return campo;
    }

    public List<String> getApontamentos() {
        return apontamentos;
    }

    public void addApontamento(String apontamento) {
        this.apontamentos.add(apontamento);
    }
}
