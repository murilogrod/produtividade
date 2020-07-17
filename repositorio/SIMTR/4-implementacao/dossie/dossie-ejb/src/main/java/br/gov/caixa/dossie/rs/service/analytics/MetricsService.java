package br.gov.caixa.dossie.rs.service.analytics;

/*
	Nesta classe é definida a integração com a coletagem de métricas. 	
	
	Esta coleta é enviada ao CaixaAnalythics e possibilita um amplo acompanhamento da utilização do seu projeto por seus clientes.
	Os dados são enviados por post via threads assíncronas. 
	
	Para seu correto funcionamento é necessário acesso a internet, no entanto a impossibilidade de acessar a internet não irá prejudicar o funcionamento da aplicação.
	Para não comprometer a privacidade do seu usuario, nenhuma informacao particular deve ser enviada.
	ex: cpf, data de nascimento, endereco, etc
*/

import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

/**
 * @author SIOGP
 */
@Stateless
@LocalBean
@Named
public class MetricsService {
	
	@EJB
	private AnalyticsService analyticsService;
	
	private CaixaAnalytics tracker;

	private static final String ANALYTHICS_CODE = "5e877bf5a0b69db79f090e3a5c16bab08246714c"; 
	private static final String APP_NAME = "DOSSIE:8515fd75704c8aa4f9c3d1a187e078f0beea33f1";
	private static final String APP_ID = "8515fd75704c8aa4f9c3d1a187e078f0beea33f1";
	private static final String APP_CID = "DOSSIE_SERVICE";
	
	private static final String APP_VERSION = "1.0";
	private static final String APP_DESCR = "Dossiê Digital";
	private static final String APP_TYPE = "ANGULAR003";
	private static final String APP_CONTACT = "usuario@caixa.gov.br";
	private static final String APP_BUILDER_NAME = "usuario@caixa.gov.br";
	private static final String APP_BUILDER_FULL_NAME = "Usuario Caixa";
	private static final String APP_USER_ID = "5598";
	private static final String APP_VIEWS = "38";
	private static final String APP_MENUS = "10";	

	/**
	 * init
	 */
	@PostConstruct
	public void init(){
		if (ANALYTHICS_CODE != null) {			
			
			final int session = randInt(1, 2147483647);
			tracker = new CaixaAnalytics(ANALYTHICS_CODE, session, APP_CID, analyticsService.getUrlSimma());
			
			this.track("appID",APP_ID);
			this.track("appCID",APP_CID);
			this.track("appName",APP_NAME);
			this.track("appVersion",APP_VERSION);
			this.track("appDescr",APP_DESCR);
			this.track("appType",APP_TYPE);
			this.track("appContact",APP_CONTACT);
			this.track("appBuilderName",APP_BUILDER_NAME);
			this.track("appBuilderFullName",APP_BUILDER_FULL_NAME);
			this.track("appUserId",APP_USER_ID);
			
			this.track("appViews",APP_VIEWS);
			this.track("appMenus",APP_MENUS);
		}
	}
	
	/**
	 * @param action
	 * @param label
	 */
	public void track(String action, String label) {
		track(action, label, null);
	}

	/**
	 * @param action
	 */
	public void track(String action) {
		track(action, null, null);
	}

	/**
	 * @param action
	 * @param label
	 * @param version
	 */
	public void track(String action, String label, Integer version) {
		if (tracker == null) {
			return;
		}
		if (version != null) {
			tracker.trackEvent(action, label, version);
		} else if (label != null) {
			tracker.trackEvent(action, label);
		} else {
			tracker.trackEvent(action);
		}
	}
	
	/**
	 * @param message
	 */
	public void trackError(String message) {
		tracker.trackError(message);
	}
	/**
	 * @param message
	 * @param code
	 */
	public void trackError(String message, int code) {
		tracker.trackError(message, code);
	}
	/**
	 * @param message
	 */
	public void trackWarn(String message) {
		tracker.trackWarn(message);
	}
	/**
	 * @param message
	 * @param code
	 */
	public void trackWarn(String message, int code) {
		tracker.trackWarn(message, code);
	}
	
	private int randInt(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}
}