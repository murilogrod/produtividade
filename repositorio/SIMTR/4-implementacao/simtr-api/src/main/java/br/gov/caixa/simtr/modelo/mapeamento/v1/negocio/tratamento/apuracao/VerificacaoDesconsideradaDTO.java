package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tratamento.apuracao;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioTratamento;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioTratamento.XML_ROOT_ELEMENT_VERIFICACAO_DESCONSIDERADA)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioTratamento.API_MODEL_V1_APURACAO__VERIFICACAO_DESCONSIDERADA,
        description = "Objeto utilizado para representar um retorno indicando as verificações que foram desconsideradas no envio das verificações realizadas no ato da apuração do tratamento encaminhado a um dossiê de produto, por já existir uma aprovação prévia ao documento, sob a ótica Apoio ao Negocio."
)
public class VerificacaoDesconsideradaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioTratamento.IDENTIFICADOR_CHECKLIST)
    @ApiModelProperty(name = ConstantesNegocioTratamento.IDENTIFICADOR_CHECKLIST, required = true, value = "Identificador do checklist associados a verificações encaminhada mas desconsiderada.")
    private Integer identificadorChecklist;

    @XmlElement(name = ConstantesNegocioTratamento.IDENTIFICADOR_INSTANCIA)
    @ApiModelProperty(name = ConstantesNegocioTratamento.IDENTIFICADOR_INSTANCIA, required = false, value = "Identificador da instancia de documento associados a verificações encaminhada mas desconsiderada.")
    private Long identificadorInstanciaDocumento;

    public VerificacaoDesconsideradaDTO() {
        super();
    }

    public VerificacaoDesconsideradaDTO(Integer identificadorChecklist, Long identificadorInstanciaDocumento) {
        this();
        this.identificadorChecklist = identificadorChecklist;
        this.identificadorInstanciaDocumento = identificadorInstanciaDocumento;
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
