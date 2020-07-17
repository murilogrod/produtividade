package br.gov.caixa.simtr.modelo.mapeamento.v1.administracao;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesConsultaFalhaBPM;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesConsultaFalhaBPM.XML_ROOT_ELEMENT_FALHA_BPM)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesConsultaFalhaBPM.API_MODEL_V1_FALHA_BPM, description = "Objeto utilizado para representar um falha BPM a ser consultado.")
public class FalhaBPMDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesConsultaFalhaBPM.ID)
    @ApiModelProperty(name = ConstantesConsultaFalhaBPM.ID, value = "Atributo que representa a chave primária da entidade.")
    private Long id;

    @JsonProperty(value = ConstantesConsultaFalhaBPM.UNIDADE_CRIACAO)
    @ApiModelProperty(name = ConstantesConsultaFalhaBPM.UNIDADE_CRIACAO, value = "Atributo que representa o identificador da unidade criação.")
    private Integer unidadeCriacao;

    @JsonProperty(value = ConstantesConsultaFalhaBPM.BPM_INSTANCIA)
    @ApiModelProperty(name = ConstantesConsultaFalhaBPM.BPM_INSTANCIA, value = "Atributo que representa o identificador da Instancia Processo BPM.")
    private Long idInstanciaProcessoBPM;

    @JsonProperty(value = ConstantesConsultaFalhaBPM.BPM_CONTAINER)
    @ApiModelProperty(name = ConstantesConsultaFalhaBPM.BPM_CONTAINER, value = "Atributo que representa BPM Container.")
    private String bpmContainer;

    @JsonProperty(value = ConstantesConsultaFalhaBPM.BPM_PROCESSO)
    @ApiModelProperty(name = ConstantesConsultaFalhaBPM.BPM_PROCESSO, value = "Atributo que representa BPM Processo.")
    private String bpmProcesso;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @JsonProperty(value = ConstantesConsultaFalhaBPM.DATA_HORA_FALHA)
    @ApiModelProperty(name = ConstantesConsultaFalhaBPM.DATA_HORA_FALHA, value = "Atributo que representa a data hora falha.")
    private Calendar dataHoraFalhaBPM;

    @JsonProperty(value = ConstantesConsultaFalhaBPM.PROCESSO)
    @ApiModelProperty(name = ConstantesConsultaFalhaBPM.PROCESSO, value = "Lista de processos relaionados a falha BPM.")
    @JsonInclude(value = Include.NON_NULL)
    private ProcessoOrigemDTO processo;

    public FalhaBPMDTO() {

    }

    public FalhaBPMDTO(DossieProduto dossieProduto) {
	this();
	if (Objects.nonNull(dossieProduto)) {
	    this.id = dossieProduto.getId();
	    this.unidadeCriacao = dossieProduto.getUnidadeCriacao();
	    this.idInstanciaProcessoBPM = dossieProduto.getIdInstanciaProcessoBPM();
	    this.bpmContainer = dossieProduto.getNomeContainerBPM();
	    this.bpmProcesso = dossieProduto.getNomeProcessoBPM();
	    if (Objects.nonNull(dossieProduto.getDataHoraFalhaBPM())) {
		this.dataHoraFalhaBPM = dossieProduto.getDataHoraFalhaBPM();
	    }
	    if (Objects.nonNull(dossieProduto.getProcesso())) {
		this.processo = new ProcessoOrigemDTO(dossieProduto.getProcesso());
	    }
	}
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Integer getUnidadeCriacao() {
	return unidadeCriacao;
    }

    public void setUnidadeCriacao(Integer unidadeCriacao) {
	this.unidadeCriacao = unidadeCriacao;
    }

    public Long getIdInstanciaProcessoBPM() {
	return idInstanciaProcessoBPM;
    }

    public void setIdInstanciaProcessoBPM(Long idInstanciaProcessoBPM) {
	this.idInstanciaProcessoBPM = idInstanciaProcessoBPM;
    }

    public String getBpmContainer() {
	return bpmContainer;
    }

    public void setBpmContainer(String bpmContainer) {
	this.bpmContainer = bpmContainer;
    }

    public String getBpmProcesso() {
	return bpmProcesso;
    }

    public void setBpmProcesso(String bpmProcesso) {
	this.bpmProcesso = bpmProcesso;
    }

    public Calendar getDataHoraFalhaBPM() {
	return dataHoraFalhaBPM;
    }

    public void setDataHoraFalhaBPM(Calendar dataHoraFalhaBPM) {
	this.dataHoraFalhaBPM = dataHoraFalhaBPM;
    }

    public ProcessoOrigemDTO getProcesso() {
	return processo;
    }

    public void setProcesso(ProcessoOrigemDTO processo) {
	this.processo = processo;
    }

}
