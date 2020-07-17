package br.gov.caixa.dossie.rs.resource;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.caixa.dossie.enumerate.EnumMensagemInterface;
import br.gov.caixa.dossie.rs.requisicao.MensagemEmailPushRequisicao;
import br.gov.caixa.dossie.rs.requisicao.MensagemPushRequisicao;
import br.gov.caixa.dossie.rs.requisicao.TokenRequisicao;
import br.gov.caixa.dossie.rs.retorno.DataRetorno;
import br.gov.caixa.dossie.rs.retorno.Retorno;
import br.gov.caixa.dossie.rs.service.PushService;
import br.gov.caixa.dossie.rs.service.analytics.MetricsService;

/**
 * @author CTMONSI
 * @since 01/09/2017
 */
@RequestScoped
@Path("/push")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PushResource extends Resource {
			
	private static final String OCORREU_UM_ERRO_AO_AUTENTICAR_NO_SICPU = "Ocorreu um erro ao autenticar no SICPU";

	private static final Logger LOGGER = LoggerFactory.getLogger(PushResource.class);
	
	@EJB
	private PushService pushService;
	
	@EJB
	private MetricsService metrics;	

	/**
	 * @param requisicao
	 * @return
	 */
	@POST
	@Path("/enviarMensagem")
	public Response enviarMensagem(MensagemPushRequisicao requisicao) {
		metrics.track("enviarMensagem", requisicao.toString());
		try {
			TokenRequisicao token = pushService.autenticar();

			Retorno retorno;
			if (token!=null && !token.getTokenAcesso().isEmpty()) {
				retorno = pushService.enviarMensagem(requisicao, token);
			} else {
				throw new Exception(OCORREU_UM_ERRO_AO_AUTENTICAR_NO_SICPU);
			}

			return build(Status.OK, retorno);
		} catch(Exception e) {
			LOGGER.error(EnumMensagemInterface.PUSH_ENVIO_MSG.getMensagem() + ": " + e);
			return build(Status.BAD_REQUEST, montarRetorno(EnumMensagemInterface.PUSH_ENVIO_MSG.getMensagem(), Boolean.TRUE));
		}
	}

	/**
	 * @param requisicao
	 * @return
	 */
	@POST
	@Path("/enviarMensagemEmail")
	public Response enviarMensagemEmail(MensagemEmailPushRequisicao requisicao) {
		metrics.track("enviarMensagemEmail", requisicao.toString());
		try {
			TokenRequisicao token = pushService.autenticar();				
			
			Retorno retorno;
			if (token!=null && !token.getTokenAcesso().isEmpty()) {
				retorno = pushService.enviarMensagemEmail(requisicao, token);
			} else {
				throw new Exception(OCORREU_UM_ERRO_AO_AUTENTICAR_NO_SICPU);
			}							
			return build(Status.OK, retorno);
		} catch(Exception e) {
			LOGGER.error(EnumMensagemInterface.PUSH_ENVIO_EMAIL.getMensagem() + ": " + e);
			return build(Status.BAD_REQUEST, montarRetorno(EnumMensagemInterface.PUSH_ENVIO_EMAIL.getMensagem(), Boolean.TRUE));
		}
	}
	
	/**
	 * @param requisicao
	 * @return
	 */
	@POST
	@Path("/listarInscritos")
	public Response listarInscritos() {
		try {
			TokenRequisicao token = pushService.autenticar();
			
			DataRetorno retorno;
			if (token!=null && !token.getTokenAcesso().isEmpty()) {
				retorno = pushService.listarInscritos(token);
			} else {
				throw new Exception(OCORREU_UM_ERRO_AO_AUTENTICAR_NO_SICPU);
			}

			return build(Status.OK, retorno);
		}catch (Exception e) {
			LOGGER.error(EnumMensagemInterface.PUSH_LISTAR_INSCRITOS.getMensagem() + ": " + e);
			return build(Status.BAD_REQUEST, montarRetorno(EnumMensagemInterface.PUSH_LISTAR_INSCRITOS.getMensagem() , Boolean.TRUE));
		}
	}
	
	@POST
	@Path("/listarInscritosMensagem")
	public Response listarInscritosMensagem() {
		try {
			TokenRequisicao token = pushService.autenticar();
			if (token!=null) {											
				
				DataRetorno retorno;
				if (token!=null && !token.getTokenAcesso().isEmpty()) {
					retorno = pushService.listarInscritosMensagem(token);
				} else {
					throw new Exception(OCORREU_UM_ERRO_AO_AUTENTICAR_NO_SICPU);
				}
								
				return build(Status.OK, retorno);
			} else {
				throw new Exception(EnumMensagemInterface.TOKEN_EXPIRADO.getMensagem());
			}
		}catch (Exception e) {
			LOGGER.error(EnumMensagemInterface.PUSH_LISTAR_INSCRITOS.getMensagem() + ": " + e);
			return build(Status.BAD_REQUEST, montarRetorno(EnumMensagemInterface.PUSH_LISTAR_INSCRITOS.getMensagem() , Boolean.TRUE));
		}
	}

	public PushService getPushService() {
		return pushService;
	}

	public void setPushService(PushService pushService) {
		this.pushService = pushService;
	}
}