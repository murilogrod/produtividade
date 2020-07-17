package br.gov.caixa.simtr.controle.servico;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.controle.excecao.SimtrPermissaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoBloqueadoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrVerificacaoInvalidaException;
import br.gov.caixa.simtr.controle.vo.checklist.ParecerApontamentoVO;
import br.gov.caixa.simtr.controle.vo.checklist.VerificacaoVO;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.Checklist;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.ProcessoFaseDossie;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDossie;
import br.gov.caixa.simtr.modelo.entidade.TipoSituacaoDossie;
import br.gov.caixa.simtr.modelo.entidade.UnidadeTratamento;
import br.gov.caixa.simtr.modelo.entidade.VinculacaoChecklist;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDossieEnum;

/**
 * <p>
 * CanalServicoTest
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
public class TratamentoServicoTest {

    @Mock
    private CanalServico canalServico;

    @Mock
    private ChecklistServico checklistServico;

    @Mock
    private DossieProdutoServico dossieProdutoServico;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private ProcessoServico processoServico;

    @Mock
    private SituacaoDossieServico situacaoDossieServico;

    @Mock
    private SituacaoDocumentoServico situacaoDocumentoServico;

    @InjectMocks
    private TratamentoServico tratamentoServico;

    @InjectMocks
    private ObjectMapper objectMapper;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Logger logger;

    Processo processo;

    TipoSituacaoDossie tipoSituacaoDossie;

    DossieProduto dossieProduto;

    List<VerificacaoVO> verificacoes;

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

    private String getJsonDeArquivo(final String nomeArquivo) {

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(nomeArquivo).getFile());
            byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));

            return new String(encoded, StandardCharsets.UTF_8);

        } catch (IOException e) {
            Assert.assertFalse(Boolean.FALSE);
            return null;
        }

    }

    @Test(expected = SimtrRequisicaoException.class)
    public void capturaDossieTratamentoNull() throws Exception {
        Assert.assertNull(tratamentoServico.capturaDossieTratamento(1));
    }

    @Test
    public void capturaDossieTratamentoDossieProdutoNull() throws Exception {
        Processo process = buscarProcesso();
        mockaProcessogetById(process);
        Assert.assertNull(tratamentoServico.capturaDossieTratamento(process.getId()));
    }

    @Test
    public void capturaDossieTratamentoSucesso() throws Exception {
        Processo process = buscarProcesso();
        DossieProduto dossieProduto = buscarDossieProduto();
        this.mockTipoSituacaoDossieAguardandoTratamento();
        this.mockTipoSituacaoDossieEMTratamento();
        tipoSituacaoDossie = this.mockTipoSituacaoDossieAguardandoTratamento();
        mockaProcessogetById(process);
        mockarDossieProdutoLocalizaMaisAntigoPorTipoSituacao(process, dossieProduto);
        mockaDossieProdutoById(dossieProduto);
        Assert.assertNotNull(tratamentoServico.capturaDossieTratamento(process.getId()));
    }

    /**
     * Finalizado Captura Dossie Tratamento ficou com 100%
     *
     */
    @Test(expected = SimtrRequisicaoException.class)
    public void retornaFilaTratamentoDossieProdutoByIdNull() throws Exception {
        this.tratamentoServico.retornaFilaTratamento(null);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void retornaFilaTratamentoDossieNaoExisteDossieEmTramento() throws Exception {
        DossieProduto dossieProduto = buscarDossieProduto();
        mockTipoSituacaoDossieEMTratamento();
        mockaDossieProdutoById(dossieProduto);
        mockarSituacaoDossieAguardandoTratamento(dossieProduto);
        this.tratamentoServico.retornaFilaTratamento(dossieProduto.getId());
    }

    @Test
    public void retornaFilaTratamentoDossieExisteDossieEmTramento() throws Exception {
        DossieProduto dossieProduto = buscarDossieProduto();
        mockTipoSituacaoDossieEMTratamento();
        mockaDossieProdutoById(dossieProduto);
        mockarSituacaoDossieSemDossieEmTratamento(dossieProduto);
        mockarDossieProdutoById(dossieProduto);
        this.tratamentoServico.retornaFilaTratamento(dossieProduto.getId());
    }

    @Test
    public void retornaFilaTratamentoDossieSUCESSO() throws Exception {
        DossieProduto dossieProduto = buscarDossieProduto();
        mockTipoSituacaoDossieEMTratamento();
        mockaDossieProdutoById(dossieProduto);
        mockarSituacaoDossieSemDossieEmTratamento(dossieProduto);
        mockarDossieProdutoById(dossieProduto);
        dossieProduto.setUnidadePriorizado(null);
        this.tratamentoServico.retornaFilaTratamento(dossieProduto.getId());
    }

    @Test
    public void retornaFilaTratamentoDossieSUCESSOMatriculaComValor() throws Exception {
        DossieProduto dossieProduto = buscarDossieProduto();
        mockTipoSituacaoDossieEMTratamento();
        mockaDossieProdutoById(dossieProduto);
        mockarSituacaoDossieSemDossieEmTratamento(dossieProduto);
        mockarDossieProdutoById(dossieProduto);
        dossieProduto.setUnidadePriorizado(null);
        dossieProduto.setMatriculaPriorizado("c999999");
        this.tratamentoServico.retornaFilaTratamento(dossieProduto.getId());
    }

    /**
     * Finalizado 100% RetornaFilaTratamento
     *
     */
    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void executarTratamentoCanalInvalido() {
        mockCanalInvalido();
        this.tratamentoServico.executaTratamento(1L, null);
    }

    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void executarTratamentoCanalValido() {
        mockCanalValido();
        this.tratamentoServico.executaTratamento(1L, null);
    }

    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void executarTratamentoLocalizarDossieProdutoNull() throws Exception {
        mockCanalValido();
        mockaDossieProdutoById(null);
        this.tratamentoServico.executaTratamento(1L, null);
    }

    @Test(expected = SimtrEstadoImpeditivoException.class)
    public void executarTratamentoSituacaoInvalida() throws Exception {
        mockCanalValido();
        DossieProduto dossieProduto = buscarDossieProduto();
        mockTipoSituacaoDossieEMTratamento();
        mockaDossieProdutoById(dossieProduto);
        mockarSituacaoDossieAguardandoTratamento(dossieProduto);
        this.tratamentoServico.executaTratamento(1L, null);
    }

    @Test(expected = SimtrRecursoBloqueadoException.class)
    public void executarTratamentoSituacaoMatricarInvalida() throws Exception {
        mockCanalValido();
        DossieProduto dossieProduto = buscarDossieProduto();
        mockTipoSituacaoDossieEMTratamento();
        mockaDossieProdutoById(dossieProduto);
        mockarSituacaoDossieSemDossieEmTratamento(dossieProduto);
        mockarDossieProdutoById(dossieProduto);
        mockMatriculaDiferente();
        this.tratamentoServico.executaTratamento(1L, null);
    }

    @Test
    public void executarTratamentoVerificarcoesDesconsideradas() throws Exception {
        mockCanalValido();
        DossieProduto dossieProduto = buscarDossieProduto();
        mockTipoSituacaoDossieEMTratamento();
        mockaDossieProdutoById(dossieProduto);
        mockarSituacaoDossieSemDossieEmTratamento(dossieProduto);
        mockarDossieProdutoById(dossieProduto);

        Processo process = buscarProcesso();
        process.setProcessoDocumentos(new HashSet<>());
        process.setProdutos(new HashSet<>());
        dossieProduto.setProcesso(process);

        Processo processoFase = buscarProcessoFase();
        processoFase.setVinculacoesChecklistsFase(new HashSet<>());
        ProcessoFaseDossie processoFaseDossie = new ProcessoFaseDossie();
        processoFaseDossie.setId(3L);
        processoFaseDossie.setProcessoFase(processoFase);
        dossieProduto.addProcessosFaseDossie(processoFaseDossie);

        Mockito.when(this.processoServico.getById(process.getId())).thenReturn(process);
        Mockito.when(this.processoServico.getById(processoFase.getId())).thenReturn(processoFase);

        dossieProduto.setChecklistsAssociados(new HashSet<>());
        dossieProduto.setInstanciasDocumento(new HashSet<>());

        mockMatricula();
        verificacoes = new ArrayList<>();

        this.tratamentoServico.executaTratamento(1L, verificacoes);
    }

    @Test(expected = SimtrVerificacaoInvalidaException.class)
    public void executarTratamentoVerificarcoesDesconsideradasExistePendencia() throws Exception {
        mockCanalValido();
        DossieProduto dossieProduto = buscarDossieProduto();
        mockTipoSituacaoDossieEMTratamento();
        mockaDossieProdutoById(dossieProduto);
        mockarSituacaoDossieSemDossieEmTratamento(dossieProduto);
        mockarDossieProdutoById(dossieProduto);

        mokarProcessoEProcessoFase(dossieProduto);

        dossieProduto.setChecklistsAssociados(new HashSet<>());
        dossieProduto.setInstanciasDocumento(new HashSet<>());

        mockMatricula();
        mockVerificacoesVO();

        this.tratamentoServico.executaTratamento(1L, verificacoes);
    }

    @Test
    public void executarTratamentoVerificarcoesDesconsideradasCheckListFaseSituacaoInConforme() throws Exception {
        mockCanalValido();
        DossieProduto dossieProduto = buscarDossieProduto();
        mockTipoSituacaoDossieEMTratamento();
        mockaDossieProdutoById(dossieProduto);
        mockarSituacaoDossieSemDossieEmTratamento(dossieProduto);
        mockarDossieProdutoById(dossieProduto);
        mokarProcessoEProcessoFase(dossieProduto);
        dossieProduto.setChecklistsAssociados(new HashSet<>());
        dossieProduto.setInstanciasDocumento(new HashSet<>());
        mockarSituacaoConforme();
        mockMatricula();
        mockarListaVerificacaoVO("mock/tratamento/verificacaoVO.json");
        mockarCheckList();

        this.tratamentoServico.executaTratamento(1L, verificacoes);
    }

    @Test(expected = SimtrVerificacaoInvalidaException.class)
    public void executarTratamentoVerificarcoesDesconsideradasCheckListFaseSituacaoInConformeCheklistnullVerificacaoInesperada() throws Exception {
        mockCanalValido();
        DossieProduto dossieProduto = buscarDossieProduto();
        mockTipoSituacaoDossieEMTratamento();
        mockaDossieProdutoById(dossieProduto);
        mockarSituacaoDossieSemDossieEmTratamento(dossieProduto);
        mockarDossieProdutoById(dossieProduto);
        mokarProcessoEProcessoFase(dossieProduto);
        dossieProduto.setChecklistsAssociados(new HashSet<>());
        dossieProduto.setInstanciasDocumento(new HashSet<>());
        mockarSituacaoConforme();
        mockMatricula();
        mockarListaVerificacaoVO("mock/tratamento/verificacaoVO-ChecklistFase-Inconforme.json");
        mockarCheckList();

        this.tratamentoServico.executaTratamento(1L, verificacoes);
    }

    @Test(expected = SimtrVerificacaoInvalidaException.class)
    public void executarTratamentoVerificarcoesDesconsideradasCheckListFaseSituacaoInConformeCheklistnull() throws Exception {
        mockCanalValido();
        DossieProduto dossieProduto = buscarDossieProduto();
        mockTipoSituacaoDossieEMTratamento();
        mockaDossieProdutoById(dossieProduto);
        mockarSituacaoDossieSemDossieEmTratamento(dossieProduto);
        mockarDossieProdutoById(dossieProduto);
        mokarProcessoEProcessoFase(dossieProduto);
        dossieProduto.setChecklistsAssociados(new HashSet<>());
        dossieProduto.setInstanciasDocumento(new HashSet<>());
        mockarSituacaoConforme();
        mockMatricula();
        mockarListaVerificacaoVO("mock/tratamento/verificacaoVO-ChecklistFase-Inconforme.json");
        mockarCheckList();

        this.tratamentoServico.executaTratamento(1L, verificacoes);
    }
    
    @Test(expected = SimtrRequisicaoException.class)
    public void selecionaDossieProdutoNaoEncontrado() throws Exception {
        Mockito.when(this.dossieProdutoServico.getById(1L)).thenReturn(null);
        this.tratamentoServico.selecionaDossieProduto(1L);
    }
    
    @Test(expected = SimtrPermissaoException.class)
    public void selecionaDossieProdutoSempPermissãoLotacaoAdministrativa() throws Exception {
        DossieProduto dossieProduto = buscarDossieProduto();
        mockLotacaoAdministrativa();
        mockUnidadeTratamento(7777, dossieProduto);     
        Mockito.when(this.dossieProdutoServico.getById(116L)).thenReturn(dossieProduto);
        this.tratamentoServico.selecionaDossieProduto(116L);
    }
    
    @Test(expected = SimtrPermissaoException.class)
    public void selecionaDossieProdutoProcessoNaoSeletivo() throws Exception {
        DossieProduto dossieProduto = buscarDossieProduto();
        dossieProduto.getProcesso().setTratamentoSeletivo(Boolean.FALSE);
        mockarSituacaoDossieAguardandoTratamento(dossieProduto);
        mockLotacaoFisica();
        mockTipoSituacaoDossieAguardandoTratamento();
        mockUnidadeTratamento(8888, dossieProduto);
        Mockito.when(this.dossieProdutoServico.getById(116L)).thenReturn(dossieProduto);
        this.tratamentoServico.selecionaDossieProduto(116L);
    }
    
    @Test
    public void selecionaDossieProduto() throws Exception {
        DossieProduto dossieProduto = buscarDossieProduto();
        dossieProduto.getProcesso().setTratamentoSeletivo(Boolean.TRUE);
        mockarSituacaoDossieAguardandoTratamentoId1(dossieProduto);
        mockLotacaoFisica();
        mockTipoSituacaoDossieAguardandoTratamento();
        mockUnidadeTratamento(8888, dossieProduto);
        Mockito.when(this.dossieProdutoServico.getById(116L)).thenReturn(dossieProduto);
        this.tratamentoServico.selecionaDossieProduto(116L);
    }
    
    @Test
    public void selecionaDossieProdutoSituacaoDossieDiferente() throws Exception {
        DossieProduto dossieProduto = buscarDossieProduto();
        dossieProduto.getProcesso().setTratamentoSeletivo(Boolean.TRUE);
        mockarSituacaoDossieSemDossieEmTratamento(dossieProduto);
        mockLotacaoFisica();
        mockTipoSituacaoDossieAguardandoTratamento();
        mockUnidadeTratamento(8888, dossieProduto);
        Mockito.when(this.dossieProdutoServico.getById(116L)).thenReturn(dossieProduto);
        this.tratamentoServico.selecionaDossieProduto(116L);
    }
    
    @Test(expected = SimtrRequisicaoException.class)
    public void renovarTempoDeSessaoNaoEncontrado() throws Exception {
        Mockito.when(this.dossieProdutoServico.getById(1L)).thenReturn(null);
        this.tratamentoServico.renovarTempoTratamento(1L);
    }
    
    @Test(expected = SimtrPermissaoException.class)
    public void renovarTempoDeSessaoResponsavelNaoEncontrado() throws Exception {
        DossieProduto dossieProduto = buscarDossieProduto();
        mockMatricula();
        Mockito.when(this.dossieProdutoServico.getById(116L)).thenReturn(dossieProduto);
        this.tratamentoServico.renovarTempoTratamento(116L);
    }
    
    @Test(expected = SimtrRequisicaoException.class)
    public void renovarTempoDeSessaoSituacaoDosisseDiferenteDoEsperado() throws Exception {
        DossieProduto dossieProduto = buscarDossieProduto();
        mockMatricula();
        mockarSituacaoDossieSemDossieEmTratamento(dossieProduto);
        mockTipoSituacaoDossieAguardandoTratamento();
        Mockito.when(this.dossieProdutoServico.getById(116L)).thenReturn(dossieProduto);
        this.tratamentoServico.renovarTempoTratamento(116L);
    }
    
    @Test
    public void renovarTempoDeSessaoFinalizado() throws Exception {
        DossieProduto dossieProduto = buscarDossieProduto();
        mockMatricula();
        mockarSituacaoDossieSemDossieEmTratamento(dossieProduto);
        mockTipoSituacaoDossieEMTratamento();
        Mockito.when(this.dossieProdutoServico.getById(116L)).thenReturn(dossieProduto);
        this.tratamentoServico.renovarTempoTratamento(116L);
    }

    private void mockarCheckList() {
        Checklist ckList = mockarCheckListPrevia();
        Mockito.when(this.checklistServico.getById(Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(ckList);
    }

    private void mockarSituacaoConforme() {
        SituacaoDossieEnum situacaoFinalDossieEnum = SituacaoDossieEnum.CONFORME;
        TipoSituacaoDossie tipoSituacaoFinalDossie = new TipoSituacaoDossie();
        tipoSituacaoFinalDossie.setId(5);
        Mockito.when(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(situacaoFinalDossieEnum)).thenReturn(tipoSituacaoFinalDossie);
    }

    private void mockarListaVerificacaoVO(String url) throws Exception {
        verificacoes = new ArrayList<VerificacaoVO>();
        String retornoConsulta = getJsonDeArquivo(url);

        JSONObject objJson = new JSONObject(retornoConsulta);
        JSONArray jArray = objJson.getJSONArray("verificacoesRealizadas");

        for (int i = 0; i < jArray.length(); i++) {

            JSONObject e = jArray.getJSONObject(i);
            VerificacaoVO verifica = new VerificacaoVO();

            if (!e.isNull("identificadorChecklist")) {
                verifica.setIdentificadorChecklist(e.getInt("identificadorChecklist"));
            }

            verifica.setAnaliseRealizada(e.getBoolean("analiseRealizada"));

            if (!e.isNull("identificadorIntanciaDocumento")) {
                verifica.setIdentificadorInstanciaDocumento(e.getLong("identificadorIntanciaDocumento"));
            }
            JSONArray listaParecer = e.getJSONArray("parecerApontamentosVO");
            for (int j = 0; j < listaParecer.length(); j++) {

                JSONObject ap = listaParecer.getJSONObject(j);
                String comentario = !ap.isNull("comentario") ? ap.getString("comentario") : "sem comentario";
                ParecerApontamentoVO parecerVO = new ParecerApontamentoVO(ap.getLong("identificadorApontamento"), ap.getBoolean("aprovado"), comentario);
                verifica.addParecerApontamentosVO(parecerVO);
            }

            verificacoes.add(verifica);
        }
    }

    private void mokarProcessoEProcessoFase(DossieProduto dossieProduto) throws Exception {
        Processo process = buscarProcesso();
        process.setProcessoDocumentos(new HashSet<>());
        process.setProdutos(new HashSet<>());
        process.setChecklistsAssociados(new HashSet<>());
        dossieProduto.setProcesso(process);

        Processo processoFase = buscarProcessoFase();

        VinculacaoChecklist vcheck = new VinculacaoChecklist();
        vcheck.setId(113L);
        Integer dia = 1, mes = 1, ano = 2022;
        Calendar cal = definirDataCalendar(dia, mes, ano);
        vcheck.setDataRevogacao(cal);
        Checklist ckList = mockarCheckListPrevia();
        vcheck.setProcessoDossie(dossieProduto.getProcesso());
        vcheck.setChecklist(ckList);
        processoFase.setVinculacoesChecklistsFase(new HashSet<>());
        processoFase.getVinculacoesChecklistsFase().add(vcheck);
        ProcessoFaseDossie processoFaseDossie = new ProcessoFaseDossie();
        processoFaseDossie.setId(3L);
        processoFaseDossie.setProcessoFase(processoFase);
        dossieProduto.addProcessosFaseDossie(processoFaseDossie);

        Mockito.when(this.processoServico.getById(process.getId())).thenReturn(process);
        Mockito.when(this.processoServico.getById(processoFase.getId())).thenReturn(processoFase);
    }

    private Checklist mockarCheckListPrevia() {
        Checklist ckList = new Checklist();
        ckList.setId(4);
        ckList.setIndicacaoVerificacaoPrevia(true);
        return ckList;
    }

    private Calendar definirDataCalendar(Integer dia, Integer mes, Integer ano) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, dia);
        cal.set(Calendar.MONTH, mes);
        cal.set(Calendar.DAY_OF_YEAR, ano);
        return cal;
    }

    /**
     * Parametros globais mockados
     *
     */
    private void mockVerificacoesVO() {
        verificacoes = new ArrayList<VerificacaoVO>();
        VerificacaoVO verificacao = new VerificacaoVO();
        verificacao.setIdentificadorChecklist(1);
        verificacao.setIdentificadorInstanciaDocumento(2L);
        verificacoes.add(verificacao);
    }

    private void mockarSituacaoDossieSemDossieEmTratamento(DossieProduto dossieProduto) {
        SituacaoDossie situacao = new SituacaoDossie();
        TipoSituacaoDossie tipoS = new TipoSituacaoDossie();
        tipoS.setNome("EM_TRATAMENTO");
        tipoS.setId(1);
        situacao.setTipoSituacaoDossie(tipoS);
        situacao.setResponsavel("c999999");
        situacao.setDataHoraInclusao(Calendar.getInstance());
        dossieProduto.setSituacoesDossie(new HashSet<>());
        dossieProduto.getSituacoesDossie().add(situacao);
    }

    private void mockarSituacaoDossieAguardandoTratamento(DossieProduto dossieProduto) {
        SituacaoDossie situacao = new SituacaoDossie();
        TipoSituacaoDossie tipoS = new TipoSituacaoDossie();
        tipoS.setNome("AGUARDANDO_TRATAMENTO");
        tipoS.setId(2);
        situacao.setTipoSituacaoDossie(tipoS);
        situacao.setResponsavel("c999999");
        dossieProduto.setSituacoesDossie(new HashSet<>());
        dossieProduto.getSituacoesDossie().add(situacao);
    }
    
    private void mockarSituacaoDossieAguardandoTratamentoId1(DossieProduto dossieProduto) {
        SituacaoDossie situacao = new SituacaoDossie();
        TipoSituacaoDossie tipoS = new TipoSituacaoDossie();
        tipoS.setNome("AGUARDANDO_TRATAMENTO");
        tipoS.setId(1);
        situacao.setTipoSituacaoDossie(tipoS);
        situacao.setResponsavel("c999999");
        dossieProduto.setSituacoesDossie(new HashSet<>());
        dossieProduto.getSituacoesDossie().add(situacao);
    }

    private void mockaDossieProdutoById(DossieProduto dossieProduto) {
        Mockito.when(this.dossieProdutoServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(dossieProduto);
    }

    private TipoSituacaoDossie mockTipoSituacaoDossieAguardandoTratamento() {
        TipoSituacaoDossie tipoSituacaoDossieAgurdandoTratamento = new TipoSituacaoDossie();
        tipoSituacaoDossieAgurdandoTratamento.setId(1);
        tipoSituacaoDossieAgurdandoTratamento.setNome("AGUARDANDO_TRATAMENTO");
        Mockito.when(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.AGUARDANDO_TRATAMENTO)).thenReturn(tipoSituacaoDossieAgurdandoTratamento);
        return tipoSituacaoDossieAgurdandoTratamento;
    }

    private void mockTipoSituacaoDossieEMTratamento() {
        tipoSituacaoDossie = new TipoSituacaoDossie();
        tipoSituacaoDossie.setId(1);
        tipoSituacaoDossie.setNome("EM_TRATAMENTO");
        Mockito.when(this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.EM_TRATAMENTO)).thenReturn(tipoSituacaoDossie);
    }

    private Processo buscarProcesso() throws Exception {
        String retornoConsulta = getJsonDeArquivo("mock/tratamento/processoPJ-T1.json");
        Processo process = (Processo) UtilJson.converterDeJson(retornoConsulta, Processo.class);
        return process;
    }

    private Processo buscarProcessoFase() throws Exception {
        String retornoConsulta = getJsonDeArquivo("mock/tratamento/processoFase-ATI-F1.json");
        Processo process = (Processo) UtilJson.converterDeJson(retornoConsulta, Processo.class);
        return process;
    }

    private DossieProduto buscarDossieProduto() throws Exception {
        String retornoConsulta = getJsonDeArquivo("mock/tratamento/dossie-produto.json");
        DossieProduto dossieProduto = (DossieProduto) UtilJson.converterDeJson(retornoConsulta, DossieProduto.class);
        return dossieProduto;
    }

    private void mockarDossieProdutoLocalizaMaisAntigoPorTipoSituacao(Processo process, DossieProduto dossieProduto) {
        Mockito.when(this.dossieProdutoServico.localizaMaisAntigoByProcessoAndTipoSituacao(process, tipoSituacaoDossie)).thenReturn(dossieProduto);
    }

    private void mockarDossieProdutoById(DossieProduto dossieProduto) {
        Mockito.when(this.dossieProdutoServico.getById(Mockito.anyLong())).thenReturn(dossieProduto);
    }

    private void mockCanalValido() {
        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(new Canal());
    }

    private void mockCanalInvalido() {
        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(null);
    }

    private void mockMatricula() {
        Mockito.when(this.keycloakService.getMatricula()).thenReturn("c999999");
    }

    private void mockMatriculaDiferente() {
        Mockito.when(this.keycloakService.getMatricula()).thenReturn("c999998");
    }

    private void mockaProcessogetById(Processo process) {
        Mockito.when(this.processoServico.getById(Mockito.anyInt())).thenReturn(process);
    }
    
    private void mockLotacaoAdministrativa() {
        Mockito.when(this.keycloakService.getLotacaoAdministrativa()).thenReturn(9999);
    }
    
    private void mockLotacaoFisica() {
        Mockito.when(this.keycloakService.getLotacaoFisica()).thenReturn(8888);
    }
    
    private void mockUnidadeTratamento(Integer unidadeId, DossieProduto dossieProduto) {
        UnidadeTratamento unidade = new UnidadeTratamento();
        unidade.setUnidade(unidadeId);
        unidade.setDossieProduto(dossieProduto);
        dossieProduto.addUnidadesTratamento(unidade);
    }
}
