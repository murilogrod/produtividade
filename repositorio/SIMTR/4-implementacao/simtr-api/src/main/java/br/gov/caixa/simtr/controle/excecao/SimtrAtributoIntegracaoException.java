/**
 * Copyright (c) 2020 Caixa Econômica Federal. Todos os direitos
 * reservados.
 *
 * Caixa Econômica Federal - simtr-api - Sistema de Crédito Rural
 *
 * Este software foi desenvolvido sob demanda da CAIXA e está
 * protegido por leis de direitos autorais e tratados internacionais. As
 * condições de cópia e utilização do total ou partes dependem de autorização da
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
package br.gov.caixa.simtr.controle.excecao;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * SimtrAtributoIntegracaoException
 * </p>
 *
 * <p>
 * Descrição: Descrição do tipo
 * </p>
 *
 * <br>
 * <b>Empresa:</b> Cef - Caixa Econômica Federal
 *
 *
 * @author f725905
 *
 * @version 1.0
 */
public class SimtrAtributoIntegracaoException extends RuntimeException {

    /** Atributo serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    private final List<String> pendencias;

    public SimtrAtributoIntegracaoException(String msg) {
	super(msg);
	List<String> ListPendencias = new ArrayList<>();
	ListPendencias.add(msg);
	this.pendencias = ListPendencias;
    }
    
    public SimtrAtributoIntegracaoException(String mensagem, List<String> pendencias) {
        super(mensagem);
        this.pendencias = pendencias;
    }

    public SimtrAtributoIntegracaoException(String mensagem, Throwable causa, List<String> pendencias) {
        super(mensagem, causa);
        this.pendencias = pendencias;
    }
    
    public List<String> getPendencias() {
        return pendencias;
    }
}
