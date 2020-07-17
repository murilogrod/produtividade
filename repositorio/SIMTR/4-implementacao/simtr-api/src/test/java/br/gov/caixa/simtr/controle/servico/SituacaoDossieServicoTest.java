package br.gov.caixa.simtr.controle.servico;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.simtr.controle.excecao.SimtrException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.TipoSituacaoDossie;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDossieEnum;
import java.util.Calendar;

/**
 *
 * @author Igor Furtado - Fóton
 *
 */
public class SituacaoDossieServicoTest {

    /**
     * Atributo manager.
     */
    @Mock
    private EntityManager manager;

    @Mock
    private TypedQuery<TipoSituacaoDossie> querySituacao;

    @Mock
    private TypedQuery<Calendar> queryCalendar;

    @Mock
    private Query query;

    @Mock
    private Logger logger;

    @Mock
    private CanalServico canalServico;
        
    @Mock
    private KeycloakService keycloakService;

    @InjectMocks
    private SituacaoDossieServico situacaoDossieServico = new SituacaoDossieServico();

    private DossieProduto dossieProduto;

    private List<TipoSituacaoDossie> tiposSituacao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        dossieProduto = new DossieProduto();
        dossieProduto.setId(1L);

        Canal canal = new Canal();
        canal.setSigla("SIGLA_SISTEMA");
        canal.setClienteSSO("cli-web-xxx");
        
        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(canal);
        Mockito.when(this.keycloakService.getMatricula()).thenReturn("service-account-cli-web-dep-xxx");
    }

    @Test
    public void consultarDossiesEmpty() {
        mockCreateQuery();
        TipoSituacaoDossie tipoSituacaoDossie = new TipoSituacaoDossie();
        tipoSituacaoDossie.setTipoFinal(Boolean.TRUE);
        situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoDossie, "Observacao", "Nova Observacao");

    }

    @Test
    public void consultarDossiesEmptyNull() {
        mockCreateQuery();
        TipoSituacaoDossie tipoSituacaoDossie = new TipoSituacaoDossie();
        tipoSituacaoDossie.setTipoFinal(Boolean.FALSE);
        situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoDossie, null, null);

    }

    @Test
    public void removeUltimaSituacaoDossieTest() {
        mockCreateQuery();
        situacaoDossieServico.removeUltimaSituacaoDossie(dossieProduto, "Observacao");

    }

    @Test
    public void getByTipoSituacaoDossieEnumTest() {
        mockCreateTypedQuery();
        mockTiposSituacoes();
        mockResultListSituacao();
        this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.RASCUNHO);

    }

    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void getByTipoSituacaoDossieEnumTestFalha() {
        mockCreateTypedQuery();
        mockTiposSituacoesError();
        mockResultListSituacao();
        this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.RASCUNHO);

    }

    private void mockCreateTypedQuery() {
        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(TipoSituacaoDossie.class))).thenReturn(querySituacao);
        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(Calendar.class))).thenReturn(queryCalendar);
        Mockito.when(queryCalendar.getSingleResult()).thenReturn(Calendar.getInstance());
    }

    private void mockCreateQuery() {
        Mockito.when(this.manager.createQuery(Mockito.anyString())).thenReturn(query).thenReturn(query);
    }

    private void mockTiposSituacoes() {
        tiposSituacao = new ArrayList<TipoSituacaoDossie>();
        TipoSituacaoDossie tipoSituacao = new TipoSituacaoDossie();
        tipoSituacao.setNome("Rascunho");
        tiposSituacao.add(tipoSituacao);
    }

    private void mockTiposSituacoesError() {
        tiposSituacao = new ArrayList<TipoSituacaoDossie>();
        TipoSituacaoDossie tipoSituacao = new TipoSituacaoDossie();
        tipoSituacao.setNome("Error");
        tiposSituacao.add(tipoSituacao);
    }

    private void mockResultListSituacao() {
        Mockito.when(querySituacao.getResultList()).thenReturn(tiposSituacao);
    }

}
