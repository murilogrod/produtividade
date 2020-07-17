package br.gov.caixa.dossie.generic;

import java.util.ArrayList;
import java.util.List;

import org.testng.ITestResult;

/**.
 * Descricao: Classe responsavel por armazenar lista com resultados dos testes
 * Nome: ResultadoTeste
 * @author c112460
 * @since 21/08/2013
 * @version 1.0
 */
public class ResultadoTeste {
	
	private List<ITestResult> colunas = new ArrayList<ITestResult>();

	public List<ITestResult> getColunas() {
		return colunas;
	}

	public void setColunas(List<ITestResult> colunas) {
		this.colunas = colunas;
	}

}
