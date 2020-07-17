package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tratamento.apuracao;

import br.gov.caixa.simtr.controle.vo.checklist.ChecklistPendenteVO;
import br.gov.caixa.simtr.modelo.entidade.Checklist;
import br.gov.caixa.simtr.modelo.entidade.InstanciaDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioTratamento;
import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@XmlRootElement(name = ConstantesNegocioTratamento.XML_ROOT_ELEMENT_VERIFICACAO_INVALIDA)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioTratamento.API_MODEL_V1_APURACAO__VERIFICACAO_INVALIDA,
        description = "Objeto utilizado para representar um retorno indicando os problemas apresentados no envio das verificações realizadas no ato da apuração do tratamento encaminhado a um dossiê de produto sob a ótica Apoio ao Negocio."
)
public class VerificacaoInvalidaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioTratamento.IDENTIFICADOR_INSTANCIA_INVALIDA)
    @XmlElementWrapper(name = ConstantesNegocioTratamento.IDENTIFICADORES_INSTANCIA_INVALIDA)
    @JsonProperty(value = ConstantesNegocioTratamento.IDENTIFICADORES_INSTANCIA_INVALIDA)
    @ApiModelProperty(name = ConstantesNegocioTratamento.IDENTIFICADORES_INSTANCIA_INVALIDA, required = false, value = "Lista de identificadores de instancias de documento que foram associados a verificações encaminhadas mas que não possuem vinculo com o dossiê analisado.")
    private List<Long> instanciasDocumentoInvalidas;

    @XmlElement(name = ConstantesNegocioTratamento.CHECKLIST_NAO_DOCUMENTAL_AUSENTE)
    @XmlElementWrapper(name = ConstantesNegocioTratamento.CHECKLISTS_NAO_DOCUMENTAIS_AUSENTES)
    @JsonProperty(value = ConstantesNegocioTratamento.CHECKLISTS_NAO_DOCUMENTAIS_AUSENTES)
    @ApiModelProperty(name = ConstantesNegocioTratamento.CHECKLISTS_NAO_DOCUMENTAIS_AUSENTES, required = false, value = "Lista de checklists não documentais que não foram identificados no envio da verificação.")
    private List<ChecklistPendenteDTO> checklistsFaseAusentes;

    @XmlElement(name = ConstantesNegocioTratamento.CHECKLIST_INESPERADO)
    @XmlElementWrapper(name = ConstantesNegocioTratamento.CHECKLISTS_INESPERADOS)
    @JsonProperty(value = ConstantesNegocioTratamento.CHECKLISTS_INESPERADOS)
    @ApiModelProperty(name = ConstantesNegocioTratamento.CHECKLISTS_INESPERADOS, required = false, value = "Lista de checklists inesperados para a verificação encaminhada. Caso o identificador do documento seja nulo significa que trata-se de um checklist não documental.")
    private List<ChecklistInesperadoDTO> checklistsInesperados;

    @XmlElement(name = ConstantesNegocioTratamento.CHECKLIST_NAO_DOCUMENTAL_REPLICADO)
    @XmlElementWrapper(name = ConstantesNegocioTratamento.CHECKLISTS_NAO_DOCUMENTAIS_REPLICADOS)
    @JsonProperty(value = ConstantesNegocioTratamento.CHECKLISTS_NAO_DOCUMENTAIS_REPLICADOS)
    @ApiModelProperty(name = ConstantesNegocioTratamento.CHECKLISTS_NAO_DOCUMENTAIS_REPLICADOS, required = false, value = "Lista de checklists não documentais que foram identificados mais de uma vez no envio da verificação.")
    private List<ChecklistFaseReplicadoDTO> checklistsFaseReplicados;

    @XmlElement(name = ConstantesNegocioTratamento.INSTANCIA_VERIFICACAO_REPLICADA)
    @XmlElementWrapper(name = ConstantesNegocioTratamento.INSTANCIAS_VERIFICACAO_REPLICADA)
    @JsonProperty(value = ConstantesNegocioTratamento.INSTANCIAS_VERIFICACAO_REPLICADA)
    @ApiModelProperty(name = ConstantesNegocioTratamento.INSTANCIAS_VERIFICACAO_REPLICADA, required = false, value = "Lista de instâncias de documentos que foram identificadas em mais de uma verificação encaminhada.")
    private List<InstanciaDocumentoReplicadaDTO> instanciasReplicadas;

    @XmlElement(name = ConstantesNegocioTratamento.INSTANCIA_PENDENTE)
    @XmlElementWrapper(name = ConstantesNegocioTratamento.INSTANCIAS_PENDENTES)
    @JsonProperty(value = ConstantesNegocioTratamento.INSTANCIAS_PENDENTES)
    @ApiModelProperty(name = ConstantesNegocioTratamento.INSTANCIAS_PENDENTES, required = false, value = "Lista de instâncias de documentos que não tiveram sua verificação encaminhada, ou foram encaminhadas com sinalização de não realizada para documentos obrigatórios.")
    private List<InstanciaDocumentoPendenteDTO> instanciasPendentes;

    @XmlElement(name = ConstantesNegocioTratamento.CHECKLIST_INCOMPLETO)
    @XmlElementWrapper(name = ConstantesNegocioTratamento.CHECKLISTS_INCOMPLETOS)
    @JsonProperty(value = ConstantesNegocioTratamento.CHECKLISTS_INCOMPLETOS)
    @ApiModelProperty(name = ConstantesNegocioTratamento.CHECKLISTS_INCOMPLETOS, required = false, value = "Lista de checklists que foram encaminhados com ausencias de parecer para um ou mais apontamentos vinculados ao checklist indicado.")
    private List<ChecklistApontamentoPendenteDTO> checklistsApontamentoPendenteDTO;

    public VerificacaoInvalidaDTO() {
        super();
        this.instanciasDocumentoInvalidas = new ArrayList<>();
        this.checklistsFaseAusentes = new ArrayList<>();
        this.checklistsInesperados = new ArrayList<>();
        this.checklistsFaseReplicados = new ArrayList<>();
        this.instanciasReplicadas = new ArrayList<>();
        this.instanciasPendentes = new ArrayList<>();
        this.checklistsApontamentoPendenteDTO = new ArrayList<>();
    }

    public VerificacaoInvalidaDTO(List<Long> instanciasDocumentoInvalidas,
            List<Checklist> checklistsFaseAusentes,
            Map<Integer, Long> checklistsInesperados,
            Map<Checklist, Integer> checklistsFaseReplicados,
            Map<InstanciaDocumento, Integer> instanciasReplicadas,
            Map<InstanciaDocumento, Checklist> instanciasPendentes,
            List<ChecklistPendenteVO> checklistsApontamentoPendente) {
        this();

        if (Objects.nonNull(instanciasDocumentoInvalidas)) {
            this.instanciasDocumentoInvalidas = instanciasDocumentoInvalidas;
        }

        if (Objects.nonNull(checklistsFaseAusentes)) {
            checklistsFaseAusentes.forEach(cfa -> {
                this.checklistsFaseAusentes.add(new ChecklistPendenteDTO(cfa));
            });
        }

        if (Objects.nonNull(checklistsInesperados)) {
            checklistsInesperados.entrySet().forEach(registro -> {
                this.checklistsInesperados.add(new ChecklistInesperadoDTO(registro.getKey(), registro.getValue()));
            });
        }

        if (Objects.nonNull(checklistsFaseReplicados)) {
            checklistsFaseReplicados.entrySet().forEach(registro -> {
                this.checklistsFaseReplicados.add(new ChecklistFaseReplicadoDTO(registro.getKey(), registro.getValue()));
            });
        }

        if (Objects.nonNull(instanciasReplicadas)) {
            instanciasReplicadas.entrySet().forEach(registro -> {
                this.instanciasReplicadas.add(new InstanciaDocumentoReplicadaDTO(registro.getKey(), registro.getValue()));
            });
        }

        if (Objects.nonNull(instanciasPendentes)) {
            instanciasPendentes.entrySet().forEach(registro -> {
                this.instanciasPendentes.add(new InstanciaDocumentoPendenteDTO(registro.getKey(), registro.getValue()));
            });
        }

        if (Objects.nonNull(checklistsApontamentoPendente)) {
            checklistsApontamentoPendente.forEach(registro -> {
                this.checklistsApontamentoPendenteDTO.add(new ChecklistApontamentoPendenteDTO(registro.getChecklist(), registro.getIdentificadorInstanciaDocumento(), registro.getApontamentosAusentes()));
            });
        }

    }

    public List<Long> getInstanciasDocumentoInvalidas() {
        return instanciasDocumentoInvalidas;
    }

    public void setInstanciasDocumentoInvalidas(List<Long> instanciasDocumentoInvalidas) {
        this.instanciasDocumentoInvalidas = instanciasDocumentoInvalidas;
    }

    public List<ChecklistPendenteDTO> getChecklistsFaseAusentes() {
        return checklistsFaseAusentes;
    }

    public void setChecklistsFaseAusentes(List<ChecklistPendenteDTO> checklistsFaseAusentes) {
        this.checklistsFaseAusentes = checklistsFaseAusentes;
    }

    public List<ChecklistInesperadoDTO> getChecklistsInesperados() {
        return checklistsInesperados;
    }

    public void setChecklistsInesperados(List<ChecklistInesperadoDTO> checklistsInesperados) {
        this.checklistsInesperados = checklistsInesperados;
    }

    public List<ChecklistFaseReplicadoDTO> getChecklistsFaseReplicados() {
        return checklistsFaseReplicados;
    }

    public void setChecklistsFaseReplicados(List<ChecklistFaseReplicadoDTO> checklistsFaseReplicados) {
        this.checklistsFaseReplicados = checklistsFaseReplicados;
    }

    public List<InstanciaDocumentoReplicadaDTO> getInstanciasReplicadas() {
        return instanciasReplicadas;
    }

    public void setInstanciasReplicadas(List<InstanciaDocumentoReplicadaDTO> instanciasReplicadas) {
        this.instanciasReplicadas = instanciasReplicadas;
    }

    public List<InstanciaDocumentoPendenteDTO> getInstanciasPendentes() {
        return instanciasPendentes;
    }

    public void setInstanciasPendentes(List<InstanciaDocumentoPendenteDTO> instanciasPendentes) {
        this.instanciasPendentes = instanciasPendentes;
    }

    public List<ChecklistApontamentoPendenteDTO> getChecklistsApontamentoPendenteDTO() {
        return checklistsApontamentoPendenteDTO;
    }

    public void setChecklistsApontamentoPendenteDTO(List<ChecklistApontamentoPendenteDTO> checklistsApontamentoPendenteDTO) {
        this.checklistsApontamentoPendenteDTO = checklistsApontamentoPendenteDTO;
    }

}
