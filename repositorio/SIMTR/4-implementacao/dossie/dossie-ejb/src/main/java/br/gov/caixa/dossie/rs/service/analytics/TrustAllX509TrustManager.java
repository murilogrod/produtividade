package br.gov.caixa.dossie.rs.service.analytics;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * 
 * @author CTMONSI 
 *
 * Não usar em produção, apenas para teste em desenvolvimento.
 *
 */
public class TrustAllX509TrustManager implements X509TrustManager {
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    public void checkClientTrusted(java.security.cert.X509Certificate[] certs,
            String authType) {
    }

    public void checkServerTrusted(java.security.cert.X509Certificate[] certs,
            String authType) {
    }
}