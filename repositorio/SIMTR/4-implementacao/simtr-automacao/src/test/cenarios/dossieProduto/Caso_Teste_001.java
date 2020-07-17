/**
 * 
 */
package dossieProduto;


import org.junit.Test;
import org.junit.runner.RunWith;

import caixa_simtr.GenericCenario;
import driver.AutomacaoRunner;
import login.CenarioRealizarLogin;
import massadedados.LerXls;
import precondicoes.PreCondicao;

/**
 * Validar o Envio de um dossie produto sem informar os campos necessários.
 * 
 * @since 28/10/2019
 */
@RunWith(AutomacaoRunner.class)
public class Caso_Teste_001 extends GenericCenario {
	LerXls xls = new LerXls();
	@Test
	@PreCondicao(alvo = CenarioRealizarLogin.class, casoDeTeste = "realizarLogin")
	public void casoTeste() throws Exception{
		super.casoTeste("dossieProduto_Pesquisa");
	}
}