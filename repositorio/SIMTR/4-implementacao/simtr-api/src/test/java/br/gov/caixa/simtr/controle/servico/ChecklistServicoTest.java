package br.gov.caixa.simtr.controle.servico;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.helper.CadastroHelper;
import br.gov.caixa.simtr.modelo.entidade.Apontamento;
import br.gov.caixa.simtr.modelo.entidade.Checklist;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist.AlteracaoApontamentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist.CadastroChecklistDTO;
public class ChecklistServicoTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    Query queryPersistChecklist;
    
    @Mock
    TypedQuery<Checklist> query;
    
    @Mock
    Query queryChecklistsVO;
    
    @Mock
    TypedQuery<Calendar> queryDataHoraUltimaAlteracao;
    
    @Mock
    Query queryContagemAssociados;
    
    @Mock
    CadastroHelper cadastroHelper;
    
    @Mock
    private final Map<Integer, Checklist> mapaChecklists = new HashMap<>();
    
    @Mock
    Map<String, List<String>> mapaPendencias = new HashMap<>();

    @InjectMocks
    ChecklistServico checklistservico = new ChecklistServico();
    
    @Mock
    private VinculacaoChecklistServico vinculacaoChecklistServico;
    
    @Mock
    private ApontamentoServico apontamentoServico;
    

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_DIR = "/mock/checklist/";
    private static final String UTF_8 = "UTF-8";

    private Checklist checklistIdentificacao;

    @Before
    public void init() throws IOException {
        String jsonChecklistIdentificacao = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("checklist-identificacao.json")), UTF_8);
        this.checklistIdentificacao = mapper.readValue(jsonChecklistIdentificacao, Checklist.class);
        
        MockitoAnnotations.initMocks(this);
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Checklist.class))).thenReturn(query);
        Mockito.when(this.query.getSingleResult()).thenReturn(this.checklistIdentificacao);

    }

    @Test
    public void testGetEntityManager() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method metodoGetEntitymanager = this.checklistservico.getClass().getDeclaredMethod("getEntityManager");
        metodoGetEntitymanager.setAccessible(Boolean.TRUE);

        metodoGetEntitymanager.invoke(this.checklistservico);
    }

    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void testGetByIdCasoIdNulo() {
        this.checklistservico.getById(null, true, true);
    }

    @Test
    public void testGetById() {
        //Consulta Checklist Completo
        Assert.assertNotNull(this.checklistservico.getById(1, Boolean.TRUE, Boolean.TRUE));
        
        //Consulta Checklist apenas com vinculo de Checklists associados
        Assert.assertNotNull(this.checklistservico.getById(1, Boolean.TRUE, Boolean.FALSE));
        
        //Consulta Checklist apenas com vinculo de Documentos por processo
        Assert.assertNotNull(this.checklistservico.getById(1, Boolean.FALSE, Boolean.TRUE));
    }

    @Test
    public void testGetByIdCasoRegistroNaoEncontrado() {
        Mockito.doThrow(new NoResultException("Registro Não Encontrado")).when(query).getSingleResult();
        Assert.assertNull(this.checklistservico.getById(1, false, false));
    }
    
    @Test
    public void testCarregaTodosChecklistsCadastrados() {
        List<Object[]> listaContagemAssociados = new ArrayList<>();
        Integer chaveChecklist = 1;
        Long contagemAssociacoes = 2L;
        Object[] tupla = new Object[2];
        tupla[0] = chaveChecklist;
        tupla[1] = contagemAssociacoes;
        listaContagemAssociados.add(tupla);
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Calendar.class))).thenReturn(queryDataHoraUltimaAlteracao);
        Mockito.when(queryDataHoraUltimaAlteracao.getSingleResult()).thenReturn(Calendar.getInstance());
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryChecklistsVO);
        Mockito.when(queryChecklistsVO.getResultList()).thenReturn(listaContagemAssociados);
        Assert.assertNotNull(this.checklistservico.carregaChecklists());
    }
    
    @Test
    public void testCarregaChecklistById() {
        Object associacoes = 1L;
        Object[] resultadoConsulta = new Object[2];
        resultadoConsulta[0] = null;
        resultadoConsulta[1] = associacoes;
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Calendar.class))).thenReturn(queryDataHoraUltimaAlteracao);
        Mockito.when(queryDataHoraUltimaAlteracao.getSingleResult()).thenReturn(Calendar.getInstance());
        
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.setParameter(0, 1)).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.getSingleResult()).thenReturn(resultadoConsulta);
        
        Assert.assertNotNull(this.checklistservico.carregaChecklistById(1));
    }
    
    @Test
    public void testExcecaoChecklistById() {
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Calendar.class))).thenReturn(queryDataHoraUltimaAlteracao);
        Mockito.when(queryDataHoraUltimaAlteracao.getSingleResult()).thenReturn(Calendar.getInstance());
        
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.setParameter(0, 1)).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.getSingleResult()).thenThrow(new NoResultException());
        
        Assert.assertNull(this.checklistservico.carregaChecklistById(1));
    }
    
    @Test
    public void testSalvaNovoChecklist() {
        Checklist checklist = new Checklist();
        checklist.setIndicacaoVerificacaoPrevia(null);
        this.checklistservico.criaNovoChecklist(checklist);
    }
    
    @Test()
    public void testAlteraChecklist() {
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Calendar.class))).thenReturn(queryDataHoraUltimaAlteracao);
        Mockito.when(queryDataHoraUltimaAlteracao.getSingleResult()).thenReturn(null);
        
        List<Checklist> lista = new ArrayList<>();
        Checklist checklist = new Checklist();
        checklist.setId(1);
        lista.add(checklist);
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Checklist.class))).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(lista);
        
        Object associacoes = 0L;
        Object[] resultadoConsulta = new Object[2];
        resultadoConsulta[0] = null;
        resultadoConsulta[1] = associacoes;
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.setParameter(0, 1)).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.getSingleResult()).thenReturn(resultadoConsulta);
        
        CadastroChecklistDTO checklistDTO = new CadastroChecklistDTO();
        checklistDTO.setNomeChecklist("teste");
        checklistDTO.setUnidadeResponsavel(1234);
        checklistDTO.setVerificacaoPrevia(false);
        checklistDTO.setOrientacaoOperador("teste");
        this.checklistservico.alteraChecklist(1, checklistDTO);
    }
    
    @Test(expected=SimtrEstadoImpeditivoException.class)
    public void testExcecaoEstadoImpeditivoChecklist() {
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Calendar.class))).thenReturn(queryDataHoraUltimaAlteracao);
        Mockito.when(queryDataHoraUltimaAlteracao.getSingleResult()).thenReturn(null);
        
        List<Checklist> lista = new ArrayList<>();
        Checklist checklist = new Checklist();
        checklist.setId(1);
        lista.add(checklist);
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Checklist.class))).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(lista);
        
        Object associacoes = 1L;
        Object[] resultadoConsulta = new Object[2];
        resultadoConsulta[0] = null;
        resultadoConsulta[1] = associacoes;
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.setParameter(0, 1)).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.getSingleResult()).thenReturn(resultadoConsulta);
        
        this.checklistservico.alteraChecklist(1, new CadastroChecklistDTO());
    }
    
    @Test(expected=SimtrRequisicaoException.class)
    public void testExcecaoAlteraChecklist() {
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Calendar.class))).thenReturn(queryDataHoraUltimaAlteracao);
        Mockito.when(queryDataHoraUltimaAlteracao.getSingleResult()).thenReturn(Calendar.getInstance());
        
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.setParameter(0, 1)).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.getSingleResult()).thenThrow(new NoResultException());
        
        this.checklistservico.alteraChecklist(1, new CadastroChecklistDTO());
    }
    
    @Test()
    public void testExcluiChecklistLogicamente() {
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Calendar.class))).thenReturn(queryDataHoraUltimaAlteracao);
        Mockito.when(queryDataHoraUltimaAlteracao.getSingleResult()).thenReturn(null);
        
        List<Checklist> lista = new ArrayList<>();
        Checklist checklist = new Checklist();
        checklist.setId(1);
        lista.add(checklist);
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Checklist.class))).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(lista);
        
        Object associacoes = 1L;
        Object[] resultadoConsulta = new Object[2];
        resultadoConsulta[0] = null;
        resultadoConsulta[1] = associacoes;
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.setParameter(0, 1)).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.getSingleResult()).thenReturn(resultadoConsulta);

        this.checklistservico.excluiChecklist(1);
    }
    
    @Test()
    public void testExcluiChecklistFisicamente() {
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Calendar.class))).thenReturn(queryDataHoraUltimaAlteracao);
        Mockito.when(queryDataHoraUltimaAlteracao.getSingleResult()).thenReturn(null);
        
        List<Checklist> lista = new ArrayList<>();
        Checklist checklist = new Checklist();
        checklist.setId(1);
        lista.add(checklist);
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Checklist.class))).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(lista);
        
        Object associacoes = 0L;
        Object[] resultadoConsulta = new Object[2];
        resultadoConsulta[0] = null;
        resultadoConsulta[1] = associacoes;
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.setParameter(0, 1)).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.getSingleResult()).thenReturn(resultadoConsulta);

        this.checklistservico.excluiChecklist(1);
    }
    
    @Test()
    public void testCadastraApontamentoChecklist() {
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Calendar.class))).thenReturn(queryDataHoraUltimaAlteracao);
        Mockito.when(queryDataHoraUltimaAlteracao.getSingleResult()).thenReturn(null);
        
        List<Checklist> lista = new ArrayList<>();
        Checklist checklist = new Checklist();
        checklist.setId(1);
        lista.add(checklist);
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Checklist.class))).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(lista);
        
        Object associacoes = 0L;
        Object[] resultadoConsulta = new Object[2];
        resultadoConsulta[0] = null;
        resultadoConsulta[1] = associacoes;
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.setParameter(0, 1)).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.getSingleResult()).thenReturn(resultadoConsulta);

        this.checklistservico.cadastraApontamentoChecklistAtual(1, new Apontamento());
    }
    
    @Test()
    public void testAlteraApontamentoChecklist() {
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Calendar.class))).thenReturn(queryDataHoraUltimaAlteracao);
        Mockito.when(queryDataHoraUltimaAlteracao.getSingleResult()).thenReturn(null);
        
        List<Checklist> lista = new ArrayList<>();
        Checklist checklist = new Checklist();
        checklist.setId(1);
        lista.add(checklist);
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Checklist.class))).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(lista);
        
        Object associacoes = 0L;
        Object[] resultadoConsulta = new Object[2];
        resultadoConsulta[0] = null;
        resultadoConsulta[1] = associacoes;
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.setParameter(0, 1)).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.getSingleResult()).thenReturn(resultadoConsulta);

        this.checklistservico.alteraApontamentoChecklistAtual(1, 1L, new AlteracaoApontamentoDTO());
    }
    
    @Test()
    public void testExcluiApontamentoChecklist() {
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Calendar.class))).thenReturn(queryDataHoraUltimaAlteracao);
        Mockito.when(queryDataHoraUltimaAlteracao.getSingleResult()).thenReturn(null);
        
        List<Checklist> lista = new ArrayList<>();
        Checklist checklist = new Checklist();
        checklist.setId(1);
        lista.add(checklist);
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Checklist.class))).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(lista);
        
        Object associacoes = 0L;
        Object[] resultadoConsulta = new Object[2];
        resultadoConsulta[0] = null;
        resultadoConsulta[1] = associacoes;
        Mockito.when(entityManager.createQuery(Mockito.anyString())).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.setParameter(0, 1)).thenReturn(queryContagemAssociados);
        Mockito.when(queryContagemAssociados.getSingleResult()).thenReturn(resultadoConsulta);

        this.checklistservico.deletaApontamentoChecklistAtual(1, 1L);
    }
}
