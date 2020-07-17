package br.gov.caixa.simtr.controle.servico;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

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

import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.pedesgo.arquitetura.util.UtilParametro;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.ApensoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.ContratoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.DocumentoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.ProcessoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoApensoEnum;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UtilParametro.class})
public class ProcessoAdministrativoServicoTest {

    @Mock
    private CanalServico canalServico;

    @Mock
    private DocumentoServico documentoServico;

    @Mock
    private DocumentoAdministrativoServico documentoAdministrativoServico;

    @Mock
    private TipoDocumentoServico tipoDocumentoServico;

    @Mock
    private EntityManager manager;

    @Mock
    private Logger logger;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private TypedQuery<ProcessoAdministrativo> query;

    @Mock
    private TypedQuery<TipoDocumento> queryTipoDocumento;

    @Mock
    private RelatorioServico relatorioServico;

    private String binario;

    @InjectMocks
    private ProcessoAdministrativoServico servico;

    ProcessoAdministrativo processoAdministrativo;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(UtilParametro.class);
    }

    @Test
    public void saveTest() {

        mockQuery();
        mockResultList();
        mockProcessoAdmnistrativo();
        servico.save(processoAdministrativo);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void saveTestException() {

        mockQuery();
        mockSingleResultNRE();
        mockProcessoAdmnistrativo();
        processoAdministrativo.setProtocoloSICLG("123");
        servico.save(processoAdministrativo);
    }

    @Test
    public void getByIdTeste() {
        mockQuery();
        mockProcessoAdmnistrativo();
        mockSingleResult();
        servico.getById(1L, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
    }

    @Test
    public void getByIdTesteNull() {
        mockQuery();
        mockSingleResultNRE();
        Assert.assertNull(servico.getById(1L, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
    }

    @Test
    public void getByNumeroAnoTest() {
        mockQuery();
        mockProcessoAdmnistrativo();
        mockSingleResult();
        servico.getByNumeroAno(1, 2018, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
    }

    @Test
    public void getByNumeroAnoTestNull() {
        mockQuery();
        mockSingleResultNRE();
        servico.getByNumeroAno(1, 2018, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
    }

    @Test
    public void listByDescricaoTest() {
        mockQuery();
        mockSingleResult();
        servico.listByDescricao("Objeto", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
    }

    @Test
    public void listByDescricaoTestNull() {
        mockQuery();
        mockResultListNRE();
        Assert.assertNull(servico.listByDescricao("Objeto", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
    }

    @Test
    public void aplicaPatchTest() {
        mockQuery();
        mockProcessoAdmnistrativo();
        mockSingleResult();
        mockProcessoAdmnistrativo();
        servico.aplicaPatch(1L, processoAdministrativo);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void aplicaPatchTestVazio() {
        mockQuery();
        mockSingleResult();
        ProcessoAdministrativo processo = new ProcessoAdministrativo();
        processo.setProtocoloSICLG("123");
        servico.aplicaPatch(1L, processo);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void criaDocumentoProcessoAdministrativoTesteCanalInvalido() {
        mockCanalInvalido();
        servico.criaDocumentoProcessoAdministrativo(1L, 1, 1l, "JustificativaSubstituicao", OrigemDocumentoEnum.A, true, true, "Descrição", "image/bmp", new ArrayList<>(), null);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void criaDocumentoProcessoAdministrativoTesteTipoDocumentoInvalido() {
        mockCanalValido();
        mockTipoDocumentoInvalido();
        servico.criaDocumentoProcessoAdministrativo(1L, 1, 1l, "JustificativaSubstituicao", OrigemDocumentoEnum.A, true, true, "Descrição", "image/bmp", new ArrayList<>(), null);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void criaDocumentoProcessoAdministrativoTesteProcessoInvalido() {
        mockCanalValido();
        mockTipoDocumentoValido();
        mockQuery();
        mockSingleResultNRE();
        servico.criaDocumentoProcessoAdministrativo(1L, 1, 1l, "", OrigemDocumentoEnum.A, true, true, "Descrição", "image/bmp", new ArrayList<>(), null);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void criaDocumentoProcessoAdministrativoTesteProcessoValidoMimeTrue() {
        mockCanalValido();
        mockTipoDocumentoValido();
        mockQuery();
        mockProcessoAdmnistrativo();
        mockSingleResult();
        mockCarregarListaBase64();
        carregarMockConteudos64True();
        servico.criaDocumentoProcessoAdministrativo(1L, 1, 1l, "", OrigemDocumentoEnum.A, true, true, "Descrição", "image/bmp", new ArrayList<>(), binario);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void criaDocumentoProcessoAdministrativoTesteProcessoValidoMimeFalse() {
        mockCanalValido();
        mockTipoDocumentoValido();
        mockQuery();
        mockProcessoAdmnistrativo();
        mockSingleResult();
        mockCarregarListaBase64();
        carregarMockConteudos64False();
        servico.criaDocumentoProcessoAdministrativo(1L, 1, 1l, "", OrigemDocumentoEnum.A, true, true, "Descrição", "image/bmp", new ArrayList<>(), binario);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void criaDocumentoProcessoAdministrativoTesteProcessoValidoException() {
        mockCanalValido();
        mockTipoDocumentoValido();
        mockQuery();
        mockProcessoAdmnistrativo();
        mockSingleResult();
        mockCarregarListaBase64();
        carregarMockConteudos64Exception();
        servico.criaDocumentoProcessoAdministrativo(1L, 1, 1l, "", OrigemDocumentoEnum.A, true, true, "Descrição", "image/bmp", new ArrayList<>(), binario);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void criaDocumentoProcessoAdministrativoTesteJustificativaException() {
        mockCanalValido();
        mockTipoDocumentoValido();
        mockQuery();
        mockProcessoAdmnistrativo();
        mockSingleResult();
        mockCarregarListaBase64();
        servico.criaDocumentoProcessoAdministrativo(1L, 1, 1L, "", OrigemDocumentoEnum.A, true, true, "Descrição", "image/bmp", new ArrayList<>(), binario);
    }

    @Test
    public void criaDocumentoProcessoAdministrativoTestSuccess() {
        mockCanalValido();
        mockTipoDocumentoValido();
        mockQuery();
        mockProcessoAdmnistrativo();
        mockSingleResult();
        mockCarregarListaBase64();
        servico.criaDocumentoProcessoAdministrativo(1L, 1, 1L, "JustificativaSubstituicao", OrigemDocumentoEnum.A, true, true, "Descrição", null, new ArrayList<>(), binario);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void criaRelatorioPAETest() throws IOException {
        mockQuery();
        mockProcessoAdmnistrativo();
        mockSingleResultNRE();
        servico.criaRelatorioPAE(1L, Boolean.TRUE, Boolean.TRUE);
    }

    @Test
    public void criaRelatorioPAETestProcesso() throws IOException {
        mockQuery();
        mockProcessoAdmnistrativo();
        mockSingleResult();
        mockArquivoZip();
        servico.criaRelatorioPAE(1L, Boolean.TRUE, Boolean.TRUE);
    }

    @Test(expected = SimtrConfiguracaoException.class)
    public void criaRelatorioPAETestProcessoFalha() throws IOException {
        mockQuery();
        mockProcessoAdmnistrativoFalhaApenso();
        mockSingleResult();
        mockArquivoZip();
        servico.criaRelatorioPAE(1L, Boolean.TRUE, Boolean.TRUE);
    }

    private void mockProcessoAdmnistrativo() {
        processoAdministrativo = new ProcessoAdministrativo();
        processoAdministrativo.setAnoPregao(2019);
        processoAdministrativo.adicionarMensagem("MensagemTeste");
        processoAdministrativo.setAnoProcesso(2020);
        processoAdministrativo.setDataHoraFinalizacao(Calendar.getInstance());
        processoAdministrativo.setDataHoraInclusao(Calendar.getInstance());
        processoAdministrativo.setMatriculaFinalizacao("C999999");
        processoAdministrativo.setMatriculaInclusao("F999999");
        processoAdministrativo.setNumeroPregao(10000000);
        processoAdministrativo.setUnidadeDemandante(1036);
        processoAdministrativo.setUnidadeContratacao(2222);
        processoAdministrativo.setNumeroProcesso(321654987);
        processoAdministrativo.setObjetoContratacao("Objeto  de Contratação");
        processoAdministrativo.setProtocoloSICLG("14782032");
        processoAdministrativo.setContratosAdministrativos(new HashSet<>());
        ContratoAdministrativo contrato = new ContratoAdministrativo();
        contrato.setAnoContrato(2020);
        contrato.setNumeroContrato(123333);
        contrato.setDocumentosAdministrativos(new HashSet<>());
        contrato.setApensosAdministrativos(new HashSet<>());
        ApensoAdministrativo apenso = new ApensoAdministrativo();
        apenso.setId(1L);
        apenso.setTipoApenso(TipoApensoEnum.AX);
        apenso.setTitulo("titutlo apenso");
        contrato.getApensosAdministrativos().add(apenso);
        DocumentoAdministrativo doc = new DocumentoAdministrativo();
        doc.setId(1L);
        contrato.getDocumentosAdministrativos().add(doc);
        processoAdministrativo.getContratosAdministrativos().add(contrato);
        processoAdministrativo.setApensosAdministrativos(new HashSet<>());
        processoAdministrativo.getApensosAdministrativos().add(apenso);
    }

    private void mockProcessoAdmnistrativoFalhaApenso() {
        processoAdministrativo = new ProcessoAdministrativo();
        processoAdministrativo.setAnoPregao(2019);
        processoAdministrativo.adicionarMensagem("MensagemTeste");
        processoAdministrativo.setAnoProcesso(2020);
        processoAdministrativo.setDataHoraFinalizacao(Calendar.getInstance());
        processoAdministrativo.setDataHoraInclusao(Calendar.getInstance());
        processoAdministrativo.setMatriculaFinalizacao("C999999");
        processoAdministrativo.setMatriculaInclusao("F999999");
        processoAdministrativo.setNumeroPregao(10000000);
        processoAdministrativo.setUnidadeDemandante(1036);
        processoAdministrativo.setUnidadeContratacao(2222);
        processoAdministrativo.setNumeroProcesso(321654987);
        processoAdministrativo.setObjetoContratacao("Objeto  de Contratação");
        processoAdministrativo.setProtocoloSICLG("14782032");
        processoAdministrativo.setContratosAdministrativos(new HashSet<>());
        ContratoAdministrativo contrato = new ContratoAdministrativo();
        contrato.setAnoContrato(2020);
        contrato.setNumeroContrato(123333);
        contrato.setDocumentosAdministrativos(new HashSet<>());
        contrato.setApensosAdministrativos(new HashSet<>());
        ApensoAdministrativo apenso = new ApensoAdministrativo();
        apenso.setId(1L);
        apenso.setTipoApenso(TipoApensoEnum.AX);
        apenso.setTitulo("titutlo apenso");
        contrato.getApensosAdministrativos().add(apenso);
        DocumentoAdministrativo doc = new DocumentoAdministrativo();
        doc.setId(1L);
        contrato.getDocumentosAdministrativos().add(doc);
        processoAdministrativo.getContratosAdministrativos().add(contrato);
        processoAdministrativo.setApensosAdministrativos(new HashSet<>());
        processoAdministrativo.getApensosAdministrativos().add(new ApensoAdministrativo());
    }

    private void mockQuery() {
        when(manager.createQuery(anyString(), eq(ProcessoAdministrativo.class))).thenReturn(query);
    }

    private void mockSingleResult() {
        when(query.getSingleResult()).thenReturn(processoAdministrativo);
    }

    private void mockSingleResultNull() {
        when(query.getSingleResult()).thenReturn(null);
    }

    private void mockResultList() {
        when(query.getResultList()).thenReturn(new ArrayList<>());
    }

    private void mockSingleResultNRE() {
        Mockito.doThrow(NoResultException.class).when(query).getSingleResult();
    }

    private void mockResultListNRE() {
        Mockito.doThrow(NoResultException.class).when(query).getResultList();
    }

    private void mockCanalValido() {
        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(new Canal());
    }

    private void mockCanalInvalido() {
        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(null);
    }

    private void mockTipoDocumentoValido() {
        Mockito.when(this.tipoDocumentoServico.getById(Mockito.anyInt())).thenReturn(new TipoDocumento());
    }

    private void mockTipoDocumentoInvalido() {
        Mockito.when(this.tipoDocumentoServico.getById(Mockito.anyInt())).thenReturn(null);
    }

    private void mockCarregarListaBase64() {
        binario = "65AS165D1236451R65S1D65F153241T53S1D3T54SE54";
    }

    private void carregarMockConteudos64True() {
        Mockito.when(this.documentoAdministrativoServico.validaMimetypeConteudo(Mockito.any(FormatoConteudoEnum.class), Mockito.anyString())).thenReturn(Boolean.TRUE);
    }

    private void carregarMockConteudos64False() {
        Mockito.when(this.documentoAdministrativoServico.validaMimetypeConteudo(Mockito.any(FormatoConteudoEnum.class), Mockito.anyString())).thenReturn(Boolean.FALSE);
    }

    private void carregarMockConteudos64Exception() {
        Mockito.doThrow(new IllegalArgumentException()).when(documentoAdministrativoServico).validaMimetypeConteudo(Mockito.any(FormatoConteudoEnum.class), Mockito.anyString());
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

}
