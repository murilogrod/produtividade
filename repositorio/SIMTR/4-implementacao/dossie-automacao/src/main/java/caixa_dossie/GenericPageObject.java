/**
 * 
 */
package caixa_dossie;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import driver.AbstractCenario;
import massadedados.LerXls;
import parametrizacao.MassaDadosUtil;
import parametrizacao.ParametroUtil;
import report.Report;
import util.MensagemLog;
import util.MensagemUtil;
import util.Relogio;

/**
 * 
 * @since 16/06/2015
 */
public class GenericPageObject extends AbstractCenario {

	private MensagemLog log = new MensagemLog();
	private MensagemUtil msg = new MensagemUtil();
	private static String habilitarColetaEvidenciasTeste = ParametroUtil.getValueAsString("coletaEvidenciasTeste");
	private LerXls xls = new LerXls();
	static Report report = new Report();
	private String pathRTA1;
	private String pathRTA2;

	public void pathRTA(String rta) throws Exception {
		LerXls.setPath(MassaDadosUtil.getValueAsString(rta));
/*		if (!AbstractCenario.getCasoDeTesteAtual().getDeclaringClass().getPackage().getName().contains("instalacao")) {
			xls.inserirPlanVersao(
					webDriver.findElement(By.xpath("//*[@class='cit--footer-container-right-version']")).getText());
		} else if (AbstractCenario.getCasoDeTesteAtual().getDeclaringClass().getPackage().getName()
				.contains("instalacao")) {
			xls.inserirPlanVersao(webDriver.findElement(By.xpath("//*[@id='footer-inner']/div[1]/p/b")).getText());
		}*/
	}

	protected List<String> getListaJanelas() {
		String[] janelas = WebDriverSingleton.getDriver().getWindowHandles().toArray(new String[0]);
		List<String> listaJanelas = new ArrayList<String>();
		for (String janela : janelas) {
			listaJanelas.add(janela);
		}
		return listaJanelas;
	}

	public void mudarFrame(WebElement frame) {
		webDriver.switchTo().frame(frame);
	}

	public void mudarFrameDefault() {
		webDriver.switchTo().defaultContent();
	}

	public String getPathTratado(String casoTeste) {
		return casoTeste.split("\\.")[casoTeste.split("\\.").length - 2].substring(0, 1).toLowerCase()
				+ casoTeste.split("\\.")[casoTeste.split("\\.").length - 2].substring(1);
	}

	private void atualizarRelogio() {
		if (!getCasoDeTesteAtual().getDeclaringClass().getSimpleName().contains("Auxiliar")
				&& !getCasoDeTesteAtual().getDeclaringClass().getSimpleName().equals("Caso_Teste_001")) {
			Relogio.incremetarNumeroCasosTesteAtual();
			Relogio.executarRelogio();
		}
		delay(1000);
		coletarEvidencia();
		delay();
	}

	protected void mouseOverElement(WebElement element) {
		String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
		((JavascriptExecutor) webDriver).executeScript(mouseOverScript, element);
	}

	public String getElement2(String tipoLocalizacaoElemento, String localizacaoElemento) {
		if (tipoLocalizacaoElemento.equals("outros")) {
			return localizacaoElemento;
		}
		return null;
	}

	public WebElement getElement(String tipoLocalizacaoElemento, String localizacaoElemento) {
		if (tipoLocalizacaoElemento.equals("xpath")) {
			aguardarElementoEstarPresente(webDriver.findElement(By.xpath(localizacaoElemento)), 3);
			return webDriver.findElement(By.xpath(localizacaoElemento));
		}
		if (tipoLocalizacaoElemento.equals("id")) {
			aguardarElementoEstarPresente(webDriver.findElement(By.id(localizacaoElemento)), 3);
			return webDriver.findElement(By.id(localizacaoElemento));
		}
		if (tipoLocalizacaoElemento.equals("css")) {
			aguardarElementoEstarPresente(webDriver.findElement(By.cssSelector(localizacaoElemento)), 3);
			return webDriver.findElement(By.cssSelector(localizacaoElemento));
		}
		if (tipoLocalizacaoElemento.equals("class")) {
			aguardarElementoEstarPresente(webDriver.findElement(By.className(localizacaoElemento)), 3);
			return webDriver.findElement(By.className(localizacaoElemento));
		}
		if (tipoLocalizacaoElemento.equals("name")) {
			aguardarElementoEstarPresente(webDriver.findElement(By.name(localizacaoElemento)), 3);
			return webDriver.findElement(By.name(localizacaoElemento));
		}
		if (tipoLocalizacaoElemento.equals("linkText")) {
			aguardarElementoEstarPresente(webDriver.findElement(By.linkText(localizacaoElemento)), 3);
			return webDriver.findElement(By.linkText(localizacaoElemento));
		}
		if (tipoLocalizacaoElemento.equals("tagName")) {
			aguardarElementoEstarPresente(webDriver.findElement(By.tagName(localizacaoElemento)), 3);
			return webDriver.findElement(By.tagName(localizacaoElemento));
		}
		return null;
	}

	@SuppressWarnings("unused")
	public void executar(final ArrayList<String> arrayList) throws Exception {
		List<String> valoresRTA = montarListaParaAlvo(arrayList);
		for (int i = 0; i < valoresRTA.size(); i++) {
			String valor = xls.lerPlanilha(valoresRTA.get(i));
			if (xls.naox(valor)) {
				verificaLoadPresente("//*[@id='loadingID']");
				WebElement elemento = null;
				String elemento2 = null;
				String tipoElemento = xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 5);
				if (xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 7).equals("outros")) {
					elemento2 = getElement2(xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 7),
							xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 8));
				} else {
					elemento = getElement(xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 7),
							xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 8));
				}
				String navegarHTML = xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 9);
				String valorEsperadoElemento = xls.lerPlanilhaDadosElementos(valoresRTA.get(i),
						LerXls.getLinhaCasoTeste());
				if (tipoElemento.equals("frame")) {
					delay(500);
					if (elemento2 == null) {
						mudarFrame(elemento);
					} else {
						mudarFrameDefault();
					}
				}
				if (tipoElemento.equals("acoes")) {
					delay(500);
					try {
						if (valor.toLowerCase().equals("f5")) {
							elemento.sendKeys(Keys.F5);
							delay(2000);
						}
						if (valor.toLowerCase().equals("tab")) {
							elemento.sendKeys(Keys.TAB);
							delay(200);
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException();
					}
				} else if (tipoElemento.equals("geraTexto")) {
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					Verify_Dossie.assertEquals(
							msg.obterMsgComparacaoCampo(valoresRTA.get(i).split("_")[0],
									getCasoDeTesteAtual().getDeclaringClass().getSimpleName().toString()),
							valorEsperadoElemento, elemento.getAttribute("value"));
					log.comparouCampo(valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", ""));
					geraString(Integer.parseInt(valor));
					log.informouCampoComParametro(valoresRTA.get(i).split("_")[0], valor);
				} else if (tipoElemento.equals("geraTexto ckeditor javaScript")) {
					delay(200);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});",
							webDriver.findElement(By.xpath(xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 9))));
					((JavascriptExecutor) webDriver)
							.executeScript("CKEDITOR.instances['" + elemento.getAttribute("id").split("_")[1]
									+ "'].setData('" + geraString(Integer.parseInt(valor)) + "')");
					log.informouCampoComParametro(valoresRTA.get(i).split("_")[0], valor);
				} else if (tipoElemento.equals("text")) {
					delay(500);
					try {
						if (AbstractCenario.getCasoDeTestePrincipal().getDeclaringClass().getSimpleName()
								.contains("Suite")) {
							elemento.clear();
						}
						((JavascriptExecutor) webDriver).executeScript(
								"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
						Verify_Dossie.assertEquals(
								msg.obterMsgComparacaoCampo(valoresRTA.get(i).split("_")[0],
										getCasoDeTesteAtual().getDeclaringClass().getSimpleName().toString()),
								valorEsperadoElemento, elemento.getAttribute("value"));
						log.comparouCampo(valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", ""));
						if (!valor.equals("") && !elemento.getAttribute("value").equals(valor)) {
							elemento.clear();
							elemento.sendKeys(valor);
							log.informouCampoComParametro(valoresRTA.get(i).split("_")[0], valor);
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoCampoIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				} else if(tipoElemento.equals("label")) {
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					Verify_Dossie.assertEquals(
							msg.obterMsgComparacaoCampo(valoresRTA.get(i).split("_")[0],
									getCasoDeTesteAtual().getDeclaringClass().getSimpleName().toString()),
							valorEsperadoElemento, elemento.getAttribute("value"));
					log.comparouCampo(valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", ""));
				} else if (tipoElemento.equals("text grid")) {
					delay(500);
					try {
						((JavascriptExecutor) webDriver).executeScript(
								"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
						Verify_Dossie.assertEquals(
								msg.obterMsgComparacaoCampo(valoresRTA.get(i).split("_")[0],
										getCasoDeTesteAtual().getDeclaringClass().getSimpleName().toString()),
								valorEsperadoElemento, elemento.getAttribute("value"));
						log.comparouCampo(valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", ""));
						if (!valor.equals("") && !elemento.getAttribute("value").equals(valor)) {
							elemento.clear();
							elemento.sendKeys(valor);
							delay(200);
							elemento.sendKeys(Keys.TAB);
							log.informouCampoComParametro(valoresRTA.get(i).split("_")[0], valor);
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoCampoIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				} /*else if (tipoElemento.equals("upload")) {
					delay(500);
					try {
						if (!valor.equals("")) {
							aguardarElementoEstarPresente(elemento, 2);
							elemento.click();
							Pattern nome = new Pattern(ParametroUtil.getValueAsString("imagemNome"));
							Pattern abrir = new Pattern(ParametroUtil.getValueAsString("imagemAbrir"));

							Screen src = new Screen();
							src.setAutoWaitTimeout(30);
							delay(500);
							src.type(nome, valor);
							src.click(abrir);

							log.informouCampoComParametro(valoresRTA.get(i).split("_")[0], valor);
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoCampoIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				}*/ else if (tipoElemento.equals("autocomplete")) {
					delay(500);
					try {
						((JavascriptExecutor) webDriver).executeScript(
								"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
						Verify_Dossie.assertEquals(
								msg.obterMsgComparacaoCampo(valoresRTA.get(i).split("_")[0],
										getCasoDeTesteAtual().getDeclaringClass().getSimpleName().toString()),
								valorEsperadoElemento, elemento.getAttribute("value"));
						log.comparouCampo(valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", ""));
						if (!valor.equals("") && !elemento.getAttribute("value").equals(valor)) {
							elemento.clear();
							elemento.sendKeys(valor);
							delay(2500);
							elemento.sendKeys(Keys.ARROW_DOWN);
							delay(300);
							elemento.sendKeys(Keys.ENTER);
							log.informouCampoComParametro(valoresRTA.get(i).split("_")[0], valor);
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoCampoIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				} else if (tipoElemento.equals("text javaScript")) {
					delay(200);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});",
							webDriver.findElement(By.xpath(xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 9))));
					((JavascriptExecutor) webDriver).executeScript(
							"$(\"" + elemento2 + "\").data('wysihtml5').editor.setValue('" + valor + "');");
					log.informouCampoComParametro(valoresRTA.get(i).split("_")[0], valor);
				} else if (tipoElemento.equals("ckeditor javaScript")) {
					delay(200);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});",
							webDriver.findElement(By.xpath(xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 9))));
					((JavascriptExecutor) webDriver).executeScript("CKEDITOR.instances['"
							+ elemento.getAttribute("id").split("_")[1] + "'].setData('" + valor + "')");
					log.informouCampoComParametro(valoresRTA.get(i).split("_")[0], valor);
				} else if (tipoElemento.equals("button")) {
					delay(200);
					try {
						for (String valor2 : valor.split(",")) {
							aguardarElementoEstarPresente(elemento, 1);
							((JavascriptExecutor) webDriver).executeScript(
									"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
							coletarEvidencia();
							delay(600);
							elemento.click();
							log.clicouBotaoComParametro(valoresRTA.get(i).split("_")[0],
									xls.lerPlan().replace("Módulo: ", ""));
							delay(1000);
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoBotaoIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				} else if (tipoElemento.equals("buttonList")) {
					delay(200);
					String[] tagName = navegarHTML.split("-");
					try {
						for (String valor2 : valor.split(",")) {
							aguardarElementoEstarPresente(elemento, 1);
							((JavascriptExecutor) webDriver).executeScript(
									"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
							coletarEvidencia();
							delay(300);
							elemento.findElement(By.tagName(tagName[0])).click();
							delay(300);
							try {
								boolean achou = false;
								for (int j = 0; j < 20; j++) {
									List<WebElement> gridItens = elemento.findElements(By.tagName(tagName[1]));
									for (WebElement itemGrid : gridItens) {
										if (achou)
											break;
										List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[2]));
										for (int k = 0; k < (linhaGrid.size()); k++) {
											if (tagName.length > 3) {
												linhaGrid = itemGrid.findElements(By.tagName(tagName[3]));
											}
											if (valor.equals(linhaGrid.get(k).getText())) {
												achou = true;
												linhaGrid.get(k).click();
												log.clicouBotaoComParametro(valoresRTA.get(i).split("_")[0],
														xls.lerPlan().replace("Módulo: ", ""));
												delay(1000);
												break;
											}
										}
									}
									if (achou)
										break;
								}
								if (!achou) {
									throw new Exception("O registro -> " + valor + " <- não foi encontrado na lista.");
								}
							} catch (NoSuchElementException nsee) {
								throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
							} catch (Exception e) {
								throw new Exception(e.getMessage());
							}
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoBotaoIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				} else if (tipoElemento.equals("buttonChave")) {
					delay(200);
					String valorButton = null;
					if (elemento.getAttribute("class").contains("btn-primary")) {
						valorButton = "Sim";
					} else if (elemento.getAttribute("class").contains("btn-default")) {
						valorButton = "Não";
					}
					try {
						for (String valor2 : valor.split(",")) {
							aguardarElementoEstarPresente(elemento, 1);
							((JavascriptExecutor) webDriver).executeScript(
									"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
							coletarEvidencia();
							delay(600);
							if (!valorButton.equals(valor2)) {
								elemento.click();
								log.clicouBotaoComParametro2(valoresRTA.get(i).split("_")[0], valor2);
							}
							delay(1000);
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoBotaoIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				}
				if (tipoElemento.equals("data")) {
					delay(500);
					try {
						((JavascriptExecutor) webDriver).executeScript(
								"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
						if (valorEsperadoElemento.equals("D")) {
							valorEsperadoElemento = data(0, 0, 0);
						} else if (valorEsperadoElemento.contains("D")) {
							valorEsperadoElemento = data(
									Integer.parseInt(valorEsperadoElemento.replace("D", "").replace(" ", "")), 0, 0);
						}
						Verify_Dossie.assertEquals(
								msg.obterMsgComparacaoCampo(valoresRTA.get(i).split("_")[0],
										getCasoDeTesteAtual().getDeclaringClass().getSimpleName().toString()),
								valorEsperadoElemento, elemento.getAttribute("value"));
						log.comparouCampo(valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", ""));
						if (!elemento.getAttribute("value").equals(valor)) {
							elemento.clear();
							if (valor.equals("D")) {
								elemento.sendKeys(data(0, 0, 0));
								// elemento.sendKeys(Keys.ESCAPE);
								log.informouCampoComParametro(valoresRTA.get(i).split("_")[0], data(0, 0, 0));
							} else if (valor.contains("D")) {
								elemento.sendKeys(
										data(Integer.parseInt(valor.replace("D", "").replace(" ", "")), 0, 0));
								// elemento.sendKeys(Keys.ESCAPE);
								log.informouCampoComParametro(valoresRTA.get(i).split("_")[0],
										data(Integer.parseInt(valor.replace("D", "").replace(" ", "")), 0, 0));
							} else {
								elemento.sendKeys(valor);
								// elemento.sendKeys(Keys.ESCAPE);
								log.informouCampoComParametro(valoresRTA.get(i).split("_")[0], valor);
							}
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoCampoIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				} else if (tipoElemento.equals("button Suspenso")) {
					delay(500);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					WebElement button = getElement(xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 7),
							xls.lerPlanilhaDadosElementos(valoresRTA.get(i), 8));
					try {
						coletarEvidencia();
						mouseOverElement(button);
						delay(300);
						elemento.click();
						log.clicouBotaoComParametro(valoresRTA.get(i).split("_")[0],
								xls.lerPlan().replace("Módulo: ", ""));
						delay(1000);
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoBotaoIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				} else if (tipoElemento.equals("click")) {
					delay(500);
					try {
						coletarEvidencia();
						elemento.click();
						delay(600);
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoBotaoIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				} else if (tipoElemento.equals("combo")) {
					delay(500);
					try {
						((JavascriptExecutor) webDriver).executeScript(
								"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
						Verify_Dossie.assertEquals(
								msg.obterMsgComparacaoCombo(valoresRTA.get(i).split("_")[0], recuperarCasoTeste()),
								valorEsperadoElemento, getCombo(elemento));
						log.comparouCombo(xls.lerPlan().replace("Módulo: ", ""), valoresRTA.get(i).split("_")[0]);

						if (!valor.equals("") && !getCombo(elemento).equals(valor)) {
							Select combo = new Select(elemento);
							combo.selectByVisibleText(valor);
							log.selecionouCombo(valor, valoresRTA.get(i).split("_")[0]);
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoComboIncorreto(
								xls.lerPlan().replace("Módulo: ", ""), valoresRTA.get(i).split("_")[0]));

					}
				} else if (tipoElemento.equals("check")) {
					delay(500);
					try {
						((JavascriptExecutor) webDriver).executeScript(
								"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
						if (valor.equals("Marcar")) {
							elemento.click();
							delay(800);
							log.marcouCheck(valoresRTA.get(i).split("_")[0]);
						} else if (valor.equals("Desmarcar")) {
							elemento.click();
							delay(800);
							log.desmarcouCheck(valoresRTA.get(i).split("_")[0]);
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(
								msg.obterMsgMapeamentoCheckIncorretoParametro(valoresRTA.get(i).split("_")[0]));
					}
				} else if (tipoElemento.equals("checkGrid")) {
					delay(1000);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					String[] tagName = navegarHTML.split("-");
					String[] valores = valor.split("-");
					try {
						boolean achou = false;
						for (int j = 0; j < 20; j++) {
							List<WebElement> gridItens = elemento.findElements(By.tagName(tagName[0]));
							for (WebElement itemGrid : gridItens) {
								if (achou)
									break;
								List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
								for (int k = 0; k < (linhaGrid.size()); k++) {
									if (valores[0].trim().equals(linhaGrid.get(k).getText())) {
										linhaGrid = itemGrid.findElements(By.tagName(tagName[2]));
										achou = true;
										linhaGrid.get(k).click();
										log.marcouCheck(valores.toString().trim());
										delay(900);
										break;
									}
								}
							}
							if (achou)
								break;
						}
						if (!achou) {
							throw new Exception("O registro -> " + valores + " <- não foi encontrado na lista.");
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
					} catch (Exception e) {
						throw new Exception(e.getMessage());
					}
				} /*
					 * else if(tipoElemento.equals("checkGrid")){ delay(1000);
					 * ((JavascriptExecutor)webDriver).executeScript(
					 * "arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});"
					 * , elemento); String[] tagName = navegarHTML.split("-");
					 * String[] valores = valor.split("-"); try { boolean achou
					 * = false; for(int j = 0; j < 20; j++){ List<WebElement>
					 * gridItens =
					 * elemento.findElements(By.tagName(tagName[0])); for
					 * (WebElement itemGrid : gridItens) { if(achou) break;
					 * List<WebElement> linhaGrid =
					 * itemGrid.findElements(By.tagName(tagName[1])); for(int k
					 * = 1; k <(linhaGrid.size()); k++){
					 * if(valores[0].trim().equals(linhaGrid.get(k).getText())){
					 * linhaGrid =
					 * itemGrid.findElements(By.tagName(tagName[2])); for(int b
					 * = 0; b < valores.length - 1; b++){ for(int p =
					 * valores.length - 3; p < valores.length; p++){
					 * if(valores[p].trim().equals(linhaGrid.get(b).getText().
					 * trim())){ achou = true; linhaGrid.get(b).click();
					 * log.marcouCheck(valores.toString().trim()); delay(900);
					 * break; } } } } } } if(achou) break; } if(!achou){ throw
					 * new Exception("O registro -> " + valores +
					 * " <- não foi encontrado na lista."); } } catch
					 * (NoSuchElementException nsee) { throw new
					 * NoSuchElementException(msg.
					 * obterMsgMapeamentoGridIncorreto()); }catch (Exception e)
					 * { throw new Exception(e.getMessage()); } }
					 */else if (tipoElemento.equals("mensagem")) {
					try {
						delay(350);
						Verify_Dossie.assertEquals(msg.obterMsgMensagemNaoEsperada(valor), valor,
								elemento.getText().replace("×\n", ""));
						log.verificouMensagemEsperada(valor);
						delay(2000);
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(
								msg.obterMsgMapeamentoMensagemIncorreto("mensagem de validação"));
					}
				} else if (tipoElemento.equals("load")) {
					try {
						delay(500);
						aguardarElementoNaoEstarPresente(elemento, 300);
						delay(800);
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoCampoIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				} else if (tipoElemento.equals("popup")) {
					String[] valores = valor.split("-");
					try {
						delay(400);
						Verify_Dossie.assertEquals(msg.obterMsgMensagemNaoEsperada(valores[0]), valores[0],
								fecharAlertaRecuperarTexto(valores[1]));
						log.verificouMensagemEsperada(valores[0]);
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(
								msg.obterMsgMapeamentoMensagemIncorreto("mensagem de validação"));
					}
				} else if (tipoElemento.equals("inserirPlan")) {
					try {
						String[] contLinha = xls.lerPlanilha("inserirPlan").split(",");
						List<String> col = new ArrayList<String>();
						List<String> linhaPlanTratado = new ArrayList<String>();
						pathRTA(getPathRTA1());
						for (String linhaPlan : contLinha) {
							if (linhaPlan.toUpperCase().contains("STR")) {
								col.add(linhaPlan.substring(linhaPlan.indexOf("_") + 1));
							} else {
								linhaPlanTratado.add(linhaPlan);
							}
						}
						for (String linhaPlan : linhaPlanTratado) {
							for (String coluna : col) {
								String valorCampo;
								if (elemento.getAttribute("value").replace("×\n", "").equals("")) {
									valorCampo = elemento.getText().replace("×\n", "");
								} else {
									valorCampo = elemento.getAttribute("value").replace("×\n", "");
								}
								xls.inserirPlan(linhaPlan, coluna, valorCampo);
								delay(6000);
							}
						}
						pathRTA(getPathRTA2());
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(
								msg.obterMsgMapeamentoMensagemIncorreto("mensagem de validação"));
					}
				} else if (tipoElemento.equals("aba")) {
					delay(500);
					String[] tagName = navegarHTML.split("-");
					try {
						((JavascriptExecutor) webDriver).executeScript(
								"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
						boolean achou = false;
						List<WebElement> passos = elemento.findElements(By.tagName(tagName[0]));
						for (WebElement itemPassos : passos) {
							if (achou)
								break;
							List<WebElement> linhaGrid = itemPassos.findElements(By.tagName(tagName[1]));
							for (int k = 0; k < linhaGrid.size(); k++) {
								if (valor.equals(linhaGrid.get(k).getText())) {
									achou = true;
									linhaGrid.get(k).findElement(By.tagName(tagName[2])).click();
									break;
								}
							}
						}
						if (!achou) {
							throw new Exception("Valor -> " + valor + " <- não encontrado.");
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(nsee.toString());
					}
				} else if (tipoElemento.equals("grid")) {
					delay(1000);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					String[] tagName = navegarHTML.split("-");
					try {
						boolean achou = false;
						for (int j = 0; j < 20; j++) {
							List<WebElement> gridItens = elemento.findElements(By.tagName(tagName[0]));
							for (WebElement itemGrid : gridItens) {
								if (achou)
									break;
								List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
								for (int k = 0; k < (linhaGrid.size()); k++) {
									if (tagName.length > 2) {
										linhaGrid = itemGrid.findElements(By.tagName(tagName[2]));
									}
									if (valor.equals(linhaGrid.get(k).getText())) {
										achou = true;
										linhaGrid.get(k).click();
										delay(900);
										break;
									}
								}
							}
							if (achou)
								break;
						}
						if (!achou) {
							throw new Exception("O registro -> " + valor + " <- não foi encontrado na lista.");
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
					} catch (Exception e) {
						throw new Exception(e.getMessage());
					}
				} else if (tipoElemento.equals("duasGrids")) {
					delay(1000);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					String[] tagName = navegarHTML.split("-");
					String[] valores = valor.split("-");
					try {
						boolean achou = false;
						for (int j = 0; j < 20; j++) {
							List<WebElement> gridItens = elemento.findElements(By.tagName(tagName[0]));
							for (WebElement itemGrid : gridItens) {
								if (achou)
									break;
								List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
								for (int k = 0; k < (linhaGrid.size()); k++) {
									if (valores[0].equals(linhaGrid.get(k).getText())) {
										List<WebElement> linhaGrid2 = itemGrid.findElements(By.tagName(tagName[2]));
										linhaGrid2 = itemGrid.findElements(By.tagName(tagName[3]));
										for (int p = 0; p < (linhaGrid2.size()); p++) {
											if (valores[1].equals(linhaGrid2.get(p).getText())) {
												achou = true;
												linhaGrid2.get(p).click();
												delay(900);
												break;
											}
										}
									}
									if (achou)
										break;
								}
							}
							if (achou)
								break;
						}
						if (!achou) {
							throw new Exception("O registro -> " + valores[0] + " <- não foi encontrado na lista.");
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
					} catch (Exception e) {
						throw new Exception(e.getMessage());
					}
				} else if (tipoElemento.equals("verificarItemGrid")) {
					delay(500);
					// aguardarElementoEstarPresente(webDriver.findElement(By.xpath("//*[@id='service-request-list-container']/div[1]/div/div[3]")),
					// 5);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					String[] tagName = navegarHTML.split("-");
					/*
					 * delay(200);
					 * aguardarElementoEstarPresente(webDriver.findElement(By.
					 * cssSelector("div.m15:nth-child(2)")), 5);
					 */
					delay(1500);
					try {
						boolean achou = false;
						for (int j = 0; j < 20; j++) {
							List<WebElement> gridItens = elemento.findElements(By.tagName(tagName[0]));
							for (WebElement itemGrid : gridItens) {
								if (achou)
									break;
								List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
								for (int k = 0; k < (linhaGrid.size()); k++) {
									if (tagName.length > 2) {
										linhaGrid = itemGrid.findElements(By.tagName(tagName[2]));
									}
									if (valor.equals(linhaGrid.get(k).getText())) {
										achou = true;
										break;
									}
								}
							}
							if (achou)
								break;
						}
						if (!achou) {
							throw new Exception("O registro -> " + valor + " <- não foi encontrado na lista.");
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
					} catch (Exception e) {
						throw new Exception(e.getMessage());
					}
				} else if (tipoElemento.equals("clickItemGrid")) {
					aguardarElementoEstarPresente(elemento, 5);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					String[] tagName = navegarHTML.split("-");
					delay(1300);
					try {
						boolean achou = false;
						for (int j = 0; j < 20; j++) {
							List<WebElement> gridItens = elemento.findElements(By.tagName(tagName[0]));
							for (WebElement itemGrid : gridItens) {
								if (achou)
									break;
								List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
								for (int k = 0; k < (linhaGrid.size()); k++) {
									if (tagName.length > 2) {
										linhaGrid = itemGrid.findElements(By.tagName(tagName[2]));
									}
									if (valor.equals(linhaGrid.get(k).getText())) {
										achou = true;
										linhaGrid.get(k).click();
										delay(900);
										break;
									}
								}
							}
							if (achou)
								break;
						}
						if (!achou) {
							throw new Exception("O registro -> " + valor + " <- não foi encontrado na lista.");
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
					} catch (Exception e) {
						throw new Exception(e.getMessage());
					}
				} else if (tipoElemento.equals("gridItemGrid")) {
					aguardarElementoEstarPresente(elemento, 5);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					String[] tagName = navegarHTML.split("-");
					delay(200);
					try {
						boolean achou = false;
						for (int j = 0; j < 20; j++) {
							List<WebElement> gridItens = elemento.findElements(By.tagName(tagName[0]));
							for (WebElement itemGrid : gridItens) {
								if (achou)
									break;
								List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
								for (int k = 0; k < (linhaGrid.size()); k++) {
									if (tagName.length > 2) {
										linhaGrid = itemGrid.findElements(By.tagName(tagName[2]));
									}
									if (valor.equals(linhaGrid.get(k).getText())) {
										achou = true;
										linhaGrid.get(k).click();
										try {
											if (!valor.equals("") && !getCombo(elemento).equals(valor)) {
												Select combo = new Select(elemento);
												combo.selectByVisibleText(valor);
												log.selecionouCombo(valor, valoresRTA.get(i).split("_")[0]);
											}
										} catch (NoSuchElementException nsee) {
											throw new NoSuchElementException(msg.obterMsgMapeamentoComboIncorreto(
													xls.lerPlan().replace("Módulo: ", ""),
													valoresRTA.get(i).split("_")[0]));

										}
										delay(900);
										break;
									}
								}
							}
							if (achou)
								break;
						}
						if (!achou) {
							throw new Exception("O registro -> " + valor + " <- não foi encontrado na lista.");
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
					} catch (Exception e) {
						throw new Exception(e.getMessage());
					}
				} else if (tipoElemento.equals("treeView")) {
					delay(1000);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					String[] tagName = navegarHTML.split("-");
					String[] valores = valor.split("-");
					try {
						boolean achou = false;
						for (int j = 0; j < 20; j++) {
							List<WebElement> gridItens = elemento.findElements(By.tagName(tagName[0]));
							for (WebElement itemGrid : gridItens) {
								if (achou)
									break;
								List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
								for (int k = 1; k < (linhaGrid.size()); k++) {
									if (valores[0].trim().equals(linhaGrid.get(k).getText())) {
										linhaGrid = itemGrid.findElements(By.tagName(tagName[2]));
										for (int b = 0; b < valores.length - 1; b++) {
											for (int p = valores.length - 3; p < valores.length; p++) {
												if (valores[p].trim().equals(linhaGrid.get(b).getText().trim())) {
													achou = true;
													linhaGrid.get(b).click();
													log.marcouCheck(valores.toString().trim());
													delay(900);
													break;
												}
											}
										}
									}
								}
							}
							if (achou)
								break;
						}
						if (!achou) {
							throw new Exception("O registro -> " + valores + " <- não foi encontrado na lista.");
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
					} catch (Exception e) {
						throw new Exception(e.getMessage());
					}
				} else if (tipoElemento.equals("radio")) {
					delay(500);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					try {
						elemento.click();
						log.marcouCheck(valoresRTA.get(i).split("_")[0]);

					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoCheckIncorreto(
								valoresRTA.get(i).split("_")[0], xls.lerPlan().replace("Módulo: ", "")));
					}
				} else if (tipoElemento.equals("radioGrid")) {
					delay(500);
					((JavascriptExecutor) webDriver).executeScript(
							"arguments[0].scrollIntoView({block: \"end\", behavior: \"auto\"});", elemento);
					String[] tagName = navegarHTML.split("-");
					int posicaoRadio;
					if (valorEsperadoElemento.equals("")) {
						posicaoRadio = 1;
					} else {
						posicaoRadio = Integer.parseInt(valorEsperadoElemento);
					}
					try {
						boolean achou = false;
						for (int h = 0; h < 20; h++) {
							List<WebElement> gridItens = elemento.findElements(By.tagName(tagName[0]));
							for (WebElement itemGrid : gridItens) {
								if (achou)
									break;
								List<WebElement> linhaGrid = itemGrid.findElements(By.tagName(tagName[1]));
								for (int k = Integer.parseInt(tagName[2]); k < (linhaGrid.size()); k++) {
									if (valor.equals(linhaGrid.get(k).getText())) {
										achou = true;
										if (tagName.length > 3) {
											linhaGrid.get(k - posicaoRadio).findElement(By.tagName(tagName[3])).click();
										} else
											linhaGrid.get(k - posicaoRadio).click();
										log.selecionouRegistro(valor.toString());
										break;
									}
								}
							}
							if (achou)
								break;
						}
						if (!achou) {
							throw new Exception("Valor -> " + valor + " <- não encontrado.");
						}
					} catch (NoSuchElementException nsee) {
						throw new NoSuchElementException(msg.obterMsgMapeamentoGridIncorreto());
					} catch (Exception e) {
						throw new Exception(e.getMessage());
					}
				} else if (tipoElemento.equals("elementoPresente")) {
					aguardarElementoEstarPresente(elemento, 10);
				}
			}
		}
	}

	public static String getCombo(WebElement elemento) {
		Select combo = new Select(elemento);
		return combo.getAllSelectedOptions().get(0).getText();
	}

	public void coletarEvidencia() {
		if (habilitarColetaEvidenciasTeste.equals("documento")) {
			coletarEvidenciaTeste();
		}
		if (habilitarColetaEvidenciasTeste.equals("print")) {
			coletarPrint();
		}
	}

	private void coletarPrint() {
		if (!getCasoDeTestePrincipal().getDeclaringClass().getSimpleName().contains("Suite")
				&& (getCasoDeTesteAtual().equals(getCasoDeTestePrincipal())
						|| getCasoDeTesteAtual().getDeclaringClass().getSimpleName().equals("CenarioRealizarLogin"))) {
			StringBuffer caminhoCompleto = new StringBuffer();
			caminhoCompleto.append(ParametroUtil.getValueAsString("pastaArquivosEvidencias"));
			caminhoCompleto.append(getCasoDeTesteAtual().getDeclaringClass().getPackage() + "//");
			File file = new File(caminhoCompleto.toString());
			file.mkdir();
			caminhoCompleto.append("[");
			caminhoCompleto.append(getCasoDeTesteAtual().getDeclaringClass().getSimpleName());
			caminhoCompleto.append("]");
			caminhoCompleto.append(" ");
			caminhoCompleto.append(getDateTime());
			caminhoCompleto.append(" ");
			// caminhoCompleto.append(descricao);
			caminhoCompleto.append(".png");
			try {
				BufferedImage image = new Robot()
						.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
				ImageIO.write(image, "png", new File(caminhoCompleto.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (!getCasoDeTesteAtual().getDeclaringClass().getSimpleName().contains("Auxiliar")) {
			StringBuffer caminhoCompleto = new StringBuffer();
			caminhoCompleto.append(ParametroUtil.getValueAsString("pastaArquivosEvidencias"));
			caminhoCompleto.append(getCasoDeTesteAtual().getDeclaringClass().getPackage() + "//");
			File file = new File(caminhoCompleto.toString());
			file.mkdir();
			caminhoCompleto.append("[");
			caminhoCompleto.append(getCasoDeTesteAtual().getDeclaringClass().getSimpleName());
			caminhoCompleto.append("]");
			caminhoCompleto.append(" ");
			caminhoCompleto.append(getDateTime());
			caminhoCompleto.append(" ");
			// caminhoCompleto.append(descricao);
			caminhoCompleto.append(".png");
			try {
				BufferedImage image = new Robot()
						.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
				ImageIO.write(image, "png", new File(caminhoCompleto.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void coletarEvidenciaTeste() {
		BufferedImage bufImage = null;
		if (!getCasoDeTesteAtual().getDeclaringClass().getSimpleName().contains("Auxiliar")) {
			try {
				bufImage = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			addImgEvidencias(bufImage);
		}
	}

	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh.mm.ssaaa");
		Date date = new Date();
		return dateFormat.format(date);
	}

	@SuppressWarnings("unused")
	public static String data(int dia, int mes, int ano) {

		GregorianCalendar cl = new GregorianCalendar();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date data = new Date();
		;
		cl.setTime(data);

		// Pega a dia
		int dateAtual = cl.get(GregorianCalendar.DATE) + dia;
		cl.set(GregorianCalendar.DATE, dateAtual);
		Date dataAtual = cl.getTime();

		// Pega o Mês
		int month = cl.get(GregorianCalendar.MONTH) + mes;
		cl.set(GregorianCalendar.MONTH, mes);

		// Pega o ano
		int year = cl.get(GregorianCalendar.YEAR) + ano;
		cl.set(GregorianCalendar.YEAR, ano);

		return dateFormat.format(dataAtual);
	}

	@SuppressWarnings("unused")
	private int qtdePaginas(int qtdRegistros) {
		int qtdPage = 1;
		if (qtdRegistros > 10) {
			if ((qtdRegistros % 10) == 0) {
				qtdPage = qtdRegistros / 10;
			} else {
				qtdPage = (qtdRegistros / 10) + 1;
			}
		}
		return qtdPage;
	}

	@SuppressWarnings("unused")
	private List<String> montarListaParaAlvo(String... alvo) {
		List<String> alvos = new ArrayList<String>();
		for (String item : alvo) {
			alvos.add(item);
		}
		return alvos;
	}

	private List<String> montarListaParaAlvo(ArrayList<String> arrayList) {
		List<String> alvos = new ArrayList<String>();
		for (String item : arrayList) {
			alvos.add(item);
		}
		return alvos;
	}

	public void informarCamposJavaScript(String campo, String path, String valor) {
		((JavascriptExecutor) webDriver)
				.executeScript("$(" + campo + ").data('" + path + "').editor.setValue('" + valor + "');");
	}
	
	private void verificaLoadPresente(String xpath) {
		try {
			if (AbstractCenario.getCasoDeTesteAtual().getDeclaringClass().getPackage().getName()
					.contains("instalacao")) {
				aguardarElementoNaoEstarPresente(
						webDriver.findElement(By.xpath(xpath)), 180);
			} else {
				aguardarElementoNaoEstarPresente(
						webDriver.findElement(By.xpath(xpath)), 180);
			}
		} catch (org.openqa.selenium.NoSuchElementException e) {
		} catch (UnhandledAlertException e) {
		}
	}

	public String recuperarCasoTeste() {
		String casoTeste = getCasoDeTesteAtual().getDeclaringClass().getSimpleName().toString();
		return casoTeste;
	}

	public String getPathRTA1() {
		return pathRTA1;
	}

	public void setPathRTA1(String pathRTA) {
		this.pathRTA1 = pathRTA;
	}

	public String getPathRTA2() {
		return pathRTA2;
	}

	public void setPathRTA2(String pathRTA) {
		this.pathRTA2 = pathRTA;
	}
}
