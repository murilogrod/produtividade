package br.gov.caixa.simtr.controle.servico;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.SipesService;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.CadinType;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SerasaType;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SiccfType;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SicowType;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SinadType;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SipesRequestDTO;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SipesResponseDTO;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SpcType;
import br.gov.caixa.pedesgo.arquitetura.siiso.dto.RetornoPessoasFisicasDTO;
import br.gov.caixa.simtr.controle.excecao.DossieAutorizacaoException;
import br.gov.caixa.simtr.controle.excecao.SiecmException;
import br.gov.caixa.simtr.controle.excecao.SimtrPermissaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.helper.AutorizacaoConsultaHelper;
import br.gov.caixa.simtr.modelo.entidade.Autorizacao;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.ComportamentoPesquisa;
import br.gov.caixa.simtr.modelo.entidade.ComposicaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.DossieClientePF;
import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.entidade.RegraDocumental;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.SistemaPesquisaEnum;
import br.gov.caixa.simtr.modelo.enumerator.SistemaPesquisaTipoRetornoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.CalendarUtil;
import br.gov.caixa.simtr.util.ConstantesUtil;

/**
 * <p>
 * AutorizacaoSevicoTest</p>
 *
 * <p>
 * Descrição: Classe de teste do servico AutorizacaoServico</p>
 *
 * <br><b>Empresa:</b> Cef - Caixa Econômica Federal
 *
 *
 * @author p559852
 *
 * @version 1.0
 */
public class AutorizacaoServicoTest {

    /**
     * Atributo manager.
     */
    @Mock
    private EntityManager manager;

    /**
     * Atributo query.
     */
    @Mock
    TypedQuery<Autorizacao> query;

    @Mock
    Query queryBasica;

    /**
     * Atributo autorizacaoServico.
     */
    @InjectMocks
    private AutorizacaoServico autorizacaoServico;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private CanalServico canalServico;

    @Mock
    private ProdutoServico produtoServico;

    @Mock
    private DocumentoServico documentoServico;

    @Mock
    private NivelDocumentalServico nivelDocumentalService;

    @Mock
    private TipoDocumentoServico tipoDocumentoServico;

    @Mock
    private ComposicaoDocumentalServico composicaoDocumentalService;

    @Mock
    private CadastroReceitaServico sicpfServico;

    @Mock
    private ComportamentoPesquisaServico comportamentoPesquisaServico;

    @Mock
    private DossieClienteServico dossieClienteServico;

    @Mock
    private SipesService sipesService;

    @Mock
    private CalendarUtil calendarUtil;

    @Mock
    private RelatorioServico relatorioServico;

    @Mock
    private CadastroCaixaServico sicliServico;

    @Mock
    private AutorizacaoConsultaHelper autorizacaoConsultasHelper;

    @Mock
    private SiecmServico siecmServico;

    private static Integer pesquisaId = 1;
    private ComportamentoPesquisa comportamentoPesquisa;
    private Canal canalSimtrWeb = new Canal();
    private Produto produtoSolicitado = new Produto();
    private DossieClientePF dossieClientePF = new DossieClientePF();
    private Autorizacao aut1;
    private Autorizacao aut2;

    /**
     * <p>
     * Método responsável por inicializar os mocks</p>.
     *
     * @author p559852
     *
     */
    @Before
    public void setUpPostConstruct() throws Exception {
        MockitoAnnotations.initMocks(this);
        canalSimtrWeb.setSigla("SIMTRWEB");

        Mockito.when(canalServico.getByClienteSSO()).thenReturn(null);
        Mockito.when(canalServico.getByClienteSSO()).thenReturn(canalSimtrWeb);

        mockProdutoSolicitadoServicoNull();

        mockDossieCliente();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();

        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(Autorizacao.class))).thenReturn(query);
       
        Documento doc = new Documento();
        TipoDocumento tipoDoc = new TipoDocumento();
        tipoDoc.setNome("teste");
        doc.setTipoDocumento(tipoDoc);
        
        Mockito.when(this.documentoServico.prototype(Mockito.any(Canal.class), Mockito.anyBoolean(), Mockito.any(TipoDocumento.class), Mockito.any(TemporalidadeDocumentoEnum.class), Mockito.any(OrigemDocumentoEnum.class), Mockito.any(FormatoConteudoEnum.class), Mockito.anyMap(), Mockito.anyString()))
                .thenReturn(doc);
        
        Mockito.when(this.documentoServico.prototype(Mockito.any(Canal.class), Mockito.anyBoolean(), Mockito.any(TipoDocumento.class), Mockito.any(TemporalidadeDocumentoEnum.class), Mockito.any(OrigemDocumentoEnum.class), Mockito.any(FormatoConteudoEnum.class), Mockito.anyList(), Mockito.anyString()))
                .thenReturn(doc);
        
        Mockito.when(this.dossieClienteServico.getById(1L, false, false, false)).thenReturn(dossieClientePF);
    }

    @Test(expected = SimtrPermissaoException.class)
    public void getAutorizacaoTestCanalInvalido() {
        mockCanalInvalido();
        Mockito.doThrow(SimtrPermissaoException.class).when(this.canalServico).validaRecursoLocalizado(Mockito.any(Canal.class), Mockito.anyString());
        autorizacaoServico.getAutorizacao(null, null, null);
    }

    @Test(expected = DossieAutorizacaoException.class)
    public void getAutorizacaoTestExceptionSicpf() {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServicoNull();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test(expected = DossieAutorizacaoException.class)
    public void getAutorizacaoTestComportamentoPesquisaTrue() {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockComportamentoPesquisaTrue();
        mockComportamentoPesquisa();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test(expected = DossieAutorizacaoException.class)
    public void getAutorizacaoTestComportamentoPesquisaReceitaFalse() {
        mockCanalValido();
        mockProdutoSolicitadoA();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockComportamentoPesquisaTrue();
        mockComportamentoPesquisa();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test(expected = DossieAutorizacaoException.class)
    public void getAutorizacaoTestComportamentoPesquisaFalse() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockSipesServiceSimples();
        mockComportamentoPesquisa();
        mockComportamentoPesquisaBloqueioFalse();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test(expected = DossieAutorizacaoException.class)
    public void AutorizacaoTestDossieClienteServico() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockSipesServiceSimples();
        mockComportamentoPesquisa();
        mockDossieClienteServico();
        mockComportamentoPesquisaBloqueioFalse();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test(expected = DossieAutorizacaoException.class)
    public void getAutorizacaoTestDossieClienteServicoNull() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockSipesServiceSimples();
        mockComportamentoPesquisa();
        mockDossieClienteServicoNull();
        mockComportamentoPesquisaBloqueioFalse();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test(expected = DossieAutorizacaoException.class)
    public void getAutorizacaoTestSipesService() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockComportamentoPesquisa();
        mockDossieClienteServicoNull();
        mockSipesServiceCompleto();
        mockComportamentoPesquisaBloqueioFalse();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test(expected = DossieAutorizacaoException.class)
    public void getAutorizacaoTestSipesExceptionComportamentais() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockComportamentoPesquisa();
        mockDossieClienteServicoNull();
        mockSipesServiceSimples();
        mockComportamentoPesquisaBloqueioFalse();
        mockDadosClienteF();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test(expected = NullPointerException.class)
    public void getAutorizacaoTestSipesSimples() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockComportamentoPesquisa();
        mockDossieClienteServicoNull();
        mockSipesServiceSimples();
        mockComportamentoPesquisaBloqueioFalse();
        mockDadosClienteF();
        mockTipoDocumentoGed();
        mockComposicoesComportamentais();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test(expected = NullPointerException.class)
    public void getAutorizacaoTestFalha() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockComportamentoPesquisa();
        mockDossieClienteServicoNull();
        mockSipesServiceSimples();
        mockComportamentoPesquisaBloqueioFalse();
        mockDadosClienteF();
        mockTipoDocumentoGed();
        mockComposicoesComportamentais();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test(expected = DossieAutorizacaoException.class)
    public void getAutorizacaoTestDadosCliente() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockComportamentoPesquisa();
        mockDossieClienteServicoNull();
        mockSipesServiceSimples();
        mockComportamentoPesquisaBloqueioFalse();
//        mockDadosClienteFException();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test(expected = DossieAutorizacaoException.class)
    public void getAutorizacaoTestRequestAutorizacao() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockComportamentoPesquisa();
        mockDossieClienteServicoNull();
        mockSipesServiceSimples();
        mockComportamentoPesquisaBloqueioFalse();
        mockComposicoesComportamentais();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test(expected = DossieAutorizacaoException.class)
    public void getAutorizacaoTestConsultaSicliException() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockComportamentoPesquisa();
        mockDossieClienteServicoNull();
        mockSipesServiceSimples();
        mockComportamentoPesquisaBloqueioFalse();
        mockComposicoesComportamentais();
        mockGedServiceCriaTransacaoClienteSiecmException();
        mockQueryBasica();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test(expected = NullPointerException.class)
    public void getAutorizacaoTestECMRetorno0() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockComportamentoPesquisa();
        mockDossieClienteServicoNull();
        mockSipesServiceSimples();
        mockComportamentoPesquisaBloqueioFalse();
        mockDadosClienteF();
        mockComposicoesComportamentais();
        mockGedServiceCriaTransacaoCliente();
        mockQueryBasica();
        mockTipoDocumentoGed();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test(expected = SiecmException.class)
    public void getAutorizacaoTestECMRetorno10() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockComportamentoPesquisa();
        mockDossieClienteServicoNull();
        mockSipesServiceSimples();
        mockComportamentoPesquisaBloqueioFalse();
        mockDadosClienteF();
        mockComposicoesComportamentais();
        mockGedServiceCriaTransacaoClienteSiecmException();
        mockQueryBasica();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test(expected = DossieAutorizacaoException.class)
    public void getAutorizacaoTestUpdateError() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockComportamentoPesquisa();
        mockDossieClienteServicoNull();
        mockSipesServiceSimples();
        mockComportamentoPesquisaBloqueioFalse();
        mockDadosClienteF();
        mockComposicoesComportamentais();
        mockGedServiceCriaTransacaoClienteSiecmException();
        mockQueryBasica();
        mockUpdateError();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test(expected = SiecmException.class)
    public void getAutorizacaoTestServiceDocumentoFolder10() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockComportamentoPesquisa();
        mockDossieClienteServicoNull();
        mockSipesServiceSimples();
        mockComportamentoPesquisaBloqueioFalse();
        mockDadosClienteF();
        mockComposicoesComportamentais();
        mockGedServiceCriaTransacaoCliente();
        mockQueryBasica();
        mockGedServiceVinculacaoDocumentoDocumentoSiecmException();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test(expected = DossieAutorizacaoException.class)
    public void getAutorizacaoTestServiceDocumentoFolder00() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockComportamentoPesquisa();
        mockDossieClienteServicoNull();
        mockSipesServiceCompleto();
        mockComportamentoPesquisaBloqueioFalse();
        mockDadosClienteF();
        mockComposicoesComportamentais();
        mockGedServiceCriaTransacaoCliente();
        mockQueryBasica();
        mockGedServiceVinculacaoDocumento();
        mockTipoDocumentoGed();
        mockGerarRelatorioPDF();
        mockGedServiceCreateDocument10();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test
    public void getAutorizacaoTestServiceDocumentoFolder0() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockComportamentoPesquisa();
        mockDossieClienteServicoNull();
        mockSipesServiceCompleto();
        mockComportamentoPesquisaBloqueioFalse();
        mockDadosClienteF();
        mockComposicoesComportamentais();
        mockGedServiceCriaTransacaoCliente();
        mockQueryBasica();
        mockGedServiceVinculacaoDocumento();
        mockTipoDocumentoGed();
        mockGerarRelatorioPDF();
        mockGedServiceCreateDocument0();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    private void mockUpdateError() {
        Mockito.doThrow(DossieAutorizacaoException.class).when(queryBasica).executeUpdate();
    }

    @Test
    public void getAutorizacaoTest() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockComportamentoPesquisa();
        mockDossieClienteServicoNull();
        mockSipesServiceCompleto();
        mockComportamentoPesquisaBloqueioFalse();
        mockDadosClienteF();
        mockComposicoesComportamentais();
        mockGedServiceCriaTransacaoCliente();
        mockQueryBasica();
        mockGedServiceVinculacaoDocumento();
        mockTipoDocumentoGed();
        mockGerarRelatorioPDF();
        mockGedServiceCreateDocumentCompleto();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test
    public void getAutorizacaoTestServiceDocumentoFolderCompleto() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockComportamentoPesquisa();
        mockDossieClienteServicoNull();
        mockSipesServiceCompleto();
        mockComportamentoPesquisaBloqueioFalse();
        mockDadosClienteF();
        mockComposicoesComportamentais();
        mockGedServiceCriaTransacaoCliente();
        mockQueryBasica();
        mockGedServiceVinculacaoDocumento();
        mockTipoDocumentoGed();
        mockGerarRelatorioPDF();
        mockGedServiceCreateDocumentCompleto();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test
    public void getAutorizacaoTestServiceDocumentoFolderSimples() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockComportamentoPesquisa();
        mockDossieClienteServicoNull();
        mockSipesServiceSimples();
        mockComportamentoPesquisaBloqueioFalse();
        mockDadosClienteF();
        mockComposicoesComportamentais();
        mockGedServiceCriaTransacaoCliente();
        mockQueryBasica();
        mockGedServiceVinculacaoDocumento();
        mockTipoDocumentoGed();
        mockGerarRelatorioPDF();
        mockGedServiceCreateDocumentCompleto();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test(expected = DossieAutorizacaoException.class)
    public void getAutorizacaoTestFalhaException() throws Exception {
        mockCanalValido();
        mockProdutoSolicitado();
        mockProdutoSolicitadoServico();
        mockSicpf();
        mockComportamentoPesquisa();
        mockDossieClienteServicoNull();
        mockSipesServiceCompleto();
        mockComportamentoPesquisaTrue();
        mockDadosClienteF();
        mockComposicoesComportamentais();
        mockGedServiceCriaTransacaoCliente();
        mockQueryBasica();
        mockGedServiceVinculacaoDocumento();
        mockTipoDocumentoGed();
        mockGerarRelatorioPDF();
        mockGedServiceCreateDocumentCompleto();
        autorizacaoServico.getAutorizacao(1L, 1, 1);
    }

    @Test
    public void finalizaOperacaoTesteException() {
        Autorizacao autorizacao = new Autorizacao();
        autorizacao.setCodigoNSU(123L);
        autorizacao.setId(1L);
        mockQueryBasica();
        autorizacaoServico.finalizaOperacao(autorizacao);
    }

    @Test
    public void finalizaOperacaoTeste() {
        Autorizacao autorizacao = new Autorizacao();
        autorizacao.setCodigoNSU(123L);
        autorizacao.setId(1L);
        mockQueryBasica();
        autorizacaoServico.finalizaOperacao(new Autorizacao());
    }

    @Test
    public void testGetCodigoAutorizacao() {
        autorizacaoServico.getByCodigoAutorizacao(1255171191L, Boolean.TRUE, Boolean.TRUE);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void testAutorizacaoConjuntaFalhaIdsNull() {

        List<Long> auts = new ArrayList<>();
        auts.add(430855235515880L);
        auts.add(181604386164258L);
        mockCarregarAutorizacaoConjunta();
        mockGedServiceCriaTransacaoCliente();
        mockQueryBasica();
        mockCreateTransacaoCliente();
        mockAttachDocumentToTransactinalFolder();

        autorizacaoServico.setAutorizacaoConjunta(null, 1, 1);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void testAutorizacaoConjuntaFalhaProdutoNull() {

        List<Long> auts = new ArrayList<>();
        auts.add(430855235515880L);
        auts.add(181604386164258L);
        mockCarregarAutorizacaoConjunta();
        mockProdutoSolicitadoServicoNull();
        mockGedServiceCriaTransacaoCliente();
        mockQueryBasica();
        mockCreateTransacaoCliente();
        mockAttachDocumentToTransactinalFolder();

        autorizacaoServico.setAutorizacaoConjunta(null, 1, 1);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void testAutorizacaoConjuntaCodigoSemAutorizacao() {

        List<Long> auts = new ArrayList<>();
        auts.add(1L);
        auts.add(181604386164258L);
        mockCarregarAutorizacaoConjunta();
        mockProdutoSolicitadoServicoNull();
        mockGedServiceCriaTransacaoCliente();
        mockQueryBasica();
        mockCreateTransacaoCliente();
        mockAttachDocumentToTransactinalFolder();

        autorizacaoServico.setAutorizacaoConjunta(auts, 1, 1);
    }

    @Test(expected = EJBException.class)
    public void testAutorizacaoConjuntaCreateCod10() {

        List<Long> auts = new ArrayList<>();
        auts.add(1L);
        auts.add(181604386164258L);
        mockQueryBasica();
        mockCarregarAutorizacaoConjunta();
        mockProdutoSolicitadoServico();
        mockGedServiceCriaTransacaoClienteSiecmException();
        mockAttachDocumentToTransactinalFolder();

        autorizacaoServico.setAutorizacaoConjunta(auts, 1, 1);
    }

    @Test(expected = EJBException.class)
    public void testAutorizacaoConjuntaAutorizacoesLocaisTransacaoCliente10() {

        List<Long> auts = new ArrayList<>();
        auts.add(1L);
        auts.add(181604386164258L);
        mockQueryBasica();
        mockCarregarAutorizacaoConjunta();
        mockProdutoSolicitadoServico();
        mockCreateTransacaoClienteSiecmException();
        mockAttachDocumentToTransactinalFolder();

        autorizacaoServico.setAutorizacaoConjunta(auts, 1, 1);
    }

    @Test
    public void testAutorizacaoConjuntaAutorizacoesLocais() {

        List<Long> auts = new ArrayList<>();
        auts.add(1L);
        auts.add(181604386164258L);
        mockQueryBasica();
        mockCarregarAutorizacaoConjunta();
        mockProdutoSolicitadoServico();
        mockCreateTransacaoCliente();
        mockAttachDocumentToTransactinalFolder();

        autorizacaoServico.setAutorizacaoConjunta(auts, 1, 1);
    }

    @Test(expected = EJBException.class)
    public void setAutorizacaoConjuntaTestTransactionalFolder10() {

        List<Long> auts = new ArrayList<>();
        auts.add(1L);
        auts.add(181604386164258L);
        mockQueryBasica();
        mockCarregarAutorizacaoConjunta();
        mockProdutoSolicitadoServico();
        mockCreateTransacaoCliente();
        mockAttachDocumentToTransactinalFolder10();

        autorizacaoServico.setAutorizacaoConjunta(auts, 1, 1);
    }

    @Test(expected = EJBException.class)
    public void setAutorizacaoConjuntaTestFalhaCarregar() {

        List<Long> auts = new ArrayList<>();
        auts.add(1L);
        auts.add(181604386164258L);
        mockQueryBasica();
        mockCarregarAutorizacaoConjuntaFalha();
        mockProdutoSolicitadoServico();
        mockCreateTransacaoCliente();
        mockAttachDocumentToTransactinalFolder10();

        autorizacaoServico.setAutorizacaoConjunta(auts, 1, 1);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void setAutorizacaoConjuntaTestFalhaQuery() {

        List<Long> auts = new ArrayList<>();
        auts.add(1L);
        auts.add(181604386164258L);
        mockQueryBasica();
        mockSingleResultFalha();
        mockProdutoSolicitadoServico();
        mockCreateTransacaoCliente();
        mockAttachDocumentToTransactinalFolder10();

        autorizacaoServico.setAutorizacaoConjunta(auts, 1, 1);
    }

    private void mockSingleResultFalha() {
        Mockito.doThrow(NoResultException.class).when(query).getSingleResult();
    }

    public void mockCreateTransacaoCliente() {
        Mockito.doNothing().when(this.siecmServico).criaTransacaoClienteSIECM(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.anyString(), Mockito.anyString());
    }

    public void mockCreateTransacaoClienteSiecmException() {
        Mockito.doThrow(new EJBException("Falha ao criar transação de cliente")).when(this.siecmServico).criaTransacaoClienteSIECM(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.anyString(), Mockito.anyString());
    }

    public void mockAttachDocumentToTransactinalFolder() {
        Mockito.doNothing().when(this.siecmServico).vinculaDocumentosTransacaoSIECM(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.anyString(), Mockito.anyList());
    }

    public void mockAttachDocumentToTransactinalFolder10() {
        Mockito.doThrow(new EJBException("Falha ao vincular documentos na transação")).when(this.siecmServico).vinculaDocumentosTransacaoSIECM(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.anyString(), Mockito.anyList());
    }

    public void mockCarregarAutorizacaoConjunta() {
        aut1 = new Autorizacao();
        aut1.setCpfCnpj(11111111111L);
        aut1.setTipoPessoa(TipoPessoaEnum.F);
        aut1.setCodigoNSU(430855235515880L);
        aut1.setProdutoOperacao(1);
        aut1.setProdutoModalidade(1);

        aut2 = new Autorizacao();
        aut2.setCpfCnpj(22222222222L);
        aut2.setTipoPessoa(TipoPessoaEnum.F);
        aut2.setCodigoNSU(181604386164258L);
        aut2.setProdutoOperacao(1);
        aut2.setProdutoModalidade(1);

        Mockito.when(autorizacaoServico.getByCodigoAutorizacao(430855235515880L, false, false)).thenReturn(aut1);
        Mockito.when(autorizacaoServico.getByCodigoAutorizacao(181604386164258L, false, false)).thenReturn(aut2);

    }

    public void mockCarregarAutorizacaoConjuntaFalha() {
        aut1 = new Autorizacao();
        aut1.setCpfCnpj(11111111111L);
        aut1.setTipoPessoa(TipoPessoaEnum.F);
        aut1.setCodigoNSU(430855235515880L);
        aut1.setProdutoOperacao(1);
        aut1.setProdutoModalidade(1);

        aut2 = new Autorizacao();
        aut2.setCpfCnpj(22222222222L);
        aut2.setTipoPessoa(TipoPessoaEnum.F);
        aut2.setCodigoNSU(181604386164258L);
        aut2.setProdutoOperacao(1);
        aut2.setProdutoModalidade(1);

        Mockito.when(autorizacaoServico.getByCodigoAutorizacao(430855235515880L, true, false)).thenReturn(aut1);
        Mockito.when(autorizacaoServico.getByCodigoAutorizacao(181604386164258L, true, false)).thenReturn(aut2);

    }

    private void mockCanalValido() {
        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(new Canal());
    }

    private void mockCanalInvalido() {
        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(null);
    }

    private void mockProdutoSolicitadoServicoNull() {
        Mockito.when(produtoServico.getByOperacaoModalidade(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(),
                Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(null);
    }

    private void mockDossieCliente() {
        dossieClientePF.setId(1L);
        dossieClientePF.setCpfCnpj(72772450163L);
        dossieClientePF.setTipoPessoa(TipoPessoaEnum.F);
    }
    
    private void mockProdutoSolicitado() {
        produtoSolicitado.setTipoPessoa(TipoPessoaEnum.F);
        produtoSolicitado.setDossieDigital(true);
        produtoSolicitado.setPesquisaReceita(true);
        produtoSolicitado.setPesquisaCadin(true);
        produtoSolicitado.setPesquisaScpc(true);
        produtoSolicitado.setPesquisaSerasa(true);
        produtoSolicitado.setPesquisaCcf(true);
        produtoSolicitado.setPesquisaSicow(true);
        produtoSolicitado.setPesquisaSinad(true);
        produtoSolicitado.setComportamentosPesquisas(new HashSet<>());
    }

    private void mockProdutoSolicitadoA() {
        produtoSolicitado.setTipoPessoa(TipoPessoaEnum.F);
        produtoSolicitado.setDossieDigital(true);
        produtoSolicitado.setPesquisaReceita(true);
        produtoSolicitado.setPesquisaCadin(true);
        produtoSolicitado.setPesquisaScpc(true);
        produtoSolicitado.setPesquisaSerasa(true);
        produtoSolicitado.setPesquisaCcf(true);
        produtoSolicitado.setPesquisaSicow(true);
        produtoSolicitado.setPesquisaSinad(true);
        produtoSolicitado.setComportamentosPesquisas(new HashSet<>());
    }

    private void mockProdutoSolicitadoPesquisaReceitaFalse() {
        produtoSolicitado.setTipoPessoa(TipoPessoaEnum.F);
        produtoSolicitado.setDossieDigital(true);
        produtoSolicitado.setPesquisaReceita(false);
        produtoSolicitado.setPesquisaCadin(true);
        produtoSolicitado.setPesquisaScpc(true);
        produtoSolicitado.setPesquisaSerasa(true);
        produtoSolicitado.setPesquisaCcf(true);
        produtoSolicitado.setPesquisaSicow(true);
        produtoSolicitado.setPesquisaSinad(true);
        produtoSolicitado.setComportamentosPesquisas(new HashSet<>());
    }

    private void mockProdutoSolicitadoServico() {
        Mockito.when(produtoServico.getByOperacaoModalidade(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(),
                Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(produtoSolicitado);

    }

    private void mockComportamentoPesquisaBloqueioFalse() {
        comportamentoPesquisa = new ComportamentoPesquisa();
        comportamentoPesquisa.setOrientacao("Orientacao");
        comportamentoPesquisa.setBloqueio(Boolean.FALSE);
        for (SistemaPesquisaTipoRetornoEnum valor : SistemaPesquisaTipoRetornoEnum.values()) {
            criarComportamentoPesquisa(valor, false);
        }
    }

    private void criarComportamentoPesquisa(SistemaPesquisaTipoRetornoEnum tipo, Boolean bloqueio) {
        ComportamentoPesquisa comp = new ComportamentoPesquisa();
        comp.setId(AutorizacaoServicoTest.pesquisaId++);
        comp.setOrientacao(tipo.getDescricaoOcorrencia());
        comp.setBloqueio(bloqueio);
        comp.setValorCodigoRetorno(tipo);
        produtoSolicitado.getComportamentosPesquisas().add(comp);
    }

    private void mockComportamentoPesquisaTrue() {
        DossieAutorizacaoException dossieAutorizacaoException = new DossieAutorizacaoException("Impedimento por pendência junto ao SIPES.", null, new SipesResponseDTO(), 30030030030L, TipoPessoaEnum.F, new Produto(), "SIZZZ", Boolean.FALSE);
        Mockito.doThrow(dossieAutorizacaoException).when(this.autorizacaoConsultasHelper).realizaConsultaSIPES(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.any(Produto.class), Mockito.any(Canal.class), Mockito.any());
    }

    private void mockComportamentoPesquisa() {
        Mockito.when(this.comportamentoPesquisaServico.getBySistemaAndCodigoTipoPesquisa(Mockito.any(Produto.class), Mockito.any(SistemaPesquisaEnum.class), Mockito.anyInt(), Mockito.any())).thenReturn(comportamentoPesquisa);
    }

    private void mockSicpf() {
        RetornoPessoasFisicasDTO response = new RetornoPessoasFisicasDTO();
        response.setCodigoSituacaoCpf("0");
        response.setNomeContribuinte("NomeContribuinte");
        response.setNomeMae("NomeMae");
        response.setTituloEleitor("123456789");
        response.setDataNascimento(Calendar.getInstance());
        Mockito.when(this.sicpfServico.consultaCadastroPF(Mockito.anyLong())).thenReturn(response);
    }

    private void mockDossieClienteServico() {
        DossieCliente dossie = new DossieClientePF();
        Mockito.when(this.dossieClienteServico.getByCpfCnpj(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(dossie);
    }

    private void mockDossieClienteServicoNull() {
        Mockito.when(this.dossieClienteServico.getByCpfCnpj(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(null);
    }

    private void mockSipesServiceCompleto() throws Exception {
        SipesResponseDTO sipes = new SipesResponseDTO();
        sipes.setCADIN(new CadinType());
        sipes.getCADIN().setRetornoOcorrencias(new ArrayList<>());
        sipes.setSERASA(new SerasaType());
        sipes.getSERASA().setChequesSemFundo(new ArrayList<>());
        sipes.getSERASA().setConveniosDevedores(new ArrayList<>());
        sipes.getSERASA().setDadosResumo(new ArrayList<>());
        sipes.getSERASA().setFalenciaConcordata(new ArrayList<>());
        sipes.getSERASA().setParticipacaoConcordata(new ArrayList<>());
        sipes.getSERASA().setPendeciasFinanceiras(new ArrayList<>());
        sipes.getSERASA().setAcaoJudicial(new ArrayList<>());
        sipes.getSERASA().setProtesto(new ArrayList<>());
        sipes.setSICCF(new SiccfType());
        sipes.getSICCF().setRetornoOcorrencias(new ArrayList<>());
        sipes.setSICOW(new SicowType());
        sipes.getSICOW().setPpePrimario(new ArrayList<>());
        sipes.getSICOW().setPpeRelacionados(new ArrayList<>());
        sipes.getSICOW().setConres(new ArrayList<>());
        sipes.getSICOW().setPartesRelacionadas(new ArrayList<>());
        sipes.getSICOW().setEmpregadosTrabalhoEscravo(new ArrayList<>());
        sipes.getSICOW().setProibidoContratarSetorPublico(new ArrayList<>());
        sipes.getSICOW().setInformacoesSeguranca(new ArrayList<>());
        sipes.getSICOW().setProibidoContratarSetorPublico(new ArrayList<>());
        sipes.getSICOW().setInterdicaoJudicial(new ArrayList<>());
        sipes.getSICOW().setPld(new ArrayList<>());
        sipes.setSINAD(new SinadType());
        sipes.getSINAD().setOcorrenciasSiina(new ArrayList<>());
        sipes.getSINAD().setOcorrenciasSinad(new ArrayList<>());
        sipes.setSPC(new SpcType());
        sipes.getSPC().setDadosResumo(new ArrayList<>());
        sipes.getSPC().setRetornoOcorrencias(new ArrayList<>());
        Mockito.when(this.sipesService.callService(Mockito.any(SipesRequestDTO.class))).thenReturn(sipes);
    }

    private void mockSipesServiceSimples() throws Exception {
        SipesResponseDTO sipes = new SipesResponseDTO();
        sipes.setCADIN(new CadinType());
        sipes.setSERASA(new SerasaType());
        sipes.setSICCF(new SiccfType());
        sipes.setSICOW(new SicowType());
        sipes.setSINAD(new SinadType());
        sipes.setSPC(new SpcType());
        Mockito.when(this.sipesService.callService(Mockito.any(SipesRequestDTO.class))).thenReturn(sipes);
    }

//    private void mockDadosClienteFException() {
//        Mockito.when(gedService.getDocumentosCliente(Mockito.anyLong(), Mockito.anyString())).thenThrow(new RuntimeException());
//    }
    private void mockDadosClienteF() {
        Map<Documento, String> docs = new HashMap<>();
        TipoDocumento tipoDoc = new TipoDocumento();
        tipoDoc.setNomeClasseSIECM("CLASSE_GED");
        Documento doc = new Documento();
        doc.setTipoDocumento(tipoDoc);
        docs.put(doc, "http://siecm.des.caixa/siecm-web/ECM/getDocumento/false/6fd2d87e6/CLASSE_GED");
        Mockito.when(this.documentoServico.listDocumentosDefinitivosDossieDigital(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class))).thenReturn(docs);
    }

    private void mockComposicoesComportamentais() {
        List<ComposicaoDocumental> composicoes = new ArrayList<>();
        ComposicaoDocumental composicao = new ComposicaoDocumental();
        composicao.setRegrasDocumentais(new HashSet<>());
        RegraDocumental regra = new RegraDocumental();
        regra.setId(1L);
        FuncaoDocumental funcao = new FuncaoDocumental();
        funcao.setTiposDocumento(new HashSet<>());
        TipoDocumento tipoDoc = new TipoDocumento();
        tipoDoc.setNomeClasseSIECM("NomeClasseGed");
        funcao.addTiposDocumento(tipoDoc);
        regra.setFuncaoDocumental(funcao);
        composicao.getRegrasDocumentais().add(regra);

        regra = new RegraDocumental();
        regra.setId(2L);
        tipoDoc = new TipoDocumento();
        tipoDoc.setNomeClasseSIECM("NomeClasseGed");
        regra.setTipoDocumento(tipoDoc);
        composicao.getRegrasDocumentais().add(regra);

        composicoes.add(composicao);

        Mockito.when(this.composicaoDocumentalService.getComposicoesByProduto(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean())).thenReturn(composicoes);
    }

    private void mockGedServiceCriaTransacaoClienteSiecmException() {
        Mockito.doThrow(new EJBException(new SiecmException("Falha ao criar pasta da transação vinculada ao cliente", true))).when(this.siecmServico).criaTransacaoClienteSIECM(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.anyString(), Mockito.anyString());
    }

    private void mockGedServiceCriaTransacaoCliente() {
        Mockito.doNothing().when(this.siecmServico).criaTransacaoClienteSIECM(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.anyString(), Mockito.anyString());
    }

    private void mockQueryBasica() {
        Mockito.when(this.manager.createQuery(Mockito.anyString())).thenReturn(queryBasica);
    }

    private void mockGedServiceVinculacaoDocumentoDocumentoSiecmException() {
        Mockito.doThrow(new EJBException(new SiecmException("Falha ao vincular documentos da operação", true))).when(this.siecmServico).vinculaDocumentosTransacaoSIECM(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.anyString(), Mockito.anyList());
    }

    private void mockGedServiceVinculacaoDocumento() {
        Mockito.doNothing().when(this.siecmServico).vinculaDocumentosTransacaoSIECM(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.anyString(), Mockito.anyList());
    }

    private void mockTipoDocumentoGed() {
        TipoDocumento tipoDoc = new TipoDocumento();
        tipoDoc.setNome("CONSULTA CADASTRAL");
        tipoDoc.setNomeClasseSIECM("NomeClasseGed");
        tipoDoc.setNomeArquivoMinuta("pesquisa_cadastral");
        tipoDoc.setPrazoValidade(2);
        Mockito.when(this.tipoDocumentoServico.getByTipologia(ConstantesUtil.TIPOLOGIA_CONSULTA_CADASTRAL)).thenReturn(tipoDoc);
    }

    private void mockGerarRelatorioPDF() {
        Mockito.when(this.relatorioServico.gerarRelatorioPDFJsonDataSource(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap())).thenReturn(new byte[100]);
    }

    private void mockGedServiceCreateDocument0() {
        Mockito.when(this.siecmServico.armazenaDocumentoOperacaoSIECM(Mockito.any(Documento.class), Mockito.any(TemporalidadeDocumentoEnum.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn("5iou5-323urf-03f83u8-93f2e-5489e3");
    }

    private void mockGedServiceCreateDocument10() {
        Mockito.doThrow(new EJBException(new SiecmException("Falha ao armazenar documento da operação", true))).when(this.siecmServico).armazenaDocumentoOperacaoSIECM(Mockito.any(Documento.class), Mockito.any(TemporalidadeDocumentoEnum.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    private void mockGedServiceCreateDocumentCompleto() {
        Mockito.when(this.siecmServico.armazenaDocumentoOperacaoSIECM(Mockito.any(Documento.class), Mockito.any(TemporalidadeDocumentoEnum.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn("5iou5-323urf-03f83u8-93f2e-5489e3");
    }

}
