package br.gov.caixa.simtr.controle.servico.helper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.caixa.simtr.controle.servico.TipoRelacionamentoServico;
import br.gov.caixa.simtr.modelo.entidade.CampoEntrada;
import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;
import br.gov.caixa.simtr.modelo.enumerator.TipoCampoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.GarantiaInformadaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.ProdutoContratadoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.RespostaFormularioDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.VinculoPessoaDTO;

public class DossieProdutoValidacaoHelperTest {

    @InjectMocks
    private DossieProdutoValidacaoHelper dossieProdutoValidacaoHelper;

    @Mock
    private TipoRelacionamentoServico tipoRelacionamentoServico;

    private Method metodoValidaFormularioVinculo, metodoValidaFormularioFase;

    @Before
    public void init() throws NoSuchMethodException {
        MockitoAnnotations.initMocks(this);

        metodoValidaFormularioVinculo = this.dossieProdutoValidacaoHelper.getClass().getDeclaredMethod("validaFormularioVinculo", Set.class, List.class);
        metodoValidaFormularioVinculo.setAccessible(Boolean.TRUE);

        metodoValidaFormularioFase = this.dossieProdutoValidacaoHelper.getClass().getDeclaredMethod("validaFormularioFase", Set.class, List.class, List.class, List.class, List.class);
        metodoValidaFormularioFase.setAccessible(Boolean.TRUE);
    }

    private CampoFormulario criarCampoFormulario(Long id, String nome, TipoCampoEnum tipoCampo, String expressao, Boolean obrigatorio) {
        CampoFormulario campo = new CampoFormulario();
        CampoEntrada entrada = new CampoEntrada();

        campo.setId(id);
        campo.setExpressaoInterface(expressao);
        campo.setObrigatorio(obrigatorio == null ? false : obrigatorio);

        entrada.setTipo(tipoCampo);
        entrada.setLabel(nome);

        campo.setCampoEntrada(entrada);

        return campo;
    }

    private RespostaFormularioDTO criarRespostaFormulario(Long id, String respostaAberta, List<String> valores) {
        RespostaFormularioDTO resposta = new RespostaFormularioDTO();

        resposta.setIdCampoFomulario(id);
        resposta.setResposta(respostaAberta);

        if (valores != null && !valores.isEmpty()) {
            resposta.setOpcoesSelecionadas(valores);
        }

        return resposta;
    }

    private VinculoPessoaDTO criarVinculoPessoa(Integer tipoRelacionamento) {
        VinculoPessoaDTO pessoa = new VinculoPessoaDTO();
        pessoa.setTipoRelacionamento(tipoRelacionamento);
        return pessoa;
    }

    private ProdutoContratadoDTO criarProduto(Integer idProduto, Integer codigoModalidade, Integer codigoOperacao) {
        ProdutoContratadoDTO produto = new ProdutoContratadoDTO();

        produto.setIdProduto(idProduto);
        produto.setModalidade(codigoModalidade);
        produto.setOperacao(codigoOperacao);

        return produto;
    }

    private GarantiaInformadaDTO cirarGarantia(Integer identificardor) {
        GarantiaInformadaDTO garantia = new GarantiaInformadaDTO();
        garantia.setIdentificadorGarantia(identificardor);
        return garantia;
    }

    @Test
    public void campoSelectComExpressaoObrigatorioTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Set<CampoFormulario> camposPreenchido = new HashSet<>();
        List<RespostaFormularioDTO> respostasPreenchido = new ArrayList<>();
        final String expressao = "[[{\"tipo_regra\":\"GC\", \"campo_resposta\": 1, \"valores\":[\"999999999\"]}]]";

        CampoFormulario campoDonoDaResposta = this.criarCampoFormulario(1L, "Dono da Resposta", TipoCampoEnum.TEXT, null, false);
        RespostaFormularioDTO respostaDonoDaResposta = this.criarRespostaFormulario(1L, "999999999", null);

        CampoFormulario campoSelect = this.criarCampoFormulario(2L, "Campo Select", TipoCampoEnum.SELECT, expressao, true);
        RespostaFormularioDTO respostaSelect = this.criarRespostaFormulario(2L, null, Arrays.asList("campoRespondito"));

        camposPreenchido.add(campoDonoDaResposta);
        camposPreenchido.add(campoSelect);

        respostasPreenchido.add(respostaDonoDaResposta);
        respostasPreenchido.add(respostaSelect);

        List<CampoFormulario> camposObrigatorios = (List<CampoFormulario>) metodoValidaFormularioVinculo.invoke(this.dossieProdutoValidacaoHelper, camposPreenchido, respostasPreenchido);

        // List<CampoFormulario> camposObrigatorios = this.dossieProdutoValidacaoHelper.validar(camposPreenchido, respostasPreenchido);

        Assert.assertEquals(0, camposObrigatorios.size());
    }

    @Test
    public void campoSelectComExpressaoObrigatorioSemRespostaTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Set<CampoFormulario> camposPreenchido = new HashSet<>();
        List<RespostaFormularioDTO> respostasPreenchido = new ArrayList<>();
        final String expressao = "[[{\"tipo_regra\":\"GC\", \"campo_resposta\": 1, \"valores\":[\"999999999\"]}]]";

        CampoFormulario campoDonoDaResposta = this.criarCampoFormulario(1L, "Dono da Resposta", TipoCampoEnum.TEXT, null, false);
        RespostaFormularioDTO respostaDonoDaResposta = this.criarRespostaFormulario(1L, "999999999", null);

        CampoFormulario campoSelect = this.criarCampoFormulario(2L, "Campo Select", TipoCampoEnum.SELECT, expressao, true);
        RespostaFormularioDTO respostaSelect = this.criarRespostaFormulario(2L, null, null);

        camposPreenchido.add(campoDonoDaResposta);
        camposPreenchido.add(campoSelect);

        respostasPreenchido.add(respostaDonoDaResposta);
        respostasPreenchido.add(respostaSelect);

        List<CampoFormulario> camposObrigatorios = (List<CampoFormulario>) metodoValidaFormularioVinculo.invoke(this.dossieProdutoValidacaoHelper, camposPreenchido, respostasPreenchido);

        // List<CampoFormulario> camposObrigatorios = this.dossieProdutoValidacaoHelper.validar(camposPreenchido, respostasPreenchido);

        Assert.assertEquals(1, camposObrigatorios.size());
        Assert.assertEquals(camposObrigatorios.get(0).getId(), Long.valueOf(2));
    }

    @Test
    public void campoRadioComExpressaoObrigatorioComRespostaTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Set<CampoFormulario> camposPreenchido = new HashSet<>();
        List<RespostaFormularioDTO> respostasPreenchido = new ArrayList<>();
        final String expressao = "[[{\"tipo_regra\":\"GP\", \"campo_resposta\": 1, \"valores\":[\"SIM\"]}]]";

        CampoFormulario campoDonoDaResposta = this.criarCampoFormulario(1L, "CASADO ?", TipoCampoEnum.RADIO, null, false);
        RespostaFormularioDTO respostaDonoDaResposta = this.criarRespostaFormulario(1L, null, Arrays.asList("SIM"));

        CampoFormulario campoRadio = this.criarCampoFormulario(2L, "SEU ESTADO DA SUA FELIZCIDADE", TipoCampoEnum.RADIO, expressao, true);
        RespostaFormularioDTO respostaRadio = this.criarRespostaFormulario(2L, null, Arrays.asList("TRISTE"));

        camposPreenchido.add(campoDonoDaResposta);
        camposPreenchido.add(campoRadio);

        respostasPreenchido.add(respostaDonoDaResposta);
        respostasPreenchido.add(respostaRadio);

        List<CampoFormulario> camposObrigatorios = (List<CampoFormulario>) metodoValidaFormularioVinculo.invoke(this.dossieProdutoValidacaoHelper, camposPreenchido, respostasPreenchido);
        // List<CampoFormulario> camposObrigatorios = this.dossieProdutoValidacaoHelper.validar(camposPreenchido, respostasPreenchido);

        Assert.assertEquals(0, camposObrigatorios.size());
    }

    @Test
    public void campoRadioComExpressaoObrigatorioSemRespostaTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Set<CampoFormulario> camposPreenchido = new HashSet<>();
        List<RespostaFormularioDTO> respostasPreenchido = new ArrayList<>();
        final String expressao = "[[{\"tipo_regra\":\"GP\", \"campo_resposta\": 1, \"valores\":[\"SIM\"]}]]";

        CampoFormulario campoDonoDaResposta = this.criarCampoFormulario(1L, "CASADO ?", TipoCampoEnum.RADIO, null, false);
        RespostaFormularioDTO respostaDonoDaResposta = this.criarRespostaFormulario(1L, null, Arrays.asList("SIM"));

        CampoFormulario campoRadio = this.criarCampoFormulario(2L, "SEU ESTADO DA SUA FELIZCIDADE", TipoCampoEnum.RADIO, expressao, true);
        RespostaFormularioDTO respostaRadio = this.criarRespostaFormulario(2L, null, null);

        camposPreenchido.add(campoDonoDaResposta);
        camposPreenchido.add(campoRadio);

        respostasPreenchido.add(respostaDonoDaResposta);
        respostasPreenchido.add(respostaRadio);

        List<CampoFormulario> camposObrigatorios = (List<CampoFormulario>) metodoValidaFormularioVinculo.invoke(this.dossieProdutoValidacaoHelper, camposPreenchido, respostasPreenchido);
        // List<CampoFormulario> camposObrigatorios = this.dossieProdutoValidacaoHelper.validar(camposPreenchido, respostasPreenchido);

        Assert.assertEquals(1, camposObrigatorios.size());
        Assert.assertEquals(camposObrigatorios.get(0).getId(), Long.valueOf(2));
    }

    @Test
    public void campoCheckboxComExpressaoObrigatorioComRespostaTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Set<CampoFormulario> camposPreenchido = new HashSet<>();
        List<RespostaFormularioDTO> respostasPreenchido = new ArrayList<>();
        final String expressao = "[[{\"tipo_regra\":\"GG\", \"campo_resposta\": 1, \"valores\":[\"AMARELO\", \"VERMELHO\", \"PRETO\"]}]]";

        CampoFormulario campoDonoDaResposta = this.criarCampoFormulario(1L, "QUAL SUA COR PREFERIDA", TipoCampoEnum.CHECKBOX, null, false);
        RespostaFormularioDTO respostaDonoDaResposta = this.criarRespostaFormulario(1L, null, Arrays.asList("AMARELO", "VERMELHO", "PRETO"));

        CampoFormulario campoRadio = this.criarCampoFormulario(2L, "Descreva alguma coisa sobre essas cores", TipoCampoEnum.TEXT, expressao, true);
        RespostaFormularioDTO respostaRadio = this.criarRespostaFormulario(2L, null, Arrays.asList(".........."));

        camposPreenchido.add(campoDonoDaResposta);
        camposPreenchido.add(campoRadio);

        respostasPreenchido.add(respostaDonoDaResposta);
        respostasPreenchido.add(respostaRadio);

        List<CampoFormulario> camposObrigatorios = (List<CampoFormulario>) metodoValidaFormularioVinculo.invoke(this.dossieProdutoValidacaoHelper, camposPreenchido, respostasPreenchido);
        // List<CampoFormulario> camposObrigatorios = this.dossieProdutoValidacaoHelper.validar(camposPreenchido, respostasPreenchido);

        Assert.assertEquals(0, camposObrigatorios.size());
    }

    @Test
    public void campoCheckboxComExpressaoComRespostasDiferenteDaQuantidadeEsperadaTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Set<CampoFormulario> camposPreenchido = new HashSet<>();
        List<RespostaFormularioDTO> respostasPreenchido = new ArrayList<>();
        final String expressao = "[[{\"tipo_regra\":\"GG\", \"campo_resposta\": 1, \"valores\":[\"AMARELO\", \"VERMELHO\", \"PRETO\"]}]]";

        CampoFormulario campoDonoDaResposta = this.criarCampoFormulario(1L, "QUAL SUA COR PREFERIDA", TipoCampoEnum.CHECKBOX, null, false);
        RespostaFormularioDTO respostaDonoDaResposta = this.criarRespostaFormulario(1L, null, Arrays.asList("AMARELO"));

        CampoFormulario campoCheckbox = this.criarCampoFormulario(2L, "Descreva alguma coisa sobre essas cores", TipoCampoEnum.TEXTAREA, expressao, true);
        RespostaFormularioDTO respostaCheckbox = this.criarRespostaFormulario(2L, "blabla", null);

        camposPreenchido.add(campoDonoDaResposta);
        camposPreenchido.add(campoCheckbox);

        respostasPreenchido.add(respostaDonoDaResposta);
        respostasPreenchido.add(respostaCheckbox);

        List<CampoFormulario> camposObrigatorios = (List<CampoFormulario>) metodoValidaFormularioVinculo.invoke(this.dossieProdutoValidacaoHelper, camposPreenchido, respostasPreenchido);
        // List<CampoFormulario> camposObrigatorios = this.dossieProdutoValidacaoHelper.validar(camposPreenchido, respostasPreenchido);

        Assert.assertEquals(0, camposObrigatorios.size());
    }

    @Test
    public void campoComTresRegrasEmUmConjuntoComRespostaTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Set<CampoFormulario> camposPreenchido = new HashSet<>();
        List<RespostaFormularioDTO> respostasPreenchido = new ArrayList<>();
        final String expressao = "[[{\"tipo_regra\":\"GG\", \"campo_resposta\": 1, \"valores\":[\"SIM\"]}, {\"tipo_regra\":\"GP\", \"campo_resposta\": 2, \"valores\":[\"SIM\"]}, {\"tipo_regra\":\"GC\", \"campo_resposta\": 3, \"valores\":[\"SIM\"]}]]";

        CampoFormulario campoDonoDaResposta1 = this.criarCampoFormulario(1L, "Voce tem caro?", TipoCampoEnum.RADIO, null, false);
        RespostaFormularioDTO respostaDonoDaResposta1 = this.criarRespostaFormulario(1L, null, Arrays.asList("AMARELO"));

        CampoFormulario campoDonoDaResposta2 = this.criarCampoFormulario(2L, "Voce tem moto", TipoCampoEnum.RADIO, null, false);
        RespostaFormularioDTO respostaDonoDaResposta2 = this.criarRespostaFormulario(1L, null, Arrays.asList("AMARELO"));

        CampoFormulario campoDonoDaResposta3 = this.criarCampoFormulario(3L, "Voce tem casa propria?", TipoCampoEnum.RADIO, null, false);
        RespostaFormularioDTO respostaDonoDaResposta3 = this.criarRespostaFormulario(1L, null, Arrays.asList("AMARELO"));

        CampoFormulario campoRespostaObrigatorio = this.criarCampoFormulario(4L, "Descreva o que vc acha sobre isso", TipoCampoEnum.TEXT, expressao, true);
        RespostaFormularioDTO respostaObrigatorio = this.criarRespostaFormulario(4L, "tem que rala muito", null);

        camposPreenchido.add(campoDonoDaResposta1);
        camposPreenchido.add(campoDonoDaResposta2);
        camposPreenchido.add(campoDonoDaResposta3);
        camposPreenchido.add(campoRespostaObrigatorio);

        respostasPreenchido.add(respostaDonoDaResposta1);
        respostasPreenchido.add(respostaDonoDaResposta2);
        respostasPreenchido.add(respostaDonoDaResposta3);
        respostasPreenchido.add(respostaObrigatorio);

        List<CampoFormulario> camposObrigatorios = (List<CampoFormulario>) metodoValidaFormularioVinculo.invoke(this.dossieProdutoValidacaoHelper, camposPreenchido, respostasPreenchido);
        // List<CampoFormulario> camposObrigatorios = this.dossieProdutoValidacaoHelper.validar(camposPreenchido, respostasPreenchido);

        Assert.assertEquals(0, camposObrigatorios.size());
    }

    @Test
    public void campoComTresRegrasEmUmConjuntoSemRespostaTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Set<CampoFormulario> camposPreenchido = new HashSet<>();
        List<RespostaFormularioDTO> respostasPreenchido = new ArrayList<>();
        final String expressao = "[[{\"tipo_regra\":\"GG\", \"campo_resposta\": 1, \"valores\":[\"SIM\"]}, {\"tipo_regra\":\"GP\", \"campo_resposta\": 2, \"valores\":[\"SIM\"]}, {\"tipo_regra\":\"GC\", \"campo_resposta\": 3, \"valores\":[\"SIM\"]}]]";

        CampoFormulario campoDonoDaResposta1 = this.criarCampoFormulario(1L, "Voce tem caro?", TipoCampoEnum.RADIO, null, false);
        RespostaFormularioDTO respostaDonoDaResposta1 = this.criarRespostaFormulario(1L, null, Arrays.asList("SIM"));

        CampoFormulario campoDonoDaResposta2 = this.criarCampoFormulario(2L, "Voce tem moto", TipoCampoEnum.RADIO, null, false);
        RespostaFormularioDTO respostaDonoDaResposta2 = this.criarRespostaFormulario(2L, null, Arrays.asList("SIM"));

        CampoFormulario campoDonoDaResposta3 = this.criarCampoFormulario(3L, "Voce tem casa propria?", TipoCampoEnum.RADIO, null, false);
        RespostaFormularioDTO respostaDonoDaResposta3 = this.criarRespostaFormulario(3L, null, Arrays.asList("SIM"));

        CampoFormulario campoRespostaObrigatorio = this.criarCampoFormulario(4L, "Descreva o que vc acha sobre isso", TipoCampoEnum.TEXT, expressao, true);

        camposPreenchido.add(campoDonoDaResposta1);
        camposPreenchido.add(campoDonoDaResposta2);
        camposPreenchido.add(campoDonoDaResposta3);
        camposPreenchido.add(campoRespostaObrigatorio);

        respostasPreenchido.add(respostaDonoDaResposta1);
        respostasPreenchido.add(respostaDonoDaResposta2);
        respostasPreenchido.add(respostaDonoDaResposta3);

        List<CampoFormulario> camposObrigatorios = (List<CampoFormulario>) metodoValidaFormularioVinculo.invoke(this.dossieProdutoValidacaoHelper, camposPreenchido, respostasPreenchido);
        // List<CampoFormulario> camposObrigatorios = this.dossieProdutoValidacaoHelper.validar(camposPreenchido, respostasPreenchido);

        Assert.assertEquals(1, camposObrigatorios.size());
        Assert.assertEquals(camposObrigatorios.get(0).getId(), Long.valueOf(4));
    }

    @Test
    public void campoComDoisConjuntosSastifazendoSomenteUmConjuntoSemRespostaTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Set<CampoFormulario> camposPreenchido = new HashSet<>();
        List<RespostaFormularioDTO> respostasPreenchido = new ArrayList<>();
        final String expressao = "[[{\"tipo_regra\": \"GG\", \"campo_resposta\": 1, \"valores\":[\"SIM\"]}],[{\"tipo_regra\":\"GP\", \"campo_resposta\": 2, \"valores\":[\"NAO\"]}]]";

        CampoFormulario campo1 = this.criarCampoFormulario(1L, "Voce tem caro?", TipoCampoEnum.RADIO, null, false);
        RespostaFormularioDTO resposta1 = this.criarRespostaFormulario(1L, null, Arrays.asList("SIM"));

        CampoFormulario campo2 = this.criarCampoFormulario(2L, "Voce tem moto", TipoCampoEnum.RADIO, null, false);
        RespostaFormularioDTO resposta2 = this.criarRespostaFormulario(2L, null, Arrays.asList("SIM"));

        CampoFormulario campoRespostaObrigatorio = this.criarCampoFormulario(3L, "Descreva o que vc acha sobre isso", TipoCampoEnum.TEXT, expressao, true);

        camposPreenchido.add(campo1);
        camposPreenchido.add(campo2);
        camposPreenchido.add(campoRespostaObrigatorio);

        respostasPreenchido.add(resposta1);
        respostasPreenchido.add(resposta2);

        List<CampoFormulario> camposObrigatorios = (List<CampoFormulario>) metodoValidaFormularioVinculo.invoke(this.dossieProdutoValidacaoHelper, camposPreenchido, respostasPreenchido);
        // List<CampoFormulario> camposObrigatorios = this.dossieProdutoValidacaoHelper.validar(camposPreenchido, respostasPreenchido);

        Assert.assertEquals(1, camposObrigatorios.size());
        Assert.assertEquals(camposObrigatorios.get(0).getId(), campoRespostaObrigatorio.getId());
    }

    @Test
    public void camposFormularioFaseVinculadoPessoaComCampoObrigatorioTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        TipoRelacionamento tipoRelacionamento = new TipoRelacionamento();

        tipoRelacionamento.setId(6);
        tipoRelacionamento.setNome("Procurador");

        Mockito.when(this.tipoRelacionamentoServico.getByNome(Mockito.anyString())).thenReturn(tipoRelacionamento);

        Set<CampoFormulario> camposPreenchido = new HashSet<>();
        List<RespostaFormularioDTO> respostasPreenchido = new ArrayList<>();
        List<VinculoPessoaDTO> vinculosPessoas = new ArrayList<>();

        final String expressao = "[[{\"tipo_regra\": \"GC\", \"atributo\": \"tipo_relacionamento\", \"valores\":[\"Procurador\"]}, {\"tipo_regra\":\"FF\", \"campo_resposta\": 1, \"valores\":[\"CNPJ\"]}]]";

        CampoFormulario campo1 = this.criarCampoFormulario(1L, "Tipo Pessoa?", TipoCampoEnum.SELECT, null, false);
        RespostaFormularioDTO resposta1 = this.criarRespostaFormulario(1L, null, Arrays.asList("cnpj"));

        CampoFormulario campo2 = this.criarCampoFormulario(2L, "Nome do Outorgante", TipoCampoEnum.TEXT, expressao, true);
        RespostaFormularioDTO resposta2 = this.criarRespostaFormulario(2L, "Fulano de tal", null);

        camposPreenchido.add(campo1);
        camposPreenchido.add(campo2);

        VinculoPessoaDTO pessoaProcurador = this.criarVinculoPessoa(6);
        vinculosPessoas.add(pessoaProcurador);

        respostasPreenchido.add(resposta1);
        respostasPreenchido.add(resposta2);

        List<CampoFormulario> camposObrigatorios = (List<CampoFormulario>) metodoValidaFormularioFase.invoke(this.dossieProdutoValidacaoHelper, camposPreenchido, respostasPreenchido, vinculosPessoas, new ArrayList<>(), new ArrayList<>());
        // List<CampoFormulario> camposObrigatorios = this.dossieProdutoValidacaoHelper.validarFormularioFase(camposPreenchido, respostasPreenchido, vinculosPessoas, new ArrayList<>(), new
        // ArrayList<>());

        Assert.assertEquals(0, camposObrigatorios.size());
    }

    @Test
    public void camposFormularioFaseVinculadoPessoaComCampoObrigatorioSemRespostaTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        TipoRelacionamento tipoRelacionamento = new TipoRelacionamento();

        tipoRelacionamento.setId(6);
        tipoRelacionamento.setNome("Procurador");

        Mockito.when(this.tipoRelacionamentoServico.getByNome(Mockito.anyString())).thenReturn(tipoRelacionamento);

        Set<CampoFormulario> camposPreenchido = new HashSet<>();
        List<RespostaFormularioDTO> respostasPreenchido = new ArrayList<>();
        List<VinculoPessoaDTO> vinculosPessoas = new ArrayList<>();

        final String expressao = "[[{\"tipo_regra\": \"GC\", \"atributo\": \"tipo_relacionamento\", \"valores\":[\"Procurador\"]}, {\"tipo_regra\":\"FF\", \"campo_resposta\": 1, \"valores\":[\"CNPJ\"]}]]";

        CampoFormulario campo1 = this.criarCampoFormulario(1L, "Tipo Pessoa?", TipoCampoEnum.SELECT, null, false);
        RespostaFormularioDTO resposta1 = this.criarRespostaFormulario(1L, null, Arrays.asList("CNPJ"));

        CampoFormulario campo2 = this.criarCampoFormulario(2L, "Nome do Outorgante", TipoCampoEnum.TEXT, expressao, true);

        camposPreenchido.add(campo1);
        camposPreenchido.add(campo2);

        VinculoPessoaDTO pessoaProcurador = this.criarVinculoPessoa(6);
        vinculosPessoas.add(pessoaProcurador);

        respostasPreenchido.add(resposta1);

        List<CampoFormulario> camposObrigatorios = (List<CampoFormulario>) metodoValidaFormularioFase.invoke(this.dossieProdutoValidacaoHelper, camposPreenchido, respostasPreenchido, vinculosPessoas, new ArrayList<>(), new ArrayList<>());
        // List<CampoFormulario> camposObrigatorios = this.dossieProdutoValidacaoHelper.validarFormularioFase(camposPreenchido, respostasPreenchido, vinculosPessoas, new ArrayList<>(), new
        // ArrayList<>());

        Assert.assertEquals(1, camposObrigatorios.size());
        Assert.assertEquals(camposObrigatorios.get(0).getId(), campo2.getId());
    }

    @Test
    public void camposFormularioFaseVinculadoProdutoComCampoObrigatorioSemRespostaTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Set<CampoFormulario> camposPreenchido = new HashSet<>();
        List<RespostaFormularioDTO> respostasPreenchido = new ArrayList<>();
        List<ProdutoContratadoDTO> produtos = new ArrayList<>();

        final String expressao = "[[{\"tipo_regra\": \"GP\", \"atributo\": \"codigo_operacao\", \"valores\":[\"033\"]}, {\"tipo_regra\":\"FF\", \"campo_resposta\": 1, \"valores\":[\"CNPJ\"]}]]";

        CampoFormulario campo1 = this.criarCampoFormulario(1L, "Tipo Pessoa?", TipoCampoEnum.SELECT, null, false);
        RespostaFormularioDTO resposta1 = this.criarRespostaFormulario(1L, null, Arrays.asList("cnpj"));

        CampoFormulario campo2 = this.criarCampoFormulario(2L, "Nome do Outorgante", TipoCampoEnum.TEXT, expressao, true);

        camposPreenchido.add(campo1);
        camposPreenchido.add(campo2);

        respostasPreenchido.add(resposta1);

        ProdutoContratadoDTO produto = this.criarProduto(null, null, 33);
        produtos.add(produto);

        List<CampoFormulario> camposObrigatorios = (List<CampoFormulario>) metodoValidaFormularioFase.invoke(this.dossieProdutoValidacaoHelper, camposPreenchido, respostasPreenchido, new ArrayList<>(), produtos, new ArrayList<>());
        // List<CampoFormulario> camposObrigatorios = this.dossieProdutoValidacaoHelper.validarFormularioFase(camposPreenchido, respostasPreenchido, new ArrayList<>(), produtos, new ArrayList<>());

        Assert.assertEquals(1, camposObrigatorios.size());
        Assert.assertEquals(camposObrigatorios.get(0).getId(), campo2.getId());
    }

    @Test
    public void camposFormularioFaseVinculadoProdutoComCampoObrigatorioComRespostaTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Set<CampoFormulario> camposPreenchido = new HashSet<>();
        List<RespostaFormularioDTO> respostasPreenchido = new ArrayList<>();
        List<ProdutoContratadoDTO> produtos = new ArrayList<>();

        final String expressao = "[[{\"tipo_regra\": \"GP\", \"atributo\": \"codigo_modalidade\", \"valores\":[\"010\"]}, {\"tipo_regra\":\"FF\", \"campo_resposta\": 1, \"valores\":[\"CNPJ\"]}]]";

        CampoFormulario campo1 = this.criarCampoFormulario(1L, "Tipo Pessoa?", TipoCampoEnum.SELECT, null, false);
        RespostaFormularioDTO resposta1 = this.criarRespostaFormulario(1L, null, Arrays.asList("cnpj"));

        CampoFormulario campo2 = this.criarCampoFormulario(2L, "Nome do Outorgante", TipoCampoEnum.TEXT, expressao, true);
        RespostaFormularioDTO resposta2 = this.criarRespostaFormulario(2L, "fulano de tal", null);

        camposPreenchido.add(campo1);
        camposPreenchido.add(campo2);

        respostasPreenchido.add(resposta1);
        respostasPreenchido.add(resposta2);

        ProdutoContratadoDTO produto = this.criarProduto(null, 10, null);
        produtos.add(produto);

        List<CampoFormulario> camposObrigatorios = (List<CampoFormulario>) metodoValidaFormularioFase.invoke(this.dossieProdutoValidacaoHelper, camposPreenchido, respostasPreenchido, new ArrayList<>(), produtos, new ArrayList<>());
        // List<CampoFormulario> camposObrigatorios = this.dossieProdutoValidacaoHelper.validarFormularioFase(camposPreenchido, respostasPreenchido, new ArrayList<>(), produtos, new ArrayList<>());

        Assert.assertEquals(0, camposObrigatorios.size());
    }

    @Test
    public void camposFormularioFaseVinculadoGarantiaComCampoObrigatorioComRespostaTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Set<CampoFormulario> camposPreenchido = new HashSet<>();
        List<RespostaFormularioDTO> respostasPreenchido = new ArrayList<>();
        List<GarantiaInformadaDTO> garantias = new ArrayList<>();

        final String expressao = "[[{\"tipo_regra\": \"GG\", \"atributo\": \"codigo_bacen\", \"valores\":[\"0424\"]}, {\"tipo_regra\":\"FF\", \"campo_resposta\": 1, \"valores\":[\"CNPJ\"]}]]";

        CampoFormulario campo1 = this.criarCampoFormulario(1L, "Tipo Pessoa?", TipoCampoEnum.SELECT, null, false);
        RespostaFormularioDTO resposta1 = this.criarRespostaFormulario(1L, null, Arrays.asList("cnpj"));

        CampoFormulario campo2 = this.criarCampoFormulario(2L, "Nome do Outorgante", TipoCampoEnum.TEXT, expressao, true);
        RespostaFormularioDTO resposta2 = this.criarRespostaFormulario(2L, "fulano de tal", null);

        camposPreenchido.add(campo1);
        camposPreenchido.add(campo2);

        respostasPreenchido.add(resposta1);
        respostasPreenchido.add(resposta2);

        GarantiaInformadaDTO garantia = this.cirarGarantia(424);
        garantias.add(garantia);

        List<CampoFormulario> camposObrigatorios = (List<CampoFormulario>) metodoValidaFormularioFase.invoke(this.dossieProdutoValidacaoHelper, camposPreenchido, respostasPreenchido, new ArrayList<>(), new ArrayList<>(), garantias);
        // List<CampoFormulario> camposObrigatorios = this.dossieProdutoValidacaoHelper.validarFormularioFase(camposPreenchido, respostasPreenchido, new ArrayList<>(), new ArrayList<>(), garantias);

        Assert.assertEquals(0, camposObrigatorios.size());
    }

    @Test
    public void camposFormularioFaseVinculadoGarantiaComCampoObrigatorioSemRespostaTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Set<CampoFormulario> camposPreenchido = new HashSet<>();
        List<RespostaFormularioDTO> respostasPreenchido = new ArrayList<>();
        List<GarantiaInformadaDTO> garantias = new ArrayList<>();

        final String expressao = "[[{\"tipo_regra\": \"GG\", \"atributo\": \"codigo_bacen\", \"valores\":[\"0424\"]}, {\"tipo_regra\":\"FF\", \"campo_resposta\": 1, \"valores\":[\"CNPJ\"]}]]";

        CampoFormulario campo1 = this.criarCampoFormulario(1L, "Tipo Pessoa?", TipoCampoEnum.SELECT, null, false);
        RespostaFormularioDTO resposta1 = this.criarRespostaFormulario(1L, null, Arrays.asList("cnpj"));

        CampoFormulario campo2 = this.criarCampoFormulario(2L, "Nome do Outorgante", TipoCampoEnum.TEXT, expressao, true);

        camposPreenchido.add(campo1);
        camposPreenchido.add(campo2);

        respostasPreenchido.add(resposta1);

        GarantiaInformadaDTO garantia = this.cirarGarantia(424);
        garantias.add(garantia);

        List<CampoFormulario> camposObrigatorios = (List<CampoFormulario>) metodoValidaFormularioFase.invoke(this.dossieProdutoValidacaoHelper, camposPreenchido, respostasPreenchido, new ArrayList<>(), new ArrayList<>(), garantias);
        // List<CampoFormulario> camposObrigatorios = this.dossieProdutoValidacaoHelper.validarFormularioFase(camposPreenchido, respostasPreenchido, new ArrayList<>(), new ArrayList<>(), garantias);

        Assert.assertEquals(1, camposObrigatorios.size());
        Assert.assertEquals(camposObrigatorios.get(0).getId(), campo2.getId());
    }
}
