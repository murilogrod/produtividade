package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.caixa.simtr.util.ConstantesUtil;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb052_apontamento", indexes = {
    @Index(name = "ix_mtrtb052_01", unique = true, columnList = "nu_checklist,no_apontamento")
})
public class Apontamento extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Checklist checklist;
    private String nome;
    private String descricao;
    private String orientacao;
    private Boolean indicativoInformacao;
    private Boolean indicativoRejeicao;
    private Boolean indicativoSeguranca;
    private Integer ordem;
    private String teclaAtalho;
    //*********************************************
    private Set<Parecer> pareceres;

    // *********************************************
    public Apontamento() {
        super();
        this.pareceres = new HashSet<>();
    }

    @Id
    @Column(name = "nu_apontamento")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Checklist.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "nu_checklist", nullable = false)
    public Checklist getChecklist() {
        return checklist;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    @Column(name = "no_apontamento", length = 500, nullable = false)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "de_apontamento", nullable = true, columnDefinition = "text")
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Column(name = "de_orientacao", nullable = false, columnDefinition = "text")
    public String getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(String orientacao) {
        this.orientacao = orientacao;
    }

    @Column(name = "ic_informacao", nullable = false)
    public Boolean getIndicativoInformacao() {
        return indicativoInformacao;
    }

    public void setIndicativoInformacao(Boolean indicativoInformacao) {
        this.indicativoInformacao = indicativoInformacao;
    }

    @Column(name = "ic_rejeicao", nullable = false)
    public Boolean getIndicativoRejeicao() {
        return indicativoRejeicao;
    }

    public void setIndicativoRejeicao(Boolean indicativoRejeicao) {
        this.indicativoRejeicao = indicativoRejeicao;
    }

    @Column(name = "ic_seguranca", nullable = false)
    public Boolean getIndicativoSeguranca() {
        return indicativoSeguranca;
    }

    public void setIndicativoSeguranca(Boolean indicativoSeguranca) {
        this.indicativoSeguranca = indicativoSeguranca;
    }

    @Column(name = "nu_ordem", nullable = false)
    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    @Column(name = "co_tecla_atalho", nullable = true, length = 30)
    public String getTeclaAtalho() {
        return teclaAtalho;
    }

    public void setTeclaAtalho(String teclaAtalho) {
        this.teclaAtalho = teclaAtalho;
    }

    // ***********************************
    @OneToMany(targetEntity = Parecer.class, mappedBy = "apontamento", fetch = FetchType.LAZY)
    public Set<Parecer> getPareceres() {
        return pareceres;
    }

    public void setPareceres(Set<Parecer> pareceres) {
        this.pareceres = pareceres;
    }

    // ************************************
    public boolean addPareceres(Parecer... pareceres) {
        return this.pareceres.addAll(Arrays.asList(pareceres));
    }

    public boolean removePareceres(Parecer... pareceres) {
        return this.pareceres.removeAll(Arrays.asList(pareceres));
    }

    // ************************************
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.id);
        hash = 71 * hash + Objects.hashCode(this.checklist);
        hash = 71 * hash + Objects.hashCode(this.nome);
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
        final Apontamento other = (Apontamento) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.checklist, other.checklist);
    }

}
