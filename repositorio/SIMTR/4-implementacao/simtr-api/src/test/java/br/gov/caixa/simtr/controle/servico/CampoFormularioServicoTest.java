/**
 * Copyright (c) 2018 Caixa Econômica Federal. Todos os direitos
 * reservados.
 *
 * Caixa Econômica Federal - SIACG
 *
 * Este software foi desenvolvido sob demanda da CAIXA e está
 * protegido por leis de direitos autorais e tratados internacionais. As
 * condições de cópia e utilização total ou partes dependem de autorização da
 * empresa. Cópias não são permitidas sem expressa autorização. Não pode ser
 * comercializado ou utilizado para propósitos particulares.
 *
 * Uso exclusivo da Caixa Econômica Federal. A reprodução ou distribuição não
 * autorizada deste programa ou de parte dele, resultará em punições civis e
 * criminais e os infratores incorrem em sanções previstas na legislação em
 * vigor.
 *
 * Histórico do TFS:
 *
 * LastChangedRevision: $Revision$
 * LastChangedBy: $Author$
 * LastChangedDate: $Date$
 *
 * HeadURL: $HeadURL$
 *
 */
package br.gov.caixa.simtr.controle.servico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.servico.helper.CampoFormularioServicoHelper;
import br.gov.caixa.simtr.controle.vo.campoformulario.CamposEntradaValidadoVO;
import br.gov.caixa.simtr.modelo.entidade.CampoApresentacao;
import br.gov.caixa.simtr.modelo.entidade.CampoEntrada;
import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import br.gov.caixa.simtr.modelo.entidade.Garantia;
import br.gov.caixa.simtr.modelo.entidade.OpcaoCampo;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario.AlteracaoCadastroDefinicaoCampoFormularioDTO;

public class CampoFormularioServicoTest {

    @InjectMocks
    private final CampoFormularioServico campoFormularioServico = new CampoFormularioServico();

    @Mock
    private EntityManager manager;
    
    @Mock
    private CampoEntradaServico campoEntradaServico = new CampoEntradaServico();
    
    @Mock
    private OpcaoCampoServico opcaoCampoServico = new OpcaoCampoServico();
    
    @Mock
    private CampoApresentacaoServico campoApresentacaoServico = new CampoApresentacaoServico();
    
    @Mock
    private CampoFormularioServicoHelper campoFormularioServicoHelper;

    @Mock
    TypedQuery<CampoFormulario> queryCampoFormulario;
    
    @Mock
    TypedQuery<Long> queryIdentificadorBPM;

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_DIR = "/mock/campo-formulario/";
    private static final String UTF_8 = "UTF-8";

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        CampoFormulario campoRespostaAberta = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("campo-formulario-01-aberta.json")), UTF_8), CampoFormulario.class);
        CampoFormulario campoRespostaObjetiva = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("campo-formulario-02-objetiva.json")), UTF_8), CampoFormulario.class);

        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(CampoFormulario.class))).thenReturn(this.queryCampoFormulario);

        Mockito.when(this.queryCampoFormulario.getResultList()).thenReturn(Arrays.asList(campoRespostaAberta, campoRespostaObjetiva));
        Mockito.when(this.queryCampoFormulario.getSingleResult()).thenReturn(campoRespostaAberta);
    }

    @Test
    public void getByIdTeste() {

        this.campoFormularioServico.getById(1L);

        this.campoFormularioServico.getById(1L, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        this.campoFormularioServico.getById(1L, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE);
        this.campoFormularioServico.getById(1L, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
        this.campoFormularioServico.getById(1L, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);

    }

    @Test
    public void getByIdTesteCasoCampoNaoEncontrado() {

        Mockito.doThrow(NoResultException.class).when(this.queryCampoFormulario).getSingleResult();
        CampoFormulario campoFormulario = this.campoFormularioServico.getById(1L, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
        Assert.assertNull(campoFormulario);

    }
    
    private List<CampoFormulario> criaMockListaCampoFormulario() {
        List<CampoFormulario> lista = new ArrayList<>();
        CampoFormulario campoFormulario = new CampoFormulario();
        campoFormulario.setId(1L);
        campoFormulario.setAtivo(Boolean.TRUE);
        campoFormulario.setOrdemApresentacao(1);
        campoFormulario.setExpressaoInterface("fadfadf");
        campoFormulario.setIdentificadorBPM(1);
        campoFormulario.setNomeCampo("fasdfadf");
        campoFormulario.setObrigatorio(Boolean.FALSE);
        CampoEntrada campoEntrada = new CampoEntrada();
        campoEntrada.setId(1L);
        campoEntrada.setLabel("fasdfasdf");
        campoFormulario.setCampoEntrada(campoEntrada);
        OpcaoCampo opcaoCampo = new OpcaoCampo();
        opcaoCampo.setId(1L);
        opcaoCampo.setNome("asdfadf");
        Set<OpcaoCampo> opcoesCampo = new HashSet<>();
        opcoesCampo.add(opcaoCampo);
        campoEntrada.setOpcoesCampo(opcoesCampo);
        CampoApresentacao campoApresentacao = new CampoApresentacao();
        campoApresentacao.setId(1L);
        campoApresentacao.setLargura(2);
        campoFormulario.addCamposApresentacao(campoApresentacao);
        lista.add(campoFormulario);
        return lista;
    }
    
    private CampoFormulario criaMockCampoFormulario() {
        CampoFormulario campoFormulario = new CampoFormulario();
        campoFormulario.setId(1L);
        campoFormulario.setAtivo(Boolean.TRUE);
        campoFormulario.setOrdemApresentacao(1);
        campoFormulario.setExpressaoInterface("fadfadf");
        campoFormulario.setIdentificadorBPM(1);
        campoFormulario.setNomeCampo("fasdfadf");
        campoFormulario.setObrigatorio(Boolean.FALSE);
        Processo processo = new Processo();
        processo.setId(1);
        campoFormulario.setProcesso(processo);
        CampoEntrada campoEntrada = new CampoEntrada();
        campoEntrada.setId(1L);
        campoEntrada.setLabel("fasdfasdf");
        campoFormulario.setCampoEntrada(campoEntrada);
        OpcaoCampo opcaoCampo = new OpcaoCampo();
        opcaoCampo.setId(1L);
        opcaoCampo.setNome("asdfadf");
        Set<OpcaoCampo> opcoesCampo = new HashSet<>();
        opcoesCampo.add(opcaoCampo);
        campoEntrada.setOpcoesCampo(opcoesCampo);
        CampoApresentacao campoApresentacao = new CampoApresentacao();
        campoApresentacao.setId(1L);
        campoApresentacao.setLargura(2);
        campoFormulario.addCamposApresentacao(campoApresentacao);
        return campoFormulario;
    }
    
    @Test
    public void testConsultaFormularioPorOrigemProcessoFase() {
        Integer idProcessoOrigem = 1; 
        Integer idProcessoFase = 2; 
        Integer idTipoRelacionamento = null; 
        Integer idProduto = null;
        Integer idGarantia = null;
        
        List<CampoFormulario> lista = this.criaMockListaCampoFormulario();
        Mockito.when(this.queryCampoFormulario.getResultList()).thenReturn(lista);
        
        Processo processoOrigem = new Processo();
        processoOrigem.setId(1);
        Processo processoFase = new Processo();
        processoFase.setId(1);
        CamposEntradaValidadoVO camposEntradaValidadoVO = new CamposEntradaValidadoVO(processoOrigem, processoFase, null, null, null);
        Mockito.when(this.campoFormularioServicoHelper.validaExistenciaValoresEntradaBaseDados(idProcessoOrigem, idProcessoFase, idTipoRelacionamento, idProduto, idGarantia)).thenReturn(camposEntradaValidadoVO);
        
        Assert.assertNotNull(this.campoFormularioServico.consultaFormularioPorProcessoOrigem(idProcessoOrigem, idProcessoFase, idTipoRelacionamento, idProduto, idGarantia));
    }
    
    @Test
    public void testConsultaFormularioPorOrigemTipoRelacionamento() {
        Integer idProcessoOrigem = 1; 
        Integer idProcessoFase = null; 
        Integer idTipoRelacionamento = 3; 
        Integer idProduto = null;
        Integer idGarantia = null;
        
        List<CampoFormulario> lista = this.criaMockListaCampoFormulario();
        Mockito.when(this.queryCampoFormulario.getResultList()).thenReturn(lista);
        
        Processo processoOrigem = new Processo();
        processoOrigem.setId(1);
        TipoRelacionamento tipoRelacionamento = new TipoRelacionamento();
        tipoRelacionamento.setId(2);
        
        CamposEntradaValidadoVO camposEntradaValidadoVO = new CamposEntradaValidadoVO(processoOrigem, null, tipoRelacionamento, null, null);
        Mockito.when(this.campoFormularioServicoHelper.validaExistenciaValoresEntradaBaseDados(idProcessoOrigem, idProcessoFase, idTipoRelacionamento, idProduto, idGarantia)).thenReturn(camposEntradaValidadoVO);
        
        Assert.assertNotNull(this.campoFormularioServico.consultaFormularioPorProcessoOrigem(idProcessoOrigem, idProcessoFase, idTipoRelacionamento, idProduto, idGarantia));
    }
    
    @Test
    public void testConsultaFormularioPorOrigemProduto() {
        Integer idProcessoOrigem = 1; 
        Integer idProcessoFase = null; 
        Integer idTipoRelacionamento = null; 
        Integer idProduto = 4;
        Integer idGarantia = null;
        
        List<CampoFormulario> lista = this.criaMockListaCampoFormulario();
        Mockito.when(this.queryCampoFormulario.getResultList()).thenReturn(lista);
        
        Processo processoOrigem = new Processo();
        processoOrigem.setId(1);
        Produto produto = new Produto();
        produto.setId(2);
        
        CamposEntradaValidadoVO camposEntradaValidadoVO = new CamposEntradaValidadoVO(processoOrigem, null, null, produto, null);
        Mockito.when(this.campoFormularioServicoHelper.validaExistenciaValoresEntradaBaseDados(idProcessoOrigem, idProcessoFase, idTipoRelacionamento, idProduto, idGarantia)).thenReturn(camposEntradaValidadoVO);
        
        Assert.assertNotNull(this.campoFormularioServico.consultaFormularioPorProcessoOrigem(idProcessoOrigem, idProcessoFase, idTipoRelacionamento, idProduto, idGarantia));
    }
    
    @Test
    public void testConsultaFormularioPorOrigemGarantia() {
        Integer idProcessoOrigem = 1; 
        Integer idProcessoFase = null; 
        Integer idTipoRelacionamento = null; 
        Integer idProduto = null;
        Integer idGarantia = 5;
        
        List<CampoFormulario> lista = this.criaMockListaCampoFormulario();
        Mockito.when(this.queryCampoFormulario.getResultList()).thenReturn(lista);
        
        Processo processoOrigem = new Processo();
        processoOrigem.setId(1);
        Garantia garantia = new Garantia();
        garantia.setId(2);
        
        CamposEntradaValidadoVO camposEntradaValidadoVO = new CamposEntradaValidadoVO(processoOrigem, null, null, null, garantia);
        Mockito.when(this.campoFormularioServicoHelper.validaExistenciaValoresEntradaBaseDados(idProcessoOrigem, idProcessoFase, idTipoRelacionamento, idProduto, idGarantia)).thenReturn(camposEntradaValidadoVO);
        
        Assert.assertNotNull(this.campoFormularioServico.consultaFormularioPorProcessoOrigem(idProcessoOrigem, idProcessoFase, idTipoRelacionamento, idProduto, idGarantia));
    }
    
    @Test
    public void testCadastraCampoFormulario() {
        Integer idProcessoOrigem = 1; 
        Integer idProcessoFase = 2; 
        Integer idTipoRelacionamento = null; 
        Integer idProduto = null;
        Integer idGarantia = null;
        
        List<CampoFormulario> lista = this.criaMockListaCampoFormulario();
        Mockito.when(this.queryCampoFormulario.getResultList()).thenReturn(lista);
        
        CampoFormulario campoFormulario = this.criaMockCampoFormulario();
        
        Processo processoOrigem = new Processo();
        processoOrigem.setId(1);
        Processo processoFase = new Processo();
        processoFase.setId(1);
        CamposEntradaValidadoVO camposEntradaValidadoVO = new CamposEntradaValidadoVO(processoOrigem, processoFase, null, null, null);
        Mockito.when(this.campoFormularioServicoHelper.validaExistenciaValoresEntradaBaseDados(idProcessoOrigem, idProcessoFase, idTipoRelacionamento, idProduto, idGarantia)).thenReturn(camposEntradaValidadoVO);
        
        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(Long.class))).thenReturn(this.queryIdentificadorBPM);
        Mockito.when(this.queryIdentificadorBPM.getSingleResult()).thenReturn(0L);
        
        Mockito.doNothing().when(this.campoEntradaServico).validaInclusaoCampoEntrada(campoFormulario.getCampoEntrada());
        this.campoFormularioServico.cadastraFormularioPorProcessoOrigem(campoFormulario, idProcessoOrigem, idProcessoFase, idTipoRelacionamento, idProduto, idGarantia);
    }
    
    @Test
    public void testAtualizaCampoFormulario() {
        AlteracaoCadastroDefinicaoCampoFormularioDTO alteracaoCadastroCampoFormularioDTO = new AlteracaoCadastroDefinicaoCampoFormularioDTO();
        alteracaoCadastroCampoFormularioDTO.setAtivo(Boolean.TRUE);
        alteracaoCadastroCampoFormularioDTO.setOrdem(1);
        alteracaoCadastroCampoFormularioDTO.setExpressaoInterface("fadfadf");
        alteracaoCadastroCampoFormularioDTO.setIdentificadorBpm(1);
        alteracaoCadastroCampoFormularioDTO.setNome("fasdfadf");
        alteracaoCadastroCampoFormularioDTO.setObrigatorio(Boolean.FALSE);
        alteracaoCadastroCampoFormularioDTO.setLabel("fasdfasdf");
    
        CampoFormulario campoFormulario = this.criaMockCampoFormulario();
        
        Processo processoOrigem = new Processo();
        processoOrigem.setId(1);
        Processo processoFase = new Processo();
        processoFase.setId(1);
        
        Mockito.when(this.campoFormularioServicoHelper.existeRespostasDossieCampoFormularioAtual(campoFormulario)).thenReturn(Boolean.TRUE);
        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(CampoFormulario.class))).thenReturn(this.queryCampoFormulario);
        Mockito.when(this.queryCampoFormulario.getSingleResult()).thenReturn(campoFormulario);
        
        Mockito.doNothing().when(this.campoFormularioServicoHelper).validaIntegridadeValoresEntrada(alteracaoCadastroCampoFormularioDTO.getIdentificadorProcessoFase(),
                alteracaoCadastroCampoFormularioDTO.getIdentificadorTipoRelacionamento(),
                alteracaoCadastroCampoFormularioDTO.getIdentificadorProduto(),
                alteracaoCadastroCampoFormularioDTO.getIdentificadorGarantia());

        CamposEntradaValidadoVO camposEntradaValidadoVO = new CamposEntradaValidadoVO(processoOrigem, processoFase, null, null, null);
        Mockito.when(this.campoFormularioServicoHelper.validaExistenciaValoresEntradaBaseDados(alteracaoCadastroCampoFormularioDTO.getIdentificadorProcessoDossie(),
                                                        alteracaoCadastroCampoFormularioDTO.getIdentificadorProcessoFase(),
                                                        alteracaoCadastroCampoFormularioDTO.getIdentificadorTipoRelacionamento(),
                                                        alteracaoCadastroCampoFormularioDTO.getIdentificadorProduto(),
                                                        alteracaoCadastroCampoFormularioDTO.getIdentificadorGarantia())).thenReturn(camposEntradaValidadoVO);
        
        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(Long.class))).thenReturn(this.queryIdentificadorBPM);
        Mockito.when(this.queryIdentificadorBPM.getSingleResult()).thenReturn(0L);
        
        Mockito.doNothing().when(this.campoEntradaServico).validaAlteracaoCampoEntrada(campoFormulario.getCampoEntrada(), alteracaoCadastroCampoFormularioDTO);
        this.campoFormularioServico.alteraCampoFormulario(campoFormulario.getId(), alteracaoCadastroCampoFormularioDTO);
    }
    
    @Test
    public void testExclusaoLogicaCampoFormulario() {
        CampoFormulario campoFormulario = this.criaMockCampoFormulario();
        
        Mockito.when(this.campoFormularioServicoHelper.existeRespostasDossieCampoFormularioAtual(campoFormulario)).thenReturn(Boolean.TRUE);
        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(CampoFormulario.class))).thenReturn(this.queryCampoFormulario);
        Mockito.when(this.queryCampoFormulario.getSingleResult()).thenReturn(campoFormulario);
        
        this.campoFormularioServico.excluiCampoFormulario(campoFormulario.getId());
    }
    
    @Test
    public void testExclusaoFisicaCampoFormulario() {
        CampoFormulario campoFormulario = this.criaMockCampoFormulario();
        
        Mockito.when(this.campoFormularioServicoHelper.existeRespostasDossieCampoFormularioAtual(campoFormulario)).thenReturn(Boolean.FALSE);
        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(CampoFormulario.class))).thenReturn(this.queryCampoFormulario);
        Mockito.when(this.queryCampoFormulario.getSingleResult()).thenReturn(campoFormulario);
        
        this.campoFormularioServico.excluiCampoFormulario(campoFormulario.getId());
    }
    
    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void testExcecaoExclusaoCampoFormulario() {
        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(CampoFormulario.class))).thenReturn(this.queryCampoFormulario);
        Mockito.when(this.queryCampoFormulario.getSingleResult()).thenReturn(null);
        
        this.campoFormularioServico.excluiCampoFormulario(1L);
    }
}
