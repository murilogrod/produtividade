package br.gov.caixa.dossie.generic;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**.
 * Descricao: Classe responsavel por realizar o encode ou decode em base64 de uma string qualquer.
 * Nome: Criptografia
 * @author c112460
 * @since 24/06/2014
 * @version 1.0
 */
public class Criptografia {
	
	    private static final String NAO_FOI_POSSIVEL_CRIAR_INSTANCIA_DA_CLASSE_CRIPTOGRAFIA = "Nao foi possivel criar instancia da Classe Criptografia ";
		private static final String NAO_FOI_POSSIVEL_REALIZAR_O_DECODE_DO_VALOR_INFORMADO = "Nao foi possivel realizar o decode do valor informado.";
	    private static final String NAO_FOI_POSSIVEL_REALIZAR_O_ENCODE_DO_VALOR_INFORMADO = "Nao foi possivel realizar o encode do valor informado.";
		private static final String UTF = "UTF8";
	    private static final Logger LOGGER = LoggerFactory .getLogger(Criptografia.class);
		private static String cryptokey;
	    private Cipher ecipher;
	    private Cipher dcipher;

	    private static byte[] salt = {
	        (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
	        (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
	    };

	    private static int iterationCount = 19;
	    private static AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
	    
	    /**.
	     * @param key
	     * @throws InvalidKeySpecException
	     * @throws NoSuchAlgorithmException
	     * @throws NoSuchPaddingException
	     * @throws InvalidKeyException
	     * @throws InvalidAlgorithmParameterException
	     */
	    private synchronized void initCipher(String key) throws InvalidKeySpecException,
	                                                                   NoSuchAlgorithmException, 
	                                                                   NoSuchPaddingException,
	                                                                   InvalidKeyException, 
	                                                                   InvalidAlgorithmParameterException {
	        KeySpec keySpec = new PBEKeySpec(key.toCharArray(), salt, iterationCount);
	        SecretKey secretKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
	        
	        ecipher = Cipher.getInstance(secretKey.getAlgorithm());
	        dcipher = Cipher.getInstance(secretKey.getAlgorithm());
	        ecipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
	        dcipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
	    }

	  
	    
	    /**.
	     * Metod responsavel por fazer o encode em base64 de uma string 
	     * @param valueToEncode
	     * @return String
	     */
	    public String encode(String valueToEncode) {
	       
    	   try {
	            
    		    byte[] valueBytesUtf8 = valueToEncode.getBytes(UTF);
	            byte[] valueBytes = ecipher.doFinal(valueBytesUtf8);
	            return Base64.encodeBase64String(valueBytes);
	            
	        } catch (javax.crypto.BadPaddingException e) {
	        	LOGGER.error(NAO_FOI_POSSIVEL_REALIZAR_O_ENCODE_DO_VALOR_INFORMADO, e);
	        } catch (IllegalBlockSizeException e) {
	        	LOGGER.error(NAO_FOI_POSSIVEL_REALIZAR_O_ENCODE_DO_VALOR_INFORMADO, e);
	        } catch (UnsupportedEncodingException e) {
	        	LOGGER.error(NAO_FOI_POSSIVEL_REALIZAR_O_ENCODE_DO_VALOR_INFORMADO, e);
	        } catch (Exception e) {
	        	LOGGER.error(NAO_FOI_POSSIVEL_REALIZAR_O_ENCODE_DO_VALOR_INFORMADO, e);
			}  
	            
	        return null;
	    }   

		     
	 	    
	    /**.
	     * Metodo responsavel por realizar o decode de uma string com encode na base 64
	     * @param valueToDecode
	     * @return String
	     */
	    public String decode(String valueToDecode) {
	       
	        try {
	        
	        	byte[] decodedBytes = Base64.decodeBase64(valueToDecode);
	            byte[] decodedBytesUtf8 = dcipher.doFinal(decodedBytes);
	            return new String(decodedBytesUtf8, UTF);
	        
	        } catch (javax.crypto.BadPaddingException e) {
	        	LOGGER.error(NAO_FOI_POSSIVEL_REALIZAR_O_DECODE_DO_VALOR_INFORMADO, e);
	        } catch (IllegalBlockSizeException e) {
	        	LOGGER.error(NAO_FOI_POSSIVEL_REALIZAR_O_DECODE_DO_VALOR_INFORMADO, e);
	        } catch (UnsupportedEncodingException e) {
	        	LOGGER.error(NAO_FOI_POSSIVEL_REALIZAR_O_DECODE_DO_VALOR_INFORMADO, e);
	        } catch (Exception e) {
	        	LOGGER.error(NAO_FOI_POSSIVEL_REALIZAR_O_DECODE_DO_VALOR_INFORMADO, e);
	        }
	    	
	        return null;
	    }     
	    
	    /**.
	     * Metodo responsavel por setar a chave para o encode ou decode 
	     * @param novaChave
	     */
	    public void addCryptoKey(String novaChave){
	    	
	    	try {
	    		cryptokey = novaChave;
	            initCipher(cryptokey);

	        } catch (java.security.InvalidAlgorithmParameterException e) {
	        	LOGGER.error(NAO_FOI_POSSIVEL_CRIAR_INSTANCIA_DA_CLASSE_CRIPTOGRAFIA, e);
	        } catch (java.security.spec.InvalidKeySpecException e) {
	        	LOGGER.error(NAO_FOI_POSSIVEL_CRIAR_INSTANCIA_DA_CLASSE_CRIPTOGRAFIA, e);
	        } catch (javax.crypto.NoSuchPaddingException e) {
	        	LOGGER.error(NAO_FOI_POSSIVEL_CRIAR_INSTANCIA_DA_CLASSE_CRIPTOGRAFIA, e);
	        } catch (java.security.NoSuchAlgorithmException e) {
	        	LOGGER.error(NAO_FOI_POSSIVEL_CRIAR_INSTANCIA_DA_CLASSE_CRIPTOGRAFIA, e);
	        } catch (java.security.InvalidKeyException e) {
	        	LOGGER.error(NAO_FOI_POSSIVEL_CRIAR_INSTANCIA_DA_CLASSE_CRIPTOGRAFIA, e);
	        } catch (Exception e) {
	        	LOGGER.error(NAO_FOI_POSSIVEL_CRIAR_INSTANCIA_DA_CLASSE_CRIPTOGRAFIA, e);
			}
	    	
	    }

}
