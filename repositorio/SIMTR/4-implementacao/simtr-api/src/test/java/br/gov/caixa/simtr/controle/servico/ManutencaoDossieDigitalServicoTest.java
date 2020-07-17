package br.gov.caixa.simtr.controle.servico;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.AtributosDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.DadosDocumentoLocalizadoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.DocumentoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMAtributosDocumentoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMDadoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMDadosImagemDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMDocumentoLocalizadoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMRespostaDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMRespostaDefaultDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMRespostaExtracaoDadosDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.RespostaGravaDocumentoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.RetornoPesquisaDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.ged.ClasseBaseECM;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.GEDService;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.pedesgo.arquitetura.servico.sicpf.SicpfResponseDTO;
import br.gov.caixa.pedesgo.arquitetura.servico.sicpf.SicpfResultadoCpf;
import br.gov.caixa.pedesgo.arquitetura.siiso.dto.RetornoPessoasFisicasDTO;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.simtr.controle.excecao.DossieManutencaoException;
import br.gov.caixa.simtr.controle.excecao.SiecmException;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
//import br.gov.caixa.simtr.controle.excecao.SimtrException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.vo.DocumentoVO;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.Autorizacao;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.ComposicaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.DossieClientePF;
import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.RegraDocumental;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.EstrategiaPartilhaEnum;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.ModoPartilhaEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.SICPFCampoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.AtributoDocumentoDTO;
import br.gov.caixa.simtr.util.CalendarUtil;
import br.gov.caixa.simtr.util.ConstantesUtil;

/**
 *
 * @author Bruno Henrique - Fóton
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({UtilJson.class})
public class ManutencaoDossieDigitalServicoTest {

    @Mock
    private EntityManager entityManager;
    
    @Mock
    private CanalServico canalServico;

    @Mock
    private DossieClienteServico dossieClienteServico;

    @Mock
    private TipoDocumentoServico tipoDocumentoServico;

    @Mock
    private GEDService gedService;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private DocumentoServico documentoServico;

    @Mock
    private CadastroReceitaServico sicpfServico;

    @Mock
    private AvaliacaoFraudeServico avaliacaoFraudeServico;

    @Mock
    private RelatorioServico relatorioServico;

    @Mock
    private CadastroCaixaServico sicliServico;

    @Mock
    private AutorizacaoServico autorizacaoServico;

    @InjectMocks
    private DossieDigitalServico servico;
    
    @InjectMocks
    private ManutencaoDossieDigitalServico servicoOld;

    @Mock
    private CalendarUtil calendarUtil;

    @Mock
    private ComposicaoDocumentalServico composicaoDocumentalServico;

    @Mock
    private SiecmServico siecmServico;

    Canal canalSipan = new Canal();

    DossieCliente dossieCliente = new DossieCliente();
    
    final Long cpfCnpj = 957585691L;
    final Long id = 10L;
    final TipoPessoaEnum tipoPessoaEnum = TipoPessoaEnum.F;
    final Long codigoIntegracao = 123456789L;
    final FormatoConteudoEnum formatoConteudoEnum = FormatoConteudoEnum.TIF;
    final String nomeTipoDocumento = "IDENTIFICACAO";
    final List<String> imagensBase64 = new ArrayList<>();
    final TipoDocumento tipoDoc = new TipoDocumento();
    final String identificadorDocumento = "RG";
    final Long idDocumento = 580L;
    final Map<String, String> mapaDadosAtualizacao = new HashMap<>();
    private List<ComposicaoDocumental> composicoes;
    private Autorizacao autorizacao;
    List<DocumentoVO> documentosOperacaoVO = new ArrayList<>();
    List<AtributoDocumentoDTO> atributosDocumento = new ArrayList<>();
    Map<String, String> mapaAtributosDeclarados = new HashMap<>();
    List<br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.AtributoDocumentoDTO> listaAtributosDeclarados;

    @Test
    public void testFake() {
        Assert.assertTrue(Boolean.TRUE);
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(UtilJson.class);

        imagensBase64.add("xxxxx");

        Canal canalSipan = new Canal();
        canalSipan.setSigla("SIPAN");

        dossieCliente = new DossieClientePF();
        dossieCliente.setId(id);
        dossieCliente.setCpfCnpj(cpfCnpj);
        dossieCliente.setTipoPessoa(tipoPessoaEnum);

        tipoDoc.setNomeClasseSIECM("RG");
        tipoDoc.setEnviaAvaliacaoCadastral(Boolean.TRUE);
        tipoDoc.setEnviaAvaliacaoDocumental(Boolean.TRUE);
        tipoDoc.setValidadeAutoContida(true);

        Set<AtributoExtracao> atributosExtracao = tipoDoc.getAtributosExtracao();

        tipoDoc.setAtributosExtracao(carregarAtributosExtracao(atributosExtracao, Boolean.FALSE, Boolean.FALSE));

        ECMRespostaExtracaoDadosDTO ecmRespostaExtracaoDTO = new ECMRespostaExtracaoDadosDTO();
        ECMDadosImagemDTO ecmDadosImagemDTO = new ECMDadosImagemDTO();
        ECMDadoDTO dadosDTO = new ECMDadoDTO();
        dadosDTO.setNome("nome_retorno");
        dadosDTO.setValor(null);
        ecmDadosImagemDTO.addECMDadoDTO(dadosDTO);
        ecmRespostaExtracaoDTO.setCodigoRetorno(0);
        ecmRespostaExtracaoDTO.setEcmDadosImagemDTO(ecmDadosImagemDTO);

        ECMRespostaDTO ecmRespostaDTO = new ECMRespostaDTO();
        ecmRespostaDTO.setCodigoRetorno(0);

        RespostaGravaDocumentoDTO respostaGravaDocumentoDTO = new RespostaGravaDocumentoDTO();
        respostaGravaDocumentoDTO.setCodigoRetorno(0);

        List<DocumentoDTO> documentos = new ArrayList<>();
        DocumentoDTO docDTO = new DocumentoDTO();
        AtributosDTO atrDocs = new AtributosDTO();
        atrDocs.setId("2");
        docDTO.setAtributos(atrDocs);
        documentos.add(docDTO);
        respostaGravaDocumentoDTO.setDocumentos(documentos);

        RetornoPesquisaDTO retornoPesquisaDTO = new RetornoPesquisaDTO();
        retornoPesquisaDTO.setCodigoRetorno(0);
        List<DadosDocumentoLocalizadoDTO> dadosDocumentoLocalizados = new ArrayList<>();
        DadosDocumentoLocalizadoDTO dto = new DadosDocumentoLocalizadoDTO();
        dto.setLink("link");
        dadosDocumentoLocalizados.add(dto);
        retornoPesquisaDTO.setDadosDocumentoLocalizados(dadosDocumentoLocalizados);

        Documento documento = new Documento();
        Set<DossieCliente> dossiesCliente = new HashSet<>();
        DossieCliente dc = new DossieClientePF();
        dc.setCpfCnpj(cpfCnpj);
        dossiesCliente.add(dc);
        documento.setDossiesCliente(dossiesCliente);
        documento.setTipoDocumento(tipoDoc);
        Set<AtributoDocumento> atributosDocumento = new HashSet<>();
        AtributoDocumento atributoDocumento = new AtributoDocumento();
        atributoDocumento.setDescricao("nome");
        atributosDocumento.add(atributoDocumento);
        documento.setAtributosDocumento(atributosDocumento);
        mapaDadosAtualizacao.put("nome", "Jose");
        mapaDadosAtualizacao.put("mae", "Joaquina");

        ECMRespostaDefaultDTO ecmRespostaDefaultDTO = new ECMRespostaDefaultDTO();
        ecmRespostaDefaultDTO.setCodigoRetorno(0);

        mockCanalValido();
        Mockito.when(dossieClienteServico.getByCpfCnpj(cpfCnpj, tipoPessoaEnum, false, true, false))
                .thenReturn(dossieCliente);
        Mockito.when(tipoDocumentoServico.getByNome("IDENTIFICACAO")).thenReturn(tipoDoc);
        Mockito.when(keycloakService.getMatricula()).thenReturn("957585691");
        Mockito.when(gedService.extractInfo(tipoDoc.getNomeClasseSIECM(), formatoConteudoEnum.name(), "xxxxx",
                ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL)).thenReturn(ecmRespostaExtracaoDTO);
        Mockito.when(
                gedService.createDossie(cpfCnpj, TipoPessoaEnum.F.toString(), ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL))
                .thenReturn(ecmRespostaDTO);
        // Mockito.when(gedService.createDocumentForPFOrPJ(Mockito.any(), Mockito.any(),
        // Mockito.any(), Mockito.any(),
        // Mockito.any(), Mockito.any(), Mockito.any(),
        // Mockito.any())).thenReturn(respostaGravaDocumentoDTO);
        Mockito.when(gedService.searchDocument(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(retornoPesquisaDTO);
        Mockito.when(documentoServico.getByGedId(identificadorDocumento, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE,
                Boolean.FALSE, Boolean.TRUE)).thenReturn(documento);
        Mockito.when(gedService.updateInfoDocument(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(ecmRespostaDefaultDTO);
        Mockito.when(tipoDocumentoServico.getByTipologia(Mockito.anyString())).thenReturn(tipoDoc);
        Mockito.when(documentoServico.localizaDocumentoMaisRecenteClienteByCpfCnpj(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.any(TipoDocumento.class),
                Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(documento);
        // Mockito.when(sicpfServico.consultaCadastroPF(cpfCnpj)).thenReturn(ecmRespostaDefaultDTO);
        //
        // Mockito.doAnswer(sicpfServico.validaDocumentoSICPF(cpfCnpj, documento));

    }

    private Set<AtributoExtracao> carregarAtributosExtracao(Set<AtributoExtracao> atributosExtracao, boolean sicpfCampoEnumMae, boolean tipoAtributoEnumData) {
        AtributoExtracao at = new AtributoExtracao();
        at.setNomeAtributoRetorno("nome_retorno");
        at.setTipoAtributoSiecmEnum(tipoAtributoEnumData ? TipoAtributoEnum.DATE : TipoAtributoEnum.STRING);
        at.setNomeAtributoDocumento("nome");
        at.setObrigatorioSIECM(true);
        at.setSicpfCampoEnum(sicpfCampoEnumMae ? SICPFCampoEnum.NOME_MAE : null);
        at.setEstrategiaPartilhaEnum(EstrategiaPartilhaEnum.RECEITA_MAE);
        at.setModoPartilhaEnum(ModoPartilhaEnum.S);
        at.setNomeAtributoSIECM("nome_ged");

        AtributoExtracao atributoPartilha = at.getAtributoPartilha();
        at.setAtributoPartilha(atributoPartilha);
        at.setObrigatorioSIECM(true);
        atributosExtracao.add(at);

        AtributoExtracao at3 = new AtributoExtracao();
        at3.setNomeAtributoRetorno("nome_retorno1");
        at3.setNomeAtributoSIECM("nome_ged1");
        at3.setTipoAtributoSiecmEnum(TipoAtributoEnum.BOOLEAN);
        at3.setObrigatorioSIECM(true);
        at3.setSicpfCampoEnum(sicpfCampoEnumMae ? SICPFCampoEnum.NOME_MAE : null);
        at3.setEstrategiaPartilhaEnum(EstrategiaPartilhaEnum.RECEITA_MAE);
        at3.setModoPartilhaEnum(ModoPartilhaEnum.S);
        atributosExtracao.add(at3);

        AtributoExtracao at4 = new AtributoExtracao();
        at4.setNomeAtributoRetorno("nome_retorno2");
        at4.setNomeAtributoSIECM("nome_ged2");
        at4.setTipoAtributoSiecmEnum(TipoAtributoEnum.DECIMAL);
        at4.setObrigatorioSIECM(true);
        at4.setSicpfCampoEnum(sicpfCampoEnumMae ? SICPFCampoEnum.NOME_MAE : null);
        at4.setEstrategiaPartilhaEnum(EstrategiaPartilhaEnum.RECEITA_MAE);
        at4.setModoPartilhaEnum(ModoPartilhaEnum.S);
        atributosExtracao.add(at4);

        AtributoExtracao at5 = new AtributoExtracao();
        at5.setNomeAtributoRetorno("nome_retorno3");
        at5.setNomeAtributoSIECM("nome_ged3");
        at5.setTipoAtributoSiecmEnum(TipoAtributoEnum.LONG);
        at5.setObrigatorioSIECM(true);
        at5.setSicpfCampoEnum(sicpfCampoEnumMae ? SICPFCampoEnum.NOME_MAE : null);
        at5.setEstrategiaPartilhaEnum(EstrategiaPartilhaEnum.RECEITA_MAE);
        at5.setModoPartilhaEnum(ModoPartilhaEnum.S);
        atributosExtracao.add(at5);

        AtributoExtracao at6 = new AtributoExtracao();
        at6.setNomeAtributoRetorno("nome_retorno4");
        at6.setNomeAtributoSIECM("nome_ged4");
        at6.setTipoAtributoSiecmEnum(TipoAtributoEnum.DATE);
        at6.setObrigatorioSIECM(true);
        at6.setSicpfCampoEnum(sicpfCampoEnumMae ? SICPFCampoEnum.NOME_MAE : null);
        at6.setEstrategiaPartilhaEnum(EstrategiaPartilhaEnum.RECEITA_MAE);
        atributosExtracao.add(at6);

        AtributoExtracao at7 = new AtributoExtracao();
        AtributoExtracao atributoPartilha1 = new AtributoExtracao();
        atributoPartilha1.setObrigatorioSIECM(true);
        atributoPartilha1.setNomeAtributoSIECM("nome");
        atributoPartilha1.setNomeAtributoDocumento("mae");
        atributoPartilha1.setTipoAtributoSiecmEnum(TipoAtributoEnum.DATE);
        at7.setAtributoPartilha(atributoPartilha1);
        at7.setModoPartilhaEnum(ModoPartilhaEnum.S);
        at7.setSicpfCampoEnum(sicpfCampoEnumMae ? SICPFCampoEnum.NOME_MAE : null);
        at7.setEstrategiaPartilhaEnum(EstrategiaPartilhaEnum.RECEITA_MAE);
        at7.setObrigatorioSIECM(true);
        at7.setAtributoPartilha(atributoPartilha1);
        at7.setNomeAtributoRetorno("mae");
        at7.setTipoAtributoSiecmEnum(tipoAtributoEnumData ? TipoAtributoEnum.DATE : TipoAtributoEnum.STRING);
        at7.setNomeAtributoDocumento("mae");
        at7.setNomeAtributoSIECM("mae");
        at7.setSicpfCampoEnum(SICPFCampoEnum.NOME_MAE);
        atributosExtracao.add(at7);

        AtributoExtracao at8 = new AtributoExtracao();
        AtributoExtracao atributoPartilha2 = new AtributoExtracao();
        atributoPartilha2.setObrigatorioSIECM(true);
        atributoPartilha2.setNomeAtributoSIECM("mae");
        atributoPartilha2.setNomeAtributoDocumento("pai");
        atributoPartilha2.setTipoAtributoSiecmEnum(TipoAtributoEnum.STRING);
        at8.setAtributoPartilha(atributoPartilha2);
        at8.setObrigatorioSIECM(true);
        at8.setNomeAtributoRetorno("mae");
        at8.setModoPartilhaEnum(ModoPartilhaEnum.V);
        at8.setTipoAtributoSiecmEnum(tipoAtributoEnumData ? TipoAtributoEnum.DATE : TipoAtributoEnum.STRING);
        at8.setNomeAtributoDocumento("mae");
        at8.setNomeAtributoSIECM("mae");
        at8.setSicpfCampoEnum(sicpfCampoEnumMae ? SICPFCampoEnum.NOME_MAE : null);
        at8.setEstrategiaPartilhaEnum(EstrategiaPartilhaEnum.RECEITA_MAE);
        atributosExtracao.add(at8);

        AtributoExtracao at9 = new AtributoExtracao();
        AtributoExtracao atributoPartilha3 = new AtributoExtracao();
        atributoPartilha3.setObrigatorioSIECM(true);
        atributoPartilha3.setNomeAtributoSIECM("nome");
        atributoPartilha3.setNomeAtributoDocumento("campo");
        atributoPartilha3.setTipoAtributoSiecmEnum(TipoAtributoEnum.STRING);
        at9.setAtributoPartilha(atributoPartilha3);
        at9.setObrigatorioSIECM(true);
        at9.setNomeAtributoRetorno("nome");
        at9.setTipoAtributoSiecmEnum(tipoAtributoEnumData ? TipoAtributoEnum.DATE : TipoAtributoEnum.STRING);
        at9.setNomeAtributoDocumento("nome");
        at9.setNomeAtributoSIECM("nome");
        at9.setSicpfCampoEnum(sicpfCampoEnumMae ? SICPFCampoEnum.NOME_MAE : null);
        at9.setEstrategiaPartilhaEnum(EstrategiaPartilhaEnum.RECEITA_MAE);
        at9.setModoPartilhaEnum(ModoPartilhaEnum.S);
        atributosExtracao.add(at9);
        
        AtributoExtracao at10 = new AtributoExtracao();
        at10.setObrigatorioSIECM(false);
        at10.setNomeAtributoRetorno("data_validade");
        at10.setNomeAtributoDocumento("data_validade");
        at10.setSicpfCampoEnum(sicpfCampoEnumMae ? SICPFCampoEnum.NOME_MAE : null);
        at10.setUtilizadoCalculoValidade(true);
        atributosExtracao.add(at10);

        return atributosExtracao;
    }

    /**
     * <p>
     * Método responsável inicializar os requisitos para testar o método:
     * extraiDadosDocumentoDossieDigital
     * </p>
     * .
     *
     * @author manoel.albino
     *
     */
    private void inicializaMockMetodoExtrairDadosDocumentosDossieDigital() {
        DossieCliente dossiecliente = new DossieClientePF();

        Mockito.when(this.dossieClienteServico.getByCpfCnpj(this.cpfCnpj, this.tipoPessoaEnum, false, false, false))
                .thenReturn(dossiecliente);

        Mockito.when(this.tipoDocumentoServico.getByNome(Mockito.anyString())).thenReturn(this.tipoDoc);

        Mockito.when(documentoServico.prototype(Mockito.any(Canal.class), Mockito.anyBoolean(),
                Mockito.any(TipoDocumento.class), Mockito.eq(TemporalidadeDocumentoEnum.TEMPORARIO_OCR),
                Mockito.eq(OrigemDocumentoEnum.S), Mockito.any(FormatoConteudoEnum.class), Mockito.anyList(),
                Mockito.anyString())).thenReturn(new Documento());

        ECMRespostaExtracaoDadosDTO ecmRespostaExtracaoDadosDTO = new ECMRespostaExtracaoDadosDTO();

        ECMDadosImagemDTO ecmDadosImagemDTO = new ECMDadosImagemDTO();
        ECMDadoDTO dadoDTO = new ECMDadoDTO();
        dadoDTO.setNome("nome");
        dadoDTO.setValor("Maria");
        ecmDadosImagemDTO.addECMDadoDTO(dadoDTO);
        ecmRespostaExtracaoDadosDTO.setEcmDadosImagemDTO(ecmDadosImagemDTO);

        ecmRespostaExtracaoDadosDTO.setEcmDadosImagemDTO(ecmDadosImagemDTO);

        ecmRespostaExtracaoDadosDTO.setCodigoRetorno(0);

        Mockito.when(this.gedService.extractInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString())).thenReturn(ecmRespostaExtracaoDadosDTO);
    }

    /**
     * <p>
     * Método responsável por testar o método
     * atualizaDadosDocumentoDossieDigital.. utilizando o SicpfCampoEnum = MAE
     * para tratar DossieManutencaoException
     * </p>
     * .
     *
     * @author manoel.albino
     *
     */
    @Test
    public void atualizaDadosDocumentosDossieDigitalTestCatchDossieManutencaoExceptionSicpfCampoEnumMae()
            throws Exception {

        Map<String, String> mapaDados = this.inicializaMockAtualizaDadosDocumetoDossieDigital(true, false, cpfCnpj);

        try {
            mockCanalValido();
            servico.atualizaDadosDocumentoDossieDigital(dossieCliente, idDocumento, mapaDados);

        } catch (DossieManutencaoException e) {
            Assert.assertTrue(Optional.of((DossieManutencaoException) e).map(DossieManutencaoException::getFalhaSIMTR)
                    .isPresent());
        }

    }

    @Test(expected = SimtrRequisicaoException.class)
    public void atualizaDadosDocumentosDossieDigitalTestDossieManutencaoExceptionDocumentoNull() throws Exception {

        Map<String, String> mapaDados = this.inicializaMockAtualizaDadosDocumetoDossieDigital(false, false, cpfCnpj);

        Mockito.when(documentoServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(null);

        servico.atualizaDadosDocumentoDossieDigital(dossieCliente, idDocumento, mapaDados);

    }

    /**
     * <p>
     * Método responsável por testar o método
     * atualizaDadosDocumentoDossieDigital..
     * </p>
     *
     * @author manoel.albino
     *
     */
    @Test
    public void atualizaDadosDocumentosDossieDigitalTest() throws Exception {

        Map<String, String> mapaDados = this.inicializaMockAtualizaDadosDocumetoDossieDigital(true, false, cpfCnpj);

        servico.atualizaDadosDocumentoDossieDigital(dossieCliente, idDocumento, mapaDados);
    }

    @Test
    public void atualizaDadosDocumentosDossieDigitalTestPartilhaSucess() throws Exception {

        Map<String, String> mapaDados = this.inicializaMockAtualizaDadosDocumetoDossieDigital(false, false, cpfCnpj);

        try {
            servico.atualizaDadosDocumentoDossieDigital(dossieCliente, idDocumento, mapaDados);
            Assert.assertTrue(true);

        } catch (DossieManutencaoException e) {
            Assert.assertTrue(false);
        }

    }

    /**
     * <p>
     * Método responsável inicializar os requisitos para testar o método:
     * atualizaDadosDocumetoDossieDigital
     * </p>
     *
     * @param sicpfCampoEnumMae
     * @param tipoAtributoEnumData
     *
     * @author manoel.albino
     *
     */
    private Map<String, String> inicializaMockAtualizaDadosDocumetoDossieDigital(boolean sicpfCampoEnumMae, boolean tipoAtributoEnumData, Long cpfCnpj)
            throws Exception {
        Set<AtributoExtracao> atributosExtracao = new HashSet<>();
        tipoDoc.setAtributosExtracao(carregarAtributosExtracao(atributosExtracao, sicpfCampoEnumMae, tipoAtributoEnumData));

        Documento documento = new Documento();
        Set<DossieCliente> dossiesCliente = new HashSet<>();
        dossieCliente = new DossieClientePF();
        dossieCliente.setCpfCnpj(cpfCnpj);
        dossieCliente.setId(id);
        dossiesCliente.add(dossieCliente);
        documento.setDossiesCliente(dossiesCliente);
        documento.setTipoDocumento(tipoDoc);
        Set<AtributoDocumento> atributosDocumento = new HashSet<>();
        AtributoDocumento atributoDocumento = new AtributoDocumento();
        atributoDocumento.setDescricao("nome");
        atributosDocumento.add(atributoDocumento);
        documento.setAtributosDocumento(atributosDocumento);

        Map<String, String> mapaDados = new HashMap<>();
        mapaDados.put("nome", "Joaquina");

        RetornoPessoasFisicasDTO retornoPessoasFisicasDTO = new RetornoPessoasFisicasDTO();
        retornoPessoasFisicasDTO.setNomeMae("Joaquina");

        mapaDadosAtualizacao.put("nome", "Jose");
        mapaDadosAtualizacao.put("mae", "Joaquina");

        Mockito.when(tipoDocumentoServico.getByNome(Mockito.anyString())).thenReturn(tipoDoc);
        Mockito.when(documentoServico.getByGedId(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(documento);
        Mockito.when(documentoServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(documento);
        Mockito.when(sicpfServico.consultaCadastroPF(Mockito.anyLong())).thenReturn(retornoPessoasFisicasDTO);
        return mapaDados;
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void atualizaDadosDocumentosDossieDigitalTestFalhaCliente() throws Exception {

        Set<AtributoExtracao> atributosExtracao = new HashSet<>();
        tipoDoc.setAtributosExtracao(carregarAtributosExtracao(atributosExtracao, Boolean.FALSE, Boolean.FALSE));

        Documento documento = new Documento();
        Set<DossieCliente> dossiesCliente = new HashSet<>();
        DossieCliente dc = new DossieClientePF();
        dc.setCpfCnpj(3213213211L);
        dossiesCliente.add(dc);
        documento.setDossiesCliente(dossiesCliente);
        documento.setTipoDocumento(tipoDoc);
        Set<AtributoDocumento> atributosDocumento = new HashSet<>();
        AtributoDocumento atributoDocumento = new AtributoDocumento();
        atributoDocumento.setDescricao("mae");
        atributosDocumento.add(atributoDocumento);
        documento.setAtributosDocumento(atributosDocumento);

        Map<String, String> mapaDados = new HashMap<>();
        mapaDados.put("mae", "Joaquina");

        SicpfResponseDTO sicpfResponseDTO = new SicpfResponseDTO();
        SicpfResultadoCpf sicpfResultadoCpf = new SicpfResultadoCpf();
        sicpfResultadoCpf.setNomeMae("Joaquina");
        sicpfResponseDTO.setSicpfResultadoCpf(sicpfResultadoCpf);

        Mockito.when(tipoDocumentoServico.getByNome(Mockito.anyString())).thenReturn(tipoDoc);
        Mockito.when(documentoServico.getByGedId(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyBoolean(),
                Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(documento);
        /* Simulação do erro no SICPF. Método retorna nulo */
        Mockito.when(sicpfServico.consultaCadastroPF(Mockito.anyLong())).thenReturn(null);

        servico.atualizaDadosDocumentoDossieDigital(dossieCliente, idDocumento, mapaDados);

    }

    @Test(expected = SimtrRequisicaoException.class)
    public void atualizaDadosDocumentosDossieDigitalTestFalhaAtributoExtracao() throws Exception {

        Set<AtributoExtracao> atributosExtracao = new HashSet<>();
        tipoDoc.setAtributosExtracao(carregarAtributosExtracao(atributosExtracao, Boolean.FALSE, Boolean.FALSE));

        Documento documento = new Documento();
        Set<DossieCliente> dossiesCliente = new HashSet<>();
        DossieCliente dc = new DossieClientePF();
        dc.setCpfCnpj(cpfCnpj);
        dossiesCliente.add(dc);
        documento.setDossiesCliente(dossiesCliente);
        documento.setTipoDocumento(tipoDoc);
        Set<AtributoDocumento> atributosDocumento = new HashSet<>();
        AtributoDocumento atributoDocumento = new AtributoDocumento();
        atributoDocumento.setDescricao("mae");
        atributosDocumento.add(atributoDocumento);
        documento.setAtributosDocumento(atributosDocumento);

        Map<String, String> mapaDados = new HashMap<>();
        mapaDados.put("pai", "João");

        SicpfResponseDTO sicpfResponseDTO = new SicpfResponseDTO();
        SicpfResultadoCpf sicpfResultadoCpf = new SicpfResultadoCpf();
        sicpfResultadoCpf.setNomeMae("Joaquina");
        sicpfResponseDTO.setSicpfResultadoCpf(sicpfResultadoCpf);

        Mockito.when(tipoDocumentoServico.getByNome(Mockito.anyString())).thenReturn(tipoDoc);
        Mockito.when(documentoServico.getByGedId(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyBoolean(),
                Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(documento);
        /* Simulação do erro no SICPF. Método retorna nulo */
        Mockito.when(sicpfServico.consultaCadastroPF(Mockito.anyLong())).thenReturn(null);

        servico.atualizaDadosDocumentoDossieDigital(dossieCliente, idDocumento, mapaDados);

    }

    /**
     * Valida a informação de Partilha
     */
    @Test(expected = SimtrConfiguracaoException.class)
    public void atualizaDadosDocumentosDossieDigitalTestFalhaAtributoPartilha() {

        Set<AtributoExtracao> atributosExtracao = new HashSet<>();
        tipoDoc.setAtributosExtracao(carregarAtributosExtracao(atributosExtracao, Boolean.FALSE, Boolean.FALSE));

        Documento documento = new Documento();
        Set<DossieCliente> dossiesCliente = new HashSet<>();
        dossiesCliente.add(dossieCliente);
        documento.setDossiesCliente(dossiesCliente);
        documento.setTipoDocumento(tipoDoc);
        Set<AtributoDocumento> atributosDocumento = new HashSet<>();
        AtributoDocumento atributoDocumento = new AtributoDocumento();
        atributoDocumento.setDescricao("mae");
        atributosDocumento.add(atributoDocumento);
        documento.setAtributosDocumento(atributosDocumento);

        Map<String, String> mapaDados = new HashMap<>();
        mapaDados.put("mae", "Joaquina");

        Mockito.when(tipoDocumentoServico.getByNome(Mockito.anyString())).thenReturn(tipoDoc);
        Mockito.when(documentoServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(documento);

        servico.atualizaDadosDocumentoDossieDigital(dossieCliente, idDocumento, mapaDados);
    }

    @Test
    public void atualizaDadosDocumentosDossieDigitalTestAtributoPartilha() throws Exception {

        Set<AtributoExtracao> atributosExtracao = new HashSet<>();
        tipoDoc.setAtributosExtracao(carregarAtributosExtracao(atributosExtracao, Boolean.FALSE, Boolean.FALSE));

        Documento documento = new Documento();
        Set<DossieCliente> dossiesCliente = new HashSet<>();
        dossiesCliente.add(dossieCliente);
        documento.setDossiesCliente(dossiesCliente);
        documento.setTipoDocumento(tipoDoc);
        Set<AtributoDocumento> atributosDocumento = new HashSet<>();
        AtributoDocumento atributoDocumento = new AtributoDocumento();
        atributoDocumento.setDescricao("mae");
        atributosDocumento.add(atributoDocumento);
        documento.setAtributosDocumento(atributosDocumento);

        Map<String, String> mapaDados = new HashMap<>();
        mapaDados.put("mae", "Joaquina");

        RetornoPessoasFisicasDTO retornoPessoasFisicasDTO = new RetornoPessoasFisicasDTO();
        retornoPessoasFisicasDTO.setNomeMae("Joaquina");

        Mockito.when(tipoDocumentoServico.getByNome(Mockito.anyString())).thenReturn(tipoDoc);
        Mockito.when(documentoServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(documento);

        Mockito.when(sicpfServico.consultaCadastroPF(Mockito.anyLong())).thenReturn(retornoPessoasFisicasDTO);
        servico.atualizaDadosDocumentoDossieDigital(dossieCliente, idDocumento, mapaDados);

    }

    @Test
    public void atualizaDadosDocumentosDossieDigitalTestAtributoPartilhaFalha() {

        Set<AtributoExtracao> atributosExtracao = new HashSet<>();
        tipoDoc.setAtributosExtracao(carregarAtributosExtracao(atributosExtracao, Boolean.FALSE, Boolean.FALSE));

        AtributoExtracao at9 = new AtributoExtracao();
        AtributoExtracao atributoPartilha3 = new AtributoExtracao();
        at9.setAtributoPartilha(atributoPartilha3);
        at9.setSicpfCampoEnum(SICPFCampoEnum.NOME_MAE);
        at9.setNomeAtributoRetorno("filho");
        at9.setTipoAtributoSiecmEnum(TipoAtributoEnum.STRING);
        at9.setNomeAtributoDocumento("filho");
        at9.setNomeAtributoSIECM("filho");
        tipoDoc.getAtributosExtracao().add(at9);

        Documento documento = new Documento();
        Set<DossieCliente> dossiesCliente = new HashSet<>();
        DossieCliente dc = new DossieClientePF();
        dc.setCpfCnpj(cpfCnpj);
        dossiesCliente.add(dc);
        documento.setDossiesCliente(dossiesCliente);
        documento.setTipoDocumento(tipoDoc);
        Set<AtributoDocumento> atributosDocumento = new HashSet<>();
        AtributoDocumento atributoDocumento = new AtributoDocumento();
        atributoDocumento.setDescricao("mae");
        atributosDocumento.add(atributoDocumento);
        documento.setAtributosDocumento(atributosDocumento);

        Map<String, String> mapaDados = new HashMap<>();
        mapaDados.put("mae", "Joaquina");

        RetornoPessoasFisicasDTO retornoPessoasFisicasDTO = new RetornoPessoasFisicasDTO();
        retornoPessoasFisicasDTO.setNomeMae("Joaquina");

        mapaDadosAtualizacao.put("nome", "Jose");
        mapaDadosAtualizacao.put("mae", "Joaquina");

        //
        Mockito.when(tipoDocumentoServico.getByNome(Mockito.anyString())).thenReturn(tipoDoc);
        Mockito.when(documentoServico.getByGedId(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyBoolean(),
                Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(documento);

        try {
            Mockito.when(sicpfServico.consultaCadastroPF(Mockito.anyLong())).thenReturn(retornoPessoasFisicasDTO);
            servico.atualizaDadosDocumentoDossieDigital(dossieCliente, idDocumento, mapaDados);
        } catch (DossieManutencaoException e) {
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

    }

    @Test(expected = SimtrRequisicaoException.class)
    public void atualizaDadosDocumentosDossieDigitalTestFalhaObterGedId() {
        Mockito.when(documentoServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(null);
        servico.atualizaDadosDocumentoDossieDigital(dossieCliente, idDocumento, mapaDadosAtualizacao);
    }

    @Test
    public void validacaoDossieDigitalTestSucess() throws Exception {

        Documento documento = new Documento();
        Set<DossieCliente> dossiesCliente = new HashSet<>();
        DossieCliente dc = new DossieClientePF();
        dc.setCpfCnpj(cpfCnpj);
        dossiesCliente.add(dc);
        documento.setDossiesCliente(dossiesCliente);
        documento.setTipoDocumento(tipoDoc);
        Set<AtributoDocumento> atributosDocumento = new HashSet<>();
        AtributoDocumento atributoDocumento = new AtributoDocumento();
        atributoDocumento.setDescricao("mae");
        atributosDocumento.add(atributoDocumento);
        documento.setAtributosDocumento(atributosDocumento);
        
        Mockito.doNothing().when(sicpfServico).validaDocumentoSICPF(Mockito.any(), Mockito.any());
        Mockito.when(documentoServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(documento);

        try {
            servico.validaDocumentoDossieDigital(dossieCliente, idDocumento);
            Assert.assertTrue(true);

        } catch (DossieManutencaoException e) {
            Assert.assertTrue(false);
        }

    }

    @Test(expected = SimtrRequisicaoException.class)
    public void validacaoDossieDigitalTestDocumentoNulo() throws Exception {

        Mockito.doNothing().when(sicpfServico).validaDocumentoSICPF(Mockito.any(), Mockito.any());
        Mockito.when(documentoServico.getByGedId(identificadorDocumento, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE)).thenReturn(null);

        servico.validaDocumentoDossieDigital(dossieCliente, idDocumento);
        Assert.assertTrue(false);
    }

    @Test
    public void atualizaCartaoAssinaturaDossieDigitalTestSucess() {

        final String conteudoCartaoBase64 = "xxxx";
        ECMRespostaExtracaoDadosDTO ecmRespostaExtracaoDadosDTO = new ECMRespostaExtracaoDadosDTO();

        ECMDadosImagemDTO ecmDadosImagemDTO = new ECMDadosImagemDTO();
        ECMDadoDTO dadoDTO = new ECMDadoDTO();
        dadoDTO.setNome("nome");
        dadoDTO.setValor("Maria");
        ecmDadosImagemDTO.addECMDadoDTO(dadoDTO);
        ecmRespostaExtracaoDadosDTO.setEcmDadosImagemDTO(ecmDadosImagemDTO);

        ecmRespostaExtracaoDadosDTO.setCodigoRetorno(0);

        DossieCliente dossiecliente = new DossieClientePF();
        Mockito.when(dossieClienteServico.getByCpfCnpj(cpfCnpj, tipoPessoaEnum, false, false, false))
                .thenReturn(dossiecliente);
        Mockito.when(gedService.extractInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString())).thenReturn(ecmRespostaExtracaoDadosDTO);

        try {
            servico.atualizaCartaoAssinatura(id, formatoConteudoEnum, conteudoCartaoBase64);
            Assert.assertTrue(true);

        } catch (Exception e) {
            Assert.assertTrue(true);
        }

    }

    /**
     * Lança uma exeção caso a busca pelo Canal Retorne Null
     */
    @Test(expected = SimtrRequisicaoException.class)
    public void atualizaCartaoAssinaturaDossieDigitalTesteErrorObterCanal() {

        Mockito.when(canalServico.getByClienteSSO()).thenReturn(null);

        servico.atualizaCartaoAssinatura(id, formatoConteudoEnum, "xxxx");

    }

    /**
     * Lança uma Exceção caso não encontre o dossiê do cliente
     */
    @Test(expected = SimtrRequisicaoException.class)
    public void atualizaCartaoAssinaturaDossieDigitalTestErroDossieNaoEncontrado() {

        final String conteudoCartaoBase64 = "xxxx";

        Mockito.when(dossieClienteServico.getById(id, false, false, false)).thenReturn(null);
        servico.atualizaCartaoAssinatura(id, formatoConteudoEnum, conteudoCartaoBase64);
        Assert.assertTrue(false);
    }

    /**
     * Lança uma Exceção caso não encontre o Tipo de Documento do Cartão
     * Assinatura
     */
    @Test(expected = SimtrConfiguracaoException.class)
    public void atualizaCartaoAssinaturaDossieDigitalTestErroTipoDocCartaoNaoEncontrado() {

        final String conteudoCartaoBase64 = "xxxx";
        final DossieCliente dc = new DossieClientePF();
        final TipoDocumento tipoDocumentoCartaoAssinatura = null;

        Mockito.when(dossieClienteServico.getById(id, false, false, false)).thenReturn(dc);
        Mockito.when(tipoDocumentoServico.getByTipologia(ConstantesUtil.TIPOLOGIA_CARTAO_ASSINATURA))
                .thenReturn(tipoDocumentoCartaoAssinatura);

        servico.atualizaCartaoAssinatura(id, formatoConteudoEnum, conteudoCartaoBase64);
        Assert.assertTrue(false);
    }

    /**
     * Lança Exceção quando houver erro ao Criar o documento para Pf ou PJ
     */
    @Test(expected = SiecmException.class)
    public void atualizaCartaoAssinaturaDossieDigitalTestErroCreateDocPForPJ() throws Exception {

        final String conteudoCartaoBase64 = "aaaaa";
        final DossieCliente dc = new DossieClientePF();
        final TipoDocumento tipoDocumentoCartaoAssinatura = new TipoDocumento();
        final Documento cartaoAsssinatura = new Documento();
        cartaoAsssinatura.setTipoDocumento(tipoDocumentoCartaoAssinatura);
        final ECMRespostaExtracaoDadosDTO ecmRespostaExtracaoDadosDTO = new ECMRespostaExtracaoDadosDTO();
        ecmRespostaExtracaoDadosDTO.setCodigoRetorno(0);
        final ECMDadosImagemDTO ecmDadosImagemDTO = new ECMDadosImagemDTO();
        ecmDadosImagemDTO.addECMDadoDTO(new ECMDadoDTO());
        ecmRespostaExtracaoDadosDTO.setEcmDadosImagemDTO(ecmDadosImagemDTO);

        Mockito.when(dossieClienteServico.getById(id, false, false, false)).thenReturn(dc);
        Mockito.when(tipoDocumentoServico.getByTipologia(Mockito.anyString())).thenReturn(tipoDocumentoCartaoAssinatura);
        Mockito.when(documentoServico.prototype(Mockito.any(Canal.class), Mockito.anyBoolean(),
                Mockito.any(TipoDocumento.class), Mockito.eq(TemporalidadeDocumentoEnum.VALIDO),
                Mockito.eq(OrigemDocumentoEnum.O), Mockito.any(FormatoConteudoEnum.class), Mockito.anyList(),
                Mockito.anyString())).thenReturn(cartaoAsssinatura);
        Mockito.when(gedService.extractInfo(null, "TIF", conteudoCartaoBase64, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL))
                .thenReturn(ecmRespostaExtracaoDadosDTO);
        Mockito.when(gedService.createDocumentForPFOrPJ(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(null);
        Mockito.when(siecmServico.executaExtracaoDadosDocumento(tipoDocumentoCartaoAssinatura, formatoConteudoEnum, conteudoCartaoBase64)).thenReturn(new HashMap<>());
        Mockito.doThrow(SiecmException.class).when(siecmServico).armazenaDocumentoPessoalSIECM(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.any(Documento.class), Mockito.any(TemporalidadeDocumentoEnum.class), Mockito.anyString(), Mockito.anyString());

        servico.atualizaCartaoAssinatura(id, formatoConteudoEnum, conteudoCartaoBase64);
        Assert.assertTrue(false);
    }

    /**
     * Lança exceção se o documento retorna nulo
     */
    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void atualizaDadosCadastraisDossieDigitalTestErroClienteNaoLocalizado() {

        final List<String> listaIdentificadorDocumento = new ArrayList<>();
        final String identificadorDoc = "EH00666";
        final Long cpfCnpj = 1108567169L;
        listaIdentificadorDocumento.add(identificadorDoc);
        final Documento doc = new Documento();
        Set<DossieCliente> dossiesCliente = new HashSet<>();
        DossieCliente dcCli = new DossieClientePF();
        dcCli.setCpfCnpj(1255191171L);
        dossiesCliente.add(dcCli);
        doc.setDossiesCliente(dossiesCliente);

        Mockito.when(dossieClienteServico.getById(id, false, true, false)).thenReturn(null);
        Mockito.doThrow(SimtrRecursoDesconhecidoException.class).when(dossieClienteServico).validaRecursoLocalizado(Mockito.eq(null), Mockito.anyString());
        Mockito.when(documentoServico.getByGedId(identificadorDoc, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE,
                Boolean.TRUE, Boolean.FALSE)).thenReturn(doc);

        servico.atualizaCadastroCaixaDossieCliente(id, null, listaIdentificadorDocumento);
        Assert.assertTrue(false);
    }

    private AtributoExtracao criarAtributoExtracao(Integer id, TipoAtributoEnum tipo) {
        AtributoExtracao atributo = new AtributoExtracao();
        atributo.setId(id);
        atributo.setTipoAtributoGeralEnum(tipo);
        atributo.setNomeAtributoDocumento("tipoDoc" + id);
        atributo.setObrigatorioSIECM(Boolean.TRUE);
        atributo.setNomeAtributoSIECM("NomeGED");
        atributo.setTipoAtributoSiecmEnum(tipo);
        return atributo;
    }

    private void mockTipoDocumentoAtributoInvalido() {
        tipoDoc.setNomeClasseSIECM("RG");

        Set<AtributoExtracao> atributos = new HashSet<>();
        AtributoExtracao atributo = new AtributoExtracao();
        atributo.setId(2);
        atributo.setNomeAtributoDocumento("tipoDoc20");
        atributo.setObrigatorioSIECM(Boolean.TRUE);
        atributo.setNomeAtributoSIECM("NomeGED20");
        atributo.setTipoAtributoSiecmEnum(TipoAtributoEnum.DATE);
        atributos.add(atributo);

        tipoDoc.setAtributosExtracao(atributos);

        Mockito.when(tipoDocumentoServico.getByNome(Mockito.anyString())).thenReturn(tipoDoc);
    }

    private void mockTipoDocumentoInvalido() {
        Mockito.when(tipoDocumentoServico.getByNome(Mockito.anyString())).thenReturn(null);
    }

    private void mockCalendarUtil() {
        try {
            Mockito.when(calendarUtil.toCalendar(Mockito.anyString(), Mockito.anyBoolean()))
                    .thenReturn(Calendar.getInstance());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void mockCalendarUtilException() {
        try {
            Mockito.doThrow(ParseException.class).when(calendarUtil).toCalendar(Mockito.anyString(),
                    Mockito.anyBoolean());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void mockGerarRelatorioPDFJsonDataSource() {
        byte[] b = {Byte.parseByte("50")};
        Mockito.when(relatorioServico.gerarRelatorioPDFJsonDataSource(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyMap())).thenReturn(b);
    }

    private void mockGerarRelatorioPDFJsonDataSourceException() {
        Mockito.doThrow(Exception.class).when(relatorioServico).gerarRelatorioPDFJsonDataSource(Mockito.anyString(),
                Mockito.anyString(), Mockito.anyMap());
    }

    private void mockCreateIntoFolder() {
        RespostaGravaDocumentoDTO resposta = new RespostaGravaDocumentoDTO();
        resposta.setCodigoRetorno(0);
        Mockito.when(gedService.createDocumentIntoFolder(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyList(), Mockito.any(ClasseBaseECM.class), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString())).thenReturn(resposta);
    }

    private void mockSicpf() {
        RetornoPessoasFisicasDTO retornoPessoasFisicasDTO = new RetornoPessoasFisicasDTO();
        retornoPessoasFisicasDTO.setNomeMae("Joaquina");

        try {
            Mockito.when(sicpfServico.consultaCadastroPF(Mockito.anyLong())).thenReturn(retornoPessoasFisicasDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mockListaAtributoDTO() {
        AtributoDocumentoDTO atributo = new AtributoDocumentoDTO();
        atributo.setChave("tipoDoc1");
        atributo.setValor("2019-10-10");
        atributosDocumento.add(atributo);

        atributo = new AtributoDocumentoDTO();
        atributo.setChave("tipoDoc2");
        atributo.setValor("String");
        atributosDocumento.add(atributo);

        atributo = new AtributoDocumentoDTO();
        atributo.setChave("tipoDoc3");
        atributo.setValor("2019-10-10");
        atributosDocumento.add(atributo);

        atributo = new AtributoDocumentoDTO();
        atributo.setChave("tipoDoc4");
        atributo.setValor("123");
        atributosDocumento.add(atributo);

        atributo = new AtributoDocumentoDTO();
        atributo.setChave("tipoDoc5");
        atributo.setValor("123456789");
        atributosDocumento.add(atributo);

        atributo = new AtributoDocumentoDTO();
        atributo.setChave("tipoDoc6");
        atributo.setValor("true");
        atributosDocumento.add(atributo);

    }

    private void mockDossieGetByCPFCNPJ() {
        DossieCliente dc = new DossieClientePF();
        dc.setCpfCnpj(72772450163L);
        Mockito.when(dossieClienteServico.getByCpfCnpj(Mockito.anyLong(), Mockito.any(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(dc);        
        Mockito.when(dossieClienteServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(dc);
    }

    private void mockDossieGetByCPFCNPJNull() {
        Mockito.when(dossieClienteServico.getByCpfCnpj(Mockito.anyLong(), Mockito.any(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(null);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void geraPDFMinutaDocumentoTestCanalInvalido() {
        mockCanalInvalido();
        mockListaAtributoDTO();
        servicoOld.geraPDFMinutaDocumento("RG", atributosDocumento);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void geraPDFMinutaDocumentoTestTipoDocumentoInvalido() {
        mockCanalValido();
        mockListaAtributoDTO();
        servicoOld.geraPDFMinutaDocumento("RG", atributosDocumento);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void geraPDFMinutaDocumentoTestNomeMinutaInvalida() {
        mockCanalValido();
        mockTipoDocumentoValido();
        mockListaAtributoDTO();
        servicoOld.geraPDFMinutaDocumento("RG", atributosDocumento);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void geraPDFMinutaDocumentoTestMinutaInvalida() {
        mockCanalValido();
        mockTipoDocumentoValidoMinutaInvalida();
        mockListaAtributoDTO();
        servicoOld.geraPDFMinutaDocumento("RG", atributosDocumento);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void geraPDFMinutaDocumentoTestCalendarException() {
        mockCanalValido();
        mockTipoDocumentoValidoMinuta();
        mockListaAtributoDTO();
        mockCalendarUtilException();
        servicoOld.geraPDFMinutaDocumento("RG", atributosDocumento);
    }

    @Test
    public void geraPDFMinutaDocumentoTest() {
        mockCanalValido();
        mockTipoDocumentoValidoMinuta();
        mockListaAtributoDTO();
        mockCalendarUtil();
        servicoOld.geraPDFMinutaDocumento("RG", atributosDocumento);
    }

    @Test(expected = SimtrConfiguracaoException.class)
    public void geraPDFMinutaDocumentoTestPDFException() {
        mockCanalValido();
        mockTipoDocumentoValidoMinuta();
        mockListaAtributoDTO();
        mockCalendarUtil();
        mockGerarRelatorioPDFJsonDataSourceException();
        servicoOld.geraPDFMinutaDocumento("RG", atributosDocumento);
    }

    @Test(expected = SimtrConfiguracaoException.class)
    public void geraCartaoAssinaturaTestFalha() {
        mockSicpf();
        mockGerarRelatorioPDFJsonDataSourceException();
        Mockito.when(dossieClienteServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(dossieCliente);
        servico.geraCartaoAssinatura(id);
    }

    
    @Test(expected = SimtrRequisicaoException.class)
    public void atualizaDadosDeclaradosDossieDigitalTestCpfNull() {
        mockCanalValido();
        mockDossieGetByCPFCNPJNull();
        servico.atualizaDadosDeclaradosDossieDigital(2L, null);
    }

    @Test(expected = SimtrConfiguracaoException.class)
    public void atualizaDadosDeclaradosDossieDigitalTestTipoDocInvalido() {
        mockCanalValido();
        mockDossieGetByCPFCNPJ();
        Mockito.when(this.tipoDocumentoServico.getByTipologia(ConstantesUtil.TIPOLOGIA_DADOS_DECLARADOS_DD)).thenReturn(null);
        servico.atualizaDadosDeclaradosDossieDigital(1L, null);
    }

    private void mockGedServiceDocumentosCliente() {
        ECMDocumentoLocalizadoDTO docLocalizado = new ECMDocumentoLocalizadoDTO();
        docLocalizado.setAtributosDocumento(new ECMAtributosDocumentoDTO());
        docLocalizado.getAtributosDocumento().setId("1");
        List<ECMDocumentoLocalizadoDTO> docs = new ArrayList<>();
        docs.add(docLocalizado);
        Mockito.when(this.gedService.getDocumentosCliente(Mockito.anyLong(), Mockito.anyString())).thenReturn(docs);
    }

    private void mockCanalValido() {
        Mockito.when(canalServico.getByClienteSSO()).thenReturn(new Canal());
    }

    private void mockCanalInvalido() {
        Mockito.when(canalServico.getByClienteSSO()).thenReturn(null);
    }

    private void mockDocumentoVO() {
        documentosOperacaoVO = new ArrayList<>();
        DocumentoVO doc = new DocumentoVO();
        doc.setTipoDocumento("tipoDoc1");
        Map<String, String> atributos = new HashMap<>();
        atributos.put("tipoDoc1", "2019-10-10");
        atributos.put("tipoDoc2", "String");
        atributos.put("tipoDoc3", "2019-10-10");
        atributos.put("tipoDoc4", "123");
        atributos.put("tipoDoc5", "123456789");
        atributos.put("tipoDoc6", "true");
        doc.setAtributos(atributos);
        documentosOperacaoVO.add(doc);
    }

    private void mockDocumentoVONumberFormat() {
        documentosOperacaoVO = new ArrayList<>();
        DocumentoVO doc = new DocumentoVO();
        doc.setTipoDocumento("tipoDoc1");
        Map<String, String> atributos = new HashMap<>();
        atributos.put("tipoDoc1", "2019-140-40");
        atributos.put("tipoDoc2", "String");
        atributos.put("tipoDoc3", "2019-123-12312391");
        atributos.put("tipoDoc4", "2019-10-11");// DEVERIA SER DECIMAL, ERRO DE NUMBER FORMAT
        atributos.put("tipoDoc5", "123456789");
        atributos.put("tipoDoc6", "true");
        doc.setAtributos(atributos);
        documentosOperacaoVO.add(doc);
    }

    private void mockComposicao() {
        List<ComposicaoDocumental> composicoes = new ArrayList<>();
        ComposicaoDocumental composicao = new ComposicaoDocumental();
        composicao.setRegrasDocumentais(new HashSet<>());
        RegraDocumental regra = new RegraDocumental();
        regra.setId(1L);
        FuncaoDocumental funcao = new FuncaoDocumental();
        funcao.setTiposDocumento(new HashSet<>());
        TipoDocumento tipoDoc = new TipoDocumento();
        tipoDoc.setNomeClasseSIECM("NomeClasseGed");
        tipoDoc.setNome("tipoDocInvalido");
        funcao.getTiposDocumento().add(tipoDoc);
        regra.setFuncaoDocumental(funcao);
        composicao.getRegrasDocumentais().add(regra);

        regra = new RegraDocumental();
        regra.setId(2L);
        tipoDoc = new TipoDocumento();
        tipoDoc.setNomeClasseSIECM("NomeClasseGed");
        tipoDoc.setNome("tipoDocInvalido");
        regra.setTipoDocumento(tipoDoc);
        composicao.getRegrasDocumentais().add(regra);

        composicoes.add(composicao);

        Mockito.when(this.composicaoDocumentalServico.getComposicoesByProduto(Mockito.anyInt(), Mockito.anyInt(),
                Mockito.anyBoolean())).thenReturn(composicoes);
    }

    private void mockComposicaoValida() {
        List<ComposicaoDocumental> composicoes = new ArrayList<>();
        ComposicaoDocumental composicao = new ComposicaoDocumental();
        composicao.setRegrasDocumentais(new HashSet<>());
        RegraDocumental regra = new RegraDocumental();
        regra.setId(1L);
        FuncaoDocumental funcao = new FuncaoDocumental();
        funcao.setTiposDocumento(new HashSet<>());
        TipoDocumento tipoDoc = new TipoDocumento();
        tipoDoc.setId(3);
        tipoDoc.setNomeClasseSIECM("NomeClasseGed");
        tipoDoc.setNome("tipoDoc1");
        funcao.getTiposDocumento().add(tipoDoc);
        regra.setFuncaoDocumental(funcao);
        composicao.getRegrasDocumentais().add(regra);

        regra = new RegraDocumental();
        regra.setId(2L);
        tipoDoc = new TipoDocumento();
        tipoDoc.setNomeClasseSIECM("NomeClasseGed");
        tipoDoc.setNome("tipoDoc1");
        tipoDoc.setId(4);
        regra.setTipoDocumento(tipoDoc);
        composicao.getRegrasDocumentais().add(regra);

        composicoes.add(composicao);

        Mockito.when(this.composicaoDocumentalServico.getComposicoesByProduto(Mockito.anyInt(), Mockito.anyInt(),
                Mockito.anyBoolean())).thenReturn(composicoes);
    }

    private void mockAutorizacaoValida() {
        carregarAutorizacao();
        Mockito.when(autorizacaoServico.getByCodigoAutorizacao(Mockito.anyLong(), Mockito.anyBoolean(),
                Mockito.anyBoolean())).thenReturn(autorizacao);
    }

    private void mockAutorizacaoInvalida() {
        Mockito.when(autorizacaoServico.getByCodigoAutorizacao(Mockito.anyLong(), Mockito.anyBoolean(),
                Mockito.anyBoolean())).thenReturn(null);
    }

    private void carregarAutorizacao() {
        autorizacao = new Autorizacao();
        autorizacao.setCodigoNSU(123123123L);
    }

    private void mockTipoDocumentoValido() {
        tipoDoc.setNomeClasseSIECM("RG");
        Set<AtributoExtracao> atributos = new HashSet<>();
        atributos.add(criarAtributoExtracao(1, TipoAtributoEnum.STRING));

        tipoDoc.setAtributosExtracao(atributos);

        Mockito.when(tipoDocumentoServico.getByNome(Mockito.anyString())).thenReturn(tipoDoc);
    }

    private void mockTipoDocumentoByClasseGED() {
        tipoDoc.setNomeClasseSIECM("RG");
        tipoDoc.setNomeArquivoMinuta("ArquivoMinuta");
        Set<AtributoExtracao> atributos = new HashSet<>();
        atributos.add(criarAtributoExtracao(1, TipoAtributoEnum.DATE));
        atributos.add(criarAtributoExtracao(2, TipoAtributoEnum.STRING));
        atributos.add(criarAtributoExtracao(3, TipoAtributoEnum.DATE));
        atributos.add(criarAtributoExtracao(4, TipoAtributoEnum.DECIMAL));
        atributos.add(criarAtributoExtracao(5, TipoAtributoEnum.LONG));
        atributos.add(criarAtributoExtracao(6, TipoAtributoEnum.BOOLEAN));

        tipoDoc.setAtributosExtracao(atributos);
        Mockito.when(tipoDocumentoServico.getByTipologia(Mockito.anyString())).thenReturn(tipoDoc);
        
        Mockito.when(this.tipoDocumentoServico.getByTipologia(ConstantesUtil.TIPOLOGIA_DADOS_DECLARADOS_DD)).thenReturn(tipoDoc);
    }

    private void mockTipoDocumentoValidoMinuta() {
        tipoDoc.setNomeClasseSIECM("RG");
        tipoDoc.setNomeArquivoMinuta("NomeMinuta");
        Set<AtributoExtracao> atributos = new HashSet<>();
        atributos.add(criarAtributoExtracao(1, TipoAtributoEnum.DATE));
        atributos.add(criarAtributoExtracao(2, TipoAtributoEnum.STRING));
        atributos.add(criarAtributoExtracao(3, TipoAtributoEnum.DATE));
        atributos.add(criarAtributoExtracao(4, TipoAtributoEnum.DECIMAL));
        atributos.add(criarAtributoExtracao(5, TipoAtributoEnum.LONG));
        atributos.add(criarAtributoExtracao(6, TipoAtributoEnum.BOOLEAN));

        tipoDoc.setAtributosExtracao(atributos);

        Mockito.when(tipoDocumentoServico.getByNome(Mockito.anyString())).thenReturn(tipoDoc);
    }

    private void mockTipoDocumentoValidoMinutaTipoDocInvalido() {
        tipoDoc.setNomeClasseSIECM("RG");
        tipoDoc.setNomeArquivoMinuta("NomeMinuta");
        Set<AtributoExtracao> atributos = new HashSet<>();
        atributos.add(criarAtributoExtracao(1, null));
        atributos.add(criarAtributoExtracao(2, null));
        atributos.add(criarAtributoExtracao(3, null));
        atributos.add(criarAtributoExtracao(4, null));
        atributos.add(criarAtributoExtracao(5, null));
        atributos.add(criarAtributoExtracao(6, null));

        tipoDoc.setAtributosExtracao(atributos);

        Mockito.when(tipoDocumentoServico.getByNome(Mockito.anyString())).thenReturn(tipoDoc);
    }

    private void mockTipoDocumentoValidoMinutaInvalida() {
        tipoDoc.setNomeClasseSIECM("RG");
        tipoDoc.setNomeArquivoMinuta("NomeMinuta");
        Set<AtributoExtracao> atributos = new HashSet<>();
        atributos.add(criarAtributoExtracao(1, null));

        tipoDoc.setAtributosExtracao(atributos);

        Mockito.when(tipoDocumentoServico.getByNome(Mockito.anyString())).thenReturn(tipoDoc);
    }

    private void mockCreateDocumentForPForPJ() {
        RespostaGravaDocumentoDTO resposta = new RespostaGravaDocumentoDTO();
        resposta.setCodigoRetorno(0);
        List<DocumentoDTO> documentos = new ArrayList<>();
        DocumentoDTO docDTO = new DocumentoDTO();
        AtributosDTO atrDocs = new AtributosDTO();
        atrDocs.setId("2");
        docDTO.setAtributos(atrDocs);
        documentos.add(docDTO);
        resposta.setDocumentos(documentos);
        Mockito.when(gedService.createDocumentForPFOrPJ(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.any(ClasseBaseECM.class),
                Mockito.anyString())).thenReturn(resposta);
    }

    private void mockNovoDocumentoDadosDeclarados() {
        Documento documento = new Documento();

        documento.setCanalCaptura(new Canal());
        documento.setDossieDigital(Boolean.TRUE);
        documento.setTipoDocumento(tipoDoc);
        documento.setDataHoraCaptura(Calendar.getInstance());
        documento.setResponsavel("C000000");
        documento.setSituacaoTemporalidade(TemporalidadeDocumentoEnum.VALIDO);
        documento.setOrigemDocumento(OrigemDocumentoEnum.O);
        documento.setQuantidadeBytes(0L);
        documento.setQuantidadeConteudos(0);

        Mockito.when(this.documentoServico.prototype(Mockito.any(Canal.class), Mockito.anyBoolean(), Mockito.any(TipoDocumento.class), Mockito.any(TemporalidadeDocumentoEnum.class), Mockito.any(OrigemDocumentoEnum.class), Mockito.any(FormatoConteudoEnum.class), Mockito.anyList(), Mockito.anyString())).thenReturn(documento);
        Mockito.when(this.documentoServico.prototype(Mockito.any(Canal.class), Mockito.anyBoolean(), Mockito.any(TipoDocumento.class), Mockito.any(TemporalidadeDocumentoEnum.class), Mockito.any(OrigemDocumentoEnum.class), Mockito.any(FormatoConteudoEnum.class), Mockito.anyMap(), Mockito.anyString())).thenReturn(documento);
    }

}
