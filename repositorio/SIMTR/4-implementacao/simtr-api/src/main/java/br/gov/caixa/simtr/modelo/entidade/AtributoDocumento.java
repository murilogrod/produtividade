package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb007_atributo_documento")
public class AtributoDocumento extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Documento documento;
    private String deObjeto;
    private String descricao;
    private String conteudo;
    private BigDecimal indiceAssertividade;
    private Boolean acertoManual;
    // *********************************************
    private Set<OpcaoSelecionada> opcoesSelecionadas;

    public AtributoDocumento() {
        super();
        this.opcoesSelecionadas = new HashSet<>();
    }

    @Id
    @Column(name = "nu_atributo_documento")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Documento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_documento", nullable = false)
    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    @Column(name = "de_atributo", nullable = false, length = 100)
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Column(name = "de_conteudo", nullable = true, columnDefinition = "text")
    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    @OneToMany(targetEntity = OpcaoSelecionada.class, mappedBy = "atributoDocumento", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<OpcaoSelecionada> getOpcoesSelecionadas() {
        return opcoesSelecionadas;
    }

    public void setOpcoesSelecionadas(Set<OpcaoSelecionada> opcoesSelecionadas) {
        this.opcoesSelecionadas = opcoesSelecionadas;
    }

    @Column(name = "ix_assertividade", nullable = true, scale = 5, precision = 2)
    public BigDecimal getIndiceAssertividade() {
        return indiceAssertividade;
    }

    public void setIndiceAssertividade(BigDecimal indiceAssertividade) {
        this.indiceAssertividade = indiceAssertividade;
    }

    @Column(name = "ic_acerto_manual", nullable = false)
    public Boolean getAcertoManual() {
        return this.acertoManual;
    }

    public void setAcertoManual(Boolean acertoManual) {
        this.acertoManual = acertoManual;
    }

    @Column(name = "de_objeto", nullable = false)
    public String getDeObjeto() {
		return deObjeto;
	}

	public void setDeObjeto(String deObjeto) {
		this.deObjeto = deObjeto;
	}
  

    // *************************************
    public boolean addOpcaoSelecionada(OpcaoSelecionada... opcoesSelecionadas) {
        return this.opcoesSelecionadas.addAll(Arrays.asList(opcoesSelecionadas));
    }

	public boolean removeOpcaoSelecionada(OpcaoSelecionada... opcoesSelecionadas) {
        return this.opcoesSelecionadas.removeAll(Arrays.asList(opcoesSelecionadas));
    }

    // *********************************************
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.id);
        hash = 71 * hash + Objects.hashCode(this.documento);
        hash = 71 * hash + Objects.hashCode(this.descricao);
        hash = 71 * hash + Objects.hashCode(this.conteudo);
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
        final AtributoDocumento other = (AtributoDocumento) obj;
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.conteudo, other.conteudo)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.documento, other.documento);
    }

}
