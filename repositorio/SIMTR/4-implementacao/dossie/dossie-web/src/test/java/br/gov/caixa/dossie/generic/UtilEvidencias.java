package br.gov.caixa.dossie.generic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.IResultMap;
import org.testng.ITestResult;

/**
 * 
 * @author CTMONSI
 * @since 05/02/2016
 *
 */
public class UtilEvidencias {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(UtilEvidencias.class);
		
	private static final String ERRO_AO_SALVAR_SCREEN_SHOTS = "Erro ao Salvar ScreenShots";
	
	public static final Map<String, ITestResult> RESULTADOS = new HashMap<String, ITestResult>();	
	public static final List<ResultadoTeste> LINHASRELATORIO = new ArrayList<ResultadoTeste>();	
	public static final List<String> TESTCONTEXTNAME = new ArrayList<String>();
	public static final List<String> TESTRESULTNAMES = new ArrayList<String>();	
	
	/**.
	 * Descricao: Metodo responsavel por tirar evidencia da tela que esta sendo testada
	 * @param nomeArquivo
	 * @param subDiretorio
	 * @param webDriver
	 */
	public static void tirarEvidencia(String nomeArquivo, String subDiretorio, WebDriver webDriver){
		
		WebDriver augmentedDriver = new Augmenter().augment(webDriver);
		File scrFile = ((TakesScreenshot)augmentedDriver).
        getScreenshotAs(OutputType.FILE);
		  
		salvarEvidencia(scrFile, subDiretorio +"/" + nomeArquivo + ".png");
	}
	
	/**.
	 * Descricao: Metodo responsavel por salvar evidencia retirada
	 * @param arquivo
	 * @param nomeArquivo
	 */
	public static void salvarEvidencia(File arquivo, String nomeArquivo){
        File outputFile = new File(GenericTest.getScreenshotsFolder(), nomeArquivo);
        try {
            FileUtils.copyFile(arquivo, outputFile);
        } catch (IOException ioe) {
        	LOGGER.error(ERRO_AO_SALVAR_SCREEN_SHOTS + ioe);
        	Assert.fail(ERRO_AO_SALVAR_SCREEN_SHOTS + ioe.getMessage());
        }
    }
		
	public static void adicionaResultados(){
		for(String testResultName : TESTRESULTNAMES){
			ResultadoTeste linha = new ResultadoTeste();
			for(String testContextName : TESTCONTEXTNAME){
				StringBuilder chave = new StringBuilder(testResultName).append("_").append(testContextName);
				ITestResult testResult = RESULTADOS.get(chave.toString());
				if(testResult!=null){
					testResult.setAttribute("testContextName", testContextName);
				}
				linha.getColunas().add(testResult);
			}
			LINHASRELATORIO.add(linha);
		}
	}
	
	
	
	/**.
	 * Dexcricao: Metodo responsavel por coletar os resultados dos testes
	 * @param testContextName
	 * @param results
	 */
	public static void coletaResultados(String testContextName, IResultMap results){
        for(ITestResult testResult : results.getAllResults()){
			StringBuilder chave = new StringBuilder(testResult.getName()).append("_").append(testContextName);
			RESULTADOS.put(chave.toString(), testResult);
        }
	}
			
	public static String getDetalhes(ITestResult r) {
		StringBuilder sb = new StringBuilder();
		if(r.getThrowable()!=null && r.getThrowable().getLocalizedMessage()!=null){
			String localizedMessage = r.getThrowable().getLocalizedMessage().
											replace("'", "").
											replace("<", "(").
											replace(">",")").
											replaceAll("\\r|\\n", "<br/>").
											replace("\"", "&quot;");
			sb.append("<br/><strong>").append(localizedMessage).append("</strong><br/><br/>");
			if(r.getThrowable().getLocalizedMessage()!=null){
				for (StackTraceElement ste : r.getThrowable().getStackTrace()) {
					String stackLine = ste.toString().
										replace("'", "").
										replace("<", "(").
										replace(">",")").
										replaceAll("\\r|\\n", "<br/>").
										replace("\"", "&quot;");
					sb.append(stackLine).append("<br/>");
				}
			}
		}
		return sb.toString();
	}
}
