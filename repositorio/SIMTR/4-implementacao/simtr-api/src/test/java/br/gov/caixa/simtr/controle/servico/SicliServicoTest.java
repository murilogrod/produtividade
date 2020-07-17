package br.gov.caixa.simtr.controle.servico;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.specimpl.BuiltResponse;
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
import br.gov.caixa.simtr.controle.excecao.SicliException;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.KeycloakUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UtilParametro.class, UtilRest.class})
public class SicliServicoTest {

    @InjectMocks
    private CadastroCaixaServico sicliServico;

    @Mock
    private KeycloakUtil keycloakUtil;

    @Mock
    private Response responseConsultaInexistente;

    @Mock
    private Response responseConsulta00065179609;

    @Mock
    private Response responseAtualizacao;

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_DIR = "/mock/sicli/";
    private static final String UTF_8 = "UTF-8";

    private String respostaSicliClasse5Inexistente;
    private String respostaSicliClasse5CPF00065179609;

    private Documento cnh00065279609;

    private final MockStatusType mockStatusTypeBadRequest = new MockStatusType(Response.Status.Family.CLIENT_ERROR, 400);
    private final MockStatusType mockStatusTypeConflict = new MockStatusType(Response.Status.Family.CLIENT_ERROR, 409);
    private final MockStatusType mockStatusTypeNotFound = new MockStatusType(Response.Status.Family.CLIENT_ERROR, 404);
    private final MockStatusType mockStatusTypeOK = new MockStatusType(Response.Status.Family.SUCCESSFUL, 200);
    private final MockStatusType mockStatusTypeServerError = new MockStatusType(Response.Status.Family.SERVER_ERROR, 500);

    ////////////// CONSTANTES UTILIZADAS NA ATUALIZACAO DE DADOS DO SICLI
    private final String PARAMETRO_SICLI = "url.servico.atualiza.cliente.sicli";
    private final String PARAMETRO_SICLI_RETORNO = "http://api.mock.sicli/cliente/v1";
    //----------------------

    @Before
    public void init() throws IOException {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(UtilParametro.class, UtilRest.class);

        PowerMockito.when(UtilParametro.getParametro(Mockito.eq(PARAMETRO_SICLI), Mockito.anyBoolean())).thenReturn(PARAMETRO_SICLI_RETORNO);

        String jsonDocumentoCNH = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("cnh-00065179609.json")));
        this.cnh00065279609 = this.mapper.readValue(jsonDocumentoCNH, Documento.class);

        this.respostaSicliClasse5Inexistente = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("classe5-inexistente.json")), UTF_8);
        this.respostaSicliClasse5CPF00065179609 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("classe5-00065179609.json")), UTF_8);

        Mockito.when(this.responseConsulta00065179609.readEntity(String.class)).thenReturn(respostaSicliClasse5CPF00065179609);
        Mockito.when(this.responseConsulta00065179609.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        Mockito.when(this.responseConsulta00065179609.getStatusInfo()).thenReturn(mockStatusTypeOK);
        Mockito.when(this.responseConsultaInexistente.readEntity(String.class)).thenReturn(respostaSicliClasse5Inexistente);
        Mockito.when(this.responseConsultaInexistente.getStatus()).thenReturn(Response.Status.NOT_FOUND.getStatusCode());
        Mockito.when(this.responseConsultaInexistente.getStatusInfo()).thenReturn(mockStatusTypeNotFound);
    }

    @Test
    public void atualizaDadosDossieDigitalCasoCadastroExistente() {
        PowerMockito.when(UtilRest.consumirServicoOAuth2(Mockito.anyString(), Mockito.eq(EnumMetodoHTTP.GET), Mockito.anyMap(), Mockito.anyMap(), Mockito.any(), Mockito.anyString(), Mockito.isNull(String.class), Mockito.anyString())).thenReturn(responseConsulta00065179609);
        PowerMockito.when(UtilRest.consumirServicoOAuth2JSON(Mockito.anyString(), Mockito.eq(EnumMetodoHTTP.PUT), Mockito.anyMap(), Mockito.anyMap(), Mockito.any(), Mockito.anyString())).thenReturn(Response.ok().build());

        this.sicliServico.atualizaDadosDossieDigital(65179609L, TipoPessoaEnum.F, new ArrayList<>(), Arrays.asList(cnh00065279609));
    }

    @Test(expected = SicliException.class)
    public void atualizaDadosDossieDigitalCasoRespostaBadRequestNovoCadastroSICLI() {
        PowerMockito.when(UtilRest.consumirServicoOAuth2(Mockito.anyString(), Mockito.eq(EnumMetodoHTTP.GET), Mockito.anyMap(), Mockito.anyMap(), Mockito.any(), Mockito.anyString(), Mockito.isNull(String.class), Mockito.anyString())).thenReturn(responseConsultaInexistente);

        responseAtualizacao = new MockResponse("FALHA MOCKADA AO CADASTRAR CLIENTE NO SICLI", Response.Status.BAD_REQUEST, mockStatusTypeBadRequest);
        PowerMockito.when(UtilRest.consumirServicoOAuth2JSON(Mockito.anyString(), Mockito.eq(EnumMetodoHTTP.POST), Mockito.anyMap(), Mockito.anyMap(), Mockito.any(), Mockito.anyString())).thenReturn(responseAtualizacao);

        this.sicliServico.atualizaDadosDossieDigital(65179609L, TipoPessoaEnum.F, new ArrayList<>(), Arrays.asList(cnh00065279609));
    }

    @Test(expected = SicliException.class)
    public void atualizaDadosDossieDigitalCasoRespostaServerErrorNovoCadastroSICLI() {
        PowerMockito.when(UtilRest.consumirServicoOAuth2(Mockito.anyString(), Mockito.eq(EnumMetodoHTTP.GET), Mockito.anyMap(), Mockito.anyMap(), Mockito.any(), Mockito.anyString(), Mockito.isNull(String.class), Mockito.anyString())).thenReturn(responseConsultaInexistente);

        responseAtualizacao = new MockResponse("FALHA MOCKADA AO CADASTRAR CLIENTE NO SICLI", Response.Status.INTERNAL_SERVER_ERROR, mockStatusTypeServerError);
        PowerMockito.when(UtilRest.consumirServicoOAuth2JSON(Mockito.anyString(), Mockito.eq(EnumMetodoHTTP.POST), Mockito.anyMap(), Mockito.anyMap(), Mockito.any(), Mockito.anyString())).thenReturn(responseAtualizacao);

        this.sicliServico.atualizaDadosDossieDigital(65179609L, TipoPessoaEnum.F, new ArrayList<>(), Arrays.asList(cnh00065279609));
    }

    @Test(expected = SicliException.class)
    public void atualizaDadosDossieDigitalCasoRespostaConflictedNovoCadastroSICLI() {
        PowerMockito.when(UtilRest.consumirServicoOAuth2(Mockito.anyString(), Mockito.eq(EnumMetodoHTTP.GET), Mockito.anyMap(), Mockito.anyMap(), Mockito.any(), Mockito.anyString(), Mockito.isNull(String.class), Mockito.anyString())).thenReturn(responseConsultaInexistente);

        responseAtualizacao = new MockResponse("FALHA MOCKADA AO CADASTRAR CLIENTE NO SICLI", Response.Status.CONFLICT, mockStatusTypeConflict);
        PowerMockito.when(UtilRest.consumirServicoOAuth2JSON(Mockito.anyString(), Mockito.eq(EnumMetodoHTTP.POST), Mockito.anyMap(), Mockito.anyMap(), Mockito.any(), Mockito.anyString())).thenReturn(responseAtualizacao);

        this.sicliServico.atualizaDadosDossieDigital(65179609L, TipoPessoaEnum.F, new ArrayList<>(), Arrays.asList(cnh00065279609));
    }

    @Test
    public void atualizaDadosDossieDigitalCasoRespostaSucessNovoCadastroSICLI() {
        PowerMockito.when(UtilRest.consumirServicoOAuth2(Mockito.anyString(), Mockito.eq(EnumMetodoHTTP.GET), Mockito.anyMap(), Mockito.anyMap(), Mockito.any(), Mockito.anyString(), Mockito.isNull(String.class), Mockito.anyString())).thenReturn(responseConsultaInexistente);

        responseAtualizacao = new MockResponse("CADASTRO INCLUIDO COM SUCESSO", Response.Status.OK, mockStatusTypeOK);
        PowerMockito.when(UtilRest.consumirServicoOAuth2JSON(Mockito.anyString(), Mockito.eq(EnumMetodoHTTP.POST), Mockito.anyMap(), Mockito.anyMap(), Mockito.any(), Mockito.anyString())).thenReturn(responseAtualizacao);

        this.sicliServico.atualizaDadosDossieDigital(65179609L, TipoPessoaEnum.F, new ArrayList<>(), Arrays.asList(cnh00065279609));
    }

    @Test
    public void atualizaDadosDossieDigitalCasoRuntimeExceptionConversaoAtributo() {
        PowerMockito.when(UtilRest.consumirServicoOAuth2(Mockito.anyString(), Mockito.eq(EnumMetodoHTTP.GET), Mockito.anyMap(), Mockito.anyMap(), Mockito.any(), Mockito.anyString(), Mockito.isNull(String.class), Mockito.anyString())).thenReturn(responseConsultaInexistente);

        responseAtualizacao = new MockResponse("CADASTRO INCLUIDO COM SUCESSO", Response.Status.OK, mockStatusTypeOK);
        PowerMockito.when(UtilRest.consumirServicoOAuth2JSON(Mockito.anyString(), Mockito.eq(EnumMetodoHTTP.POST), Mockito.anyMap(), Mockito.anyMap(), Mockito.any(), Mockito.anyString())).thenReturn(responseAtualizacao);

        AtributoDocumento atributoDocumento = new AtributoDocumento();
        atributoDocumento.setAcertoManual(Boolean.FALSE);
        atributoDocumento.setConteudo("teste");
        atributoDocumento.setDescricao("possui_qrcode");
        atributoDocumento.setId(99L);
        atributoDocumento.setIndiceAssertividade(BigDecimal.valueOf(96L));
        this.cnh00065279609.getAtributosDocumento().add(atributoDocumento);

        this.sicliServico.atualizaDadosDossieDigital(65179609L, TipoPessoaEnum.F, new ArrayList<>(), Arrays.asList(cnh00065279609));
    }
}

class MockResponse extends BuiltResponse {

    private final Status httpStatus;
    private final StatusType statusType;

    public MockResponse(Object entity, Status status, StatusType statusType) {
        this.entity = entity;
        this.httpStatus = status;
        this.statusType = statusType;
    }

    @Override
    public <T> T readEntity(Class<T> type) {
        return (T) entity;
    }

    @Override
    public int getStatus() {
        return this.httpStatus.getStatusCode();
    }
    
    @Override
    public StatusType getStatusInfo() {
        return this.statusType;
    }
}

class MockStatusType implements Response.StatusType {

    private final Response.Status.Family family;
    private final int statusCode;

    public MockStatusType(Response.Status.Family family, int statusCode) {
        this.family = family;
        this.statusCode = statusCode;
    }

    @Override
    public int getStatusCode() {
        return this.statusCode;
    }

    @Override
    public Response.Status.Family getFamily() {
        return this.family;
    }

    @Override
    public String getReasonPhrase() {
        return "MockedReasonPhrase";
    }

}
