package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tratamento;

import br.gov.caixa.simtr.controle.vo.checklist.ParecerApontamentoVO;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioTratamento;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioTratamento.XML_ROOT_ELEMENT_PARECER_APONTAMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioTratamento.API_MODEL_V1_PARECER_APONTAMENTO,
        description = "Objeto utilizado para representar a analise realizada em um apontamento especifico, representando o parecer deste apontamento, no ato da execução do tratamento sob a ótica Apoio ao Negocio."
)
public class ParecerApontamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioTratamento.IDENTIFICADOR_APONTAMENTO)
    @ApiModelProperty(name = ConstantesNegocioTratamento.IDENTIFICADOR_APONTAMENTO, required = true, value = "Código de identificação do apontamento analisado.")
    private Long identificadorApontamento;

    @XmlElement(name = ConstantesNegocioTratamento.APROVADO)
    @ApiModelProperty(name = ConstantesNegocioTratamento.APROVADO, required = true, value = "Indicativo de aprovação ou rejeição do apontamento analisado.")
    private boolean aprovado;

    @XmlElement(name = ConstantesNegocioTratamento.COMENTARIO)
    @ApiModelProperty(name = ConstantesNegocioTratamento.COMENTARIO, required = false, value = "Texto livre utilizado pelo analista para enviar um comentario sobre a analise do apontamento associado.")
    private String comentario;

    public ParecerApontamentoDTO() {
        super();
    }

    public Long getIdentificadorApontamento() {
        return identificadorApontamento;
    }

    public void setIdentificadorApontamento(Long identificadorApontamento) {
        this.identificadorApontamento = identificadorApontamento;
    }

    public boolean isAprovado() {
        return aprovado;
    }

    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    //*******************************************
    public ParecerApontamentoVO prototype() {
        ParecerApontamentoVO parecerApontamentoVO = new ParecerApontamentoVO(this.identificadorApontamento, this.isAprovado(), this.getComentario());
        return parecerApontamentoVO;
    }

}
