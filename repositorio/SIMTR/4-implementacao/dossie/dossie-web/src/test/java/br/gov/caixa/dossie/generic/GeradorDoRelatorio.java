package br.gov.caixa.dossie.generic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

/**
 * .
 * 
 * Descricao: Classe responsavel por realizar o relatorio dos testes integrados
 * 
 * Nome: GeradorDoRelatorio
 * 
 * @author c112460
 * 
 * @since 21/08/2016
 * 
 * @version 1.0
 */

public class GeradorDoRelatorio implements org.testng.IReporter {

	private static final String SKIP = "skip";

	private static final String FALHA = "falha";

	private static final String SUCESSO = "sucesso";

	private static final String OCOREU_UM_ERRO_INESPERADO = "Ocoreu um erro inesperado ";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GeradorDoRelatorio.class);

	private String fileSeparator = System.getProperty("file.separator");

	private PrintWriter printerWriter;

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
			String outputDirectory) {

		LOGGER.info("Come�ando a gerar o relatorio. ");

		LOGGER.info("Coletando resultados. ");

		coletaResultados(suites);

		LOGGER.info("Organizando resultados. ");

		organizaResultados(xmlSuites);

		try {

			LOGGER.info("Gerando arquivo html de evidencias. ");

			createWriter(outputDirectory, "evidencia.html");

		} catch (IOException e) {

			LOGGER.error(OCOREU_UM_ERRO_INESPERADO, e);

			return;

		}

		startHtml(printerWriter);

		writeContent(printerWriter);

		endHtml(printerWriter);

		printerWriter.flush();

		printerWriter.close();

	}

	private void writeContent(PrintWriter out) {

		out.println("                              <div class=\"content\">");

		out.println("                                         <table id=\"results\">");

		for (ResultadoTeste ln : UtilEvidencias.LINHASRELATORIO) {

			out.println("                                                     <tr>");

			for (ITestResult r : ln.getColunas()) {

				if (r != null) {

					out.println("                                                                 <td class=\""
							+ getStatus(r) + "\">");

					out.println("                                                                             <strong>Teste:</strong> "
							+ r.getName() + "<br/>");

					if (getStatus(r).equals(SUCESSO)) {

						out.println("                                                                             <strong>Status: <font color=\"#009933\">"
								+ getStatus(r) + "</font></strong><br/>");

					} else if (getStatus(r).equals(FALHA)) {

						out.println("                                                                             <strong>Status: <font color=\"#FF0000\">"
								+ getStatus(r)
								+ "</font></strong>&nbsp;"

								+ "<a href=\"#\" onClick=\"displayDetalhes('"
								+ UtilEvidencias.getDetalhes(r)
								+ "')\">(exibir detalhes...)</a><br/>");

					} else if (getStatus(r).equals(SKIP)) {

						out.println("                                                                             <strong>Status: <font color=\"#FF9900\">"
								+ getStatus(r) + "</font></strong><br/>");

					}

					out.println("                                                                             <strong>Navegador:</strong> "
							+ r.getAttribute("testContextName") + "<br/>");

					out.println("                                                                             <strong>Tempo:</strong> "
							+ (r.getEndMillis() - r.getStartMillis())
							/ 1000
							+ " s<br/>");

					out.println("                                                                             <br/>");

					out.println("                                                                             <a href=\""
							+ r.getName()
							+ fileSeparator
							+ r.getName()
							+ "_"
							+ r.getAttribute("testContextName") + ".png\">");

					out.println("                                                                                        <div class=\"img\">");

					out.println("                                                                                                    <img class=\"thumb\" border=\"1\" src=\""
							+ r.getName()

							+ fileSeparator
							+ r.getName()
							+ "_"
							+ r.getAttribute("testContextName")
							+ ".png\">"
							+ "<br/>");

					out.println("                                                                                        </div>");

					out.println("                                                                             </a>");

					out.println("                                                                 </td>");

				} else {

					out.println("                                                                 <td>");

					out.println("                                                                 </td>");

				}

			}

			out.println("                                                     </tr>");

		}

		out.println("                                         </table>");

		out.println("                              </div>");

		out.println("                              <div id=\"divStack\" class=\"stacktrace\">");

		out.println("                              </div>");

		out.println("                  </center>");

	}

	private String getStatus(ITestResult r) {

		if (r.getStatus() == ITestResult.SUCCESS) {

			return SUCESSO;

		} else if (r.getStatus() == ITestResult.SKIP) {

			return SKIP;

		} else if (r.getStatus() == ITestResult.FAILURE) {

			return FALHA;

		}

		return "";

	}

	protected void endHtml(PrintWriter out) {
		out.println("      </body>");
		out.println("</html>");
	}

	protected void startHtml(PrintWriter out) {
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
		out.println("      <head>");
		out.println("                  <title>Testes de Regress�o</title>");
		out.println("                  <style type=\"text/css\">");
		out.println("                              #cabecalho");
		out.println("                                         {");
		out.println("                                                     font-family:\"Trebuchet MS\", Arial, Helvetica, sans-serif;");
		out.println("                                                     width:969px;");
		out.println("                                                     border-collapse:collapse;");
		out.println("                                         }");
		out.println("                              #cabecalho th, #cabecalho span");
		out.println("                                         {");
		out.println("                                                     font-size:1.1em;");
		out.println("                                                     padding:3px 7px 2px 7px;");
		out.println("                                                     vertical-align: top;");
		out.println("                                                     text-align:left;");
		out.println("                                                     background-color: rgb(70,70,70);");
		out.println("                                                     color:#ffffff;");
		out.println("                                         }");
		out.println("                              #results");
		out.println("                                         {");
		out.println("                                                     font-family:\"Trebuchet MS\", Arial, Helvetica, sans-serif;");
		out.println("                                                     width:900px;");
		out.println("                                                     border-collapse:collapse;");
		out.println("                                         }");
		out.println("                              #results td, #results th");
		out.println("                                         {");
		out.println("                                                     font-size:0.80em;");
		out.println("                                                     padding:3px 7px 2px 7px;");
		out.println("                                                     vertical-align: top;");
		out.println("                                                     border: 1px solid #000000;");
		out.println("                                         }");
		out.println("                              #results th ");
		out.println("                                         {");
		out.println("                                                     font-size:1.1em;");
		out.println("                                                     text-align:left;");
		out.println("                                                     padding-top:5px;");
		out.println("                                                     padding-bottom:4px;");
		out.println("                                                     background-color: rgb(70,70,70);");
		out.println("                                                     color:#ffffff;");
		out.println("                                         }");
		out.println("                              #results td.skip");
		out.println("                                         {");
		out.println("                                                     color:#000000;");
		out.println("                                                     background-color: rgb(255,253,230);");
		out.println("                                         }");
		out.println("                              #results td.sucesso");
		out.println("                                         {");
		out.println("                                                     color:#000000;");
		out.println("                                                     background-color:rgb(230,253,215);");
		out.println("                                         }");
		out.println("                              #results td.falha");
		out.println("                                         {");
		out.println("                                                     color:#000000;");
		out.println("                                                     background-color: rgb(255,223,223);");
		out.println("                                         }");
		out.println("                              #results img.thumb");
		out.println("                                         {");
		out.println("                                                     width:220px;");
		out.println("                                                     height: auto;");
		out.println("                                         }");
		out.println("                              div.content");
		out.println("                                         {");
		out.println("                                                     width:967px; ");
		out.println("                                                     height:370px;");
		out.println("                                                     overflow-x:hidden;");
		out.println("                                                     overflow-y:scroll;");
		out.println("                                                     border: 1px solid #000000;");
		out.println("                                         }");
		out.println("                              div.stacktrace");
		out.println("                                         {");
		out.println("                                                     width:953px; ");
		out.println("                                                     height:155px;");
		out.println("                                                     overflow-x:hidden;");
		out.println("                                                     overflow-y:scroll;");
		out.println("                                                     border: 1px solid #000000;");
		out.println("                                                     font-family:\"Trebuchet MS\", Arial, Helvetica, sans-serif;");
		out.println("                                                     padding:3px 7px 2px 7px;");
		out.println("                                                     font-size:12px;");
		out.println("                                                     vertical-align: top;");
		out.println("                                                     text-align:left;");
		out.println("                                         }");
		out.println("                              div.img");
		out.println("                                         {");
		out.println("                                                     width:220px;");
		out.println("                                                     height:230px;");
		out.println("                                                     overflow-x:hidden;");
		out.println("                                                     overflow-y:hidden;");
		out.println("                                                     border: 1px solid #000000;");
		out.println("                                         }");
		out.println("                              a:link,a:visited {color:rgb(147,147,147); background-color:transparent; text-decoration: none;}");
		out.println("                              a:hover,a:active {color:rgb(0,0,0); background-color:transparent; text-decoration: none;}");
		out.println("                  </style>");
		out.println("                  <script type=\"text/javascript\" >");
		out.println("                     function displayDetalhes(content) { ");
		out.println("                                         document.getElementById(\"divStack\").style.color=\"#000000\";");
		out.println("                                         document.getElementById(\"divStack\").innerHTML = content;");
		out.println("                     }");
		out.println("                  </script>");
		out.println("      </head>");
		out.println("      <body>");
		out.println("                  <center>");
		out.println("                              <table id=\"cabecalho\">");
		out.println("                                         <tr>");
		out.println("                                                     <th>");

		if (UtilEvidencias.TESTCONTEXTNAME.size() >= 1) {
			out.println("                                                                 <span style=\"position: relative; left: 0px\">"
					+ UtilEvidencias.TESTCONTEXTNAME.get(0) + "</span>");
		}

		if (UtilEvidencias.TESTCONTEXTNAME.size() >= 2) {
			out.println("                                                                 <span style=\"position: relative; left: 40px\">"
					+ UtilEvidencias.TESTCONTEXTNAME.get(1) + "</span>");
		}

		if (UtilEvidencias.TESTCONTEXTNAME.size() >= 3) {
			out.println("                                                                 <span style=\"position: relative; left: 92px\">"
					+ UtilEvidencias.TESTCONTEXTNAME.get(2) + "</span>");
		}

		if (UtilEvidencias.TESTCONTEXTNAME.size() >= 4) {
			out.println("                                                                 <span style=\"position: relative; left: 145px\">"
					+ UtilEvidencias.TESTCONTEXTNAME.get(3) + "</span>");
		}

		out.println("                                                     </th>");
		out.println("                                         </tr>");
		out.println("                              </table>");
	}

	protected void createWriter(String outdir, String nomeArquivo)
			throws IOException {

		File dir = getUltimoDiretorio();

		new File(dir.getPath()).mkdirs();

		printerWriter = new PrintWriter(new BufferedWriter(new FileWriter(
				new File(dir.getPath(), nomeArquivo))));

	}

	/**
	 * 
	 * Descricao: Metodo responsavel por coletar os resultados dos testes
	 * 
	 * @param suites
	 */

	private void coletaResultados(List<ISuite> suites) {
		LOGGER.info("Total de resultados: " + suites.size());

		for (ISuite suite : suites) {
			for (ISuiteResult suiteResult : suite.getResults().values()) {
				ITestContext testContext = suiteResult.getTestContext();

				String testContextName = testContext.getName();

				UtilEvidencias.coletaResultados(testContextName,
						testContext.getFailedTests());
				UtilEvidencias.coletaResultados(testContextName,
						testContext.getPassedTests());
				UtilEvidencias.coletaResultados(testContextName,
						testContext.getSkippedTests());
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static void organizaResultados(List<XmlSuite> xmlSuites) {
		for (XmlSuite xmlSuite : xmlSuites) {

			for (XmlTest tests : xmlSuite.getTests()) {
				UtilEvidencias.TESTCONTEXTNAME.add(tests.getName());

				for (XmlClass xmlClass : tests.getXmlClasses()) {
					if (!xmlClass.getIncludedMethods().isEmpty()) {

						for (XmlInclude xmlInclude : xmlClass
								.getIncludedMethods()) {

							if (!UtilEvidencias.TESTRESULTNAMES.contains(xmlInclude
									.getName())) {

								UtilEvidencias.TESTRESULTNAMES.add(xmlInclude.getName());
							}
						}
					} else {
						try {
							Class classObj = Class.forName(xmlClass.getName());
							Method[] metodos = classObj.getDeclaredMethods();

							for (Method m : metodos) {
								if (!UtilEvidencias.TESTRESULTNAMES.contains(m.getName())
										&& Util.isMetodoTeste(m)) {

									UtilEvidencias.TESTRESULTNAMES.add(m.getName());
								}
							}
						} catch (Exception e) {
							LOGGER.error(OCOREU_UM_ERRO_INESPERADO, e);
						}
					}
				}
			}
		}
		UtilEvidencias.adicionaResultados();
	}

	private File getUltimoDiretorio() {
		String screenshotsDir = System.getProperty("diretorioPrints");

		if (screenshotsDir == null) {
			screenshotsDir = Util.TARGET_EVIDENCIAS;
		}

		System.out.println("Diretorio das imagens " + screenshotsDir);

		File scDir = new File(screenshotsDir);

		// Adiciono na estrutura TreeSet para ordenar alfabeticamente os
		// diret�rios,

		// cujos nomes s�o datas no formato yyyy-MM-dd-HH-mm-ss

		TreeSet<String> tS = new TreeSet<String>();

		if(scDir.listFiles() != null){
			for (File file : scDir.listFiles()) {

				if (file.isDirectory()) {
					tS.add(file.getName());
				}
			}
		}

		File lastDir = new File(screenshotsDir + tS.pollLast());

		return lastDir;
	}
}
