package br.gov.caixa.dossie.generic;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.AssertJUnit;

/**.
 * Nome: Helper
 * Objetivo : Classe com metodos acessorios para os testes.
 * @author c112460
 * @version 1.0
 * @since 14/07/2014
 */
public class Helper {
	
	private WebDriver webDriver;
	public static final String TARGET_EVIDENCIAS = "target/evidencias/";
	
	/**
	 * 
	 * M�todo   : esperarPorElementoNaoVisivel.
	 * Descri��o: 
	 * @param by
	 * @throws InterruptedException
	 */
	public void esperarPorElementoNaoVisivel(By by) throws InterruptedException {
		Thread.sleep(5000);
		for(int second = 0;; second++){
			if(second >= 20){
				AssertJUnit.fail("timeout");
			}
			try{
				if(!this.elementoEstaVisivel(by)){
					break;
				}
			}catch(Exception e){
				AssertJUnit.fail("timeout");
			}
			Thread.sleep(1000);
		}
	}
	
	/**.
	 * 
	 * M�todo   : elementoEstaVisivel.
	 * Descri��o: 
	 * @param by
	 * @return
	 */
	public boolean elementoEstaVisivel(By by) {
		try {
			return getWebDriver().findElement(by).isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	public WebDriver getWebDriver() {
		return webDriver;
	}

	public void setWebDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
	}
	
	/**.
	 * Descricao: Metodo responsavel por criar a pasta onde serao armazenadas as screenshots de evidencias
	 * @return String
	 */
	public static String criarPastaParaScreenShots(){
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");
	    Date date = new Date();
	    
	    String screenshotsDir = System.getProperty("diretorioPrints");
	    
	    if(screenshotsDir == null){
			screenshotsDir = TARGET_EVIDENCIAS;
		}
	    
		File dir = new File(screenshotsDir + dateFormat.format(date));
		dir.mkdirs();
		return dir.getPath();
	}
	
	/**.
	 * Metodo responsavel por selecionar uma opcao de uma combo
	 * @param combo
	 * @return WebElement
	 */
	public WebElement selecionaElementoCombo(WebElement combo){
		Random seletor = new Random();
		List<WebElement> options =  new Select(combo).getOptions();
		return options.get(seletor.nextInt(options.size()));
	}
	
	/**.
	 * metodo responsavel por gerar um numero aleatorio ate o maximo informado
	 * Caso o valor seja zero ir� enviar 1
	 * @param maximo
	 * @return Integer
	 */
	public Integer recuperaNumeroRandomico(Integer maximo){
		Random seletor = new Random();
		Integer numero = seletor.nextInt(maximo);
		return numero.intValue() == 0 ? 1 : numero;
	}

}
