package br.gov.caixa.simtr.modelo.mapeamento.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import br.gov.caixa.simtr.modelo.enumerator.SIFRCRecomendacaoEnum;

public class SIFRCRecomendacaoAdapter extends XmlAdapter<String, SIFRCRecomendacaoEnum> {

    @Override
    public String marshal(SIFRCRecomendacaoEnum recomendacao) throws Exception {
        return recomendacao.getDescricao();
    }

    @Override
    public SIFRCRecomendacaoEnum unmarshal(String descricao) throws Exception {
        return SIFRCRecomendacaoEnum.getByDescricao(descricao);
    }
}
