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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.controle.excecao.SimtrPermissaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.enumerator.CanalCaixaEnum;
import br.gov.caixa.simtr.modelo.enumerator.JanelaTemporalExtracaoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.canal.CanalManutencaoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import org.apache.commons.io.IOUtils;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

/**
 * <p>
 * CanalServicoTest</p>
 *
 * <p>
 * Descrição: Classe de teste do servico CanalServico</p>
 *
 * <br><b>Empresa:</b> Cef - Caixa Econômica Federal
 *
 *
 * @author p541915
 *
 * @version 1.0
 */
public class CanalServicoTest {

    @Mock
    private EntityManager manager;

    @Mock
    private TypedQuery<Canal> query;

    @Mock
    private TypedQuery<Calendar> queryAlteracao;

    @InjectMocks
    private CanalServico canalServico;
    
    private Canal canalSIMTR, canalSIPAN;
    
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_DIR = "/mock/canal/";
    private static final String UTF_8 = "UTF-8";

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        this.canalSIMTR = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("canal-simtr.json")), UTF_8), Canal.class);
        this.canalSIPAN = mapper.readValue(IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("canal-sipan.json")), UTF_8), Canal.class);
        
        List<Canal> listaCanais = Arrays.asList(this.canalSIMTR, this.canalSIPAN);

        Mockito.when(query.getResultList()).thenReturn(listaCanais);
        Mockito.when(queryAlteracao.getSingleResult()).thenReturn(Calendar.getInstance());
        
        Mockito.when(manager.createQuery(Mockito.anyString(), Mockito.eq(Canal.class))).thenReturn(query);
        Mockito.when(manager.createQuery(Mockito.anyString(), Mockito.eq(Calendar.class))).thenReturn(queryAlteracao);
    }

    /**
     * <p>Método responsável por testar o metodo getByClienteSSO com codigo existente</p>.
     * @author p541915
     */
    @Test
    public void getByCodigoSSOExistente() {
        Canal canal = canalServico.getByClienteSSO("cli-web-pan");
        assertEquals("SIPAN", canal.getSigla());
        assertNotNull(canal);
    }

    /**
     * <p>Método responsável por testar o metodo getByClienteSSO com codigo inexistente</p>.
     * @author p541915
     */
    @Test
    public void testGetByCodigoSSOInexistente() {
        Canal canal = canalServico.getByClienteSSO("cli-abc-web");
        assertNull(canal);
    }

    @Test(expected = SimtrPermissaoException.class)
    public void aplicaPatchTestFalha() {
        mockFind();
        CanalManutencaoDTO canalManutencaoDTO = this.mockCanalPatch();
        canalServico.aplicaPatch(2, canalManutencaoDTO);
    }

    @Test
    public void aplicaPatchSuccess() {
        mockFind();
        CanalManutencaoDTO canalManutencaoDTO = this.mockCanalPatch();
        canalServico.aplicaPatch(1, canalManutencaoDTO);
    }

    @Test(expected = SimtrEstadoImpeditivoException.class)
    public void aplicaPatchTest() {
        mockFind();
        mergeException("ix_mtrtb006_01");
        canalServico.aplicaPatch(1, new CanalManutencaoDTO());
    }

    @Test(expected = SimtrEstadoImpeditivoException.class)
    public void aplicaPatchTestCEName() {
        mockFind();
        mergeException("");
        canalServico.aplicaPatch(1, new CanalManutencaoDTO());
    }

    @Test
    public void deleteCanalByIDTest() {
        mockFind();
        canalServico.deleteCanalByID(1);
    }

    @Test(expected = SimtrEstadoImpeditivoException.class)
    public void deleteCanalByIDTestFalha() {
        mockFind();
        deleteException("fk_mtrtb003_mtrtb006");
        canalServico.deleteCanalByID(1);
    }

    @Test(expected = SimtrEstadoImpeditivoException.class)
    public void deleteCanalByIDTestFalhaCE() {
        mockFind();
        deleteException("fk_mtrtb037_mtrtb006");
        canalServico.deleteCanalByID(1);
    }

    @Test(expected = SimtrEstadoImpeditivoException.class)
    public void deleteCanalByIDTestFalhaSemCE() {
        mockFind();
        deleteException("");
        canalServico.deleteCanalByID(1);
    }

    @Test(expected = SimtrRecursoDesconhecidoException.class)
    public void deleteCanalByIDTestNull() {
        canalServico.deleteCanalByID(1);
    }

    @Test
    public void validaJanelaExtracaoM0PermitidaTest() {
        Canal canal = new Canal();
        canal.setJanelaExtracaoM0(Boolean.TRUE);
        canalServico.validaJanelaExtracaoPermitida(canal, JanelaTemporalExtracaoEnum.M0, "msgErro");
    }

    @Test
    public void validaJanelaExtracaoM30PermitidaTest() {
        Canal canal = new Canal();
        canal.setJanelaExtracaoM30(Boolean.TRUE);
        canalServico.validaJanelaExtracaoPermitida(canal, JanelaTemporalExtracaoEnum.M30, "msgErro");
    }

    @Test
    public void validaJanelaExtracaoM60PermitidaTest() {
        Canal canal = new Canal();
        canal.setJanelaExtracaoM60(Boolean.TRUE);
        canalServico.validaJanelaExtracaoPermitida(canal, JanelaTemporalExtracaoEnum.M60, "msgErro");
    }

    @Test(expected = SimtrPermissaoException.class)
    public void validaJanelaExtracaoNaoPermitidaTest() {
        Canal canal = new Canal();
        canal.setJanelaExtracaoM0(Boolean.FALSE);
        canal.setJanelaExtracaoM30(Boolean.TRUE);
        canal.setJanelaExtracaoM60(Boolean.TRUE);
        canalServico.validaJanelaExtracaoPermitida(canal, JanelaTemporalExtracaoEnum.M0, "msgErro");
    }

    private void mockFind() {
        Mockito.when(manager.find(Canal.class, 1)).thenReturn(new Canal());

    }

    private void mergeException(String ceName) {
        PSQLException psqle = new PSQLException(ceName, PSQLState.DATA_ERROR);
        ConstraintViolationException ce = new ConstraintViolationException("msg", psqle, ceName);
        PersistenceException pe = new PersistenceException("pe", ce);

        Mockito.doThrow(pe).when(manager).merge(Mockito.any(Canal.class));
    }

    private void deleteException(String ceName) {
        PSQLException psqle = new PSQLException(ceName, PSQLState.DATA_ERROR);
        ConstraintViolationException ce = new ConstraintViolationException("msg", psqle, ceName);
        PersistenceException pe = new PersistenceException("pe", ce);

        Mockito.doThrow(pe).when(manager).remove(Mockito.any(Canal.class));
    }

    private CanalManutencaoDTO mockCanalPatch() {
        CanalManutencaoDTO canalManutencaoDTO = new CanalManutencaoDTO();
        canalManutencaoDTO.setSiglaCanal("SIGLA");
        canalManutencaoDTO.setDescricaoCanal("Descrição do Canal");
        canalManutencaoDTO.setCanalCaixaEnum(CanalCaixaEnum.AGD);
        canalManutencaoDTO.setIndicadorJanelaM0(Boolean.TRUE);
        canalManutencaoDTO.setIndicadorJanelaM30(Boolean.TRUE);
        canalManutencaoDTO.setIndicadorJanelaM60(Boolean.TRUE);
        canalManutencaoDTO.setIndicadorAvaliacaoAutenticidade(Boolean.TRUE);
        canalManutencaoDTO.setIndicadorAtualizacaoDocumental(Boolean.TRUE);
        return canalManutencaoDTO;
    }

}
