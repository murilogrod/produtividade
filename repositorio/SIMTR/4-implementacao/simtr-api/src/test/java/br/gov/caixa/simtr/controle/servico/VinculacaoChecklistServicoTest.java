package br.gov.caixa.simtr.controle.servico;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.caixa.simtr.modelo.entidade.Checklist;
import br.gov.caixa.simtr.modelo.entidade.VinculacaoChecklist;

public class VinculacaoChecklistServicoTest {
    
    @Mock
    private EntityManager entityManager;

    @Mock
    Query queryPersistChecklist;
    
    @Mock
    TypedQuery<Checklist> queryCheckList;
    
    @Mock
    TypedQuery<VinculacaoChecklist> queryVinculacaoCheckList;
    
    @Mock
    Query queryChecklistsVO;
    
    @InjectMocks
    VinculacaoChecklistServico vinculacaoChecklistServico;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Checklist.class))).thenReturn(queryCheckList);
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(VinculacaoChecklist.class))).thenReturn(queryVinculacaoCheckList);
    }
    
    public Checklist mockChecklist(){
        Checklist checklist = new Checklist();
        checklist.setId(1);
        checklist.setNome("Checklist Vinculação 01");
        return checklist;
    }
    
    public VinculacaoChecklist mockVinculacaoChecklist() {
        Checklist checklist = this.mockChecklist();
        
        VinculacaoChecklist vinculacaoCheclist = new VinculacaoChecklist();
        vinculacaoCheclist.setChecklist(checklist);
        vinculacaoCheclist.setId(1L);
        
        return vinculacaoCheclist;
    }
    
    @Test
    public void excluiVinculacoesChecklistLogicamenteTest() {
        Set<VinculacaoChecklist> vinculacoes = new HashSet<>();
        
        VinculacaoChecklist vinculacaoCheclist = this.mockVinculacaoChecklist();
        
        vinculacoes.add(vinculacaoCheclist);
        
        this.vinculacaoChecklistServico.excluiVinculacoesChecklistLogicamente(vinculacoes);
        
        Assert.assertNotNull(vinculacoes.iterator().next().getDataRevogacao());
    }
 
    @Test
    public void excluiVinculacoesChecklistFisicamenteTest() {
        Set<VinculacaoChecklist> vinculacoes = new HashSet<>();
        
        VinculacaoChecklist vinculacaoCheclist = this.mockVinculacaoChecklist();
        
        vinculacoes.add(vinculacaoCheclist);
        
        this.vinculacaoChecklistServico.excluiVinculacoesChecklistFisicamente(vinculacoes);
    }
}
