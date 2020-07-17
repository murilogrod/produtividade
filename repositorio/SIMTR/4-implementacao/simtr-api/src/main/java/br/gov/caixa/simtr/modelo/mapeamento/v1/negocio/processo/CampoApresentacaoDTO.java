package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.processo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.CampoApresentacao;
import br.gov.caixa.simtr.modelo.enumerator.TipoDispositivoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioProcesso;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioProcesso.XML_ROOT_ELEMENT_CAMPO_APRESENTACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioProcesso.API_MODEL_V1_CAMPO_APRESENTACAO,
        description = "Objeto utilizado para representar a forma de apresentação do campo de formulario no retorno as consultas realizadas no Processo sob a ótica Apoio ao Negocio."
)
public class CampoApresentacaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioProcesso.LARGURA)
    @ApiModelProperty(name = ConstantesNegocioProcesso.LARGURA, required = true, value = "Identifica o numero de colunas ocupadas na montagem da interface baseado no conceito do bootstrap. Este valor pode variar de 1 a 12.")
    private Integer largura;

    @XmlElement(name = ConstantesNegocioProcesso.TIPO_DISPOSITIVO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.TIPO_DISPOSITIVO, required = true, value = "Indica o tipo resolução do dispositivo para renderização do campo com o numero de colunas indicadas.")
    private TipoDispositivoEnum tipoDispositivoEnum;

    // ************************************
    public CampoApresentacaoDTO() {
        super();
    }

    public CampoApresentacaoDTO(CampoApresentacao campoApresentacao) {
        this();
        this.largura = campoApresentacao.getLargura();
        this.tipoDispositivoEnum = campoApresentacao.getTipoDispositivoEnum();
    }

    public Integer getLargura() {
        return largura;
    }

    public void setLargura(Integer largura) {
        this.largura = largura;
    }

    public TipoDispositivoEnum getTipoDispositivoEnum() {
        return tipoDispositivoEnum;
    }

    public void setTipoDispositivoEnum(TipoDispositivoEnum tipoDispositivoEnum) {
        this.tipoDispositivoEnum = tipoDispositivoEnum;
    }

}
