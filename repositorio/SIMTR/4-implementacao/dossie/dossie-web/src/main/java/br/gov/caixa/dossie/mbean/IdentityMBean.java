package br.gov.caixa.dossie.mbean;

import java.util.Date;

/**
 * @author SIOGP
 */
public interface IdentityMBean {
	
	/**
	 * @return
	 */
	int getCode();
	
	/**
	 * @return
	 */
	String getId();
	
	/**
	 * @return
	 */
	String getName();
	
	/**
	 * @return
	 */
	String getDescription();
	
	/**
	 * @return
	 */
	String getFormattedVersion();
	
	/**
	 * @return
	 */
	int getVersion();
	
	/**
	 * @return
	 */
	int getBuild();
	
	/**
	 * @return
	 */
	int getRelease();
	
	/**
	 * @return
	 */
	String getContext();
	
	/**
	 * @return
	 */
	String getIcon();
	
	/**
	 * @return
	 */
	String getExtraInfo();
	
	/**
	 * @return
	 */
	Date getDate();
	
	/**
	 * @return
	 */
	String getFormattedDate();
	
	/**
	 * @return
	 */
	String getUser();
	
	/**
	 * @return
	 */
	String getOwner();
	
	/**
	 * @return
	 */
	String getFramework();
    
    /**
	 * @return
	 */
	String getFormattedLoadTime();
    
    /**
	 * @return
	 */
	Date getLoadTime();	
    
    /**
	 * @return
	 */
	int getAuthCode();
    
    /**
	 * @return
	 */
	Date getAuthDate();     
    
    /**
	 * @return
	 */
	String getFormattedAuthDate();
	
	/**
	 * @return
	 */
	void generate();
	
	/**
	 * @param min
	 * @param max
	 */
	void generateCode(int min, int max);
	
	/**
	 * @param validToHours
	 */
	void generateDate(int validToHours); 
}
