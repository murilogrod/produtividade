package br.gov.caixa.simtr.controle.servico;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.io.Files;

import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;


@RunWith(PowerMockRunner.class)
@PrepareForTest(JRLoader.class)
public class RelatorioServicoTest {
	
	@InjectMocks
	private RelatorioServico servico;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	
	}
	
	@Test
	public void testGerarRelatorioPDFJsonDataSourceSucess() throws JRException, NoSuchMethodException, SecurityException, IOException {
		final String caminhoArquivo = "relatorios/dossiedigital/cartao_assinatura.jasper";		

		InputStream reportInputStream = this.getClass().getClassLoader().getResourceAsStream(caminhoArquivo);
		
		JasperReport report = (JasperReport) JRLoader.loadObject(reportInputStream);
		
		PowerMockito.mockStatic(JRLoader.class);
		PowerMockito.when(JRLoader.loadObject(Mockito.any(InputStream.class))).thenReturn(report);
		
		final String reportName = "dossiedigital/cartao_assinatura";
		final String jsonData = "{\"CPF\":\"0111111111111\",\"NOME\":\"TIÃO CARREIRO\"}";
		final Map<String, Object> parametros = new HashMap<>();

		byte[] rel = servico.gerarRelatorioPDFJsonDataSource(reportName, jsonData, parametros);
		
		Assert.assertNotNull(rel);
	}
	
	@Test(expected=SimtrConfiguracaoException.class)
	public void testGerarRelatorioPDFJsonDataSourceError() throws JRException, NoSuchMethodException, SecurityException, IOException {
		final String caminhoArquivo = "relatorios/dossiedigital/cartao_assinatura.jasper";		

		InputStream reportInputStream = this.getClass().getClassLoader().getResourceAsStream(caminhoArquivo);
		
		JasperReport report = (JasperReport) JRLoader.loadObject(reportInputStream);
		
		PowerMockito.mockStatic(JRLoader.class);
		PowerMockito.when(JRLoader.loadObject(Mockito.any(InputStream.class))).thenReturn(report);
		
		final String reportName = "dossiedigital/cartao_assinatura";
		final String jsonData = "{\"CPF\":\"0111111111111\"\"NOME\":\"TIÃO CARREIRO\"}";
		final Map<String, Object> parametros = new HashMap<>();

		servico.gerarRelatorioPDFJsonDataSource(reportName, jsonData, parametros);
	}
	
	
	public static void gerarRelatorioPesquisaCadastral() throws IOException {
		final RelatorioServico ser = new RelatorioServico();
		final String reportName = "dossiedigital/pesquisa_cadastral";
		final String jsonData = "{\"dados\" : [	{\"cpf\" : \"988888888\",	 \"documento\" : \"RG\",	 \"situacao\" : \"Alterado\",	 \"total_documentos\" : \"20\"	 }]}";
		
		final String jsonPesquisa = "{\"sicpf\":{\"mae\":\"REGIA MARIA FONTINELE VIANA\",\"nascimento\":\"21/08/1985\",\"situacao\":\"REGULAR\",\"nome\":\"RENATO FONTINELE VIANA\",\"titulo_eleitor\":150659690256},\"cpf\":\"01255191171\",\"sipes\":[{\"ocorrencia\":\"CPF Em Situação Regular\",\"sistema\":\"SICPF\"},{\"ocorrencia\":\"OCORRÊNCIA SINAD\",\"sistema\":\"SINAD\"},{\"ocorrencia\":\"OCORRÊNCIA SINAD\",\"sistema\":\"SINAD\"},{\"ocorrencia\":\"OCORRÊNCIA CADIN\",\"sistema\":\"CADIN\"},{\"ocorrencia\":\"PENDÊNCIAS FINANCEIRAS (PEFIN/REFIN)\",\"sistema\":\"SERASA\"},{\"ocorrencia\":\"OCORRÊNCIA SICCF\",\"sistema\":\"SICCF\"},{\"ocorrencia\":\"OCORRÊNCIA SPC\",\"sistema\":\"SPC\"},{\"ocorrencia\":\"PPE PRIMARIO\",\"sistema\":\"SICOW\"},{\"ocorrencia\":\"PPE RELACIONADO\",\"sistema\":\"SICOW\"},{\"ocorrencia\":\"CONRES\",\"sistema\":\"SICOW\"},{\"ocorrencia\":\"PROIBIÇÃO DE CONTRATAR COM O SETOR PÚBLICO\",\"sistema\":\"SICOW\"},{\"ocorrencia\":\"INTERDIÇÃO JUDICIAL\",\"sistema\":\"SICOW\"},{\"ocorrencia\":\"INFORMAÇÕES DE SEGURANÇA\",\"sistema\":\"SICOW\"},{\"ocorrencia\":\"EMPREGADORES ENVOLVIDOS COM TRABALHO ESCRAVO\",\"sistema\":\"SICOW\"},{\"ocorrencia\":\"PLD\",\"sistema\":\"SICOW\"}]}";
		
		final Map<String, Object> parametros = new HashMap<>();
		
		byte[] rel = ser.gerarRelatorioPDFJsonDataSource(reportName, jsonPesquisa, parametros);
		Files.write(rel, new File("C:\\Users\\F542167\\Documents\\relatorios_tst\\test_dossie.xls"));
	}
	
	
	public static void gerarRelatorioDadosDeclarados() throws IOException {
		final RelatorioServico ser = new RelatorioServico();
		final String reportName = "dossiedigital/dados_declarados";
		final String jsonData = "{\"declaracao_fatca_crs\":\"Não\",\"logradouro_comercial\":\"MARCILIO DIAS         \",\"endereco_comercial\":\"25555555 RUA             MARCILIO DIAS                            22 casdas COELHO DA ROCHA                          RJ SAO JOAO DE MERITI                 \",\"grau_instrucao\":\"Não Informado\",\"bloco_sms\":\"Não\",\"numero_comercial\":\"22\",\"cpf\":\"01255191171\",\"valor_total_patrimonio\":\"0.00\",\"complemento_residencial\":\"casdasd\",\"ddd_1\":\"11\",\"profissao\":\"AGENCIADOR DE PROPAGANDA\",\"nome\":\"RENATO FONTINELE VIANA\",\"uf_comercial\":\"RJ\",\"sms_valor_saque_trans\":50.00,\"complemento_comercial\":\"casdas\",\"tipo_logradouro_residencial\":\"RUA            \",\"endereco_residencial\":\"25555555 RUA             MARCILIO DIAS                            22 casdasd COELHO DA ROCHA                          RJ SAO JOAO DE MERITI                 \",\"logradouro_residencial\":\"MARCILIO DIAS                           \",\"naturalidade_uf\":\"MT\",\"declaracao_proposito\":\"0001 Conta de Depósito/Poupança/Caução/Conta Vinculada/Judicial,0002 Empréstimos/Financiamentos,0004 Cartão de Crédito\",\"estado_civil\":\"Divorciado (a)\",\"sms_produtos_servicos\":\"Sim\",\"finalidade_1\":\"Celular\",\"cep_residencial\":\"25555555\",\"cep_comercial\":\"25555555\",\"naturalidade\":\"Alto Araguaia\",\"sms_valor_cartao\":50.00,\"tipo_patrimonio\":\"Não\",\"contato_1\":\"RENATO FONTINELE VIANA\",\"cidade_comercial\":\"SAO JOAO DE MERITI                 \",\"nome_social\":\"Nome Teste Socia333\",\"codigo_ocupacao\":\"\",\"conjuge_cpf\":\"72772450163\",\"endereco\":\"\",\"telefone_1\":\"111111111\",\"bairro_comercial\":\"COELHO DA ROCHA                         \",\"uf_residencial\":\"RJ\",\"tipo_logradouro_comercial\":\"RUA\",\"pais_crs\":\"\",\"token\":\"{\\\"datahora\\\" : \\\"2019-01-03 15:21:02\\\", \\\"token\\\": \\\"eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJvbGczaGJWRU0zX2dSdDFEZHBYTXdPWHRTLUUyTVFRRGIwQmh3Nkl2WmRvIn0.eyJqdGkiOiI5MDBmN2QxZC1mNmU1LTQwZjMtOTdmOS0xNTRhODlkYTE4MDkiLCJleHAiOjE1MjgzNzgyNTcsIm5iZiI6MCwiaWF0IjoxNTI4Mzc4MTk3LCJpc3MiOiJodHRwczovL2xvZ2luZGVzLmNhaXhhLmdvdi5ici9hdXRoIiwiYXVkIjoiY2xpLXNlci1tdHIiLCJzdWIiOiI1MTRkZjNhOC05NTc4LTQyY2YtOWE4OS1jY2NmNmY1ZTAzNjUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJjbGktc2VyLW10ciIsIm5vbmNlIjoiYzRmZDMyZWItZmVjMS00ZjY2LTg4MjktYjRhZTI5MjU0YTBlIiwiYXV0aF90aW1lIjowLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiIwMTI1NTE5MTE3MSIsInJlc291cmNlX2FjY2VzcyI6e30sInRpcG9jcmVkIjoic2VuaGEiLCJpcCI6IjEwLjIyNC41Ljc5IiwidmFsb3IiOiJhdXRvIn0.NEbBj6TppJsIKW1ycCfSOjUPj-U5sf1rzP2rtzrJDNQ8GzSSkV2sFhyPHF14IF_naJcNg52VL7XrnLOMNT15exBZmlGPgtaR-jcpqyxHEEM9gTQ6rLpCJ39BqI4Wd_ddIy3tjXoAXKcUhcMDzQ87utGk5V9QeOltObmgvVVSMopB0sViGZeYYDy9SV5a9ChD9nQDvV2E98VlyHMIaPXyHaxeboPJiLkEC9q3WoD8IiVoOrfUTVLTmqf-KGHvZKTWULKNbe0KmQ-LxxFcKTux1QTnha_jR2HoKnkYlTa6nZLMk1NBGdvc_MqX09j3AtkRd5jn7X8MpKByVsWZ8RkAHg\\\"}\",\"sms_fgts\":\"Sim\",\"n_tin\":\"\",\"conjuge_nascimento\":\"21-08-1985 00:00:00\",\"declaracao\":\"\",\"bairro_residencial\":\"COELHO DA ROCHA                         \",\"cidade_assinatura\":\"Goiania\",\"email_principal\":\"usuario772@email.com.br\",\"conjuge_nome\":\"RENATO FONTINELE VIANA\",\"sexo\":\"Masculino\",\"cidade_residencial\":\"SAO JOAO DE MERITI                 \",\"numero_residencial\":\"22\"}";
		
		
		final Map<String, Object> parametros = new HashMap<>();
		
		byte[] rel = ser.gerarRelatorioPDFJsonDataSource(reportName, jsonData, parametros);
		Files.write(rel, new File("C:\\Users\\F542167\\Documents\\relatorios_tst\\test_dados_01.pdf"));
	}
	
	

}
