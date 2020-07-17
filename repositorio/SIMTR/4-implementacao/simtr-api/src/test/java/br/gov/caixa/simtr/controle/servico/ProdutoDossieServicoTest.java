package br.gov.caixa.simtr.controle.servico;

import java.math.BigDecimal;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.caixa.simtr.controle.excecao.SimtrPermissaoException;
import br.gov.caixa.simtr.modelo.entidade.ProdutoDossie;
import br.gov.caixa.simtr.modelo.enumerator.PeriodoJurosEnum;

public class ProdutoDossieServicoTest {

    @Mock
    private EntityManager manager;

    @Mock
    private Logger logger;

    @Mock
    TypedQuery<ProdutoDossie> query;

    @InjectMocks
    ProdutoDossieServico servico = new ProdutoDossieServico();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(ProdutoDossie.class))).thenReturn(query);
        Mockito.when(this.query.getSingleResult()).thenReturn(new ProdutoDossie());
    }

    @Test
    public void getByIdTest() {
        this.servico.getById(1L, true, true);
    }

    @Test
    public void getByIdTestException() {
        Mockito.doThrow(new NoResultException()).when(query).getSingleResult();
        this.servico.getById(1L, false, false);
    }

    @Test
    public void atualizaProdutoDossieTest() {
        this.servico.atualizaProdutoDossie(1L);
    }

    @Test(expected = SimtrPermissaoException.class)
    public void delete() {
        this.servico.delete(null);
    }
}
