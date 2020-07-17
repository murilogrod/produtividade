/*
 * Copyright (c) 2017 Caixa Econômica Federal. Todos os direitos
 * reservados.
 *
 * Caixa Econômica Federal - SECLI-ejb
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
package br.gov.caixa.simtr.modelo.enumerator;

/**
 * <p>
 * EstadoCivilEnum</p>
 *
 * <p>
 * Descrição: Descrição do tipo</p>
 *
 * <br><b>Empresa:</b> Cef - Caixa Econômica Federal
 *
 *
 * @author TO-Brasil
 *
 * @version 1.0
 */
public enum EstadoCivilEnum {
    NAO_INFORMADO("Não Informado"),
    SOLTEIRO("Solteiro (a)"),
    CASADO("Casado (a)"),
    DIVORCIADO("Divorciado (a)"),
    SEPARADO_JUDICIALMENTE("Separado (a) Judicialmente"),
    VIUVO("Viúvo (a)"),
    UNIAO_ESTAVEL("Com União Estável"),
    CASADO_COMUNHAO_TOTAL_BENS("Casado (a) com comunhão total de bens"),
    CASADO_SEM_COMUNHAO_TOTAL_BENS("Casado (a) sem comunhão de bens"),
    CASADO_COMUNHAO_PARCIAL_BENS("Casado (a) com comunhão parcial de bens");

    private String description;

    EstadoCivilEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

}
