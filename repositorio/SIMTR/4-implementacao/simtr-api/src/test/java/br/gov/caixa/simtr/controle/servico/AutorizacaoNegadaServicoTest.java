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

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.pedesgo.arquitetura.util.UtilParametro;
import br.gov.caixa.pedesgo.arquitetura.util.UtilWS;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;

/**
 * <p>
 * AutorizacaoNegadaServico</p>
 *
 * <p>
 * Descrição: Autorização Serviço Teste</p>
 *
 * <br><b>Empresa:</b> Cef - Caixa Econômica Federal
 *
 *
 * @author f538462
 *
 * @version 1.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({UtilParametro.class, UtilWS.class, UtilJson.class})
public class AutorizacaoNegadaServicoTest {

    @Mock
    private DossieProduto dossieProduto;
    
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private final AutorizacaoNegadaServico autorizacaoNegadaServico = new AutorizacaoNegadaServico();
    
    @Mock
    private final ProdutoServico produtoServico = new ProdutoServico();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testeAutorizacaoTest() {
    	Mockito.when(produtoServico.getByOperacaoModalidade(1, 1, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE)).thenReturn(null);
    	autorizacaoNegadaServico.createAutorizacaoNegada(1L, TipoPessoaEnum.F, 1, 1, "SIMTR", "Forbidden");
    	
    }
}
