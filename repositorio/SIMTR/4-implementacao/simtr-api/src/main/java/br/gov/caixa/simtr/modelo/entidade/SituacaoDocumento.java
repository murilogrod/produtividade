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
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb015_situacao_documento", indexes = {
    @Index(name = "ix_mtrtb015_01", unique = true, columnList = "no_situacao")})
public class SituacaoDocumento extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private Boolean situacaoInicial;
    private Boolean situacaoFinal;
    private Calendar dataHoraUltimaAlteracao;

    // *******************************************************
    private Set<SituacaoInstanciaDocumento> situacoesInstanciaDocumento;

    public SituacaoDocumento() {
        super();
        this.dataHoraUltimaAlteracao = Calendar.getInstance();
        this.situacoesInstanciaDocumento = new HashSet<>();
    }

    @Id
    @Column(name = "nu_situacao_documento")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "no_situacao", nullable = false, length = 100, unique = true)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "ic_situacao_inicial", nullable = false)
    public Boolean getSituacaoInicial() {
        return situacaoInicial;
    }

    public void setSituacaoInicial(Boolean situacaoInicial) {
        this.situacaoInicial = situacaoInicial;
    }

    @Column(name = "ic_situacao_final", nullable = false)
    public Boolean getSituacaoFinal() {
        return situacaoFinal;
    }

    public void setSituacaoFinal(Boolean situacaoFinal) {
        this.situacaoFinal = situacaoFinal;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_ultima_alteracao")
    public Calendar getDataHoraUltimaAlteracao() {
        return dataHoraUltimaAlteracao;
    }

    public void setDataHoraUltimaAlteracao(Calendar dataHoraUltimaAlteracao) {
        this.dataHoraUltimaAlteracao = dataHoraUltimaAlteracao;
    }

    // *******************************************************
    @OneToMany(targetEntity = SituacaoInstanciaDocumento.class, mappedBy = "situacaoDocumento", fetch = FetchType.LAZY)
    public Set<SituacaoInstanciaDocumento> getSituacoesInstanciaDocumento() {
        return situacoesInstanciaDocumento;
    }

    public void setSituacoesInstanciaDocumento(Set<SituacaoInstanciaDocumento> situacoesInstanciaDocumento) {
        this.situacoesInstanciaDocumento = situacoesInstanciaDocumento;
    }

    // *******************************************************
    public boolean addSituacoesInstanciaDocumento(SituacaoInstanciaDocumento... situacoesInstanciaDocumento) {
        return this.situacoesInstanciaDocumento.addAll(Arrays.asList(situacoesInstanciaDocumento));
    }

    public boolean removeSituacoesInstanciaDocumento(SituacaoInstanciaDocumento... situacoesInstanciaDocumento) {
        return this.situacoesInstanciaDocumento.removeAll(Arrays.asList(situacoesInstanciaDocumento));
    }

    // *******************************************************
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.id);
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
        final SituacaoDocumento other = (SituacaoDocumento) obj;
        return Objects.equals(this.id, other.id);
    }

}
