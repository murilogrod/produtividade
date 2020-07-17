package br.gov.caixa.dossie.generic;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.AssertJUnit;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

/**.
 * Nome: GenericTest
 * Objetivo : Classe com metodos acessorios para os testes.
 * @author c112460
 * @version 1.0
 * @since 21/08/2013
 */
public class GenericTest extends Helper {

	private static final String MSG_OPERANDO_NO_AMBIENTE = "Operando no ambiente ";
	private static final String ATRIBUTO_VALUE = "value";
	private static final String MODAL_SIOGP_CSS_SELECTOR = "#AlertFooter > button.btn.btn-primary";
	
	private static final String PWD_SIPRO = "pwd.projeto";
	private static final String KEY_SIPRO = "key.projeto";
	private static final String USR_SIPRO = "usr.projeto";
	private static final String URL_SIPRO = "url.projeto";
	private static final String URL_SISGR = "url.sisgr";	
	private static final String HABILITA_SISGR = "habilita.sisgr";
	
	private static final String SIITA_PLANILHAS = "siita.caminhoPlanilhas";
	private static final String SIITA_AMBIENTE = "siita.ambiente";
	private static final String SIITA_FERIADO = "siita.caminhoFeriado";
	
	private static final String AMBIENTE_LOCAL = "AMBIENTE_LOCAL";
	private static final String LINK_SUBMIT = "a";
	private static final String SUBMIT = "submit";
	private static final String SENHA = "senha";
	private static final String USUARIO = "usuario";
	
	private static final String FIM_DE_CENARIO_DE_TESTE = "[Fim do Caso de Teste]: "; 
	private static final String UNDERLINE = "_";
	private static final String INICIO_DE_CENARIO_DE_TESTE = "[Inicio Caso de Teste]: "; 
	private static final String OCORREU_UM_ERRO_INESPERADO = "Ocorreu um erro inesperado. ";
	private static final String OCORREU_UM_ERRO_AO_INSTANCIAR_O_HUB_DO_SELENIUM = "Ocorreu um erro ao instanciar o hub do Selenium. ";
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericTest.class);
	private static final String SCREENSHOTSFOLDER = Util.criarPastaParaScreenShots();
	private static final String BASE_URL = System.getProperty("base.url.porta");
	
	protected String caminhoPlanilhas;
	protected String caminhoFeriado;
	protected String ambiente;
	
	private static WebDriver webDriver;
	
	/**.
	 * Descricao: Metodo responsavel por instanciar o webdrive e configurar os browsers a serem utilizados nos testes
	 * @param browser
	 * @param platform
	 * @param version
	 * @param hubUrlFabrica
	 * @param hubUrlCaixa
	 * @since 21/08/2013
	 */
	@Parameters({ "browser", "platform", "version", "hubUrlFabrica", "hubUrlCaixa", "proxyHost" })
	@BeforeClass
	public void iniciaTestes(String browser, String platform, String version, String hubUrlFabrica, String hubUrlCaixa, String proxyHost) {		
		try {

			DesiredCapabilities extraCapabilities = new DesiredCapabilities();
			extraCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			extraCapabilities.setBrowserName(browser);
			
			if ("windows".equals(platform)){
				extraCapabilities.setPlatform(Platform.WINDOWS);
			}else if ("linux".equals(platform)){
				extraCapabilities.setPlatform(Platform.LINUX);
			}else if ("mac".equals(platform)){
				extraCapabilities.setPlatform(Platform.MAC);
			}
			
			if(!"".equals(proxyHost.trim())){
				Proxy proxy = new Proxy();
				proxy.setHttpProxy(proxyHost)
					 .setFtpProxy(proxyHost)
					 .setSslProxy(proxyHost);
				extraCapabilities.setCapability(CapabilityType.PROXY, proxy);
			}
			
			extraCapabilities.setVersion(version);
				
			if("firefox".equalsIgnoreCase(browser)){
				Util.setFirefoxProfile(extraCapabilities);
			}
			
			if("chrome".equalsIgnoreCase(browser)){
				Map<String, Object> chromeOptions = new HashMap<String, Object>();
				chromeOptions.put("binary", "C:\\PortableApps\\GoogleChromePortable\\GoogleChromePortable.exe");
				extraCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
			}
			
			try {
                
				boolean ambienteCaixa = System.getProperty(AMBIENTE_LOCAL) != null ? Boolean.FALSE : Boolean.TRUE;
				
				webDriver = new RemoteWebDriver(new URL(ambienteCaixa ? hubUrlCaixa : hubUrlFabrica),extraCapabilities);
				webDriver.manage().window().maximize();
				webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				
			} catch (Exception e) {
				LOGGER.error(OCORREU_UM_ERRO_AO_INSTANCIAR_O_HUB_DO_SELENIUM,e);
			}
		} catch (Exception e) {
			LOGGER.error(OCORREU_UM_ERRO_INESPERADO,e);
		}
		
	}
	
	/**.
	 * Descricao: Metodo responsavel por finalizar o webdriver apos a execucao do teste
	 * @param context
	 * @throws Exception
	 */
	@AfterClass
	public void encerraTestes(ITestContext context) throws Exception {
		webDriver.quit();
		webDriver = null;
	}
	
	/**.
	 * Metodo responsavel por logar inicio dos testes 
	 * @param metodo
	 */
	@BeforeMethod
	public void beforeMethod(Method metodo){
		LOGGER.info(INICIO_DE_CENARIO_DE_TESTE + metodo.getName());
	}
	
	/**.
	 * Descricao: Metodo resposnavel por tirar um print da tela apos execucao de um metodo de teste
	 * @param metodo
	 * @param context
	 */
	@AfterMethod
	public void afterMethod(Method metodo, ITestContext context){
		
		//StringBuilder nomeFigura = new StringBuilder();
		//nomeFigura.append(metodo.getName()).append(UNDERLINE).append(context.getName());
		
		String nomeFigura = metodo.getName()+UNDERLINE+context.getName();
		
		UtilEvidencias.tirarEvidencia(nomeFigura, metodo.getName(),webDriver);
		LOGGER.info(FIM_DE_CENARIO_DE_TESTE + metodo.getName());
	}
	
	public static String getScreenshotsFolder(){
		return SCREENSHOTSFOLDER;
	}

	public WebDriver getWebDriver() {
		return webDriver;
	}

	public String getBaseUrl() {
		return BASE_URL;
	}

	/**
	 * Metodo responsavel por inicializar a aplica��o.
	 * Ser� verificado se o SISGR est� Habilitado.
	 * O mesmo deve ser habilitado ou n�o e ter sua Url cadastrada no Arquivo POM.xml.
	 * @throws InterruptedException
	 */
	public void inicializaAplicacao() throws InterruptedException{
		
		carregarParametrosSiita();
		
		if(Boolean.parseBoolean(System.getProperty(HABILITA_SISGR)) || Boolean.parseBoolean(System.getenv(HABILITA_SISGR))){
			realizaLoginSISGR();
		}else{
			String url = System.getProperty(URL_SIPRO) == null ? System.getenv(URL_SIPRO) : System.getProperty(URL_SIPRO);
			getWebDriver().get(url);
			Thread.sleep(2000);
		}
	}

	private void carregarParametrosSiita() {
		caminhoPlanilhas = System.getProperty(SIITA_PLANILHAS);
		caminhoFeriado = System.getProperty(SIITA_FERIADO);
		ambiente = System.getProperty(SIITA_AMBIENTE);
		
		if (ambiente!=null) {
			LOGGER.info(MSG_OPERANDO_NO_AMBIENTE + ambiente);
		}
	}

	/**
	 * Metodo Responsavel por autenticar o usuario de teste no SISGR
	 * A Url do SISGR de cada ambiente deve estar configurada no arquivo POM.xml em seu respectivo profile
	 * @throws InterruptedException
	 */
	private void realizaLoginSISGR() throws InterruptedException {
		
		/* A variavel abaixo deve ter seu nome alterado, trocando simodelo para o nome do sistema real.
		 * Ex: url.sifug, url.sipct 
		 * */ 
		
		String url = System.getProperty(URL_SISGR) == null ? System.getenv(URL_SISGR) : System.getProperty(URL_SISGR);
		getWebDriver().get(url);
		Thread.sleep(2000);
		
		/* As variaveis abaixo devem ter seus nomes alterados, trocando simodelo para o nome do sistema real.
		 * Ex: usr.sifug, usr.sipct, pwd.sifug, pwd.sipct 
		 * */
		
		String userPWD = null;
		
		if(System.getenv(KEY_SIPRO) == null && System.getProperty(KEY_SIPRO) == null){
			userPWD = System.getProperty(PWD_SIPRO) == null ? System.getenv(PWD_SIPRO) : System.getProperty(PWD_SIPRO);
		}else{
			Criptografia criptografia = new Criptografia();
			criptografia.addCryptoKey(System.getProperty(KEY_SIPRO) == null ? System.getenv(KEY_SIPRO) : System.getProperty(KEY_SIPRO));
			userPWD = criptografia.decode(System.getProperty(PWD_SIPRO) == null ? System.getenv(PWD_SIPRO) : System.getProperty(PWD_SIPRO));
		}
		
		getWebDriver().findElement(By.id(USUARIO)).sendKeys(System.getenv(USR_SIPRO));
		getWebDriver().findElement(By.id(SENHA)).sendKeys(userPWD);
		Thread.sleep(2000);
		
		getWebDriver().findElement(By.className(SUBMIT)).findElements(By.tagName(LINK_SUBMIT)).get(0).click();		
	}
		
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
	
	protected void clickElementByCssSelector(String classElement){
		getWebDriver().findElement(By.cssSelector(classElement)).click();		
	}
	
	
	protected void clickElementByLinkTextId(String idLink){
		getWebDriver().findElement(By.linkText(idLink)).click();
	}
	
	protected void clickElementById(String idElement){
		
		JavascriptExecutor jse = (JavascriptExecutor)getWebDriver();					
		jse.executeScript("var element = document.getElementById('" + idElement+ "');element.scrollIntoView(false);element.click();");
				
	}
	
	protected void clickElementByIdTratamentoPopup(String idElement){
		
		clickElementById(idElement);
		
		try {
			if (elementoEstaVisivel(By.cssSelector(MODAL_SIOGP_CSS_SELECTOR))) {
				clickElementByCssSelector(MODAL_SIOGP_CSS_SELECTOR);
			}
			inicializaAplicacao();
		}catch (Exception e) {
			LOGGER.warn(e.getMessage());
		}		
	}
	
	protected void clickElementByIdOriginal(String idElement){
		getWebDriver().findElement(By.id(idElement)).click();		
	}
		
	protected void clickCheckById(String id, String valor){
		List<WebElement> caixaMarcacaoOuRadio = getWebDriver().findElements(By.id(id));
		int tamanho = caixaMarcacaoOuRadio.size();
		for(int i=0; i<tamanho; i++ ){
			String valorInput = caixaMarcacaoOuRadio.get(i).getAttribute(ATRIBUTO_VALUE);
			if (valorInput.equals(valor)){
				caixaMarcacaoOuRadio.get(i).click();
			}
		}
	}
	
	protected void clickCheckByXpath(String id, String valor){
		String xpath = "(//input[@id='" + id + "'][" + valor +"])";		
		clickElementByXpath(xpath);
	}
	
	protected void clickRadioByXpath(String id, String valor){
		String xpath = "(//button[@id='" + id + "'][" + valor +"])";		
		clickElementByXpath(xpath);
	}	
	
	protected void clickElementByXpath(String xpath){
		getWebDriver().findElement(By.xpath(xpath)).click();
	}
	
	protected void clearElementById(String idElement){		
		JavascriptExecutor jse = (JavascriptExecutor)getWebDriver();
		jse.executeScript("arguments[0].scrollIntoView()", getWebDriver().findElement(By.id(idElement)));				
		getWebDriver().findElement(By.id(idElement)).clear();
	}
	
	protected void inputTextById(String idElement, String text){			
		getWebDriver().findElement(By.id(idElement)).sendKeys(text);		
	}
	
	protected void selectComboByIdIndex(String idElement, Integer position){
		(new Select(getWebDriver().findElement(By.id(idElement)))).selectByIndex(position);
	}
	
	protected void selectComboByVisibleText(String idElement, String valor){
		clickElementByIdOriginal(idElement);		
		(new Select(getWebDriver().findElement(By.id(idElement)))).selectByValue(valor);
	}
	
	protected void inFrameByXpath(String xpath){
		getWebDriver().switchTo().frame(getWebDriver().findElement(By.xpath(xpath)));
	}
	
	protected void defaultContent(){
		getWebDriver().switchTo().defaultContent();
	}
	
	protected void sleep(long tempo) throws InterruptedException{
		Thread.sleep(tempo);
	}
}
