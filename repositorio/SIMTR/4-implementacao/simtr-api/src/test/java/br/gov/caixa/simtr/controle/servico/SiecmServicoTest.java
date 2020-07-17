package br.gov.caixa.simtr.controle.servico;

import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMCriaDossieDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMCriaTransacaoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMGravaDocumentoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMRespostaDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMRespostaDefaultDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMRespostaExtracaoDadosDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.GravaDocumentoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.RespostaGravaDocumentoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.RetornoPesquisaDTO;
import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.arquitetura.excecao.ged.GEDException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.GEDService;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.pedesgo.arquitetura.util.UtilParametro;
import br.gov.caixa.pedesgo.arquitetura.util.UtilRest;
import br.gov.caixa.pedesgo.arquitetura.util.UtilUsuario;
import br.gov.caixa.simtr.controle.excecao.SiecmException;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.controle.servico.helper.SiecmObjetosHelper;
import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.CalendarUtil;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.util.KeycloakUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import javax.ejb.EJBException;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UtilParametro.class, UtilRest.class})
public class SiecmServicoTest {

    @Mock
    private GEDService gedService;

    @Mock
    private KeycloakService keycloakService;
    
    @Mock
    private KeycloakUtil keycloakUtil;
    
    @Mock
    private SiecmObjetosHelper siecmObjetosHelper;
    
    @Mock
    private UtilUsuario utilUsuario;

    @Spy
    private CalendarUtil calendarUtil = new CalendarUtil();
    
    @InjectMocks
    private SiecmServico siecmServico;

    private final String PROPRIEDADE_URL_SIECM = "url_siecm_ged";
    
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_DIR = "/mock/siecm/";
    private static final String UTF_8 = "UTF-8";

    Unmarshaller unmarshallerCriaDossie;
    Unmarshaller unmarshallerCriaTransacao;
    Unmarshaller unmarshallerGravaDocumentoPessoal;
    Unmarshaller unmarshallerGravaDocumentoOperacao;
    Unmarshaller unmarshallerDocumento;
    Unmarshaller unmarshallerExtracao;
    Unmarshaller unmarshallerPesquisa;
    Unmarshaller unmarshallerDossie;

    private ECMRespostaExtracaoDadosDTO retornoExtracaoCNH;
    private ECMRespostaExtracaoDadosDTO retornoExtracaoFalha;

    private RetornoPesquisaDTO retornoPesquisaCNH;
    private RetornoPesquisaDTO retornoPesquisaFalha;

    private String xmlRespostaCriaDossieSucesso;
    private String xmlRespostaCriaDossieExistente;
    
    private String xmlRespostaCriaTransacaoSucesso;
    
    private String xmlRespostaGravaDocumentoSucesso;
    private String xmlRespostaGravaDocumentoFalha;

    private ECMCriaDossieDTO requisicaoCriaDossie;
    private ECMCriaTransacaoDTO requisicaoCriaTransacao;
    private ECMGravaDocumentoDTO requisicaoGravaDocumentoPessoal;
    private GravaDocumentoDTO requisicaoGravaDocumentoOperacao;
    
    private RespostaGravaDocumentoDTO respostaGravaDocumentoSucesso;
    private RespostaGravaDocumentoDTO respostaGravaDocumentoFalha;

    private ECMRespostaDefaultDTO retornoAtualizaIndicesDocumentoSucesso;
    private ECMRespostaDefaultDTO retornoAtualizaIndicesDocumentoFalha;

    private ECMRespostaDTO retornoCreateDossieSucesso;
    private ECMRespostaDTO retornoCreateDossieFalha;

    private String cnhBase64;
    private String enderecoBase64;
    private Documento documentoCNH22199586120;
    private Documento documentoEndereco00013888000105;

    private static final String URL_BASE_SIECM = "http://url.siecm";
    private static final String ENDPOINT_CRIA_DOSSIE = "/ECM/reuso/dossie";
    private static final String ENDPOINT_CRIA_TRANSACAO = "/ECM/reuso/dossie/transacao";
    private static final String ENDPOINT_GRAVA_DOCUMENTO_CLIENTE = "/ECM/reuso/dossie/gravaDocumento";
    private static final String ENDPOINT_GRAVA_DOCUMENTO_TRANSACAO = "/ECM/gravaDocumento";
    
    @Before
    public void setup() throws IOException, JAXBException {
        MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(UtilParametro.class, UtilRest.class);
        PowerMockito.when(UtilParametro.getParametro(Mockito.eq(PROPRIEDADE_URL_SIECM), Mockito.anyBoolean())).thenReturn(URL_BASE_SIECM);       
        
        this.initUnmarshallers();

        this.requisicaoCriaDossie = (ECMCriaDossieDTO) unmarshallerCriaDossie.unmarshal(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("requisicao-cria-dossie.xml")));   
        this.requisicaoCriaTransacao = (ECMCriaTransacaoDTO) unmarshallerCriaTransacao.unmarshal(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("requisicao-cria-transacao.xml")));
        this.requisicaoGravaDocumentoPessoal = (ECMGravaDocumentoDTO) unmarshallerGravaDocumentoPessoal.unmarshal(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("requisicao-grava-documento-pessoal.xml")));
        this.requisicaoGravaDocumentoOperacao = (GravaDocumentoDTO) unmarshallerGravaDocumentoOperacao.unmarshal(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("requisicao-grava-documento-operacao.xml")));
        
        this.xmlRespostaCriaDossieSucesso = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("resposta-cria-dossie-sucesso.xml")), UTF_8);
        this.xmlRespostaCriaDossieExistente = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("resposta-cria-dossie-existente.xml")), UTF_8);
        this.xmlRespostaCriaTransacaoSucesso = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("resposta-cria-transacao-sucesso.xml")), UTF_8);
        this.xmlRespostaGravaDocumentoSucesso = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("resposta-grava-documento-sucesso.xml")), UTF_8);
        this.xmlRespostaGravaDocumentoFalha = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("resposta-grava-documento-falha.xml")), UTF_8);

        this.respostaGravaDocumentoSucesso = (RespostaGravaDocumentoDTO) unmarshallerDocumento.unmarshal(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("resposta-grava-documento-sucesso.xml")));
        this.respostaGravaDocumentoFalha = (RespostaGravaDocumentoDTO) unmarshallerDocumento.unmarshal(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("resposta-grava-documento-falha.xml")));

        this.retornoExtracaoCNH = (ECMRespostaExtracaoDadosDTO) unmarshallerExtracao.unmarshal(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("retorno-extracao-cnh.xml")));
        this.retornoExtracaoFalha = (ECMRespostaExtracaoDadosDTO) unmarshallerExtracao.unmarshal(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("retorno-extracao-falha.xml")));

        this.retornoPesquisaCNH = (RetornoPesquisaDTO) unmarshallerPesquisa.unmarshal(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("retorno-pesquisa-cnh.xml")));
        this.retornoPesquisaFalha = (RetornoPesquisaDTO) unmarshallerPesquisa.unmarshal(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("retorno-pesquisa-falha.xml")));

        this.retornoAtualizaIndicesDocumentoSucesso = (ECMRespostaDefaultDTO) unmarshallerExtracao.unmarshal(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("retorno-atualiza-indices-sucesso.xml")));
        this.retornoAtualizaIndicesDocumentoFalha = (ECMRespostaDefaultDTO) unmarshallerExtracao.unmarshal(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("retorno-atualiza-indices-falha.xml")));

        this.retornoCreateDossieSucesso = (ECMRespostaDTO) unmarshallerDossie.unmarshal(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("retorno-create-dossie-sucesso.xml")));
        this.retornoCreateDossieFalha = (ECMRespostaDTO) unmarshallerDossie.unmarshal(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("retorno-create-dossie-falha.xml")));

        this.cnhBase64 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("cnh-base64.txt")), UTF_8);
        this.enderecoBase64 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("endereco-base64.txt")), UTF_8);

        String jsonCNH22199586120 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("cnh-22199586120.json")), UTF_8);
        this.documentoCNH22199586120 = mapper.readValue(jsonCNH22199586120, Documento.class);

        String jsonEndereco00013888000105 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("endereco-00013888000105.json")), UTF_8);
        this.documentoEndereco00013888000105 = mapper.readValue(jsonEndereco00013888000105, Documento.class);

        Mockito.when(this.siecmObjetosHelper.montaObjetoDossie(Mockito.anyLong(),Mockito.any(TipoPessoaEnum.class), Mockito.anyString())).thenReturn(this.requisicaoCriaDossie);
        Mockito.when(this.siecmObjetosHelper.montaObjetoTransacao(Mockito.anyString(), Mockito.anyString())).thenReturn(this.requisicaoCriaTransacao);
        Mockito.when(this.siecmObjetosHelper.montaObjetoDocumentoCliente(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.any(Documento.class), Mockito.anyString(), Mockito.anyString())).thenReturn(this.requisicaoGravaDocumentoPessoal);
        Mockito.when(this.siecmObjetosHelper.montaObjetoOperacao(Mockito.any(Documento.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(this.requisicaoGravaDocumentoOperacao);
        Mockito.when(this.keycloakService.getMatricula()).thenReturn("c999999");
        Mockito.when(this.utilUsuario.getIpUsuario()).thenReturn("127.0.0.1");
        Mockito.when(this.keycloakUtil.getTokenServico()).thenReturn("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJNRmVKNjVfRC14cU55M1Zta01Ib01WS1NjZlA3S21ZazdtVjBJaEsta0F3In0.eyJqdGkiOiI4MDM3YTBmNi0yZmMzLTQzOTgtYmU3Zi05MzMyNzJmNjZmY2EiLCJleHAiOjE1ODY2NTM2NDYsIm5iZiI6MCwiaWF0IjoxNTg2NjUzMzQ2LCJpc3MiOiJodHRwczovL2xvZ2luLmRlcy5jYWl4YS9hdXRoL3JlYWxtcy9pbnRyYW5ldCIsInN1YiI6ImM2NGQ0N2RmLTZiZGUtNDA3ZC1hZDhhLWM1MmNmYjY0MzJmYyIsInR5cCI6IkJlYXJlciIsImF6cCI6ImNsaS1zZXItbXRyIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiYjFkMGIzOTYtNmUxMC00ZDFmLThhMGMtZmNmMGJkODgyMjczIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJTRVRfU0VSVklDTyIsIlNFVF9UT01CQU1FTlRPIl19LCJzY29wZSI6IiIsImNsaWVudEhvc3QiOiIxMC4xOTMuODIuMTY4IiwiY2xpZW50SWQiOiJjbGktc2VyLW10ciIsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC1jbGktc2VyLW10ciIsImNsaWVudEFkZHJlc3MiOiIxMC4xOTMuODIuMTY4IiwiZW1haWwiOiJzZXJ2aWNlLWFjY291bnQtY2xpLXNlci1tdHJAcGxhY2Vob2xkZXIub3JnIn0.AhBIU8A1h-bwtM5iZFVwDYLUJgRR3T-NQnx1REg-6WhMCf2r1tyHtnmYijXu9qbIStHInWW0egpmeKdtbgXGP4cl_O3khtwP9V1JMtQ_jwfqIj6FElKnEcKBwQl40lK6UYBysfkFsfWiopWZZCwWiNtksQFrvsS2VL5ksu_FvNk8whd2d-gX8tyFdA9opyuFaNkYsSKH30L-KzPpCBR2UmdejxhWyKq6uJLvGZ6vru2vxxWUsYh9pVv0_F2QOlFuuj3m6oP_teDi4jWD3BhyZyIN1G0WdZ-QnMnr18GHRH8rhJyoAD0o4Aap0pbExkBvLLnpIP7W9VIQ3tV3VkfFVw");
    }

    private void initUnmarshallers() throws JAXBException {
        if (this.unmarshallerDocumento == null && this.unmarshallerExtracao == null && this.unmarshallerPesquisa == null && this.unmarshallerDossie == null) {
            JAXBContext jcCriaDossie = JAXBContext.newInstance(ECMCriaDossieDTO.class);
            JAXBContext jcCriaTransacao = JAXBContext.newInstance(ECMCriaTransacaoDTO.class);
            JAXBContext jcDocumento = JAXBContext.newInstance(RespostaGravaDocumentoDTO.class);
            JAXBContext jcExtracao = JAXBContext.newInstance(ECMRespostaExtracaoDadosDTO.class);
            JAXBContext jcGravaDocumentoPessoal = JAXBContext.newInstance(ECMGravaDocumentoDTO.class);
            JAXBContext jcGravaDocumentoOperacao = JAXBContext.newInstance(GravaDocumentoDTO.class);
            JAXBContext jcPesquisa = JAXBContext.newInstance(RetornoPesquisaDTO.class);
            JAXBContext jcDossie = JAXBContext.newInstance(ECMRespostaDTO.class);

            this.unmarshallerCriaDossie = jcCriaDossie.createUnmarshaller();
            this.unmarshallerCriaTransacao = jcCriaTransacao.createUnmarshaller();
            this.unmarshallerDocumento = jcDocumento.createUnmarshaller();
            this.unmarshallerExtracao = jcExtracao.createUnmarshaller();
            this.unmarshallerGravaDocumentoPessoal = jcGravaDocumentoPessoal.createUnmarshaller();
            this.unmarshallerGravaDocumentoOperacao = jcGravaDocumentoOperacao.createUnmarshaller();
            this.unmarshallerPesquisa = jcPesquisa.createUnmarshaller();
            this.unmarshallerDossie = jcDossie.createUnmarshaller();
        }
    }

    @Test
    public void testExecutaExtracaoDadosDocumento() {
        Mockito.when(this.gedService.extractInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(this.retornoExtracaoCNH);
        this.siecmServico.executaExtracaoDadosDocumento(this.documentoCNH22199586120.getTipoDocumento(), FormatoConteudoEnum.JPG, this.cnhBase64);
    }

    @Test(expected = SiecmException.class)
    public void testExecutaExtracaoDadosDocumentoRetornoNull() {
        Mockito.when(this.gedService.extractInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        this.siecmServico.executaExtracaoDadosDocumento(this.documentoCNH22199586120.getTipoDocumento(), FormatoConteudoEnum.JPG, this.cnhBase64);
    }

    @Test(expected = SiecmException.class)
    public void testExecutaExtracaoDadosDocumentoRetornoFalhaExtracao() {
        Mockito.when(this.gedService.extractInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(this.retornoExtracaoFalha);
        this.siecmServico.executaExtracaoDadosDocumento(this.documentoCNH22199586120.getTipoDocumento(), FormatoConteudoEnum.JPG, this.cnhBase64);
    }

    @Test
    public void testArmazenaDocumentoPessoalGED() {
        //Teste simulando pasta de documentos do cliente não existente no GED
        String urlDossie = URL_BASE_SIECM.concat(ENDPOINT_CRIA_DOSSIE);
        Response responseDossieSucesso = Response.ok(this.xmlRespostaCriaDossieSucesso).build();
        responseDossieSucesso.bufferEntity();
        PowerMockito.when(UtilRest.consumirServicoOAuth2(Mockito.eq(urlDossie), Mockito.eq(EnumMetodoHTTP.PUT), Mockito.anyMap(), Mockito.anyMap(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(responseDossieSucesso);
        
        String urlDocumento = URL_BASE_SIECM.concat(ENDPOINT_GRAVA_DOCUMENTO_CLIENTE);
        Response responseDocumento = Response.ok(this.xmlRespostaGravaDocumentoSucesso).build();
        responseDocumento.bufferEntity();
        PowerMockito.when(UtilRest.consumirServicoOAuth2(Mockito.eq(urlDocumento), Mockito.eq(EnumMetodoHTTP.PUT), Mockito.anyMap(), Mockito.anyMap(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(responseDocumento);
        this.siecmServico.armazenaDocumentoPessoalSIECM(22199586120L, TipoPessoaEnum.F, this.documentoCNH22199586120, TemporalidadeDocumentoEnum.TEMPORARIO_OCR, this.cnhBase64, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);

        //Teste simulando pasta de documentos do cliente pré existente no GED
        Response responseDossieExistente = Response.ok(this.xmlRespostaCriaDossieExistente).build();
        responseDossieExistente.bufferEntity();
        PowerMockito.when(UtilRest.consumirServicoOAuth2(Mockito.eq(urlDossie), Mockito.eq(EnumMetodoHTTP.PUT), Mockito.anyMap(), Mockito.anyMap(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(responseDossieExistente);
        this.siecmServico.armazenaDocumentoPessoalSIECM(13888000105L, TipoPessoaEnum.J, this.documentoEndereco00013888000105, TemporalidadeDocumentoEnum.TEMPORARIO_OCR, this.enderecoBase64, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
    }

    @Test(expected = SiecmException.class)
    public void testArmazenaDocumentoPessoalGEDRetornoNull() {
        Response response = Response.ok().build();
        PowerMockito.when(UtilRest.consumirServicoOAuth2(Mockito.anyString(), Mockito.eq(EnumMetodoHTTP.PUT), Mockito.anyMap(), Mockito.anyMap(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(response);             
        Mockito.when(this.gedService.createDossie(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString())).thenReturn(this.retornoCreateDossieSucesso);
        
        this.siecmServico.armazenaDocumentoPessoalSIECM(22199586120L, TipoPessoaEnum.F, this.documentoCNH22199586120, TemporalidadeDocumentoEnum.TEMPORARIO_OCR, this.cnhBase64, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
    }

    @Test(expected = SiecmException.class)
    public void testArmazenaDocumentoPessoalGEDRetornoFalhaArmazenamento() {
        //Teste simulando pasta de documentos do cliente não existente no GED
        String urlDossie = URL_BASE_SIECM.concat(ENDPOINT_CRIA_DOSSIE);
        Response responseDossieSucesso = Response.ok(this.xmlRespostaCriaDossieSucesso).build();
        responseDossieSucesso.bufferEntity();
        PowerMockito.when(UtilRest.consumirServicoOAuth2(Mockito.eq(urlDossie), Mockito.eq(EnumMetodoHTTP.PUT), Mockito.anyMap(), Mockito.anyMap(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(responseDossieSucesso);
        
        String urlDocumento = URL_BASE_SIECM.concat(ENDPOINT_GRAVA_DOCUMENTO_CLIENTE);
        Response response = Response.ok(this.xmlRespostaGravaDocumentoFalha).build();
        response.bufferEntity();
        PowerMockito.when(UtilRest.consumirServicoOAuth2(Mockito.eq(urlDocumento), Mockito.eq(EnumMetodoHTTP.PUT), Mockito.anyMap(), Mockito.anyMap(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(response);
        Mockito.when(this.gedService.createDossie(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString())).thenReturn(this.retornoCreateDossieSucesso);

        this.siecmServico.armazenaDocumentoPessoalSIECM(22199586120L, TipoPessoaEnum.F, this.documentoCNH22199586120, TemporalidadeDocumentoEnum.TEMPORARIO_OCR, this.cnhBase64, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
    }

    @Test(expected = SiecmException.class)
    public void testArmazenaDocumentoPessoalGEDFalhaCriacaoDossie() {
        String urlDossie = URL_BASE_SIECM.concat(ENDPOINT_CRIA_DOSSIE);
        Response responseDossieSucesso = Response.ok(this.xmlRespostaCriaDossieSucesso).build();
        responseDossieSucesso.bufferEntity();
        PowerMockito.when(UtilRest.consumirServicoOAuth2(Mockito.eq(urlDossie), Mockito.eq(EnumMetodoHTTP.PUT), Mockito.anyMap(), Mockito.anyMap(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(responseDossieSucesso);
        
        String url = URL_BASE_SIECM.concat(ENDPOINT_GRAVA_DOCUMENTO_CLIENTE);
        Response response = Response.ok().build();
        response.bufferEntity();
        PowerMockito.when(UtilRest.consumirServicoOAuth2(Mockito.eq(url), Mockito.eq(EnumMetodoHTTP.PUT), Mockito.anyMap(), Mockito.anyMap(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(response);
//        Mockito.doThrow(new EJBException(new GEDException("Falha interna mockada para o SIECM. Utilização em testes únitarios."))).when(this.gedService).createDossie(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        this.siecmServico.armazenaDocumentoPessoalSIECM(22199586120L, TipoPessoaEnum.F, this.documentoCNH22199586120, TemporalidadeDocumentoEnum.TEMPORARIO_OCR, this.cnhBase64, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);       
    }

    @Test(expected = SiecmException.class)
    public void testArmazenaDocumentoPessoalGEDFalhaArmazenamento() {
        Response response = Response.serverError().build();
        response.bufferEntity();
        PowerMockito.when(UtilRest.consumirServicoOAuth2(Mockito.anyString(), Mockito.eq(EnumMetodoHTTP.PUT), Mockito.anyMap(), Mockito.anyMap(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(response);
        Mockito.when(this.gedService.createDossie(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString())).thenReturn(this.retornoCreateDossieSucesso);

        //Espera-se que seja lançada uma exceção do tipo SiecmException por falha interna do SIECM
        this.siecmServico.armazenaDocumentoPessoalSIECM(22199586120L, TipoPessoaEnum.F, this.documentoCNH22199586120, TemporalidadeDocumentoEnum.TEMPORARIO_OCR, this.cnhBase64, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
    }

    @Test
    public void testObtemLinkAcesso() {
        Mockito.when(this.gedService.searchDocument(Mockito.anyString(), Mockito.anyString())).thenReturn(this.retornoPesquisaCNH);
        this.siecmServico.obtemLinkAcessoDocumentoSIECM("20879769-0000-C288-82D9-2EB6DE50A892", ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
    }

    @Test(expected = SiecmException.class)
    public void testObtemLinkAcessoRetornoNull() {
        Mockito.when(this.gedService.searchDocument(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        this.siecmServico.obtemLinkAcessoDocumentoSIECM("20879769-0000-C288-82D9-2EB6DE50A892", ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
    }

    @Test(expected = SiecmException.class)
    public void testObtemLinkAcessoRetornoFalhaExtracao() {
        Mockito.when(this.gedService.searchDocument(Mockito.anyString(), Mockito.anyString())).thenReturn(this.retornoPesquisaFalha);
        this.siecmServico.obtemLinkAcessoDocumentoSIECM("20879769-0000-C288-82D9-2EB6DE50A892", ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
    }

    @Test
    public void testAlteraSituacaoTemporalidadeDocumentoGED() {
        Mockito.when(this.gedService.updateInfoDocument(Mockito.anyString(), Mockito.anyList(), Mockito.anyString())).thenReturn(this.retornoAtualizaIndicesDocumentoSucesso);
        this.siecmServico.alteraSituacaoTemporalidadeDocumentoSIECM("20879769-0000-C288-82D9-2EB6DE50A892", TemporalidadeDocumentoEnum.VALIDO, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
    }
    
    @Test(expected = SiecmException.class)
    public void testAlteraSituacaoTemporalidadeDocumentoGEDRetornoNull() {
        Mockito.when(this.gedService.updateInfoDocument(Mockito.anyString(), Mockito.anyList(), Mockito.anyString())).thenReturn(null);
        this.siecmServico.alteraSituacaoTemporalidadeDocumentoSIECM("20879769-0000-C288-82D9-2EB6DE50A892", TemporalidadeDocumentoEnum.VALIDO, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
    }
    
    @Test(expected = SiecmException.class)
    public void testAlteraSituacaoTemporalidadeDocumentoGEDRetornoFalhaAtualizacao() {
        Mockito.when(this.gedService.updateInfoDocument(Mockito.anyString(), Mockito.anyList(), Mockito.anyString())).thenReturn(this.retornoAtualizaIndicesDocumentoFalha);
        this.siecmServico.alteraSituacaoTemporalidadeDocumentoSIECM("20879769-0000-C288-82D9-2EB6DE50A892", TemporalidadeDocumentoEnum.VALIDO, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
    }
}
