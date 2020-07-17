package br.gov.caixa.dossie.rs.interceptors;

import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;


/**
 * @author CTMONSI
 */
public interface CustomPreprocessInterceptor extends PreProcessInterceptor {
	
	public ServerResponse preProcess(HttpRequest request, ResourceMethod method);
	public ServerResponse preProcess(HttpRequest request, ResourceMethodInvoker method);
	
}

