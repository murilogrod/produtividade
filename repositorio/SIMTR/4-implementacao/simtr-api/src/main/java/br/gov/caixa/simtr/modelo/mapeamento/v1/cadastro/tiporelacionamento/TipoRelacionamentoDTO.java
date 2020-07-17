package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tiporelacionamento;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesConsultaTipoRelacionamento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = ConstantesConsultaTipoRelacionamento.API_MODEL_V1_TIPO_RELACIONAMENTO,
        description = "Objeto utilizado para representar um tipo de relacionamento na consulta realizada."
)
public class TipoRelacionamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @JsonProperty(value = ConstantesConsultaTipoRelacionamento.ID, required = true)
    @ApiModelProperty(name = ConstantesConsultaTipoRelacionamento.ID, value = "Atributo que representa a chave primária da entidade.")
    private Integer id;
    
    @JsonProperty(value = ConstantesConsultaTipoRelacionamento.NOME, required = true)
    @ApiModelProperty(name = ConstantesConsultaTipoRelacionamento.NOME, value = "Atributo que representa o nome dado ao tipo de relacionamento.")
    private String nome;
    
    @JsonProperty(value = ConstantesConsultaTipoRelacionamento.TIPO_PESSOA, required = true)
    @ApiModelProperty(name = ConstantesConsultaTipoRelacionamento.TIPO_PESSOA, value = "Atributo que representa o tipo de pessoa ao tipo de relacionamento Jurídica|Física")
    private TipoPessoaEnum tipoPessoa;
    
    @JsonProperty(value = ConstantesConsultaTipoRelacionamento.INDICADOR_PRINCIPAL, required = true)
    @ApiModelProperty(name = ConstantesConsultaTipoRelacionamento.INDICADOR_PRINCIPAL, value = "Atributo que representa se tipo de relacionamento é principal")
    private Boolean indicadorPrincipal;
    
    @JsonProperty(value = ConstantesConsultaTipoRelacionamento.INDICADOR_RELACIONADO, required = true)
    @ApiModelProperty(name = ConstantesConsultaTipoRelacionamento.INDICADOR_RELACIONADO, value = "Atributo que representa se tipo de relacionamento é relacionado")
    private Boolean indicadorRelacionado;
    
    @JsonProperty(value = ConstantesConsultaTipoRelacionamento.INDICADOR_SEQUENCIA, required = true)
    @ApiModelProperty(name = ConstantesConsultaTipoRelacionamento.INDICADOR_SEQUENCIA, value = "Atributo que representa se tipo de relacionamento é sequencial")
    private Boolean indicadorSequencia;
    
    @JsonProperty(value = ConstantesConsultaTipoRelacionamento.INDICADOR_RECEITA_PF, required = true)
    @ApiModelProperty(name = ConstantesConsultaTipoRelacionamento.INDICADOR_RECEITA_PF, value = "Atributo que representa se tipo de relacionamento será do tipo pessoa física")
    private Boolean indicadorReceitaPF;
    
    @JsonProperty(value = ConstantesConsultaTipoRelacionamento.INDICADOR_RECEITA_PJ, required = true)
    @ApiModelProperty(name = ConstantesConsultaTipoRelacionamento.INDICADOR_RECEITA_PJ, value = "Atributo que representa se tipo de relacionamento será do tipo pessoa jurídica")
    private Boolean indicadorReceitaPJ;
    
    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @JsonProperty(value = ConstantesConsultaTipoRelacionamento.ULTIMA_ALTERACAO, required = true)
    @ApiModelProperty(name = ConstantesConsultaTipoRelacionamento.ULTIMA_ALTERACAO, value = "Atributo que representa data/hora da ultima atualização do tipo de relacionamento")
    private Calendar ultimaAlteracao;
    
    public TipoRelacionamentoDTO() {}
    
    public TipoRelacionamentoDTO(TipoRelacionamento tipoRelacionamento) {
        this();
        if(Objects.nonNull(tipoRelacionamento)) {
            this.id = tipoRelacionamento.getId();
            this.nome = tipoRelacionamento.getNome();
            this.tipoPessoa = tipoRelacionamento.getTipoPessoaEnum();
            this.indicadorPrincipal = tipoRelacionamento.getIndicadorPrincipal() != null ? tipoRelacionamento.getIndicadorPrincipal() : false;
            this.indicadorRelacionado = tipoRelacionamento.getIndicadorRelacionado() != null ? tipoRelacionamento.getIndicadorRelacionado() : false;
            this.indicadorSequencia = tipoRelacionamento.getIndicadorSequencia() != null ? tipoRelacionamento.getIndicadorSequencia() : false;
            this.indicadorReceitaPJ = tipoRelacionamento.getIndicadorReceitaPessoaJuridica() != null ? tipoRelacionamento.getIndicadorReceitaPessoaJuridica() : false;
            this.indicadorReceitaPF = tipoRelacionamento.getIndicadorReceitaPessoaFisica() != null ? tipoRelacionamento.getIndicadorReceitaPessoaFisica() : false;
            this.ultimaAlteracao = tipoRelacionamento.getDataHoraUltimaAlteracao();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Calendar getUltimaAlteracao() {
        return ultimaAlteracao;
    }

    public void setUltimaAlteracao(Calendar ultimaAlteracao) {
        this.ultimaAlteracao = ultimaAlteracao;
    }
}
