package br.gov.caixa.simtr.modelo.mapeamento.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CPFAdapter extends XmlAdapter<String, Long> {

	@Override
	public String marshal(Long cpf) throws Exception {
		String cpfFormatado = "00000000000".concat(String.valueOf(cpf));
		return cpfFormatado.substring(cpfFormatado.length() - 11);
	}

	@Override
	public Long unmarshal(String cpf) throws Exception {
		return Long.valueOf(cpf);
	}
}