/**
 * 
 */
package login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import caixa_simtr.GenericPageObject;
import caixa_simtr.WebDriverSingleton;
import dominio.EnumLog;
import log.Log;
import parametrizacao.ParametroUtil;
import util.MensagemLog;

/**
 * Classe que mapea os elementos da tela e os instancia.
 * 
 * @since 28/12/2018
 */
public class PageLoginLogout extends GenericPageObject {

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
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Informou_Usuario, ParametroUtil.getValueAsString("usuarioPadrao")));
		//Log.infoManualUsuario(MensagemLog.getMensagem(EnumLog.MSG_Informou_Usuario, ParametroUtil.getValueAsString("usuarioPadrao")));
	}

	public void informarSenha() throws Exception {
		delay(200);
		instancia().campoSenha.sendKeys(ParametroUtil.getValueAsString("senhaPadrao"));
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Informou_Senha, "*******"));
		//Log.infoManualUsuario(MensagemLog.getMensagem(EnumLog.MSG_Informou_Senha, "*******"));
	}

	public void solicitarLogin() throws Exception {
		coletarEvidencia();
		instancia().botaoEntrar.click();
		delay(300);
		aguardarElementoNaoEstarPresente(webDriver.findElement(By.xpath("//*[@id='body-load']")), 20);
		coletarEvidencia();
		Log.infoLog(MensagemLog.getMensagem(EnumLog.MSG_Clicou_Botao, "Entrar"));
		//Log.infoManualUsuario(MensagemLog.getMensagem(EnumLog.MSG_Clicou_Botao, "Entrar"));
	}
}
