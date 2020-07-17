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

import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.gov.caixa.simtr.controle.excecao.SimtrPermissaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.ControleDocumento;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.JanelaTemporalExtracaoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.extracaodados.ResultadoExtracaoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.ejb.SessionContext;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.xml.bind.JAXBException;
import org.apache.commons.io.IOUtils;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UtilJson.class})
public class AvaliacaoExtracaoServicoTest {

    @Mock
    private CanalServico canalServico;

    @Mock
    private DocumentoServico documentoServico;
    
    @Mock
    private SiecmServico siecmServico;

    @Mock
    private TipoDocumentoServico tipoDocumentoServico;

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery queryListaTipologia;

    @Mock
    private TypedQuery queryDocumento;

    @Mock
    private Query queryRemoveControle;

    @Mock
    private SessionContext sessionContext;

    @InjectMocks
    private AvaliacaoExtracaoServico avaliacaoExtracaoServico;

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_DIR = "/mock/avaliacao-extracao/";
    private static final String UTF_8 = "UTF-8";

    private ResultadoExtracaoDTO resultadoRejeicaoDTO;
    private ResultadoExtracaoDTO resultadoClassificacaoCnhDTO;
    private ResultadoExtracaoDTO resultadoExtracaoCnhDTO;
    private ResultadoExtracaoDTO resultadoExtracaoClassificacaoInvalidaDTO;

    private Canal canalNaoAutorizado;
    private Canal canalSimtr;

    private Documento documentoCnhAutenticidade;
    private Documento documentoCnhExtracao;
    private Documento documentoNaoClassificado;
    private Documento documentoRejeitado;
    private Documento documentoValido;
    private Documento documentoVencido;
    private TipoDocumento tipoDocumentoCNH;

    private final String MTRADM = "MTRADM";
    private final String MTRSDNEXT = "MTRSDNEXT";

    @Before
    public void setup() throws IOException, JAXBException {
        MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(UtilJson.class);

        String jsonCanalNaoAutorizado = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("canal-nao-autorizado.json")), UTF_8);
        this.canalNaoAutorizado = mapper.readValue(jsonCanalNaoAutorizado, Canal.class);

        String jsonCanalSimtr = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("canal-simtr.json")), UTF_8);
        this.canalSimtr = mapper.readValue(jsonCanalSimtr, Canal.class);

        String jsonCnhAutenticidade = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("documento-cnh-autenticidade.json")), UTF_8);
        this.documentoCnhAutenticidade = mapper.readValue(jsonCnhAutenticidade, Documento.class);

        String jsonCnhExtracao = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("documento-cnh-extracao.json")), UTF_8);
        this.documentoCnhExtracao = mapper.readValue(jsonCnhExtracao, Documento.class);

        String jsonNaoClassificado = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("documento-nao-classificado.json")), UTF_8);
        this.documentoNaoClassificado = mapper.readValue(jsonNaoClassificado, Documento.class);

        String jsonRejeitado = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("documento-rejeitado.json")), UTF_8);
        this.documentoRejeitado = mapper.readValue(jsonRejeitado, Documento.class);

        String jsonValido = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("documento-cnh-valido.json")), UTF_8);
        this.documentoValido = mapper.readValue(jsonValido, Documento.class);

        String jsonVencido = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("documento-cnh-vencido.json")), UTF_8);
        this.documentoVencido = mapper.readValue(jsonVencido, Documento.class);

        String jsonResultadoRejeicao = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("resultado-rejeicao.json")), UTF_8);
        this.resultadoRejeicaoDTO = mapper.readValue(jsonResultadoRejeicao, ResultadoExtracaoDTO.class);

        String jsonResultadoClassificacaoCNH = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("resultado-classificacao-cnh.json")), UTF_8);
        this.resultadoClassificacaoCnhDTO = mapper.readValue(jsonResultadoClassificacaoCNH, ResultadoExtracaoDTO.class);

        String jsonResultadoExtracaoCNH = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("resultado-extracao-cnh.json")), UTF_8);
        this.resultadoExtracaoCnhDTO = mapper.readValue(jsonResultadoExtracaoCNH, ResultadoExtracaoDTO.class);

        String jsonResultadoExtracaoClassificacaoInvalida = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("resultado-classificacao-invalida.json")), UTF_8);
        this.resultadoExtracaoClassificacaoInvalidaDTO = mapper.readValue(jsonResultadoExtracaoClassificacaoInvalida, ResultadoExtracaoDTO.class);

        String jsonTipoDocumentoCNH = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("tipo-documento-cnh.json")), UTF_8);
        this.tipoDocumentoCNH = mapper.readValue(jsonTipoDocumentoCNH, TipoDocumento.class);

        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(this.canalSimtr);
        Mockito.when(this.queryRemoveControle.executeUpdate()).thenReturn(1);
        Mockito.when(this.sessionContext.isCallerInRole(MTRADM)).thenReturn(true);
        Mockito.when(this.tipoDocumentoServico.getByTipologia("0001000100020007")).thenReturn(this.tipoDocumentoCNH);
        Mockito.when(this.tipoDocumentoServico.getById(Mockito.anyInt())).thenReturn(this.tipoDocumentoCNH);
        Mockito.doNothing().when(this.documentoServico).invalidarDocumentosAtivosCliente(Mockito.anyLong(), Mockito.eq(null), Mockito.eq(null), Mockito.any(TipoDocumento.class), Mockito.anyBoolean());
    }

    @Test
    public void testListaDocumentosPendentesExtracao() {
        Mockito.when(this.queryListaTipologia.getResultList())
                .thenReturn(Arrays.asList(documentoCnhExtracao))
                .thenReturn(Arrays.asList(documentoNaoClassificado));

        Mockito.when(this.entityManager.createQuery(Mockito.anyString())).thenReturn(this.queryRemoveControle);
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Documento.class))).thenReturn(queryListaTipologia);

        //Realiza a chamada ao método incluindo tipos de documentos definidos para envio ao fornecedor externo
        Map<TipoDocumento, List<Documento>> mapaDocumentosPendenteComExternos = this.avaliacaoExtracaoServico.listaDocumentosPendentesExtracao(Boolean.TRUE);
        Assert.assertNotNull(mapaDocumentosPendenteComExternos);

        //Realiza a chamada ao método excluindo tipos de documentos definidos para envio ao fornecedor externo
        Map<TipoDocumento, List<Documento>> mapaDocumentosPendenteSemExternos = this.avaliacaoExtracaoServico.listaDocumentosPendentesExtracao(Boolean.FALSE);
        Assert.assertNotNull(mapaDocumentosPendenteSemExternos);
    }

    @Test
    public void testCapturaDocumentoExtracaoGeral() {
        Mockito.when(this.queryDocumento.getSingleResult())
                .thenReturn(documentoCnhExtracao)
                .thenReturn(documentoNaoClassificado);

        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Documento.class))).thenReturn(queryDocumento);

        //Realiza a chamada ao método utilizando a definição de tipo documento
        Documento documento1 = this.avaliacaoExtracaoServico.capturaDocumentoExtracaoByTipoDocumento(2);
        Assert.assertNotNull(documento1);

        //Realiza a chamada ao método utilizando a definição de identificador especifico do documento
        Documento documento2 = this.avaliacaoExtracaoServico.capturaDocumentoExtracaoByIdDocumento(10L);
        Assert.assertNotNull(documento2);
    }

    @Test
    public void testAtualizaInformacaoControleNaoLocalizado() {
        Mockito.when(this.queryDocumento.getSingleResult())
                .thenThrow(NoResultException.class);

        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Documento.class))).thenReturn(queryDocumento);

        try {
            this.avaliacaoExtracaoServico.atualizaInformacaoDocumentoOutsourcing("CX00-0", this.resultadoRejeicaoDTO.prototype());
            Assert.assertTrue("Código de controle inexistente deve lançar exceção", Boolean.FALSE);
        } catch (SimtrRecursoDesconhecidoException srde) {
            Assert.assertTrue(Boolean.TRUE);
        } catch (Exception e) {
            String mensagem = MessageFormat.format("A exceção capturada deve ser do tipo SimtrRecursoDesconhecidoException, mas foi lançada a exceção do tipo {0}", e.getClass().getName());
            Assert.assertTrue(mensagem, Boolean.FALSE);
        }
    }

    @Test
    public void testAtualizaInformacaoDocumentoRejeitado() {
        Mockito.when(this.queryDocumento.getSingleResult())
                .thenReturn(documentoCnhExtracao)
                .thenReturn(documentoCnhExtracao)
                .thenReturn(documentoRejeitado)
                .thenReturn(documentoVencido)
                .thenReturn(documentoValido)
                .thenReturn(documentoNaoClassificado);

        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Documento.class))).thenReturn(queryDocumento);
        Mockito.when(this.entityManager.merge(Mockito.any(Documento.class))).thenReturn(this.documentoCnhExtracao);

        //Rejeicao Bem sucedida documento rejeitado
        try {
            this.avaliacaoExtracaoServico.atualizaInformacaoDocumentoOutsourcing("CX10-1", this.resultadoRejeicaoDTO.prototype());
        } catch (Exception e) {
            String mensagem = MessageFormat.format("Caso de rejeição bem sucedida não deve lançar exceção, mas foi lançada a exceção do tipo {0}", e.getClass().getName());
            Assert.assertTrue(mensagem, Boolean.FALSE);
        }

        //Rejeicao Bem sucedida documento vencido
        try {
            this.avaliacaoExtracaoServico.atualizaInformacaoDocumentoOutsourcing("CX10-1", this.resultadoRejeicaoDTO.prototype());
        } catch (Exception e) {
            String mensagem = MessageFormat.format("Caso de rejeição bem sucedida não deve lançar exceção, mas foi lançada a exceção do tipo {0}", e.getClass().getName());
            Assert.assertTrue(mensagem, Boolean.FALSE);
        }

        //Rejeicao Bem sucedida documento valido
        try {
            this.avaliacaoExtracaoServico.atualizaInformacaoDocumentoOutsourcing("CX10-1", this.resultadoRejeicaoDTO.prototype());
        } catch (Exception e) {
            String mensagem = MessageFormat.format("Caso de rejeição bem sucedida não deve lançar exceção, mas foi lançada a exceção do tipo {0}", e.getClass().getName());
            Assert.assertTrue(mensagem, Boolean.FALSE);
        }

        //Rejeicao Bem sucedida documento sem validade
        try {
            this.avaliacaoExtracaoServico.atualizaInformacaoDocumentoOutsourcing("CX10-1", this.resultadoRejeicaoDTO.prototype());
        } catch (Exception e) {
            String mensagem = MessageFormat.format("Caso de rejeição bem sucedida não deve lançar exceção, mas foi lançada a exceção do tipo {0}", e.getClass().getName());
            Assert.assertTrue(mensagem, Boolean.FALSE);
        }
    }

    @Test
    public void testAtualizaInformacaoDocumentoClassificacao() {
        Mockito.when(this.queryDocumento.getSingleResult())
                .thenReturn(documentoNaoClassificado)
                .thenReturn(documentoNaoClassificado)
                .thenReturn(documentoCnhExtracao);

        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Documento.class))).thenReturn(queryDocumento);
        Mockito.when(this.entityManager.merge(Mockito.any(Documento.class))).thenReturn(this.documentoCnhExtracao);

        //Classificação invalida para documento não classificado
        try {
//            Mockito.when(queryDocumento.getSingleResult()).thenReturn(documentoNaoClassificado);
            this.avaliacaoExtracaoServico.atualizaInformacaoDocumentoOutsourcing("CX10-1", this.resultadoExtracaoClassificacaoInvalidaDTO.prototype());
            Assert.assertTrue("Classificação invalida para documento deve lançar exceção do tipo SimtrRequisicaoException", Boolean.FALSE);
        } catch (SimtrRequisicaoException sre) {
            Assert.assertTrue(Boolean.TRUE);
        } catch (Exception e) {
            String mensagem = MessageFormat.format("A exceção capturada deve ser do tipo SimtrRequisicaoException, mas foi lançada a exceção do tipo {0}", e.getClass().getName());
            Assert.assertTrue(mensagem, Boolean.FALSE);
        }

        //Classificação para documento não classificado
        try {
//            Mockito.when(queryDocumento.getSingleResult()).thenReturn(documentoNaoClassificado);
            this.avaliacaoExtracaoServico.atualizaInformacaoDocumentoOutsourcing("CX10-1", this.resultadoClassificacaoCnhDTO.prototype());
        } catch (Exception e) {
            String mensagem = MessageFormat.format("Caso de classificação bem sucedida não deve lançar exceção, mas foi lançada a exceção do tipo {0}", e.getClass().getName());
            Assert.assertTrue(mensagem, Boolean.FALSE);
        }

        //Classificação para documento CNH já classificado
        try {
//            Mockito.when(queryDocumento.getSingleResult()).thenReturn(documentoCnhExtracao);
            this.avaliacaoExtracaoServico.atualizaInformacaoDocumentoOutsourcing("CX10-1", this.resultadoClassificacaoCnhDTO.prototype());
        } catch (Exception e) {
            String mensagem = MessageFormat.format("Caso de classificação bem sucedida não deve lançar exceção, mas foi lançada a exceção do tipo {0}", e.getClass().getName());
            Assert.assertTrue(mensagem, Boolean.FALSE);
        }
    }

    @Test
    public void testAtualizaInformacaoDocumentoExtracao() {
        Mockito.when(this.queryDocumento.getSingleResult())
                .thenReturn(documentoCnhExtracao)
                .thenThrow(NoResultException.class)
                .thenReturn(documentoNaoClassificado)
                .thenReturn(documentoValido)
                .thenReturn(documentoVencido);

        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Documento.class))).thenReturn(queryDocumento);
        Mockito.when(this.entityManager.merge(Mockito.any(Documento.class))).thenReturn(this.documentoCnhExtracao);
        Mockito.doNothing().when(this.siecmServico).atualizaAtributosDocumento(Mockito.any(Documento.class));

        //Realiza a chamada ao método utilizando retorno para uma situação de documento localizado
        try {
            this.avaliacaoExtracaoServico.atualizaInformacaoDocumentoOutsourcing("CX10-1", this.resultadoExtracaoCnhDTO.prototype());
            Assert.assertTrue(Boolean.TRUE);
        } catch (SimtrRecursoDesconhecidoException srde) {
            Assert.assertTrue("Não deve ser lançada exceção na simulaçao de um recurso localizado", Boolean.FALSE);
        }

        //Realiza a chamada ao método utilizando retorno para uma situação de documento não localizado
        try {
            this.avaliacaoExtracaoServico.atualizaInformacaoDocumentoOutsourcing("CX00-1", this.resultadoExtracaoCnhDTO.prototype());
            Assert.assertTrue("Deve ser lançada exceção na simulaçao de um recurso não localizado", Boolean.FALSE);
        } catch (SimtrRecursoDesconhecidoException srde) {
            Assert.assertTrue(Boolean.TRUE);
        }

        //Falha na conversão do objeto que representa o corpo da mensagem para armazenamento
        try {
            PowerMockito.doThrow(new Exception("Falha de conversão do objeto para JSON")).when(UtilJson.class, "converterParaJson", Matchers.anyList());
            this.avaliacaoExtracaoServico.atualizaInformacaoDocumentoOutsourcing("CX10-1", this.resultadoExtracaoCnhDTO.prototype());
            Assert.assertTrue("Conversão de objeto invalido para JSON deve lançar exceção do tipo SimtrRequisicaoException", Boolean.FALSE);
        } catch (SimtrRequisicaoException sre) {
            Assert.assertTrue(Boolean.TRUE);
        } catch (Exception e) {
            String mensagem = MessageFormat.format("A exceção capturada deve ser do tipo SimtrRequisicaoException, mas foi lançada a exceção do tipo {0}", e.getClass().getName());
            Assert.assertTrue(mensagem, Boolean.FALSE);
        }

        //Extração para documento sem dados extraidos
        try {
            PowerMockito.doCallRealMethod().when(UtilJson.class, "converterParaJson", Matchers.anyList());
            this.avaliacaoExtracaoServico.atualizaInformacaoDocumentoOutsourcing("CX10-1", this.resultadoExtracaoCnhDTO.prototype());
        } catch (Exception e) {
            String mensagem = MessageFormat.format("Caso de extração de dados bem sucedida não deve lançar exceção, mas foi lançada a exceção do tipo {0}", e.getClass().getName());
            Assert.assertTrue(mensagem, Boolean.FALSE);
        }

        //Classificação para documento CNH já classificado e com dados extraidos
        try {
            PowerMockito.doCallRealMethod().when(UtilJson.class, "converterParaJson", Matchers.anyList());
            this.avaliacaoExtracaoServico.atualizaInformacaoDocumentoOutsourcing("CX10-1", this.resultadoExtracaoCnhDTO.prototype());
        } catch (Exception e) {
            String mensagem = MessageFormat.format("Caso de classificação bem sucedida não deve lançar exceção, mas foi lançada a exceção do tipo {0}", e.getClass().getName());
            Assert.assertTrue(mensagem, Boolean.FALSE);
        }
    }

    @Test
    public void testRemoveRegistroControle() {
        Mockito.when(this.queryRemoveControle.executeUpdate())
                .thenReturn(1)
                .thenReturn(0);

        Mockito.when(this.entityManager.createQuery(Mockito.anyString())).thenReturn(queryRemoveControle);

        //Realiza a chamada ao método utilizando retorno para uma situação código de controle localizado
        try {
            this.avaliacaoExtracaoServico.removeRegistroControle("CX10-1");
            Assert.assertTrue(Boolean.TRUE);
        } catch (SimtrRecursoDesconhecidoException srde) {
            Assert.assertTrue("Não deve ser lançada exceção na simulaçao de um recurso localizado", Boolean.FALSE);
        }

        //Realiza a chamada ao método utilizando retorno para uma situação código de controle não localizado
        try {
            this.avaliacaoExtracaoServico.removeRegistroControle("CX00-1");
            Assert.assertTrue("Deve ser lançada exceção na simulaçao de um recurso não localizado", Boolean.FALSE);
        } catch (SimtrRequisicaoException sre) {
            Assert.assertTrue(Boolean.TRUE);
        }
    }

    //******** TESTES DOS METODOS PRIVADOS ****************//
    @Test
    public void testValidaPerfilUsuario() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Method metodoValidaPerfilUsuario = this.avaliacaoExtracaoServico.getClass().getDeclaredMethod("validaPerfilUsuario");
        metodoValidaPerfilUsuario.setAccessible(Boolean.TRUE);

        //MTRSDNEXT = False && MTRADM = False
        try {
            Mockito.when(this.sessionContext.isCallerInRole(MTRADM)).thenReturn(false);
            Mockito.when(this.sessionContext.isCallerInRole(MTRSDNEXT)).thenReturn(false);
            metodoValidaPerfilUsuario.invoke(this.avaliacaoExtracaoServico);

            Assert.assertTrue("Usuário sem perfil MTRADM ou MTRSDNEXT deve lançar exceção", Boolean.FALSE);
        } catch (InvocationTargetException ite) {
            if (ite.getCause().getClass().equals(SimtrPermissaoException.class)) {
                Assert.assertTrue(Boolean.TRUE);
            } else {
                String mensagem = MessageFormat.format("A exceção capturada deve ser do tipo SimtrPermissaoException, mas foi lançada a exceção do tipo {0}", ite.getCause().getClass().getName());
                Assert.assertTrue(mensagem, Boolean.FALSE);
            }
        }

        //MTRSDNEXT = True && MTRADM = False
        try {
            Mockito.when(this.sessionContext.isCallerInRole(MTRADM)).thenReturn(false);
            Mockito.when(this.sessionContext.isCallerInRole(MTRSDNEXT)).thenReturn(true);
            metodoValidaPerfilUsuario.invoke(this.avaliacaoExtracaoServico);

            Assert.assertTrue(Boolean.TRUE);
        } catch (InvocationTargetException ite) {
            Assert.assertTrue("Usuário com perfil MTRADM ou MTRSDNEXT não deve lançar exceção", Boolean.FALSE);
        }

        //MTRSDNEXT = False && MTRADM = True
        try {
            Mockito.when(this.sessionContext.isCallerInRole(MTRADM)).thenReturn(true);
            Mockito.when(this.sessionContext.isCallerInRole(MTRSDNEXT)).thenReturn(false);
            metodoValidaPerfilUsuario.invoke(this.avaliacaoExtracaoServico);

            Assert.assertTrue(Boolean.TRUE);
        } catch (InvocationTargetException ite) {
            Assert.assertTrue("Usuário com perfil MTRADM ou MTRSDNEXT não deve lançar exceção", Boolean.FALSE);
        }

        //MTRSDNEXT = True && MTRADM = True
        try {
            Mockito.when(this.sessionContext.isCallerInRole(MTRADM)).thenReturn(true);
            Mockito.when(this.sessionContext.isCallerInRole(MTRSDNEXT)).thenReturn(true);
            metodoValidaPerfilUsuario.invoke(this.avaliacaoExtracaoServico);

            Assert.assertTrue(Boolean.TRUE);
        } catch (InvocationTargetException ite) {
            Assert.assertTrue("Usuário com perfil MTRADM ou MTRSDNEXT não deve lançar exceção", Boolean.FALSE);
        }
    }

    @Test
    public void testremoveRegistroControleCAIXA() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Method metodoRemoveRegistroControleCAIXA = this.avaliacaoExtracaoServico.getClass().getDeclaredMethod("removeRegistroControleCAIXA", String.class, Integer.TYPE);
        metodoRemoveRegistroControleCAIXA.setAccessible(Boolean.TRUE);

        Mockito.when(this.entityManager.createQuery(Mockito.anyString())).thenReturn(this.queryRemoveControle);
        int qtdeAfetados = (int) metodoRemoveRegistroControleCAIXA.invoke(this.avaliacaoExtracaoServico, "CX100-1", 10);

        Assert.assertTrue(qtdeAfetados > 0);

        qtdeAfetados = (int) metodoRemoveRegistroControleCAIXA.invoke(this.avaliacaoExtracaoServico, null, 10);

        Assert.assertTrue(qtdeAfetados > 0);
    }

    @Test
    public void testAssociaRegistroControleDocumento() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Method metodoAssociaRegistroControleDocumento = this.avaliacaoExtracaoServico.getClass().getDeclaredMethod("associaRegistroControleDocumento", Documento.class, Boolean.TYPE, Boolean.TYPE, Boolean.TYPE, JanelaTemporalExtracaoEnum.class, Boolean.TYPE);
        metodoAssociaRegistroControleDocumento.setAccessible(Boolean.TRUE);

        ControleDocumento controleDocumento = (ControleDocumento) metodoAssociaRegistroControleDocumento.invoke(this.avaliacaoExtracaoServico, new Documento(), true, true, true, JanelaTemporalExtracaoEnum.M0, true);

        Assert.assertNotNull(controleDocumento);
    }

    @Test
    public void testCapturaDocumentoExtracao() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Method metodoCapturaDocumentoExtracao = this.avaliacaoExtracaoServico.getClass().getDeclaredMethod("capturaDocumentoExtracao", Integer.class, Long.class);
        metodoCapturaDocumentoExtracao.setAccessible(Boolean.TRUE);

        TypedQuery query = Mockito.mock(TypedQuery.class);
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Documento.class))).thenReturn(query);

        //Documento obtidopor fila de tipo
        Mockito.when(query.getSingleResult()).thenReturn(documentoCnhAutenticidade);
        Documento documentoEspecifico = (Documento) metodoCapturaDocumentoExtracao.invoke(this.avaliacaoExtracaoServico, 10, null);
        Assert.assertNotNull(documentoEspecifico);

        //Documento espefico por identificador proprio
        Mockito.when(query.getSingleResult()).thenReturn(documentoCnhAutenticidade);
        Documento documentoFila = (Documento) metodoCapturaDocumentoExtracao.invoke(this.avaliacaoExtracaoServico, null, 10L);
        Assert.assertNotNull(documentoFila);

        //Documento a classificar
        Mockito.when(query.getSingleResult()).thenReturn(documentoNaoClassificado);
        Documento documentoClassificar = (Documento) metodoCapturaDocumentoExtracao.invoke(this.avaliacaoExtracaoServico, null, null);
        Assert.assertNotNull(documentoClassificar);

        //Documento não localizado para o tipo informado
        Mockito.doThrow(NoResultException.class).when(query).getSingleResult();
        Documento documentoInexistente = (Documento) metodoCapturaDocumentoExtracao.invoke(this.avaliacaoExtracaoServico, 100, null);
        Assert.assertNull(documentoInexistente);
    }
}
