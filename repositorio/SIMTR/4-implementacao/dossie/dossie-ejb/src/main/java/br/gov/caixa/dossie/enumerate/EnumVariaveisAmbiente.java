package br.gov.caixa.dossie.enumerate;

public enum EnumVariaveisAmbiente {
	
	VAR_JBOSS_URL_SIMMA("simma.local"),
	VAR_JBOSS_URL_SICPU("sicpu.local"),
	VAR_JBOSS_CAMINHO_ARQUIVOS("simma.videos"),
	VAR_JBOSS_AMBIENTE("simma.ambiente");
	
	private String valor;
	
	private EnumVariaveisAmbiente(String valor) {
		this.valor = valor;
	}
	
	public String getValor() {
		return valor;
	}
}