package br.gov.caixa.simtr.modelo.mapeamento.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CGCUnidadeAdapter extends XmlAdapter<String, Integer> {

	@Override
	public String marshal(Integer cgc) throws Exception {
		String cgcFormatado = "0000".concat(String.valueOf(cgc));
		return cgcFormatado.substring(cgcFormatado.length() - 4);
	}

	@Override
	public Integer unmarshal(String cgc) throws Exception {
		return Integer.valueOf(cgc);
	}
}