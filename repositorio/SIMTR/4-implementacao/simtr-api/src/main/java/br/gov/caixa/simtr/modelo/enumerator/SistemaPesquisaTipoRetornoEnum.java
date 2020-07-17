package br.gov.caixa.simtr.modelo.enumerator;

import br.gov.caixa.simtr.util.ConstantesUtil;

public enum SistemaPesquisaTipoRetornoEnum {

    SICPF_0(SistemaPesquisaEnum.SICPF, 0, null, null, ConstantesUtil.KEY_REGULARIDADE, "Regular"),
    SICPF_1(SistemaPesquisaEnum.SICPF, 1, null, null, ConstantesUtil.KEY_REGULARIDADE, "Cancelado por Encerramento de Espolio"),
    SICPF_2(SistemaPesquisaEnum.SICPF, 2, null, null, ConstantesUtil.KEY_REGULARIDADE, "Suspenso"),
    SICPF_3(SistemaPesquisaEnum.SICPF, 3, null, null, ConstantesUtil.KEY_REGULARIDADE, "Cancelado por Obito Sem Espolio"),
    SICPF_4(SistemaPesquisaEnum.SICPF, 4, null, null, ConstantesUtil.KEY_REGULARIDADE, "Pendente de Regularização"),
    SICPF_5(SistemaPesquisaEnum.SICPF, 5, null, null, ConstantesUtil.KEY_REGULARIDADE, "Cancelada por Multiplicidade"),
    SICPF_8(SistemaPesquisaEnum.SICPF, 8, null, null, ConstantesUtil.KEY_REGULARIDADE, "Nulo"),
    SICPF_9(SistemaPesquisaEnum.SICPF, 9, null, null, ConstantesUtil.KEY_REGULARIDADE, "Cancelado de Oficio"),
    SINAD_2_1(SistemaPesquisaEnum.SINAD, 1, 2, null, "ocorrencias_siina", "Ocorrência SINAD"),
    SINAD_2_2(SistemaPesquisaEnum.SINAD, 1, 2, null, "ocorrencias_sinad", "Ocorrência SINAD"),
    CADIN_3(SistemaPesquisaEnum.CADIN, 2, 3, null, "retorno_ocorrencias", "Ocorrência CADIN"),
    SERASA_2(SistemaPesquisaEnum.SERASA, 3, 2, null, "pendencias_financeiras ", "Pendências Financeiras (PEFIN/REFIN)"),
    SERASA_3(SistemaPesquisaEnum.SERASA, 3, 3, null, "cheques_sem_fundo", "Cheques Sem Fundo"),
    SERASA_4(SistemaPesquisaEnum.SERASA, 3, 4, null, "protesto", "Protesto"),
    SERASA_5(SistemaPesquisaEnum.SERASA, 3, 5, null, "acao_judicial", "Ação judicial"),
    SERASA_6(SistemaPesquisaEnum.SERASA, 3, 6, null, "paticipacao_concordata", "Participação em Concordata"),
    SERASA_7(SistemaPesquisaEnum.SERASA, 3, 7, null, "falencia_concordata", "Falência e Concordata"),
    SERASA_8(SistemaPesquisaEnum.SERASA, 3, 8, null, "convenios_devedores", "Convênios Devedores"),
    SICCF_2(SistemaPesquisaEnum.SICCF, 4, 2, null, "retorno_ocorrencias", "Ocorrência SICCF"),
    SPC_2T(SistemaPesquisaEnum.SPC, 5, 2, "T", "retorno_ocorrencias", "Ocorrência SPC - Tomador"),
    SPC_2A(SistemaPesquisaEnum.SPC, 5, 2, "A", "retorno_ocorrencias", "Ocorrência SPC - Avalista"),
    SICOW_1P(SistemaPesquisaEnum.SICOW, 6, 1, "P", "ppe_primario", "PPE Primario"),
    SICOW_1R(SistemaPesquisaEnum.SICOW, 6, 1, "R", "ppe_relacionados", "PPE Relacionado"),
    SICOW_2(SistemaPesquisaEnum.SICOW, 6, 2, null, "conres", "Conres"),
    SICOW_3(SistemaPesquisaEnum.SICOW, 6, 3, null, "proibido_contratar_setor_publico", "Proibição de Contratar com o Setor Público"),
    SICOW_4(SistemaPesquisaEnum.SICOW, 6, 4, null, "interdicao_judicial", "Interdição Judicial"),
    SICOW_5_309(SistemaPesquisaEnum.SICOW, 6, 5, 309L, "informacoes_seguranca", "Segurança - CPF/CNPJ de sacador impedido de movimentar depósitos judiciais e Precatório/RPV por determinação judicial"),
    SICOW_5_310(SistemaPesquisaEnum.SICOW, 6, 5, 310L, "informacoes_seguranca", "Segurança - CPF/CNPJ vinculado a Fraude em Precatório/RPV"),
    SICOW_5_311(SistemaPesquisaEnum.SICOW, 6, 5, 311L, "informacoes_seguranca", "Segurança - CPF vinculado a ocorrência de fraude em garantia de Penhor"),
    SICOW_5_500(SistemaPesquisaEnum.SICOW, 6, 5, 500L, "informacoes_seguranca", "Segurança - CPF/CNPJ de conta credora de fraude/golpe"),
    SICOW_5_501(SistemaPesquisaEnum.SICOW, 6, 5, 501L, "informacoes_seguranca", "Segurança - CPF/CNPJ vinculado a utilização de documentação com indícios de fraude"),
    SICOW_5_502(SistemaPesquisaEnum.SICOW, 6, 5, 502L, "informacoes_seguranca", "Segurança - Comunicação de perda/roubo/furto/extravio de documentos"),
    SICOW_5_505(SistemaPesquisaEnum.SICOW, 6, 5, 505L, "informacoes_seguranca", "Segurança - CPF vinculado a registro de óbito"),
    SICOW_5_506(SistemaPesquisaEnum.SICOW, 6, 5, 506L, "informacoes_seguranca", "Segurança - CPF vinculado a registro de óbito – Óbito a confirmar"),
    SICOW_5_507(SistemaPesquisaEnum.SICOW, 6, 5, 507L, "informacoes_seguranca", "Segurança - CPF/CNPJ vinculado a alerta de alteração cadastral com suspeita de fraude"),
    SICOW_6(SistemaPesquisaEnum.SICOW, 6, 6, null, "empregados_trabalho_escravo", "Empregadores Envolvidos com Trabalho Escravo"),
    SICOW_7(SistemaPesquisaEnum.SICOW, 6, 7, null, "pld", "PLD"),
    SICOW_8(SistemaPesquisaEnum.SICOW, 6, 8, null, "partes_relacionadas", "Partes Relacionadas");

    private final SistemaPesquisaEnum sistemaPesquisaEnum;
    private final Integer numeroTipoPesquisa;
    private final Integer numeroSubTipoPesquisa;
    private final Object codigoEspecifico;
    private final String grupoOcorrencia;
    private final String descricaoOcorrencia;

    private SistemaPesquisaTipoRetornoEnum(SistemaPesquisaEnum sistemaPesquisaEnum, Integer numeroTipoPesquisa, Integer numeroSubTipoPesquisa, Object codigoEspecifico, String grupoOcorrencia, String descricaoOcorrencia) {
        this.sistemaPesquisaEnum = sistemaPesquisaEnum;
        this.numeroTipoPesquisa = numeroTipoPesquisa;
        this.numeroSubTipoPesquisa = numeroSubTipoPesquisa;
        this.codigoEspecifico = codigoEspecifico;
        this.grupoOcorrencia = grupoOcorrencia;
        this.descricaoOcorrencia = descricaoOcorrencia;
    }

    /**
     * Método utilizado para capturar o Enum especifico baseado no tipo de
     * pesquisa realizada.<br>
     * Para o caso do SICPF o codigo de retorno é utilizado como tipo de
     * pesquisa e o subtipo é sempre nulo.
     *
     * @param sistemaPesquisaEnum Indicação do sistema de pesquisa solicitado
     * @param tipoPesquisa Codigo do tipo de pesquisa realizado para os sistemas
     * do SIPES e codigo de retorno para o SICPF.
     * @param subTipoPesquisa Codigo do subtipo de pesquisa realizado para os
     * sistemas do SICPF
     * @return Elemeno que representa o codigo de retorno cadastrado no banco de
     * dados.
     */
    public static Object getBySistemaCodigo(SistemaPesquisaEnum sistemaPesquisaEnum, Integer tipoPesquisa, Integer subTipoPesquisa) {
        for (SistemaPesquisaTipoRetornoEnum elemento : SistemaPesquisaTipoRetornoEnum.values()) {
            if ((elemento.getSistemaPesquisaEnum().equals(sistemaPesquisaEnum))
                    && (elemento.getNumeroTipoPesquisa().equals(tipoPesquisa))
                    && ((elemento.getNumeroSubTipoPesquisa() == null) || (elemento.getNumeroSubTipoPesquisa().equals(subTipoPesquisa)))) {
                return elemento;
            }
        }

        return null;
    }

    public SistemaPesquisaEnum getSistemaPesquisaEnum() {
        return sistemaPesquisaEnum;
    }

    public Integer getNumeroTipoPesquisa() {
        return numeroTipoPesquisa;
    }

    public Integer getNumeroSubTipoPesquisa() {
        return numeroSubTipoPesquisa;
    }

    public Object getCodigoEspecifico() {
        return codigoEspecifico;
    }

    public String getGrupoOcorrencia() {
        return grupoOcorrencia;
    }

    public String getDescricaoOcorrencia() {
        return descricaoOcorrencia;
    }

}
