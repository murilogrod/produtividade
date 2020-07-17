package br.gov.caixa.simtr.controle.servico;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.caixa.simtr.controle.servico.helper.PortalEmpreendedorHelper;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.ComprovanteResidenciaVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.ConjugeVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.ContaVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.DadosComplementaresSolicitanteVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.DeclaracaoFaturamentoVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.DocumentoConstituicaoEmpresaVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.DocumentoIdentidadeVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.NecessidadeVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.PessoaJuridicaVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.PortalEmpreendedorVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.QuadroSocietarioVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.ResponsavelVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.validacao.ParametrosDossieProdutoMeiVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.validacao.TiposDocumentoValidadoVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.validacao.TiposRelacionamentoValidadoVO;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaPortalEmpreendedorEnum;

public class PortalEmpreendedorServicoTest {

    @Mock
    private EntityManager manager;
    
    @Mock
    private ProcessoServico processoServico;

    @Mock
    private DossieClienteServico dossieClienteServico;

    @Mock
    private ProdutoServico produtoServico;

    @Mock
    private TipoRelacionamentoServico tipoRelacionamentoServico;

    @Mock
    private TipoDocumentoServico tipoDocumentoServico;

    @Mock
    private PortalEmpreendedorHelper portalEmpreendedorHelper;
    
    @InjectMocks
    private PortalEmpreendedorServico portalEmpreendedorServico;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Ignore
    @Test
    public void testCriacaoDossieProdutoMEI() {
        PortalEmpreendedorVO portalEmpreendedorVO = new PortalEmpreendedorVO();
        PessoaJuridicaVO pessoaJuridicaVO = new PessoaJuridicaVO();
        pessoaJuridicaVO.setCnpj("98765432198761");
        portalEmpreendedorVO.setPessoaJuridica(pessoaJuridicaVO);
        
        Long cnpj = Long.parseLong(portalEmpreendedorVO.getPessoaJuridica().getCnpj());
        DossieCliente dossieClientePJ = new DossieCliente();
        dossieClientePJ.setId(2L);
        Mockito.when(this.dossieClienteServico.getByCpfCnpj(cnpj, TipoPessoaEnum.J, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE)).thenReturn(dossieClientePJ);
        
        ResponsavelVO responsavelVO = new ResponsavelVO();
        responsavelVO.setNi("123456789");
        pessoaJuridicaVO.setResponsavel(responsavelVO);
        
        Long cpfResponsavelLegal = Long.parseLong(portalEmpreendedorVO.getPessoaJuridica().getResponsavel().getNi());
        DossieCliente dossieClienteResponsavelLegal = new DossieCliente();
        dossieClienteResponsavelLegal.setId(1L);
        Mockito.when(this.dossieClienteServico.getByCpfCnpj(cpfResponsavelLegal, TipoPessoaEnum.F, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE)).thenReturn(dossieClienteResponsavelLegal);
        
        DadosComplementaresSolicitanteVO dadosComplementaresSolicitanteVO = new DadosComplementaresSolicitanteVO();
        ConjugeVO conjugeVO = new ConjugeVO();
        conjugeVO.setCpf("2412341234");
        dadosComplementaresSolicitanteVO.setConjuge(conjugeVO);
        portalEmpreendedorVO.setDadosComplementaresSolicitante(dadosComplementaresSolicitanteVO);
        
        Long cpfConjuge = Long.parseLong(dadosComplementaresSolicitanteVO.getConjuge().getCpf());
        DossieCliente dossieClienteConjuge = new DossieCliente();
        dossieClienteConjuge.setId(1L);
        Mockito.when(this.dossieClienteServico.getByCpfCnpj(cpfConjuge, TipoPessoaEnum.F, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE)).thenReturn(dossieClienteConjuge);
        
        List<QuadroSocietarioVO> listaQuadroSocietario = new ArrayList<QuadroSocietarioVO>();
        QuadroSocietarioVO quadroSocietario = new QuadroSocietarioVO();
        quadroSocietario.setTipo(TipoPessoaPortalEmpreendedorEnum.F.getDescricao());
        quadroSocietario.setNi("23234534553");
        Long cpfSocio = Long.parseLong(quadroSocietario.getNi());
        DossieCliente dossieClienteSocio = new DossieCliente();
        dossieClienteSocio.setId(1L);
        Mockito.when(this.dossieClienteServico.getByCpfCnpj(cpfSocio, TipoPessoaEnum.F, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE)).thenReturn(dossieClienteSocio);
        listaQuadroSocietario.add(quadroSocietario);
        
        QuadroSocietarioVO quadroSocietario2 = new QuadroSocietarioVO();
        quadroSocietario2.setTipo(TipoPessoaPortalEmpreendedorEnum.J.getDescricao());
        quadroSocietario2.setNi("34234534553423");
        Long cnpjSocio = Long.parseLong(quadroSocietario2.getNi());
        DossieCliente dossieClienteSocio2 = new DossieCliente();
        dossieClienteSocio2.setId(1L);
        Mockito.when(this.dossieClienteServico.getByCpfCnpj(cnpjSocio, TipoPessoaEnum.J, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE)).thenReturn(dossieClienteSocio2);
        listaQuadroSocietario.add(quadroSocietario2);
        portalEmpreendedorVO.getPessoaJuridica().setQuadroSocietario(listaQuadroSocietario);
        
        ParametrosDossieProdutoMeiVO parametrosDossieProdutoMeiVO = new ParametrosDossieProdutoMeiVO();
        parametrosDossieProdutoMeiVO.setProcesso_originador(123456789);
        parametrosDossieProdutoMeiVO.setTipologia_identificacao_pf("452345345");
        parametrosDossieProdutoMeiVO.setTipologia_endereco_pf("5345345");
        parametrosDossieProdutoMeiVO.setTipologia_constituicao_pj("2343");
        parametrosDossieProdutoMeiVO.setTipologia_faturamento_pj("5324534");
        parametrosDossieProdutoMeiVO.setTipo_relacionamento_responsavel_legal(9);
        parametrosDossieProdutoMeiVO.setTipo_relacionamento_conjuge(3);
        parametrosDossieProdutoMeiVO.setTipo_relacionamento_pj(12);
        parametrosDossieProdutoMeiVO.setTipo_relacionamento_socio_pf(7);
        parametrosDossieProdutoMeiVO.setTipo_relacionamento_socio_pj(8);
        
        
        List<NecessidadeVO> lista = new ArrayList<>();
        NecessidadeVO necessidadeVO = new NecessidadeVO();
        necessidadeVO.setCodigo(9876543);
        lista.add(necessidadeVO);
        portalEmpreendedorVO.setNecessidades(lista);
        Produto produto = new Produto();
        produto.setId(12345);
        Mockito.when(this.produtoServico.getByCodigoPortalEmpreendedor(necessidadeVO.getCodigo())).thenReturn(produto);
        
        Processo processoGeraDossie = new Processo();
        processoGeraDossie.setId(1);
        Mockito.when(this.processoServico.getById(parametrosDossieProdutoMeiVO.getProcesso_originador())).thenReturn(processoGeraDossie);
        
        ContaVO conta = new ContaVO();
        conta.setAgencia("1234");
        conta.setConta("83432-3");
        portalEmpreendedorVO.setConta(conta);
        
        
        TipoDocumento identificacaoPF = new TipoDocumento();
        DocumentoIdentidadeVO documentoIdentificacao = new DocumentoIdentidadeVO();
        documentoIdentificacao.setConteudo("asdfasdfasdfas");
        documentoIdentificacao.setNome("fasdfasdf.png");
        portalEmpreendedorVO.setDocumentoIdentidade(documentoIdentificacao);
        
        TipoDocumento enderecoPF = new TipoDocumento();
        enderecoPF.setId(1);
        ComprovanteResidenciaVO comprovanteResidencia = new ComprovanteResidenciaVO();
        comprovanteResidencia.setConteudo("fasdfadfadfaf");
        comprovanteResidencia.setNome("asfdjaçlfj.pdf");
        portalEmpreendedorVO.setComprovanteResidencia(comprovanteResidencia);
        
        TipoDocumento constituicaoPJ = new TipoDocumento();
        constituicaoPJ.setId(1);
        DocumentoConstituicaoEmpresaVO documentoConstituicao = new DocumentoConstituicaoEmpresaVO();
        documentoConstituicao.setConteudo("asdfasdfasdfas");
        documentoConstituicao.setNome("fasdfasdf.png");
        portalEmpreendedorVO.setDocumentoConstituicaoEmpresa(documentoConstituicao);
        
        TipoDocumento faturamentoPJ = new TipoDocumento();
        DeclaracaoFaturamentoVO declaracaoFaturamento = new DeclaracaoFaturamentoVO();
        declaracaoFaturamento.setConteudo("alsjkfalçdjflaksjdf");
        declaracaoFaturamento.setNome("ajsfajs54234.pdf");
        portalEmpreendedorVO.setDeclaracaoFaturamento(declaracaoFaturamento);
        
        TiposDocumentoValidadoVO tiposDocumentoValidado = new TiposDocumentoValidadoVO(identificacaoPF, enderecoPF, constituicaoPJ, faturamentoPJ);
        Mockito.when(this.portalEmpreendedorHelper.validaTiposDocumento(parametrosDossieProdutoMeiVO)).thenReturn(tiposDocumentoValidado);
        TipoRelacionamento responsavelLegal = new TipoRelacionamento();
        responsavelLegal.setId(1);
        
        TipoRelacionamento conjuge = new TipoRelacionamento();
        conjuge.setId(2);
        
        TipoRelacionamento tomadorContrato = new TipoRelacionamento();
        tomadorContrato.setId(4);
        
        TipoRelacionamento socioPF = new TipoRelacionamento();
        socioPF.setId(5);
        
        TipoRelacionamento socioPJ = new TipoRelacionamento();
        socioPJ.setId(6);
        
        TiposRelacionamentoValidadoVO tiposRelacionamentoValidado = new TiposRelacionamentoValidadoVO(responsavelLegal, conjuge, tomadorContrato, socioPF, socioPJ);
        Mockito.when(this.portalEmpreendedorHelper.validaTiposRelacionamento(parametrosDossieProdutoMeiVO)).thenReturn(tiposRelacionamentoValidado);
        
        
        Mockito.when(this.portalEmpreendedorHelper.consultaServicoPortalEmpreendedorPeloProtocolo("1213123")).thenReturn(portalEmpreendedorVO);        
        Mockito.when(portalEmpreendedorHelper.consultaParametrosCriacaoDossieProdutoMei()).thenReturn(parametrosDossieProdutoMeiVO);
        
        this.portalEmpreendedorServico.montaDossieProdutoMEI("1213123");
    }
}
