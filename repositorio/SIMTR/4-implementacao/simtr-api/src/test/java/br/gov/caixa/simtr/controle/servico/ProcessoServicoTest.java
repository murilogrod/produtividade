package br.gov.caixa.simtr.controle.servico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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
import br.gov.caixa.simtr.modelo.entidade.DocumentoGarantia;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.ProcessoDocumento;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.entidade.VinculacaoChecklist;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDossieEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.processo.ManterProcessoDTO;
import io.jsonwebtoken.lang.Assert;
import java.util.Calendar;
import javax.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

public class ProcessoServicoTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    TypedQuery<Processo> query;

    @Mock
    TypedQuery<Calendar> queryAlteracao;

    @Mock
    Query querySimples;

    @Mock
    TypedQuery<Produto> queryProduto;

    @Mock
    TypedQuery<DocumentoGarantia> queryDocumentoGarantia;

    @Mock
    TypedQuery<ProcessoDocumento> queryProcessoDocumento;

    @Mock
    TypedQuery<VinculacaoChecklist> queryVinculacaoChecklist;

    @InjectMocks
    private ProcessoServico servico = new ProcessoServico();

    @Mock
    private DossieProdutoServico dossieProdutoServico;

    @Mock
    private KeycloakService keycloackService;

    @Mock
    private Logger logger;

    @Mock
    private final Map<Integer, Processo> mapaProcessos = new HashMap<>();

    List<Processo> processosPatriarca;

    List<ProcessoDocumento> processosDocumentos;

    List<VinculacaoChecklist> vinculacoes;

    List<DocumentoGarantia> documentosGarantia;

    List<Produto> produtos;

    Processo processo;

    List<DossieProduto> dossiesProdutos;

    ManterProcessoDTO processoDTO;

    @Before
    public void loadMockito() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetById() {
        mockQuery();
        mockQueryAlteracao();
        mockQueryDocumentoGarantia();
        mockQueryProduto();
        mockQueryProcessoDocumento();
        mockProcessoPessoaFisica();
        mockSingleResult();
        mockListaProcessos();
        mockDocumentosGarantia();
        mockCarregarListaProdutos();
        mockResultListProduto();
        mockResultList();
        mockVinculaChecklist();
        mockListVinculacoes();
        mockListProcessosDocumentos();
        mockResultListProcesso();
        mockResultListDocumentoGarantia();
        mockVinculaChecklistResultList();
        Assert.notNull(servico.getById(1));
    }

    @Test
    public void testGetByNome() {
        mockQuery();
        mockQueryAlteracao();
        mockQueryDocumentoGarantia();
        mockQueryProduto();
        mockQueryProcessoDocumento();
        mockProcessoPessoaFisica();
        mockSingleResult();
        mockListaProcessos();
        mockDocumentosGarantia();
        mockCarregarListaProdutos();
        mockResultListProduto();
        mockResultList();
        mockVinculaChecklist();
        mockListVinculacoes();
        mockListProcessosDocumentos();
        mockResultListProcesso();
        mockResultListDocumentoGarantia();
        mockVinculaChecklistResultList();
        Assert.notNull(servico.getByNome("Nome Processo"));
    }

    @Test
    public void testlistPatriarcas() {
        mockQuery();
        mockQueryAlteracao();
        mockQueryDocumentoGarantia();
        mockQueryProduto();
        mockQueryProcessoDocumento();
        mockProcessoPessoaFisica();
        mockSingleResult();
        mockListaProcessos();
        mockDocumentosGarantia();
        mockCarregarListaProdutos();
        mockResultListProduto();
        mockResultList();
        mockVinculaChecklist();
        mockListVinculacoes();
        mockListProcessosDocumentos();
        mockResultListProcesso();
        mockResultListDocumentoGarantia();
        mockVinculaChecklistResultList();
        servico.listPatriarcas();
    }

    @Test
    public void listaProcessosTratamentoTest() {
        mockListaProcessos();
        mockProcessoPessoaFisica();
        mockQuery();
        mockQueryAlteracao();
        mockSingleResult();
        mockResultList();
        mockDossieProduto();
        servico.listaProcessosPorSituacao(SituacaoDossieEnum.AGUARDANDO_TRATAMENTO);
    }

    @Test
    public void listaProcessosTratamentoTestNRE() {
        mockListaProcessos();
        mockQuery();
        mockQueryAlteracao();
        mockResultList();
        mockDossieProdutoNRE();
        servico.listaProcessosPorSituacao(SituacaoDossieEnum.AGUARDANDO_TRATAMENTO);
    }

    @Test
    public void testSalvar() {
        mockQuery();
        mockQueryDocumentoGarantia();
        mockQueryProduto();
        mockQueryProcessoDocumento();
        mockProcessoPessoaFisica();
        mockSingleResult();
        mockListaProcessos();
        mockDocumentosGarantia();
        mockCarregarListaProdutos();
        mockResultListProduto();
        mockResultList();
        mockVinculaChecklist();
        mockListVinculacoes();
        mockListProcessosDocumentos();
        mockResultListProcesso();
        mockResultListDocumentoGarantia();
        mockVinculaChecklistResultList();
        servico.save(processo);
    }

    @Test(expected = PersistenceException.class)
    public void testSalvarException() {
        mockQuery();
        mockQueryDocumentoGarantia();
        mockQueryProduto();
        mockQueryProcessoDocumento();
        mockProcessoPessoaFisica();
        mockSingleResult();
        mockListaProcessos();
        mockDocumentosGarantia();
        mockCarregarListaProdutos();
        mockResultListProduto();
        mockResultList();
        mockVinculaChecklist();
        mockListVinculacoes();
        mockListProcessosDocumentos();
        mockResultListProcesso();
        mockResultListDocumentoGarantia();
        mockVinculaChecklistResultList();
        mockFalhaEntity("fk_mtrtb020_mtrtb026");
        servico.save(processo);
    }

    @Test
    public void testAplicarPatch() {
        mockQuery();
        mockQueryAlteracao();
        mockQueryDocumentoGarantia();
        mockQueryProduto();
        mockQueryProcessoDocumento();
        mockProcessoPessoaFisica();
        mockSingleResult();
        mockListaProcessos();
        mockDocumentosGarantia();
        mockCarregarListaProdutos();
        mockResultListProduto();
        mockResultList();
        mockVinculaChecklist();
        mockListVinculacoes();
        mockListProcessosDocumentos();
        mockResultListProcesso();
        mockResultListDocumentoGarantia();
        mockVinculaChecklistResultList();
        mockManterProcessoDTO();
        servico.getById(1);//Força o carregamento dos mapas
        servico.aplicarPatch(processoDTO);
    }

    @Test
    public void testAplicarPatchVazio() {
        mockQuery();
        mockQueryDocumentoGarantia();
        mockQueryProduto();
        mockQueryProcessoDocumento();
        mockProcessoPessoaFisica();
        mockSingleResult();
        mockListaProcessos();
        mockDocumentosGarantia();
        mockCarregarListaProdutos();
        mockResultListProduto();
        mockResultList();
        mockVinculaChecklist();
        mockListVinculacoes();
        mockListProcessosDocumentos();
        mockResultListProcesso();
        mockResultListDocumentoGarantia();
        mockVinculaChecklistResultList();
        mockManterProcessoDTO();
        servico.aplicarPatch(new ManterProcessoDTO());
    }

    private void mockFalhaEntity(String constraintName) {
        PSQLException psqle = new PSQLException(constraintName, PSQLState.DATA_ERROR);
        ConstraintViolationException ce = new ConstraintViolationException("msg", psqle, constraintName);
        PersistenceException pe = new PersistenceException("pe", ce);

        Mockito.doThrow(pe).when(entityManager).persist(Mockito.any());;
    }

    private void mockDocumentosGarantia() {
        documentosGarantia = new ArrayList<>();
        DocumentoGarantia doc = new DocumentoGarantia();
        doc.setId(1L);
        documentosGarantia.add(doc);
    }

    private void mockCarregarListaProdutos() {
        produtos = new ArrayList<>();
        Produto produto = new Produto();
        produto.setId(1);
        produtos.add(produto);
    }

    private void mockListVinculacoes() {
        vinculacoes = new ArrayList<>();
        VinculacaoChecklist vinculacao = new VinculacaoChecklist();
        Processo processo = new Processo();
        processo.setId(1);
        vinculacao.setProcessoDossie(processo);
        processo = new Processo();
        processo.setId(2);
        vinculacao.setProcessoFase(processo);
        vinculacoes.add(vinculacao);

    }

    private void mockListProcessosDocumentos() {
        processosDocumentos = new ArrayList<>();
        ProcessoDocumento processo = new ProcessoDocumento();
        processo.setId(1L);
        processosDocumentos.add(processo);
    }

    private void mockSingleResult() {
        Mockito.when(query.getSingleResult()).thenReturn(processo);
    }

    private void mockResultList() {
        Mockito.when(query.getResultList()).thenReturn(processosPatriarca);
    }

    private void mockResultListDocumentoGarantia() {
        Mockito.when(queryDocumentoGarantia.getResultList()).thenReturn(documentosGarantia);
    }

    private void mockResultListProcesso() {
        Mockito.when(queryProcessoDocumento.getResultList()).thenReturn(processosDocumentos);
    }

    private void mockResultListProduto() {
        Mockito.when(queryProduto.getResultList()).thenReturn(produtos);
    }

    private void mockQuery() {
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Processo.class))).thenReturn(query);
    }

    private void mockQueryAlteracao() {
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Calendar.class))).thenReturn(queryAlteracao);
        Mockito.when(queryAlteracao.getSingleResult()).thenReturn(Calendar.getInstance());
    }

    private void mockQueryProduto() {
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Produto.class))).thenReturn(queryProduto);
    }

    private void mockQueryProcessoDocumento() {
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(ProcessoDocumento.class))).thenReturn(queryProcessoDocumento);
    }

    private void mockQueryDocumentoGarantia() {
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(DocumentoGarantia.class))).thenReturn(queryDocumentoGarantia);
    }

    private void mockVinculaChecklist() {
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(VinculacaoChecklist.class))).thenReturn(queryVinculacaoChecklist);
    }

    private void mockVinculaChecklistResultList() {
        Mockito.when(queryVinculacaoChecklist.getResultList()).thenReturn(vinculacoes);
    }

    private void mockProcessoPessoaFisica() {
        processo = new Processo();
        processo.setId(1);
        processo.setAvatar("Avatar");
        processo.setIndicadorGeracaoDossie(Boolean.TRUE);
        processo.setControlaValidade(Boolean.TRUE);
        processo.setNome("Nome Processo");
        processo.setTipoPessoa(TipoPessoaEnum.F);
        processo.setNomeContainerBPM("NomeContainerBPM");
        processo.setNomeProcessoBPM("Nome Processo BPM");
    }

    private void mockListaProcessos() {
        processosPatriarca = new ArrayList<>();
        Processo processo = new Processo();
        processo.setId(1);
        processo.setNome("Processo1");
        processo.setIndicadorGeracaoDossie(Boolean.TRUE);
        processosPatriarca.add(processo);
        processo = new Processo();
        processo.setNome("processo2");
        processo.setId(2);
        processo.setIndicadorGeracaoDossie(Boolean.FALSE);
        processosPatriarca.add(processo);
    }

    private void mockDossieProdutoNRE() {
        Mockito.doThrow(NoResultException.class).when(dossieProdutoServico).listByProcessoAndSituacao(Mockito.any(Processo.class), Mockito.any(SituacaoDossieEnum.class));
    }

    private void mockDossieProduto() {
        Mockito.when(this.dossieProdutoServico.listByProcessoAndSituacao(Mockito.any(Processo.class), Mockito.any(SituacaoDossieEnum.class))).thenReturn(new ArrayList<>());
    }

    private void mockManterProcessoDTO() {
        processoDTO = new ManterProcessoDTO();
        processoDTO.setAvatar("Avatar");
        processoDTO.setContainerBpm("Container");
        processoDTO.setControlaValidadeDocumento(Boolean.TRUE);
        processoDTO.setDossieDigital(Boolean.TRUE);
        processoDTO.setId(1);
        processoDTO.setNome("Nome");
        processoDTO.setTipoPessoa(TipoPessoaEnum.F);
        processoDTO.setProcessoBpm("ProcessoBpm");
    }

}
