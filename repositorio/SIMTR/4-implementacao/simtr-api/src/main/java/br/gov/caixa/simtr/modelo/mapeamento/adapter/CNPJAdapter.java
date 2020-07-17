package br.gov.caixa.simtr.modelo.mapeamento.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CNPJAdapter extends XmlAdapter<String, Long> {

	@Override
	public String marshal(Long cnpj) throws Exception {
		String cnpjFormatado = "00000000000000".concat(String.valueOf(cnpj));
		return cnpjFormatado.substring(cnpjFormatado.length() - 14);
	}

	@Override
	public Long unmarshal(String cpf) throws Exception {
		return Long.valueOf(cpf);
	}
}