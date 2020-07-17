package br.gov.caixa.dossie.exception;

/**
 * @author SIOGP
 */
public class AuthenticationException extends GeneralException {
	
	private static final long serialVersionUID = -1895279900462464149L;
	
	private static final int ERRO_519 = 519;
	private static final int ERRO_518 = 518;
	private static final int ERRO_517 = 517;
	private static final int ERRO_516 = 516;
	private static final int ERRO_515 = 515;
	private static final int ERRO_514 = 514;
	private static final int ERRO_513 = 513;
	private static final int ERRO_512 = 512;
	private static final int ERRO_511 = 511;
	private static final int ERRO_510 = 510;
	
	public static final GeneralException RESPONSE_CODE_ERROR_USER_NOT_FOUND = new GeneralException(ERRO_510,"Usu?rio %s n?o foi localizado");
	public static final GeneralException RESPONSE_CODE_ERROR_USER_PASS_INVALID = new GeneralException(ERRO_511,"A senha informada n?o ? v?lida");
	public static final GeneralException RESPONSE_CODE_ERROR_USER_DISABLED = new GeneralException(ERRO_512,"Este usu?rio ainda n?o foi habilitado, contate o administrador");
	public static final GeneralException RESPONSE_CODE_ERROR_USER_REGISTER_ERROR = new GeneralException(ERRO_513,"Falha ao verificar usu?rio");
	public static final GeneralException RESPONSE_CODE_ERROR_USER_ALREADY_EXISTS = new GeneralException(ERRO_514,"Us?rio %s j? foi registrado");
	public static final GeneralException RESPONSE_CODE_ERROR_USER_NOT_PRIVILEGED = new GeneralException(ERRO_515,"O usu?rio n?o possui acesso a esta informa??o");
	public static final GeneralException RESPONSE_CODE_ERROR_USER_UPDATE_ERROR = new GeneralException(ERRO_516,"Falha ao atualizar usu?rio");
	public static final GeneralException RESPONSE_CODE_ERROR_USER_REMOVE_ERROR = new GeneralException(ERRO_517,"Falha ao remover usu?rio %s");
	public static final GeneralException RESPONSE_CODE_ERROR_USER_NEWPASS_ERROR = new GeneralException(ERRO_518,"Falha ao processar nova senha");
	public static final GeneralException RESPONSE_CODE_ERROR_USER_PASS_NOT_DEFINED = new GeneralException(ERRO_519,"O usu?rio n?o possui senha de acesso definida");
	
	/**
	 * @param code
	 * @param message
	 */
	public AuthenticationException(int code, String message) {
		super(code,message);
	}
	
	/**
	 * @param general
	 * @param args
	 */
	public AuthenticationException(GeneralException general, String[]args) {
		super(general,args);
	}
	
	/**
	 * @param general
	 * @param arg
	 */
	public AuthenticationException(GeneralException general, String arg) {
		super(general,arg);
	}
	
	/**
	 * @param general
	 */
	public AuthenticationException(GeneralException general) {
		super(general);
	}
}
