package br.gov.caixa.simtr.util;

import br.gov.caixa.pedesgo.arquitetura.excecao.ParametroException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.pedesgo.arquitetura.util.UtilParametro;
import java.io.IOException;
import java.util.Calendar;
import org.apache.commons.io.IOUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UtilParametro.class, UtilJson.class})
public class KeycloakUtilTest {

    private static final String RESOURCE_DIR = "/mock/util/";
    private static final String UTF_8 = "UTF-8";

    @Mock
    private KeycloakService keycloakService;

    @InjectMocks
    private final KeycloakUtil keycloackUtil = new KeycloakUtil();

    private String token;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(UtilParametro.class);

        PowerMockito.when(UtilParametro.getParametro("url_sso_intranet", true)).thenReturn("url-sso");
        PowerMockito.when(UtilParametro.getParametro("simtr_resource_sso_filter", true)).thenReturn("simtr-client-id");
        PowerMockito.when(UtilParametro.getParametro("simtr_sso_client_secret", true)).thenReturn("simtr-secret-id");

        token = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("token")), UTF_8);
    }

    @Test
    public void testGetIpFromToken() throws IOException {
        Mockito.when(this.keycloakService.getAccessTokenServico(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.eq(true))).thenReturn(null);
        Assert.assertNull(this.keycloackUtil.getIpFromToken());

        Mockito.when(this.keycloakService.getAccessTokenServico(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.eq(true))).thenReturn(token);
        Assert.assertNotNull(this.keycloackUtil.getIpFromToken());

        Assert.assertNotNull(this.keycloackUtil.getIpFromToken(token));
    }

    @Test
    public void testeFalhaObterParametroGetIpFromToken() {
        Mockito.when(this.keycloakService.getAccessTokenServico(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.eq(true))).thenThrow(new ParametroException("Mensagem de Exceção"));
        Assert.assertNull(this.keycloackUtil.getIpFromToken());
    }

    @Test
    public void testeObterTokenServico() throws IOException {
        Mockito.when(this.keycloakService.getAccessTokenServico(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.eq(true))).thenReturn(token);

        //Chama o metodo uma vez pra obter o token e chama o metodo novamente para forçar a existencia do token
        this.keycloackUtil.getTokenServico();
        Assert.assertNotNull(this.keycloackUtil.getTokenServico());

        //Mocka a classe long para retornar o timestamp atual em segundos de forma viabilizar o teste de tempo inferior a 30 segundos
        long expire = (Calendar.getInstance().getTimeInMillis() / 1000) + 600;
        PowerMockito.mockStatic(UtilJson.class);
        PowerMockito.when(UtilJson.consultarAtributo(Mockito.anyString(), Mockito.eq("exp"))).thenReturn(String.valueOf(expire));
        Assert.assertNotNull(this.keycloackUtil.getTokenServico());
    }
}
