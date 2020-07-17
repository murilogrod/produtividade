package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesConsultaChecklist;
import io.swagger.annotations.ApiModelProperty;
public class ProcessosDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @XmlElement(name = ConstantesConsultaChecklist.ID_PROCESSO_DOSSIE)
    @JsonProperty(value = ConstantesConsultaChecklist.ID_PROCESSO_DOSSIE)
    @ApiModelProperty(name = ConstantesConsultaChecklist.ID_PROCESSO_DOSSIE, value = "Atributo que representa processo dossiê a qual checklist pertence.")
    private Integer idProcessoDossie;
    
    @XmlElement(name = ConstantesConsultaChecklist.ID_PROCESSO_FASE)
    @JsonProperty(value = ConstantesConsultaChecklist.ID_PROCESSO_FASE)
    @ApiModelProperty(name = ConstantesConsultaChecklist.ID_PROCESSO_FASE, value = "Atributo que representa processo fase a qual checklist pertence.")
    private Integer idProcessoFase;
    
    public ProcessosDTO(Integer idProcessoDossie, Integer idProcessoFase) {
        this.idProcessoDossie = idProcessoDossie;
        this.idProcessoFase = idProcessoFase;
    }
    
    public Integer getIdProcessoDossie() {
        return idProcessoDossie;
    }
    
    public void setIdProcessoDossie(Integer idProcessoDossie) {
        this.idProcessoDossie = idProcessoDossie;
    }
    
    public Integer getIdProcessoFase() {
        return idProcessoFase;
    }
    
    public void setIdProcessoFase(Integer idProcessoFase) {
        this.idProcessoFase = idProcessoFase;
    }
}