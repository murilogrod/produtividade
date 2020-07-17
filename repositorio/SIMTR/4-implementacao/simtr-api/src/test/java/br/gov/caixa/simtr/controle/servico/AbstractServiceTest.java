package br.gov.caixa.simtr.controle.servico;

import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.Checklist;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import javax.persistence.TypedQuery;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class AbstractServiceTest {

    @Mock
    private EntityManager manager;

    @Mock
    private TypedQuery<Canal> queryCanal;

    @InjectMocks
    private final CanalServico canalServico = new CanalServico();
    
    @InjectMocks
    private final ChecklistServico checklistServico = new ChecklistServico();

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_DIR = "/mock/abstract/";
    private static final String UTF_8 = "UTF-8";

    private Canal canalSIMTR;
    private Checklist checklistIdentificacao;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        String jsonCanal = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("canal-simtr.json")), UTF_8);
        this.canalSIMTR = mapper.readValue(jsonCanal, Canal.class);
        
        String jsonChecklist = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("checklist-identificacao.json")), UTF_8);
        this.checklistIdentificacao = mapper.readValue(jsonChecklist, Checklist.class);

        Mockito.when(manager.find(Mockito.eq(Canal.class), Mockito.anyInt())).thenReturn(canalSIMTR);

        Mockito.when(manager.createQuery(Mockito.anyString(), Mockito.eq(Canal.class))).thenReturn(queryCanal);
        Mockito.when(queryCanal.getResultList()).thenReturn(Arrays.asList(canalSIMTR, new Canal()));
    }

    @Test
    public void constructorTest() {
        ClasseServicoB classeB = new ClasseServicoB(this.manager);
        Assert.assertNotNull(classeB);
    }

    @Test
    public void refreshTest() {
        this.canalServico.refresh(canalSIMTR);
    }

    @Test
    public void saveTest() {
        this.canalServico.save(canalSIMTR);
    }

    @Test
    public void deleteTest() {
        this.canalServico.delete(canalSIMTR);
    }

    @Test
    public void updateTest() {
        this.canalServico.update(canalSIMTR);
    }

    @Test
    public void getByIdTest() {
        this.canalServico.getById(1);
    }

    @Test
    public void listTest() {
        this.canalServico.list();
    }

    @Test
    public void validaRecursoTest() {
        this.canalServico.list();
    }

    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void validaRecursoLocalizado() {
        this.checklistServico.validaRecursoLocalizado(checklistIdentificacao, "Mensagem de Erro");

        this.checklistServico.validaRecursoLocalizado(null, "Mensagem de Erro");
    }
}

abstract class ClasseServicoA extends AbstractService<Canal, Integer> {

    EntityManager em;

    public ClasseServicoA(EntityManager em) {
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}

class ClasseServicoB extends ClasseServicoA {

    public ClasseServicoB(EntityManager em) {
        super(em);
    }
}
