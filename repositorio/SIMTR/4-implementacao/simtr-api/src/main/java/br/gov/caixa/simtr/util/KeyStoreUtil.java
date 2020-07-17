package br.gov.caixa.simtr.util;

import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.text.MessageFormat;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class KeyStoreUtil {

    private static final Logger LOGGER = Logger.getLogger(KeyStoreUtil.class.getName());

    /**
     * Obtem o valor de uma chave existente em um arquivo criptografado de
     * chaves, conhecido por KeyStore
     *
     * @param keyStoreURL Path completo para acesso ao arquivo de repositorio de
     * chaves
     * @param storeType Indicação do tipo de repositorio também conhecido por
     * algoritmo do repositorio (Ex: JCEKS)
     * @param keyStoreSecret Password definida para acesso ao arquivo de chaves
     * @param keyAlias Nome do alias desejado dentro do arquivo
     * @param keySecret Senha de acesso ao alias existente no arquivo
     * @return
     */
    public static String getDataFromKeyStore(String keyStoreURL, String storeType, String keyStoreSecret, String keyAlias, String keySecret) {
        try (InputStream keystoreStream = new FileInputStream(keyStoreURL)) {
            //Captura o arquivo de senhas como stream de dados

            KeyStore keystore = KeyStore.getInstance(storeType);

            //Abre o arquivo para a leitura das propriedades
            keystore.load(keystoreStream, keyStoreSecret.toCharArray());

            //Captura o registro criptografado do arquivo baseado no alias e keypass definidos na inclusão do registro
            Key key = keystore.getKey(keyAlias, keySecret.toCharArray());

            //Obtem o objeto manipulador de registro PBE (Password Based Encryption) baseado no registro obtido do arquivo de senhas
            SecretKeyFactory factory = SecretKeyFactory.getInstance(key.getAlgorithm());
            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keystore.getEntry(keyAlias, new KeyStore.PasswordProtection(keySecret.toCharArray()));
            PBEKeySpec keySpec = (PBEKeySpec) factory.getKeySpec(secretKeyEntry.getSecretKey(), PBEKeySpec.class);

            //Obtem a senha que estava armazenada em texto limpo
            return String.valueOf(keySpec.getPassword());

        } catch (KeyStoreException ex) {
            String mensagem = MessageFormat.format("KSU.gDFKS.001 - Falha ao acessar a KeyStore. KeyStore informada: {0}", keyStoreURL);
            throw new SimtrConfiguracaoException(mensagem, ex);
        } catch (CertificateException | IOException | NoSuchAlgorithmException | UnrecoverableEntryException ex) {
            String mensagem = MessageFormat.format("KSU.gDFKS.002 - Falha ao obter valor na Keystore. Verificar os valores de secret utilizados para KeyStore {0} e KeyAlias {1}", keyStoreSecret, keyAlias);
            String mensagemLog = MessageFormat.format("KSU.gDFKS.002 - Falha ao obter valor na Keystore. Verificar os valores de secret utilizados para KeyStore {0} e KeyAlias {1} - KeyStoreSecret: {2} | KeySecret: {3}", keyStoreSecret, keyAlias, keyStoreSecret, keySecret);
            LOGGER.warning(mensagemLog);
            throw new SimtrConfiguracaoException(mensagem, ex);
        } catch (InvalidKeySpecException ex) {
            throw new SimtrConfiguracaoException("KSU.gDFKS.003 - Falha ao obter a especificação de chave. Verificar consistência do Keystore e importação das chaves como PBE (Password Based Encryption)", ex);
        }
    }

}
