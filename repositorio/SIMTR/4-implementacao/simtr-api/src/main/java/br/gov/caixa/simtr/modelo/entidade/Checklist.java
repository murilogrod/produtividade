package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.simtr.util.ConstantesUtil;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb051_checklist", indexes = {
    @Index(name = "ix_mtrtb051_01", unique = true, columnList = "no_checklist,nu_unidade_responsavel")
})
public class Checklist extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private Integer unidade;
    private Boolean indicacaoVerificacaoPrevia;
    private String orientacaoOperador;
    private Calendar dataHoraCriacao;
    private Calendar dataHoraInativacao;
    private Calendar dataHoraUltimaAlteracao;
    
    // *********************************************
    private Set<Apontamento> apontamentos;
    private Set<VinculacaoChecklist> vinculacoesChecklists;
    private Set<ChecklistAssociado> checklistsAssociados;

    public Checklist() {
        super();
        this.apontamentos = new HashSet<>();
        this.vinculacoesChecklists = new HashSet<>();
        this.checklistsAssociados = new HashSet<>();
        this.indicacaoVerificacaoPrevia = Boolean.FALSE;
    }

    @Id
    @Column(name = "nu_checklist")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "no_checklist", length = 150, nullable = false)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "nu_unidade_responsavel", nullable = false)
    public Integer getUnidade() {
        return unidade;
    }

    public void setUnidade(Integer unidade) {
        this.unidade = unidade;
    }

    @Column(name = "ic_verificacao_previa", nullable = false)
    public Boolean getIndicacaoVerificacaoPrevia() {
        return indicacaoVerificacaoPrevia;
    }

    public void setIndicacaoVerificacaoPrevia(Boolean indicacaoVerificacaoPrevia) {
        this.indicacaoVerificacaoPrevia = indicacaoVerificacaoPrevia;
    }

    @Column(name = "de_orientacao_operador", nullable = true, columnDefinition = "text")
    public String getOrientacaoOperador() {
        return orientacaoOperador;
    }

    public void setOrientacaoOperador(String orientacaoOperador) {
        this.orientacaoOperador = orientacaoOperador;
    }

    @Column(name = "ts_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar getDataHoraCriacao() {
        return dataHoraCriacao;
    }

    public void setDataHoraCriacao(Calendar dataHoraCriacao) {
        this.dataHoraCriacao = dataHoraCriacao;
    }
    
    @Column(name = "ts_inativacao", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar getDataHoraInativacao() {
        return dataHoraInativacao;
    }

    public void setDataHoraInativacao(Calendar dataHoraInativacao) {
        this.dataHoraInativacao = dataHoraInativacao;
    }

    @Column(name = "ts_ultima_alteracao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar getDataHoraUltimaAlteracao() {
        return dataHoraUltimaAlteracao;
    }

    public void setDataHoraUltimaAlteracao(Calendar dataHoraUltimaAlteracao) {
        this.dataHoraUltimaAlteracao = dataHoraUltimaAlteracao;
    }

    // ************************************
    @OneToMany(targetEntity = Apontamento.class, mappedBy = "checklist", fetch = FetchType.LAZY, orphanRemoval = true)
    public Set<Apontamento> getApontamentos() {
        return apontamentos;
    }

    public void setApontamentos(Set<Apontamento> apontamentos) {
        this.apontamentos = apontamentos;
    }

    @OneToMany(targetEntity = VinculacaoChecklist.class, mappedBy = "checklist", fetch = FetchType.LAZY)
    public Set<VinculacaoChecklist> getVinculacoesChecklists() {
        return vinculacoesChecklists;
    }

    public void setVinculacoesChecklists(Set<VinculacaoChecklist> vinculacoesChecklists) {
        this.vinculacoesChecklists = vinculacoesChecklists;
    }

    @OneToMany(targetEntity = ChecklistAssociado.class, mappedBy = "checklist", fetch = FetchType.LAZY)
    public Set<ChecklistAssociado> getChecklistsAssociados() {
        return checklistsAssociados;
    }

    public void setChecklistsAssociados(Set<ChecklistAssociado> checklistsAssociados) {
        this.checklistsAssociados = checklistsAssociados;
    }

    // ************************************
    public boolean addApontamentos(Apontamento... apontamentos) {
        return this.apontamentos.addAll(Arrays.asList(apontamentos));
    }

    public boolean removeApontamentos(Apontamento... apontamentos) {
        return this.apontamentos.removeAll(Arrays.asList(apontamentos));
    }

    public boolean addVinculacoesChecklists(VinculacaoChecklist... vinculacoesChecklists) {
        return this.vinculacoesChecklists.addAll(Arrays.asList(vinculacoesChecklists));
    }

    public boolean removeVinculacoesChecklists(VinculacaoChecklist... vinculacoesChecklists) {
        return this.vinculacoesChecklists.removeAll(Arrays.asList(vinculacoesChecklists));
    }

    public boolean addChecklistsAssociados(ChecklistAssociado... checklistsAssociados) {
        return this.checklistsAssociados.addAll(Arrays.asList(checklistsAssociados));
    }

    public boolean removeChecklistsAssociados(ChecklistAssociado... checklistsAssociados) {
        return this.checklistsAssociados.removeAll(Arrays.asList(checklistsAssociados));
    }

    // ************************************
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.nome);
        hash = 67 * hash + Objects.hashCode(this.unidade);
        hash = 67 * hash + Objects.hashCode(this.indicacaoVerificacaoPrevia);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Checklist other = (Checklist) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.unidade, other.unidade)) {
            return false;
        }
        return Objects.equals(this.indicacaoVerificacaoPrevia, other.indicacaoVerificacaoPrevia);
    }
}
