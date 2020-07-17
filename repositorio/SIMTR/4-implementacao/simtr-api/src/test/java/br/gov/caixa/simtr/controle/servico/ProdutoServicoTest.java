package br.gov.caixa.simtr.controle.servico;

import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import java.util.Objects;
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

import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.produto.ProdutoManutencaoDTO;
import io.jsonwebtoken.lang.Assert;

public class ProdutoServicoTest {

    @Mock
    private EntityManager manager;

    @Mock
    private Logger logger;

    @Mock
    TypedQuery<Produto> query;

    @InjectMocks
    ProdutoServico servico = new ProdutoServico();

    private ProdutoManutencaoDTO produtoManutencaoDTO;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getByIdOrOperacaoModalidadeTest() {
        mockQuery();
        mockSingleResult();
        Assert.notNull(this.servico.getById(1, true, true, true, true, true, true));
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void getByIdOrOperacaoModalidadeTestVazio() {
        mockQuery();
        mockSingleResult();
        Assert.notNull(this.servico.getById(null, false, false, false, false, false, false));
    }

    @Test
    public void getByOperacaoModalidadeTest() {
        mockQuery();
        mockSingleResult();
        Assert.notNull(this.servico.getByOperacaoModalidade(1, 1, false, false, false, false, false, false));
    }

    @Test
    public void aplicaPatchTest() {
        mockQuery();
        mockSingleResult();
        mockProdutoDTO();
        mockFindProduto();
        this.servico.aplicaPatch(1, produtoManutencaoDTO);
    }

    @Test
    public void getByIdOrOperacaoModalidadeTestException() {
        mockQuery();
        mockSingleResultException();
        Assert.isNull(this.servico.getById(1, true, true, true, true, true, true));
    }

    private void mockQuery() {
        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(Produto.class))).thenReturn(query);
    }

    private void mockSingleResult() {
        Mockito.when(query.getSingleResult()).thenReturn(new Produto());
    }

    private void mockSingleResultException() {
        Mockito.doThrow(new NoResultException()).when(query).getSingleResult();
    }

    private void mockFindProduto() {
        Mockito.when(this.servico.getById(1)).thenReturn(new Produto());
    }

    private void mockProdutoDTO() {
        produtoManutencaoDTO = new ProdutoManutencaoDTO();
        produtoManutencaoDTO.setContratacaoConjunta(Boolean.TRUE);
        produtoManutencaoDTO.setDossieDigital(Boolean.TRUE);
        produtoManutencaoDTO.setModalidadeProduto(1);
        produtoManutencaoDTO.setNomeProduto("Nome Produto");
        produtoManutencaoDTO.setOperacaoProduto(1);
        produtoManutencaoDTO.setPesquisaCCF(Boolean.TRUE);
        produtoManutencaoDTO.setPesquisaCadin(Boolean.TRUE);
        produtoManutencaoDTO.setPesquisaReceita(Boolean.TRUE);
        produtoManutencaoDTO.setPesquisaSCPC(Boolean.TRUE);
        produtoManutencaoDTO.setPesquisaSerasa(Boolean.TRUE);
        produtoManutencaoDTO.setPesquisaSicow(Boolean.TRUE);
        produtoManutencaoDTO.setPesquisaSinad(Boolean.TRUE);
        produtoManutencaoDTO.setTipoPessoa(TipoPessoaEnum.A);
    }

}
