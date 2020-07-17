package br.gov.caixa.simtr.controle.vo.checklist;

import java.io.Serializable;

public class ParecerApontamentoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long identificadorApontamento;
    private boolean aprovado;
    private String comentario;

    public ParecerApontamentoVO(Long identificadorApontamento, boolean aprovado, String comentario) {
        super();
        this.identificadorApontamento = identificadorApontamento;
        this.aprovado = aprovado;
        this.comentario = comentario;
    }

    public Long getIdentificadorApontamento() {
        return identificadorApontamento;
    }

    public void setIdentificadorApontamento(Long identificadorApontamento) {
        this.identificadorApontamento = identificadorApontamento;
    }

    public boolean isAprovado() {
        return aprovado;
    }

    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

}
