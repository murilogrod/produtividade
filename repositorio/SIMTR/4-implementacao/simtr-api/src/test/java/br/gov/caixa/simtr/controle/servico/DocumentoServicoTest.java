package br.gov.caixa.simtr.controle.servico;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.AtributosDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.DadosDocumentoLocalizadoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.DocumentoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.RespostaGravaDocumentoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.RetornoPesquisaDTO;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.GEDService;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.pedesgo.arquitetura.util.UtilPDF;
import br.gov.caixa.pedesgo.arquitetura.util.UtilParametro;
import br.gov.caixa.pedesgo.arquitetura.util.UtilWS;
import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.Autorizacao;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.Conteudo;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DossieClientePF;
import br.gov.caixa.simtr.modelo.entidade.InstanciaDocumento;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDocumento;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.CalendarUtil;
import br.gov.caixa.simtr.util.KeycloakUtil;

/**
 * <p>
 * DocumentoServicoTest
 * </p>
 *
 * <p>
 * Descrição: Classe de teste do servico DocumentoServico
 * </p>
 *
 * <br>
 * <b>Empresa:</b> Cef - Caixa Econômica Federal
 *
 *
 * @author f798783 - Welington Junio
 *
 * @version 1.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ UtilParametro.class, UtilWS.class, Base64.class, UtilPDF.class, Boolean.class })
public class DocumentoServicoTest {

	/**
	 * Atributo manager.
	 */
	@Mock
	private EntityManager manager;

	/**
	 * Atributo query.
	 */
	@Mock
	private TypedQuery<Documento> query;

	@Mock
	private CanalServico canalServico;

	@Mock
	private TipoDocumentoServico tipoDocumentoServico;

	@Mock
	private KeycloakService keycloakService;

	@Mock
	private GEDService gedService;
        
        @Mock
        private SiecmServico siecmServico;

	@Mock
	private DocumentoServico self;

	@Mock
	private CalendarUtil calendarUtil = new CalendarUtil();

	@Mock
	private KeycloakUtil keycloakUtil;
        
        @Mock
        private SituacaoDocumentoServico situacaoDocumentoServico;

	/**
	 * Atributo documentoServico.
	 */
	@InjectMocks
	private DocumentoServico documentoServico;

	/**
	 * Atributo Logger.
	 */
	@Mock
	private Logger logger;

	// variaveis Globais para o teste
	Long id;
	Long cpfCnpj;
	boolean vinculacaoConteudos;
	boolean vinculacaoDossiesCliente;
	boolean vinculacaoCanal;
	boolean vinculacaoTipoDocumental;
	boolean vinculacaoTipoDocumento;
	boolean vinculacaoAtributos;
	boolean vinculacaoInstancias;
	TipoPessoaEnum tipoPessoaEnum;
	Documento documento;
	TipoDocumento tipoDocumento;
	AtributoDocumento atributosDocumento;
	AtributoExtracao atributoExtracao;
	Canal canalCaptura;
	Autorizacao autorizacao;
	String imagemBase64;

	/**
	 * <p>
	 * Método responsável por inicializar os mocks
	 * </p>
	 * .
	 *
	 * @author p541915
	 *
	 */
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		// Mockito.when(documentoServico.savar())
		vinculacaoDossiesCliente = true;
		vinculacaoCanal = true;
		vinculacaoTipoDocumento = true;
		vinculacaoAtributos = true;
		vinculacaoInstancias = true;
		PowerMockito.mockStatic(UtilParametro.class, UtilWS.class, Base64.class, UtilPDF.class, Boolean.class);
		Mockito.when(keycloakUtil.getIpFromToken()).thenReturn("127.0.0.1");

	}

	private void mokarUtilsWsObterBytes(byte[] bytes) {
		PowerMockito.mockStatic(UtilWS.class);
		PowerMockito.when(UtilWS.obterBytes(Matchers.anyString())).thenReturn(bytes);
	}

	private void mockarBase64(byte[] bytes) {
		Encoder encoder = PowerMockito.mock(Encoder.class);
		PowerMockito.mockStatic(Base64.class);
		PowerMockito.when(Base64.getEncoder()).thenReturn(encoder);
		PowerMockito.when(encoder.encodeToString(bytes)).thenReturn("link");
	}

	@Test
	public void getByGedIdTestSucess() {

		final String gedId = "B004E063-0000-CA96-B4A9-898B3EF18787";
		Documento doc = new Documento();
		doc.setCodigoGED("B004E063-0000-CA96-B4A9-898B3EF18787");
		Mockito.when(manager.createQuery(Mockito.anyString(), Mockito.eq(Documento.class))).thenReturn(query);
		Mockito.when(query.getSingleResult()).thenReturn(doc);
		final Documento documento = documentoServico.getByGedId(gedId, vinculacaoDossiesCliente, vinculacaoCanal,
				vinculacaoTipoDocumento, vinculacaoAtributos, vinculacaoInstancias);
		Assert.assertEquals(gedId, documento.getCodigoGED());

	}

	@Test
	public void getByGedIdTestNoResult() {

		final String gedID = "B004E063-0000-CA96-B4A9-898B3EF18787";
		final Documento doc = new Documento();
		doc.setCodigoGED("B004E063-0000-B4A9-CA96-898B3EF18787");
		Mockito.when(manager.createQuery(Mockito.anyString(), Mockito.eq(Documento.class))).thenReturn(query);
		Mockito.doThrow(NoResultException.class).when(query).getSingleResult();
		final Documento documento = documentoServico.getByGedId(gedID, vinculacaoDossiesCliente, vinculacaoCanal,
				vinculacaoTipoDocumento, vinculacaoAtributos, vinculacaoInstancias);
		Assert.assertNull(documento);

	}

	private void mokiGetByIdNoResult() {
		vinculacaoConteudos = true;
		vinculacaoDossiesCliente = true;
		vinculacaoCanal = true;
		vinculacaoTipoDocumento = true;
		vinculacaoAtributos = true;
		vinculacaoInstancias = true;

		Mockito.when(manager.createQuery(Mockito.anyString(), Mockito.eq(Documento.class))).thenReturn(query);

	}

	@Test
	public void getByIdSucess() {

		final Documento doc = new Documento();
		doc.setId(12L);
		id = doc.getId();
		mokiGetByIdNoResult();
		Mockito.when(query.getSingleResult()).thenReturn(doc);
		final Documento documento = documentoServico.getById(id, vinculacaoConteudos, vinculacaoDossiesCliente,
				vinculacaoCanal, vinculacaoTipoDocumento, vinculacaoAtributos, vinculacaoInstancias);
		Assert.assertEquals(doc.getId(), documento.getId());

	}

	@Test
	public void getByIdNoResult() {
		final Documento doc = new Documento();
		doc.setId(12L);
		id = doc.getId();
		mokiGetByIdNoResult();
		Mockito.doThrow(NoResultException.class).when(query).getSingleResult();
		final Documento documento = documentoServico.getById(id, vinculacaoConteudos, vinculacaoDossiesCliente,
				vinculacaoCanal, vinculacaoTipoDocumento, vinculacaoAtributos, vinculacaoInstancias);
		Assert.assertNull(documento);

	}

	@Test
	public void getByIdNoResultVinculadoConteudosTrue() {
		final Documento doc = new Documento();
		doc.setId(12L);
		doc.setCodigoGED("CAIXAGED");
		id = doc.getId();
		mokiGetByIdNoResult();
		Mockito.when(query.getSingleResult()).thenReturn(doc);

		RetornoPesquisaDTO retornoGED = new RetornoPesquisaDTO();
		List<DadosDocumentoLocalizadoDTO> dadosDocumentoLocalizados = new ArrayList<>();
		DadosDocumentoLocalizadoDTO docDocumentosLocalizadoDto = new DadosDocumentoLocalizadoDTO();
		docDocumentosLocalizadoDto.setLink("link");
		dadosDocumentoLocalizados.add(docDocumentosLocalizadoDto);
		retornoGED.setDadosDocumentoLocalizados(dadosDocumentoLocalizados);
		retornoGED.setQuantidade(1);
		Mockito.when(this.gedService.searchDocument(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(retornoGED);
		byte[] bytes = "Link".getBytes();
		mokarUtilsWsObterBytes(bytes);
		mockarBase64(bytes);

		final Documento documento = documentoServico.getById(id, vinculacaoConteudos, vinculacaoDossiesCliente,
				vinculacaoCanal, vinculacaoTipoDocumento, vinculacaoAtributos, vinculacaoInstancias);
		Assert.assertNotNull("", documento);

	}

	@Test(expected = SimtrRequisicaoException.class)
	public void insereDocumentoClienteNegocioCpfInvalidoTest() {
		tipoPessoaEnum = TipoPessoaEnum.F;
		cpfCnpj = null;
		setaDocumento();
		documentoServico.insereDocumentoClienteNegocio(cpfCnpj, tipoPessoaEnum, documento);
		Assert.assertNotNull(cpfCnpj);
	}

	@Test(expected = SimtrRequisicaoException.class)
	public void insereDocumentoClienteNegocioTipoDocumentoCnpjTest() {
		tipoPessoaEnum = TipoPessoaEnum.J;
		cpfCnpj = null;
		setaDocumento();
		mokiInserirDocumentoClienteNegocial();
	}

	@Test(expected = SimtrRequisicaoException.class)
	public void insereDocumentoClienteNegocioDocumentNullTest() {
		tipoPessoaEnum = TipoPessoaEnum.F;
		cpfCnpj = Long.parseLong("00065179609");
		documento = null;
		mokiInserirDocumentoClienteNegocial();
	}

	@Test(expected = SimtrRequisicaoException.class)
	public void insereDocumentoClienteNegocioTipoDocumentNullTest() {
		tipoPessoaEnum = TipoPessoaEnum.F;
		cpfCnpj = Long.parseLong("00065179609");
		documento = new Documento();
		mokiInserirDocumentoClienteNegocial();
	}

	@Test
	public void insereDocumentoOperacaoNegocioTeste() {
		mockDocumento();
		setarTipoDocumento();
		setarAtributoExtracao(null, null);
		setaCaptura();
		mockGedServiceCreateDocument0();
		documentoServico.insereDocumentoOperacaoNegocio(1L, documento);
	}

	private void setarTipoDocumento() {
		tipoDocumento = new TipoDocumento();
		tipoDocumento.setId(1);
                tipoDocumento.setNome("Tipo de Documento Mockado");
		tipoDocumento.setPrazoValidade(20);
		tipoDocumento.setValidadeAutoContida(Boolean.FALSE);
		documento.setTipoDocumento(tipoDocumento);
	}

	private void setarAtributoExtracao(Boolean gedObrigatorio, TipoAtributoEnum tipoAtributoEnum) {
		atributoExtracao = new AtributoExtracao();
		atributoExtracao.setAtivo(Boolean.TRUE);
		atributoExtracao.setObrigatorio(Boolean.TRUE);
		atributoExtracao.setNomeAtributoDocumento("NomeAtributo");
		atributoExtracao.setNomeAtributoSIECM("NomeAtributoGED");
		atributoExtracao.setNomeAtributoRetorno("NomeAtributoRetorno");
		atributoExtracao.setUtilizadoCalculoValidade(Boolean.TRUE);
		atributoExtracao.setUtilizadoIdentificadorPessoa(Boolean.TRUE);
		atributoExtracao.setObrigatorioSIECM(gedObrigatorio);
		atributoExtracao.setTipoAtributoSiecmEnum(tipoAtributoEnum);
		documento.getTipoDocumento().addAtributosExtracao(atributoExtracao);
	}

	private void setarAtributoExtracaoDiferente() {
		atributoExtracao = new AtributoExtracao();
		atributoExtracao.setAtivo(Boolean.TRUE);
		atributoExtracao.setObrigatorio(Boolean.TRUE);
		atributoExtracao.setNomeAtributoDocumento("NomeAtributoDiferente");
		atributoExtracao.setNomeAtributoSIECM("NomeAtributoGEDDiferente");
		atributoExtracao.setNomeAtributoRetorno("NomeAtributoRetornoDiferente");
		atributoExtracao.setUtilizadoCalculoValidade(Boolean.TRUE);
		atributoExtracao.setUtilizadoIdentificadorPessoa(Boolean.TRUE);
		documento.getTipoDocumento().addAtributosExtracao(atributoExtracao);
	}

	@Test(expected = SimtrRequisicaoException.class)
	public void insereDocumentoClienteNegocioAtributosDocumentoNullTest() {
		tipoPessoaEnum = TipoPessoaEnum.F;
		cpfCnpj = Long.parseLong("00065179609");
		documento = new Documento();
		setarTipoDocumento();
		documento.setAtributosDocumento(null);
		mokiInserirDocumentoClienteNegocial();
	}

	private void mokiInserirDocumentoClienteNegocial() {
		documentoServico.insereDocumentoClienteNegocio(cpfCnpj, tipoPessoaEnum, documento);
		Assert.assertNotNull(documento);
	}

	@Test(expected = SimtrRequisicaoException.class)
	public void insereDocumentoClienteNegocioCanalCapturaNullTest() {
		tipoPessoaEnum = TipoPessoaEnum.F;
		cpfCnpj = Long.parseLong("00065179609");
		documento = new Documento();
		setarTipoDocumento();
		setarAtributosDocumentos();
		mokiInserirDocumentoClienteNegocial();
	}

	@Test
	public void insereDocumentoClienteDocumentoLocalizadoNullTest() {
		tipoPessoaEnum = TipoPessoaEnum.F;
		cpfCnpj = Long.parseLong("00065179609");
		Mockito.when(manager.createQuery(Mockito.anyString(), Mockito.eq(Documento.class))).thenReturn(query);
		Mockito.when(query.setMaxResults(1)).thenReturn(query);
		final RespostaGravaDocumentoDTO respostaGravaDocumentoDTO = new RespostaGravaDocumentoDTO();
		respostaGravaDocumentoDTO.setCodigoRetorno(0);

		AtributosDTO atributosDTO = new AtributosDTO();
		atributosDTO.setId("378643-ddvyv3-dcnjen-djndjed");
		DocumentoDTO documentoDTO = new DocumentoDTO();
		documentoDTO.setAtributos(atributosDTO);
		respostaGravaDocumentoDTO.setDocumentos(Arrays.asList(documentoDTO));

                SituacaoDocumento situacaoSubstituido = new SituacaoDocumento();
                situacaoSubstituido.setNome("Substituido");
                situacaoSubstituido.setSituacaoFinal(Boolean.TRUE);
                
                Mockito.when(this.situacaoDocumentoServico.getBySituacaoDocumentoEnum(SituacaoDocumentoEnum.SUBSTITUIDO)).thenReturn(situacaoSubstituido);
                Mockito.when(this.tipoDocumentoServico.getByTipologia(Mockito.anyString())).thenReturn(new TipoDocumento());
                
		Mockito.when(gedService.createDocumentIntoFolder(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.any(String.class), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyString()))
				.thenReturn(respostaGravaDocumentoDTO);
		Mockito.when(query.getSingleResult()).thenReturn(null);

		documento = new Documento();
		documento.setFormatoConteudoEnum(FormatoConteudoEnum.JPEG);
		Conteudo conteudo = new Conteudo();
		conteudo.setBase64("3428723672hdydchdd98==");
		conteudo.setId(1L);
		conteudo.setOrdem(1);
		documento.addConteudos(conteudo);
		setarTipoDocumento();
                setarAtributoExtracao(Boolean.TRUE, TipoAtributoEnum.STRING);
		setarAtributosDocumentos();
		setaCaptura();
		mokiInserirDocumentoClienteNegocial();
	}

	private void mockGedServiceCreateDocument0() {
		RespostaGravaDocumentoDTO grava = new RespostaGravaDocumentoDTO();
		grava.setCodigoRetorno(0);
		grava.setMensagem("Mensagem");
		Mockito.when(gedService.createDocumentIntoFolder(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.any(String.class), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyString()))
				.thenReturn(grava);
	}

	private void setarAtributosDocumentos() {
		atributosDocumento = new AtributoDocumento();
		atributosDocumento.setId(1L);
		atributosDocumento.setConteudo("10/11/2010");
		atributosDocumento.setDescricao("NomeAtributo");
		atributosDocumento.setAcertoManual(Boolean.TRUE);
		documento.getAtributosDocumento().add(atributosDocumento);
	}

	// Teste pra metodo Localizar documento mais Recente
	@Test(expected = IllegalArgumentException.class)
	public void localizaDocumentoMaisRecenteCPFNullTest() {
		cpfCnpj = null;
		documentoServico.localizaDocumentoMaisRecenteClienteByCpfCnpj(cpfCnpj, TipoPessoaEnum.F, tipoDocumento, vinculacaoCanal, vinculacaoTipoDocumental, vinculacaoAtributos, vinculacaoInstancias);
		Assert.assertNotNull(cpfCnpj);
	}

	@Test(expected = IllegalArgumentException.class)
	public void localizaDocumentoMaisRecenteTipoDocuementoNullTest() {
		cpfCnpj = Long.parseLong("00065179609");
		tipoDocumento = null;
		documentoServico.localizaDocumentoMaisRecenteClienteByCpfCnpj(cpfCnpj, TipoPessoaEnum.F, tipoDocumento, vinculacaoCanal, vinculacaoTipoDocumental, vinculacaoAtributos, vinculacaoInstancias);
		Assert.assertNotNull(cpfCnpj);
	}

	@Test
	public void localizaDocumentoMaisRecenteTestSucess() {
		MokiLocalizaDocumentoMaisRecente();
		Mockito.when(query.getSingleResult()).thenReturn(documento);
		Documento doc = documentoServico.localizaDocumentoMaisRecenteClienteByCpfCnpj(cpfCnpj, tipoPessoaEnum, tipoDocumento, vinculacaoCanal, vinculacaoTipoDocumental, vinculacaoAtributos, vinculacaoInstancias);
		Assert.assertEquals(doc.getId(), documento.getId());
	}

	@Test
	public void localizaDocumentoMaisRecenteTestNotResult() {
		MokiLocalizaDocumentoMaisRecente();
		Mockito.doThrow(NoResultException.class).when(query).getSingleResult();
		Documento doc = documentoServico.localizaDocumentoMaisRecenteClienteByCpfCnpj(cpfCnpj, tipoPessoaEnum, tipoDocumento, vinculacaoCanal, vinculacaoTipoDocumental, vinculacaoAtributos, vinculacaoInstancias);
		Assert.assertNull(doc);
	}

	private void MokiLocalizaDocumentoMaisRecente() {
		parametrosMokLocalizarRecentes();
		Mockito.when(manager.createQuery(Mockito.anyString(), Mockito.eq(Documento.class))).thenReturn(query);
		Mockito.when(query.setMaxResults(1)).thenReturn(query);
	}

	private void parametrosMokLocalizarRecentes() {
		cpfCnpj = Long.parseLong("00065179609");
                tipoPessoaEnum = TipoPessoaEnum.F;
		tipoDocumento = new TipoDocumento();
		tipoDocumento.setId(1);
		vinculacaoCanal = true;
		vinculacaoTipoDocumental = true;
		vinculacaoAtributos = true;
		vinculacaoInstancias = true;
		vinculacaoDossiesCliente = true;
		setaDocumento();
	}

	@Test(expected = SimtrRequisicaoException.class)
	public void insereDocumentoOperacaoDossieDigitalAutorizacaoNullTest() {
		documentoServico.insereDocumentoOperacaoDossieDigital(autorizacao, documento, imagemBase64);
		Assert.assertNotNull(cpfCnpj);
	}

	@Test(expected = SimtrRequisicaoException.class)
	public void insereDocumentoOperacaoDossieDigitalDocumentoNullTest() {
		setaAutorizacao();
		documentoServico.insereDocumentoOperacaoDossieDigital(autorizacao, documento, imagemBase64);
		Assert.assertNotNull(cpfCnpj);
	}

	@Test(expected = SimtrRequisicaoException.class)
	public void insereDocumentoOperacaoDossieDigitalTipoDocumentoNullTest() {
		setaAutorizacao();
		setaDocumento();
		documentoServico.insereDocumentoOperacaoDossieDigital(autorizacao, documento, imagemBase64);
		Assert.assertNotNull(cpfCnpj);
	}

	@Test(expected = SimtrRequisicaoException.class)
	public void insereDocumentoOperacaoDossieDigitalAtributosDocumentoNullTest() {
		setaAutorizacao();
		setaDocumento();
		setarTipoDocumento();
		documento.setAtributosDocumento(null);
		documentoServico.insereDocumentoOperacaoDossieDigital(autorizacao, documento, imagemBase64);
		Assert.assertNotNull(cpfCnpj);
	}

	@Test(expected = SimtrRequisicaoException.class)
	public void insereDocumentoOperacaoDossieDigitalCanalCapturaNullTest() {
		setaAutorizacao();
		setaDocumento();
		setarTipoDocumento();
		setarAtributosDocumentos();
		documentoServico.insereDocumentoOperacaoDossieDigital(autorizacao, documento, imagemBase64);
		Assert.assertNotNull(cpfCnpj);
	}

	@Test
	public void insereDocumentoOperacaoDossieDigitalCpfFormatadoEPastaAutorizacaoTest() {
		setaAutorizacao();
		setaDocumento();
		setarTipoDocumento();
		setarAtributosDocumentos();
		setaCaptura();
		final Documento doc = new Documento();
		final TipoDocumento tpDoc = new TipoDocumento();
		doc.setCanalCaptura(new Canal());
		tpDoc.setNomeClasseSIECM("nomeClasseGed");
		doc.setTipoDocumento(tpDoc);

		final RespostaGravaDocumentoDTO respostaGravaDocumentoDTO = new RespostaGravaDocumentoDTO();
		respostaGravaDocumentoDTO.setCodigoRetorno(0);

		Mockito.when(gedService.createDocument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(respostaGravaDocumentoDTO);
                Mockito.when(siecmServico.armazenaDocumentoOperacaoSIECM(Mockito.any(Documento.class), Mockito.any(TemporalidadeDocumentoEnum.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("sdasda-wewerewsc-werscdww");
		Documento docSalvo = documentoServico.insereDocumentoOperacaoDossieDigital(autorizacao, doc, imagemBase64);
		Assert.assertNotNull(docSalvo);
	}

	private void setaCaptura() {
		canalCaptura = new Canal();
		canalCaptura.setId(1);
		canalCaptura.setIndicadorAtualizacaoDocumento(Boolean.TRUE);
		canalCaptura.setIndicadorAvaliacaoAutenticidade(Boolean.TRUE);
		canalCaptura.setJanelaExtracaoM0(Boolean.TRUE);
		canalCaptura.setJanelaExtracaoM30(Boolean.TRUE);
		canalCaptura.setJanelaExtracaoM60(Boolean.TRUE);
		documento.setCanalCaptura(canalCaptura);
	}

	private void setaDocumento() {
		documento = new Documento();
		documento.setId(1L);
	}

	private void setaAutorizacao() {
		autorizacao = new Autorizacao();
		autorizacao.setId(1L);
		autorizacao.setCpfCnpj(65179609L);
		autorizacao.setCodigoNSU(255142L);
	}

	@Test(expected = SimtrRequisicaoException.class)
	public void insereDocumentoOperacaoNegocioDocumentoNull() {
		mokiAssertInsereDOcumentoOperacaoNegocio();
	}

	private void mokiAssertInsereDOcumentoOperacaoNegocio() {
		documentoServico.insereDocumentoOperacaoNegocio(1L, documento);
		Assert.assertNotNull(cpfCnpj);
	}

	@Test(expected = SimtrRequisicaoException.class)
	public void insereDocumentoOperacaoNegocioTipoDocumentoNull() {
		setaDocumento();
		mokiAssertInsereDOcumentoOperacaoNegocio();
	}

	@Test(expected = SimtrRequisicaoException.class)
	public void insereDocumentoOperacaoNegocioAtributosDocumentoNull() {
		setaDocumento();
		setarTipoDocumento();
		mokiAssertInsereDOcumentoOperacaoNegocio();
	}

	@Test(expected = SimtrRequisicaoException.class)
	public void insereDocumentoOperacaoCanalCapturaNull() {
		setaDocumento();
		setarTipoDocumento();
		documento.setAtributosDocumento(null);
		mokiAssertInsereDOcumentoOperacaoNegocio();
	}

	@Test(expected = IllegalArgumentException.class)
	public void defineDataValidadeTipoDocumentoNullTest() {
		setaDocumento();
		documentoServico.defineDataValidade(documento);
	}

	@Test
	public void defineDataValidadeTipoDocumentoComAtributosFalhaParse() throws ParseException {
		Mockito.doThrow(ParseException.class).when(calendarUtil).toCalendar(Mockito.anyString(), Mockito.anyBoolean());
		setaDocumento();
		setarTipoDocumento();
		setarAtributosDocumentos();
		setarAtributoExtracao(null, null);
		documentoServico.defineDataValidade(documento);
	}

	@Test
	public void defineDataValidadeTipoDocumentoComAtributos() throws ParseException {
		Mockito.when(calendarUtil.toCalendar(Mockito.anyString(), Mockito.anyBoolean()))
				.thenReturn(Calendar.getInstance());
		setaDocumento();
		setarTipoDocumento();
		setarAtributosDocumentos();
		setarAtributoExtracao(null, null);
		documentoServico.defineDataValidade(documento);
	}

	@Test
	public void defineDataValidadeAtributosDocumentoNullTest() throws ParseException {
		Mockito.when(calendarUtil.toCalendar(Mockito.anyString(), Mockito.anyBoolean()))
				.thenReturn(Calendar.getInstance());
		setaDocumento();
		setarTipoDocumento();
		documentoServico.defineDataValidade(documento);
		Assert.assertNotNull(documento.getDataHoraValidade());
	}

	@Test(expected = SimtrEstadoImpeditivoException.class)
	public void validaAtributosDocumentoTestErroTipoDocumentoNulo() {

		final Documento documento = new Documento();
		TipoDocumento tpdoc = null;
		documento.setTipoDocumento(tpdoc);

		documentoServico.validaAtributosDocumento(documento);
		Assert.assertTrue(false);
	}

	@Test(expected = SimtrRequisicaoException.class)
	public void validaAtributosDocumentoTestAtributosDocumentoException() {

		setaDocumento();
		setarTipoDocumento();
		setarAtributosDocumentos();
		setarAtributoExtracaoDiferente();
		documentoServico.validaAtributosDocumento(documento);
	}

	@Test(expected = SimtrRequisicaoException.class)
	public void validaAtributosDocumentoTestCompleto() {

		setaDocumento();
		setarTipoDocumento();
		setarAtributosDocumentos();
		setarAtributoExtracaoDiferente();
		setarAtributoExtracao(null, null);
		documentoServico.validaAtributosDocumento(documento);
	}

	@Test
	public void listDocumentosDefinitivosDossieDigital() {
		Mockito.when(manager.createQuery(Mockito.anyString(), Mockito.eq(Documento.class))).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(new ArrayList<>());
		Assert.assertNotNull(documentoServico.listDocumentosDefinitivosDossieDigital(11111111111L, TipoPessoaEnum.F));
	}

	@Test
	public void testListaDocumentosNaoEncontradosGEDDefinitivosDossieDigital() {
		Mockito.when(manager.createQuery(Mockito.anyString(), Mockito.eq(Documento.class))).thenReturn(query);
		List<Documento> listDocumentos = new ArrayList<Documento>();
		final Documento documento = new Documento();
		TipoDocumento tpdoc = new TipoDocumento();
		documento.setTipoDocumento(tpdoc);

		documento.setAtributosDocumento(null);
		listDocumentos.add(documento);
		Mockito.when(query.getResultList()).thenReturn(listDocumentos);
		RetornoPesquisaDTO retorno = new RetornoPesquisaDTO();
		retorno.setCodigoRetorno(10);
		retorno.setQuantidade(0);
		Mockito.when(this.gedService.searchDocument(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(retorno);
		Assert.assertNotNull(documentoServico.listDocumentosDefinitivosDossieDigital(11111111111L, TipoPessoaEnum.F));
	}

	@Test
	public void validaAtributosDocumentoTestAtributosDocumentoNulo() {

		final Documento documento = new Documento();
		TipoDocumento tpdoc = new TipoDocumento();
		documento.setTipoDocumento(tpdoc);
		documento.setAtributosDocumento(null);

		documentoServico.validaAtributosDocumento(documento);
	}

        @Test
	public void deleteDocumentoDossieClienteTeste() {
		mockDocumento();
		mockCreateQuery();
		mockSingleResult();
		this.documentoServico.deleteDocumentoDossieCliente(1L, 1L);
	}

	@Test
	public void deleteDocumentoDossieClienteTesteSemInstancia() {
		mockDocumentoSemInstancia();
		mockCreateQuery();
		mockSingleResult();
		this.documentoServico.deleteDocumentoDossieCliente(1L, 1L);
	}

	private void mockCreateQuery() {
		Mockito.when(manager.createQuery(Mockito.anyString(), Mockito.eq(Documento.class))).thenReturn(query);
	}

	private void mockSingleResult() {
		Mockito.when(query.getSingleResult()).thenReturn(documento);
	}

	private void mockDocumento() {
		documento = new Documento();
		documento.setDossiesCliente(new HashSet<>());
		DossieClientePF dc = new DossieClientePF();
		documento.setFormatoConteudoEnum(FormatoConteudoEnum.PDF);
		dc.setId(1L);

		documento.getDossiesCliente().add(dc);
		InstanciaDocumento instancia = new InstanciaDocumento();
		instancia.setId(1L);
		documento.getInstanciasDocumento().add(instancia);

	}

	private void mockDocumentoSemInstancia() {
		documento = new Documento();
		documento.setCodigoGED("B004E063-0000-CA96-B4A9-898B3EF18787");
		documento.setDossiesCliente(new HashSet<>());
		DossieClientePF dc = new DossieClientePF();
		dc.setId(1L);
		documento.getDossiesCliente().add(dc);

	}

}
