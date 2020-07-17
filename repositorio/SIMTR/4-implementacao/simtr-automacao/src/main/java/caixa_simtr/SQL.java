package caixa_simtr;

public class SQL extends AbstractCargaSIMTR {
	
	protected void excluirDossieCliente(String cpf_cnpj) throws Throwable{
		Long nu_dossie_cliente = consultaSQL("select nu_dossie_cliente from mtr.mtrtb001_dossie_cliente where nu_cpf_cnpj = "+cpf_cnpj);
		
		executarSQL("delete from mtr.mtrtb001_pessoa_fisica where nu_dossie_cliente = "+nu_dossie_cliente);
		executarSQL("delete from mtr.mtrtb001_pessoa_juridica where nu_dossie_cliente = "+nu_dossie_cliente);
		executarSQL("delete from mtr.mtrtb001_dossie_cliente where nu_cpf_cnpj = "+cpf_cnpj);
	}

}
