package br.gov.caixa.simtr.controle.servico;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.caixa.simtr.modelo.entidade.ComportamentoPesquisa;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.enumerator.SistemaPesquisaEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.lang.Assert;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

public class ComportamentoPesquisaServicoTest {

    @Mock
    private EntityManager manager;

    @Mock
    private Logger logger;

    @Mock
    TypedQuery<ComportamentoPesquisa> query;

    @InjectMocks
    ComportamentoPesquisaServico comportamentoPesquisaServico = new ComportamentoPesquisaServico();

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_DIR = "/mock/comportamento-pesquisa/";
    private static final String UTF_8 = "UTF-8";

    private Produto produto110;
    private ComportamentoPesquisa comportamentoNaoBloqueioSERASA;
    private ComportamentoPesquisa comportamentoBloqueioSICCF;

    @Before
    public void init() throws Exception {
        String jsonProduto110 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("produto-110.json")), UTF_8);
        this.produto110 = this.mapper.readValue(jsonProduto110, Produto.class);

        String jsonSERASA = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("SERASA-nao-bloqueio.json")), UTF_8);
        this.comportamentoNaoBloqueioSERASA = this.mapper.readValue(jsonSERASA, ComportamentoPesquisa.class);

        String jsonSICCF = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("SICCF-bloqueio.json")), UTF_8);
        this.comportamentoBloqueioSICCF = this.mapper.readValue(jsonSICCF, ComportamentoPesquisa.class);

        MockitoAnnotations.initMocks(this);
        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(ComportamentoPesquisa.class))).thenReturn(query);
        Mockito.when(this.query.getResultList()).thenReturn(Arrays.asList(comportamentoNaoBloqueioSERASA, comportamentoBloqueioSICCF));
        Mockito.when(this.query.getSingleResult()).thenReturn(comportamentoBloqueioSICCF);
    }

    @Test
    public void getEntityManagerTest() {
        List<ComportamentoPesquisa> listaComportamentos = this.comportamentoPesquisaServico.list();
        Assert.notEmpty(listaComportamentos);
    }

    @Test
    public void getBySistemas() {
        List<ComportamentoPesquisa> comportamentos = this.comportamentoPesquisaServico.getBySistemas(produto110, SistemaPesquisaEnum.SERASA, SistemaPesquisaEnum.SICCF);
        Assert.notNull(comportamentos);
    }

    @Test
    public void getBySistemaAndCodigoTipoPesquisa() {
        ComportamentoPesquisa comportamento = this.comportamentoPesquisaServico.getBySistemaAndCodigoTipoPesquisa(produto110, SistemaPesquisaEnum.SICCF, 1, 1);
        Assert.notNull(comportamento);
    }

    @Test
    public void getBySistemaAndCodigoTipoPesquisaCasoNoResult() {
        Mockito.doThrow(new NoResultException()).when(query).getSingleResult();

        //Aqui inicia o teste
        ComportamentoPesquisa comportamento = this.comportamentoPesquisaServico.getBySistemaAndCodigoTipoPesquisa(produto110, SistemaPesquisaEnum.SICCF, 1, 1);
        Assert.isNull(comportamento);
    }
}
