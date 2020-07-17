package br.gov.caixa.simtr.modelo.enumerator;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.caixa.simtr.modelo.enumerator.serializer.TipoApensoSerializer;

@JsonSerialize(using = TipoApensoSerializer.class)
public enum TipoApensoEnum {

    AX("Anexo", Boolean.FALSE, Boolean.TRUE),
    CT("Contratação", Boolean.FALSE, Boolean.TRUE),
    GF("Gestão Formal", Boolean.FALSE, Boolean.FALSE),
    GO("Gestão Operacional", Boolean.FALSE, Boolean.FALSE),
    OC("Demandas de Orgãos de Controle", Boolean.FALSE, Boolean.TRUE),
    PG("Pagamento", Boolean.FALSE, Boolean.FALSE),
    PP("Penalidade de Processo", Boolean.TRUE, Boolean.FALSE),
    PC("Penalidade de Contrato", Boolean.TRUE, Boolean.FALSE),
    RC("Ressarcimento de Contrato", Boolean.TRUE, Boolean.FALSE);

    private final String descricao;
    private final boolean siclgObrigatorio;
    private final boolean siclgProibido;

    private TipoApensoEnum(final String description, boolean siclgObrigatorio, boolean siclgProibido) {
        this.descricao = description;
        this.siclgObrigatorio = siclgObrigatorio;
        this.siclgProibido = siclgProibido;
    }

    public static TipoApensoEnum getByCodigo(final String codigo) {
        for (final TipoApensoEnum item : TipoApensoEnum.values()) {
            if (item.name().equals(codigo)) {
                return item;
            }
        }

        throw new IllegalArgumentException("Tipo de apenso não encontrado: " + codigo);
    }

    public String getDescricao() {
        return this.descricao;
    }

    public boolean isSiclgObrigatorio() {
        return siclgObrigatorio;
    }

    public boolean isSiclgProibido() {
        return siclgProibido;
    }

}
