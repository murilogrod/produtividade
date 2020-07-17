/**
 * 
 */
package exemplo;

import org.junit.Test;
import org.junit.runner.RunWith;

import caixa_dossie.GenericCenario;
import driver.AutomacaoRunner;
import precondicoes.PreCondicao;
import precondicoes.PreCondicoes;

/**
 * Executa todos os casos de teste do RTA.
 * 
 * @since 04/10/2018
 */
@RunWith(AutomacaoRunner.class)
public class SuiteExemplo extends GenericCenario{
	
	@Test
	@PreCondicoes(preCondicoes = {
		    @PreCondicao(alvo = Caso_Teste_001.class, casoDeTeste = "casoTeste"),
			})
	public void suiteTest(){
	}

}
