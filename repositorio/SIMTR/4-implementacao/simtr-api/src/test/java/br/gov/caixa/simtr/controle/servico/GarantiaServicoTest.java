package br.gov.caixa.simtr.controle.servico;

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

import br.gov.caixa.simtr.modelo.entidade.Garantia;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.lang.Assert;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;

public class GarantiaServicoTest {

    @Mock
    private EntityManager manager;

    @Mock
    private Logger logger;

    @Mock
    TypedQuery<Garantia> query;

    @InjectMocks
    private final GarantiaServico servico = new GarantiaServico();

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_DIR = "/mock/garantia/";
    private static final String UTF_8 = "UTF-8";

    private Garantia garantiaFidejussoria;
    private Garantia garantiaNaoFidejussoria;

    @Before
    public void init() throws Exception {
        
        String jsonGarantiaFidejussoria = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("garantia-fidejussoria.json")), UTF_8);
        this.garantiaFidejussoria = this.mapper.readValue(jsonGarantiaFidejussoria, Garantia.class);
        
        String jsonGarantiaNaoFidejussoria = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("garantia-nao-fidejussoria.json")), UTF_8);
        this.garantiaNaoFidejussoria = this.mapper.readValue(jsonGarantiaNaoFidejussoria, Garantia.class);
        
        MockitoAnnotations.initMocks(this);
        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(Garantia.class))).thenReturn(query);
        Mockito.when(this.query.getResultList()).thenReturn(Arrays.asList(garantiaFidejussoria, garantiaNaoFidejussoria));
        Mockito.when(this.query.getSingleResult()).thenReturn(garantiaFidejussoria);
    }

    @Test
    public void getEntityManagerTest() {
        List<Garantia> listaGarantias = this.servico.list();
        Assert.notEmpty(listaGarantias);
    }
    
    @Test
    public void getById() {
        Garantia garantia = this.servico.getById(1, true, true, true);
        Assert.notNull(garantia);
    }

    @Test
    public void getByIdCasoNoResult() {
        Mockito.doThrow(new NoResultException()).when(query).getSingleResult();
        
        //Inicio do teste;
        Garantia garantia = this.servico.getById(1, false, false, false);
        Assert.isNull(garantia);
    }

    @Test
    public void getByCodigoBacen() {
        Mockito.when(this.query.getSingleResult()).thenReturn(garantiaFidejussoria);
        Garantia garantia = this.servico.getByCodigoBacen(9901, false, false, false);
        Assert.notNull(garantia);
    }
}
