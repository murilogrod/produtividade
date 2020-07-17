/**
 * 
 */
package dossieCliente;


import org.junit.Test;
import org.junit.runner.RunWith;

import caixa_simtr.GenericCenario;
import driver.AutomacaoRunner;
import login.CenarioRealizarLogin;
import massadedados.LerXls;
import precondicoes.PreCondicao;

/**
 * Infornar número de CPF inválido.
 * 
 * @since 28/08/2019
 */
@RunWith(AutomacaoRunner.class)
public class Caso_Teste_001 extends GenericCenario {
	LerXls xls = new LerXls();
	@Test
	@PreCondicao(alvo = CenarioRealizarLogin.class, casoDeTeste = "realizarLogin")
	public void casoTeste() throws Exception{
		super.casoTeste("dossieCliente_Pesquisa");
	}
}