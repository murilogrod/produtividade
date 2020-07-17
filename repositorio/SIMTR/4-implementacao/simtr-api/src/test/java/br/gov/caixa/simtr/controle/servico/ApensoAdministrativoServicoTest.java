package br.gov.caixa.simtr.controle.servico;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;

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

import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.ApensoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.ContratoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.DocumentoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.ProcessoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoApensoEnum;

public class ApensoAdministrativoServicoTest {
	/** Atributo manager. */
	@Mock
	private EntityManager manager;

	/** Atributo query. */
	@Mock
	TypedQuery<ApensoAdministrativo> query;

	@Mock
	private KeycloakService keycloakService;

	/** Atributo Logger. */
	@Mock
	private Logger logger;

	/** Atributo autorizacaoServico. */
	@InjectMocks
	private ApensoAdministrativoServico apensoAdmServico;
	
	@Mock
	private CanalServico canalServico;
	
	@Mock
	DocumentoAdministrativoServico  documentoAdministrativoServico;
	
	@Mock
	private RelatorioServico relatorioServico;
	
	@Mock
	private TipoDocumentoServico tipoDocumentoServico;
	
	@Mock
	private ProcessoAdministrativoServico processoAdministrativoServico;
	
	@Mock
	private ContratoAdministrativoServico contratoAdministrativoServico;
	
	private Long idApensoAdminstrativo;
	private Integer idTipoDocumento;
	private Long idDocumentoSubstituicao;
	private String justificativaSubstituicao;
	private OrigemDocumentoEnum origemDocumentoEnum;
	private boolean confidencial;
	private boolean valido;
	private String descricao;
	private String mimeType;
	private List<AtributoDocumento> atributosDocumento;
	private String binario;
	FormatoConteudoEnum formatoConteudoEnum;

	/**
	 * <p>
	 * Método responsável por inicializar os mocks
	 * </p>
	 * .
	 *
	 * @author f798783
	 *
	 */
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = SimtrRequisicaoException.class)
	public void saveValidaProtocoloAx() {
		ApensoAdministrativo apensoAdministrativo = new ApensoAdministrativo();
		TipoApensoEnum tipoApenso = TipoApensoEnum.AX;
		apensoAdministrativo.setTipoApenso(tipoApenso);
		apensoAdmServico.save(apensoAdministrativo);
		Assert.assertNull(apensoAdministrativo);
	}

	@Test(expected = SimtrRequisicaoException.class)
	public void saveValidaProtocoloSICLGObrigatorio() {
		ApensoAdministrativo apensoAdministrativo = new ApensoAdministrativo();
		TipoApensoEnum tipoApenso = TipoApensoEnum.PP;
		apensoAdministrativo.setTipoApenso(tipoApenso);
		apensoAdmServico.save(apensoAdministrativo);
		Assert.assertNotNull(apensoAdministrativo);
	}

	@Test(expected = SimtrRequisicaoException.class)
	public void saveValidaProtocoloSICLGProibido() {
		ApensoAdministrativo apensoAdministrativo = new ApensoAdministrativo();
		TipoApensoEnum tipoApenso = TipoApensoEnum.AX;
		apensoAdministrativo.setTipoApenso(tipoApenso);
		ProcessoAdministrativo processoAdministrativo = new ProcessoAdministrativo();
		processoAdministrativo.setId(1L);
		apensoAdministrativo.setProcessoAdministrativo(processoAdministrativo);
		ContratoAdministrativo contratoAdministrativo = new ContratoAdministrativo();
		contratoAdministrativo.setId(1L);
		apensoAdministrativo.setContratoAdministrativo(contratoAdministrativo);
		apensoAdmServico.save(apensoAdministrativo);
		Assert.assertNotNull(apensoAdministrativo);
	}

	@Test
	public void aplicaPatch() {
		ApensoAdministrativo apensoPatch = new ApensoAdministrativo();	
		apensoPatch.setProtocoloSICLG("hgjkgkgkkk");
		apensoPatch.setProcessoAdministrativo(new ProcessoAdministrativo());
		apensoPatch.setContratoAdministrativo(new ContratoAdministrativo());
		apensoPatch.setCpfCnpjFornecedor("11111111111");
		apensoPatch.setDescricaoApenso("descricao");
		apensoPatch.setDataHoraInclusao(Calendar.getInstance());
		apensoPatch.setMatriculaInclusao("c999999");
		apensoPatch.setTipoApenso(TipoApensoEnum.PP);
		apensoPatch.setTitulo("titulo");
		mockQuery();		
		mockSingleResult();
		mockProcessoAdministrativo();
		mockContratoAdministrativoServico();
		apensoAdmServico.aplicaPatch( 1L, apensoPatch);
		Assert.assertNotNull(apensoPatch);

	}
	
	@Test (expected = SimtrRequisicaoException.class)
	public void aplicaPatchApensoVazio() {
		ApensoAdministrativo apensoPatch = new ApensoAdministrativo();
		apensoPatch.setTipoApenso(TipoApensoEnum.PP);
		mockQuery();		
		mockSingleResult();
		mockProcessoAdministrativo();
		mockContratoAdministrativoServico();
		apensoAdmServico.aplicaPatch( 1L, apensoPatch);
		Assert.assertNotNull(apensoPatch);

	}
	
	private void mockContratoAdministrativoServico() {
		Mockito.when(contratoAdministrativoServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(new ContratoAdministrativo());
	}
	
	private void mockProcessoAdministrativo() {
		Mockito.when(processoAdministrativoServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(new ProcessoAdministrativo());
	}
	
	@Test(expected = SimtrRequisicaoException.class)
	public void criaDocumentoApensoAdministrativoCanalNull() {
		Mockito.when(canalServico.getByClienteSSO()).thenReturn(null);		
		this.apensoAdmServico.criaDocumentoApensoAdministrativo(idApensoAdminstrativo, idTipoDocumento, idDocumentoSubstituicao, 
				justificativaSubstituicao, origemDocumentoEnum, confidencial, valido, descricao, mimeType, atributosDocumento, binario);
	}
	
	@Test(expected = SimtrRequisicaoException.class)
	public void criaDocumentoApensoAdministrativoTipoDocumentoNull() {
		mockObjCanal();	
		Mockito.when(tipoDocumentoServico.getById(idTipoDocumento)).thenReturn(null);		
		this.apensoAdmServico.criaDocumentoApensoAdministrativo(idApensoAdminstrativo, idTipoDocumento, idDocumentoSubstituicao, 
				justificativaSubstituicao, origemDocumentoEnum, confidencial, valido, descricao, mimeType, atributosDocumento, binario);
	}
	
	@Test(expected = SimtrRequisicaoException.class)
	public void criaDocumentoApensoAdministrativoApensoAdminNull() {
		mockObjCanal();	
		mockObjTipoDocumento();	
		mockQuery();
		mockSingleResultNull();
		this.apensoAdmServico.criaDocumentoApensoAdministrativo(idApensoAdminstrativo, idTipoDocumento, idDocumentoSubstituicao, 
				justificativaSubstituicao, origemDocumentoEnum, confidencial, valido, descricao, mimeType, atributosDocumento, binario);
	}
	
	@Test  (expected = SimtrRequisicaoException.class)
	public void criaDocumentoApensoAdministrativoValidaMineTypeConteudoERRORNaoEncontrado() {		
		mockObjCanal();	
		mockObjTipoDocumento();	
		mockSingleResult();
		mimeType = "fdssd";
		binario = "data:image/gif;base64,...";
		formatoConteudoEnum = FormatoConteudoEnum.PDF;
		Mockito.when( documentoAdministrativoServico.validaMimetypeConteudo( formatoConteudoEnum, binario )).thenReturn(true);
		this.apensoAdmServico.criaDocumentoApensoAdministrativo(idApensoAdminstrativo, idTipoDocumento, idDocumentoSubstituicao, 
				justificativaSubstituicao, origemDocumentoEnum, confidencial, valido, descricao, mimeType, atributosDocumento, binario);
		
	}
	
	@Test  (expected = SimtrRequisicaoException.class)
	public void criaDocumentoApensoAdministrativoValidaMineTypeConteudoERROValidar() {		
		mockObjCanal();	
		mockObjTipoDocumento();	
		mockSingleResult();
		mockObjsValidarImagemErrado();
		Mockito.when( documentoAdministrativoServico.validaMimetypeConteudo( formatoConteudoEnum, binario )).thenReturn(true);
		this.apensoAdmServico.criaDocumentoApensoAdministrativo(idApensoAdminstrativo, idTipoDocumento, idDocumentoSubstituicao, 
				justificativaSubstituicao, origemDocumentoEnum, confidencial, valido, descricao, mimeType, atributosDocumento, binario);
		
	}
	
	@Test (expected = SimtrRequisicaoException.class)
	public void criaDocumentoApensoAdministrativoValidaMineTypeConteudoSucessoValidar() {		
		mockObjCanal();	
		mockObjTipoDocumento();	
		mockSingleResult();
		mockObjsValidarImagemCorreto();
		idDocumentoSubstituicao = 1L;
		Mockito.when( documentoAdministrativoServico.validaMimetypeConteudo( formatoConteudoEnum, binario )).thenReturn(true);
		this.apensoAdmServico.criaDocumentoApensoAdministrativo(idApensoAdminstrativo, idTipoDocumento, idDocumentoSubstituicao, 
				justificativaSubstituicao, origemDocumentoEnum, confidencial, valido, descricao, mimeType, atributosDocumento, binario);
		
	}
	
	@Test (expected = SimtrRequisicaoException.class)
	public void criaRelatorioPAEapensoAdminNull() throws IOException {
		Long idApenso = 1L;
		mockQuery();
		mockSingleResultNull();
		this.apensoAdmServico.criaRelatorioPAE(idApenso, false, false);
		Assert.assertNull(idApenso);
		
	}
	
//	@Test (expected = SimtrConfiguracaoException.class)
//	public void criaRelatorioPAEapensoAdmin() throws IOException {
//		Long idApenso = 1L;
//		mockQuery();
//		mockSingleResult();
//		this.apensoAdmServico.criaRelatorioPAE(idApenso, false, false);
//		
//	}
	
	@Test (expected = SimtrRequisicaoException.class)
	public void criaRelatorioPAEapensoAdminNRE() throws IOException {
		Long idApenso = 1L;
		mockQuery();
		mockSingleResultNRE();
		this.apensoAdmServico.criaRelatorioPAE(idApenso, false, false);
		
	}
	
	
	@Test 
	public void criaRelatorioPAEapensoAdminSuccess() throws IOException {
		Long idApenso = 1L;
		mockQuery();
		mockSingleResult();
		mockArquivoZip();
		Assert.assertNotNull(this.apensoAdmServico.criaRelatorioPAE(idApenso, false, false));
		
	}


	private void mockSingleResultNull() {
		Mockito.when( query.getSingleResult()).thenReturn(null);
	}

	private void mockQuery() {
		Mockito.when( manager.createQuery(Mockito.anyString(), Mockito.eq(ApensoAdministrativo.class))).thenReturn(query);
	}
	
	@Test
	public void testeGetByProtocoloSICLGTest() {
		mockSingleResult();
		this.apensoAdmServico.getByProtocoloSICLG("1230129", Boolean.TRUE, Boolean.TRUE);
		
	}
	
	@Test
	public void testeGetByProtocoloSICLGTestNRE() {
		mockObjApensoAdminNRE();
		this.apensoAdmServico.getByProtocoloSICLG("1230129", Boolean.TRUE, Boolean.TRUE);
		
	}

	private void mockObjsValidarImagemErrado() {
		mimeType = "image/jpg";
		binario = "data:image/gif;base64,...";
		formatoConteudoEnum = FormatoConteudoEnum.JPEG;
	}

	private void mockObjsValidarImagemCorreto() {
		mimeType = "application/pdf";
		binario = "data:image/gif;base64,...";
		formatoConteudoEnum = FormatoConteudoEnum.PDF;
	}

	private void mockSingleResult() {
		ApensoAdministrativo apensoAdministrativo = new ApensoAdministrativo();
		apensoAdministrativo.setId(1L);
		apensoAdministrativo.setProcessoAdministrativo(new ProcessoAdministrativo());
		apensoAdministrativo.getProcessoAdministrativo().setNumeroProcesso(12311);
		apensoAdministrativo.getProcessoAdministrativo().setAnoProcesso(2020);
		apensoAdministrativo.setTipoApenso(TipoApensoEnum.AX);
		DocumentoAdministrativo docAdm = new DocumentoAdministrativo();
		docAdm.setId(1L);
		apensoAdministrativo.getDocumentosAdministrativos().add(docAdm);
		
		mockQuery();
		Mockito.when( query.getSingleResult()).thenReturn(apensoAdministrativo);
	}
	
	
	private void mockSingleResultNRE() {
		Mockito.doThrow(NoResultException.class).when(query).getSingleResult();
	}
	
	private void mockArquivoZip() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zipOut = new ZipOutputStream(baos);
		try {
			Mockito.when(this.relatorioServico.adicionaArquivoZIP(Mockito.any(), Mockito.any(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString())).thenReturn(zipOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void mockObjApensoAdminNRE() {
		mockQuery();
		Mockito.doThrow(NoResultException.class).when(query).getSingleResult();
	}

	private void mockObjTipoDocumento() {
		TipoDocumento tipoDocumento = new TipoDocumento();
		tipoDocumento.setId(1);
		Mockito.when(tipoDocumentoServico.getById(idTipoDocumento)).thenReturn(tipoDocumento);
	}

	private void mockObjCanal() {
		Canal canal = new Canal();
		canal.setId(1);
		Mockito.when(canalServico.getByClienteSSO()).thenReturn(canal);
	}

}
