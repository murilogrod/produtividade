/**
 * 
 */
package login;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import caixa_dossie.GenericPageObject;
import caixa_dossie.WebDriverSingleton;
import dominio.EnumLog;
import parametrizacao.ParametroUtil;
import util.MensagemLog;

/**
 * Classe que mapea os elementos da tela e os instancia.
 * 
 * @since 07/10/2015
 */
public class PageLoginLogout extends GenericPageObject {

	private static Logger Log = Logger.getLogger("Inicializando");
	private static PageLoginLogout instancia = null;

	public PageLoginLogout() {
		PageFactory.initElements(WebDriverSingleton.getDriver(), this);
	}

	public static PageLoginLogout instancia() {
		synchronized (PageLoginLogout.class) {
			if (instancia == null) {
				instancia = new PageLoginLogout();
			}
		}
		return instancia;
	}

	@FindBy(xpath = "//*[@id='username']")
	private WebElement campoUsuario;

	@FindBy(xpath = "//*[@id='password']")
	private WebElement campoSenha;

	@FindBy(xpath = "//*[@id='kc-login']")
	private WebElement botaoEntrar;


	public void informarUsuario() throws Exception {
		instancia().campoUsuario.sendKeys(ParametroUtil.getValueAsString("usuarioPadrao"));
		Log.info(MensagemLog.getMensagem(EnumLog.MSG_Informou_Usuario, ParametroUtil.getValueAsString("usuarioPadrao")));
	}

	public void informarSenha() throws Exception {
		instancia().campoSenha.sendKeys(ParametroUtil.getValueAsString("senhaPadrao"));
		Log.info(MensagemLog.getMensagem(EnumLog.MSG_Informou_Senha, "*******"));	
	}

	public void solicitarLogin() throws IOException {
		coletarEvidencia();
		instancia().botaoEntrar.click();
		delay(300);
		aguardarElementoNaoEstarPresente(webDriver.findElement(By.xpath("//*[@id='loadingID']")), 40);
		delay(700);
		coletarEvidencia();
		Log.info(MensagemLog.getMensagem(EnumLog.MSG_Clicou_Botao, "Entrar"));

	}

	public void realizarLogout() throws IOException{
		try {
			webDriver.switchTo().window(
					getListaJanelas().get(getListaJanelas().size() - 1));

			webDriver.findElement(By.xpath("//*[@id='toolbar']/ul/li[5]/ul/li[5]/a")).click();
			Log.info(MensagemLog.getMensagem(EnumLog.MSG_Clicou_Botao,"Sair"));
			delay(3500);
			coletarEvidencia();
		} catch (NoSuchElementException nsee) {
			throw new NoSuchElementException(MensagemLog.getMensagem(EnumLog.MSG_Mapeamento_do, 
					"Botão", "Sair"));
		}

	}

}
