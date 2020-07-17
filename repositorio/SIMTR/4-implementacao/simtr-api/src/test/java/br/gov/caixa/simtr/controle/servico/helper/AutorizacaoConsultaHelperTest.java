package br.gov.caixa.simtr.controle.servico.helper;

import br.gov.caixa.simtr.controle.servico.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import javax.persistence.NoResultException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.AtributosDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.DocumentoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMRespostaDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.RespostaGravaDocumentoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.ged.ClasseBaseECM;
import br.gov.caixa.pedesgo.arquitetura.servico.ServicoGEDConstantes;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.SipesService;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.CadinType;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SerasaType;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SiccfType;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SicowType;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SinadType;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SipesRequestDTO;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SipesResponseDTO;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SpcType;
import br.gov.caixa.pedesgo.arquitetura.siiso.dto.RetornoPessoasFisicasDTO;
import br.gov.caixa.simtr.controle.excecao.DossieAutorizacaoException;
import br.gov.caixa.simtr.controle.excecao.SicpfException;
import br.gov.caixa.simtr.controle.excecao.SiecmException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.Autorizacao;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.ComportamentoPesquisa;
import br.gov.caixa.simtr.modelo.entidade.ComposicaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.DossieClientePF;
import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.entidade.RegraDocumental;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.SistemaPesquisaEnum;
import br.gov.caixa.simtr.modelo.enumerator.SistemaPesquisaTipoRetornoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;

public class AutorizacaoConsultaHelperTest {

    @InjectMocks
    private AutorizacaoConsultaHelper autorizacaoConsultasHelper;

    @Mock
    private CadastroReceitaServico cadastroReceitaServico;

    @Mock
    private ComportamentoPesquisaServico comportamentoPesquisaServico;

    @Mock
    private SipesService sipesService;

    private Canal canalSIMTR;
    private ComportamentoPesquisa comportamentoPesquisaReceitaRegular;
    private ComportamentoPesquisa comportamentoPesquisaReceitaSuspenso;
    private Produto produto001, produto110;
    private RetornoPessoasFisicasDTO retorno00065179609, retorno23162134480;
    private final ThreadLocal orientacoes = new ThreadLocal<>();
    
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_DIR = "/mock/autorizacao/";
    private static final String UTF_8 = "UTF-8";
    
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        orientacoes.set(new ArrayList<>());

        this.canalSIMTR = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("canal-simtr.json")), UTF_8), Canal.class);
        this.comportamentoPesquisaReceitaRegular = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("comportamento-pesquisa-receita-regular.json")), UTF_8), ComportamentoPesquisa.class);
        this.comportamentoPesquisaReceitaSuspenso = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("comportamento-pesquisa-receita-suspenso.json")), UTF_8), ComportamentoPesquisa.class);
        this.produto001 = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("produto-001.json")), UTF_8), Produto.class);
        this.produto110 = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("produto-110.json")), UTF_8), Produto.class);
        this.retorno00065179609 = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("00065179609.json")), UTF_8), RetornoPessoasFisicasDTO.class);
        this.retorno23162134480 = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("23162134480.json")), UTF_8), RetornoPessoasFisicasDTO.class);

        Mockito.when(this.cadastroReceitaServico.consultaCadastroPF(65179609L)).thenReturn(retorno00065179609);
        Mockito.when(this.cadastroReceitaServico.consultaCadastroPF(23162134480L)).thenReturn(retorno23162134480);
    }

    @Test(expected = SicpfException.class)
    public void realizaConsultaReceitaFederalTestNaoLocalizado() {
        Mockito.when(this.cadastroReceitaServico.consultaCadastroPF(30030030030L)).thenReturn(null);
        this.autorizacaoConsultasHelper.realizaConsultaReceitaFederal(30030030030L, produto001, canalSIMTR, orientacoes);
    }
    
    @Test(expected = DossieAutorizacaoException.class)
    public void realizaConsultaReceitaFederalTestBloqueio() {       
        Mockito.when(this.comportamentoPesquisaServico.getBySistemaAndCodigoTipoPesquisa(produto001, SistemaPesquisaEnum.SICPF, 2, null)).thenReturn(comportamentoPesquisaReceitaSuspenso);
        this.autorizacaoConsultasHelper.realizaConsultaReceitaFederal(23162134480L, produto001, canalSIMTR, orientacoes);
    }
    
    @Test
    public void realizaConsultaReceitaFederalTest() {
        //Produto não espera pesquisa na Receita
        this.autorizacaoConsultasHelper.realizaConsultaReceitaFederal(65179609L, produto110, canalSIMTR, orientacoes);
        
        //Produto não espera pesquisa na Receita mas simulação de comportamentpo de pesquisa não mapeado
        Mockito.when(this.comportamentoPesquisaServico.getBySistemaAndCodigoTipoPesquisa(produto001, SistemaPesquisaEnum.SICPF, 0, null)).thenReturn(null);
        this.autorizacaoConsultasHelper.realizaConsultaReceitaFederal(65179609L, produto001, canalSIMTR, orientacoes);
        
        //Produto não espera pesquisa na Receita e comportamentpo de pesquisa localizado
        Mockito.when(this.comportamentoPesquisaServico.getBySistemaAndCodigoTipoPesquisa(produto001, SistemaPesquisaEnum.SICPF, 0, null)).thenReturn(comportamentoPesquisaReceitaRegular);
        this.autorizacaoConsultasHelper.realizaConsultaReceitaFederal(65179609L, produto001, canalSIMTR, orientacoes);
    }
}
