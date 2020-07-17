package br.gov.caixa.simtr.controle.servico;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.simtr.controle.excecao.SimtrPermissaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.DossieClienteProduto;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.RespostaDossie;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDossie;
import br.gov.caixa.simtr.modelo.entidade.TipoSituacaoDossie;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDossieEnum;

/**
 * <p>
 * CanalServicoTest</p>
 *
 * <p>
 * Descrição: Classe de teste do servico DocumentoServico</p>
 *
 * <br><b>Empresa:</b> Cef - Caixa Econômica Federal
 *
 *
 * @author f798783 - Welington Junio
 *
 * @version 1.0
 */
public class DossieProdutoServicoTest {

    @Mock
    private DossieProdutoServico _self;

    @Mock
    private EntityManager manager;

    @Mock
    private BPMServico bpmServico;

    @Mock
    TypedQuery<DossieProduto> queryDossieProduto;

    @Mock
    TypedQuery<SituacaoDossie> querySituacaoDossie;
    
    @Mock
    TypedQuery<RespostaDossie> queryRespostaDossie;

    @Mock
    Query query;

    @Mock
    private SituacaoDocumentoServico situacaoDocumentoServico;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private DossieClienteServico dossieClienteServico;

    /**
     * Atributo autorizacaoServico.
     */
    @InjectMocks
    private DossieProdutoServico dossieProdutoServico;

    @Mock
    private ProcessoServico processoServico;

    @Mock
    private SituacaoDossieServico situacaoDossieService;

    DossieClienteProduto dossieClienteProduto;

    /**
     * <p>
     * Método responsável por inicializar os mocks</p>.
     *
     * @author f798783
     *
     */
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private Long mokGetByGedId(final DossieProduto dossieProd) {
        Long id = dossieProd.getId();
        Mockito.when(manager.createQuery(Mockito.anyString(), Mockito.eq(DossieProduto.class))).thenReturn(queryDossieProduto);
        return id;
    }

    private Long chamadaEmComuGetById() {
        DossieProduto dossieProd = new DossieProduto();
        dossieProd.setId(2L);
        Long id = mokGetByGedId(dossieProd);
        Mockito.when(queryDossieProduto.getSingleResult()).thenReturn(dossieProd);
        return id;
    }

    @Test
    public void atribuiIntanciaBPMTest() {
        DossieProduto dossieProduto = new DossieProduto();
        dossieProduto.setProcesso(new Processo());
        Mockito.when(queryDossieProduto.getSingleResult()).thenReturn(dossieProduto);
        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(DossieProduto.class))).thenReturn(queryDossieProduto);
        Mockito.when(this.bpmServico.criaProcessoBPM(Mockito.any(DossieProduto.class))).thenReturn(1L);
        dossieProdutoServico.atribuiIntanciaBPM(1L);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void mokgetByIdNullTest() {
        Long id = null;
        final DossieProduto dossieProduto = dossieProdutoServico.getById(id, false, false, false, false, false, false);
        Assert.assertNotNull(dossieProduto);
    }

    private void mokarConsultaListResultList() {
        List<DossieProduto> lista = new ArrayList<>();
        Mockito.when(manager.createQuery(Mockito.anyString(), Mockito.eq(DossieProduto.class))).thenReturn(queryDossieProduto);
        Mockito.when(queryDossieProduto.getResultList()).thenReturn(lista);
    }

    @Test
    public void listByProcessoAndSituacaoProcessoEProcessoFaseTest() {
        parametroListByProcessOAndSistuacao();
        Processo processo = new Processo();
        processo.setId(1);
        List<DossieProduto> listDossieProduto = dossieProdutoServico.listByProcessoAndSituacao(processo, null);
        Assert.assertNotNull(listDossieProduto);

    }

    @Test
    public void listByVinculoUsuarioSolicitanteSucess() {
        parametroListByProcessOAndSistuacao();
        List<DossieProduto> listDossieProduto = dossieProdutoServico.listByUnidade(null, false);
        Assert.assertNotNull(listDossieProduto);
    }

    public void mockGetByTipoSituacao() {
        Mockito.when(this.situacaoDossieService.getByTipoSituacaoDossieEnum(Mockito.any())).thenReturn(new TipoSituacaoDossie());
    }

    public void mockSingleResult() {
        DossieProduto dossie = new DossieProduto();
        dossie.setSituacoesDossie(new HashSet<>());
        SituacaoDossie situacao = new SituacaoDossie();
        situacao.setDataHoraInclusao(Calendar.getInstance());
        TipoSituacaoDossie tipo = new TipoSituacaoDossie();
        tipo.setNome("Nometipo");
        situacao.setTipoSituacaoDossie(tipo);
        dossie.getSituacoesDossie().add(situacao);
        Mockito.when(queryDossieProduto.setMaxResults(1)).thenReturn(queryDossieProduto);
        Mockito.when(queryDossieProduto.getSingleResult()).thenReturn(dossie);

    }

    public void mockSingleResultNull() {
        Mockito.when(queryDossieProduto.setMaxResults(1)).thenReturn(queryDossieProduto);
        Mockito.when(queryDossieProduto.getSingleResult()).thenReturn(null);
    }

    public void mockSingleResultNRE() {
        Mockito.when(queryDossieProduto.setMaxResults(1)).thenReturn(queryDossieProduto);
        Mockito.doThrow(NoResultException.class).when(queryDossieProduto).getSingleResult();
    }

    @Test
    public void mokgetByIdParametroVinculacoesDossieClienteTrueTest() {
        Long id = chamadaEmComuGetById();
        DossieProduto dossieProduto = dossieProdutoServico.getById(id, true, false, false, false, false, false);
        Assert.assertEquals(id, dossieProduto.getId());
    }

    @Test
    public void mokgetByIdParametroVinculacoesProdutosContratadosTrueTest() {
        Long id = chamadaEmComuGetById();
        DossieProduto dossieProduto = dossieProdutoServico.getById(id, true, true, false, false, false, false);
        Assert.assertEquals(id, dossieProduto.getId());
    }

    @Test
    public void mokgetByIdParametroVinculacoesDocumentosTrueTest() {
        Long id = chamadaEmComuGetById();
        DossieProduto dossieProduto = dossieProdutoServico.getById(id, true, true, true, false, false, false);
        Assert.assertEquals(id, dossieProduto.getId());
    }

    @Test
    public void mokgetByIdParametroVinculacoesGarantiasTrueTest() {
        Long id = chamadaEmComuGetById();
        DossieProduto dossieProduto = dossieProdutoServico.getById(id, true, true, true, true, false, false);
        Assert.assertEquals(id, dossieProduto.getId());
    }

    @Test
    public void mokgetByIdSucessTest() {
        Long id = chamadaEmComuGetById();
        DossieProduto dossieProduto = dossieProdutoServico.getById(id, true, true, true, true, true, true);
        Assert.assertEquals(id, dossieProduto.getId());
    }

    @Test
    public void mokgetByIdNullParametrosTrueTest() {
        Long id = 1l;
        Mockito.when(manager.createQuery(Mockito.anyString(), Mockito.eq(DossieProduto.class))).thenReturn(queryDossieProduto);
        Mockito.doThrow(NoResultException.class).when(queryDossieProduto).getSingleResult();
        DossieProduto dossieProduto = dossieProdutoServico.getById(id, true, true, true, true, true, true);
        Assert.assertNull(dossieProduto);
    }

    @Test
    public void localizaMaisAntigoByProcessoAndTipoSituacaoTest() {
        mockListProcessosVinculados();
        mockQueryDossieProduto();
        mockSingleResult();
        Mockito.when(this.keycloakService.getMatricula()).thenReturn("c000000");
        this.dossieProdutoServico.localizaMaisAntigoByProcessoAndTipoSituacao(new Processo(), new TipoSituacaoDossie());
    }

    @Test
    public void localizaMaisAntigoByProcessoAndTipoSituacaoTestSingleNRE() {
        mockListProcessosVinculados();
        mockQueryDossieProduto();
        mockSingleResultNRE();
        Mockito.when(this.keycloakService.getMatricula()).thenReturn("c000000");
        this.dossieProdutoServico.localizaMaisAntigoByProcessoAndTipoSituacao(new Processo(), new TipoSituacaoDossie());
    }

    @Test
    public void vinculaUnidadeTratamentoTest() {
        mockQueryDossieProduto();
        mockSingleResult();
        this.dossieProdutoServico.vinculaUnidadeTratamento(2L, 1);
    }
    
    @Test(expected= SimtrPermissaoException.class)
    public void testExcecaoSituacaoFinalExistente() {
        DossieProduto dossieProd = new DossieProduto();
        dossieProd.setId(2L);
        SituacaoDossie situacao = new SituacaoDossie();
        situacao.setId(9L);
        situacao.setDataHoraInclusao(Calendar.getInstance());
        TipoSituacaoDossie tipo = new TipoSituacaoDossie();
        tipo.setTipoFinal(true);
        situacao.setTipoSituacaoDossie(tipo);
        dossieProd.getSituacoesDossie().add(situacao);
        Mockito.when(queryDossieProduto.getSingleResult()).thenReturn(dossieProd);
        
        this.dossieProdutoServico.adicionaSituacaoDossieProduto(mokGetByGedId(dossieProd),SituacaoDossieEnum.SEGURANCA_FINALIZADO, null);
        
    }
    
    @Test
    public void testAdicionaSituacaoDossieProduto() {
        DossieProduto dossieProd = new DossieProduto();
        dossieProd.setId(2L);
        SituacaoDossie situacao = new SituacaoDossie();
        situacao.setId(2L);
        situacao.setDataHoraInclusao(Calendar.getInstance());
        TipoSituacaoDossie tipo = new TipoSituacaoDossie();
        tipo.setTipoFinal(false);
        situacao.setTipoSituacaoDossie(tipo);
        dossieProd.getSituacoesDossie().add(situacao);
        
        Mockito.when(queryDossieProduto.getSingleResult()).thenReturn(dossieProd);
        Mockito.when(this.situacaoDossieService.getByTipoSituacaoDossieEnum(Mockito.any())).thenReturn(tipo);
        
        this.dossieProdutoServico.adicionaSituacaoDossieProduto(mokGetByGedId(dossieProd), SituacaoDossieEnum.SEGURANCA_FINALIZADO, null);        
    }
    
    @Test
    public void testAdicionaSituacaoDossieAbortaProcessoBPM() {
        DossieProduto dossieProd = new DossieProduto();
        dossieProd.setId(2L);
        dossieProd.setIdInstanciaProcessoBPM(1234L);
        SituacaoDossie situacao = new SituacaoDossie();
        situacao.setId(2L);
        situacao.setDataHoraInclusao(Calendar.getInstance());
        TipoSituacaoDossie tipoSituacao = new TipoSituacaoDossie();
        tipoSituacao.setTipoFinal(false);
        situacao.setTipoSituacaoDossie(tipoSituacao);
        dossieProd.getSituacoesDossie().add(situacao);
        Mockito.when(queryDossieProduto.getSingleResult()).thenReturn(dossieProd);
        
        TipoSituacaoDossie tipoSituacaoNova = new TipoSituacaoDossie();
        tipoSituacaoNova.setTipoFinal(true);
        Mockito.when(this.situacaoDossieService.getByTipoSituacaoDossieEnum(Mockito.any())).thenReturn(tipoSituacaoNova);
        
        this.dossieProdutoServico.adicionaSituacaoDossieProduto(mokGetByGedId(dossieProd), SituacaoDossieEnum.SEGURANCA_FINALIZADO, null);
    }
    
    @Test
    public void consultaFalhaBPMTest() {
        mockListProcessosVinculados();
        mockQueryDossieProduto();
        mockSingleResult();
        Mockito.when(this.keycloakService.getMatricula()).thenReturn("c000000");
        this.dossieProdutoServico.consultaFalhaBPM();
    }

    private void mockListProcessosVinculados() {
        Mockito.when(this.processoServico.listProcessosVinculados(Mockito.any(Processo.class))).thenReturn(new ArrayList<>());
    }

    private void mockQueryDossieProduto() {
        Mockito.when(manager.createQuery(Mockito.anyString(), Mockito.eq(DossieProduto.class))).thenReturn(queryDossieProduto);
    }

    public void mockSingleResultResposta() {
        Mockito.when(queryRespostaDossie.getSingleResult()).thenReturn(new RespostaDossie());
    }

    public void mockSingleResultRespostaNRE() {
        Mockito.doThrow(NoResultException.class).when(queryRespostaDossie).getSingleResult();
    }

    private void parametroListByProcessOAndSistuacao() {
        mokarConsultaListResultList();
        Mockito.when(keycloakService.getLotacaoAdministrativa()).thenReturn(2333);
        Mockito.when(keycloakService.getLotacaoFisica()).thenReturn(2333);
    }
}
