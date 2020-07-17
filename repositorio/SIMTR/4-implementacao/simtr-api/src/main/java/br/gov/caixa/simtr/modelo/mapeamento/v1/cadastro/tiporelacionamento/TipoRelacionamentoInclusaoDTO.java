package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tiporelacionamento;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoRelacionamento;
import br.gov.caixa.simtr.visao.PrototypeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = ConstantesCadastroTipoRelacionamento.API_MODEL_V1_TIPO_RELACIONAMENTO_INCLUSAO,
        description = "Objeto utilizado para representar um tipo de relacionamento cadastrado."
)
public class TipoRelacionamentoInclusaoDTO implements Serializable, PrototypeDTO<TipoRelacionamento> {
    
    private static final long serialVersionUID = 1L;
    
    @JsonProperty(value = ConstantesCadastroTipoRelacionamento.NOME, required = true)
    @ApiModelProperty(name = ConstantesCadastroTipoRelacionamento.NOME, required = true, value = "Atributo que representa o nome dado ao tipo de relacionamento.")
    private String nome;
    
    @JsonProperty(value = ConstantesCadastroTipoRelacionamento.TIPO_PESSOA, required = true)
    @ApiModelProperty(name = ConstantesCadastroTipoRelacionamento.TIPO_PESSOA, required = true, value = "Atributo que representa o tipo de pessoa ao tipo de relacionamento Jurídica|Física")
    private TipoPessoaEnum tipoPessoa;
    
    @JsonProperty(value = ConstantesCadastroTipoRelacionamento.INDICADOR_PRINCIPAL, required = true)
    @ApiModelProperty(name = ConstantesCadastroTipoRelacionamento.INDICADOR_PRINCIPAL, required = true, value = "Atributo que representa se tipo de relacionamento é principal")
    private Boolean indicadorPrincipal;
    
    @JsonProperty(value = ConstantesCadastroTipoRelacionamento.INDICADOR_RELACIONADO, required = true)
    @ApiModelProperty(name = ConstantesCadastroTipoRelacionamento.INDICADOR_RELACIONADO, required = true, value = "Atributo que representa se tipo de relacionamento é relacionado")
    private Boolean indicadorRelacionado;
    
    @JsonProperty(value = ConstantesCadastroTipoRelacionamento.INDICADOR_SEQUENCIA, required = true)
    @ApiModelProperty(name = ConstantesCadastroTipoRelacionamento.INDICADOR_SEQUENCIA, required = true, value = "Atributo que representa se tipo de relacionamento é sequencial")
    private Boolean indicadorSequencia;
    
    @JsonProperty(value = ConstantesCadastroTipoRelacionamento.INDICADOR_RECEITA_PF, required = true)
    @ApiModelProperty(name = ConstantesCadastroTipoRelacionamento.INDICADOR_RECEITA_PF, required = true, value = "Atributo que representa se tipo de relacionamento será do tipo pessoa física")
    private Boolean indicadorReceitaPF;
    
    @JsonProperty(value = ConstantesCadastroTipoRelacionamento.INDICADOR_RECEITA_PJ, required = true)
    @ApiModelProperty(name = ConstantesCadastroTipoRelacionamento.INDICADOR_RECEITA_PJ, required = true, value = "Atributo que representa se tipo de relacionamento será do tipo pessoa jurídica")
    private Boolean indicadorReceitaPJ;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoPessoaEnum getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoaEnum tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public Boolean getIndicadorPrincipal() {
        return indicadorPrincipal;
    }

    public void setIndicadorPrincipal(Boolean indicadorPrincipal) {
        this.indicadorPrincipal = indicadorPrincipal;
    }

    public Boolean getIndicadorRelacionado() {
        return indicadorRelacionado;
    }

    public void setIndicadorRelacionado(Boolean indicadorRelacionado) {
        this.indicadorRelacionado = indicadorRelacionado;
    }

    public Boolean getIndicadorSequencia() {
        return indicadorSequencia;
    }

    public void setIndicadorSequencia(Boolean indicadorSequencia) {
        this.indicadorSequencia = indicadorSequencia;
    }

    public Boolean getIndicadorReceitaPF() {
        return indicadorReceitaPF;
    }

    public void setIndicadorReceitaPF(Boolean indicadorReceitaPF) {
        this.indicadorReceitaPF = indicadorReceitaPF;
    }

    public Boolean getIndicadorReceitaPJ() {
        return indicadorReceitaPJ;
    }

    public void setIndicadorReceitaPJ(Boolean indicadorReceitaPJ) {
        this.indicadorReceitaPJ = indicadorReceitaPJ;
    }

    @Override
    public TipoRelacionamento prototype() {
        TipoRelacionamento tipoRelacioamento = new TipoRelacionamento();
        tipoRelacioamento.setNome(this.getNome());
        tipoRelacioamento.setTipoPessoaEnum(this.getTipoPessoa());
        tipoRelacioamento.setIndicadorPrincipal(this.getIndicadorPrincipal());
        tipoRelacioamento.setIndicadorRelacionado(this.getIndicadorRelacionado());
        tipoRelacioamento.setIndicadorSequencia(this.getIndicadorSequencia());
        tipoRelacioamento.setIndicadorReceitaPessoaJuridica(this.getIndicadorReceitaPJ());
        tipoRelacioamento.setIndicadorReceitaPessoaFisica(this.getIndicadorReceitaPF());
        return tipoRelacioamento;
    }
}
