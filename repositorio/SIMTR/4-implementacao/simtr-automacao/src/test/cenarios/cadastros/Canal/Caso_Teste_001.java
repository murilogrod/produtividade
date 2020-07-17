/**
 * 
 */
package cadastros.Canal;


import org.junit.Test;
import org.junit.runner.RunWith;

import caixa_simtr.GenericCenario;
import driver.AutomacaoRunner;
import login.CenarioRealizarLogin;
import massadedados.LerXls;
import precondicoes.PreCondicao;

/**
 * Cadastrar um Novo canal.
 * 
 * @since 30/06/2020
 */
@RunWith(AutomacaoRunner.class)
public class Caso_Teste_001 extends GenericCenario {
	LerXls xls = new LerXls();
	@Test
	@PreCondicao(alvo = CenarioRealizarLogin.class, casoDeTeste = "realizarLogin")
	public void casoTeste() throws Exception{
		super.casoTeste("canal");
	}
}