/*
 * Copyright (c) 2017 Caixa Econômica Federal. Todos os direitos
 * reservados.
 *
 * Caixa Econômica Federal - simtr-api
 *
 * Este software foi desenvolvido sob demanda da CAIXA e está
 * protegido por leis de direitos autorais e tratados internacionais. As
 * condições de cópia e utilização da totalidade ou partes dependem de autorização da
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

public class SicpfException extends RuntimeException {

    /**
     * Atributo serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private final boolean servicoDisponivel;

    public SicpfException(String mensagem, boolean servicoDisponivel) {
        super(mensagem);
        this.servicoDisponivel = servicoDisponivel;
    }

    public SicpfException(String mensagem, Throwable causa, boolean servicoDisponivel) {
        super(mensagem, causa);
        this.servicoDisponivel = servicoDisponivel;
    }

    public boolean isServicoDisponivel() {
        return servicoDisponivel;
    }

}
