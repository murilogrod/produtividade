package br.gov.caixa.simtr.controle.vo.portalempreendedor.validacao;

import java.io.Serializable;

public class ParametrosDossieProdutoMeiVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer processo_originador;
    private Long formulario_data;
    private Long formulario_protocolo;
    private Long formulario_cnae;
    private Long formulario_necessidades;
    private Long formulario_conta;
    private Long formulario_indicador_mei;
    private Long formulario_contato;
    private Long formulario_estado_civil;
    private Long formulario_escolaridade;
    private Integer tipo_relacionamento_socio_pf;
    private Integer tipo_relacionamento_pj;
    private Integer tipo_relacionamento_socio_pj;
    private Integer tipo_relacionamento_responsavel_legal;
    private Integer tipo_relacionamento_conjuge;
    private String tipologia_endereco_pf;
    private String tipologia_identificacao_pf;
    private String tipologia_constituicao_pj;
    private String tipologia_faturamento_pj;

    public Integer getProcesso_originador() {
        return processo_originador;
    }

    public void setProcesso_originador(Integer processo_originador) {
        this.processo_originador = processo_originador;
    }

    public Long getFormulario_data() {
        return formulario_data;
    }

    public void setFormulario_data(Long formulario_data) {
        this.formulario_data = formulario_data;
    }

    public Long getFormulario_protocolo() {
        return formulario_protocolo;
    }

    public void setFormulario_protocolo(Long formulario_protocolo) {
        this.formulario_protocolo = formulario_protocolo;
    }

    public Long getFormulario_cnae() {
        return formulario_cnae;
    }

    public void setFormulario_cnae(Long formulario_cnae) {
        this.formulario_cnae = formulario_cnae;
    }

    public Long getFormulario_necessidades() {
        return formulario_necessidades;
    }

    public void setFormulario_necessidades(Long formulario_necessidades) {
        this.formulario_necessidades = formulario_necessidades;
    }

    public Long getFormulario_conta() {
        return formulario_conta;
    }

    public void setFormulario_conta(Long formulario_conta) {
        this.formulario_conta = formulario_conta;
    }

    public Long getFormulario_indicador_mei() {
        return formulario_indicador_mei;
    }

    public void setFormulario_indicador_mei(Long formulario_indicador_mei) {
        this.formulario_indicador_mei = formulario_indicador_mei;
    }

    public Long getFormulario_contato() {
        return formulario_contato;
    }

    public void setFormulario_contato(Long formulario_contato) {
        this.formulario_contato = formulario_contato;
    }

    public Long getFormulario_estado_civil() {
        return formulario_estado_civil;
    }

    public void setFormulario_estado_civil(Long formulario_estado_civil) {
        this.formulario_estado_civil = formulario_estado_civil;
    }

    public Long getFormulario_escolaridade() {
        return formulario_escolaridade;
    }

    public void setFormulario_escolaridade(Long formulario_escolaridade) {
        this.formulario_escolaridade = formulario_escolaridade;
    }

    public Integer getTipo_relacionamento_socio_pf() {
        return tipo_relacionamento_socio_pf;
    }

    public void setTipo_relacionamento_socio_pf(Integer tipo_relacionamento_socio_pf) {
        this.tipo_relacionamento_socio_pf = tipo_relacionamento_socio_pf;
    }

    public Integer getTipo_relacionamento_socio_pj() {
        return tipo_relacionamento_socio_pj;
    }

    public void setTipo_relacionamento_socio_pj(Integer tipo_relacionamento_socio_pj) {
        this.tipo_relacionamento_socio_pj = tipo_relacionamento_socio_pj;
    }

    public Integer getTipo_relacionamento_responsavel_legal() {
        return tipo_relacionamento_responsavel_legal;
    }

    public void setTipo_relacionamento_responsavel_legal(Integer tipo_relacionamento_responsavel_legal) {
        this.tipo_relacionamento_responsavel_legal = tipo_relacionamento_responsavel_legal;
    }

    public Integer getTipo_relacionamento_conjuge() {
        return tipo_relacionamento_conjuge;
    }

    public void setTipo_relacionamento_conjuge(Integer tipo_relacionamento_conjuge) {
        this.tipo_relacionamento_conjuge = tipo_relacionamento_conjuge;
    }

    public Integer getTipo_relacionamento_pj() {
        return tipo_relacionamento_pj;
    }

    public void setTipo_relacionamento_pj(Integer tipo_relacionamento_pj) {
        this.tipo_relacionamento_pj = tipo_relacionamento_pj;
    }

    public String getTipologia_endereco_pf() {
        return tipologia_endereco_pf;
    }

    public void setTipologia_endereco_pf(String tipologia_endereco_pf) {
        this.tipologia_endereco_pf = tipologia_endereco_pf;
    }

    public String getTipologia_identificacao_pf() {
        return tipologia_identificacao_pf;
    }

    public void setTipologia_identificacao_pf(String tipologia_identificacao_pf) {
        this.tipologia_identificacao_pf = tipologia_identificacao_pf;
    }

    public String getTipologia_constituicao_pj() {
        return tipologia_constituicao_pj;
    }

    public void setTipologia_constituicao_pj(String tipologia_constituicao_pj) {
        this.tipologia_constituicao_pj = tipologia_constituicao_pj;
    }

    public String getTipologia_faturamento_pj() {
        return tipologia_faturamento_pj;
    }

    public void setTipologia_faturamento_pj(String tipologia_faturamento_pj) {
        this.tipologia_faturamento_pj = tipologia_faturamento_pj;
    }
}
