/**
 * Copyright (c) 2018 Caixa Econômica Federal. Todos os direitos
 * reservados.
 *
 * Caixa Econômica Federal - SIACG
 *
 * Este software foi desenvolvido sob demanda da CAIXA e está
 * protegido por leis de direitos autorais e tratados internacionais. As
 * condições de cópia e utilização total ou partes dependem de autorização da
 * empresa. Cópias não são permitidas sem expressa autorização. Não pode ser
 * comercializado ou utilizado para propósitos particulares.
 *
 * Uso exclusivo da Caixa Econômica Federal. A reprodução ou distribuição não
 * autorizada deste programa ou de parte dele, resultará em punições civis e
 * criminais e os infratores incorrem em sanções previstas na legislação em
 * vigor.
 *
 * Histórico do TFS:
 *
 * LastChangedRevision: $Revision$
 * LastChangedBy: $Author$
 * LastChangedDate: $Date$
 *
 * HeadURL: $HeadURL$
 *
 */
package br.gov.caixa.simtr.controle.servico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

import br.gov.caixa.simtr.controle.excecao.SimtrCadastroException;
import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.OpcaoAtributo;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.EstrategiaPartilhaEnum;
import br.gov.caixa.simtr.modelo.enumerator.ModoPartilhaEnum;
import br.gov.caixa.simtr.modelo.enumerator.SICPFCampoEnum;
import br.gov.caixa.simtr.modelo.enumerator.SICPFModoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoCampoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento.AtributoExtracaoManutencaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento.TipoDocumentoManutencaoDTO;
import io.jsonwebtoken.lang.Assert;

/**
 * <p>
 * CanalServicoTest</p>
 *
 * <p>
 * Descrição: Classe de teste do servico CanalServico</p>
 *
 * <br><b>Empresa:</b> Cef - Caixa Econômica Federal
 *
 *
 * @author p541915
 *
 * @version 1.0
 */
public class TipoDocumentoServicoTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<TipoDocumento> query;

    @Mock
    private TypedQuery<Calendar> queryAlteracao;

    @Mock
    private Logger logger;

    @Mock
    private FuncaoDocumentalServico funcaoDocumentalServico;

    List<TipoDocumento> listaTipologia;

    AtributoExtracao atributoExtracao;
    
    private FuncaoDocumental funcaoDocumental;
    
    private TipoDocumento tipo;

    @InjectMocks
    private TipoDocumentoServico servico = new TipoDocumentoServico();

    /**
     * <p>
     * Método responsável por inicializar os mocks</p>.
     *
     * @author f538462
     *
     */
    @Before
    public void setUpPostConstruct() {
        MockitoAnnotations.initMocks(this);
        mockTipoDocumentoList();
    }

    private void mockFuncaoDocumentaoServico() {
        Mockito.when(this.funcaoDocumentalServico.findById(Mockito.anyInt())).thenReturn(funcaoDocumental);
    }

    private void mockFuncaoDocumentaoServicoGetById() {
        Mockito.when(this.funcaoDocumentalServico.getById(Mockito.anyInt())).thenReturn(funcaoDocumental);
    }

    private void mockFuncaoDocumental(Integer id) {
        funcaoDocumental = new FuncaoDocumental();
        funcaoDocumental.getTiposDocumento().add(mockTipoDocumento(id));
    }

    private void mockTipoDocumentoList() {
        listaTipologia = new ArrayList<TipoDocumento>();

        TipoDocumento tipo = mockTipoDocumento(1);
        listaTipologia.add(tipo);

        tipo = mockTipoDocumento(2);

        listaTipologia.add(tipo);

        tipo = mockTipoDocumento(3);
        listaTipologia.add(tipo);

    }

    private TipoDocumento mockTipoDocumento(Integer id) {
    	tipo = new TipoDocumento();
        tipo.setId(id);
        tipo.setNome("Nome" + id);
        tipo.setNomeClasseSIECM("ClasseGED");
        tipo.setCodigoTipologia("codigoTipologia");
        tipo.setAvatar("glyphicon glyphicon-picture");
        tipo.setCorRGB("#FFCC00");
        tipo.setUsoApoioNegocio(true);
        tipo.setUsoDossieDigital(true);
        tipo.setUsoProcessoAdministrativo(true);
        tipo.setGuardaBinarioOutsourcing(false);
        tipo.setAtivo(true);
        return tipo;
    }
    
    private OpcaoAtributo mockOpcaoAtributo(String chave, String valor) {
        OpcaoAtributo opcaoAtributo = new OpcaoAtributo();
        opcaoAtributo.setId(1);
        opcaoAtributo.setDescricaoOpcao(chave);
        opcaoAtributo.setValorOpcao(valor);
        return opcaoAtributo;
    }
    
    private OpcaoAtributo mockOpcaoAtributoVinculadoAtributoExtracao(String chave, String valor) {
        this.atributoExtracao.setOpcoesAtributo(new HashSet<>());
        
        OpcaoAtributo opcaoAtributo = new OpcaoAtributo();
        opcaoAtributo.setDescricaoOpcao(chave);
        opcaoAtributo.setValorOpcao(valor);
        
        this.atributoExtracao.getOpcoesAtributo().add(opcaoAtributo);
        
        return opcaoAtributo;
    }
    
    private TipoDocumento mockTipoDocumentoAtributoExtracaoOpcaoAtributoCompleto() {
        TipoDocumento tipo = mockTipoDocumento(1);
        mockAtributoExtracaoOpcaoAtributoCompleto();
        tipo.setAtributosExtracao(new HashSet<>());
        tipo.getAtributosExtracao().add(atributoExtracao);
        
        return tipo;
    }
    
    private TipoDocumento mockTipoDocumentoAtributoExtracaoCompleto() {
    	TipoDocumento tipo = mockTipoDocumento(1);
    	mockAtributoExtracaoCompleto();
    	tipo.setAtributosExtracao(new HashSet<>());
    	tipo.getAtributosExtracao().add(atributoExtracao);
    	
    	return tipo;
    }
    
    private TipoDocumento mockTipoDocumentoAtributoExtracaoVazio() {
    	TipoDocumento tipo = mockTipoDocumento(1);
    	atributoExtracao = new AtributoExtracao();
        atributoExtracao.setId(1);
    	tipo.setAtributosExtracao(new HashSet<>());
    	tipo.getAtributosExtracao().add(atributoExtracao);
    	return tipo;
    }

    private TipoDocumento mockTipoDocumentoVazio() {
        TipoDocumento tipo = new TipoDocumento();
        tipo.setId(null);
        tipo.setNome(null);
        tipo.setNomeClasseSIECM(null);
        tipo.setUsoApoioNegocio(null);
        tipo.setUsoDossieDigital(null);
        tipo.setUsoProcessoAdministrativo(null);
        tipo.setValidadeAutoContida(null);
        tipo.setEnviaAvaliacaoCadastral(null);
        tipo.setEnviaExtracaoExterna(null);
        return tipo;
    }

    private TipoDocumento mockTipoDocumentoVazioDossie() {
        TipoDocumento tipo = new TipoDocumento();
        tipo.setId(null);
        tipo.setNome(null);
        tipo.setNomeClasseSIECM(null);
        tipo.setUsoApoioNegocio(null);
        tipo.setUsoProcessoAdministrativo(null);
        tipo.setValidadeAutoContida(null);
        tipo.setEnviaAvaliacaoCadastral(null);
        tipo.setEnviaExtracaoExterna(null);
        tipo.setUsoDossieDigital(Boolean.TRUE);
        return tipo;
    }

    private TipoDocumento mockTipoDocumentoSemAtributo(Integer id) {
        TipoDocumento tipo = new TipoDocumento();
        tipo.setId(id);
        tipo.setNome("Nome" + id);
        tipo.setNomeClasseSIECM("ClasseGED");
        tipo.setAvatar("glyphicon glyphicon-picture");
        tipo.setCorRGB("#FFCC00");
        tipo.setUsoApoioNegocio(true);
        tipo.setUsoDossieDigital(true);
        tipo.setUsoProcessoAdministrativo(true);
        return tipo;
    }

    @Test
    public void carregarMapasTeste() {
        mockCreateQuery();
        mockResultList();
        servico.carregarMapas();
    }

    @Test
    public void getProcessoAdministrativoByIdTeste() {
        mockCreateQuery();
        mockResultList();
        Assert.notNull(servico.getById(2));
    }

    @Test
    public void getProcessoAdministrativoByNomeTeste() {
        mockCreateQuery();
        mockResultList();
        Assert.notNull(servico.getByNome("Nome2"));
    }

    @Test
    public void getDossieDigitalByNomeTeste() {
        mockCreateQuery();
        mockResultList();
        Assert.notNull(servico.getByNome("Nome2"));
    }

    @Test
    public void getDossieDigitalByIdTeste() {
        mockCreateQuery();
        mockResultList();
        Assert.notNull(servico.getById(2));
    }

    @Test
    public void getDossieDigitalByClasseGEDTeste() {
        mockCreateQuery();
        mockResultList();
        Assert.notNull(servico.getByTipologia("codigoTipologia"));
    }

    @Test
    public void getApoioNegocioByIdTeste() {
        mockCreateQuery();
        mockResultList();
        Assert.notNull(servico.getById(2));
    }

    @Test
    public void getApoioNegocioByNomeTeste() {
        mockCreateQuery();
        mockResultList();
        Assert.notNull(servico.getByNome("Nome2"));
    }

    @Test
    public void listTipologiaDossieDigitalTeste() {
        mockCreateQuery();
        mockResultList();
        Assert.notNull(servico.listTipologiaByCategoria(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
    }

    @Test
    public void getDataHoraUltimaAtualizacaoTeste() {
        mockCreateQuery();
        mockResultList();
        servico.getDataHoraUltimaAlteracao();
    }

    @Test
    public void getProcessoAdministrativoByIdTesteMapas() {
        mockCreateQuery();
        mockResultList();
        servico.carregarMapas();
        Assert.notNull(servico.getById(2));
    }

    @Test
    public void getProcessoAdministrativoByNomeTesteMapas() {
        mockCreateQuery();
        mockResultList();
        servico.carregarMapas();
        Assert.notNull(servico.getByNome("Nome2"));
    }

    @Test
    public void getDossieDigitalByNomeTesteMapas() {
        mockCreateQuery();
        mockResultList();
        servico.carregarMapas();
        Assert.notNull(servico.getByNome("Nome2"));
    }

    @Test
    public void getDossieDigitalByIdTesteMapas() {
        mockCreateQuery();
        mockResultList();
        servico.carregarMapas();
        Assert.notNull(servico.getById(2));
    }

    @Test
    public void getDossieDigitalByClasseGEDTesteMapas() {
        mockCreateQuery();
        mockResultList();
        servico.carregarMapas();
        Assert.notNull(servico.getByTipologia("codigoTipologia"));
    }

    @Test
    public void getApoioNegocioByIdTesteMapas() {
        mockCreateQuery();
        mockResultList();
        servico.carregarMapas();
        Assert.notNull(servico.getById(2));
    }

    @Test
    public void getApoioNegocioByNomeTesteMapas() {
        mockCreateQuery();
        mockResultList();
        servico.carregarMapas();
        Assert.notNull(servico.getByNome("Nome2"));
    }

    @Test
    public void listTipologiaDossieDigitalTesteCarregarMapas() {
        mockCreateQuery();
        mockResultList();
        servico.carregarMapas();
        Assert.notNull(servico.listTipologiaByCategoria(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
    }

    @Test
    public void getDataHoraUltimaAtualizacaoTesteMapas() {
        mockCreateQuery();
        mockResultList();
        servico.carregarMapas();
        servico.getDataHoraUltimaAlteracao();
    }

    @Test
    public void saveTeste() {
        mockCreateQuery();
        mockResultList();
        servico.save(new TipoDocumento());
    }

    @Test
    public void listTeste() {
        mockCreateQuery();
        mockResultList();
        Assert.notNull(servico.list());
    }

    @Test
    public void listTodos() {
        mockCreateQuery();
        mockResultList();
        Assert.notNull(servico.list());
    }

    @Test
    public void delete() {
        mockCreateQuery();
        mockTipoDocumento(1);
        mockSingleResult();
        servico.delete(1);
    }

    @Test(expected = SimtrEstadoImpeditivoException.class)
    public void deleteException() {
        mockCreateQuery();
        mockTipoDocumento(1);
        mockSingleResult();
        mockManagerException();
        servico.delete(1);
    }

    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void deleteEstadoException() {
        mockCreateQuery();
        mockResultList();
        mockManagerSemAtributo();
        mockManagerException();
        servico.delete(1);
    }

    @Test
    public void saveTest() {
        mockCreateQuery();
        mockResultList();
        mockManager();
        mockFuncaoDocumental(2);
        mockFuncaoDocumentaoServico();
        List<Integer> lista = new ArrayList<>();
        lista.add(1);
        servico.save(mockTipoDocumento(1), lista);
    }

    @Test(expected = SimtrCadastroException.class)
    public void saveTestTipoDocVazio() {
        mockCreateQuery();
        mockResultList();
        mockManager();
        servico.save(mockTipoDocumentoVazio(), new ArrayList<>());
    }

    @Test(expected = SimtrEstadoImpeditivoException.class)
    public void saveTestException() {
        mockCreateQuery();
        mockResultList();
        mockManagerPersistException();
        servico.save(mockTipoDocumento(1), new ArrayList<>());
    }

    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void saveAtributoExtracaoTest() {
        mockCreateQuery();
        mockTipoDocumento(1);
        mockSingleResult();
        mockAtributoExtracao();
        servico.saveAtributoExtracao(1, 1, atributoExtracao);
    }

    @Test
    public void saveAtributoExtracaoTestSemPartilha() {
        mockCreateQuery();
        mockTipoDocumento(1);
        mockSingleResult();
        mockAtributoExtracaoCompleto();
        servico.saveAtributoExtracao(1, null, atributoExtracao);
    }

    @Test(expected = SimtrEstadoImpeditivoException.class)
    public void saveAtributoExtracaoTestSemPartilhaException() {
        mockCreateQuery();
        mockTipoDocumento(1);
        mockSingleResult();
        mockAtributoExtracaoCompleto();
        mockManagerPersistException();
        servico.saveAtributoExtracao(1, null, atributoExtracao);
    }
    
    @Test(expected = SimtrCadastroException.class)
    public void saveAtributoExtracaoTestAtributoVazio() {
        mockCreateQuery();
        mockTipoDocumento(1);
        mockSingleResult();
        mockAtributoExtracaoCompleto();
        mockManagerPersistException();
        servico.saveAtributoExtracao(1, null, new AtributoExtracao());
    }
    
    @Test(expected = SimtrEstadoImpeditivoException.class)
    public void saveAtributoExtracaoTestAtributoCompleto() {
        mockCreateQuery();
        mockTipoDocumento(1);
        mockSingleResult();
        mockAtributoExtracaoCompleto();
        mockManagerPersistException();
        mockAtributoExtracaoCompleto();
        servico.saveAtributoExtracao(1, null, atributoExtracao);
    }

    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void saveAtributoExtracaoGEDTest() {
        mockCreateQuery();
        mockTipoDocumento(1);
        mockSingleResult();
        mockAtributoExtracaoGED();
        servico.saveAtributoExtracao(1, 1, atributoExtracao);
    }

    @Test
    public void deleteAtributoExtracaoTest() {
        mockCreateQuery();
        mockManager();
        mockSingleResult();
        servico.deleteAtributoExtracao(1, 1);
    }

    @Test
    public void deleteOpcaoAtributoExtracaoTest() {
        mockCreateQuery();
        mockManagerCompleto();
        mockSingleResult();
        servico.deleteOpcaoAtributoExtracao(1, 1, 1);
    }
    
    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void deleteAtributoExtracaoTestException() {
        mockCreateQuery();
        mockManager();
        mockSingleResult();
        mockManagerException();
        servico.deleteAtributoExtracao(1, 2);
    }

    @Test(expected = SimtrCadastroException.class)
    public void aplicaPatchTest() {
        mockCreateQuery();
        mockManager();
        mockSingleResultInvalidoSemClasseGED();
        mockFuncaoDocumental(1);
        mockFuncaoDocumentaoServicoGetById();
        mockFuncaoDocumentaoServico();
        servico.aplicaPatch(1, mockTipoDocumentoManutencaoDTODossie());
    }

    @Test
    public void aplicaPatchTestNegocial() {
        mockCreateQuery();
        mockManager();
        mockTipoDocumento(1);
        mockSingleResult();
        mockFuncaoDocumental(1);
        mockFuncaoDocumentaoServicoGetById();
        mockFuncaoDocumentaoServico();
        servico.aplicaPatch(1, mockTipoDocumentoManutencaoDTO());
    }

    @Test(expected = SimtrEstadoImpeditivoException.class)
    public void aplicaPatchTestMergeException() {
        mockManagerMergeException();
        mockCreateQuery();
        mockManager();
        mockTipoDocumento(1);
        mockSingleResult();
        mockFuncaoDocumental(1);
        mockFuncaoDocumentaoServicoGetById();
        mockFuncaoDocumentaoServico();
        servico.aplicaPatch(1, mockTipoDocumentoManutencaoDTO());
    }

    @Test
    public void aplicaPatchAtributoExtracaoTest() {
    	mockTipoDocumentoAtributoExtracaoCompleto();
        mockCreateQuery();
        mockSingleResult();
        servico.aplicaPatchAtributoExtracao(1, 1, mockAtributoExtracaoManutencaoDTO(1));
    }
    
    @Test(expected = SimtrCadastroException.class)
    public void aplicaPatchAtributoExtracaoTestPartilhaDiferente() {
    	mockTipoDocumentoAtributoExtracaoCompleto();
        mockCreateQuery();
        mockSingleResult();
        servico.aplicaPatchAtributoExtracao(1, 1, mockAtributoExtracaoManutencaoDTO(2));
    }
    
    @Test(expected = SimtrCadastroException.class)
    public void aplicaPatchAtributoExtracaoTestVazio() {
    	mockTipoDocumentoAtributoExtracaoVazio();
        mockCreateQuery();
        mockSingleResult();
        AtributoExtracaoManutencaoDTO atributo = new AtributoExtracaoManutencaoDTO();
        atributo.setNomeAtributoSIECM("ATRIBUTO_GED");
        servico.aplicaPatchAtributoExtracao(1, 1, atributo);
    }
    
    @Test
    public void listInativosIncluidos() {
    	mockCreateQuery();
    	mockResultList();
    	servico.listInativosIncluidos();
    }
    
    @Test
    public void inserirOpcaoAtributoTest() {
        mockTipoDocumentoAtributoExtracaoCompleto();
        mockCreateQuery();
        mockSingleResult();
        OpcaoAtributo opcaoAtributo = mockOpcaoAtributo("opcao_1", "1");
        servico.insereNovaOpcaoAtributo(1, 1, opcaoAtributo);
    }
    
    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void inserirOpcaoAtributoTipoDocumentoNaoEncontradoTest() {
        mockCreateQuery();
        mockSingleResultNull();
        mockTipoDocumentoAtributoExtracaoCompleto();
        OpcaoAtributo opcaoAtributo = mockOpcaoAtributo("opcao_1", "1");
        servico.insereNovaOpcaoAtributo(1, 1, opcaoAtributo);
    }
    
    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void inserirOpcaoAtributoExtracaoNaoEncontradoTest() {
        mockCreateQuery();
        mockSingleResult();
        OpcaoAtributo opcaoAtributo = mockOpcaoAtributo("opcao_1", "1");
        servico.insereNovaOpcaoAtributo(1, 1, opcaoAtributo);
    }
    
    @Test(expected = SimtrRequisicaoException.class)
    public void inserirOpcaoAtributoJaExistenteTest() {
        mockTipoDocumentoAtributoExtracaoCompleto();
        mockCreateQuery();
        mockSingleResult();
        OpcaoAtributo opcaoAtributo = mockOpcaoAtributoVinculadoAtributoExtracao("opcao_1", "1");
        servico.insereNovaOpcaoAtributo(1, 1, opcaoAtributo);
    }

    private void mockManager() {
        Mockito.when(entityManager.find(Mockito.eq(TipoDocumento.class), Mockito.anyInt())).thenReturn(mockTipoDocumentoAtributoExtracaoCompleto());
    }
    
    private void mockManagerCompleto() {
        Mockito.when(entityManager.find(Mockito.eq(TipoDocumento.class), Mockito.anyInt())).thenReturn(mockTipoDocumentoAtributoExtracaoOpcaoAtributoCompleto());
    }
    
    private void mockManagerSemAtributo() {
        Mockito.when(entityManager.find(Mockito.eq(TipoDocumento.class), Mockito.anyInt())).thenReturn(mockTipoDocumentoSemAtributo(1));
    }

    private void mockManagerException() {
        PSQLException psqle = new PSQLException("Falha do indice XYZ do SGBD", PSQLState.NO_DATA);
        Mockito.doThrow(new PersistenceException("error", psqle)).when(entityManager).remove(Mockito.any());
    }

    private void mockManagerPersistException() {
        PSQLException psqle = new PSQLException("Falha do indice XYZ do SGBD", PSQLState.NO_DATA);
        Mockito.doThrow(new PersistenceException("error", psqle)).when(entityManager).persist(Mockito.any());
    }

    private void mockManagerMergeException() {
        PSQLException psqle = new PSQLException("Falha do indice XYZ do SGBD", PSQLState.NO_DATA);
        Mockito.doThrow(new PersistenceException("error", psqle)).when(entityManager).merge(Mockito.any());
    }

    private void mockCreateQuery() {
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(TipoDocumento.class))).thenReturn(query);
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Calendar.class))).thenReturn(queryAlteracao);
        Mockito.when(queryAlteracao.getSingleResult()).thenReturn(Calendar.getInstance());
    }

    private void mockResultList() {
        Mockito.when(query.getResultList()).thenReturn(listaTipologia);
    }

    private void mockSingleResult() {
        Mockito.when(query.getSingleResult()).thenReturn(tipo);
    }
    
    private void mockSingleResultNull() {
        Mockito.when(query.getSingleResult()).thenReturn(null);
    }
    
    private void mockSingleResultInvalidoSemClasseGED() {
        TipoDocumento tipoDocumento = mockTipoDocumento(1);
        tipoDocumento.setNomeClasseSIECM(null);
        Mockito.when(query.getSingleResult()).thenReturn(tipoDocumento);
    }

    private void mockAtributoExtracao() {
        atributoExtracao = new AtributoExtracao();
        atributoExtracao.setId(1);
        atributoExtracao.setTipoAtributoGeralEnum(TipoAtributoEnum.BOOLEAN);
        atributoExtracao.setObrigatorioSIECM(Boolean.TRUE);
        atributoExtracao.setNomeAtributoSIECM("NomeGED");
        atributoExtracao.setTipoAtributoSiecmEnum(TipoAtributoEnum.BOOLEAN);
        atributoExtracao.setModoPartilhaEnum(ModoPartilhaEnum.S);
        atributoExtracao.setEstrategiaPartilhaEnum(EstrategiaPartilhaEnum.RECEITA_MAE);
        atributoExtracao.setPresenteDocumento(Boolean.TRUE);
    }

    private void mockAtributoExtracaoCompleto() {
        atributoExtracao = new AtributoExtracao();
        atributoExtracao.setId(1);
        atributoExtracao.setTipoAtributoGeralEnum(TipoAtributoEnum.BOOLEAN);
        atributoExtracao.setTipoCampoEnum(TipoCampoEnum.TEXT);
        atributoExtracao.setObrigatorioSIECM(Boolean.TRUE);
        atributoExtracao.setNomeAtributoSIECM("NomeGED");
        atributoExtracao.setTipoAtributoSiecmEnum(TipoAtributoEnum.BOOLEAN);
        atributoExtracao.setModoPartilhaEnum(ModoPartilhaEnum.S);
        atributoExtracao.setEstrategiaPartilhaEnum(EstrategiaPartilhaEnum.RECEITA_MAE);
        atributoExtracao.setNomeNegocial("Nome Negocial");
        atributoExtracao.setAtivo(true);
        atributoExtracao.setUtilizadoCalculoValidade(Boolean.TRUE);
        atributoExtracao.setNomeAtributoDocumento("Nome Atributo Documento");
        atributoExtracao.setObrigatorio(Boolean.TRUE);
        atributoExtracao.setUtilizadoIdentificadorPessoa(Boolean.TRUE);
        atributoExtracao.setOrdemApresentacao(1);
        atributoExtracao.setPresenteDocumento(Boolean.TRUE);
    }
    
    private void mockAtributoExtracaoOpcaoAtributoCompleto() {
        atributoExtracao = new AtributoExtracao();
        atributoExtracao.setId(1);
        atributoExtracao.setTipoAtributoGeralEnum(TipoAtributoEnum.BOOLEAN);
        atributoExtracao.setTipoCampoEnum(TipoCampoEnum.TEXT);
        atributoExtracao.setObrigatorioSIECM(Boolean.TRUE);
        atributoExtracao.setNomeAtributoSIECM("NomeGED");
        atributoExtracao.setTipoAtributoSiecmEnum(TipoAtributoEnum.BOOLEAN);
        atributoExtracao.setModoPartilhaEnum(ModoPartilhaEnum.S);
        atributoExtracao.setEstrategiaPartilhaEnum(EstrategiaPartilhaEnum.RECEITA_MAE);
        atributoExtracao.setNomeNegocial("Nome Negocial");
        atributoExtracao.setAtivo(true);
        atributoExtracao.setUtilizadoCalculoValidade(Boolean.TRUE);
        atributoExtracao.setNomeAtributoDocumento("Nome Atributo Documento");
        atributoExtracao.setObrigatorio(Boolean.TRUE);
        atributoExtracao.setUtilizadoIdentificadorPessoa(Boolean.TRUE);
        atributoExtracao.setOrdemApresentacao(1);
        atributoExtracao.setPresenteDocumento(Boolean.TRUE);
        atributoExtracao.setOpcoesAtributo(mockOpcoesAtributoExtracao());
    }
    
    private void mockAtributoExtracaoGED() {
        atributoExtracao = new AtributoExtracao();
        atributoExtracao.setId(1);
        atributoExtracao.setTipoAtributoGeralEnum(TipoAtributoEnum.BOOLEAN);
        atributoExtracao.setModoPartilhaEnum(ModoPartilhaEnum.S);
        atributoExtracao.setEstrategiaPartilhaEnum(EstrategiaPartilhaEnum.RECEITA_MAE);
        atributoExtracao.setSicpfCampoEnum(SICPFCampoEnum.CPF);
        atributoExtracao.setNomeAtributoSICLI("AtributoSicli");
        atributoExtracao.setPresenteDocumento(Boolean.TRUE);
    }

    private Set<OpcaoAtributo> mockOpcoesAtributoExtracao() {
        OpcaoAtributo opcaoAtributo = new OpcaoAtributo();
        opcaoAtributo.setId(1);
        Set<OpcaoAtributo> set = new HashSet<>();
        set.add(opcaoAtributo);
        return set;
    }
    
    public TipoDocumentoManutencaoDTO mockTipoDocumentoManutencaoDTO() {
        TipoDocumentoManutencaoDTO tipoDocumentoManutencaoDTO = new TipoDocumentoManutencaoDTO();
        tipoDocumentoManutencaoDTO.setNome("Nome");
        tipoDocumentoManutencaoDTO.setIndicadorUsoApoioNegocio(Boolean.TRUE);
        tipoDocumentoManutencaoDTO.setIndicadorUsoDossieDigital(Boolean.FALSE);
        tipoDocumentoManutencaoDTO.setIndicadorUsoProcessoAdministrativo(Boolean.FALSE);
        tipoDocumentoManutencaoDTO.setTipoPessoa(TipoPessoaEnum.F);
        tipoDocumentoManutencaoDTO.setValidadeAutoContida(Boolean.TRUE);
        tipoDocumentoManutencaoDTO.setPrazoValidadeDias(2);
        tipoDocumentoManutencaoDTO.setCodigoTipologia("1234ABCD");
        tipoDocumentoManutencaoDTO.setNomeClasseSIECM("Nome Classe GED");
        tipoDocumentoManutencaoDTO.setIndicadorReuso(Boolean.TRUE);
        tipoDocumentoManutencaoDTO.setNomeArquivoMinuta("Arquivo Minuta");
        tipoDocumentoManutencaoDTO.setIndicadorValidacaoCadastral(Boolean.TRUE);
        tipoDocumentoManutencaoDTO.setIndicadorValidacaoDocumental(Boolean.TRUE);
        tipoDocumentoManutencaoDTO.setIndicadorValidacaoSicod(Boolean.TRUE);
        tipoDocumentoManutencaoDTO.setIndicadorExtracaoExterna(Boolean.TRUE);
        tipoDocumentoManutencaoDTO.setIndicadorExtracaoM0(Boolean.TRUE);
        tipoDocumentoManutencaoDTO.setAvatar("glyphicon glyphicon-list");
        tipoDocumentoManutencaoDTO.setCorRGB("FFCC00");
        List<Integer> inteiros = new ArrayList<>();
        inteiros.add(1);
        tipoDocumentoManutencaoDTO.setIdentificadoresFuncaoDocumentalExclusaoVinculo(inteiros);
        tipoDocumentoManutencaoDTO.setIdentificadoresFuncaoDocumentalInclusaoVinculo(inteiros);
        tipoDocumentoManutencaoDTO.setTags(Arrays.asList("tags"));
        return tipoDocumentoManutencaoDTO;
    }

    public TipoDocumentoManutencaoDTO mockTipoDocumentoManutencaoDTODossie() {
        TipoDocumentoManutencaoDTO tipoDocumentoManutencaoDTO = new TipoDocumentoManutencaoDTO();
        tipoDocumentoManutencaoDTO.setNome("Nome");
        tipoDocumentoManutencaoDTO.setIndicadorUsoApoioNegocio(Boolean.FALSE);
        tipoDocumentoManutencaoDTO.setIndicadorUsoDossieDigital(Boolean.TRUE);
        tipoDocumentoManutencaoDTO.setIndicadorUsoProcessoAdministrativo(Boolean.FALSE);
        tipoDocumentoManutencaoDTO.setTipoPessoa(TipoPessoaEnum.F);
        tipoDocumentoManutencaoDTO.setValidadeAutoContida(Boolean.TRUE);
        tipoDocumentoManutencaoDTO.setPrazoValidadeDias(2);
        tipoDocumentoManutencaoDTO.setCodigoTipologia("1234ABCD");
        tipoDocumentoManutencaoDTO.setIndicadorReuso(Boolean.TRUE);
        tipoDocumentoManutencaoDTO.setIndicadorUsoApoioNegocio(Boolean.TRUE);
        tipoDocumentoManutencaoDTO.setNomeArquivoMinuta("Arquivo Minuta");
        tipoDocumentoManutencaoDTO.setIndicadorValidacaoCadastral(Boolean.TRUE);
        tipoDocumentoManutencaoDTO.setIndicadorValidacaoDocumental(Boolean.TRUE);
        tipoDocumentoManutencaoDTO.setIndicadorExtracaoExterna(Boolean.TRUE);
        tipoDocumentoManutencaoDTO.setIndicadorExtracaoM0(Boolean.TRUE);
        tipoDocumentoManutencaoDTO.setAvatar("glyphicon glyphicon-list");
        tipoDocumentoManutencaoDTO.setCorRGB("FFCC00");
        List<Integer> inteiros = new ArrayList<>();
        inteiros.add(1);
        tipoDocumentoManutencaoDTO.setIdentificadoresFuncaoDocumentalExclusaoVinculo(inteiros);
        tipoDocumentoManutencaoDTO.setIdentificadoresFuncaoDocumentalInclusaoVinculo(inteiros);
        tipoDocumentoManutencaoDTO.setTags(Arrays.asList("tags"));

        return tipoDocumentoManutencaoDTO;
    }

    public AtributoExtracaoManutencaoDTO mockAtributoExtracaoManutencaoDTO(Integer indicadorPartilha) {
        AtributoExtracaoManutencaoDTO atributoExtracaoManutencaoDTO = new AtributoExtracaoManutencaoDTO();
        atributoExtracaoManutencaoDTO.setNomeAtributoNegocial("Nome Negocial");
        atributoExtracaoManutencaoDTO.setNomeAtributoRetorno("Nome Atributo");
        atributoExtracaoManutencaoDTO.setNomeAtributoSIECM("Nome GED");
        atributoExtracaoManutencaoDTO.setNomeAtributoDocumento("Nome Atributo");
        atributoExtracaoManutencaoDTO.setNomeObjetoSICLI("Nome SICLI");
        atributoExtracaoManutencaoDTO.setNomeAtributoSICLI("Nome Atributo SICLI");
        atributoExtracaoManutencaoDTO.setAtivo(Boolean.TRUE);
        atributoExtracaoManutencaoDTO.setCalculoData(Boolean.TRUE);
        atributoExtracaoManutencaoDTO.setObrigatorio(Boolean.TRUE);
        atributoExtracaoManutencaoDTO.setObrigatorioSIECM(Boolean.TRUE);
        atributoExtracaoManutencaoDTO.setIdentificadorPessoa(Boolean.TRUE);
        atributoExtracaoManutencaoDTO.setSicpfCampoEnum(SICPFCampoEnum.CPF);
        atributoExtracaoManutencaoDTO.setSicpfModoEnum(SICPFModoEnum.E);
        atributoExtracaoManutencaoDTO.setTipoCampoEnum(TipoCampoEnum.CNPJ);
        atributoExtracaoManutencaoDTO.setTipoAtributoGeral(TipoAtributoEnum.STRING);
        atributoExtracaoManutencaoDTO.setTipoAtributoSIECM(TipoAtributoEnum.STRING);
        atributoExtracaoManutencaoDTO.setTipoAtributoSICLI(TipoAtributoEnum.STRING);
        atributoExtracaoManutencaoDTO.setPercentualAlteracaoPermitido(80);
        atributoExtracaoManutencaoDTO.setModoPartilhaEnum(ModoPartilhaEnum.S);
        atributoExtracaoManutencaoDTO.setEstrategiaPartilhaEnum(EstrategiaPartilhaEnum.RECEITA_MAE);
        atributoExtracaoManutencaoDTO.setPresenteDocumento(Boolean.TRUE);
        atributoExtracaoManutencaoDTO.setIdentificadorAtributoPartilha(indicadorPartilha);
        return atributoExtracaoManutencaoDTO;
    }

}
