/**
 * 
 */
package dossieProduto;

import org.junit.Test;
import org.junit.runner.RunWith;

import caixa_simtr.GenericCenario;
import driver.AutomacaoRunner;
import precondicoes.PreCondicao;
import precondicoes.PreCondicoes;

/**
 * Executa todos os casos de teste do RTA.
 * 
 * @since 28/10/2019
 */
@RunWith(AutomacaoRunner.class)
public class SuiteDossieCliente extends GenericCenario{
	
	@Test
	@PreCondicoes(preCondicoes = {
		    @PreCondicao(alvo = Caso_Teste_001.class, casoDeTeste = "casoTeste"),
			})
	public void suiteTest(){
	}

}
