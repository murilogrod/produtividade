/**
 * 
 */
package cadastros.Canal;

import org.junit.Test;
import org.junit.runner.RunWith;

import caixa_simtr.GenericCenario;
import driver.AutomacaoRunner;
import precondicoes.PreCondicao;
import precondicoes.PreCondicoes;

/**
 * Executa todos os casos de teste do RTA.
 * 
 * @since 30/06/2020
 */
@RunWith(AutomacaoRunner.class)
public class SuiteCanal extends GenericCenario{
	
	@Test
	@PreCondicoes(preCondicoes = {
		    @PreCondicao(alvo = Caso_Teste_001.class, casoDeTeste = "casoTeste")
		    
		  			})
	public void suiteTest(){
	}

}
