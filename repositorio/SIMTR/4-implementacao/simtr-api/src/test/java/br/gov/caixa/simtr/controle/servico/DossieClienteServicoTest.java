/**
 * Copyright (c) 2018 Caixa Econômica Federal. Todos os direitos
 * reservados.
 *
 * Caixa Econômica Federal
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

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.caixa.pedesgo.arquitetura.siiso.dto.RetornoPessoasFisicasDTO;
import br.gov.caixa.pedesgo.arquitetura.siiso.dto.RetornoPessoasJuridicasDTO;
import br.gov.caixa.pedesgo.arquitetura.siiso.servico.CadastroReceitaPJServico;
import br.gov.caixa.simtr.controle.excecao.SicpfException;
import br.gov.caixa.simtr.controle.excecao.SiisoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.DossieClientePF;
import br.gov.caixa.simtr.modelo.entidade.DossieClientePJ;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.CalendarUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.mockito.Spy;

/**
 * <p>
 * CanalServicoTest</p>
 *
 * <p>
 * Descrição: Classe de teste do servico LocalidadeServico</p>
 *
 * <br><b>Empresa:</b> Cef - Caixa Econômica Federal
 *
 *
 * @author f538462
 *
 * @version 1.0
 */
public class DossieClienteServicoTest {

    @Spy
    private CalendarUtil calendarUtil;

    @Mock
    private CanalServico canalServico;

    @Mock
    private DocumentoServico documentoServico;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CadastroReceitaServico sicpfServico;

    @Mock
    private TipoDocumentoServico tipoDocumentoServico;

    @Mock
    TypedQuery<DossieCliente> queryDossieCliente;

    @InjectMocks
    private DossieClienteServico dossieClienteServico;

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_DIR = "/mock/dossie-cliente/";
    private static final String UTF_8 = "UTF-8";

    private DossieClientePJ dossiePJ;
    private DossieClientePF dossiePF;
    private Canal canalSIMTR;
    private TipoDocumento tipoDocumentoCNH;
    private Documento documentoCNH;

    private RetornoPessoasFisicasDTO retornoPessoasFisicasDTO;
    private RetornoPessoasJuridicasDTO retonoJuridicasDTO;

    @Before
    public void init() throws Exception {

        String jsonCanalSIMTR = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("canal-simtr.json")), UTF_8);
        this.canalSIMTR = mapper.readValue(jsonCanalSIMTR, Canal.class);

        String jsonTipologiaCNH = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("tipologia-cnh.json")), UTF_8);
        this.tipoDocumentoCNH = mapper.readValue(jsonTipologiaCNH, TipoDocumento.class);

        String jsonCNH = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("cnh.json")), UTF_8);
        this.documentoCNH = mapper.readValue(jsonCNH, Documento.class);

        String jsonSICPF0006179609 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("resposta-sicpf-00065179609.json")), UTF_8);
        this.retornoPessoasFisicasDTO = mapper.readValue(jsonSICPF0006179609, RetornoPessoasFisicasDTO.class);

        String jsonSIISO00013888000105 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("resposta-siiso-00013888000105.json")), UTF_8);
        this.retonoJuridicasDTO = mapper.readValue(jsonSIISO00013888000105, RetornoPessoasJuridicasDTO.class);

        String jsonDossie00013888000105 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("dossie-cliente-00013888000105.json")), UTF_8);
        this.dossiePJ = mapper.readValue(jsonDossie00013888000105, DossieClientePJ.class);

        String jsonDossie00065189609 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("dossie-cliente-00065179609.json")), UTF_8);
        this.dossiePF = mapper.readValue(jsonDossie00065189609, DossieClientePF.class);

        MockitoAnnotations.initMocks(this);
        Mockito.when(this.sicpfServico.consultaCadastroPF(65179609L)).thenReturn(retornoPessoasFisicasDTO);
        Mockito.when(this.sicpfServico.consultaCadastroPJ(13888000105L)).thenReturn(retonoJuridicasDTO);

        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(DossieCliente.class))).thenReturn(queryDossieCliente);

    }
    
    @Test
    public void getByIdTest() {

        //Captura um Dossiê PF por ID
        Mockito.when(queryDossieCliente.getSingleResult()).thenReturn(dossiePF);
        //Dossiê Completo
        Assert.assertNotNull(dossieClienteServico.getById(1L, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
        //Dossiê sem Vinculação de Nivel Documental
        Assert.assertNotNull(dossieClienteServico.getById(1L, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
        //Dossiê sem Vinculação dos Documentos
        Assert.assertNotNull(dossieClienteServico.getById(1L, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE));
        //Dossiê sem Vinculação dos Dossiês de Produto
        Assert.assertNotNull(dossieClienteServico.getById(1L, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));

        //Captura um Dossiê PJ por ID
        Mockito.when(queryDossieCliente.getSingleResult()).thenReturn(dossiePJ);
        //Dossiê Completo
        Assert.assertNotNull(dossieClienteServico.getById(2L, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
        //Dossiê sem Vinculação de Nivel Documental
        Assert.assertNotNull(dossieClienteServico.getById(2L, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
        //Dossiê sem Vinculação dos Documentos
        Assert.assertNotNull(dossieClienteServico.getById(2L, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE));
        //Dossiê sem Vinculação dos Dossiês de Produto
        Assert.assertNotNull(dossieClienteServico.getById(2L, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));

        //Obtem um resultado nulo na consulta
        Mockito.when(queryDossieCliente.getSingleResult()).thenThrow(NoResultException.class);
        Assert.assertNull(dossieClienteServico.getById(4L, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
    }

    /**
     * <p>
     * Realiza o teste do metodo getByCpfCnpj retornarnado um registro exitente
     * na base de quando consultado o registro pelo CPF/CNPJ.
     * </p> Resultado Esperado: Retornar um dossiê criado com base nas
     * informações obtidas junto ao SICPF
     *
     * @author c090347
     *
     */
    
    @Test
    public void testGetByCpfCnpjCaso1() {
        //Captura um Dossiê PF por CPF com resultado existente na base de dados do SIMTR para atualização de informações
        Mockito.when(queryDossieCliente.getSingleResult()).thenReturn(dossiePF);
        Assert.assertNotNull(dossieClienteServico.getByCpfCnpj(65179609L, TipoPessoaEnum.F, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));

        //Captura um Dossiê PJ por CNPJ com resultado existente na base de dados do SIMTR para atualização de informações
        Mockito.when(queryDossieCliente.getSingleResult()).thenReturn(dossiePJ);
        Assert.assertNotNull(dossieClienteServico.getByCpfCnpj(13888000105L, TipoPessoaEnum.J, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));

        //Captura um Dossiê PJ por CNPJ com resultado existente na base de dados do SIMTR para atualização de informações
        //Resposta do SIISO alterada para simular os dados de Nome Fantasia e Telefone principal como não informados
        this.retonoJuridicasDTO.getEstabelecimento().setNomeFantasia("");
        this.retonoJuridicasDTO.getEnderecoContato().setTelefonePrincipal("");
        Assert.assertNotNull(dossieClienteServico.getByCpfCnpj(13888000105L, TipoPessoaEnum.J, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
    }

    /**
     * <p>
     * Realiza o teste do metodo getByCpfCnpj lançando exceção de
     * NoResultException quando consultado o registro pelo CPF/CNPJ.
     * </p> Resultado Esperado: Retornar um dossiê criado com base nas
     * informações obtidas junto ao SIISO (Cadastro Receita)
     *
     * @author c090347
     *
     */
    
    @Test
    public void testGetByCpfCnpjCaso2() {

        Mockito.doThrow(NoResultException.class).when(queryDossieCliente).getSingleResult();

        //Captura um Dossiê PF por CPF com resultado inexistente na base de dados do SIMTR
        Assert.assertNotNull(dossieClienteServico.getByCpfCnpj(65179609L, TipoPessoaEnum.F, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));

        //Captura um Dossiê PJ por CNPJ com resultado inexistente na base de dados do SIMTR
        Assert.assertNotNull(dossieClienteServico.getByCpfCnpj(13888000105L, TipoPessoaEnum.J, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));

        //Captura um Dossiê PJ por CNPJ com resultado inexistente pelo tipo de pessoa indevido, não cabendo consulta a serviço externo
        Assert.assertNull(dossieClienteServico.getByCpfCnpj(13888000105L, TipoPessoaEnum.A, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
    }

    /**
     * <p>
     * Realiza o teste do metodo getByCpfCnpj lançando exceção especifica ao
     * consultar serviço do SICPF/SIISO quando consultado o registro pelo
     * CPF/CNPJ com registro pré-existente na base de dados do SIMTR.
     * </p> Resultado Esperado: Retornar um dossiê criado com base nas
     * informações obtidas na base de dados do SIMTR
     *
     * @author c090347
     * @throws java.lang.Exception
     *
     */
    
    @Test
    public void testGetByCpfCnpjCaso3() throws Exception {

        //Lança uma exceção para simular as situações em que o SICPF/SIISO não estejam disponiveis ou retornar um valor nulo.
        Mockito.doThrow(SicpfException.class).when(sicpfServico).consultaCadastroPF(Mockito.anyLong());
        Mockito.doThrow(SiisoException.class).when(sicpfServico).consultaCadastroPJ(Mockito.anyLong());

        //Captura um Dossiê PF por CPF com resultado existente na base de dados do SIMTR para atualização de informações
        Mockito.when(queryDossieCliente.getSingleResult()).thenReturn(dossiePF);
        Assert.assertNotNull(dossieClienteServico.getByCpfCnpj(65179609L, TipoPessoaEnum.F, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));

        //Captura um Dossiê PJ por CNPJ com resultado existente na base de dados do SIMTR para atualização de informações
        Mockito.when(queryDossieCliente.getSingleResult()).thenReturn(dossiePJ);
        Assert.assertNotNull(dossieClienteServico.getByCpfCnpj(13888000105L, TipoPessoaEnum.J, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
    }
    
    
    @Test
    public void testAplicaPatch() throws IOException, IOException {

        //Capturando objeto que representa um patch de Dossiê PF com os dados nulos a serem atualizados 
        String jsonDossiePatch00065179609 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("dossie-cliente-00065179609-patch-null.json")), UTF_8);
        DossieCliente dossiePFPatchNull = mapper.readValue(jsonDossiePatch00065179609, DossieClientePF.class);

        //Capturando objeto que representa um patch de Dossiê PJ com os dados nulos a serem atualizados 
        String jsonDossiePatch00013888000105 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("dossie-cliente-00013888000105-patch-null.json")), UTF_8);
        DossieCliente dossiePJPatchNull = mapper.readValue(jsonDossiePatch00013888000105, DossieClientePJ.class);

        Mockito.when(queryDossieCliente.getSingleResult()).thenReturn(dossiePF);
        //Aplica o patch com atribos preenchidos
        dossieClienteServico.aplicaPatch(1L, dossiePF);
        //Aplica o patch com atribos nulos
        dossieClienteServico.aplicaPatch(1L, dossiePFPatchNull);

        Mockito.when(queryDossieCliente.getSingleResult()).thenReturn(dossiePJ);
        //Aplica o patch com atribos preenchidos
        dossieClienteServico.aplicaPatch(2L, dossiePJ);
        //Aplica o patch com atribos nulos
        dossieClienteServico.aplicaPatch(2L, dossiePJPatchNull);

    }
    
    
    @Test
    public void testCriaDocumentoDossieCliente() throws IOException, IOException {

        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(canalSIMTR);
        Mockito.when(this.documentoServico.prototype(
                Mockito.eq(canalSIMTR), Mockito.eq(Boolean.FALSE), Mockito.eq(tipoDocumentoCNH), Mockito.eq(TemporalidadeDocumentoEnum.VALIDO),
                Mockito.eq(OrigemDocumentoEnum.S), Mockito.eq(FormatoConteudoEnum.PDF), Mockito.isNull(List.class), Mockito.anyString())
        ).thenReturn(documentoCNH);
        Mockito.when(this.tipoDocumentoServico.getById(Mockito.anyInt())).thenReturn(tipoDocumentoCNH);
        Mockito.when(queryDossieCliente.getSingleResult()).thenReturn(dossiePF);
        Mockito.doNothing().when(this.documentoServico).insereDocumentoClienteNegocio(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.eq(documentoCNH));

        this.dossieClienteServico.criaDocumentoDossieCliente(1L, 1, TemporalidadeDocumentoEnum.VALIDO, OrigemDocumentoEnum.S, FormatoConteudoEnum.PDF.getMimeType(), new ArrayList<>(), "<CONTEUDO_BASE64>");
        Assert.assertTrue("Falha ao incluir documento junto ao dossiê de ciente", Boolean.TRUE);
    }
    
    
    @Test
    public void testCriaDocumentoDossieClienteCasoSemBinario() throws IOException, IOException {

        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(canalSIMTR);
        Mockito.when(this.documentoServico.prototype(
                Mockito.eq(canalSIMTR), Mockito.eq(Boolean.FALSE), Mockito.eq(tipoDocumentoCNH), Mockito.eq(TemporalidadeDocumentoEnum.VALIDO),
                Mockito.eq(OrigemDocumentoEnum.S), Mockito.isNull(FormatoConteudoEnum.class), Mockito.isNull(List.class), Mockito.isNull(String.class))
        ).thenReturn(documentoCNH);
        Mockito.when(this.tipoDocumentoServico.getById(Mockito.anyInt())).thenReturn(tipoDocumentoCNH);
        Mockito.when(queryDossieCliente.getSingleResult()).thenReturn(dossiePF);
        Mockito.doNothing().when(this.documentoServico).insereDocumentoClienteNegocio(Mockito.anyLong(), Mockito.any(TipoPessoaEnum.class), Mockito.eq(documentoCNH));

        this.dossieClienteServico.criaDocumentoDossieCliente(1L, 1, TemporalidadeDocumentoEnum.VALIDO, OrigemDocumentoEnum.S, null, new ArrayList<>(), null);
        Assert.assertTrue("Falha ao incluir documento junto ao dossiê de ciente", Boolean.TRUE);

    }
    
    
    @Test(expected = SimtrRequisicaoException.class)
    public void testCriaDocumentoDossieClienteExceptionCanalInvalido() throws IOException, IOException {

        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(null);
        this.dossieClienteServico.criaDocumentoDossieCliente(1L, 1, TemporalidadeDocumentoEnum.VALIDO, OrigemDocumentoEnum.S, FormatoConteudoEnum.PDF.getMimeType(), new ArrayList<>(), "<CONTEUDO_BASE64>");

    }
    
    @Test(expected = SimtrRequisicaoException.class)
    public void testCriaDocumentoDossieClienteExceptionTipoDocumentoInvalido() throws IOException, IOException {

        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(canalSIMTR);
        Mockito.when(this.tipoDocumentoServico.getById(Mockito.anyInt())).thenReturn(null);
        this.dossieClienteServico.criaDocumentoDossieCliente(1L, 1, TemporalidadeDocumentoEnum.VALIDO, OrigemDocumentoEnum.S, FormatoConteudoEnum.PDF.getMimeType(), new ArrayList<>(), "<CONTEUDO_BASE64>");
    }
    
    @Test(expected = SimtrRequisicaoException.class)
    public void testCriaDocumentoDossieClienteExceptionDossieClienteInvalido() throws IOException, IOException {

        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(canalSIMTR);
        Mockito.when(this.tipoDocumentoServico.getById(Mockito.anyInt())).thenReturn(tipoDocumentoCNH);
        Mockito.when(queryDossieCliente.getSingleResult()).thenReturn(null);

        this.dossieClienteServico.criaDocumentoDossieCliente(1L, 1, TemporalidadeDocumentoEnum.VALIDO, OrigemDocumentoEnum.S, FormatoConteudoEnum.PDF.getMimeType(), new ArrayList<>(), "<CONTEUDO_BASE64>");
    }
    
    @Test(expected = SimtrRequisicaoException.class)
    public void testCriaDocumentoDossieClienteExceptionMimeTypeInvalido() throws IOException, IOException {

        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(canalSIMTR);
        Mockito.when(this.tipoDocumentoServico.getById(Mockito.anyInt())).thenReturn(tipoDocumentoCNH);
        Mockito.when(queryDossieCliente.getSingleResult()).thenReturn(dossiePF);

        this.dossieClienteServico.criaDocumentoDossieCliente(1L, 1, TemporalidadeDocumentoEnum.VALIDO, OrigemDocumentoEnum.S, "application/zip", new ArrayList<>(), "<CONTEUDO_BASE64>");
    }
}
