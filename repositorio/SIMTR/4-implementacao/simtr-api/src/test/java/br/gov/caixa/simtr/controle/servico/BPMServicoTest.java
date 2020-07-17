package br.gov.caixa.simtr.controle.servico;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.arquitetura.util.UtilParametro;
import br.gov.caixa.pedesgo.arquitetura.util.UtilRest;
import br.gov.caixa.simtr.controle.excecao.BpmException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDossie;
import br.gov.caixa.simtr.modelo.entidade.TipoSituacaoDossie;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDossieEnum;
import br.gov.caixa.simtr.util.KeyStoreUtil;
import br.gov.caixa.simtr.util.KeycloakUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UtilParametro.class, UtilRest.class, KeyStoreUtil.class})
public class BPMServicoTest {

    @Mock
    private DossieProduto dossieProduto;

    @Mock
    private DossieProdutoServico dossieProdutoServico;
    
    @Mock
    private KeycloakUtil keycloakUtil;

    @InjectMocks
    private BPMServico bpmServico;

    @Mock
    private SituacaoDossieServico situacaoDossieServico;

    private final String PROPRIEDADE_URL_API = "url.simtr.api";
    private final String PROPRIEDADE_URL_SSO = "url_sso_intranet";
    private final String PROPRIEDADE_URL_BPM = "url.servico.bpm";
    private final String PROPRIEDADE_USUARIO_BPM = "usuario.nome.servico.bpm";
    private final String PROPRIEDADE_SENHA_BPM = "usuario.senha.servico.bpm";
    private static final String PROPRIEDADE_KEYSTORE_URL = "simtr.keystore.url";
    private static final String PROPRIEDADE_KEYSTORE_SECRET = "simtr.keystore.secret";
    private static final String PROPRIEDADE_KEYSTORE_TYPE = "simtr.keystore.type";
    private static final String PROPRIEDADE_KEYSTORE_BPM_ALIAS = "simtr.keystore.bpm.alias";
    private static final String PROPRIEDADE_KEYSTORE_BPM_SECRET = "simtr.keystore.bpm.secret";

    private TipoSituacaoDossie tipoSituacaoAguardandoAlimentacao;
    private TipoSituacaoDossie tipoSituacaoAguardandoTratamento;
    private TipoSituacaoDossie tipoSituacaoAlimentacaoFinalizada;
    private TipoSituacaoDossie tipoSituacaoAnaliseSeguranca;
    private TipoSituacaoDossie tipoSituacaoCancelado;
    private TipoSituacaoDossie tipoSituacaoConforme;
    private TipoSituacaoDossie tipoSituacaoCriado;
    private TipoSituacaoDossie tipoSituacaoEmAlimentacao;
    private TipoSituacaoDossie tipoSituacaoEmTratamento;
    private TipoSituacaoDossie tipoSituacaoFinalizado;
    private TipoSituacaoDossie tipoSituacaoFinalizadoConforme;
    private TipoSituacaoDossie tipoSituacaoFinalizadoInconforme;
    private TipoSituacaoDossie tipoSituacaoPendenteInformacao;
    private TipoSituacaoDossie tipoSituacaoPendenteSeguranca;
    private TipoSituacaoDossie tipoSituacaoRascunho;
    private TipoSituacaoDossie tipoSituacaoSegurancaFinalizado;

    private String token;

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_DIR = "/mock/bpm-servico/";
    private static final String UTF_8 = "UTF-8";

    @Before
    public void setup() throws IOException, NoSuchMethodException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(UtilParametro.class, UtilRest.class, KeyStoreUtil.class);

        PowerMockito.when(UtilParametro.getParametro(PROPRIEDADE_URL_API, true)).thenReturn("url-api");
        PowerMockito.when(UtilParametro.getParametro(PROPRIEDADE_URL_SSO, true)).thenReturn("url-sso");
        PowerMockito.when(UtilParametro.getParametro(PROPRIEDADE_URL_BPM, true)).thenReturn("url-jbpm");
        PowerMockito.when(UtilParametro.getParametro(PROPRIEDADE_USUARIO_BPM, false)).thenReturn("usuario-jbpm");
        PowerMockito.when(UtilParametro.getParametro(PROPRIEDADE_SENHA_BPM, false)).thenReturn("senha-jbpm");

        this.dossieProduto = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("dossie-produto-base.json")), UTF_8), DossieProduto.class);
        this.token = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("token")), UTF_8);

        this.initTipoSitacao();

        Mockito.when(this.keycloakUtil.getTokenServico()).thenReturn(token);

        Method metodoCarregarDadosAcesso = this.bpmServico.getClass().getDeclaredMethod("carregarDadosAcesso");
        metodoCarregarDadosAcesso.setAccessible(Boolean.TRUE);

        metodoCarregarDadosAcesso.invoke(this.bpmServico);
    }

    @Test
    public void testCarregaDadosAcessoPropriedade() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        PowerMockito.when(UtilParametro.getParametro(PROPRIEDADE_USUARIO_BPM, false)).thenReturn("usuario-jbpm");
        PowerMockito.when(UtilParametro.getParametro(PROPRIEDADE_SENHA_BPM, false)).thenReturn("senha-jbpm");

        Method metodoCarregarDadosAcesso = this.bpmServico.getClass().getDeclaredMethod("carregarDadosAcesso");
        metodoCarregarDadosAcesso.setAccessible(Boolean.TRUE);

        metodoCarregarDadosAcesso.invoke(this.bpmServico);
    }

    @Test
    public void testCarregaDadosAcessoKeystore() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String arquivoKeystore = BPMServicoTest.class.getResource("/keystore.jceks").getFile();

        PowerMockito.when(UtilParametro.getParametro(Mockito.eq(PROPRIEDADE_URL_BPM), Mockito.anyBoolean())).thenReturn("url-jbpm");
        PowerMockito.when(UtilParametro.getParametro(Mockito.eq(PROPRIEDADE_USUARIO_BPM), Mockito.anyBoolean())).thenReturn("usuario-jbpm");
        PowerMockito.when(UtilParametro.getParametro(Mockito.eq(PROPRIEDADE_KEYSTORE_URL), Mockito.anyBoolean())).thenReturn(arquivoKeystore);
        PowerMockito.when(UtilParametro.getParametro(Mockito.eq(PROPRIEDADE_KEYSTORE_TYPE), Mockito.anyBoolean())).thenReturn("JCEKS");
        PowerMockito.when(UtilParametro.getParametro(Mockito.eq(PROPRIEDADE_KEYSTORE_SECRET), Mockito.anyBoolean())).thenReturn("serverpwd");
        PowerMockito.when(UtilParametro.getParametro(Mockito.eq(PROPRIEDADE_KEYSTORE_BPM_ALIAS), Mockito.anyBoolean())).thenReturn("bpm-usr-alias");
        PowerMockito.when(UtilParametro.getParametro(Mockito.eq(PROPRIEDADE_KEYSTORE_BPM_SECRET), Mockito.anyBoolean())).thenReturn("bpm-usr-secret");

        PowerMockito.when(KeyStoreUtil.getDataFromKeyStore(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("alias-secret");

        Method metodoCarregarDadosAcesso = this.bpmServico.getClass().getDeclaredMethod("carregarDadosAcesso");
        metodoCarregarDadosAcesso.setAccessible(Boolean.TRUE);

        metodoCarregarDadosAcesso.invoke(this.bpmServico);

    }

    /**
     * <p>
     * Executa a chamada de criação do dossiê nas diversas possibilidade em que
     * o espera-se que o valor retornado seja null
     * </p>. Resultado Esperado: Não realizar nada e retornar null na criação do
     * processo
     *
     * @author c090347
     *
     */
    @Test
    public void testCriaProcessoBPMCaso1() {
        this.dossieProduto.setId(1L);
        Set<SituacaoDossie> situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.RASCUNHO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        Long id = bpmServico.criaProcessoBPM(this.dossieProduto);
        Assert.assertNull(id);
    }

    /**
     * <p>
     * Realiza o teste do metodo criaProcessoBPM passando como paramatro um
     * dossiê de produto contendo um processo com definição incompleta dos
     * parametros de comunicação com o BPM.
     * </p> Resultado Esperado: Não realizar nada e retornar null na criação do
     * processo
     *
     * @author c090347
     *
     */
    @Test
    public void testCriaProcessoBPMCaso2() {
        this.dossieProduto.setNomeContainerBPM(null);
        Set<SituacaoDossie> situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.CRIADO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        Long id = this.bpmServico.criaProcessoBPM(dossieProduto);
        Assert.assertNull(id);
    }

    /**
     * <p>
     * Realiza o teste do metodo criaProcessoBPM passando como paramatro um
     * dossiê de produto sem a definição do processo.
     * </p> Resultado Esperado: Não realizar nada e retornar null na criação do
     * processo
     *
     * @author c090347
     *
     */
    @Test
    public void testCriaProcessoBPMCaso3() {
        this.dossieProduto.setNomeProcessoBPM(null);
        Set<SituacaoDossie> situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.CRIADO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        Long id = this.bpmServico.criaProcessoBPM(dossieProduto);
        Assert.assertNull(id);
    }

    /**
     * <p>
     * Realiza o teste do metodo criaProcessoBPM forçando o retorno do ID do
     * processo no BPM em formato alfanumerico .
     * </p>. Resultado Esperado: Lançamento de Exceção do tipo
     * BPMException.class
     *
     * @author c090347
     *
     */
    @Test(expected = BpmException.class)
    public void testCriaProcessoBPMCaso4() {
        this.dossieProduto.setId(1L);
        Set<SituacaoDossie> situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.CRIADO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        Response response = Response.ok("ABC").build();
        Response responseNotFound = Response.status(Response.Status.NOT_FOUND).build();
        PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(anyString(), eq(EnumMetodoHTTP.POST), anyMap(), anyMap(), anyString(), anyString())).thenReturn(response);
        PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(anyString(), eq(EnumMetodoHTTP.GET), anyMap(), anyMap(), anyString(), anyString())).thenReturn(responseNotFound);
        this.bpmServico.criaProcessoBPM(dossieProduto);
    }

    /**
     * <p>
     * Realiza o teste do metodo criaProcessoBPM enviando um dossiê sem o seu ID
     * definido.
     * </p>. Resultado Esperado: Lançamento de Exceção do tipo
     * SimtrRequisicaoException.class
     *
     * @author c090347
     *
     */
    @Test(expected = SimtrRequisicaoException.class)
    public void testCriaProcessoBPMCaso5() {
        this.dossieProduto.setId(null);
        Set<SituacaoDossie> situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.CRIADO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        Response response = Response.ok("ABC").build();
        PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(anyString(), eq(EnumMetodoHTTP.POST), anyMap(), anyMap(), anyString(), anyString())).thenReturn(response);
        this.bpmServico.criaProcessoBPM(dossieProduto);
    }

    /**
     * <p>
     * Realiza o teste do metodo criaProcessoBPM enviando um dossiê sem
     * problemas apresentados.
     * </p>. Resultado Esperado: Execução com sucesso.
     *
     * @author c090347
     *
     */
    @Test
    public void testCriaProcessoBPMCaso6() {
        this.dossieProduto.setId(1L);
        Set<SituacaoDossie> situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.CRIADO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        Response response = Response.ok("123").build();
        Response responseNotFound = Response.status(Response.Status.NOT_FOUND).build();
        PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(anyString(), eq(EnumMetodoHTTP.POST), anyMap(), anyMap(), anyString(), anyString())).thenReturn(response);
        PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(anyString(), eq(EnumMetodoHTTP.GET), anyMap(), anyMap(), anyString(), anyString())).thenReturn(responseNotFound);
        this.bpmServico.criaProcessoBPM(dossieProduto);
    }

    /**
     * <p>
     * Realiza o teste do método criaProcessoBPM em que a consulta ao BPM pela
     * chave de correlação retorna a indicação de existência de uma instanca de
     * processo.
     * </p>. Resultado Esperado: Execução com sucesso. com retorno do
     * identificaor existente
     *
     * @author c090347
     *
     */
    @Test
    public void testCriaProcessoBPMCaso7() {
        this.dossieProduto.setId(1L);
        Set<SituacaoDossie> situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.CRIADO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        Response response = Response.ok().build();
        Response responseConsultaIntanciaBPM = Response.ok("{\"process-instance-id\":30}").build();
        PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(anyString(), eq(EnumMetodoHTTP.POST), anyMap(), anyMap(), anyString(), anyString())).thenReturn(response);
        PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(anyString(), eq(EnumMetodoHTTP.GET), anyMap(), anyMap(), isNull(String.class), anyString())).thenReturn(responseConsultaIntanciaBPM);
        this.bpmServico.criaProcessoBPM(dossieProduto);
    }

    /**
     * <p>
     * Realiza o teste do metodo criaProcessoBPM simulando um retorno nulo.
     * </p>. Resultado Esperado: Lançamento de Exceção do tipo
     * BPMException.class
     *
     * @author c090347
     *
     */
    @Test(expected = BpmException.class)
    public void testCriaProcessoBPMCaso8() {
        this.dossieProduto.setId(1L);
        Set<SituacaoDossie> situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.CRIADO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        Response responseNotFound = Response.status(Response.Status.NOT_FOUND).build();
        PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(anyString(), eq(EnumMetodoHTTP.POST), anyMap(), anyMap(), anyString(), anyString())).thenReturn(null);
        PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(anyString(), eq(EnumMetodoHTTP.GET), anyMap(), anyMap(), anyString(), anyString())).thenReturn(responseNotFound);
        this.bpmServico.criaProcessoBPM(dossieProduto);
    }

    /**
     * <p>
     * Realiza o teste do metodo criaProcessoBPM simulando um retorno de
     * indisponibilidade sob codigo 503.
     * </p>. Resultado Esperado: Lançamento de Exceção do tipo
     * BPMException.class
     *
     * @author c090347
     *
     */
    @Test(expected = BpmException.class)
    public void testCriaProcessoBPMCaso9() {
        this.dossieProduto.setId(1L);
        Set<SituacaoDossie> situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.CRIADO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        Response response = Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        Response responseNotFound = Response.status(Response.Status.NOT_FOUND).build();
        PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(anyString(), eq(EnumMetodoHTTP.POST), anyMap(), anyMap(), anyString(), anyString())).thenReturn(response);
        PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(anyString(), eq(EnumMetodoHTTP.GET), anyMap(), anyMap(), anyString(), anyString())).thenReturn(responseNotFound);
        this.bpmServico.criaProcessoBPM(dossieProduto);
    }

    /**
     * <p>
     * Realiza o teste do método criaProcessoBPM em que a consulta ao BPM pela
     * chave de correlação retorna um codigo HTTP difergente de 200 e 400.
     * </p>. Resultado Esperado: Execução com retorno do identificaor de
     * processo null
     *
     * @author c090347
     *
     */
    @Test
    public void testCriaProcessoBPMCaso10() {
        this.dossieProduto.setId(1L);
        Set<SituacaoDossie> situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.CRIADO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        Response response = Response.serverError().build();
        PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(anyString(), eq(EnumMetodoHTTP.GET), anyMap(), anyMap(), isNull(String.class), anyString())).thenReturn(response);
        Long id = this.bpmServico.criaProcessoBPM(dossieProduto);
        Assert.assertNull(id);
    }

    /**
     * <p>
     * Realiza o teste do metodo notificaBPM enviando uma chamada com o reigstro
     * do dossiê definido com cada uma das situações previstas para validar
     * todas as situações possíveis..
     * </p>. Resultado Esperado: Chamdas ao BPM realizadas com sucesso para as
     * situaçlões previstas e aausência de chamada quando a instancia de
     * processo no BPM não prevê a sinalização.
     *
     * @author c090347
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     * @throws java.io.IOException
     *
     */
    @Test
    public void testNotificaBPM() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, IOException {
        Field campoURL = this.bpmServico.getClass().getDeclaredField("urlBPM");
        campoURL.setAccessible(Boolean.TRUE);
        campoURL.set(this.bpmServico, null);

        Response response = Response.ok("{}").build();
        Response responseDelete = Response.noContent().build();
        Response responseSinal = Response.ok("[\"ALIMENTACAO\",\"CRIADO\",\"FINALIZADO\",\"TRATAMENTO\"]").build();
        PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(anyString(), eq(EnumMetodoHTTP.POST), anyMap(), anyMap(), anyString(), anyString())).thenReturn(response);
        PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(anyString(), eq(EnumMetodoHTTP.DELETE), anyMap(), anyMap(), anyString(), anyString())).thenReturn(responseDelete);
        PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(anyString(), eq(EnumMetodoHTTP.GET), anyMap(), anyMap(), anyString(), anyString())).thenReturn(responseSinal);
        PowerMockito.when(UtilRest.consumirServicoOAuth2JSON(anyString(), eq(EnumMetodoHTTP.POST), anyMap(), anyMap(), anyString(), anyString())).thenReturn(response);
        PowerMockito.when(UtilRest.consumirServicoOAuth2JSON(anyString(), eq(EnumMetodoHTTP.DELETE), anyMap(), anyMap(), anyString(), anyString())).thenReturn(responseDelete);
        PowerMockito.when(UtilRest.consumirServicoOAuth2JSON(anyString(), eq(EnumMetodoHTTP.GET), anyMap(), anyMap(), anyString(), anyString())).thenReturn(responseSinal);
        this.dossieProduto.setNomeContainerBPM("container-bpm");
        this.dossieProduto.setNomeProcessoBPM("processo-bpm");
        this.dossieProduto.setIdInstanciaProcessoBPM((long) (Math.random() * 100));

        Set<SituacaoDossie> situacoesDossie = new HashSet<>();

        //Estas chamadas fazem com que os dados de autenticação (usuario e senha) 
        //não estejam definidos para criar o cenario em que a classe realiza a comnicação vom autenticação OAuth2
        campoURL.set(this.bpmServico, null);
        PowerMockito.when(UtilParametro.getParametro(PROPRIEDADE_USUARIO_BPM, false)).thenReturn("usuario-jbpm");
        PowerMockito.when(UtilParametro.getParametro(PROPRIEDADE_SENHA_BPM, false)).thenReturn(null);
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.CRIADO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        this.bpmServico.notificaBPM(dossieProduto);

        campoURL.set(this.bpmServico, null);
        PowerMockito.when(UtilParametro.getParametro(PROPRIEDADE_USUARIO_BPM, false)).thenReturn(null);
        PowerMockito.when(UtilParametro.getParametro(PROPRIEDADE_SENHA_BPM, false)).thenReturn(null);
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.CRIADO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        this.bpmServico.notificaBPM(dossieProduto);

        situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.CRIADO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        this.bpmServico.notificaBPM(dossieProduto);

        situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.ALIMENTACAO_FINALIZADA));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        this.bpmServico.notificaBPM(dossieProduto);

        situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.CONFORME));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        this.bpmServico.notificaBPM(dossieProduto);

        situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.PENDENTE_INFORMACAO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        this.bpmServico.notificaBPM(dossieProduto);

        situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.PENDENTE_SEGURANCA));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        this.bpmServico.notificaBPM(dossieProduto);

        situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.SEGURANCA_FINALIZADO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        this.bpmServico.notificaBPM(dossieProduto);

        situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.CANCELADO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        this.bpmServico.notificaBPM(dossieProduto);

        situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.FINALIZADO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        this.bpmServico.notificaBPM(dossieProduto);

        situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.FINALIZADO_CONFORME));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        this.bpmServico.notificaBPM(dossieProduto);

        situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.FINALIZADO_INCONFORME));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        this.bpmServico.notificaBPM(dossieProduto);

        //Esta chamada faz com que não seja definido um sinal e o metodo não executa nada.
        situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.EM_ALIMENTACAO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        this.bpmServico.notificaBPM(dossieProduto);

        //Esta chamada faz pular o bloco por enviar um proceso com configuração não aptas para comunicação, finalizado e sem instancia de BPM definida
        this.dossieProduto = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("dossie-produto-base.json")), UTF_8), DossieProduto.class);
        this.dossieProduto.getProcesso().setNomeContainerBPM(null);
        this.dossieProduto.getProcesso().setNomeProcessoBPM(null);
        situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.EM_ALIMENTACAO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        this.bpmServico.notificaBPM(dossieProduto);

        //Esta chamada faz pular o bloco por enviar dossiê de produto sem definição de instancia do BPM para comunicação
        this.dossieProduto = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("dossie-produto-base.json")), UTF_8), DossieProduto.class);
        this.dossieProduto.setNomeContainerBPM(null);
        this.dossieProduto.setNomeProcessoBPM(null);
        this.dossieProduto.setIdInstanciaProcessoBPM(null);
        situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.EM_ALIMENTACAO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        this.bpmServico.notificaBPM(dossieProduto);

        //Esta chamada faz com  que não seja definido uma solicitação para um dossiê finalizado e o metodo não executa nada.
        this.dossieProduto = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("dossie-produto-base.json")), UTF_8), DossieProduto.class);
        this.dossieProduto.setDataHoraFinalizacao(Calendar.getInstance());
        this.dossieProduto.setNomeContainerBPM("container-bpm");
        this.dossieProduto.setNomeProcessoBPM("processo-bpm");
        this.dossieProduto.setIdInstanciaProcessoBPM((long) (Math.random() * 100));
        situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.EM_ALIMENTACAO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);
        this.bpmServico.notificaBPM(dossieProduto);
    }

    @Test
    public void testVerificaInstanciaEsperaSinalTratamento() {

        Response responseSinal = Response.ok("[\"ALIMENTACAO\",\"CRIADO\",\"FINALIZADO\",\"TRATAMENTO\"]").build();
        PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(anyString(), eq(EnumMetodoHTTP.GET), anyMap(), anyMap(), anyString(), anyString())).thenReturn(responseSinal);
        PowerMockito.when(UtilRest.consumirServicoOAuth2JSON(anyString(), eq(EnumMetodoHTTP.GET), anyMap(), anyMap(), anyString(), anyString())).thenReturn(responseSinal);

        //Define o identificador do processo BPM para 
        this.dossieProduto.setIdInstanciaProcessoBPM(1L);
        this.bpmServico.verificaInstanciaEsperaSinalTratamento(this.dossieProduto);

        //Chamada realizada sem o identificador do processo BPM
        this.dossieProduto.setIdInstanciaProcessoBPM(null);
        this.bpmServico.verificaInstanciaEsperaSinalTratamento(this.dossieProduto);

        //Chamada realizada sem o identificador do processo BPM
        this.dossieProduto.setNomeContainerBPM(null);
        this.dossieProduto.setNomeProcessoBPM(null);
        this.bpmServico.verificaInstanciaEsperaSinalTratamento(this.dossieProduto);

    }

    @Test
    public void testRealizarComunicacaoBPM() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String nomeProcessoBPM = this.dossieProduto.getNomeProcessoBPM();
        String nomeContainerBPM = this.dossieProduto.getNomeContainerBPM();

        Method metodoRealizarComunicacaoBPM = this.bpmServico.getClass().getDeclaredMethod("realizarComunicacaoBPM", DossieProduto.class);
        metodoRealizarComunicacaoBPM.setAccessible(Boolean.TRUE);

        this.dossieProduto.setNomeContainerBPM(null);
        this.dossieProduto.setNomeProcessoBPM(null);
        Object resultado1 = metodoRealizarComunicacaoBPM.invoke(this.bpmServico, this.dossieProduto);
        Assert.assertFalse(Boolean.valueOf(resultado1.toString()));

        this.dossieProduto.setNomeContainerBPM(nomeContainerBPM);
        Object resultado2 = metodoRealizarComunicacaoBPM.invoke(this.bpmServico, this.dossieProduto);
        Assert.assertFalse(Boolean.valueOf(resultado2.toString()));

        this.dossieProduto.setNomeProcessoBPM(nomeProcessoBPM);
        Object resultado3 = metodoRealizarComunicacaoBPM.invoke(this.bpmServico, this.dossieProduto);
        Assert.assertTrue(Boolean.valueOf(resultado3.toString()));
    }

    /**
     * <p>
     * Realiza o teste do método envia sinal prevendo os retornos: - Retorno
     * nulo como resposta do JBPM; - Retorno como serviço indisponivel; e -
     * Retorno como erro interno do servidor.
     * </p>. Resultado Esperado: Lançamento de Exceção do tipo
     * BpmException.class em todos os casos
     *
     * @author c090347
     */
    @Test(expected = BpmException.class)
    public void testEnviaSinalException() {

        Response responseServiceUnavailable = Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        Response responseServerError = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        Response responseSinal = Response.ok("[\"CRIADO\"]").build();

        PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(anyString(), eq(EnumMetodoHTTP.GET), anyMapOf(String.class, String.class), anyMapOf(String.class, String.class), anyString(), anyString())).thenReturn(responseSinal);
        PowerMockito.when(UtilRest.consumirServicoOAuth2JSON(anyString(), eq(EnumMetodoHTTP.GET), anyMapOf(String.class, String.class), anyMapOf(String.class, String.class), anyString(), anyString())).thenReturn(responseSinal);
        this.dossieProduto.setNomeContainerBPM("container-bpm");
        this.dossieProduto.setNomeProcessoBPM("processo-bpm");
        this.dossieProduto.setIdInstanciaProcessoBPM((long) (Math.random() * 100));

        Set<SituacaoDossie> situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.CRIADO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);

        boolean falhaNullPointer = Boolean.FALSE;
        try {
            String endpoint = "url-jbpm/server/containers/"
                    .concat(dossieProduto.getNomeContainerBPM())
                    .concat("/processes/instances/")
                    .concat(dossieProduto.getIdInstanciaProcessoBPM().toString())
                    .concat("/signal/CRIADO");
            PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(eq(endpoint), eq(EnumMetodoHTTP.POST), anyMapOf(String.class, String.class), anyMapOf(String.class, String.class), anyString(), anyString())).thenReturn(null);
            PowerMockito.when(UtilRest.consumirServicoOAuth2JSON(eq(endpoint), eq(EnumMetodoHTTP.POST), anyMapOf(String.class, String.class), anyMapOf(String.class, String.class), anyString(), anyString())).thenReturn(null);
            this.bpmServico.notificaBPM(dossieProduto);
        } catch (BpmException be) {
            falhaNullPointer = Boolean.TRUE;
        }
        Assert.assertTrue("Falha ao validar ocorrencia de exceção por NullPointerException.", falhaNullPointer);

        boolean falhaServiceUnavailable = Boolean.FALSE;
        try {
            String endpoint = "url-jbpm/server/containers/"
                    .concat(dossieProduto.getNomeContainerBPM())
                    .concat("/processes/instances/")
                    .concat(dossieProduto.getIdInstanciaProcessoBPM().toString())
                    .concat("/signal/CRIADO");
            PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(eq(endpoint), eq(EnumMetodoHTTP.POST), anyMapOf(String.class, String.class), anyMapOf(String.class, String.class), anyString(), anyString())).thenReturn(responseServiceUnavailable);
            PowerMockito.when(UtilRest.consumirServicoOAuth2JSON(eq(endpoint), eq(EnumMetodoHTTP.POST), anyMapOf(String.class, String.class), anyMapOf(String.class, String.class), anyString(), anyString())).thenReturn(responseServiceUnavailable);
            this.bpmServico.notificaBPM(dossieProduto);
        } catch (BpmException be) {
            falhaServiceUnavailable = Boolean.TRUE;
        }
        Assert.assertTrue("Falha ao validar ocorrencia de exceção por ServiceUnavailable 503.", falhaServiceUnavailable);

        boolean falhaServerError = Boolean.FALSE;
        try {
            String endpoint = "url-jbpm/server/containers/"
                    .concat(dossieProduto.getNomeContainerBPM())
                    .concat("/processes/instances/")
                    .concat(dossieProduto.getIdInstanciaProcessoBPM().toString())
                    .concat("/signal/CRIADO");
            PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(eq(endpoint), eq(EnumMetodoHTTP.POST), anyMapOf(String.class, String.class), anyMapOf(String.class, String.class), anyString(), anyString())).thenReturn(responseServerError);
            PowerMockito.when(UtilRest.consumirServicoOAuth2JSON(eq(endpoint), eq(EnumMetodoHTTP.POST), anyMapOf(String.class, String.class), anyMapOf(String.class, String.class), anyString(), anyString())).thenReturn(responseServerError);
            this.bpmServico.notificaBPM(dossieProduto);
        } catch (BpmException be) {
            falhaServerError = Boolean.TRUE;
        }
        Assert.assertTrue("Falha ao validar ocorrencia de exceção por ServiceUnavailable 503.", falhaServerError);

        if (falhaNullPointer && falhaServerError && falhaServiceUnavailable) {
            throw new BpmException("Falha ao comunicar com o BPM.", Boolean.TRUE);
        }
    }

    /**
     * Realiza o teste do método aborta processo prevendo os retornos: Null como
     * resposta do JBPM; Serviço indisponivel; e erro interno do servidor.
     * Resultado Esperado: Lançamento de Exceção do tipo BpmException.class em
     * todos os casos
     *
     * Comunicações realizadas com fluxo de autenticação OAuth2 para garantir
     * cobertura completa
     *
     * @author c090347
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     */
    @Test(expected = BpmException.class)
    public void testAbortaProcessoBPMException() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field campoBasicAuth = this.bpmServico.getClass().getDeclaredField("basicAuth");
        campoBasicAuth.setAccessible(Boolean.TRUE);
        campoBasicAuth.set(this.bpmServico, null);

        Field campoURL = this.bpmServico.getClass().getDeclaredField("urlBPM");
        campoURL.setAccessible(Boolean.TRUE);

        Response responseServiceUnavailable = Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        Response responseServerError = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        this.dossieProduto.setNomeContainerBPM("container-bpm");
        this.dossieProduto.setNomeProcessoBPM("processo-bpm");
        this.dossieProduto.setIdInstanciaProcessoBPM((long) (Math.random() * 100));

        Set<SituacaoDossie> situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.CANCELADO));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);

        campoURL.set(this.bpmServico, null);
        PowerMockito.when(UtilParametro.getParametro(PROPRIEDADE_USUARIO_BPM, false)).thenReturn("usuario-jbpm");
        PowerMockito.when(UtilParametro.getParametro(PROPRIEDADE_SENHA_BPM, false)).thenReturn(null);

        boolean falhaNullPointer = Boolean.FALSE;
        try {
            String endpoint = "url-jbpm/server/containers/"
                    .concat(dossieProduto.getNomeContainerBPM())
                    .concat("/processes/instances/")
                    .concat(dossieProduto.getIdInstanciaProcessoBPM().toString());
            PowerMockito.when(UtilRest.consumirServicoOAuth2JSON(eq(endpoint), eq(EnumMetodoHTTP.DELETE), anyMapOf(String.class, String.class), anyMapOf(String.class, String.class), eq(null), anyString())).thenReturn(null);

            this.bpmServico.notificaBPM(dossieProduto);
        } catch (BpmException be) {
            falhaNullPointer = Boolean.TRUE;
        }
        Assert.assertTrue("Falha ao validar ocorrencia de exceção por NullPointerException.", falhaNullPointer);

        campoURL.set(this.bpmServico, null);
        PowerMockito.when(UtilParametro.getParametro(PROPRIEDADE_USUARIO_BPM, false)).thenReturn(null);
        PowerMockito.when(UtilParametro.getParametro(PROPRIEDADE_SENHA_BPM, false)).thenReturn("senha-jbpm");

        boolean falhaServiceUnavailable = Boolean.FALSE;
        try {
            String endpoint = "url-jbpm/server/containers/"
                    .concat(dossieProduto.getNomeContainerBPM())
                    .concat("/processes/instances/")
                    .concat(dossieProduto.getIdInstanciaProcessoBPM().toString());
            PowerMockito.when(UtilRest.consumirServicoOAuth2JSON(eq(endpoint), eq(EnumMetodoHTTP.DELETE), anyMapOf(String.class, String.class), anyMapOf(String.class, String.class), anyString(), anyString())).thenReturn(responseServiceUnavailable);
            this.bpmServico.notificaBPM(dossieProduto);
        } catch (BpmException be) {
            falhaServiceUnavailable = Boolean.TRUE;
        }
        Assert.assertTrue("Falha ao validar ocorrencia de exceção por ServiceUnavailable 503.", falhaServiceUnavailable);

        campoURL.set(this.bpmServico, null);
        PowerMockito.when(UtilParametro.getParametro(PROPRIEDADE_USUARIO_BPM, false)).thenReturn(null);
        PowerMockito.when(UtilParametro.getParametro(PROPRIEDADE_SENHA_BPM, false)).thenReturn(null);

        boolean falhaServerError = Boolean.FALSE;
        try {
            String endpoint = "url-jbpm/server/containers/"
                    .concat(dossieProduto.getNomeContainerBPM())
                    .concat("/processes/instances/")
                    .concat(dossieProduto.getIdInstanciaProcessoBPM().toString());
            PowerMockito.when(UtilRest.consumirServicoOAuth2JSON(eq(endpoint), eq(EnumMetodoHTTP.DELETE), anyMapOf(String.class, String.class), anyMapOf(String.class, String.class), anyString(), anyString())).thenReturn(responseServerError);
            this.bpmServico.notificaBPM(dossieProduto);
        } catch (BpmException be) {
            falhaServerError = Boolean.TRUE;
        }
        Assert.assertTrue("Falha ao validar ocorrencia de exceção por ServiceUnavailable 503.", falhaServerError);

        if (falhaNullPointer && falhaServerError && falhaServiceUnavailable) {
            throw new BpmException("Falha ao comunicar com o BPM.", Boolean.TRUE);
        }
    }

    /**
     * <p>
     * Realiza o teste do método checa sinais prevendo os retornos: - Retorno
     * nulo como resposta do JBPM; - Retorno como serviço indisponivel; e -
     * Retorno como erro interno do servidor.
     * </p>. Resultado Esperado: Lançamento de Exceção do tipo
     * BpmException.class em todos os casos
     *
     * @author c090347
     */
    @Test(expected = BpmException.class)
    public void testChecaSinalEsperadoException() {

        Response responseServiceUnavailable = Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        Response responseServerError = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        this.dossieProduto.setNomeContainerBPM("container-bpm");
        this.dossieProduto.setNomeProcessoBPM("processo-bpm");
        this.dossieProduto.setIdInstanciaProcessoBPM((long) (Math.random() * 100));

        Set<SituacaoDossie> situacoesDossie = new HashSet<>();
        situacoesDossie.add(this.mockSituacaoDossie(SituacaoDossieEnum.CONFORME));
        this.dossieProduto.setSituacoesDossie(situacoesDossie);

        boolean falhaNullPointer = Boolean.FALSE;
        try {
            String endpoint = "url-jbpm/server/containers/"
                    .concat(dossieProduto.getNomeContainerBPM())
                    .concat("/processes/instances/")
                    .concat(dossieProduto.getIdInstanciaProcessoBPM().toString())
                    .concat("/signals");
            PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(eq(endpoint), eq(EnumMetodoHTTP.GET), anyMapOf(String.class, String.class), anyMapOf(String.class, String.class), eq(null), anyString())).thenReturn(null);

            this.bpmServico.notificaBPM(dossieProduto);
        } catch (BpmException be) {
            falhaNullPointer = Boolean.TRUE;
        }
        Assert.assertTrue("Falha ao validar ocorrencia de exceção por NullPointerException.", falhaNullPointer);

        boolean falhaServiceUnavailable = Boolean.FALSE;
        try {
            String endpoint = "url-jbpm/server/containers/"
                    .concat(dossieProduto.getNomeContainerBPM())
                    .concat("/processes/instances/")
                    .concat(dossieProduto.getIdInstanciaProcessoBPM().toString())
                    .concat("/signals");
            PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(eq(endpoint), eq(EnumMetodoHTTP.GET), anyMapOf(String.class, String.class), anyMapOf(String.class, String.class), eq(null), anyString())).thenReturn(responseServiceUnavailable);
            this.bpmServico.notificaBPM(dossieProduto);
        } catch (BpmException be) {
            falhaServiceUnavailable = Boolean.TRUE;
        }
        Assert.assertTrue("Falha ao validar ocorrencia de exceção por ServiceUnavailable 503.", falhaServiceUnavailable);

        boolean falhaServerError = Boolean.FALSE;
        try {
            String endpoint = "url-jbpm/server/containers/"
                    .concat(dossieProduto.getNomeContainerBPM())
                    .concat("/processes/instances/")
                    .concat(dossieProduto.getIdInstanciaProcessoBPM().toString())
                    .concat("/signals");
            PowerMockito.when(UtilRest.consumirServicoBasicAuthJSON(eq(endpoint), eq(EnumMetodoHTTP.GET), anyMapOf(String.class, String.class), anyMapOf(String.class, String.class), eq(null), anyString())).thenReturn(responseServerError);
            this.bpmServico.notificaBPM(dossieProduto);
        } catch (BpmException be) {
            falhaServerError = Boolean.TRUE;
        }
        Assert.assertTrue("Falha ao validar ocorrencia de exceção por ServiceUnavailable 503.", falhaServerError);

        if (falhaNullPointer && falhaServerError && falhaServiceUnavailable) {
            throw new BpmException("Falha ao comunicar com o BPM.", Boolean.TRUE);
        }
    }

    private void initTipoSitacao() throws IOException {
        this.tipoSituacaoAguardandoAlimentacao = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("aguardando-alimentacao.json")), UTF_8), TipoSituacaoDossie.class);
        this.tipoSituacaoAguardandoTratamento = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("aguardando-tratamento.json")), UTF_8), TipoSituacaoDossie.class);
        this.tipoSituacaoAlimentacaoFinalizada = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("alimentacao-finalizada.json")), UTF_8), TipoSituacaoDossie.class);
        this.tipoSituacaoAnaliseSeguranca = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("analise-seguranca.json")), UTF_8), TipoSituacaoDossie.class);
        this.tipoSituacaoCancelado = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("cancelado.json")), UTF_8), TipoSituacaoDossie.class);
        this.tipoSituacaoConforme = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("conforme.json")), UTF_8), TipoSituacaoDossie.class);
        this.tipoSituacaoCriado = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("criado.json")), UTF_8), TipoSituacaoDossie.class);
        this.tipoSituacaoEmAlimentacao = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("em-alimentacao.json")), UTF_8), TipoSituacaoDossie.class);
        this.tipoSituacaoEmTratamento = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("em-tratamento.json")), UTF_8), TipoSituacaoDossie.class);
        this.tipoSituacaoFinalizado = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("finalizado.json")), UTF_8), TipoSituacaoDossie.class);
        this.tipoSituacaoFinalizadoConforme = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("finalizado-conforme.json")), UTF_8), TipoSituacaoDossie.class);
        this.tipoSituacaoFinalizadoInconforme = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("finalizado-inconforme.json")), UTF_8), TipoSituacaoDossie.class);
        this.tipoSituacaoPendenteInformacao = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("pendente-informacao.json")), UTF_8), TipoSituacaoDossie.class);
        this.tipoSituacaoPendenteSeguranca = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("pendente-seguranca.json")), UTF_8), TipoSituacaoDossie.class);
        this.tipoSituacaoRascunho = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("rascunho.json")), UTF_8), TipoSituacaoDossie.class);
        this.tipoSituacaoSegurancaFinalizado = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("seguranca-finalizado.json")), UTF_8), TipoSituacaoDossie.class);

        Mockito.when(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.AGUARDANDO_ALIMENTACAO)).thenReturn(this.tipoSituacaoAguardandoAlimentacao);
        Mockito.when(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.AGUARDANDO_TRATAMENTO)).thenReturn(this.tipoSituacaoAguardandoTratamento);
        Mockito.when(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.ALIMENTACAO_FINALIZADA)).thenReturn(this.tipoSituacaoAlimentacaoFinalizada);
        Mockito.when(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.ANALISE_SEGURANCA)).thenReturn(this.tipoSituacaoAnaliseSeguranca);
        Mockito.when(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.CANCELADO)).thenReturn(this.tipoSituacaoCancelado);
        Mockito.when(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.CONFORME)).thenReturn(this.tipoSituacaoConforme);
        Mockito.when(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.CRIADO)).thenReturn(this.tipoSituacaoCriado);
        Mockito.when(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.EM_ALIMENTACAO)).thenReturn(this.tipoSituacaoEmAlimentacao);
        Mockito.when(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.EM_TRATAMENTO)).thenReturn(this.tipoSituacaoEmTratamento);
        Mockito.when(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.FINALIZADO)).thenReturn(this.tipoSituacaoFinalizado);
        Mockito.when(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.FINALIZADO_CONFORME)).thenReturn(this.tipoSituacaoFinalizadoConforme);
        Mockito.when(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.FINALIZADO_INCONFORME)).thenReturn(this.tipoSituacaoFinalizadoInconforme);
        Mockito.when(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.PENDENTE_INFORMACAO)).thenReturn(this.tipoSituacaoPendenteInformacao);
        Mockito.when(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.PENDENTE_SEGURANCA)).thenReturn(this.tipoSituacaoPendenteSeguranca);
        Mockito.when(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.RASCUNHO)).thenReturn(this.tipoSituacaoRascunho);
        Mockito.when(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.SEGURANCA_FINALIZADO)).thenReturn(this.tipoSituacaoSegurancaFinalizado);
    }

    private SituacaoDossie mockSituacaoDossie(SituacaoDossieEnum situacaoDossieEnum) {
        switch (situacaoDossieEnum) {
            case AGUARDANDO_ALIMENTACAO:
                SituacaoDossie situacaoAguardandoAlimentacao = new SituacaoDossie();
                situacaoAguardandoAlimentacao.setDataHoraInclusao(Calendar.getInstance());
                situacaoAguardandoAlimentacao.setTipoSituacaoDossie(tipoSituacaoAlimentacaoFinalizada);
                return situacaoAguardandoAlimentacao;

            case AGUARDANDO_TRATAMENTO:
                SituacaoDossie situacaoAguardandoTratamento = new SituacaoDossie();
                situacaoAguardandoTratamento.setDataHoraInclusao(Calendar.getInstance());
                situacaoAguardandoTratamento.setTipoSituacaoDossie(tipoSituacaoAguardandoTratamento);
                return situacaoAguardandoTratamento;

            case ANALISE_SEGURANCA:
                SituacaoDossie situacaoAnaliseSeguranca = new SituacaoDossie();
                situacaoAnaliseSeguranca.setDataHoraInclusao(Calendar.getInstance());
                situacaoAnaliseSeguranca.setTipoSituacaoDossie(tipoSituacaoAnaliseSeguranca);
                return situacaoAnaliseSeguranca;

            case ALIMENTACAO_FINALIZADA:
                SituacaoDossie situacaoAlimentacaoFinalizado = new SituacaoDossie();
                situacaoAlimentacaoFinalizado.setDataHoraInclusao(Calendar.getInstance());
                situacaoAlimentacaoFinalizado.setTipoSituacaoDossie(tipoSituacaoAlimentacaoFinalizada);
                return situacaoAlimentacaoFinalizado;

            case CANCELADO:
                SituacaoDossie situacaoCancelado = new SituacaoDossie();
                situacaoCancelado.setDataHoraInclusao(Calendar.getInstance());
                situacaoCancelado.setTipoSituacaoDossie(tipoSituacaoCancelado);
                return situacaoCancelado;

            case CONFORME:
                SituacaoDossie situacaoConforme = new SituacaoDossie();
                situacaoConforme.setDataHoraInclusao(Calendar.getInstance());
                situacaoConforme.setTipoSituacaoDossie(tipoSituacaoConforme);
                return situacaoConforme;

            case CRIADO:
                SituacaoDossie situacaoDossieCriado = new SituacaoDossie();
                situacaoDossieCriado.setDataHoraInclusao(Calendar.getInstance());
                situacaoDossieCriado.setTipoSituacaoDossie(tipoSituacaoCriado);
                return situacaoDossieCriado;

            case EM_ALIMENTACAO:
                SituacaoDossie situacaoDossieEmAlimentacao = new SituacaoDossie();
                situacaoDossieEmAlimentacao.setDataHoraInclusao(Calendar.getInstance());
                situacaoDossieEmAlimentacao.setTipoSituacaoDossie(tipoSituacaoEmAlimentacao);
                return situacaoDossieEmAlimentacao;

            case EM_TRATAMENTO:
                SituacaoDossie situacaoDossieEmTratamento = new SituacaoDossie();
                situacaoDossieEmTratamento.setDataHoraInclusao(Calendar.getInstance());
                situacaoDossieEmTratamento.setTipoSituacaoDossie(tipoSituacaoEmTratamento);
                return situacaoDossieEmTratamento;

            case FINALIZADO:
                SituacaoDossie situacaoDossieFinalizado = new SituacaoDossie();
                situacaoDossieFinalizado.setDataHoraInclusao(Calendar.getInstance());
                situacaoDossieFinalizado.setTipoSituacaoDossie(tipoSituacaoFinalizado);
                return situacaoDossieFinalizado;

            case FINALIZADO_CONFORME:
                SituacaoDossie situacaoDossieFinalizadoConforme = new SituacaoDossie();
                situacaoDossieFinalizadoConforme.setDataHoraInclusao(Calendar.getInstance());
                situacaoDossieFinalizadoConforme.setTipoSituacaoDossie(tipoSituacaoFinalizadoConforme);
                return situacaoDossieFinalizadoConforme;

            case FINALIZADO_INCONFORME:
                SituacaoDossie situacaoDossieFinalizadoInconforme = new SituacaoDossie();
                situacaoDossieFinalizadoInconforme.setDataHoraInclusao(Calendar.getInstance());
                situacaoDossieFinalizadoInconforme.setTipoSituacaoDossie(tipoSituacaoFinalizadoInconforme);
                return situacaoDossieFinalizadoInconforme;

            case PENDENTE_INFORMACAO:
                SituacaoDossie situacaoPendenteInformacao = new SituacaoDossie();
                situacaoPendenteInformacao.setDataHoraInclusao(Calendar.getInstance());
                situacaoPendenteInformacao.setTipoSituacaoDossie(tipoSituacaoPendenteInformacao);
                return situacaoPendenteInformacao;

            case PENDENTE_SEGURANCA:
                SituacaoDossie situacaoPendenteSeguranca = new SituacaoDossie();
                situacaoPendenteSeguranca.setDataHoraInclusao(Calendar.getInstance());
                situacaoPendenteSeguranca.setTipoSituacaoDossie(tipoSituacaoPendenteSeguranca);
                return situacaoPendenteSeguranca;

            case RASCUNHO:
                SituacaoDossie situacaoRascunho = new SituacaoDossie();
                situacaoRascunho.setDataHoraInclusao(Calendar.getInstance());
                situacaoRascunho.setTipoSituacaoDossie(tipoSituacaoRascunho);
                return situacaoRascunho;

            case SEGURANCA_FINALIZADO:
                SituacaoDossie situacaoSegurancaFinalizado = new SituacaoDossie();
                situacaoSegurancaFinalizado.setDataHoraInclusao(Calendar.getInstance());
                situacaoSegurancaFinalizado.setTipoSituacaoDossie(tipoSituacaoSegurancaFinalizado);
                return situacaoSegurancaFinalizado;
            default:
                return null;
        }
    }

}
