package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesAlteracaoApontamento;
import io.swagger.annotations.ApiModelProperty;

public class AlteracaoApontamentoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @JsonProperty(value = ConstantesAlteracaoApontamento.NOME)
    @ApiModelProperty(name = ConstantesAlteracaoApontamento.NOME, value = "Atributo que representa o nome dado ao apontamento.")
    private String nomeApontamento;
    
    @JsonProperty(value = ConstantesAlteracaoApontamento.DESCRICAO)
    @ApiModelProperty(name = ConstantesAlteracaoApontamento.DESCRICAO, value = "Valor que indica um detalhamento sobre o apontamento.")
    private String descricaoApontamento;

    @JsonProperty(value = ConstantesAlteracaoApontamento.ORIENTACAO_RETORNO)
    @ApiModelProperty(name = ConstantesAlteracaoApontamento.ORIENTACAO_RETORNO, value = "Texto de orientação ao usuário.")
    private String orientacaoRetorno;
    
    @JsonProperty(value = ConstantesAlteracaoApontamento.INDICATIVO_INFORMACAO)
    @ApiModelProperty(name = ConstantesAlteracaoApontamento.INDICATIVO_INFORMACAO, value = "Indica que o elemento avaliado será questionado.")
    private Boolean indicativoInformacao;
    
    @JsonProperty(value = ConstantesAlteracaoApontamento.INDICATIVO_SEGURANCA)
    @ApiModelProperty(name = ConstantesAlteracaoApontamento.INDICATIVO_SEGURANCA, value = "Indica que o elemento possui uma suspeita de fraude.")
    private Boolean indicativoSeguranca;
    
    @JsonProperty(value = ConstantesAlteracaoApontamento.INDICATIVO_REJEICAO)
    @ApiModelProperty(name = ConstantesAlteracaoApontamento.INDICATIVO_REJEICAO, value = "Indica que o elemento avaliado será rejeitado.")
    private Boolean indicativoRejeicao;
    
    @JsonProperty(value = ConstantesAlteracaoApontamento.ORDEM)
    @ApiModelProperty(name = ConstantesAlteracaoApontamento.ORDEM, value = "Indica a ordem de exibição dos apontamentos no checklist.")
    private Integer ordem;
    
    @JsonProperty(value = ConstantesAlteracaoApontamento.TECLA_ATALHO)
    @ApiModelProperty(name = ConstantesAlteracaoApontamento.TECLA_ATALHO, value = "indica a tecla de atalho do teclado que aciona o apontamento.")
    private String teclaAtalhoApontamento;
    
    public String getNomeApontamento() {
        return nomeApontamento;
    }

    public void setNomeApontamento(String nomeApontamento) {
        this.nomeApontamento = nomeApontamento;
    }

    public String getDescricaoApontamento() {
        return descricaoApontamento;
    }

    public void setDescricaoApontamento(String descricaoApontamento) {
        this.descricaoApontamento = descricaoApontamento;
    }

    public String getOrientacaoRetorno() {
        return orientacaoRetorno;
    }

    public void setOrientacaoRetorno(String orientacaoRetorno) {
        this.orientacaoRetorno = orientacaoRetorno;
    }

    public Boolean getIndicativoInformacao() {
        return indicativoInformacao;
    }

    public void setIndicativoInformacao(Boolean indicativoInformacao) {
        this.indicativoInformacao = indicativoInformacao;
    }

    public Boolean getIndicativoSeguranca() {
        return indicativoSeguranca;
    }

    public void setIndicativoSeguranca(Boolean indicativoSeguranca) {
        this.indicativoSeguranca = indicativoSeguranca;
    }

    public Boolean getIndicativoRejeicao() {
        return indicativoRejeicao;
    }

    public void setIndicativoRejeicao(Boolean indicativoRejeicao) {
        this.indicativoRejeicao = indicativoRejeicao;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public String getTeclaAtalhoApontamento() {
        return teclaAtalhoApontamento;
    }

    public void setTeclaAtalhoApontamento(String teclaAtalhoApontamento) {
        this.teclaAtalhoApontamento = teclaAtalhoApontamento;
    }
}
