package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao;

import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoValidacao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProdutoValidacao.XML_ROOT_ELEMENT_PENDENCIA_RESPOSTA_FORMULARIO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoValidacao.API_MODEL_V1_PENDENCIA_RESPOSTA_FORMULARIO,
        description = "Objeto utilizado para representar a resposta de formulario pendente no contexto da validação de um dossiê de produto"
)
public class RespostaFormularioPendenteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.ID, required = true, value = "Valor que identifica o campo do formulário pendente")
    private Long id;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.LABEL)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.LABEL, required = true, value = "Valor que reprensenta o label apresentado no campo do formulário")
    private String label;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.CAMPO_FORMULARIO_LOCALIZADO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.CAMPO_FORMULARIO_LOCALIZADO, required = true, value = "Indica se o campo do formulário informado foi identificado ou não")
    private boolean campoFormularioLocalizado;
    
    public RespostaFormularioPendenteDTO(Long identificadorCampoPendente) {
        super();
        this.id = identificadorCampoPendente;
        this.campoFormularioLocalizado = Boolean.FALSE;
    }

    public RespostaFormularioPendenteDTO(CampoFormulario campoFormulario) {
        super();
        if (campoFormulario != null) {
            this.id = campoFormulario.getId();
            this.label= campoFormulario.getCampoEntrada().getLabel();
            this.campoFormularioLocalizado = Boolean.TRUE;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isCampoFormularioLocalizado() {
        return campoFormularioLocalizado;
    }

    public void setCampoFormularioLocalizado(boolean campoFormularioLocalizado) {
        this.campoFormularioLocalizado = campoFormularioLocalizado;
    }
}
