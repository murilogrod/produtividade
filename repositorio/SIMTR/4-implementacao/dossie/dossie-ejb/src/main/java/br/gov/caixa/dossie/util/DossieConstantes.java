package br.gov.caixa.dossie.util;

/**
 * @author SIOGP
 */
public class DossieConstantes {

	public static final String ERRO_CODIGO_NAO_AUTORIZADO = "Não autorizado o código de acesso ";
	
	public static final String INFO_CHAMANDO_METODO_CREATE = "Chamando o metodo: create()";
	public static final String INFO_CHAMANDO_METODO_DELETE = "Chamando o metodo: delete ";
	public static final String INFO_CHAMANDO_METODO_READ = "Chamando o metodo: read ";
	public static final String INFO_CHAMANDO_METODO_READ_ALL = "Chamando o metodo: readAll()";
	public static final String INFO_CHAMANDO_METODO_UPDATE = "Chamando o metodo: update ";
	
	public static final String INFO_EXCLUSAO_SUCESSO = "Exclusão feita com sucesso.";
	public static final String INFO_INCLUSAO_SUCESSO = "Inclusão feita com sucesso.";
	public static final String INFO_LEITURA_SUCESSO = "Leitura feita com sucesso.";
	public static final String INFO_LEITURA_ID_SUCESSO = "Leitura por \"id\" feita com sucesso.";
	public static final String INFO_UPDATE_SUCESSO = "Atualização feita com sucesso.";
	public static final String INFO_MBEAN_REGISTRADO = "MBean registered: ";

	public static final String METRICS_WEB_SERVICES = "webservice";
	public static final String METRICS_DELETE = "delete";
	public static final String METRICS_READ = "read";
	public static final String METRICS_AUTH = "Auth";
	public static final String METRICS_READ_ALL = "readAll";
	public static final String METRICS_UPDATE = "update";
	public static final String METRICS_RESPONSE = "response";

	public static final String ANALYTICS_KEY =	"2c8050025544715148412210fe73b64fda98a1df";
	public static final String APP_KEY =	"429f2d9a3bfe6fc775ea193391eb466992fb8463";
	public static final String URL_SICPU = Util.getSystemEnvOrProperty("sicpu.local");
	public static final String COD_AUTENTICACAO_SIAUT = "7c4a8d09ca3762af61e59520943dc26494f8941b";
	public static final String EMAIL_DOSSIE= "admin@email.com.br";
	public static final String SICPU_PATH_LOGIN = "ws/user/auth";
	public static final String SICPU_PATH_SEND_MESSAGE_IMMEDIATE = "ws/push/sendimmediate/";
	public static final String URL_SISIT = Util.getSystemEnvOrProperty("sisit.local");

	public static final String PATTERN_DD_MM_YYYY_HH_MM = "dd-MM-yyyy HH:mm";
	
	public static final String CREDENCIAL = "credencial";
	public static final String TOKENACESSO = "tokenAcesso";
	public static final String TOKENRENOVACAO = "tokenRenovacao";
	public static final String TEMPOEXPIRACAOACESSO =	"tempoExpiracaoAcesso";
	public static final String TEMPOEXPIRACAORENOVACAO ="tempoExpiracaoRenovacao";
	public static final String NAVEGADOR = "navegador";	
	
	private DossieConstantes() {}	
}
