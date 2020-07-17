package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tratamento.apuracao;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioTratamento;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioTratamento.XML_ROOT_ELEMENT_CHECKLIST_INESPERADO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioTratamento.API_MODEL_V1_APURACAO__CHECKLIST_INESPERADO,
        description = "Objeto utilizado para representar o checklist encaminhado que não estava sendo esperado no retorno da apuracao da execução do tratamento sob a ótica Apoio ao Negocio."
)
public class ChecklistInesperadoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioTratamento.IDENTIFICADOR_CHECKLIST)
    @ApiModelProperty(name = ConstantesNegocioTratamento.IDENTIFICADOR_CHECKLIST, required = true, value = "Codigo de identificação do checklist encaminhado.")
    private Integer identificadorChecklist;

    @XmlElement(name = ConstantesNegocioTratamento.IDENTIFICADOR_INSTANCIA)
    @ApiModelProperty(name = ConstantesNegocioTratamento.IDENTIFICADOR_INSTANCIA, required = false, value = "Identificador da instancia de documento ao qual o checklist esta vinculado. Este campo não será encaminhado quando se tratar de checklists não documentais.")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long identificadorInstanciaDocumento;

    public ChecklistInesperadoDTO() {
        super();
    }

    public ChecklistInesperadoDTO(Integer identificadorChecklist, Long identificadorInstancia) {
        this();
        this.identificadorChecklist = identificadorChecklist;
        this.identificadorInstanciaDocumento = identificadorInstancia;
    }

    public Integer getIdentificadorChecklist() {
        return identificadorChecklist;
    }

    public void setIdentificadorChecklist(Integer identificadorChecklist) {
        this.identificadorChecklist = identificadorChecklist;
    }

    public Long getIdentificadorInstanciaDocumento() {
        return identificadorInstanciaDocumento;
    }

    public void setIdentificadorInstanciaDocumento(Long identificadorInstanciaDocumento) {
        this.identificadorInstanciaDocumento = identificadorInstanciaDocumento;
    }

}
