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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb033_garantia", indexes = {
    @Index(name = "ix_mtrtb033_01", unique = true, columnList = "nu_garantia_bacen")})
public class Garantia extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer garantiaBacen;
    private String nome;
    private Boolean fidejussoria;

    // ************************************
    private Set<Produto> produtos;
    private Set<DocumentoGarantia> documentosGarantia;
    private Set<GarantiaInformada> garantiasInformadas;
    private Set<CampoFormulario> camposFormulario;

    public Garantia() {
        super();
        this.produtos = new HashSet<>();
        this.documentosGarantia = new HashSet<>();
        this.garantiasInformadas = new HashSet<>();
        this.camposFormulario = new HashSet<>();
    }

    @Id
    @Column(name = "nu_garantia")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "nu_garantia_bacen", nullable = false)
    public Integer getGarantiaBacen() {
        return this.garantiaBacen;
    }

    public void setGarantiaBacen(Integer garantiaBacen) {
        this.garantiaBacen = garantiaBacen;
    }

    @Column(name = "no_garantia", nullable = false, length = 255)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "ic_fidejussoria", nullable = false)
    public Boolean getFidejussoria() {
        return fidejussoria;
    }

    public void setFidejussoria(Boolean fidejussoria) {
        this.fidejussoria = fidejussoria;
    }

    // ************************************
    @ManyToMany(targetEntity = Produto.class, fetch = FetchType.LAZY)
    @JoinTable(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb034_garantia_produto", joinColumns = {
        @JoinColumn(name = "nu_garantia", referencedColumnName = "nu_garantia")}, inverseJoinColumns = {
        @JoinColumn(name = "nu_produto", referencedColumnName = "nu_produto")})
    public Set<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(Set<Produto> produtos) {
        this.produtos = produtos;
    }

    @OneToMany(targetEntity = DocumentoGarantia.class, mappedBy = "garantia", fetch = FetchType.LAZY)
    public Set<DocumentoGarantia> getDocumentosGarantia() {
        return documentosGarantia;
    }

    public void setDocumentosGarantia(Set<DocumentoGarantia> documentosGarantia) {
        this.documentosGarantia = documentosGarantia;
    }

    @OneToMany(targetEntity = GarantiaInformada.class, mappedBy = "garantia", fetch = FetchType.LAZY)
    public Set<GarantiaInformada> getGarantiasInformadas() {
        return garantiasInformadas;
    }

    public void setGarantiasInformadas(Set<GarantiaInformada> garantiasInformadas) {
        this.garantiasInformadas = garantiasInformadas;
    }
    
    @OneToMany(targetEntity = CampoFormulario.class, mappedBy = "garantia", fetch = FetchType.LAZY)
    public Set<CampoFormulario> getCamposFormulario() {
        return camposFormulario;
    }

    public void setCamposFormulario(Set<CampoFormulario> camposFormulario) {
        this.camposFormulario = camposFormulario;
    }


    // ************************************
    public boolean addProdutos(Produto... produtos) {
        return this.produtos.addAll(Arrays.asList(produtos));
    }

    public boolean removeProdutos(Produto... produtos) {
        return this.produtos.removeAll(Arrays.asList(produtos));
    }

    public boolean addDocumentosGarantia(DocumentoGarantia... documentosGarantia) {
        return this.documentosGarantia.addAll(Arrays.asList(documentosGarantia));
    }

    public boolean removeDocumentosGarantia(DocumentoGarantia... documentosGarantia) {
        return this.documentosGarantia.removeAll(Arrays.asList(documentosGarantia));
    }

    public boolean addGarantiasInformadas(GarantiaInformada... garantiasInformadas) {
        return this.garantiasInformadas.addAll(Arrays.asList(garantiasInformadas));
    }

    public boolean removeGarantiasInformadas(GarantiaInformada... garantiasInformadas) {
        return this.garantiasInformadas.removeAll(Arrays.asList(garantiasInformadas));
    }

    public boolean addCamposFormulario(CampoFormulario... camposFormulario) {
        return this.camposFormulario.addAll(Arrays.asList(camposFormulario));
    }

    public boolean removeCamposFormulario(CampoFormulario... camposFormulario) {
        return this.camposFormulario.removeAll(Arrays.asList(camposFormulario));
    }

    // ************************************
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final Garantia other = (Garantia) obj;
        return Objects.equals(this.id, other.id);
    }
}
