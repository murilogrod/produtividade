package br.gov.caixa.simtr.modelo.mapeamento.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import br.gov.caixa.simtr.modelo.enumerator.SIFRCOperacaoEnum;

public class SIFRCOperacaoAdapter extends XmlAdapter<String, SIFRCOperacaoEnum> {

	@Override
	public String marshal(SIFRCOperacaoEnum operacao) throws Exception {
		return operacao.getDescricao();
	}

	@Override
	public SIFRCOperacaoEnum unmarshal(String operacao) throws Exception {
		return SIFRCOperacaoEnum.getByDescricao(operacao);
	}
}