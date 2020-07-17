package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.controle.vo.ChecklistVO;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarSimpleBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesConsultaChecklist;
import io.swagger.annotations.ApiModelProperty;

public class ChecklistDTO implements Serializable{
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
    
    @JsonProperty(value = ConstantesConsultaChecklist.ORIENTACAO_OPERADOR)
    @ApiModelProperty(name = ConstantesConsultaChecklist.ORIENTACAO_OPERADOR, value = "Atributo que define uma mensagem de orientação ao usuário.")
    @JsonInclude(value=Include.NON_NULL)
    private String orientacaoOperador;
    
    @JsonProperty(value = ConstantesConsultaChecklist.QUANTIDADE_ASSOCIACOES)
    @ApiModelProperty(name = ConstantesConsultaChecklist.QUANTIDADE_ASSOCIACOES, value = "Atributo que representa a quantidade de associações que um chekclist tem.")
    private Long quantidadeAssociacoes;
    
    @JsonProperty(value = ConstantesConsultaChecklist.APONTAMENTOS)
    @ApiModelProperty(name = ConstantesConsultaChecklist.APONTAMENTOS, value = "Lista de apontamentos relaionados ao checklist.")
    @JsonInclude(value=Include.NON_NULL)
    private List<ApontamentoDTO> apontamentos;
    
    @JsonProperty(value = ConstantesConsultaChecklist.VINCULACOES)
    @ApiModelProperty(name = ConstantesConsultaChecklist.VINCULACOES, value = "Lista de apontamentos relaionados ao checklist.")
    @JsonInclude(value=Include.NON_NULL)
    private List<VinculacaoChecklistDTO> vinculacoes;
    
    public ChecklistDTO(ChecklistVO checklistVO) {
        if(Objects.nonNull(checklistVO)) {
            this.id = checklistVO.getChecklist().getId();
            this.dataHoraCriacao = checklistVO.getChecklist().getDataHoraCriacao();
            if(Objects.nonNull(checklistVO.getChecklist().getDataHoraInativacao())) {
                this.dataHoraInativacao = checklistVO.getChecklist().getDataHoraInativacao();
            }
            this.dataHoraUltimaAlteracao = checklistVO.getChecklist().getDataHoraUltimaAlteracao();
            this.nome = checklistVO.getChecklist().getNome();
            this.unidadeResponsavel = checklistVO.getChecklist().getUnidade();
            this.verificacaoPrevia = checklistVO.getChecklist().getIndicacaoVerificacaoPrevia();
            this.orientacaoOperador = checklistVO.getChecklist().getOrientacaoOperador();
            this.quantidadeAssociacoes = checklistVO.getQuantidadeAssociacoes();
            if(Objects.nonNull(checklistVO.getChecklist().getApontamentos()) && !checklistVO.getChecklist().getApontamentos().isEmpty()) {
                checklistVO.getChecklist().getApontamentos().forEach(apontamento -> {
                    ApontamentoDTO apontamentoDTO = new ApontamentoDTO(apontamento);
                    this.getApontamentos().add(apontamentoDTO);
                });
            }
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

    public String getOrientacaoOperador() {
        return orientacaoOperador;
    }

    public void setOrientacaoOperador(String orientacaoOperador) {
        this.orientacaoOperador = orientacaoOperador;
    }
    
    public Long getQuantidadeAssociacoes() {
        return quantidadeAssociacoes;
    }

    public void setQuantidadeAssociacoes(Long quantidadeAssociacoes) {
        this.quantidadeAssociacoes = quantidadeAssociacoes;
    }

    public List<ApontamentoDTO> getApontamentos() {
        if(Objects.isNull(this.apontamentos)) {
            this.apontamentos = new ArrayList<>();
        }
        return apontamentos;
    }

    public void setApontamentos(List<ApontamentoDTO> apontamentos) {
        this.apontamentos = apontamentos;
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
