package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tratamento.apuracao;

import br.gov.caixa.simtr.modelo.entidade.Apontamento;
import br.gov.caixa.simtr.modelo.entidade.Checklist;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioTratamento;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlRootElement(name = ConstantesNegocioTratamento.XML_ROOT_ELEMENT_CHECKLIST_APONTAMENTO_PENDENTE)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioTratamento.API_MODEL_V1_APURACAO__CHECKLIST_APONTAMENTO_PENDENTE,
        description = "Objeto utilizado para representar o checklist encaminhado com apontamentos pendentes de envio no retorno da apuracao da execução do tratamento sob a ótica Apoio ao Negocio."
)
public class ChecklistApontamentoPendenteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioTratamento.IDENTIFICADOR_CHECKLIST)
    @ApiModelProperty(name = ConstantesNegocioTratamento.IDENTIFICADOR_CHECKLIST, required = true, value = "Codigo de identificação do checklist encaminhado.")
    private Integer identificadorChecklist;

    @XmlElement(name = ConstantesNegocioTratamento.NOME_CHECKLIST)
    @ApiModelProperty(name = ConstantesNegocioTratamento.NOME_CHECKLIST, required = true, value = "Nome do checklist definido para identificação.")
    private String nome;

    @XmlElement(name = ConstantesNegocioTratamento.IDENTIFICADOR_INSTANCIA)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(name = ConstantesNegocioTratamento.IDENTIFICADOR_INSTANCIA, required = false, value = "Identificador da instancia de documento ao qual o checklist esta vinculado. Este campo não será encaminhado quando se tratar de checklists não documentais.")
    private Long identificadorInstanciaDocumento;

    @XmlElement(name = ConstantesNegocioTratamento.APONTAMENTO_PENDENTE)
    @XmlElementWrapper(name = ConstantesNegocioTratamento.APONTAMENTOS_PENDENTES)
    @JsonProperty(value = ConstantesNegocioTratamento.APONTAMENTOS_PENDENTES)
    @ApiModelProperty(name = ConstantesNegocioTratamento.APONTAMENTOS_PENDENTES, required = true, value = "Lista de apontamentos que são esperados para o checklists referencia e que não foram identificados no envio da verificação.")
    private List<ApontamentoDTO> apontamentosAusentes;

    public ChecklistApontamentoPendenteDTO() {
        super();
        this.apontamentosAusentes = new ArrayList<>();
    }

    public ChecklistApontamentoPendenteDTO(Checklist checklist, Long identificadorInstanciaDocumento, List<Apontamento> apontamentosAusentes) {
        this();
        if (Objects.nonNull(checklist)) {
            this.identificadorChecklist = checklist.getId();
            this.nome = checklist.getNome();
        }

        this.identificadorInstanciaDocumento = identificadorInstanciaDocumento;

        if (Objects.nonNull(apontamentosAusentes)) {
            apontamentosAusentes.forEach(apontamento -> {
                this.apontamentosAusentes.add(new ApontamentoDTO(apontamento));
            });
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

    public Long getIdentificadorInstanciaDocumento() {
        return identificadorInstanciaDocumento;
    }

    public void setIdentificadorInstanciaDocumento(Long identificadorInstanciaDocumento) {
        this.identificadorInstanciaDocumento = identificadorInstanciaDocumento;
    }

    public List<ApontamentoDTO> getApontamentosAusentes() {
        return apontamentosAusentes;
    }

    public void setApontamentosAusentes(List<ApontamentoDTO> apontamentosAusentes) {
        this.apontamentosAusentes = apontamentosAusentes;
    }

}
