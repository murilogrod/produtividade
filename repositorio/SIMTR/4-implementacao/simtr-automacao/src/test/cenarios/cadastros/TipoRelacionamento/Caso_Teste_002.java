/**
 * 
 */
package cadastros.TipoRelacionamento;


import org.junit.Test;
import org.junit.runner.RunWith;

import caixa_simtr.GenericCenario;
import driver.AutomacaoRunner;
import login.CenarioRealizarLogin;
import massadedados.LerXls;
import precondicoes.PreCondicao;

/**
 * Cadastrar um tipo de relacionamento do tipo pessoa Pessoa Física.
 * 
 * @since 14/07/2020
 */
@RunWith(AutomacaoRunner.class)
public class Caso_Teste_002 extends GenericCenario {
	LerXls xls = new LerXls();
	@Test
	@PreCondicao(alvo = CenarioRealizarLogin.class, casoDeTeste = "realizarLogin")
	public void casoTeste() throws Exception{
		super.casoTeste("TipoRelacionamento");
	}
}