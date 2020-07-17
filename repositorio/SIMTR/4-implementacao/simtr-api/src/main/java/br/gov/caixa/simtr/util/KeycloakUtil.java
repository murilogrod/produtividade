package br.gov.caixa.simtr.util;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.gov.caixa.pedesgo.arquitetura.excecao.ParametroException;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJWT;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.pedesgo.arquitetura.util.UtilParametro;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class KeycloakUtil {

    @EJB
    private KeycloakService keycloakService;

    private String token;
    private long millisCaptura = 0;

    private static final String PROPRIEDADE_AZP = "azp";
    private static final String PROPRIEDADE_CLIENT_HOST = "clientHost";
    private static final String PROPRIEDADE_EXP = "exp";

    private static final String PROPRIEDADE_URL_SSO_INTRANET = "url_sso_intranet";
    private static final String PROPRIEDADE_CLIENT_ID = "simtr_resource_sso_filter";
    private static final String PROPRIEDADE_CLIENT_SECRET = "simtr_sso_client_secret";

    private static final Logger LOGGER = Logger.getLogger(KeycloakUtil.class.getName());

    public String getIpFromToken() {
        return this.getIpFromToken(null);
    }

    public String getClientId() {
        return this.getClientId(null);
    }

    public String getClientId(String accessToken) {
        if (accessToken == null) {
            accessToken = this.keycloakService.getAccessTokenString();
        }

        return UtilJson.consultarAtributo(UtilJWT.getPayloadDecoded(accessToken), PROPRIEDADE_AZP);
    }

    public String getIpFromToken(String token) {
        if (token == null) {
            token = this.getTokenServico();
        }

        //Retorna a informação do token de serviço obtido ou loga o erro e retorna nulo caso não consiga obter o token
        if (token != null) {
            return UtilJson.consultarAtributo(UtilJWT.getPayloadDecoded(token), PROPRIEDADE_CLIENT_HOST);
        } else {
            LOGGER.severe("Falha ao obter token de serviço junto ao SSO. Valor recebido: null.");
            return null;
        }
    }

    public String getAccessTokenCliente() {
        return this.keycloakService.getAccessTokenString();
    }

    public String getTokenServico() {
        if (token == null) {
            this.obtemNovoToken();
            return token;
        }

        long momento = Calendar.getInstance().getTimeInMillis();

        //Captura o timestamp do token definido em segundos e multiplica por 1000 pois o java trabalha com o tempo em milissegundos
        long expire = Long.valueOf(UtilJson.consultarAtributo(UtilJWT.getPayloadDecoded(token), PROPRIEDADE_EXP)) * 1000;
        LOGGER.log(Level.FINE, "Calendar getTime(Millis): ".concat(String.valueOf(momento)));
        LOGGER.log(Level.FINE, "AccessToken exp (Millis): ".concat(String.valueOf(expire)));
        LOGGER.log(Level.FINE, "Base Captura    (Millis): ".concat(String.valueOf(millisCaptura + 300000)));
        //if (expire - momento < 30000) {
        //@TODO: Substituir a clausula IF pela clausula comentada após ajustar a propriedade "exp" obtida no token
        if ((millisCaptura + 300000) - momento < 30000) {
            this.obtemNovoToken();
            millisCaptura = momento;
        } else {
            //@TODO: Remover essa clausula ELSE após resolução do problema de DataHora devido ao horario de verão
            //this.obtemNovoToken();
        }

        return token;
    }

    private String obtemNovoToken() {
        return this.obtemNovoToken(null, null);
    }
    
    public String obtemNovoToken(String clientId, String clientSecret) {
        try {
            String urlSSO = UtilParametro.getParametro(PROPRIEDADE_URL_SSO_INTRANET, Boolean.TRUE);
            if(clientId == null) {
                clientId = UtilParametro.getParametro(PROPRIEDADE_CLIENT_ID, Boolean.TRUE);
            }
            if(clientSecret == null) {
                clientSecret = UtilParametro.getParametro(PROPRIEDADE_CLIENT_SECRET, Boolean.TRUE);
            }
            token = this.keycloakService.getAccessTokenServico(urlSSO, clientId, clientSecret, Boolean.TRUE);
        } catch (ParametroException e) {
            LOGGER.log(Level.SEVERE, "Falha ao obter token de serviço junto ao SSO. Ocorrência de exceção", e);
        }
        return token;
    }
}
