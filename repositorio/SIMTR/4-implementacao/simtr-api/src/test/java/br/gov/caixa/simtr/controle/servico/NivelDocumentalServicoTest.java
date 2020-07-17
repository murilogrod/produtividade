package br.gov.caixa.simtr.controle.servico;

import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.helper.NivelDocumentalComposicaoHelper;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.caixa.simtr.modelo.entidade.ComposicaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.mockito.Spy;

public class NivelDocumentalServicoTest {

    @Mock
    private EntityManager manager;

    @Mock
    private TypedQuery<ComposicaoDocumental> queryComposicaoDocumental;
    
    @Mock
    private TypedQuery<DossieCliente> queryDossieCliente;

    @Mock
    private DocumentoServico documentoServico;

    @Mock
    private DossieClienteServico dossieClienteServico;
    
    @Mock
    private ComposicaoDocumentalServico composicaoDocumentalServico;

    @Spy
    private NivelDocumentalComposicaoHelper nivelDocumentalComposicaoHelper;
    
    @InjectMocks
    private final NivelDocumentalServico nivelDocumentalServico = new NivelDocumentalServico();

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_DIR = "/mock/nivel-documental/";
    private static final String UTF_8 = "UTF-8";

    private DossieCliente dossieCliente;
    private final List<ComposicaoDocumental> composicoesAtivas = new ArrayList<>();
    private Map<Documento, String> documentosCliente;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        ComposicaoDocumental composicaoDocumental1 = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("composicao-ativa-1.json")), UTF_8), ComposicaoDocumental.class);
        this.composicoesAtivas.add(composicaoDocumental1);
        
        this.dossieCliente = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("dossie-00600066177.json")), UTF_8), DossieCliente.class);
        this.documentosCliente = dossieCliente.getDocumentos().stream()
                .collect(Collectors.toMap(d -> d, d -> "http://link.de.acesso.ao.documento?id=".concat(d.getCodigoGED())));

        Mockito.when(this.composicaoDocumentalServico.list(Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any(TipoPessoaEnum.class))).thenReturn(composicoesAtivas);
        Mockito.when(this.dossieClienteServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(this.dossieCliente);
        Mockito.when(this.dossieClienteServico.getByCpfCnpj(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(this.dossieCliente);
        Mockito.when(this.documentoServico.listDocumentosDefinitivosDossieDigital(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class))).thenReturn(documentosCliente);
        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(ComposicaoDocumental.class))).thenReturn(queryComposicaoDocumental);
        Mockito.when(this.manager.createQuery(Mockito.anyString(), Mockito.eq(DossieCliente.class))).thenReturn(queryDossieCliente);
        Mockito.when(this.queryComposicaoDocumental.getResultList()).thenReturn(composicoesAtivas);
        Mockito.when(this.queryDossieCliente.getSingleResult()).thenReturn(dossieCliente);
    }

    @Test(expected = SimtrRequisicaoException.class)
    public void atualizaNiveisDocumentaisClienteTestCasoIdentificadorDossieInvalido() {
        Mockito.when(this.dossieClienteServico.getById(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(null);
        this.nivelDocumentalServico.atualizaNiveisDocumentaisCliente(1L);
    }

    @Test
    public void atualizaNiveisDocumentaisClienteTestCasoRegraAtendida() {
        this.nivelDocumentalServico.atualizaNiveisDocumentaisCliente(1L);
    }
    
    @Test
    public void atualizaNiveisDocumentaisClienteTestCasoRegraNaoAtendida() {
        //Remove um documento que tem a analise realizada pela função
        Documento objetoRemocaoRegraFuncao = this.documentosCliente.entrySet().stream().filter(e -> e.getKey().getTipoDocumento().getNome().contains("CONTRA")).findAny().get().getKey();
        this.documentosCliente.remove(objetoRemocaoRegraFuncao);
        this.nivelDocumentalServico.atualizaNiveisDocumentaisCliente(1L);
        
        //Reinclui o documento para voltar ao estado original da coleção e remove um documento que tem a analise realizada pelo tipo
        this.documentosCliente.put(objetoRemocaoRegraFuncao, "http://link.de.acesso.ao.documento?id=".concat(objetoRemocaoRegraFuncao.getCodigoGED()));
        Documento objetoRemocaoRegraTipo = this.documentosCliente.entrySet().stream().filter(e -> e.getKey().getTipoDocumento().getNome().contains("ASSINATURA")).findAny().get().getKey();
        this.documentosCliente.remove(objetoRemocaoRegraTipo);
        this.nivelDocumentalServico.atualizaNiveisDocumentaisCliente(1L);
    }

}
