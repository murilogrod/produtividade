package br.gov.caixa.simtr.controle.servico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.ContratoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.ProcessoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;

public class ContratoAdministrativoServicoTest {
	/** Atributo manager. */
	@Mock
	private EntityManager manager;
	
	/** Atributo query. */
	@Mock
	TypedQuery<ContratoAdministrativo> query;
	
	@Mock
	private KeycloakService keycloakService;
	
	/** Atributo autorizacaoServico. */
	@InjectMocks
	private ContratoAdministrativoServico contratoAdminServico;
	
	/** Atributo Logger. */
	@Mock
    private Logger logger;
	
	@Mock
	private ProcessoAdministrativoServico processoAdministrativoServico;
	
	@Mock
	DocumentoAdministrativoServico  documentoAdministrativoServico;
	
	@Mock
	private CanalServico canalServico;
	
	@Mock
	private TipoDocumentoServico tipoDocumentoServico;
	
	Long idContratoAdminstrativo;
	Integer idTipoDocumento;
	Long idDocumentoSubstituicao;
	String justificativaSubstituicao;
	OrigemDocumentoEnum origemDocumentoEnum;
	FormatoConteudoEnum formatoConteudoEnum;
	boolean confidencial;
	boolean valido;
	String descricao;
	String mimeType;
	List<AtributoDocumento> atributosDocumento;
	String binario;
	

	/** 
	 * <p>Método responsável por inicializar os mocks</p>.
	 *
	 * @author f798783
	 *
	 */
	@Before
	public void init() {		
		MockitoAnnotations.initMocks(this);
	}

	private void mockarCanalPraRetornaValor() {
		Canal canal = new Canal();
		canal.setId(1);
		Mockito.when(canalServico.getByClienteSSO()).thenReturn(canal);
	}

	private void mockarTipoDocumentoPraRetornaValor() {
		TipoDocumento tipoDocumento = new TipoDocumento();
		tipoDocumento.setId(1);
		Mockito.when( tipoDocumentoServico.getById(idTipoDocumento)).thenReturn(tipoDocumento);
	}

	private void mockarContratoAdministrativoPraRetornaValor() {
		ContratoAdministrativo contratoAdmin = new ContratoAdministrativo();
		contratoAdmin.setId(1L);
		Mockito.when( manager.createQuery(Mockito.anyString(), Mockito.eq(ContratoAdministrativo.class))).thenReturn(query);
		Mockito.when( query.getSingleResult()).thenReturn(contratoAdmin);
	}
	
	private void mockarContratoAdministrativoListPraRetornaValor() {
		Mockito.when( manager.createQuery(Mockito.anyString(), Mockito.eq(ContratoAdministrativo.class))).thenReturn(query);
		Mockito.when( query.getResultList()).thenReturn(new ArrayList<>());
	}
	
	private void mockarContratoAdministrativoListPraRetornaNRE() {
		Mockito.when( manager.createQuery(Mockito.anyString(), Mockito.eq(ContratoAdministrativo.class))).thenReturn(query);
		Mockito.when( query.getResultList()).thenThrow(NoResultException.class);
	}
	
	private void mockarContratoAdministrativoPraRetornarNRE() {
		ContratoAdministrativo contratoAdmin = new ContratoAdministrativo();
		contratoAdmin.setId(1L);
		Mockito.when( manager.createQuery(Mockito.anyString(), Mockito.eq(ContratoAdministrativo.class))).thenReturn(query);
		Mockito.when( query.getSingleResult()).thenThrow(NoResultException.class);
	}
	
	

	@Test  (expected = SimtrRequisicaoException.class)
	public void getQueryCanalNull() {	
		Mockito.when(canalServico.getByClienteSSO()).thenReturn(null);		
		this.contratoAdminServico.criaDocumentoContratoAdministrativo(idContratoAdminstrativo, idTipoDocumento, 
				idDocumentoSubstituicao, justificativaSubstituicao, origemDocumentoEnum, confidencial, valido, descricao, mimeType, atributosDocumento, binario);
	}

	@Test  (expected = SimtrRequisicaoException.class)
	public void getQueryTipoDocumentoNull() {
		idTipoDocumento		= 1;
		mockarCanalPraRetornaValor();		
		Mockito.when( tipoDocumentoServico.getById(idTipoDocumento)).thenReturn(null);
		this.contratoAdminServico.criaDocumentoContratoAdministrativo(idContratoAdminstrativo, idTipoDocumento, 
				idDocumentoSubstituicao, justificativaSubstituicao, origemDocumentoEnum, confidencial, valido, descricao, mimeType, atributosDocumento, binario);
	}

	@Test  (expected = SimtrRequisicaoException.class)
	public void getQueryContratoAdmiNull() {
		idTipoDocumento		= 1;
		idContratoAdminstrativo = 3L;
		mockarCanalPraRetornaValor();
		mockarTipoDocumentoPraRetornaValor();
		Mockito.when(manager.createQuery(Mockito.anyString(), Mockito.eq(ContratoAdministrativo.class))).thenReturn(query);		
		this.contratoAdminServico.criaDocumentoContratoAdministrativo(idContratoAdminstrativo, idTipoDocumento, 
				idDocumentoSubstituicao, justificativaSubstituicao, origemDocumentoEnum, confidencial, valido, descricao, mimeType, atributosDocumento, binario);
	}
	
	
	@Test
	public void listByDescricaoTestException() {
		mockarContratoAdministrativoListPraRetornaNRE();	
		Assert.assertNotNull(this.contratoAdminServico.listByDescricao("", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
	}
	
	@Test
	public void listByDescricaoTest() {
		mockarContratoAdministrativoListPraRetornaValor();	
		Assert.assertNotNull(this.contratoAdminServico.listByDescricao("", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
	}
	
	
	@Test
	public void getByNumeroAnoTestException() {
		mockarContratoAdministrativoPraRetornarNRE();	
		Assert.assertNull(this.contratoAdminServico.getByNumeroAno(1, 2019, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
	}
	
	@Test
	public void getByNumeroAnoTest() {
		mockarContratoAdministrativoPraRetornaValor();	
		Assert.assertNotNull(this.contratoAdminServico.getByNumeroAno(1, 2019, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
	}
	
	
	@Test
	public void aplicarPatchObjetoCompletoTest() {
		ContratoAdministrativo contrato = new ContratoAdministrativo();
		contrato.setAnoContrato(2019);
		contrato.setCoResp("Responsavel");
		contrato.setCpfCnpjFornecedor("11111111111");
		contrato.setMatriculaInclusao("C999999");
		contrato.setDataHoraInclusao(Calendar.getInstance());
		contrato.setDescricaoContrato("Descricao Contrato");
		contrato.setNumeroContrato(123456);
		contrato.setUnidadeOperacional(1000);
		ProcessoAdministrativo processo = new ProcessoAdministrativo();
		processo.setId(2L);
		contrato.setProcessoAdministrativo(processo);
		
		Mockito.when(processoAdministrativoServico.getById(2L, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE)).thenReturn(processo);
		
		mockarContratoAdministrativoPraRetornaValor();	
		this.contratoAdminServico.aplicaPatch(1L, contrato);
	}
	
	@Test
	public void aplicarPatchObjetovazioTest() {
		mockarContratoAdministrativoPraRetornaValor();	
		this.contratoAdminServico.aplicaPatch(1L, new ContratoAdministrativo());
	}
	
	
	@Test  (expected = SimtrRequisicaoException.class)
	public void getQueryValidaMineTypeConteudoERROR() {
		idTipoDocumento		= 1;
		idContratoAdminstrativo = 3L;
		mimeType = "image/jpg";
		binario = "data:image/gif;base64,...";
		formatoConteudoEnum = FormatoConteudoEnum.JPEG;
		mockarCanalPraRetornaValor();
		mockarTipoDocumentoPraRetornaValor();
		mockarContratoAdministrativoPraRetornaValor();	
		Mockito.when( documentoAdministrativoServico.validaMimetypeConteudo( formatoConteudoEnum, binario )).thenReturn(true);
		this.contratoAdminServico.criaDocumentoContratoAdministrativo(idContratoAdminstrativo, idTipoDocumento, 
				idDocumentoSubstituicao, justificativaSubstituicao, origemDocumentoEnum, confidencial, valido, descricao, mimeType, atributosDocumento, binario);
		
	}

	@Test  (expected = SimtrRequisicaoException.class)
	public void getQueryValidaMineFormatConteudoErro() {
		idTipoDocumento		= 1;
		idContratoAdminstrativo = 3L;
		mimeType = "fsad";
		binario = "";
		mockarCanalPraRetornaValor();
		mockarTipoDocumentoPraRetornaValor();
		mockarContratoAdministrativoPraRetornaValor();	
		this.contratoAdminServico.criaDocumentoContratoAdministrativo(idContratoAdminstrativo, idTipoDocumento, 
				idDocumentoSubstituicao, justificativaSubstituicao, origemDocumentoEnum, confidencial, valido, descricao, mimeType, atributosDocumento, binario);
		
	}

	@Test  (expected = SimtrRequisicaoException.class)
	public void getQueryValidaMineTypeConteudoFalse() {
		idTipoDocumento		= 1;
		idContratoAdminstrativo = 3L;
		mimeType = "image/jpg";
		binario = "data:image/gif;base64,...";
		formatoConteudoEnum = FormatoConteudoEnum.JPEG;
		mockarCanalPraRetornaValor();
		mockarTipoDocumentoPraRetornaValor();
		mockarContratoAdministrativoPraRetornaValor();	
		Mockito.when( documentoAdministrativoServico.validaMimetypeConteudo( formatoConteudoEnum, binario )).thenReturn(false);
		this.contratoAdminServico.criaDocumentoContratoAdministrativo(idContratoAdminstrativo, idTipoDocumento, 
				idDocumentoSubstituicao, justificativaSubstituicao, origemDocumentoEnum, confidencial, valido, descricao, mimeType, atributosDocumento, binario);
		
	}

	@Test  (expected = SimtrRequisicaoException.class)
	public void getQueryValidaMineTypeConteudoSucess() {
		idTipoDocumento		= 1;
		idContratoAdminstrativo = 3L;
		idDocumentoSubstituicao =  4L;
		binario = "data:image/gif;base64,...";
		formatoConteudoEnum = FormatoConteudoEnum.JPEG;
		mockarCanalPraRetornaValor();
		mockarTipoDocumentoPraRetornaValor();
		mockarContratoAdministrativoPraRetornaValor();	
		Mockito.when( documentoAdministrativoServico.validaMimetypeConteudo( formatoConteudoEnum, binario )).thenReturn(true);
		this.contratoAdminServico.criaDocumentoContratoAdministrativo(idContratoAdminstrativo, idTipoDocumento, 
				idDocumentoSubstituicao, justificativaSubstituicao, origemDocumentoEnum, confidencial, valido, descricao, mimeType, atributosDocumento, binario);
		
	}
	
	@Test   (expected = SimtrRequisicaoException.class)
	public void criaRelatorioPAEContratoAdminNull() throws WebApplicationException, IOException {	

		Mockito.when( manager.createQuery(Mockito.anyString(), Mockito.eq(ContratoAdministrativo.class))).thenReturn(query);
		Mockito.when( query.getSingleResult()).thenReturn(null);		
		this.contratoAdminServico.criaRelatorioPAE(null, false, false);
	}

}
