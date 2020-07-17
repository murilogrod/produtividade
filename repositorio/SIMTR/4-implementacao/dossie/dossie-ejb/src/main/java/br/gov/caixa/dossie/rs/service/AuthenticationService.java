
package br.gov.caixa.dossie.rs.service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.json.JSONObject;

import br.gov.caixa.dossie.exception.GeneralException;
import br.gov.caixa.dossie.rs.requisicao.AuthenticationRequisicao;
import br.gov.caixa.dossie.rs.retorno.AuthenticationRetorno;
import br.gov.caixa.dossie.util.DossieConstantes;

@Stateless
@LocalBean
@Named
public class AuthenticationService {

	private static final Logger LOGGER = Logger.getLogger(AuthenticationService.class);

	private ClientRequestFactory getSICPUService() {
		return new ClientRequestFactory(UriBuilder.fromUri("".concat(DossieConstantes.URL_SISIT).trim()).build());
	}

	public AuthenticationRetorno autenticar(final AuthenticationRequisicao requisicao) {

		final AuthenticationRetorno retorno = new AuthenticationRetorno();
		try {
			final JSONObject paramtrosIn = new JSONObject();
			paramtrosIn.put("canal", "SIOGP");
			paramtrosIn.put("navegador", requisicao.getNavegador());
			paramtrosIn.put("email", requisicao.getUsuario());
			paramtrosIn.put("password", requisicao.getSenha());
			paramtrosIn.put("ip", "127.0.0.1");

			ClientResponse<String> response = this.getSICPUService()
					.createRelativeRequest("/ws/auth/token/create/prototipo/01.00")
					.accept(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.body(MediaType.APPLICATION_JSON, paramtrosIn.toString()).post(String.class);
			if (response.getStatus() == Status.OK.getStatusCode()) {
				JSONObject objRetorno = new JSONObject(String.valueOf(response.getEntity()));
				if (objRetorno != null && objRetorno.get("temErro").equals(Boolean.FALSE)) {
					retorno.setTemErro(false);
					retorno.setCredencial(objRetorno.getString(DossieConstantes.CREDENCIAL));
					retorno.setTokenAcesso(objRetorno.getString(DossieConstantes.TOKENACESSO));
					retorno.setTokenRenovacao(objRetorno.getString(DossieConstantes.TOKENRENOVACAO));
					retorno.setTempoExpiracaoAcesso(objRetorno.getLong(DossieConstantes.TEMPOEXPIRACAOACESSO));
					retorno.setTempoExpiracaoRenovacao(objRetorno.getLong(DossieConstantes.TEMPOEXPIRACAORENOVACAO));

				} else if (objRetorno != null && objRetorno.get("temErro").equals(Boolean.TRUE)) {

					/*
					 * LOGGER.info("DOSSIE - falha na autenticação de usuario : ");
					 * LOGGER.info("DOSSIE - Tem Error " + objRetorno.get("temErro"));
					 * LOGGER.info("DOSSIE - Msg Error " +
					 * objRetorno.getJSONArray("msgsErro").getString(0));
					 * 
					 * retorno.setTemErro(true);
					 * retorno.setMsgsErro(Arrays.asList(objRetorno.getJSONArray("msgsErro").
					 * getString(0)));
					 */

					// nao validando sisit - descomentar acima, retirar abaixo
					retorno.setTemErro(false);
					retorno.setCredencial(
							"Zytwcmh1YldlbmI3OVEvY2NLWVRrZjNBbWM3a0xmbmZ0V1FUT2QzNmYxTVFJYkhrTk9mWjFnYlNpTjBNaHpRWUowWUR2cVExTUkwMg0KZzlwNGZJSTdLZz09DQo");
					retorno.setTokenAcesso(
							"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1MDg0NDc5ODgsInN1YiI6IjE1MDg0NDc2Mjc5NTdTSU9HUFVtWkZjemwzU3pkUlZTOU9ZM2htT0dGSlJuTjVXbVl3V25ocllYWmpVV0p3TTNsM1JGaHZRakZxV1hkMVQyNVFZWEkxYzIxQ1Vtd3lXbEJ6UWxGYVRGTm1UMGhwT1RsdmNrUjBUZzBLYURseGVYTlJSRWhKZHowOURRbyIsIm5iZiI6MTUwODQ0NzYyMiwiaXNzIjoiQ0FJWEEiLCJqdGkiOiJhMDExZGMwNy05NjNkLTRlMDItOWM2YS1iNjRkY2U4ODJlOTgiLCJpYXQiOjE1MDg0NDc2Mjd9.paVfGi2lzA9vLqttVcDWdr0ztbmZwRRvjkqv3tGHaug");
					retorno.setTokenRenovacao(
							"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1MDg4MDc2MjcsInN1YiI6IjE1MDg0NDc2Mjc5ODdTSU9HUFNJT0dQVW1aRmN6bDNTemRSVlM5T1kzaG1PR0ZKUm5ONVdtWXdXbmhyWVhaalVXSndNM2wzUkZodlFqRnFXWGQxVDI1UVlYSTFjMjFDVW13eVdsQnpRbEZhVEZObVQwaHBPVGx2Y2tSMFRnMEthRGx4ZVhOUlJFaEpkejA5RFFvIiwibmJmIjoxNTA4NDQ3NjIyLCJpc3MiOiJDQUlYQSIsImp0aSI6ImMwODNlMThjLTEzZjgtNGI2My1hOWIyLWVkYzZiYTc4NmU0MiIsImlhdCI6MTUwODQ0NzYyN30.XBAZo8El_xOwgGeKqD5MmQIH9Cfok_Iw0NIBrEjN6RI");
					retorno.setTempoExpiracaoAcesso((long) 961);
					retorno.setTempoExpiracaoRenovacao((long) 960000);
				}
			} else {
				/*
				 * LOGGER.info("DOSSIE - falha na autenticação de usuario: ");
				 * LOGGER.info("DOSSIE - Codigo status: "+ response.getStatus());
				 * LOGGER.info("DOSSIE - Descrição status: "+
				 * response.getResponseStatus().getReasonPhrase());
				 * 
				 * throw new Exception("Erro tentativa de Autenticação: "+ response.getStatus()+
				 * response.getResponseStatus().getReasonPhrase());
				 */

				// nao validando sisit - descomentar acima, retirar abaixo
				retorno.setTemErro(false);
				retorno.setCredencial(
						"Zytwcmh1YldlbmI3OVEvY2NLWVRrZjNBbWM3a0xmbmZ0V1FUT2QzNmYxTVFJYkhrTk9mWjFnYlNpTjBNaHpRWUowWUR2cVExTUkwMg0KZzlwNGZJSTdLZz09DQo");
				retorno.setTokenAcesso(
						"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1MDg0NDc5ODgsInN1YiI6IjE1MDg0NDc2Mjc5NTdTSU9HUFVtWkZjemwzU3pkUlZTOU9ZM2htT0dGSlJuTjVXbVl3V25ocllYWmpVV0p3TTNsM1JGaHZRakZxV1hkMVQyNVFZWEkxYzIxQ1Vtd3lXbEJ6UWxGYVRGTm1UMGhwT1RsdmNrUjBUZzBLYURseGVYTlJSRWhKZHowOURRbyIsIm5iZiI6MTUwODQ0NzYyMiwiaXNzIjoiQ0FJWEEiLCJqdGkiOiJhMDExZGMwNy05NjNkLTRlMDItOWM2YS1iNjRkY2U4ODJlOTgiLCJpYXQiOjE1MDg0NDc2Mjd9.paVfGi2lzA9vLqttVcDWdr0ztbmZwRRvjkqv3tGHaug");
				retorno.setTokenRenovacao(
						"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1MDg4MDc2MjcsInN1YiI6IjE1MDg0NDc2Mjc5ODdTSU9HUFNJT0dQVW1aRmN6bDNTemRSVlM5T1kzaG1PR0ZKUm5ONVdtWXdXbmhyWVhaalVXSndNM2wzUkZodlFqRnFXWGQxVDI1UVlYSTFjMjFDVW13eVdsQnpRbEZhVEZObVQwaHBPVGx2Y2tSMFRnMEthRGx4ZVhOUlJFaEpkejA5RFFvIiwibmJmIjoxNTA4NDQ3NjIyLCJpc3MiOiJDQUlYQSIsImp0aSI6ImMwODNlMThjLTEzZjgtNGI2My1hOWIyLWVkYzZiYTc4NmU0MiIsImlhdCI6MTUwODQ0NzYyN30.XBAZo8El_xOwgGeKqD5MmQIH9Cfok_Iw0NIBrEjN6RI");
				retorno.setTempoExpiracaoAcesso((long) 961);
				retorno.setTempoExpiracaoRenovacao((long) 960000);
			}
		} catch (Exception e) {

			/*
			 * LOGGER.info("DOSSIE - falha na autenticação de usuario: ", e);
			 * 
			 * retorno.setTemErro(true); retorno.setMsgsErro(new ArrayList<String>());
			 * retorno.getMsgsErro().
			 * add("DOSSIE - falha na autenticação de usuário. - ERROR: "+e.getMessage());
			 */

			// nao validando sisit - descomentar acima, retirar abaixo
			retorno.setTemErro(false);
			retorno.setCredencial(
					"Zytwcmh1YldlbmI3OVEvY2NLWVRrZjNBbWM3a0xmbmZ0V1FUT2QzNmYxTVFJYkhrTk9mWjFnYlNpTjBNaHpRWUowWUR2cVExTUkwMg0KZzlwNGZJSTdLZz09DQo");
			retorno.setTokenAcesso(
					"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1MDg0NDc5ODgsInN1YiI6IjE1MDg0NDc2Mjc5NTdTSU9HUFVtWkZjemwzU3pkUlZTOU9ZM2htT0dGSlJuTjVXbVl3V25ocllYWmpVV0p3TTNsM1JGaHZRakZxV1hkMVQyNVFZWEkxYzIxQ1Vtd3lXbEJ6UWxGYVRGTm1UMGhwT1RsdmNrUjBUZzBLYURseGVYTlJSRWhKZHowOURRbyIsIm5iZiI6MTUwODQ0NzYyMiwiaXNzIjoiQ0FJWEEiLCJqdGkiOiJhMDExZGMwNy05NjNkLTRlMDItOWM2YS1iNjRkY2U4ODJlOTgiLCJpYXQiOjE1MDg0NDc2Mjd9.paVfGi2lzA9vLqttVcDWdr0ztbmZwRRvjkqv3tGHaug");
			retorno.setTokenRenovacao(
					"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1MDg4MDc2MjcsInN1YiI6IjE1MDg0NDc2Mjc5ODdTSU9HUFNJT0dQVW1aRmN6bDNTemRSVlM5T1kzaG1PR0ZKUm5ONVdtWXdXbmhyWVhaalVXSndNM2wzUkZodlFqRnFXWGQxVDI1UVlYSTFjMjFDVW13eVdsQnpRbEZhVEZObVQwaHBPVGx2Y2tSMFRnMEthRGx4ZVhOUlJFaEpkejA5RFFvIiwibmJmIjoxNTA4NDQ3NjIyLCJpc3MiOiJDQUlYQSIsImp0aSI6ImMwODNlMThjLTEzZjgtNGI2My1hOWIyLWVkYzZiYTc4NmU0MiIsImlhdCI6MTUwODQ0NzYyN30.XBAZo8El_xOwgGeKqD5MmQIH9Cfok_Iw0NIBrEjN6RI");
			retorno.setTempoExpiracaoAcesso((long) 961);
			retorno.setTempoExpiracaoRenovacao((long) 960000);
		}

		return retorno;
	}

	public void validarTokenAcesso(final String credencial, final String tokenAcesso, final String navegador)
			throws GeneralException {
		try {

			LOGGER.info("DOSSIE - Inicio validação usuário atenticado");
			final JSONObject paramtrosIn = new JSONObject();
			paramtrosIn.put("canal", "DOSSIE");
			paramtrosIn.put(DossieConstantes.TOKENACESSO, tokenAcesso);
			paramtrosIn.put(DossieConstantes.CREDENCIAL, credencial);
			paramtrosIn.put(DossieConstantes.NAVEGADOR, navegador);

			ClientResponse<String> response = this.getSICPUService()
					.createRelativeRequest("/ws/auth/token/validate/prototipo/01.00")
					.accept(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.body(MediaType.APPLICATION_JSON, paramtrosIn.toString()).post(String.class);

			if (response.getStatus() == Status.OK.getStatusCode()) {

				JSONObject objRetorno = new JSONObject(response.getEntity());

				if (objRetorno != null && objRetorno.has("temErro") && objRetorno.get("temErro").equals(Boolean.TRUE)) {

					/*
					 * LOGGER.info(GeneralException.RESPONSE_CODE_ERROR_SISIT_FAIL.getMessage());
					 * LOGGER.info(objRetorno.getJSONArray("msgsErro").getString(0)); throw new
					 * GeneralException(GeneralException.RESPONSE_CODE_ERROR_SISIT_FAIL);
					 */
				}

			} else {
				/*
				 * LOGGER.
				 * info("DOSSIE - falha na validação de token de  acesso do usuario autenticado: "
				 * ); LOGGER.info("DOSSIE - Codigo status: "+ response.getStatus());
				 * LOGGER.info("DOSSIE - Descrição status: "+
				 * response.getResponseStatus().getReasonPhrase()); throw new
				 * GeneralException(GeneralException.RESPONSE_CODE_ERROR_SISIT_FAIL,
				 * "Falha na renovação de token de  acesso do usuario autenticado");
				 */
			}

		} catch (Exception e) {

			// throw new
			// GeneralException(GeneralException.RESPONSE_CODE_ERROR_SISIT_FAIL,e);
		}

	}

	public AuthenticationRetorno renovarTokenAcesso(String credencial, String tokenRenovacao, String navegador)
			throws GeneralException {

		AuthenticationRetorno retorno = new AuthenticationRetorno();
		try {

			LOGGER.info("DOSSIE - Inicio renovação  token Acesso usuário autenticado.");
			final JSONObject paramtrosIn = new JSONObject();
			paramtrosIn.put(DossieConstantes.TOKENRENOVACAO, tokenRenovacao);
			paramtrosIn.put(DossieConstantes.CREDENCIAL, credencial);
			paramtrosIn.put(DossieConstantes.NAVEGADOR, navegador);

			ClientResponse<String> response = this.getSICPUService()
					.createRelativeRequest("/ws/auth/token/update/prototipo/01.00")
					.accept(MediaType.APPLICATION_JSON + ";charset=UTF-8")
					.body(MediaType.APPLICATION_JSON, paramtrosIn.toString()).post(String.class);

			final JSONObject objRetorno = new JSONObject(String.valueOf(response.getEntity()));

			if (response.getStatus() == Status.OK.getStatusCode() && objRetorno != null && objRetorno.has("temErro")) {

				if (objRetorno.get("temErro").equals(Boolean.FALSE)) {
					retorno.setTemErro(false);
					retorno.setCredencial(objRetorno.getString(DossieConstantes.CREDENCIAL));
					retorno.setTokenAcesso(objRetorno.getString(DossieConstantes.TOKENACESSO));
					retorno.setTokenRenovacao(objRetorno.getString(DossieConstantes.TOKENRENOVACAO));
					retorno.setTempoExpiracaoAcesso(objRetorno.getLong(DossieConstantes.TEMPOEXPIRACAOACESSO));
					retorno.setTempoExpiracaoRenovacao(objRetorno.getLong(DossieConstantes.TEMPOEXPIRACAORENOVACAO));

				} else if (objRetorno.get("temErro").equals(Boolean.TRUE)) {

					LOGGER.info("DOSSIE - Erro renovação token acesso: "
							+ objRetorno.getJSONArray("msgsErro").getString(0));
					throw new GeneralException(GeneralException.RESPONSE_CODE_ERROR_SISIT_FAIL,
							objRetorno.getJSONArray("msgsErro").getString(0));
				}
			} else {

				LOGGER.info("DOSSIE - falha na renovação de token de  acesso do usuario autenticado: ");
				LOGGER.info("DOSSIE - Codigo status: " + response.getStatus());
				LOGGER.info("DOSSIE - Descrição status: " + response.getResponseStatus().getReasonPhrase());
				throw new GeneralException(GeneralException.RESPONSE_CODE_ERROR_SISIT_FAIL,
						"Falha na renovação de token de  acesso do usuario autenticado");
			}

		} catch (Exception e) {

			LOGGER.info("DOSSIE - Erro renovação token acesso: ", e);
			throw new GeneralException(GeneralException.RESPONSE_CODE_ERROR_SISIT_FAIL, e);
		}

		return retorno;
	}

}
