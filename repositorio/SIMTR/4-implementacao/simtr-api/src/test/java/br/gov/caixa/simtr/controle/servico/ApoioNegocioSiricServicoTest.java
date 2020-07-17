package br.gov.caixa.simtr.controle.servico;

import java.io.IOException;
import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.caixa.pedesgo.arquitetura.siric.dto.propostas.PropostaOperacaoReqDTO;
import br.gov.caixa.pedesgo.arquitetura.siric.dto.propostas.PropostaOperacaoRespDTO;
import br.gov.caixa.pedesgo.arquitetura.siric.dto.propostas.PropostaTomadorReqDTO;
import br.gov.caixa.pedesgo.arquitetura.siric.dto.propostas.PropostaTomadorRespDTO;
import br.gov.caixa.pedesgo.arquitetura.siric.enumerador.TipoSiricEnum;
import br.gov.caixa.pedesgo.arquitetura.siric.exceptions.SiricException;
import br.gov.caixa.pedesgo.arquitetura.siric.servico.SiricServico;
import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.RespostaDossie;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDossie;
import br.gov.caixa.simtr.modelo.entidade.TipoSituacaoDossie;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDossieEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric.AvaliacaoOperacaoSimplesPJDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric.AvaliacaoTomadorPJDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric.PropostaOperacaoRespostaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric.PropostaTomadorRespostaDTO;

/**
 * 
 * @author f525904
 *
 */

public class ApoioNegocioSiricServicoTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<DossieProduto> queryDossieProduto;
    
    @Mock
    private TypedQuery<CampoFormulario> queryCampoFormulario;

    @InjectMocks
    private ApoioNegocioSiricServico servico = new ApoioNegocioSiricServico();

    @Mock
    private CampoFormularioServico campoFormularioServico = new CampoFormularioServico();

    @Mock
    private DossieProdutoServico dossieProdutoServico = new DossieProdutoServico();

    @Mock
    private BPMServico bmpServico = new BPMServico();

    @Mock
    private CanalServico canalServico;

    @Mock
    private SiricServico siricServicoWebService;

    private AvaliacaoTomadorPJDTO avaliacao;
    
    private AvaliacaoOperacaoSimplesPJDTO avaliacaoOperacao;

    private PropostaTomadorRespDTO respostaPropostaSiricDTO;

    private PropostaTomadorRespostaDTO propostaTomadorRespostaDTO;
    
    private PropostaOperacaoRespDTO respostaOperacaoRespDTO;
    
    private PropostaOperacaoRespostaDTO propostaOperacaoRespostaDTO;

    private Canal canalSimtr;

    private static final String RESOURCE_DIR = "/mock/apoio-negocio-siric/";
    private static final String UTF_8 = "UTF-8";
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String PROTOCOLO_TOMADOR = "protocolo_resposta_avaliacao_tomador";
    private static final String PROTOCOLO_OPERACAO = "protocolo_resposta_avaliacao_operacao";
    
    private static final Integer NU_IDENTIFICADOR_BPM_SIRIC_AVALICAO_TOMADOR = 99990001;
    
    private static final Integer NU_IDENTIFICADOR_BPM_SIRIC_AVALICAO_OPERACAO = 99990003;

    @Before
    public void setup() throws IOException, JAXBException {
        MockitoAnnotations.initMocks(this);

        String jsonValido = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("avaliacao-tomador-pj.json")), UTF_8);
        this.avaliacao = mapper.readValue(jsonValido, AvaliacaoTomadorPJDTO.class);
        
        String jsonAvaliacaoOperacao = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("avaliacao-operacao-pj.json")), UTF_8);
        this.avaliacaoOperacao = mapper.readValue(jsonAvaliacaoOperacao, AvaliacaoOperacaoSimplesPJDTO.class);

        String jsonCanalSimtr = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("canal-simtr.json")), UTF_8);
        this.canalSimtr = mapper.readValue(jsonCanalSimtr, Canal.class);

        String jsonResposta = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("resposta-proposta-siric.json")), UTF_8);
        this.respostaPropostaSiricDTO = mapper.readValue(jsonResposta, PropostaTomadorRespDTO.class);
        
        String jsonRespostaAvalicao = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("resposta-proposta-operacao-siric.json")), UTF_8);
        this.respostaOperacaoRespDTO = mapper.readValue(jsonRespostaAvalicao, PropostaOperacaoRespDTO.class);

        String jsonRespostaCallback = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("resposta-proposta-siric.json")), UTF_8);
        this.propostaTomadorRespostaDTO = mapper.readValue(jsonRespostaCallback, PropostaTomadorRespostaDTO.class);
        
        String jsonRespostaOperacaoCallback = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("resposta-proposta-operacao-siric.json")), UTF_8);
        this.propostaOperacaoRespostaDTO = mapper.readValue(jsonRespostaOperacaoCallback, PropostaOperacaoRespostaDTO.class);

        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(this.canalSimtr);
    }

    @Test
    public void solicitarAvaliacaoTomadorPJTest() throws SiricException {
        PropostaTomadorReqDTO requisicao = this.servico.montarObjetoRequisicaoPropostaTomador(this.avaliacao);

        Mockito.when(this.siricServicoWebService.realizarProposta(requisicao, TipoSiricEnum.TOMADOR)).thenReturn(this.respostaPropostaSiricDTO);

        this.servico.solicitarAvaliacaoTomadorPJ(this.avaliacao);
    }

    @Test
    public void processarPropostaCallbackTest() throws SiricException {
        Mockito.when(this.campoFormularioServico.retornaRespostaPorProtocoloSiric(PROTOCOLO_TOMADOR)).thenReturn(this.mockRespostaDossie(PROTOCOLO_TOMADOR));

        Mockito.when(this.campoFormularioServico.retornaCampoFormularioPorProcessoGeradorEProcessoFaseEIdentificadorBPM(1, 1, NU_IDENTIFICADOR_BPM_SIRIC_AVALICAO_TOMADOR)).thenReturn(this.mockCampoFormulario());

        DossieProduto dossieProd = this.mockDossieProduto();

        Mockito.when(this.dossieProdutoServico.getById(dossieProd.getId())).thenReturn(dossieProd);

        this.dossieProdutoServico.adicionaSituacaoDossieProduto(mokGetByGedId(dossieProd), SituacaoDossieEnum.ALIMENTACAO_FINALIZADA, null);

        this.servico.processarPropostaCallback(this.propostaTomadorRespostaDTO);
    }
    
    @Test
    public void solicitarAvaliacaoOperacaoPJTest() throws SiricException {
    	PropostaOperacaoReqDTO requisicao = this.servico.montarObjetoRequisicaoPropostaOperacaoSimples(this.avaliacaoOperacao);

        Mockito.when(this.siricServicoWebService.realizarProposta(requisicao, this.avaliacaoOperacao.getCnpj(), TipoSiricEnum.OPERACAO)).thenReturn(this.respostaOperacaoRespDTO);

        this.servico.solicitarAvaliacaoOperacaoSimplesPJ(this.avaliacaoOperacao);
    }
    
    @Test
    public void processarPropostaOperacaoCallbackTest() throws SiricException {
        Mockito.when(this.campoFormularioServico.retornaRespostaPorProtocoloSiric(PROTOCOLO_OPERACAO)).thenReturn(this.mockRespostaDossie(PROTOCOLO_OPERACAO));

        Mockito.when(this.campoFormularioServico.retornaCampoFormularioPorProcessoGeradorEProcessoFaseEIdentificadorBPM(1, 1, NU_IDENTIFICADOR_BPM_SIRIC_AVALICAO_OPERACAO)).thenReturn(this.mockCampoFormulario());

        DossieProduto dossieProd = this.mockDossieProduto();

        Mockito.when(this.dossieProdutoServico.getById(dossieProd.getId())).thenReturn(dossieProd);

        this.dossieProdutoServico.adicionaSituacaoDossieProduto(mokGetByGedId(dossieProd), SituacaoDossieEnum.ALIMENTACAO_FINALIZADA, null);

        this.servico.processarPropostaCallbackOperacao(this.propostaOperacaoRespostaDTO);
    }

    private RespostaDossie mockRespostaDossie(String protocolo) {
        RespostaDossie resposta = new RespostaDossie();
        resposta.setId(1L);
        resposta.setRespostaAberta(protocolo);

        CampoFormulario campo = new CampoFormulario();
        campo.setId(1L);

        Processo processo = new Processo();
        processo.setId(1);

        Processo processoFase = new Processo();
        processoFase.setId(1);

        DossieProduto dossie = new DossieProduto();
        dossie.setId(1L);

        campo.setProcesso(processo);
        campo.setProcessoFase(processoFase);

        resposta.setCampoFormulario(campo);
        resposta.setDossieProduto(dossie);

        return resposta;
    }

    private CampoFormulario mockCampoFormulario() {
        CampoFormulario campo = new CampoFormulario();
        campo.setId(2L);
        return campo;
    }

    private DossieProduto mockDossieProduto() {
        DossieProduto dossieProd = new DossieProduto();
        dossieProd.setId(1L);
        SituacaoDossie situacao = new SituacaoDossie();
        situacao.setId(9L);
        situacao.setDataHoraInclusao(Calendar.getInstance());
        TipoSituacaoDossie tipo = new TipoSituacaoDossie();
        tipo.setTipoFinal(false);
        situacao.setTipoSituacaoDossie(tipo);
        dossieProd.getSituacoesDossie().add(situacao);
        Mockito.when(queryDossieProduto.getSingleResult()).thenReturn(dossieProd);

        return dossieProd;
    }

    private Long mokGetByGedId(final DossieProduto dossieProd) {
        Long id = dossieProd.getId();
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(DossieProduto.class))).thenReturn(queryDossieProduto);
        return id;
    }
}
