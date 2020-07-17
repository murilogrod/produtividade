/*
 * Copyright (c) 2018 Caixa Econômica Federal. Todos os direitos
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
package br.gov.caixa.simtr.modelo.enumerator;

import java.text.MessageFormat;

public enum OrigemDocumentoEnum {

    A("Cópia Autenticada Administrativamente"),
    S("Cópia Simples"),
    C("Cópia Autenticada em Cartório"),
    O("Documento Original");

    private final String descricao;

    private OrigemDocumentoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static OrigemDocumentoEnum getByCodigo(final String codigo) {
        for (final OrigemDocumentoEnum enumTemplate : OrigemDocumentoEnum.values()) {
            if (enumTemplate.name().equalsIgnoreCase(codigo)) {
                return enumTemplate;
            }
        }

        throw new IllegalArgumentException(MessageFormat.format("Origem Documento não conhecido: {0}", codigo));
    }

}
