package br.gov.caixa.dossie.generic;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**.
 * Descricao: Classe utilitaria de apoio as classes de teste
 * Nome: Util
 * @author c112460
 * @version 1.0
 * @since 21/08/2013
 */
public class Util {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(Util.class);
		
	public static final String TARGET_EVIDENCIAS = "target/evidencias/";
		
	/**.
	 * Descricao: Configura propriedades expecificas para o firefox
	 * @param extraCapabilities
	 */
	public static void setFirefoxProfile(DesiredCapabilities extraCapabilities) {
		FirefoxProfile profile = new FirefoxProfile();
			
		profile.setPreference("browser.cache.disk.enable", false);
		profile.setPreference("browser.cache.memory.enable", false);
		profile.setPreference("browser.cache.offline.enable", false);
		profile.setPreference("network.http.use-cache", false);
		
		extraCapabilities.setCapability(FirefoxDriver.PROFILE, profile);
	
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
			
	/**
	 * Descricao:  Metodo respons�vel por verificar se o metodo � um metodo de teste. 
	 * Nome: isMetodoTeste
	 * @param m
	 * @return boolean
	 */
	public static boolean isMetodoTeste(Method m) {
		for(Annotation annotation : m.getAnnotations()){
			if(annotation.annotationType().toString().contains("org.testng.annotations.Test")){
				return true;
			}
		}
		return Boolean.FALSE;
	}
	
}
