package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tratamento.apuracao;

import br.gov.caixa.simtr.modelo.entidade.Checklist;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioTratamento;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

@XmlRootElement(name = ConstantesNegocioTratamento.XML_ROOT_ELEMENT_CHECKLIST_PENDENTE)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioTratamento.API_MODEL_V1_APURACAO__CHECKLIST_PENDENTE,
        description = "Objeto utilizado para representar o checklist pendente no retorno da apuracao da execução do tratamentosob a ótica Apoio ao Negocio."
)
public class ChecklistPendenteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioTratamento.IDENTIFICADOR_CHECKLIST)
    @ApiModelProperty(name = ConstantesNegocioTratamento.IDENTIFICADOR_CHECKLIST, required = true, value = "Codigo de identificação do checklist encaminhado.")
    private Integer identificadorChecklist;

    @XmlElement(name = ConstantesNegocioTratamento.NOME)
    @ApiModelProperty(name = ConstantesNegocioTratamento.NOME, required = true, value = "Nome do checklist definido para identificação.")
    private String nome;

    public ChecklistPendenteDTO() {
        super();
    }

    public ChecklistPendenteDTO(Checklist checklist) {
        this();
        if (Objects.nonNull(checklist)) {
            this.identificadorChecklist = checklist.getId();
            this.nome = checklist.getNome();
        }
    }

    public Integer getIdentificadorChecklist() {
        return identificadorChecklist;
    }

    public void setIdentificadorChecklist(Integer identificadorChecklist) {
        this.identificadorChecklist = identificadorChecklist;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
