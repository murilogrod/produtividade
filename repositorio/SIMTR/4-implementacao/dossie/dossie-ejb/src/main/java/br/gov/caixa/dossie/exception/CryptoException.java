package br.gov.caixa.dossie.exception;

/**
 * @author SIOGP
 */
public class CryptoException extends GeneralException {

	private static final long serialVersionUID = -8243773990636806396L;
	
	public static final CryptoException RESPONSE_CODE_ERROR_GENERAL = new CryptoException(800,"Falha ao processar criptografia");
	public static final CryptoException RESPONSE_CODE_ERROR_NOSUCH_ALG = new CryptoException(801,"Algoritimo de criptografia n?o encontrado");
	public static final CryptoException RESPONSE_CODE_ERROR_NOSUCH_PAD = new CryptoException(802,"Dimensionador de criptografia n?o encontrado");
	public static final CryptoException RESPONSE_CODE_ERROR_INVALID_KEY = new CryptoException(803,"Chave de criptografia inv?lida");
	public static final CryptoException RESPONSE_CODE_ERROR_INVALID_BLOCK = new CryptoException(804,"Tamanho de bloco de criptografia inv?lido");
	public static final CryptoException RESPONSE_CODE_ERROR_INVALID_PAD = new CryptoException(805,"Dimensionador de criptografia inv?lido");
	public static final CryptoException RESPONSE_CODE_ERROR_INVALID_PASS = new CryptoException(806,"C?digo de criptografia inv?lido");
	
	/**
	 * @param code
	 * @param message
	 */
	public CryptoException(int code, String message) {
		super(code,message);
	}	
	
	/**
	 * @param code
	 * @param message
	 * @param cause
	 */
	public CryptoException(int code, String message, Exception cause) {
		super(code,message,cause);
	}		
	
	/**
	 * @param general
	 * @param args
	 */
	public CryptoException(GeneralException general, String[]args) {
		super(general,args);
	}
	
	/**
	 * @param general
	 * @param arg
	 */
	public CryptoException(GeneralException general, String arg) {
		super(general,arg);
	}	
	
	/**
	 * @param general
	 * @param cause
	 */
	public CryptoException(GeneralException general, Exception cause) {
		super(general,cause);
	}	
	
	/**
	 * @param general
	 * @param arg
	 * @param cause
	 */
	public CryptoException(GeneralException general, String arg, Exception cause) {
		super(general,arg,cause);
	}	
	
	/**
	 * @param general
	 */
	public CryptoException(GeneralException general) {
		super(general);
	}	
	
	@Override
	public String toString() {
		return super.getCause().toString();	
	}
}
