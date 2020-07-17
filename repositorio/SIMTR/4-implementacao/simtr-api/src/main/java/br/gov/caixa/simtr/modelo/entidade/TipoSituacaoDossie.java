package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.caixa.simtr.util.ConstantesUtil;
import java.util.Calendar;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb012_tipo_situacao_dossie", indexes = {
    @Index(name = "ix_mtrtb012_01", unique = true, columnList = "no_tipo_situacao")
})
public class TipoSituacaoDossie extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private Boolean tipoInicial;
    private Boolean tipoFinal;
    private Calendar dataHoraUltimaAlteracao;
    // ************************************************
    private Set<SituacaoDossie> situacoesDossie;

    public TipoSituacaoDossie() {
        super();
        this.dataHoraUltimaAlteracao = Calendar.getInstance();
        this.situacoesDossie = new HashSet<>();
    }

    @Id
    @Column(name = "nu_tipo_situacao_dossie")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "no_tipo_situacao", nullable = false, length = 100)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "ic_tipo_inicial", nullable = false)
    public Boolean getTipoInicial() {
        return tipoInicial;
    }

    public void setTipoInicial(Boolean tipoInicial) {
        this.tipoInicial = tipoInicial;
    }

    @Column(name = "ic_tipo_final", nullable = false)
    public Boolean getTipoFinal() {
        return tipoFinal;
    }

    public void setTipoFinal(Boolean tipoFinal) {
        this.tipoFinal = tipoFinal;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_ultima_alteracao")
    public Calendar getDataHoraUltimaAlteracao() {
        return dataHoraUltimaAlteracao;
    }

    public void setDataHoraUltimaAlteracao(Calendar dataHoraUltimaAlteracao) {
        this.dataHoraUltimaAlteracao = dataHoraUltimaAlteracao;
    }

    // ************************************************
    @OneToMany(targetEntity = SituacaoDossie.class, mappedBy = "tipoSituacaoDossie", fetch = FetchType.LAZY)
    public Set<SituacaoDossie> getSituacoesDossie() {
        return situacoesDossie;
    }

    public void setSituacoesDossie(Set<SituacaoDossie> situacoesDossie) {
        this.situacoesDossie = situacoesDossie;
    }

    // ************************************************
    public boolean addSituacoesDossie(SituacaoDossie... situacoesDossie) {
        return this.situacoesDossie.addAll(Arrays.asList(situacoesDossie));
    }

    public boolean removeSituacoesDossie(SituacaoDossie... situacoesDossie) {
        return this.situacoesDossie.removeAll(Arrays.asList(situacoesDossie));
    }

    // ************************************************
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.id);
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
        final TipoSituacaoDossie other = (TipoSituacaoDossie) obj;
        return Objects.equals(this.id, other.id);
    }
}
