/**
 * 
 */
package dossieCliente;


import org.junit.Test;
import org.junit.runner.RunWith;

import banco.DossieClienteCNPJ;
import caixa_simtr.GenericCenario;
import dadosdeteste.Carga;
import driver.AutomacaoRunner;
import login.CenarioRealizarLogin;
import massadedados.LerXls;
import precondicoes.PreCondicao;

/**
 * Incluir Dossiê para um CNPJ válido não existente na base do SICPF.
 * 
 * @since 28/08/2019
 */
@RunWith(AutomacaoRunner.class)
public class Caso_Teste_004 extends GenericCenario {
	LerXls xls = new LerXls();
	@Test
	@PreCondicao(alvo = CenarioRealizarLogin.class, casoDeTeste = "realizarLogin")
	@Carga(alvo = DossieClienteCNPJ.class)
	public void casoTeste() throws Exception{
		super.casoTeste("dossieCliente_Pesquisa");
	}
}