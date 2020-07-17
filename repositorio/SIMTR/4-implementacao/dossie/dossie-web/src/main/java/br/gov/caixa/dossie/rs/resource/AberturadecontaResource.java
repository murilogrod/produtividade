package br.gov.caixa.dossie.rs.resource;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.gov.caixa.dossie.rs.requisicao.AberturadecontaRequisicao;
import br.gov.caixa.dossie.rs.retorno.Retorno;
import br.gov.caixa.dossie.rs.service.AberturadecontaService;
import br.gov.caixa.dossie.rs.service.analytics.MetricsService;
import br.gov.caixa.dossie.util.DossieConstantes;

/**
 * @author SIOGP
 */
@RequestScoped
@Path("/aberturadeconta")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class AberturadecontaResource extends Resource {
	@EJB
	private AberturadecontaService service;
	@EJB
	private MetricsService metrics;

	/**
	 * init()
	 */
//    @PostConstruct
//    public void init() {
//	metrics.track(DossieConstantes.METRICS_WEB_SERVICES, AberturadecontaResource.class.getSimpleName());
//    }

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/informacesdoclienteeparametrosdaconta")
	public Response createInformacesdoclienteeparametrosdaconta(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/informacesdoclienteeparametrosdaconta/{id}")
	public Response updateInformacesdoclienteeparametrosdaconta(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id, AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/informacesdoclienteeparametrosdaconta")
	public Response readAllInformacesdoclienteeparametrosdaconta(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/informacesdoclienteeparametrosdaconta/{id}")
	public Response readInformacesdoclienteeparametrosdaconta(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/informacesdoclienteeparametrosdaconta/{id}")
	public Response deleteInformacesdoclienteeparametrosdaconta(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/informacesdoclientecontaconjunta")
	public Response createInformacesdoclientecontaconjunta(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/informacesdoclientecontaconjunta/{id}")
	public Response updateInformacesdoclientecontaconjunta(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id, AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/informacesdoclientecontaconjunta")
	public Response readAllInformacesdoclientecontaconjunta(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/informacesdoclientecontaconjunta/{id}")
	public Response readInformacesdoclientecontaconjunta(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/informacesdoclientecontaconjunta/{id}")
	public Response deleteInformacesdoclientecontaconjunta(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/informacesdoclienteprocuradortutorcurador")
	public Response createInformacesdoclienteprocuradortutorcurador(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/informacesdoclienteprocuradortutorcurador/{id}")
	public Response updateInformacesdoclienteprocuradortutorcurador(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id, AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/informacesdoclienteprocuradortutorcurador")
	public Response readAllInformacesdoclienteprocuradortutorcurador(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/informacesdoclienteprocuradortutorcurador/{id}")
	public Response readInformacesdoclienteprocuradortutorcurador(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/informacesdoclienteprocuradortutorcurador/{id}")
	public Response deleteInformacesdoclienteprocuradortutorcurador(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/pesquisascadastrais")
	public Response createPesquisascadastrais(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/pesquisascadastrais/{id}")
	public Response updatePesquisascadastrais(@HeaderParam("authCode") int authCode, @PathParam("id") Long id,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/pesquisascadastrais")
	public Response readAllPesquisascadastrais(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/pesquisascadastrais/{id}")
	public Response readPesquisascadastrais(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/pesquisascadastrais/{id}")
	public Response deletePesquisascadastrais(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/digitalizacaodedocumentos")
	public Response createDigitalizacaodedocumentos(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/digitalizacaodedocumentos/{id}")
	public Response updateDigitalizacaodedocumentos(@HeaderParam("authCode") int authCode, @PathParam("id") Long id,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/digitalizacaodedocumentos")
	public Response readAllDigitalizacaodedocumentos(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/digitalizacaodedocumentos/{id}")
	public Response readDigitalizacaodedocumentos(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/digitalizacaodedocumentos/{id}")
	public Response deleteDigitalizacaodedocumentos(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/digitalizacaodocidentificacaofechado")
	public Response createDigitalizacaodocidentificacaofechado(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/digitalizacaodocidentificacaofechado/{id}")
	public Response updateDigitalizacaodocidentificacaofechado(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id, AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/digitalizacaodocidentificacaofechado")
	public Response readAllDigitalizacaodocidentificacaofechado(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/digitalizacaodocidentificacaofechado/{id}")
	public Response readDigitalizacaodocidentificacaofechado(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/digitalizacaodocidentificacaofechado/{id}")
	public Response deleteDigitalizacaodocidentificacaofechado(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/digitalizacaodocidentificacaoaberto")
	public Response createDigitalizacaodocidentificacaoaberto(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/digitalizacaodocidentificacaoaberto/{id}")
	public Response updateDigitalizacaodocidentificacaoaberto(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id, AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/digitalizacaodocidentificacaoaberto")
	public Response readAllDigitalizacaodocidentificacaoaberto(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/digitalizacaodocidentificacaoaberto/{id}")
	public Response readDigitalizacaodocidentificacaoaberto(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/digitalizacaodocidentificacaoaberto/{id}")
	public Response deleteDigitalizacaodocidentificacaoaberto(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/digitalizacaocompresidnciafechado")
	public Response createDigitalizacaocompresidnciafechado(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/digitalizacaocompresidnciafechado/{id}")
	public Response updateDigitalizacaocompresidnciafechado(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id, AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/digitalizacaocompresidnciafechado")
	public Response readAllDigitalizacaocompresidnciafechado(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/digitalizacaocompresidnciafechado/{id}")
	public Response readDigitalizacaocompresidnciafechado(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/digitalizacaocompresidnciafechado/{id}")
	public Response deleteDigitalizacaocompresidnciafechado(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/digitalizacaocompresidnciaaberto")
	public Response createDigitalizacaocompresidnciaaberto(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/digitalizacaocompresidnciaaberto/{id}")
	public Response updateDigitalizacaocompresidnciaaberto(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id, AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/digitalizacaocompresidnciaaberto")
	public Response readAllDigitalizacaocompresidnciaaberto(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/digitalizacaocompresidnciaaberto/{id}")
	public Response readDigitalizacaocompresidnciaaberto(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/digitalizacaocompresidnciaaberto/{id}")
	public Response deleteDigitalizacaocompresidnciaaberto(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/digitalizacaocomprendafechado")
	public Response createDigitalizacaocomprendafechado(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/digitalizacaocomprendafechado/{id}")
	public Response updateDigitalizacaocomprendafechado(@HeaderParam("authCode") int authCode, @PathParam("id") Long id,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/digitalizacaocomprendafechado")
	public Response readAllDigitalizacaocomprendafechado(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/digitalizacaocomprendafechado/{id}")
	public Response readDigitalizacaocomprendafechado(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/digitalizacaocomprendafechado/{id}")
	public Response deleteDigitalizacaocomprendafechado(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/digitalizacaocomprendaaberto")
	public Response createDigitalizacaocomprendaaberto(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/digitalizacaocomprendaaberto/{id}")
	public Response updateDigitalizacaocomprendaaberto(@HeaderParam("authCode") int authCode, @PathParam("id") Long id,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/digitalizacaocomprendaaberto")
	public Response readAllDigitalizacaocomprendaaberto(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/digitalizacaocomprendaaberto/{id}")
	public Response readDigitalizacaocomprendaaberto(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/digitalizacaocomprendaaberto/{id}")
	public Response deleteDigitalizacaocomprendaaberto(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/cartaoassinatura")
	public Response createCartaoassinatura(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/cartaoassinatura/{id}")
	public Response updateCartaoassinatura(@HeaderParam("authCode") int authCode, @PathParam("id") Long id,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/cartaoassinatura")
	public Response readAllCartaoassinatura(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/cartaoassinatura/{id}")
	public Response readCartaoassinatura(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/cartaoassinatura/{id}")
	public Response deleteCartaoassinatura(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/cadastramentodesenhacliente")
	public Response createCadastramentodesenhacliente(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/cadastramentodesenhacliente/{id}")
	public Response updateCadastramentodesenhacliente(@HeaderParam("authCode") int authCode, @PathParam("id") Long id,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/cadastramentodesenhacliente")
	public Response readAllCadastramentodesenhacliente(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/cadastramentodesenhacliente/{id}")
	public Response readCadastramentodesenhacliente(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/cadastramentodesenhacliente/{id}")
	public Response deleteCadastramentodesenhacliente(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/declaracaodedados")
	public Response createDeclaracaodedados(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/declaracaodedados/{id}")
	public Response updateDeclaracaodedados(@HeaderParam("authCode") int authCode, @PathParam("id") Long id,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/declaracaodedados")
	public Response readAllDeclaracaodedados(@HeaderParam("authCode") int authCode) {
//	metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	@GET
	@Path("/relatorios")
	public Response readAllRelatorios(@HeaderParam("authCode") int authCode) {
//	metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllRelatorios();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/declaracaodedados/{id}")
	public Response readDeclaracaodedados(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/declaracaodedados/{id}")
	public Response deleteDeclaracaodedados(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/assinaturadogerente")
	public Response createAssinaturadogerente(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/assinaturadogerente/{id}")
	public Response updateAssinaturadogerente(@HeaderParam("authCode") int authCode, @PathParam("id") Long id,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/assinaturadogerente")
	public Response readAllAssinaturadogerente(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/assinaturadogerente/{id}")
	public Response readAssinaturadogerente(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/assinaturadogerente/{id}")
	public Response deleteAssinaturadogerente(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/autorizacaodocumental")
	public Response createAutorizacaodocumental(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/autorizacaodocumental/{id}")
	public Response updateAutorizacaodocumental(@HeaderParam("authCode") int authCode, @PathParam("id") Long id,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/autorizacaodocumental")
	public Response readAllAutorizacaodocumental(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/autorizacaodocumental/{id}")
	public Response readAutorizacaodocumental(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/autorizacaodocumental/{id}")
	public Response deleteAutorizacaodocumental(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/consultaavaliacesvalidas")
	public Response createConsultaavaliacesvalidas(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/consultaavaliacesvalidas/{id}")
	public Response updateConsultaavaliacesvalidas(@HeaderParam("authCode") int authCode, @PathParam("id") Long id,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/consultaavaliacesvalidas")
	public Response readAllConsultaavaliacesvalidas(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/consultaavaliacesvalidas/{id}")
	public Response readConsultaavaliacesvalidas(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/consultaavaliacesvalidas/{id}")
	public Response deleteConsultaavaliacesvalidas(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/avaliacaoderiscoclientecomercial")
	public Response createAvaliacaoderiscoclientecomercial(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/avaliacaoderiscoclientecomercial/{id}")
	public Response updateAvaliacaoderiscoclientecomercial(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id, AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/avaliacaoderiscoclientecomercial")
	public Response readAllAvaliacaoderiscoclientecomercial(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/avaliacaoderiscoclientecomercial/{id}")
	public Response readAvaliacaoderiscoclientecomercial(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/avaliacaoderiscoclientecomercial/{id}")
	public Response deleteAvaliacaoderiscoclientecomercial(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/consultaavaliacesvalidasaprovadas")
	public Response createConsultaavaliacesvalidasaprovadas(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/consultaavaliacesvalidasaprovadas/{id}")
	public Response updateConsultaavaliacesvalidasaprovadas(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id, AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/consultaavaliacesvalidasaprovadas")
	public Response readAllConsultaavaliacesvalidasaprovadas(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/consultaavaliacesvalidasaprovadas/{id}")
	public Response readConsultaavaliacesvalidasaprovadas(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/consultaavaliacesvalidasaprovadas/{id}")
	public Response deleteConsultaavaliacesvalidasaprovadas(@HeaderParam("authCode") int authCode,
			@PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/avaliacaoderiscooperacao")
	public Response createAvaliacaoderiscooperacao(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/avaliacaoderiscooperacao/{id}")
	public Response updateAvaliacaoderiscooperacao(@HeaderParam("authCode") int authCode, @PathParam("id") Long id,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/avaliacaoderiscooperacao")
	public Response readAllAvaliacaoderiscooperacao(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/avaliacaoderiscooperacao/{id}")
	public Response readAvaliacaoderiscooperacao(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/avaliacaoderiscooperacao/{id}")
	public Response deleteAvaliacaoderiscooperacao(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/aberturadeconta")
	public Response createAberturadeconta(@HeaderParam("authCode") int authCode, AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/aberturadeconta/{id}")
	public Response updateAberturadeconta(@HeaderParam("authCode") int authCode, @PathParam("id") Long id,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/aberturadeconta")
	public Response readAllAberturadeconta(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/aberturadeconta/{id}")
	public Response readAberturadeconta(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/aberturadeconta/{id}")
	public Response deleteAberturadeconta(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/assinaturadogerentefinal")
	public Response createAssinaturadogerentefinal(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/assinaturadogerentefinal/{id}")
	public Response updateAssinaturadogerentefinal(@HeaderParam("authCode") int authCode, @PathParam("id") Long id,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/assinaturadogerentefinal")
	public Response readAllAssinaturadogerentefinal(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/assinaturadogerentefinal/{id}")
	public Response readAssinaturadogerentefinal(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/assinaturadogerentefinal/{id}")
	public Response deleteAssinaturadogerentefinal(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param requisicao
	 * @return retorno
	 */
	@POST
	@Path("/aberturadecontaresultado")
	public Response createAberturadecontaresultado(@HeaderParam("authCode") int authCode,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_RESPONSE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.createAberturadeconta(requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	@PUT
	@Path("/aberturadecontaresultado/{id}")
	public Response updateAberturadecontaresultado(@HeaderParam("authCode") int authCode, @PathParam("id") Long id,
			AberturadecontaRequisicao requisicao) {
		metrics.track(DossieConstantes.METRICS_UPDATE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.updateAberturadeconta(id, requisicao);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @return retorno
	 */
	@GET
	@Path("/aberturadecontaresultado")
	public Response readAllAberturadecontaresultado(@HeaderParam("authCode") int authCode) {
		metrics.track(DossieConstantes.METRICS_READ_ALL, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAllAberturadeconta();
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@GET
	@Path("/aberturadecontaresultado/{id}")
	public Response readAberturadecontaresultado(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_READ, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.readAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}

	/**
	 * @param authCode
	 * @param id
	 * @return retorno
	 */
	@DELETE
	@Path("/aberturadecontaresultado/{id}")
	public Response deleteAberturadecontaresultado(@HeaderParam("authCode") int authCode, @PathParam("id") Long id) {
		metrics.track(DossieConstantes.METRICS_DELETE, AberturadecontaRequisicao.class.getSimpleName());
		if (authCode != auth.getCode()) {
			return semAutorizacao(authCode);
		}
		Status status = Status.OK;
		Retorno retorno = service.deleteAberturadeconta(id);
		if (retorno != null && retorno.isTemErro()) {
			status = Status.NOT_FOUND;
		}
		return build(status, retorno);
	}
}
