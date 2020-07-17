package br.gov.caixa.dossie.rs.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.gov.caixa.dossie.Authentication;
import br.gov.caixa.dossie.rs.retorno.Retorno;

import br.gov.caixa.dossie.util.DossieConstantes;

/**
 * @author SIOGP
 */
public class Resource {
	
    protected Authentication auth = Authentication.getInstance();
	
	protected static final String CREDENCIAL = "credencial";
	protected static final String TOKENACESSO = "tokenAcesso";
	protected static final String TOKENRENOVACAO = "tokenRenovacao";
	
	protected static final String NEW_CREDENCIAL = "new_credencial";
	protected static final String NEW_TOKENACESSO = "new_tokenAcesso";
	protected static final String NEW_TOKENRENOVACAO = "new_tokenRenovacao";
	
	protected Response build(Status status, Object object) {
		return Response.status(status)
				.entity(object)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
				.header("Access-Control-Max-Age", "1209600")
				.header("Access-Control-Allow-Headers", "Origin, X-Request-Width, Content-Type, Accept")
				.header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", 0)
                .header("X-Frame-Options", "DENY")
                .header("X-XSS-Protection", "1")
				.build();
	}
	
	protected Response semAutorizacao(int authCode) {
	    final List<String> msgsErro = new ArrayList<String>();
	
	    msgsErro.add(DossieConstantes.ERRO_CODIGO_NAO_AUTORIZADO + Integer.toString(authCode));                                               
	
	    Retorno retorno = new Retorno();
	    retorno.setTemErro(true);
	    retorno.setMsgsErro(msgsErro);                                            
	
	    return build(Status.UNAUTHORIZED, retorno);
	}
	
	protected Response buildFile(Status status, Object object, String nomeArquivo) {
		return Response.status(status)
				.entity(object)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Headers", "Origin, X-Request-Width, Content-Type, Accept")
				.header("Content-Type", "application/pdf")
				.header("Content-Disposition", "attachment; filename="+ nomeArquivo +".pdf")
				.build();
	}
	
	protected Retorno getMensagemErro(final String msg){
		 Retorno retorno = new Retorno();

		 List<String> msgs = new ArrayList<String>();
		 msgs.add(msg);
		 retorno.setMsgsErro(msgs);
		 
		 return retorno;
	}
	
	protected Retorno montarRetorno(final String mensagem, Boolean temErro) {
		Retorno retorno = new Retorno();
		List<String> msgErro = new ArrayList<String>();

		msgErro.add(mensagem);
		retorno.setTemErro(temErro);
		retorno.setMsgsErro(msgErro);

		return retorno;
	}
}