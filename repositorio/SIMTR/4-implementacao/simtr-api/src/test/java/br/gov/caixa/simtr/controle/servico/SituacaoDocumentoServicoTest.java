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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.InstanciaDocumento;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDocumentoEnum;
import java.util.Calendar;

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
public class SituacaoDocumentoServicoTest {

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private Logger logger;

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<SituacaoDocumento> query;
    
    @Mock
    private TypedQuery<Calendar> queryAlteracao;

    @Mock
    private CanalServico canalServico;
    
    @InjectMocks
    private SituacaoDocumentoServico servico = new SituacaoDocumentoServico();

    @Mock
    private final Map<SituacaoDocumentoEnum, SituacaoDocumento> mapaSituacaoDocumentoEnum = new HashMap<>();

    List<SituacaoDocumento> situacoes;

    /**
     * <p>
     * Método responsável por inicializar os mocks</p>.
     *
     * @author f538462
     *
     */
    @Before
    public void setUpPostConstruct() {
        MockitoAnnotations.initMocks(this);
        
        Canal canal = new Canal();
        canal.setSigla("SIGLA_SISTEMA");
        canal.setClienteSSO("cli-web-xxx");
        
        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(canal);
        Mockito.when(this.keycloakService.getMatricula()).thenReturn("service-account-cli-web-dep-xxx");
    }

    @Test(expected = SimtrConfiguracaoException.class)
    public void getBySistuacaoDocumentoEnumFalha() {
        mockSituacoesFalha();
        mockCreateQuery();
        mockResultList();
        this.servico.getBySituacaoDocumentoEnum(SituacaoDocumentoEnum.CRIADO);
    }

    @Test
    public void getBySistuacaoDocumentoEnum() {
        mockSituacoes();
        mockCreateQuery();
        mockResultList();
        this.servico.getBySituacaoDocumentoEnum(SituacaoDocumentoEnum.CRIADO);
    }

    @Test
    public void registraNovaSituacaoInstanciaDocumentoTest() {
        mockCreateQuery();
        this.servico.registraNovaSituacaoInstanciaDocumento(new InstanciaDocumento(), new SituacaoDocumento());
    }

    @Test
    public void listTest() {
        mockCreateQuery();
        this.servico.list();
    }

    private void mockCreateQuery() {
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(SituacaoDocumento.class))).thenReturn(query);
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Calendar.class))).thenReturn(queryAlteracao);
        Mockito.when(queryAlteracao.getSingleResult()).thenReturn(Calendar.getInstance());
    }

    private void mockResultList() {
        Mockito.when(query.getResultList()).thenReturn(situacoes);
    }

    private void mockSituacoes() {
        situacoes = new ArrayList<>();
        SituacaoDocumento situacao = new SituacaoDocumento();
        situacao.setId(1);
        situacao.setNome("Criado");
        situacoes.add(situacao);
    }

    private void mockSituacoesFalha() {
        situacoes = new ArrayList<>();
        SituacaoDocumento situacao = new SituacaoDocumento();
        situacao.setId(1);
        situacoes.add(situacao);
    }

}
