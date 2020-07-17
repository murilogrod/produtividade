package br.gov.caixa.simtr.controle.servico;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.caixa.pedesgo.arquitetura.siiso.dto.RetornoPessoasFisicasDTO;
import br.gov.caixa.pedesgo.arquitetura.siiso.servico.CadastroReceitaPFServico;
import br.gov.caixa.simtr.controle.excecao.SicpfException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.enumerator.SICPFModoEnum;
import br.gov.caixa.simtr.util.CalendarUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import org.apache.commons.io.IOUtils;
import static org.junit.Assert.*;
import org.mockito.Spy;

/**
 *
 * @author Igor Furtado - Fóton
 * @author Fabio Seixas Sales - CAIXA
 *
 */
public class SicpfServicoTest {

    @Spy
    private CalendarUtil calendarUtil;

    @InjectMocks
    private final CadastroReceitaServico servico = new CadastroReceitaServico();

    @Mock
    private CadastroReceitaPFServico cadastroReceitaPFServico;

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_DIR = "/mock/sicpf/";
    private static final String UTF_8 = "UTF-8";

    private RetornoPessoasFisicasDTO respostaSICPF00065179609;
    private Documento cnhSemAtributos;
    private Documento cnhComAtributos;

    @Before
    public void init() throws Exception {

        String jsonSICPF0006179609 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("00065179609.json")), UTF_8);
        this.respostaSICPF00065179609 = mapper.readValue(jsonSICPF0006179609, RetornoPessoasFisicasDTO.class);

        String jsonDocumentoSemAtributos = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("doc-sem-atributos.json")), UTF_8);
        this.cnhSemAtributos = this.mapper.readValue(jsonDocumentoSemAtributos, Documento.class);

        String jsonDocumentoComAtributos = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("doc-com-atributos.json")), UTF_8);
        this.cnhComAtributos = this.mapper.readValue(jsonDocumentoComAtributos, Documento.class);

        MockitoAnnotations.initMocks(this);
        Mockito.when(this.cadastroReceitaPFServico.consultarPF("00065179609")).thenReturn(respostaSICPF00065179609);
    }

    @Test
    public void consultaSICPFSucesso() throws IOException, Exception {
        this.servico.consultaCadastroPF(65179609L);
    }

    @Test(expected = SicpfException.class)
    public void consultaSICPFCasoSICPFResponseNull() throws Exception {
        Mockito.when(this.cadastroReceitaPFServico.consultarPF(Mockito.anyString())).thenReturn(null);
        this.servico.consultaCadastroPF(32182273212L);
    }

    @Test(expected = SicpfException.class)
    public void consultaSICPFCasoSICPFResultadoNull() throws Exception {
        respostaSICPF00065179609 = null;
        Mockito.when(this.cadastroReceitaPFServico.consultarPF(Mockito.anyString())).thenReturn(respostaSICPF00065179609);
        this.servico.consultaCadastroPF(32182273212L);
    }

    @Test(expected = SicpfException.class)
    public void consultaSICPFCasoException() throws Exception {
        respostaSICPF00065179609 = null;
        Mockito.doThrow(new Exception("Lançamento da Exception")).when(cadastroReceitaPFServico).consultarPF(Mockito.anyString());
        this.servico.consultaCadastroPF(32182273212L);
    }

    @Test
    public void consultaSICPFCasoCPFNaoLocalizado() throws Exception {
        respostaSICPF00065179609 = null;
        Mockito.doThrow(new Exception("DOCUMENTO NÃO LOCALIZADO NA BASE FISCAL")).when(cadastroReceitaPFServico).consultarPF(Mockito.anyString());
        this.servico.consultaCadastroPF(32182273212L);
    }

    //**************************************************************
    @Test
    public void validaDocumentoSICPF() throws Exception {
        this.servico.validaDocumentoSICPF(65179609L, this.cnhComAtributos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validaDocumentoSICPFCasoCPFNull() throws Exception {
        this.servico.validaDocumentoSICPF(null, cnhComAtributos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validaDocumentoSICPFCasoCPFInvalido() throws Exception {
        this.servico.validaDocumentoSICPF(12312312344L, cnhComAtributos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validaDocumentoSICPFCasoTipoDocumentoNulo() throws Exception {
        cnhComAtributos.setTipoDocumento(null);
        this.servico.validaDocumentoSICPF(65179609L, cnhComAtributos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validaDocumentoSICPFCasoTipoDocumentoAtributosExtracaoVazio() throws Exception {
        cnhComAtributos.getTipoDocumento().setAtributosExtracao(new HashSet<>());
        this.servico.validaDocumentoSICPF(65179609L, cnhComAtributos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validaDocumentoSICPFCasoDocumentoSemAtributo() throws Exception {
        this.servico.validaDocumentoSICPF(65179609L, cnhSemAtributos);
    }

    //***** Inicio dos testes de validação dos atributos do documento com o retorno do SICPF ******//
    //NOME
    @Test
    public void validaDocumentoSICPFCasoNomeValidado() throws Exception {
        boolean falhaParcial = Boolean.FALSE;
        boolean falhaExtato = Boolean.FALSE;

        AtributoExtracao atributoNome = cnhComAtributos.getTipoDocumento().getAtributosExtracao().stream()
                .filter(ae -> ae.getNomeAtributoDocumento().equals("nome")).findFirst().get();

        //Comparação do atributo em modo Parcial
        try {
            atributoNome.setSicpfModoEnum(SICPFModoEnum.P);
            this.servico.validaDocumentoSICPF(65179609L, cnhComAtributos);
        } catch (SimtrRequisicaoException e) {
            falhaParcial = Boolean.TRUE;
        }
        assertFalse("Falha na validação do NOME em modo PARCIAL", falhaParcial);

        //Comparação do atributo em modo Exato
        try {
            atributoNome.setSicpfModoEnum(SICPFModoEnum.E);
            this.servico.validaDocumentoSICPF(65179609L, cnhComAtributos);
        } catch (SimtrRequisicaoException e) {
            falhaExtato = Boolean.TRUE;
        }
        assertFalse("Falha na validação do NOME em modo EXATO", falhaExtato);
    }

    @Test
    public void validaDocumentoSICPFCasoNomeNaoValidado() throws Exception {
        boolean falhaParcial = Boolean.FALSE;
        boolean falhaExtato = Boolean.FALSE;

        cnhComAtributos.getAtributosDocumento().stream()
                .filter(ad -> ad.getDescricao().equals("nome"))
                .forEach(ad -> ad.setConteudo("FULANO DE TAL"));

        AtributoExtracao atributoNome = cnhComAtributos.getTipoDocumento().getAtributosExtracao().stream()
                .filter(ae -> ae.getNomeAtributoDocumento().equals("nome")).findFirst().get();

        //Comparação do atributo em modo Parcial
        try {
            atributoNome.setSicpfModoEnum(SICPFModoEnum.P);
            this.servico.validaDocumentoSICPF(65179609L, cnhComAtributos);
        } catch (SimtrRequisicaoException e) {
            falhaParcial = Boolean.TRUE;
        }
        assertTrue("Falha na validação do NOME em modo PARCIAL", falhaParcial);

        //Comparação do atributo em modo Exato
        try {
            atributoNome.setSicpfModoEnum(SICPFModoEnum.E);
            this.servico.validaDocumentoSICPF(65179609L, cnhComAtributos);
        } catch (SimtrRequisicaoException e) {
            falhaExtato = Boolean.TRUE;
        }
        assertTrue("Falha na validação do NOME em modo EXATO", falhaExtato);
    }

    //MAE
    @Test
    public void validaDocumentoSICPFCasoMaeValidado() throws Exception {
        boolean falhaParcial = Boolean.FALSE;
        boolean falhaExtato = Boolean.FALSE;

        AtributoExtracao atributoFiliacao = cnhComAtributos.getTipoDocumento().getAtributosExtracao().stream()
                .filter(ae -> ae.getNomeAtributoDocumento().equals("filiacao")).findFirst().get();

        try {
            atributoFiliacao.setSicpfModoEnum(SICPFModoEnum.P);
            this.servico.validaDocumentoSICPF(65179609L, cnhComAtributos);
        } catch (SimtrRequisicaoException e) {
            falhaParcial = Boolean.TRUE;
        }
        assertFalse("Falha na validação do NOME DA MAE em modo PARCIAL", falhaParcial);

        try {
            atributoFiliacao.setSicpfModoEnum(SICPFModoEnum.E);
            this.servico.validaDocumentoSICPF(65179609L, cnhComAtributos);
        } catch (SimtrRequisicaoException e) {
            falhaExtato = Boolean.TRUE;
        }

        assertFalse("Falha na validação do NOME DA MAE em modo EXATO", falhaExtato);
    }

    @Test
    public void validaDocumentoSICPFCasoMaeNaoValidado() throws Exception {
        boolean falhaParcial = Boolean.FALSE;
        boolean falhaExtato = Boolean.FALSE;

        cnhComAtributos.getAtributosDocumento().stream()
                .filter(ad -> ad.getDescricao().equals("filiacao"))
                .forEach(ad -> ad.setConteudo("FULANA DE TAL"));

        AtributoExtracao atributoFiliacao = cnhComAtributos.getTipoDocumento().getAtributosExtracao().stream()
                .filter(ae -> ae.getNomeAtributoDocumento().equals("filiacao")).findFirst().get();

        try {
            atributoFiliacao.setSicpfModoEnum(SICPFModoEnum.P);
            this.servico.validaDocumentoSICPF(65179609L, cnhComAtributos);
        } catch (SimtrRequisicaoException e) {
            falhaParcial = Boolean.TRUE;
        }
        assertTrue("Falha na validação do NOME DA MAE em modo PARCIAL", falhaParcial);

        try {
            atributoFiliacao.setSicpfModoEnum(SICPFModoEnum.E);
            this.servico.validaDocumentoSICPF(65179609L, cnhComAtributos);
        } catch (SimtrRequisicaoException e) {
            falhaExtato = Boolean.TRUE;
        }

        assertTrue("Falha na validação do NOME DA MAE em modo EXATO", falhaExtato);
    }

    //NASCIMENTO
    @Test
    public void validaDocumentoSICPFCasoDataNascimento() throws Exception {
        boolean falhaDia = Boolean.FALSE;
        boolean falhaMes = Boolean.FALSE;
        boolean falhaAno = Boolean.FALSE;
        boolean falhaConversao = Boolean.FALSE;

        // Data de Nascimento na Consulta SICPF = 09.08.1975
        AtributoDocumento atributoDataNascimento = cnhComAtributos.getAtributosDocumento().stream()
                .filter(ad -> ad.getDescricao().equals("data_nascimento")).findFirst().get();

        //Teste de falha no dia
        try {
            atributoDataNascimento.setConteudo("01/08/1975");
            this.servico.validaDocumentoSICPF(65179609L, cnhComAtributos);
        } catch (SimtrRequisicaoException e) {
            falhaDia = Boolean.TRUE;
        }

        //Teste de falha no mês
        try {
            atributoDataNascimento.setConteudo("09/01/1975");
            this.servico.validaDocumentoSICPF(65179609L, cnhComAtributos);
        } catch (SimtrRequisicaoException e) {
            falhaMes = Boolean.TRUE;
        }

        //Teste de falha no ano
        try {
            atributoDataNascimento.setConteudo("09/08/1980");
            this.servico.validaDocumentoSICPF(65179609L, cnhComAtributos);
        } catch (SimtrRequisicaoException e) {
            falhaAno = Boolean.TRUE;
        }

        //Teste de falha na conversão (formato)
        try {
            atributoDataNascimento.setConteudo("o9/o8/L98o");
            this.servico.validaDocumentoSICPF(65179609L, cnhComAtributos);
        } catch (SimtrRequisicaoException e) {
            falhaConversao = Boolean.TRUE;
        }

        assertTrue("Falha na validação da DATA DE NASCIMENTO pelo valor do DIA", falhaDia);
        assertTrue("Falha na validação da DATA DE NASCIMENTO pelo valor do MES", falhaMes);
        assertTrue("Falha na validação da DATA DE NASCIMENTO pelo valor do ANO", falhaAno);
        assertTrue("Falha na validação da DATA DE NASCIMENTO pelo formato geral", falhaConversao);
    }

    //ELEITOR
    @Test
    public void validaDocumentoSICPFCasoTituloEleitor() throws Exception {
        boolean falhaDivergencia = Boolean.FALSE;
        boolean falhaFormato = Boolean.FALSE;

        // Titulo Eleitor na Consulta SICPF = 2361642492
        AtributoDocumento atributoTituloEleitor = cnhComAtributos.getAtributosDocumento().stream()
                .filter(ad -> ad.getDescricao().equals("titulo_eleitor")).findFirst().get();

        try {
            atributoTituloEleitor.setConteudo("32874289402");
            this.servico.validaDocumentoSICPF(65179609L, cnhComAtributos);
        } catch (SimtrRequisicaoException e) {
            falhaDivergencia = Boolean.TRUE;
        }

        try {
            atributoTituloEleitor.setConteudo("SDFJHKJDHFD");
            this.servico.validaDocumentoSICPF(65179609L, cnhComAtributos);
        } catch (SimtrRequisicaoException e) {
            falhaFormato = Boolean.TRUE;
        }

        assertTrue("Falha na validação do TITULO DE ELEITOR por VALOR DIVERGENTE", falhaDivergencia);
        assertTrue("Falha na validação do TITULO DE ELEITOR por FORMATO DE NUMERO INVALIDO", falhaFormato);

    }

    //CPF
    @Test
    public void validaDocumentoSICPFCasoCPF() throws Exception {
        boolean falhaDivergencia = Boolean.FALSE;
        boolean falhaFormato = Boolean.FALSE;

        // CPF na Consulta SICPF = Não existe retorno. Valor comparado com o valor do CPF encaminhado como base.
        AtributoDocumento atributoCPF = cnhComAtributos.getAtributosDocumento().stream()
                .filter(ad -> ad.getDescricao().equals("cpf")).findFirst().get();

        try {
            atributoCPF.setConteudo("90065179600");
            this.servico.validaDocumentoSICPF(65179609L, cnhComAtributos);
        } catch (SimtrRequisicaoException e) {
            falhaDivergencia = Boolean.TRUE;
        }

        try {
            atributoCPF.setConteudo("");
            this.servico.validaDocumentoSICPF(65179609L, cnhComAtributos);
        } catch (SimtrRequisicaoException e) {
            falhaFormato = Boolean.TRUE;
        }

        assertTrue("Falha na validação do CPF por VALOR DIVERGENTE", falhaDivergencia);
        assertTrue("Falha na validação do CPF por FORMATO DE NUMERO INVALIDO", falhaFormato);
    }

}
