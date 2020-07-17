package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.CampoApresentacao;
import br.gov.caixa.simtr.modelo.enumerator.TipoDispositivoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesConsultaFormasApresentacao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = ConstantesConsultaFormasApresentacao.API_MODEL_FORMAS_APRESENTACAO,
        description = "Objeto utilizado para representar as formas que um campo deve ser apresentado de acordo com o dispositivo usado pelo usuário.")
public class FormaApresentacaoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesConsultaFormasApresentacao.TIPO_DISPOSITIVO)
    @ApiModelProperty(name = ConstantesConsultaFormasApresentacao.TIPO_DISPOSITIVO, value = "Atributo utilizado para indicar o dispositivo de renderizaçãodo do componente em tela.")
    private TipoDispositivoEnum tipoDispositivo;
    
    @JsonProperty(value = ConstantesConsultaFormasApresentacao.LARGURA)
    @ApiModelProperty(name = ConstantesConsultaFormasApresentacao.LARGURA, value = "Atributo que armazena o número de colunas do bootstrap ocupadas pelo campo do formulário na estrutura de tela. Este valor pode variar de 1 a 12.")
    private Integer largura;
    
    public FormaApresentacaoDTO() {
    }
    
    public FormaApresentacaoDTO(CampoApresentacao campoApresentacao) {
        this.tipoDispositivo = campoApresentacao.getTipoDispositivoEnum();
        this.largura = campoApresentacao.getLargura();
    }

    public TipoDispositivoEnum getTipoDispositivo() {
        return tipoDispositivo;
    }

    public void setTipoDispositivo(TipoDispositivoEnum tipoDispositivo) {
        this.tipoDispositivo = tipoDispositivo;
    }

    public Integer getLargura() {
        return largura;
    }

    public void setLargura(Integer largura) {
        this.largura = largura;
    }
}
