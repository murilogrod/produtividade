package br.gov.caixa.simtr.controle.servico;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.pedesgo.arquitetura.util.UtilRest;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.analytics.EnviarAnalyticsDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class AnalyticsServico {

	private static final Logger LOGGER = Logger.getLogger(AnalyticsServico.class.getName());
	
    @EJB
    private CanalServico canalServico;
    
    @EJB
    private KeycloakService keycloakService;
    
    @PermitAll
    public void registraEvento(String verbo, String endpoint, String engine) {
    	this.registraEvento(verbo, endpoint, engine, null);
    }
    
    /**
     * Metodo responsável por enviar as informações do serviço utilizado para o Analytics, registrando as informações.
     * 
     * 
     * @param verbo Verbo a ser registrado no Analystics
     * @param endpoint Endpoint a ser registrado no Analystics
     * @param engine Engine a ser registrada no Analystics
     */
    @PermitAll
    public void registraEvento(String verbo, String endpoint, String engine, String valor) {
    	Boolean analitycs = Boolean.getBoolean("simtr.analitycs");
    	if(analitycs) {
            Canal canal = this.canalServico.getByClienteSSO();
            this.canalServico.validaRecursoLocalizado(canal, "AS.rE.001 - Não foi localizado canal sob o identificador de cliente SSO {clientID}. Necessário entrar em contato com o gestor do SIMTR.");
            
            String analitycsUrl = System.getProperty("url.servico.analytics");
            
            EnviarAnalyticsDTO corpo = carregarCorpoMensagemAnalytics(verbo, endpoint, engine, valor, canal.getSigla());
        	try {
        		final String corpoMensagem = UtilJson.converterParaJson(corpo);
        		UtilRest.consumirServicoOAuth2JSON(analitycsUrl, EnumMetodoHTTP.POST, new HashMap<>(), new HashMap<>(), corpoMensagem, this.keycloakService.getAccessTokenString());
			} catch (Exception e) {
				LOGGER.log(Level.INFO, "AS.rE.002 - Erro ao enviar informações para o analytics.", e);
			}
    	}
    	
    }

    /**
     * Carrega o corpo da mensagem a ser enviado ao analytics.
     * 
     * @param verbo Verbo a ser salvo como base da acao do usuario
     * @param endpoint endpoint utilizado a ser salvo no analytics
     * @param engine Informacoes contidas no user-agent
     * @param canal 
     * @return Corpo carregado a ser enviado para analytics
     */
	private EnviarAnalyticsDTO carregarCorpoMensagemAnalytics(String verbo, String endpoint, String engine, String valor, String canal) {
		EnviarAnalyticsDTO corpo = new EnviarAnalyticsDTO();
		corpo.setAcao(canal);
		corpo.setUserAgente(engine);
		corpo.setEtiqueta(verbo + " " + endpoint);
		corpo.setValor(valor);
		corpo.setCategoria("API");
		corpo.setChave(System.getProperty("analitycs.simtr.key"));
//		corpo.setNomeNavegador("");
//		corpo.setNomeSistemaOperacional("");
//		corpo.setSessao(valor);
//		corpo.setDispositivo("");
//		corpo.setVersaoNavegador("");
//		corpo.setVersaoSistemaOperacional("");
		return corpo;
	}
    
}
