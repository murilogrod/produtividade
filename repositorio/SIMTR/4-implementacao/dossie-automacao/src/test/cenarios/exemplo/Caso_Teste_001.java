/**
 * 
 */
package exemplo;


import org.junit.Test;
import org.junit.runner.RunWith;

import caixa_dossie.GenericCenario;
import caixa_dossie.GenericPageObject;
import driver.AutomacaoRunner;
import login.CenarioRealizarLogin;
import massadedados.LerXls;
import precondicoes.PreCondicao;

/**
 * Delegar a solicitação de serviço para um usuário.
 * 
 * @since 04/10/2018
 */
@RunWith(AutomacaoRunner.class)
public class Caso_Teste_001 extends GenericCenario {
	LerXls xls = new LerXls();
	@Test
	@PreCondicao(alvo = CenarioRealizarLogin.class, casoDeTeste = "realizarLogin")
	public void casoTeste() throws Exception{
		GenericPageObject cenario = new GenericPageObject();
		LerXls.setCasoTeste(Caso_Teste_001.class.getSimpleName());
		cenario.pathRTA("pesquisaRequisicaoServicosIncidentes");
		cenario.acessarMenu();
		cenario.executar(xls.lerPlanilhaElementos());
		cenario.pathRTA("delegacaoRequisicaoServicosIncidentes");
		cenario.executar(xls.lerPlanilhaElementos());
	}
}