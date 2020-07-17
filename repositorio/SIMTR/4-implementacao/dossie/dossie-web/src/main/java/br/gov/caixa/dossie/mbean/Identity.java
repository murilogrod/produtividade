package br.gov.caixa.dossie.mbean;


/*
	Nesta classe é definido a identificação do seu projeto. 	
	Esta identificação é lida pelo SIPRO via JMX de forma a apresentar informações sobre o projeto publicado na tela inicial.
*/

import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.log4j.Logger;

import br.gov.caixa.dossie.Authentication;

import br.gov.caixa.dossie.util.DossieConstantes;

/**
 * @author SIOGP
 */
@Singleton
@Startup
public class Identity implements IdentityMBean {
	
	private static final Logger LOG = Logger.getLogger(Identity.class);
	
	private static final String BEAN_ID = "br.gov.caixa.app:deployment=dossie-ear.ear";
	private static final int CODE = 985;
	private static final String ID = "DOSSIE";
	private static final String NAME = "DOSSIE";
	private static final String DESCRIPTION = "Dossiê Digital";
	private static final String VERSION = "1.0";
	private static final String CONTEXT = "/app/dossie";
	private static final String ICON = "fa-ils";
	private static final String EXTRA_INFO = "lastchange:'07/05/2018 14:31:27',menu:true,contact:'usuario@caixa.gov.br'";
	private static final String DATE =  "07-05-2018 14:32";
	private static final String FRAMEWORK = "ANGULAR003";
	private static final String USER = "usuario@caixa.gov.br";
	private static final String OWNER = "Caixa Econômica Federal";
	
	private Date loadTime = new Date(); 
	
	private Authentication auth = Authentication.getInstance();
	
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public Identity() {
		inicializa();
	}

	private void inicializa() {
	
		LOG.info("Identity()");
		LOG.info("code: " + getCode());
		LOG.info("id: " + getId());
		LOG.info("name: " + getName());
		LOG.info("description: " + getDescription());
		LOG.info("version: " + getFormattedVersion());
		LOG.info("context: " + getContext());
		LOG.info("icon: " + getIcon());
		LOG.info("extraInfo: " + getExtraInfo());
		LOG.info("date: " + getFormattedDate());
		LOG.info("framework: " + getFramework());
		LOG.info("user: " + getUser());
		LOG.info("owner: " + getOwner());
		LOG.info("authCode: " + getAuthCode());
		LOG.info("authDate: " + getAuthDate());		
		
		registerBean();		
	}

	/**
	 * registerBean()
	 */
	public void registerBean() {
		final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
		try {
			ObjectName objectName = new ObjectName(BEAN_ID);
			
			try {
				server.unregisterMBean(objectName);
			} catch (InstanceNotFoundException ignore) {
				LOG.error(ignore);
			}
			
			server.registerMBean(this, objectName);
			
			LOG.info(DossieConstantes.INFO_MBEAN_REGISTRADO + objectName);			
		} catch (MalformedObjectNameException mone) {
			LOG.error(mone);
		} catch (InstanceAlreadyExistsException iaee) {
			LOG.error(iaee);
		} catch (MBeanRegistrationException mbre) {
			LOG.error(mbre);
		} catch (NotCompliantMBeanException ncmbe) {
			LOG.error(ncmbe);
		}
	}

	@Override
	public int getCode() {
		return CODE;
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public int getBuild() {
		return 0;
	}

	@Override
	public int getRelease() {
		return 0;
	}
	

	@Override
	public String getFormattedVersion() {
		return VERSION;
	}	

	@Override
	public String getContext() {
		return CONTEXT;
	}

	@Override
	public String getIcon() {
		return ICON;
	}
	
	@Override
	public String getExtraInfo() {
		return EXTRA_INFO;
	}	

	@Override
	public Date getDate() {
		Date result = null;
		DateFormat df = new SimpleDateFormat(DossieConstantes.PATTERN_DD_MM_YYYY_HH_MM);
		try {
			result = df.parse(DATE);
		} catch (ParseException e) {
			LOG.error(e);
		}
		return result;
	}

	@Override
	public String getFormattedDate() {		
		return DATE;
	}	
	
	@Override
	public String getFramework() {
		return FRAMEWORK;
	}	

	@Override
	public String getUser() {
		return USER;
	}
	
	@Override
	public String getOwner() {
		return OWNER;
	}

	@Override
	public String getFormattedLoadTime() {
		DateFormat df = new SimpleDateFormat(DossieConstantes.PATTERN_DD_MM_YYYY_HH_MM);
		return df.format(loadTime);
	}

	@Override
	public Date getLoadTime() {
		return loadTime;
	}	
	
	@Override
	public int getAuthCode() {
		return auth.getCode();
	}

	@Override
	public Date getAuthDate() {
		return auth.getDate();
	}		
	
	@Override
	public String getFormattedAuthDate() {
		DateFormat df = new SimpleDateFormat(DossieConstantes.PATTERN_DD_MM_YYYY_HH_MM);
		Date authDate = auth.getDate();
		return df.format(authDate);
	}	
	
	@Override
	public void generate() {
		auth.generate();		
	}

	@Override
	public void generateCode(int min, int max) {
		auth.generateCode(min, max);		
	}

	@Override
	public void generateDate(int validToHours) {
		auth.generateDate(validToHours);		
	}
}
