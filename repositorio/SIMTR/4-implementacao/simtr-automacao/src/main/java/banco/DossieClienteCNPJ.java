package banco;

import caixa_simtr.SQL;

public class DossieClienteCNPJ extends SQL{
	
	@Override
	public void descarregar() throws Throwable {
		System.out.println("Realizando descarga para o Dossiê Cliente");
		excluirDossieCliente("'52517835667'");
		excluirDossieCliente("'49761334000177'");
		System.out.println("Descarga realizada para o Dossiê Cliente");
	}

}
