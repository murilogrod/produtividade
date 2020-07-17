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

/**
 * <p>Exceção utilizada para representar uma falha na configuração do SIMTR.</p>
 * <p>Codigo de retorno sugerido para a camada de visão: 500</p>
 * @author c090347
 */
public class SimtrConfiguracaoException extends RuntimeException {

    /**
     * Atributo serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    public SimtrConfiguracaoException(String mensagem) {
        super(mensagem);
    }

    public SimtrConfiguracaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

}
