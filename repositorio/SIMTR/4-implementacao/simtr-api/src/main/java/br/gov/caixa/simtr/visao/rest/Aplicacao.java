package br.gov.caixa.simtr.visao.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import br.gov.caixa.pedesgo.arquitetura.excecao.AplicacaoRESTExceptionMapper;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.AutenticacaoServicoSSO;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.InformacaoJbossServico;
import br.gov.caixa.pedesgo.keycloak.rest.v1.KeycloakRest;
import io.swagger.annotations.Api;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

/**
 * <p>
 * AppREST
 * </p>
 *
 * <p>
 * Descrição: Classe que configura a raiz do caminho dos servicos REST
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
@Api
@ApplicationPath("/rest")
public class Aplicacao extends Application {

    private String apiVersion = "N/A";
    private String protocolo = "http";
    private String host = "url.simtr.api";
    private final Properties properties = new Properties();

    public Aplicacao() {

        try {
            String url = System.getProperty("url.simtr.api");
            if (Objects.nonNull(url)) {
                String[] elementos = url.split("://");
                protocolo = elementos[0];
                host = elementos[1];
            }

            InputStream is = Aplicacao.class.getResourceAsStream("/version.properties");
            properties.load(is);
            apiVersion = properties.getProperty("version");
        } catch (IOException ex) {
            Logger.getLogger(Aplicacao.class.getName()).log(Level.SEVERE, null, ex);
        }

        BeanConfig config = new BeanConfig();
        config.setTitle("API do sistema SIMTR");
        config.setDescription("Lista de APIs do SIMTR");
        config.setVersion(apiVersion);
        config.setHost(host);
        config.setBasePath("/simtr-api/rest");
        config.setSchemes(new String[]{protocolo});
        config.setResourcePackage("br.gov.caixa.simtr");
        config.setScan(true);
    }

    @Override
    public Set<Class<?>> getClasses() {
        
        /**
         * ATENÇÃO:
         * 
         * VISANDO FACILITAR A MANUTENÇÃO DO ARQUIVO, 
         * MANTER DECLARAÇÕES EM ORDEM ALFABETICA POR CADA NIVEL DE PACOTE
         */
        
        Set<Class<?>> resources = new HashSet<>();
        resources.add(AplicacaoRESTExceptionMapper.class);
        resources.add(ApiListingResource.class);
        resources.add(SwaggerSerializers.class);
        resources.add(InformacaoJbossServico.class);
        resources.add(AutenticacaoServicoSSO.class);
        resources.add(KeycloakRest.class);
        resources.add(br.gov.caixa.simtr.visao.administracao.rest.v1.AdministracaoDossieProdutoREST.class);
        resources.add(br.gov.caixa.simtr.visao.administracao.rest.v1.AdministracaoREST.class);
        resources.add(br.gov.caixa.simtr.visao.apimanager.rest.v1.ApiManagerREST.class);
        resources.add(br.gov.caixa.simtr.visao.cadastro.rest.v1.CanalREST.class);
        resources.add(br.gov.caixa.simtr.visao.cadastro.rest.v1.CheckListREST.class);
        resources.add(br.gov.caixa.simtr.visao.cadastro.rest.v1.ComposicaoDocumentalREST.class);
        resources.add(br.gov.caixa.simtr.visao.cadastro.rest.v1.FuncaoDocumentalREST.class);
        resources.add(br.gov.caixa.simtr.visao.cadastro.rest.v1.ProcessoREST.class);
        resources.add(br.gov.caixa.simtr.visao.cadastro.rest.v1.ProdutoREST.class);
        resources.add(br.gov.caixa.simtr.visao.cadastro.rest.v1.TipoDocumentoREST.class);
        resources.add(br.gov.caixa.simtr.visao.cadastro.rest.v1.TipoRelacionamentoREST.class);
        resources.add(br.gov.caixa.simtr.visao.cadastro.rest.v1.VinculacaoCheckListREST.class);
        resources.add(br.gov.caixa.simtr.visao.dossiedigital.rest.v1.AutorizacaoREST.class);
        resources.add(br.gov.caixa.simtr.visao.dossiedigital.rest.v1.DossieClienteREST.class);
        resources.add(br.gov.caixa.simtr.visao.dossiedigital.rest.v1.ManutencaoDossieDigitalREST.class);
        resources.add(br.gov.caixa.simtr.visao.dossiedigital.rest.v2.AutorizacaoREST.class);
        resources.add(br.gov.caixa.simtr.visao.dossiedigital.rest.v2.DossieClienteREST.class);
        resources.add(br.gov.caixa.simtr.visao.dossiedigital.rest.v2.ManutencaoDossieDigitalREST.class);
        resources.add(br.gov.caixa.simtr.visao.negocio.rest.v1.DashboardREST.class);
        resources.add(br.gov.caixa.simtr.visao.negocio.rest.v1.DocumentoREST.class);
        resources.add(br.gov.caixa.simtr.visao.negocio.rest.v1.DossieClienteREST.class);
        resources.add(br.gov.caixa.simtr.visao.negocio.rest.v1.DossieProdutoREST.class);
        resources.add(br.gov.caixa.simtr.visao.negocio.rest.v1.ProcessoREST.class);
        resources.add(br.gov.caixa.simtr.visao.negocio.rest.v1.TratamentoREST.class);
        resources.add(br.gov.caixa.simtr.visao.pae.rest.v1.ApensoAdministrativoREST.class);
        resources.add(br.gov.caixa.simtr.visao.pae.rest.v1.ContratoAdministrativoREST.class);
        resources.add(br.gov.caixa.simtr.visao.pae.rest.v1.DocumentoAdministrativoREST.class);
        resources.add(br.gov.caixa.simtr.visao.pae.rest.v1.ProcessoAdministrativoREST.class);
        resources.add(br.gov.caixa.simtr.visao.portalempreendedor.rest.v1.PortalEmpreendedorREST.class);
        resources.add(br.gov.caixa.simtr.visao.retaguarda.rest.v1.ExtracaoDadosREST.class);
        resources.add(br.gov.caixa.simtr.visao.retaguarda.rest.v1.OutsourcingREST.class);
        resources.add(br.gov.caixa.pedesgo.arquitetura.rest.UsuarioServicoRest.class);
        resources.add(br.gov.caixa.simtr.visao.cadastro.rest.v1.FormularioRest.class);
        resources.add(br.gov.caixa.simtr.visao.negocio.rest.v1.ApoioNegocioSiricRest.class);
        resources.add(br.gov.caixa.simtr.visao.negocio.rest.v2.DossieClienteREST.class);
        resources.add(br.gov.caixa.simtr.visao.negocio.rest.v2.DossieProdutoREST.class);
        return resources;
    }

}
