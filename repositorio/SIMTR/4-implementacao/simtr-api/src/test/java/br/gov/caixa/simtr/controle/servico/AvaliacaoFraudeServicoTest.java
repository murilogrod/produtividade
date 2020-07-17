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

import java.util.List;

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

import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.RetornoPesquisaDTO;
import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.GEDService;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.pedesgo.arquitetura.util.UtilParametro;
import br.gov.caixa.pedesgo.arquitetura.util.UtilRest;
import br.gov.caixa.pedesgo.arquitetura.util.UtilWS;
import br.gov.caixa.pedesgo.arquitetura.util.UtilXml;
import br.gov.caixa.simtr.controle.excecao.SiecmException;
import br.gov.caixa.simtr.controle.excecao.SifrcException;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.KeycloakUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.io.IOUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UtilRest.class, UtilWS.class, UtilParametro.class, UtilXml.class})
public class AvaliacaoFraudeServicoTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private GEDService gedService;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private KeycloakUtil keycloakUtil;

    @InjectMocks
    private AvaliacaoFraudeServico avaliacaoFraudeServico;

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_DIR = "/mock/avaliacao-fraude/";
    private static final String UTF_8 = "UTF-8";

    private Response responseRecomendacaoPositiva;
    private Response responseRecomendacaoNegativa;
    private Response responseClientError;
    private Response responseServerError;
    private RetornoPesquisaDTO retornoPesquisaDTO;
    private RetornoPesquisaDTO retornoVazioPesquisaDTO;

    private Documento documentoTeste;
    
    List<SituacaoDocumento> situacoes;

    @Before
    public void setup() throws IOException, JAXBException {
        MockitoAnnotations.initMocks(this);

        JAXBContext jc = JAXBContext.newInstance(RetornoPesquisaDTO.class);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        this.retornoPesquisaDTO = (RetornoPesquisaDTO) unmarshaller.unmarshal(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("retorno-siecm.xml")));
        this.retornoVazioPesquisaDTO = (RetornoPesquisaDTO) unmarshaller.unmarshal(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("retorno-siecm-vazio.xml")));

        String xmlRecomendacaoPositiva = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("retorno-sifrc-sim.xml")), UTF_8);
        this.responseRecomendacaoPositiva = Response.ok(xmlRecomendacaoPositiva).build();
        this.responseClientError = Response.status(Response.Status.BAD_REQUEST).build();
        this.responseServerError = Response.status(Response.Status.SERVICE_UNAVAILABLE).build();

        String xmlRecomendacaoNegativa = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("retorno-sifrc-nao.xml")), UTF_8);
        this.responseRecomendacaoNegativa = Response.ok(xmlRecomendacaoNegativa).build();

        PowerMockito.mockStatic(UtilRest.class, UtilWS.class, UtilParametro.class);
        PowerMockito.spy(UtilXml.class);

        Mockito.when(keycloakUtil.getIpFromToken()).thenReturn("127.0.0.1");
        Mockito.when(UtilParametro.getParametro(Mockito.anyString(), Mockito.anyBoolean())).thenReturn("valor-parametro");

        Mockito.when(gedService.searchDocument(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(this.retornoPesquisaDTO);
        Mockito.when(UtilWS.obterBytes(Mockito.anyString())).thenReturn(new byte[325]);
        
        String jsonDocComAtributos = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("doc-com-atributos.json")), UTF_8);
        this.documentoTeste = mapper.readValue(jsonDocComAtributos, Documento.class);
    }

    @Test
    public void testRecuperaResultadoAvaliacaoAutenticidade() {
        Assert.assertTrue(avaliacaoFraudeServico.recuperaResultadoAvaliacaoAutenticidade(1L, TipoPessoaEnum.F, documentoTeste));
    }

    @Test
    public void testAvalaicaoSuccess() {
        //Submissao documento Pessoa Fisica
        Mockito.when(UtilRest.consumirServicoOAuth2(Mockito.anyString(), Mockito.any(EnumMetodoHTTP.class), Mockito.anyMapOf(String.class, String.class), Mockito.anyMapOf(String.class, String.class), Mockito.any(Object.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(responseRecomendacaoPositiva);
        this.avaliacaoFraudeServico.submeteDocumentoSIFRC(1L, TipoPessoaEnum.F, new Documento(), true);
        
        //Submissao documento Pessoa juridica
        Mockito.when(UtilRest.consumirServicoOAuth2(Mockito.anyString(), Mockito.any(EnumMetodoHTTP.class), Mockito.anyMapOf(String.class, String.class), Mockito.anyMapOf(String.class, String.class), Mockito.any(Object.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(responseRecomendacaoNegativa);
        this.avaliacaoFraudeServico.submeteDocumentoSIFRC(1L, TipoPessoaEnum.J, new Documento(), false);
    }
    
    @Test(expected = SifrcException.class)
    public void testSubmeteDocumentoSIFRCCasoClientError() {
        Mockito.when(UtilRest.consumirServicoOAuth2(Mockito.anyString(), Mockito.any(EnumMetodoHTTP.class), Mockito.anyMapOf(String.class, String.class), Mockito.anyMapOf(String.class, String.class), Mockito.any(Object.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(responseClientError);
        this.avaliacaoFraudeServico.submeteDocumentoSIFRC(1L, TipoPessoaEnum.F, new Documento(), true);
    }
    
    @Test(expected = SifrcException.class)
    public void testSubmeteDocumentoSIFRCCasoServerError() {
        Mockito.when(UtilRest.consumirServicoOAuth2(Mockito.anyString(), Mockito.any(EnumMetodoHTTP.class), Mockito.anyMapOf(String.class, String.class), Mockito.anyMapOf(String.class, String.class), Mockito.any(Object.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(responseServerError);
        this.avaliacaoFraudeServico.submeteDocumentoSIFRC(1L, TipoPessoaEnum.F, new Documento(), true);
    }

    @Test(expected = SiecmException.class)
    public void testSubmeteDocumentoSIFRCCasoDocumentoNaoLocalizado() {
        Mockito.when(gedService.searchDocument(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(this.retornoVazioPesquisaDTO);
        this.avaliacaoFraudeServico.submeteDocumentoSIFRC(1L, TipoPessoaEnum.F, new Documento(), true);
    }

}
