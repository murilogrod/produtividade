package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.Apontamento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesConsultaApontamento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesConsultaApontamento.XML_ROOT_ELEMENT_APONTAMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesConsultaApontamento.API_MODEL_V1_APONTAMENTO,
        description = "Objeto utilizado para representar apontamento relacionado a um checklist"
)
public class ApontamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesConsultaApontamento.ID)
    @ApiModelProperty(name = ConstantesConsultaApontamento.ID, value = "Numero identificador do apontamento.")
    private Long id;
    
    @JsonProperty(value = ConstantesConsultaApontamento.NOME)
    @ApiModelProperty(name = ConstantesConsultaApontamento.NOME, value = "Valor que representa o titulo do apontamento a ser apresentado ao operador")
    private String nome;

    @JsonProperty(value = ConstantesConsultaApontamento.DESCRICAO)
    @ApiModelProperty(name = ConstantesConsultaApontamento.DESCRICAO, value = "Valor que indica um detalhamento sobre o apontamento.")
    private String descricao;
    
    @JsonProperty(value = ConstantesConsultaApontamento.ORIENTACAO_OPERADOR)
    @ApiModelProperty(name = ConstantesConsultaApontamento.ORIENTACAO_OPERADOR, value = "Texto de orientação ao usuário.")
    @JsonInclude(value=Include.NON_NULL)
    private String orientacaoOperador;
    
    @JsonProperty(value = ConstantesConsultaApontamento.PENDENCIA_INFORMACAO)
    @ApiModelProperty(name = ConstantesConsultaApontamento.PENDENCIA_INFORMACAO, value = "Indica que o elemento avaliado será questionado.")
    private Boolean pendenciaInformacao;
    
    @JsonProperty(value = ConstantesConsultaApontamento.PENDENCIA_SEGURANCA)
    @ApiModelProperty(name = ConstantesConsultaApontamento.PENDENCIA_SEGURANCA, value = "Indica que o elemento possui uma suspeita de fraude.")
    private Boolean pendenciaSeguranca;
    
    @JsonProperty(value = ConstantesConsultaApontamento.REJEICAO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesConsultaApontamento.REJEICAO_DOCUMENTO, value = "Indica que o elemento avaliado será rejeitado.")
    private Boolean rejeicaoDocumento;
    
    @JsonProperty(value = ConstantesConsultaApontamento.SEQUENCIA_APRESENTACAO)
    @ApiModelProperty(name = ConstantesConsultaApontamento.SEQUENCIA_APRESENTACAO, value = "Indica a ordem de exibição dos apontamentos no checklist.")
    private Integer sequenciaApresentacao;
    
    @JsonProperty(value = ConstantesConsultaApontamento.TECLA_ATALHO)
    @ApiModelProperty(name = ConstantesConsultaApontamento.TECLA_ATALHO, value = "indica a tecla de atalho do teclado que aciona o apontamento.")
    private String teclaAtalhoApontamento;
    
    public ApontamentoDTO() {
        super();
    }
    
    public ApontamentoDTO(Apontamento apontamento) {
        this();
        if(Objects.nonNull(apontamento)) {
            this.id = apontamento.getId();
            this.nome = apontamento.getNome();
            this.descricao = apontamento.getDescricao();
            this.orientacaoOperador = apontamento.getOrientacao();
            this.pendenciaInformacao = apontamento.getIndicativoInformacao();
            this.pendenciaSeguranca = apontamento.getIndicativoSeguranca();
            this.rejeicaoDocumento = apontamento.getIndicativoRejeicao();
            this.sequenciaApresentacao = apontamento.getOrdem();
            this.teclaAtalhoApontamento = apontamento.getTeclaAtalho();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getOrientacaoOperador() {
        return orientacaoOperador;
    }

    public void setOrientacaoOperador(String orientacaoOperador) {
        this.orientacaoOperador = orientacaoOperador;
    }

    public Boolean getPendenciaInformacao() {
        return pendenciaInformacao;
    }

    public void setPendenciaInformacao(Boolean pendenciaInformacao) {
        this.pendenciaInformacao = pendenciaInformacao;
    }

    public Boolean getPendenciaSeguranca() {
        return pendenciaSeguranca;
    }

    public void setPendenciaSeguranca(Boolean pendenciaSeguranca) {
        this.pendenciaSeguranca = pendenciaSeguranca;
    }

    public Boolean getRejeicaoDocumento() {
        return rejeicaoDocumento;
    }

    public void setRejeicaoDocumento(Boolean rejeicaoDocumento) {
        this.rejeicaoDocumento = rejeicaoDocumento;
    }

    public Integer getSequenciaApresentacao() {
        return sequenciaApresentacao;
    }

    public void setSequenciaApresentacao(Integer sequenciaApresentacao) {
        this.sequenciaApresentacao = sequenciaApresentacao;
    }

    public String getTeclaAtalhoApontamento() {
        return teclaAtalhoApontamento;
    }

    public void setTeclaAtalhoApontamento(String teclaAtalhoApontamento) {
        this.teclaAtalhoApontamento = teclaAtalhoApontamento;
    }
    
    
}
