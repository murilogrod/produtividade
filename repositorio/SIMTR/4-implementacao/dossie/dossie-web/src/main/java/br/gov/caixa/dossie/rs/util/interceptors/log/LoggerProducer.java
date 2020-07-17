package br.gov.caixa.dossie.rs.util.interceptors.log;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author SIOGP
 */
public class LoggerProducer {

	/**
	 * @param injectionPoint
	 * @return
	 */
    @Produces
    public Logger getLogger(InjectionPoint injectionPoint) {
        Class<?> clazz;
        if (injectionPoint.getBean() != null) {
            clazz = injectionPoint.getBean().getBeanClass();
        } else {
            clazz = injectionPoint.getMember().getDeclaringClass();
        }
        return LoggerFactory.getLogger(clazz);
    }
}
