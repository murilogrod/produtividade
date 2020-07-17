package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb035_garantia_informada")
public class GarantiaInformada extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private DossieProduto dossieProduto;
    private Garantia garantia;
    private Produto produto;
    private BigDecimal valorGarantia;

    // ************************************
    private Set<InstanciaDocumento> instanciasDocumento;
    private Set<DossieCliente> dossiesCliente;
    private Set<RespostaDossie> respostasDossies;

    public GarantiaInformada() {
        super();
        this.instanciasDocumento = new HashSet<>();
        this.dossiesCliente = new HashSet<>();
        this.respostasDossies = new HashSet<>();
    }

    @Id
    @Column(name = "nu_garantia_informada")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = DossieProduto.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_dossie_produto", nullable = false)
    public DossieProduto getDossieProduto() {
        return dossieProduto;
    }

    public void setDossieProduto(DossieProduto dossieProduto) {
        this.dossieProduto = dossieProduto;
    }

    @ManyToOne(targetEntity = Garantia.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_garantia", nullable = false)
    public Garantia getGarantia() {
        return garantia;
    }

    public void setGarantia(Garantia garantia) {
        this.garantia = garantia;
    }

    @ManyToOne(targetEntity = Produto.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_produto", nullable = true)
    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Column(name = "vr_garantia_informada", scale = 15, precision = 2, nullable = false)
    public BigDecimal getValorGarantia() {
        return valorGarantia;
    }

    public void setValorGarantia(BigDecimal valorGarantia) {
        this.valorGarantia = valorGarantia;
    }

    // ************************************
    @OneToMany(targetEntity = InstanciaDocumento.class, mappedBy = "garantiaInformada", fetch = FetchType.LAZY)
    public Set<InstanciaDocumento> getInstanciasDocumento() {
        return instanciasDocumento;
    }

    public void setInstanciasDocumento(Set<InstanciaDocumento> instanciasDocumento) {
        this.instanciasDocumento = instanciasDocumento;
    }

    @ManyToMany(targetEntity = DossieCliente.class, fetch = FetchType.LAZY)
    @JoinTable(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb042_cliente_garantia", joinColumns = {
        @JoinColumn(name = "nu_garantia_informada", referencedColumnName = "nu_garantia_informada")}, inverseJoinColumns = {
        @JoinColumn(name = "nu_dossie_cliente", referencedColumnName = "nu_dossie_cliente")})
    public Set<DossieCliente> getDossiesCliente() {
        return dossiesCliente;
    }

    public void setDossiesCliente(Set<DossieCliente> dossiesCliente) {
        this.dossiesCliente = dossiesCliente;
    }
    
    @OneToMany(targetEntity = RespostaDossie.class, mappedBy = "garantiaInformada", fetch = FetchType.LAZY)
    public Set<RespostaDossie> getRespostasDossies() {
        return respostasDossies;
    }

    public void setRespostasDossies(Set<RespostaDossie> respostasDossies) {
        this.respostasDossies = respostasDossies;
    }

    // ************************************
    public boolean addInstanciasDocumento(InstanciaDocumento... instanciasDocumento) {
        return this.instanciasDocumento.addAll(Arrays.asList(instanciasDocumento));
    }

    public boolean removeInstanciasDocumento(InstanciaDocumento... instanciasDocumentos) {
        return this.instanciasDocumento.removeAll(Arrays.asList(instanciasDocumentos));
    }

    public boolean addDossiesCliente(DossieCliente... dossiesCliente) {
        return this.dossiesCliente.addAll(Arrays.asList(dossiesCliente));
    }

    public boolean removeDossiesCliente(DossieCliente... dossiesCliente) {
        return this.dossiesCliente.removeAll(Arrays.asList(dossiesCliente));
    }
    
    public boolean addRespostaDossie(RespostaDossie... respostasDossies) {
        return this.respostasDossies.addAll(Arrays.asList(respostasDossies));
    }

    public boolean removeRespostaDossie(RespostaDossie... respostasDossies) {
        return this.respostasDossies.removeAll(Arrays.asList(respostasDossies));
    }

    // ************************************
    @Override
    public int hashCode() {
        int hash = 5;
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
        final GarantiaInformada other = (GarantiaInformada) obj;
        return Objects.equals(this.id, other.id);
    }
}
