package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.OpcaoCampo;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesConsultaOpcoesCampo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = ConstantesConsultaOpcoesCampo.API_MODEL_OPCOES_CAMPO,
        description = "Objeto utilizado para representar as opções de um campo a ser apresentado em um formulário.")
public class OpcaoCampoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesConsultaOpcoesCampo.ID)
    @ApiModelProperty(name = ConstantesConsultaOpcoesCampo.ID, value = "Atributo que representa o identificador único do objeto.")
    private Long id;
    
    @JsonProperty(value = ConstantesConsultaOpcoesCampo.VALOR)
    @ApiModelProperty(name = ConstantesConsultaOpcoesCampo.VALOR, value = "Atributo utilizado para armazenar o valor que será definido como value da opção na interface gráfica.")
    private String valor;
    
    @JsonProperty(value = ConstantesConsultaOpcoesCampo.DESCRICAO)
    @ApiModelProperty(name = ConstantesConsultaOpcoesCampo.DESCRICAO, value = "Atributo que armazena o valor da opção que será exibida para o usuário no campo do formulário.")
    private String descricao;
    
    @JsonProperty(value = ConstantesConsultaOpcoesCampo.ATIVO)
    @ApiModelProperty(name = ConstantesConsultaOpcoesCampo.ATIVO, value = "Atributo que indica se a opção do campo de entrada está apta ou não para ser inserido no campo de entrada do formulário.")
    private Boolean ativo;

    public OpcaoCampoDTO() {
    }
    
    public OpcaoCampoDTO(OpcaoCampo opcaoCampo) {
        this.id = opcaoCampo.getId();
        this.valor = opcaoCampo.getValue();
        this.descricao = opcaoCampo.getNome();
        this.ativo = opcaoCampo.getAtivo();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
