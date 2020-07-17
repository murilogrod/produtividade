package br.gov.caixa.simtr.controle.servico;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.core.Response;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.json.JSONArray;
import org.json.JSONObject;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.arquitetura.util.UtilRest;
import br.gov.caixa.simtr.controle.excecao.SimtrPermissaoException;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.util.KeycloakUtil;

@Stateless
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class AdministracaoServico {

    private static final Logger LOGGER = Logger.getAnonymousLogger();
    
    private static final String APPLICATION_JSON = "application/json";

    @Resource(lookup = "java:jboss/datasources/simtr-ds")
    private DataSource dataSource;
    
    @EJB
    private CanalServico canalServico;
    
    @Inject
    private KeycloakUtil keycloakUtil;

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public JSONArray executaConsultaAdministrativa(final String consulta, final Level logLevel) {
        try (
             Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(consulta)) {

            JSONArray jsonArray = new JSONArray();
            while (resultSet.next()) {
                int totalColumns = resultSet.getMetaData().getColumnCount();
                JSONObject obj = new JSONObject();
                for (int i = 1; i <= totalColumns; i++) {
                    obj.put(resultSet.getMetaData().getColumnLabel(i).toLowerCase(), resultSet.getObject(i));
                }
                jsonArray.put(obj);
            }
            return jsonArray;
        } catch (SQLException ex) {
            LOGGER.log(logLevel, ex.getLocalizedMessage(), ex);
            return this.convertToJSON(ex);
        }
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public JSONArray executaComandoAdministrativo(final String consulta, final Level logLevel) {
        try (
             Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            Integer total = statement.executeUpdate(consulta);
            JSONArray jsonArray = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("registros_afetados", total);
            jsonArray.put(obj);
            return jsonArray;
        } catch (SQLException ex) {
            LOGGER.log(logLevel, ex.getLocalizedMessage(), ex);
            return this.convertToJSON(ex);
        }
    }

    private JSONArray convertToJSON(Exception ex) {
        JSONArray jsonArray = new JSONArray();
        JSONObject obj = new JSONObject();
        obj.put("excecao", ex.getLocalizedMessage());
        String pilha = "";
        StackTraceElement[] stackTrace = ex.getStackTrace();
        for (StackTraceElement elelement : stackTrace) {
            pilha = pilha.concat(elelement.toString()).concat("\n");
        }

        obj.put("pilha", pilha);
        jsonArray.put(obj);
        return jsonArray;
    }
    
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    /**
     * Metodo generico para consumo de serviço no API manager. Valida o canal para verificar se existe a permissão para consumir os serviços do ApiManager.
     *  
     *  
     * @param verbo Verbo do metodo a ser consumido
     * @param endpoint Endereço do endpoint a ser consumido no API Manager.
     * @param corpo Corpo a ser enviado para o serviço que será consumido.
     * @param cabecalhos Cabecalhos a ser enviado para o serviço que será consumido.
     * @return Retorno é o retorno do serviço consumido pelo endpoint.
     */
    public Response consumirServicoApiManager(String verbo, String endpoint, String corpo, Map<String, String> cabecalhos) {
        validarCanalComunicacao();
        if(Objects.isNull(cabecalhos)) {
        	cabecalhos = new HashMap<>();
        }
        //Obtendo a APIKEY e adicionando aos cabeçalhos
        String apikey = System.getProperty("simtr_apikey");
        cabecalhos.put("apikey", apikey);

        String urlBaseApiManager = System.getProperty("url.servico.apimanager");
        StringBuilder urlApiManager = new StringBuilder(urlBaseApiManager);
        urlApiManager.append(endpoint);
        
        return UtilRest.consumirServicoOAuth2JSON(urlApiManager.toString(), EnumMetodoHTTP.valueOf(verbo), cabecalhos, new HashMap<>(), corpo, this.keycloakUtil.getTokenServico());
    }

    /**
     * 
     * Verifica se o Canal de comunicação do usuário logado no SSO permite consumir serviços da API Manager através do SIMTR.
     * Caso o canal não seja encontrado retorna exception com mensagem. 
     * Caso o canal seja encontrado e não contenha autorização de consumo junto ao API Manager, é lançado uma exception.
     * 
     */
     private void validarCanalComunicacao() {
        Canal canal = this.canalServico.getByClienteSSO();
        if(Objects.isNull(canal)) {
            throw new SimtrPermissaoException("AS.cSAM.001 - Canal de comunicação não cadastrado para o ClientID do SSO utilizado.");
        }
        
        if (!canal.getIndicadorOutorgaApimanager()) {
            throw new SimtrPermissaoException("AS.cSAM.002 - Canal de comunicação não autorizado a consumir serviços do API Manager através da API do SIMTR.");
        }
    }
}
