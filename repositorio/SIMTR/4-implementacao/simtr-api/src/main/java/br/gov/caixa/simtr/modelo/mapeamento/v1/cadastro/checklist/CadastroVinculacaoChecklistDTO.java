package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist;

import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarSimpleBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroVinculacaoChecklist;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesCadastroVinculacaoChecklist.XML_ROOT_ELEMENT_VINCULACAO_CHECKLIST)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
          value = ConstantesCadastroVinculacaoChecklist.API_MODEL_V1_VINCULACAO_CHECKLIST,
          description = "Objeto utilizado para representar uma vinculação de checklist a ser cadastrada.")
public class CadastroVinculacaoChecklistDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesCadastroVinculacaoChecklist.ID_PROCESSO_DOSSIE)
    @ApiModelProperty(name = ConstantesCadastroVinculacaoChecklist.ID_PROCESSO_DOSSIE, value = "Atributo que representa processo dossiê a qual checklist pertence.")
    private Integer identificadorProcessoDossie;

    @JsonProperty(value = ConstantesCadastroVinculacaoChecklist.ID_PROCESSO_FASE)
    @ApiModelProperty(name = ConstantesCadastroVinculacaoChecklist.ID_PROCESSO_FASE, value = "Atributo que representa processo fase a qual checklist pertence.")
    private Integer identificadorProcessoFase;

    @JsonProperty(value = ConstantesCadastroVinculacaoChecklist.ID_TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesCadastroVinculacaoChecklist.ID_TIPO_DOCUMENTO, value = "Atributo que reprsenta o tipo de documento usado no checklist para validação documental.")
    private Integer identificadorTipoDocumento;

    @JsonProperty(value = ConstantesCadastroVinculacaoChecklist.ID_FUNCAO_DOCUMENTAO)
    @ApiModelProperty(name = ConstantesCadastroVinculacaoChecklist.ID_FUNCAO_DOCUMENTAO, value = "Atributo que reprsenta a funcão documental usado no checklist para validação documental.")
    private Integer identificadorFuncaoDocumental;

    @JsonProperty(value = ConstantesCadastroVinculacaoChecklist.ID_CHECKLIST)
    @ApiModelProperty(name = ConstantesCadastroVinculacaoChecklist.ID_CHECKLIST, value = "Atributo que reprsenta o checklist relacionado a vinculação a ser cadastrada.")
    private Integer identificadorChecklist;
    
    @XmlJavaTypeAdapter(value = CalendarSimpleBRAdapter.class)
    @JsonProperty(value = ConstantesCadastroVinculacaoChecklist.DATA_ATIVACAO)
    @ApiModelProperty(name = ConstantesCadastroVinculacaoChecklist.DATA_ATIVACAO, value = "Atributo que representa a data atual definida para o checklist ser ativado.")
    private Calendar dataAtivacaoAtual;

    public Integer getIdentificadorProcessoDossie() {
        return this.identificadorProcessoDossie;
    }
    
    public void setIdentificadorProcessoDossie(Integer identificadorProcessoDossie) {
        this.identificadorProcessoDossie = identificadorProcessoDossie;
    }

    public Integer getIdentificadorProcessoFase() {
        return identificadorProcessoFase;
    }

    public void setIdentificadorProcessoFase(Integer identificadorProcessoFase) {
        this.identificadorProcessoFase = identificadorProcessoFase;
    }

    public Integer getIdentificadorTipoDocumento() {
        return identificadorTipoDocumento;
    }

    public void setIdentificadorTipoDocumento(Integer identificadorTipoDocumento) {
        this.identificadorTipoDocumento = identificadorTipoDocumento;
    }

    public Integer getIdentificadorFuncaoDocumental() {
        return identificadorFuncaoDocumental;
    }

    public void setIdentificadorFuncaoDocumental(Integer identificadorFuncaoDocumental) {
        this.identificadorFuncaoDocumental = identificadorFuncaoDocumental;
    }

    public Integer getIdentificadorChecklist() {
        return identificadorChecklist;
    }

    public void setIdentificadorChecklist(Integer identificadorChecklist) {
        this.identificadorChecklist = identificadorChecklist;
    }

    public Calendar getDataAtivacaoAtual() {
        return dataAtivacaoAtual;
    }

    public void setDataAtivacaoAtual(Calendar dataAtivacaoAtual) {
        this.dataAtivacaoAtual = dataAtivacaoAtual;
    }
}
