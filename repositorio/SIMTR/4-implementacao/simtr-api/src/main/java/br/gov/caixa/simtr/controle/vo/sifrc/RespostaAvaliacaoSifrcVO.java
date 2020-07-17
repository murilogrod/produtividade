package br.gov.caixa.simtr.controle.vo.sifrc;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.gov.caixa.simtr.modelo.enumerator.SIFRCRecomendacaoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.SIFRCRecomendacaoAdapter;

@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.FIELD)
public class RespostaAvaliacaoSifrcVO implements Serializable {

    /**
     * Atributo serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "id_transacao")
    private Long identificadorTransacao;

    @XmlElement(name = "recomendacao")
    @XmlJavaTypeAdapter(value = SIFRCRecomendacaoAdapter.class)
    private SIFRCRecomendacaoEnum recomendacao;

    public RespostaAvaliacaoSifrcVO() {
    }

    public Long getIdentificadorTransacao() {
        return identificadorTransacao;
    }

    public void setIdentificadorTransacao(Long identificadorTransacao) {
        this.identificadorTransacao = identificadorTransacao;
    }

    public SIFRCRecomendacaoEnum getRecomendacao() {
        return recomendacao;
    }

    public void setRecomendacao(SIFRCRecomendacaoEnum recomendacao) {
        this.recomendacao = recomendacao;
    }
}
