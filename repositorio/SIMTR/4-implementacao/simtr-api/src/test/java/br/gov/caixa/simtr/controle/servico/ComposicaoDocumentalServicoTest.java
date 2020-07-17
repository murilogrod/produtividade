package br.gov.caixa.simtr.controle.servico;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.simtr.controle.excecao.DossieException;
import br.gov.caixa.simtr.modelo.entidade.ComposicaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.RegraDocumental;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.composicaodocumental.AlterarComposicaoDocumentalDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.composicaodocumental.ComposicaoDocumentalDTO;

public class ComposicaoDocumentalServicoTest {

    @Mock
    private EntityManager manager;

    @Mock
    private Logger logger;

    @Mock
    private TypedQuery<ComposicaoDocumental> query;

    @Mock
    private TypedQuery<Calendar> queryAlteracao;

    @Mock
    private TypedQuery<FuncaoDocumental> queryFuncaoDocumental;

    @Mock
    private TypedQuery<TipoDocumento> queryTipoDocumento;

    @Mock
    private KeycloakService keycloakService;

    @InjectMocks
    private ComposicaoDocumentalServico servico;

    @Mock
    private TypedQuery<RegraDocumental> queryRegraDocumental;

    @InjectMocks
    private RegraDocumentalServico regraDocumentalServico;

    ComposicaoDocumental composicao;

    ComposicaoDocumentalDTO composicaoDocumentalDTO;

    AlterarComposicaoDocumentalDTO alterarComposicaoDocumentalDTO;
    
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_DIR = "/mock/composicao-documental/";
    private static final String UTF_8 = "UTF-8";

    @Before
    public void init() throws JsonParseException, JsonMappingException, IOException {
        MockitoAnnotations.initMocks(this);
        
        this.composicao = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("composicao-ativa-1.json")), UTF_8), ComposicaoDocumental.class);

        List<ComposicaoDocumental> listaComposicoes = Arrays.asList(this.composicao);

        Mockito.when(query.getResultList()).thenReturn(listaComposicoes);
        Mockito.when(queryAlteracao.getSingleResult()).thenReturn(Calendar.getInstance());
        
        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(ComposicaoDocumental.class))).thenReturn(query);
        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(RegraDocumental.class))).thenReturn(queryRegraDocumental);
        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(Calendar.class))).thenReturn(queryAlteracao);
    }

    @Test
    public void getRegraDocumental() {
        Mockito.when(this.queryRegraDocumental.getSingleResult()).thenReturn(new RegraDocumental());
        this.regraDocumentalServico.getById(1);
    }

    @Test
    public void deleteRegraDocumental() {
        RegraDocumental regraDocumental = new RegraDocumental();
        regraDocumental.setId(1L);
        Mockito.when(this.manager.find(Mockito.eq(RegraDocumental.class), Mockito.anyInt())).thenReturn(regraDocumental);
        Mockito.doNothing().when(this.manager).remove(regraDocumental);

        this.regraDocumentalServico.delete(1L);
    }

    @Test(expected = DossieException.class)
    public void deleteRegraDocumentalDossieException() {
        RegraDocumental regraDocumental = new RegraDocumental();
        regraDocumental.setId(1L);
        Mockito.when(this.manager.find(Mockito.eq(RegraDocumental.class), Mockito.anyInt())).thenReturn(regraDocumental);
        Mockito.doThrow(new RuntimeException("Falha ao exluir regra.")).when(this.manager).remove(regraDocumental);

        this.regraDocumentalServico.delete(1L);
    }

    @Test
    public void listComposicaoDocumentalTestSucess() {

        mockQuery();
        mockResultList();

        assertNotNull(servico.list());
    }

    @Test
    public void getByIdSucess() {
        mockComposicaoDocumental();
        mockQuery();
        mockSingleResult();

        assertNotNull(servico.getById(1l));
    }

    @Test
    public void getComposicoesByProdutoTest() {
        mockQuery();
        mockResultList();
        assertNotNull(servico.getComposicoesByProduto(1, 1, true));
    }

    @Test
    public void getComposicoesByProdutoTestFalse() {
        mockQuery();
        mockResultList();
        assertNotNull(servico.getComposicoesByProduto(1, 1, false));
    }

    @Test
    public void salvarTestSuccess() {
        ComposicaoDocumental compDoc = new ComposicaoDocumental();
        Set<RegraDocumental> regras = new HashSet<>();
        RegraDocumental regra = new RegraDocumental();
        regra.setId(1L);
        regra.setFuncaoDocumental(new FuncaoDocumental());
        regras.add(regra);

        regra = new RegraDocumental();
        regra.setId(2L);
        regra.setTipoDocumento(new TipoDocumento());

        regras.add(regra);
        compDoc.setRegrasDocumentais(regras);

        mockQuery();
        mockSingleResult();
        servico.salvar(compDoc);
    }

    @Test
    public void updateRegrasDocumentaisTest() {
        mockComposicaoDocumental();
        mockQuery();
        mockSingleResult();
        mockQueryFuncaoDocumental();
        mockResultListFuncaoDocumental();
        mockQueryTipoDocumento();
        mockResultListTipoDocumento();
        mockRegrasDocumentais();
        List<RegraDocumental> regras = new ArrayList<>();
        RegraDocumental regra = new RegraDocumental();
        regra.setId(1L);
        regra.setFuncaoDocumental(new FuncaoDocumental());
        regras.add(regra);

        regra = new RegraDocumental();
        regra.setId(2L);
        regra.setTipoDocumento(new TipoDocumento());

        regras.add(regra);
        servico.updateRegrasDocumentais(1L, regras);;

    }

    @Test(expected = DossieException.class)
    public void updateRegrasDocumentaisTestException() {
        mockQuery();
        mockSingleResultNRE();
        servico.updateRegrasDocumentais(1L, null);

    }

    @Test
    public void aplicarPatchTest() {
        mockQuery();
        mockFind();
        mockDTO();
        mockAlterarDTO();
        servico.aplicarPatch(1L, alterarComposicaoDocumentalDTO);
    }

    @Test
    public void aplicarPatchTestDTOVazio() {
        mockQuery();
        mockFind();
        servico.aplicarPatch(1L, new AlterarComposicaoDocumentalDTO());
    }

    private void mockDTO() {
        composicaoDocumentalDTO = new ComposicaoDocumentalDTO();
        composicaoDocumentalDTO.setDataHoraInclusao(Calendar.getInstance());
        composicaoDocumentalDTO.setDataHoraRevogacao(Calendar.getInstance());
        composicaoDocumentalDTO.setIndicadorConclusao(Boolean.TRUE);
        composicaoDocumentalDTO.setMatriculaInclusao("C999999");
        composicaoDocumentalDTO.setMatriculaRevogacao("c999999");
        composicaoDocumentalDTO.setNome("Nome");
    }

    private void mockAlterarDTO() {
        alterarComposicaoDocumentalDTO = new AlterarComposicaoDocumentalDTO();
        alterarComposicaoDocumentalDTO.setIndicadorConclusao(Boolean.TRUE);
        alterarComposicaoDocumentalDTO.setNome("Nome");
    }

    private void removeError() {
        Mockito.doThrow(new RuntimeException("error")).when(manager).remove(Mockito.any());
    }

    private void mockQuery() {
        when(manager.createQuery(anyString(), eq(ComposicaoDocumental.class))).thenReturn(query);
    }

    private void mockFind() {
        when(manager.find(eq(ComposicaoDocumental.class), Mockito.any())).thenReturn(new ComposicaoDocumental());
    }

    private void mockQueryFuncaoDocumental() {
        when(manager.createQuery(anyString(), eq(FuncaoDocumental.class))).thenReturn(queryFuncaoDocumental);
    }

    private void mockQueryTipoDocumento() {
        when(manager.createQuery(anyString(), eq(TipoDocumento.class))).thenReturn(queryTipoDocumento);
    }

    private void mockResultList() {
        when(query.getResultList()).thenReturn(new ArrayList<>());
    }

    private void mockResultListFuncaoDocumental() {
        List<FuncaoDocumental> funcoes = new ArrayList<>();
        funcoes.add(new FuncaoDocumental());
        when(queryFuncaoDocumental.getResultList()).thenReturn(funcoes);
    }

    private void mockResultListTipoDocumento() {
        List<TipoDocumento> tipos = new ArrayList<>();
        tipos.add(new TipoDocumento());
        when(queryTipoDocumento.getResultList()).thenReturn(tipos);
    }

    private void mockSingleResult() {
        when(query.getSingleResult()).thenReturn(composicao);
    }

    private void mockSingleResultNRE() {
        Mockito.doThrow(NoResultException.class).when(query).getSingleResult();
    }

    private void mockComposicaoDocumental() {
        composicao = new ComposicaoDocumental();
        composicao.setId(1L);
    }

    private void mockRegrasDocumentais() {
        RegraDocumental regra = new RegraDocumental();
        regra.setId(1L);
        composicao.addRegrasDocumentais(regra);
    }

}
