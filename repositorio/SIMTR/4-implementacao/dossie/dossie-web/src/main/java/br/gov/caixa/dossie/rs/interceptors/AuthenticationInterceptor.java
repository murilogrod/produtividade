//package br.gov.caixa.dossie.rs.interceptors;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.ejb.EJB;
//import javax.inject.Inject;
//import javax.servlet.http.HttpServletRequest;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.Cookie;
//import javax.ws.rs.core.HttpHeaders;
//import javax.ws.rs.core.MultivaluedMap;
//import javax.ws.rs.core.contNewCookie;
//import javax.ws.rs.core.Response.Status;
//import javax.ws.rs.ext.Provider;
//
//import org.jboss.resteasy.annotations.interception.ServerInterceptor;
//import org.jboss.resteasy.core.Headers;
//import org.jboss.resteasy.core.ResourceMethod;
//import org.jboss.resteasy.core.ResourceMethodInvoker;
//import org.jboss.resteasy.core.ServerResponse;
//import org.jboss.resteasy.spi.HttpRequest;
//import org.jboss.resteasy.spi.interception.PostProcessInterceptor;
//
//import br.gov.caixa.dossie.exception.GeneralException;
//import br.gov.caixa.dossie.rs.retorno.AuthenticationRetorno;
//import br.gov.caixa.dossie.rs.retorno.Retorno;
//import br.gov.caixa.dossie.rs.service.AuthenticationService;
//import br.gov.caixa.dossie.util.CookieUtil;
//import br.gov.caixa.dossie.util.DossieConstantes;
//
//@Provider
//@ServerInterceptor
//public class AuthenticationInterceptor implements CustomPreprocessInterceptor, PostProcessInterceptor {
//
//	@Context
//	private HttpServletRequest uri;
//	
//	@EJB
//	AuthenticationService service;
//	
//	@Inject
//	CookieUtil cookieUtil;
//	
//	public ServerResponse authentication(HttpRequest httpRequest) {
//		ServerResponse response = null;
//		String uriString = uri.getPathInfo();
//
//		try{
//			cookieUtil.setTokenTemAtualizacao(false);
//			if (headerCookie(httpRequest)) {
//				String navegador = httpRequest.getHttpHeaders().getRequestHeader(DossieConstantes.NAVEGADOR).get(0);
//				String credencial = httpRequest.getHttpHeaders().getCookies().get(DossieConstantes.CREDENCIAL).getValue();
//				String tokenAcesso = httpRequest.getHttpHeaders().getCookies().get(DossieConstantes.TOKENACESSO).getValue();
//				service.validarTokenAcesso(credencial, tokenAcesso, navegador);
//			} else if (isRenovarAcesso(httpRequest)) {
//				String navegador = httpRequest.getHttpHeaders().getRequestHeader(DossieConstantes.NAVEGADOR).get(0);
//				String credencial = httpRequest.getHttpHeaders().getCookies().get(DossieConstantes.CREDENCIAL).getValue();
//				String tokenRenovacao = httpRequest.getHttpHeaders().getCookies().get(DossieConstantes.TOKENRENOVACAO).getValue();
//				AuthenticationRetorno retorno = service.renovarTokenAcesso(credencial, tokenRenovacao, navegador);
//				cookieUtil.setTokenTemAtualizacao(true);
//				cookieUtil.builder(retorno.getCredencial(),retorno.getTokenAcesso(), retorno.getTokenRenovacao(),retorno.getTempoExpiracaoAcesso(),retorno.getTempoExpiracaoRenovacao());
//			} else if (!uriString.contains("/auth/") && !uriString.contains("/analyticres/")){
//				throw new GeneralException(GeneralException.RESPONSE_CODE_ERROR_FAIL, "Usuário não autenticado ou Autenticação de Usuário inválida.");
//			}
//		} catch(GeneralException ge) {
//			Retorno retorno = new Retorno();
//			retorno.setTemErro(true);
//			retorno.setMsgsErro(new ArrayList<String>());
//			retorno.getMsgsErro().add(ge.getMessage());
//			response = new ServerResponse(retorno, Status.UNAUTHORIZED.getStatusCode(), new Headers<Object>());
//		}
//		return response;
//	}
//
//	@Override
//	public ServerResponse preProcess(HttpRequest request, ResourceMethod method) {
//		return authentication(request);
//	}
//	
//	@Override
//	public ServerResponse preProcess(HttpRequest request, ResourceMethodInvoker method) {
//		return authentication(request);
//	}
//	
//	@Override
//	public void postProcess(ServerResponse serverResponse) {
//		MultivaluedMap<String, Object> headers = serverResponse.getMetadata();
//		if(cookieUtil.getTokenTemAtualizacao()){
//			
//			List<Object> cookies = headers.get(HttpHeaders.SET_COOKIE);
//			
//			if(cookies == null){
//                cookies = new ArrayList<Object>();
//                cookies.add(buildCookie(DossieConstantes.TOKENACESSO, String.valueOf(cookieUtil.getCookies().get(DossieConstantes.TOKENACESSO)),  (Integer)cookieUtil.getCookies().get(DossieConstantes.TEMPOEXPIRACAOACESSO)));
//                cookies.add(buildCookie(DossieConstantes.TOKENRENOVACAO, String.valueOf(cookieUtil.getCookies().get(DossieConstantes.TOKENRENOVACAO)), (Integer)cookieUtil.getCookies().get(DossieConstantes.TEMPOEXPIRACAORENOVACAO)));
//                cookies.add(buildCookie(DossieConstantes.CREDENCIAL, String.valueOf(cookieUtil.getCookies().get(DossieConstantes.CREDENCIAL)), (Integer)cookieUtil.getCookies().get(DossieConstantes.TEMPOEXPIRACAORENOVACAO)));
//                headers.put(HttpHeaders.SET_COOKIE, cookies);
//            }
//		}
//	}
//	
//	protected Boolean headerCookie(HttpRequest request){
//		return request !=null  && request.getHttpHeaders() != null &&  request.getHttpHeaders().getCookies() != null 
//				&& !request.getHttpHeaders().getCookies().isEmpty()
//				&& request.getHttpHeaders().getCookies().containsKey(DossieConstantes.CREDENCIAL) 
//				&& request.getHttpHeaders().getCookies().containsKey(DossieConstantes.TOKENRENOVACAO) 
//				&& request.getHttpHeaders().getCookies().containsKey(DossieConstantes.TOKENACESSO)
//				&& !request.getHttpHeaders().getRequestHeaders().isEmpty()
//				&& request.getHttpHeaders().getRequestHeaders().containsKey(DossieConstantes.NAVEGADOR);
//	}
//	
//	protected Boolean isRenovarAcesso (HttpRequest request){
//		return request !=null  && request.getHttpHeaders() != null &&  request.getHttpHeaders().getCookies() != null 
//				&& !request.getHttpHeaders().getCookies().isEmpty()
//				&& request.getHttpHeaders().getCookies().containsKey(DossieConstantes.CREDENCIAL) 
//				&& request.getHttpHeaders().getCookies().containsKey(DossieConstantes.TOKENRENOVACAO)
//				&& !request.getHttpHeaders().getCookies().containsKey(DossieConstantes.TOKENACESSO)
//				&& !request.getHttpHeaders().getRequestHeaders().isEmpty()
//				&& request.getHttpHeaders().getRequestHeaders().containsKey(DossieConstantes.NAVEGADOR);
//	}
//	
//	private NewCookie buildCookie(String chave,String valor,Integer validade){
//		return new NewCookie(new Cookie(chave, valor, "/", null),null,validade.intValue(),false);
//	}
//}