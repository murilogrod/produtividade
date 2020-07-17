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

import br.gov.caixa.simtr.visao.SimtrExceptionDTO;

@Deprecated
public class SimtrException extends RuntimeException {

    /**
     * Atributo serialVersionUID.
     */
    private static final long serialVersionUID = 3L;

    private final Boolean falhaFornecedor;
    private final Boolean falhaPermissao;
    private final Boolean falhaRequisicao;
    private final Boolean falhaSIECM;
    private final Boolean falhaSIMTR;

    public SimtrException(String mensagem) {
        super(mensagem);
        this.falhaFornecedor = null;
        this.falhaPermissao = null;
        this.falhaRequisicao = null;
        this.falhaSIECM = null;
        this.falhaSIMTR = null;
    }

    public SimtrException(String mensagem, Throwable causa) {
        super(mensagem, causa);
        this.falhaFornecedor = null;
        this.falhaPermissao = null;
        this.falhaRequisicao = null;
        this.falhaSIECM = null;
        this.falhaSIMTR = null;
    }
    
    public SimtrException(String mensagem, Throwable causa, SimtrExceptionDTO simtrExceptionDTO) {
        super(mensagem, causa);
        this.falhaFornecedor = simtrExceptionDTO.getFalhaFornecedor();
        this.falhaPermissao = simtrExceptionDTO.getFalhaPermissao();
        this.falhaRequisicao = simtrExceptionDTO.getFalhaRequisicao();
        this.falhaSIECM = simtrExceptionDTO.getFalhaSIECM();
        this.falhaSIMTR = simtrExceptionDTO.getFalhaSIMTR();
    }
    
    public SimtrException(String mensagem, SimtrExceptionDTO simtrExceptionDTO) {
        super(mensagem);
        this.falhaFornecedor = simtrExceptionDTO.getFalhaFornecedor();
        this.falhaPermissao = simtrExceptionDTO.getFalhaPermissao();
        this.falhaRequisicao = simtrExceptionDTO.getFalhaRequisicao();
        this.falhaSIECM = simtrExceptionDTO.getFalhaSIECM();
        this.falhaSIMTR = simtrExceptionDTO.getFalhaSIMTR();
    }

    public Boolean getFalhaFornecedor() {
        return falhaFornecedor;
    }

    public Boolean getFalhaPermissao() {
        return falhaPermissao;
    }

    public Boolean getFalhaRequisicao() {
        return falhaRequisicao;
    }

    public Boolean getFalhaSIECM() {
        return falhaSIECM;
    }

    public Boolean getFalhaSIMTR() {
        return falhaSIMTR;
    }

}
