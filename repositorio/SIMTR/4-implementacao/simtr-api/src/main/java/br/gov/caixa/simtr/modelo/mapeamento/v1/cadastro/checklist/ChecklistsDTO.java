package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.controle.vo.ChecklistVO;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarSimpleBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesConsultaChecklist;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@XmlRootElement(name = ConstantesConsultaChecklist.XML_ROOT_ELEMENT_CHECKLIST)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesConsultaChecklist.API_MODEL_V1_CHECKLIST,
        description = "Objeto utilizado para representar um checklist cadastrado."
)
public class ChecklistsDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @JsonProperty(value = ConstantesConsultaChecklist.ID)
    @ApiModelProperty(name = ConstantesConsultaChecklist.ID, value = "Atributo que representa a chave primária da entidade.")
    private Integer id;
    
    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @JsonProperty(value = ConstantesConsultaChecklist.DATA_HORA_CRIACAO)
    @ApiModelProperty(name = ConstantesConsultaChecklist.DATA_HORA_CRIACAO, value = "Atributo que representa a data e hora da criação do checklist.")
    private Calendar dataHoraCriacao;
    
    @XmlJavaTypeAdapter(value = CalendarSimpleBRAdapter.class)
    @JsonProperty(value = ConstantesConsultaChecklist.DATA_HORA_INATIVACAO)
    @ApiModelProperty(name = ConstantesConsultaChecklist.DATA_HORA_INATIVACAO, value = "Atributo que representa a data e hora da inativação lógica de um checklist.")
    private Calendar dataHoraInativacao;
    
    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @JsonProperty(value = ConstantesConsultaChecklist.DATA_HORA_ULTIMA_ALTERACAO)
    @ApiModelProperty(name = ConstantesConsultaChecklist.DATA_HORA_ULTIMA_ALTERACAO, value = "Atributo que representa a data e hora da ultima alteração do checklist.")
    private Calendar dataHoraUltimaAlteracao;
    
    @JsonProperty(value = ConstantesConsultaChecklist.NOME)
    @ApiModelProperty(name = ConstantesConsultaChecklist.NOME, value = "Atributo que representa o nome dado ao checklist.")
    private String nome;
    
    @JsonProperty(value = ConstantesConsultaChecklist.UNIDADE_RESPONSAVEL)
    @ApiModelProperty(name = ConstantesConsultaChecklist.UNIDADE_RESPONSAVEL, value = "Atributo que representa qual unidade o checklist pertence.")
    private Integer unidadeResponsavel;
    
    @JsonProperty(value = ConstantesConsultaChecklist.VERIFICACAO_PREVIA)
    @ApiModelProperty(name = ConstantesConsultaChecklist.VERIFICACAO_PREVIA, value = "Atributo que define se o checklist é do tipo prévio.")
    private Boolean verificacaoPrevia;
   
    @JsonProperty(value = ConstantesConsultaChecklist.QUANTIDADE_APONTAMENTOS)
    @ApiModelProperty(name = ConstantesConsultaChecklist.QUANTIDADE_APONTAMENTOS, value = "Atributo que representa a quantidade de apontamentos que um chekclist tem.")
    private Integer quantidadeApontamentos;
    
    @JsonProperty(value = ConstantesConsultaChecklist.QUANTIDADE_ASSOCIACOES)
    @ApiModelProperty(name = ConstantesConsultaChecklist.QUANTIDADE_ASSOCIACOES, value = "Atributo que representa a quantidade de associações que um chekclist tem.")
    private Long quantidadeAssociacoes;
    
    @JsonProperty(value = ConstantesConsultaChecklist.VINCULACOES)
    @ApiModelProperty(name = ConstantesConsultaChecklist.VINCULACOES, value = "Lista de apontamentos relaionados ao checklist.")
    @JsonInclude(value=Include.NON_NULL)
    private List<VinculacaoChecklistDTO> vinculacoes;
    
    public ChecklistsDTO() {
    }
    
    public ChecklistsDTO(ChecklistVO checklistVO) {
        this();
        if (Objects.nonNull(checklistVO)) {
            this.id = checklistVO.getChecklist().getId();
            this.dataHoraCriacao = checklistVO.getChecklist().getDataHoraCriacao();
            if(Objects.nonNull(checklistVO.getChecklist().getDataHoraInativacao())) {
                this.dataHoraInativacao = checklistVO.getChecklist().getDataHoraInativacao();
            }
            this.dataHoraUltimaAlteracao = checklistVO.getChecklist().getDataHoraUltimaAlteracao();
            this.nome = checklistVO.getChecklist().getNome();
            this.unidadeResponsavel = checklistVO.getChecklist().getUnidade();
            this.verificacaoPrevia = checklistVO.getChecklist().getIndicacaoVerificacaoPrevia();
            this.quantidadeApontamentos = checklistVO.getChecklist().getApontamentos().size();
            this.quantidadeAssociacoes = checklistVO.getQuantidadeAssociacoes();
            if(Objects.nonNull(checklistVO.getChecklist().getVinculacoesChecklists()) && !checklistVO.getChecklist().getVinculacoesChecklists().isEmpty()) {
                checklistVO.getChecklist().getVinculacoesChecklists().forEach(vinculacao -> { 
                    VinculacaoChecklistDTO vinculacaoDTO =  new VinculacaoChecklistDTO(vinculacao);
                    this.getVinculacoes().add(vinculacaoDTO);
                });
            }
        }
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Calendar getDataHoraCriacao() {
        return dataHoraCriacao;
    }
    
    public void setDataHoraCriacao(Calendar dataHoraCriacao) {
        this.dataHoraCriacao = dataHoraCriacao;
    }
    
    public Calendar getDataHoraInativacao() {
        return dataHoraInativacao;
    }

    public void setDataHoraInativacao(Calendar dataHoraInativacao) {
        this.dataHoraInativacao = dataHoraInativacao;
    }

    public Calendar getDataHoraUltimaAlteracao() {
        return dataHoraUltimaAlteracao;
    }

    public void setDataHoraUltimaAlteracao(Calendar dataHoraUltimaAlteracao) {
        this.dataHoraUltimaAlteracao = dataHoraUltimaAlteracao;
    }

    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public Integer getUnidadeResponsavel() {
        return unidadeResponsavel;
    }
    
    public void setUnidadeResponsavel(Integer unidadeResponsavel) {
        this.unidadeResponsavel = unidadeResponsavel;
    }
    
    public Boolean getVerificacaoPrevia() {
        return verificacaoPrevia;
    }
    
    public void setVerificacaoPrevia(Boolean verificacaoPrevia) {
        this.verificacaoPrevia = verificacaoPrevia;
    }
    
    public Integer getQuantidadeApontamentos() {
        return quantidadeApontamentos;
    }

    public void setQuantidadeApontamentos(Integer quantidadeApontamentos) {
        this.quantidadeApontamentos = quantidadeApontamentos;
    }

    public Long getQuantidadeAssociacoes() {
        return quantidadeAssociacoes;
    }

    public void setQuantidadeAssociacoes(Long quantidadeAssociacoes) {
        this.quantidadeAssociacoes = quantidadeAssociacoes;
    }

    public List<VinculacaoChecklistDTO> getVinculacoes() {
        if(Objects.isNull(this.vinculacoes)) {
            this.vinculacoes = new ArrayList<>();
        }
        return this.vinculacoes;
    }

    public void setVinculacoes(List<VinculacaoChecklistDTO> vinculacoes) {
        this.vinculacoes = vinculacoes;
    }
}
