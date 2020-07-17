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

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.caixa.pedesgo.arquitetura.servico.localidade.LocalidadeService;
import br.gov.caixa.pedesgo.arquitetura.servico.localidade.dto.LocalidadeResponseDTO;
import br.gov.caixa.simtr.controle.excecao.SimtrException;
import io.jsonwebtoken.lang.Assert;

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
public class LocalidadeServicoTest {

    @Mock
    private LocalidadeService localidadeService;

    @Mock
    private Logger logger;

    /**
     * Atributo canalServico.
     */
    @InjectMocks
    private LocalidadeServico servico = new LocalidadeServico();

    /**
     * <p>
     * Método responsável por inicializar os mocks</p>.
     *
     * @author p541915
     *
     */
    @Before
    public void setUpPostConstruct() {
        MockitoAnnotations.initMocks(this);

    }

    /**
     * <p>
     * Método responsável por testar o metodo consultaCep</p>.
     *
     * @author p541915
     * @throws Exception
     *
     */
    @Test(expected = RuntimeException.class)
    public void testConsultaCEPRunTime() throws Exception {
        Mockito.doThrow(RuntimeException.class).when(this.localidadeService).callService("25555", "555");
        this.servico.consultaCEP("25555", "555");
    }

    /**
     * <p>
     * Método responsável por testar o catch da exception</p>.
     *
     * @author p541915
     * @throws Exception
     *
     */
    @Test
    public void testConsultaCEPException() {
        try {
            Mockito.doThrow(RuntimeException.class).when(this.localidadeService).callService("25555", "555");
            this.servico.consultaCEP("25555", "555");
            Assert.isTrue(false);
        } catch (Exception e) {
            Assert.isTrue(true);
        }

    }

    @Test(expected = SimtrException.class)
    public void testConsultaCEPSimtr() throws Exception {
        Mockito.when(this.localidadeService.callService("25555", "555")).thenReturn(null);
        this.servico.consultaCEP("25555", "555");
    }

    @Test
    public void testConsultaCEPSucesso() throws Exception {
        Mockito.when(this.localidadeService.callService("25555", "555")).thenReturn(new LocalidadeResponseDTO());
        Assert.notNull(this.servico.consultaCEP("25555", "555"));
    }

}
