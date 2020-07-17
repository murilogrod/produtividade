package br.gov.caixa.dossie.rs.resource;

import java.util.HashMap;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * <p>
 * InformacaoJbossServico
 * </p>
 *
 * <p>
 * Descrição: Servico da arquitetura utilizado para fornecer informações sobre o
 * servidor jboss em que a aplicação está executando
 * </p>
 *
 * <br>
 * <b>Empresa:</b> Cef - Caixa Econômica Federal
 *
 *
 * @author ricardo.crispim
 *
 * @version 1.0
 */
@Path("/servidor")
public class InformacaoJbossServico {

    /**
     * <p>
     * Método responsável por fornecer o nome da instância que a aplicação está
     * executando
     * </p>
     * .
     *
     * @return
     *
     * @author ricardo.crispim
     */
    @GET
    @Path("/instancia")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNomeInstancia() {
        HashMap<String, String> retorno = new HashMap<>();
        retorno.put("nomeInstancia", System.getProperty("jboss.node.name"));
        return Response.ok(retorno).build();
    }
    
    /**
     * <p>
     * Método responsável por fornecer o nome da instância que a aplicação está
     * executando
     * </p>
     * .
     *
     * @return
     *
     * @author ricardo.crispim
     */
    @GET
    @Path("/propriedades/urlapi")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPropriedadesUrlApi() {
        HashMap<String, String> retorno = new HashMap<>();
        retorno.put("urlSimtrApi", System.getProperty("url.simtr.api"));
        retorno.put("urlSsoAuth", System.getProperty("url.sso.auth"));
        return Response.ok(retorno).build();
    }

}
