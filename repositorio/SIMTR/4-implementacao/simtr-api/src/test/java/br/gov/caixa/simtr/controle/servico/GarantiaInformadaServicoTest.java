package br.gov.caixa.simtr.controle.servico;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.caixa.simtr.controle.excecao.SimtrException;
import br.gov.caixa.simtr.modelo.entidade.DossieClientePF;
import br.gov.caixa.simtr.modelo.entidade.Garantia;
import br.gov.caixa.simtr.modelo.entidade.GarantiaInformada;

public class GarantiaInformadaServicoTest {
	
	/** Atributo manager. */
	@Mock
	private EntityManager manager;
	
	/** Atributo query. */
	@Mock
	private TypedQuery<GarantiaInformada> query;
	
	/** Atributo Logger. */
	@Mock
    private Logger logger;

	/** Atributo documentoServico. */
	@InjectMocks
	private GarantiaInformadaServico garantiaInformadaServico = new GarantiaInformadaServico();
	
	@Mock
	private DossieClienteServico dossieClienteServico;
	
	/** 
	 * <p>Método responsável por inicializar os mocks</p>.
	 *
	 * @author p541915
	 *
	 */
	@Before
	public void init() {		
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void garantiaInformadaTrue() {
		mockQuery();
		mockSingleResult();
		Assert.assertNotNull(this.garantiaInformadaServico.getById(1L, true, true, true, true));
	}
	
	@Test
	public void garantiaInformadaFalse() {
		mockQuery();
		mockSingleResult();	
		Assert.assertNotNull(this.garantiaInformadaServico.getById(1L, false, false, false, false));
	}
	
	@Test
	public void garantiaInformadaError() {
		mockQuery();
		mockSingleResultException();
		Assert.assertNull(this.garantiaInformadaServico.getById(1L, false, false, false, false));
	}
	
	private void mockSingleResultException() {
		Mockito.doThrow(NoResultException.class).when(query).getSingleResult();
	}
	
	@Test (expected = SimtrException.class)
	public void atualizaGarantiaInformadaVerificarGarantiafideJussoriaERRO() {
		mockQuery();
		mockSingleResult();
		this.garantiaInformadaServico.atualizaGarantiaInformada(1L, null, null);
	}
	
	@Test(expected = SimtrException.class) 
	public void atualizaGarantiaInformada() {
		mockQuery();
		mockSingleResult();
		List<Long> ids = new ArrayList<Long>();
		ids.add(1L);
		ids.add(2L);
		ids.add(3L);
		this.garantiaInformadaServico.atualizaGarantiaInformada(new Long(1l), new BigDecimal(1), ids);
	}
	
	@Test 
	public void atualizaGarantiaInformadaDossie() {
		mockQuery();
		mockSingleResult();
		List<Long> ids = new ArrayList<Long>();
		ids.add(1L);
		ids.add(2L);
		ids.add(3L);
		mockDossie();
		this.garantiaInformadaServico.atualizaGarantiaInformada(new Long(1l), new BigDecimal(1), ids);
	}
	
	@Test(expected = SimtrException.class)
	public void deleteTest() {
		this.garantiaInformadaServico.delete(null);
	}
	
	private void mockQuery() {
		Mockito.when(manager.createQuery( Mockito.anyString(), Mockito.eq(GarantiaInformada.class))).thenReturn(query);
	}
	
	private void mockSingleResult() {
		GarantiaInformada garantiaInformada = new GarantiaInformada();
		Garantia garantia = new Garantia();
		garantia.setFidejussoria(Boolean.TRUE);
		garantiaInformada.setGarantia(garantia);
		Mockito.when(query.getSingleResult()).thenReturn(garantiaInformada);
	}
	
	private void mockDossie() {
		Mockito.when(dossieClienteServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(new DossieClientePF());
	}
	

}
