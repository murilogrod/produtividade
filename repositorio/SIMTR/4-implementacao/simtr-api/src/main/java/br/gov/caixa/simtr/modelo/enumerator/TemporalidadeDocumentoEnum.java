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

public enum TemporalidadeDocumentoEnum {

    VALIDO("0", "Valido"),
    VENCIDO("1", "Vencido"),
    INCONFORME("2", "Inconforme"),
    AGUARDANDO_CONFORMIDADE("3", "Aguardando COnformidade"),
    TEMPORARIO_OCR("4", "Temporario - Extração de Dados (OCR)"),
    TEMPORARIO_ANTIFRAUDE("5", "Temporario - Antifraude"),
    TEMPORARIO_DADOS("6", "Temporario - Dados Confirmados"),
    REMOVER("R", "Remover");

    private final String id;
    private final String descricao;

    private TemporalidadeDocumentoEnum(String id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public String getId() {
        return this.id;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TemporalidadeDocumentoEnum getByCodigo(String codigo) {
        switch (codigo) {
            case "R":
                return TemporalidadeDocumentoEnum.VALIDO;
            case "0":
                return TemporalidadeDocumentoEnum.VALIDO;
            case "1":
                return TemporalidadeDocumentoEnum.VENCIDO;
            case "2":
                return TemporalidadeDocumentoEnum.INCONFORME;
            case "3":
                return TemporalidadeDocumentoEnum.AGUARDANDO_CONFORMIDADE;
            case "4":
                return TemporalidadeDocumentoEnum.TEMPORARIO_OCR;
            case "5":
                return TemporalidadeDocumentoEnum.TEMPORARIO_ANTIFRAUDE;
            case "6":
                return TemporalidadeDocumentoEnum.TEMPORARIO_DADOS;
        }

        throw new IllegalArgumentException(MessageFormat.format("Temporalidade Documento não identificada sob codigo: {0}", codigo));
    }
}
