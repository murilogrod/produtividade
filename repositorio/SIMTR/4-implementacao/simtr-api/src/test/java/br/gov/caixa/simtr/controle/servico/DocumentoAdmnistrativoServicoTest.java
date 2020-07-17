package br.gov.caixa.simtr.controle.servico;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.AtributosDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.DadosDocumentoLocalizadoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.DocumentoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.RespostaGravaDocumentoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.RetornoPesquisaDTO;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.GEDService;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.pedesgo.arquitetura.util.UtilWS;
import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.ApensoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.Conteudo;
import br.gov.caixa.simtr.modelo.entidade.ContratoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DocumentoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.ProcessoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.SICPFCampoEnum;
import br.gov.caixa.simtr.modelo.enumerator.SICPFModoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.util.CalendarUtil;
import br.gov.caixa.simtr.util.KeycloakUtil;

/**
 *
 * @author Igor Furtado - Fóton
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({UtilWS.class, Base64.class})
public class DocumentoAdmnistrativoServicoTest {

    /**
     * Atributo manager.
     */
    @Mock
    private EntityManager manager;

    /**
     * Atributo query.
     */
    @Mock
    private TypedQuery<DocumentoAdministrativo> queryDocumento;

    @Mock
    private ApensoAdministrativoServico apensoAdministrativoServico;

    @Mock
    private CalendarUtil calendarUtil;

    @Mock
    private CanalServico canalServico;

    @Mock
    private ContratoAdministrativoServico contratoAdministrativoServico;

    @Mock
    private DocumentoServico documentoServico;

    @Mock
    private GEDService gedService;

    @Mock
    private ProcessoAdministrativoServico processoAdministrativoServico;

    @Mock
    private TipoDocumentoServico tipoDocumentoServico;

    @Mock
    private KeycloakService keycloackService;

    @Mock
    private KeycloakUtil keycloakUtil;

    @Mock
    private Logger logger;
    
    @Mock
    private SessionContext sessionContext;
    
    @Mock
    private SiecmServico siecmServico;

    @InjectMocks
    private DocumentoAdministrativoServico documentoAdministrativoServico = new DocumentoAdministrativoServico();

    private ProcessoAdministrativo processoAdministrativo;

    DocumentoAdministrativo docAdministrativo;

    private Documento doc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(keycloakUtil.getIpFromToken()).thenReturn("127.0.0.1");
        Mockito.when(sessionContext.isCallerInRole(Mockito.anyString())).thenReturn(Boolean.TRUE);
        
        Mockito.when(this.siecmServico.armazenaDocumentoOperacaoSIECM(Mockito.any(Documento.class), Mockito.any(TemporalidadeDocumentoEnum.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyList())).thenReturn("F002D46C-0000-C566-840E-D3ECDCF2D2D5");
    }

    @Test
    public void getDocumentoById() {
        mockDocAdmnistrativo();
        mockCreateQuery();
        mockSingleResult();
        mockGedSearchDocument();
        mokarUtilsWsObterBytes();
        documentoAdministrativoServico.getDocumentoById(1L, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
    }

    @Test
    public void getDocumentoByIdError() {
        mockDocAdmnistrativo();
        mockCreateQuery();
        mockGedSearchDocument();
        mockSingleResultNRE();
        documentoAdministrativoServico.getDocumentoById(1L, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
    }

    @Test
    public void testListByDescricao() {
        mockCreateQuery();
        mockResultList();
        Assert.assertNotNull(documentoAdministrativoServico.listByDescricao(""));
    }

    @Test
    public void testListByDescricaoNRE() {
        mockCreateQuery();
        mockResultListNRE();
        Assert.assertNotNull(documentoAdministrativoServico.listByDescricao(""));
    }

    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void insereDocumentoProcessoAdministrativoProcessoNull() {
        mockaProcessoAdministrativo();
        documentoAdministrativoServico.insereDocumentoProcessoAdministrativo(1L, doc, 1L, "", Boolean.TRUE, Boolean.TRUE, "");
    }

    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void insereDocumentoApensoAdministrativoNull() {
        documentoAdministrativoServico.insereDocumentoApensoAdministrativo(1L, doc, Boolean.TRUE, Boolean.TRUE, "descricao", 1L, null);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void insereDocumentoApensoAdministrativoDocNull() {
        mockApensoAdmnistrativo();
        documentoAdministrativoServico.insereDocumentoApensoAdministrativo(1L, doc, Boolean.TRUE, Boolean.TRUE, "descricao", 1L, null);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void insereDocumentoApensoAdministrativoDocValido() {
        mockApensoAdmnistrativo();
        mockDocAdmnistrativo();
        documentoAdministrativoServico.insereDocumentoApensoAdministrativo(1L, doc, Boolean.TRUE, Boolean.TRUE, "descricao", 1L, null);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void insereDocumentoApensoAdministrativoTipoDocInvalido() {
        mockApensoAdmnistrativo();
        mockDocAdmnistrativo();
        doc.setTipoDocumento(null);
        documentoAdministrativoServico.insereDocumentoApensoAdministrativo(1L, doc, Boolean.TRUE, Boolean.TRUE, "descricao", 1L, null);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void insereDocumentoApensoAdministrativoAtributosDocumentoNull() {
        mockApensoAdmnistrativo();
        mockDocAdmnistrativo();
        doc.setTipoDocumento(null);
        doc.setAtributosDocumento(null);
        documentoAdministrativoServico.insereDocumentoApensoAdministrativo(1L, doc, Boolean.TRUE, Boolean.TRUE, "descricao", 1L, null);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void insereDocumentoApensoAdministrativo() {
        mockApensoAdmnistrativo();
        mockDocAdmnistrativo();
        documentoAdministrativoServico.insereDocumentoApensoAdministrativo(1L, doc, Boolean.TRUE, Boolean.TRUE, "descricao", 1L, null);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void insereDocumentoApensoAdministrativoApenso() {
        mockApensoAdmnistrativo();
        mockDocAdmnistrativo();
        doc.setAtributosDocumento(null);
        documentoAdministrativoServico.insereDocumentoApensoAdministrativo(1L, doc, Boolean.TRUE, Boolean.TRUE, "descricao", 1L, null);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void insereDocumentoProcessoAdministrativoComTipoDocumento() {
        this.processoAdministrativo = new ProcessoAdministrativo();
        mockaProcessoAdministrativo();
        mockDocAdmnistrativo();
        documentoAdministrativoServico.insereDocumentoProcessoAdministrativo(1L, doc, 1L, "", Boolean.TRUE, Boolean.TRUE, "");
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void insereDocumentoProcessoAdministrativoSemAtributosDocumento() {
        this.processoAdministrativo = new ProcessoAdministrativo();
        mockaProcessoAdministrativo();
        mockDocAdmnistrativo();
        doc.setTipoDocumento(new TipoDocumento());
        doc.setAtributosDocumento(null);
        documentoAdministrativoServico.insereDocumentoProcessoAdministrativo(1L, doc, 1L, "", Boolean.TRUE, Boolean.TRUE, "");
    }

    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void insereDocumentoContratoAdministrativoTest() {
        mockaProcessoAdministrativo();
        documentoAdministrativoServico.insereDocumentoContratoAdministrativo(1L, doc, 1L, Boolean.TRUE, Boolean.TRUE, "descricao");
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void insereDocumentoContratoAdministrativoTestProcesso() {
        mockContratoAdministrativoServico();
        documentoAdministrativoServico.insereDocumentoContratoAdministrativo(1L, doc, 1L, Boolean.TRUE, Boolean.TRUE, "descricao");
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void insereDocumentoContratoAdministrativoTesteDoc() {
        mockaProcessoAdministrativo();
        mockContratoAdministrativoServico();
        documentoAdministrativoServico.insereDocumentoContratoAdministrativo(1L, new Documento(), 1L, Boolean.TRUE, Boolean.TRUE, "descricao");
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void insereDocumentoProcessoAdministrativoComAtributosDocumento() {
        this.processoAdministrativo = new ProcessoAdministrativo();
        mockaProcessoAdministrativo();
        mockDocAdmnistrativo();
        doc.setTipoDocumento(new TipoDocumento());
        doc.setAtributosDocumento(Collections.emptySet());
        documentoAdministrativoServico.insereDocumentoProcessoAdministrativo(1L, doc, 1L, "", Boolean.TRUE, Boolean.TRUE, "");
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void insereDocumentoContratoAdministrativoTipoDoc() {
        this.processoAdministrativo = new ProcessoAdministrativo();
        mockaProcessoAdministrativo();
        mockDocAdmnistrativo();
        doc.setTipoDocumento(new TipoDocumento());
        doc.setAtributosDocumento(null);
        mockContratoAdministrativoServico();
        documentoAdministrativoServico.insereDocumentoContratoAdministrativo(1L, doc, 1L, Boolean.TRUE, Boolean.TRUE, "descricao");
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void insereDocumentoContratoAdministrativoAtributos() {
        mockaProcessoAdministrativo();
        mockContratoAdministrativoServico();
        mockDocAdmnistrativo();
        doc.setAtributosDocumento(new HashSet<>());
        documentoAdministrativoServico.insereDocumentoContratoAdministrativo(1L, doc, 1L, Boolean.TRUE, Boolean.TRUE, "descricao");
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void insereDocumentoProcessoAdministrativoSemTipoDoc() {
        this.processoAdministrativo = new ProcessoAdministrativo();
        mockaProcessoAdministrativo();
        mockDocAdmnistrativo();
        doc.setTipoDocumento(null);
        doc.setAtributosDocumento(Collections.emptySet());
        documentoAdministrativoServico.insereDocumentoProcessoAdministrativo(1L, doc, 1L, "", Boolean.TRUE, Boolean.TRUE, "");
    }

    @Test
    public void insereDocumentoProcessoAdministrativoComCanal() {
        this.processoAdministrativo = new ProcessoAdministrativo();
        mockaProcessoAdministrativo();
        mockDocAdmnistrativo();
        doc.setCanalCaptura(new Canal());
        this.processoAdministrativo.setNumeroPregao(12312322);
        this.processoAdministrativo.setAnoProcesso(2020);
        mockFind();
        documentoAdministrativoServico.insereDocumentoProcessoAdministrativo(1L, doc, 1L, "", Boolean.TRUE, Boolean.TRUE, "");
    }

    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void testeAplicaPatchDocumento() {
        mockaBuscaCanalServico();
        documentoAdministrativoServico.aplicaPatch(1L, Boolean.TRUE, Boolean.TRUE, "", "", OrigemDocumentoEnum.A, 1, null);
    }

    @Test(expected = SimtrEstadoImpeditivoException.class)
    public void testeAplicaPatchDocumentoNull() {
        mockaBuscaCanalServico();
        mockDocAdmnistrativo();
        docAdministrativo.setDocumentoSubstituto(null);
        mockFind();
        mockaBuscaCanalServico();
        documentoAdministrativoServico.aplicaPatch(1L,  Boolean.TRUE, Boolean.TRUE, "", "", OrigemDocumentoEnum.A, 1, null);
    }

    @Test(expected = SimtrEstadoImpeditivoException.class)
    public void testeAplicaPatchDtHoraExclusao() {
        mockaBuscaCanalServico();
        mockDocAdmnistrativo();
        docAdministrativo.setDocumentoSubstituto(null);
        mockFind();
        mockaBuscaCanalServico();
        documentoAdministrativoServico.aplicaPatch(1L, Boolean.TRUE, Boolean.TRUE, "", "", OrigemDocumentoEnum.A, 1, null);
    }

    @Test(expected = SimtrEstadoImpeditivoException.class)
    public void testeAplicaPatchDocSubstituto() {
        mockaBuscaCanalServico();
        mockDocAdmnistrativo();
        mockFind();
        mockaBuscaCanalServico();
        documentoAdministrativoServico.aplicaPatch(1L, Boolean.TRUE, Boolean.TRUE, "", "", OrigemDocumentoEnum.A, 1, null);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void testeAplicaPatchDtHoraExclusaoNull() {
        mockaBuscaCanalServico();
        mockDocAdmnistrativo();
        docAdministrativo.setDocumentoSubstituto(null);
        docAdministrativo.setDataHoraExclusao(null);
        mockFind();
        mockaBuscaCanalServico();
        documentoAdministrativoServico.aplicaPatch(1L, Boolean.TRUE, Boolean.TRUE, "", "", OrigemDocumentoEnum.A, 1, null);
    }

    @Test
    public void testeAplicaPatchTipoDocumento() {
        mockaBuscaCanalServico();
        mockDocAdmnistrativo();
        docAdministrativo.setDocumentoSubstituto(null);
        docAdministrativo.setDataHoraExclusao(null);
        mockFind();
        mockaBuscaCanalServico();
        mockTipoDocumento();
        Map<String, String> atributosDocumento = new HashMap<String, String>();
        atributosDocumento.put("key", "value");
        documentoAdministrativoServico.aplicaPatch(1L, Boolean.TRUE, Boolean.TRUE, "", "", OrigemDocumentoEnum.A, 1, atributosDocumento);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void testeDeleteDocumentoAdministrativoJustificativaNull() {
        documentoAdministrativoServico.deleteDocumentoAdministrativo(1L, null);
    }

    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void testeDeleteDocumentoAdministrativoDocumentoNull() {
        Mockito.when(this.processoAdministrativoServico.getById(Mockito.anyLong())).thenReturn(null);
        documentoAdministrativoServico.deleteDocumentoAdministrativo(1L, "122333");
    }

    @Test
    public void testeDeleteDocumentoAdministrativoDocumento() {
        mockDocAdmnistrativo();
        mockFind();
        this.processoAdministrativo = new ProcessoAdministrativo();
        mockProcessoAdmnistrativoById();
        documentoAdministrativoServico.deleteDocumentoAdministrativo(1L, "122333");
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void testeInserirDocumentoProcessoAdministrativoNull() {
        this.processoAdministrativo = new ProcessoAdministrativo();
        doc = null;
        mockaBuscaCanalServico();
        mockaProcessoAdministrativo();
        documentoAdministrativoServico.insereDocumentoProcessoAdministrativo(1L, doc, 1L, "", Boolean.TRUE, Boolean.TRUE, "");
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void testeInserirDocumentoProcessoAdministrativoCanalCapturaNull() {
        this.processoAdministrativo = new ProcessoAdministrativo();
        mockaBuscaCanalServico();
        mockaProcessoAdministrativo();
        documentoAdministrativoServico.insereDocumentoProcessoAdministrativo(1L, doc, 1L, "", Boolean.TRUE, Boolean.TRUE, "");
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void validaMineTypeConteudoTest() {
        this.documentoAdministrativoServico.validaMimetypeConteudo(FormatoConteudoEnum.PDF, "base64");
    }

    private void mockFind() {
        Mockito.when(manager.find(DocumentoAdministrativo.class, 1L)).thenReturn(docAdministrativo);
    }



    private void mockResultList() {
        Mockito.when(queryDocumento.getResultList()).thenReturn(new ArrayList<>());
    }

    private void mockResultListNRE() {
        Mockito.doThrow(new NoResultException()).when(queryDocumento).getResultList();
    }

    private void mockSingleResultNRE() {
        Mockito.doThrow(new NoResultException()).when(queryDocumento).getSingleResult();
    }

    private void mockGedSearchDocument() {
        Mockito.when(gedService.searchDocument(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(mockDocAdmnistrativo());
    }

    private void mockSingleResult() {
        Mockito.when(queryDocumento.getSingleResult()).thenReturn(docAdministrativo);
    }

    private void mockCreateQuery() {
        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(DocumentoAdministrativo.class))).thenReturn(queryDocumento);
    }

    private void mockProcessoAdmnistrativoById() {
        Mockito.when(this.processoAdministrativoServico.getById(1L, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE)).thenReturn(processoAdministrativo);
    }

    private RetornoPesquisaDTO mockDocAdmnistrativo() {
        docAdministrativo = new DocumentoAdministrativo();
        this.doc = new Documento();
        docAdministrativo.setDataHoraExclusao(Calendar.getInstance());
        docAdministrativo.setDocumentoSubstituto(new DocumentoAdministrativo());
        doc.setAtributosDocumento(new HashSet<>());
        doc.setFormatoConteudoEnum(FormatoConteudoEnum.BASE64);
        carregarAtributosDocumento();

        doc.setCodigoGED("11111-111111-1111111");
        doc.setConteudos(new HashSet<>());
        mockCarregarAtributosDoc();
        Conteudo cont = new Conteudo();
        cont.setBase64("base64...");
        doc.getConteudos().add(cont);
        docAdministrativo.setDocumento(doc);
        RetornoPesquisaDTO retornoGED = new RetornoPesquisaDTO();
        List<DadosDocumentoLocalizadoDTO> colecaoDados = new ArrayList<>();
        DadosDocumentoLocalizadoDTO dados = new DadosDocumentoLocalizadoDTO();
        dados.setLink("http://siecm.des.caixa/siecm-web/ECM/getDocumento/false/6fd2d97f6a7e1f7e51c2ef03240fb5f2b4923f8b06e5acfb3a44a3678b0037dbbe4d317017ec5b8410aab9347f8ae0676eae30409ace37e9d1cc0704c10c5d789283bd96250f517ce9f0ac6c919b683036ed225ded44a3ec609ded34d01de7dd95/DADOS_DECLARADOS");
        colecaoDados.add(dados);
        dados = new DadosDocumentoLocalizadoDTO();
        dados.setLink("http://siecm.des.caixa/siecm-web/ECM/getDocumento/false/6fd2d97f6a7e1f7e51c2ef03240fb5f2b4923f8b06e5acfb3a44a3678b0037dbbe4d317017ec5b8410aab9347f8ae0676eae30409ace37e9d1cc0704c10c5d789283bd96250f517ce9f0ac6c919b683036ed225ded44a3ec609ded34d01de7dd95/DADOS_DECLARADOS");
        colecaoDados.add(dados);
        retornoGED.setDadosDocumentoLocalizados(colecaoDados);
        List<String> conteudos = new ArrayList<>();
        conteudos.add("1");
        conteudos.add("2");

        docAdministrativo.setConfidencial(Boolean.TRUE);
        docAdministrativo.setValido(Boolean.TRUE);
        docAdministrativo.setDescricao("DESC");
        docAdministrativo.setProcessoAdministrativo(processoAdministrativo);
        docAdministrativo.setContratoAdministrativo(null);
        docAdministrativo.setApensoAdministrativo(null);
        docAdministrativo.setDocumento(doc);

        RespostaGravaDocumentoDTO respostaGrava = new RespostaGravaDocumentoDTO();
        DocumentoDTO docDTO = new DocumentoDTO();
        AtributosDTO atributo = new AtributosDTO();
        atributo.setId("111-1111");
        docDTO.setAtributos(atributo);
        respostaGrava.setDocumentos(new ArrayList<>());
        respostaGrava.getDocumentos().add(docDTO);

        return retornoGED;
    }

    private void carregarAtributosDocumento() {
        cpfValido();

        eleitorValido();

        nomeMaeValido();

        nascimentoValido();

        nomeContribuinteValido();

    }

    private void nomeContribuinteValido() {
        AtributoDocumento atributoDoc;
        atributoDoc = new AtributoDocumento();
        atributoDoc.setId(5L);
        atributoDoc.setConteudo("NomeContribuinte");
        atributoDoc.setDescricao(SICPFCampoEnum.NOME.getDescricao());
        doc.getAtributosDocumento().add(atributoDoc);
    }

    private void nascimentoValido() {
        AtributoDocumento atributoDoc;
        atributoDoc = new AtributoDocumento();
        atributoDoc.setId(4L);
        atributoDoc.setConteudo(Calendar.getInstance().toString());
        atributoDoc.setDescricao(SICPFCampoEnum.NASCIMENTO.getDescricao());
        doc.getAtributosDocumento().add(atributoDoc);
    }

    private void nomeMaeValido() {
        AtributoDocumento atributoDoc;
        atributoDoc = new AtributoDocumento();
        atributoDoc.setId(3L);
        atributoDoc.setConteudo("NomeMae");
        atributoDoc.setDescricao(SICPFCampoEnum.NOME_MAE.getDescricao());
        doc.getAtributosDocumento().add(atributoDoc);
    }

    private void eleitorValido() {
        AtributoDocumento atributoDoc;
        atributoDoc = new AtributoDocumento();
        atributoDoc.setId(2L);
        atributoDoc.setConteudo("123");
        atributoDoc.setDescricao(SICPFCampoEnum.ELEITOR.getDescricao());
        doc.getAtributosDocumento().add(atributoDoc);
    }

    private void cpfValido() {
        AtributoDocumento atributoDoc = new AtributoDocumento();
        atributoDoc.setId(1L);
        atributoDoc.setConteudo("72772450163");
        atributoDoc.setDescricao(SICPFCampoEnum.CPF.getDescricao());
        doc.getAtributosDocumento().add(atributoDoc);
    }

    private void mokarUtilsWsObterBytes() {
        byte[] bytes = "Link".getBytes();
        PowerMockito.mockStatic(UtilWS.class);
        PowerMockito.when(UtilWS.obterBytes(Matchers.anyString())).thenReturn(bytes);
    }

    private void mockaBuscaCanalServico() {
        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(new Canal());
    }

    private void mockaProcessoAdministrativo() {
        Mockito.when(this.processoAdministrativoServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(this.processoAdministrativo);
    }

    private void mockCarregarAtributosDoc() {
        doc.setTipoDocumento(new TipoDocumento());
        doc.getTipoDocumento().setAtributosExtracao(new HashSet<>());
        AtributoExtracao atributo = new AtributoExtracao();
        atributo.setId(1);
        atributo.setObrigatorioSIECM(Boolean.TRUE);
        atributo.setNomeAtributoDocumento(SICPFCampoEnum.CPF.getDescricao());
        atributo.setSicpfCampoEnum(SICPFCampoEnum.CPF);
        atributo.setSicpfModoEnum(SICPFModoEnum.P);
        atributo.setTipoAtributoSiecmEnum(TipoAtributoEnum.STRING);
        doc.getTipoDocumento().addAtributosExtracao(atributo);

        atributo = new AtributoExtracao();
        atributo.setId(1);
        atributo.setObrigatorioSIECM(Boolean.TRUE);
        atributo.setNomeAtributoDocumento(SICPFCampoEnum.CPF.getDescricao());
        atributo.setSicpfCampoEnum(SICPFCampoEnum.CPF);
        atributo.setSicpfModoEnum(SICPFModoEnum.E);
        atributo.setTipoAtributoSiecmEnum(TipoAtributoEnum.STRING);
        doc.getTipoDocumento().addAtributosExtracao(atributo);

        atributo = new AtributoExtracao();
        atributo.setId(2);
        atributo.setObrigatorioSIECM(Boolean.TRUE);
        atributo.setNomeAtributoDocumento(SICPFCampoEnum.ELEITOR.getDescricao());
        atributo.setSicpfCampoEnum(SICPFCampoEnum.ELEITOR);
        atributo.setSicpfModoEnum(SICPFModoEnum.P);
        atributo.setTipoAtributoSiecmEnum(TipoAtributoEnum.STRING);
        doc.getTipoDocumento().addAtributosExtracao(atributo);

        atributo = new AtributoExtracao();
        atributo.setId(3);
        atributo.setObrigatorioSIECM(Boolean.TRUE);
        atributo.setNomeAtributoDocumento(SICPFCampoEnum.NOME_MAE.getDescricao());
        atributo.setSicpfCampoEnum(SICPFCampoEnum.NOME_MAE);
        atributo.setSicpfModoEnum(SICPFModoEnum.P);
        atributo.setTipoAtributoSiecmEnum(TipoAtributoEnum.STRING);
        doc.getTipoDocumento().addAtributosExtracao(atributo);

        atributo = new AtributoExtracao();
        atributo.setId(4);
        atributo.setObrigatorioSIECM(Boolean.TRUE);
        atributo.setNomeAtributoDocumento(SICPFCampoEnum.NASCIMENTO.getDescricao());
        atributo.setSicpfCampoEnum(SICPFCampoEnum.NASCIMENTO);
        atributo.setSicpfModoEnum(SICPFModoEnum.P);
        atributo.setTipoAtributoSiecmEnum(TipoAtributoEnum.DATE);
        doc.getTipoDocumento().addAtributosExtracao(atributo);

        atributo = new AtributoExtracao();
        atributo.setId(5);
        atributo.setObrigatorioSIECM(Boolean.TRUE);
        atributo.setNomeAtributoDocumento(SICPFCampoEnum.NOME.getDescricao());
        atributo.setSicpfCampoEnum(SICPFCampoEnum.NOME);
        atributo.setSicpfModoEnum(SICPFModoEnum.P);
        atributo.setTipoAtributoSiecmEnum(TipoAtributoEnum.STRING);
        doc.getTipoDocumento().addAtributosExtracao(atributo);
    }

    private void mockContratoAdministrativoServico() {
        ContratoAdministrativo contratoAdministrativo = new ContratoAdministrativo();
        this.processoAdministrativo = new ProcessoAdministrativo();
        this.processoAdministrativo.setNumeroPregao(12312322);
        this.processoAdministrativo.setAnoProcesso(2020);
        contratoAdministrativo.setProcessoAdministrativo(this.processoAdministrativo);

        Mockito.when(contratoAdministrativoServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(contratoAdministrativo);
    }

    private void mockApensoAdmnistrativo() {
        ApensoAdministrativo apensoAdministrativo = new ApensoAdministrativo();
        this.processoAdministrativo = new ProcessoAdministrativo();
        this.processoAdministrativo.setNumeroPregao(12312322);
        this.processoAdministrativo.setAnoProcesso(2020);
        apensoAdministrativo.setProcessoAdministrativo(this.processoAdministrativo);
        Mockito.when(this.apensoAdministrativoServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(apensoAdministrativo);
    }

    private void mockApensoAdmnistrativoSemProcesso() {
        ApensoAdministrativo apensoAdministrativo = new ApensoAdministrativo();
        apensoAdministrativo.setProcessoAdministrativo(this.processoAdministrativo);
        apensoAdministrativo.setContratoAdministrativo(new ContratoAdministrativo());
        apensoAdministrativo.getContratoAdministrativo().setId(2L);
        Mockito.when(this.apensoAdministrativoServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(apensoAdministrativo);
    }

    private void mockTipoDocumento() {
        Mockito.when(tipoDocumentoServico.getById(Mockito.anyInt())).thenReturn(new TipoDocumento());
    }

}
