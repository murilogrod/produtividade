package br.gov.caixa.simtr.controle.servico;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.io.IOUtils;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.caixa.pedesgo.arquitetura.siiso.dto.RetornoPessoasFisicasDTO;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.DossieClientePF;
import br.gov.caixa.simtr.modelo.entidade.DossieClientePJ;
import br.gov.caixa.simtr.modelo.entidade.DossieClienteProduto;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.InstanciaDocumento;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.AtributoDocumentoDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UtilJson.class, Boolean.class, System.class})
public class DossieDigitalServicoTest {

    @Mock
    private AvaliacaoFraudeServico avaliacaoFraudeServico;
    
    @Mock
    private CadastroReceitaServico cadastroReceitaServico;

    @Mock
    private CadastroCaixaServico cadastroCaixaServico;
    
    @Mock
    private CanalServico canalServico;

    @Mock
    private DocumentoServico documentoServico;

    @Mock
    private DossieDigitalServico _self;

    @Mock
    private DossieClienteServico dossieClienteServico;

    @Mock
    private DossieProdutoServico dossieProdutoServico;

    @Mock
    private RelatorioServico relatorioServico;

    @Mock
    private SiecmServico siecmServico;

    @Mock
    private TipoDocumentoServico tipoDocumentoServico;

    @Mock
    private EntityManager entityManager;
    
    @InjectMocks
    private DossieDigitalServico dossieDigitalServico;

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_DIR = "/mock/dossie-digital/";
    private static final String UTF_8 = "UTF-8";

    private Canal canalSIMTR;
    private String cnhBase64;
    private String cartaoAssinaturaBase64;
    private Documento documentoCNH;
    private Documento documentoCartaoAssinatura;
    private Documento documentoDadosDeclarados;
    private TipoDocumento tipoDocumentoCNH;
    private TipoDocumento tipoDocumentoCartaoAssinatura;
    private TipoDocumento tipoDocumentoDadosDeclarados;
    private DossieClientePF dossieCliente00065179609;
    private DossieClientePJ dossieCliente00013888000105;
    private RetornoPessoasFisicasDTO retornoReceita00065179609;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(UtilJson.class);
        
        Mockito.doNothing().when(this.entityManager).persist(Mockito.any());

        String jsonCanalSIMTR = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("canal-simtr.json")), UTF_8);
        this.canalSIMTR = mapper.readValue(jsonCanalSIMTR, Canal.class);

        this.cnhBase64 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("cnh-base64.txt")), UTF_8);
        this.cartaoAssinaturaBase64 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("cartao-assinatura-base64.txt")), UTF_8);

        String json00065179609 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("00065179609.json")), UTF_8);
        this.retornoReceita00065179609 = mapper.readValue(json00065179609, RetornoPessoasFisicasDTO.class);

        String jsonCNH = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("cnh.json")), UTF_8);
        this.documentoCNH = mapper.readValue(jsonCNH, Documento.class);
        
        String jsonCartaoAssinatura = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("cartao-assinatura.json")), UTF_8);
        this.documentoCartaoAssinatura = mapper.readValue(jsonCartaoAssinatura, Documento.class);

        String jsonDadosDeclarados = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("dados-declarados.json")), UTF_8);
        this.documentoDadosDeclarados = mapper.readValue(jsonDadosDeclarados, Documento.class);
        
        String jsonTipologiaCNH = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("tipologia-cnh.json")), UTF_8);
        this.tipoDocumentoCNH = mapper.readValue(jsonTipologiaCNH, TipoDocumento.class);
        
        String jsonTipologiaDadosDeclarados = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("tipologia-dados-declarados.json")), UTF_8);
        this.tipoDocumentoDadosDeclarados = mapper.readValue(jsonTipologiaDadosDeclarados, TipoDocumento.class);

        String jsonTipologiaCartaoAssinatura = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("tipologia-cartao-assinatura.json")), UTF_8);
        this.tipoDocumentoCartaoAssinatura = mapper.readValue(jsonTipologiaCartaoAssinatura, TipoDocumento.class);

        String jsonDossie00065179609 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("dossie-cliente-00065179609.json")), UTF_8);
        this.dossieCliente00065179609 = mapper.readValue(jsonDossie00065179609, DossieClientePF.class);

        String jsonDossie00013888000105 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("dossie-cliente-00013888000105.json")), UTF_8);
        this.dossieCliente00013888000105 = mapper.readValue(jsonDossie00013888000105, DossieClientePJ.class);

        Mockito.when(this.cadastroReceitaServico.consultaCadastroPF(65179609L)).thenReturn(this.retornoReceita00065179609);
        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(this.canalSIMTR);
        Mockito.when(this.dossieClienteServico.getByCpfCnpj(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(this.dossieCliente00065179609);
        Mockito.when(this.dossieClienteServico.getById(Mockito.eq(1L), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(this.dossieCliente00065179609);
        Mockito.when(this.dossieClienteServico.getById(Mockito.eq(2L), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(null);
        Mockito.when(this.dossieClienteServico.getById(Mockito.eq(3L), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(this.dossieCliente00013888000105);
        Mockito.when(this.tipoDocumentoServico.getByNome(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(this.tipoDocumentoCNH);
        Mockito.when(this.tipoDocumentoServico.getByTipologia(ConstantesUtil.TIPOLOGIA_CARTAO_ASSINATURA)).thenReturn(this.tipoDocumentoCartaoAssinatura);
        Mockito.when(this.tipoDocumentoServico.getByTipologia(ConstantesUtil.TIPOLOGIA_DADOS_DECLARADOS_DD)).thenReturn(tipoDocumentoDadosDeclarados);
        Mockito.when(this.siecmServico.armazenaDocumentoPessoalSIECM(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.any(Documento.class), Mockito.eq(TemporalidadeDocumentoEnum.TEMPORARIO_OCR), Mockito.anyString(), Mockito.anyString())).thenReturn("20879769-0000-C288-82D9-2EB6DE50A892");
        Mockito.when(this.siecmServico.obtemLinkAcessoDocumentoSIECM(Mockito.anyString(), Mockito.anyString())).thenReturn("http://servidor-siecm/siecm-web/ECM/getDocumento/false/a7b565c3ec208450acdfcffdd279810681/CNH");
        Mockito.doNothing().when(this.cadastroReceitaServico).validaDocumentoSICPF(Mockito.anyLong(), Mockito.eq(documentoCartaoAssinatura));
        Mockito.doNothing().when(this.documentoServico).defineDataValidade(documentoCartaoAssinatura);
        Mockito.doNothing().when(this.siecmServico).atualizaAtributosDocumento(Mockito.anyString(), Mockito.anyString(), Mockito.anyVararg());
        Mockito.doNothing().when(this.siecmServico).alteraSituacaoTemporalidadeDocumentoSIECM(Mockito.anyString(), Mockito.any(TemporalidadeDocumentoEnum.class), Mockito.anyString());
    }

    @Test
    public void testExtraiDadosDocumentoDossieDigital() {

        Mockito.when(this.siecmServico.executaExtracaoDadosDocumento(Mockito.any(TipoDocumento.class), Mockito.any(FormatoConteudoEnum.class), Mockito.anyString())).thenReturn(new HashMap<>());
        Mockito.when(this.documentoServico.prototype(
                Mockito.eq(canalSIMTR), Mockito.anyBoolean(), Mockito.eq(tipoDocumentoCNH), Mockito.eq(TemporalidadeDocumentoEnum.TEMPORARIO_OCR),
                Mockito.eq(OrigemDocumentoEnum.S), Mockito.any(FormatoConteudoEnum.class), Mockito.anyMap(), Mockito.anyString())
        ).thenReturn(this.documentoCNH);
        Mockito.doNothing().when(_self).armazenaDocumentoCliente(Mockito.anyLong(), Mockito.any(Documento.class));

        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(this.canalSIMTR);
        this.dossieDigitalServico.extraiDadosDocumentoDossieDigital(1L, FormatoConteudoEnum.JPG, "CNH", this.cnhBase64);
    }

    @Test
    public void testExtraiDadosDocumentoDossieDigitalArmazenamentoLocal() {
        PowerMockito.mockStatic(Boolean.class, System.class);

        Mockito.when(this.siecmServico.executaExtracaoDadosDocumento(Mockito.any(TipoDocumento.class), Mockito.any(FormatoConteudoEnum.class), Mockito.anyString())).thenReturn(new HashMap<>());
        Mockito.when(this.documentoServico.prototype(
                Mockito.eq(canalSIMTR), Mockito.anyBoolean(), Mockito.eq(tipoDocumentoCNH), Mockito.eq(TemporalidadeDocumentoEnum.TEMPORARIO_OCR),
                Mockito.eq(OrigemDocumentoEnum.S), Mockito.any(FormatoConteudoEnum.class), Mockito.anyMap(), Mockito.anyString())
        ).thenReturn(this.documentoCNH);
        Mockito.doNothing().when(_self).armazenaDocumentoCliente(Mockito.anyLong(), Mockito.any(Documento.class));

        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(this.canalSIMTR);
        PowerMockito.when(Boolean.getBoolean("simtr_imagens_local")).thenReturn(Boolean.TRUE);
//        PowerMockito.when(System.getProperty("simtr_imagens_local")).thenReturn("true");
        this.dossieDigitalServico.extraiDadosDocumentoDossieDigital(1L, FormatoConteudoEnum.JPG, "CNH", this.cnhBase64);
    }

    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void testExtraiDadosDocumentoDossieDigitalExceptionDossieClienteNull() {
        Mockito.when(this.dossieClienteServico.getById(Mockito.eq(2L), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(null);
        this.dossieDigitalServico.extraiDadosDocumentoDossieDigital(2L, FormatoConteudoEnum.JPG, "CNH", this.cnhBase64);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void testExtraiDadosDocumentoDossieDigitalExceptionConversaoAtributosJSON() throws Exception {

        Map<String, String> mapaAtributos = new HashMap<>();
        mapaAtributos.put("chave01", "valor01");
        mapaAtributos.put("chave02", "valor02");

        Mockito.when(this.siecmServico.executaExtracaoDadosDocumento(Mockito.any(TipoDocumento.class), Mockito.any(FormatoConteudoEnum.class), Mockito.anyString())).thenReturn(mapaAtributos);
        Mockito.when(this.documentoServico.prototype(
                Mockito.eq(canalSIMTR), Mockito.anyBoolean(), Mockito.eq(tipoDocumentoCNH), Mockito.eq(TemporalidadeDocumentoEnum.TEMPORARIO_OCR),
                Mockito.eq(OrigemDocumentoEnum.S), Mockito.any(FormatoConteudoEnum.class), Mockito.anyList(), Mockito.anyString())
        ).thenReturn(this.documentoCNH);
        PowerMockito.when(UtilJson.converterParaJson(mapaAtributos)).thenThrow(new SimtrRequisicaoException("Falha mockada para fins de execução de testes unitarios"));

        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(new Canal());
        this.dossieDigitalServico.extraiDadosDocumentoDossieDigital(1L, FormatoConteudoEnum.JPG, "CNH", this.cnhBase64);
    }

    @Test
    public void testGeraCartaoAssinatura() {
        Mockito.when(this.relatorioServico.gerarRelatorioPDFJsonDataSource(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap())).thenReturn("REPORT_PDF_MOCK_BASE64".getBytes());

        this.dossieDigitalServico.geraCartaoAssinatura(1L);
    }

    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void testGeraCartaoAssinaturaExceptionDossieClienteNull() {
        this.dossieDigitalServico.geraCartaoAssinatura(2L);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void testGeraCartaoAssinaturaExceptionTipoPessoa() {
        this.dossieDigitalServico.geraCartaoAssinatura(3L);
    }

    @Test(expected = SimtrConfiguracaoException.class)
    public void testGeraCartaoAssinaturaExceptionTipologiaNaoDefinida() {
        Mockito.when(this.tipoDocumentoServico.getByTipologia(ConstantesUtil.TIPOLOGIA_CARTAO_ASSINATURA)).thenReturn(null);
        
        this.dossieDigitalServico.geraCartaoAssinatura(1L);
    }

    @Test(expected = SimtrConfiguracaoException.class)
    public void testGeraCartaoAssinaturaExceptionGeracaoMinuta() {
        Mockito.doThrow(SimtrConfiguracaoException.class).when(this.relatorioServico).gerarRelatorioPDFJsonDataSource(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap());

        this.dossieDigitalServico.geraCartaoAssinatura(1L);
    }
    
    @Test
    public void testAtualizaCartaoAssinatura() {
        Mockito.when(this.documentoServico.prototype(
                Mockito.eq(canalSIMTR), Mockito.anyBoolean(), Mockito.eq(tipoDocumentoCartaoAssinatura), Mockito.eq(TemporalidadeDocumentoEnum.VALIDO),
                Mockito.eq(OrigemDocumentoEnum.O), Mockito.any(FormatoConteudoEnum.class), Mockito.anyList(), Mockito.anyString())
        ).thenReturn(this.documentoCartaoAssinatura);

//        Mockito.when(this.siecmServico.armazenaDocumentoPessoalGED(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.any(Documento.class), Mockito.eq(TemporalidadeDocumentoEnum.TEMPORARIO_OCR), Mockito.anyString(), Mockito.anyString())).thenReturn("20879769-0000-C288-82D9-2EB6DE50A892");
        Mockito.when(this.documentoServico.localizaDocumentoClienteMaisRecenteByIdDossie(Mockito.anyLong(), Mockito.eq(tipoDocumentoCartaoAssinatura), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(new Documento());
        
        Mockito.when(this.relatorioServico.gerarRelatorioPDFJsonDataSource(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap())).thenReturn("REPORT_PDF_MOCK_BASE64".getBytes());

        this.dossieDigitalServico.atualizaCartaoAssinatura(1L, FormatoConteudoEnum.PDF, cartaoAssinaturaBase64);
    }
    
    @Test(expected = SimtrRequisicaoException.class)
    public void testAtualizaCartaoAssinaturaExceptionDossieClienteNull() {
        this.dossieDigitalServico.atualizaCartaoAssinatura(2L, FormatoConteudoEnum.PDF, cartaoAssinaturaBase64);
    }
    
    @Test(expected = SimtrRequisicaoException.class)
    public void testAtualizaCartaoAssinaturaExceptionTipoPessoa() {
        this.dossieDigitalServico.atualizaCartaoAssinatura(3L, FormatoConteudoEnum.PDF, cartaoAssinaturaBase64);
    }
    
     @Test(expected = SimtrConfiguracaoException.class)
    public void testAtualizaCartaoAssinaturaExceptionTipologiaNaoDefinida() {
        Mockito.when(this.tipoDocumentoServico.getByTipologia(ConstantesUtil.TIPOLOGIA_CARTAO_ASSINATURA)).thenReturn(null);
        this.dossieDigitalServico.atualizaCartaoAssinatura(1L, FormatoConteudoEnum.PDF, cartaoAssinaturaBase64);
    }
    
    @Test
    public void testExecutaAtualizacaoDocumentoDossieDigital() {
        Mockito.doNothing().when(this._self).atualizaDadosDocumentoDossieDigital(Mockito.eq(dossieCliente00065179609), Mockito.anyLong(), Mockito.anyMap());
        Mockito.doNothing().when(this._self).validaDocumentoDossieDigital(Mockito.eq(dossieCliente00065179609), Mockito.anyLong());
        
        Map<String, String> mapaDadosAtualizacao = new HashMap<>();
        mapaDadosAtualizacao.put("nome", "Fulano de Tal");
        mapaDadosAtualizacao.put("filiacao", "Mae de Fulano de Tal");
        mapaDadosAtualizacao.put("filiacao_pai", "Pai de Fulano de Tal");
        
        this.dossieDigitalServico.executaAtualizacaoDocumentoDossieDigital(1L, 10L, mapaDadosAtualizacao);
    }
    
    @Test(expected = SimtrRequisicaoException.class)
    public void testExecutaAtualizacaoDocumentoDossieDigitalExceptionDossieClienteNull() {
        Mockito.doNothing().when(this._self).atualizaDadosDocumentoDossieDigital(Mockito.eq(dossieCliente00065179609), Mockito.anyLong(), Mockito.anyMap());
        Mockito.doNothing().when(this._self).validaDocumentoDossieDigital(Mockito.eq(dossieCliente00065179609), Mockito.anyLong());
        
        Map<String, String> mapaDadosAtualizacao = new HashMap<>();
        mapaDadosAtualizacao.put("nome", "Fulano de Tal");
        mapaDadosAtualizacao.put("filiacao", "Mae de Fulano de Tal");
        mapaDadosAtualizacao.put("filiacao_pai", "Pai de Fulano de Tal");
        
        this.dossieDigitalServico.executaAtualizacaoDocumentoDossieDigital(2L, 10L, mapaDadosAtualizacao);
    }
    
    @Test
    public void testAtualizaDadosDocumentoDossieDigital() {
        Map<String, String> mapaDadosAtualizacao = new HashMap<>();
        mapaDadosAtualizacao.put("nome", "Fulano de Tal");
        mapaDadosAtualizacao.put("filiacao", "Mae de Fulano de Tal");
        mapaDadosAtualizacao.put("filiacao_pai", "Pai de Fulano de Tal");
        
        Mockito.when(this.documentoServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(this.documentoCNH);
        
        this.dossieDigitalServico.atualizaDadosDocumentoDossieDigital(this.dossieCliente00065179609, 10L, mapaDadosAtualizacao);
    }
    
    @Test
    public void testValidaDocumentoDossieDigital() {
        Map<String, String> mapaDadosAtualizacao = new HashMap<>();
        mapaDadosAtualizacao.put("nome", "Fulano de Tal");
        mapaDadosAtualizacao.put("filiacao", "Mae de Fulano de Tal");
        mapaDadosAtualizacao.put("filiacao_pai", "Pai de Fulano de Tal");
        
        Mockito.when(this.documentoServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(this.documentoCNH);
        Mockito.doReturn(true).when(this.avaliacaoFraudeServico).recuperaResultadoAvaliacaoAutenticidade(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.any(Documento.class));
        Mockito.doReturn(true).when(this.avaliacaoFraudeServico).submeteDocumentoSIFRC(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.any(Documento.class), Mockito.anyBoolean());
        
        this.dossieDigitalServico.validaDocumentoDossieDigital(this.dossieCliente00065179609, 10L);
    }

    @Test
    public void testArmazenaDocumentoCliente() {
        Mockito.doNothing().when(this.documentoServico).invalidarDocumentosAtivosCliente(Mockito.isNull(Long.class), Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.any(TipoDocumento.class), Mockito.anyBoolean());
        Mockito.doNothing().when(this.documentoServico).save(Mockito.any(Documento.class));

        this.dossieDigitalServico.armazenaDocumentoCliente(1L, this.documentoCNH);
    }

    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void testArmazenaDocumentoClienteDossieClientePFInexistente() {
        Mockito.when(this.dossieClienteServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(null);
        this.dossieDigitalServico.armazenaDocumentoCliente(1L, this.documentoCNH);
    }

    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void testArmazenaDocumentoClienteDossieClientePJInexistente() {
        Mockito.when(this.dossieClienteServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(null);
        this.dossieDigitalServico.armazenaDocumentoCliente(1L, this.documentoCNH);
    }

    @Test
    public void testConsultaDadosDeclaradosDossieDigital() {
        Mockito.when(this.documentoServico.localizaDocumentoClienteMaisRecenteByIdDossie(Mockito.anyLong(), Mockito.eq(tipoDocumentoDadosDeclarados), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(this.documentoDadosDeclarados);
        this.dossieDigitalServico.consultaDadosDeclaradosDossieDigital(1L);
    }
    
    @Test
    public void testAtualizaDadosDeclaradosDossieDigital() throws IOException {
        String jsonAtributos = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("atualizacao-dados-declarados.json")), UTF_8);
        List<AtributoDocumentoDTO> atributos = mapper.readValue(jsonAtributos, mapper.getTypeFactory().constructCollectionType(List.class, AtributoDocumentoDTO.class));
        
        Mockito.when(this.documentoServico.prototype(
                Mockito.eq(canalSIMTR), Mockito.anyBoolean(), Mockito.eq(tipoDocumentoDadosDeclarados), Mockito.eq(TemporalidadeDocumentoEnum.VALIDO),
                Mockito.eq(OrigemDocumentoEnum.O), Mockito.any(FormatoConteudoEnum.class), Mockito.anyList(), Mockito.anyString())
        ).thenReturn(this.documentoDadosDeclarados);
        
        Mockito.when(this.documentoServico.localizaDocumentoClienteMaisRecenteByIdDossie(Mockito.anyLong(), Mockito.eq(tipoDocumentoDadosDeclarados), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(new Documento());

        Mockito.when(this.relatorioServico.gerarRelatorioPDF(Mockito.anyString(), Mockito.eq(tipoDocumentoDadosDeclarados), Mockito.anyList(), Mockito.anyMap())).thenReturn("REPORT_PDF_MOCK_BASE64".getBytes());
             
        this.dossieDigitalServico.atualizaDadosDeclaradosDossieDigital(1L, atributos);
    }
    
    ////////////////////////////////////////////////
    //TESTES REALIZADOS NOS METODOS PRIVADOS PARA GARANTIR A TOTAL COBERTURA DA CLASSE
    ///////////////////////////////////////////////
    @Test
    public void testValidaBinario() throws IOException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Method method = this.dossieDigitalServico.getClass().getDeclaredMethod("validaBinario", String.class);
        method.setAccessible(Boolean.TRUE);

        method.invoke(this.dossieDigitalServico, this.cnhBase64);

        try {
            method.invoke(this.dossieDigitalServico, new Object[]{null});
            Assert.assertTrue("A validação de binario com valor nulo deve levantar exceção do tipo SimtrRequisicaoException", Boolean.FALSE);
        } catch (InvocationTargetException ite) {
            if (SimtrRequisicaoException.class.equals(ite.getCause().getClass())) {
                Assert.assertTrue(Boolean.TRUE);
            } else {
                Assert.assertTrue("A validação de binario com valor nulo deve levantar exceção do tipo SimtrRequisicaoException", Boolean.FALSE);
            }
        }

        try {
            method.invoke(this.dossieDigitalServico, "Base64Invalido");
            Assert.assertTrue("A validação de binario com valor inválido deve levantar exceção do tipo SimtrRequisicaoException", Boolean.FALSE);
        } catch (InvocationTargetException ite) {
            if (SimtrRequisicaoException.class.equals(ite.getCause().getClass())) {
                Assert.assertTrue(Boolean.TRUE);
            } else {
                Assert.assertTrue("A validação de binario com valor nulo deve levantar exceção do tipo SimtrRequisicaoException", Boolean.FALSE);
            }
        }
    }
}
