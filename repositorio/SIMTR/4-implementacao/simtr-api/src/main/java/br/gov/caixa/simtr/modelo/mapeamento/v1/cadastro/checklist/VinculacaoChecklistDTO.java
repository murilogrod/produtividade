package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.VinculacaoChecklist;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarSimpleBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroChecklist;
import io.swagger.annotations.ApiModelProperty;

public class VinculacaoChecklistDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    

    @JsonProperty(value = ConstantesCadastroChecklist.ID_VINCULACAO)
    @ApiModelProperty(name = ConstantesCadastroChecklist.ID_VINCULACAO, value = "Atributo que representa o identificador da vinculação do checklist.")
    private Long id;
    
    @JsonProperty(value = ConstantesCadastroChecklist.ID_PROCESSO_DOSSIE)
    @ApiModelProperty(name = ConstantesCadastroChecklist.ID_PROCESSO_DOSSIE, value = "Atributo que representa processo dossiê a qual checklist pertence.")
    private Integer identificadorProcessoDossie;
    
    @JsonProperty(value = ConstantesCadastroChecklist.ID_PROCESSO_FASE)
    @ApiModelProperty(name = ConstantesCadastroChecklist.ID_PROCESSO_FASE, value = "Atributo que representa processo fase a qual checklist pertence.")
    private Integer identificadorProcessoFase;
    
    @JsonProperty(value = ConstantesCadastroChecklist.ID_TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesCadastroChecklist.ID_TIPO_DOCUMENTO, value = "Atributo que reprsenta o tipo de documento usado no checklist para validação documental.")
    private Integer identificadorTipoDocumento;
    
    @JsonProperty(value = ConstantesCadastroChecklist.ID_FUNCAO_DOCUMENTAO)
    @ApiModelProperty(name = ConstantesCadastroChecklist.ID_FUNCAO_DOCUMENTAO, value = "Atributo que reprsenta a funcão documental usado no checklist para validação documental.")
    private Integer identificadorFuncaoDocumental;
    
    @XmlJavaTypeAdapter(value = CalendarSimpleBRAdapter.class)
    @JsonProperty(value = ConstantesCadastroChecklist.DATA_REVOGACAO)
    @ApiModelProperty(name = ConstantesCadastroChecklist.DATA_REVOGACAO, value = "Atributo que representa a data atual definida para o checklist expirar.")
    private Calendar dataRevogacaoAtual;

    
    public VinculacaoChecklistDTO(VinculacaoChecklist vinculacao) {
        super();
        if(Objects.nonNull(vinculacao)) {
            this.id = vinculacao.getId();
            this.identificadorProcessoDossie = vinculacao.getProcessoDossie().getId();
            this.identificadorProcessoFase = vinculacao.getProcessoFase().getId();
            if(Objects.nonNull(vinculacao.getTipoDocumento())) {
                this.identificadorTipoDocumento = vinculacao.getTipoDocumento().getId();
            }
            if(Objects.nonNull(vinculacao.getFuncaoDocumental())) {
                this.identificadorFuncaoDocumental = vinculacao.getFuncaoDocumental().getId();
            }
            this.dataRevogacaoAtual = vinculacao.getDataRevogacao();
        }
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdentificadorProcessoDossie() {
        return identificadorProcessoDossie;
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
    
    public Calendar getDataRevogacaoAtual() {
        return dataRevogacaoAtual;
    }
    
    public void setDataRevogacaoAtual(Calendar dataRevogacaoAtual) {
        this.dataRevogacaoAtual = dataRevogacaoAtual;
    }
}
