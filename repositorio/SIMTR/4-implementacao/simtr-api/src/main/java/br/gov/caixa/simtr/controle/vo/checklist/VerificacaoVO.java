package br.gov.caixa.simtr.controle.vo.checklist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VerificacaoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long identificadorInstanciaDocumento;
    private Integer identificadorChecklist;
    private boolean analiseRealizada;
    // *********************************************
    private List<ParecerApontamentoVO> parecerApontamentosVO;

    public VerificacaoVO() {
        super();
        this.parecerApontamentosVO = new ArrayList<>();
    }

    public Long getIdentificadorInstanciaDocumento() {
        return identificadorInstanciaDocumento;
    }

    public void setIdentificadorInstanciaDocumento(Long identificadorInstanciaDocumento) {
        this.identificadorInstanciaDocumento = identificadorInstanciaDocumento;
    }

    public Integer getIdentificadorChecklist() {
        return identificadorChecklist;
    }

    public void setIdentificadorChecklist(Integer identificadorChecklist) {
        this.identificadorChecklist = identificadorChecklist;
    }

    public boolean isAnaliseRealizada() {
        return analiseRealizada;
    }

    public void setAnaliseRealizada(boolean analiseRealizada) {
        this.analiseRealizada = analiseRealizada;
    }

    public List<ParecerApontamentoVO> getParecerApontamentosVO() {
        return parecerApontamentosVO;
    }

    public void setParecerApontamentosVO(List<ParecerApontamentoVO> parecerApontamentosVO) {
        this.parecerApontamentosVO = parecerApontamentosVO;
    }

    public void addParecerApontamentosVO(ParecerApontamentoVO... parecerApontamentosVO) {
        this.parecerApontamentosVO.addAll(Arrays.asList(parecerApontamentosVO));
    }

    public void removeParecerApontamentosVO(ParecerApontamentoVO... parecerApontamentosVO) {
        this.parecerApontamentosVO.removeAll(Arrays.asList(parecerApontamentosVO));
    }

}
